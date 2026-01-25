package org.phellang.inspection.deprecated

import org.phellang.completion.data.DeprecationInfo

object DeprecationMessageBuilder {

    fun build(functionName: String, deprecation: DeprecationInfo?): String {
        if (deprecation == null) {
            return "'$functionName' is deprecated"
        }

        // Handle cases where version contains "Use xyz" message
        if (deprecation.version.startsWith("Use ", ignoreCase = true)) {
            val replacement = deprecation.version.removePrefix("Use ").trim()
            val displayReplacement = ReplacementParser.formatForDisplay(replacement)
            return "'$functionName' is deprecated. Use '$displayReplacement' instead"
        }

        // Standard case with replacement
        if (deprecation.replacement != null) {
            val displayReplacement = ReplacementParser.formatForDisplay(deprecation.replacement)
            return "'$functionName' is deprecated since ${deprecation.version}. Use '$displayReplacement' instead"
        }

        // Version only
        return "'$functionName' is deprecated since ${deprecation.version}"
    }

    /**
     * Extracts the replacement function from deprecation info.
     * 
     * @param deprecation The deprecation info
     * @return The replacement string (in phel\namespace\function format), or null if none
     */
    fun extractReplacement(deprecation: DeprecationInfo?): String? {
        if (deprecation == null) return null

        // Extract from "Use xyz" message
        if (deprecation.version.startsWith("Use ", ignoreCase = true)) {
            return deprecation.version.removePrefix("Use ").trim()
        }

        // Standard replacement field
        return deprecation.replacement
    }
}
