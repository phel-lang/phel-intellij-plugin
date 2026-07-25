package org.phellang.editor.enter

import com.intellij.openapi.editor.Document
import org.phellang.editor.indentation.PhelIndentationCalculator

class PhelEnterHandlerIndentationCalculator {

    private val indentationCalculator = PhelIndentationCalculator()

    /**
     * The indentation the new line should end up with: one level per still-open parenthesis.
     *
     * Absolute, not relative. This used to return only the *extra* indentation to add on top of what
     * the platform's enter handler had already copied from the previous line, clamped at zero — so it
     * could indent further but never less. Closing a form therefore left the new line at the old
     * depth: enter after `(print "hello"))` gave two spaces where the form was over and the answer
     * was none, and after `(print 1)))` gave four.
     */
    fun targetIndentation(document: Document, currentLineNumber: Int, textBeforeCaret: String): String {
        val level = indentationCalculator.calculateIndentationLevel(document, currentLineNumber, textBeforeCaret)

        return " ".repeat(level * SPACES_PER_LEVEL)
    }

    fun getCurrentIndentationSpaces(currentLineText: String): Int {
        return currentLineText.takeWhile { it.isWhitespace() }.length
    }

    private companion object {
        const val SPACES_PER_LEVEL = 2
    }
}
