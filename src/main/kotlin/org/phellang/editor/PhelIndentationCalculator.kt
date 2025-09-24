package org.phellang.editor

object PhelIndentationCalculator {

    private const val INDENT_SIZE = 2

    fun getIndentSize(line: String): Int {
        var count = 0
        for (char in line) {
            when (char) {
                ' ' -> count++
                '\t' -> count += INDENT_SIZE
                else -> break
            }
        }
        return count
    }

    fun createIndent(spaces: Int): String = " ".repeat(spaces)

    fun getNextIndentLevel(currentIndent: Int): Int = currentIndent + INDENT_SIZE
}
