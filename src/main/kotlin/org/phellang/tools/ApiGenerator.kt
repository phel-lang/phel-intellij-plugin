package org.phellang.tools

import org.phellang.tools.generator.KotlinCodeGenerator
import org.phellang.tools.generator.RegistryWiringGenerator
import org.phellang.tools.http.ApiFetcher
import java.io.File
import kotlin.system.exitProcess

/**
 * Main entry point for the Phel Function Registry generator.
 *
 * This tool fetches the official Phel API JSON from https://phel-lang.org/api.json and regenerates
 * all register*Functions.kt files in the registry directory.
 *
 * Usage:
 *   ./gradlew updatePhelRegistry
 *
 * Or run directly from IntelliJ using the "Update Phel Registry" run configuration.
 */
fun main() {
    println("=".repeat(60))
    println("Phel Function Registry Generator")
    println("=".repeat(60))
    println()

    try {
        val outputDir = determineOutputDirectory()
        println("Output directory: ${outputDir.absolutePath}")
        println()

        val fetcher = ApiFetcher()
        val apiFunctions = fetcher.fetchApiFunctions()

        println()
        println("Functions by namespace:")
        apiFunctions.groupBy { it.namespace }.toSortedMap().forEach { (namespace, functions) ->
            println("  $namespace: ${functions.size} functions")
        }
        println()

        println("Generating Kotlin files...")
        val dataDir = File(outputDir, "data").apply { mkdirs() }
        val generator = KotlinCodeGenerator(dataDir)
        generator.generate(apiFunctions)

        // Sync the hand-wired enum + registry to NamespaceConfig (kills the bootstrap deadlock).
        RegistryWiringGenerator(outputDir).generate()

        println()
        println("=".repeat(60))
        println("Generation complete!")
        println("Total functions processed: ${apiFunctions.size}")
        println("=".repeat(60))

    } catch (e: Exception) {
        System.err.println("Error: ${e.message}")
        e.printStackTrace()
        exitProcess(1)
    }
}

private fun determineOutputDirectory(): File {
    // The updatePhelRegistry Gradle task pins workingDir to the project root. Anchor on a file the
    // generator never writes, so a wiped registry/data still resolves instead of failing the guard.
    if (!File("src/main/kotlin/org/phellang").isDirectory) {
        throw IllegalStateException(
            "Could not find output directory. Please run this tool from the project root directory."
        )
    }
    return File("src/main/kotlin/org/phellang/registry").apply { mkdirs() }
}
