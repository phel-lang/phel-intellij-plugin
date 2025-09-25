package org.phellang.editor.indentation

import com.intellij.openapi.editor.Document

class PhelLineAnalyzer(private val document: Document) {

    fun getLineText(lineNumber: Int): String {
        if (lineNumber < 0 || lineNumber >= document.lineCount) {
            return ""
        }
        val lineStart = document.getLineStartOffset(lineNumber)
        val lineEnd = document.getLineEndOffset(lineNumber)
        return document.text.substring(lineStart, lineEnd)
    }

    fun countOpenParentheses(text: String): Int {
        return countParentheses(text, '(')
    }

    fun countCloseParentheses(text: String): Int {
        return countParentheses(text, ')')
    }

    fun getParenthesesBalance(text: String): Int {
        return countOpenParentheses(text) - countCloseParentheses(text)
    }

    private fun countParentheses(text: String, targetChar: Char): Int {
        var count = 0
        var inString = false
        var inComment = false
        var i = 0

        while (i < text.length) {
            val char = text[i]

            when {
                char == ';' && !inString -> {
                    inComment = true
                }

                char == '"' && !inComment -> {
                    if (!inString) {
                        inString = true
                    } else {
                        // Check if it's escaped
                        val backslashCount = countPrecedingBackslashes(text, i)
                        if (backslashCount % 2 == 0) {
                            inString = false
                        }
                    }
                }

                char == targetChar && !inString && !inComment -> {
                    count++
                }
            }
            i++
        }

        return count
    }

    private fun countPrecedingBackslashes(text: String, position: Int): Int {
        var count = 0
        var i = position - 1
        while (i >= 0 && text[i] == '\\') {
            count++
            i--
        }
        return count
    }
}
