package org.phellang.editor

object PhelStringDetector {

    fun isInsideString(text: CharSequence, offset: Int): Boolean {
        var quoteCount = 0
        var i = 0

        while (i < offset && i < text.length) {
            val c = text[i]
            if (c == '"') {
                var backslashCount = 0
                var j = i - 1
                while (j >= 0 && text[j] == '\\') {
                    backslashCount++
                    j--
                }
                if (backslashCount % 2 == 0) {
                    quoteCount++
                }
            }
            i++
        }

        return quoteCount % 2 == 1
    }
}
