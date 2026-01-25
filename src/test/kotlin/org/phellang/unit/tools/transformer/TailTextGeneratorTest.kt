package org.phellang.unit.tools.transformer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.tools.transformer.TailTextGenerator

class TailTextGeneratorTest {

    @Nested
    inner class FirstSentenceExtraction {

        @Test
        fun `should extract first sentence ending with period`() {
            val input = "Returns the sum. This is extra text."
            val result = TailTextGenerator.generate(input)
            assertEquals("Returns the sum", result)
        }

        @Test
        fun `should extract first sentence ending with exclamation`() {
            val input = "Warning! This is important."
            val result = TailTextGenerator.generate(input)
            assertEquals("Warning", result)
        }

        @Test
        fun `should extract first sentence ending with question mark`() {
            val input = "Is this valid? Yes it is."
            val result = TailTextGenerator.generate(input)
            assertEquals("Is this valid", result)
        }

        @Test
        fun `should return full text if no sentence terminator`() {
            val input = "No sentence ending here"
            val result = TailTextGenerator.generate(input)
            assertEquals("No sentence ending here", result)
        }
    }

    @Nested
    inner class MarkdownRemoval {

        @Test
        fun `should remove code blocks`() {
            val input = "Description.\n```phel\n(code)\n```\nMore text."
            val result = TailTextGenerator.generate(input)
            assertEquals("Description", result)
        }

        @Test
        fun `should remove inline code backticks`() {
            val input = "Use the `map` function to transform."
            val result = TailTextGenerator.generate(input)
            assertEquals("Use the map function to transform", result)
        }

        @Test
        fun `should normalize whitespace`() {
            val input = "Text   with   multiple   spaces."
            val result = TailTextGenerator.generate(input)
            assertEquals("Text with multiple spaces", result)
        }

        @Test
        fun `should convert newlines to spaces`() {
            val input = "First line\nSecond line."
            val result = TailTextGenerator.generate(input)
            assertEquals("First line Second line", result)
        }
    }

    @Nested
    inner class LengthTruncation {

        @Test
        fun `should not truncate text under 100 characters`() {
            val input = "Short description"
            val result = TailTextGenerator.generate(input)
            assertEquals("Short description", result)
            assertFalse(result.endsWith("..."))
        }

        @Test
        fun `should truncate text over 100 characters`() {
            val input = "A".repeat(150)
            val result = TailTextGenerator.generate(input)
            assertTrue(result.length <= 100)
            assertTrue(result.endsWith("..."))
        }

        @Test
        fun `should truncate to 97 chars plus ellipsis for exactly 100 char result`() {
            val input = "A".repeat(200)
            val result = TailTextGenerator.generate(input)
            assertEquals(100, result.length)
            assertEquals("A".repeat(97) + "...", result)
        }
    }

    @Nested
    inner class EdgeCases {

        @Test
        fun `should handle empty string`() {
            val result = TailTextGenerator.generate("")
            assertEquals("", result)
        }

        @Test
        fun `should handle whitespace only`() {
            val result = TailTextGenerator.generate("   ")
            assertEquals("", result)
        }

        @Test
        fun `should handle string with only code block`() {
            val input = "```phel\n(code)\n```"
            val result = TailTextGenerator.generate(input)
            assertEquals("", result)
        }
    }
}
