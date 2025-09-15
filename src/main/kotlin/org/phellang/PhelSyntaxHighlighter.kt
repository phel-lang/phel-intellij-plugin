package org.phellang

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.phellang.language.PhelLexerAdapter
import org.phellang.language.psi.PhelTypes

class PhelSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return PhelLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        val tokenName: String = tokenType.toString()

        // Comments
        if (tokenType == PhelTypes.LINE_COMMENT || tokenType == PhelTypes.FORM_COMMENT || tokenType == PhelTypes.MULTILINE_COMMENT) {
            return COMMENT_KEYS
        }

        // Literals
        if (tokenType == PhelTypes.STRING) {
            return STRING_KEYS
        }
        if (tokenType == PhelTypes.NUMBER || tokenType == PhelTypes.HEXNUM || tokenType == PhelTypes.BINNUM || tokenType == PhelTypes.OCTNUM) {
            return NUMBER_KEYS
        }
        if (tokenType == PhelTypes.BOOL) {
            return BOOLEAN_KEYS
        }
        if (tokenType == PhelTypes.NIL) {
            return NIL_KEYS
        }
        if (tokenType == PhelTypes.NAN) {
            return NAN_KEYS
        }
        if (tokenType == PhelTypes.CHAR) {
            return CHARACTER_KEYS
        }

        // Delimiters
        if (tokenType == PhelTypes.PAREN1 || tokenType == PhelTypes.PAREN2) {
            return PARENTHESES_KEYS
        }
        if (tokenType == PhelTypes.BRACKET1 || tokenType == PhelTypes.BRACKET2) {
            return BRACKETS_KEYS
        }
        if (tokenType == PhelTypes.BRACE1 || tokenType == PhelTypes.BRACE2) {
            return BRACES_KEYS
        }

        // Quote and macro syntax
        if (tokenType == PhelTypes.QUOTE) {
            return QUOTE_KEYS
        }
        if (tokenType == PhelTypes.SYNTAX_QUOTE) {
            return SYNTAX_QUOTE_KEYS
        }
        if (tokenType == PhelTypes.TILDE || tokenType == PhelTypes.COMMA) {
            return UNQUOTE_KEYS
        }
        if (tokenType == PhelTypes.TILDE_AT || tokenType == PhelTypes.COMMA_AT) {
            return UNQUOTE_SPLICING_KEYS
        }

        // Keywords - complete keyword elements (:something, ::something)
        if (tokenType == PhelTypes.KEYWORD || tokenType == PhelTypes.KEYWORD_TOKEN) {
            return KEYWORD_KEYS
        }

        // Individual colon tokens (for partial keywords during typing)
        if (tokenType == PhelTypes.COLON || tokenType == PhelTypes.COLONCOLON) {
            return KEYWORD_KEYS
        }

        // Multi-character PHP operators
        if (tokenType == PhelTypes.AND_AND || tokenType == PhelTypes.OR_OR || tokenType == PhelTypes.SHIFT_LEFT || tokenType == PhelTypes.SHIFT_RIGHT || tokenType == PhelTypes.NOT_EQUAL || tokenType == PhelTypes.NOT_IDENTICAL || tokenType == PhelTypes.INCREMENT || tokenType == PhelTypes.DECREMENT) {
            return SYMBOL_KEYS
        }

        // Metadata
        if (tokenType == PhelTypes.HAT) {
            return METADATA_KEYS
        }

        // Operators
        if (tokenType == PhelTypes.DOT || tokenType == PhelTypes.DOTDASH) {
            return DOT_KEYS
        }
        if (tokenType == PhelTypes.COMMA) {
            return COMMA_KEYS
        }

        // Symbols (everything else that's valid)
        if (tokenType == PhelTypes.SYM) {
            return SYMBOL_KEYS
        }

        // Bad characters (handled by token name)
        if ("BAD_CHARACTER" == tokenName || tokenType == TokenType.BAD_CHARACTER) {
            return BAD_CHARACTER_KEYS
        }

        return EMPTY_KEYS
    }

    companion object {
        // Core language elements
        @JvmField
        val COMMENT: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)

        @JvmField
        val STRING: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_STRING", DefaultLanguageHighlighterColors.STRING)

        @JvmField
        val NUMBER: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_NUMBER", DefaultLanguageHighlighterColors.NUMBER)

        @JvmField
        val KEYWORD: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)

        @JvmField
        val BOOLEAN: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_BOOLEAN", DefaultLanguageHighlighterColors.KEYWORD)

        // Literals and constants
        @JvmField
        val NIL_LITERAL: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_NIL", DefaultLanguageHighlighterColors.CONSTANT)

        @JvmField
        val NAN_LITERAL: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_NAN", DefaultLanguageHighlighterColors.CONSTANT)

        @JvmField
        val CHARACTER: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_CHARACTER", DefaultLanguageHighlighterColors.STRING)

        // Brackets and delimiters
        @JvmField
        val PARENTHESES: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)

        @JvmField
        val BRACKETS: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)

        @JvmField
        val BRACES: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_BRACES", DefaultLanguageHighlighterColors.BRACES)

        // Macro and quote syntax
        @JvmField
        val QUOTE: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_QUOTE", DefaultLanguageHighlighterColors.METADATA)

        @JvmField
        val SYNTAX_QUOTE: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_SYNTAX_QUOTE", DefaultLanguageHighlighterColors.METADATA)

        @JvmField
        val UNQUOTE: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_UNQUOTE", DefaultLanguageHighlighterColors.METADATA)

        @JvmField
        val UNQUOTE_SPLICING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "PHEL_UNQUOTE_SPLICING", DefaultLanguageHighlighterColors.METADATA
        )

        // Symbols and identifiers  
        @JvmField
        val SYMBOL: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_SYMBOL", DefaultLanguageHighlighterColors.IDENTIFIER)

        @JvmField
        val KEYWORD_IDENTIFIER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "PHEL_KEYWORD_ID", DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )

        // Special operators
        @JvmField
        val METADATA: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_METADATA", DefaultLanguageHighlighterColors.METADATA)

        @JvmField
        val DOT_OPERATOR: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_DOT", DefaultLanguageHighlighterColors.DOT)

        @JvmField
        val COMMA: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_COMMA", DefaultLanguageHighlighterColors.COMMA)

        // Error highlighting
        @JvmField
        val BAD_CHARACTER: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("PHEL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        private val COMMENT_KEYS = arrayOf<TextAttributesKey>(COMMENT)
        private val STRING_KEYS = arrayOf<TextAttributesKey>(STRING)
        private val NUMBER_KEYS = arrayOf<TextAttributesKey>(NUMBER)
        private val BOOLEAN_KEYS = arrayOf<TextAttributesKey>(BOOLEAN)
        private val NIL_KEYS = arrayOf<TextAttributesKey>(NIL_LITERAL)
        private val NAN_KEYS = arrayOf<TextAttributesKey>(NAN_LITERAL)
        private val CHARACTER_KEYS = arrayOf<TextAttributesKey>(CHARACTER)
        private val PARENTHESES_KEYS = arrayOf<TextAttributesKey>(PARENTHESES)
        private val BRACKETS_KEYS = arrayOf<TextAttributesKey>(BRACKETS)
        private val BRACES_KEYS = arrayOf<TextAttributesKey>(BRACES)
        private val QUOTE_KEYS = arrayOf<TextAttributesKey>(QUOTE)
        private val SYNTAX_QUOTE_KEYS = arrayOf<TextAttributesKey>(SYNTAX_QUOTE)
        private val UNQUOTE_KEYS = arrayOf<TextAttributesKey>(UNQUOTE)
        private val UNQUOTE_SPLICING_KEYS = arrayOf<TextAttributesKey>(UNQUOTE_SPLICING)
        private val SYMBOL_KEYS = arrayOf<TextAttributesKey>(SYMBOL)
        private val KEYWORD_KEYS = arrayOf<TextAttributesKey>(KEYWORD)
        private val METADATA_KEYS = arrayOf<TextAttributesKey>(METADATA)
        private val DOT_KEYS = arrayOf<TextAttributesKey>(DOT_OPERATOR)
        private val COMMA_KEYS = arrayOf<TextAttributesKey>(COMMA)
        private val BAD_CHARACTER_KEYS = arrayOf<TextAttributesKey>(BAD_CHARACTER)
        private val EMPTY_KEYS: Array<TextAttributesKey> = arrayOf<TextAttributesKey>()
    }
}
