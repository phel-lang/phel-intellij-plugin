package org.phellang.tools.transformer

object StringEscaper {

    /**
     * Escapes HTML special characters for display in documentation popups.
     */
    fun escapeHtml(text: String): String {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    }

    /**
     * Escapes a string for use as a Kotlin string literal with double quotes.
     */
    fun toKotlinString(text: String): String {
        val escaped = text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r")
            .replace("\t", "\\t").replace("$", "\\$")
        return "\"$escaped\""
    }

    /**
     * Formats a string as a triple-quoted Kotlin string for multi-line content.
     * Uses regular quotes for simple strings without special characters.
     */
    fun toTripleQuotedString(text: String): String {
        // For simple strings without special characters, use regular quotes
        if (!text.contains("\"") && !text.contains("\n") && !text.contains("<") && text.length < 80) {
            return toKotlinString(text)
        }

        // For complex strings with HTML, quotes, or newlines, use triple quotes
        val escaped = text.replace("$", "\${'$'}")
        return "\"\"\"\n$escaped\n\"\"\""
    }
}
