package org.phellang.inspection.analysis

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/** Finds bindings that reuse a name already bound by an enclosing form. */
internal object PhelShadowedBindingFinder {

    /** The binding targets of [list] that shadow an outer binding, in source order. */
    fun shadowedBindings(list: PhelList): List<PhelSymbol> {
        // activeForms, not forms: a `#_`-discarded form must not shift the head/binding-vector reads
        // or the name/value pairing inside the binding vector.
        val forms = PhelPsiUtils.activeForms(list)
        if (forms.size < 2) return emptyList()

        val head = PhelPsiUtils.asSymbol(forms[0])?.text ?: return emptyList()
        if (head !in PhelSpecialForms.LET_LIKE) return emptyList()

        val bindingVector = forms[1] as? PhelVec ?: return emptyList()

        return declaredNames(PhelPsiUtils.activeForms(bindingVector))
            .filter { findOuterBinding(list, it.text) != null }
    }

    /** Bindings are name/value pairs, so only the even-indexed entries declare anything. */
    private fun declaredNames(bindings: List<PhelForm>): List<PhelSymbol> =
        bindings.filterIndexed { index, _ -> index % 2 == 0 }
            .mapNotNull { PhelPsiUtils.asSymbol(it) }
            .filter { isReportable(it.text) }

    /** `_` is a deliberate discard and `&` introduces a rest parameter; neither is a real shadow. */
    private fun isReportable(name: String?): Boolean =
        !name.isNullOrEmpty() && name != "_" && !name.startsWith("&")

    /** The nearest enclosing binding or parameter that already uses [name], or null. */
    private fun findOuterBinding(innerForm: PhelList, name: String): PsiElement? {
        var current: PsiElement? = innerForm.parent

        while (current != null) {
            val match = (current as? PhelList)?.let { bindingIn(it, name) }
            if (match != null && match !== innerForm) return match

            current = current.parent
        }

        return null
    }

    private fun bindingIn(form: PhelList, name: String): PsiElement? {
        val forms = PhelPsiUtils.activeForms(form)
        val head = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text ?: return null

        return when (head) {
            in PhelSpecialForms.LET_LIKE -> findInBindingVector(forms, name)
            in PhelSpecialForms.FUNCTION_DEFINING -> findInParameterVector(forms, name)
            else -> null
        }
    }

    private fun findInBindingVector(forms: List<PhelForm>, name: String): PsiElement? {
        val vector = forms.getOrNull(1) as? PhelVec ?: return null

        return declaredNames(PhelPsiUtils.activeForms(vector)).firstOrNull { it.text == name }
    }

    /**
     * The parameter vector is the first vector after the head, which covers `(fn [params] ...)`,
     * `(defn name [params] ...)` and `(defn name "doc" [params] ...)` alike. Only that first vector
     * is searched: a later one is a value in the body, not a parameter list.
     */
    private fun findInParameterVector(forms: List<PhelForm>, name: String): PsiElement? {
        val vector = forms.drop(1).filterIsInstance<PhelVec>().firstOrNull() ?: return null

        return PhelPsiUtils.activeForms(vector).firstOrNull { PhelPsiUtils.asSymbol(it)?.text == name }
    }
}
