package org.phellang.annotator.highlighters.rules

import org.phellang.core.highlighting.PhelAnnotationConstants.DEPRECATED_SYMBOL
import org.phellang.core.highlighting.PhelAnnotationConstants.FUNCTION_CALL
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.utils.SymbolCategory
import org.phellang.registry.PhelFunctionRegistry

/** Core functions the registry marks deprecated. Applies strikethrough. */
object DeprecatedSymbolRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!PhelFunctionRegistry.isDeprecated(context.text)) return null

        return PhelHighlightDecision.Paint(DEPRECATED_SYMBOL)
    }
}

/** Anything the language layer recognises as a built-in form, macro, or core/collection function. */
object KnownFormRule : PhelHighlightRule {
    private val CATEGORIES = listOf(
        SymbolCategory.SPECIAL_FORMS,
        SymbolCategory.CONTROL_FLOW,
        SymbolCategory.CORE_FUNCTIONS,
        SymbolCategory.COLLECTION_FUNCTIONS,
        SymbolCategory.MACROS,
    )

    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (CATEGORIES.none { PhelSymbolAnalyzer.isSymbolType(context.text, it) }) return null

        return PhelHighlightDecision.Paint(FUNCTION_CALL)
    }
}
