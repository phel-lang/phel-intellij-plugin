package org.phellang.documentation.resolvers

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.documentation.PhelApiDocumentation
import org.phellang.completion.documentation.PhelBasicDocumentation
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelSymbol

class PhelSymbolDocumentationResolver {

    fun resolveDocumentation(element: PsiElement?, originalElement: PsiElement?): String? {
        val elementToClassify = extractSymbolElement(element, originalElement) ?: return null

        if (elementToClassify !is PhelSymbol) {
            return null
        }

        val symbolName = elementToClassify.text
        if (symbolName.isNullOrEmpty()) {
            return null
        }

        val content = when {
            isLocalSymbol(elementToClassify) -> generateLocalSymbolDoc(elementToClassify, symbolName)
            else -> resolveApiDocumentation(elementToClassify, symbolName)
        }

        return formatAsHtml(content)
    }

    /**
     * Extracts the topmost PhelSymbol element from the hover position.
     * 
     * For qualified symbols like `json/decode`, the PSI structure has nested PhelSymbol elements:
     * - The full `json/decode` is a PhelSymbol
     * - The `json` part (symbol_plain) is also a PhelSymbol
     * - The `/decode` part (symbol_nsq) is also a PhelSymbol
     * 
     * We need to find the topmost PhelSymbol to get the full qualified name.
     */
    private fun extractSymbolElement(element: PsiElement?, originalElement: PsiElement?): PsiElement? {
        val startElement = originalElement ?: element ?: return null
        
        // Find the first PhelSymbol
        var symbol: PhelSymbol? = when (startElement) {
            is PhelSymbol -> startElement
            else -> PsiTreeUtil.getParentOfType(startElement, PhelSymbol::class.java)
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

    private fun isLocalSymbol(symbol: PhelSymbol): Boolean {
        return PhelSymbolAnalyzer.isParameterReference(symbol) ||
                PhelSymbolAnalyzer.isFunctionParameter(symbol) ||
                PhelSymbolAnalyzer.isLetBinding(symbol)
    }

    private fun generateLocalSymbolDoc(symbol: PhelSymbol, symbolName: String): String {
        val category = PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return "<h3>$symbolName</h3><br />$category<br /><br />"
    }

    private fun resolveApiDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val apiDoc = PhelApiDocumentation.functionDocs[symbolName]
        return apiDoc ?: generateBasicDocumentation(symbol, symbolName)
    }

    private fun generateBasicDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val category = PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return "<h3>$symbolName</h3><br />$category<br /><br />"
    }

    private fun formatAsHtml(content: String): String {
        return "<html><body>$content</body></html>"
    }
}
