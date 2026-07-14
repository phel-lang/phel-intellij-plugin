package org.phellang.annotator.validators

import com.intellij.codeInsight.intention.IntentionAction

/**
 * A problem to report on a symbol: the warning to show, how loudly, and the fix to offer when one
 * applies. Validators return a list of these — empty means the symbol is fine — and a highlighter's
 * only job is to render whatever comes back.
 *
 * Deliberately one type rather than a per-validator hierarchy. Nothing dispatches on *which*
 * validator produced a problem: each validator is called from a site that already knows what it
 * asked for, so a sealed supertype would only add a `when` that never runs. A list rather than a
 * single nullable value because one symbol can warrant two annotations (an import that neither
 * exists nor is used), and because it lets a validator express precedence — a duplicate import
 * suppresses the unused-import warning — without leaking a flag for the caller to branch on.
 */
data class PhelValidationProblem(
    val message: String,
    val quickFix: IntentionAction? = null,
    val severity: Severity = Severity.WARNING,
) {
    enum class Severity { WARNING, WEAK_WARNING }
}
