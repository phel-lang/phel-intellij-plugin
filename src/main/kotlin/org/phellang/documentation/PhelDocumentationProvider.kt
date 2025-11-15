package org.phellang.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement
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
        return quickNavigateInfoProvider.getQuickNavigateInfo(element)
            ?: super.getQuickNavigateInfo(element, originalElement)
    }
}
