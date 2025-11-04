package org.phellang.integration.syntax

import com.intellij.psi.TokenType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.phellang.syntax.PhelSyntaxHighlighter
import org.phellang.language.psi.PhelTypes
import org.phellang.language.lexer.PhelLexerAdapter
import org.phellang.syntax.attributes.PhelTextAttributesRegistry
import org.phellang.syntax.mapping.PhelTokenAttributeMapper
import org.phellang.syntax.classification.PhelTokenClassifier

class PhelSyntaxHighlightingWorkflowTest {

    @Test
    fun `complete workflow should work for all token types`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Test the complete workflow: token -> classification -> mapping -> highlighting
        val testCases = mapOf(
            PhelTypes.LINE_COMMENT to PhelTextAttributesRegistry.COMMENT,
            PhelTypes.STRING to PhelTextAttributesRegistry.STRING,
            PhelTypes.NUMBER to PhelTextAttributesRegistry.NUMBER,
            PhelTypes.BOOL to PhelTextAttributesRegistry.BOOLEAN,
            PhelTypes.NIL to PhelTextAttributesRegistry.NIL_LITERAL,
            PhelTypes.NAN to PhelTextAttributesRegistry.NAN_LITERAL,
            PhelTypes.CHAR to PhelTextAttributesRegistry.CHARACTER,
            PhelTypes.PAREN1 to PhelTextAttributesRegistry.PARENTHESES,
            PhelTypes.BRACKET1 to PhelTextAttributesRegistry.BRACKETS,
            PhelTypes.BRACE1 to PhelTextAttributesRegistry.BRACES,
            PhelTypes.QUOTE to PhelTextAttributesRegistry.QUOTE,
            PhelTypes.SYNTAX_QUOTE to PhelTextAttributesRegistry.SYNTAX_QUOTE,
            PhelTypes.TILDE to PhelTextAttributesRegistry.UNQUOTE,
            PhelTypes.TILDE_AT to PhelTextAttributesRegistry.UNQUOTE_SPLICING,
            PhelTypes.KEYWORD to PhelTextAttributesRegistry.KEYWORD,
            PhelTypes.HAT to PhelTextAttributesRegistry.METADATA,
            PhelTypes.DOT to PhelTextAttributesRegistry.DOT_OPERATOR,
            PhelTypes.COMMA to PhelTextAttributesRegistry.UNQUOTE, // COMMA is treated as UNQUOTE in Phel
            PhelTypes.SYM to PhelTextAttributesRegistry.SYMBOL,
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
    fun `workflow should be consistent between direct and indirect access`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()
        val testTokens = listOf(
            PhelTypes.LINE_COMMENT, PhelTypes.STRING, PhelTypes.NUMBER, PhelTypes.KEYWORD, PhelTypes.SYM
        )

        testTokens.forEach { tokenType ->
            // Direct workflow through syntax highlighter
            val directAttributes = syntaxHighlighter.getTokenHighlights(tokenType)

            // Indirect workflow through components
            val category = PhelTokenClassifier.classifyToken(tokenType)
            val indirectAttributes = PhelTokenAttributeMapper.getTextAttributes(category)

            assertArrayEquals(
                directAttributes, indirectAttributes, "Direct and indirect workflows should match for token: $tokenType"
            )
        }
    }

    @Test
    fun `lexer integration should work correctly`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()
        val lexer = syntaxHighlighter.highlightingLexer

        assertNotNull(lexer, "Lexer should not be null")

        // Test lexer with simple Phel code
        val testCode = "(defn hello [name] (str \"Hello, \" name))"
        lexer.start(testCode)

        var tokenCount = 0
        while (lexer.tokenType != null) {
            val tokenType = lexer.tokenType!!
            val attributes = syntaxHighlighter.getTokenHighlights(tokenType)

            assertNotNull(attributes, "Attributes should not be null for token: $tokenType")
            tokenCount++

            lexer.advance()
        }

        assertTrue(tokenCount > 0, "Should have processed some tokens")
    }

    @ParameterizedTest
    @CsvSource(
        "LINE_COMMENT, COMMENT",
        "STRING, STRING",
        "NUMBER, NUMBER",
        "BOOL, BOOLEAN",
        "NIL, NIL_LITERAL",
        "KEYWORD, KEYWORD",
        "SYM, SYMBOL"
    )
    fun `workflow should map tokens to correct attributes`(tokenTypeName: String, expectedAttributeName: String) {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Get token type by name
        val tokenType = when (tokenTypeName) {
            "LINE_COMMENT" -> PhelTypes.LINE_COMMENT
            "STRING" -> PhelTypes.STRING
            "NUMBER" -> PhelTypes.NUMBER
            "BOOL" -> PhelTypes.BOOL
            "NIL" -> PhelTypes.NIL
            "KEYWORD" -> PhelTypes.KEYWORD
            "SYM" -> PhelTypes.SYM
            else -> throw IllegalArgumentException("Unknown token type: $tokenTypeName")
        }

        // Get expected attribute by name
        val expectedAttribute = when (expectedAttributeName) {
            "COMMENT" -> PhelTextAttributesRegistry.COMMENT
            "STRING" -> PhelTextAttributesRegistry.STRING
            "NUMBER" -> PhelTextAttributesRegistry.NUMBER
            "BOOLEAN" -> PhelTextAttributesRegistry.BOOLEAN
            "NIL_LITERAL" -> PhelTextAttributesRegistry.NIL_LITERAL
            "KEYWORD" -> PhelTextAttributesRegistry.KEYWORD
            "SYMBOL" -> PhelTextAttributesRegistry.SYMBOL
            else -> throw IllegalArgumentException("Unknown attribute: $expectedAttributeName")
        }

        val attributes = syntaxHighlighter.getTokenHighlights(tokenType)
        assertEquals(
            expectedAttribute, attributes[0], "Token $tokenTypeName should map to attribute $expectedAttributeName"
        )
    }

    @Test
    fun `workflow should handle all delimiter pairs correctly`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        val delimiterPairs = mapOf(
            PhelTypes.PAREN1 to PhelTypes.PAREN2,
            PhelTypes.BRACKET1 to PhelTypes.BRACKET2,
            PhelTypes.BRACE1 to PhelTypes.BRACE2
        )

        delimiterPairs.forEach { (openDelim, closeDelim) ->
            val openAttributes = syntaxHighlighter.getTokenHighlights(openDelim)
            val closeAttributes = syntaxHighlighter.getTokenHighlights(closeDelim)

            assertArrayEquals(
                openAttributes,
                closeAttributes,
                "Matching delimiters should have same attributes: $openDelim and $closeDelim"
            )
        }
    }

    @Test
    fun `workflow should handle all number types consistently`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        val numberTypes = listOf(
            PhelTypes.NUMBER, PhelTypes.HEXNUM, PhelTypes.BINNUM, PhelTypes.OCTNUM
        )

        val expectedAttribute = PhelTextAttributesRegistry.NUMBER

        numberTypes.forEach { numberType ->
            val attributes = syntaxHighlighter.getTokenHighlights(numberType)
            assertEquals(1, attributes.size, "Should have one attribute for number type: $numberType")
            assertEquals(expectedAttribute, attributes[0], "All number types should use NUMBER attribute")
        }
    }

    @Test
    fun `workflow should handle all comment types consistently`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        val commentTypes = listOf(
            PhelTypes.LINE_COMMENT, PhelTypes.FORM_COMMENT, PhelTypes.MULTILINE_COMMENT
        )

        val expectedAttribute = PhelTextAttributesRegistry.COMMENT

        commentTypes.forEach { commentType ->
            val attributes = syntaxHighlighter.getTokenHighlights(commentType)
            assertEquals(1, attributes.size, "Should have one attribute for comment type: $commentType")
            assertEquals(expectedAttribute, attributes[0], "All comment types should use COMMENT attribute")
        }
    }

    @Test
    fun `workflow should handle all keyword variations consistently`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        val keywordTypes = listOf(
            PhelTypes.KEYWORD, PhelTypes.KEYWORD_TOKEN, PhelTypes.COLON, PhelTypes.COLONCOLON
        )

        val expectedAttribute = PhelTextAttributesRegistry.KEYWORD

        keywordTypes.forEach { keywordType ->
            val attributes = syntaxHighlighter.getTokenHighlights(keywordType)
            assertEquals(1, attributes.size, "Should have one attribute for keyword type: $keywordType")
            assertEquals(expectedAttribute, attributes[0], "All keyword types should use KEYWORD attribute")
        }
    }

    @Test
    fun `workflow should handle quote variations correctly`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        val quoteTestCases = mapOf(
            PhelTypes.QUOTE to PhelTextAttributesRegistry.QUOTE,
            PhelTypes.SYNTAX_QUOTE to PhelTextAttributesRegistry.SYNTAX_QUOTE,
            PhelTypes.TILDE to PhelTextAttributesRegistry.UNQUOTE,
            PhelTypes.COMMA to PhelTextAttributesRegistry.UNQUOTE, // COMMA is also unquote
            PhelTypes.TILDE_AT to PhelTextAttributesRegistry.UNQUOTE_SPLICING,
            PhelTypes.COMMA_AT to PhelTextAttributesRegistry.UNQUOTE_SPLICING
        )

        quoteTestCases.forEach { (tokenType, expectedAttribute) ->
            val attributes = syntaxHighlighter.getTokenHighlights(tokenType)
            assertEquals(1, attributes.size, "Should have one attribute for quote type: $tokenType")
            assertEquals(expectedAttribute, attributes[0], "Quote type $tokenType should use correct attribute")
        }
    }

    @Test
    fun `workflow should be performant with many tokens`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()
        val testTokens = listOf(
            PhelTypes.LINE_COMMENT, PhelTypes.STRING, PhelTypes.NUMBER, PhelTypes.KEYWORD, PhelTypes.SYM
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
        assertTrue(durationMs < 50.0, "Workflow should be performant: ${durationMs}ms for 5000 tokens")
    }

    @Test
    fun `workflow should use new architecture consistently`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Test that highlighting works with new architecture
        val commentAttributes = syntaxHighlighter.getTokenHighlights(PhelTypes.LINE_COMMENT)
        assertEquals(PhelTextAttributesRegistry.COMMENT, commentAttributes[0])

        val stringAttributes = syntaxHighlighter.getTokenHighlights(PhelTypes.STRING)
        assertEquals(PhelTextAttributesRegistry.STRING, stringAttributes[0])

        val numberAttributes = syntaxHighlighter.getTokenHighlights(PhelTypes.NUMBER)
        assertEquals(PhelTextAttributesRegistry.NUMBER, numberAttributes[0])

        val keywordAttributes = syntaxHighlighter.getTokenHighlights(PhelTypes.KEYWORD)
        assertEquals(PhelTextAttributesRegistry.KEYWORD, keywordAttributes[0])

        val symbolAttributes = syntaxHighlighter.getTokenHighlights(PhelTypes.SYM)
        assertEquals(PhelTextAttributesRegistry.SYMBOL, symbolAttributes[0])
    }

    @Test
    fun `workflow components should be properly integrated`() {
        // 1. Lexer is properly initialized
        val lexer = PhelLexerAdapter()
        assertNotNull(lexer)

        // 2. Classifier categorizes tokens
        val category = PhelTokenClassifier.classifyToken(PhelTypes.STRING)
        assertEquals(PhelTokenClassifier.TokenCategory.STRING, category)

        // 3. Mapper provides attributes for categories
        val attributes = PhelTokenAttributeMapper.getTextAttributes(category)
        assertEquals(1, attributes.size)
        assertEquals(PhelTextAttributesRegistry.STRING, attributes[0])

        // 4. Syntax highlighter coordinates everything
        val syntaxHighlighter = PhelSyntaxHighlighter()
        val highlighterAttributes = syntaxHighlighter.getTokenHighlights(PhelTypes.STRING)
        assertArrayEquals(attributes, highlighterAttributes)
    }

    @Test
    fun `workflow should handle edge cases gracefully`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Test with bad character token
        val badCharAttributes = syntaxHighlighter.getTokenHighlights(TokenType.BAD_CHARACTER)
        assertEquals(1, badCharAttributes.size)
        assertEquals(PhelTextAttributesRegistry.BAD_CHARACTER, badCharAttributes[0])

        // Test consistency across multiple calls
        val firstCall = syntaxHighlighter.getTokenHighlights(PhelTypes.STRING)
        val secondCall = syntaxHighlighter.getTokenHighlights(PhelTypes.STRING)
        assertArrayEquals(firstCall, secondCall)
    }

    @Test
    fun `workflow should provide comprehensive language support`() {
        val syntaxHighlighter = PhelSyntaxHighlighter()

        // Verify that all major Phel language constructs are supported
        val languageConstructs = mapOf(
            "Comments" to PhelTypes.LINE_COMMENT,
            "Strings" to PhelTypes.STRING,
            "Numbers" to PhelTypes.NUMBER,
            "Booleans" to PhelTypes.BOOL,
            "Keywords" to PhelTypes.KEYWORD,
            "Symbols" to PhelTypes.SYM,
            "Lists" to PhelTypes.PAREN1,
            "Vectors" to PhelTypes.BRACKET1,
            "Maps" to PhelTypes.BRACE1,
            "Quotes" to PhelTypes.QUOTE,
            "Metadata" to PhelTypes.HAT
        )

        languageConstructs.forEach { (constructName, tokenType) ->
            val attributes = syntaxHighlighter.getTokenHighlights(tokenType)
            assertTrue(attributes.isNotEmpty(), "Should support $constructName")
            assertNotNull(attributes[0], "Should have valid attribute for $constructName")
        }
    }
}
