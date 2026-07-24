package org.phellang.completion.infrastructure

import org.phellang.registry.PhelCompletionPriority

/**
 * What a locally-bound symbol offered in completion *is*: a function parameter, or one of the
 * three flavours of binding-vector entry.
 *
 * This is the closed set that [PhelCompletionUtils.addLocalSymbolCompletion] accepts. It used to
 * be a plain `String` that served double duty as the lookup element's tail text and as a
 * dispatch key for the completion priority, which meant a typo silently fell through to the
 * wrong priority. Both roles now hang off the enum constant, so the display text and the
 * ranking of a kind are declared in one place and cannot drift apart.
 *
 * Only symbols bound in the *current* scope live here. The edited file's own top-level
 * definitions take a different path ([PhelCompletionUtils.addRankedCompletion], which is handed
 * its priority explicitly), and symbols from other files come from the project symbol index.
 */
enum class PhelLocalSymbolKind(
    /** Rendered as the lookup element's tail text, so it is user-visible. */
    val displayText: String,
    val priority: PhelCompletionPriority,
) {
    FUNCTION_PARAMETER("Function Parameter", PhelCompletionPriority.CURRENT_SCOPE_LOCALS),
    LET_BINDING("Let Binding", PhelCompletionPriority.CURRENT_SCOPE_LOCALS),
    LOOP_BINDING("Loop Binding", PhelCompletionPriority.CURRENT_SCOPE_LOCALS),

    /** A `for` or `binding` entry: still a current-scope local, just without a nicer name. */
    LOCAL_VARIABLE("Local Variable", PhelCompletionPriority.CURRENT_SCOPE_LOCALS),
}
