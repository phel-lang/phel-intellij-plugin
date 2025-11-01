package org.phellang.editor.folding

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*

object PhelFoldingDefaults {

    fun isCollapsedByDefault(node: ASTNode): Boolean {
        val psi = node.psi

        // Auto-collapse namespace declarations by default
        if (psi is PhelList) {
            return isNamespaceDeclaration(psi)
        }

        return false
    }

    fun getDefaultPlaceholderText(node: ASTNode): String? {
        val psi = node.psi
        return when (psi) {
            is PhelList -> "(...)"
            is PhelVec -> "[...]"
            is PhelMap -> "{...}"
            is PhelFormCommentMacro -> "#_..."
            else -> "..."
        }
    }

    private fun isNamespaceDeclaration(list: PhelList): Boolean {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java) ?: return false
        if (forms.isNotEmpty()) {
            val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
            val firstSymbolText = firstSymbol?.text
            return firstSymbolText == "ns"
        }
        return false
    }
}
