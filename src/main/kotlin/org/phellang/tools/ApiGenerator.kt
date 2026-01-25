package org.phellang.tools

import org.phellang.tools.generator.KotlinCodeGenerator
import org.phellang.tools.http.ApiFetcher
import java.io.File
import kotlin.system.exitProcess

/**
 * Main entry point for the Phel Function Registry generator.
 *
 * This tool fetches the official Phel API JSON from https://phel-lang.org/api.json and regenerates
 * all register*Functions.kt files in the completion/data directory.
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
        // Determine output directory
        val outputDir = determineOutputDirectory()
        println("Output directory: ${outputDir.absolutePath}")
        println()

        // Fetch API data
        val fetcher = ApiFetcher()
        val apiFunctions = fetcher.fetchApiFunctions()

        // Print statistics by namespace
        println()
        println("Functions by namespace:")
        apiFunctions.groupBy { it.namespace }.toSortedMap().forEach { (namespace, functions) ->
            println("  $namespace: ${functions.size} functions")
        }
        println()

        // Generate Kotlin files
        println("Generating Kotlin files...")
        val generator = KotlinCodeGenerator(outputDir)
        generator.generate(apiFunctions)

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
    val possiblePaths = listOf(
        // When run from project root
        "src/main/kotlin/org/phellang/completion/data",
        // When run from Gradle with project dir
        System.getProperty("user.dir")?.let { "$it/src/main/kotlin/org/phellang/completion/data" },
        // Absolute path fallback
        System.getenv("PROJECT_DIR")?.let { "$it/src/main/kotlin/org/phellang/completion/data" })

    for (path in possiblePaths.filterNotNull()) {
        val dir = File(path)
        if (dir.exists() && dir.isDirectory) {
            return dir
        }
    }

    // If not found, try to create it relative to current directory
    val defaultDir = File("src/main/kotlin/org/phellang/completion/data")
    if (!defaultDir.exists()) {
        throw IllegalStateException(
            "Could not find output directory. " + "Please run this tool from the project root directory."
        )
    }

    return defaultDir
}
