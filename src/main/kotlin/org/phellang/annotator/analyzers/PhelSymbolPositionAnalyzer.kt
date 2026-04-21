package org.phellang.annotator.analyzers

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

object PhelSymbolPositionAnalyzer {

    fun isInFunctionCallPosition(symbol: PhelSymbol): Boolean {
        var current = symbol.parent
        while (current != null && current !is PhelList) {
            current = current.parent
        }

        if (current is PhelList) {
            when (val firstForm = current.forms.firstOrNull()) {
                is PhelSymbol -> return firstForm === symbol
                is PhelAccess -> return firstForm.children.firstOrNull() === symbol
                else -> return findFirstSymbol(firstForm) === symbol
            }
        }

        return false
    }

    private fun findFirstSymbol(element: PsiElement?): PhelSymbol? {
        if (element is PhelSymbol) return element
        if (element is PhelForm) {
            for (child in element.children) {
                val symbol = findFirstSymbol(child)
                if (symbol != null) return symbol
            }
        }
        return null
    }

    fun hasNamespacePrefix(text: String): Boolean {
        return text.contains("\\") && !text.startsWith("\\") && !text.endsWith("\\")
    }
}
