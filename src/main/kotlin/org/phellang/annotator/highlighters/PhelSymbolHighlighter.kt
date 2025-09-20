package org.phellang.annotator.highlighters

import com.intellij.lang.annotation.AnnotationHolder
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.FUNCTION_PARAMETER
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.NAMESPACE_PREFIX
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.PHP_INTEROP
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.SPECIAL_FORM
import org.phellang.annotator.analyzers.PhelPhpInteropAnalyzer
import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.completion.infrastructure.PhelCompletionPriority
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelSymbol

object PhelSymbolHighlighter {

    fun annotateSymbol(symbol: PhelSymbol, text: String, holder: AnnotationHolder) {
        if (!PhelAnnotationUtils.isValidText(text)) return

        // Function parameters and let bindings (check first, before other classifications)
        if (PhelSymbolAnalyzer.isDefinition(symbol) || PhelSymbolAnalyzer.isParameterReference(symbol)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_PARAMETER)
            return
        }

        // Check for PHP interop patterns that span multiple tokens (php/SYMBOL)
        val phpInteropRange = PhelPhpInteropAnalyzer.findPhpInteropRange(symbol)
        if (phpInteropRange != null) {
            // Only create annotation if this is the qualified symbol itself, not a sub-element
            if (phpInteropRange == symbol.textRange) {
                PhelAnnotationUtils.createAnnotation(holder, phpInteropRange, PHP_INTEROP)
                return
            } else {
                // This is a sub-element of a qualified symbol, skip highlighting here
                // The qualified symbol will be highlighted when it's processed
                return
            }
        }

        // PHP interop patterns (single token)
        if (PhelPhpInteropAnalyzer.isPhpInterop(text)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, PHP_INTEROP)
            return
        }

        // Special forms
        if (PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.SPECIAL_FORMS)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, SPECIAL_FORM)
            return
        }

        // Macros
        if (PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.MACROS)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, SPECIAL_FORM)
            return
        }

        // Core functions
        if (PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.CORE_FUNCTIONS)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, SPECIAL_FORM)
            return
        }

        // Function call position - highlight user-defined functions in function position
        if (PhelSymbolPositionAnalyzer.isInFunctionCallPosition(symbol)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, SPECIAL_FORM)
            return
        }

        // Namespace prefix highlighting
        if (PhelSymbolPositionAnalyzer.hasNamespacePrefix(text)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, NAMESPACE_PREFIX)
            return
        }
    }
}
