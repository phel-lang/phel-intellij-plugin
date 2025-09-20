package org.phellang.annotator.infrastructure

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object PhelAnnotationConstants {

    @JvmField
    val PHP_INTEROP: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_PHP_INTEROP", DefaultLanguageHighlighterColors.STATIC_METHOD
    )

    @JvmField
    val SPECIAL_FORM: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_SPECIAL_FORM", DefaultLanguageHighlighterColors.KEYWORD)

    @JvmField
    val NAMESPACE_PREFIX: TextAttributesKey =
        TextAttributesKey.createTextAttributesKey("PHEL_NAMESPACE", DefaultLanguageHighlighterColors.IDENTIFIER)

    @JvmField
    val COMMENTED_OUT_FORM: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_COMMENTED_OUT_FORM", DefaultLanguageHighlighterColors.LINE_COMMENT
    )

    @JvmField
    val FUNCTION_PARAMETER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_FUNCTION_PARAMETER", DefaultLanguageHighlighterColors.INSTANCE_FIELD
    )

    @JvmField
    val COLLECTION_TYPE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_COLLECTION_TYPE", DefaultLanguageHighlighterColors.STATIC_METHOD
    )

    @JvmField
    val KEYWORD: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
    )

    @JvmField
    val SHORT_FUNCTION: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_SHORT_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
    )
}
