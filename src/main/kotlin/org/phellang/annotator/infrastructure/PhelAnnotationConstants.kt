package org.phellang.annotator.infrastructure

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object PhelAnnotationConstants {

    val NAMESPACE_SYMBOL: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_NAMESPACE_SYMBOL", DefaultLanguageHighlighterColors.CLASS_REFERENCE
        )
    }

    val FUNCTION_NAME: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_FUNCTION_NAME", DefaultLanguageHighlighterColors.CONSTANT
        )
    }

    val SHORT_FUNCTION: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_SHORT_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )
    }

    val PHP_INTEROP: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_PHP_INTEROP", DefaultLanguageHighlighterColors.NUMBER
        )
    }

    val FUNCTION_CALL: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_FUNCTION_CALL", DefaultLanguageHighlighterColors.KEYWORD
        )
    }

    val COMMENTED_OUT_FORM: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_COMMENTED_OUT_FORM", DefaultLanguageHighlighterColors.LINE_COMMENT
        )
    }

    val FUNCTION_PARAMETER: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_FUNCTION_PARAMETER", DefaultLanguageHighlighterColors.PARAMETER
        )
    }

    val COLLECTION_TYPE: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_COLLECTION_TYPE", DefaultLanguageHighlighterColors.BRACES
        )
    }

    val KEYWORD: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_SEMANTIC_KEYWORD", DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
    }

    val REGULAR_SYMBOL: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_REGULAR_SYMBOL", DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )
    }

    val VARIADIC_PARAMETER: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_VARIADIC_PARAMETER", DefaultLanguageHighlighterColors.STATIC_FIELD
        )
    }
}
