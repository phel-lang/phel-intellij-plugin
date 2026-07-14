package org.phellang.registry.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreArraysFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "aclone",
        signature = "(aclone arr)",
        completion = CompletionInfo(
            tailText = "Returns a shallow copy of a PHP array",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a shallow copy of a PHP array. The returned array is a distinct value — mutating the copy via <code>php/aset</code> does not affect the original, and vice versa. Matches Clojure's <code>aclone</code> for <code>.cljc</code> interop; raises <code>InvalidArgumentException</code> on non-array inputs since Phel's persistent collections are already immutable and don't need cloning.
""",
            example = "(aclone (object-array 3)) ; =&gt; &lt;PHP-Array [nil, nil, nil]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L89",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "aget",
        signature = "(aget arr & indices)",
        completion = CompletionInfo(
            tailText = "Returns the value at index in a PHP array",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the value at <code>index</code> in a PHP array. With multiple indices, accesses nested arrays: <code>(aget arr i j)</code> is <code>(aget (aget arr i) j)</code>. Matches Clojure's <code>aget</code> for <code>.cljc</code> interop.
""",
            example = "(aget (php/array 10 20 30) 1) ; =&gt; 20",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L189",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "alength",
        signature = "(alength arr)",
        completion = CompletionInfo(
            tailText = "Returns the number of elements in a PHP array",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the number of elements in a PHP array. Matches Clojure's<br />
  <code>alength</code> for <code>.cljc</code> interop; raises <code>InvalidArgumentException</code> on<br />
  non-array inputs (use <code>count</code> for collections).
""",
            example = "(alength (int-array 3)) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L96",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "aset",
        signature = "(aset arr idx & more)",
        completion = CompletionInfo(
            tailText = "Sets the value at index in a PHP array to val",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sets the value at <code>index</code> in a PHP array to <code>val</code>. Returns <code>val</code>.<br />
   With additional indices, navigates nested arrays before setting:<br />
   <code>(aset arr i j val)</code> sets index <code>j</code> in <code>(aget arr i)</code>.<br />
   Matches Clojure's <code>aset</code> for <code>.cljc</code> interop.<br />
   This is a macro because PHP arrays are value types; a function<br />
   wrapper would mutate a copy rather than the original.
""",
            example = "(let [a (php/array 1 2 3)] (aset a 0 42) (aget a 0)) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L200",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "double-array",
        signature = "(double-array size-or-seq)\n(double-array size init-val-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of doubles (same as float-array in PHP)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of doubles (same as float-array in PHP). Accepts the same arities and semantics as <code>float-array</code>.
""",
            example = "(double-array 3) ; =&gt; &lt;PHP-Array [0, 0, 0]&gt;\n(double-array 4 1.5) ; =&gt; &lt;PHP-Array [1.5, 1.5, 1.5, 1.5]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L175",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "float-array",
        signature = "(float-array size-or-seq)\n(float-array size init-val-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of floats",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of floats. Accepts the same arities as <code>int-array</code>; coerces elements to float via <code>floatval</code> and zero-pads with <code>0.0</code>.
""",
            example = "(float-array 3) ; =&gt; &lt;PHP-Array [0, 0, 0]&gt;\n(float-array 4 1.5) ; =&gt; &lt;PHP-Array [1.5, 1.5, 1.5, 1.5]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L168",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "int-array",
        signature = "(int-array size-or-seq)\n(int-array size init-val-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of integers, matching Clojure's clojure",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of integers, matching Clojure's <code>clojure.core/int-array</code>.<br /><br />
- <code>(int-array size-or-seq)</code> — given a non-negative integer, fills with <code>0</code>;<br />
    given a sequence, coerces each element to int via <code>intval</code>.<br />
  - <code>(int-array size init-val-or-seq)</code> — when <code>init-val-or-seq</code> is a number, every<br />
    slot is filled with it (coerced to int). When it is a sequence, its elements<br />
    fill the prefix of the array (truncated to <code>size</code> if longer) and the remaining<br />
    slots are zero-padded.<br /><br />
PHP has no typed arrays, so the result is a plain PHP array.
""",
            example = "(int-array 3) ; =&gt; &lt;PHP-Array [0, 0, 0]&gt;\n(int-array [1.5 2.7]) ; =&gt; &lt;PHP-Array [1, 2]&gt;\n(int-array 4 7) ; =&gt; &lt;PHP-Array [7, 7, 7, 7]&gt;\n(int-array 5 [10 20]) ; =&gt; &lt;PHP-Array [10, 20, 0, 0, 0]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L145",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "into-array",
        signature = "(into-array aseq)\n(into-array _type aseq)",
        completion = CompletionInfo(
            tailText = "Returns a PHP array containing the elements of aseq",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a PHP array containing the elements of <code>aseq</code>. Accepts any<br />
  collection (vector, list, set, map, PHP array) or <code>nil</code>, which yields<br />
  an empty PHP array. The optional <code>type</code> argument is accepted for<br />
  <code>.cljc</code> interop but has no runtime effect — PHP has no typed arrays,<br />
  so the result is always a plain PHP array. Use the dedicated coercion<br />
  helpers (<code>int-array</code>, <code>long-array</code>, <code>float-array</code>, <code>double-array</code>,<br />
  <code>short-array</code>) when element coercion is actually required.
""",
            example = "(into-array [1 2 3]) ; =&gt; &lt;PHP-Array [1, 2, 3]&gt;\n(into-array :Object [:a :b]) ; =&gt; &lt;PHP-Array [:a, :b]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L68",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "long-array",
        signature = "(long-array size-or-seq)\n(long-array size init-val-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of longs (same as int-array in PHP)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of longs (same as int-array in PHP). Accepts the same arities and semantics as <code>int-array</code>.
""",
            example = "(long-array 3) ; =&gt; &lt;PHP-Array [0, 0, 0]&gt;\n(long-array 4 7) ; =&gt; &lt;PHP-Array [7, 7, 7, 7]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L161",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "object-array",
        signature = "(object-array size-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of the given size initialized to nil, or a PHP array containing the elements ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of the given size initialized to <code>nil</code>, or a PHP<br />
  array containing the elements of the given sequence. Matches Clojure's<br />
  <code>object-array</code> for <code>.cljc</code> interop — in Phel the result is a plain PHP<br />
  array (accessible via <code>php/aget</code>/<code>php/aset</code>) since PHP has no typed<br />
  array distinction.
""",
            example = "(object-array 3) ; =&gt; &lt;PHP-Array [nil, nil, nil]&gt;\n(object-array [1 2 3]) ; =&gt; &lt;PHP-Array [1, 2, 3]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L42",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "php-associative-array",
        signature = "(php-associative-array & xs)",
        completion = CompletionInfo(
            tailText = "Creates a PHP associative array from key-value pairs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP associative array from key-value pairs.<br /><br />
Arguments:<br />
    Key-value pairs (must be even number of arguments)
""",
            example = "(php-associative-array \"name\" \"Alice\" \"age\" 30) ; =&gt; &lt;PHP-Array [\"name\":\"Alice\", \"age\":30]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "php-indexed-array",
        signature = "(php-indexed-array & xs)",
        completion = CompletionInfo(
            tailText = "Creates a PHP indexed array from the given values",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a PHP indexed array from the given values.",
            example = "(php-indexed-array 1 2 3) ; =&gt; &lt;PHP-Array [1, 2, 3]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L18",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "short-array",
        signature = "(short-array size-or-seq)\n(short-array size init-val-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of shorts (16-bit integers)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of shorts (16-bit integers). Accepts the same arities and semantics as <code>int-array</code>.
""",
            example = "(short-array 3) ; =&gt; &lt;PHP-Array [0, 0, 0]&gt;\n(short-array 4 7) ; =&gt; &lt;PHP-Array [7, 7, 7, 7]&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L182",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "to-array",
        signature = "(to-array coll)",
        completion = CompletionInfo(
            tailText = "Returns a PHP array containing the elements of coll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a PHP array containing the elements of <code>coll</code>. Accepts any collection (vector, list, set, map, PHP array) or <code>nil</code>, which yields an empty PHP array. Matches Clojure's <code>to-array</code> for <code>.cljc</code> interop — in Phel the result is a plain PHP array since PHP has no <code>Object[]</code>.
""",
            example = "(to-array [1 2 3]) ; =&gt; &lt;PHP-Array [1, 2, 3]&gt;\n(to-array nil) ; =&gt; &lt;PHP-Array []&gt;",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/arrays.phel#L59",
                docs = "",
            ),
        ),
    )
)
