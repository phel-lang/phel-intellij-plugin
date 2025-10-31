package org.phellang.unit.syntax.attributes

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.syntax.attributes.PhelTextAttributesRegistry

class PhelTextAttributesRegistryTest {

    @Test
    fun `all text attributes should be properly initialized`() {
        val attributes = listOf(
            PhelTextAttributesRegistry.COMMENT,
            PhelTextAttributesRegistry.STRING,
            PhelTextAttributesRegistry.NUMBER,
            PhelTextAttributesRegistry.KEYWORD,
            PhelTextAttributesRegistry.BOOLEAN,
            PhelTextAttributesRegistry.NIL_LITERAL,
            PhelTextAttributesRegistry.NAN_LITERAL,
            PhelTextAttributesRegistry.CHARACTER,
            PhelTextAttributesRegistry.PARENTHESES,
            PhelTextAttributesRegistry.BRACKETS,
            PhelTextAttributesRegistry.BRACES,
            PhelTextAttributesRegistry.QUOTE,
            PhelTextAttributesRegistry.SYNTAX_QUOTE,
            PhelTextAttributesRegistry.UNQUOTE,
            PhelTextAttributesRegistry.UNQUOTE_SPLICING,
            PhelTextAttributesRegistry.SYMBOL,
            PhelTextAttributesRegistry.METADATA,
            PhelTextAttributesRegistry.DOT_OPERATOR,
            PhelTextAttributesRegistry.COMMA,
            PhelTextAttributesRegistry.BAD_CHARACTER
        )

        attributes.forEach { attribute ->
            assertNotNull(attribute, "Text attribute should not be null")
            assertNotNull(attribute.externalName, "External name should not be null")
            assertTrue(attribute.externalName.startsWith("PHEL_"), "External name should start with PHEL_")
        }
    }

    @Test
    fun `text attributes should have meaningful names`() {
        val expectedNames = mapOf(
            PhelTextAttributesRegistry.COMMENT to "PHEL_COMMENT",
            PhelTextAttributesRegistry.STRING to "PHEL_STRING",
            PhelTextAttributesRegistry.NUMBER to "PHEL_NUMBER",
            PhelTextAttributesRegistry.KEYWORD to "PHEL_KEYWORD",
            PhelTextAttributesRegistry.BOOLEAN to "PHEL_BOOLEAN",
            PhelTextAttributesRegistry.NIL_LITERAL to "PHEL_NIL",
            PhelTextAttributesRegistry.NAN_LITERAL to "PHEL_NAN",
            PhelTextAttributesRegistry.CHARACTER to "PHEL_CHARACTER",
            PhelTextAttributesRegistry.PARENTHESES to "PHEL_PARENTHESES",
            PhelTextAttributesRegistry.BRACKETS to "PHEL_BRACKETS",
            PhelTextAttributesRegistry.BRACES to "PHEL_BRACES",
            PhelTextAttributesRegistry.QUOTE to "PHEL_QUOTE",
            PhelTextAttributesRegistry.SYNTAX_QUOTE to "PHEL_SYNTAX_QUOTE",
            PhelTextAttributesRegistry.UNQUOTE to "PHEL_UNQUOTE",
            PhelTextAttributesRegistry.UNQUOTE_SPLICING to "PHEL_UNQUOTE_SPLICING",
            PhelTextAttributesRegistry.SYMBOL to "PHEL_SYMBOL",
            PhelTextAttributesRegistry.METADATA to "PHEL_METADATA",
            PhelTextAttributesRegistry.DOT_OPERATOR to "PHEL_DOT",
            PhelTextAttributesRegistry.COMMA to "PHEL_COMMA",
            PhelTextAttributesRegistry.BAD_CHARACTER to "PHEL_BAD_CHARACTER"
        )

        expectedNames.forEach { (attribute, expectedName) ->
            assertEquals(expectedName, attribute.externalName, "Attribute should have expected name")
        }
    }
}
