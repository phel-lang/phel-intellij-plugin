package org.phellang.editor.colorsettings

import com.intellij.openapi.options.colors.AttributesDescriptor
import org.phellang.PhelSyntaxHighlighter
import org.phellang.annotator.infrastructure.PhelAnnotationConstants

class PhelColorSettingsProvider {

    fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return arrayOf(
            // Basic syntax elements
            AttributesDescriptor("Comments", PhelSyntaxHighlighter.COMMENT),
            AttributesDescriptor("Strings", PhelSyntaxHighlighter.STRING),
            AttributesDescriptor("Numbers", PhelSyntaxHighlighter.NUMBER),
            AttributesDescriptor("Booleans", PhelSyntaxHighlighter.BOOLEAN),
            AttributesDescriptor("Nil", PhelSyntaxHighlighter.NIL_LITERAL),
            AttributesDescriptor("NAN", PhelSyntaxHighlighter.NAN_LITERAL),
            AttributesDescriptor("Characters", PhelSyntaxHighlighter.CHARACTER),

            // Delimiters
            AttributesDescriptor("Parentheses", PhelSyntaxHighlighter.PARENTHESES),
            AttributesDescriptor("Brackets", PhelSyntaxHighlighter.BRACKETS),
            AttributesDescriptor("Braces", PhelSyntaxHighlighter.BRACES),

            // Macro syntax
            AttributesDescriptor("Quote", PhelSyntaxHighlighter.QUOTE),
            AttributesDescriptor("Syntax quote", PhelSyntaxHighlighter.SYNTAX_QUOTE),
            AttributesDescriptor("Unquote", PhelSyntaxHighlighter.UNQUOTE),
            AttributesDescriptor("Unquote splicing", PhelSyntaxHighlighter.UNQUOTE_SPLICING),
            AttributesDescriptor("Metadata", PhelSyntaxHighlighter.METADATA),

            // Semantic highlighting (from annotator)
            AttributesDescriptor("Function names (in call position)", PhelAnnotationConstants.FUNCTION_NAME),
            AttributesDescriptor("Function calls", PhelAnnotationConstants.FUNCTION_CALL),
            AttributesDescriptor("Function parameters", PhelAnnotationConstants.FUNCTION_PARAMETER),
            AttributesDescriptor("Variadic parameters (&)", PhelAnnotationConstants.VARIADIC_PARAMETER),
            AttributesDescriptor("PHP interop", PhelAnnotationConstants.PHP_INTEROP),
            AttributesDescriptor("Namespace symbols", PhelAnnotationConstants.NAMESPACE_SYMBOL),
            AttributesDescriptor("Short functions", PhelAnnotationConstants.SHORT_FUNCTION),
            AttributesDescriptor("Commented out forms", PhelAnnotationConstants.COMMENTED_OUT_FORM),
            AttributesDescriptor("Regular symbols", PhelAnnotationConstants.REGULAR_SYMBOL),

            // Basic symbols and operators
            AttributesDescriptor("Symbols", PhelSyntaxHighlighter.SYMBOL),
            AttributesDescriptor("Dot operator", PhelSyntaxHighlighter.DOT_OPERATOR),
            AttributesDescriptor("Comma", PhelSyntaxHighlighter.COMMA),
            AttributesDescriptor("Bad characters", PhelSyntaxHighlighter.BAD_CHARACTER),
        )
    }
}
