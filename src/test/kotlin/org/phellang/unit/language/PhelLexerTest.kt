package org.phellang.unit.language

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.phellang.language.PhelLexer
import org.phellang.language.psi.PhelTypes

class PhelLexerTest {

    private fun tokenize(input: String): List<Pair<IElementType, String>> {
        val lexer = PhelLexer(null)
        lexer.reset(input, 0, input.length, PhelLexer.YYINITIAL)
        val tokens = mutableListOf<Pair<IElementType, String>>()
        while (true) {
            val type = lexer.advance() ?: break
            val text = input.substring(lexer.tokenStart, lexer.tokenEnd)
            tokens.add(type to text)
        }
        return tokens
    }

    private fun tokenTypes(input: String): List<IElementType> {
        return tokenize(input).map { it.first }
    }

    // --- New short function syntax #( ---

    @Test
    fun `hash paren should tokenize as HASH_PAREN`() {
        val tokens = tokenize("#(+ % %2)")
        assertEquals(PhelTypes.HASH_PAREN, tokens[0].first)
        assertEquals("#(", tokens[0].second)
        assertEquals(PhelTypes.PAREN2, tokens.last().first)
    }

    // --- Reader conditionals ---

    @Test
    fun `reader conditional should tokenize as READER_COND`() {
        val tokens = tokenize("#?(:phel 1)")
        assertEquals(PhelTypes.READER_COND, tokens[0].first)
        assertEquals("#?(", tokens[0].second)
    }

    @Test
    fun `reader conditional splice should tokenize as READER_COND_SPLICE`() {
        val tokens = tokenize("#?@(:phel [1 2])")
        assertEquals(PhelTypes.READER_COND_SPLICE, tokens[0].first)
        assertEquals("#?@(", tokens[0].second)
    }

    // --- Regex literals ---

    @Test
    fun `regex literal should tokenize as REGEX_START and REGEX_BODY`() {
        val tokens = tokenize("""#"[a-z]+"""")
        assertEquals(PhelTypes.REGEX_START, tokens[0].first)
        assertEquals("#\"", tokens[0].second)
        assertEquals(PhelTypes.REGEX_BODY, tokens[1].first)
        assertEquals("[a-z]+\"", tokens[1].second)
    }

    @Test
    fun `regex literal with escapes should tokenize correctly`() {
        val tokens = tokenize("""#"foo\"bar"""")
        assertEquals(PhelTypes.REGEX_START, tokens[0].first)
        assertEquals(PhelTypes.REGEX_BODY, tokens[1].first)
    }

    // --- Deref ---

    @Test
    fun `at sign should tokenize as DEREF`() {
        val tokens = tokenize("@my-atom")
        assertEquals(PhelTypes.DEREF, tokens[0].first)
        assertEquals("@", tokens[0].second)
        assertEquals(PhelTypes.SYM, tokens[1].first)
    }

    @Test
    fun `tilde-at should still tokenize as TILDE_AT not DEREF`() {
        val tokens = tokenize("~@forms")
        assertEquals(PhelTypes.TILDE_AT, tokens[0].first)
        assertEquals("~@", tokens[0].second)
    }

    @Test
    fun `comma is treated as whitespace not unquote-splicing`() {
        val tokens = tokenize(",@forms")
        // Comma is whitespace in Phel; ,@ tokenizes as WHITE_SPACE then DEREF then symbol
        assertEquals(TokenType.WHITE_SPACE, tokens[0].first)
        assertEquals(PhelTypes.DEREF, tokens[1].first)
        assertEquals("@", tokens[1].second)
    }

    // --- Var quote ---

    @Test
    fun `hash-quote should tokenize as VAR_QUOTE`() {
        val tokens = tokenize("#'my-fn")
        assertEquals(PhelTypes.VAR_QUOTE, tokens[0].first)
        assertEquals("#'", tokens[0].second)
        assertEquals(PhelTypes.SYM, tokens[1].first)
    }

    // --- Symbolic numbers ---

    @Test
    fun `symbolic Inf should tokenize as SYMBOLIC_NUM`() {
        val tokens = tokenize("##Inf")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.SYMBOLIC_NUM, tokens[0].first)
        assertEquals("##Inf", tokens[0].second)
    }

    @Test
    fun `symbolic negative Inf should tokenize as SYMBOLIC_NUM`() {
        val tokens = tokenize("##-Inf")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.SYMBOLIC_NUM, tokens[0].first)
        assertEquals("##-Inf", tokens[0].second)
    }

    @Test
    fun `symbolic NaN should tokenize as SYMBOLIC_NUM`() {
        val tokens = tokenize("##NaN")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.SYMBOLIC_NUM, tokens[0].first)
        assertEquals("##NaN", tokens[0].second)
    }

    @Test
    fun `malformed symbolic number should tokenize as BAD_CHARACTER`() {
        val types = tokenTypes("##foo")
        assertEquals(1, types.size)
        assertEquals("BAD_CHARACTER", types[0].toString())
    }

    // --- Radix literals ---

    @Test
    fun `binary radix literal should tokenize as RADIXNUM`() {
        val tokens = tokenize("2r1010")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.RADIXNUM, tokens[0].first)
        assertEquals("2r1010", tokens[0].second)
    }

    @Test
    fun `hex radix literal should tokenize as RADIXNUM`() {
        val tokens = tokenize("16rFF")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.RADIXNUM, tokens[0].first)
    }

    @Test
    fun `base36 radix literal should tokenize as RADIXNUM`() {
        val tokens = tokenize("36rZZ")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.RADIXNUM, tokens[0].first)
    }

    // --- BigInt and BigDecimal suffixes ---

    @Test
    fun `integer with N suffix should tokenize as NUMBER`() {
        val tokens = tokenize("42N")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.NUMBER, tokens[0].first)
        assertEquals("42N", tokens[0].second)
    }

    @Test
    fun `decimal with M suffix should tokenize as NUMBER`() {
        val tokens = tokenize("3.14M")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.NUMBER, tokens[0].first)
        assertEquals("3.14M", tokens[0].second)
    }

    @Test
    fun `hex with N suffix should tokenize as HEXNUM`() {
        val tokens = tokenize("0xFFN")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.HEXNUM, tokens[0].first)
    }

    // --- Symbol names with ' and # ---

    @Test
    fun `symbol with trailing hash for auto-gensym should tokenize as single SYM`() {
        val tokens = tokenize("x#")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.SYM, tokens[0].first)
        assertEquals("x#", tokens[0].second)
    }

    @Test
    fun `symbol with embedded quote should tokenize as single SYM`() {
        val tokens = tokenize("a'b")
        assertEquals(1, tokens.size)
        assertEquals(PhelTypes.SYM, tokens[0].first)
        assertEquals("a'b", tokens[0].second)
    }

    @Test
    fun `leading quote is still QUOTE not part of symbol`() {
        val tokens = tokenize("'foo")
        assertEquals(PhelTypes.QUOTE, tokens[0].first)
        assertEquals("'", tokens[0].second)
        assertEquals(PhelTypes.SYM, tokens[1].first)
        assertEquals("foo", tokens[1].second)
    }

    // --- Tokens that are NOT reader macros (regression tests for removed-syntax handling) ---

    @Test
    fun `pipe character is now a plain symbol not a short-fn opener`() {
        val tokens = tokenize("|(foo)")
        // | is now part of a symbol; (foo) remains a list
        assertEquals(PhelTypes.SYM, tokens[0].first)
    }

    @Test
    fun `comma is now treated as whitespace not an unquote reader macro`() {
        val tokens = tokenize(",x")
        // Comma is whitespace; [0] is WHITE_SPACE, [1] is the symbol x
        assertEquals(TokenType.WHITE_SPACE, tokens[0].first)
        assertEquals(PhelTypes.SYM, tokens[1].first)
        assertEquals("x", tokens[1].second)
    }

    // --- Tagged literal dispatch: #inst, #uuid, #regex, #php, generic #<name> ---

    @Test
    fun `hash-inst tokenizes as TAG`() {
        val tokens = tokenize("#inst \"2020-01-01\"")
        assertEquals(PhelTypes.TAG, tokens[0].first)
        assertEquals("#inst", tokens[0].second)
    }

    @Test
    fun `hash-uuid tokenizes as TAG`() {
        val tokens = tokenize("#uuid \"abc\"")
        assertEquals(PhelTypes.TAG, tokens[0].first)
        assertEquals("#uuid", tokens[0].second)
    }

    @Test
    fun `hash-php tokenizes as TAG`() {
        val tokens = tokenize("#php [1 2 3]")
        assertEquals(PhelTypes.TAG, tokens[0].first)
        assertEquals("#php", tokens[0].second)
    }

    // --- Existing syntax preserved ---

    @Test
    fun `set literal should still tokenize correctly`() {
        val tokens = tokenize("#{1 2}")
        assertEquals(PhelTypes.HASH_BRACE, tokens[0].first)
    }

    @Test
    fun `form comment should still tokenize correctly`() {
        val tokens = tokenize("#_foo")
        assertEquals(PhelTypes.FORM_COMMENT, tokens[0].first)
    }

    @Test
    fun `tilde unquote should tokenize correctly`() {
        val tokens = tokenize("~x")
        assertEquals(PhelTypes.TILDE, tokens[0].first)
    }

    @Test
    fun `semicolon line comment should tokenize correctly`() {
        val tokens = tokenize("; a comment")
        assertEquals(PhelTypes.LINE_COMMENT, tokens[0].first)
    }
}
