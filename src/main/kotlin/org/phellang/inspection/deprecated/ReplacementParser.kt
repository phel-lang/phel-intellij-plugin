package org.phellang.inspection.deprecated

object ReplacementParser {

    data class ParsedReplacement(
        val namespace: String?, val functionName: String
    )

    /**
     * Parses a replacement string into namespace and function name. Accepts both the
     * canonical dot-separated form (Phel 0.35+) and the legacy backslash form, as well
     * as already-formatted `ns/fn` strings.
     *
     * Examples:
     * - "phel.string/contains?" -> ParsedReplacement("string", "string/contains?")
     * - "phel.string.contains?" -> ParsedReplacement("string", "string/contains?")
     * - "phel\string\contains?" -> ParsedReplacement("string", "string/contains?")
     * - "string/contains?"      -> ParsedReplacement("string", "string/contains?")
     * - "assoc"                 -> ParsedReplacement(null, "assoc")
     */
    fun parse(replacement: String): ParsedReplacement {
        // "ns/fn" form — already in canonical display shape.
        if (replacement.contains('/') && !replacement.contains('\\')) {
            val slash = replacement.indexOf('/')
            val ns = replacement.substring(0, slash)
            // Strip optional "phel." prefix on the namespace side
            val shortNs = ns.removePrefix("phel.").ifEmpty { return ParsedReplacement(null, replacement) }
            if (!shortNs.contains('.') && !shortNs.contains('\\')) {
                return ParsedReplacement(shortNs, "$shortNs/${replacement.substring(slash + 1)}")
            }
        }

        val normalized = replacement.replace('\\', '.')
        if (normalized.contains('.')) {
            val parts = normalized.split('.')
            if (parts.size >= 3 && parts[0] == "phel") {
                val namespace = parts[1]
                val functionName = parts.drop(2).joinToString(".")
                return ParsedReplacement(namespace, "$namespace/$functionName")
            }
        }
        return ParsedReplacement(null, replacement)
    }

    /**
     * Formats a replacement string for display in the UI as `ns/fn`.
     *
     * Examples:
     * - "phel.string/contains?" -> "string/contains?"
     * - "phel\string\contains?" -> "string/contains?"
     * - "assoc"                 -> "assoc"
     */
    fun formatForDisplay(replacement: String): String {
        val parsed = parse(replacement)
        return parsed.functionName
    }
}
