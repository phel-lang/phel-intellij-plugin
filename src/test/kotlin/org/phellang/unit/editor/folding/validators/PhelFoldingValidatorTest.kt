package org.phellang.unit.editor.folding.validators

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.editor.folding.validators.PhelFoldingValidator

class PhelFoldingValidatorTest {

    private lateinit var mockDocument: Document

    @BeforeEach
    fun setUp() {
        mockDocument = mock(Document::class.java)
    }

    @Test
    fun `isValidFoldingRange should return false for ranges shorter than minimum length`() {
        val shortRange = TextRange(0, PhelFoldingValidator.MIN_FOLDING_LENGTH - 1)
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(shortRange.endOffset)).thenReturn(1)

        val result = PhelFoldingValidator.isValidFoldingRange(shortRange, mockDocument)

        assertFalse(result)
    }

    @Test
    fun `isValidFoldingRange should return false for single-line ranges`() {
        val singleLineRange = TextRange(0, 20) // Meets length requirement
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(20)).thenReturn(0) // Same line

        val result = PhelFoldingValidator.isValidFoldingRange(singleLineRange, mockDocument)

        assertFalse(result)
    }

    @Test
    fun `isValidFoldingRange should return true for multi-line ranges meeting minimum length`() {
        val validRange = TextRange(0, 20) // Meets length requirement
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(20)).thenReturn(2) // Different line

        val result = PhelFoldingValidator.isValidFoldingRange(validRange, mockDocument)

        assertTrue(result)
    }

    @Test
    fun `isMultiLine should return true for ranges spanning multiple lines`() {
        val multiLineRange = TextRange(0, 50)
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(50)).thenReturn(3)

        val result = PhelFoldingValidator.isMultiLine(multiLineRange, mockDocument)

        assertTrue(result)
    }

    @Test
    fun `isMultiLine should return false for ranges on same line`() {
        val singleLineRange = TextRange(10, 30)
        `when`(mockDocument.getLineNumber(10)).thenReturn(1)
        `when`(mockDocument.getLineNumber(30)).thenReturn(1)

        val result = PhelFoldingValidator.isMultiLine(singleLineRange, mockDocument)

        assertFalse(result)
    }

    @Test
    fun `MIN_FOLDING_LENGTH constant should be 15`() {
        assertEquals(15, PhelFoldingValidator.MIN_FOLDING_LENGTH)
    }

    @Test
    fun `isValidFoldingRange should handle edge case at exact minimum length`() {
        val exactMinRange = TextRange(0, PhelFoldingValidator.MIN_FOLDING_LENGTH)
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(PhelFoldingValidator.MIN_FOLDING_LENGTH)).thenReturn(1)

        val result = PhelFoldingValidator.isValidFoldingRange(exactMinRange, mockDocument)

        assertTrue(result)
    }

    @Test
    fun `validator should be consistent across multiple calls`() {
        val range = TextRange(0, 25)
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(25)).thenReturn(2)

        val result1 = PhelFoldingValidator.isValidFoldingRange(range, mockDocument)
        val result2 = PhelFoldingValidator.isValidFoldingRange(range, mockDocument)
        val result3 = PhelFoldingValidator.isValidFoldingRange(range, mockDocument)

        assertEquals(result1, result2)
        assertEquals(result2, result3)
        assertTrue(result1) // Should be valid
    }

    @Test
    fun `validator should handle large ranges correctly`() {
        val largeRange = TextRange(0, 1000)
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(1000)).thenReturn(50)

        val result = PhelFoldingValidator.isValidFoldingRange(largeRange, mockDocument)

        assertTrue(result)
    }

    @Test
    fun `validator should handle zero-length ranges`() {
        val zeroRange = TextRange(10, 10)
        `when`(mockDocument.getLineNumber(10)).thenReturn(1)

        val result = PhelFoldingValidator.isValidFoldingRange(zeroRange, mockDocument)

        assertFalse(result) // Zero length should be invalid
    }
}
