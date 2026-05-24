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

    /**
     * Returns true when [symbol] is the immediate argument of a PHP constructor form:
     * either `(new ClassName ...)` or `(php/new ClassName ...)`. Used to colour the
     * class name itself as PHP interop even when it isn't a `:use`-d short class.
     */
    fun isConstructorClassArgument(symbol: PhelSymbol): Boolean {
        var current: PsiElement? = symbol.parent
        while (current != null && current !is PhelList) {
            current = current.parent
        }
        val list = current as? PhelList ?: return false
        val forms = list.forms
        if (forms.size < 2) return false

        val head = forms[0]
        val headSymbol = head as? PhelSymbol ?: findFirstSymbol(head) ?: return false
        val headText = headSymbol.text ?: return false
        if (headText != "new" && headText != "php/new") return false

        val argSymbol = forms[1] as? PhelSymbol ?: findFirstSymbol(forms[1]) ?: return false
        return argSymbol === symbol
    }
}
