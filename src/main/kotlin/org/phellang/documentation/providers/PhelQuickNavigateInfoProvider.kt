package org.phellang.documentation.providers

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelTypeTags
import org.phellang.registry.PhelFunctionRegistry
import org.phellang.language.psi.utils.PhelPsiUtils

class PhelQuickNavigateInfoProvider {

    fun getQuickNavigateInfo(element: PsiElement?): String? {
        val symbol = PhelPsiUtils.findTopmostSymbol(element) ?: return null
        // `^map` names a type; the signature of the function it spells would describe something else.
        if (PhelTypeTags.isTypeTag(symbol)) return null

        val symbolName = symbol.text
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
