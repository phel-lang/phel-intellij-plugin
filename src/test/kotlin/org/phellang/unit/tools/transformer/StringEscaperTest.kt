package org.phellang.unit.tools.transformer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.tools.transformer.StringEscaper

class StringEscaperTest {

    @Nested
    inner class HtmlEscaping {

        @Test
        fun `should escape ampersand`() {
            val result = StringEscaper.escapeHtml("a & b")
            assertEquals("a &amp; b", result)
        }

        @Test
        fun `should escape less than`() {
            val result = StringEscaper.escapeHtml("<div>")
            assertEquals("&lt;div&gt;", result)
        }

        @Test
        fun `should escape greater than`() {
            val result = StringEscaper.escapeHtml("a > b")
            assertEquals("a &gt; b", result)
        }

        @Test
        fun `should escape all HTML special characters`() {
            val result = StringEscaper.escapeHtml("<tag attr=\"val\"> & text")
            assertEquals("&lt;tag attr=\"val\"&gt; &amp; text", result)
        }

        @Test
        fun `should handle empty string`() {
            val result = StringEscaper.escapeHtml("")
            assertEquals("", result)
        }

        @Test
        fun `should not modify string without special characters`() {
            val input = "plain text"
            val result = StringEscaper.escapeHtml(input)
            assertEquals(input, result)
        }
    }

    @Nested
    inner class KotlinStringEscaping {

        @Test
        fun `should wrap in double quotes`() {
            val result = StringEscaper.toKotlinString("hello")
            assertEquals("\"hello\"", result)
        }

        @Test
        fun `should escape backslash`() {
            val result = StringEscaper.toKotlinString("a\\b")
            assertEquals("\"a\\\\b\"", result)
        }

        @Test
        fun `should escape double quotes`() {
            val result = StringEscaper.toKotlinString("say \"hello\"")
            assertEquals("\"say \\\"hello\\\"\"", result)
        }

        @Test
        fun `should escape newline`() {
            val result = StringEscaper.toKotlinString("line1\nline2")
            assertEquals("\"line1\\nline2\"", result)
        }

        @Test
        fun `should escape carriage return`() {
            val result = StringEscaper.toKotlinString("a\rb")
            assertEquals("\"a\\rb\"", result)
        }

        @Test
        fun `should escape tab`() {
            val result = StringEscaper.toKotlinString("a\tb")
            assertEquals("\"a\\tb\"", result)
        }

        @Test
        fun `should escape dollar sign`() {
            val result = StringEscaper.toKotlinString("cost: $100")
            assertEquals("\"cost: \\$100\"", result)
        }

        @Test
        fun `should handle empty string`() {
            val result = StringEscaper.toKotlinString("")
            assertEquals("\"\"", result)
        }
    }

    @Nested
    inner class TripleQuotedString {

        @Test
        fun `should use regular quotes for simple short strings`() {
            val result = StringEscaper.toTripleQuotedString("simple text")
            assertEquals("\"simple text\"", result)
        }

        @Test
        fun `should use triple quotes for strings with double quotes`() {
            val result = StringEscaper.toTripleQuotedString("say \"hello\"")
            assertTrue(result.startsWith("\"\"\""))
            assertTrue(result.endsWith("\"\"\""))
        }

        @Test
        fun `should use triple quotes for strings with newlines`() {
            val result = StringEscaper.toTripleQuotedString("line1\nline2")
            assertTrue(result.startsWith("\"\"\""))
            assertTrue(result.endsWith("\"\"\""))
        }

        @Test
        fun `should use triple quotes for strings with HTML`() {
            val result = StringEscaper.toTripleQuotedString("<div>content</div>")
            assertTrue(result.startsWith("\"\"\""))
            assertTrue(result.endsWith("\"\"\""))
        }

        @Test
        fun `should use triple quotes for long strings`() {
            val longString = "a".repeat(100)
            val result = StringEscaper.toTripleQuotedString(longString)
            assertTrue(result.startsWith("\"\"\""))
        }

        @Test
        fun `should escape dollar sign in triple quoted string`() {
            // Use a string with HTML to force triple quotes
            val result = StringEscaper.toTripleQuotedString("<code>\$variable</code>")
            assertTrue(result.startsWith("\"\"\""))
            assertTrue(result.contains("\${'$'}"))
        }
    }
}
