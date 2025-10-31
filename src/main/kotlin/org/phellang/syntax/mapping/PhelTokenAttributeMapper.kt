package org.phellang.syntax.mapping

import com.intellij.openapi.editor.colors.TextAttributesKey
import org.phellang.syntax.classification.PhelTokenClassifier.TokenCategory
import org.phellang.syntax.attributes.PhelTextAttributesRegistry

object PhelTokenAttributeMapper {

    private val EMPTY_KEYS: Array<TextAttributesKey> = arrayOf()

    // Pre-computed attribute arrays for performance
    private val COMMENT_KEYS = arrayOf(PhelTextAttributesRegistry.COMMENT)
    private val STRING_KEYS = arrayOf(PhelTextAttributesRegistry.STRING)
    private val NUMBER_KEYS = arrayOf(PhelTextAttributesRegistry.NUMBER)
    private val BOOLEAN_KEYS = arrayOf(PhelTextAttributesRegistry.BOOLEAN)
    private val NIL_KEYS = arrayOf(PhelTextAttributesRegistry.NIL_LITERAL)
    private val NAN_KEYS = arrayOf(PhelTextAttributesRegistry.NAN_LITERAL)
    private val CHARACTER_KEYS = arrayOf(PhelTextAttributesRegistry.CHARACTER)
    private val PARENTHESES_KEYS = arrayOf(PhelTextAttributesRegistry.PARENTHESES)
    private val BRACKETS_KEYS = arrayOf(PhelTextAttributesRegistry.BRACKETS)
    private val BRACES_KEYS = arrayOf(PhelTextAttributesRegistry.BRACES)
    private val QUOTE_KEYS = arrayOf(PhelTextAttributesRegistry.QUOTE)
    private val SYNTAX_QUOTE_KEYS = arrayOf(PhelTextAttributesRegistry.SYNTAX_QUOTE)
    private val UNQUOTE_KEYS = arrayOf(PhelTextAttributesRegistry.UNQUOTE)
    private val UNQUOTE_SPLICING_KEYS = arrayOf(PhelTextAttributesRegistry.UNQUOTE_SPLICING)
    private val SYMBOL_KEYS = arrayOf(PhelTextAttributesRegistry.SYMBOL)
    private val KEYWORD_KEYS = arrayOf(PhelTextAttributesRegistry.KEYWORD)
    private val METADATA_KEYS = arrayOf(PhelTextAttributesRegistry.METADATA)
    private val DOT_KEYS = arrayOf(PhelTextAttributesRegistry.DOT_OPERATOR)
    private val COMMA_KEYS = arrayOf(PhelTextAttributesRegistry.COMMA)
    private val BAD_CHARACTER_KEYS = arrayOf(PhelTextAttributesRegistry.BAD_CHARACTER)

    fun getTextAttributes(category: TokenCategory): Array<TextAttributesKey> {
        return when (category) {
            TokenCategory.COMMENT -> COMMENT_KEYS
            TokenCategory.STRING -> STRING_KEYS
            TokenCategory.NUMBER -> NUMBER_KEYS
            TokenCategory.BOOLEAN -> BOOLEAN_KEYS
            TokenCategory.NIL -> NIL_KEYS
            TokenCategory.NAN -> NAN_KEYS
            TokenCategory.CHARACTER -> CHARACTER_KEYS
            TokenCategory.PARENTHESES -> PARENTHESES_KEYS
            TokenCategory.BRACKETS -> BRACKETS_KEYS
            TokenCategory.BRACES -> BRACES_KEYS
            TokenCategory.QUOTE -> QUOTE_KEYS
            TokenCategory.SYNTAX_QUOTE -> SYNTAX_QUOTE_KEYS
            TokenCategory.UNQUOTE -> UNQUOTE_KEYS
            TokenCategory.UNQUOTE_SPLICING -> UNQUOTE_SPLICING_KEYS
            TokenCategory.KEYWORD -> KEYWORD_KEYS
            TokenCategory.METADATA -> METADATA_KEYS
            TokenCategory.DOT_OPERATOR -> DOT_KEYS
            TokenCategory.COMMA -> COMMA_KEYS
            TokenCategory.SYMBOL -> SYMBOL_KEYS
            TokenCategory.BAD_CHARACTER -> BAD_CHARACTER_KEYS
            TokenCategory.UNKNOWN -> EMPTY_KEYS
        }
    }
}
