package org.phellang.annotator.analyzers

import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelSymbol

object PhelPhpInteropAnalyzer {

    fun isPhpInterop(text: String): Boolean {
        return text.startsWith("php/")
    }

    fun findPhpInteropRange(symbol: PhelSymbol): TextRange? {
        val text = symbol.text ?: return null

        // Check if this is the "php" part of a php/SYMBOL construction
        if (text == "php") {
            return findPhpInteropRangeFromPhp(symbol)
        }

        // Check if this is the SYMBOL part of a php/SYMBOL construction
        return findPhpInteropRangeFromSymbol(symbol)
    }

    private fun findPhpInteropRangeFromPhp(phpSymbol: PhelSymbol): TextRange? {
        // Look for the next sibling that should be the "/" and then the function name
        var nextSibling = phpSymbol.nextSibling
        while (nextSibling != null && nextSibling.text?.trim()?.isEmpty() == true) {
            nextSibling = nextSibling.nextSibling
        }

        if (nextSibling?.text == "/") {
            // Found the slash, now look for the function name
            var functionNameSibling = nextSibling.nextSibling
            while (functionNameSibling != null && functionNameSibling.text?.trim()?.isEmpty() == true) {
                functionNameSibling = functionNameSibling.nextSibling
            }

            if (functionNameSibling is PhelSymbol) {
                // Create a range that spans from "php" to the function name
                val startOffset = phpSymbol.textRange.startOffset
                val endOffset = functionNameSibling.textRange.endOffset
                return TextRange(startOffset, endOffset)
            }
        }

        // Check if this is inside a PhelAccess (qualified symbol)
        val access = PsiTreeUtil.getParentOfType(phpSymbol, PhelAccess::class.java)
        if (access != null && access.text.startsWith("php/")) {
            return access.textRange
        }

        return null
    }

    private fun findPhpInteropRangeFromSymbol(symbol: PhelSymbol): TextRange? {
        // Check if this symbol is part of a php/SYMBOL construction
        // Look for a previous "php" and "/" pattern
        var prevSibling = symbol.prevSibling
        while (prevSibling != null && prevSibling.text?.trim()?.isEmpty() == true) {
            prevSibling = prevSibling.prevSibling
        }

        if (prevSibling?.text == "/") {
            // Found the slash, now look for "php" before it
            var phpSibling = prevSibling.prevSibling
            while (phpSibling != null && phpSibling.text?.trim()?.isEmpty() == true) {
                phpSibling = phpSibling.prevSibling
            }

            if (phpSibling is PhelSymbol && phpSibling.text == "php") {
                // Create a range that spans from "php" to this symbol
                val startOffset = phpSibling.textRange.startOffset
                val endOffset = symbol.textRange.endOffset
                return TextRange(startOffset, endOffset)
            }
        }

        // Check if this is inside a PhelAccess (qualified symbol)
        val access = PsiTreeUtil.getParentOfType(symbol, PhelAccess::class.java)
        if (access != null && access.text.startsWith("php/")) {
            return access.textRange
        }

        return null
    }
}
