package org.phellang.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
import org.phellang.core.psi.PhelPsiUtils
import org.phellang.documentation.providers.PhelQuickNavigateInfoProvider
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver

class PhelDocumentationProvider : AbstractDocumentationProvider() {

    private val documentationResolver = PhelSymbolDocumentationResolver()
    private val quickNavigateInfoProvider = PhelQuickNavigateInfoProvider()

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        return documentationResolver.resolveDocumentation(element, originalElement)
            ?: super.generateDoc(element, originalElement)
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        val symbol = PhelPsiUtils.findTopmostSymbol(originalElement)
            ?: PhelPsiUtils.findTopmostSymbol(element)
        return quickNavigateInfoProvider.getQuickNavigateInfo(symbol)
            ?: super.getQuickNavigateInfo(element, originalElement)
    }

    override fun getCustomDocumentationElement(
        editor: com.intellij.openapi.editor.Editor,
        file: com.intellij.psi.PsiFile,
        contextElement: PsiElement?,
        targetOffset: Int
    ): PsiElement? {
        return PhelPsiUtils.findTopmostSymbol(contextElement)
    }
}
