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

    /**
     * Heads whose textual argument list is not a plain positional call: special forms, variadic
     * macros, threading macros, and the interop punctuation forms.
     *
     * Anything that reads a call's arguments positionally must skip these, because the count and
     * the order the reader sees are not the count and order the form actually binds. Two consumers
     * ask exactly this question — the arity-mismatch inspection (would the count be wrong?) and the
     * parameter-hint provider (would a `name:` label be wrong?) — and they kept separate copies
     * that drifted: the hints copy was missing `.`, `..`, `import`, `require`, `set!` and `use`.
     *
     * `use` is the one that bit: it is a real registry entry with the signature
     * `(use ClassName & options)`, so it resolves, and the hints provider rendered `ClassName:` /
     * `options:` inside `(use \DateTimeImmutable :as Date)`.
     */
    val VARIADIC_HEADS: Set<String> = setOf(
        "if", "if-not", "when", "when-not", "if-let", "when-let", "if-some", "when-some",
        "do", "let", "loop", "recur", "fn", "defn", "defn-", "def", "def-", "defmacro", "defmacro-",
        "defstruct", "definterface", "defexception", "declare", "ns", "quote", "var",
        "try", "catch", "finally", "throw", "case", "cond", "condp", "and", "or",
        "->", "->>", "as->", "some->", "some->>", "doto", "binding", "for", "foreach", "dofor",
        "comment", "deftest", "is", "are", "testing", "with-output-buffer",
        "import", "require", "use", "set!", "..", ".",
    )
}
