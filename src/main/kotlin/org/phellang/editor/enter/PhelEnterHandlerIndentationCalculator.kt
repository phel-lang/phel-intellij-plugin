package org.phellang.editor.enter

import com.intellij.openapi.editor.Document
import org.phellang.editor.indentation.PhelIndentationCalculator

class PhelEnterHandlerIndentationCalculator {

    private val indentationCalculator = PhelIndentationCalculator()

    fun calculateIndentation(document: Document, currentLineNumber: Int, textBeforeCaret: String, currentLineText: String): String {
        val indentationLevel = indentationCalculator.calculateIndentationLevel(
            document, currentLineNumber, textBeforeCaret
        )

        val currentIndentationSpaces = currentLineText.takeWhile { it.isWhitespace() }.length
        val currentIndentationLevel = currentIndentationSpaces / 2 // Assuming 2 spaces per level

        val additionalIndentationLevel = indentationLevel - currentIndentationLevel
        return " ".repeat(maxOf(0, additionalIndentationLevel) * 2) // 2 spaces per level
    }

    fun getCurrentIndentationSpaces(currentLineText: String): Int {
        return currentLineText.takeWhile { it.isWhitespace() }.length
    }
}
