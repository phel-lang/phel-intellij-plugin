package org.phellang.inspection.deprecated

import org.phellang.registry.DeprecationInfo

object DeprecationMessageBuilder {
    fun build(functionName: String, deprecation: DeprecationInfo?): String {
        if (deprecation == null) {
            return "'$functionName' is deprecated"
        }

        // In api.json the `version` field sometimes carries a "Use xyz" message instead of a version.
        if (deprecation.version.startsWith("Use ", ignoreCase = true)) {
            val replacement = deprecation.version.removePrefix("Use ").trim()
            val displayReplacement = ReplacementParser.formatForDisplay(replacement)
            return "'$functionName' is deprecated. Use '$displayReplacement' instead"
        }

        if (deprecation.replacement != null) {
            val displayReplacement = ReplacementParser.formatForDisplay(deprecation.replacement)
            return "'$functionName' is deprecated since ${deprecation.version}. Use '$displayReplacement' instead"
        }

        return "'$functionName' is deprecated since ${deprecation.version}"
    }

    /** The replacement in `phel\namespace\function` format, or null when the deprecation names none. */
    fun extractReplacement(deprecation: DeprecationInfo?): String? {
        if (deprecation == null) return null

        if (deprecation.version.startsWith("Use ", ignoreCase = true)) {
            return deprecation.version.removePrefix("Use ").trim()
        }

        return deprecation.replacement
    }
}
