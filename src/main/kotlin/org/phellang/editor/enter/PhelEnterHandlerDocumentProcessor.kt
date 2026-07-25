package org.phellang.editor.enter

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor

class PhelEnterHandlerDocumentProcessor {

    fun extractLineInformation(document: Document, caretOffset: Int): LineInformation {
        val currentLineNumber = document.getLineNumber(caretOffset)
        val currentLineStart = document.getLineStartOffset(currentLineNumber)
        val textBeforeCaret = document.text.substring(currentLineStart, caretOffset)
        val currentLineText = document.text.substring(currentLineStart, document.getLineEndOffset(currentLineNumber))

        return LineInformation(currentLineNumber, textBeforeCaret, currentLineText)
    }

    /**
     * Replaces the new line's leading whitespace with [indentation], rather than adding to it.
     *
     * The platform's enter handler has already run and copied the previous line's indentation, so
     * inserting could only ever indent further. Setting it is what allows a line to be *less*
     * indented than the one above — which is the normal case the moment a form closes.
     */
    fun applyIndentationAndParenthesis(
        document: Document,
        editor: Editor,
        caretPosition: Int,
        indentation: String,
        shouldAddClosingParen: Boolean,
        closingParenthesisText: String
    ) {
        val lineNumber = document.getLineNumber(caretPosition)
        val lineStart = document.getLineStartOffset(lineNumber)
        val lineEnd = document.getLineEndOffset(lineNumber)

        // Only the leading run: text already on the line (enter pressed mid-line) must survive.
        val existing = document.text.substring(lineStart, lineEnd).takeWhile { it == ' ' || it == '\t' }.length

        if (document.text.substring(lineStart, lineStart + existing) != indentation) {
            document.replaceString(lineStart, lineStart + existing, indentation)
        }

        val newCaretPosition = lineStart + indentation.length
        if (shouldAddClosingParen) {
            document.insertString(newCaretPosition, closingParenthesisText)
        }

        editor.caretModel.moveToOffset(newCaretPosition)
    }
}
