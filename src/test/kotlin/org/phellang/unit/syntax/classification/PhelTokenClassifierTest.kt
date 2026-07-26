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
        assertEquals(TokenCategory.NUMBER, PhelTokenClassifier.classifyToken(PhelTypes.RADIXNUM))
        assertEquals(TokenCategory.NUMBER, PhelTokenClassifier.classifyToken(PhelTypes.SYMBOLIC_NUM))
        assertEquals(TokenCategory.NUMBER, PhelTokenClassifier.classifyToken(PhelTypes.RATIO))
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
    fun `classifyToken should correctly classify regex tokens`() {
        assertEquals(TokenCategory.REGEX, PhelTokenClassifier.classifyToken(PhelTypes.REGEX_START))
        assertEquals(TokenCategory.REGEX, PhelTokenClassifier.classifyToken(PhelTypes.REGEX_BODY))
    }

    @Test
    fun `classifyToken should correctly classify deref tokens`() {
        assertEquals(TokenCategory.DEREF, PhelTokenClassifier.classifyToken(PhelTypes.DEREF))
    }

    @Test
    fun `classifyToken should correctly classify delimiter tokens`() {
        // Parentheses
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.PAREN1))
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.PAREN2))
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.HASH_PAREN))
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.READER_COND))
        assertEquals(TokenCategory.PARENTHESES, PhelTokenClassifier.classifyToken(PhelTypes.READER_COND_SPLICE))

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
        assertEquals(TokenCategory.QUOTE, PhelTokenClassifier.classifyToken(PhelTypes.VAR_QUOTE))
        assertEquals(TokenCategory.SYNTAX_QUOTE, PhelTokenClassifier.classifyToken(PhelTypes.SYNTAX_QUOTE))
    }

    @Test
    fun `classifyToken should correctly classify unquote tokens`() {
        assertEquals(TokenCategory.UNQUOTE, PhelTokenClassifier.classifyToken(PhelTypes.TILDE))
        assertEquals(TokenCategory.UNQUOTE_SPLICING, PhelTokenClassifier.classifyToken(PhelTypes.TILDE_AT))
    }

    @Test
    fun `classifyToken should correctly classify tagged literal dispatch`() {
        assertEquals(TokenCategory.TAG, PhelTokenClassifier.classifyToken(PhelTypes.TAG))
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

    /** Each token type belongs to exactly one category, which is what makes the map lookup valid. */
    @Test
    fun `a token classifies into exactly one category`() {
        val sampled = listOf(
            PhelTypes.STRING, PhelTypes.LINE_COMMENT, PhelTypes.NUMBER, PhelTypes.BOOL,
            PhelTypes.PAREN1, PhelTypes.BRACKET1, PhelTypes.BRACE1, PhelTypes.HASH_BRACE,
            PhelTypes.KEYWORD, PhelTypes.SYM, PhelTypes.HAT, PhelTypes.DOT,
        )

        sampled.forEach { token ->
            val matches = TokenCategory.entries.filter { PhelTokenClassifier.classifyToken(token) == it }
            assertEquals(1, matches.size, "$token should land in exactly one category, got $matches")
        }
    }

    /**
     * `#{` opens a set, but for styling it is a brace like any other.
     *
     * There used to be a dedicated `isSetOpener` predicate alongside an `isBraces` that excluded
     * `HASH_BRACE` — a second, disagreeing encoding of the category map. The map is the only answer
     * now, and this pins the one it gives.
     */
    @Test
    fun `the set opener is styled as a brace`() {
        assertEquals(TokenCategory.BRACES, PhelTokenClassifier.classifyToken(PhelTypes.HASH_BRACE))
    }

    /** An unmapped token falls through to UNKNOWN rather than to a wrong category. */
    @Test
    fun `an unmapped token is unknown`() {
        assertEquals(TokenCategory.UNKNOWN, PhelTokenClassifier.classifyToken(TokenType.WHITE_SPACE))
    }
}
