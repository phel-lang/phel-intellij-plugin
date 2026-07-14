package org.phellang.core.psi

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Whether a name is bound locally — by an enclosing `let`/`loop`/`for`/`binding`, or as a function
 * parameter — at the point [symbol] appears.
 *
 * Both the arity inspection and the parameter-hint provider need this to suppress output for a name
 * that shadows a same-named core function, and had identical copies of it.
 */
internal object PhelLocalBindingScope {

    private val BINDING_INTRO_FORMS = PhelSpecialForms.LET_LIKE
    private val FUNCTION_INTRO_FORMS = PhelSpecialForms.FUNCTION_DEFINING

    fun resolvesToLocalBinding(symbol: PhelSymbol, name: String): Boolean {
        var current: PsiElement? = symbol.parent
        while (current != null) {
            if (current is PhelList) {
                val forms = current.forms
                val head = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text
                if (head in BINDING_INTRO_FORMS && bindingVecContains(forms, name)) return true
                if (head in FUNCTION_INTRO_FORMS && paramVecContains(forms, name)) return true
            }
            current = current.parent
        }
        return false
    }

    /** Names sit at the even positions of a `[name value …]` binding vector. */
    private fun bindingVecContains(forms: List<PhelForm>, name: String): Boolean {
        val bindings = (forms.getOrNull(1) as? PhelVec)?.forms ?: return false
        return (bindings.indices step 2).any { PhelPsiUtils.asSymbol(bindings[it])?.text == name }
    }

    private fun paramVecContains(forms: List<PhelForm>, name: String): Boolean {
        val paramVec = forms.drop(1).firstNotNullOfOrNull { it as? PhelVec } ?: return false
        return paramVec.forms.any { PhelPsiUtils.asSymbol(it)?.text == name }
    }
}
