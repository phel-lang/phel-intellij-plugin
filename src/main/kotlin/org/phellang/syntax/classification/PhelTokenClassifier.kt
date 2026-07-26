package org.phellang.syntax.classification

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.phellang.language.psi.PhelTypes

object PhelTokenClassifier {
    enum class TokenCategory {
        COMMENT, STRING, NUMBER, BOOLEAN, NIL, NAN, CHARACTER, PARENTHESES, BRACKETS, BRACES, QUOTE, SYNTAX_QUOTE,
        UNQUOTE, UNQUOTE_SPLICING, KEYWORD, METADATA, DOT_OPERATOR, SYMBOL, BAD_CHARACTER, UNKNOWN, REGEX, DEREF, TAG
    }

    // Token types are interned singletons, so a map lookup classifies each token in O(1) on
    // the syntax-highlighting hot path; each token type belongs to exactly one category.
    private val CATEGORY_BY_TOKEN: Map<IElementType, TokenCategory> = buildMap {
        put(PhelTypes.LINE_COMMENT, TokenCategory.COMMENT)
        put(PhelTypes.FORM_COMMENT, TokenCategory.COMMENT)
        put(PhelTypes.STRING, TokenCategory.STRING)
        put(PhelTypes.REGEX_START, TokenCategory.REGEX)
        put(PhelTypes.REGEX_BODY, TokenCategory.REGEX)
        put(PhelTypes.NUMBER, TokenCategory.NUMBER)
        put(PhelTypes.BINNUM, TokenCategory.NUMBER)
        put(PhelTypes.OCTNUM, TokenCategory.NUMBER)
        put(PhelTypes.HEXNUM, TokenCategory.NUMBER)
        put(PhelTypes.RADIXNUM, TokenCategory.NUMBER)
        put(PhelTypes.SYMBOLIC_NUM, TokenCategory.NUMBER)
        put(PhelTypes.RATIO, TokenCategory.NUMBER)
        put(PhelTypes.BOOL, TokenCategory.BOOLEAN)
        put(PhelTypes.NIL, TokenCategory.NIL)
        put(PhelTypes.NAN, TokenCategory.NAN)
        put(PhelTypes.CHAR, TokenCategory.CHARACTER)
        put(PhelTypes.PAREN1, TokenCategory.PARENTHESES)
        put(PhelTypes.PAREN2, TokenCategory.PARENTHESES)
        put(PhelTypes.HASH_PAREN, TokenCategory.PARENTHESES)
        put(PhelTypes.READER_COND, TokenCategory.PARENTHESES)
        put(PhelTypes.READER_COND_SPLICE, TokenCategory.PARENTHESES)
        put(PhelTypes.BRACKET1, TokenCategory.BRACKETS)
        put(PhelTypes.BRACKET2, TokenCategory.BRACKETS)
        put(PhelTypes.BRACE1, TokenCategory.BRACES)
        put(PhelTypes.BRACE2, TokenCategory.BRACES)
        put(PhelTypes.HASH_BRACE, TokenCategory.BRACES)
        put(PhelTypes.QUOTE, TokenCategory.QUOTE)
        put(PhelTypes.VAR_QUOTE, TokenCategory.QUOTE)
        put(PhelTypes.SYNTAX_QUOTE, TokenCategory.SYNTAX_QUOTE)
        put(PhelTypes.DEREF, TokenCategory.DEREF)
        put(PhelTypes.TILDE, TokenCategory.UNQUOTE)
        put(PhelTypes.TILDE_AT, TokenCategory.UNQUOTE_SPLICING)
        put(PhelTypes.TAG, TokenCategory.TAG)
        put(PhelTypes.KEYWORD, TokenCategory.KEYWORD)
        put(PhelTypes.KEYWORD_TOKEN, TokenCategory.KEYWORD)
        put(PhelTypes.COLON, TokenCategory.KEYWORD)
        put(PhelTypes.COLONCOLON, TokenCategory.KEYWORD)
        put(PhelTypes.HAT, TokenCategory.METADATA)
        put(PhelTypes.DOT, TokenCategory.DOT_OPERATOR)
        put(PhelTypes.DOTDASH, TokenCategory.DOT_OPERATOR)
        put(PhelTypes.SYM, TokenCategory.SYMBOL)
    }

    fun classifyToken(tokenType: IElementType): TokenCategory {
        CATEGORY_BY_TOKEN[tokenType]?.let { return it }
        return if (isBadCharacter(tokenType)) TokenCategory.BAD_CHARACTER else TokenCategory.UNKNOWN
    }

    /**
     * Matched by name as well as by identity: the lexer's own bad-character token is not always the
     * platform's, so comparing against [TokenType.BAD_CHARACTER] alone misses it.
     */
    private fun isBadCharacter(tokenType: IElementType): Boolean {
        val tokenName = tokenType.toString()
        return "BAD_CHARACTER" == tokenName || tokenType == TokenType.BAD_CHARACTER
    }
}
