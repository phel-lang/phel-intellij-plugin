package org.phellang.registry.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreTransientsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "assoc!",
        signature = "(assoc! tcoll key value & more)",
        completion = CompletionInfo(
            tailText = "Associates one or more key-value pairs with a transient collection, mutating it in place",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Associates one or more key-value pairs with a transient collection,<br />
   mutating it in place. Works on transient hash-maps and transient vectors.<br />
   Variadic forms apply each <code>key-value</code> pair in order. A trailing key<br />
   without a value is associated with <code>nil</code>. Raises<br />
   <code>InvalidArgumentException</code> when <code>tcoll</code> is not a supported transient<br />
   collection. Matches Clojure's <code>assoc!</code> semantics.
""",
            example = "(persistent! (assoc! (transient {}) :a 1 :b 2)) ; =&gt; {:a 1, :b 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/transients.phel#L103",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "conj!",
        signature = "(conj!)\n(conj! tcoll)\n(conj! tcoll value)\n(conj! tcoll value & more)",
        completion = CompletionInfo(
            tailText = "Adds value to the transient collection tcoll, mutating it in place, and returns tcoll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Adds <code>value</code> to the transient collection <code>tcoll</code>, mutating it in place, and returns <code>tcoll</code>. The 'addition' may happen at different 'places' depending on the concrete transient type: transient vectors append at the tail, transient hash-sets add the element (no-op if already present), and transient hash-maps treat <code>value</code> as a <code>[key value]</code> pair (or an associative collection of entries). With zero arguments returns a new empty transient vector. With one argument returns <code>tcoll</code> unchanged. Variadic forms reduce <code>conj!</code> over the remaining values. Raises <code>InvalidArgumentException</code> when <code>tcoll</code> is not a transient collection. Matches Clojure's <code>conj!</code> semantics.
""",
            example = "(persistent (conj! (transient [1 2]) 3)) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/transients.phel#L80",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "disj!",
        signature = "(disj! tcoll)\n(disj! tcoll value)\n(disj! tcoll value & more)",
        completion = CompletionInfo(
            tailText = "Removes one or more elements from a transient set, mutating it in place",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes one or more elements from a transient set, mutating it in place. Raises <code>InvalidArgumentException</code> when <code>tcoll</code> is not a transient set. Matches Clojure's <code>disj!</code> semantics.
""",
            example = "(persistent! (disj! (transient #{1 2 3}) 2)) ; =&gt; #{1 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/transients.phel#L153",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dissoc!",
        signature = "(dissoc! tcoll)\n(dissoc! tcoll key)\n(dissoc! tcoll key & ks)",
        completion = CompletionInfo(
            tailText = "Dissociates one or more keys from a transient map, mutating it in place",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Dissociates one or more keys from a transient map, mutating it in place. Raises <code>InvalidArgumentException</code> when <code>tcoll</code> is not a transient map. Matches Clojure's <code>dissoc!</code> semantics.
""",
            example = "(persistent! (dissoc! (transient {:a 1 :b 2}) :a)) ; =&gt; {:b 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/transients.phel#L134",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "persistent",
        signature = "(persistent coll)",
        completion = CompletionInfo(
            tailText = "Converts a transient collection back to a persistent collection",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Converts a transient collection back to a persistent collection.",
            example = "(def t (transient {}))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/transients.phel#L27",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "persistent!",
        signature = "(persistent! coll)",
        completion = CompletionInfo(
            tailText = "Converts a transient collection back to a persistent collection",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Converts a transient collection back to a persistent collection. Alias for <code>persistent</code>, matching Clojure's <code>persistent!</code> naming.
""",
            example = "(persistent! (conj! (transient []) 1 2 3)) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/transients.phel#L34",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "pop!",
        signature = "(pop! tcoll)",
        completion = CompletionInfo(
            tailText = "Removes the last element from a transient vector, mutating it in place",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes the last element from a transient vector, mutating it in place. Raises <code>InvalidArgumentException</code> when <code>tcoll</code> is not a transient vector. Matches Clojure's <code>pop!</code> semantics.
""",
            example = "(persistent! (pop! (transient [1 2 3]))) ; =&gt; [1 2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/transients.phel#L162",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "transient",
        signature = "(transient coll)",
        completion = CompletionInfo(
            tailText = "Converts a persistent collection to a transient collection for efficient updates",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Converts a persistent collection to a transient collection for efficient updates.<br /><br />
Transient collections provide faster performance for multiple sequential updates. Use <code>persistent</code> to convert back.
""",
            example = "(def t (transient []))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/transients.phel#L18",
                docs = "",
            ),
        ),
    )
)
