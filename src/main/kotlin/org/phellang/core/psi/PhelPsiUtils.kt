package org.phellang.core.psi

import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.psi.*

object PhelPsiUtils {

    @JvmStatic
    fun getName(symbol: PhelSymbol): String? {
        return PhelErrorHandler.safeOperation({
            val text = symbol.text ?: return@safeOperation null

            // Handle qualified symbols (namespace/name)
            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0 && slashIndex < text.length - 1) {
                text.substring(slashIndex + 1)
            } else {
                text
            }
        })
    }

    @JvmStatic
    fun getQualifier(symbol: PhelSymbol): String? {
        return PhelErrorHandler.safeOperation({
            val text = symbol.text ?: return@safeOperation null

            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0) {
                text.take(slashIndex)
            } else {
                null
            }
        })
    }

    @JvmStatic
    fun getNameTextOffset(symbol: PhelSymbol): Int {
        return PhelErrorHandler.safeOperation({
            val text = symbol.text ?: return@safeOperation symbol.textRange.startOffset

            val slashIndex = text.lastIndexOf('/')
            if (slashIndex > 0 && slashIndex < text.length - 1) {
                symbol.textRange.startOffset + slashIndex + 1
            } else {
                symbol.textRange.startOffset
            }
        }) ?: 0
    }
}
