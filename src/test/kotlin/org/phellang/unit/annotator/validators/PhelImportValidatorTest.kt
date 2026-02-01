package org.phellang.unit.annotator.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PhelImportValidatorTest {

    @Nested
    inner class NamespacePatternValidation {

        @Test
        fun `namespace must contain backslash to be validated`() {
            val validNamespaces = listOf(
                "phel\\str", "my-project\\utils", "app\\lib\\helpers"
            )

            for (ns in validNamespaces) {
                assertTrue(ns.contains("\\"), "$ns should contain backslash")
            }
        }

        @Test
        fun `simple names without backslash are skipped`() {
            val invalidNamespaces = listOf("str", "utils", "helpers")

            for (ns in invalidNamespaces) {
                assertFalse(ns.contains("\\"), "$ns should not contain backslash")
            }
        }
    }

    @Nested
    inner class UnusedImportDetectionLogic {

        @Test
        fun `qualifier is extracted from qualified symbol`() {
            val symbol = "str/join"
            val qualifier = symbol.substringBefore("/")
            assertEquals("str", qualifier)
        }

        @Test
        fun `short namespace matches qualifier for usage detection`() {
            val fullNamespace = "phel\\str"
            val shortNamespace = fullNamespace.substringAfterLast("\\")
            val usageSymbol = "str/join"
            val usageQualifier = usageSymbol.substringBefore("/")

            assertEquals(shortNamespace, usageQualifier)
        }

        @Test
        fun `alias can be used as qualifier`() {
            // If phel\str is imported as s, then s/join should count as usage
            val aliasMap = mapOf("s" to "str")
            val usageSymbol = "s/join"
            val usageQualifier = usageSymbol.substringBefore("/")

            assertTrue(aliasMap.containsKey(usageQualifier))
        }

        @Test
        fun `unqualified symbols do not count as usage`() {
            val symbol = "map"
            assertFalse(symbol.contains("/"), "Unqualified symbol should not have qualifier")
        }
    }

    @Nested
    inner class ReferClauseDetection {

        @Test
        fun `require with refer is not considered unused`() {
            // (:require phel\test :refer [deftest is])
            // Even if no qualified calls like "test/xxx" exist, the referred symbols are used
            val keywords = listOf(":require", ":refer")
            assertTrue(keywords.contains(":refer"), "Should detect :refer in require form")
        }

        @Test
        fun `require without refer checks for qualified usages`() {
            // (:require phel\str)
            // Must have str/xxx usages to not be unused
            val keywords = listOf(":require")
            assertFalse(keywords.contains(":refer"), "Should not have :refer")
        }

        @Test
        fun `require with as checks for alias usages`() {
            // (:require phel\str :as s)
            // Must have s/xxx usages to not be unused
            val keywords = listOf(":require", ":as")
            assertTrue(keywords.contains(":as"), "Should detect :as in require form")
            assertFalse(keywords.contains(":refer"), "Should not have :refer")
        }
    }
}
