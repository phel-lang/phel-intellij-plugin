package org.phellang.language.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.phellang.language.psi.impl.PhelLVFormImpl

abstract class PhelListMixin(node: ASTNode) : PhelLVFormImpl(node), PhelList {
    override fun getTextOffset(): Int {
        return textRange.startOffset
    }

    fun getFirst(): PsiElement? {
        return children.find { it is PhelForm }
    }
}
