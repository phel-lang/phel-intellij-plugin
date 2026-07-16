package org.phellang.editor.folding.validators

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange

object PhelFoldingValidator {
    const val MIN_FOLDING_LENGTH = 15

    fun isValidFoldingRange(range: TextRange, document: Document): Boolean {
        if (range.length < MIN_FOLDING_LENGTH) {
            return false
        }

        val startLine = document.getLineNumber(range.startOffset)
        val endLine = document.getLineNumber(range.endOffset)
        return endLine > startLine
    }

    fun isMultiLine(range: TextRange, document: Document): Boolean {
        val startLine = document.getLineNumber(range.startOffset)
        val endLine = document.getLineNumber(range.endOffset)
        return endLine > startLine
    }
}
