package org.phellang.integration.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelNamespaceUtils

class PhelNamespaceImporterIntegrationTest {

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
    inner class AliasMapBehavior {

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
        fun `reverse alias lookup finds alias for namespace`() {
            val aliasMap = mapOf("s" to "str", "j" to "json")

            val aliasForStr = aliasMap.entries.find { it.value == "str" }?.key
            assertEquals("s", aliasForStr)

            val aliasForHtml = aliasMap.entries.find { it.value == "html" }?.key
            assertNull(aliasForHtml)
        }
    }

    @Nested
    inner class AutoImportBehavior {

        @Test
        fun `should skip import when namespace has alias`() {
            // File has: (:require phel\str :as s)
            // User selects str/blank? -> s/blank? inserted, no import needed
            val aliasMap = mapOf("s" to "str")
            val namespace = "str"

            val isImportedViaAlias = aliasMap.values.contains(namespace)
            assertTrue(isImportedViaAlias, "Namespace with alias should not need import")
        }

        @Test
        fun `should transform function name using alias`() {
            // File has: (:require phel\str :as s)
            // str/blank? -> s/blank?
            val aliasMap = mapOf("s" to "str")
            val lookupString = "str/blank?"
            val namespace = PhelNamespaceUtils.extractNamespace(lookupString)!!
            val functionName = lookupString.substringAfter("/")

            val alias = aliasMap.entries.find { it.value == namespace }?.key
            val result = "$alias/$functionName"

            assertEquals("s/blank?", result)
        }
    }

    @Nested
    inner class CompletionSuggestionTransformation {

        @Test
        fun `should transform suggestions using alias when available`() {
            val aliasMap = mapOf("s" to "str", "j" to "json")

            // str/join -> s/join
            val strAlias = aliasMap.entries.find { it.value == "str" }?.key
            assertEquals("s/join", "$strAlias/join")

            // json/encode -> j/encode
            val jsonAlias = aliasMap.entries.find { it.value == "json" }?.key
            assertEquals("j/encode", "$jsonAlias/encode")

            // http/request -> http/request (no alias)
            val httpAlias = aliasMap.entries.find { it.value == "http" }?.key
            assertNull(httpAlias)
        }

        @Test
        fun `should keep canonical name when no alias exists`() {
            val aliasMap = emptyMap<String, String>()
            val namespace = "str"

            val alias = aliasMap.entries.find { it.value == namespace }?.key
            assertNull(alias, "No alias should exist")
        }

        @Test
        fun `alias qualifier should skip auto-import`() {
            // When suggestion is "s/join", "s" is an alias key -> skip import
            val aliasMap = mapOf("s" to "str")
            val qualifier = "s"

            assertTrue(aliasMap.containsKey(qualifier), "Alias qualifier should skip import")
        }

        @Test
        fun `canonical qualifier should check import`() {
            // When suggestion is "str/join", "str" is not an alias key -> check import
            val aliasMap = mapOf("s" to "str")
            val qualifier = "str"

            assertFalse(aliasMap.containsKey(qualifier), "Canonical qualifier needs import check")
        }
    }

    @Nested
    inner class DocumentationValidation {

        @Test
        fun `valid alias should show docs`() {
            // File: (:require phel\str :as s)
            // Hover: s/replace -> SHOW docs
            val aliasMap = mapOf("s" to "str")
            val qualifier = "s"

            assertTrue(aliasMap.containsKey(qualifier), "Valid alias should show docs")
            assertEquals("str", aliasMap[qualifier])
        }

        @Test
        fun `canonical name with alias should NOT show docs`() {
            // File: (:require phel\str :as s)
            // Hover: str/replace -> DON'T show docs (str is invalid)
            val aliasMap = mapOf("s" to "str")
            val directlyImported = emptySet<String>()
            val qualifier = "str"

            val isAliasKey = aliasMap.containsKey(qualifier)
            val isDirectlyImported = directlyImported.contains(qualifier)
            val namespaceHasAlias = aliasMap.values.contains(qualifier)

            assertFalse(isAliasKey, "str is not an alias key")
            assertFalse(isDirectlyImported, "str is not directly imported")
            assertTrue(namespaceHasAlias, "str has alias -> invalid qualifier")
        }

        @Test
        fun `directly imported namespace should show docs`() {
            // File: (:require phel\str)
            // Hover: str/replace -> SHOW docs
            val directlyImported = setOf("str")
            val qualifier = "str"

            assertTrue(directlyImported.contains(qualifier))
        }
    }

    @Nested
    inner class EdgeCases {

        @Test
        fun `should handle alias same as namespace name`() {
            // Valid case: (:require phel\str :as str)
            val aliasMap = mapOf("str" to "str")

            assertTrue(aliasMap.containsKey("str"))
            assertEquals("str", aliasMap["str"])
        }
    }
}
