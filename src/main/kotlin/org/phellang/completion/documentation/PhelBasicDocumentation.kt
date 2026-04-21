package org.phellang.completion.documentation

import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelSymbol

object PhelBasicDocumentation {
    fun generateBasicDocForElement(element: PhelSymbol): String = categorizeSymbol(element)

    private fun categorizeSymbol(element: PhelSymbol): String {
        if (PhelSymbolAnalyzer.isParameterReference(element)) return "Function Argument"

        if (PhelSymbolAnalyzer.isDefinition(element)) {
            if (PhelSymbolAnalyzer.isFunctionParameter(element)) return "Function Parameter"
            if (PhelSymbolAnalyzer.isLetBinding(element)) return "Let Binding"

            return when (PhelPsiUtils.getDefiningKeyword(element)) {
                "defn", "defn-" -> "Function Definition"
                "defmacro", "defmacro-" -> "Macro Definition"
                "def" -> "Variable Definition"
                "defstruct" -> "Struct Definition"
                else -> "Definition"
            }
        }

        return "Symbol"
    }

}
