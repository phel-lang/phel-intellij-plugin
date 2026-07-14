package org.phellang.language.psi.references

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec

/**
 * Resolves a symbol to a binding introduced in an enclosing scope: a `let`-like binding vector, or a
 * function's parameter vector.
 */
internal object PhelLocalScopeResolver {

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

    /** Every parameter named [symbolName] anywhere in [file] — polyvariant resolve shows them all. */
    fun findAllParametersIn(file: PsiElement, symbolName: String): List<PsiElement> {
        return PsiTreeUtil.findChildrenOfType(file, PhelList::class.java)
            .flatMap { list -> parametersOf(list).orEmpty() }
            .filter { symbolName == it.text }
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

        // Bindings are pairs: [name1 value1 name2 value2 …] — names sit at even indices.
        val bindings = bindingsVec.forms
        return (bindings.indices step 2)
            .mapNotNull { PsiTreeUtil.findChildOfType(bindings[it], PhelSymbol::class.java) }
            .firstOrNull { symbolName == it.text }
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

    /** The parameter symbols a function-defining form declares, or null when it declares none. */
    private fun parametersOf(list: PhelList): List<PhelSymbol>? {
        val forms = list.forms
        if (forms.size < 2) return null

        val keyword = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)?.text ?: return null
        val paramVec = PhelDefinitionFinder.findParameterVector(forms, keyword) ?: return null

        return paramVec.forms.mapNotNull { PsiTreeUtil.findChildOfType(it, PhelSymbol::class.java) }
    }
}
