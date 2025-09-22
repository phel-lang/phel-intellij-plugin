package org.phellang.annotator.highlighters

import com.intellij.lang.annotation.AnnotationHolder
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.FUNCTION_PARAMETER
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.FUNCTION_CALL
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.FUNCTION_NAME
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.NAMESPACE_SYMBOL
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.PHP_INTEROP
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.REGULAR_SYMBOL
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.VARIADIC_PARAMETER
import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.completion.infrastructure.PhelCompletionPriority
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelSymbol

object PhelSymbolHighlighter {

    fun annotateSymbol(symbol: PhelSymbol, text: String, holder: AnnotationHolder) {
        if (!PhelAnnotationUtils.isValidText(text)) return

        if (PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.SPECIAL_FORMS)
            || PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.CONTROL_FLOW)
            || PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.CORE_FUNCTIONS)
            || PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.COLLECTION_FUNCTIONS)
            || PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.MACROS)
        ) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_CALL)
            return
        }

        // Variadic parameter marker (&) - check before other parameter checks
        if (text == "&") {
            if (PhelSymbolAnalyzer.isInParameterVector(symbol)) {
                PhelAnnotationUtils.createAnnotation(holder, symbol, VARIADIC_PARAMETER)
                return
            }
        }

        // Function parameters and let bindings (check early, before other classifications)
        if (PhelSymbolAnalyzer.isParameterReference(symbol)
            || PhelSymbolAnalyzer.isFunctionParameter(symbol)
            || PhelSymbolAnalyzer.isLetBinding(symbol)
        ) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_PARAMETER)
            return
        }

        // PHP interop patterns (single token)
        if (text.startsWith("php/")) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, PHP_INTEROP)
            return
        }

        // Namespace prefix highlighting
        if (PhelSymbolPositionAnalyzer.hasNamespacePrefix(text)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, NAMESPACE_SYMBOL)
            return
        }

        // Function call position
        if (PhelSymbolPositionAnalyzer.isInFunctionCallPosition(symbol)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_NAME)
            return
        }

        // Regular symbols - give them a color instead of leaving them white
        PhelAnnotationUtils.createAnnotation(holder, symbol, REGULAR_SYMBOL)
    }
}
