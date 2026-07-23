package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreSeqFnsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "assoc-in",
        signature = "(assoc-in ds [k & ks] v)",
        completion = CompletionInfo(
            tailText = "Associates a value in a nested data structure at the given path",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Associates a value in a nested data structure at the given path.<br /><br />
Creates intermediate maps if they don't exist.
""",
            example = "(assoc-in {:a {:b 1}} [:a :c] 2) ; =&gt; {:a {:b 1, :c 2}}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L342",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bean",
        signature = "(bean obj)",
        completion = CompletionInfo(
            tailText = "Returns a Phel map of the public properties of PHP object obj, with keyword keys",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a Phel map of the public properties of PHP object <code>obj</code>, with keyword keys. Inverse of <code>hydrate</code> for public-property objects.
""",
            example = "(bean (hydrate \"My\\\\Dto\" {:id 1})) ; =&gt; {:id 1}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L940",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bounded-count",
        signature = "(bounded-count n coll)",
        completion = CompletionInfo(
            tailText = "Returns the number of items in coll, but counts at most n when coll is not counted",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the number of items in <code>coll</code>, but counts at most <code>n</code> when <code>coll</code> is not <code>counted?</code>. If <code>coll</code> is <code>counted?</code> (its size is known in O(1)) the full <code>count</code> is returned, which may exceed <code>n</code>; otherwise the sequence is walked at most <code>n</code> steps.
""",
            example = "(bounded-count 3 [1 2 3 4 5]) ; =&gt; 5\n(bounded-count 3 (map inc (range 100))) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L693",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "butlast",
        signature = "(butlast coll)",
        completion = CompletionInfo(
            tailText = "Returns all but the last item in coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns all but the last item in <code>coll</code>. Returns <code>nil</code> when <code>coll</code> is<br />
  <code>nil</code> or has fewer than two items.
""",
            example = "(butlast [1 2 3 4]) ; =&gt; @[1 2 3]\n(butlast [0]) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L440",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "cat",
        signature = "(cat rf)",
        completion = CompletionInfo(
            tailText = "A transducer that concatenates the contents of each input into the reduction",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "A transducer that concatenates the contents of each input into the reduction.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1131",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "comp",
        signature = "(comp & fs)",
        completion = CompletionInfo(
            tailText = "Takes a list of functions and returns a function that is the composition of those functions",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a list of functions and returns a function that is the composition of those functions.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L92",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "compact",
        signature = "(compact coll & values)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence with specified values removed from coll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence with specified values removed from <code>coll</code>. If no values are specified, removes nil values by default.
""",
            example = "(compact [1 nil 2 nil 3]) ; =&gt; @[1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1417",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "concat",
        signature = "(concat & colls)",
        completion = CompletionInfo(
            tailText = "Concatenates multiple collections into a lazy sequence",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Concatenates multiple collections into a lazy sequence.",
            example = "(concat [1 2] [3 4]) ; =&gt; @[1 2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1116",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "contains-value?",
        signature = "(contains-value? coll val)",
        completion = CompletionInfo(
            tailText = "Returns true if the value is present in the given collection, otherwise returns false",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if the value is present in the given collection, otherwise returns false.
""",
            example = "(contains-value? {:a 1 :b 2} 2) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L961",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "cycle",
        signature = "(cycle coll)",
        completion = CompletionInfo(
            tailText = "Returns an infinite lazy sequence that cycles through the elements of collection",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns an infinite lazy sequence that cycles through the elements of collection. Maps are iterated as <code>[key value]</code> pairs.
""",
            example = "(take 7 (cycle [1 2 3])) ; =&gt; @[1 2 3 1 2 3 1]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1107",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dedupe",
        signature = "(dedupe & args)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence with consecutive duplicate values removed in coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence with consecutive duplicate values removed in <code>coll</code>. When called with no args, returns a transducer.
""",
            example = "(dedupe [1 1 2 2 2 3 1 1]) ; =&gt; @[1 2 3 1]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1376",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "disj",
        signature = "(disj set)\n(disj set k)\n(disj set k & ks)",
        completion = CompletionInfo(
            tailText = "Returns a new set that does not contain the given key(s)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new set that does not contain the given key(s). Works on hash-sets and sorted-sets. Removing a non-existent key is a no-op. Returns <code>nil</code> when called on <code>nil</code>.
""",
            example = "(disj #{1 2 3} 2) ; =&gt; #{1 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L237",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dissoc-in",
        signature = "(dissoc-in ds [k & ks])",
        completion = CompletionInfo(
            tailText = "Dissociates a value from a nested data structure at the given path",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Dissociates a value from a nested data structure at the given path.",
            example = "(dissoc-in {:a {:b 1 :c 2}} [:a :b]) ; =&gt; {:a {:c 2}}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L377",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "distinct",
        signature = "(distinct & args)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence with duplicated values removed in coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence with duplicated values removed in <code>coll</code>. When called with no args, returns a transducer.
""",
            example = "(distinct [1 2 1 3 2 4 3]) ; =&gt; @[1 2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L661",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "distinct?",
        signature = "(distinct? x & more)",
        completion = CompletionInfo(
            tailText = "Returns true if no two of the arguments are =",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if no two of the arguments are <code>=</code>. Requires at least one argument.
""",
            example = "(distinct? 1 2 3) ; =&gt; true\n(distinct? 1 2 1) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L685",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "doall",
        signature = "(doall coll)",
        completion = CompletionInfo(
            tailText = "Forces realization of a lazy sequence and returns it as a vector",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Forces realization of a lazy sequence and returns it as a vector.",
            example = "(doall (map println [1 2 3])) ; =&gt; [nil nil nil]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1223",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dorun",
        signature = "(dorun coll)",
        completion = CompletionInfo(
            tailText = "Forces realization of a lazy sequence for side effects, returns nil",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Forces realization of a lazy sequence for side effects, returns nil.",
            example = "(dorun (map println [1 2 3])) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1231",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "drop",
        signature = "(drop n & args)",
        completion = CompletionInfo(
            tailText = "Drops the first n elements of coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Drops the first <code>n</code> elements of <code>coll</code>. Returns a lazy sequence. When called with n only, returns a transducer.
""",
            example = "(drop 2 [1 2 3 4 5]) ; =&gt; @[3 4 5]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L396",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "drop-last",
        signature = "(drop-last coll)\n(drop-last n coll)",
        completion = CompletionInfo(
            tailText = "Drops the last n elements of coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Drops the last <code>n</code> elements of <code>coll</code>. <code>n</code> defaults to <code>1</code> when omitted, matching Clojure's <code>(drop-last coll)</code> single-arity form. Returns an empty sequence when <code>coll</code> is <code>nil</code>. Works with any seqable collection including lazy sequences and ranges.
""",
            example = "(drop-last [1 2 3 4 5]) ; =&gt; @[1 2 3 4]\n(drop-last 2 [1 2 3 4 5]) ; =&gt; @[1 2 3]\n(drop-last 5 nil) ; =&gt; @[]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L418",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "drop-while",
        signature = "(drop-while pred & args)",
        completion = CompletionInfo(
            tailText = "Drops all elements at the front of coll where (pred x) is true",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Drops all elements at the front of <code>coll</code> where <code>(pred x)</code> is true. Returns a lazy sequence. When called with pred only, returns a transducer.
""",
            example = "(drop-while #(&lt; % 5) [1 2 3 4 5 6 3 2 1]) ; =&gt; @[5 6 3 2 1]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L449",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "eduction",
        signature = "(eduction & args)",
        completion = CompletionInfo(
            tailText = "Returns a reducible/iterable applying transducers to coll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a reducible/iterable applying transducers to <code>coll</code>. The last<br />
  argument is the source collection; preceding arguments are transducers<br />
  composed left-to-right. The transformation is re-run on each consumption<br />
  (no caching), so the result is safe to reduce or iterate multiple times.
""",
            example = "(reduce + (eduction (map inc) (filter odd?) [1 2 3 4])) ; =&gt; 8",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1402",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "filter",
        signature = "(filter pred & args)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of elements where predicate returns true",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of elements where predicate returns true. When called with pred only, returns a transducer.
""",
            example = "(filter even? [1 2 3 4 5 6]) ; =&gt; @[2 4 6]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L549",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "filterv",
        signature = "(filterv pred coll)",
        completion = CompletionInfo(
            tailText = "Returns a vector of the items in coll for which (pred item) returns a logical true value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of the items in <code>coll</code> for which <code>(pred item)</code> returns a logical true value. Like <code>filter</code>, but eager and returns a vector rather than a lazy sequence.
""",
            example = "(filterv even? [1 2 3 4 5 6]) ; =&gt; [2 4 6]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L575",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "find",
        signature = "(find pred-or-coll coll-or-key)",
        completion = CompletionInfo(
            tailText = "When called with a collection first, returns [key value] when key is present",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
When called with a collection first, returns <code>[key value]</code> when <code>key</code> is present. Otherwise returns the first item in <code>coll</code> where <code>(pred item)</code> evaluates to true.
""",
            example = "(find {:a 1} :a) ; =&gt; [:a 1]\n(find #(&gt; % 5) [1 2 3 6 7 8]) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L640",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "find-index",
        signature = "(find-index pred coll)",
        completion = CompletionInfo(
            tailText = "Returns the index of the first item in coll where (pred item) evaluates to true",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the index of the first item in <code>coll</code> where <code>(pred item)</code> evaluates to true.
""",
            example = "(find-index #(&gt; % 5) [1 2 3 6 7 8]) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L649",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "frequencies",
        signature = "(frequencies coll)",
        completion = CompletionInfo(
            tailText = "Returns a map from distinct items in coll to the number of times they appear",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a map from distinct items in <code>coll</code> to the number of times they appear.<br /><br />
Works with vectors, lists, sets, and strings.
""",
            example = "(frequencies [:a :b :a :c :b :a]) ; =&gt; {:a 3, :b 2, :c 1}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L736",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "get-in",
        signature = "(get-in ds ks & [opt])",
        completion = CompletionInfo(
            tailText = "Accesses a value in a nested data structure via a sequence of keys",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Accesses a value in a nested data structure via a sequence of keys.<br /><br />
Returns <code>opt</code> (default nil) when a key is missing mid-traversal. When the path is nil or empty, returns <code>ds</code> as-is (which may be nil).
""",
            example = "(get-in {:a {:b {:c 42}}} [:a :b :c]) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L320",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "group-by",
        signature = "(group-by f coll)",
        completion = CompletionInfo(
            tailText = "Returns a map of the elements of coll keyed by the result of f on each element",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a map of the elements of coll keyed by the result of <code>f</code> on each element.
""",
            example = "(group-by count [\"a\" \"bb\" \"c\" \"ddd\" \"ee\"]) ; =&gt; {1 [\"a\" \"c\"], 2 [\"bb\" \"ee\"], 3 [\"ddd\"]}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1255",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "hydrate",
        signature = "(hydrate class-name m)",
        completion = CompletionInfo(
            tailText = "Creates an instance of PHP class class-name (a class-string) without invoking its constructor, th...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates an instance of PHP class <code>class-name</code> (a class-string) without invoking its constructor, then sets each declared property named by the keyword keys of map <code>m</code>. Mirrors how ORMs/serializers rebuild entities from raw data; pairs with <code>bean</code> for the reverse direction.
""",
            example = "(hydrate \"My\\\\Dto\" {:id 1 :name \"x\"})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L950",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "interleave",
        signature = "(interleave & colls)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of the first item in each colls, then the second item in each, until any ...",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of the first item in each <code>colls</code>, then the second item in each, until any one of the collections is exhausted. Any remaining items in the other collections are ignored. Returns an empty sequence when no collections are supplied or when any collection is empty or <code>nil</code>. Maps are iterated as <code>[key value]</code> pairs.
""",
            example = "(interleave [1 2 3] [:a :b :c]) ; =&gt; @[1 :a 2 :b 3 :c]\n(interleave [1 2 3] [:a :b]) ; =&gt; @[1 :a 2 :b]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1206",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "interpose",
        signature = "(interpose sep & args)",
        completion = CompletionInfo(
            tailText = "Returns elements separated by a separator",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns elements separated by a separator. Returns a lazy sequence. When called with sep only, returns a transducer.
""",
            example = "(interpose 0 [1 2 3]) ; =&gt; @[1 0 2 0 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1162",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "into",
        signature = "(into to & rest)",
        completion = CompletionInfo(
            tailText = "Returns to with all elements of from added",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>to</code> with all elements of <code>from</code> added.<br /><br />
When <code>from</code> is associative, it is treated as a sequence of key-value pairs. Supports persistent and transient collections.
""",
            example = "(into [] '(1 2 3)) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L132",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "invert",
        signature = "(invert map)",
        completion = CompletionInfo(
            tailText = "Returns a new map where the keys and values are swapped",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new map where the keys and values are swapped.<br /><br />
If map has duplicated values, some keys will be ignored.
""",
            example = "(invert {:a 1 :b 2 :c 3}) ; =&gt; {1 :a, 2 :b, 3 :c}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1334",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "iterate",
        signature = "(iterate f x)",
        completion = CompletionInfo(
            tailText = "Returns an infinite lazy sequence of x, (f x), (f (f x)), and so on",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns an infinite lazy sequence of x, (f x), (f (f x)), and so on.",
            example = "(take 5 (iterate inc 0)) ; =&gt; @[0 1 2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1074",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "iterator-seq",
        signature = "(iterator-seq traversable)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence over a PHP Traversable (Iterator, Generator, IteratorAggregate,",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence over a PHP <code>Traversable</code> (Iterator, Generator, IteratorAggregate, ...), pulling one element at a time. Unlike the eager coercion that materialises a <code>Traversable</code> via <code>iterator_to_array</code>, this streams a large or infinite source (DB cursor, stream reader, paginated API), so <code>take</code>/<code>map</code>/<code>filter</code> only pull what they consume.
""",
            example = "(take 3 (iterator-seq (php/new \\ArrayIterator (php/array 1 2 3 4 5)))) ; =&gt; @[1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1100",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keep",
        signature = "(keep pred & args)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of non-nil results of applying function to elements",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of non-nil results of applying function to elements. When called with f only, returns a transducer.
""",
            example = "(keep #(when (even? %) (* % %)) [1 2 3 4 5]) ; =&gt; @[4 16]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L592",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keep-indexed",
        signature = "(keep-indexed pred & args)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of non-nil results of (pred i x)",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of non-nil results of <code>(pred i x)</code>. When called with f only, returns a transducer.
""",
            example = "(keep-indexed #(when (even? %1) %2) [\"a\" \"b\" \"c\" \"d\"]) ; =&gt; @[\"a\" \"c\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L609",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "key",
        signature = "(key entry)",
        completion = CompletionInfo(
            tailText = "Returns the key of a map entry",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the key of a map entry. Accepts a typed<br />
  <code>Phel\Lang\Collections\Map\MapEntry</code> value or a 2-element vector<br />
  <code>[key value]</code> (the form Phel maps yield when iterated).
""",
            example = "(key (first (pairs {:a 1}))) ; =&gt; :a",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L770",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keys",
        signature = "(keys coll)",
        completion = CompletionInfo(
            tailText = "Returns a sequence of all keys in a map, or nil when the map is nil or empty",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a sequence of all keys in a map, or <code>nil</code> when the map is <code>nil</code> or empty.
""",
            example = "(keys {:a 1 :b 2}) ; =&gt; [:a :b]\n(keys nil) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L749",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "kvs",
        signature = "(kvs coll)",
        completion = CompletionInfo(
            tailText = "Returns a vector of key-value pairs like [k1 v1 k2 v2 k3 v3",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of key-value pairs like <code>[k1 v1 k2 v2 k3 v3 ...]</code>.
""",
            example = "(kvs {:a 1 :b 2}) ; =&gt; [:a 1 :b 2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L855",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "last",
        signature = "(last coll)",
        completion = CompletionInfo(
            tailText = "Returns the last element of coll or nil if coll is empty or nil",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the last element of <code>coll</code> or nil if <code>coll</code> is empty or nil.
""",
            example = "(last [1 2 3]) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L426",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "lazy-cat",
        signature = "(lazy-cat & colls)",
        completion = CompletionInfo(
            tailText = "Concatenates collections into a lazy sequence (expands to concat)",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Concatenates collections into a lazy sequence (expands to concat).",
            example = "(lazy-cat [1 2] [3 4]) ; =&gt; @[1 2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1068",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "lazy-seq",
        signature = "(lazy-seq & body)",
        completion = CompletionInfo(
            tailText = "Creates a lazy sequence that evaluates the body only when accessed",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a lazy sequence that evaluates the body only when accessed.",
            example = "(lazy-seq (cons 1 (lazy-seq nil))) ; =&gt; @[1]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1059",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "map",
        signature = "(map f & colls)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of the result of applying f to all of the first items in each coll, follo...",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of the result of applying <code>f</code> to all of the first items in each coll,<br />
   followed by applying <code>f</code> to all the second items in each coll until anyone of the colls is exhausted.<br /><br />
When given a single collection, applies the function to each element.<br />
  When applied to a map, iterates over entries as <code>[key value]</code> pairs,<br />
  matching Clojure semantics.<br />
  With multiple collections, applies the function to corresponding elements from each collection,<br />
  stopping when the shortest collection is exhausted.
""",
            example = "(map inc [1 2 3]) ; =&gt; @[2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L59",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "map-entry",
        signature = "(map-entry k v)",
        completion = CompletionInfo(
            tailText = "Returns a typed Phel\\Lang\\Collections\\Map\\MapEntry for k/v",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a typed <code>Phel\Lang\Collections\Map\MapEntry</code> for <code>k</code>/<code>v</code>. Equal to the two-element vector <code>[k v]</code> so existing code that destructures <code>[k v]</code> keeps working.
""",
            example = "(map-entry :a 1) ; =&gt; [:a 1]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L763",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "map-indexed",
        signature = "(map-indexed f coll)",
        completion = CompletionInfo(
            tailText = "Maps a function over a collection with index",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Maps a function over a collection with index. Returns a lazy sequence.<br /><br />
Applies <code>f</code> to each element in <code>xs</code>. <code>f</code> is a two-argument function where the first argument is the index (0-based) and the second is the element itself. Works with infinite sequences.
""",
            example = "(map-indexed vector [:a :b :c]) ; =&gt; @[[0 :a] [1 :b] [2 :c]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1192",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "mapcat",
        signature = "(mapcat f & args)",
        completion = CompletionInfo(
            tailText = "Maps a function over one or more collections and concatenates the results",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Maps a function over one or more collections and concatenates the results.<br />
  Returns a lazy sequence. When called with <code>f</code> alone, returns a transducer.<br /><br />
With a single collection behaves like <code>(apply concat (map f coll))</code>. With<br />
  multiple collections, <code>f</code> is called with corresponding elements from each<br />
  (stopping when the shortest is exhausted) and the resulting sequences are<br />
  concatenated.
""",
            example = "(mapcat reverse [[1 2] [3 4]]) ; =&gt; @[2 1 4 3]\n(mapcat list [:a :b :c] [1 2 3]) ; =&gt; @[:a 1 :b 2 :c 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1138",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "mapv",
        signature = "(mapv f coll & colls)",
        completion = CompletionInfo(
            tailText = "Returns a vector consisting of the result of applying f to the set of first items of each coll, f...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector consisting of the result of applying <code>f</code> to the set of first items of each coll, followed by applying <code>f</code> to the set of second items in each coll, until any one of the colls is exhausted. Like <code>map</code>, but eager and returns a vector rather than a lazy sequence.
""",
            example = "(mapv inc [1 2 3]) ; =&gt; [2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L568",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "merge",
        signature = "(merge)\n(merge map)\n(merge map & more)",
        completion = CompletionInfo(
            tailText = "Merges multiple maps into one new map",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Merges multiple maps into one new map.<br /><br />
If a key appears in more than one collection, later values replace previous ones.
""",
            example = "(merge {:a 1 :b 2} {:b 3 :c 4}) ; =&gt; {:a 1, :b 3, :c 4}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1301",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "pairs",
        signature = "(pairs coll)",
        completion = CompletionInfo(
            tailText = "Gets the pairs of an associative data structure",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Gets the pairs of an associative data structure.",
            example = "(pairs {:a 1 :b 2}) ; =&gt; [[:a 1] [:b 2]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L847",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "partition",
        signature = "(partition n coll)\n(partition n step coll)\n(partition n step pad coll)",
        completion = CompletionInfo(
            tailText = "Partitions coll into chunks of n elements",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Partitions <code>coll</code> into chunks of <code>n</code> elements.<br /><br />
With one collection argument, returns consecutive non-overlapping chunks and drops any incomplete final chunk. <code>step</code> (default <code>n</code>) is how far to advance between chunks, so a <code>step</code> smaller than <code>n</code> yields overlapping chunks. With a <code>pad</code> collection, a final incomplete chunk is filled from <code>pad</code> up to <code>n</code> elements (kept even when <code>pad</code> runs short); without <code>pad</code> the incomplete final chunk is dropped. Each chunk is a vector.
""",
            example = "(partition 3 [1 2 3 4 5 6 7]) ; =&gt; @[[1 2 3] [4 5 6]]\n(partition 2 1 [1 2 3]) ; =&gt; @[[1 2] [2 3]]\n(partition 3 3 [:x] [1 2 3 4]) ; =&gt; @[[1 2 3] [4 :x]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1429",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "partition-all",
        signature = "(partition-all n coll)\n(partition-all n step coll)",
        completion = CompletionInfo(
            tailText = "Partitions collection into chunks of size n, including any incomplete trailing chunks",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Partitions collection into chunks of size <code>n</code>, including any incomplete trailing chunks. <code>step</code> (default <code>n</code>) is how far to advance between chunks, so a <code>step</code> smaller than <code>n</code> yields overlapping chunks. Each chunk is a vector.
""",
            example = "(partition-all 3 [1 2 3 4 5 6 7]) ; =&gt; @[[1 2 3] [4 5 6] [7]]\n(partition-all 3 2 [0 1 2 3 4]) ; =&gt; @[[0 1 2] [2 3 4] [4]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1459",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "partition-by",
        signature = "(partition-by f coll)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of partitions",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of partitions. Applies <code>f</code> to each value in <code>coll</code>, splitting them each time the return value changes.
""",
            example = "(partition-by #(&lt; % 3) [1 2 3 4 5 1 2]) ; =&gt; @[[1 2] [3 4 5] [1 2]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1366",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "phel->php",
        signature = "(phel->php x)",
        completion = CompletionInfo(
            tailText = "Recursively converts a Phel data structure to a PHP array",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Recursively converts a Phel data structure to a PHP array.",
            example = "(phel-&gt;php {:a [1 2 3] :b {:c 4}}) ; =&gt; &lt;PHP-Array [\"a\":&lt;PHP-Array [1, 2, 3]&gt;, \"b\":&lt;PHP-Array [\"c\":4]&gt;]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L886",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "php->phel",
        signature = "(php->phel x)",
        completion = CompletionInfo(
            tailText = "Recursively converts a PHP array to Phel data structures",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Recursively converts a PHP array to Phel data structures.<br /><br />
Indexed PHP arrays become vectors, associative PHP arrays become maps.
""",
            example = "(php-&gt;phel (php-associative-array \"a\" 1 \"b\" 2)) ; =&gt; {\"a\" 1, \"b\" 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L918",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "php-array-to-map",
        signature = "(php-array-to-map arr)",
        completion = CompletionInfo(
            tailText = "Converts a PHP Array to a Phel map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Converts a PHP Array to a Phel map.",
            example = "(php-array-to-map (php-associative-array \"a\" 1 \"b\" 2)) ; =&gt; {\"a\" 1, \"b\" 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L866",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "put-in",
        signature = "(put-in ds ks v)",
        completion = CompletionInfo(
            tailText = "Puts a value into a nested data structure",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Puts a value into a nested data structure.",
            example = null,
            deprecation = DeprecationInfo(version = "0.25.0", replacement = "assoc-in"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L353",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "realized?",
        signature = "(realized? coll)",
        completion = CompletionInfo(
            tailText = "Returns true if a lazy sequence, delay, promise, or future has been realized, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if a lazy sequence, delay, promise, or future has been realized, false otherwise.
""",
            example = "(realized? (lazy-seq (cons 1 nil))) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1241",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reductions",
        signature = "(reductions f coll)\n(reductions f init coll)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of the intermediate values of the reduction (as per reduce) of coll by f,...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of the intermediate values of the reduction (as per reduce) of coll by f, starting with init when supplied.
""",
            example = "(reductions + [1 2 3 4]) ; =&gt; @[1 3 6 10]\n(reductions + 0 [1 2 3 4]) ; =&gt; @[0 1 3 6 10]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1081",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "remove",
        signature = "(remove pred & args)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of elements where predicate returns false",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of elements where predicate returns false. Opposite of filter. When called with pred only, returns a transducer.
""",
            example = "(remove even? [1 2 3 4 5 6]) ; =&gt; @[1 3 5]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L582",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rename-keys",
        signature = "(rename-keys m kmap)",
        completion = CompletionInfo(
            tailText = "Returns the map with keys renamed according to kmap",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the map with keys renamed according to kmap. Keys not present in kmap are left unchanged.
""",
            example = "(rename-keys {:a 1 :b 2 :c 3} {:a :x :b :y}) ; =&gt; {:x 1, :y 2, :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1324",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "repeat",
        signature = "(repeat a & rest)",
        completion = CompletionInfo(
            tailText = "Returns a vector of length n where every element is x",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of length n where every element is x.<br /><br />
With one argument returns an infinite lazy sequence of x.
""",
            example = "(repeat 3 :a) ; =&gt; [:a :a :a]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1027",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "repeatedly",
        signature = "(repeatedly a & rest)",
        completion = CompletionInfo(
            tailText = "Returns a vector of length n with values produced by repeatedly calling f",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of length n with values produced by repeatedly calling f.<br /><br />
With one argument returns an infinite lazy sequence of calls to f.
""",
            example = "(repeatedly 3 rand) ; =&gt; [0.234 0.892 0.456] (random values)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1039",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reverse",
        signature = "(reverse coll)",
        completion = CompletionInfo(
            tailText = "Reverses the order of the elements in the given sequence",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Reverses the order of the elements in the given sequence.",
            example = "(reverse [1 2 3 4]) ; =&gt; [4 3 2 1]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L706",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reversible?",
        signature = "(reversible? coll)",
        completion = CompletionInfo(
            tailText = "Returns true if coll can be reverse-iterated in constant time",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>coll</code> can be reverse-iterated in constant time. Currently this is true for vectors, sorted-maps, and sorted-sets.
""",
            example = "(reversible? [1 2 3]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L717",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rseq",
        signature = "(rseq rev)",
        completion = CompletionInfo(
            tailText = "Returns, in constant time, a sequence of the items in rev in reverse order",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns, in constant time, a sequence of the items in <code>rev</code> in reverse order. <code>rev</code> must be reversible (a vector, sorted-map, or sorted-set); otherwise an exception is thrown. For sorted-maps, returns reversed <code>[key value]</code> pairs. Returns nil if <code>rev</code> is empty.
""",
            example = "(rseq [1 2 3]) ; =&gt; (3 2 1)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L725",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rsubseq",
        signature = "(rsubseq sc test bound-key)\n(rsubseq sc start-test start-key end-test end-key)",
        completion = CompletionInfo(
            tailText = "Like subseq, but returns the matching entries in descending order",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>subseq</code>, but returns the matching entries in descending order. With one boundary, use <code><</code> / <code><=</code> to start from the top end and <code>></code> / <code>>=</code> to stop; with two boundaries, returns the entries between them in reverse, e.g. <code>(rsubseq sm >= 2 < 5)</code>.
""",
            example = "(rsubseq (sorted-map 1 :a 2 :b 3 :c) &lt;= 2) ; =&gt; ([2 :b] [1 :a])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L823",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "select-keys",
        signature = "(select-keys m ks)",
        completion = CompletionInfo(
            tailText = "Returns a new map including key value pairs from m selected with keys ks",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new map including key value pairs from <code>m</code> selected with keys <code>ks</code>.
""",
            example = "(select-keys {:a 1 :b 2 :c 3} [:a :c]) ; =&gt; {:a 1, :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1310",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sequence",
        signature = "(sequence xform coll)",
        completion = CompletionInfo(
            tailText = "Applies transducer xform to coll, returning a vector of results",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Applies transducer <code>xform</code> to <code>coll</code>, returning a vector of results.
""",
            example = "(sequence (comp (filter even?) (map inc)) [1 2 3 4 5]) ; =&gt; [3 5]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1395",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set",
        signature = "(set coll)",
        completion = CompletionInfo(
            tailText = "Coerces a collection to a set",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces a collection to a set. Returns a set containing the distinct elements of <code>coll</code>. For creating sets from arguments, use <code>hash-set</code>.
""",
            example = "(set [1 2 3 2 1]) ; =&gt; #{1 2 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L289",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "shuffle",
        signature = "(shuffle coll)",
        completion = CompletionInfo(
            tailText = "Returns a random permutation of coll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a random permutation of coll.",
            example = "(shuffle [1 2 3 4 5]) ; =&gt; [2 3 5 1 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1019",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "slice",
        signature = "(slice coll & [offset & [length]])",
        completion = CompletionInfo(
            tailText = "Extracts a slice of coll starting at offset with optional length",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extracts a slice of <code>coll</code> starting at <code>offset</code> with optional <code>length</code>.
""",
            example = "(slice [1 2 3 4 5] 1 3) ; =&gt; [2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L247",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sort",
        signature = "(sort coll)\n(sort a b)",
        completion = CompletionInfo(
            tailText = "Returns a sorted vector",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a sorted vector. If no comparator is supplied compare is used.",
            example = "(sort [3 1 4 1 5 9 2 6]) ; =&gt; [1 1 2 3 4 5 6 9]\n(sort (fn [a b] (compare b a)) [1 2 3]) ; =&gt; [3 2 1]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L995",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sort-by",
        signature = "(sort-by keyfn coll)\n(sort-by keyfn comp-or-coll coll-or-comp)",
        completion = CompletionInfo(
            tailText = "Returns a sorted vector where the sort order is determined by comparing (keyfn item)",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a sorted vector where the sort order is determined by comparing <code>(keyfn item)</code>.<br /><br />
If no comparator is supplied compare is used.
""",
            example = "(sort-by count [\"aaa\" \"c\" \"bb\"]) ; =&gt; [\"c\" \"bb\" \"aaa\"]\n(sort-by count compare [\"aaa\" \"c\" \"bb\"]) ; =&gt; [\"c\" \"bb\" \"aaa\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1006",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "split-at",
        signature = "(split-at n coll)",
        completion = CompletionInfo(
            tailText = "Returns a vector of [(take n coll) (drop n coll)]",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of <code>[(take n coll) (drop n coll)]</code>.
""",
            example = "(split-at 2 [1 2 3 4 5]) ; =&gt; [@[1 2] @[3 4 5]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1345",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "split-with",
        signature = "(split-with f coll)",
        completion = CompletionInfo(
            tailText = "Returns a vector of [(take-while pred coll) (drop-while pred coll)]",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of <code>[(take-while pred coll) (drop-while pred coll)]</code>.
""",
            example = "(split-with #(&lt; % 4) [1 2 3 4 5 6]) ; =&gt; [@[1 2 3] @[4 5 6]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1359",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "splitv-at",
        signature = "(splitv-at n coll)",
        completion = CompletionInfo(
            tailText = "Returns a vector [(vec (take n coll)) (vec (drop n coll))]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector <code>[(vec (take n coll)) (vec (drop n coll))]</code>. Like <code>split-at</code>, but eagerly returns vectors rather than lazy sequences.
""",
            example = "(splitv-at 2 [1 2 3 4 5]) ; =&gt; [[1 2] [3 4 5]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1352",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "subseq",
        signature = "(subseq sc test bound-key)\n(subseq sc start-test start-key end-test end-key)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of the entries of the sorted collection sc (map entries for a sorted map,...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of the entries of the sorted collection <code>sc</code> (map entries for a sorted map, elements for a sorted set) whose keys satisfy the boundary test, in ascending order. With one boundary, use <code>></code> / <code>>=</code> to start after or at <code>bound-key</code> and <code><</code> / <code><=</code> to stop before or at it. With two boundaries, returns the entries between them, e.g. <code>(subseq sm >= 2 < 5)</code>. Boundaries respect the collection's own comparator (<code>sorted-map-by</code> / <code>sorted-set-by</code>).
""",
            example = "(subseq (sorted-map 1 :a 2 :b 3 :c) &gt;= 2) ; =&gt; ([2 :b] [3 :c])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L807",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "subvec",
        signature = "(subvec v start)\n(subvec v start end)",
        completion = CompletionInfo(
            tailText = "Returns a persistent vector of the items in v from start (inclusive) to end (exclusive)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a persistent vector of the items in <code>v</code> from <code>start</code> (inclusive) to <code>end</code> (exclusive). <code>end</code> defaults to <code>(count v)</code>. Non-integer indices truncate toward zero and <code>NaN</code> counts as <code>0</code>, matching Clojure. The result shares structure with <code>v</code>. Throws an OutOfBoundsException when <code>start</code> or <code>end</code> is out of range or <code>start</code> is greater than <code>end</code>.
""",
            example = "(subvec [1 2 3 4 5] 1 3) ; =&gt; [2 3]\n(subvec [1 2 3 4 5] 2) ; =&gt; [3 4 5]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L273",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "take",
        signature = "(take n & args)",
        completion = CompletionInfo(
            tailText = "Takes the first n elements of coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes the first <code>n</code> elements of <code>coll</code>. When called with n only, returns a transducer.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L468",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "take-last",
        signature = "(take-last n coll)",
        completion = CompletionInfo(
            tailText = "Takes the last n elements of coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes the last <code>n</code> elements of <code>coll</code>.
""",
            example = "(take-last 3 [1 2 3 4 5]) ; =&gt; [3 4 5]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L500",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "take-nth",
        signature = "(take-nth n & args)",
        completion = CompletionInfo(
            tailText = "Returns every nth item in coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns every nth item in <code>coll</code>. Returns a lazy sequence. When called with n only, returns a transducer.
""",
            example = "(take-nth 2 [0 1 2 3 4 5 6 7 8]) ; =&gt; @[0 2 4 6 8]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L528",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "take-while",
        signature = "(take-while pred & args)",
        completion = CompletionInfo(
            tailText = "Takes all elements at the front of coll where (pred x) is true",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes all elements at the front of <code>coll</code> where <code>(pred x)</code> is true. Returns a lazy sequence. When called with pred only, returns a transducer.
""",
            example = "(take-while #(&lt; % 5) [1 2 3 4 5 6 3 2 1]) ; =&gt; @[1 2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L512",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "unset-in",
        signature = "(unset-in ds ks)",
        completion = CompletionInfo(
            tailText = "Removes a value from a nested data structure",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes a value from a nested data structure.",
            example = null,
            deprecation = DeprecationInfo(version = "0.25.0", replacement = "dissoc-in"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L389",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "update",
        signature = "(update ds k f & args)",
        completion = CompletionInfo(
            tailText = "Updates a value in a datastructure by applying f to the current value",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Updates a value in a datastructure by applying <code>f</code> to the current value.
""",
            example = "(update {:count 5} :count inc) ; =&gt; {:count 6}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L360",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "update-in",
        signature = "(update-in ds [k & ks] f & args)",
        completion = CompletionInfo(
            tailText = "Updates a value in a nested data structure by applying f to the value at path",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Updates a value in a nested data structure by applying <code>f</code> to the value at path.
""",
            example = "(update-in {:a {:b 5}} [:a :b] inc) ; =&gt; {:a {:b 6}}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L367",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "val",
        signature = "(val entry)",
        completion = CompletionInfo(
            tailText = "Returns the value of a map entry",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the value of a map entry. Accepts a typed<br />
  <code>Phel\Lang\Collections\Map\MapEntry</code> value or a 2-element vector<br />
  <code>[key value]</code>.
""",
            example = "(val (first (pairs {:a 1}))) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L781",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vals",
        signature = "(vals coll)",
        completion = CompletionInfo(
            tailText = "Returns a sequence of all values in a map, or nil when the map is nil or empty",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a sequence of all values in a map, or <code>nil</code> when the map is <code>nil</code> or empty.
""",
            example = "(vals {:a 1 :b 2}) ; =&gt; [1 2]\n(vals nil) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L756",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "values",
        signature = "(values coll)",
        completion = CompletionInfo(
            tailText = "Returns a sequence of all values in a map",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a sequence of all values in a map.",
            example = "(values {:a 1 :b 2}) ; =&gt; [1 2]",
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "vals"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L839",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vec",
        signature = "(vec coll)",
        completion = CompletionInfo(
            tailText = "Coerces a collection to a vector",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces a collection to a vector. For hash-maps and structs, entries are returned as 2-element <code>[key value]</code> vectors, matching Clojure.
""",
            example = "(vec {:a 1 :b 2}) ; =&gt; [[:a 1] [:b 2]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L300",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "zipcoll",
        signature = "(zipcoll a b)",
        completion = CompletionInfo(
            tailText = "Creates a map from two sequential data structures",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a map from two sequential data structures. Returns a new map.",
            example = "(zipcoll [:a :b :c] [1 2 3]) ; =&gt; {:a 1, :b 2, :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1287",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "zipmap",
        signature = "(zipmap keys vals)",
        completion = CompletionInfo(
            tailText = "Returns a new map with the keys mapped to the corresponding values",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new map with the keys mapped to the corresponding values.<br /><br />
Stops when the shorter of <code>keys</code> or <code>vals</code> is exhausted. Works safely with infinite lazy sequences.
""",
            example = "(zipmap [:a :b :c] [1 2 3]) ; =&gt; {:a 1, :b 2, :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/seq-fns.phel#L1273",
                docs = "",
            ),
        ),
    )
)
