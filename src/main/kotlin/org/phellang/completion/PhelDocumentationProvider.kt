package org.phellang.completion

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.completion.documentation.PhelApiDocumentation
import org.phellang.completion.documentation.PhelBasicDocumentation
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelSymbol

class PhelDocumentationProvider : AbstractDocumentationProvider() {

    /**
     * This is the modal that pop-ups when hovering over a symbol
     */
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        val elementToClassify = originalElement as? PhelSymbol ?: if (originalElement != null) {
            val parentSymbol = PsiTreeUtil.getParentOfType(originalElement, PhelSymbol::class.java)
            parentSymbol ?: element
        } else {
            element
        }

        if (elementToClassify is PhelSymbol) {
            val symbolName = elementToClassify.text
            if (symbolName != null && symbolName.isNotEmpty()) {
                // Check if the symbol is a argument/parameter - avoid the argument "name" displays the doc from Phel function "name"
                if (PhelSymbolAnalyzer.isParameterReference(elementToClassify) ||
                    PhelSymbolAnalyzer.isFunctionParameter(elementToClassify) ||
                    PhelSymbolAnalyzer.isLetBinding(elementToClassify)
                ) {
                    val category = PhelBasicDocumentation.generateBasicDocForElement(elementToClassify)
                    return wrapInHtml("<h3>$symbolName</h3><br />$category<br /><br />")
                }

                val doc = PhelApiDocumentation.functionDocs[symbolName]
                if (doc != null) {
                   return wrapInHtml(doc)
                }

                val category = PhelBasicDocumentation.generateBasicDocForElement(elementToClassify)
                return wrapInHtml("<h3>$symbolName</h3><br />$category<br /><br />")
            }
        }

        return super.generateDoc(element, originalElement)
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element is PhelSymbol) {
            val symbolName = element.text
            if (symbolName != null && symbolName.isNotEmpty()) {
                val signature = getSignature(symbolName)
                if (signature != null) {
                    return "$symbolName $signature"
                }
            }
        }
        return super.getQuickNavigateInfo(element, originalElement)
    }

    private fun wrapInHtml(content: String): String {
        return "<html><body>$content</body></html>"
    }

    private fun getSignature(symbolName: String): String? {
        return PhelFunctionRegistry.getFunction(symbolName)?.signature
    }
}