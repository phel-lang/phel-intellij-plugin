package org.phellang.unit.syntax.classification

import com.intellij.psi.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelTypes
import org.phellang.syntax.classification.PhelTokenClassifier
import org.phellang.syntax.classification.PhelTokenClassifier.TokenCategory

class PhelTokenClassifierTest {

    @Test
    fun `classifyToken should correctly classify comment tokens`() {
        assertEquals(TokenCategory.COMMENT, PhelTokenClassifier.classifyToken(PhelTypes.LINE_COMMENT))
        assertEquals(TokenCategory.COMMENT, PhelTokenClassifier.classifyToken(PhelTypes.FORM_COMMENT))
        assertEquals(TokenCategory.COMMENT, PhelTokenClassifier.classifyToken(PhelTypes.MULTILINE_COMMENT))
    }

    @Test
    fun `classifyToken should correctly classify string tokens`() {
        assertEquals(TokenCategory.STRING, PhelTokenClassifier.classifyToken(PhelTypes.STRING))
    }

    @Test
    fun `classifyToken should correctly classify number tokens`() {
        assertEquals(TokenCategory.NUMBER, PhelTokenClassifier.classifyToken(PhelTypes.NUMBER))
        assertEquals(TokenCategory.NUMBER, PhelTokenClassifier.classifyToken(PhelTypes.HEXNUM))
        assertEquals(TokenCategory.NUMBER, PhelTokenClassifier.classifyToken(PhelTypes.BINNUM))
        assertEquals(TokenCategory.NUMBER, PhelTokenClassifier.classifyToken(PhelTypes.OCTNUM))
    }

    @Test
    fun `classifyToken should correctly classify boolean tokens`() {
        assertEquals(TokenCategory.BOOLEAN, PhelTokenClassifier.classifyToken(PhelTypes.BOOL))
    }

    @Test
    fun `classifyToken should correctly classify nil tokens`() {
        assertEquals(TokenCategory.NIL, PhelTokenClassifier.classifyToken(PhelTypes.NIL))
    }

    @Test
    fun `classifyToken should correctly classify nan tokens`() {
        assertEquals(TokenCategory.NAN, PhelTokenClassifier.classifyToken(PhelTypes.NAN))
    }

    @Test
    fun `classifyToken should correctly classify character tokens`() {
        assertEquals(TokenCategory.CHARACTER, PhelTokenClassifier.classifyToken(PhelTypes.CHAR))
    }

    @Test
    fun `classifyToken should correctly classify delimiter tokens`() {
        // Parentheses
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.PAREN1))
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.PAREN2))
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.FN_SHORT))

        // Brackets
        assertEquals(TokenCategory.BRACKETS, PhelTokenClassifier.classifyToken(PhelTypes.BRACKET1))
        assertEquals(TokenCategory.BRACKETS, PhelTokenClassifier.classifyToken(PhelTypes.BRACKET2))

        // Braces
        assertEquals(TokenCategory.BRACES, PhelTokenClassifier.classifyToken(PhelTypes.BRACE1))
        assertEquals(TokenCategory.BRACES, PhelTokenClassifier.classifyToken(PhelTypes.BRACE2))
        assertEquals(TokenCategory.BRACES, PhelTokenClassifier.classifyToken(PhelTypes.HASH_BRACE))
    }

    @Test
    fun `classifyToken should correctly classify quote tokens`() {
        assertEquals(TokenCategory.QUOTE, PhelTokenClassifier.classifyToken(PhelTypes.QUOTE))
        assertEquals(TokenCategory.SYNTAX_QUOTE, PhelTokenClassifier.classifyToken(PhelTypes.SYNTAX_QUOTE))
    }

    @Test
    fun `classifyToken should correctly classify unquote tokens`() {
        assertEquals(TokenCategory.UNQUOTE, PhelTokenClassifier.classifyToken(PhelTypes.TILDE))
        assertEquals(TokenCategory.UNQUOTE, PhelTokenClassifier.classifyToken(PhelTypes.COMMA))
        assertEquals(TokenCategory.UNQUOTE_SPLICING, PhelTokenClassifier.classifyToken(PhelTypes.TILDE_AT))
        assertEquals(TokenCategory.UNQUOTE_SPLICING, PhelTokenClassifier.classifyToken(PhelTypes.COMMA_AT))
    }

    @Test
    fun `classifyToken should correctly classify keyword tokens`() {
        assertEquals(TokenCategory.KEYWORD, PhelTokenClassifier.classifyToken(PhelTypes.KEYWORD))
        assertEquals(TokenCategory.KEYWORD, PhelTokenClassifier.classifyToken(PhelTypes.KEYWORD_TOKEN))
        assertEquals(TokenCategory.KEYWORD, PhelTokenClassifier.classifyToken(PhelTypes.COLON))
        assertEquals(TokenCategory.KEYWORD, PhelTokenClassifier.classifyToken(PhelTypes.COLONCOLON))
    }

    @Test
    fun `classifyToken should correctly classify metadata tokens`() {
        assertEquals(TokenCategory.METADATA, PhelTokenClassifier.classifyToken(PhelTypes.HAT))
    }

    @Test
    fun `classifyToken should correctly classify operator tokens`() {
        assertEquals(TokenCategory.DOT_OPERATOR, PhelTokenClassifier.classifyToken(PhelTypes.DOT))
        assertEquals(TokenCategory.DOT_OPERATOR, PhelTokenClassifier.classifyToken(PhelTypes.DOTDASH))
        // Note: COMMA is classified as UNQUOTE in Phel (same as original implementation)
    }

    @Test
    fun `classifyToken should correctly classify symbol tokens`() {
        assertEquals(TokenCategory.SYMBOL, PhelTokenClassifier.classifyToken(PhelTypes.SYM))
    }

    @Test
    fun `classifyToken should correctly classify bad character tokens`() {
        assertEquals(TokenCategory.BAD_CHARACTER, PhelTokenClassifier.classifyToken(TokenType.BAD_CHARACTER))
    }

    @Test
    fun `individual classification methods should work correctly`() {
        // Test comment classification
        assertTrue(PhelTokenClassifier.isComment(PhelTypes.LINE_COMMENT))
        assertTrue(PhelTokenClassifier.isComment(PhelTypes.FORM_COMMENT))
        assertTrue(PhelTokenClassifier.isComment(PhelTypes.MULTILINE_COMMENT))
        assertFalse(PhelTokenClassifier.isComment(PhelTypes.STRING))

        // Test string classification
        assertTrue(PhelTokenClassifier.isString(PhelTypes.STRING))
        assertFalse(PhelTokenClassifier.isString(PhelTypes.NUMBER))

        // Test number classification
        assertTrue(PhelTokenClassifier.isNumber(PhelTypes.NUMBER))
        assertTrue(PhelTokenClassifier.isNumber(PhelTypes.HEXNUM))
        assertTrue(PhelTokenClassifier.isNumber(PhelTypes.BINNUM))
        assertTrue(PhelTokenClassifier.isNumber(PhelTypes.OCTNUM))
        assertFalse(PhelTokenClassifier.isNumber(PhelTypes.STRING))

        // Test boolean classification
        assertTrue(PhelTokenClassifier.isBoolean(PhelTypes.BOOL))
        assertFalse(PhelTokenClassifier.isBoolean(PhelTypes.NIL))

        // Test delimiter classification
        assertTrue(PhelTokenClassifier.isParentheses(PhelTypes.PAREN1))
        assertTrue(PhelTokenClassifier.isParentheses(PhelTypes.PAREN2))
        assertFalse(PhelTokenClassifier.isParentheses(PhelTypes.BRACKET1))

        assertTrue(PhelTokenClassifier.isBrackets(PhelTypes.BRACKET1))
        assertTrue(PhelTokenClassifier.isBrackets(PhelTypes.BRACKET2))
        assertFalse(PhelTokenClassifier.isBrackets(PhelTypes.PAREN1))

        assertTrue(PhelTokenClassifier.isBraces(PhelTypes.BRACE1))
        assertTrue(PhelTokenClassifier.isBraces(PhelTypes.BRACE2))
        assertFalse(PhelTokenClassifier.isBraces(PhelTypes.BRACKET1))
    }

    @Test
    fun `classification should be consistent across multiple calls`() {
        val testTokens = listOf(
            PhelTypes.LINE_COMMENT,
            PhelTypes.STRING,
            PhelTypes.NUMBER,
            PhelTypes.BOOL,
            PhelTypes.NIL,
            PhelTypes.PAREN1,
            PhelTypes.BRACKET1,
            PhelTypes.BRACE1,
            PhelTypes.QUOTE,
            PhelTypes.KEYWORD,
            PhelTypes.SYM,
            TokenType.BAD_CHARACTER
        )

        testTokens.forEach { token ->
            val firstClassification = PhelTokenClassifier.classifyToken(token)
            val secondClassification = PhelTokenClassifier.classifyToken(token)
            assertEquals(
                firstClassification, secondClassification, "Classification should be consistent for token: $token"
            )
        }
    }

    @Test
    fun `token categories should have meaningful names`() {
        TokenCategory.entries.forEach { category ->
            assertTrue(category.name.isNotEmpty(), "Category name should not be empty")
            assertTrue(category.name.matches(Regex("[A-Z_]+")), "Category name should be uppercase with underscores")
        }
    }

    @Test
    fun `classification methods should be mutually exclusive for different token types`() {
        val testToken = PhelTypes.STRING

        // A string token should only be classified as string, not as other types
        assertTrue(PhelTokenClassifier.isString(testToken))
        assertFalse(PhelTokenClassifier.isComment(testToken))
        assertFalse(PhelTokenClassifier.isNumber(testToken))
        assertFalse(PhelTokenClassifier.isBoolean(testToken))
        assertFalse(PhelTokenClassifier.isNil(testToken))
        assertFalse(PhelTokenClassifier.isNan(testToken))
        assertFalse(PhelTokenClassifier.isCharacter(testToken))
        assertFalse(PhelTokenClassifier.isParentheses(testToken))
        assertFalse(PhelTokenClassifier.isBrackets(testToken))
        assertFalse(PhelTokenClassifier.isBraces(testToken))
        assertFalse(PhelTokenClassifier.isQuote(testToken))
        assertFalse(PhelTokenClassifier.isSyntaxQuote(testToken))
        assertFalse(PhelTokenClassifier.isUnquote(testToken))
        assertFalse(PhelTokenClassifier.isUnquoteSplicing(testToken))
        assertFalse(PhelTokenClassifier.isKeyword(testToken))
        assertFalse(PhelTokenClassifier.isMetadata(testToken))
        assertFalse(PhelTokenClassifier.isDotOperator(testToken))
        assertFalse(PhelTokenClassifier.isComma(testToken))
        assertFalse(PhelTokenClassifier.isSymbol(testToken))
        assertFalse(PhelTokenClassifier.isBadCharacter(testToken))
    }

    @Test
    fun `set and short function openers should have dedicated check methods`() {
        // Compound tokens have semantic check methods
        assertTrue(PhelTokenClassifier.isSetOpener(PhelTypes.HASH_BRACE), "isSetOpener should recognize #{")
        assertTrue(PhelTokenClassifier.isShortFnOpener(PhelTypes.FN_SHORT), "isShortFnOpener should recognize |(")
        
        // But they classify as their visual equivalents for styling
        assertEquals(TokenCategory.BRACES, PhelTokenClassifier.classifyToken(PhelTypes.HASH_BRACE))
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.FN_SHORT))
        
        // And they're NOT regular delimiters
        assertFalse(PhelTokenClassifier.isBraces(PhelTypes.HASH_BRACE), "#{ is a set opener, not a regular brace")
        assertFalse(PhelTokenClassifier.isParentheses(PhelTypes.FN_SHORT), "|( is a short fn opener, not a regular paren")
    }
}
