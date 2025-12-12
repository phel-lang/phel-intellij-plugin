package org.phellang.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.documentation.providers.PhelQuickNavigateInfoProvider
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver
import org.phellang.language.psi.PhelSymbol

class PhelDocumentationProvider : AbstractDocumentationProvider() {

    private val documentationResolver = PhelSymbolDocumentationResolver()
    private val quickNavigateInfoProvider = PhelQuickNavigateInfoProvider()

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        return documentationResolver.resolveDocumentation(element, originalElement)
            ?: super.generateDoc(element, originalElement)
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        // Use the topmost symbol for quick navigate info
        val symbol = findTopmostSymbol(originalElement) ?: findTopmostSymbol(element)
        return quickNavigateInfoProvider.getQuickNavigateInfo(symbol)
            ?: super.getQuickNavigateInfo(element, originalElement)
    }

    /**
     * Returns the element to use for documentation lookup.
     * This is called by IntelliJ to determine the target element when the user hovers.
     * We find the topmost PhelSymbol to ensure qualified symbols like `json/decode`
     * are treated as a single unit.
     */
    override fun getCustomDocumentationElement(
        editor: com.intellij.openapi.editor.Editor,
        file: com.intellij.psi.PsiFile,
        contextElement: PsiElement?,
        targetOffset: Int
    ): PsiElement? {
        return findTopmostSymbol(contextElement)
    }

    /**
     * Finds the topmost PhelSymbol ancestor for an element.
     * For qualified symbols like `json/decode`, this ensures we get the full symbol
     * regardless of which part the cursor is on.
     */
    private fun findTopmostSymbol(element: PsiElement?): PhelSymbol? {
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
}
