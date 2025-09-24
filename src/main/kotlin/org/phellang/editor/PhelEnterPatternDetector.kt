package org.phellang.editor

import com.intellij.openapi.editor.Document

object PhelEnterPatternDetector {

    fun shouldHandleSmartEnter(text: CharSequence, offset: Int): Boolean {
        if (offset > 0 && offset < text.length) {
            val prevChar = text[offset - 1]
            val nextChar = text[offset]

            if (prevChar == '(' && nextChar == ')') {
                return !PhelStringDetector.isInsideString(text, offset)
            }
        }

        return false
    }

    fun shouldHandlePostEnter(text: CharSequence, offset: Int, document: Document): Boolean {
        val currentLineNum = document.getLineNumber(offset)

        if (currentLineNum == 0) return false

        val prevLineNum = currentLineNum - 1
        val prevLineStart = document.getLineStartOffset(prevLineNum)
        val prevLineEnd = document.getLineEndOffset(prevLineNum)
        val prevLine = text.substring(prevLineStart, prevLineEnd).trim()

        if (!prevLine.endsWith("(")) return false

        if (currentLineNum + 1 >= document.lineCount) return false

        val nextLineNum = currentLineNum + 1
        val nextLineStart = document.getLineStartOffset(nextLineNum)
        val nextLineEnd = document.getLineEndOffset(nextLineNum)
        val nextLine = text.substring(nextLineStart, nextLineEnd).trim()

        return nextLine.startsWith(")")
    }
}
