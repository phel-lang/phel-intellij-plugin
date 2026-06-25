package org.phellang.completion.data.test

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerTestGenFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/boolean",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of booleans",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generator of booleans.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L62",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/char",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of printable ASCII characters (space through ~)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of printable ASCII characters (space through <code>~</code>).
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L111",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/char-alpha",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of ASCII letters",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generator of ASCII letters.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L115",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/char-alphanumeric",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of ASCII letters and digits",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generator of ASCII letters and digits.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L119",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/choose",
        signature = "(choose lo hi)",
        completion = CompletionInfo(
            tailText = "Generator of integers in the closed interval [lo hi]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of integers in the closed interval <code>[lo hi]</code>.
""",
            example = "((choose 1 6) 100) ; =&gt; 1..6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L84",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/default-num-tests",
        signature = "",
        completion = CompletionInfo(
            tailText = "Number of trials quick-check runs by default",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Number of trials <code>quick-check</code> runs by default.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L34",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/default-size",
        signature = "",
        completion = CompletionInfo(
            tailText = "Magnitude used when no :size option is supplied",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Magnitude used when no <code>:size</code> option is supplied.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/defspec",
        signature = "(defspec name options args-gen property)",
        completion = CompletionInfo(
            tailText = "Defines a property test",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a property test. The generated <code>deftest</code> runs <code>quick-check</code><br />
  and asserts the result is <code>:pass</code>. <code>options</code> is a hash-map accepting<br />
  <code>:num-tests</code>, <code>:size</code>, <code>:seed</code> and <code>:shrink?</code>.<br /><br />
Attach <code>^:no-shrink</code> metadata to the name to skip shrinking on<br />
  failure. The <code>test-name</code> symbol's metadata is also forwarded to the<br />
  underlying <code>deftest</code>, so tag-based selectors apply as usual.<br /><br />
On failure a <code>:defspec-failed</code> event is sent to the reporter set<br />
  before the surrounding <code>is</code> assertion records a <code>:failed</code> event.<br />
  Reporters that want rich shrink information can subscribe to it; the<br />
  built-in reporters keep rendering their usual failure summary.<br /><br />
Shape: <code>(defspec name options args-gen property)</code>.
""",
            example = "(defspec addition-commutes {:num-tests 200}\n              (tuple int int)\n              (fn [a b] (= (+ a b) (+ b a))))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L429",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/elements",
        signature = "(elements coll)",
        completion = CompletionInfo(
            tailText = "Generator that picks a random element from coll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator that picks a random element from <code>coll</code>.
""",
            example = "((elements [:a :b :c]) 100) ; =&gt; :a, :b or :c",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L212",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/float",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of floats in [0, 1)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of floats in <code>[0, 1)</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L80",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/fmap",
        signature = "(fmap f g)",
        completion = CompletionInfo(
            tailText = "Returns a generator that applies f to values produced by g",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a generator that applies <code>f</code> to values produced by <code>g</code>.
""",
            example = "((fmap inc (return 1)) 100) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L180",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/frequency",
        signature = "(frequency pairs)",
        completion = CompletionInfo(
            tailText = "Generator that picks one of the [weight gen] pairs with probability proportional to weight",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator that picks one of the <code>[weight gen]</code> pairs with probability proportional to weight.
""",
            example = "((frequency [[9 (return :a)] [1 (return :b)]]) 100)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L232",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/generate",
        signature = "(generate g)\n(generate g {:size size, :seed seed})",
        completion = CompletionInfo(
            tailText = "Runs g once and returns a single value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs <code>g</code> once and returns a single value. Accepts <code>:size</code> and <code>:seed</code>.
""",
            example = "(generate int) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L309",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/int",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of integers in [-size, size]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of integers in <code>[-size, size]</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L70",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/keyword",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of keywords with alphabetic names, length in [1, max(1, size)]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of keywords with alphabetic names, length in <code>[1, max(1, size)]</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L162",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/large-int",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of arbitrary PHP-range integers",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generator of arbitrary PHP-range integers.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L76",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/list-of",
        signature = "(list-of g)",
        completion = CompletionInfo(
            tailText = "Generator of lists whose elements come from g",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of lists whose elements come from <code>g</code>. Length in <code>[0, size]</code>.
""",
            example = "((list-of int) 3) ; =&gt; (-1 2 0)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L278",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/map-of",
        signature = "(map-of kg vg)",
        completion = CompletionInfo(
            tailText = "Generator of hash-maps with keys from kg and values from vg",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of hash-maps with keys from <code>kg</code> and values from <code>vg</code>. Number of entries is in <code>[0, size]</code>.
""",
            example = "((map-of keyword int) 2) ; =&gt; {:a 1 :b -3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L285",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/nat",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of non-negative integers in [0, size]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of non-negative integers in <code>[0, size]</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L66",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/one-of",
        signature = "(one-of gens)",
        completion = CompletionInfo(
            tailText = "Generator that selects uniformly from gens and runs the chosen one",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator that selects uniformly from <code>gens</code> and runs the chosen one.
""",
            example = "((one-of [int boolean]) 100)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L222",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/quick-check",
        signature = "(quick-check num-tests args-gen property)\n(quick-check num-tests args-gen property {:size size, :seed seed, :shrink? shrink?})",
        completion = CompletionInfo(
            tailText = "Runs property for num-tests trials, drawing each trial's arguments from args-gen (a generator ret...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs <code>property</code> for <code>num-tests</code> trials, drawing each trial's arguments from<br />
  <code>args-gen</code> (a generator returning a vector of arguments). Returns a hash-map<br />
  with <code>:result</code> (<code>:pass</code>, <code>:failed</code>, or <code>:error</code>), <code>:num-tests</code>, and the<br />
  effective <code>:seed</code> for replay. On failure/error the failing <code>:args</code> (and<br />
  <code>:exception</code>) are also included.<br /><br />
Options:<br />
  - <code>:size</code> (default 100): magnitude passed to the generator.<br />
  - <code>:seed</code> (default: fresh per-run): PRNG seed.<br />
  - <code>:shrink?</code> (default: true): when true, a failing trial is shrunk to<br />
    a minimal counterexample; the resulting map carries <code>:shrunk-args</code>,<br />
    <code>:original-args</code> and <code>:shrink-steps</code>.
""",
            example = "(quick-check 50 (tuple int int) (fn [a b] (= (+ a b) (+ b a))))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L365",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/resize",
        signature = "(resize n g)",
        completion = CompletionInfo(
            tailText = "Returns a generator equivalent to g but with size forced to n",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a generator equivalent to <code>g</code> but with <code>size</code> forced to <code>n</code>.
""",
            example = "((resize 5 nat) 1000) ; =&gt; value in [0, 5]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L206",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/return",
        signature = "(return x)",
        completion = CompletionInfo(
            tailText = "Generator that always yields x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator that always yields <code>x</code>.
""",
            example = "((return 42) 100) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L174",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/sample",
        signature = "(sample g)\n(sample g num-samples)\n(sample g num-samples {:size size, :seed seed})",
        completion = CompletionInfo(
            tailText = "Runs g num-samples times (default 10) and returns a vector of values",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs <code>g</code> <code>num-samples</code> times (default 10) and returns a vector of values. Accepts <code>:size</code> and <code>:seed</code>.
""",
            example = "(sample int 5) ; =&gt; [3 -7 0 1 -2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L317",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/set-of",
        signature = "(set-of g)",
        completion = CompletionInfo(
            tailText = "Generator of hash-sets with elements from g",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of hash-sets with elements from <code>g</code>. Cardinality in <code>[0, size]</code>.
""",
            example = "((set-of nat) 3) ; =&gt; (set 1 2 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L295",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/sized",
        signature = "(sized f)",
        completion = CompletionInfo(
            tailText = "Builds a generator from a function f of size",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Builds a generator from a function <code>f</code> of <code>size</code>. The returned generator forwards its <code>size</code> into <code>f</code>, which must yield another generator that is then invoked with the same size.
""",
            example = "(sized (fn [n] (return n)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L200",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/spec-failure-message",
        signature = "(spec-failure-message name-str res)",
        completion = CompletionInfo(
            tailText = "Renders the human-readable failure message for a defspec outcome map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Renders the human-readable failure message for a <code>defspec</code> outcome map. Includes the shrink summary when the trial's arguments were shrunk to a smaller counterexample.
""",
            example = "(spec-failure-message \"my-spec\" {:result :failed ...})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L389",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/string",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of printable ASCII strings, length in [0, size]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of printable ASCII strings, length in <code>[0, size]</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L134",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/string-alpha",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of ASCII alphabetic strings, length in [0, size]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of ASCII alphabetic strings, length in <code>[0, size]</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L138",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/string-alphanumeric",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of ASCII alphanumeric strings, length in [0, size]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of ASCII alphanumeric strings, length in <code>[0, size]</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L142",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/such-that",
        signature = "(such-that pred g)\n(such-that pred g max-tries)",
        completion = CompletionInfo(
            tailText = "Generator yielding only values from g that satisfy pred",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator yielding only values from <code>g</code> that satisfy <code>pred</code>. Retries up to<br />
  <code>max-tries</code> (default 100) before throwing.
""",
            example = "((such-that even? nat) 100)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L186",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/symbol",
        signature = "",
        completion = CompletionInfo(
            tailText = "Generator of symbols with alphabetic names, length in [1, max(1, size)]",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of symbols with alphabetic names, length in <code>[1, max(1, size)]</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L166",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/tuple",
        signature = "(tuple & gens)",
        completion = CompletionInfo(
            tailText = "Generator of a fixed-length vector produced by running each of the given generators in order",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of a fixed-length vector produced by running each of the given generators in order. Accepts either a vector of generators or variadic generator arguments.
""",
            example = "((tuple int boolean) 10) ; =&gt; [3 true]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L251",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.gen",
        name = "test.gen/vector-of",
        signature = "(vector-of g)\n(vector-of g n)\n(vector-of g lo hi)",
        completion = CompletionInfo(
            tailText = "Generator of vectors",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generator of vectors. Length is <code>[0, size]</code> with one arg, exactly <code>n</code> with<br />
  <code>(vector-of g n)</code>, or <code>[lo, hi]</code> with <code>(vector-of g lo hi)</code>.
""",
            example = "((vector-of int) 3) ; =&gt; [-1 2 0]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.46.0/src/phel/test/gen.phel#L264",
                docs = "",
            ),
        ),
    )
)
