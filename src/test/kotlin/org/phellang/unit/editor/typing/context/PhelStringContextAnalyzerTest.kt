package org.phellang.unit.editor.typing.context

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.editor.typing.context.PhelStringContextAnalyzer

class PhelStringContextAnalyzerTest {

    @Test
    fun `isInsideString should return false for empty text`() {
        val result = PhelStringContextAnalyzer.isInsideString("", 0)

        assertFalse(result)
    }

    @Test
    fun `isInsideString should return false when no quotes present`() {
        val text = "hello world"
        val result = PhelStringContextAnalyzer.isInsideString(text, 5)

        assertFalse(result)
    }

    @Test
    fun `isInsideString should return true when inside simple string`() {
        val text = "\"hello world\""
        val result = PhelStringContextAnalyzer.isInsideString(text, 5)

        assertTrue(result)
    }

    @Test
    fun `isInsideString should return false when outside string`() {
        val text = "\"hello\" world"
        val result = PhelStringContextAnalyzer.isInsideString(text, 10)

        assertFalse(result)
    }

    @Test
    fun `isInsideString should handle escaped quotes correctly`() {
        val text = "\"hello \\\"world\\\" test\""
        val result = PhelStringContextAnalyzer.isInsideString(text, 15)

        assertTrue(result)
    }

    @Test
    fun `isInsideString should handle multiple strings`() {
        val text = "\"first\" \"second\" \"third\""
        // Positions: 0123456789012345678901234

        assertFalse(PhelStringContextAnalyzer.isInsideString(text, 0))  // At opening quote of first
        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 3))   // Inside first
        assertFalse(PhelStringContextAnalyzer.isInsideString(text, 7))  // Between first and second
        assertFalse(PhelStringContextAnalyzer.isInsideString(text, 8))  // At opening quote of second
        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 11))  // Inside second
        assertFalse(PhelStringContextAnalyzer.isInsideString(text, 16)) // Between second and third
        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 19))  // Inside third
    }

    @Test
    fun `isInsideString should handle various positions correctly`() {
        val testCases = listOf(
            Triple("\"hello\"", 0, false),  // At opening quote
            Triple("\"hello\"", 1, true),   // Inside string
            Triple("\"hello\"", 5, true),   // Inside string
            Triple("\"hello\"", 6, true),   // At closing quote (still inside)
            Triple("\"hello\"", 7, false)   // After string
        )

        testCases.forEach { (text, offset, expected) ->
            val result = PhelStringContextAnalyzer.isInsideString(text, offset)
            assertEquals(expected, result, "Failed for '$text' at offset $offset")
        }
    }

    @Test
    fun `string context should handle complex escaped scenarios`() {
        val text = "\"start \\\\\\\" middle \\\" end\""

        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 5))   // Inside string
        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 10))  // After escaped quote
        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 20))  // Before escaped quote
        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 25))  // After escaped quote
    }

    @Test
    fun `analyzer should be consistent across multiple calls`() {
        val text = "\"hello world\""
        val offset = 5

        val result1 = PhelStringContextAnalyzer.isInsideString(text, offset)
        val result2 = PhelStringContextAnalyzer.isInsideString(text, offset)
        val result3 = PhelStringContextAnalyzer.isInsideString(text, offset)

        assertEquals(result1, result2)
        assertEquals(result2, result3)
        assertTrue(result1)
    }

    @Test
    fun `analyzer should handle edge cases gracefully`() {
        // Test with offset at text boundaries
        val text = "\"hello\""

        assertFalse(PhelStringContextAnalyzer.isInsideString(text, 0))  // At opening quote
        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 1))   // Just inside
        assertTrue(PhelStringContextAnalyzer.isInsideString(text, 6))   // Just before closing
        assertFalse(PhelStringContextAnalyzer.isInsideString(text, 7))  // At closing quote
    }

    @Test
    fun `analyzer should handle offset beyond text length`() {
        val text = "\"hello\""

        // Should not throw exception for offset beyond text length
        assertDoesNotThrow {
            PhelStringContextAnalyzer.isInsideString(text, 100)
        }

        assertFalse(PhelStringContextAnalyzer.isInsideString(text, 100))
    }

    @Test
    fun `analyzer should handle negative offset gracefully`() {
        val text = "\"hello\""

        // Should not throw exception for negative offset
        assertDoesNotThrow {
            PhelStringContextAnalyzer.isInsideString(text, -1)
        }

        assertFalse(PhelStringContextAnalyzer.isInsideString(text, -1))
    }
}
