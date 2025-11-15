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

    private fun extractSymbolElement(element: PsiElement?, originalElement: PsiElement?): PsiElement? {
        return when {
            originalElement is PhelSymbol -> originalElement
            originalElement != null -> {
                PsiTreeUtil.getParentOfType(originalElement, PhelSymbol::class.java) ?: element
            }

            else -> element
        }
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
