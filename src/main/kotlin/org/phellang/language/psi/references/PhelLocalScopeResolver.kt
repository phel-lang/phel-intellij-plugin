package org.phellang.language.psi.references

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.analysis.PhelDestructuringAnalyzer
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.utils.cachedPerPsi

/**
 * Resolves a symbol to a binding introduced in an enclosing scope: a `let`-like binding vector, or a
 * function's parameter vector.
 */
internal object PhelLocalScopeResolver {

    private val PARAMETERS_BY_NAME_KEY: Key<CachedValue<Map<String, List<PsiElement>>>> =
        Key.create("phel.parametersByName")

    /** The nearest enclosing binding or parameter that declares [symbolName], or null. */
    fun resolve(symbol: PhelSymbol, symbolName: String): PsiElement? {
        var current: PsiElement? = symbol

        while (current != null) {
            PsiTreeUtil.getParentOfType(current, PhelList::class.java)
                ?.let { findInLetBindings(it, symbolName) }
                ?.let { return it }

            findInFunctionParameters(current, symbolName)?.let { return it }

            current = current.parent
        }

        return null
    }

    /**
     * Every parameter named [symbolName] anywhere in [file], in document order: polyvariant resolve
     * shows them all.
     *
     * A whole file is answered from [parametersByName], built once per file version: every unqualified
     * resolve asks this of its own file, and walking every list per symbol was most of what a resolve
     * still cost once the definitions were cached.
     */
    fun findAllParametersIn(file: PsiElement, symbolName: String): List<PsiElement> {
        if (file is PsiFile) return parametersByName(file)[symbolName].orEmpty()

        return PsiTreeUtil.findChildrenOfType(file, PhelList::class.java)
            .flatMap { list -> parametersOf(list).orEmpty() }
            .filter { symbolName == it.text }
    }

    private fun parametersByName(file: PsiFile): Map<String, List<PsiElement>> =
        cachedPerPsi(file, PARAMETERS_BY_NAME_KEY) {
            PsiTreeUtil.findChildrenOfType(file, PhelList::class.java)
                .flatMap { list -> parametersOf(list).orEmpty() }
                .groupBy { it.text }
        }

    /**
     * The binding named [symbolName] in [letForm]'s binding vector.
     *
     * Only forms whose second element is a `[name value …]` vector are relevant. `catch` was once
     * listed here but its second element is the exception class, so it never matched; `when-let` was
     * missing, so its bindings failed to resolve. The shared [PhelSpecialForms.LET_LIKE] set fixes both.
     */
    private fun findInLetBindings(letForm: PhelList, symbolName: String): PsiElement? {
        val forms = letForm.forms
        if (forms.size < 2) return null

        val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java) ?: return null
        if (firstSymbol.text !in PhelSpecialForms.LET_LIKE) return null

        val bindingsVec = forms[1] as? PhelVec
            ?: PsiTreeUtil.findChildOfType(forms[1], PhelVec::class.java)
            ?: return null

        // Bindings are pairs: [pattern1 value1 pattern2 value2 …] — patterns sit at even indices,
        // and each may destructure, so every name a pattern introduces is a candidate. Reading the
        // first symbol found inside the entry used to resolve only `a` of `[a b]`.
        return PhelDestructuringAnalyzer.letBoundSymbols(bindingsVec).firstOrNull { symbolName == it.text }
    }

    /** Walks every enclosing function form, not just the innermost, so shadowed params still resolve. */
    private fun findInFunctionParameters(context: PsiElement, symbolName: String): PsiElement? {
        var current: PsiElement? = context

        while (current != null) {
            val fnForm = PsiTreeUtil.getParentOfType(current, PhelList::class.java) ?: return null

            parametersOf(fnForm)
                ?.firstOrNull { symbolName == it.text }
                ?.let { return it }

            current = fnForm.parent
        }

        return null
    }

    /** The parameter symbols a function-defining form declares, destructured ones included, or null when it declares none. */
    private fun parametersOf(list: PhelList): List<PhelSymbol>? {
        val paramVec = PhelSymbolAnalyzer.findParameterVector(list) ?: return null

        return PhelDestructuringAnalyzer.parameterSymbols(paramVec)
    }
}
