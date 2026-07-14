package org.phellang.language.psi.references

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.PhelVendorUtils
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.SymbolCategory

/**
 * Matches a name against the definitions a Phel file declares — `(def x …)`, `(defn f [..] …)` and
 * friends — plus the parameter vectors those forms introduce.
 *
 * Shared by every resolver: scanning a file's lists for a matching definition is the one operation
 * they all perform, and it was previously written out four separate times.
 */
internal object PhelDefinitionFinder {

    /** Keywords that define new symbols. */
    private val DEFINING_KEYWORDS = setOf(
        "def", "defn", "defn-", "defmacro", "defmacro-",
        "defstruct", "definterface", "def-"
    )

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

    fun isDefiningKeyword(keyword: String?): Boolean {
        if (keyword == null) return false

        return keyword in DEFINING_KEYWORDS ||
                PhelSymbolAnalyzer.isSymbolType(keyword, SymbolCategory.SPECIAL_FORMS) ||
                PhelSymbolAnalyzer.isSymbolType(keyword, SymbolCategory.MACROS)
    }

    /**
     * The parameter vector a function-defining form introduces, or null when [keyword] doesn't
     * introduce one. `fn` carries it at index 1; `defn` and friends carry it after the name, past
     * any docstring or metadata — `(defn name "doc" {:meta} [params] …)`.
     */
    fun findParameterVector(forms: List<PhelForm>, keyword: String): PhelVec? = when (keyword) {
        "fn" -> forms.getOrNull(1)?.let(::asVector)
        "defn", "defn-", "defmacro", "defmacro-" ->
            forms.drop(2).firstNotNullOfOrNull(::asVector)

        else -> null
    }

    private fun asVector(form: PhelForm): PhelVec? =
        form as? PhelVec ?: PsiTreeUtil.findChildOfType(form, PhelVec::class.java)
}
