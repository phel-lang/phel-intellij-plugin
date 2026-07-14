package org.phellang.tools.generator

import java.io.File

/**
 * Regenerates the hand-maintained wiring in [Namespace.kt] and [PhelFunctionRegistry.kt] so that adding a
 * namespace to [NamespaceConfig] is enough — no manual edits, no compile-time bootstrap deadlock.
 *
 * Only the marked regions are rewritten; everything else (the curated alias map, registry helper logic) is
 * left untouched. [NamespaceConfig] is the single source of truth.
 */
class RegistryWiringGenerator(private val dataDirectory: File) {

    private data class Wiring(
        val enumName: String,
        val functionName: String,
        val import: String?,
    )

    fun generate() {
        val wirings = NamespaceConfig.all().values
            .map { info ->
                Wiring(
                    enumName = enumName(info.functionName),
                    functionName = info.functionName,
                    import = info.subfolder?.let { "$DATA_PACKAGE.$it.${info.functionName}" }
                        ?: "$DATA_PACKAGE.${info.functionName}",
                )
            }
            .sortedBy { it.enumName }

        updateNamespaceEnum(wirings)
        updateRegistry(wirings)
    }

    private fun updateNamespaceEnum(wirings: List<Wiring>) {
        val file = File(dataDirectory, "Namespace.kt")
        val body = wirings.joinToString("\n") { "    ${it.enumName}," }
        file.writeText(replaceRegion(file.readText(), ENUM_MARKER, body))
        println("Updated Namespace.kt enum (${wirings.size} namespaces)")
    }

    private fun updateRegistry(wirings: List<Wiring>) {
        val file = File(dataDirectory, "PhelFunctionRegistry.kt")
        val imports = wirings.mapNotNull { it.import }.sorted().joinToString("\n") { "import $it" }
        val init = wirings.joinToString("\n") {
            "        functions[Namespace.${it.enumName}] = ${it.functionName}()"
        }
        var text = file.readText()
        text = replaceRegion(text, IMPORTS_MARKER, imports)
        text = replaceRegion(text, INIT_MARKER, init)
        file.writeText(text)
        println("Updated PhelFunctionRegistry.kt (${wirings.size} namespaces)")
    }

    /**
     * Replaces the lines between `// region <marker>` and `// endregion <marker>` (markers kept) with [body].
     */
    private fun replaceRegion(text: String, marker: String, body: String): String {
        val lines = text.lines()
        val start = lines.indexOfFirst { it.trimStart().startsWith("// region $marker") }
        val end = lines.indexOfFirst { it.trimStart().startsWith("// endregion $marker") }
        require(start >= 0) { "Missing '// region $marker' marker" }
        require(end > start) { "Missing or misplaced '// endregion $marker' marker" }
        return (lines.subList(0, start + 1) + body + lines.subList(end, lines.size)).joinToString("\n")
    }

    /** `registerPhpInteropFunctions` -> `PHP_INTEROP`, `registerHttpClientFunctions` -> `HTTP_CLIENT`. */
    private fun enumName(functionName: String): String =
        functionName.removePrefix("register").removeSuffix("Functions")
            .replace(Regex("(?<=[a-z0-9])(?=[A-Z])"), "_")
            .uppercase()

    companion object {
        private const val DATA_PACKAGE = "org.phellang.registry.data"
        private const val ENUM_MARKER = "GENERATED — updatePhelRegistry"
        private const val IMPORTS_MARKER = "GENERATED IMPORTS — updatePhelRegistry"
        private const val INIT_MARKER = "GENERATED INIT — updatePhelRegistry"
    }
}
