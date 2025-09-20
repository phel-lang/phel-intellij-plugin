package org.phellang.annotator.analyzers

import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

object PhelSymbolPositionAnalyzer {

    fun isInFunctionCallPosition(symbol: PhelSymbol): Boolean {
        var current = symbol.parent
        while (current != null && current !is PhelList) {
            current = current.parent
        }

        if (current is PhelList) {
            val list = current

            // Get the first form in the list (function name position)
            val firstForm = list.forms.firstOrNull()

            if (firstForm is PhelSymbol) {
                return firstForm === symbol
            } else if (firstForm is PhelAccess) {
                // Check if our symbol is contained within the first form (access)
                return firstForm.textRange.contains(symbol.textRange)
            }
        }

        return false
    }

    fun hasNamespacePrefix(text: String): Boolean {
        return text.contains("/") && !text.startsWith("/") && !text.endsWith("/")
    }
}
