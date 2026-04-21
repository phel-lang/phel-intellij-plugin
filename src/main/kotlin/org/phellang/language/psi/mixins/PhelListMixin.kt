package org.phellang.language.psi.mixins

import com.intellij.lang.ASTNode
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.impl.PhelLVFormImpl

abstract class PhelListMixin(node: ASTNode) : PhelLVFormImpl(node), PhelList {
    override fun getTextOffset(): Int = textRange.startOffset

    fun getFirst(): PhelForm? = children.firstOrNull { it is PhelForm } as? PhelForm
}
