package org.phellang.completion

import com.intellij.codeInsight.lookup.CharFilter
import com.intellij.codeInsight.lookup.Lookup
import org.phellang.language.infrastructure.PhelFileType

class PhelCompletionCharFilter : CharFilter() {

    override fun acceptChar(c: Char, prefixLength: Int, lookup: Lookup): Result? {
        val psiFile = lookup.psiFile ?: return null

        if (psiFile.fileType !is PhelFileType) {
            return null
        }

        if (isPhelIdentifierChar(c)) {
            return Result.ADD_TO_PREFIX
        }

        return null
    }

    fun isPhelIdentifierChar(c: Char): Boolean {
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
                c == '$' ||
                c == '/' ||
                c == ':'
    }
}
