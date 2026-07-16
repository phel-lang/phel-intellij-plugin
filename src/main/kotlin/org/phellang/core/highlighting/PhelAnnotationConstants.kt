package org.phellang.core.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object PhelAnnotationConstants {

    /** Namespace-qualified symbols (e.g., core\map, phel\string) */
    val NAMESPACE_SYMBOL: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_NAMESPACE_SYMBOL", DefaultLanguageHighlighterColors.CLASS_REFERENCE
        )
    }

    /** Function names in definition contexts */
    val FUNCTION_NAME: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_FUNCTION_NAME", DefaultLanguageHighlighterColors.CONSTANT
        )
    }

    /** Anonymous function shorthand #(+ %1 %2). External key kept as PHEL_SHORT_FUNCTION to preserve user color settings. */
    val ANONYMOUS_FUNCTION: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_SHORT_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )
    }

    /** PHP interoperability symbols (php/array, php/new, etc.) */
    val PHP_INTEROP: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_PHP_INTEROP", DefaultLanguageHighlighterColors.NUMBER
        )
    }

    /** Function calls in call position */
    val FUNCTION_CALL: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_FUNCTION_CALL", DefaultLanguageHighlighterColors.KEYWORD
        )
    }

    /** Forms commented out with #_ reader macro */
    val COMMENTED_OUT_FORM: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_COMMENTED_OUT_FORM", DefaultLanguageHighlighterColors.LINE_COMMENT
        )
    }

    /** Function parameters and let bindings */
    val FUNCTION_PARAMETER: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_FUNCTION_PARAMETER", DefaultLanguageHighlighterColors.PARAMETER
        )
    }

    /** Keywords (:keyword-name) */
    val KEYWORD: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_SEMANTIC_KEYWORD", DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
    }

    /** Regular symbols that don't fit other categories */
    val REGULAR_SYMBOL: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_REGULAR_SYMBOL", DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )
    }

    /** Variadic parameter marker (&) */
    val VARIADIC_PARAMETER: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_VARIADIC_PARAMETER", DefaultLanguageHighlighterColors.STATIC_FIELD
        )
    }

    /** Deprecated symbols (strikethrough) */
    val DEPRECATED_SYMBOL: TextAttributesKey by lazy {
        TextAttributesKey.createTextAttributesKey(
            "PHEL_DEPRECATED_SYMBOL", CodeInsightColors.DEPRECATED_ATTRIBUTES
        )
    }
}
