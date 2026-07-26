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
        forEachBracket(text) { _, char -> if (char in OPENERS) balance++ else balance-- }

        return balance
    }

    /**
     * Where [text]'s code ends: before any trailing `;` comment, and before the whitespace in front
     * of it.
     *
     * Anything appending to a line has to stop here rather than at the end. `(println (inc 1) ; note`
     * ends inside a comment, so a closing paren added at the end would be commented out along with
     * the note — and appending at the comment's `;` would leave `(inc 1) )`, a space the author did
     * not write. A `;` inside a string or after a `\` is not a comment and does not count.
     */
    fun activeCodeLength(text: String): Int {
        var commentStart = text.length
        forEachCodeCharacter(text) { index, char ->
            if (char == ';' && commentStart == text.length) commentStart = index
        }

        return text.substring(0, commentStart).trimEnd().length
    }

    /**
     * The brackets [text] leaves open, outermost first.
     *
     * Where [bracketBalance] answers *how deep*, this answers *which* — `(let [x` leaves `['(', '[']`,
     * so a caller completing the form knows to close the vector before the list. A closer with no
     * matching opener is dropped rather than going negative: there is nothing to complete for it.
     */
    fun unclosedOpeners(text: String): List<Char> {
        val open = ArrayDeque<Char>()
        forEachBracket(text) { _, char -> if (char in OPENERS) open.addLast(char) else open.removeLastOrNull() }

        return open.toList()
    }

    private inline fun forEachBracket(text: String, onBracket: (Int, Char) -> Unit) {
        forEachCodeCharacter(text) { index, char ->
            if (char in OPENERS || char in CLOSERS) onBracket(index, char)
        }
    }

    /**
     * Walks [text], reporting only the characters that are really code.
     *
     * Every public function here runs this rather than carrying its own scan. The skipping is the
     * part that is easy to get subtly wrong — strings, `;` comments and `\(` character literals —
     * and a second copy would be a second chance to drift.
     *
     * The `;` that opens a comment is reported, since one caller needs to know where that is; the
     * rest of the comment is not.
     */
    private inline fun forEachCodeCharacter(text: String, onCode: (Int, Char) -> Unit) {
        var inString = false
        var inComment = false
        var i = 0

        while (i < text.length) {
            val char = text[i]

            when {
                inComment -> if (char == '\n') inComment = false

                // Outside a string `\(` is a character literal, not an opening paren: the lexer's
                // CHARACTER rule ends in a catch-all `.`, so whatever follows the backslash is part
                // of the literal. Inside a string the same backslash escapes the next character. Both
                // cases consume the pair, which is also what keeps `"\""` from ending the string.
                char == '\\' -> i++

                char == '"' -> {
                    inString = !inString
                    onCode(i, char)
                }

                inString -> Unit

                else -> {
                    if (char == ';') inComment = true
                    onCode(i, char)
                }
            }

            i++
        }
    }

    private companion object {
        val OPENERS = setOf('(', '[', '{')
        val CLOSERS = setOf(')', ']', '}')
    }
}
