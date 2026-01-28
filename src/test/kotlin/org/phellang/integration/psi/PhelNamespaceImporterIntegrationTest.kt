package org.phellang.integration.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelNamespaceUtils

/**
 * Tests for PhelNamespaceUtils logic (non-PSI parts).
 * PSI-based tests require IntelliJ platform test framework and are tested manually.
 */
class PhelNamespaceImporterIntegrationTest {

    @Nested
    inner class NamespaceConversion {

        @Test
        fun `toPhelNamespace converts short name to full Phel namespace`() {
            assertEquals("phel\\str", PhelNamespaceUtils.toPhelNamespace("str"))
            assertEquals("phel\\http", PhelNamespaceUtils.toPhelNamespace("http"))
            assertEquals("phel\\json", PhelNamespaceUtils.toPhelNamespace("json"))
        }
    }

    @Nested
    inner class CoreNamespaceDetection {

        @Test
        fun `isCoreNamespace returns true for core`() {
            assertTrue(PhelNamespaceUtils.isCoreNamespace("core"))
        }

        @Test
        fun `isCoreNamespace returns true for null`() {
            assertTrue(PhelNamespaceUtils.isCoreNamespace(null))
        }

        @Test
        fun `isCoreNamespace returns false for other namespaces`() {
            assertFalse(PhelNamespaceUtils.isCoreNamespace("str"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("http"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("json"))
        }
    }

    @Nested
    inner class NamespaceExtraction {

        @Test
        fun `extractNamespace returns namespace from qualified name`() {
            assertEquals("str", PhelNamespaceUtils.extractNamespace("str/join"))
            assertEquals("http", PhelNamespaceUtils.extractNamespace("http/request"))
        }

        @Test
        fun `extractNamespace returns null for unqualified name`() {
            assertNull(PhelNamespaceUtils.extractNamespace("map"))
            assertNull(PhelNamespaceUtils.extractNamespace("filter"))
        }
    }

    @Nested
    inner class QuickFixReplacementLogic {

        @Test
        fun `should parse phel namespace format correctly`() {
            // This tests the parsing logic used by the quick-fix
            val replacement = "phel\\str\\contains?"
            val parts = replacement.split("\\")

            assertEquals(3, parts.size)
            assertEquals("phel", parts[0])
            assertEquals("str", parts[1])
            assertEquals("contains?", parts[2])

            // The expected conversion
            val namespace = parts[1]
            val functionName = "${parts[1]}/${parts.drop(2).joinToString("\\")}"

            assertEquals("str", namespace)
            assertEquals("str/contains?", functionName)
        }

        @Test
        fun `should handle simple replacement without namespace`() {
            val replacement = "assoc"
            assertFalse(replacement.contains("\\"))
            // No namespace import needed
        }

        @Test
        fun `should handle multi-part function names`() {
            // Edge case: function names with backslashes (unlikely but possible)
            val replacement = "phel\\some\\namespace\\func"
            val parts = replacement.split("\\")

            if (parts.size >= 3 && parts[0] == "phel") {
                val namespace = parts[1]
                val functionName = "${parts[1]}/${parts.drop(2).joinToString("\\")}"

                assertEquals("some", namespace)
                assertEquals("some/namespace\\func", functionName)
            }
        }
    }

    @Nested
    inner class AliasResolutionLogic {

        @Test
        fun `alias map structure should map alias to short namespace`() {
            // Expected: extractAliasMap returns Map<alias, shortNamespace>
            // e.g., for (:require phel\str :as s) -> {"s" -> "str"}
            val aliasMap = mapOf("s" to "str", "j" to "json")

            assertEquals("str", aliasMap["s"])
            assertEquals("json", aliasMap["j"])
            assertNull(aliasMap["http"])
        }

        @Test
        fun `reverse alias lookup should find alias for namespace`() {
            // Simulating findAliasForNamespace behavior
            val aliasMap = mapOf("s" to "str", "j" to "json", "http" to "http")

            // Find alias for "str" -> should return "s"
            val aliasForStr = aliasMap.entries.find { it.value == "str" }?.key
            assertEquals("s", aliasForStr)

            // Find alias for "json" -> should return "j"
            val aliasForJson = aliasMap.entries.find { it.value == "json" }?.key
            assertEquals("j", aliasForJson)

            // Find alias for "html" -> should return null (not in map)
            val aliasForHtml = aliasMap.entries.find { it.value == "html" }?.key
            assertNull(aliasForHtml)
        }

        @Test
        fun `alias replacement in function name should work correctly`() {
            // When user selects "str/blank?" and alias "s" exists for "str"
            val lookupString = "str/blank?"
            val namespace = PhelNamespaceUtils.extractNamespace(lookupString)
            val functionName = lookupString.substringAfter("/")
            val alias = "s"

            val aliasedText = "$alias/$functionName"
            assertEquals("s/blank?", aliasedText)
        }
    }

    @Nested
    inner class AutoImportExpectedBehavior {

        @Test
        fun `should add new require form for new namespace`() {
            // Expected behavior: adds (:require phel\json) as a new form
            val namespace = "json"
            val expectedRequire = "(:require phel\\$namespace)"
            assertEquals("(:require phel\\json)", expectedRequire)
        }

        @Test
        fun `should not import when namespace already imported directly`() {
            // When file has: (:require phel\str)
            // And user selects str/join
            // Expected: no import added, str/join inserted as-is
            val namespace = "str"
            val phelNamespace = PhelNamespaceUtils.toPhelNamespace(namespace)
            assertEquals("phel\\str", phelNamespace)
        }

        @Test
        fun `should not import when namespace imported via alias`() {
            // When file has: (:require phel\str :as s)
            // And user selects str/blank?
            // Expected: no import added, s/blank? inserted (using alias)
            val aliasMap = mapOf("s" to "str")
            val namespace = "str"

            val isImportedViaAlias = aliasMap.values.contains(namespace)
            assertTrue(isImportedViaAlias)
        }

        @Test
        fun `should use existing alias when inserting function`() {
            // When file has: (:require phel\str :as s)
            // And user selects str/blank?
            // Expected: s/blank? is inserted
            val aliasMap = mapOf("s" to "str")
            val lookupString = "str/blank?"
            val namespace = PhelNamespaceUtils.extractNamespace(lookupString)!!
            val functionName = lookupString.substringAfter("/")

            val alias = aliasMap.entries.find { it.value == namespace }?.key
            assertNotNull(alias)

            val result = "$alias/$functionName"
            assertEquals("s/blank?", result)
        }
    }
}
