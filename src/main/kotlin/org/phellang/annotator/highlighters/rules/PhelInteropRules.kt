package org.phellang.annotator.highlighters.rules

import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.core.highlighting.PhelAnnotationConstants.PHP_INTEROP
import org.phellang.language.psi.PhelInteropShorthands

/**
 * Explicitly qualified interop: `php/aget`, `php/->`, `php/new`, the `php/` operators.
 *
 * Kept ahead of [InteropShorthandRule] so the common case never triggers the `(:use ...)` scan.
 */
object PhpQualifiedRule : PhelHighlightRule {
    private const val PHP_PREFIX = "php/"

    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!context.text.startsWith(PHP_PREFIX)) return null

        return PhelHighlightDecision.Paint(PHP_INTEROP)
    }
}

/** The lexical shorthands: `Class.`, `.method`, `.-field`, `Class/method`, `Class/CONST`, `\Foo\Bar`. */
object InteropShorthandRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!PhelInteropShorthands.isInteropShorthand(context.text, context.usedClasses)) return null

        return PhelHighlightDecision.Paint(PHP_INTEROP)
    }
}

/**
 * The class argument of `(new ClassName ...)` / `(php/new ClassName ...)`.
 *
 * Gated on the text actually looking like a PHP class: without that, a user-defined macro named
 * `new` would have its first argument painted as interop.
 */
object ConstructorClassArgumentRule : PhelHighlightRule {
    override fun decide(context: PhelSymbolContext): PhelHighlightDecision? {
        if (!PhelInteropShorthands.isInteropClassName(context.text)) return null
        if (!PhelSymbolPositionAnalyzer.isConstructorClassArgument(context.symbol)) return null

        return PhelHighlightDecision.Paint(PHP_INTEROP)
    }
}
