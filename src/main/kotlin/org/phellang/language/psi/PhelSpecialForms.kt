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
     *
     * `if-some` and `when-some` belong here for the same reason `if-let` and `when-let` do — their
     * registry signatures are identical (`(if-some bindings then & [else])`), and they bind on
     * non-nil rather than on truthy. They were missing, so their bindings were invisible to every
     * consumer of this set at once: unresolved on go-to-definition, absent from local completion,
     * never reported as unused or shadowed, and unrecognised as locals by the parameter hints.
     *
     * `when-first` was the same omission a second time: `(when-first bindings & body)` binds the head
     * of a collection with exactly `when-let`'s shape. Membership is decided by the registry
     * signature, not by the name.
     */
    val LET_LIKE: Set<String> = setOf(
        "let", "if-let", "when-let", "if-some", "when-some", "when-first",
        "loop", "for", "foreach", "binding", "dofor", "doseq",
    )

    /** Forms that introduce a parameter vector — the `fn` / `defn` family. */
    val FUNCTION_DEFINING: Set<String> = setOf(
        "fn", "defn", "defn-", "defmacro", "defmacro-",
    )

    /**
     * Macros that thread an expression through their remaining forms.
     *
     * Their arguments are rewritten at expansion, so neither the count nor the order the reader sees
     * survives. Two consumers ask this: the arity inspection skips a *threaded argument list*, and
     * [VARIADIC_HEADS] skips the threading call itself.
     *
     * The conditional pair is easy to forget — the arity inspection's own copy had it while
     * [VARIADIC_HEADS] did not, so `(cond-> 1 true inc)` was arity-checked and rendered `expr:` /
     * `clauses:` hints while every other threading macro was left alone.
     */
    val THREADING: Set<String> = setOf(
        "->", "->>", "as->", "some->", "some->>", "cond->", "cond->>", "doto",
    )

    /**
     * Forms whose *second* element is a name being introduced: `(defn name ...)`, `(ns name ...)`.
     *
     * Completion has nothing to offer at such a slot — every candidate is a name that already
     * exists, and accepting one silently redefines it.
     *
     * Membership is per-form, verified against each form's registry signature, because there is no
     * completion-priority bucket that means this. Consumers previously asked
     * `isSymbolType(head, SPECIAL_FORMS)`, which is a *ranking* bucket: it holds `do`, `try`,
     * `throw`, `recur`, `catch` and `quote` — whose second element is an ordinary expression — while
     * omitting `defn`, `defmacro` and the private and starred variants. The answer it gave was
     * close to inverted.
     *
     * `defmethod` is deliberately absent: `(defmethod multi-name dispatch-val ...)` names an
     * existing multimethod, so completing it there is exactly what the user wants.
     */
    val NAME_DECLARING: Set<String> = setOf(
        "def", "def-", "defn", "defn-", "defmacro", "defmacro-",
        "defstruct", "defstruct*", "definterface", "definterface*",
        "defexception", "defexception*", "defonce", "defenum",
        "declare", "defprotocol", "defrecord", "deftype", "defmulti", "deftest", "ns",
    )

    /**
     * [NAME_DECLARING] minus `ns`: the forms that declare a *program symbol*.
     *
     * `ns` names a namespace, which the consumers of this set either render specially (the folding
     * placeholder) or must not treat as a definition at all — marking a namespace name as one would
     * have the annotator paint it like a local binding.
     */
    val DEFINITION_FORMS: Set<String> = NAME_DECLARING - "ns"

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
     *
     * [LET_LIKE], [FUNCTION_DEFINING] and [THREADING] are folded in rather than re-listed. Every form
     * in them is disqualified from positional reading for a reason this set already encodes, and
     * spelling the names twice is how `doseq` ended up in one and not the other.
     */
    val VARIADIC_HEADS: Set<String> = setOf(
        "if", "if-not", "when", "when-not",
        "do", "recur", "def", "def-",
        "defstruct", "definterface", "defexception", "declare", "ns", "quote", "var",
        "try", "catch", "finally", "throw", "case", "cond", "condp", "and", "or",
        "comment", "deftest", "is", "are", "testing", "with-output-buffer",
        "import", "require", "use", "set!", "..", ".",
    ) + LET_LIKE + FUNCTION_DEFINING + THREADING
}
