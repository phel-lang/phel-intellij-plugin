package org.phellang.tools.transformer

/**
 * Transforms Markdown text to HTML for documentation popups.
 *
 * Supported conversions:
 * - Code blocks (```phel ... ```) → <pre><code>...</code></pre>
 * - Inline code (`...`) → <code>...</code>
 * - Links [text](url) → <a href="url">text</a>
 * - Bold **text** or __text__ → <strong>text</strong>
 * - Italic *text* or _text_ → <em>text</em>
 * - Newlines → <br />
 */
object MarkdownToHtmlTransformer {

    fun transform(markdown: String): String {
        val codeBlockPlaceholders = mutableMapOf<String, String>()
        var placeholderIndex = 0

        // Extract and preserve code blocks with placeholders
        var result = markdown.replace(Regex("```(?:phel)?\\s*\\n([\\s\\S]*?)```")) { match ->
            val code = match.groupValues[1].trimEnd()
            val placeholder = "\u0000CODE_BLOCK_${placeholderIndex++}\u0000"
            codeBlockPlaceholders[placeholder] = "<pre><code>\n$code\n</code></pre>"
            placeholder
        }

        result = convertInlineCode(result)
        result = convertLinks(result)
        result = convertBold(result)
        result = convertItalic(result)
        result = convertNewlines(result)

        // Restore code blocks
        codeBlockPlaceholders.forEach { (placeholder, codeBlock) ->
            result = result.replace(placeholder, codeBlock)
        }

        return result.trim()
    }

    private fun convertInlineCode(text: String): String {
        return text.replace(Regex("`([^`]+)`"), "<code>$1</code>")
    }

    private fun convertLinks(text: String): String {
        return text.replace(Regex("\\[([^]]+)]\\(([^)]+)\\)"), "<a href=\"$2\">$1</a>")
    }

    private fun convertBold(text: String): String {
        return text.replace(Regex("\\*\\*([^*]+)\\*\\*"), "<strong>$1</strong>")
            .replace(Regex("__([^_]+)__"), "<strong>$1</strong>")
    }

    private fun convertItalic(text: String): String {
        return text.replace(Regex("(?<![\\w*])\\*([^*]+)\\*(?![\\w*])"), "<em>$1</em>")
            .replace(Regex("(?<![\\w_])_([^_]+)_(?![\\w_])"), "<em>$1</em>")
    }

    private fun convertNewlines(text: String): String {
        var result = text
        // Normalize multiple newlines to double newlines (paragraph breaks)
        result = result.replace(Regex("\\n{3,}"), "\n\n")
        // Convert newlines to <br /> tags
        result = result.replace("\n\n", "<br /><br />\n")
        result = result.replace("\n", "<br />\n")
        // Clean up excessive <br /> tags
        result = result.replace(Regex("(<br />\\s*){3,}"), "<br /><br />\n")
        return result
    }
}
