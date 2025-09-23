package org.phellang.core.psi

import com.intellij.psi.PsiElement
import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.psi.*

object PhelPsiUtils {

    @JvmStatic
    fun getName(symbol: PhelSymbol): String? {
        return PhelErrorHandler.safeOperation({
            val text = symbol.text ?: return@safeOperation null

            // Handle qualified symbols (namespace/name)
            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0 && slashIndex < text.length - 1) {
                text.substring(slashIndex + 1)
            } else {
                text
            }
        })
    }

    @JvmStatic
    fun getQualifier(symbol: PhelSymbol): String? {
        return PhelErrorHandler.safeOperation({
            val text = symbol.text ?: return@safeOperation null

            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0) {
                text.take(slashIndex)
            } else {
                null
            }
        })
    }

    @JvmStatic
    fun getNameTextOffset(symbol: PhelSymbol): Int {
        return PhelErrorHandler.safeOperation({
            val text = symbol.text ?: return@safeOperation symbol.textRange.startOffset

            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0 && slashIndex < text.length - 1) {
                symbol.textRange.startOffset + slashIndex + 1
            } else {
                symbol.textRange.startOffset
            }
        }) ?: 0
    }

    @JvmStatic
    fun toString(form: PhelForm): String {
        return PhelErrorHandler.safeOperation({
            form.text ?: ""
        }) ?: ""
    }

    @JvmStatic
    fun toString(metadata: PhelMetadata): String {
        return PhelErrorHandler.safeOperation({
            metadata.text ?: ""
        }) ?: ""
    }

    @JvmStatic
    fun toString(readerMacro: PhelReaderMacro): String {
        return PhelErrorHandler.safeOperation({
            readerMacro.text ?: ""
        }) ?: ""
    }

    @JvmStatic
    fun getTextOffset(list: PhelList): Int {
        return PhelErrorHandler.safeOperation({
            list.textRange.startOffset
        }) ?: 0
    }

    @JvmStatic
    fun getFirst(list: PhelList): PsiElement? {
        return PhelErrorHandler.safeOperation({
            // Get the first form inside the list (skip the opening parenthesis)
            val children = list.children
            children.find { it is PhelForm }
        })
    }

    @JvmStatic
    fun getLiteralType(literal: PhelLiteral): String {
        return PhelErrorHandler.safeOperation({
            val text = literal.text ?: return@safeOperation "unknown"
            
            when {
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
        }) ?: "unknown"
    }

    @JvmStatic
    fun getLiteralText(literal: PhelLiteral): String {
        return PhelErrorHandler.safeOperation({
            literal.text ?: ""
        }) ?: ""
    }
}
