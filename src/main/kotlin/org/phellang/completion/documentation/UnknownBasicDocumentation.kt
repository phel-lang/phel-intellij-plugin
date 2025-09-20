package org.phellang.completion.documentation

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

object UnknownBasicDocumentation {
    fun generateBasicDocForElement(element: PhelSymbol, symbolName: String): String {
        return categorizeSymbol(element, symbolName)
    }

    private fun categorizeSymbol(element: PsiElement?, symbolName: String): String {
        // First check if this is a definition and determine its type
        if (element is PhelSymbol) {
            if (PhelSymbolAnalyzer.isDefinition(element)) {
                // Check if it's a function parameter or let binding first
                if (isInParameterVector(element)) {
                    return "Function Parameter"
                } else if (isInLetBinding(element)) {
                    return "Let Binding"
                }

                // Check the defining form to determine type
                val definingForm = getDefiningForm(element)
                return when (definingForm) {
                    "defn", "defn-" -> "Function Definition"
                    "defmacro", "defmacro-" -> "Macro Definition"
                    "def" -> "Variable Definition"
                    "defstruct" -> "Struct Definition"
                    else -> "Definition"
                }
            }
        }

        return "Symbol"
    }

    private fun isInParameterVector(symbol: PhelSymbol): Boolean {
        return PhelSymbolAnalyzer.isFunctionParameter(symbol)
    }

    private fun isInLetBinding(symbol: PhelSymbol): Boolean {
        return PhelSymbolAnalyzer.isLetBinding(symbol)
    }

    private fun getDefiningForm(symbol: PhelSymbol): String? {
        val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java)
        if (containingList != null) {
            val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java)
            if (firstForm != null) {
                val firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)
                return firstSymbol?.text
            }
        }
        return null
    }
}
