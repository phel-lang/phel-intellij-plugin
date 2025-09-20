package org.phellang.completion.infrastructure

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.completion.documentation.PhelFunctionDocumentation
import org.phellang.completion.documentation.UnknownBasicDocumentation
import org.phellang.language.psi.PhelSymbol

class PhelDocumentationProvider : AbstractDocumentationProvider() {

    /**
     * This is the modal that pop-ups when hovering over a function
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
                val doc = PhelFunctionDocumentation.functionDocs[symbolName]
                if (doc != null) {
                    return wrapInHtml(doc)
                }

                val signature = getSignature(symbolName) ?: symbolName
                val category = UnknownBasicDocumentation.generateBasicDocForElement(elementToClassify)
                return wrapInHtml("<h3>$symbolName</h3><p><b>Type:</b> $category</p><p><b>Signature:</b> <code>$signature</code></p><p>Documentation not available for this symbol.</p>")
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