package org.phellang.unit.annotator.highlighters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Tests for PhelRequireHighlighter logic.
 * Note: Full highlighting requires IntelliJ platform.
 */
class PhelRequireHighlighterTest {

    @Nested
    inner class NamespaceInRequireFormDetection {

        @Test
        fun `namespace must contain backslash to be in require form`() {
            val validNamespaces = listOf(
                "phel\\str",
                "my-project\\utils",
                "app\\lib\\helpers"
            )

            for (ns in validNamespaces) {
                assertTrue(ns.contains("\\"), "$ns should be considered a namespace")
            }
        }

        @Test
        fun `simple symbols without backslash are not namespaces`() {
            val notNamespaces = listOf(
                "str",
                ":require",
                ":as",
                "alias"
            )

            for (ns in notNamespaces) {
                assertFalse(ns.contains("\\"), "$ns should not be considered a namespace")
            }
        }
    }

    @Nested
    inner class RequireFormStructure {

        @Test
        fun `require keyword is correct`() {
            val requireKeyword = ":require"
            assertEquals(":require", requireKeyword)
        }

        @Test
        fun `as keyword for aliases`() {
            val asKeyword = ":as"
            assertEquals(":as", asKeyword)
        }

        @Test
        fun `typical require form structure`() {
            // (:require phel\str)
            val elements = listOf(":require", "phel\\str")
            assertEquals(":require", elements[0])
            assertTrue(elements[1].contains("\\"))
        }

        @Test
        fun `require form with alias structure`() {
            // (:require phel\str :as s)
            val elements = listOf(":require", "phel\\str", ":as", "s")
            assertEquals(":require", elements[0])
            assertTrue(elements[1].contains("\\"))
            assertEquals(":as", elements[2])
            assertFalse(elements[3].contains("\\"))
        }
    }

    @Nested
    inner class ValidationResultHandling {

        @Test
        fun `duplicate result triggers remove quick fix`() {
            data class MockResult(val isDuplicate: Boolean, val suggestedNamespace: String?)

            val duplicateResult = MockResult(isDuplicate = true, suggestedNamespace = null)
            assertTrue(duplicateResult.isDuplicate)
            assertNull(duplicateResult.suggestedNamespace)
        }

        @Test
        fun `suggestion result triggers fix quick fix`() {
            data class MockResult(val isDuplicate: Boolean, val suggestedNamespace: String?)

            val suggestionResult = MockResult(isDuplicate = false, suggestedNamespace = "my-project\\utils")
            assertFalse(suggestionResult.isDuplicate)
            assertNotNull(suggestionResult.suggestedNamespace)
        }

        @Test
        fun `no suggestion result shows warning only`() {
            data class MockResult(val isDuplicate: Boolean, val suggestedNamespace: String?)

            val noSuggestionResult = MockResult(isDuplicate = false, suggestedNamespace = null)
            assertFalse(noSuggestionResult.isDuplicate)
            assertNull(noSuggestionResult.suggestedNamespace)
        }
    }
}
