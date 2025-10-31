package org.phellang.editor.colorsettings

import com.intellij.openapi.options.colors.AttributesDescriptor
import org.phellang.annotator.infrastructure.PhelAnnotationConstants
import org.phellang.syntax.attributes.PhelTextAttributesRegistry

class PhelColorSettingsProvider {

    fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return arrayOf(
            // Basic syntax elements
            AttributesDescriptor("Comments", PhelTextAttributesRegistry.COMMENT),
            AttributesDescriptor("Strings", PhelTextAttributesRegistry.STRING),
            AttributesDescriptor("Numbers", PhelTextAttributesRegistry.NUMBER),
            AttributesDescriptor("Booleans", PhelTextAttributesRegistry.BOOLEAN),
            AttributesDescriptor("Nil", PhelTextAttributesRegistry.NIL_LITERAL),
            AttributesDescriptor("NAN", PhelTextAttributesRegistry.NAN_LITERAL),
            AttributesDescriptor("Characters", PhelTextAttributesRegistry.CHARACTER),

            // Delimiters
            AttributesDescriptor("Parentheses", PhelTextAttributesRegistry.PARENTHESES),
            AttributesDescriptor("Brackets", PhelTextAttributesRegistry.BRACKETS),
            AttributesDescriptor("Braces", PhelTextAttributesRegistry.BRACES),

            // Macro syntax
            AttributesDescriptor("Quote", PhelTextAttributesRegistry.QUOTE),
            AttributesDescriptor("Syntax quote", PhelTextAttributesRegistry.SYNTAX_QUOTE),
            AttributesDescriptor("Unquote", PhelTextAttributesRegistry.UNQUOTE),
            AttributesDescriptor("Unquote splicing", PhelTextAttributesRegistry.UNQUOTE_SPLICING),
            AttributesDescriptor("Metadata", PhelTextAttributesRegistry.METADATA),

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
            AttributesDescriptor("Symbols", PhelTextAttributesRegistry.SYMBOL),
            AttributesDescriptor("Dot operator", PhelTextAttributesRegistry.DOT_OPERATOR),
            AttributesDescriptor("Comma", PhelTextAttributesRegistry.COMMA),
            AttributesDescriptor("Bad characters", PhelTextAttributesRegistry.BAD_CHARACTER),
        )
    }
}
