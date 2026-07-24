package org.phellang.annotator.highlighters.rules

import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.core.highlighting.PhelAnnotationConstants.FUNCTION_CALL
import org.phellang.core.highlighting.PhelAnnotationConstants.FUNCTION_NAME
import org.phellang.core.highlighting.PhelAnnotationConstants.REGULAR_SYMBOL
import org.phellang.language.psi.PhelNamespaceUtils

/**
 * An unqualified symbol in head position.
 *
 * One brought in by `:refer` is a call into another namespace and gets the call colour; anything
 * else is a name belonging to this file.
 */
object FunctionCallPositionRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!PhelSymbolPositionAnalyzer.isInFunctionCallPosition(context.symbol)) return null

        val file = context.file
        val referred = file != null && PhelNamespaceUtils.isReferredSymbol(file, context.text)

        return PhelHighlightDecision.Paint(if (referred) FUNCTION_CALL else FUNCTION_NAME)
    }
}

/**
 * Terminal rule: whatever reached here is an ordinary symbol and still gets a colour rather than
 * being left the default foreground. Never declines, so the chain always produces a decision.
 */
object RegularSymbolRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision =
        PhelHighlightDecision.Paint(REGULAR_SYMBOL)
}
