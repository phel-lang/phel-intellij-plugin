package org.phellang.documentation.providers

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.language.psi.PhelSymbol

class PhelQuickNavigateInfoProvider {

    fun getQuickNavigateInfo(element: PsiElement?): String? {
        val symbol = extractTopmostSymbol(element) ?: return null

        val symbolName = symbol.text
        if (symbolName.isNullOrEmpty()) return null

        val signature = getSignature(symbolName)

        return if (signature != null) {
            "$symbolName $signature"
        } else {
            null
        }
    }

    /**
     * Finds the topmost PhelSymbol for qualified symbols like `json/decode`.
     */
    private fun extractTopmostSymbol(element: PsiElement?): PhelSymbol? {
        if (element == null) return null
        
        var symbol: PhelSymbol? = when (element) {
            is PhelSymbol -> element
            else -> PsiTreeUtil.getParentOfType(element, PhelSymbol::class.java)
        }
        
        // Traverse up to find the topmost PhelSymbol
        while (symbol != null) {
            val parentSymbol = PsiTreeUtil.getParentOfType(symbol, PhelSymbol::class.java)
            if (parentSymbol == null) {
                break
            }
            symbol = parentSymbol
        }
        
        return symbol
    }

    private fun getSignature(symbolName: String): String? {
        return PhelFunctionRegistry.getFunction(symbolName)?.signature
    }
}
