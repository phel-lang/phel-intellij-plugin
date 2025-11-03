package org.phellang.language.psi.mixins

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import org.phellang.language.psi.PhelReaderMacro

abstract class PhelReaderMacroMixin(node: ASTNode) : ASTWrapperPsiElement(node), PhelReaderMacro {
    override fun toString(): String {
        return text ?: ""
    }
}
