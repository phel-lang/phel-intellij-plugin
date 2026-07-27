package org.phellang.unit.samples

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.editor.colorsettings.PhelDemoTextProvider
import java.io.File

/**
 * The Phel snippets this plugin shows to users must be valid, current Phel.
 *
 * They are plain string constants and HTML files, so nothing compiles or parses them — which is how
 * the Code Style preview came to use `(ns app\example …)` with `(:require phel\str :as str)`: a
 * separator deprecated since Phel 0.35, a namespace that does not exist, and a `str/trim` call that
 * therefore resolved to nothing. The plugin ships an inspection reporting exactly that separator, so
 * its own preview would have been flagged by its own inspection.
 */
class PhelShippedSamplesTest {

    /**
     * `(ns a\b)` and `(:require x\y)`, which are the deprecated spellings.
     *
     * `:use` is deliberately not matched: PHP class names in `(:use \DateTime)` really are
     * backslash-separated, and flagging those would be wrong.
     */
    private val deprecatedSeparator =
        Regex("""\(ns\s+[A-Za-z][\w-]*\\|:require\s+[A-Za-z][\w-]*\\""")

    private fun assertNoBackslashNamespace(label: String, sample: String) {
        val offenders = deprecatedSeparator.findAll(sample).map { it.value }.toList()

        assertTrue(
            offenders.isEmpty(),
        ) { "$label uses the backslash namespace separator, deprecated since Phel 0.35: $offenders" }
    }

    @Test
    fun `the code style preview uses current Phel`() {
        val sample = codeStyleSample()

        assertNoBackslashNamespace("The Code Style preview", sample)
        assertTrue(sample.contains("(:require phel.string)")) { "should require phel.string, got:\n$sample" }
        assertTrue(sample.contains("string/trim")) { "should call string/trim, got:\n$sample" }
    }

    /** `str` is core's string-building function; `str/…` is not a namespace the registry knows. */
    @Test
    fun `the code style preview does not use the non-existent str namespace`() {
        assertFalse(codeStyleSample().contains("str/")) {
            "`str/` is not a Phel namespace — the registry declares `string`"
        }
    }

    @Test
    fun `the colour scheme demo text uses current Phel`() {
        assertNoBackslashNamespace("The Color Scheme demo text", PhelDemoTextProvider().getDemoText())
    }

    /**
     * Every inspection description except the one whose whole subject is the deprecated form —
     * `PhelBackslashNamespace.html` shows `(ns my-app\core …)` on purpose, as the "before" example.
     */
    @Test
    fun `inspection descriptions use current Phel`() {
        val descriptions = File("src/main/resources/inspectionDescriptions")
            .listFiles { file -> file.extension == "html" && file.nameWithoutExtension != "PhelBackslashNamespace" }
            .orEmpty()

        assertTrue(descriptions.isNotEmpty()) { "expected the inspection descriptions to be found" }

        descriptions.forEach { assertNoBackslashNamespace(it.name, it.readText()) }
    }

    /** The one file that is meant to contain it, so the exclusion above cannot quietly stop mattering. */
    @Test
    fun `the backslash inspection still documents the deprecated form it reports`() {
        val description = File("src/main/resources/inspectionDescriptions/PhelBackslashNamespace.html").readText()

        assertTrue(deprecatedSeparator.containsMatchIn(description)) {
            "this description must keep showing the deprecated spelling as its example"
        }
    }

    private fun codeStyleSample(): String =
        org.phellang.editor.format.PhelLanguageCodeStyleSettingsProvider()
            .getCodeSample(com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType.BLANK_LINES_SETTINGS)
}
