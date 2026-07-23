package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCorePredicatesFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "any?",
        signature = "(any? _)",
        completion = CompletionInfo(
            tailText = "Returns true given any argument, including nil and false",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true given any argument, including <code>nil</code> and <code>false</code>. Mirrors Clojure's <code>clojure.core/any?</code> — useful as a default predicate in spec / validation contexts where every value should be accepted.
""",
            example = "(any? nil) ; =&gt; true\n(any? 0) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L384",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "associative?",
        signature = "(associative? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is an associative data structure, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is an associative data structure, false otherwise.<br /><br />
Associative data structures include vectors, hash maps, structs, and PHP arrays<br />
  (both indexed and associative), matching Clojure's <code>Associative</code> protocol.
""",
            example = "(associative? [1 2 3]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L530",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "boolean",
        signature = "(boolean x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a boolean",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a boolean. Returns <code>false</code> if <code>x</code> is <code>nil</code> or <code>false</code>,<br />
   <code>true</code> otherwise.
""",
            example = "(boolean nil) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L377",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "boolean?",
        signature = "(boolean? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a boolean, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a boolean, false otherwise.
""",
            example = "(boolean? true) ; =&gt; true\n(boolean? nil) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L370",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "char?",
        signature = "(char? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a single-character string, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a single-character string, false otherwise.<br />
   Phel has no dedicated character type — character literals such as<br />
   <code>\A</code> are already single-character strings — so <code>char?</code> is true for<br />
   any string of length 1 (UTF-8 counted). Matches ClojureScript's<br />
   <code>char?</code> for <code>.cljc</code> interop; Clojure/JVM's <code>char?</code> tests for the<br />
   distinct <code>Character</code> type, which does not exist here.
""",
            example = "(char? \\A) ; =&gt; true\n(char? \"a\") ; =&gt; true\n(char? \"ab\") ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L221",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "class",
        signature = "(class x)",
        completion = CompletionInfo(
            tailText = "Returns a Phel\\Lang\\PhpClass for x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a <code>Phel\Lang\PhpClass</code> for <code>x</code>. With an object argument returns the class of the object. With a string argument resolves the named class or interface FQN. Throws <code>InvalidArgumentException</code> for any other input.
""",
            example = "(class (php/new \\stdClass)) ; =&gt; stdClass",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L111",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "class-name",
        signature = "(class-name c)",
        completion = CompletionInfo(
            tailText = "Returns the FQN string of c (a Phel\\Lang\\PhpClass)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the FQN string of <code>c</code> (a <code>Phel\Lang\PhpClass</code>). Leading backslashes are not preserved (e.g. <code>\stdClass</code> becomes <code>stdClass</code>).
""",
            example = "(class-name (class \"stdClass\")) ; =&gt; \"stdClass\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L122",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "class?",
        signature = "(class? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Phel\\Lang\\PhpClass value",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a <code>Phel\Lang\PhpClass</code> value.
""",
            example = "(class? (class (php/new \\stdClass))) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L104",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "coll?",
        signature = "(coll? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a persistent collection — vector, list, hash-map (including sorted-map), str...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a persistent collection — vector, list, hash-map<br />
   (including sorted-map), struct, set (including sorted-set), or lazy-seq —<br />
   and false otherwise. Strings, numbers, <code>nil</code>, booleans, keywords, symbols,<br />
   and plain PHP arrays are not considered collections, matching Clojure's<br />
   <code>IPersistentCollection</code> membership.
""",
            example = "(coll? [1 2 3]) ; =&gt; true\n(coll? \"abc\") ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L551",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "counted?",
        signature = "(counted? coll)",
        completion = CompletionInfo(
            tailText = "Returns true if coll can report its length in constant time — persistent vectors, lists, hash-map...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>coll</code> can report its length in constant time — persistent<br />
   vectors, lists, hash-maps (including sorted-map), structs, and sets<br />
   (including sorted-set). Returns false for lazy sequences (counting them<br />
   requires realizing the whole sequence), strings, numbers, <code>nil</code>, and every<br />
   other non-counted type. Matches Clojure's <code>counted?</code> semantics, which<br />
   mirror the <code>clojure.lang.Counted</code> marker interface.
""",
            example = "(counted? [1 2 3]) ; =&gt; true\n(counted? (range)) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L576",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "double?",
        signature = "(double? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a floating-point number, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a floating-point number, false otherwise. Alias for <code>float?</code>, matching Clojure's <code>double?</code> naming. Since Phel uses PHP floats (IEEE 754 doubles) there is no separate single-precision float type.
""",
            example = "(double? 1.0) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L200",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "empty",
        signature = "(empty coll)",
        completion = CompletionInfo(
            tailText = "Returns an empty collection of the same category as coll, preserving the original metadata, or ni...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns an empty collection of the same category as <code>coll</code>, preserving the original metadata, or nil if <code>coll</code> has no empty equivalent.
""",
            example = "(empty [1 2 3]) ; =&gt; []\n(empty {:a 1}) ; =&gt; {}\n(empty (range 10)) ; =&gt; ()",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L461",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "empty?",
        signature = "(empty? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x would be 0, \"\" or empty collection, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if x would be 0, "" or empty collection, false otherwise. Safe on infinite/lazy sequences: checks the first element instead of counting.
""",
            example = "(empty? []) ; =&gt; true\n(empty? [1]) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L440",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "float?",
        signature = "(float? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is float point number, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is float point number, false otherwise.
""",
            example = "(float? 1.0) ; =&gt; true\n(float? 1) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L131",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "fn?",
        signature = "(fn? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a function, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a function, false otherwise.
""",
            example = "(fn? inc) ; =&gt; true\n(fn? 42) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L295",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "function?",
        signature = "(function? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a function, false otherwise",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a function, false otherwise.
""",
            example = null,
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "fn?"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L310",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "hash-map?",
        signature = "(hash-map? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a hash map, false otherwise",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a hash map, false otherwise.
""",
            example = null,
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "map?"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L330",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ident?",
        signature = "(ident? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a symbol or keyword",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a symbol or keyword.
""",
            example = "(ident? 'x) ; =&gt; true\n(ident? :a) ; =&gt; true\n(ident? 42) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L248",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ifn?",
        signature = "(ifn? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x can be invoked as a function",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> can be invoked as a function. This includes functions, keywords, maps, vectors, sets, and lists.
""",
            example = "(ifn? inc) ; =&gt; true\n(ifn? :a) ; =&gt; true\n(ifn? {}) ; =&gt; true\n(ifn? 42) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L302",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "indexed?",
        signature = "(indexed? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is an indexed sequence, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is an indexed sequence, false otherwise.<br /><br />
Indexed sequences include lists, vectors, and indexed PHP arrays.
""",
            example = "(indexed? [1 2 3]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L520",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "instance?",
        signature = "(instance? c x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is an instance of class c, false otherwise",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is an instance of class <code>c</code>, false otherwise.<br />
  Mirrors Clojure's <code>clojure.core/instance?</code> argument order (class first,<br />
  value second). <code>c</code> should be a literal class reference such as<br />
  <code>DateTime</code> or a <code>:use</code>d short name; for runtime class names use<br />
  <code>(php/is_a x class-name)</code>.
""",
            example = "(instance? DateTime (php/new DateTime)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L391",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "int?",
        signature = "(int? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a fixed-precision PHP integer",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a fixed-precision PHP integer. <code>BigInt</code> values return false; use <code>integer?</code> to accept either.
""",
            example = "(int? 1) ; =&gt; true\n(int? 1.0) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L147",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "integer?",
        signature = "(integer? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a mathematical integer: a fixed-precision PHP int or a BigInt",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a mathematical integer: a fixed-precision PHP<br />
   <code>int</code> or a <code>BigInt</code>.
""",
            example = "(integer? 1) ; =&gt; true\n(integer? 1.0) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L138",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keyword?",
        signature = "(keyword? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a keyword, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a keyword, false otherwise.
""",
            example = "(keyword? :a) ; =&gt; true\n(keyword? 'a) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L234",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "lazy-seq?",
        signature = "(lazy-seq? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a lazy sequence, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a lazy sequence, false otherwise.<br /><br />
Unlike <code>seq?</code>, this predicate is true only for lazy sequences, not for realized lists. Use <code>seq?</code> if you want to accept either.
""",
            example = "(lazy-seq? (map inc [1 2 3])) ; =&gt; true\n(lazy-seq? '(1 2 3))         ; =&gt; false\n(lazy-seq? [1 2 3])          ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L431",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "list?",
        signature = "(list? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a list, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a list, false otherwise. Returns false for the seq view of non-list collections produced by <code>seq</code>.
""",
            example = "(list? '(1 2)) ; =&gt; true\n(list? [1 2]) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L355",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "map-entry?",
        signature = "(map-entry? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a map entry",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a map entry. Accepts both the typed<br />
  <code>Phel\Lang\Collections\Map\MapEntry</code> value and any 2-element vector<br />
  (Phel maps still yield vectors when iterated).
""",
            example = "(map-entry? [:a 1]) ; =&gt; true\n(map-entry? (map-entry :a 1)) ; =&gt; true\n(map-entry? [1 2 3]) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L345",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "map?",
        signature = "(map? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a hash map, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a hash map, false otherwise.
""",
            example = "(map? {:a 1}) ; =&gt; true\n(map? [1 2]) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L322",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nat-int?",
        signature = "(nat-int? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a non-negative integer (zero or positive)",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a non-negative integer (zero or positive). Accepts both fixed-precision PHP <code>int</code> and arbitrary-precision <code>BigInt</code> values.
""",
            example = "(nat-int? 0) ; =&gt; true\n(nat-int? 1) ; =&gt; true\n(nat-int? (bigint 5)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L175",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "neg-int?",
        signature = "(neg-int? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a negative integer",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a negative integer. Accepts both fixed-precision PHP <code>int</code> and arbitrary-precision <code>BigInt</code> values.
""",
            example = "(neg-int? -1) ; =&gt; true\n(neg-int? 0) ; =&gt; false\n(neg-int? (bigint -5)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L161",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "not-empty",
        signature = "(not-empty coll)",
        completion = CompletionInfo(
            tailText = "Returns coll if it contains elements, otherwise nil",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>coll</code> if it contains elements, otherwise nil.
""",
            example = "(not-empty [1 2]) ; =&gt; [1 2]\n(not-empty []) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L452",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "number?",
        signature = "(number? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a number: int, float, Ratio, BigInt, or BigDecimal",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a number: int, float, <code>Ratio</code>, <code>BigInt</code>, or <code>BigDecimal</code>.
""",
            example = "(number? 1) ; =&gt; true\n(number? \"a\") ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L182",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "php-array?",
        signature = "(php-array? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a PHP Array, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a PHP Array, false otherwise.
""",
            example = "(php-array? (php/array 1 2)) ; =&gt; true\n(php-array? [1 2]) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L402",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "php-object?",
        signature = "(php-object? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a PHP object, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a PHP object, false otherwise.
""",
            example = "(php-object? (php/new \\stdClass)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L414",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "php-resource?",
        signature = "(php-resource? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a PHP resource, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a PHP resource, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L409",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "pos-int?",
        signature = "(pos-int? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a positive integer (greater than zero)",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a positive integer (greater than zero). Accepts both fixed-precision PHP <code>int</code> and arbitrary-precision <code>BigInt</code> values.
""",
            example = "(pos-int? 1) ; =&gt; true\n(pos-int? 0) ; =&gt; false\n(pos-int? (bigint 5)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L168",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "queue?",
        signature = "(queue? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Phel\\Lang\\Collections\\Queue\\PersistentQueue value",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a <code>Phel\Lang\Collections\Queue\PersistentQueue</code> value.
""",
            example = "(queue? (queue 1 2 3)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L363",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ratio?",
        signature = "(ratio? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Ratio value",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a <code>Ratio</code> value. Integer-valued rationals auto-collapse to <code>int</code>/<code>BigInt</code> at construction time, so <code>(ratio? 2/2)</code> is <code>false</code>.
""",
            example = "(ratio? 1/2) ; =&gt; true\n(ratio? 0.5) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L193",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rational?",
        signature = "(rational? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a rational number: an integer (int or BigInt), a Ratio, or a BigDecimal",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a rational number: an integer (<code>int</code> or <code>BigInt</code>), a <code>Ratio</code>, or a <code>BigDecimal</code>. Floating-point numbers (including <code>##Inf</code>, <code>##-Inf</code>, and <code>##NaN</code>) are not rational.
""",
            example = "(rational? 1/2) ; =&gt; true\n(rational? 1.0) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L207",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "seq",
        signature = "(seq coll)",
        completion = CompletionInfo(
            tailText = "Returns a seq on the collection",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a seq on the collection. Strings are converted to a vector of characters. Other non-empty collections (vectors, sets, sorted-maps, sorted-sets, PHP arrays) are converted to a non-list seq. Returns nil if <code>coll</code> is empty or nil.<br /><br />
This function is useful for explicitly converting strings to sequences of characters, enabling sequence operations like map, filter, and frequencies.
""",
            example = "(seq \"hello\") ; =&gt; [\"h\" \"e\" \"l\" \"l\" \"o\"]\n(seq [1 2 3]) ; =&gt; (1 2 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L487",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "seq?",
        signature = "(seq? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a seq (a list, a lazy sequence, or a realized Cons cell returned by (next so...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a seq (a list, a lazy sequence, or a realized<br />
  <code>Cons</code> cell returned by <code>(next some-lazy-seq)</code>).
""",
            example = "(seq? '(1 2)) ; =&gt; true\n(seq? [1 2]) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L421",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "seqable?",
        signature = "(seqable? x)",
        completion = CompletionInfo(
            tailText = "Returns true if (seq x) is supported: collections (vectors, lists, maps, sets, structs), lazy seq...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>(seq x)</code> is supported: collections (vectors, lists, maps, sets, structs), lazy sequences, strings, PHP arrays, and nil. Returns false for numbers, booleans, keywords, symbols, and other types.
""",
            example = "(seqable? [1 2]) ; =&gt; true\n(seqable? 42) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L566",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sequential?",
        signature = "(sequential? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a sequential collection (vector, list, or lazy sequence), false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a sequential collection (vector, list, or lazy sequence), false otherwise. Sequential collections maintain insertion order and support indexed or linear access. Maps, sets, and structs are not sequential, matching Clojure's <code>Sequential</code> marker.
""",
            example = "(sequential? [1 2 3]) ; =&gt; true\n(sequential? {:a 1}) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L542",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set?",
        signature = "(set? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a set, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a set, false otherwise.
""",
            example = "(set? (hash-set 1 2)) ; =&gt; true\n(set? [1 2]) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L591",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "simple-ident?",
        signature = "(simple-ident? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a symbol or keyword without a namespace",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a symbol or keyword without a namespace.
""",
            example = "(simple-ident? 'a) ; =&gt; true\n(simple-ident? 'foo/bar) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L269",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "simple-keyword?",
        signature = "(simple-keyword? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a keyword without a namespace",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a keyword without a namespace.
""",
            example = "(simple-keyword? :a) ; =&gt; true\n(simple-keyword? :foo/bar) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L262",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "simple-symbol?",
        signature = "(simple-symbol? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a symbol without a namespace",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a symbol without a namespace.
""",
            example = "(simple-symbol? 'a) ; =&gt; true\n(simple-symbol? 'foo/bar) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L255",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted?",
        signature = "(sorted? coll)",
        completion = CompletionInfo(
            tailText = "Returns true if coll is a sorted collection (sorted-map or sorted-set), false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>coll</code> is a sorted collection (sorted-map or sorted-set), false otherwise.
""",
            example = "(sorted? (sorted-set 1 2 3)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L598",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "special-symbol?",
        signature = "(special-symbol? s)",
        completion = CompletionInfo(
            tailText = "Returns true if s names a special form",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>s</code> names a special form.
""",
            example = "(special-symbol? 'def) ; =&gt; true\n(special-symbol? 'map) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L288",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "string?",
        signature = "(string? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a string, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a string, false otherwise.
""",
            example = "(string? \"a\") ; =&gt; true\n(string? :a) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L214",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "struct?",
        signature = "(struct? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a struct, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a struct, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L317",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "symbol?",
        signature = "(symbol? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a symbol, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a symbol, false otherwise.
""",
            example = "(symbol? 'a) ; =&gt; true\n(symbol? :a) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L241",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "type",
        signature = "(type x)",
        completion = CompletionInfo(
            tailText = "Returns the type of x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the type of <code>x</code>. The following types can be returned:<br /><br />
<em> <code>:vector</code><br />
</em> <code>:list</code><br />
<em> <code>:struct</code><br />
</em> <code>:hash-map</code><br />
<em> <code>:map-entry</code><br />
</em> <code>:set</code><br />
<em> <code>:keyword</code><br />
</em> <code>:symbol</code><br />
<em> <code>:atom</code><br />
</em> <code>:var</code><br />
<em> <code>:int</code><br />
</em> <code>:float</code><br />
<em> <code>:ratio</code><br />
</em> <code>:bigint</code><br />
<em> <code>:bigdec</code><br />
</em> <code>:uuid</code><br />
<em> <code>:php/class</code><br />
</em> <code>:queue</code><br />
<em> <code>:string</code><br />
</em> <code>:nil</code><br />
<em> <code>:boolean</code><br />
</em> <code>:function</code><br />
<em> <code>:php/array</code><br />
</em> <code>:php/resource</code><br />
<em> <code>:php/object</code><br />
</em> <code>:unknown</code>
""",
            example = "(type [1 2]) ; =&gt; :vector\n(type :a) ; =&gt; :keyword",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L39",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vector?",
        signature = "(vector? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a vector",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a vector. Map entries returned by iterating a hash map (<code>(first {:a 1})</code>) are vector-shaped two-element values, so this predicate also accepts the typed <code>MapEntry</code>.
""",
            example = "(vector? [1 2]) ; =&gt; true\n(vector? '(1 2)) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/core/predicates.phel#L337",
                docs = "",
            ),
        ),
    )
)
