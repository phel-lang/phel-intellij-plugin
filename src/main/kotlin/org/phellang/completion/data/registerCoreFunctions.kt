package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "%",
        signature = "(% dividend divisor)",
        completion = CompletionInfo(
            tailText = "Return the remainder of dividend / divisor",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Return the remainder of <code>dividend</code> / <code>divisor</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L148",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*",
        signature = "(* & xs)",
        completion = CompletionInfo(
            tailText = "Returns the product of all elements in xs",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the product of all elements in <code>xs</code>. All elements in <code>xs</code> must be<br />
numbers. If <code>xs</code> is empty, return 1.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L122",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "**",
        signature = "(** a x)",
        completion = CompletionInfo(
            tailText = "Return a to the power of x",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Return <code>a</code> to the power of <code>x</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L155",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*assert*",
        signature = "",
        completion = CompletionInfo(
            tailText = "Controls whether assert expands to a runtime check",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Controls whether <code>assert</code> expands to a runtime check. When logical<br />
  false at macroexpansion time, <code>assert</code> expands to nil and performs no<br />
  runtime check, matching Clojure's compile-time <code><em>assert</em></code> semantics.<br />
  Defaults to <code>true</code>. To disable globally, set the core binding before<br />
  compilation via PHP: <code>\Phel::addDefinition("phel\\core", "<em>assert</em>", false)</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L17",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*build-mode*",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*file*",
        signature = "*file*",
        completion = CompletionInfo(
            tailText = "Returns the path of the current source file",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the path of the current source file.",
            example = "(println *file*) ; =&gt; \"/path/to/current/file.phel\"",
            links = DocumentationLinks(
                github = "",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*hierarchy*",
        signature = "",
        completion = CompletionInfo(
            tailText = "Global hierarchy for keyword/symbol taxonomies",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Global hierarchy for keyword/symbol taxonomies.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L47",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*ns*",
        signature = "*ns*",
        completion = CompletionInfo(
            tailText = "Returns the namespace in the current scope",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the namespace in the current scope.",
            example = "(println *ns*) ; =&gt; \"my-app\\core\"",
            links = DocumentationLinks(
                github = "",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*program*",
        signature = "",
        completion = CompletionInfo(
            tailText = "The script path or namespace being executed",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "The script path or namespace being executed.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L145",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "+",
        signature = "(+ & xs)",
        completion = CompletionInfo(
            tailText = "Returns the sum of all elements in xs",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the sum of all elements in <code>xs</code>. All elements <code>xs</code> must be numbers.<br />
  If <code>xs</code> is empty, return 0.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L98",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "-",
        signature = "(- & xs)",
        completion = CompletionInfo(
            tailText = "Returns the difference of all elements in xs",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the difference of all elements in <code>xs</code>. If <code>xs</code> is empty, return 0. If <code>xs</code><br />
  has one element, return the negative value of that element.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L109",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "->",
        signature = "(-> x & forms)",
        completion = CompletionInfo(
            tailText = "Threads the expr through the forms",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Threads the expr through the forms. Inserts <code>x</code> as the second item<br />
  in the first form, making a list of it if it is not a list already.<br />
  If there are more forms, insert the first form as the second item in<br />
  the second form, etc.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L184",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "->>",
        signature = "(->> x & forms)",
        completion = CompletionInfo(
            tailText = "Threads the expr through the forms",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Threads the expr through the forms. Inserts <code>x</code> as the<br />
  last item in the first form, making a list of it if it is not a<br />
  list already. If there are more forms, insert the first form as the<br />
  last item in the second form, etc.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L200",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "/",
        signature = "(/ & xs)",
        completion = CompletionInfo(
            tailText = "Returns the nominator divided by all the denominators",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the nominator divided by all the denominators. If <code>xs</code> is empty,<br />
returns 1. If <code>xs</code> has one value, returns the reciprocal of x.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L135",
                docs = "",
            ),
        ),
    ),
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L125",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L138",
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
            summary = "Alias for the spaceship PHP operator in ascending order. Returns an int.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L175",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L100",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L150",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L163",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L181",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "NAN",
        signature = "",
        completion = CompletionInfo(
            tailText = "Constant for Not a Number (NAN) values",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Constant for Not a Number (NAN) values.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L94",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "NaN?",
        signature = "(NaN? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is not a number",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is not a number. Alias for <code>nan?</code>, matching Clojure's <code>NaN?</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L211",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "abs",
        signature = "(abs x)",
        completion = CompletionInfo(
            tailText = "Returns the absolute value of x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the absolute value of <code>x</code>.<br />
  Throws <code>InvalidArgumentException</code> if <code>x</code> is not a number, matching Clojure<br />
  rather than PHP's permissive <code>abs(null) => 0</code> / <code>abs("abc")</code> coercions.
""",
            example = "(abs -5) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L224",
                docs = "",
            ),
        ),
    ),
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
        name = "add-tap",
        signature = "(add-tap f)",
        completion = CompletionInfo(
            tailText = "Registers f as a tap",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers <code>f</code> as a tap. Every call to <code>tap></code> invokes each registered tap<br />
  with the tapped value. Returns nil.
""",
            example = "(add-tap println)\n(tap&gt; 42) ; prints 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/tap.phel#L15",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "add-watch",
        signature = "(add-watch variable key f)",
        completion = CompletionInfo(
            tailText = "Adds a watch function to a variable",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Adds a watch function to a variable. The watch fn is called when the variable<br />
  changes with four arguments: key, ref, old-value, new-value.
""",
            example = "(add-watch my-var :logger (fn [key ref old new] (println old \"-&gt;\" new)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L101",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L189",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "alter-var-root",
        signature = "(alter-var-root & _)",
        completion = CompletionInfo(
            tailText = "Clojure's alter-var-root is out of scope in Phel: there are no first-class vars whose root bindin...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Clojure's <code>alter-var-root</code> is out of scope in Phel: there are no first-class<br />
  vars whose root binding could be swapped. Reaching for this function in<br />
  <code>.cljc</code> code is nearly always a bug; prefer an <code>atom</code> and <code>swap!</code> for mutable<br />
  state, or redefine the top-level binding with <code>def</code> if the intent was to<br />
  replace it at load time. Calling <code>alter-var-root</code> at runtime throws to make<br />
  the mismatch obvious instead of silently no-oping.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L110",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ancestors",
        signature = "(ancestors tag)",
        completion = CompletionInfo(
            tailText = "Returns the set of all transitive ancestors of tag, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the set of all transitive ancestors of tag, or nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L137",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L45",
                docs = "",
            ),
        ),
    ),
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
Returns true given any argument, including <code>nil</code> and <code>false</code>. Mirrors<br />
  Clojure's <code>clojure.core/any?</code> — useful as a default predicate in spec<br />
  / validation contexts where every value should be accepted.
""",
            example = "(any? nil) ; =&gt; true\n(any? 0) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L267",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "apply",
        signature = "(apply f expr*)",
        completion = CompletionInfo(
            tailText = "Calls the function with the given arguments",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Calls the function with the given arguments. The last argument must be a list of values, which are passed as separate arguments, rather than a single list. Apply returns the result of the calling function.
""",
            example = "(apply + [1 2 3]) ; =&gt; 6",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/functions-and-recursion/#apply-functions",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "argv",
        signature = "",
        completion = CompletionInfo(
            tailText = "Vector of user arguments passed to the script (excludes program name)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Vector of user arguments passed to the script (excludes program name).<br />
  Use <em>program</em> to get the script path or namespace.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L150",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "array-map",
        signature = "",
        completion = CompletionInfo(
            tailText = "Constructs a map from the given key/value pairs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Constructs a map from the given key/value pairs. If any keys are<br />
  equal, later values replace earlier ones, as if by repeated <code>assoc</code>.<br />
  Phel has no distinct array-map type, so the result is the same<br />
  persistent map as <code>hash-map</code> — <code>array-map</code> exists for <code>.cljc</code> interop<br />
  with Clojure sources.
""",
            example = "(array-map :a 1 :b 2) ; =&gt; {:a 1 :b 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L47",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "as->",
        signature = "(as-> expr name & forms)",
        completion = CompletionInfo(
            tailText = "Binds name to expr, evaluates the first form in the lexical context of that binding, then binds n...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Binds <code>name</code> to <code>expr</code>, evaluates the first form in the lexical context<br />
  of that binding, then binds name to that result, repeating for each<br />
  successive form, returning the result of the last form.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L264",
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
        name = "assert",
        signature = "(assert expr & [message])",
        completion = CompletionInfo(
            tailText = "Throws an exception if expr is falsy",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Throws an exception if expr is falsy. Optional message string.<br />
  Used for precondition checking in application code. When <code><em>assert</em></code><br />
  is logical false at macroexpansion time, <code>assert</code> expands to nil and<br />
  performs no runtime check.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L684",
                docs = "",
            ),
        ),
    ),
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
Associates one or more key-value pairs with a collection.<br />
  Additional key-value pairs beyond the first are applied in order.<br />
  Throws if an odd number of extra arguments is provided.
""",
            example = "(assoc {:a 1} :b 2) ; =&gt; {:a 1 :b 2}\n(assoc {:a 1} :b 2 :c 3) ; =&gt; {:a 1 :b 2 :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L186",
                docs = "",
            ),
        ),
    ),
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
   Variadic forms apply each <code>key-value</code> pair in order. Raises<br />
   <code>InvalidArgumentException</code> when <code>tcoll</code> is not a supported transient<br />
   collection or when an odd number of extra arguments is provided.<br />
   Matches Clojure's <code>assoc!</code> semantics.
""",
            example = "(persistent! (assoc! (transient {}) :a 1 :b 2)) ; =&gt; {:a 1 :b 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transients.phel#L108",
                docs = "",
            ),
        ),
    ),
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
            example = "(assoc-in {:a {:b 1}} [:a :c] 2) ; =&gt; {:a {:b 1 :c 2}}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L266",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L382",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "atom",
        signature = "(atom value)",
        completion = CompletionInfo(
            tailText = "Creates a new atom with the given value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new atom with the given value.<br /><br />
Atoms provide a way to manage mutable state. Use <code>reset!</code> to set a new value<br />
  and <code>swap!</code> to update based on the current value.
""",
            example = "(def counter (atom 0))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "atom?",
        signature = "(atom? x)",
        completion = CompletionInfo(
            tailText = "Returns true if the given value is an atom",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if the given value is an atom.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L39",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "binding",
        signature = "(binding bindings & body)",
        completion = CompletionInfo(
            tailText = "Temporary redefines definitions while executing the body",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Temporary redefines definitions while executing the body.<br />
  The value will be reset after the body was executed.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L383",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-and",
        signature = "(bit-and x y & args)",
        completion = CompletionInfo(
            tailText = "Bitwise and",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise and.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L25",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-clear",
        signature = "(bit-clear x n)",
        completion = CompletionInfo(
            tailText = "Clear bit an index n",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Clear bit an index <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L75",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-flip",
        signature = "(bit-flip x n)",
        completion = CompletionInfo(
            tailText = "Flip bit at index n",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Flip bit at index <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L80",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-not",
        signature = "(bit-not x)",
        completion = CompletionInfo(
            tailText = "Bitwise complement",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise complement.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L49",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-or",
        signature = "(bit-or x y & args)",
        completion = CompletionInfo(
            tailText = "Bitwise or",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise or.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L33",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-set",
        signature = "(bit-set x n)",
        completion = CompletionInfo(
            tailText = "Set bit an index n",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Set bit an index <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L70",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-shift-left",
        signature = "(bit-shift-left x n)",
        completion = CompletionInfo(
            tailText = "Bitwise shift left",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise shift left.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L56",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-shift-right",
        signature = "(bit-shift-right x n)",
        completion = CompletionInfo(
            tailText = "Bitwise shift right",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise shift right.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L63",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-test",
        signature = "(bit-test x n)",
        completion = CompletionInfo(
            tailText = "Test bit at index n",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Test bit at index <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L85",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "bit-xor",
        signature = "(bit-xor x y & args)",
        completion = CompletionInfo(
            tailText = "Bitwise xor",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bitwise xor.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L41",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L260",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L255",
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
Returns all but the last item in <code>coll</code>.
""",
            example = "(butlast [1 2 3 4]) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L363",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "byte",
        signature = "(byte x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a signed 8-bit integer in the range -128",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a signed 8-bit integer in the range <code>-128..127</code>.<br />
   Decimal values are truncated toward zero (as in Clojure on the JVM).<br />
   Values outside the range or non-numeric inputs raise<br />
   <code>InvalidArgumentException</code>. Phel has no dedicated byte type, so the<br />
   result is a plain PHP int — <code>byte</code> exists for <code>.cljc</code> interop with<br />
   Clojure sources.
""",
            example = "(byte 127) ; =&gt; 127\n(byte 1.9) ; =&gt; 1\n(byte -128) ; =&gt; -128",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L285",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "case",
        signature = "(case e & pairs)",
        completion = CompletionInfo(
            tailText = "Evaluates expression and matches it against constant test values, returning the associated result",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates expression and matches it against constant test values, returning the associated result.
""",
            example = "(case x 1 \"one\" 2 \"two\" \"other\") ; =&gt; \"one\" (when x is 1)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L45",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L842",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "catch",
        signature = "(catch exception-type exception-name expr*)",
        completion = CompletionInfo(
            tailText = "Handle exceptions thrown in a try block by matching on the provided exception type",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Handle exceptions thrown in a <code>try</code> block by matching on the provided exception type. The caught exception is bound to exception-name while evaluating the expressions.
""",
            example = "(try (throw (php/new \\Exception \"error\")) (catch \\Exception e (php/-&gt; e (getMessage))))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/control-flow/#try-catch-and-finally",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "char",
        signature = "(char x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a single-character string representing the given Unicode code point",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a single-character string representing the given<br />
   Unicode code point. Accepts a non-negative integer (the code point,<br />
   converted via <code>mb_chr</code>) or a single-character string, which is<br />
   returned as-is. Phel has no dedicated char type — character literals<br />
   such as <code>\A</code> are already single-character strings — so the result<br />
   is always a plain string. Matches Clojure's <code>char</code> for <code>.cljc</code><br />
   interop; raises <code>InvalidArgumentException</code> on negative ints,<br />
   non-single-character strings, and all other inputs.
""",
            example = "(char 65) ; =&gt; \"A\"\n(char 32) ; =&gt; \" \"\n(char \\A) ; =&gt; \"A\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L303",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L142",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "coerce-in",
        signature = "(coerce-in v min max)",
        completion = CompletionInfo(
            tailText = "Returns v if it is in the range, or min if v is less than min, or max if v is greater than max",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>v</code> if it is in the range, or <code>min</code> if <code>v</code> is less than <code>min</code>, or <code>max</code> if <code>v</code> is greater than <code>max</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L380",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L406",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "comment",
        signature = "(comment &)",
        completion = CompletionInfo(
            tailText = "Ignores the body of the comment",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Ignores the body of the comment.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L125",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L77",
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
Returns a lazy sequence with specified values removed from <code>coll</code>.<br />
  If no values are specified, removes nil values by default.
""",
            example = "(compact [1 nil 2 nil 3]) ; =&gt; (1 2 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1108",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "compare",
        signature = "(compare x y)",
        completion = CompletionInfo(
            tailText = "Compares x and y using PHP's spaceship operator, returning a negative integer, zero, or a positiv...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Compares <code>x</code> and <code>y</code> using PHP's spaceship operator, returning a negative<br />
  integer, zero, or a positive integer when <code>x</code> is less than, equal to, or<br />
  greater than <code>y</code>.<br /><br />
<code>nil</code> is less than every non-nil value and equal to itself. Throws<br />
  <code>InvalidArgumentException</code> when <code>x</code> and <code>y</code> come from mutually incomparable<br />
  categories (e.g. <code>(compare 1 [])</code>).
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L303",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "compile",
        signature = "(compile form)",
        completion = CompletionInfo(
            tailText = "Returns the compiled PHP code string for the given form",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the compiled PHP code string for the given form.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L728",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L91",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "completing",
        signature = "(completing f & args)",
        completion = CompletionInfo(
            tailText = "Takes a reducing function f of 2 args and returns a fn suitable for transduce by adding a 1-arity...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a reducing function <code>f</code> of 2 args and returns a fn suitable for transduce<br />
  by adding a 1-arity (completion) that calls <code>cf</code> (default: identity).
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L74",
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
            example = "(concat [1 2] [3 4]) ; =&gt; (1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L828",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "cond",
        signature = "(cond & pairs)",
        completion = CompletionInfo(
            tailText = "Evaluates test/expression pairs, returning the first matching expression",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Evaluates test/expression pairs, returning the first matching expression.",
            example = "(cond (&lt; x 0) \"negative\" (&gt; x 0) \"positive\" \"zero\") ; =&gt; \"negative\", \"positive\", or \"zero\" depending on x",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L31",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "cond->",
        signature = "(cond-> expr & clauses)",
        completion = CompletionInfo(
            tailText = "Takes an expression and a set of test/form pairs",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes an expression and a set of test/form pairs. Threads <code>expr</code> (via <code>-></code>)<br />
  through each form for which the corresponding test expression is true.<br />
  Note that, unlike <code>cond</code> branching, <code>cond-></code> threading does not short-circuit<br />
  after the first true test expression.
""",
            example = "(cond-&gt; 1 true inc false (* 42) true (* 3)) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L288",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "cond->>",
        signature = "(cond->> expr & clauses)",
        completion = CompletionInfo(
            tailText = "Takes an expression and a set of test/form pairs",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes an expression and a set of test/form pairs. Threads <code>expr</code> (via <code>->></code>)<br />
  through each form for which the corresponding test expression is true.<br />
  Note that, unlike <code>cond</code> branching, <code>cond->></code> threading does not short-circuit<br />
  after the first true test expression.
""",
            example = "(cond-&gt;&gt; [1 2 3] true (map inc) false (filter odd?)) ; =&gt; [2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L305",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "condp",
        signature = "(condp pred expr & clauses)",
        completion = CompletionInfo(
            tailText = "Takes a binary predicate, an expression, and a set of clauses",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a binary predicate, an expression, and a set of clauses.<br />
  Each clause takes the form of either:<br />
    test-expr result-expr<br />
    test-expr :>> result-fn<br />
  For each clause, (pred test-expr expr) is evaluated. If it returns<br />
  logical true, the clause is a match. If a binary clause is a match,<br />
  result-expr is returned. If a ternary clause with :>> is a match,<br />
  the result of (pred test-expr expr) is passed to result-fn and the<br />
  return value is the result. If no clause matches, the default value<br />
  is returned (if provided), otherwise an exception is thrown.
""",
            example = "(condp = 1 1 \"one\" 2 \"two\" \"other\") ; =&gt; \"one\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L76",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "conj",
        signature = "(conj)",
        completion = CompletionInfo(
            tailText = "Returns a new collection with values added",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new collection with values added. Appends to vectors/sets, prepends to lists.
""",
            example = "(conj [1 2] 3) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/data-structures/#adding-elements-with-conj",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "conj!",
        signature = "(conj!)",
        completion = CompletionInfo(
            tailText = "Adds value to the transient collection tcoll, mutating it in place, and returns tcoll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Adds <code>value</code> to the transient collection <code>tcoll</code>, mutating it in place,<br />
   and returns <code>tcoll</code>. The 'addition' may happen at different 'places'<br />
   depending on the concrete transient type: transient vectors append at<br />
   the tail, transient hash-sets add the element (no-op if already<br />
   present), and transient hash-maps treat <code>value</code> as a <code>[key value]</code><br />
   pair (or an associative collection of entries).<br />
   With zero arguments returns a new empty transient vector. With one<br />
   argument returns <code>tcoll</code> unchanged. Variadic forms reduce <code>conj!</code> over<br />
   the remaining values. Raises <code>InvalidArgumentException</code> when <code>tcoll</code><br />
   is not a transient collection. Matches Clojure's <code>conj!</code> semantics.
""",
            example = "(persistent (conj! (transient [1 2]) 3)) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transients.phel#L76",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "cons",
        signature = "(cons x coll)",
        completion = CompletionInfo(
            tailText = "Prepends an element to the beginning of a collection",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Prepends an element to the beginning of a collection.",
            example = "(cons 0 [1 2 3]) ; =&gt; [0 1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-basics.phel#L26",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L86",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L735",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L275",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "count",
        signature = "(count coll)",
        completion = CompletionInfo(
            tailText = "Counts the number of elements in a sequence",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Counts the number of elements in a sequence. Can be used on everything that implements the PHP Countable interface.<br /><br />
Works with lists, vectors, hash-maps, sets, strings, and PHP arrays.<br />
  Returns 0 for nil.
""",
            example = "(count [1 2 3]) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-basics.phel#L81",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L433",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "csv-seq",
        signature = "(csv-seq filename)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of rows from a CSV file",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of rows from a CSV file.",
            example = "(take 10 (csv-seq \"data.csv\")) ; =&gt; [[\"col1\" \"col2\"] [\"val1\" \"val2\"] ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L163",
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
Returns an infinite lazy sequence that cycles through the elements of collection.
""",
            example = "(take 7 (cycle [1 2 3])) ; =&gt; (1 2 3 1 2 3 1)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L819",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dec",
        signature = "(dec x)",
        completion = CompletionInfo(
            tailText = "Decrements x by one",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Decrements <code>x</code> by one.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L169",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "declare",
        signature = "",
        completion = CompletionInfo(
            tailText = "Declare a global symbol before it is defined",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Declare a global symbol before it is defined.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L135",
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
Returns a lazy sequence with consecutive duplicate values removed in <code>coll</code>.<br />
  When called with no args, returns a transducer.
""",
            example = "(dedupe [1 1 2 2 2 3 1 1]) ; =&gt; (1 2 3 1)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1081",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L270",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "def",
        signature = "(def name meta? value)",
        completion = CompletionInfo(
            tailText = "This special form binds a value to a global symbol",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = "This special form binds a value to a global symbol.",
            example = "(def my-value 42)",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/global-and-local-bindings/#definition-def",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "def-",
        signature = "",
        completion = CompletionInfo(
            tailText = "Define a private value that will not be exported",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a private value that will not be exported.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L79",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defexception",
        signature = "(defexception name)",
        completion = CompletionInfo(
            tailText = "Define a new exception",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a new exception.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L111",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defexception*",
        signature = "(defexception name)",
        completion = CompletionInfo(
            tailText = "Defines a new exception",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = "Defines a new exception.",
            example = "(defexception my-error)",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/exceptions",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "definterface",
        signature = "(definterface name & fns)",
        completion = CompletionInfo(
            tailText = "An interface in Phel defines an abstract set of functions",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
An interface in Phel defines an abstract set of functions. It is directly mapped to a PHP interface. An interface can be defined by using the definterface macro.
""",
            example = "(definterface name &amp; fns)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "definterface*",
        signature = "(definterface name & fns)",
        completion = CompletionInfo(
            tailText = "An interface in Phel defines an abstract set of functions",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
An interface in Phel defines an abstract set of functions. It is directly mapped to a PHP interface. An interface can be defined by using the definterface macro.
""",
            example = "(definterface Greeter (greet [name]))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/interfaces/#defining-interfaces",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defmacro",
        signature = "",
        completion = CompletionInfo(
            tailText = "Define a macro",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a macro.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L84",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defmacro-",
        signature = "(defmacro- name & fdecl)",
        completion = CompletionInfo(
            tailText = "Define a private macro that will not be exported",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a private macro that will not be exported.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L94",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defmethod",
        signature = "(defmethod multi-name dispatch-val & fn-tail)",
        completion = CompletionInfo(
            tailText = "Registers a method implementation for a multimethod",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers a method implementation for a multimethod.<br /><br />
<code>multi-name</code> is the name of the multimethod defined by <code>defmulti</code>.<br />
  When extending a multimethod from a different namespace, fully qualify<br />
  the multi-name (e.g. <code>phel\test/assert-expr</code>) so the methods table is<br />
  resolved in the multimethod's home namespace.<br />
  <code>dispatch-val</code> is the value that triggers this method.<br />
  <code>args</code> and <code>body</code> define the function implementation.
""",
            example = "(defmethod area :circle [{:radius r}] (* 3.14159 r r))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L537",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defmulti",
        signature = "(defmulti name dispatch-fn)",
        completion = CompletionInfo(
            tailText = "Defines a multimethod",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a multimethod. <code>dispatch-fn</code> is called on the arguments to<br />
  produce a dispatch value, which is then used to select the appropriate<br />
  method registered via <code>defmethod</code>.<br /><br />
If no method matches the dispatch value, the <code>:default</code> method is used<br />
  (if defined), otherwise an error is thrown.
""",
            example = "(defmulti area :shape)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L508",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defn",
        signature = "",
        completion = CompletionInfo(
            tailText = "Define a new global function",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a new global function.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L74",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defn-",
        signature = "(defn- name & fdecl)",
        completion = CompletionInfo(
            tailText = "Define a private function that will not be exported",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Define a private function that will not be exported.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L89",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defprotocol",
        signature = "(defprotocol protocol-name & method-specs)",
        completion = CompletionInfo(
            tailText = "Defines a protocol with the given method signatures",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a protocol with the given method signatures. Each method signature<br />
  is a list of (method-name [args]).<br /><br />
Creates a dispatching function for each method that dispatches on the type<br />
  of the first argument. Use <code>extend-type</code> to add implementations.<br /><br />
A <code>:default</code> type can be registered via <code>extend-type</code> as a fallback when<br />
  no specific type implementation is found.
""",
            example = "(defprotocol Stringable (to-string [this]))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L212",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defrecord",
        signature = "(defrecord name fields & impls)",
        completion = CompletionInfo(
            tailText = "Defines a record type with the given fields, matching Clojure's defrecord",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a record type with the given fields, matching Clojure's <code>defrecord</code>.<br /><br />
Expands to a <code>defstruct</code> plus Clojure-style factory functions:<br />
  - <code>Name</code> — positional constructor (from <code>defstruct</code>)<br />
  - <code>Name?</code> — type predicate (from <code>defstruct</code>)<br />
  - <code>->Name</code> — positional factory, identical to <code>Name</code><br />
  - <code>map->Name</code> — map factory that takes <code>{:field value ...}</code><br /><br />
An optional tail of protocol/method forms is spliced into an <code>extend-type</code><br />
  call, so inline protocol implementations work exactly like Clojure's<br />
  <code>defrecord</code> body. Only Phel protocols are supported in the inline tail;<br />
  PHP interface implementations remain on <code>defstruct</code>.
""",
            example = "(defrecord Point [x y] Drawable (draw [this canvas] ...))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L445",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defstruct",
        signature = "(defstruct name keys & implementations)",
        completion = CompletionInfo(
            tailText = "A Struct is a special kind of Map",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
A Struct is a special kind of Map. It only supports a predefined number of keys and is associated to a global name. The Struct not only defines itself but also a predicate function.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L99",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "defstruct*",
        signature = "(defstruct name [keys*])",
        completion = CompletionInfo(
            tailText = "A Struct is a special kind of Map",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
A Struct is a special kind of Map. It only supports a predefined number of keys and is associated to a global name. The Struct not only defines itself but also a predicate function.
""",
            example = "(defstruct point [x y])",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/data-structures/#structs",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "deftype",
        signature = "(deftype name fields & impls)",
        completion = CompletionInfo(
            tailText = "Defines a type with the given fields, matching Clojure's deftype syntax",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a type with the given fields, matching Clojure's <code>deftype</code> syntax.<br /><br />
Expands to a <code>defstruct</code> plus a Clojure-style positional factory:<br />
  - <code>Name</code> — positional constructor (from <code>defstruct</code>)<br />
  - <code>Name?</code> — type predicate (from <code>defstruct</code>)<br />
  - <code>->Name</code> — positional factory, identical to <code>Name</code><br /><br />
Unlike <code>defrecord</code>, no <code>map->Name</code> factory is generated.<br /><br />
An optional tail of protocol/method forms is spliced into an <code>extend-type</code><br />
  call. Only Phel protocols are supported in the inline tail.<br /><br />
Deviation from Clojure: Phel's <code>deftype</code> shares the map-backed<br />
  <code>defstruct</code> infrastructure, so instances remain map-like (keys are<br />
  accessible via <code>get</code>). Clojure's <code>deftype</code> produces a non-map type;<br />
  if you need that semantic, use native PHP interop.
""",
            example = "(deftype PointT [x y] Drawable (draw [this canvas] ...))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L475",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "delay",
        signature = "(delay & body)",
        completion = CompletionInfo(
            tailText = "Takes a body of expressions and yields a Delay object that will invoke the body only the first ti...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a body of expressions and yields a Delay object that will invoke the<br />
  body only the first time it is forced (via force or deref/@), caching the result.
""",
            example = "(def d (delay (println \"computing\") 42))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/lazy.phel#L12",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "delay?",
        signature = "(delay? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Delay",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if x is a Delay.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/lazy.phel#L29",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "deref",
        signature = "(deref variable & args)",
        completion = CompletionInfo(
            tailText = "Returns the current value inside the variable",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the current value inside the variable.<br /><br />
With three arguments, and when <code>variable</code> is a <code>PhelFuture</code>, blocks for<br />
  at most <code>timeout-ms</code> milliseconds waiting for the future to resolve. If<br />
  the future has not completed within the timeout, returns <code>timeout-val</code>.<br />
  The 3-arg form is only supported on <code>PhelFuture</code>.
""",
            example = "(deref (atom 42)) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L67",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "derive",
        signature = "(derive child parent)",
        completion = CompletionInfo(
            tailText = "Establishes a parent/child relationship between child and parent keywords in the global hierarchy",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Establishes a parent/child relationship between child and parent keywords<br />
  in the global hierarchy. Throws on cyclic derivation.
""",
            example = "(derive :square :shape)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L97",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "descendants",
        signature = "(descendants tag)",
        completion = CompletionInfo(
            tailText = "Returns the set of all descendants of tag, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the set of all descendants of tag, or nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L144",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L54",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "disj",
        signature = "(disj set)",
        completion = CompletionInfo(
            tailText = "Returns a new set that does not contain the given key(s)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new set that does not contain the given key(s). Works on hash-sets and sorted-sets.<br />
  Removing a non-existent key is a no-op. Returns <code>nil</code> when called on <code>nil</code>.
""",
            example = "(disj #{1 2 3} 2) ; =&gt; #{1 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L207",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "disj!",
        signature = "(disj! tcoll)",
        completion = CompletionInfo(
            tailText = "Removes one or more elements from a transient set, mutating it in place",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes one or more elements from a transient set, mutating it in place.<br />
   Raises <code>InvalidArgumentException</code> when <code>tcoll</code> is not a transient set.<br />
   Matches Clojure's <code>disj!</code> semantics.
""",
            example = "(persistent! (disj! (transient #{1 2 3}) 2)) ; =&gt; #{1 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transients.phel#L160",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L226",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dissoc!",
        signature = "(dissoc! tcoll)",
        completion = CompletionInfo(
            tailText = "Dissociates one or more keys from a transient map, mutating it in place",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Dissociates one or more keys from a transient map, mutating it in place.<br />
   Raises <code>InvalidArgumentException</code> when <code>tcoll</code> is not a transient map.<br />
   Matches Clojure's <code>dissoc!</code> semantics.
""",
            example = "(persistent! (dissoc! (transient {:a 1 :b 2}) :a)) ; =&gt; {:b 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transients.phel#L139",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L301",
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
Returns a lazy sequence with duplicated values removed in <code>coll</code>.<br />
  When called with no args, returns a transducer.
""",
            example = "(distinct [1 2 1 3 2 4 3]) ; =&gt; (1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L545",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "do",
        signature = "(do expr*)",
        completion = CompletionInfo(
            tailText = "Evaluates the expressions in order and returns the value of the last expression",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates the expressions in order and returns the value of the last expression. If no expression is given, nil is returned.
""",
            example = "(do (println \"Hello\") (+ 1 2)) ; prints \"Hello\", returns 3",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/control-flow/#statements-do",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L937",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dofor",
        signature = "(dofor head & body)",
        completion = CompletionInfo(
            tailText = "Repeatedly executes body for side effects with bindings and modifiers as provided by for",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Repeatedly executes body for side effects with bindings and modifiers as<br />
  provided by for. Returns nil.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L261",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L948",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "doseq",
        signature = "(doseq seq-exprs & body)",
        completion = CompletionInfo(
            tailText = "Repeatedly executes body for side effects with Clojure-style bindings",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Repeatedly executes body for side effects with Clojure-style bindings.<br />
  <code>(doseq [x coll] body)</code> runs <code>body</code> once per element of <code>coll</code>. When <code>coll</code><br />
  is a map, each iteration sees a <code>[k v]</code> entry pair, so destructuring works<br />
  just like in Clojure: <code>(doseq [[k v] m] ...)</code>. Supports <code>:when</code>, <code>:while</code>,<br />
  and <code>:let</code> modifiers between bindings.
""",
            example = "(doseq [x [1 2 3]] (println x))\n(doseq [[k v] {:a 1 :b 2}] (println k v))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L315",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "doseq-iterable",
        signature = "(doseq-iterable coll)",
        completion = CompletionInfo(
            tailText = "Internal helper used by the doseq macro expansion",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Internal helper used by the <code>doseq</code> macro expansion. Returns a value<br />
  suitable for Clojure-style iteration: maps are expanded to a sequence of<br />
  <code>[k v]</code> pair vectors so destructuring binds entries as in Clojure. Every<br />
  other value is returned unchanged.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L274",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dotimes",
        signature = "(dotimes [binding n] & body)",
        completion = CompletionInfo(
            tailText = "Evaluates body n times with binding bound to integers from 0 to n-1",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates body <code>n</code> times with <code>binding</code> bound to integers from 0 to n-1.<br />
  Returns nil.
""",
            example = "(dotimes [i 5] (println i))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/loops.phel#L17",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "doto",
        signature = "(doto x & forms)",
        completion = CompletionInfo(
            tailText = "Evaluates x then calls all of the methods and functions with the value of x supplied at the front...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates x then calls all of the methods and functions with the<br />
  value of x supplied at the front of the given arguments. The forms<br />
  are evaluated in order. Returns x.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L274",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "double",
        signature = "(double x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a double",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a double. In PHP there is no distinction between float and<br />
   double; both map to the same native PHP float type. Alias for <code>float</code>.
""",
            example = "(double 1) ; =&gt; 1.0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L252",
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
        name = "double?",
        signature = "(double? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a floating-point number, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a floating-point number, false otherwise.<br />
   Alias for <code>float?</code>, matching Clojure's <code>double?</code> naming. Since Phel<br />
   uses PHP floats (IEEE 754 doubles) there is no separate single-precision<br />
   float type.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L128",
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
Drops the first <code>n</code> elements of <code>coll</code>. Returns a lazy sequence.<br />
  When called with n only, returns a transducer.
""",
            example = "(drop 2 [1 2 3 4 5]) ; =&gt; (3 4 5)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L320",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "drop-last",
        signature = "(drop-last coll)",
        completion = CompletionInfo(
            tailText = "Drops the last n elements of coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Drops the last <code>n</code> elements of <code>coll</code>. <code>n</code> defaults to <code>1</code> when omitted,<br />
   matching Clojure's <code>(drop-last coll)</code> single-arity form. Returns an empty<br />
   sequence when <code>coll</code> is <code>nil</code>. Works with any seqable collection including<br />
   lazy sequences and ranges.
""",
            example = "(drop-last [1 2 3 4 5]) ; =&gt; (1 2 3 4)\n(drop-last 2 [1 2 3 4 5]) ; =&gt; (1 2 3)\n(drop-last 5 nil) ; =&gt; ()",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L343",
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
Drops all elements at the front of <code>coll</code> where <code>(pred x)</code> is true. Returns a lazy sequence.<br />
  When called with pred only, returns a transducer.
""",
            example = "(drop-while #(&lt; % 5) [1 2 3 4 5 6 3 2 1]) ; =&gt; (5 6 3 2 1)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L370",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "empty",
        signature = "(empty coll)",
        completion = CompletionInfo(
            tailText = "Returns an empty collection of the same category as coll, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns an empty collection of the same category as <code>coll</code>, or nil.
""",
            example = "(empty [1 2 3]) ; =&gt; []\n(empty {:a 1}) ; =&gt; {}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L325",
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
Returns true if x would be 0, "" or empty collection, false otherwise.<br />
  Safe on infinite/lazy sequences: checks the first element instead of counting.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L308",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "eval",
        signature = "(eval form)",
        completion = CompletionInfo(
            tailText = "Evaluates a form and return the evaluated results",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Evaluates a form and return the evaluated results.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L722",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "even?",
        signature = "(even? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is even",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is even.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L176",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L198",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-cause",
        signature = "(ex-cause ex)",
        completion = CompletionInfo(
            tailText = "Returns the cause of an exception, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the cause of an exception, or nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/exceptions.phel#L34",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-data",
        signature = "(ex-data ex)",
        completion = CompletionInfo(
            tailText = "Returns the data map from an ex-info exception, or nil if not an ExInfoException",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the data map from an ex-info exception, or nil if not an ExInfoException.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/exceptions.phel#L21",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-info",
        signature = "(ex-info msg data)",
        completion = CompletionInfo(
            tailText = "Creates an exception with a message and a data map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates an exception with a message and a data map. Optionally takes a cause.",
            example = "(throw (ex-info \"Invalid input\" {:field :email}))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/exceptions.phel#L12",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-message",
        signature = "(ex-message ex)",
        completion = CompletionInfo(
            tailText = "Returns the message of an exception",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the message of an exception.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/exceptions.phel#L28",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "extend-protocol",
        signature = "(extend-protocol protocol-name & specs)",
        completion = CompletionInfo(
            tailText = "Convenience macro that extends a single protocol to multiple types",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Convenience macro that extends a single protocol to multiple types.<br />
  Alternates type-specs and method implementations.<br /><br />
Equivalent to multiple <code>extend-type</code> calls.
""",
            example = "(extend-protocol Describable\n  :string (describe [s] s)\n  :int (describe [n] (str n)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L342",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "extend-type",
        signature = "(extend-type type-spec & specs)",
        completion = CompletionInfo(
            tailText = "Extends a type with protocol method implementations",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Extends a type with protocol method implementations.<br /><br />
type-spec can be:<br />
  - <code>nil</code> for the nil type<br />
  - a type keyword matching what <code>type</code> returns: <code>:string</code>, <code>:int</code>, <code>:float</code>,<br />
    <code>:boolean</code>, <code>:keyword</code>, <code>:symbol</code>, <code>:vector</code>, <code>:list</code>, <code>:hash-map</code>, <code>:set</code>,<br />
    <code>:var</code>, <code>:function</code>, <code>:php/array</code><br />
  - a symbol for struct names (resolved in current namespace)<br />
  - a string for explicit PHP class names (cross-namespace structs)<br /><br />
Note: <code>:struct</code> and <code>:php/object</code> cannot be used as type-specs because<br />
  protocol dispatch resolves these to their specific PHP class names.<br />
  Use a struct symbol or PHP class name string instead.
""",
            example = "(extend-type :string Stringable (to-string [s] s))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L252",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "extends?",
        signature = "(extends? protocol type-key)",
        completion = CompletionInfo(
            tailText = "Returns true if the given type-key has implementations for all methods of the protocol",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if the given type-key has implementations for all methods<br />
  of the protocol. type-key should match what protocol-type-key returns.
""",
            example = "(extends? Stringable :string)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L331",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "extreme",
        signature = "(extreme order args)",
        completion = CompletionInfo(
            tailText = "Returns the most extreme value in args based on the binary order function",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the most extreme value in <code>args</code> based on the binary <code>order</code> function.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L351",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L257",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ffirst",
        signature = "(ffirst coll)",
        completion = CompletionInfo(
            tailText = "Same as (first (first coll))",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Same as <code>(first (first coll))</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-basics.phel#L41",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "file-seq",
        signature = "(file-seq path)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of all files and directories in a directory tree",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of all files and directories in a directory tree.",
            example = "(filter #(php/str_ends_with % \".phel\") (file-seq \"src/\")) ; =&gt; [\"src/file1.phel\" \"src/file2.phel\" ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L137",
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
Returns a lazy sequence of elements where predicate returns true.<br />
  When called with pred only, returns a transducer.
""",
            example = "(filter even? [1 2 3 4 5 6]) ; =&gt; (2 4 6)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L458",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "finally",
        signature = "(finally expr*)",
        completion = CompletionInfo(
            tailText = "Evaluate expressions after the try body and all matching catches have completed",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluate expressions after the try body and all matching catches have completed. The finally block runs regardless of whether an exception was thrown.
""",
            example = "(defn risky-operation [] (throw (php/new \\Exception \"Error!\")))\n(defn cleanup [] (println \"Cleanup!\"))\n(try (risky-operation) (catch \\Exception e nil) (finally (cleanup)))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/control-flow/#try-catch-and-finally",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "find",
        signature = "(find pred coll)",
        completion = CompletionInfo(
            tailText = "Returns the first item in coll where (pred item) evaluates to true",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the first item in <code>coll</code> where <code>(pred item)</code> evaluates to true.
""",
            example = "(find #(&gt; % 5) [1 2 3 6 7 8]) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L523",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "find-hierarchy-method",
        signature = "(find-hierarchy-method methods dispatch-val)",
        completion = CompletionInfo(
            tailText = "Finds the best matching method for dispatch-val using the global hierarchy",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Finds the best matching method for dispatch-val using the global hierarchy.<br />
  Returns the method function or nil. Used internally by defmulti.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L159",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L533",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "first",
        signature = "",
        completion = CompletionInfo(
            tailText = "Returns the first element of a sequence, or nil if empty",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the first element of a sequence, or nil if empty.<br /><br />
Maps are treated as a sequence of entries: <code>(first {:a 1})</code> returns the<br />
  first <code>[:a 1]</code> vector. Strings are treated as sequences of multibyte<br />
  characters.
""",
            example = "(first [1 2 3]) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L95",
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
            example = "(flatten [[1 2] [3 [4 5]] 6]) ; =&gt; (1 2 3 4 5 6)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L233",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "float",
        signature = "(float x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a float",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a float. In PHP there is no distinction between float and<br />
   double; both map to the same native PHP float type. Delegates to PHP's<br />
   <code>floatval</code>, so non-numeric strings return <code>0.0</code> and <code>nil</code> returns <code>0.0</code>.
""",
            example = "(float 1) ; =&gt; 1.0",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L243",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L70",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "fn",
        signature = "(fn [params*] expr*)",
        completion = CompletionInfo(
            tailText = "Defines a function",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a function. A function consists of a list of parameters and a list of expression. The value of the last expression is returned as the result of the function. All other expression are only evaluated for side effects. If no expression is given, the function returns nil.
""",
            example = "(fn [x y] (+ x y))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/functions-and-recursion/#anonymous-function-fn",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L206",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "fnext",
        signature = "(fnext coll)",
        completion = CompletionInfo(
            tailText = "Same as (first (next coll))",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Same as <code>(first (next coll))</code>.
""",
            example = "(fnext [1 2 3]) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-basics.phel#L69",
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
Returns a function that replaces nil arguments with the provided defaults<br />
  before calling f. The number of defaults determines how many leading arguments<br />
  are nil-checked.
""",
            example = "(let [safe-inc (fnil inc 0)] (safe-inc nil)) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L130",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "for",
        signature = "(for head & body)",
        completion = CompletionInfo(
            tailText = "List comprehension",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
List comprehension. The head of the loop is a vector that contains a<br />
  sequence of bindings modifiers and options. A binding is a sequence of three<br />
  values <code>binding :verb expr</code>. Where <code>binding</code> is a binding as<br />
  in let and <code>:verb</code> is one of the following keywords:<br /><br />
<em> <code>:range</code> loop over a range by using the range function.<br />
  </em> <code>:in</code> loops over all values of a collection (including strings).<br />
  <em> <code>:keys</code> loops over all keys/indexes of a collection.<br />
  </em> <code>:pairs</code> loops over all key-value pairs of a collection.<br /><br />
After each loop binding, additional modifiers can be applied. Modifiers<br />
  have the form <code>:modifier argument</code>. The following modifiers are supported:<br /><br />
<em> <code>:while</code> breaks the loop if the expression is falsy.<br />
  </em> <code>:let</code> defines additional bindings.<br />
  <em> <code>:when</code> only evaluates the loop body if the condition is true.<br /><br />
Finally, additional options can be set:<br /><br />
</em> <code>:reduce [accumulator initial-value]</code> Instead of returning a list,<br />
     it reduces the values into <code>accumulator</code>. Initially <code>accumulator</code><br />
     is bound to <code>initial-value</code>.
""",
            example = "(for [x :in [1 2 3]] (* x 2)) ; =&gt; [2 4 6]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L221",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "force",
        signature = "(force x)",
        completion = CompletionInfo(
            tailText = "If x is a Delay, forces it and returns its cached value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "If x is a Delay, forces it and returns its cached value. Otherwise returns x.",
            example = "(force (delay 42)) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/lazy.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "foreach",
        signature = "(foreach [value valueExpr] expr*)",
        completion = CompletionInfo(
            tailText = "The foreach special form can be used to iterate over all kind of PHP datastructures",
            priority = PhelCompletionPriority.CONTROL_FLOW,
        ),
        documentation = DocumentationInfo(
            summary = """
The foreach special form can be used to iterate over all kind of PHP datastructures. The return value of foreach is always nil. The loop special form should be preferred of the foreach special form whenever possible.
""",
            example = "(foreach [x [1 2 3]] (println x))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/control-flow/#foreach",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "format",
        signature = "(format fmt & xs)",
        completion = CompletionInfo(
            tailText = "Returns a formatted string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a formatted string. See PHP's <a href="https://www.php.net/manual/en/function.sprintf.php">sprintf</a> for more information.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L57",
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
            example = "(frequencies [:a :b :a :c :b :a]) ; =&gt; {:a 3 :b 2 :c 1}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L600",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "full-name",
        signature = "(full-name x)",
        completion = CompletionInfo(
            tailText = "Return the namespace and name string of a string, keyword or symbol",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Return the namespace and name string of a string, keyword or symbol.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L708",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L220",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "gensym",
        signature = "(gensym)",
        completion = CompletionInfo(
            tailText = "Generates a new unique symbol",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generates a new unique symbol.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/strings.phel#L25",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L57",
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
Returns <code>opt</code> (default nil) if the path doesn't exist.
""",
            example = "(get-in {:a {:b {:c 42}}} [:a :b :c]) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L256",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "get-validator",
        signature = "(get-validator variable)",
        completion = CompletionInfo(
            tailText = "Returns the validator function of a variable, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the validator function of a variable, or nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L139",
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
            example = "(group-by count [\"a\" \"bb\" \"c\" \"ddd\" \"ee\"]) ; =&gt; {1 [\"a\" \"c\"] 2 [\"bb\" \"ee\"] 3 [\"ddd\"]}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L969",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "hash-map",
        signature = "(hash-map & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new hash map",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new hash map. If no argument is provided, an empty hash map is created. The number of parameters must be even.
""",
            example = "(hash-map :name \"Alice\" :age 30) ; =&gt; {:name \"Alice\" :age 30}",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/data-structures/#maps",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L238",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "hash-set",
        signature = "(hash-set & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new Set from the given arguments",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new Set from the given arguments. Shortcut: #{}",
            example = "(hash-set 1 2 3) ; =&gt; #{1 2 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/collections.phel#L15",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L93",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L165",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L83",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "identity",
        signature = "(identity x)",
        completion = CompletionInfo(
            tailText = "Returns its argument",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns its argument.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L326",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "if",
        signature = "(if test then else?)",
        completion = CompletionInfo(
            tailText = "A control flow structure",
            priority = PhelCompletionPriority.CONTROL_FLOW,
        ),
        documentation = DocumentationInfo(
            summary = """
A control flow structure. First evaluates test. If test evaluates to true, only the then form is evaluated and the result is returned. If test evaluates to false only the else form is evaluated and the result is returned. If no else form is given, nil will be returned.
""",
            example = "(if (&gt; x 0) \"positive\" \"non-positive\")",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/control-flow/#if",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "if-let",
        signature = "(if-let bindings then & [else])",
        completion = CompletionInfo(
            tailText = "If test is true, evaluates then with binding-form bound to the value of test, if not, yields else",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
If test is true, evaluates then with binding-form bound to the value of test,<br />
  if not, yields else
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L554",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "if-not",
        signature = "(if-not test then & [else])",
        completion = CompletionInfo(
            tailText = "Evaluates then if test is false, else otherwise",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Evaluates then if test is false, else otherwise.",
            example = "(if-not (&lt; 5 3) \"not less\" \"less\") ; =&gt; \"not less\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L13",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "if-some",
        signature = "(if-some bindings then & [else])",
        completion = CompletionInfo(
            tailText = "Binds name to the value of test",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Binds name to the value of test. If test is not nil, evaluates then with binding-form<br />
  bound to the value of test, if not, yields else. Unlike if-let, false and 0 are not<br />
  treated as falsy — only nil triggers the else branch.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L594",
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
Returns true if <code>x</code> can be invoked as a function.<br />
   This includes functions, keywords, maps, vectors, sets, and lists.
""",
            example = "(ifn? inc) ; =&gt; true\n(ifn? :a) ; =&gt; true\n(ifn? {}) ; =&gt; true\n(ifn? 42) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L211",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "inc",
        signature = "(inc x)",
        completion = CompletionInfo(
            tailText = "Increments x by one",
            priority = PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Increments <code>x</code> by one.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L162",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L372",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "inf?",
        signature = "(inf? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is infinite",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is infinite.
""",
            example = "(inf? php/INF) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L217",
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
  <code>\DateTime</code> or a <code>:use</code>d short name; for runtime class names use<br />
  <code>(php/is_a x class-name)</code>.
""",
            example = "(instance? \\DateTime (php/new \\DateTime)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L276",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "int",
        signature = "(int x)",
        completion = CompletionInfo(
            tailText = "Coerces x to an integer",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to an integer. Delegates to PHP's <code>intval</code>, so floats are<br />
   truncated toward zero, numeric strings are parsed, and <code>nil</code> returns <code>0</code>.
""",
            example = "(int 1.9) ; =&gt; 1\n(int \"42\") ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L235",
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
        name = "int?",
        signature = "(int? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is an integer number, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is an integer number, false otherwise.<br />
   Alias for <code>integer?</code>.<br /><br />
Note that, unlike Clojure, Phel uses PHP integers and there is no<br />
   distinction between standard and fixed-precision integers.<br />
   Integer sizes are also limited by platform (see php/PHP_INT_MAX constant).
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L81",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "integer?",
        signature = "(integer? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is an integer number, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is an integer number, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L75",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "interleave",
        signature = "(interleave & colls)",
        completion = CompletionInfo(
            tailText = "Interleaves multiple collections",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Interleaves multiple collections. Returns a lazy sequence.<br /><br />
Returns elements by taking one from each collection in turn.<br />
  Pads with nil when collections have different lengths.<br />
  Works with infinite sequences.
""",
            example = "(interleave [1 2 3] [:a :b :c]) ; =&gt; (1 :a 2 :b 3 :c)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L919",
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
Returns elements separated by a separator. Returns a lazy sequence.<br />
  When called with sep only, returns a transducer.
""",
            example = "(interpose 0 [1 2 3]) ; =&gt; (1 0 2 0 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L872",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L35",
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
When <code>from</code> is associative, it is treated as a sequence of key-value pairs.<br />
  Supports persistent and transient collections.
""",
            example = "(into [] '(1 2 3)) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L86",
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
            example = "(invert {:a 1 :b 2 :c 3}) ; =&gt; {1 :a 2 :b 3 :c}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1046",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "isa?",
        signature = "(isa? child parent)",
        completion = CompletionInfo(
            tailText = "Returns true if child equals parent, or child is a descendant of parent in the global hierarchy",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if child equals parent, or child is a descendant of parent<br />
  in the global hierarchy.
""",
            example = "(do (derive :square :shape) (isa? :square :shape)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L88",
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
            example = "(take 5 (iterate inc 0)) ; =&gt; (0 1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L812",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "iteration",
        signature = "(iteration step opts)",
        completion = CompletionInfo(
            tailText = "Creates a lazy sequence from successive calls to step",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a lazy sequence from successive calls to <code>step</code>.<br />
  <code>step</code> is called with a key (starting with <code>:initk</code>) and returns a result.<br />
  <code>:kf</code> extracts the next key, <code>:vf</code> extracts the value from the result.<br />
  Terminates when the result is nil.<br /><br />
Options map keys:<br />
    :kf     — key function (default: identity)<br />
    :vf     — value function (default: identity)<br />
    :initk  — initial key (default: nil)
""",
            example = "(iteration fetch-page {:kf :next-token :vf :items :initk nil})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/lazy.phel#L37",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L113",
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
Returns a lazy sequence of non-nil results of applying function to elements.<br />
  When called with f only, returns a transducer.
""",
            example = "(keep #(when (even? %) (* % %)) [1 2 3 4 5]) ; =&gt; (4 16)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L485",
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
Returns a lazy sequence of non-nil results of <code>(pred i x)</code>.<br />
  When called with f only, returns a transducer.
""",
            example = "(keep-indexed #(when (even? %1) %2) [\"a\" \"b\" \"c\" \"d\"]) ; =&gt; (\"a\" \"c\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L503",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "key",
        signature = "(key entry)",
        completion = CompletionInfo(
            tailText = "Returns the key of a map entry (a two-element vector [key value])",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the key of a map entry (a two-element vector <code>[key value]</code>).
""",
            example = "(key (first (pairs {:a 1}))) ; =&gt; :a",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L629",
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
Returns a sequence of all keys in a map, or <code>nil</code> when the map is <code>nil</code><br />
  or empty.
""",
            example = "(keys {:a 1 :b 2}) ; =&gt; (:a :b)\n(keys nil) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L613",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L56",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L155",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L659",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L354",
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
            example = "(lazy-cat [1 2] [3 4]) ; =&gt; (1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L806",
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
            example = "(lazy-seq (cons 1 (lazy-seq nil))) ; =&gt; (1)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L797",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "let",
        signature = "(let [bindings*] expr*)",
        completion = CompletionInfo(
            tailText = "Creates a new lexical context with assignments defined in bindings",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new lexical context with assignments defined in bindings. Afterwards the list of expressions is evaluated and the value of the last expression is returned. If no expression is given nil is returned.
""",
            example = "(let [x 1 y 2] (+ x y)) ; =&gt; 3",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/global-and-local-bindings/#local-bindings-let",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "letfn",
        signature = "(letfn bindings & body)",
        completion = CompletionInfo(
            tailText = "Defines mutually recursive local functions",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines mutually recursive local functions.<br /><br />
bindings is a vector of function specs: (letfn [(f [params] body) (g [params] body)] expr)<br />
  All function names are in scope within all function bodies and the body expression,<br />
  enabling mutual recursion.
""",
            example = "(letfn [(my-even? [n] (if (zero? n) true (my-odd? (dec n))))\n        (my-odd? [n] (if (zero? n) false (my-even? (dec n))))]\n  (my-even? 10))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L646",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "line-seq",
        signature = "(line-seq filename)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of lines from a file",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of lines from a file.",
            example = "(take 10 (line-seq \"large-file.txt\")) ; =&gt; [\"line1\" \"line2\" ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L125",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "list",
        signature = "(list & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new list",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new list. If no argument is provided, an empty list is created.",
            example = "(list 1 2 3) ; =&gt; '(1 2 3)",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/data-structures/#lists",
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
Returns true if <code>x</code> is a list, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L250",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "long",
        signature = "(long x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a long integer",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a long integer. In PHP there is no distinction between int<br />
   and long; both map to the same native PHP int type. Alias for <code>int</code>.
""",
            example = "(long 1.9) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L260",
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
        name = "loop",
        signature = "(loop [bindings*] expr*)",
        completion = CompletionInfo(
            tailText = "Creates a new lexical context with variables defined in bindings and defines a recursion point at...",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new lexical context with variables defined in bindings and defines a recursion point at the top of the loop.
""",
            example = "(loop [i 0] (if (&lt; i 5) (do (println i) (recur (inc i)))))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/control-flow/#loop",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "macroexpand",
        signature = "(macroexpand form)",
        completion = CompletionInfo(
            tailText = "Recursively expands the given form until it is no longer a macro call",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Recursively expands the given form until it is no longer a macro call.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/macroexpand.phel#L32",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "macroexpand-1",
        signature = "(macroexpand-1 form)",
        completion = CompletionInfo(
            tailText = "Expands the given form once if it is a macro call",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Expands the given form once if it is a macro call.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/macroexpand.phel#L12",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "make-hierarchy",
        signature = "(make-hierarchy)",
        completion = CompletionInfo(
            tailText = "Creates a fresh, empty hierarchy",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a fresh, empty hierarchy.<br /><br />
Returns a map with <code>:parents</code>, <code>:descendants</code>, and <code>:ancestors</code> keys, each<br />
  holding an empty map. Matches Clojure's hierarchy shape so consumers can<br />
  destructure any of the three relationship views.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L51",
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
  With multiple collections, applies the function to corresponding elements from each collection,<br />
  stopping when the shortest collection is exhausted.
""",
            example = "(map inc [1 2 3]) ; =&gt; (2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L52",
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
Applies <code>f</code> to each element in <code>xs</code>. <code>f</code> is a two-argument function where<br />
  the first argument is the index (0-based) and the second is the element itself.<br />
  Works with infinite sequences.
""",
            example = "(map-indexed vector [:a :b :c]) ; =&gt; ([0 :a] [1 :b] [2 :c])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L902",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L232",
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
            example = "(mapcat reverse [[1 2] [3 4]]) ; =&gt; (2 1 4 3)\n(mapcat list [:a :b :c] [1 2 3]) ; =&gt; (:a 1 :b 2 :c 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L849",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "max",
        signature = "(max & numbers)",
        completion = CompletionInfo(
            tailText = "Returns the numeric maximum of all numbers",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the numeric maximum of all numbers.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L361",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "max-key",
        signature = "(max-key k x & more)",
        completion = CompletionInfo(
            tailText = "Returns the arg for which (k arg) is largest",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the arg for which (k arg) is largest. On ties, returns the latest argument, matching Clojure semantics.
""",
            example = "(max-key count \"bb\" \"aaa\" \"b\") ; =&gt; \"aaa\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L373",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "mean",
        signature = "(mean xs)",
        completion = CompletionInfo(
            tailText = "Returns the mean of xs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the mean of <code>xs</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L392",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "median",
        signature = "(median xs)",
        completion = CompletionInfo(
            tailText = "Returns the median of xs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the median of <code>xs</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L397",
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
Returns a memoized version of the function <code>f</code>. The memoized function<br />
  caches the return value for each set of arguments.
""",
            example = "(defn fact [n]\n  (if (zero? n)\n    1\n    (* n (fact (dec n)))))\n(def fact-memo (memoize fact))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L149",
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
Returns a memoized version of the function <code>f</code> with an LRU (Least Recently Used)<br />
  cache limited to <code>max-size</code> entries. When the cache exceeds <code>max-size</code>, the<br />
  least recently used entry is evicted. This prevents unbounded memory growth<br />
  in long-running processes.<br /><br />
Without arguments, uses a default cache size of 128 entries.
""",
            example = "(defn fact [n]\n  (if (zero? n)\n    1\n    (* n (fact (dec n)))))\n(def fact-memo (memoize-lru fact 100))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L169",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "merge",
        signature = "(merge & maps)",
        completion = CompletionInfo(
            tailText = "Merges multiple maps into one new map",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Merges multiple maps into one new map.<br /><br />
If a key appears in more than one collection, later values replace previous ones.
""",
            example = "(merge {:a 1 :b 2} {:b 3 :c 4}) ; =&gt; {:a 1 :b 3 :c 4}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1009",
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
Merges multiple maps into one new map. If a key appears in more than one<br />
   collection, the result of <code>(f current-val next-val)</code> is used.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L249",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "meta",
        signature = "",
        completion = CompletionInfo(
            tailText = "Gets the metadata attached to a value",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Gets the metadata attached to a value.<br />
  For a quoted symbol (<code>(meta 'foo)</code>) the definition metadata registered via <code>def</code> is returned.<br />
  For any other expression the value is looked up at runtime and its <code>MetaInterface</code> metadata returned.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/meta.phel#L23",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "min",
        signature = "(min & numbers)",
        completion = CompletionInfo(
            tailText = "Returns the numeric minimum of all numbers",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the numeric minimum of all numbers.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L356",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "min-key",
        signature = "(min-key k x & more)",
        completion = CompletionInfo(
            tailText = "Returns the arg for which (k arg) is smallest",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the arg for which (k arg) is smallest. On ties, returns the latest argument, matching Clojure semantics.
""",
            example = "(min-key count \"bb\" \"aaa\" \"b\") ; =&gt; \"b\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L366",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "name",
        signature = "(name x)",
        completion = CompletionInfo(
            tailText = "Returns the name string of a string, keyword or symbol",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the name string of a string, keyword or symbol.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L698",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "namespace",
        signature = "(namespace x)",
        completion = CompletionInfo(
            tailText = "Return the namespace string of a symbol or keyword",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Return the namespace string of a symbol or keyword. Nil if not present.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L703",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nan?",
        signature = "(nan? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is not a number",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is not a number.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L206",
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
Returns true if <code>x</code> is a non-negative integer (zero or positive).
""",
            example = "(nat-int? 0) ; =&gt; true\n(nat-int? 1) ; =&gt; true\n(nat-int? -1) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L106",
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
Returns true if <code>x</code> is a negative integer.
""",
            example = "(neg-int? -1) ; =&gt; true\n(neg-int? 0) ; =&gt; false\n(neg-int? 1) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L92",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "neg?",
        signature = "(neg? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is smaller than zero",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is smaller than zero.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L201",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "next",
        signature = "",
        completion = CompletionInfo(
            tailText = "Returns the sequence after the first element, or nil if empty",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the sequence after the first element, or nil if empty.",
            example = "(next [1 2 3]) ; =&gt; [2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L58",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nfirst",
        signature = "(nfirst coll)",
        completion = CompletionInfo(
            tailText = "Same as (next (first coll))",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Same as <code>(next (first coll))</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-basics.phel#L64",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L263",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nnext",
        signature = "(nnext coll)",
        completion = CompletionInfo(
            tailText = "Same as (next (next coll))",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Same as <code>(next (next coll))</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-basics.phel#L76",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L111",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L229",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L318",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L209",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L117",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ns",
        signature = "(ns name imports*)",
        completion = CompletionInfo(
            tailText = "Defines the namespace for the current file and adds imports to the environment",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines the namespace for the current file and adds imports to the environment. Imports can either be uses or requires. The keyword :use is used to import PHP classes, the keyword :require is used to import Phel modules and the keyword :require-file is used to load php files.
""",
            example = "(ns my-app\\core (:require phel\\string :as str))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/namespaces/#namespace-ns",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nth",
        signature = "(nth coll index)",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L87",
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
Returns the nth next of <code>coll</code>, <code>(seq coll)</code> when <code>n</code> is 0.<br />
   Returns nil if there are fewer than <code>n</code> elements remaining.
""",
            example = "(nthnext [1 2 3 4 5] 2) ; =&gt; (3 4 5)\n(nthnext [1 2] 5) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L150",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "nthrest",
        signature = "(nthrest coll n)",
        completion = CompletionInfo(
            tailText = "Returns the nth rest of coll, coll when n is 0",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the nth rest of <code>coll</code>, <code>coll</code> when <code>n</code> is 0.
""",
            example = "(nthrest [1 2 3 4 5] 2) ; =&gt; (3 4 5)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L139",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "number?",
        signature = "(number? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a number, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a number, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L113",
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
        name = "odd?",
        signature = "(odd? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is odd",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is odd.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L181",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "one?",
        signature = "(one? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is one",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is one.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L191",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L34",
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
            example = "(pairs {:a 1 :b 2}) ; =&gt; ([:a 1] [:b 2])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L651",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "parents",
        signature = "(parents tag)",
        completion = CompletionInfo(
            tailText = "Returns the set of immediate parents of tag in the global hierarchy, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the set of immediate parents of tag in the global hierarchy, or nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L130",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "parse-boolean",
        signature = "(parse-boolean s)",
        completion = CompletionInfo(
            tailText = "Parses a string as a boolean",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Parses a string as a boolean. Returns true for "true", false for "false", nil otherwise.
""",
            example = "(parse-boolean \"true\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/parsing.phel#L36",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "parse-double",
        signature = "(parse-double s)",
        completion = CompletionInfo(
            tailText = "Parses a string as a float",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Parses a string as a float. Returns <code>nil</code> for non-numeric input or for<br />
  inputs that are not strings. Accepts <code>Infinity</code>, <code>-Infinity</code>, and <code>NaN</code><br />
  alongside regular decimal and scientific notation.
""",
            example = "(parse-double \"3.14\") ; =&gt; 3.14\n(parse-double \"Infinity\") ; =&gt; INF",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/parsing.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "parse-long",
        signature = "(parse-long s)",
        completion = CompletionInfo(
            tailText = "Parses a string as an integer",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Parses a string as an integer. Returns nil if parsing fails.",
            example = "(parse-long \"123\") ; =&gt; 123",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/parsing.phel#L10",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "parse-uuid",
        signature = "(parse-uuid s)",
        completion = CompletionInfo(
            tailText = "Parses s as a canonical UUID string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Parses <code>s</code> as a canonical UUID string. Returns the lower-cased UUID<br />
  string if valid, or nil otherwise. Since PHP has no UUID type, UUIDs<br />
  are returned as strings.
""",
            example = "(parse-uuid \"550E8400-E29B-41D4-A716-446655440000\")\n  ; =&gt; \"550e8400-e29b-41d4-a716-446655440000\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L43",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "partial",
        signature = "(partial f & args)",
        completion = CompletionInfo(
            tailText = "Takes a function f and fewer than normal arguments of f and returns a function that a variable nu...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a function <code>f</code> and fewer than normal arguments of <code>f</code> and returns a function<br />
  that a variable number of additional arguments. When call <code>f</code> will be called<br />
  with <code>args</code> and the additional arguments.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L123",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "partition",
        signature = "(partition n coll)",
        completion = CompletionInfo(
            tailText = "Partitions collection into chunks of size n, dropping incomplete final partition",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Partitions collection into chunks of size n, dropping incomplete final partition.
""",
            example = "(partition 3 [1 2 3 4 5 6 7]) ; =&gt; ([1 2 3] [4 5 6])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1121",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "partition-all",
        signature = "(partition-all n coll)",
        completion = CompletionInfo(
            tailText = "Partitions collection into chunks of size n, including incomplete final partition",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Partitions collection into chunks of size n, including incomplete final partition.
""",
            example = "(partition-all 3 [1 2 3 4 5 6 7]) ; =&gt; ([1 2 3] [4 5 6] [7])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1133",
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
            example = "(partition-by #(&lt; % 3) [1 2 3 4 5 1 2]) ; =&gt; [[1 2] [3 4 5] [1 2]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1071",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "peek",
        signature = "(peek coll)",
        completion = CompletionInfo(
            tailText = "Returns the last element of a sequence, or nil if empty or nil",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the last element of a sequence, or nil if empty or nil.<br />
  Works on vectors, PHP arrays, lists, and lazy sequences.
""",
            example = "(peek [1 2 3]) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L23",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transients.phel#L27",
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
Converts a transient collection back to a persistent collection.<br />
   Alias for <code>persistent</code>, matching Clojure's <code>persistent!</code> naming.
""",
            example = "(persistent! (conj! (transient []) 1 2 3)) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transients.phel#L34",
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
            example = "(phel-&gt;php {:a [1 2 3] :b {:c 4}}) ; =&gt; (PHP array [\"a\" =&gt; [1, 2, 3], \"b\" =&gt; [\"c\" =&gt; 4]])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L681",
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
            example = "(php-&gt;phel (php-associative-array \"a\" 1 \"b\" 2)) ; =&gt; {\"a\" 1 \"b\" 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L713",
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
            example = "(php-array-to-map (php-associative-array \"a\" 1 \"b\" 2)) ; =&gt; {\"a\" 1 \"b\" 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L670",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L287",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L297",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L292",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "pop",
        signature = "(pop coll)",
        completion = CompletionInfo(
            tailText = "Removes the last element of the array coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes the last element of the array <code>coll</code>. If the array is empty returns nil.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L49",
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
Removes the last element from a transient vector, mutating it in place.<br />
   Raises <code>InvalidArgumentException</code> when <code>tcoll</code> is not a transient vector.<br />
   Matches Clojure's <code>pop!</code> semantics.
""",
            example = "(persistent! (pop! (transient [1 2 3]))) ; =&gt; [1 2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transients.phel#L171",
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
Returns true if <code>x</code> is a positive integer (greater than zero).
""",
            example = "(pos-int? 1) ; =&gt; true\n(pos-int? 0) ; =&gt; false\n(pos-int? -1) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L99",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "pos?",
        signature = "(pos? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is greater than zero",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is greater than zero.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L196",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "print",
        signature = "(print & xs)",
        completion = CompletionInfo(
            tailText = "Prints the given values to the default output stream",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Prints the given values to the default output stream. Returns nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L44",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "print-str",
        signature = "(print-str & xs)",
        completion = CompletionInfo(
            tailText = "Same as print",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Same as print. But instead of writing it to an output stream, the resulting string is returned.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "printf",
        signature = "(printf fmt & xs)",
        completion = CompletionInfo(
            tailText = "Output a formatted string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Output a formatted string. See PHP's <a href="https://www.php.net/manual/en/function.printf.php">printf</a> for more information.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L62",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "println",
        signature = "(println & xs)",
        completion = CompletionInfo(
            tailText = "Same as print followed by a newline",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Same as print followed by a newline.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L50",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "protocol-type-key",
        signature = "(protocol-type-key x)",
        completion = CompletionInfo(
            tailText = "Returns the dispatch key for protocol dispatch",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the dispatch key for protocol dispatch. Returns a type keyword<br />
  for primitive types, or the PHP class name string for objects/structs.<br /><br />
Optimized to avoid the full <code>type</code> cond chain: checks scalars first<br />
  (most common in tight loops), then objects.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L191",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L42",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L203",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L277",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "quote",
        signature = "(quote form)",
        completion = CompletionInfo(
            tailText = "Returns the unevaluated form",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the unevaluated form.",
            example = "(quote (+ 1 2)) ; =&gt; '(+ 1 2)",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/macros/#quote",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rand",
        signature = "(rand)",
        completion = CompletionInfo(
            tailText = "Returns a random number between 0 and 1",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a random number between 0 and 1.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L336",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rand-int",
        signature = "(rand-int n)",
        completion = CompletionInfo(
            tailText = "Returns a random number between 0 and n",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a random number between 0 and <code>n</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L341",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rand-nth",
        signature = "(rand-nth xs)",
        completion = CompletionInfo(
            tailText = "Returns a random item from xs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a random item from xs.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L346",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "random-uuid",
        signature = "(random-uuid)",
        completion = CompletionInfo(
            tailText = "Returns a random UUID v4 string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a random UUID v4 string.",
            example = "(random-uuid) ; =&gt; \"550e8400-e29b-41d4-a716-446655440000\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L25",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "range",
        signature = "(range & args)",
        completion = CompletionInfo(
            tailText = "Creates a lazy sequence of numbers",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a lazy sequence of numbers. With no arguments returns an infinite<br />
  sequence starting at 0. With one argument returns (0..n). With two (start..end).<br />
  With three (start..end step). Note: the infinite sequence is bounded by PHP_INT_MAX.
""",
            example = "(range 5) ; =&gt; (0 1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L158",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ratio?",
        signature = "(ratio? _)",
        completion = CompletionInfo(
            tailText = "Always returns false",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Always returns false. Phel has no Ratio type — Clojure-style ratio<br />
  literals like <code>1/2</code> are accepted by the reader but evaluate to floats<br />
  (<code>num / den</code>). Provided for <code>.cljc</code> interop so cross-host code can<br />
  call <code>ratio?</code> without compilation errors.
""",
            example = "(ratio? 1/2) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L118",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "re-find",
        signature = "(re-find re s)",
        completion = CompletionInfo(
            tailText = "Returns the first match of pattern in string, or nil if no match",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the first match of pattern in string, or nil if no match.<br />
  If the pattern has groups, returns a vector of [full-match group1 group2 ...].
""",
            example = "(re-find #\"\\d+\" \"abc123def\") ; =&gt; \"123\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L351",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "re-matches",
        signature = "(re-matches re s)",
        completion = CompletionInfo(
            tailText = "Returns the match, if any, of string to pattern",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the match, if any, of string to pattern. If the pattern has groups,<br />
  returns a vector of [full-match group1 group2 ...]. Returns nil if no match.<br />
  Unlike re-find, the entire string must match.
""",
            example = "(re-matches #\"(\\d+)-(\\d+)\" \"12-34\") ; =&gt; [\"12-34\" \"12\" \"34\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L365",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "re-pattern",
        signature = "(re-pattern s)",
        completion = CompletionInfo(
            tailText = "Returns a PCRE pattern string from s",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a PCRE pattern string from <code>s</code>. If <code>s</code> is already delimited,<br />
  returns it as-is. Otherwise wraps in <code>/</code> delimiters.
""",
            example = "(re-pattern \"\\\\d+\") ; =&gt; \"/\\\\d+/\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L326",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "re-seq",
        signature = "(re-seq re s)",
        completion = CompletionInfo(
            tailText = "Returns a sequence of successive matches of pattern in string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a sequence of successive matches of pattern in string.",
            example = "(re-seq #\"\\d+\" \"a1b2c3\") ; =&gt; [\"1\" \"2\" \"3\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L339",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "read-file-lazy",
        signature = "(read-file-lazy filename)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of byte chunks from a file",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of byte chunks from a file.",
            example = "(take 5 (read-file-lazy \"large-file.bin\" 1024)) ; =&gt; [\"chunk1\" \"chunk2\" ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L149",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "read-string",
        signature = "(read-string s)",
        completion = CompletionInfo(
            tailText = "Reads the first phel expression from the string s",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Reads the first phel expression from the string s.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L713",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "realized?",
        signature = "(realized? coll)",
        completion = CompletionInfo(
            tailText = "Returns true if a lazy sequence, delay, or future has been realized, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if a lazy sequence, delay, or future has been realized, false otherwise.
""",
            example = "(realized? (take 5 (iterate inc 1))) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L958",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "recur",
        signature = "(recur expr*)",
        completion = CompletionInfo(
            tailText = "Internally recur is implemented as a PHP while loop and therefore prevents the Maximum function n...",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Internally recur is implemented as a PHP while loop and therefore prevents the Maximum function nesting level errors.
""",
            example = "(loop [n 5 acc 1] (if (&lt;= n 1) acc (recur (dec n) (* acc n))))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/functions-and-recursion/#recursion",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reduce",
        signature = "(reduce f & args)",
        completion = CompletionInfo(
            tailText = "Reduces collection to a single value by repeatedly applying function to accumulator and elements",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reduces collection to a single value by repeatedly applying function to accumulator and elements.<br />
  Respects early termination via <code>(reduced val)</code>.
""",
            example = "(reduce + [1 2 3 4]) ; =&gt; 10",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L50",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reduced",
        signature = "(reduced x)",
        completion = CompletionInfo(
            tailText = "Wraps x in a Reduced, signaling early termination from reduce/transduce",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Wraps <code>x</code> in a Reduced, signaling early termination from reduce/transduce.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L18",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reduced?",
        signature = "(reduced? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Reduced value",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a Reduced value.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reify",
        signature = "(reify & specs)",
        completion = CompletionInfo(
            tailText = "Creates an anonymous object implementing one or more protocols",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates an anonymous object implementing one or more protocols.<br />
  Method bodies close over local bindings. Each instance carries its<br />
  own captured state, so reify works correctly inside loops.<br /><br />
Syntax:<br />
    (reify<br />
      ProtocolName<br />
      (method-name [this arg1] body)<br />
      AnotherProtocol<br />
      (another-method [this] body))
""",
            example = "(reify Speakable (speak [this] \"hello\"))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L372",
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
Returns a lazy sequence of elements where predicate returns false.<br />
   Opposite of filter. When called with pred only, returns a transducer.
""",
            example = "(remove even? [1 2 3 4 5 6]) ; =&gt; (1 3 5)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L474",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "remove-tap",
        signature = "(remove-tap f)",
        completion = CompletionInfo(
            tailText = "Removes f from the tap set",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes <code>f</code> from the tap set. Returns nil.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/tap.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "remove-watch",
        signature = "(remove-watch variable key)",
        completion = CompletionInfo(
            tailText = "Removes a watch function from a variable by key",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes a watch function from a variable by key.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L122",
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
Returns the map with keys renamed according to kmap.<br />
  Keys not present in kmap are left unchanged.
""",
            example = "(rename-keys {:a 1 :b 2 :c 3} {:a :x :b :y}) ; =&gt; {:x 1 :y 2 :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1035",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L771",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L783",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reset!",
        signature = "(reset! variable value)",
        completion = CompletionInfo(
            tailText = "Sets a new value on the given atom",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sets a new value on the given atom. Returns the new value.",
            example = "(def x (atom 10))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L51",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "resolve",
        signature = "(resolve sym)",
        completion = CompletionInfo(
            tailText = "Resolves the given symbol in the current environment and returns a resolved Symbol with the absol...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Resolves the given symbol in the current environment and returns a resolved Symbol with the absolute namespace or nil if it cannot be resolved.
""",
            example = "(resolve 'map) ; =&gt; phel\\core/map",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L740",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "rest",
        signature = "(rest coll)",
        completion = CompletionInfo(
            tailText = "Returns the sequence after the first element, or empty sequence if none",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the sequence after the first element, or empty sequence if none.",
            example = "(rest [1 2 3]) ; =&gt; [2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-basics.phel#L52",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L566",
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
Returns true if <code>coll</code> can be reverse-iterated in constant time.<br />
  Currently this is true for vectors and sorted-maps.
""",
            example = "(reversible? [1 2 3]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L577",
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
Returns, in constant time, a sequence of the items in <code>rev</code> in reverse<br />
  order. <code>rev</code> must be reversible (a vector or sorted-map); otherwise an<br />
  exception is thrown. For sorted-maps, returns reversed <code>[key value]</code> pairs.<br />
  Returns nil if <code>rev</code> is empty.
""",
            example = "(rseq [1 2 3]) ; =&gt; [3 2 1]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L586",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "run!",
        signature = "(run! f coll)",
        completion = CompletionInfo(
            tailText = "Calls (f x) for each element in coll for side effects",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Calls <code>(f x)</code> for each element in <code>coll</code> for side effects. Returns nil.
""",
            example = "(run! println [1 2 3])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/loops.phel#L9",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "satisfies?",
        signature = "(satisfies? protocol x)",
        completion = CompletionInfo(
            tailText = "Returns true if x's type implements all methods of the given protocol",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if x's type implements all methods of the given protocol.",
            example = "(satisfies? Stringable \"hello\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L320",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "second",
        signature = "(second coll)",
        completion = CompletionInfo(
            tailText = "Returns the second element of a sequence, or nil if not present",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the second element of a sequence, or nil if not present.",
            example = "(second [1 2 3]) ; =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-basics.phel#L46",
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
            example = "(select-keys {:a 1 :b 2 :c 3} [:a :c]) ; =&gt; {:a 1 :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1021",
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
Returns a seq on the collection. Strings are converted to a vector of characters.<br />
  Collections are unchanged. Returns nil if coll is empty or nil.<br /><br />
This function is useful for explicitly converting strings to sequences of characters,<br />
  enabling sequence operations like map, filter, and frequencies.
""",
            example = "(seq \"hello\") ; =&gt; [\"h\" \"e\" \"l\" \"l\" \"o\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L339",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "seq?",
        signature = "(seq? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a seq (implements LazySeqInterface), false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a seq (implements LazySeqInterface), false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L302",
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
Returns true if <code>(seq x)</code> is supported: collections (vectors, lists,<br />
   maps, sets, structs), lazy sequences, strings, PHP arrays, and nil.<br />
   Returns false for numbers, booleans, keywords, symbols, and other types.
""",
            example = "(seqable? [1 2]) ; =&gt; true\n(seqable? 42) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L421",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1101",
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
Returns true if <code>x</code> is a sequential collection (vector, list, or lazy<br />
   sequence), false otherwise. Sequential collections maintain insertion<br />
   order and support indexed or linear access. Maps, sets, and structs<br />
   are not sequential, matching Clojure's <code>Sequential</code> marker.
""",
            example = "(sequential? [1 2 3]) ; =&gt; true\n(sequential? {:a 1}) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L394",
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
Coerces a collection to a set. Returns a set containing the distinct elements of <code>coll</code>.<br />
  For creating sets from arguments, use <code>hash-set</code>.
""",
            example = "(set [1 2 3 2 1]) ; =&gt; #{1 2 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L227",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set!",
        signature = "(set! variable value)",
        completion = CompletionInfo(
            tailText = "Sets a new value to the given variable",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sets a new value to the given variable.",
            example = "(def x (var 10))",
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "reset!"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L58",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set-meta!",
        signature = "",
        completion = CompletionInfo(
            tailText = "Sets the metadata to a given object",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sets the metadata to a given object.",
            example = null,
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "with-meta"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/meta.phel#L70",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set-validator!",
        signature = "(set-validator! variable f)",
        completion = CompletionInfo(
            tailText = "Sets a validator function on a variable",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sets a validator function on a variable. The validator is called before any<br />
  state change with the proposed new value. If it returns a falsy value, an<br />
  exception is thrown and the state is not changed. Pass nil to remove.
""",
            example = "(set-validator! my-var pos?)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L129",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set-var",
        signature = "(var value)",
        completion = CompletionInfo(
            tailText = "Variables provide a way to manage mutable state that can be updated with set",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Variables provide a way to manage mutable state that can be updated with <code>set!</code> and <code>swap!</code>. Each variable contains a single value. To create a variable use the var function.
""",
            example = "(def counter (var 0))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/global-and-local-bindings/#variables",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L448",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "short",
        signature = "(short x)",
        completion = CompletionInfo(
            tailText = "Coerces x to a signed 16-bit integer in the range -32768",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>x</code> to a signed 16-bit integer in the range <code>-32768..32767</code>.<br />
   Decimal values are truncated toward zero (as in Clojure on the JVM).<br />
   Values outside the range or non-numeric inputs raise<br />
   <code>InvalidArgumentException</code>. Phel has no dedicated short type, so the<br />
   result is a plain PHP int.
""",
            example = "(short 32767) ; =&gt; 32767\n(short 1.9) ; =&gt; 1\n(short -32768) ; =&gt; -32768",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L268",
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
        name = "shuffle",
        signature = "(shuffle coll)",
        completion = CompletionInfo(
            tailText = "Returns a random permutation of coll",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a random permutation of coll.",
            example = "(shuffle [1 2 3 4 5]) ; =&gt; [3 1 5 2 4] (random order)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L763",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L184",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L178",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L172",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L218",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "slurp",
        signature = "(slurp path & [opts])",
        completion = CompletionInfo(
            tailText = "Reads entire file or URL into a string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Reads entire file or URL into a string.",
            example = "(slurp \"file.txt\") ; =&gt; \"file contents\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L81",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L235",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some->",
        signature = "(some-> x & forms)",
        completion = CompletionInfo(
            tailText = "Threads x through the forms like -> but stops when a form returns nil",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Threads <code>x</code> through the forms like <code>-></code> but stops when a form returns <code>nil</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L216",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some->>",
        signature = "(some->> x & forms)",
        completion = CompletionInfo(
            tailText = "Threads x through the forms like ->> but stops when a form returns nil",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Threads <code>x</code> through the forms like <code>->></code> but stops when a form returns <code>nil</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L240",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some-fn",
        signature = "(some-fn p & ps)",
        completion = CompletionInfo(
            tailText = "Takes a variadic set of predicates and returns a function f that, when called with any number of ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a variadic set of predicates and returns a function <code>f</code> that,<br />
   when called with any number of arguments, returns the first logical<br />
   true value produced by applying any of the composing predicates to<br />
   any of its arguments, and <code>nil</code> otherwise. The returned function<br />
   short-circuits on the first truthy result: arguments after it are<br />
   not inspected, and predicates after it are not tried.<br />
   Predicates are consulted in the order supplied; for a given<br />
   predicate, arguments are consulted left-to-right. Matches Clojure's<br />
   <code>some-fn</code> semantics.
""",
            example = "((some-fn even? nil?) 1 2) ; =&gt; true\n((some-fn pos? even?) -3 -1) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L96",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L218",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sort",
        signature = "(sort coll & [comp])",
        completion = CompletionInfo(
            tailText = "Returns a sorted vector",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a sorted vector. If no comparator is supplied compare is used.",
            example = "(sort [3 1 4 1 5 9 2 6]) ; =&gt; [1 1 2 3 4 5 6 9]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L742",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sort-by",
        signature = "(sort-by keyfn coll & [comp])",
        completion = CompletionInfo(
            tailText = "Returns a sorted vector where the sort order is determined by comparing (keyfn item)",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a sorted vector where the sort order is determined by comparing <code>(keyfn item)</code>.<br /><br />
If no comparator is supplied compare is used.
""",
            example = "(sort-by count [\"aaa\" \"c\" \"bb\"]) ; =&gt; [\"c\" \"bb\" \"aaa\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L751",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted-map",
        signature = "(sorted-map & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new sorted map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new sorted map. Keys are in natural sorted order.<br />
  The number of parameters must be even.
""",
            example = "(sorted-map :c 3 :a 1 :b 2) ; keys iterate as :a :b :c",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/collections.phel#L21",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted-map-by",
        signature = "(sorted-map-by comp & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new sorted map using the given comparator for key ordering",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new sorted map using the given comparator for key ordering.<br />
  The comparator takes two arguments and returns a negative integer,<br />
  zero, or a positive integer.
""",
            example = "(sorted-map-by (fn [a b] (compare b a)) :a 1 :b 2) ; keys iterate as :b :a",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/collections.phel#L29",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted-set",
        signature = "(sorted-set & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new sorted set",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new sorted set. Elements are in natural sorted order.",
            example = "(sorted-set 3 1 2) ; iterates as 1 2 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/collections.phel#L38",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted-set-by",
        signature = "(sorted-set-by comp & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new sorted set using the given comparator for element ordering",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new sorted set using the given comparator for element ordering.",
            example = "(sorted-set-by (fn [a b] (compare b a)) 3 1 2) ; iterates as 3 2 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/collections.phel#L45",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L453",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L199",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "special-symbols",
        signature = "",
        completion = CompletionInfo(
            tailText = "The set of symbols that name Phel special forms",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "The set of symbols that name Phel special forms.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L190",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "spit",
        signature = "(spit filename data & [opts])",
        completion = CompletionInfo(
            tailText = "Writes data to file, returning number of bytest that were written or nil on failure",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Writes data to file, returning number of bytest that were written or <code>nil</code><br />
  on failure. Accepts <code>opts</code> map for overriding default PHP file_put_contents<br />
  arguments, as example to append to file use <code>{:flags php/FILE_APPEND}</code>.<br /><br />
See PHP's <a href="https://www.php.net/manual/en/function.file-put-contents.php">file_put_contents</a> for more information.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L109",
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
            example = "(split-at 2 [1 2 3 4 5]) ; =&gt; [[1 2] [3 4 5]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1057",
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
            example = "(split-with #(&lt; % 4) [1 2 3 4 5 6]) ; =&gt; [[1 2 3] [4 5 6]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1064",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "str",
        signature = "(str & args)",
        completion = CompletionInfo(
            tailText = "Creates a string by concatenating values together",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a string by concatenating values together. If no arguments are<br />
provided an empty string is returned. Nil is represented as an empty string.<br />
Booleans are represented as "true" or "false" (matching Clojure semantics).<br />
Otherwise, it tries to call <code>__toString</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/strings.phel#L55",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L269",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L137",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L227",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L64",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sum",
        signature = "(sum xs)",
        completion = CompletionInfo(
            tailText = "Returns the sum of all elements is xs",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the sum of all elements is <code>xs</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L387",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L76",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "swap!",
        signature = "(swap! variable f & args)",
        completion = CompletionInfo(
            tailText = "Atomically swaps the value of the atom to (apply f current-value args)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Atomically swaps the value of the atom to <code>(apply f current-value args)</code>.<br /><br />
Returns the new value after the swap.
""",
            example = "(def counter (atom 0))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L86",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "symbol",
        signature = "(symbol name-or-ns & [name])",
        completion = CompletionInfo(
            tailText = "Returns a new symbol for given string with optional namespace",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new symbol for given string with optional namespace.<br /><br />
With one argument, creates a symbol without namespace.<br />
  With two arguments, creates a symbol in the given namespace.
""",
            example = "(symbol \"foo\") ; =&gt; foo",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/strings.phel#L14",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L160",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L59",
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
Takes the first <code>n</code> elements of <code>coll</code>.<br />
  When called with n only, returns a transducer.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L390",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L412",
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
Returns every nth item in <code>coll</code>. Returns a lazy sequence.<br />
  When called with n only, returns a transducer.
""",
            example = "(take-nth 2 [0 1 2 3 4 5 6 7 8]) ; =&gt; (0 2 4 6 8)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L436",
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
Takes all elements at the front of <code>coll</code> where <code>(pred x)</code> is true. Returns a lazy sequence.<br />
  When called with pred only, returns a transducer.
""",
            example = "(take-while #(&lt; % 5) [1 2 3 4 5 6 3 2 1]) ; =&gt; (1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L419",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "tap>",
        signature = "(tap> x)",
        completion = CompletionInfo(
            tailText = "Sends x to every registered tap",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sends <code>x</code> to every registered tap. Exceptions thrown by individual taps are<br />
  swallowed so one misbehaving tap does not affect the others. Returns true.
""",
            example = "(add-tap println)\n(tap&gt; {:event :login :user \"alice\"})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/tap.phel#L31",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "throw",
        signature = "(throw exception)",
        completion = CompletionInfo(
            tailText = "Throw an exception",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = "Throw an exception.",
            example = "(throw (php/new \\InvalidArgumentException \"Invalid input\"))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/control-flow/#try-catch-and-finally",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "time",
        signature = "(time expr)",
        completion = CompletionInfo(
            tailText = "Evaluates expr and prints the time it took",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Evaluates expr and prints the time it took. Returns the value of expr.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L676",
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
    ),
    PhelFunction(
        namespace = "core",
        name = "to-php-array",
        signature = "",
        completion = CompletionInfo(
            tailText = "Creates a PHP Array from a sequential data structure",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a PHP Array from a sequential data structure.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/defs.phel#L22",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "transduce",
        signature = "(transduce xform f & args)",
        completion = CompletionInfo(
            tailText = "Reduce with a transformation of f (xf)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reduce with a transformation of <code>f</code> (xf). If init is not supplied,<br />
  <code>(f)</code> will be called to produce it. <code>f</code> should be a reducing function<br />
  that returns the initial value when called with no arguments.
""",
            example = "(transduce (map inc) + [1 2 3]) ; =&gt; 9",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L86",
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
Transient collections provide faster performance for multiple sequential updates.<br />
  Use <code>persistent</code> to convert back.
""",
            example = "(def t (transient []))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transients.phel#L17",
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
Returns a vector of the nodes in the tree, via a depth-first walk.<br />
  branch? is a function with one argument that returns true if the given node<br />
  has children.<br />
  children must be a function with one argument that returns the children of the node.<br />
  root the root node of the tree.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L215",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L246",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/booleans.phel#L252",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "try",
        signature = "(try expr* catch-clause* finally-clause?)",
        completion = CompletionInfo(
            tailText = "All expressions are evaluated and if no exception is thrown the value of the last expression is r...",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
All expressions are evaluated and if no exception is thrown the value of the last expression is returned. If an exception occurs and a matching catch-clause is provided, its expression is evaluated and the value is returned. If no matching catch-clause can be found the exception is propagated out of the function. Before returning normally or abnormally the optionally finally-clause is evaluated.
""",
            example = "(try (/ 1 0) (catch \\Exception e \"error\"))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/control-flow/#try-catch-and-finally",
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
<em> <code>:set</code><br />
</em> <code>:keyword</code><br />
<em> <code>:symbol</code><br />
</em> <code>:var</code><br />
<em> <code>:int</code><br />
</em> <code>:float</code><br />
<em> <code>:string</code><br />
</em> <code>:nil</code><br />
<em> <code>:boolean</code><br />
</em> <code>:function</code><br />
<em> <code>:php/array</code><br />
</em> <code>:php/resource</code><br />
<em> <code>:php/object</code><br />
</em> <code>:unknown</code>
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L28",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "underive",
        signature = "(underive child parent)",
        completion = CompletionInfo(
            tailText = "Removes a parent/child relationship from the global hierarchy",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Removes a parent/child relationship from the global hierarchy.",
            example = "(underive :square :shape)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L114",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L16",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "unquote",
        signature = "(unquote expr)",
        completion = CompletionInfo(
            tailText = "Values that should be evaluated in a macro are marked with the unquote function",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Values that should be evaluated in a macro are marked with the unquote function. Shortcut: ,
""",
            example = "`(+ 1 ,(+ 2 3)) ; =&gt; (+ 1 5)",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/macros/#quasiquote",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "unquote-splicing",
        signature = "(unquote-splicing expr)",
        completion = CompletionInfo(
            tailText = "Values that should be evaluated in a macro are marked with the unquote function",
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = """
Values that should be evaluated in a macro are marked with the unquote function. Shortcut: ,@
""",
            example = "`(+ ,@[1 2 3]) ; =&gt; (+ 1 2 3)",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/macros/#quasiquote",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "unreduced",
        signature = "(unreduced x)",
        completion = CompletionInfo(
            tailText = "If x is Reduced, returns the unwrapped value; otherwise returns x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
If <code>x</code> is Reduced, returns the unwrapped value; otherwise returns <code>x</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L30",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/sequences.phel#L236",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L313",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L284",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L291",
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
            example = "(update-keys {:a 1 :b 2} name) ; =&gt; {\"a\" 1 \"b\" 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L281",
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
            example = "(update-vals {:a 1 :b 2} inc) ; =&gt; {:a 2 :b 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/fns-sets.phel#L291",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid-regex",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L12",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid?",
        signature = "(uuid? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a canonical UUID string (36 characters, 8-4-4-4-12 hexadecimal groups), fals...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a canonical UUID string (36 characters,<br />
  <code>8-4-4-4-12</code> hexadecimal groups), false otherwise. PHP has no UUID<br />
  type, so UUIDs are represented as strings.
""",
            example = "(uuid? \"550e8400-e29b-41d4-a716-446655440000\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L15",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "val",
        signature = "(val entry)",
        completion = CompletionInfo(
            tailText = "Returns the value of a map entry (a two-element vector [key value])",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the value of a map entry (a two-element vector <code>[key value]</code>).
""",
            example = "(val (first (pairs {:a 1}))) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L636",
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
Returns a sequence of all values in a map, or <code>nil</code> when the map is <code>nil</code><br />
  or empty.
""",
            example = "(vals {:a 1 :b 2}) ; =&gt; (1 2)\n(vals nil) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L621",
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
            example = "(values {:a 1 :b 2}) ; =&gt; (1 2)",
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "vals"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L643",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "var",
        signature = "(var value)",
        completion = CompletionInfo(
            tailText = "Creates a new variable with the given value",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new variable with the given value.",
            example = "(def counter (var 0))",
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "atom"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "var?",
        signature = "(var? x)",
        completion = CompletionInfo(
            tailText = "Checks if the given value is a variable",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if the given value is a variable.",
            example = null,
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "atom?"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/atoms.phel#L44",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vary-meta",
        signature = "",
        completion = CompletionInfo(
            tailText = "Returns an object with (apply f (meta obj) args) as its new metadata",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns an object with (apply f (meta obj) args) as its new metadata.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/meta.phel#L77",
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
Coerces a collection to a vector. For hash-maps and structs, entries<br />
  are returned as 2-element <code>[key value]</code> vectors, matching Clojure.
""",
            example = "(vec {:a 1 :b 2}) ; =&gt; [[:a 1] [:b 2]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L239",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vector",
        signature = "(vector & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new vector",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new vector. If no argument is provided, an empty vector is created.",
            example = "(vector 1 2 3) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/data-structures/#vectors",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vector?",
        signature = "(vector? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a vector, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a vector, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/predicates.phel#L245",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "volatile!",
        signature = "(volatile! val)",
        completion = CompletionInfo(
            tailText = "Creates a volatile mutable reference with initial value val",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a volatile mutable reference with initial value <code>val</code>.<br />
  Use for transducer state that needs fast mutation without watches.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L103",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "volatile?",
        signature = "(volatile? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Volatile",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a Volatile.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L123",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vreset!",
        signature = "(vreset! vol val)",
        completion = CompletionInfo(
            tailText = "Sets the value of volatile vol to val",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sets the value of volatile <code>vol</code> to <code>val</code>. Returns <code>val</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L110",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vswap!",
        signature = "(vswap! vol f & args)",
        completion = CompletionInfo(
            tailText = "Applies f to the current value of volatile vol plus args, and sets the new value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Applies <code>f</code> to the current value of volatile <code>vol</code> plus <code>args</code>,<br />
  and sets the new value. Returns the new value.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/transducers.phel#L116",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "when",
        signature = "(when test & body)",
        completion = CompletionInfo(
            tailText = "Evaluates body if test is true, otherwise returns nil",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Evaluates body if test is true, otherwise returns nil.",
            example = "(when (&gt; 10 5) \"greater\") ; =&gt; \"greater\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L19",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "when-first",
        signature = "(when-first bindings & body)",
        completion = CompletionInfo(
            tailText = "Binds name to the first element of coll",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Binds name to the first element of coll. When the collection is non-empty<br />
  (first returns non-nil), evaluates body with the binding.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L625",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "when-let",
        signature = "(when-let bindings & body)",
        completion = CompletionInfo(
            tailText = "When test is true, evaluates body with binding-form bound to the value of test",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "When test is true, evaluates body with binding-form bound to the value of test",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L575",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "when-not",
        signature = "(when-not test & body)",
        completion = CompletionInfo(
            tailText = "Evaluates body if test is false, otherwise returns nil",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Evaluates body if test is false, otherwise returns nil.",
            example = "(when-not (empty? [1 2 3]) \"has items\") ; =&gt; \"has items\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L25",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "when-some",
        signature = "(when-some bindings & body)",
        completion = CompletionInfo(
            tailText = "Binds name to the value of test",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Binds name to the value of test. When test is not nil, evaluates body with<br />
  binding-form bound to the value of test. Unlike when-let, false and 0 are not<br />
  treated as falsy — only nil causes the body to be skipped.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/protocols.phel#L617",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "with-meta",
        signature = "",
        completion = CompletionInfo(
            tailText = "Returns obj with the given metadata meta attached",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>obj</code> with the given metadata <code>meta</code> attached.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/meta.phel#L64",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "with-output-buffer",
        signature = "(with-output-buffer & body)",
        completion = CompletionInfo(
            tailText = "Everything that is printed inside the body will be stored in a buffer",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Everything that is printed inside the body will be stored in a buffer.<br />
   The result of the buffer is returned.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/io.phel#L18",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "zero?",
        signature = "(zero? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is zero",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is zero.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/math.phel#L186",
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
            example = "(zipcoll [:a :b :c] [1 2 3]) ; =&gt; {:a 1 :b 2 :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L1002",
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
Stops when the shorter of <code>keys</code> or <code>vals</code> is exhausted.<br />
  Works safely with infinite lazy sequences.
""",
            example = "(zipmap [:a :b :c] [1 2 3]) ; =&gt; {:a 1 :b 2 :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/seq-fns.phel#L987",
                docs = "",
            ),
        ),
    )
)
