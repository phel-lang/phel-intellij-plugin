package org.phellang.editor.typing.pairing

import com.intellij.openapi.editor.Document
import org.phellang.editor.typing.context.PhelStringContextAnalyzer

object PhelCharacterPairing {

    private val OPENING_TO_CLOSING = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}'
    )

    private val CLOSING_CHARS = setOf(')', ']', '}')

    fun getClosingCharacter(openingChar: Char): Char? {
        return OPENING_TO_CLOSING[openingChar]
    }

    fun isClosingCharacter(c: Char): Boolean {
        return c in CLOSING_CHARS
    }

    fun shouldAutoClose(document: Document, offset: Int): Boolean {
        val text = document.charsSequence

        // Don't auto-close if we're inside a string
        if (PhelStringContextAnalyzer.isInsideString(text, offset)) {
            return false
        }

        // Don't auto-close if the character at the current position is a quote
        if (offset < text.length) {
            val charAtOffset = text[offset]
            if (charAtOffset == '"') {
                return false
            }
            
            // Don't auto-close if the next character is alphanumeric, dash, or underscore
            if (charAtOffset.isLetterOrDigit() || charAtOffset == '-' || charAtOffset == '_') {
                return false
            }
        }

        return true
    }

    fun shouldSkipClosingChar(document: Document, offset: Int, closingChar: Char): Boolean {
        val text = document.charsSequence

        if (offset >= text.length) {
            return false
        }

        val charAtOffset = text[offset]

        // Skip if the same closing character is already at this position
        return charAtOffset == closingChar
    }

    fun getAllPairableCharacters(): Set<Char> {
        return getOpeningCharacters() + getClosingCharacters()
    }

    private fun getOpeningCharacters(): Set<Char> {
        return OPENING_TO_CLOSING.keys.toSet()
    }

    private fun getClosingCharacters(): Set<Char> {
        return CLOSING_CHARS.toSet()
    }
}
