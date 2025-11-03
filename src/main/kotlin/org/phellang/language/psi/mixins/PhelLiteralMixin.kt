package org.phellang.language.psi.mixins

import com.intellij.lang.ASTNode
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.impl.PhelSFormImpl

abstract class PhelLiteralMixin(node: ASTNode) : PhelSFormImpl(node), PhelLiteral {
    fun getLiteralType(): String {
        val text = text ?: return "unknown"
        
        return when {
            text == "true" || text == "false" -> "boolean"
            text == "nil" -> "nil"
            text == "NAN" -> "nan"
            text.startsWith("\"") && text.endsWith("\"") -> "string"
            text.startsWith("\\") -> "char"
            text.matches(Regex("[+-]?[0-9]+")) -> "integer"
            text.matches(Regex("[+-]?[0-9]+\\.[0-9]*([eE][+-]?[0-9]+)?")) -> "float"
            text.matches(Regex("[+-]?0x[\\da-fA-F_]+")) -> "hexnum"
            text.matches(Regex("[+-]?0b[01_]+")) -> "binnum"
            text.matches(Regex("[+-]?0o[0-7_]+")) -> "octnum"
            else -> "unknown"
        }
    }

    fun getLiteralText(): String {
        return text ?: ""
    }
}
