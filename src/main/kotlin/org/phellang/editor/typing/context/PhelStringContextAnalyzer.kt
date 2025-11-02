package org.phellang.editor.typing.context

object PhelStringContextAnalyzer {

    fun isInsideString(text: CharSequence, offset: Int): Boolean {
        if (offset < 0 || offset >= text.length) {
            return false
        }
        if (text.isEmpty()) {
            return false
        }

        // Count unescaped quotes before this position
        var quoteCount = 0
        var i = 0
        while (i < offset && i < text.length) {
            val c = text[i]
            if (c == '"') {
                // Check if this quote is escaped
                if (!isEscapedQuote(text, i)) {
                    quoteCount++
                }
            }
            i++
        }

        // Odd number of quotes means we're inside a string
        return quoteCount % 2 == 1
    }

    private fun isEscapedQuote(text: CharSequence, quotePosition: Int): Boolean {
        var backslashCount = 0
        var j = quotePosition - 1
        while (j >= 0 && text[j] == '\\') {
            backslashCount++
            j--
        }
        // If odd number of backslashes, quote is escaped
        return backslashCount % 2 == 1
    }
}
