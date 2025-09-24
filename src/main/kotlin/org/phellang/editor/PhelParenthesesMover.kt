package org.phellang.editor

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor

object PhelParenthesesMover {

    fun extractClosingParentheses(line: String): ParenthesesInfo {
        var parenCount = 0
        for (char in line) {
            if (char == ')') {
                parenCount++
            } else {
                break
            }
        }

        val closingParens = ")".repeat(parenCount)
        val remainingContent = line.substring(parenCount)

        return ParenthesesInfo(closingParens, remainingContent, parenCount)
    }

    fun moveClosingParenthesesFromNextLine(
        editor: Editor, document: Document, caretOffset: Int, indentText: String
    ): Boolean {
        val currentLineNum = document.getLineNumber(caretOffset)
        val nextLineNum = currentLineNum + 1

        if (nextLineNum >= document.lineCount) {
            return false
        }

        val nextLineStart = document.getLineStartOffset(nextLineNum)
        val nextLineEnd = document.getLineEndOffset(nextLineNum)
        val nextLine = document.getText(com.intellij.openapi.util.TextRange(nextLineStart, nextLineEnd))

        val trimmedNextLine = nextLine.trim()
        if (!trimmedNextLine.startsWith(")")) {
            return false
        }

        val parenInfo = extractClosingParentheses(trimmedNextLine)

        document.replaceString(nextLineStart, nextLineEnd, parenInfo.remainingContent)

        val insertText = "$indentText${parenInfo.closingParens}"
        document.insertString(caretOffset, insertText)

        editor.caretModel.moveToOffset(caretOffset + indentText.length)

        return true
    }
}
