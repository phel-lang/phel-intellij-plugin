package org.phellang.annotator.highlighters

import com.intellij.lang.annotation.AnnotationHolder
import org.phellang.annotator.highlighters.rules.ConstructorClassArgumentRule
import org.phellang.annotator.highlighters.rules.DeprecatedSymbolRule
import org.phellang.annotator.highlighters.rules.FunctionCallPositionRule
import org.phellang.annotator.highlighters.rules.InteropShorthandRule
import org.phellang.annotator.highlighters.rules.KnownFormRule
import org.phellang.annotator.highlighters.rules.LocalBindingRule
import org.phellang.annotator.highlighters.rules.NamespacePrefixRule
import org.phellang.annotator.highlighters.rules.PhelHighlightDecision
import org.phellang.annotator.highlighters.rules.PhelHighlightRule
import org.phellang.annotator.highlighters.rules.PhelSymbolContext
import org.phellang.annotator.highlighters.rules.PhpQualifiedRule
import org.phellang.annotator.highlighters.rules.QualifiedSymbolRule
import org.phellang.annotator.highlighters.rules.RegularSymbolRule
import org.phellang.annotator.highlighters.rules.VariadicMarkerRule
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.language.psi.PhelSymbol

/**
 * Classifies a symbol by running an ordered chain of rules and applying the first decision made.
 *
 * The order in [RULES] is the specification. Two constraints in particular are load-bearing:
 * [LocalBindingRule] precedes [DeprecatedSymbolRule], so a binding that shadows a deprecated core
 * function is not struck through; and [PhpQualifiedRule] precedes [InteropShorthandRule], so the
 * common `php/...` case never triggers the file-wide `(:use ...)` scan.
 */
object PhelSymbolHighlighter {

    private val RULES: List<PhelHighlightRule> = listOf(
        VariadicMarkerRule,
        LocalBindingRule,
        DeprecatedSymbolRule,
        KnownFormRule,
        PhpQualifiedRule,
        InteropShorthandRule,
        ConstructorClassArgumentRule,
        QualifiedSymbolRule,
        NamespacePrefixRule,
        FunctionCallPositionRule,
        RegularSymbolRule,
    )

    fun annotateSymbol(symbol: PhelSymbol, text: String, holder: AnnotationHolder) {
        if (!PhelAnnotationUtils.isValidText(text)) return

        val context = PhelSymbolContext(symbol, text)
        val decision = RULES.firstNotNullOfOrNull { it.decide(context) } ?: return

        apply(decision, symbol, holder)
    }

    private fun apply(decision: PhelHighlightDecision, symbol: PhelSymbol, holder: AnnotationHolder) {
        when (decision) {
            is PhelHighlightDecision.Paint ->
                PhelAnnotationUtils.createAnnotation(holder, symbol, decision.attributes)

            is PhelHighlightDecision.Report ->
                decision.problems.forEach { PhelAnnotationUtils.report(holder, symbol, it) }
        }
    }
}
