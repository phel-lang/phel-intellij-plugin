package org.phellang.editor.indentation

import com.intellij.openapi.editor.Document

class PhelIndentationCalculator {

    fun calculateIndentationLevel(
        document: Document, currentLineNumber: Int, textBeforeCaret: String
    ): Int {
        val lineAnalyzer = PhelLineAnalyzer(document)

        val totalNestingLevel = calculateTotalNestingLevel(lineAnalyzer, currentLineNumber, textBeforeCaret)

        return maxOf(0, totalNestingLevel)
    }

    private fun calculateTotalNestingLevel(
        lineAnalyzer: PhelLineAnalyzer, upToLine: Int, textBeforeCaret: String
    ): Int {
        var nestingLevel = 0

        for (lineNumber in 0 until upToLine) {
            val lineText = lineAnalyzer.getLineText(lineNumber)
            nestingLevel += lineAnalyzer.bracketBalance(lineText)
        }

        nestingLevel += lineAnalyzer.bracketBalance(textBeforeCaret)

        return nestingLevel
    }
}
