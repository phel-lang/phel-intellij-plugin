package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

data class FunctionMeta(
    val example: String? = null,
    val deprecatedVersion: String? = null,
    val supersededBy: String? = null,
)

/**
 * Based on official Phel API documentation: https://phel-lang.org/documentation/api/
 */
data class DataFunction(
    val namespace: String,
    val name: String,
    val doc: String,
    val signature: String,
    val description: String,
    val githubUrl: String,
    val docUrl: String,
    val meta: FunctionMeta = FunctionMeta(),
    val priority: PhelCompletionPriority,
) {
    fun toHtmlDocumentation(): String = buildString {
        append("<br />")
        signature.takeIf(String::isNotBlank)?.let { append("<code>$it</code><br /><br />") }
        append(doc)
        append("<br />")

        meta.example?.let { code ->
            append("<br /><pre><code>")
            append(code)
            append("</code></pre>")
        }

        meta.deprecatedVersion?.let { version ->
            append("<br /><b>Deprecated since $version:</b> ")
            meta.supersededBy?.let { replacement ->
                append("Use <code>$replacement</code> instead.")
            } ?: append("This function is deprecated.")
        }
        append("<br />")
    }
}
