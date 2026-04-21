package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

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
Returns a shallow copy of a PHP array. The returned array is a<br />
  distinct value — mutating the copy via <code>php/aset</code> does not affect the<br />
  original, and vice versa. Matches Clojure's <code>aclone</code> for <code>.cljc</code><br />
  interop; raises <code>InvalidArgumentException</code> on non-array inputs since<br />
  Phel's persistent collections are already immutable and don't need<br />
  cloning.
""",
            example = "(aclone (object-array 3)) ; =&gt; a fresh PHP array [nil, nil, nil]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L79",
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
Returns the value at <code>index</code> in a PHP array. With multiple indices,<br />
   accesses nested arrays: <code>(aget arr i j)</code> is <code>(aget (aget arr i) j)</code>.<br />
   Matches Clojure's <code>aget</code> for <code>.cljc</code> interop.
""",
            example = "(aget (php/array 10 20 30) 1) ; =&gt; 20",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L160",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L91",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L173",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "double-array",
        signature = "(double-array size-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of doubles (same as float-array in PHP)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of doubles (same as float-array in PHP).<br />
   Given a size, fills with <code>0.0</code>. Given a sequence, coerces each element to float.
""",
            example = "(double-array 3) ; =&gt; PHP array [0.0, 0.0, 0.0]\n(double-array [1 2]) ; =&gt; PHP array [1.0, 2.0]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L144",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "float-array",
        signature = "(float-array size-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of floats",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of floats. Given a size, fills with <code>0.0</code>.<br />
   Given a sequence, coerces each element to float via <code>floatval</code>.
""",
            example = "(float-array 3) ; =&gt; PHP array [0.0, 0.0, 0.0]\n(float-array [1 2]) ; =&gt; PHP array [1.0, 2.0]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L136",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "int-array",
        signature = "(int-array size-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of integers",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of integers. Given a size, fills with <code>0</code>.<br />
   Given a sequence, coerces each element to int via <code>intval</code>.<br />
   PHP has no typed arrays, so the result is a plain PHP array.
""",
            example = "(int-array 3) ; =&gt; PHP array [0, 0, 0]\n(int-array [1.5 2.7]) ; =&gt; PHP array [1, 2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L119",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "long-array",
        signature = "(long-array size-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of longs (same as int-array in PHP)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of longs (same as int-array in PHP).<br />
   Given a size, fills with <code>0</code>. Given a sequence, coerces each element to int.
""",
            example = "(long-array 3) ; =&gt; PHP array [0, 0, 0]\n(long-array [1.5 2.7]) ; =&gt; PHP array [1, 2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L128",
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
            example = "(object-array 3) ; =&gt; a PHP array [nil, nil, nil]\n(object-array [1 2 3]) ; =&gt; a PHP array [1, 2, 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L42",
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
            example = "(php-associative-array \"name\" \"Alice\" \"age\" 30) ; =&gt; (PHP array [\"name\" =&gt; \"Alice\", \"age\" =&gt; 30])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L24",
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
            example = "(php-indexed-array 1 2 3) ; =&gt; (PHP array [1, 2, 3])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L18",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "short-array",
        signature = "(short-array size-or-seq)",
        completion = CompletionInfo(
            tailText = "Creates a PHP array of shorts (16-bit integers)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a PHP array of shorts (16-bit integers). Given a size, fills with <code>0</code>.<br />
   Given a sequence, coerces each element to int via <code>intval</code>.
""",
            example = "(short-array 3) ; =&gt; PHP array [0, 0, 0]\n(short-array [1.5 2.7]) ; =&gt; PHP array [1, 2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L152",
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
Returns a PHP array containing the elements of <code>coll</code>. Accepts any<br />
  collection (vector, list, set, map, PHP array) or <code>nil</code>, which yields<br />
  an empty PHP array. Matches Clojure's <code>to-array</code> for <code>.cljc</code> interop —<br />
  in Phel the result is a plain PHP array since PHP has no <code>Object[]</code>.
""",
            example = "(to-array [1 2 3]) ; =&gt; a PHP array [1, 2, 3]\n(to-array nil) ; =&gt; a PHP array []",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/arrays.phel#L59",
                docs = "",
            ),
        ),
    )
)
