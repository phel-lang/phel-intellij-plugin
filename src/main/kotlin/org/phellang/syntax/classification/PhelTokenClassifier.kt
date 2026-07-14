package org.phellang.syntax.classification

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.phellang.language.psi.PhelTypes

object PhelTokenClassifier {

    enum class TokenCategory {
        COMMENT, STRING, NUMBER, BOOLEAN, NIL, NAN, CHARACTER, PARENTHESES, BRACKETS, BRACES, QUOTE, SYNTAX_QUOTE,
        UNQUOTE, UNQUOTE_SPLICING, KEYWORD, METADATA, DOT_OPERATOR, SYMBOL, BAD_CHARACTER, UNKNOWN, REGEX, DEREF, TAG
    }

    // Token types are interned singletons, so a direct map lookup classifies each token in
    // O(1) instead of the sequential `when` chain that ran up to ~22 comparisons per token
    // on the syntax-highlighting hot path. Each token type belongs to exactly one category,
    // so the map is equivalent to the first-match ordering of the previous chain.
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

    fun isComment(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.LINE_COMMENT || tokenType == PhelTypes.FORM_COMMENT
    }

    fun isString(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.STRING
    }


    fun isNumber(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.NUMBER || tokenType == PhelTypes.BINNUM
                || tokenType == PhelTypes.OCTNUM || tokenType == PhelTypes.HEXNUM
                || tokenType == PhelTypes.RADIXNUM || tokenType == PhelTypes.SYMBOLIC_NUM
                || tokenType == PhelTypes.RATIO
    }

    fun isBoolean(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.BOOL
    }

    fun isNil(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.NIL
    }

    fun isNan(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.NAN
    }

    fun isCharacter(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.CHAR
    }

    fun isParentheses(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.PAREN1 || tokenType == PhelTypes.PAREN2 || tokenType == PhelTypes.HASH_PAREN
                || tokenType == PhelTypes.READER_COND || tokenType == PhelTypes.READER_COND_SPLICE
    }

    fun isBrackets(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.BRACKET1 || tokenType == PhelTypes.BRACKET2
    }

    fun isBraces(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.BRACE1 || tokenType == PhelTypes.BRACE2
    }

    fun isSetOpener(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.HASH_BRACE
    }

    fun isQuote(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.QUOTE || tokenType == PhelTypes.VAR_QUOTE
    }

    fun isSyntaxQuote(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.SYNTAX_QUOTE
    }


    fun isUnquote(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.TILDE
    }

    fun isUnquoteSplicing(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.TILDE_AT
    }


    fun isKeyword(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.KEYWORD || tokenType == PhelTypes.KEYWORD_TOKEN
                || tokenType == PhelTypes.COLON || tokenType == PhelTypes.COLONCOLON
    }

    fun isMetadata(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.HAT
    }

    fun isDotOperator(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.DOT || tokenType == PhelTypes.DOTDASH
    }

    fun isSymbol(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.SYM
    }

    fun isBadCharacter(tokenType: IElementType): Boolean {
        val tokenName = tokenType.toString()
        return "BAD_CHARACTER" == tokenName || tokenType == TokenType.BAD_CHARACTER
    }
}
