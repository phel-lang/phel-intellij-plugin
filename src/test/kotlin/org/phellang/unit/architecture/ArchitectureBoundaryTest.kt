package org.phellang.unit.architecture

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Compiler-adjacent enforcement of the module boundaries this codebase relies on.
 *
 * Kotlin's `internal` is whole-Gradle-module (the entire plugin), so it cannot stop one package from
 * importing another's internals — these rules can. Each locks in an invariant that holds today and
 * fails the build the moment a new import crosses a line, with the offending `file:line` named.
 *
 * A plain source scan rather than a library (Konsist/ArchUnit): it needs no dependency on the
 * plugin's test classpath, and it mirrors how the repo's cycle-detector already reasons about imports.
 */
class ArchitectureBoundaryTest {

    /** The generated function data under `registry/data` is reached only through the registry's API. */
    @Test
    fun `registry data is not imported outside the registry package`() {
        val offenders = mainSources()
            .filterNot { it.packageName.startsWith("$ROOT.registry") }
            .flatMap { src -> src.importsMatching("$ROOT.registry.data").map { src to it } }

        assertNoOffenders(
            offenders,
            "The generated registry data is private to `registry`. Reach it through PhelFunctionRegistry " +
                    "/ PhelArityResolver, not by importing `registry.data.*`.",
        )
    }

    /** `registry` is a leaf: it may use `language`/`core` and the platform, never a feature package. */
    @Test
    fun `registry does not import any feature package`() {
        val offenders = mainSources()
            .filter { it.packageName.startsWith("$ROOT.registry") }
            .flatMap { src -> src.importsMatchingAny(FEATURE_PACKAGES).map { src to it } }

        assertNoOffenders(
            offenders,
            "`registry` must stay a leaf — importing a feature package re-creates the cycle its " +
                    "extraction removed. Move the shared type down into `registry` or `language` instead.",
        )
    }

    /** `tools` is the build-time generator; runtime plugin code must not depend on it. */
    @Test
    fun `runtime code does not import the build-time tools package`() {
        val offenders = mainSources()
            .filterNot { it.packageName.startsWith("$ROOT.tools") }
            .flatMap { src -> src.importsMatching("$ROOT.tools").map { src to it } }

        assertNoOffenders(
            offenders,
            "`tools` runs at build time (updatePhelRegistry). Runtime code importing it would drag the " +
                    "generator into the shipped plugin.",
        )
    }

    /** The generator must not reach into runtime feature packages. */
    @Test
    fun `the tools generator does not import feature packages`() {
        val offenders = mainSources()
            .filter { it.packageName.startsWith("$ROOT.tools") }
            .flatMap { src -> src.importsMatchingAny(FEATURE_PACKAGES).map { src to it } }

        assertNoOffenders(offenders, "The build-time generator must not depend on runtime feature code.")
    }

    private fun assertNoOffenders(offenders: List<Pair<KtSource, Import>>, rule: String) {
        if (offenders.isEmpty()) return
        val detail = offenders.joinToString("\n") { (src, imp) -> "  ${src.relativePath}:${imp.line}  ${imp.fqName}" }
        assertTrue(false, "$rule\n\nOffending imports:\n$detail")
    }

    private data class Import(val fqName: String, val line: Int)

    private class KtSource(val relativePath: String, val packageName: String, private val imports: List<Import>) {
        fun importsMatching(prefix: String): List<Import> =
            imports.filter { it.fqName == prefix || it.fqName.startsWith("$prefix.") }

        fun importsMatchingAny(prefixes: List<String>): List<Import> =
            imports.filter { imp -> prefixes.any { imp.fqName == it || imp.fqName.startsWith("$it.") } }
    }

    companion object {
        private const val ROOT = "org.phellang"

        private val FEATURE_PACKAGES = listOf(
            "annotator", "completion", "editor", "inspection",
            "documentation", "actions", "inlay", "refactoring", "syntax",
        ).map { "$ROOT.$it" }

        private val MAIN_ROOT = File("src/main/kotlin")

        private fun mainSources(): List<KtSource> {
            assertTrue(MAIN_ROOT.isDirectory, "expected to run from the project root; ${MAIN_ROOT.absolutePath} not found")
            return MAIN_ROOT.walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .map { parse(it) }
                .toList()
        }

        private fun parse(file: File): KtSource {
            var packageName = ""
            val imports = mutableListOf<Import>()
            file.readLines().forEachIndexed { i, raw ->
                val line = raw.trim()
                when {
                    line.startsWith("package ") -> packageName = line.removePrefix("package ").trim()
                    line.startsWith("import ") -> {
                        val fq = line.removePrefix("import ").substringBefore(" as ").trim()
                        imports += Import(fq, i + 1)
                    }
                }
            }
            return KtSource(file.path, packageName, imports)
        }
    }
}
