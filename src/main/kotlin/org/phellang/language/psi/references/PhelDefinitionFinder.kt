package org.phellang.language.psi.references

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVendorUtils
import org.phellang.language.psi.files.PhelFile

/**
 * Matches a name against the definitions a Phel file declares — `(def x …)`, `(defn f [..] …)` and
 * friends.
 *
 * Shared by every resolver: scanning a file's lists for a matching definition is the one operation
 * they all perform. Locating the parameter vector those forms introduce is a different question,
 * answered by `PhelSymbolAnalyzer.findParameterVector`.
 */
internal object PhelDefinitionFinder {

    /** The definitions of [symbolName] declared anywhere in [root]. */
    fun collectDefinitionsIn(root: PsiElement, symbolName: String): List<PsiElement> {
        return PsiTreeUtil.findChildrenOfType(root, PhelList::class.java)
            .mapNotNull { findDefinitionInList(it, symbolName) }
    }

    /** The definitions of [symbolName] across every vendor file backing [namespace]. */
    fun collectVendorDefinitions(project: Project, namespace: String, symbolName: String): List<PsiElement> {
        // Phel 0.35+: a namespace may be backed by several vendor files
        // (e.g. phel.core spans core.phel + core/*.phel).
        val vendorFiles = PhelVendorUtils.findStandardLibraryFiles(project, namespace)
        if (vendorFiles.isEmpty()) return emptyList()

        val psiManager = PsiManager.getInstance(project)
        return vendorFiles
            .mapNotNull { psiManager.findFile(it) as? PhelFile }
            .flatMap { collectDefinitionsIn(it, symbolName) }
    }

    /** The name symbol of `(def name …)` / `(defn name …)` when it is [symbolName], else null. */
    fun findDefinitionInList(list: PhelList, symbolName: String): PsiElement? {
        val forms = list.forms
        if (forms.size < 2) return null

        val defKeyword = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java) ?: return null
        if (!isDefiningKeyword(defKeyword.text)) return null

        val definedName = PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java) ?: return null
        return definedName.takeIf { symbolName == it.text }
    }

    /**
     * Whether [keyword] heads a form that declares a name in its second position.
     *
     * This used to be a short hand-written list plus a fallback onto the SPECIAL_FORMS and MACROS
     * completion priorities. Those are *ranking* buckets: they hold `do`, `try`, `throw`, `recur`,
     * `when`, `cond`, `and`, `or` and `->`, so `(do foo)` registered `foo` as a definition of
     * itself and go-to-definition landed on whatever usage happened to sit inside such a form.
     *
     * The fallback did carry its weight — `defexception`, `declare`, `defrecord` and friends are
     * MACROS and reached resolution only through it, while the hand-written list omitted them.
     * [PhelSpecialForms.NAME_DECLARING] covers every one of those by name, and `defonce` besides,
     * which the registry classifies CORE_FUNCTIONS and so neither route reached.
     */
    fun isDefiningKeyword(keyword: String?): Boolean = keyword in PhelSpecialForms.NAME_DECLARING
}
