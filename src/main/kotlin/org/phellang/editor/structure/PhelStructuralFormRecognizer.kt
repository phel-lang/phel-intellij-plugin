package org.phellang.editor.structure

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.utils.PhelPsiUtils

object PhelStructuralFormRecognizer {

    fun classify(list: PhelList): PhelStructuralForm? {
        val firstSymbol = firstSymbolOf(list) ?: return null
        return PhelStructuralForm.fromKeyword(firstSymbol.text)
    }

    fun definedNameOf(list: PhelList): String? {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java) ?: return null
        if (forms.size < 2) return null
        // asSymbol reads past a tag on the name: `(defn ^map build ...)` is `build`, not `map`.
        return PhelPsiUtils.asSymbol(forms[1])?.text
    }

    private fun firstSymbolOf(list: PhelList): PhelSymbol? {
        val first = PsiTreeUtil.findChildOfType(list, PhelForm::class.java) ?: return null
        return PsiTreeUtil.findChildOfType(first, PhelSymbol::class.java)
    }
}
