package org.phellang.tools

import org.phellang.tools.generator.PhpDocParser
import org.phellang.tools.generator.PhpRegistryGenerator
import org.phellang.tools.http.PhpDocFetcher
import java.io.File
import kotlin.system.exitProcess

/**
 * Regenerates `registry/PhpNativeFunctions.kt` from the official PHP documentation (php/doc-en).
 *
 * Usage: `./gradlew updatePhpRegistry`
 *
 * Scope is the core extensions Phel code actually calls (see [CORE_EXTENSIONS]); widen coverage by
 * adding an extension directory name. This is separate from `updatePhelRegistry`, which owns the
 * api.json-driven files under `registry/data/`.
 */
private val CORE_EXTENSIONS = setOf(
    "strings", "array", "math", "json", "var", "ctype",
    "datetime", "filesystem", "pcre", "url", "funchand", "classobj",
)

fun main() {
    println("=".repeat(60))
    println("PHP Native Function Registry Generator (php/doc-en)")
    println("=".repeat(60))

    try {
        val registryDir = registryDirectory()

        val snapshot = PhpDocFetcher(CORE_EXTENSIONS).fetchCoreFunctions()

        val functions = snapshot.functions
            .mapNotNull { PhpDocParser.parse(it.xml, it.extension) }
            // A handful of names appear in more than one extension's docs; keep the first.
            .distinctBy { it.name }
            .sortedBy { it.name }
        println("Parsed ${functions.size} functions")

        PhpRegistryGenerator(registryDir).generate(functions, snapshot.ref)

        println("=".repeat(60))
        println("Done. ${functions.size} native PHP functions generated.")
        println("=".repeat(60))
    } catch (e: Exception) {
        System.err.println("Error: ${e.message}")
        e.printStackTrace()
        exitProcess(1)
    }
}

private fun registryDirectory(): File {
    val dir = File("src/main/kotlin/org/phellang/registry")
    check(dir.isDirectory) { "Run from the project root: ${dir.absolutePath} not found" }
    return dir
}
