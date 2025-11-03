package org.phellang.language.psi.mixins

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import org.phellang.language.psi.PhelMetadata

abstract class PhelMetadataMixin(node: ASTNode) : ASTWrapperPsiElement(node), PhelMetadata {
    override fun toString(): String {
        return text ?: ""
    }
}
