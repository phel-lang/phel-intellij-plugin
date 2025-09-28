package org.phellang.editor.enter

import com.intellij.openapi.editor.Document

class PhelEnterHandlerParenthesisManager {

    fun shouldAddClosingParenthesis(document: Document, caretPosition: Int, textBeforeCaret: String): Boolean {
        val trimmedText = textBeforeCaret.trimEnd()
        if (!trimmedText.endsWith('(')) {
            return false
        }

        val textAfterCaret = if (caretPosition < document.textLength) {
            document.text.substring(caretPosition).trimStart()
        } else {
            ""
        }

        return !textAfterCaret.startsWith(')')
    }

    fun createClosingParenthesisText(currentIndentationSpaces: Int): String {
        val closingIndent = " ".repeat(currentIndentationSpaces)
        return "\n$closingIndent)"
    }
}
