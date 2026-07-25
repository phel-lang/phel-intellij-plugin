package org.phellang.annotator.highlighters.rules

import org.phellang.core.highlighting.PhelAnnotationConstants.FUNCTION_PARAMETER
import org.phellang.core.highlighting.PhelAnnotationConstants.VARIADIC_PARAMETER
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer

/**
 * The `&` rest-parameter marker, but only where it actually introduces one. Outside a parameter
 * vector `&` is an ordinary symbol, so this rule declines and lets the chain continue.
 */
object VariadicMarkerRule : PhelHighlightRule {
    private const val VARIADIC_MARKER = "&"

    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (context.text != VARIADIC_MARKER) return null
        if (!PhelSymbolAnalyzer.isInParameterVector(context.symbol)) return null

        return PhelHighlightDecision.Paint(VARIADIC_PARAMETER)
    }
}

/**
 * Function parameters and let bindings.
 *
 * Runs before the deprecation rule deliberately: a binding named after a deprecated core function
 * shadows it, and painting the local with strikethrough would report the wrong symbol as stale.
 */
object LocalBindingRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!PhelSymbolAnalyzer.isLocalBindingOrReference(context.symbol)) return null

        return PhelHighlightDecision.Paint(FUNCTION_PARAMETER)
    }
}
