package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreFnsSetsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "complement",
        signature = "(complement f)",
        completion = CompletionInfo(
            tailText = "Returns a function that takes the same arguments as f and returns the opposite truth value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a function that takes the same arguments as <code>f</code> and returns the opposite truth value.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L97",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "constantly",
        signature = "(constantly x)",
        completion = CompletionInfo(
            tailText = "Returns a function that always returns x and ignores any passed arguments",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a function that always returns <code>x</code> and ignores any passed arguments.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L92",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "deep-merge",
        signature = "(deep-merge & args)",
        completion = CompletionInfo(
            tailText = "Recursively merges data structures",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Recursively merges data structures.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L265",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "difference",
        signature = "(difference set & sets)",
        completion = CompletionInfo(
            tailText = "Difference between multiple sets into a new one",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Difference between multiple sets into a new one.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L54",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "flatten",
        signature = "(flatten coll)",
        completion = CompletionInfo(
            tailText = "Flattens nested sequential structure into a lazy sequence of all leaf values",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Flattens nested sequential structure into a lazy sequence of all leaf values.",
            example = "(flatten [[1 2] [3 [4 5]] 6]) ; =&gt; @[1 2 3 4 5 6]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L229",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "fnil",
        signature = "(fnil f & defaults)",
        completion = CompletionInfo(
            tailText = "Returns a function that replaces nil arguments with the provided defaults before calling f",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a function that replaces nil arguments with the provided defaults before calling f. The number of defaults determines how many leading arguments are nil-checked.
""",
            example = "(let [safe-inc (fnil inc 0)] (safe-inc nil)) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L130",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "intersection",
        signature = "(intersection set & sets)",
        completion = CompletionInfo(
            tailText = "Intersect multiple sets into a new one",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Intersect multiple sets into a new one.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L35",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "juxt",
        signature = "(juxt & fs)",
        completion = CompletionInfo(
            tailText = "Takes a list of functions and returns a new function that is the juxtaposition of those functions",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a list of functions and returns a new function that is the juxtaposition of those functions.
""",
            example = "((juxt inc dec #(* % 2)) 10) =&gt; [11 9 20]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L113",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "memoize",
        signature = "(memoize f)",
        completion = CompletionInfo(
            tailText = "Returns a memoized version of the function f",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a memoized version of the function <code>f</code>. The memoized function caches the return value for each set of arguments.
""",
            example = "(defn fact [n]\n  (if (zero? n)\n    1\n    (* n (fact (dec n)))))\n(def fact-memo (memoize fact))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L147",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "memoize-lru",
        signature = "(memoize-lru f)",
        completion = CompletionInfo(
            tailText = "Returns a memoized version of the function f with an LRU (Least Recently Used) cache limited to m...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a memoized version of the function <code>f</code> with an LRU (Least Recently Used) cache limited to <code>max-size</code> entries. When the cache exceeds <code>max-size</code>, the least recently used entry is evicted. This prevents unbounded memory growth in long-running processes.<br /><br />
Without arguments, uses a default cache size of 128 entries.
""",
            example = "(defn fact [n]\n  (if (zero? n)\n    1\n    (* n (fact (dec n)))))\n(def fact-memo (memoize-lru fact 100))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L169",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "merge-with",
        signature = "(merge-with f & hash-maps)",
        completion = CompletionInfo(
            tailText = "Merges multiple maps into one new map",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Merges multiple maps into one new map. If a key appears in more than one collection, the result of <code>(f current-val next-val)</code> is used.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L245",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "partial",
        signature = "(partial f & args)",
        completion = CompletionInfo(
            tailText = "Takes a function f and fewer than the normal number of arguments to f, and returns a function tha...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a function <code>f</code> and fewer than the normal number of arguments to <code>f</code>, and returns a function that takes a variable number of additional arguments. When called, <code>f</code> will be invoked with the bound <code>args</code> prepended to the additional arguments.
""",
            example = "((partial + 10) 1 2) ; =&gt; 13",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L123",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some-fn",
        signature = "(some-fn)",
        completion = CompletionInfo(
            tailText = "Takes a variadic set of predicates and returns a function f that, when called with any number of ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a variadic set of predicates and returns a function <code>f</code> that, when called with any number of arguments, returns the first logical true value produced by applying any of the composing predicates to any of its arguments, and <code>false</code> when none match. The returned function short-circuits on the first truthy result: arguments after it are not inspected, and predicates after it are not tried. Predicates are consulted in the order supplied; for a given predicate, arguments are consulted left-to-right.
""",
            example = "((some-fn even? nil?) 1 2) ; =&gt; true\n((some-fn pos? even?) -3 -1) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L102",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "subset?",
        signature = "(subset? s1 s2)",
        completion = CompletionInfo(
            tailText = "Returns true if s1 is a subset of s2, i",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>s1</code> is a subset of <code>s2</code>, i.e. every element in <code>s1</code> is also in <code>s2</code>.
""",
            example = "(subset? (hash-set 1 2) (hash-set 1 2 3)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L64",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "superset?",
        signature = "(superset? s1 s2)",
        completion = CompletionInfo(
            tailText = "Returns true if s1 is a superset of s2, i",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>s1</code> is a superset of <code>s2</code>, i.e. every element in <code>s2</code> is also in <code>s1</code>.
""",
            example = "(superset? (hash-set 1 2 3) (hash-set 1 2)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L72",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "symmetric-difference",
        signature = "(symmetric-difference set & sets)",
        completion = CompletionInfo(
            tailText = "Symmetric difference between multiple sets into a new one",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Symmetric difference between multiple sets into a new one.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L59",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "tree-seq",
        signature = "(tree-seq branch? children root)",
        completion = CompletionInfo(
            tailText = "Returns a vector of the nodes in the tree, via a depth-first walk",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of the nodes in the tree, via a depth-first walk. branch? is a function with one argument that returns true if the given node has children. children must be a function with one argument that returns the children of the node. root the root node of the tree.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L215",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "union",
        signature = "(union & sets)",
        completion = CompletionInfo(
            tailText = "Union multiple sets into a new one",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Union multiple sets into a new one.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L16",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "update-keys",
        signature = "(update-keys m f)",
        completion = CompletionInfo(
            tailText = "Returns a map with f applied to each key",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a map with <code>f</code> applied to each key.
""",
            example = "(update-keys {:a 1 :b 2} name) ; =&gt; {\"a\" 1, \"b\" 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L276",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "update-vals",
        signature = "(update-vals m f)",
        completion = CompletionInfo(
            tailText = "Returns a map with f applied to each value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a map with <code>f</code> applied to each value.
""",
            example = "(update-vals {:a 1 :b 2} inc) ; =&gt; {:a 2, :b 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/fns-sets.phel#L286",
                docs = "",
            ),
        ),
    )
)
