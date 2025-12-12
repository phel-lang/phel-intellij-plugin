package org.phellang.core.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.psi.*

object PhelPsiUtils {

    private val INTEGER_REGEX = Regex("[+-]?[0-9]+")
    private val FLOAT_REGEX = Regex("[+-]?[0-9]+\\.[0-9]*([eE][+-]?[0-9]+)?")
    private val HEX_NUM_REGEX = Regex("[+-]?0x[\\da-fA-F_]+")
    private val BIN_NUM_REGEX = Regex("[+-]?0b[01_]+")
    private val OCT_NUM_REGEX = Regex("[+-]?0o[0-7_]+")

    @JvmStatic
    fun findTopmostSymbol(element: PsiElement?): PhelSymbol? {
        if (element == null) return null

        var symbol: PhelSymbol? = when (element) {
            is PhelSymbol -> element
            else -> PsiTreeUtil.getParentOfType(element, PhelSymbol::class.java)
        }

        // Traverse up to find the topmost PhelSymbol
        while (symbol != null) {
            val parentSymbol = PsiTreeUtil.getParentOfType(symbol, PhelSymbol::class.java) ?: break
            symbol = parentSymbol
        }

        return symbol
    }

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
        return PhelErrorHandler.safeOperation {
            val text = literal.text ?: return@safeOperation "unknown"

            when {
                text == "true" || text == "false" -> "boolean"
                text == "nil" -> "nil"
                text == "NAN" -> "nan"
                text.startsWith("\"") && text.endsWith("\"") -> "string"
                text.startsWith("\\") -> "char"
                text.matches(INTEGER_REGEX) -> "integer"
                text.matches(FLOAT_REGEX) -> "float"
                text.matches(HEX_NUM_REGEX) -> "hexnum"
                text.matches(BIN_NUM_REGEX) -> "binnum"
                text.matches(OCT_NUM_REGEX) -> "octnum"
                else -> "unknown"
            }
        } ?: "unknown"
    }

    @JvmStatic
    fun getLiteralText(literal: PhelLiteral): String {
        return PhelErrorHandler.safeOperation {
            literal.text ?: ""
        } ?: ""
    }
}
