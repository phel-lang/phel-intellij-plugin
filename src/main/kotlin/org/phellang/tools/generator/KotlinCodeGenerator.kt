package org.phellang.tools.generator

import org.phellang.tools.model.ApiFunction
import org.phellang.tools.model.PriorityRules
import java.io.File

class KotlinCodeGenerator(private val outputDirectory: File) {
    private val namespaceConfig = mapOf(
        "base64" to NamespaceInfo("registerBase64Functions", "registerBase64Functions.kt"),
        "core" to NamespaceInfo("registerCoreFunctions", "registerCoreFunctions.kt"),
        "debug" to NamespaceInfo("registerDebugFunctions", "registerDebugFunctions.kt"),
        "html" to NamespaceInfo("registerHtmlFunctions", "registerHtmlFunctions.kt"),
        "http" to NamespaceInfo("registerHttpFunctions", "registerHttpFunctions.kt"),
        "json" to NamespaceInfo("registerJsonFunctions", "registerJsonFunctions.kt"),
        "mock" to NamespaceInfo("registerMockFunctions", "registerMockFunctions.kt"),
        "php" to NamespaceInfo("registerPhpInteropFunctions", "registerPhpInteropFunctions.kt"),
        "repl" to NamespaceInfo("registerReplFunctions", "registerReplFunctions.kt"),
        "str" to NamespaceInfo("registerStringFunctions", "registerStringFunctions.kt"),
        "test" to NamespaceInfo("registerTestFunctions", "registerTestFunctions.kt")
    )

    private data class NamespaceInfo(val functionName: String, val fileName: String)

    fun generate(apiFunctions: List<ApiFunction>) {
        // Group functions by namespace
        val functionsByNamespace = apiFunctions.groupBy { it.namespace }

        // Generate a file for each namespace
        functionsByNamespace.forEach { (namespace, functions) ->
            val config = namespaceConfig[namespace]
            if (config != null) {
                generateNamespaceFile(config, functions)
                println("Generated ${config.fileName} with ${functions.size} functions")
            } else {
                println("Warning: Unknown namespace '$namespace' with ${functions.size} functions - skipping")
            }
        }
    }

    private fun generateNamespaceFile(config: NamespaceInfo, functions: List<ApiFunction>) {
        val content = buildString {
            appendLine("package org.phellang.completion.data")
            appendLine()
            appendLine("import org.phellang.completion.infrastructure.PhelCompletionPriority")
            appendLine()
            appendLine("internal fun ${config.functionName}(): List<PhelFunction> = listOf(")

            functions.forEachIndexed { index, function ->
                append(generatePhelFunction(function))
                if (index < functions.size - 1) {
                    appendLine(",")
                } else {
                    appendLine()
                }
            }

            appendLine(")")
        }

        val outputFile = File(outputDirectory, config.fileName)
        outputFile.writeText(content)
    }

    private fun generatePhelFunction(apiFunction: ApiFunction): String {
        val priority = PriorityRules.determinePriority(apiFunction)
        val signature = apiFunction.signatures.firstOrNull() ?: ""
        val tailText = generateTailText(apiFunction.description)
        val summary = generateSummary(apiFunction.description)
        val example = apiFunction.meta.example?.escapeHtml()
        val deprecation = generateDeprecation(apiFunction)

        return buildString {
            appendLine("    PhelFunction(")
            appendLine("        namespace = ${apiFunction.namespace.toKotlinString()},")
            appendLine("        name = ${apiFunction.name.toKotlinString()},")
            appendLine("        signature = ${signature.toKotlinString()},")
            appendLine("        completion = CompletionInfo(")
            appendLine("            tailText = ${tailText.toKotlinString()},")
            appendLine("            priority = PhelCompletionPriority.${priority.name},")
            appendLine("        ),")
            appendLine("        documentation = DocumentationInfo(")
            appendLine("            summary = ${summary.toTripleQuotedString()},")
            if (example != null) {
                appendLine("            example = ${example.toKotlinString()},")
            } else {
                appendLine("            example = null,")
            }
            if (deprecation != null) {
                appendLine("            deprecation = $deprecation,")
            }
            appendLine("            links = DocumentationLinks(")
            appendLine("                github = ${apiFunction.githubUrl.toKotlinString()},")
            appendLine("                docs = ${apiFunction.docUrl.toKotlinString()},")
            appendLine("            ),")
            appendLine("        ),")
            append("    )")
        }
    }

    /**
     * Generates tail text for code completion (short description).
     */
    private fun generateTailText(description: String): String {
        // Take first sentence, limit to ~100 characters
        val cleaned = description.replace(Regex("```[\\s\\S]*?```"), "") // Remove code blocks
            .replace(Regex("`([^`]+)`"), "$1") // Remove inline code formatting
            .replace("\n", " ").replace(Regex("\\s+"), " ").trim()

        val firstSentence = cleaned.split(Regex("[.!?]")).firstOrNull()?.trim() ?: cleaned

        return if (firstSentence.length > 100) {
            firstSentence.take(97) + "..."
        } else {
            firstSentence
        }
    }

    /**
     * Generates HTML summary for documentation popup.
     * Converts Markdown to HTML:
     * - Code blocks (```phel ... ```) become <pre><code>...</code></pre>
     * - Inline code (`...`) becomes <code>...</code>
     * - Links [text](url) become <a href="url">text</a>
     * - Bold **text** or __text__ becomes <strong>text</strong>
     * - Italic *text* or _text_ becomes <em>text</em>
     * - Newlines outside code blocks become <br />
     */
    private fun generateSummary(description: String): String {
        val codeBlockPlaceholders = mutableMapOf<String, String>()
        var placeholderIndex = 0

        // First, extract and preserve code blocks with placeholders
        var result = description.replace(Regex("```(?:phel)?\\s*\\n([\\s\\S]*?)```")) { match ->
            val code = match.groupValues[1].trimEnd()
            val placeholder = "___CODE_BLOCK_${placeholderIndex++}___"
            codeBlockPlaceholders[placeholder] = "<pre><code>\n$code\n</code></pre>"
            placeholder
        }

        // Convert inline code `...` to <code>...</code>
        result = result.replace(Regex("`([^`]+)`"), "<code>$1</code>")

        // Convert markdown links [text](url) to <a href="url">text</a>
        result = result.replace(Regex("\\[([^]]+)]\\(([^)]+)\\)"), "<a href=\"$2\">$1</a>")

        // Convert bold **text** or __text__ to <strong>text</strong>
        result = result.replace(Regex("\\*\\*([^*]+)\\*\\*"), "<strong>$1</strong>")
        result = result.replace(Regex("__([^_]+)__"), "<strong>$1</strong>")

        // Convert italic *text* or _text_ to <em>text</em> (but not inside words)
        result = result.replace(Regex("(?<![\\w*])\\*([^*]+)\\*(?![\\w*])"), "<em>$1</em>")
        result = result.replace(Regex("(?<![\\w_])_([^_]+)_(?![\\w_])"), "<em>$1</em>")

        // Normalize multiple newlines to double newlines (paragraph breaks)
        result = result.replace(Regex("\\n{3,}"), "\n\n")

        // Convert newlines to <br /> tags (outside of code blocks)
        result = result.replace("\n\n", "<br /><br />\n")
        result = result.replace("\n", "<br />\n")

        // Clean up excessive <br /> tags
        result = result.replace(Regex("(<br />\\s*){3,}"), "<br /><br />\n")

        // Restore code blocks
        codeBlockPlaceholders.forEach { (placeholder, codeBlock) ->
            result = result.replace(placeholder, codeBlock)
        }

        // Clean up any leading/trailing whitespace
        return result.trim()
    }

    private fun generateDeprecation(apiFunction: ApiFunction): String? {
        val deprecated = apiFunction.meta.deprecated ?: return null
        val supersededBy = apiFunction.meta.supersededBy

        return if (supersededBy != null) {
            "DeprecationInfo(version = ${deprecated.toKotlinString()}, replacement = ${supersededBy.toKotlinString()})"
        } else {
            "DeprecationInfo(version = ${deprecated.toKotlinString()})"
        }
    }

    private fun String.escapeHtml(): String {
        return this.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    }

    private fun String.toKotlinString(): String {
        val escaped = this.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r")
            .replace("\t", "\\t").replace("$", "\\$")
        return "\"$escaped\""
    }

    private fun String.toTripleQuotedString(): String {
        // For simple strings without special characters, use regular quotes
        if (!this.contains("\"") && !this.contains("\n") && !this.contains("<") && this.length < 80) {
            return this.toKotlinString()
        }

        // For complex strings with HTML, quotes, or newlines, use triple quotes
        val escaped = this.replace("$", "\${'$'}")
        return "\"\"\"\n$escaped\n\"\"\""
    }
}
