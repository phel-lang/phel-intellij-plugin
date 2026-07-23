package org.phellang.unit.completion.indexing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.registry.SymbolType
import org.phellang.registry.indexing.PhelProjectSymbolScanner

class PhelProjectSymbolScannerTest {

    @Test
    fun `scanner object is accessible`() {
        assertNotNull(PhelProjectSymbolScanner)
    }

    // Privacy detection is covered by PhelProjectSymbolScannerPrivacyTest, which parses real
    // definitions and calls scanFile. The cases that used to live here asserted String.contains
    // over local literals without involving the scanner, so they passed while the scanner was
    // dropping every public definition that merely mentioned `:private`.

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
