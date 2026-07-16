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
import org.phellang.annotator.validators.PhelFunctionReferenceValidator
import org.phellang.annotator.validators.PhelNamespaceValidator
import org.phellang.registry.PhelFunctionRegistry
import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.SymbolCategory

object PhelSymbolHighlighter {
    fun annotateSymbol(symbol: PhelSymbol, text: String, holder: AnnotationHolder) {
        if (!PhelAnnotationUtils.isValidText(text)) return

        // Variadic parameter marker (&) - check before other parameter checks
        if (text == "&") {
            if (PhelSymbolAnalyzer.isInParameterVector(symbol)) {
                PhelAnnotationUtils.createAnnotation(holder, symbol, VARIADIC_PARAMETER)
                return
            }
        }

        // Function parameters and let bindings shadow same-named core fns. Classify
        // them first so the deprecated check below does not paint local bindings
        // with strikethrough.
        if (PhelSymbolAnalyzer.isLocalBindingOrReference(symbol)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_PARAMETER)
            return
        }

        // Deprecated core functions - applies strikethrough.
        if (PhelFunctionRegistry.isDeprecated(text)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, DEPRECATED_SYMBOL)
            return
        }

        if (PhelSymbolAnalyzer.isSymbolType(text, SymbolCategory.SPECIAL_FORMS)
            || PhelSymbolAnalyzer.isSymbolType(text, SymbolCategory.CONTROL_FLOW)
            || PhelSymbolAnalyzer.isSymbolType(text, SymbolCategory.CORE_FUNCTIONS)
            || PhelSymbolAnalyzer.isSymbolType(text, SymbolCategory.COLLECTION_FUNCTIONS)
            || PhelSymbolAnalyzer.isSymbolType(text, SymbolCategory.MACROS)
        ) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_CALL)
            return
        }

        // PHP interop patterns (single token)
        if (text.startsWith("php/")) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, PHP_INTEROP)
            return
        }

        // PHP interop shorthands: `Class.`, `.method`, `.-field`, `Class/method`,
        // `Class/CONST`, bare `new`, and `\Foo` / `\Foo\Bar` class references.
        val containingFile = symbol.containingFile as? PhelFile
        val usedClasses = if (containingFile != null) {
            PhelNamespaceUtils.extractUsedClasses(containingFile)
        } else {
            emptySet()
        }
        if (PhelInteropShorthands.isInteropShorthand(text, usedClasses)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, PHP_INTEROP)
            return
        }

        // ClassName argument of `(new ClassName ...)` / `(php/new ClassName ...)`.
        // We only colour it when the text actually looks like a PHP class — otherwise
        // we'd paint user-defined macros that happen to be called `new`.
        if (PhelInteropShorthands.isInteropClassName(text)
            && PhelSymbolPositionAnalyzer.isConstructorClassArgument(symbol)
        ) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, PHP_INTEROP)
            return
        }

        if (text.contains("/") && !text.startsWith("/") && !text.endsWith("/")) {
            val namespaceProblems = PhelNamespaceValidator.validateNamespace(symbol)
            if (namespaceProblems.isNotEmpty()) {
                namespaceProblems.forEach { PhelAnnotationUtils.report(holder, symbol, it) }
                return
            }

            val functionProblems = PhelFunctionReferenceValidator.validateFunctionReference(symbol)
            if (functionProblems.isNotEmpty()) {
                functionProblems.forEach { PhelAnnotationUtils.report(holder, symbol, it) }
                return
            }

            PhelAnnotationUtils.createAnnotation(holder, symbol, NAMESPACE_SYMBOL)
            return
        }

        if (PhelSymbolPositionAnalyzer.hasNamespacePrefix(text)) {
            PhelAnnotationUtils.createAnnotation(holder, symbol, NAMESPACE_SYMBOL)
            return
        }

        if (PhelSymbolPositionAnalyzer.isInFunctionCallPosition(symbol)) {
            // Check if this is an imported function via :refer - use FUNCTION_CALL color
            if (containingFile != null && PhelNamespaceUtils.isReferredSymbol(containingFile, text)) {
                PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_CALL)
                return
            }

            PhelAnnotationUtils.createAnnotation(holder, symbol, FUNCTION_NAME)
            return
        }

        // Regular symbols - give them a color instead of leaving them white
        PhelAnnotationUtils.createAnnotation(holder, symbol, REGULAR_SYMBOL)
    }
}
