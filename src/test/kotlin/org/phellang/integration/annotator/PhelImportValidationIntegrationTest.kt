package org.phellang.integration.annotator

import com.intellij.lang.annotation.HighlightSeverity
import org.phellang.inspection.PhelUnusedImportInspection
import org.phellang.integration.PhelIntegrationTestCase

/**
 * End-to-end checks for the `(:require ...)` warnings.
 *
 * These previously had *no* real coverage: the unit tests that claimed to test them built a local
 * data class and asserted on that, never invoking a validator. The precedence rules below are the
 * substance — a duplicate import must not also be reported as unused, while an import that is both
 * unknown and unused must produce both warnings.
 *
 * The unused-import half is now `PhelUnusedImportInspection` rather than an annotation, so it is
 * enabled explicitly below. Keeping these assertions end-to-end is the point: the precedence rule
 * now spans an annotator and an inspection, and only the rendered result proves it still holds.
 */
class PhelImportValidationIntegrationTest : PhelIntegrationTestCase() {

    fun testUnusedImportIsReported() {
        val warnings = warningsFor(
            """
            (ns app\m
              (:require phel\str :as s))

            (defn f [] 1)
            """.trimIndent()
        )

        assertTrue("expected an unused-import warning, got: $warnings", warnings.any { it.contains("Unused import") })
    }

    fun testUsedImportIsNotReported() {
        val warnings = warningsFor(
            """
            (ns app\m
              (:require phel\str :as s))

            (defn f [t] (s/upper-case t))
            """.trimIndent()
        )

        assertTrue("a used import must not warn, got: $warnings", warnings.none { it.contains("Unused import") })
    }

    fun testDuplicateImportIsReportedOnce_andNotAlsoAsUnused() {
        val warnings = warningsFor(
            """
            (ns app\m
              (:require phel\str)
              (:require phel\str))

            (defn f [] 1)
            """.trimIndent()
        )

        assertTrue("expected a duplicate-import warning, got: $warnings", warnings.any { it.contains("Duplicate import") })

        // The precedence rule: a duplicate is trivially also unused, but saying both on the same
        // symbol is noise. The duplicate warning suppresses the unused one.
        val duplicates = warnings.filter { it.contains("Duplicate import") }
        assertEquals("duplicate should be reported once: $warnings", 1, duplicates.size)

        // The unused check lives in an inspection now, so this precedence spans two extension
        // points. The duplicated copy must still be silent about being unused; the original above
        // it is reported as unused exactly as before.
        val unused = warnings.filter { it.contains("Unused import") }
        assertEquals("the duplicate must not also be reported as unused: $warnings", 1, unused.size)
    }

    fun testUnknownNamespaceIsReported() {
        val warnings = warningsFor(
            """
            (ns app\m
              (:require totally\bogus))

            (defn f [] 1)
            """.trimIndent()
        )

        assertTrue("expected a missing-namespace warning, got: $warnings", warnings.any { it.contains("does not exist") })
    }

    /** Every warning/weak-warning message the annotator produces for [text]. */
    private fun warningsFor(text: String): List<String> {
        myFixture.enableInspections(PhelUnusedImportInspection())
        myFixture.configureByText("a.phel", text)
        return myFixture.doHighlighting()
            .filter { it.severity == HighlightSeverity.WARNING || it.severity == HighlightSeverity.WEAK_WARNING }
            .mapNotNull { it.description }
    }
}
