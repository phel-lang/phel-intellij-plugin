package org.phellang.tools.generator

import org.phellang.tools.model.ApiFunction
import org.phellang.tools.model.PriorityRules
import org.phellang.tools.transformer.MarkdownToHtmlTransformer
import org.phellang.tools.transformer.StringEscaper
import org.phellang.tools.transformer.TailTextGenerator
import java.io.File

class KotlinCodeGenerator(private val outputDirectory: File) {

    fun generate(apiFunctions: List<ApiFunction>) {
        apiFunctions.groupBy { it.namespace }.forEach { (namespace, functions) ->
            val config = NamespaceConfig.getInfo(namespace)
            if (config != null) {
                generateNamespaceFile(config, functions)
                println("Generated ${config.fileName} with ${functions.size} functions")
            } else {
                println("Warning: Unknown namespace '$namespace' with ${functions.size} functions - skipping")
            }
        }
    }

    private fun generateNamespaceFile(config: NamespaceConfig.NamespaceInfo, functions: List<ApiFunction>) {
        val content = buildString {
            appendLine("package org.phellang.completion.data")
            appendLine()
            appendLine("import org.phellang.completion.infrastructure.PhelCompletionPriority")
            appendLine()
            appendLine("internal fun ${config.functionName}(): List<PhelFunction> = listOf(")

            functions.forEachIndexed { index, function ->
                append(PhelFunctionRenderer.render(function))
                if (index < functions.size - 1) {
                    appendLine(",")
                } else {
                    appendLine()
                }
            }

            appendLine(")")
        }

        File(outputDirectory, config.fileName).writeText(content)
    }
}

internal object PhelFunctionRenderer {

    fun render(apiFunction: ApiFunction): String {
        val priority = PriorityRules.determinePriority(apiFunction)
        val signature = apiFunction.signatures.firstOrNull() ?: ""
        val tailText = TailTextGenerator.generate(apiFunction.description)
        val summary = MarkdownToHtmlTransformer.transform(apiFunction.description)
        val example = apiFunction.meta.example?.let { StringEscaper.escapeHtml(it) }
        val deprecation = renderDeprecation(apiFunction)

        return buildString {
            appendLine("    PhelFunction(")
            appendLine("        namespace = ${StringEscaper.toKotlinString(apiFunction.namespace)},")
            appendLine("        name = ${StringEscaper.toKotlinString(apiFunction.name)},")
            appendLine("        signature = ${StringEscaper.toKotlinString(signature)},")
            appendLine("        completion = CompletionInfo(")
            appendLine("            tailText = ${StringEscaper.toKotlinString(tailText)},")
            appendLine("            priority = PhelCompletionPriority.${priority.name},")
            appendLine("        ),")
            appendLine("        documentation = DocumentationInfo(")
            appendLine("            summary = ${StringEscaper.toTripleQuotedString(summary)},")
            if (example != null) {
                appendLine("            example = ${StringEscaper.toKotlinString(example)},")
            } else {
                appendLine("            example = null,")
            }
            if (deprecation != null) {
                appendLine("            deprecation = $deprecation,")
            }
            appendLine("            links = DocumentationLinks(")
            appendLine("                github = ${StringEscaper.toKotlinString(apiFunction.githubUrl)},")
            appendLine("                docs = ${StringEscaper.toKotlinString(apiFunction.docUrl)},")
            appendLine("            ),")
            appendLine("        ),")
            append("    )")
        }
    }

    private fun renderDeprecation(apiFunction: ApiFunction): String? {
        val deprecated = apiFunction.meta.deprecated ?: return null
        val supersededBy = apiFunction.meta.supersededBy

        return if (supersededBy != null) {
            "DeprecationInfo(version = ${StringEscaper.toKotlinString(deprecated)}, replacement = ${
                StringEscaper.toKotlinString(
                    supersededBy
                )
            })"
        } else {
            "DeprecationInfo(version = ${StringEscaper.toKotlinString(deprecated)})"
        }
    }
}
