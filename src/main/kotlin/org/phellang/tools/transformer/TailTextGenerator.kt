package org.phellang.tools.transformer

/**
 * Generates short tail text for code completion suggestions.
 * Extracts the first sentence from a description and limits it to ~100 characters.
 */
object TailTextGenerator {

    private const val MAX_LENGTH = 100
    private const val TRUNCATED_LENGTH = 97

    fun generate(description: String): String {
        val cleaned = cleanMarkdown(description)
        val firstSentence = extractFirstSentence(cleaned)
        return truncateIfNeeded(firstSentence)
    }

    private fun cleanMarkdown(text: String): String {
        return text
            .replace(Regex("```[\\s\\S]*?```"), "") // Remove code blocks
            .replace(Regex("`([^`]+)`"), "$1") // Remove inline code formatting
            .replace("\n", " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun extractFirstSentence(text: String): String {
        return text.split(Regex("[.!?]")).firstOrNull()?.trim() ?: text
    }

    private fun truncateIfNeeded(text: String): String {
        return if (text.length > MAX_LENGTH) {
            text.take(TRUNCATED_LENGTH) + "..."
        } else {
            text
        }
    }
}
