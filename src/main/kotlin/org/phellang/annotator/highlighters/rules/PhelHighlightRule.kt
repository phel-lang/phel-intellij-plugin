package org.phellang.annotator.highlighters.rules

import com.intellij.openapi.editor.colors.TextAttributesKey
import org.phellang.annotator.validators.PhelValidationProblem
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * What a rule decided about a symbol.
 *
 * A rule returning `null` instead means "not mine", which passes the symbol to the next rule.
 */
sealed interface PhelHighlightDecision {
    /** Colour the symbol with [attributes]. */
    data class Paint(val attributes: TextAttributesKey) : PhelHighlightDecision

    /** Surface validator findings instead of colouring. Never empty. */
    data class Report(val problems: List<PhelValidationProblem>) : PhelHighlightDecision
}

/**
 * One classification step of `PhelSymbolHighlighter`'s ordered chain.
 *
 * The order of the chain is behaviour, not style: a local binding that shadows a deprecated core
 * function has to be classified as a parameter before the deprecation rule paints it struck through.
 */
fun interface PhelHighlightRule {
    fun decide(context: PhelSymbolContext): PhelHighlightDecision?
}

/**
 * A symbol plus the file-level facts the rules ask about repeatedly.
 *
 * [usedClasses] is lazy on purpose: extracting the `(:use ...)` set walks the whole file, and the
 * only rules that need it sit late in the chain, so most symbols never pay for it.
 */
class PhelSymbolContext(val symbol: PhelSymbol, val text: String) {
    val file: PhelFile? = symbol.containingFile as? PhelFile

    val usedClasses: Set<String> by lazy(LazyThreadSafetyMode.NONE) {
        file?.let { PhelNamespaceUtils.extractUsedClasses(it) } ?: emptySet()
    }
}
