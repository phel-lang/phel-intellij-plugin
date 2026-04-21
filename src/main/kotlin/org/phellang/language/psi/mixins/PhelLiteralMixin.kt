package org.phellang.language.psi.mixins

import com.intellij.lang.ASTNode
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.impl.PhelSFormImpl

abstract class PhelLiteralMixin(node: ASTNode) : PhelSFormImpl(node), PhelLiteral
