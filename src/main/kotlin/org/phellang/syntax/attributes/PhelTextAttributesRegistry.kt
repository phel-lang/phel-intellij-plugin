package org.phellang.syntax.attributes

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object PhelTextAttributesRegistry {

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

    @JvmField
    val NIL_LITERAL: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_NIL", DefaultLanguageHighlighterColors.CONSTANT)

    @JvmField
    val NAN_LITERAL: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_NAN", DefaultLanguageHighlighterColors.CONSTANT)

    @JvmField
    val CHARACTER: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_CHARACTER", DefaultLanguageHighlighterColors.STRING)

    @JvmField
    val PARENTHESES: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)

    @JvmField
    val BRACKETS: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)

    @JvmField
    val BRACES: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_BRACES", DefaultLanguageHighlighterColors.BRACES)

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
    val UNQUOTE_SPLICING: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_UNQUOTE_SPLICING", DefaultLanguageHighlighterColors.METADATA
    )

    @JvmField
    val SYMBOL: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_SYMBOL", DefaultLanguageHighlighterColors.IDENTIFIER)

    @JvmField
    val METADATA: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_METADATA", DefaultLanguageHighlighterColors.METADATA)

    @JvmField
    val DOT_OPERATOR: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_DOT", DefaultLanguageHighlighterColors.DOT)

    @JvmField
    val COMMA: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_COMMA", DefaultLanguageHighlighterColors.COMMA)

    @JvmField
    val BAD_CHARACTER: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
}
