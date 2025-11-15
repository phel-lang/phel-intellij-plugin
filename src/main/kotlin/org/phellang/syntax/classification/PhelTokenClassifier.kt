package org.phellang.syntax.classification

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.phellang.language.psi.PhelTypes

object PhelTokenClassifier {

    enum class TokenCategory {
        COMMENT, STRING, NUMBER, BOOLEAN, NIL, NAN, CHARACTER,
        PARENTHESES, BRACKETS, BRACES, QUOTE, SYNTAX_QUOTE,
        UNQUOTE, UNQUOTE_SPLICING, KEYWORD, METADATA,
        DOT_OPERATOR, COMMA, SYMBOL, BAD_CHARACTER, UNKNOWN
    }

    fun classifyToken(tokenType: IElementType): TokenCategory {
        return when {
            isComment(tokenType) -> TokenCategory.COMMENT
            isString(tokenType) -> TokenCategory.STRING
            isNumber(tokenType) -> TokenCategory.NUMBER
            isBoolean(tokenType) -> TokenCategory.BOOLEAN
            isNil(tokenType) -> TokenCategory.NIL
            isNan(tokenType) -> TokenCategory.NAN
            isCharacter(tokenType) -> TokenCategory.CHARACTER
            isParentheses(tokenType) -> TokenCategory.PARENTHESES
            isBrackets(tokenType) -> TokenCategory.BRACKETS
            isBraces(tokenType) -> TokenCategory.BRACES
            isSetOpener(tokenType) -> TokenCategory.BRACES
            isShortFnOpener(tokenType) -> TokenCategory.PARENTHESES
            isQuote(tokenType) -> TokenCategory.QUOTE
            isSyntaxQuote(tokenType) -> TokenCategory.SYNTAX_QUOTE
            isUnquote(tokenType) -> TokenCategory.UNQUOTE
            isUnquoteSplicing(tokenType) -> TokenCategory.UNQUOTE_SPLICING
            isKeyword(tokenType) -> TokenCategory.KEYWORD
            isMetadata(tokenType) -> TokenCategory.METADATA
            isDotOperator(tokenType) -> TokenCategory.DOT_OPERATOR
            isComma(tokenType) -> TokenCategory.COMMA
            isSymbol(tokenType) -> TokenCategory.SYMBOL
            isBadCharacter(tokenType) -> TokenCategory.BAD_CHARACTER
            else -> TokenCategory.UNKNOWN
        }
    }

    fun isComment(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.LINE_COMMENT || tokenType == PhelTypes.FORM_COMMENT || tokenType == PhelTypes.MULTILINE_COMMENT
    }

    fun isString(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.STRING
    }

    fun isNumber(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.NUMBER || tokenType == PhelTypes.HEXNUM || tokenType == PhelTypes.BINNUM || tokenType == PhelTypes.OCTNUM
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
        return tokenType == PhelTypes.PAREN1 || tokenType == PhelTypes.PAREN2
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

    fun isShortFnOpener(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.FN_SHORT
    }

    fun isQuote(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.QUOTE
    }

    fun isSyntaxQuote(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.SYNTAX_QUOTE
    }

    fun isUnquote(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.TILDE || tokenType == PhelTypes.COMMA
    }

    fun isUnquoteSplicing(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.TILDE_AT || tokenType == PhelTypes.COMMA_AT
    }

    fun isKeyword(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.KEYWORD || tokenType == PhelTypes.KEYWORD_TOKEN || tokenType == PhelTypes.COLON || tokenType == PhelTypes.COLONCOLON
    }

    fun isMetadata(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.HAT
    }

    fun isDotOperator(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.DOT || tokenType == PhelTypes.DOTDASH
    }

    fun isComma(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.COMMA
    }

    fun isSymbol(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.SYM
    }

    fun isBadCharacter(tokenType: IElementType): Boolean {
        val tokenName = tokenType.toString()
        return "BAD_CHARACTER" == tokenName || tokenType == TokenType.BAD_CHARACTER
    }
}
