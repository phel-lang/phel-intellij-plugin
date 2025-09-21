package org.phellang.annotator.analyzers

import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

object PhelSymbolPositionAnalyzer {

    fun isInFunctionCallPosition(symbol: PhelSymbol): Boolean {
        // Find the containing list
        var current = symbol.parent
        while (current != null && current !is PhelList) {
            current = current.parent
        }

        if (current is PhelList) {
            val list = current

            // Get the first form in the list (function name position)
            when (val firstForm = list.forms.firstOrNull()) {
                is PhelSymbol -> {
                    return firstForm === symbol
                }

                is PhelAccess -> {
                    // For PhelAccess, check if our symbol is the first child
                    val firstChild = firstForm.children.firstOrNull()
                    return firstChild === symbol
                }

                else -> {
                    // Try to find the first symbol in the first form
                    val firstSymbol = findFirstSymbol(firstForm)
                    return firstSymbol === symbol
                }
            }
        }

        return false
    }

    private fun findFirstSymbol(element: Any?): PhelSymbol? {
        if (element is PhelSymbol) {
            return element
        }
        if (element is PhelForm) {
            val children = element.children
            for (child in children) {
                val symbol = findFirstSymbol(child)
                if (symbol != null) {
                    return symbol
                }
            }
        }
        return null
    }

    fun hasNamespacePrefix(text: String): Boolean {
        return text.contains("\\") && !text.startsWith("\\") && !text.endsWith("\\")
    }
}
