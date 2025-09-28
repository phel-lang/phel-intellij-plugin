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

    fun applyIndentationAndParenthesis(
        document: Document,
        editor: Editor,
        caretPosition: Int,
        indentationSpaces: String,
        shouldAddClosingParen: Boolean,
        closingParenthesisText: String
    ) {
        if (indentationSpaces.isNotEmpty()) {
            document.insertString(caretPosition, indentationSpaces)
            val newCaretPosition = caretPosition + indentationSpaces.length

            if (shouldAddClosingParen) {
                document.insertString(newCaretPosition, closingParenthesisText)
            }

            editor.caretModel.moveToOffset(newCaretPosition)
        }
    }
}
