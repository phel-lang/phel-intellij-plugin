package org.phellang.language.psi

/**
 * Canonical sets of Phel special-form / macro head symbols.
 *
 * These classifications are pure language knowledge and were previously copy-pasted
 * across inspections, highlighting, completion and parameter hints — which let them
 * drift (e.g. `with-output-buffer`, a variadic special form, had crept into one
 * copy of the binding-form set). Keep the language facts here, in one place.
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
