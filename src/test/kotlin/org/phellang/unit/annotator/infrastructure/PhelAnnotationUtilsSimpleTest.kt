package org.phellang.unit.annotator.infrastructure

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.annotator.infrastructure.PhelAnnotationUtils

@ExtendWith(MockitoExtension::class)
class PhelAnnotationUtilsSimpleTest {

    @Mock
    private lateinit var mockElement: PsiElement

    @ParameterizedTest
    @ValueSource(strings = ["hello", "world", "test-symbol", "namespace\\symbol", "php/function", "keyword", "123", "test_var"])
    fun `isValidText should return true for valid text`(text: String) {
        assertTrue(PhelAnnotationUtils.isValidText(text))
    }

    @ParameterizedTest
    @CsvSource(
        "null, false",
        "'', false",
        "' ', false",
        "'  ', false",
        "'\t', false",
        "'\n', false",
        "'\r', false",
        "'   \t  ', false"
    )
    fun `isValidText should return false for invalid text`(text: String?, expected: Boolean) {
        val actualText = if (text == "null") null else text
        assertEquals(expected, PhelAnnotationUtils.isValidText(actualText))
    }

    @Test
    fun `isValidText should handle complex whitespace scenarios`() {
        // Test complex whitespace patterns not covered by parameterized tests
        assertFalse(PhelAnnotationUtils.isValidText("   \t  \n  "))
        assertFalse(PhelAnnotationUtils.isValidText("\r\n\t  "))
    }

    @Test
    fun `isValidText should handle mixed whitespace and content`() {
        // These should be valid because they contain non-whitespace characters
        assertTrue(PhelAnnotationUtils.isValidText(" hello "))
        assertTrue(PhelAnnotationUtils.isValidText("\tworld\t"))
        assertTrue(PhelAnnotationUtils.isValidText("\n test \n"))
    }

    @Test
    fun `isValidText should handle special Phel symbols`() {
        // Test various Phel-specific symbol patterns
        assertTrue(PhelAnnotationUtils.isValidText("defn"))
        assertTrue(PhelAnnotationUtils.isValidText("let"))
        assertTrue(PhelAnnotationUtils.isValidText("if"))
        assertTrue(PhelAnnotationUtils.isValidText("fn"))
        assertTrue(PhelAnnotationUtils.isValidText("&"))
        assertTrue(PhelAnnotationUtils.isValidText("php/array"))
        assertTrue(PhelAnnotationUtils.isValidText("core\\map"))
        assertTrue(PhelAnnotationUtils.isValidText("test?"))
        assertTrue(PhelAnnotationUtils.isValidText("test!"))
        assertTrue(PhelAnnotationUtils.isValidText("test*"))
        assertTrue(PhelAnnotationUtils.isValidText("my-var"))
        assertTrue(PhelAnnotationUtils.isValidText("my_var"))
    }

    @Test
    fun `isValidText should be consistent with Phel symbol naming conventions`() {
        val validSymbols = listOf(
            "function-name",
            "variable_name",
            "test?",
            "test!",
            "test*",
            "namespace\\symbol",
            "php/interop",
            "&",
            "->",
            ">>",
            "<<",
            "+=",
            "-=",
            "*=",
            "/="
        )

        validSymbols.forEach { symbol ->
            assertTrue(
                PhelAnnotationUtils.isValidText(symbol), "Should accept '$symbol' as valid text"
            )
        }
    }

    @Test
    fun `shouldAnnotate should return true for valid element with valid textRange`() {
        val textRange = TextRange(0, 10)
        `when`(mockElement.textRange).thenReturn(textRange)

        assertTrue(PhelAnnotationUtils.shouldAnnotate(mockElement))
    }

    @Test
    fun `shouldAnnotate should return false for null element`() {
        assertFalse(PhelAnnotationUtils.shouldAnnotate(null))
    }

    @Test
    fun `shouldAnnotate should return false for element with null textRange`() {
        `when`(mockElement.textRange).thenReturn(null)

        assertFalse(PhelAnnotationUtils.shouldAnnotate(mockElement))
    }

    @Test
    fun `shouldAnnotate should return false for element with zero-length textRange`() {
        val zeroLengthRange = TextRange(5, 5)
        `when`(mockElement.textRange).thenReturn(zeroLengthRange)

        assertFalse(PhelAnnotationUtils.shouldAnnotate(mockElement))
    }

    @Test
    fun `shouldAnnotate should return true for element with positive-length textRange`() {
        val validRanges = listOf(
            TextRange(0, 1), TextRange(10, 20), TextRange(100, 150), TextRange(0, 1000)
        )

        validRanges.forEach { range ->
            `when`(mockElement.textRange).thenReturn(range)
            assertTrue(
                PhelAnnotationUtils.shouldAnnotate(mockElement), "Should return true for range: $range"
            )
        }
    }

    @Test
    fun `shouldAnnotate should handle edge cases correctly`() {
        // Test minimum valid range (length 1)
        val minValidRange = TextRange(0, 1)
        `when`(mockElement.textRange).thenReturn(minValidRange)
        assertTrue(PhelAnnotationUtils.shouldAnnotate(mockElement))

        // Test large range
        val largeRange = TextRange(0, 10000)
        `when`(mockElement.textRange).thenReturn(largeRange)
        assertTrue(PhelAnnotationUtils.shouldAnnotate(mockElement))
    }
}
