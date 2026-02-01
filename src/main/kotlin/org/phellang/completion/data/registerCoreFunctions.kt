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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2486",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2460",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2493",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "*build-mode*",
        signature = "",
        completion = CompletionInfo(
            tailText = "Set to true when a file is being built/compiled, false otherwise",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Set to true when a file is being built/compiled, false otherwise.",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L141",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2436",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2447",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2780",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2796",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2473",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L587",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L600",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L637",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L562",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L612",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L625",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L643",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2432",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L649",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L536",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L146",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2860",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "assoc",
        signature = "(assoc ds key value)",
        completion = CompletionInfo(
            tailText = "Associates a value with a key in a collection",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Associates a value with a key in a collection.",
            example = "(assoc {:a 1} :b 2) ; =&gt; {:a 1 :b 2}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1005",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1412",
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
Associative data structures include hash maps, structs, and associative PHP arrays.
""",
            example = "(associative? {:a 1}) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L932",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2903",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2363",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2413",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2418",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2387",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2371",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2408",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2394",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2401",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2423",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2379",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L848",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1497",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L509",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2579",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L309",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2188",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2085",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "compare",
        signature = "(compare x y)",
        completion = CompletionInfo(
            tailText = "Wrapper for PHP's spaceship operator (php/<=>)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Wrapper for PHP's spaceship operator (<code>php/<=></code>).<br />
  Returns an integer less than, equal to, or greater than zero<br />
  when <code>x</code> is less than, equal to, or greater than <code>y</code>, respectively.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L739",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L3023",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2197",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1876",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L495",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L407",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2183",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1783",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L729",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L455",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2759",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1867",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2507",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L133",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dedupe",
        signature = "(dedupe coll)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence with consecutive duplicate values removed in coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence with consecutive duplicate values removed in <code>coll</code>.
""",
            example = "(dedupe [1 1 2 2 2 3 1 1]) ; =&gt; (1 2 3 1)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2075",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2340",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L263",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L295",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2926",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L268",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L278",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L258",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L273",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L283",
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
        name = "deref",
        signature = "(deref variable)",
        completion = CompletionInfo(
            tailText = "Returns the current value inside the variable",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the current value inside the variable.",
            example = "(def x (var 42))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1080",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2164",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "dissoc",
        signature = "(dissoc ds key)",
        completion = CompletionInfo(
            tailText = "Dissociates key from the datastructure ds",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Dissociates <code>key</code> from the datastructure <code>ds</code>. Returns <code>ds</code> without <code>key</code>.
""",
            example = "(dissoc {:a 1 :b 2} :b) ; =&gt; {:a 1}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1030",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1447",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "distinct",
        signature = "(distinct coll)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence with duplicated values removed in coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence with duplicated values removed in <code>coll</code>.
""",
            example = "(distinct [1 2 1 3 2 4 3]) ; =&gt; (1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1619",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1956",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1211",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1967",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "doseq",
        signature = "(doseq seq-exprs & body)",
        completion = CompletionInfo(
            tailText = "Alias for dofor",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Alias for <code>dofor</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1217",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2870",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "drop",
        signature = "(drop n coll)",
        completion = CompletionInfo(
            tailText = "Drops the first n elements of coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Drops the first <code>n</code> elements of <code>coll</code>. Returns a lazy sequence.
""",
            example = "(drop 2 [1 2 3 4 5]) ; =&gt; (3 4 5)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1466",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "drop-last",
        signature = "(drop-last n coll)",
        completion = CompletionInfo(
            tailText = "Drops the last n elements of coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Drops the last <code>n</code> elements of <code>coll</code>.
""",
            example = "(drop-last 2 [1 2 3 4 5]) ; =&gt; [1 2 3]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1479",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "drop-while",
        signature = "(drop-while pred coll)",
        completion = CompletionInfo(
            tailText = "Drops all elements at the front of coll where (pred x) is true",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Drops all elements at the front of <code>coll</code> where <code>(pred x)</code> is true. Returns a lazy sequence.
""",
            example = "(drop-while |(&lt; \$ 5) [1 2 3 4 5 6 3 2 1]) ; =&gt; (5 6 3 2 1)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1504",
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
Returns true if x would be 0, "" or empty collection, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L868",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L3017",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2514",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "every?",
        signature = "(every? pred coll)",
        completion = CompletionInfo(
            tailText = "Alias for all",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Alias for <code>all?</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L658",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2564",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L711",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L422",
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
            example = "(filter |(php/str_ends_with \$ \".phel\") (file-seq \"src/\")) ; =&gt; [\"src/file1.phel\" \"src/file2.phel\" ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2733",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "filter",
        signature = "(filter pred coll)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of elements where predicate returns true",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of elements where predicate returns true.",
            example = "(filter even? [1 2 3 4 5 6]) ; =&gt; (2 4 6)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1558",
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
            example = "(find |(&gt; \$ 5) [1 2 3 6 7 8]) ; =&gt; 6",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1594",
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
            example = "(find-index |(&gt; \$ 5) [1 2 3 6 7 8]) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1606",
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
            summary = "Returns the first element of a sequence, or nil if empty.",
            example = "(first [1 2 3]) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L94",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2304",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L793",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1171",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2653",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1671",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L3003",
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
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a function, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L823",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L324",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L975",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1402",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1985",
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
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a hash map, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L833",
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
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if all values are identical. Same as <code>a === b</code> in PHP.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L552",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2178",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2945",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L477",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2500",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L920",
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
Returns true if <code>x</code> is an integer number, false otherwise.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L798",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1938",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "interpose",
        signature = "(interpose sep coll)",
        completion = CompletionInfo(
            tailText = "Returns elements separated by a separator",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns elements separated by a separator. Returns a lazy sequence.<br /><br />
Inserts <code>sep</code> between each element of the collection.<br />
  Works with infinite sequences.
""",
            example = "(interpose 0 [1 2 3]) ; =&gt; (1 0 2 0 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1906",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2145",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1281",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2041",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1860",
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
            example = "((juxt inc dec |(* \$ 2)) 10) =&gt; [11 9 20]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2202",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keep",
        signature = "(keep pred coll)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of non-nil results of applying function to elements",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a lazy sequence of non-nil results of applying function to elements.",
            example = "(keep |(when (even? \$) (* \$ \$)) [1 2 3 4 5]) ; =&gt; (4 16)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1575",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keep-indexed",
        signature = "(keep-indexed pred coll)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of non-nil results of (pred i x)",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of non-nil results of <code>(pred i x)</code>.
""",
            example = "(keep-indexed |(when (even? \$1) \$2) [\"a\" \"b\" \"c\" \"d\"]) ; =&gt; (\"a\" \"c\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1584",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keys",
        signature = "(keys coll)",
        completion = CompletionInfo(
            tailText = "Returns a sequence of all keys in a map",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a sequence of all keys in a map.",
            example = "(keys {:a 1 :b 2}) ; =&gt; (:a :b)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1683",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "keyword",
        signature = "(keyword x)",
        completion = CompletionInfo(
            tailText = "Creates a new Keyword from a given string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new Keyword from a given string.",
            example = "(keyword \"name\") ; =&gt; :name",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L368",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L813",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1705",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1488",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1854",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1845",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2721",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L843",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L3066",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L3042",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1226",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1921",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "mapcat",
        signature = "(mapcat f coll)",
        completion = CompletionInfo(
            tailText = "Maps a function over a collection and concatenates the results",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Maps a function over a collection and concatenates the results. Returns a lazy sequence.<br /><br />
Applies <code>f</code> to each element of the collection, where <code>f</code> should return a collection.<br />
  All resulting collections are concatenated into a single lazy sequence.<br />
  Works with infinite sequences.
""",
            example = "(mapcat reverse [[1 2] [3 4]]) ; =&gt; (2 1 4 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1890",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2574",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2591",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2596",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2219",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2239",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2019",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2319",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "meta",
        signature = "",
        completion = CompletionInfo(
            tailText = "Gets the metadata of the given object or definition",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Gets the metadata of the given object or definition.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L164",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2569",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2993",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2998",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2544",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2539",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L73",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L445",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L717",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L450",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L573",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L683",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L877",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L663",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L579",
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
            example = "(ns my-app\\core (:require phel\\str :as str))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/namespaces/#namespace-ns",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L803",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2519",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2529",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L525",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1697",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2212",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2098",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2110",
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
            example = "(partition-by |(&lt; \$ 3) [1 2 3 4 5 1 2]) ; =&gt; [[1 2] [3 4 5] [1 2]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2065",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "peek",
        signature = "(peek coll)",
        completion = CompletionInfo(
            tailText = "Returns the last element of a sequence",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the last element of a sequence.",
            example = "(peek [1 2 3]) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L953",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L351",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1729",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1761",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1718",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L853",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L380",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L374",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L863",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L858",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L967",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2534",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2640",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2625",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2658",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2646",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L960",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1023",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1423",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2549",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2554",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2559",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "range",
        signature = "(range a & rest)",
        completion = CompletionInfo(
            tailText = "Creates a lazy sequence of numbers from start to end (exclusive)",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a lazy sequence of numbers from start to end (exclusive).",
            example = "(range 5) ; =&gt; (0 1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1112",
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
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2888",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2745",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L3008",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "realized?",
        signature = "(realized? coll)",
        completion = CompletionInfo(
            tailText = "Returns true if a lazy sequence has been realized, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if a lazy sequence has been realized, false otherwise.",
            example = "(realized? (take 5 (iterate inc 1))) ; =&gt; false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1977",
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
Reduces collection to a single value by repeatedly applying function to accumulator and elements.
""",
            example = "(reduce + [1 2 3 4]) ; =&gt; 10",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1266",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "remove",
        signature = "(remove pred coll)",
        completion = CompletionInfo(
            tailText = "Returns a lazy sequence of elements where predicate returns false",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a lazy sequence of elements where predicate returns false.<br />
   Opposite of filter.
""",
            example = "(remove even? [1 2 3 4 5 6]) ; =&gt; (1 3 5)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1567",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1819",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1831",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L433",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1630",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L427",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2030",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L884",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set",
        signature = "(set & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new Set from the given arguments",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new Set from the given arguments. Shortcut: #{}",
            example = "(set 1 2 3) ; =&gt; #{1 2 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L362",
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
            priority = PhelCompletionPriority.SPECIAL_FORMS,
        ),
        documentation = DocumentationInfo(
            summary = "Sets a new value to the given variable.",
            example = "(def x (var 10))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1073",
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
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sets the metadata to a given object.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L187",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L944",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1811",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1393",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2677",
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
            example = "(some |(when (&gt; \$ 10) \$) [5 15 8]) ; =&gt; 15",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L689",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2812",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2836",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "some?",
        signature = "(some? pred coll)",
        completion = CompletionInfo(
            tailText = "Returns true if predicate is true for at least one element in collection, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if predicate is true for at least one element in collection, false otherwise.
""",
            example = "(some? even? [1 3 5 6 7]) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L673",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1790",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1799",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2705",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2051",
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
            example = "(split-with |(&lt; \$ 4) [1 2 3 4 5 6]) ; =&gt; [[1 2 3] [4 5 6]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2058",
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
provided an empty string is returned. Nil and false are represented as an empty<br />
string. True is represented as 1. Otherwise, it tries to call <code>__toString</code>.<br />
This is PHP equivalent to <code>${'$'}args[0] . ${'$'}args[1] . ${'$'}args[2] ...</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L329",
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
            deprecation = DeprecationInfo(version = "Use phel\\str\\contains?"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L723",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L808",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L828",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2586",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "swap!",
        signature = "(swap! variable f & args)",
        completion = CompletionInfo(
            tailText = "Atomically swaps the value of the variable to (apply f current-value args)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Atomically swaps the value of the variable to <code>(apply f current-value args)</code>.<br /><br />
Returns the new value after the swap.
""",
            example = "(def counter (var 0))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1087",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L313",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L818",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2169",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "take",
        signature = "(take n coll)",
        completion = CompletionInfo(
            tailText = "Takes the first n elements of coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes the first <code>n</code> elements of <code>coll</code>.<br /><br />
Note: Metadata preservation works with inline calls but may be lost when binding<br />
  to variables. Use inline calls or force realization with doall if metadata needed.<br />
  See local/investigate-metadata-binding-issue.md for details.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1514",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1529",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "take-nth",
        signature = "(take-nth n coll)",
        completion = CompletionInfo(
            tailText = "Returns every nth item in coll",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns every nth item in <code>coll</code>. Returns a lazy sequence.
""",
            example = "(take-nth 2 [0 1 2 3 4 5 6 7 8]) ; =&gt; (0 2 4 6 8)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1546",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "take-while",
        signature = "(take-while pred coll)",
        completion = CompletionInfo(
            tailText = "Takes all elements at the front of coll where (pred x) is true",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes all elements at the front of <code>coll</code> where <code>(pred x)</code> is true. Returns a lazy sequence.
""",
            example = "(take-while |(&lt; \$ 5) [1 2 3 4 5 6 3 2 1]) ; =&gt; (1 2 3 4)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1536",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2985",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L206",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L341",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2284",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L700",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L706",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L751",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2126",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1048",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1459",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1430",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1437",
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
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a sequence of all values in a map.",
            example = "(values {:a 1 :b 2}) ; =&gt; (1 2)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1690",
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
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new variable with the given value.<br /><br />
Variables provide a way to manage mutable state that can be updated with <code>set!</code> and <code>swap!</code>. Each variable contains a single value. To create a variable use the var function.
""",
            example = "(def counter (var 0))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1059",
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
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Checks if the given value is a variable.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1068",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L838",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L483",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2966",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L489",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2613",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2524",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L1998",
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
Stops when the shorter of <code>keys</code> or <code>vals</code> is exhausted.
""",
            example = "(zipmap [:a :b :c] [1 2 3]) ; =&gt; {:a 1 :b 2 :c 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core.phel#L2005",
                docs = "",
            ),
        ),
    )
)
