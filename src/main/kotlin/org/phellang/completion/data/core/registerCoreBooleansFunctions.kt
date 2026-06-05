package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreBooleansFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "<",
        signature = "(< a & more)",
        completion = CompletionInfo(
            tailText = "Checks if each argument is strictly less than the following argument",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if each argument is strictly less than the following argument.",
            example = "(&lt; 1 2 3 4) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L199",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "<=",
        signature = "(<= a & more)",
        completion = CompletionInfo(
            tailText = "Checks if each argument is less than or equal to the following argument",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if each argument is less than or equal to the following argument. Returns a boolean.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L210",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "<=>",
        signature = "(<=> a b)",
        completion = CompletionInfo(
            tailText = "Alias for the spaceship PHP operator in ascending order",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Alias for the spaceship PHP operator in ascending order. Returns an int.<br />
  Dispatches on <code>Ratio</code> and <code>BigInt</code> so numeric ordering stays correct<br />
  for those types.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L241",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "=",
        signature = "(= a & more)",
        completion = CompletionInfo(
            tailText = "Checks if all values are equal (value equality, not identity)",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if all values are equal (value equality, not identity).",
            example = "(= [1 2 3] [1 2 3]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L104",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "==",
        signature = "(== a & more)",
        completion = CompletionInfo(
            tailText = "Numeric equality comparison",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Numeric equality comparison. Returns true if all arguments have the same<br />
  numeric value regardless of type (e.g. int vs float); throws on non-numeric<br />
  arguments. Unlike <code>=</code>, which is value-and-type-strict, <code>==</code> treats <code>1</code> and<br />
  <code>1.0</code> as equal, matching Clojure's <code>clojure.core/==</code>.
""",
            example = "(== 1 1.0) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L131",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = ">",
        signature = "(> a & more)",
        completion = CompletionInfo(
            tailText = "Checks if each argument is strictly greater than the following argument",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if each argument is strictly greater than the following argument.",
            example = "(&gt; 4 3 2 1) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L220",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = ">=",
        signature = "(>= a & more)",
        completion = CompletionInfo(
            tailText = "Checks if each argument is greater than or equal to the following argument",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if each argument is greater than or equal to the following argument. Returns a boolean.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L231",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = ">=<",
        signature = "(>=< a b)",
        completion = CompletionInfo(
            tailText = "Alias for the spaceship PHP operator in descending order",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Alias for the spaceship PHP operator in descending order. Returns an int.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L250",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "all?",
        signature = "(all? pred coll)",
        completion = CompletionInfo(
            tailText = "Returns true if predicate is true for every element in collection, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if predicate is true for every element in collection, false otherwise.
""",
            example = "(all? even? [2 4 6 8]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L259",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "and",
        signature = "(and & args)",
        completion = CompletionInfo(
            tailText = "Evaluates expressions left to right, returning the first falsy value or the last value",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates expressions left to right, returning the first falsy value or the last value.
""",
            example = "(and true 1 \"hello\") ; =&gt; \"hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L49",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "compare",
        signature = "(compare x y)",
        completion = CompletionInfo(
            tailText = "Compares x and y, returning a negative integer, zero, or a positive integer when x is less than, ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Compares <code>x</code> and <code>y</code>, returning a negative<br />
  integer, zero, or a positive integer when <code>x</code> is less than, equal to, or<br />
  greater than <code>y</code>.<br /><br />
<code>nil</code> is less than every non-nil value and equal to itself. Throws<br />
  <code>InvalidArgumentException</code> when <code>x</code> and <code>y</code> come from mutually incomparable<br />
  categories (e.g. <code>(compare 1 [])</code>).
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L435",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "contains?",
        signature = "(contains? coll key)",
        completion = CompletionInfo(
            tailText = "Returns true if key is present in collection (checks keys/indices, not values)",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if key is present in collection (checks keys/indices, not values).",
            example = "(contains? [10 20 30] 1) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L345",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "every?",
        signature = "(every? pred coll)",
        completion = CompletionInfo(
            tailText = "Returns true if predicate is true for every element in collection, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if predicate is true for every element in collection, false otherwise.<br />
  Alias for <code>all?</code>.
""",
            example = "(every? even? [2 4 6 8]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L268",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "false?",
        signature = "(false? x)",
        completion = CompletionInfo(
            tailText = "Checks if value is exactly false (not just falsy)",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if value is exactly false (not just falsy).",
            example = "(false? nil) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L327",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "id",
        signature = "(id a & more)",
        completion = CompletionInfo(
            tailText = "Checks if all values are identical",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if all values are identical. Same as <code>a === b</code> in PHP.
""",
            example = null,
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "identical?"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L97",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "identical?",
        signature = "(identical? a & more)",
        completion = CompletionInfo(
            tailText = "Checks if all values are identical",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if all values are identical. Same as <code>a === b</code> in PHP.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L87",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keyword",
        signature = "(keyword x)",
        completion = CompletionInfo(
            tailText = "Creates a new Keyword",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new Keyword.<br /><br />
Arity-1 accepts a string, keyword, or symbol. Returns <code>nil</code> when <code>x</code> is <code>nil</code>.<br />
  If <code>x</code> is already a keyword, it is returned unchanged.<br /><br />
Arity-2 builds a namespaced keyword from the namespace and name parts; returns<br />
  <code>nil</code> when <code>name</code> is <code>nil</code>.
""",
            example = "(keyword \"name\") ; =&gt; :name\n(keyword :abc) ; =&gt; :abc\n(keyword \"ns\" \"name\") ; =&gt; :ns/name",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L60",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nil?",
        signature = "(nil? x)",
        completion = CompletionInfo(
            tailText = "Returns true if value is nil, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if value is nil, false otherwise.",
            example = "(nil? (get {:a 1} :b)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L333",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "not",
        signature = "(not x)",
        completion = CompletionInfo(
            tailText = "Returns true if value is falsy (nil or false), false otherwise",
            priority = PhelCompletionPriority.CONTROL_FLOW,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if value is falsy (nil or false), false otherwise.",
            example = "(not nil) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L146",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "not-any?",
        signature = "(not-any? pred coll)",
        completion = CompletionInfo(
            tailText = "Returns true if (pred x) is logical false for every x in coll or if coll is empty",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>(pred x)</code> is logical false for every <code>x</code> in <code>coll</code><br />
   or if <code>coll</code> is empty. Otherwise returns false.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L299",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "not-every?",
        signature = "(not-every? pred coll)",
        completion = CompletionInfo(
            tailText = "Returns false if (pred x) is logical true for every x in collection coll or if coll is empty",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns false if <code>(pred x)</code> is logical true for every <code>x</code> in collection <code>coll</code><br />
   or if <code>coll</code> is empty. Otherwise returns true.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L279",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "not=",
        signature = "(not= a & more)",
        completion = CompletionInfo(
            tailText = "Checks if all values are unequal",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if all values are unequal. Same as <code>a != b</code> in PHP.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L152",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "or",
        signature = "(or & args)",
        completion = CompletionInfo(
            tailText = "Evaluates expressions left to right, returning the first truthy value or the last value",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates expressions left to right, returning the first truthy value or the last value.
""",
            example = "(or false nil 42 100) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L38",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some",
        signature = "(some pred coll)",
        completion = CompletionInfo(
            tailText = "Returns the first truthy value of applying predicate to elements, or nil if none found",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the first truthy value of applying predicate to elements, or nil if none found.
""",
            example = "(some #(when (&gt; % 10) %) [5 15 8]) ; =&gt; 15",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L305",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some?",
        signature = "(some? x)",
        completion = CompletionInfo(
            tailText = "With 1 arg, returns true if x is not nil (Clojure semantics)",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
With 1 arg, returns true if <code>x</code> is not nil (Clojure semantics).<br />
   With 2 args, returns true if <code>pred</code> is true for at least one element in <code>coll</code>.
""",
            example = "(some? 1) ; =&gt; true\n(some? even? [1 3 5 6 7]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L288",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "str-contains?",
        signature = "(str-contains? str s)",
        completion = CompletionInfo(
            tailText = "Returns true if str contains s",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if str contains s.",
            example = null,
            deprecation = DeprecationInfo(version = "Use phel\\string\\contains?"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L339",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "true?",
        signature = "(true? x)",
        completion = CompletionInfo(
            tailText = "Checks if value is exactly true (not just truthy)",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if value is exactly true (not just truthy).",
            example = "(true? 1) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L316",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "truthy?",
        signature = "(truthy? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is truthy",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is truthy. Same as <code>x == true</code> in PHP.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/booleans.phel#L322",
                docs = "",
            ),
        ),
    )
)
