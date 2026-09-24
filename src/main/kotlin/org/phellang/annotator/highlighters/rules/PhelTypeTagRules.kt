package org.phellang.annotator.highlighters.rules

import org.phellang.core.highlighting.PhelTextAttributesRegistry.METADATA
import org.phellang.language.psi.PhelTypeTags

/**
 * A type tag, `map` in `[^map m]`, painted as the metadata it is: the colour its `^` already has.
 *
 * Heads the chain because every later rule reads the symbol as a var: `^map` would otherwise be painted
 * as a call to the `map` function it shares a name with, and `^atom` as a local if a binding called `atom`
 * were in scope.
 */
object TypeTagRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!PhelTypeTags.isTypeTag(context.symbol)) return null

        return PhelHighlightDecision.Paint(METADATA)
    }
}
