package org.phellang.annotator.highlighters.rules

import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.annotator.validators.PhelFunctionReferenceValidator
import org.phellang.annotator.validators.PhelNamespaceValidator
import org.phellang.core.highlighting.PhelAnnotationConstants.NAMESPACE_SYMBOL

/**
 * `ns/name` symbols, the one classification that can fail.
 *
 * Both the namespace and the name it qualifies have to resolve, so this rule reports validator
 * findings when either check fails and only paints a symbol that survives both.
 */
object QualifiedSymbolRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!isQualified(context.text)) return null

        val namespaceProblems = PhelNamespaceValidator.validateNamespace(context.symbol)
        if (namespaceProblems.isNotEmpty()) return PhelHighlightDecision.Report(namespaceProblems)

        val functionProblems = PhelFunctionReferenceValidator.validateFunctionReference(context.symbol)
        if (functionProblems.isNotEmpty()) return PhelHighlightDecision.Report(functionProblems)

        return PhelHighlightDecision.Paint(NAMESPACE_SYMBOL)
    }

    /** A leading or trailing `/` is the division function or a malformed symbol, not a qualifier. */
    private fun isQualified(text: String): Boolean =
        text.contains("/") && !text.startsWith("/") && !text.endsWith("/")
}

/** Backslash-separated namespace prefixes (`my-ns\func`), which carry no import validation. */
object NamespacePrefixRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!PhelSymbolPositionAnalyzer.hasNamespacePrefix(context.text)) return null

        return PhelHighlightDecision.Paint(NAMESPACE_SYMBOL)
    }
}
