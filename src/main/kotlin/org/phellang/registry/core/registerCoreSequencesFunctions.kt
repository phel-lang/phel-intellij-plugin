package org.phellang.registry.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreSequencesFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "assoc",
        signature = "(assoc ds key value & more)",
        completion = CompletionInfo(
            tailText = "Associates one or more key-value pairs with a collection",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Associates one or more key-value pairs with a collection. Additional key-value pairs beyond the first are applied in order. Throws if an odd number of extra arguments is provided.
""",
            example = "(assoc {:a 1} :b 2) ; =&gt; {:a 1, :b 2}\n(assoc {:a 1} :b 2 :c 3) ; =&gt; {:a 1, :b 2, :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L278",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dissoc",
        signature = "(dissoc ds & ks)",
        completion = CompletionInfo(
            tailText = "Returns ds without the given keys",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>ds</code> without the given keys. With no keys returns <code>ds</code> unchanged.
""",
            example = "(dissoc {:a 1 :b 2} :b) ; =&gt; {:a 1}\n(dissoc {:a 1 :b 2 :c 3} :a :c) ; =&gt; {:b 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L318",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "get",
        signature = "(get ds k & [opt])",
        completion = CompletionInfo(
            tailText = "Gets the value at key in a collection",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Gets the value at key in a collection. Returns default if not found.",
            example = "(get {:a 1} :a) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L91",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nth",
        signature = "(nth coll index)\n(nth coll index not-found)",
        completion = CompletionInfo(
            tailText = "Returns the value at index in coll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the value at <code>index</code> in <code>coll</code>. Throws an<br />
   OutOfBoundsException if the index is out of range and no<br />
   <code>not-found</code> value is supplied. For indexed collections (vectors,<br />
   strings) this is O(1); for sequences it is O(n).
""",
            example = "(nth [1 2 3] 1) ; =&gt; 2\n(nth [1 2 3] 5 :default) ; =&gt; :default",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L136",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nthnext",
        signature = "(nthnext coll n)",
        completion = CompletionInfo(
            tailText = "Returns the nth next of coll, (seq coll) when n is 0",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the nth next of <code>coll</code>, <code>(seq coll)</code> when <code>n</code> is 0. Returns nil if there are fewer than <code>n</code> elements remaining.
""",
            example = "(nthnext [1 2 3 4 5] 2) ; =&gt; (3 4 5)\n(nthnext [1 2] 5) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L207",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nthrest",
        signature = "(nthrest coll n)",
        completion = CompletionInfo(
            tailText = "Returns the nth rest of coll, coll when n is less than 1",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the nth rest of <code>coll</code>, <code>coll</code> when <code>n</code> is less than 1. Returns an empty list once the collection is exhausted.
""",
            example = "(nthrest [1 2 3 4 5] 2) ; =&gt; [3 4 5]\n(nthrest [1 2] 5) ; =&gt; ()\n(nthrest nil 0) ; =&gt; nil\n(nthrest nil 3) ; =&gt; ()",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L194",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "peek",
        signature = "(peek coll)",
        completion = CompletionInfo(
            tailText = "Returns the head of a vector / PHP array (the last element) or the head of a list / lazy sequence...",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the head of a vector / PHP array (the last element) or the head of a list / lazy sequence / queue (the first element). Returns nil if <code>coll</code> is empty or nil.
""",
            example = "(peek [1 2 3]) ; =&gt; 3\n(peek '(:a :b :c)) ; =&gt; :a",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L27",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "pop",
        signature = "(pop coll)",
        completion = CompletionInfo(
            tailText = "Removes the last element of a collection",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes the last element of a collection. Returns nil for nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L66",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "push",
        signature = "(push coll x)",
        completion = CompletionInfo(
            tailText = "Inserts x at the end of the sequence coll",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Inserts <code>x</code> at the end of the sequence <code>coll</code>.
""",
            example = null,
            deprecation = DeprecationInfo(version = "0.25.0", replacement = "conj"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L50",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "put",
        signature = "(put ds key value)",
        completion = CompletionInfo(
            tailText = "Puts value mapped to key on the datastructure ds",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Puts <code>value</code> mapped to <code>key</code> on the datastructure <code>ds</code>. Returns <code>ds</code>.
""",
            example = null,
            deprecation = DeprecationInfo(version = "0.25.0", replacement = "assoc"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L293",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "unset",
        signature = "(unset ds key)",
        completion = CompletionInfo(
            tailText = "Returns ds without key",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>ds</code> without <code>key</code>.
""",
            example = null,
            deprecation = DeprecationInfo(version = "0.25.0", replacement = "dissoc"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/sequences.phel#L328",
                docs = "",
            ),
        ),
    )
)
