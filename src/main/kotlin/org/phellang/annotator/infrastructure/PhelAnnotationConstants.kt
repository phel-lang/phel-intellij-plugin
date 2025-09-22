package org.phellang.annotator.infrastructure

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object PhelAnnotationConstants {

    @JvmField
    val NAMESPACE_SYMBOL: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_NAMESPACE_SYMBOL", DefaultLanguageHighlighterColors.CLASS_REFERENCE
    )

    @JvmField
    val FUNCTION_NAME: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_FUNCTION_NAME", DefaultLanguageHighlighterColors.CONSTANT
    )

    @JvmField
    val SHORT_FUNCTION: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_SHORT_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
    )

    @JvmField
    val PHP_INTEROP: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_PHP_INTEROP", DefaultLanguageHighlighterColors.NUMBER
    )

    @JvmField
    val FUNCTION_CALL: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_FUNCTION_CALL", DefaultLanguageHighlighterColors.KEYWORD
    )

    @JvmField
    val COMMENTED_OUT_FORM: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_COMMENTED_OUT_FORM", DefaultLanguageHighlighterColors.LINE_COMMENT
    )

    @JvmField
    val FUNCTION_PARAMETER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_FUNCTION_PARAMETER", DefaultLanguageHighlighterColors.PARAMETER
    )

    @JvmField
    val COLLECTION_TYPE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_COLLECTION_TYPE", DefaultLanguageHighlighterColors.BRACES
    )

    @JvmField
    val KEYWORD: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_KEYWORD", DefaultLanguageHighlighterColors.INSTANCE_FIELD
    )

    @JvmField
    val REGULAR_SYMBOL: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_REGULAR_SYMBOL", DefaultLanguageHighlighterColors.LOCAL_VARIABLE
    )

    @JvmField
    val VARIADIC_PARAMETER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "PHEL_VARIADIC_PARAMETER", DefaultLanguageHighlighterColors.STATIC_FIELD
    )
}
