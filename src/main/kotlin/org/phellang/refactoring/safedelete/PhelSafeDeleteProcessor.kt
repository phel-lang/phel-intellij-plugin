package org.phellang.refactoring.safedelete

import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.refactoring.safeDelete.NonCodeUsageSearchInfo
import com.intellij.refactoring.safeDelete.SafeDeleteProcessorDelegate
import com.intellij.refactoring.safeDelete.usageInfo.SafeDeleteReferenceSimpleDeleteUsageInfo
import com.intellij.usageView.UsageInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition

/**
 * Safe Delete for a Phel name: finds what still refers to it, and removes the whole form.
 *
 * The usage search is `ReferencesSearch`, which reaches `PhelReference` — so what counts as a usage
 * here is exactly what Find Usages and the unused-definition inspections already agree on, and it is
 * cheap now that resolution is cached.
 *
 * What gets deleted is the enclosing *definition form*, not the name alone. Deleting the name of
 * `(defn greet [name] …)` and stopping there would leave `(defn  [name] …)`, which is not what
 * anyone means by deleting a function. [PhelSafeDeleteTarget] works out that form.
 */
class PhelSafeDeleteProcessor : SafeDeleteProcessorDelegate {

    /**
     * Exactly what [PhelSafeDeleteTarget] can delete, so the action is never offered for a name it
     * would then only half-remove.
     */
    override fun handlesElement(element: PsiElement): Boolean = PhelSafeDeleteTarget.enclosingFormOf(element) != null

    /**
     * A usage that sits inside something already being deleted is not a reason to stop.
     *
     * The whole definition form goes, so its own body refers to a name that is on its way out with
     * it — a recursive `defn` is the obvious case, and without this it would always report itself as
     * a blocking usage.
     */
    override fun findUsages(
        element: PsiElement,
        allElementsToDelete: Array<out PsiElement>,
        result: MutableList<in UsageInfo>,
    ): NonCodeUsageSearchInfo {
        val insideDeleted = Condition<PsiElement> { candidate ->
            allElementsToDelete.any { it.isValid && it.textRange.contains(candidate.textRange) }
        }

        ReferencesSearch.search(element).forEach { reference ->
            val usage = reference.element
            result.add(SafeDeleteReferenceSimpleDeleteUsageInfo(usage, element, insideDeleted.value(usage)))
        }

        return NonCodeUsageSearchInfo(insideDeleted, element)
    }

    override fun getElementsToSearch(
        element: PsiElement,
        allElementsToDelete: MutableCollection<out PsiElement>,
    ): MutableCollection<out PsiElement> = mutableListOf(element)

    /**
     * The rest of the form goes with the name.
     *
     * Returned as *additional* elements rather than deleted here: the platform owns the write action
     * and the undo entry, and doing it early would invalidate the name before it had been searched.
     */
    override fun getAdditionalElementsToDelete(
        element: PsiElement,
        allElementsToDelete: MutableCollection<out PsiElement>,
        askUser: Boolean,
    ): MutableCollection<PsiElement>? {
        val form = PhelSafeDeleteTarget.enclosingFormOf(element) ?: return null

        return mutableListOf(form)
    }

    /**
     * No conflicts of its own: a name that is still referenced is reported through [findUsages], and
     * a Phel definition has no members or overrides that could conflict separately.
     *
     * Implemented explicitly even though newer platforms give it a default. It is still *abstract* on
     * 2024.3, the oldest release this plugin supports, so omitting it compiles against 2025.2 and
     * throws `AbstractMethodError` there — which is what the plugin verifier caught.
     */
    override fun findConflicts(
        element: PsiElement,
        allElementsToDelete: Array<out PsiElement>,
    ): MutableCollection<String>? = null

    override fun preprocessUsages(project: Project, usages: Array<out UsageInfo>): Array<UsageInfo> =
        @Suppress("UNCHECKED_CAST") (usages as Array<UsageInfo>)

    override fun prepareForDeletion(element: PsiElement) = Unit

    override fun isToSearchInComments(element: PsiElement?): Boolean = false

    override fun setToSearchInComments(element: PsiElement?, enabled: Boolean) = Unit

    override fun isToSearchForTextOccurrences(element: PsiElement?): Boolean = false

    override fun setToSearchForTextOccurrences(element: PsiElement?, enabled: Boolean) = Unit
}
