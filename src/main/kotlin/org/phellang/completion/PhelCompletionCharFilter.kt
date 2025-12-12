package org.phellang.completion

import com.intellij.codeInsight.lookup.CharFilter
import com.intellij.codeInsight.lookup.Lookup
import org.phellang.language.infrastructure.PhelFileType

/**
 * Character filter for Phel completion that allows '/' to be part of the completion prefix.
 * This enables namespaced function completion like 'str/split', 'json/encode', etc.
 */
class PhelCompletionCharFilter : CharFilter() {

    override fun acceptChar(c: Char, prefixLength: Int, lookup: Lookup): Result? {
        val psiFile = lookup.psiFile ?: return null

        if (psiFile.fileType !is PhelFileType) {
            return null
        }

        // Allow '/' as part of the completion prefix for namespaced symbols
        if (c == '/') {
            return Result.ADD_TO_PREFIX
        }

        // Allow common Phel identifier characters
        if (isPhelIdentifierChar(c)) {
            return Result.ADD_TO_PREFIX
        }

        return null
    }

    private fun isPhelIdentifierChar(c: Char): Boolean {
        return c.isLetterOrDigit() ||
                c == '-' ||
                c == '_' ||
                c == '?' ||
                c == '!' ||
                c == '*' ||
                c == '+' ||
                c == '<' ||
                c == '>' ||
                c == '=' ||
                c == '&' ||
                c == '%' ||
                c == '$'
    }
}
