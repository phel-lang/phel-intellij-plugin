package org.phellang.language.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class PhelMetadataMixin(node: ASTNode) : ASTWrapperPsiElement(node), PhelMetadata {
    override fun toString(): String {
        return text ?: ""
    }
}
