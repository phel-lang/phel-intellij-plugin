package org.phellang.editor.enter

import com.intellij.openapi.editor.Document
import org.phellang.editor.indentation.PhelLineAnalyzer

class PhelEnterHandlerParenthesisManager {

    /**
     * True when the line ends on an opening paren that still wants closing.
     *
     * Asked of the line's *code*, not its raw text. Reading the text meant a line whose comment
     * ended in `(` — `(println 1) ; ((((` — had a closing paren inserted into the code below it,
     * closing nothing, and a line ending inside a string did the same.
     */
    fun shouldAddClosingParenthesis(document: Document, caretPosition: Int, textBeforeCaret: String): Boolean {
        if (PhelLineAnalyzer(document).lastCodeCharacter(textBeforeCaret) != '(') {
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
