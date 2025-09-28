package org.phellang.unit.annotator.infrastructure

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.annotator.infrastructure.PhelAnnotationConstants

class PhelAnnotationConstantsTest {

    @Test
    fun `constants should have unique names`() {
        val constants = listOf(
            PhelAnnotationConstants.NAMESPACE_SYMBOL,
            PhelAnnotationConstants.FUNCTION_NAME,
            PhelAnnotationConstants.SHORT_FUNCTION,
            PhelAnnotationConstants.PHP_INTEROP,
            PhelAnnotationConstants.FUNCTION_CALL,
            PhelAnnotationConstants.COMMENTED_OUT_FORM,
            PhelAnnotationConstants.FUNCTION_PARAMETER,
            PhelAnnotationConstants.COLLECTION_TYPE,
            PhelAnnotationConstants.KEYWORD,
            PhelAnnotationConstants.REGULAR_SYMBOL,
            PhelAnnotationConstants.VARIADIC_PARAMETER
        )

        val names = constants.map { it.externalName }.toSet()
        assertEquals(constants.size, names.size, "All constants should have unique names")
    }

    @Test
    fun `constants should have proper Phel prefixes`() {
        val constants = mapOf(
            "NAMESPACE_SYMBOL" to PhelAnnotationConstants.NAMESPACE_SYMBOL,
            "FUNCTION_NAME" to PhelAnnotationConstants.FUNCTION_NAME,
            "SHORT_FUNCTION" to PhelAnnotationConstants.SHORT_FUNCTION,
            "PHP_INTEROP" to PhelAnnotationConstants.PHP_INTEROP,
            "FUNCTION_CALL" to PhelAnnotationConstants.FUNCTION_CALL,
            "COMMENTED_OUT_FORM" to PhelAnnotationConstants.COMMENTED_OUT_FORM,
            "FUNCTION_PARAMETER" to PhelAnnotationConstants.FUNCTION_PARAMETER,
            "COLLECTION_TYPE" to PhelAnnotationConstants.COLLECTION_TYPE,
            "KEYWORD" to PhelAnnotationConstants.KEYWORD,
            "REGULAR_SYMBOL" to PhelAnnotationConstants.REGULAR_SYMBOL,
            "VARIADIC_PARAMETER" to PhelAnnotationConstants.VARIADIC_PARAMETER
        )

        constants.forEach { (name, constant) ->
            assertTrue(
                constant.externalName.startsWith("PHEL_"),
                "Constant $name should have PHEL_ prefix, but was: ${constant.externalName}"
            )
        }
    }

    @Test
    fun `constants should have appropriate fallback colors`() {
        val expectedFallbacks = mapOf(
            PhelAnnotationConstants.NAMESPACE_SYMBOL to DefaultLanguageHighlighterColors.CLASS_REFERENCE,
            PhelAnnotationConstants.FUNCTION_NAME to DefaultLanguageHighlighterColors.CONSTANT,
            PhelAnnotationConstants.SHORT_FUNCTION to DefaultLanguageHighlighterColors.FUNCTION_DECLARATION,
            PhelAnnotationConstants.PHP_INTEROP to DefaultLanguageHighlighterColors.NUMBER,
            PhelAnnotationConstants.FUNCTION_CALL to DefaultLanguageHighlighterColors.KEYWORD,
            PhelAnnotationConstants.COMMENTED_OUT_FORM to DefaultLanguageHighlighterColors.LINE_COMMENT,
            PhelAnnotationConstants.FUNCTION_PARAMETER to DefaultLanguageHighlighterColors.PARAMETER,
            PhelAnnotationConstants.COLLECTION_TYPE to DefaultLanguageHighlighterColors.BRACES,
            PhelAnnotationConstants.KEYWORD to DefaultLanguageHighlighterColors.INSTANCE_FIELD,
            PhelAnnotationConstants.REGULAR_SYMBOL to DefaultLanguageHighlighterColors.LOCAL_VARIABLE,
            PhelAnnotationConstants.VARIADIC_PARAMETER to DefaultLanguageHighlighterColors.STATIC_FIELD
        )

        expectedFallbacks.forEach { (constant, expectedFallback) ->
            assertEquals(
                expectedFallback,
                constant.fallbackAttributeKey,
                "Constant ${constant.externalName} should have correct fallback color"
            )
        }
    }

    @Test
    fun `constants should be instances of TextAttributesKey`() {
        val constants = listOf(
            PhelAnnotationConstants.NAMESPACE_SYMBOL,
            PhelAnnotationConstants.FUNCTION_NAME,
            PhelAnnotationConstants.SHORT_FUNCTION,
            PhelAnnotationConstants.PHP_INTEROP,
            PhelAnnotationConstants.FUNCTION_CALL,
            PhelAnnotationConstants.COMMENTED_OUT_FORM,
            PhelAnnotationConstants.FUNCTION_PARAMETER,
            PhelAnnotationConstants.COLLECTION_TYPE,
            PhelAnnotationConstants.KEYWORD,
            PhelAnnotationConstants.REGULAR_SYMBOL,
            PhelAnnotationConstants.VARIADIC_PARAMETER
        )

        constants.forEach { constant ->
            assertNotNull(constant, "Constant should not be null")
            assertTrue(
                constant.javaClass.name.contains("TextAttributesKey"),
                "Constant ${constant.externalName} should be a TextAttributesKey instance, but was ${constant.javaClass.name}"
            )
        }
    }

    @Test
    fun `constants should have meaningful names`() {
        val expectedNames = mapOf(
            PhelAnnotationConstants.NAMESPACE_SYMBOL to "PHEL_NAMESPACE_SYMBOL",
            PhelAnnotationConstants.FUNCTION_NAME to "PHEL_FUNCTION_NAME",
            PhelAnnotationConstants.SHORT_FUNCTION to "PHEL_SHORT_FUNCTION",
            PhelAnnotationConstants.PHP_INTEROP to "PHEL_PHP_INTEROP",
            PhelAnnotationConstants.FUNCTION_CALL to "PHEL_FUNCTION_CALL",
            PhelAnnotationConstants.COMMENTED_OUT_FORM to "PHEL_COMMENTED_OUT_FORM",
            PhelAnnotationConstants.FUNCTION_PARAMETER to "PHEL_FUNCTION_PARAMETER",
            PhelAnnotationConstants.COLLECTION_TYPE to "PHEL_COLLECTION_TYPE",
            PhelAnnotationConstants.KEYWORD to "PHEL_SEMANTIC_KEYWORD",
            PhelAnnotationConstants.REGULAR_SYMBOL to "PHEL_REGULAR_SYMBOL",
            PhelAnnotationConstants.VARIADIC_PARAMETER to "PHEL_VARIADIC_PARAMETER"
        )

        expectedNames.forEach { (constant, expectedName) ->
            assertEquals(
                expectedName, constant.externalName, "Constant should have expected external name"
            )
        }
    }

    @Test
    fun `lazy initialization should work correctly`() {
        // Test that constants can be accessed multiple times without issues
        repeat(3) {
            assertNotNull(PhelAnnotationConstants.NAMESPACE_SYMBOL)
            assertNotNull(PhelAnnotationConstants.FUNCTION_NAME)
            assertNotNull(PhelAnnotationConstants.PHP_INTEROP)
        }

        // Test that the same instance is returned (lazy initialization)
        val first = PhelAnnotationConstants.NAMESPACE_SYMBOL
        val second = PhelAnnotationConstants.NAMESPACE_SYMBOL
        assertSame(first, second, "Lazy initialization should return the same instance")
    }

    @Test
    fun `constants should be suitable for Phel language highlighting`() {
        // Test that we have constants for key Phel language constructs
        val phelSpecificConstants = listOf(
            PhelAnnotationConstants.PHP_INTEROP,        // php/ interop
            PhelAnnotationConstants.SHORT_FUNCTION,     // |(...) syntax
            PhelAnnotationConstants.COMMENTED_OUT_FORM, // #_ comments
            PhelAnnotationConstants.VARIADIC_PARAMETER, // & parameter
            PhelAnnotationConstants.NAMESPACE_SYMBOL    // namespace\symbol
        )

        phelSpecificConstants.forEach { constant ->
            assertNotNull(constant, "Phel-specific constant should be initialized")
            assertTrue(
                constant.externalName.startsWith("PHEL_"), "Phel-specific constant should have PHEL_ prefix"
            )
        }
    }
}
