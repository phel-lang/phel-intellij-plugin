package org.phellang.documentation.providers

import com.intellij.psi.PsiElement
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.language.psi.PhelSymbol

class PhelQuickNavigateInfoProvider {

    fun getQuickNavigateInfo(element: PsiElement?): String? {
        if (element !is PhelSymbol) return null

        val symbolName = element.text
        if (symbolName.isNullOrEmpty()) return null

        val signature = getSignature(symbolName)

        return if (signature != null) {
            "$symbolName $signature"
        } else {
            null
        }
    }

    private fun getSignature(symbolName: String): String? {
        return PhelFunctionRegistry.getFunction(symbolName)?.signature
    }
}
