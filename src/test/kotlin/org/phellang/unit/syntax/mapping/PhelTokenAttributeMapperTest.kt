package org.phellang.unit.syntax.mapping

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.phellang.syntax.attributes.PhelTextAttributesRegistry
import org.phellang.syntax.mapping.PhelTokenAttributeMapper
import org.phellang.syntax.classification.PhelTokenClassifier.TokenCategory

class PhelTokenAttributeMapperTest {

    @ParameterizedTest
    @EnumSource(TokenCategory::class)
    fun `getTextAttributes should return non-null arrays for all categories`(category: TokenCategory) {
        val attributes = PhelTokenAttributeMapper.getTextAttributes(category)

        assertNotNull(attributes, "Attributes array should not be null for category: $category")

        if (category == TokenCategory.UNKNOWN) {
            assertEquals(0, attributes.size, "UNKNOWN category should have empty attributes")
        } else {
            assertTrue(attributes.isNotEmpty(), "Non-UNKNOWN category should have attributes: $category")
            attributes.forEach { attribute ->
                assertNotNull(attribute, "Individual attribute should not be null in category: $category")
            }
        }
    }

    @Test
    fun `getTextAttributes should return correct attributes for each category`() {
        val expectedMappings = mapOf(
            TokenCategory.COMMENT to arrayOf(PhelTextAttributesRegistry.COMMENT),
            TokenCategory.STRING to arrayOf(PhelTextAttributesRegistry.STRING),
            TokenCategory.NUMBER to arrayOf(PhelTextAttributesRegistry.NUMBER),
            TokenCategory.BOOLEAN to arrayOf(PhelTextAttributesRegistry.BOOLEAN),
            TokenCategory.NIL to arrayOf(PhelTextAttributesRegistry.NIL_LITERAL),
            TokenCategory.NAN to arrayOf(PhelTextAttributesRegistry.NAN_LITERAL),
            TokenCategory.CHARACTER to arrayOf(PhelTextAttributesRegistry.CHARACTER),
            TokenCategory.PARENTHESES to arrayOf(PhelTextAttributesRegistry.PARENTHESES),
            TokenCategory.BRACKETS to arrayOf(PhelTextAttributesRegistry.BRACKETS),
            TokenCategory.BRACES to arrayOf(PhelTextAttributesRegistry.BRACES),
            TokenCategory.QUOTE to arrayOf(PhelTextAttributesRegistry.QUOTE),
            TokenCategory.SYNTAX_QUOTE to arrayOf(PhelTextAttributesRegistry.SYNTAX_QUOTE),
            TokenCategory.UNQUOTE to arrayOf(PhelTextAttributesRegistry.UNQUOTE),
            TokenCategory.UNQUOTE_SPLICING to arrayOf(PhelTextAttributesRegistry.UNQUOTE_SPLICING),
            TokenCategory.KEYWORD to arrayOf(PhelTextAttributesRegistry.KEYWORD),
            TokenCategory.METADATA to arrayOf(PhelTextAttributesRegistry.METADATA),
            TokenCategory.DOT_OPERATOR to arrayOf(PhelTextAttributesRegistry.DOT_OPERATOR),
            TokenCategory.COMMA to arrayOf(PhelTextAttributesRegistry.COMMA),
            TokenCategory.SYMBOL to arrayOf(PhelTextAttributesRegistry.SYMBOL),
            TokenCategory.BAD_CHARACTER to arrayOf(PhelTextAttributesRegistry.BAD_CHARACTER)
        )

        expectedMappings.forEach { (category, expectedAttributes) ->
            val actualAttributes = PhelTokenAttributeMapper.getTextAttributes(category)
            assertArrayEquals(expectedAttributes, actualAttributes, "Mapping should be correct for category: $category")
        }
    }

    @Test
    fun `mapping should be consistent across multiple calls`() {
        val testCategories = listOf(
            TokenCategory.COMMENT,
            TokenCategory.STRING,
            TokenCategory.NUMBER,
            TokenCategory.KEYWORD,
            TokenCategory.SYMBOL,
            TokenCategory.UNKNOWN
        )

        testCategories.forEach { category ->
            val firstCall = PhelTokenAttributeMapper.getTextAttributes(category)
            val secondCall = PhelTokenAttributeMapper.getTextAttributes(category)

            assertArrayEquals(firstCall, secondCall, "Mapping should be consistent for category: $category")
        }
    }

    @Test
    fun `attribute arrays should have expected sizes`() {
        val expectedSizes = mapOf(
            TokenCategory.COMMENT to 1,
            TokenCategory.STRING to 1,
            TokenCategory.NUMBER to 1,
            TokenCategory.BOOLEAN to 1,
            TokenCategory.NIL to 1,
            TokenCategory.NAN to 1,
            TokenCategory.CHARACTER to 1,
            TokenCategory.PARENTHESES to 1,
            TokenCategory.BRACKETS to 1,
            TokenCategory.BRACES to 1,
            TokenCategory.QUOTE to 1,
            TokenCategory.SYNTAX_QUOTE to 1,
            TokenCategory.UNQUOTE to 1,
            TokenCategory.UNQUOTE_SPLICING to 1,
            TokenCategory.KEYWORD to 1,
            TokenCategory.METADATA to 1,
            TokenCategory.DOT_OPERATOR to 1,
            TokenCategory.COMMA to 1,
            TokenCategory.SYMBOL to 1,
            TokenCategory.BAD_CHARACTER to 1,
            TokenCategory.UNKNOWN to 0
        )

        expectedSizes.forEach { (category, expectedSize) ->
            val attributes = PhelTokenAttributeMapper.getTextAttributes(category)
            assertEquals(expectedSize, attributes.size, "Category $category should have $expectedSize attributes")
        }
    }

    @Test
    fun `attribute arrays should be immutable references`() {
        val category = TokenCategory.COMMENT
        val firstArray = PhelTokenAttributeMapper.getTextAttributes(category)
        val secondArray = PhelTokenAttributeMapper.getTextAttributes(category)

        // Arrays should be the same reference (for performance)
        assertSame(firstArray, secondArray, "Should return same array reference for performance")
    }

    @Test
    fun `mapping should handle all defined token categories`() {
        // Ensure we haven't missed any categories in our mapping
        TokenCategory.entries.forEach { category ->
            assertDoesNotThrow {
                PhelTokenAttributeMapper.getTextAttributes(category)
            }
        }
    }
}
