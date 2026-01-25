package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

data class CompletionInfo(
    val tailText: String,
    val priority: PhelCompletionPriority,
)

data class DeprecationInfo(
    val version: String,
    val replacement: String? = null,
)

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
        signature.takeIf(String::isNotBlank)?.let { append("<code>$it</code><br /><br />") }
        append(summary)
        append("<br />")

        example?.let { code ->
            append("<br /><pre><code>")
            append(code)
            append("</code></pre>")
        }
        append("<br />")
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
