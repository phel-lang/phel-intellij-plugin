package org.phellang.editor.structure

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

object PhelStructuralFormRecognizer {

    fun classify(list: PhelList): PhelStructuralForm? {
        val firstSymbol = firstSymbolOf(list) ?: return null
        return PhelStructuralForm.fromKeyword(firstSymbol.text)
    }

    fun definedNameOf(list: PhelList): String? {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java) ?: return null
        if (forms.size < 2) return null
        return PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)?.text
    }

    private fun firstSymbolOf(list: PhelList): PhelSymbol? {
        val first = PsiTreeUtil.findChildOfType(list, PhelForm::class.java) ?: return null
        return PsiTreeUtil.findChildOfType(first, PhelSymbol::class.java)
    }
}
