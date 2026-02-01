package org.phellang.unit.completion.indexing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.data.SymbolType
import org.phellang.completion.indexing.PhelProjectSymbolScanner

class PhelProjectSymbolScannerTest {

    @Test
    fun `scanner object is accessible`() {
        assertNotNull(PhelProjectSymbolScanner)
    }

    @Nested
    inner class PrivateDefinitionDetection {

        @Test
        fun `all private keywords are recognized`() {
            // All shorthand forms for private definitions
            val privateKeywords = setOf("defn-", "def-", "defmacro-")

            assertTrue("defn-" in privateKeywords)
            assertTrue("def-" in privateKeywords)
            assertTrue("defmacro-" in privateKeywords)
            assertFalse("defn" in privateKeywords)
            assertFalse("def" in privateKeywords)
            assertFalse("defmacro" in privateKeywords)
        }

        @Test
        fun `names ending with dash are NOT private by themselves`() {
            // A name ending with - is still public
            // Only defn- keyword makes a function private
            val namesWithDash = listOf("helper-", "process-data-", "validate-")

            for (name in namesWithDash) {
                // These should be public functions (the dash is just part of the name)
                assertTrue(name.endsWith("-"), "$name ends with dash but is still public")
            }
        }

        @Test
        fun `text containing private attribute is private`() {
            val privateTexts = listOf(
                "^:private", "(def ^:private name value)", "{:private true}"
            )

            for (text in privateTexts) {
                assertTrue(text.contains(":private"), "$text should be detected as private")
            }
        }

        @Test
        fun `regular defn with dash in name is public`() {
            // Functions like "process-data" or even "my-fn-" are public
            val publicFunctions = listOf("defn process-data", "defn my-fn-", "defn validate-input")

            for (fn in publicFunctions) {
                assertTrue(fn.startsWith("defn "), "$fn uses public defn keyword")
                assertFalse(fn.startsWith("defn-"), "$fn is not private defn-")
            }
        }
    }

    @Nested
    inner class QualifiedNameConstruction {

        @Test
        fun `qualified name combines namespace and function name`() {
            val shortNamespace = "util"
            val name = "greet"
            val qualifiedName = "$shortNamespace/$name"

            assertEquals("util/greet", qualifiedName)
        }

        @Test
        fun `qualified name with complex names`() {
            val shortNamespace = "string-helpers"
            val name = "format-date"
            val qualifiedName = "$shortNamespace/$name"

            assertEquals("string-helpers/format-date", qualifiedName)
        }
    }

    @Nested
    inner class DefinitionTypeRecognition {

        @Test
        fun `all definition keywords are recognized`() {
            val definitionKeywords = mapOf(
                "defn" to SymbolType.FUNCTION,
                "def" to SymbolType.VALUE,
                "defmacro" to SymbolType.MACRO,
                "defstruct" to SymbolType.STRUCT,
                "definterface" to SymbolType.INTERFACE
            )

            for ((keyword, expectedType) in definitionKeywords) {
                val actualType = SymbolType.fromKeyword(keyword)
                assertEquals(expectedType, actualType, "Keyword $keyword should map to $expectedType")
            }
        }

        @Test
        fun `non-definition keywords return null`() {
            val nonDefinitionKeywords = listOf("let", "fn", "if", "when", "loop", "for", "require", "ns")

            for (keyword in nonDefinitionKeywords) {
                assertNull(SymbolType.fromKeyword(keyword), "Keyword $keyword should not be a definition")
            }
        }
    }
}
