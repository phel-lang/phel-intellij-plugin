package org.phellang.inspection.deprecated

object ReplacementParser {

    data class ParsedReplacement(
        val namespace: String?, val functionName: String
    )

    /**
     * Parses a replacement string into namespace and function name.
     * 
     * Examples:
     * - "phel\str\contains?" -> ParsedReplacement("str", "str/contains?")
     * - "assoc" -> ParsedReplacement(null, "assoc")
     * - "phel\json\encode" -> ParsedReplacement("json", "json/encode")
     */
    fun parse(replacement: String): ParsedReplacement {
        if (replacement.contains("\\")) {
            val parts = replacement.split("\\")
            if (parts.size >= 3 && parts[0] == "phel") {
                val namespace = parts[1]
                val functionName = parts.drop(2).joinToString("\\")
                return ParsedReplacement(namespace, "$namespace/$functionName")
            }
        }
        return ParsedReplacement(null, replacement)
    }

    /**
     * Formats a replacement string for display in the UI.
     * Converts "phel\namespace\function" to "namespace/function".
     * 
     * Examples:
     * - "phel\str\contains?" -> "str/contains?"
     * - "assoc" -> "assoc"
     */
    fun formatForDisplay(replacement: String): String {
        if (replacement.contains("\\")) {
            val parts = replacement.split("\\")
            if (parts.size >= 3 && parts[0] == "phel") {
                return "${parts[1]}/${parts.drop(2).joinToString("\\")}"
            }
        }
        return replacement
    }
}
