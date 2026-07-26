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

    /**
     * How many levels [text] opens, minus how many it closes.
     *
     * Every bracket counts, not only parentheses. Counting `(` alone left the Enter handler blind to
     * binding vectors and map literals, so `(let [x 1` put the next line at the `let`'s level rather
     * than inside its bindings, and a top-level `[` or `{` indented nothing at all. It also disagreed
     * with the formatter, which has always treated VEC, MAP and SET as indenting containers.
     *
     * `#(` and `#{` need no special case: each contains a counted opener and the `#` is inert.
     */
    fun bracketBalance(text: String): Int {
        var balance = 0
        var inString = false
        var inComment = false
        var i = 0

        while (i < text.length) {
            val char = text[i]

            when {
                inComment -> Unit

                // Outside a string `\(` is a character literal, not an opening paren: the lexer's
                // CHARACTER rule ends in a catch-all `.`, so whatever follows the backslash is part
                // of the literal. Inside a string the same backslash escapes the next character. Both
                // cases consume the pair, which is also what keeps `"\""` from ending the string.
                char == '\\' -> i++

                char == '"' -> inString = !inString

                inString -> Unit

                char == ';' -> inComment = true

                char in OPENERS -> balance++

                char in CLOSERS -> balance--
            }

            i++
        }

        return balance
    }

    private companion object {
        val OPENERS = setOf('(', '[', '{')
        val CLOSERS = setOf(')', ']', '}')
    }
}
