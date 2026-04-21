package org.phellang.syntax.classification

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.phellang.language.psi.PhelTypes

object PhelTokenClassifier {

    enum class TokenCategory {
        COMMENT, STRING, NUMBER, BOOLEAN, NIL, NAN, CHARACTER, PARENTHESES, BRACKETS, BRACES, QUOTE, SYNTAX_QUOTE,
        UNQUOTE, UNQUOTE_SPLICING, KEYWORD, METADATA, DOT_OPERATOR, SYMBOL, BAD_CHARACTER, UNKNOWN, REGEX, DEREF, TAG
    }

    fun classifyToken(tokenType: IElementType): TokenCategory {
        return when {
            isComment(tokenType) -> TokenCategory.COMMENT
            isString(tokenType) -> TokenCategory.STRING
            isRegex(tokenType) -> TokenCategory.REGEX
            isNumber(tokenType) -> TokenCategory.NUMBER
            isBoolean(tokenType) -> TokenCategory.BOOLEAN
            isNil(tokenType) -> TokenCategory.NIL
            isNan(tokenType) -> TokenCategory.NAN
            isCharacter(tokenType) -> TokenCategory.CHARACTER
            isParentheses(tokenType) -> TokenCategory.PARENTHESES
            isBrackets(tokenType) -> TokenCategory.BRACKETS
            isBraces(tokenType) -> TokenCategory.BRACES
            isSetOpener(tokenType) -> TokenCategory.BRACES
            isQuote(tokenType) -> TokenCategory.QUOTE
            isSyntaxQuote(tokenType) -> TokenCategory.SYNTAX_QUOTE
            isDeref(tokenType) -> TokenCategory.DEREF
            isUnquote(tokenType) -> TokenCategory.UNQUOTE
            isUnquoteSplicing(tokenType) -> TokenCategory.UNQUOTE_SPLICING
            isTag(tokenType) -> TokenCategory.TAG
            isKeyword(tokenType) -> TokenCategory.KEYWORD
            isMetadata(tokenType) -> TokenCategory.METADATA
            isDotOperator(tokenType) -> TokenCategory.DOT_OPERATOR
            isSymbol(tokenType) -> TokenCategory.SYMBOL
            isBadCharacter(tokenType) -> TokenCategory.BAD_CHARACTER
            else -> TokenCategory.UNKNOWN
        }
    }

    fun isComment(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.LINE_COMMENT || tokenType == PhelTypes.FORM_COMMENT
    }

    fun isString(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.STRING
    }

    fun isRegex(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.REGEX_START || tokenType == PhelTypes.REGEX_BODY
    }

    fun isNumber(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.NUMBER || tokenType == PhelTypes.BINNUM
                || tokenType == PhelTypes.OCTNUM || tokenType == PhelTypes.HEXNUM
                || tokenType == PhelTypes.RADIXNUM || tokenType == PhelTypes.SYMBOLIC_NUM
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

    fun isDeref(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.DEREF
    }

    fun isUnquote(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.TILDE
    }

    fun isUnquoteSplicing(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.TILDE_AT
    }

    fun isTag(tokenType: IElementType): Boolean {
        return tokenType == PhelTypes.TAG
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
