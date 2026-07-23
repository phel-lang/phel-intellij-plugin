package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreFnsSetsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "arity",
        signature = "(arity f)",
        completion = CompletionInfo(
            tailText = "Returns the number of required parameters of the function f",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the number of required parameters of the function <code>f</code>. For a variadic function this is the number of parameters before <code>&</code>. A multi-arity function compiles to a single variadic dispatch, so it reports <code>0</code>.
""",
            example = "(arity inc) ; =&gt; 1\n(arity assoc) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L142",
                docs = "",
            ),
        ),
    ),
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L131",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L126",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L343",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L55",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "every-pred",
        signature = "(every-pred)\n(every-pred p & ps)",
        completion = CompletionInfo(
            tailText = "Takes a variadic set of predicates and returns a function f that, when called with any number of ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a variadic set of predicates and returns a function <code>f</code> that, when called with any number of arguments, returns <code>true</code> if every composing predicate returns a logical true value against every argument, and <code>false</code> otherwise. The returned function short-circuits on the first logical false result: arguments after it are not inspected, and predicates after it are not tried. Predicates are consulted in the order supplied; for a given predicate, arguments are consulted left-to-right. With no arguments the returned function returns <code>true</code>.
""",
            example = "((every-pred even? pos?) 2 4 6) ; =&gt; true\n((every-pred even? pos?) 2 -4) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L167",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L307",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L195",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "index",
        signature = "(index xrel ks)",
        completion = CompletionInfo(
            tailText = "Returns a map that groups the maps in the relation xrel by their values for the keys in ks",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a map that groups the maps in the relation <code>xrel</code> by their values for the keys in <code>ks</code>. Each key is <code>(select-keys map ks)</code> and each value is the set of maps sharing those key-values.
""",
            example = "(index (hash-set {:name \"a\" :dept 1} {:name \"b\" :dept 1}) [:dept]) ; =&gt; {{:dept 1} (hash-set {:name \"a\" :dept 1} {:name \"b\" :dept 1})}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L100",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L36",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L178",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "map-invert",
        signature = "(map-invert m)",
        completion = CompletionInfo(
            tailText = "Returns a map with the keys and values of m swapped",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a map with the keys and values of <code>m</code> swapped. When several keys share a value, the one visited last in iteration order wins.
""",
            example = "(map-invert {:a 1 :b 2}) ; =&gt; {1 :a, 2 :b}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L374",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L225",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "memoize-lru",
        signature = "(memoize-lru f)\n(memoize-lru f max-size)",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L247",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L323",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L188",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "project",
        signature = "(project xrel ks)",
        completion = CompletionInfo(
            tailText = "Returns a set of maps, keeping only the keys in ks from each map in the relation xrel",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a set of maps, keeping only the keys in <code>ks</code> from each map in the relation <code>xrel</code>.
""",
            example = "(project (hash-set {:a 1 :b 2}) [:a]) ; =&gt; (hash-set {:a 1})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L86",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rename",
        signature = "(rename xrel kmap)",
        completion = CompletionInfo(
            tailText = "Returns a set of maps like the relation xrel, with the keys of each map renamed according to kmap...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a set of maps like the relation <code>xrel</code>, with the keys of each map renamed according to <code>kmap</code> (a map of old-key to new-key).
""",
            example = "(rename (hash-set {:a 1 :b 2}) {:a :x}) ; =&gt; (hash-set {:x 1 :b 2})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L93",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "select",
        signature = "(select pred xset)",
        completion = CompletionInfo(
            tailText = "Returns a set of the elements of xset for which (pred element) is logical true",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a set of the elements of <code>xset</code> for which <code>(pred element)</code> is logical true.
""",
            example = "(select even? (hash-set 1 2 3 4)) ; =&gt; (hash-set 2 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L79",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some-fn",
        signature = "(some-fn)\n(some-fn p & ps)",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L156",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L65",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L73",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L60",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "trampoline",
        signature = "(trampoline f & args)",
        completion = CompletionInfo(
            tailText = "Calls f with any supplied args",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Calls <code>f</code> with any supplied <code>args</code>. While the return value is a function, calls it with no arguments and repeats; returns the first non-function value. Use it for stack-safe mutual recursion: have the functions return a zero-argument function wrapping the next call instead of calling it directly. Only Phel functions bounce — keywords and collections, although callable, are returned as-is. To return a function as the final value, wrap it in a collection and unwrap it after <code>trampoline</code> returns.
""",
            example = "(defn my-even? [n] (if (zero? n) true (fn [] (my-odd? (dec n)))))\n(defn my-odd? [n] (if (zero? n) false (fn [] (my-even? (dec n)))))\n(trampoline my-even? 10000) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L212",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L293",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L17",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L354",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L364",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "variadic?",
        signature = "(variadic? f)",
        completion = CompletionInfo(
            tailText = "Returns true if the function f accepts a variable number of arguments",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if the function <code>f</code> accepts a variable number of arguments. A multi-arity function compiles to a single variadic dispatch, so it always reports <code>true</code>.
""",
            example = "(variadic? +) ; =&gt; true\n(variadic? inc) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/fns-sets.phel#L149",
                docs = "",
            ),
        ),
    )
)
