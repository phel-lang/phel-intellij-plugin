package org.phellang.language.psi

/**
 * Canonical sets of Phel special-form / macro head symbols.
 *
 * Pure language knowledge shared by inspections, highlighting, completion and parameter
 * hints. Keep the facts here, in one place — per-feature copies have drifted before
 * (e.g. `with-output-buffer`, a variadic special form, crept into one binding-form set).
 */
object PhelSpecialForms {
    /**
     * Forms whose second element is a binding vector `[name value ...]`, i.e. forms
     * that introduce local names: `(let [x 1] ...)`, `(loop [...] ...)`, etc.
     */
    val LET_LIKE: Set<String> = setOf(
        "let", "if-let", "when-let", "loop", "for", "foreach", "binding", "dofor",
    )

    /** Forms that introduce a parameter vector — the `fn` / `defn` family. */
    val FUNCTION_DEFINING: Set<String> = setOf(
        "fn", "defn", "defn-", "defmacro", "defmacro-",
    )
}
