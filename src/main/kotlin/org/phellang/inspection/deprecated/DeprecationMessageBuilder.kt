package org.phellang.inspection.deprecated

import org.phellang.registry.DeprecationInfo

object DeprecationMessageBuilder {
    fun build(functionName: String, deprecation: DeprecationInfo?): String {
        if (deprecation == null) {
            return "'$functionName' is deprecated"
        }

        return buildString {
            append("'$functionName' is deprecated")
            deprecation.displayVersion?.let { append(" since ").append(it) }
            deprecation.displayReplacement?.let { append(". Use '").append(it).append("' instead") }
        }
    }

    /** The replacement in `phel\namespace\function` format, or null when the deprecation names none. */
    fun extractReplacement(deprecation: DeprecationInfo?): String? = deprecation?.rawReplacement
}
