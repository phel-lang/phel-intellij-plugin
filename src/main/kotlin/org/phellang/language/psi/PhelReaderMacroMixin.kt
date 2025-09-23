package org.phellang.language.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class PhelReaderMacroMixin(node: ASTNode) : ASTWrapperPsiElement(node), PhelReaderMacro {
    override fun toString(): String {
        return text ?: ""
    }
}
