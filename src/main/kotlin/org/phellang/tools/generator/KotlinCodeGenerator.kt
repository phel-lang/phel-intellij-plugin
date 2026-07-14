package org.phellang.tools.generator

import org.phellang.tools.model.ApiFunction
import org.phellang.tools.model.PriorityRules
import org.phellang.tools.transformer.MarkdownToHtmlTransformer
import org.phellang.tools.transformer.StringEscaper
import org.phellang.tools.transformer.TailTextGenerator
import java.io.File

class KotlinCodeGenerator(private val outputDirectory: File) {

    fun generate(apiFunctions: List<ApiFunction>) {
        val filtered = apiFunctions.filterNot { it.signatures.isEmpty() && it.description.isBlank() }
        val skipped = apiFunctions.size - filtered.size
        if (skipped > 0) println("Skipping $skipped function(s) with no signature and no doc")
        filtered.groupBy { it.namespace }.forEach { (namespace, functions) ->
            if (namespace == "core") {
                generateCoreNamespace(functions)
                return@forEach
            }

            val config = NamespaceConfig.getInfo(namespace)
            if (config != null) {
                val targetDir = config.subfolder?.let { File(outputDirectory, it).apply { mkdirs() } }
                    ?: outputDirectory
                val packageName = config.subfolder?.let { "$ROOT_PACKAGE.$it" } ?: ROOT_PACKAGE
                val extraImports = SHARED_DATA_IMPORTS
                writeFunctionsFile(
                    file = File(targetDir, config.fileName),
                    packageName = packageName,
                    functionName = config.functionName,
                    functions = functions,
                    extraImports = extraImports
                )
                val displayName = config.subfolder?.let { "$it/${config.fileName}" } ?: config.fileName
                println("Generated $displayName with ${functions.size} functions")
            } else {
                println("Warning: Unknown namespace '$namespace' with ${functions.size} functions - skipping")
            }
        }
    }

    private fun generateCoreNamespace(functions: List<ApiFunction>) {
        val coreDir = File(outputDirectory, "core").apply { mkdirs() }

        coreDir.listFiles { f -> f.name.endsWith(".kt") }?.forEach { it.delete() }

        val buckets = functions.groupBy { extractCoreBucket(it.githubUrl) }.toSortedMap()

        buckets.forEach { (bucket, bucketFns) ->
            val functionName = "registerCore${toPascalCase(bucket)}Functions"
            writeFunctionsFile(
                file = File(coreDir, "$functionName.kt"),
                packageName = CORE_PACKAGE,
                functionName = functionName,
                functions = bucketFns,
                extraImports = SHARED_DATA_IMPORTS
            )
            println("Generated core/$functionName.kt with ${bucketFns.size} functions")
        }

        val aggregatorFunctionNames = buckets.keys.map { "registerCore${toPascalCase(it)}Functions" }
        writeCoreAggregator(aggregatorFunctionNames)
        println(
            "Generated registerCoreFunctions.kt (aggregator for ${buckets.size} buckets, ${functions.size} total)"
        )
    }

    private fun writeCoreAggregator(subFunctionNames: List<String>) {
        val content = buildString {
            appendLine("package $ROOT_PACKAGE")
            appendLine()
            appendLine("import org.phellang.registry.PhelFunction")
            subFunctionNames.forEach { appendLine("import $CORE_PACKAGE.$it") }
            appendLine()
            appendLine("internal fun registerCoreFunctions(): List<PhelFunction> =")
            subFunctionNames.forEachIndexed { index, name ->
                val prefix = if (index == 0) "    " else "        "
                val suffix = if (index < subFunctionNames.size - 1) " +" else ""
                appendLine("$prefix$name()$suffix")
            }
        }
        writeFile(File(outputDirectory, "registerCoreFunctions.kt"), content)
    }

    private fun writeFunctionsFile(
        file: File,
        packageName: String,
        functionName: String,
        functions: List<ApiFunction>,
        extraImports: List<String>
    ) {
        val content = buildString {
            appendLine("package $packageName")
            appendLine()
            extraImports.forEach { appendLine("import $it") }
            if (extraImports.isNotEmpty()) appendLine()
            appendLine("import org.phellang.registry.PhelCompletionPriority")
            appendLine()
            appendLine("internal fun $functionName(): List<PhelFunction> = listOf(")
            functions.forEachIndexed { index, function ->
                append(PhelFunctionRenderer.render(function))
                if (index < functions.size - 1) appendLine(",") else appendLine()
            }
            appendLine(")")
        }
        writeFile(file, content)
    }

    private fun writeFile(file: File, content: String) {
        file.parentFile?.mkdirs()
        file.writeText(content)
    }

    private fun extractCoreBucket(githubUrl: String): String {
        Regex("""src/phel/core/([^/.]+)\.phel""").find(githubUrl)?.let {
            return it.groupValues[1]
        }
        return "root"
    }

    private fun toPascalCase(bucket: String): String =
        bucket.split('-', '_')
            .filter { it.isNotEmpty() }
            .joinToString("") { it.replaceFirstChar(Char::uppercase) }

    companion object {
        private const val ROOT_PACKAGE = "org.phellang.registry.data"
        private const val CORE_PACKAGE = "org.phellang.registry.data.core"
        private val SHARED_DATA_IMPORTS = listOf(
            "org.phellang.registry.CompletionInfo",
            "org.phellang.registry.DocumentationInfo",
            "org.phellang.registry.DocumentationLinks",
            "org.phellang.registry.DeprecationInfo",
            "org.phellang.registry.PhelFunction"
        )
    }
}

internal object PhelFunctionRenderer {

    fun render(apiFunction: ApiFunction): String {
        val priority = PriorityRules.determinePriority(apiFunction)
        // Join every arity (newline-separated) so multi-arity fns like `conj` keep all
        // their shapes: the arity-mismatch inspection parses these via PhelArity.parseAll,
        // and the documentation provider renders the newlines as <br />.
        val signature = apiFunction.signatures.joinToString("\n")
        val tailText = TailTextGenerator.generate(apiFunction.description)
        val summary = MarkdownToHtmlTransformer.transform(apiFunction.description)
        val example = apiFunction.meta?.example?.let { StringEscaper.escapeHtml(it) }
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
        val deprecated = apiFunction.meta?.deprecated ?: return null
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
