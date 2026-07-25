package org.phellang.editor.breadcrumbs

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import org.phellang.language.infrastructure.PhelLanguage
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * The trail of enclosing forms shown above the editor.
 *
 * Lisp is where breadcrumbs earn their keep: a few levels of nesting and the `defn` that opens the
 * form has scrolled off the top, leaving nothing on screen to say where the caret is.
 *
 * Only forms that establish context are shown. A crumb per list would mirror the parenthesis depth,
 * which the editor already draws, and push the useful outer names off the left edge.
 */
class PhelBreadcrumbsProvider : BreadcrumbsProvider {

    override fun getLanguages(): Array<Language> = arrayOf(PhelLanguage)

    override fun acceptElement(element: PsiElement): Boolean = crumbFor(element) != null

    override fun getElementInfo(element: PsiElement): String = crumbFor(element).orEmpty()

    private fun crumbFor(element: PsiElement): String? {
        val list = element as? PhelList ?: return null
        val forms = PhelPsiUtils.activeForms(list)
        val head = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text ?: return null

        if (head !in CONTEXT_FORMS) return null

        // `(defn greet [name] …)` reads better as "defn greet" than as either half alone.
        val name = forms.getOrNull(1)?.let { PhelPsiUtils.asSymbol(it) }?.text
        return if (head in PhelSpecialForms.NAME_DECLARING && name != null) "$head $name" else head
    }

    private companion object {
        /**
         * Forms worth a crumb: the ones that name something, plus the binding and control forms deep
         * enough nesting hides. Kept narrow on purpose — see the class comment.
         */
        val CONTEXT_FORMS: Set<String> = PhelSpecialForms.NAME_DECLARING + setOf(
            "let", "loop", "fn", "binding", "for", "foreach", "dofor", "doseq",
            "when", "when-not", "when-let", "when-some", "if-let", "if-some",
            "try", "catch", "finally", "case", "cond", "condp", "testing",
        )
    }
}
