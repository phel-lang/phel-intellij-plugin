package org.phellang.unit.tools.transformer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.tools.transformer.MarkdownToHtmlTransformer

class MarkdownToHtmlTransformerTest {

    @Nested
    inner class InlineCodeConversion {

        @Test
        fun `should convert inline code to code tags`() {
            val input = "Use the `map` function"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("Use the <code>map</code> function", result)
        }

        @Test
        fun `should convert multiple inline codes`() {
            val input = "Use `map` and `filter` functions"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("Use <code>map</code> and <code>filter</code> functions", result)
        }

        @Test
        fun `should handle inline code with special characters`() {
            val input = "Returns `nil?` predicate"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("Returns <code>nil?</code> predicate", result)
        }
    }

    @Nested
    inner class LinkConversion {

        @Test
        fun `should convert markdown links to anchor tags`() {
            val input = "See [PHP docs](https://php.net) for more"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("See <a href=\"https://php.net\">PHP docs</a> for more", result)
        }

        @Test
        fun `should convert multiple links`() {
            val input = "[Link1](http://a.com) and [Link2](http://b.com)"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("<a href=\"http://a.com\">Link1</a> and <a href=\"http://b.com\">Link2</a>", result)
        }
    }

    @Nested
    inner class BoldConversion {

        @Test
        fun `should convert double asterisks to strong tags`() {
            val input = "This is **bold** text"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("This is <strong>bold</strong> text", result)
        }

        @Test
        fun `should convert double underscores to strong tags`() {
            val input = "This is __bold__ text"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("This is <strong>bold</strong> text", result)
        }
    }

    @Nested
    inner class ItalicConversion {

        @Test
        fun `should convert single asterisks to em tags`() {
            val input = "This is *italic* text"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("This is <em>italic</em> text", result)
        }

        @Test
        fun `should convert single underscores to em tags`() {
            val input = "This is _italic_ text"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("This is <em>italic</em> text", result)
        }

        @Test
        fun `should not convert underscores inside words`() {
            val input = "snake_case_name"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("snake_case_name", result)
        }
    }

    @Nested
    inner class CodeBlockConversion {

        @Test
        fun `should convert code blocks to pre and code tags`() {
            val input = "Example:\n```phel\n(map inc [1 2 3])\n```"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertTrue(result.contains("<pre><code>"))
            assertTrue(result.contains("(map inc [1 2 3])"))
            assertTrue(result.contains("</code></pre>"))
        }

        @Test
        fun `should preserve code block content without line break conversion`() {
            val input = "```phel\nline1\nline2\n```"
            val result = MarkdownToHtmlTransformer.transform(input)
            // Inside code blocks, newlines should be preserved as-is, not converted to <br />
            assertTrue(result.contains("line1\nline2"))
        }

        @Test
        fun `should handle multiple code blocks`() {
            val input = "First:\n```phel\n(func-a)\n```\nSecond:\n```phel\n(func-b)\n```"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertTrue(result.contains("(func-a)"), "Result should contain (func-a): $result")
            assertTrue(result.contains("(func-b)"), "Result should contain (func-b): $result")
            // Count occurrences of <pre><code>
            val count = Regex("<pre><code>").findAll(result).count()
            assertEquals(2, count, "Should have 2 code blocks: $result")
        }
    }

    @Nested
    inner class NewlineConversion {

        @Test
        fun `should convert single newlines to br tags`() {
            val input = "Line 1\nLine 2"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertTrue(result.contains("<br />"))
        }

        @Test
        fun `should convert double newlines to double br tags`() {
            val input = "Paragraph 1\n\nParagraph 2"
            val result = MarkdownToHtmlTransformer.transform(input)
            assertTrue(result.contains("<br /><br />"))
        }

        @Test
        fun `should normalize excessive newlines`() {
            val input = "A\n\n\n\n\nB"
            val result = MarkdownToHtmlTransformer.transform(input)
            // Should not have more than 2 consecutive <br /> tags
            val brCount = Regex("<br />").findAll(result).count()
            assertTrue(brCount <= 3) // Allow for some formatting
        }
    }

    @Nested
    inner class CombinedConversions {

        @Test
        fun `should handle complex markdown with multiple elements`() {
            val input = "Use `map` to transform. See [docs](http://example.com) for **more**."
            val result = MarkdownToHtmlTransformer.transform(input)
            assertTrue(result.contains("<code>map</code>"))
            assertTrue(result.contains("<a href=\"http://example.com\">docs</a>"))
            assertTrue(result.contains("<strong>more</strong>"))
        }

        @Test
        fun `should handle empty string`() {
            val result = MarkdownToHtmlTransformer.transform("")
            assertEquals("", result)
        }

        @Test
        fun `should trim whitespace from result`() {
            val input = "  text with spaces  "
            val result = MarkdownToHtmlTransformer.transform(input)
            assertEquals("text with spaces", result)
        }
    }
}
