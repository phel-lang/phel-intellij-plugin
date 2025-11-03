package org.phellang.language.psi.mixins

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import org.phellang.language.psi.PhelForm

abstract class PhelFormMixin(node: ASTNode) : ASTWrapperPsiElement(node), PhelForm {
    override fun toString(): String {
        return text ?: ""
    }
}
