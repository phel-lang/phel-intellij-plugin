package org.phellang.annotator.highlighters

import com.intellij.lang.annotation.AnnotationHolder
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.DEPRECATED_SYMBOL
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.FUNCTION_PARAMETER
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.FUNCTION_CALL
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.FUNCTION_NAME
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.NAMESPACE_SYMBOL
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.PHP_INTEROP
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.REGULAR_SYMBOL
import org.phellang.annotator.infrastructure.PhelAnnotationConstants.VARIADIC_PARAMETER
import org.phellang.annotator.quickfixes.PhelImportNamespaceQuickFix
import org.phellang.annotator.validators.PhelFunctionReferenceValidator
import org.phellang.annotator.validators.PhelNamespaceValidator
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.completion.infrastructure.PhelCompletionPriority
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelSymbol

object PhelSymbolHighlighter {

    fun annotateSymbol(symbol: PhelSymbol, text: String, holder: AnnotationHolder) {
        if (!PhelAnnotationUtils.isValidText(text)) return

        // Check for deprecated functions first - applies strikethrough
        if (PhelFunctionRegistry.isDeprecated(text)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, DEPRECATED_SYMBOL)
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

        if (PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.SPECIAL_FORMS)
            || PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.CONTROL_FLOW)
            || PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.CORE_FUNCTIONS)
            || PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.COLLECTION_FUNCTIONS)
            || PhelSymbolAnalyzer.isSymbolType(text, PhelCompletionPriority.MACROS)
        ) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_CALL)
            return
        }

        // PHP interop patterns (single token)
        if (text.startsWith("php/")) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, PHP_INTEROP)
            return
        }

        // Namespace-qualified function calls - use "/" as separator
        if (text.contains("/") && !text.startsWith("/") && !text.endsWith("/")) {
            // First, validate the namespace itself
            val namespaceValidation = PhelNamespaceValidator.validateNamespace(symbol)
            if (namespaceValidation != null) {
                // Namespace issue - report it with optional quick fix
                if (namespaceValidation.fullNamespace != null) {
                    val quickFix = PhelImportNamespaceQuickFix(
                        namespaceValidation.fullNamespace
                    )
                    PhelAnnotationUtils.createWarningAnnotationWithFix(
                        holder, symbol, namespaceValidation.message, quickFix
                    )
                } else {
                    PhelAnnotationUtils.createWarningAnnotation(holder, symbol, namespaceValidation.message)
                }
                return
            }

            // Namespace is valid - now check if the function exists in that namespace
            val functionValidation = PhelFunctionReferenceValidator.validateFunctionReference(symbol)
            if (functionValidation != null) {
                PhelAnnotationUtils.createWarningAnnotation(holder, symbol, functionValidation.message)
                return
            }

            PhelAnnotationUtils.createAnnotation(holder, symbol, NAMESPACE_SYMBOL)
            return
        }

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
