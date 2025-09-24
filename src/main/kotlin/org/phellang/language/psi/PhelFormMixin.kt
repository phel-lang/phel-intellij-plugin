package org.phellang.language.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class PhelFormMixin(node: ASTNode) : ASTWrapperPsiElement(node), PhelForm {
    override fun toString(): String {
        return text ?: ""
    }
}
