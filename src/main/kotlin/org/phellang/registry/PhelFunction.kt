package org.phellang.registry

data class CompletionInfo(
    val tailText: String,
    val priority: PhelCompletionPriority,
)

data class DeprecationInfo(
    val version: String,
    val replacement: String? = null,
) {
    // In api.json the `version` field sometimes carries a "Use xyz" message instead of a version.
    private val versionCarriesUseMessage: Boolean
        get() = version.startsWith("Use ", ignoreCase = true)

    /** The version string, or null when the `version` field actually carries a "Use xyz" message. */
    val displayVersion: String?
        get() = version.takeUnless { versionCarriesUseMessage }

    /** The replacement in its raw `phel\ns\fn` form (from the field or a "Use xyz" version), or null. */
    val rawReplacement: String?
        get() = if (versionCarriesUseMessage) version.removePrefix("Use ").trim() else replacement

    /** The replacement formatted for display as `ns/fn`, or null when the deprecation names none. */
    val displayReplacement: String?
        get() = rawReplacement?.let(ReplacementParser::formatForDisplay)
}

data class DocumentationLinks(
    val github: String = "",
    val docs: String = "",
)

data class DocumentationInfo(
    val summary: String,
    val example: String? = null,
    val deprecation: DeprecationInfo? = null,
    val links: DocumentationLinks = DocumentationLinks(),
) {
    fun toHtml(signature: String): String = buildString {
        append("<br />")
        // Multi-arity signatures are newline-separated (see KotlinCodeGenerator); render each
        // arity on its own line, mirroring the project-symbol popup (PhelDocHtml.projectSymbol).
        signature.takeIf(String::isNotBlank)
            ?.let { append("<code>${it.replace("\n", "<br />")}</code><br /><br />") }
        // Deprecation notice. Driven by DeprecationInfo's display helpers so the popup and the
        // deprecated-function inspection (DeprecationMessageBuilder) always name the same version
        // and replacement.
        deprecation?.let { appendDeprecationNotice(it) }
        append(summary)
        append("<br />")

        example?.let { code ->
            append("<br /><pre><code>")
            append(code)
            append("</code></pre>")
        }
        append("<br />")
    }

    private fun StringBuilder.appendDeprecationNotice(deprecation: DeprecationInfo) {
        append("<b>&#9888; Deprecated")
        deprecation.displayVersion?.let { append(" since ").append(it) }
        append("</b>")
        deprecation.displayReplacement?.let { append(". Use <code>").append(it).append("</code> instead") }
        append(".<br /><br />")
    }
}

/**
 * This class consolidates all information needed for both:
 * - **Code Completion**: suggestions while typing (Ctrl+Space)
 * - **Quick Documentation**: hover/popup documentation (Ctrl+Q / F1)
 *
 * @property namespace The Phel namespace this function belongs to (e.g., "core", "str")
 * @property name The fully qualified function name (e.g., "core/map", "str/join")
 * @property signature The function signature in Phel syntax (e.g., "(map f xs)")
 * @property completion Data for the Code Completion popup
 * @property documentation Data for the Quick Documentation popup
 */
data class PhelFunction(
    val namespace: String,
    val name: String,
    val signature: String,
    val completion: CompletionInfo,
    val documentation: DocumentationInfo,
) {
    val isDeprecated: Boolean
        get() = documentation.deprecation != null

    fun toHtmlDocumentation(): String = documentation.toHtml(signature)
}
