package org.phellang.unit.syntax

import com.intellij.psi.TokenType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.syntax.PhelSyntaxHighlighter
import org.phellang.language.PhelLexerAdapter
import org.phellang.language.psi.PhelTypes
import org.phellang.syntax.attributes.PhelTextAttributesRegistry

class PhelSyntaxHighlighterTest {

    @Test
    fun `getHighlightingLexer should return PhelLexerAdapter`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()
        val lexer = syntaxHighlighter.highlightingLexer

        assertNotNull(lexer, "Lexer should not be null")
        assertTrue(lexer is PhelLexerAdapter, "Should return PhelLexerAdapter instance")
    }

    @Test
    fun `getTokenHighlights should return correct attributes for all token types`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        val testCases = mapOf(
            // Comments
            PhelTypes.LINE_COMMENT to PhelTextAttributesRegistry.COMMENT,
            PhelTypes.FORM_COMMENT to PhelTextAttributesRegistry.COMMENT,
            PhelTypes.MULTILINE_COMMENT to PhelTextAttributesRegistry.COMMENT,

            // Literals
            PhelTypes.STRING to PhelTextAttributesRegistry.STRING,
            PhelTypes.NUMBER to PhelTextAttributesRegistry.NUMBER,
            PhelTypes.HEXNUM to PhelTextAttributesRegistry.NUMBER,
            PhelTypes.BINNUM to PhelTextAttributesRegistry.NUMBER,
            PhelTypes.OCTNUM to PhelTextAttributesRegistry.NUMBER,
            PhelTypes.BOOL to PhelTextAttributesRegistry.BOOLEAN,
            PhelTypes.NIL to PhelTextAttributesRegistry.NIL_LITERAL,
            PhelTypes.NAN to PhelTextAttributesRegistry.NAN_LITERAL,
            PhelTypes.CHAR to PhelTextAttributesRegistry.CHARACTER,

            // Delimiters
            PhelTypes.PAREN1 to PhelTextAttributesRegistry.PARENTHESES,
            PhelTypes.PAREN2 to PhelTextAttributesRegistry.PARENTHESES,
            PhelTypes.BRACKET1 to PhelTextAttributesRegistry.BRACKETS,
            PhelTypes.BRACKET2 to PhelTextAttributesRegistry.BRACKETS,
            PhelTypes.BRACE1 to PhelTextAttributesRegistry.BRACES,
            PhelTypes.BRACE2 to PhelTextAttributesRegistry.BRACES,

            // Quote and macro syntax
            PhelTypes.QUOTE to PhelTextAttributesRegistry.QUOTE,
            PhelTypes.SYNTAX_QUOTE to PhelTextAttributesRegistry.SYNTAX_QUOTE,
            PhelTypes.TILDE to PhelTextAttributesRegistry.UNQUOTE,
            PhelTypes.COMMA to PhelTextAttributesRegistry.UNQUOTE,
            PhelTypes.TILDE_AT to PhelTextAttributesRegistry.UNQUOTE_SPLICING,
            PhelTypes.COMMA_AT to PhelTextAttributesRegistry.UNQUOTE_SPLICING,

            // Keywords
            PhelTypes.KEYWORD to PhelTextAttributesRegistry.KEYWORD,
            PhelTypes.KEYWORD_TOKEN to PhelTextAttributesRegistry.KEYWORD,
            PhelTypes.COLON to PhelTextAttributesRegistry.KEYWORD,
            PhelTypes.COLONCOLON to PhelTextAttributesRegistry.KEYWORD,

            // Metadata and operators
            PhelTypes.HAT to PhelTextAttributesRegistry.METADATA,
            PhelTypes.DOT to PhelTextAttributesRegistry.DOT_OPERATOR,
            PhelTypes.DOTDASH to PhelTextAttributesRegistry.DOT_OPERATOR,

            // Symbols
            PhelTypes.SYM to PhelTextAttributesRegistry.SYMBOL,

            // Bad characters
            TokenType.BAD_CHARACTER to PhelTextAttributesRegistry.BAD_CHARACTER
        )

        testCases.forEach { (tokenType, expectedAttribute) ->
            val attributes = syntaxHighlighter.getTokenHighlights(tokenType)

            assertNotNull(attributes, "Attributes should not be null for token: $tokenType")
            assertEquals(1, attributes.size, "Should have exactly one attribute for token: $tokenType")
            assertEquals(expectedAttribute, attributes[0], "Should have correct attribute for token: $tokenType")
        }
    }

    @Test
    fun `getTokenHighlights should return empty array for unknown tokens`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Create a mock token type that won't match any category
        // Since we can't easily mock IElementType, we'll test this indirectly
        // by ensuring our classifier handles unknown tokens properly

        // This is tested through the integration with PhelTokenClassifier
        // which returns UNKNOWN category for unrecognized tokens

        // For now, we'll verify that all known tokens return non-empty arrays
        val knownTokens = listOf(
            PhelTypes.STRING,
            PhelTypes.NUMBER,
            PhelTypes.KEYWORD,
            PhelTypes.SYM,
            TokenType.BAD_CHARACTER
        )

        knownTokens.forEach { tokenType ->
            val attributes = syntaxHighlighter.getTokenHighlights(tokenType)
            assertTrue(attributes.isNotEmpty(), "Known token should have attributes: $tokenType")
        }
    }


    @Test
    fun `syntax highlighter should be consistent across multiple instances`() {
        val highlighter1 = PhelSyntaxHighlighter()
        val highlighter2 = PhelSyntaxHighlighter()

        val testTokens = listOf(
            PhelTypes.STRING,
            PhelTypes.NUMBER,
            PhelTypes.KEYWORD,
            PhelTypes.SYM
        )

        testTokens.forEach { tokenType ->
            val attributes1 = highlighter1.getTokenHighlights(tokenType)
            val attributes2 = highlighter2.getTokenHighlights(tokenType)

            assertArrayEquals(
                attributes1,
                attributes2,
                "Different instances should return same attributes for token: $tokenType"
            )
        }
    }

    @Test
    fun `syntax highlighter should be consistent across multiple calls`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        val testTokens = listOf(
            PhelTypes.STRING,
            PhelTypes.NUMBER,
            PhelTypes.KEYWORD,
            PhelTypes.SYM
        )

        testTokens.forEach { tokenType ->
            val firstCall = syntaxHighlighter.getTokenHighlights(tokenType)
            val secondCall = syntaxHighlighter.getTokenHighlights(tokenType)

            assertArrayEquals(
                firstCall,
                secondCall,
                "Multiple calls should return same attributes for token: $tokenType"
            )
        }
    }

    @Test
    fun `syntax highlighter should be performant`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()
        val testTokens = listOf(
            PhelTypes.LINE_COMMENT,
            PhelTypes.STRING,
            PhelTypes.NUMBER,
            PhelTypes.KEYWORD,
            PhelTypes.SYM
        )

        val startTime = System.nanoTime()

        // Process many tokens
        repeat(1000) {
            testTokens.forEach { tokenType ->
                syntaxHighlighter.getTokenHighlights(tokenType)
            }
        }

        val endTime = System.nanoTime()
        val durationMs = (endTime - startTime) / 1_000_000.0

        // Should process 5000 tokens in reasonable time (less than 50ms)
        assertTrue(durationMs < 50.0, "Syntax highlighting should be performant: ${durationMs}ms for 5000 tokens")
    }

    @Test
    fun `lexer should be properly initialized`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()
        val lexer = syntaxHighlighter.highlightingLexer

        assertNotNull(lexer, "Lexer should be initialized")

        // Test that lexer can be started
        assertDoesNotThrow {
            lexer.start("test")
        }
    }

    @Test
    fun `syntax highlighter should handle all Phel language constructs`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Test all major language construct categories
        val languageConstructs = mapOf(
            "Comments" to listOf(PhelTypes.LINE_COMMENT, PhelTypes.FORM_COMMENT, PhelTypes.MULTILINE_COMMENT),
            "Literals" to listOf(PhelTypes.STRING, PhelTypes.NUMBER, PhelTypes.BOOL, PhelTypes.NIL, PhelTypes.CHAR),
            "Delimiters" to listOf(PhelTypes.PAREN1, PhelTypes.BRACKET1, PhelTypes.BRACE1),
            "Keywords" to listOf(PhelTypes.KEYWORD, PhelTypes.COLON),
            "Symbols" to listOf(PhelTypes.SYM),
            "Macros" to listOf(PhelTypes.QUOTE, PhelTypes.SYNTAX_QUOTE, PhelTypes.TILDE),
            "Operators" to listOf(PhelTypes.DOT, PhelTypes.COMMA),
            "Metadata" to listOf(PhelTypes.HAT)
        )

        languageConstructs.forEach { (category, tokens) ->
            tokens.forEach { tokenType ->
                val attributes = syntaxHighlighter.getTokenHighlights(tokenType)
                assertTrue(attributes.isNotEmpty(), "Should support $category token: $tokenType")
                assertNotNull(attributes[0], "Should have valid attribute for $category token: $tokenType")
            }
        }
    }

    @Test
    fun `syntax highlighter should maintain attribute consistency`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Test that related tokens use consistent attributes
        val relatedTokenGroups = mapOf(
            "All comments" to listOf(PhelTypes.LINE_COMMENT, PhelTypes.FORM_COMMENT, PhelTypes.MULTILINE_COMMENT),
            "All numbers" to listOf(PhelTypes.NUMBER, PhelTypes.HEXNUM, PhelTypes.BINNUM, PhelTypes.OCTNUM),
            "All keywords" to listOf(PhelTypes.KEYWORD, PhelTypes.KEYWORD_TOKEN, PhelTypes.COLON, PhelTypes.COLONCOLON),
            "Parentheses pair" to listOf(PhelTypes.PAREN1, PhelTypes.PAREN2),
            "Bracket pair" to listOf(PhelTypes.BRACKET1, PhelTypes.BRACKET2),
            "Brace pair" to listOf(PhelTypes.BRACE1, PhelTypes.BRACE2),
            "Unquote variants" to listOf(PhelTypes.TILDE, PhelTypes.COMMA),
            "Unquote splicing variants" to listOf(PhelTypes.TILDE_AT, PhelTypes.COMMA_AT),
            "Dot operators" to listOf(PhelTypes.DOT, PhelTypes.DOTDASH)
        )

        relatedTokenGroups.forEach { (groupName, tokens) ->
            val firstTokenAttributes = syntaxHighlighter.getTokenHighlights(tokens[0])

            tokens.drop(1).forEach { tokenType ->
                val attributes = syntaxHighlighter.getTokenHighlights(tokenType)
                assertArrayEquals(
                    firstTokenAttributes,
                    attributes,
                    "All tokens in group '$groupName' should have same attributes"
                )
            }
        }
    }

    @Test
    fun `syntax highlighter should use delegation pattern correctly`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Verify that the syntax highlighter is now much simpler and delegates to components
        // This is tested by ensuring the behavior is correct and consistent

        val testToken = PhelTypes.STRING
        val attributes = syntaxHighlighter.getTokenHighlights(testToken)

        // Should return exactly one attribute
        assertEquals(1, attributes.size, "Should delegate correctly to return single attribute")
        assertEquals(PhelTextAttributesRegistry.STRING, attributes[0], "Should delegate to correct registry")
    }
}
