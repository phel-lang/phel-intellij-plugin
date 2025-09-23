package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

/**
 * Based on official Phel API documentation: https://phel-lang.org/documentation/api/
 */
object PhelFunctionRegistry {

    private val functions = mutableMapOf<Namespace, List<DataFunction>>()

    init {
        registerBase64Functions()
        registerCoreFunctions()
        registerDebugFunctions()
        registerHtmlFunctions()
        registerHttpFunctions()
        registerJsonFunctions()
        registerPhpInteropFunctions()
        registerReplFunctions()
        registerStringFunctions()
        registerTestFunctions()
    }

    fun getFunctions(namespace: Namespace): List<DataFunction> {
        return functions[namespace] ?: emptyList()
    }

    fun getFunctions(priority: PhelCompletionPriority): List<DataFunction> {
        return functions.values.flatten().filter { it.priority == priority }
    }

    fun getFunction(name: String): DataFunction? {
        return functions.values.flatten().find { it.name == name }
    }

    fun getAllFunctions(): List<DataFunction> {
        return functions.values.flatten()
    }

    private fun registerBase64Functions() {
        functions[Namespace.BASE64] = listOf(
            DataFunction("base64/decode", "(decode s & [strict?])", PhelCompletionPriority.BASE64_FUNCTIONS, "base64", "Decodes the Base64 encoded string `s`. If `strict?` is true invalid characters trigger an error", """
<br /><code>(decode s & [strict?])</code><br /><br />
Decodes the Base64 encoded string <b>s</b>. If <b>strict?</b> is true invalid characters trigger an error.
<br /><br />
"""),
            DataFunction("base64/decode-url", "(decode-url s & [strict?])", PhelCompletionPriority.BASE64_FUNCTIONS, "base64", "Decodes a Base64 URL encoded string `s`. If `strict?` is true invalid characters trigger an error", """
<br /><code>(decode-url s & [strict?])</code><br /><br />
Decodes a Base64 URL encoded string <b>s</b>. If <b>strict?</b> is true invalid characters trigger an error.
<br /><br />
"""),
            DataFunction("base64/encode", "(encode s)", PhelCompletionPriority.BASE64_FUNCTIONS, "base64", "Returns the Base64 representation of `s`", """
<br /><code>(encode s)</code><br /><br />
Returns the Base64 representation of <b>s</b>.
<br /><br />
"""),
            DataFunction("base64/encode-url", "(encode-url s)", PhelCompletionPriority.BASE64_FUNCTIONS, "base64", "Returns the URL safe Base64 representation of `s`. Padding is removed", """
<br /><code>(encode-url s)</code><br /><br />
Returns the URL safe Base64 representation of <b>s</b>. Padding is removed.
<br /><br />
"""),
        )
    }

    private fun registerCoreFunctions() {
        functions[Namespace.CORE] = listOf(
            DataFunction("%", "(% dividend divisor)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Return the remainder of `dividend` / `divisor`", """
<br /><code>(% dividend divisor)</code><br /><br />
Return the remainder of <b>dividend</b> / <b>divisor</b>.
<br /><br />
"""),
            DataFunction("*", "(* & xs)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the product of all elements in `xs`. All elements in `xs` must benumbers. If `xs` is empty, return 1", """
<br /><code>(* & xs)</code><br /><br />
Returns the product of all elements in <b>xs</b>. All elements in <b>xs</b> must be<br />
numbers. If <b>xs</b> is empty, return 1.
<br /><br />
"""),
            DataFunction("**", "(** a x)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Return `a` to the power of `x`", """
<br /><code>(** a x)</code><br /><br />
Return <b>a</b> to the power of <b>x</b>.
<br /><br />
"""),
            DataFunction("*build-mode*", "", PhelCompletionPriority.MACROS, "core", "Set to true when a file is being built/transpiled, false otherwise", """
Set to true when a file is being built/transpiled, false otherwise.
<br /><br />
"""),
            DataFunction("*compile-mode*", "", PhelCompletionPriority.MACROS, "core", "Deprecated! Use *build-mode* instead. Set to true when a file is compiled, false otherwise", """
Deprecated! Use *build-mode* instead. Set to true when a file is compiled, false otherwise.
<br /><br />
"""),
            DataFunction("*file*", "*file*", PhelCompletionPriority.MACROS, "core", "Returns the path of the current source file", """
<br /><code>*file*</code><br /><br />
Returns the path of the current source file.
<br /><br />
"""),
            DataFunction("*ns*", "*ns*", PhelCompletionPriority.MACROS, "core", "Returns the namespace in the current scope", """
<br /><code>*ns*</code><br /><br />
Returns the namespace in the current scope.
<br /><br />
"""),
            DataFunction("+", "(+ & xs)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the sum of all elements in `xs`. All elements `xs` must be numbers.  If `xs` is empty, return 0", """
<br /><code>(+ & xs)</code><br /><br />
Returns the sum of all elements in <b>xs</b>. All elements <b>xs</b> must be numbers.<br />
  If <b>xs</b> is empty, return 0.
<br /><br />
"""),
            DataFunction("-", "(- & xs)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the difference of all elements in `xs`. If `xs` is empty, return 0. If `xs`  has one element, return the negative value of that element", """
<br /><code>(- & xs)</code><br /><br />
Returns the difference of all elements in <b>xs</b>. If <b>xs</b> is empty, return 0. If <b>xs</b><br />
  has one element, return the negative value of that element.
<br /><br />
"""),
            DataFunction("->", "(-> x & forms)", PhelCompletionPriority.MACROS, "core", "Threads the expr through the forms. Inserts `x` as the second item  in the first form, making a list of it if it is not a list already.  If there are more forms, insert the first form as the second item in  the second form, etc", """
<br /><code>(-> x & forms)</code><br /><br />
Threads the expr through the forms. Inserts <b>x</b> as the second item<br />
  in the first form, making a list of it if it is not a list already.<br />
  If there are more forms, insert the first form as the second item in<br />
  the second form, etc.
<br /><br />
"""),
            DataFunction("->>", "(->> x & forms)", PhelCompletionPriority.MACROS, "core", "Threads the expr through the forms. Inserts `x` as the  last item in the first form, making a list of it if it is not a  list already. If there are more forms, insert the first form as the  last item in the second form, etc", """
<br /><code>(->> x & forms)</code><br /><br />
Threads the expr through the forms. Inserts <b>x</b> as the<br />
  last item in the first form, making a list of it if it is not a<br />
  list already. If there are more forms, insert the first form as the<br />
  last item in the second form, etc.
<br /><br />
"""),
            DataFunction("/", "(/ & xs)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the nominator divided by all the denominators. If `xs` is empty,returns 1. If `xs` has one value, returns the reciprocal of x", """
<br /><code>(/ & xs)</code><br /><br />
Returns the nominator divided by all the denominators. If <b>xs</b> is empty,<br />
returns 1. If <b>xs</b> has one value, returns the reciprocal of x.
<br /><br />
"""),
            DataFunction("<", "(< a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if each argument is strictly less than the following argument. Returns a boolean", """
<br /><code>(< a & more)</code><br /><br />
Checks if each argument is strictly less than the following argument. Returns a boolean.
<br /><br />
"""),
            DataFunction("<=", "(<= a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if each argument is less than or equal to the following argument. Returns a boolean", """
<br /><code>(<= a & more)</code><br /><br />
Checks if each argument is less than or equal to the following argument. Returns a boolean.
<br /><br />
"""),
            DataFunction("<=>", "(<=> a b)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Alias for the spaceship PHP operator in ascending order. Returns an int", """
<br /><code>(<=> a b)</code><br /><br />
Alias for the spaceship PHP operator in ascending order. Returns an int.
<br /><br />
"""),
            DataFunction("=", "(= a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if all values are equal. Same as `a == b` in PHP", """
<br /><code>(= a & more)</code><br /><br />
Checks if all values are equal. Same as <b>a == b</b> in PHP.
<br /><br />
"""),
            DataFunction(">", "(> a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if each argument is strictly greater than the following argument. Returns a boolean", """
<br /><code>(> a & more)</code><br /><br />
Checks if each argument is strictly greater than the following argument. Returns a boolean.
<br /><br />
"""),
            DataFunction(">=", "(>= a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if each argument is greater than or equal to the following argument. Returns a boolean", """
<br /><code>(>= a & more)</code><br /><br />
Checks if each argument is greater than or equal to the following argument. Returns a boolean.
<br /><br />
"""),
            DataFunction(">=<", "(>=< a b)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Alias for the spaceship PHP operator in descending order. Returns an int", """
<br /><code>(>=< a b)</code><br /><br />
Alias for the spaceship PHP operator in descending order. Returns an int.
<br /><br />
"""),
            DataFunction("NAN", "", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Constant for Not a Number (NAN) values", """
Constant for Not a Number (NAN) values.
<br /><br />
"""),
            DataFunction("all?", "(all? pred xs)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `(pred x)` is logical true for every `x` in collection `xs`   or if `xs` is empty. Otherwise returns false", """
<br /><code>(all? pred xs)</code><br /><br />
Returns true if <b>(pred x)</b> is logical true for every <b>x</b> in collection <b>xs</b><br />
   or if <b>xs</b> is empty. Otherwise returns false.
<br /><br />
"""),
            DataFunction("and", "(and & args)", PhelCompletionPriority.MACROS, "core", "Evaluates each expression one at a time, from left to right. If a formreturns logical false, `and` returns that value and doesn't evaluate any of theother expressions, otherwise, it returns the value of the last expression.Calling the `and` function without arguments returns true", """
<br /><code>(and & args)</code><br /><br />
Evaluates each expression one at a time, from left to right. If a form<br />
returns logical false, <b>and</b> returns that value and doesn't evaluate any of the<br />
other expressions, otherwise, it returns the value of the last expression.<br />
Calling the <b>and</b> function without arguments returns true.
<br /><br />
"""),
            DataFunction("apply", "(apply f expr*)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Calls the function with the given arguments. The last argument must be a list of values, which are passed as separate arguments, rather than a single list. Apply returns the result of the calling function", """
<br /><code>(apply f expr*)</code><br /><br />
Calls the function with the given arguments. The last argument must be a list of values, which are passed as separate arguments, rather than a single list. Apply returns the result of the calling function.
<br /><br />
"""),
            DataFunction("argv", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Vector of arguments passed to the script", """
Vector of arguments passed to the script.
<br /><br />
"""),
            DataFunction("as->", "(as-> expr name & forms)", PhelCompletionPriority.MACROS, "core", "Binds `name` to `expr`, evaluates the first form in the lexical context  of that binding, then binds name to that result, repeating for each  successive form, returning the result of the last form", """
<br /><code>(as-> expr name & forms)</code><br /><br />
Binds <b>name</b> to <b>expr</b>, evaluates the first form in the lexical context<br />
  of that binding, then binds name to that result, repeating for each<br />
  successive form, returning the result of the last form.
<br /><br />
"""),
            DataFunction("assert-non-nil", "(assert-non-nil & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "", """
<br /><code>(assert-non-nil & xs)</code><br /><br />

<br /><br />
"""),
            DataFunction("assoc", "(assoc ds key value)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Alias for `put`", """
<br /><code>(assoc ds key value)</code><br /><br />
Alias for <b>put</b>.
<br /><br />
"""),
            DataFunction("assoc-in", "(assoc-in ds ks v)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Alias for `put-in`", """
<br /><code>(assoc-in ds ks v)</code><br /><br />
Alias for <b>put-in</b>.
<br /><br />
"""),
            DataFunction("associative?", "(associative? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is an associative data structure, false otherwise", """
<br /><code>(associative? x)</code><br /><br />
Returns true if <b>x</b> is an associative data structure, false otherwise.
<br /><br />
"""),
            DataFunction("binding", "(binding bindings & body)", PhelCompletionPriority.MACROS, "core", "Temporary redefines definitions while executing the body.  The value will be reset after the body was executed", """
<br /><code>(binding bindings & body)</code><br /><br />
Temporary redefines definitions while executing the body.<br />
  The value will be reset after the body was executed.
<br /><br />
"""),
            DataFunction("bit-and", "(bit-and x y & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise and", """
<br /><code>(bit-and x y & args)</code><br /><br />
Bitwise and.
<br /><br />
"""),
            DataFunction("bit-clear", "(bit-clear x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Clear bit an index `n`", """
<br /><code>(bit-clear x n)</code><br /><br />
Clear bit an index <b>n</b>.
<br /><br />
"""),
            DataFunction("bit-flip", "(bit-flip x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Flip bit at index `n`", """
<br /><code>(bit-flip x n)</code><br /><br />
Flip bit at index <b>n</b>.
<br /><br />
"""),
            DataFunction("bit-not", "(bit-not x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise complement", """
<br /><code>(bit-not x)</code><br /><br />
Bitwise complement.
<br /><br />
"""),
            DataFunction("bit-or", "(bit-or x y & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise or", """
<br /><code>(bit-or x y & args)</code><br /><br />
Bitwise or.
<br /><br />
"""),
            DataFunction("bit-set", "(bit-set x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Set bit an index `n`", """
<br /><code>(bit-set x n)</code><br /><br />
Set bit an index <b>n</b>.
<br /><br />
"""),
            DataFunction("bit-shift-left", "(bit-shift-left x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise shift left", """
<br /><code>(bit-shift-left x n)</code><br /><br />
Bitwise shift left.
<br /><br />
"""),
            DataFunction("bit-shift-right", "(bit-shift-right x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise shift right", """
<br /><code>(bit-shift-right x n)</code><br /><br />
Bitwise shift right.
<br /><br />
"""),
            DataFunction("bit-test", "(bit-test x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Test bit at index `n`", """
<br /><code>(bit-test x n)</code><br /><br />
Test bit at index <b>n</b>.
<br /><br />
"""),
            DataFunction("bit-xor", "(bit-xor x y & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise xor", """
<br /><code>(bit-xor x y & args)</code><br /><br />
Bitwise xor.
<br /><br />
"""),
            DataFunction("boolean?", "(boolean? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a boolean, false otherwise", """
<br /><code>(boolean? x)</code><br /><br />
Returns true if <b>x</b> is a boolean, false otherwise.
<br /><br />
"""),
            DataFunction("butlast", "(butlast xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns all but the last item in `xs`", """
<br /><code>(butlast xs)</code><br /><br />
Returns all but the last item in <b>xs</b>.
<br /><br />
"""),
            DataFunction("case", "(case e & pairs)", PhelCompletionPriority.CONTROL_FLOW, "core", "Takes an expression `e` and a set of test-content/expression pairs. First  evaluates `e` and then finds the first pair where the test-constant matches  the result of `e`. The associated expression is then evaluated and returned.  If no matches can be found a final last expression can be provided that is  then evaluated and returned. Otherwise, nil is returned", """
<br /><code>(case e & pairs)</code><br /><br />
Takes an expression <b>e</b> and a set of test-content/expression pairs. First<br />
  evaluates <b>e</b> and then finds the first pair where the test-constant matches<br />
  the result of <b>e</b>. The associated expression is then evaluated and returned.<br />
  If no matches can be found a final last expression can be provided that is<br />
  then evaluated and returned. Otherwise, nil is returned.
<br /><br />
"""),
            DataFunction("catch", "(catch exception-type exception-name expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Handle exceptions thrown in a `try` block by matching on the provided exception type. The caught exception is bound to exception-name while evaluating the expressions", """
<br /><code>(catch exception-type exception-name expr*)</code><br /><br />
Handle exceptions thrown in a <b>try</b> block by matching on the provided exception type. The caught exception is bound to <b>exception-name</b> while evaluating the expressions.
<br /><br />
"""),
            DataFunction("coerce-in", "(coerce-in v min max)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns `v` if it is in the range, or `min` if `v` is less than `min`, or `max` if `v` is greater than `max`", """
<br /><code>(coerce-in v min max)</code><br /><br />
Returns <b>v</b> if it is in the range, or <b>min</b> if <b>v</b> is less than <b>min</b>, or <b>max</b> if <b>v</b> is greater than <b>max</b>.
<br /><br />
"""),
            DataFunction("comment", "(comment &)", PhelCompletionPriority.MACROS, "core", "Ignores the body of the comment", """
<br /><code>(comment &)</code><br /><br />
Ignores the body of the comment.
<br /><br />
"""),
            DataFunction("comp", "(comp & fs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes a list of functions and returns a function that is the composition of those functions", """
<br /><code>(comp & fs)</code><br /><br />
Takes a list of functions and returns a function that is the composition of those functions.
<br /><br />
"""),
            DataFunction("compare", "(compare x y)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "An integer less than, equal to, or greater than zero when `x` is less than, equal to, or greater than `y`, respectively", """
<br /><code>(compare x y)</code><br /><br />
An integer less than, equal to, or greater than zero when <b>x</b> is less than, equal to, or greater than <b>y</b>, respectively.
<br /><br />
"""),
            DataFunction("compile", "(compile form)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the compiled PHP code string for the given form", """
<br /><code>(compile form)</code><br /><br />
Returns the compiled PHP code string for the given form.
<br /><br />
"""),
            DataFunction("complement", "(complement f)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a function that takes the same arguments as `f` and returns the opposite truth value", """
<br /><code>(complement f)</code><br /><br />
Returns a function that takes the same arguments as <b>f</b> and returns the opposite truth value.
<br /><br />
"""),
            DataFunction("concat", "(concat arr & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Concatenates multiple sequential data structures", """
<br /><code>(concat arr & xs)</code><br /><br />
Concatenates multiple sequential data structures.
<br /><br />
"""),
            DataFunction("cond", "(cond & pairs)", PhelCompletionPriority.CONTROL_FLOW, "core", "Takes a set of test/expression pairs. Evaluates each test one at a time.  If a test returns logically true, the expression is evaluated and returned.  If no test matches a final last expression can be provided that is then  evaluated and returned. Otherwise, nil is returned", """
<br /><code>(cond & pairs)</code><br /><br />
Takes a set of test/expression pairs. Evaluates each test one at a time.<br />
  If a test returns logically true, the expression is evaluated and returned.<br />
  If no test matches a final last expression can be provided that is then<br />
  evaluated and returned. Otherwise, nil is returned.
<br /><br />
"""),
            DataFunction("cons", "(cons x xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Prepends `x` to the beginning of `xs`", """
<br /><code>(cons x xs)</code><br /><br />
Prepends <b>x</b> to the beginning of <b>xs</b>.
<br /><br />
"""),
            DataFunction("constantly", "(constantly x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a function that always returns `x` and ignores any passed arguments", """
<br /><code>(constantly x)</code><br /><br />
Returns a function that always returns <b>x</b> and ignores any passed arguments.
<br /><br />
"""),
            DataFunction("contains-value?", "(contains-value? coll val)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if the value is present in the given collection, otherwise returns false", """
<br /><code>(contains-value? coll val)</code><br /><br />
Returns true if the value is present in the given collection, otherwise returns false.
<br /><br />
"""),
            DataFunction("contains?", "(contains? coll key)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if key is present in the given collection, otherwise returns false", """
<br /><code>(contains? coll key)</code><br /><br />
Returns true if key is present in the given collection, otherwise returns false.
<br /><br />
"""),
            DataFunction("count", "(count xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Counts the number of elements in a sequence. Can be used on everything that implements the PHP Countable interface", """
<br /><code>(count xs)</code><br /><br />
Counts the number of elements in a sequence. Can be used on everything that implements the PHP Countable interface.
<br /><br />
"""),
            DataFunction("dec", "(dec x)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Decrements `x` by one", """
<br /><code>(dec x)</code><br /><br />
Decrements <b>x</b> by one.
<br /><br />
"""),
            DataFunction("declare", "", PhelCompletionPriority.MACROS, "core", "Declare a global symbol before it is defined", """
Declare a global symbol before it is defined.
<br /><br />
"""),
            DataFunction("dedupe", "(dedupe xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector with consecutive duplicate values removed in `xs`", """
<br /><code>(dedupe xs)</code><br /><br />
Returns a vector with consecutive duplicate values removed in <b>xs</b>.
<br /><br />
"""),
            DataFunction("deep-merge", "(deep-merge & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Recursively merges data structures", """
<br /><code>(deep-merge & args)</code><br /><br />
Recursively merges data structures.
<br /><br />
"""),
            DataFunction("def", "(def name meta? value)", PhelCompletionPriority.SPECIAL_FORMS, "core", "This special form binds a value to a global symbol", """
<br /><code>(def name meta? value)</code><br /><br />
This special form binds a value to a global symbol.
<br /><br />
"""),
            DataFunction("def-", "(def- name value)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a private value that will not be exported", """
<br /><code>(def- name value)</code><br /><br />
Define a private value that will not be exported.
<br /><br />
"""),
            DataFunction("defexception", "(defexception name)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a new exception", """
<br /><code>(defexception name)</code><br /><br />
Define a new exception.
<br /><br />
"""),
            DataFunction("defexception*", "(defexception name)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Defines a new exception", """
<br /><code>(defexception my-ex)</code><br />
<br /><br />
"""),
            DataFunction("definterface", "(definterface name & fns)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Defines an interface", """
<br /><code>(definterface name & fns)</code><br /><br />
Defines an interface.
<br /><br />
"""),
            DataFunction("definterface*", "(definterface name & fns)", PhelCompletionPriority.SPECIAL_FORMS, "core", "An interface in Phel defines an abstract set of functions. It is directly mapped to a PHP interface. An interface can be defined by using the definterface macro", """
<br /><code>(definterface name & fns)</code><br /><br />
An interface in Phel defines an abstract set of functions. It is directly mapped to a PHP interface. An interface can be defined by using the definterface macro.
<br /><br />
"""),
            DataFunction("defmacro", "(defmacro name & fdecl)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a macro", """
<br /><code>(defmacro name & fdecl)</code><br /><br />
Define a macro.
<br /><br />
"""),
            DataFunction("defmacro-", "(defmacro- name & fdecl)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a private macro that will not be exported", """
<br /><code>(defmacro- name & fdecl)</code><br /><br />
Define a private macro that will not be exported.
<br /><br />
"""),
            DataFunction("defn", "(defn name & fdecl)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a new global function", """
<br /><code>(defn name & fdecl)</code><br /><br />
Define a new global function.
<br /><br />
"""),
            DataFunction("defn-", "(defn- name & fdecl)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a private function that will not be exported", """
<br /><code>(defn- name & fdecl)</code><br /><br />
Define a private function that will not be exported.
<br /><br />
"""),
            DataFunction("defstruct", "(defstruct name keys & implementations)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a new struct", """
<br /><code>(defstruct name keys & implementations)</code><br /><br />
Define a new struct.
<br /><br />
"""),
            DataFunction("defstruct*", "(defstruct my-struct [a b c])", PhelCompletionPriority.SPECIAL_FORMS, "core", "A Struct is a special kind of Map. It only supports a predefined number of keys and is associated to a global name. The Struct not only defines itself but also a predicate function", """
<br /><code>(defstruct my-struct [a b c])</code><br /><br />
A Struct is a special kind of Map. It only supports a predefined number of keys and is associated to a global name. The Struct not only defines itself but also a predicate function.
<br /><br />
"""),
            DataFunction("deref", "(deref variable)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Return the value inside the variable", """
<br /><code>(deref variable)</code><br /><br />
Return the value inside the variable.
<br /><br />
"""),
            DataFunction("difference", "(difference set & sets)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Difference between multiple sets into a new one", """
<br /><code>(difference set & sets)</code><br /><br />
Difference between multiple sets into a new one.
<br /><br />
"""),
            DataFunction("difference-pair", "(difference-pair s1 s2)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "", """
<br /><code>(difference-pair s1 s2)</code><br /><br />

<br /><br />
"""),
            DataFunction("dissoc", "(dissoc ds key)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Alias for `unset`", """
<br /><code>(dissoc ds key)</code><br /><br />
Alias for <b>unset</b>.
<br /><br />
"""),
            DataFunction("dissoc-in", "(dissoc-in ds ks)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Alias for `unset-in`", """
<br /><code>(dissoc-in ds ks)</code><br /><br />
Alias for <b>unset-in</b>.
<br /><br />
"""),
            DataFunction("distinct", "(distinct xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector with duplicated values removed in `xs`", """
<br /><code>(distinct xs)</code><br /><br />
Returns a vector with duplicated values removed in <b>xs</b>.
<br /><br />
"""),
            DataFunction("do", "(do expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates the expressions in order and returns the value of the last expression. If no expression is given, nil is returned", """
<br /><code>(do expr*)</code><br /><br />
Evaluates the expressions in order and returns the value of the last expression. If no expression is given, nil is returned.
<br /><br />
"""),
            DataFunction("dofor", "(dofor head & body)", PhelCompletionPriority.CONTROL_FLOW, "core", "Repeatedly executes body for side effects with bindings and modifiers as  provided by for. Returns nil", """
<br /><code>(dofor head & body)</code><br /><br />
Repeatedly executes body for side effects with bindings and modifiers as<br />
  provided by for. Returns nil.
<br /><br />
"""),
            DataFunction("doto", "(doto x & forms)", PhelCompletionPriority.MACROS, "core", "Evaluates x then calls all of the methods and functions with the  value of x supplied at the front of the given arguments. The forms  are evaluated in order. Returns x", """
<br /><code>(doto x & forms)</code><br /><br />
Evaluates x then calls all of the methods and functions with the<br />
  value of x supplied at the front of the given arguments. The forms<br />
  are evaluated in order. Returns x.
<br /><br />
"""),
            DataFunction("drop", "(drop n xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Drops the first `n` elements of `xs`", """
<br /><code>(drop n xs)</code><br /><br />
Drops the first <b>n</b> elements of <b>xs</b>.
<br /><br />
"""),
            DataFunction("drop-last", "(drop-last n xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Drops the last `n` elements of `xs`", """
<br /><code>(drop-last n xs)</code><br /><br />
Drops the last <b>n</b> elements of <b>xs</b>.
<br /><br />
"""),
            DataFunction("drop-while", "(drop-while pred xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Drops all elements at the front `xs` where `(pred x)` is true", """
<br /><code>(drop-while pred xs)</code><br /><br />
Drops all elements at the front <b>xs</b> where <b>(pred x)</b> is true.
<br /><br />
"""),
            DataFunction("empty?", "(empty? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if x would be 0, \"\" or empty collection, false otherwise", """
<br /><code>(empty? x)</code><br /><br />
Returns true if x would be 0, \"\" or empty collection, false otherwise.
<br /><br />
"""),
            DataFunction("eval", "(eval form)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Evaluates a form and return the evaluated results", """
<br /><code>(eval form)</code><br /><br />
Evaluates a form and return the evaluated results.
<br /><br />
"""),
            DataFunction("even?", "(even? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is even", """
<br /><code>(even? x)</code><br /><br />
Checks if <b>x</b> is even.
<br /><br />
"""),
            DataFunction("every?", "(every? pred xs)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Alias for `all?`", """
<br /><code>(every? pred xs)</code><br /><br />
Alias for <b>all?</b>.
<br /><br />
"""),
            DataFunction("extreme", "(extreme order args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the most extreme value in `args` based on the binary `order` function", """
<br /><code>(extreme order args)</code><br /><br />
Returns the most extreme value in <b>args</b> based on the binary <b>order</b> function.
<br /><br />
"""),
            DataFunction("false?", "(false? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is false. Same as `x === false` in PHP", """
<br /><code>(false? x)</code><br /><br />
Checks if <b>x</b> is false. Same as <b>x === false</b> in PHP.
<br /><br />
"""),
            DataFunction("ffirst", "(ffirst xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as `(first (first xs))`", """
<br /><code>(ffirst xs)</code><br /><br />
Same as <b>(first (first xs))</b>.
<br /><br />
"""),
            DataFunction("filter", "(filter pred xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns all elements of `xs` where `(pred x)` is true", """
<br /><code>(filter pred xs)</code><br /><br />
Returns all elements of <b>xs</b> where <b>(pred x)</b> is true.
<br /><br />
"""),
            DataFunction("finally", "(finally expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluate expressions after the try body and all matching catches have completed. The finally block runs regardless of whether an exception was thrown", """
<br /><code>(finally expr*)</code><br /><br />
Evaluate expressions after the try body and all matching catches have completed. The finally block runs regardless of whether an exception was thrown.
<br /><br />
"""),
            DataFunction("find", "(find pred xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the first item in `xs` where `(pred item)` evaluates to true", """
<br /><code>(find pred xs)</code><br /><br />
Returns the first item in <b>xs</b> where <b>(pred item)</b> evaluates to true.
<br /><br />
"""),
            DataFunction("find-index", "(find-index pred xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the index of the first item in `xs` where `(pred index item)` evaluates to true", """
<br /><code>(find-index pred xs)</code><br /><br />
Returns the index of the first item in <b>xs</b> where <b>(pred index item)</b> evaluates to true.
<br /><br />
"""),
            DataFunction("first", "(first xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns the first element of an indexed sequence or nil", """
<br /><code>(first xs)</code><br /><br />
Returns the first element of an indexed sequence or nil.
<br /><br />
"""),
            DataFunction("flatten", "(flatten xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes a nested sequential data structure `(tree)`, and returns their contents  as a single, flat vector", """
<br /><code>(flatten xs)</code><br /><br />
Takes a nested sequential data structure <b>(tree)</b>, and returns their contents<br />
  as a single, flat vector.
<br /><br />
"""),
            DataFunction("float?", "(float? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is float point number, false otherwise", """
<br /><code>(float? x)</code><br /><br />
Returns true if <b>x</b> is float point number, false otherwise.
<br /><br />
"""),
            DataFunction("fn", "(fn [params*] expr*)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Defines a function. A function consists of a list of parameters and a list of expression. The value of the last expression is returned as the result of the function. All other expression are only evaluated for side effects. If no expression is given, the function returns nil", """
<br /><code>(fn [params*] expr*)</code><br /><br />
Defines a function. A function consists of a list of parameters and a list of expression. The value of the last expression is returned as the result of the function. All other expression are only evaluated for side effects. If no expression is given, the function returns nil.
<br /><br />
"""),
            DataFunction("for", "", PhelCompletionPriority.CONTROL_FLOW, "core", "", """

<br /><br />
"""),
            DataFunction("foreach", "(foreach [key value valueExpr] expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "The foreach special form can be used to iterate over all kind of PHP datastructures. The return value of foreach is always nil. The loop special form should be preferred of the foreach special form whenever possible", """
<br /><code>(foreach [value valueExpr] expr*)<br />
(foreach [key value valueExpr] expr*)</code><br /><br />
The foreach special form can be used to iterate over all kind of PHP datastructures. The return value of foreach is always nil. The loop special form should be preferred of the foreach special form whenever possible.
<br /><br />
"""),
            DataFunction("format", "(format fmt & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a formatted string. See PHP's  for more information", """
<br /><code>(format fmt & xs)</code><br /><br />
Returns a formatted string. See PHP's <a href=\"https://www.php.net/manual/en/function.sprintf.php\">sprintf</a> for more information.
<br /><br />
"""),
            DataFunction("frequencies", "(frequencies xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a map from distinct items in `xs` to the number of times they appear", """
<br /><code>(frequencies xs)</code><br /><br />
Returns a map from distinct items in <b>xs</b> to the number of times they appear.
<br /><br />
"""),
            DataFunction("full-name", "(full-name x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Return the namespace and name string of a string, keyword or symbol", """
<br /><code>(full-name x)</code><br /><br />
Return the namespace and name string of a string, keyword or symbol.
<br /><br />
"""),
            DataFunction("function?", "(function? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a function, false otherwise", """
<br /><code>(function? x)</code><br /><br />
Returns true if <b>x</b> is a function, false otherwise.
<br /><br />
"""),
            DataFunction("gensym", "(gensym )", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Generates a new unique symbol", """
<br /><code>(gensym )</code><br /><br />
Generates a new unique symbol.
<br /><br />
"""),
            DataFunction("get", "(get ds k & [opt])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Get the value mapped to `key` from the datastructure `ds`.  Returns `opt` or nil if the value cannot be found", """
<br /><code>(get ds k & [opt])</code><br /><br />
Get the value mapped to <b>key</b> from the datastructure <b>ds</b>.<br />
  Returns <b>opt</b> or nil if the value cannot be found.
<br /><br />
"""),
            DataFunction("get-in", "(get-in ds ks & [opt])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Access a value in a nested data structure. Looks into the data structure via a sequence of keys", """
<br /><code>(get-in ds ks & [opt])</code><br /><br />
Access a value in a nested data structure. Looks into the data structure via a sequence of keys.
<br /><br />
"""),
            DataFunction("group-by", "(group-by f xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a map of the elements of xs keyed by the result of  f on each element", """
<br /><code>(group-by f xs)</code><br /><br />
Returns a map of the elements of xs keyed by the result of<br />
  f on each element.
<br /><br />
"""),
            DataFunction("hash-map", "(hash-map & xs) # {& xs}", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new hash map. If no argument is provided, an empty hash map is created. The number of parameters must be even", """
<br /><code>(hash-map & xs) # {& xs}</code><br /><br />
Creates a new hash map. If no argument is provided, an empty hash map is created. The number of parameters must be even.
<br /><br />
"""),
            DataFunction("hash-map?", "(hash-map? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a hash map, false otherwise", """
<br /><code>(hash-map? x)</code><br /><br />
Returns true if <b>x</b> is a hash map, false otherwise.
<br /><br />
"""),
            DataFunction("id", "(id a & more)", PhelCompletionPriority.MACROS, "core", "Checks if all values are identical. Same as `a === b` in PHP", """
<br /><code>(id a & more)</code><br /><br />
Checks if all values are identical. Same as <b>a === b</b> in PHP.
<br /><br />
"""),
            DataFunction("identity", "(identity x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns its argument", """
<br /><code>(identity x)</code><br /><br />
Returns its argument.
<br /><br />
"""),
            DataFunction("if", "(if test then else?)", PhelCompletionPriority.CONTROL_FLOW, "core", "A control flow structure. First evaluates test. If test evaluates to true, only the then form is evaluated and the result is returned. If test evaluates to false only the else form is evaluated and the result is returned. If no else form is given, nil will be returned", """
<br /><code>(if test then else?)</code><br /><br />
A control flow structure. First evaluates test. If test evaluates to true, only the then form is evaluated and the result is returned. If test evaluates to false only the else form is evaluated and the result is returned. If no else form is given, nil will be returned.<br />
<br />
The test evaluates to false if its value is false or equal to nil. Every other value evaluates to true. In sense of PHP this means (test != null && test !== false).
<br /><br />
"""),
            DataFunction("if-let", "(if-let bindings then & [else])", PhelCompletionPriority.CONTROL_FLOW, "core", "If test is true, evaluates then with binding-form bound to the value of test,  if not, yields else", """
<br /><code>(if-let bindings then & [else])</code><br /><br />
If test is true, evaluates then with binding-form bound to the value of test,<br />
  if not, yields else
<br /><br />
"""),
            DataFunction("if-not", "(if-not test then & [else])", PhelCompletionPriority.CONTROL_FLOW, "core", "Shorthand for `(if (not condition) else then)`", """
<br /><code>(if-not test then & [else])</code><br /><br />
Shorthand for <b>(if (not condition) else then)</b>.
<br /><br />
"""),
            DataFunction("inc", "(inc x)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Increments `x` by one", """
<br /><code>(inc x)</code><br /><br />
Increments <b>x</b> by one.
<br /><br />
"""),
            DataFunction("indexed?", "(indexed? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is indexed sequence, false otherwise", """
<br /><code>(indexed? x)</code><br /><br />
Returns true if <b>x</b> is indexed sequence, false otherwise.
<br /><br />
"""),
            DataFunction("int?", "(int? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is an integer number, false otherwise", """
<br /><code>(int? x)</code><br /><br />
Returns true if <b>x</b> is an integer number, false otherwise.
<br /><br />
"""),
            DataFunction("interleave", "(interleave & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector with the first items of each col, then the second items, etc", """
<br /><code>(interleave & xs)</code><br /><br />
Returns a vector with the first items of each col, then the second items, etc.
<br /><br />
"""),
            DataFunction("interpose", "(interpose sep xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of elements separated by `sep`", """
<br /><code>(interpose sep xs)</code><br /><br />
Returns a vector of elements separated by <b>sep</b>.
<br /><br />
"""),
            DataFunction("intersection", "(intersection set & sets)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Intersect multiple sets into a new one", """
<br /><code>(intersection set & sets)</code><br /><br />
Intersect multiple sets into a new one.
<br /><br />
"""),
            DataFunction("into", "(into to & rest)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns `to` with all elements of `from` added.   When `from` is associative, it is treated as a sequence of key-value pairs.   Supports persistent and transient collections", """
<br /><code>(into to & rest)</code><br /><br />
Returns <b>to</b> with all elements of <b>from</b> added.<br />
<br />
   When <b>from</b> is associative, it is treated as a sequence of key-value pairs.<br />
   Supports persistent and transient collections.
<br /><br />
"""),
            DataFunction("invert", "(invert map)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a new map where the keys and values are swapped. If map has  duplicated values, some keys will be ignored", """
<br /><code>(invert map)</code><br /><br />
Returns a new map where the keys and values are swapped. If map has<br />
  duplicated values, some keys will be ignored.
<br /><br />
"""),
            DataFunction("iterate", "(iterate f x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns an infinite sequence of x, (f x), (f (f x)), and so on", """
<br /><code>(iterate f x)</code><br /><br />
Returns an infinite sequence of x, (f x), (f (f x)), and so on.
<br /><br />
"""),
            DataFunction("juxt", "(juxt & fs)", PhelCompletionPriority.MACROS, "core", "Takes a list of functions and returns a new function that is the juxtaposition of those functions.  `((juxt a b c) x) => [(a x) (b x) (c x)]`", """
<br /><code>(juxt & fs)</code><br /><br />
Takes a list of functions and returns a new function that is the juxtaposition of those functions.<br />
  <b>((juxt a b c) x) => [(a x) (b x) (c x)]</b>.
<br /><br />
"""),
            DataFunction("keep", "(keep pred xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a list of non-nil results of `(pred x)`", """
<br /><code>(keep pred xs)</code><br /><br />
Returns a list of non-nil results of <b>(pred x)</b>.
<br /><br />
"""),
            DataFunction("keep-indexed", "(keep-indexed pred xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a list of non-nil results of `(pred i x)`", """
<br /><code>(keep-indexed pred xs)</code><br /><br />
Returns a list of non-nil results of <b>(pred i x)</b>.
<br /><br />
"""),
            DataFunction("keys", "(keys xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Gets the keys of an associative data structure", """
<br /><code>(keys xs)</code><br /><br />
Gets the keys of an associative data structure.
<br /><br />
"""),
            DataFunction("keyword", "(keyword x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new Keyword from a given string", """
<br /><code>(keyword x)</code><br /><br />
Creates a new Keyword from a given string.
<br /><br />
"""),
            DataFunction("keyword?", "(keyword? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a keyword, false otherwise", """
<br /><code>(keyword? x)</code><br /><br />
Returns true if <b>x</b> is a keyword, false otherwise.
<br /><br />
"""),
            DataFunction("kvs", "(kvs xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of key-value pairs like `[k1 v1 k2 v2 k3 v3 ...]`", """
<br /><code>(kvs xs)</code><br /><br />
Returns a vector of key-value pairs like <b>[k1 v1 k2 v2 k3 v3 ...]</b>.
<br /><br />
"""),
            DataFunction("last", "(last xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns the last element of `xs` or nil if `xs` is empty or nil", """
<br /><code>(last xs)</code><br /><br />
Returns the last element of <b>xs</b> or nil if <b>xs</b> is empty or nil.
<br /><br />
"""),
            DataFunction("let", "(let [bindings*] expr*)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Creates a new lexical context with assignments defined in bindings. Afterwards the list of expressions is evaluated and the value of the last expression is returned. If no expression is given nil is returned", """
<br /><code>(let [bindings*] expr*)</code><br /><br />
Creates a new lexical context with assignments defined in bindings. Afterwards the list of expressions is evaluated and the value of the last expression is returned. If no expression is given nil is returned.
<br /><br />
"""),
            DataFunction("list", "(list & xs) # '(& xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new list. If no argument is provided, an empty list is created", """
<br /><code>(list & xs) # '(& xs)</code><br /><br />
Creates a new list. If no argument is provided, an empty list is created.
<br /><br />
"""),
            DataFunction("list?", "(list? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a list, false otherwise", """
<br /><code>(list? x)</code><br /><br />
Returns true if <b>x</b> is a list, false otherwise.
<br /><br />
"""),
            DataFunction("loop", "(loop [bindings*] expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Creates a new lexical context with variables defined in bindings and defines a recursion point at the top of the loop", """
<br /><code>(loop [bindings*] expr*)</code><br /><br />
Creates a new lexical context with variables defined in bindings and defines a recursion point at the top of the loop.
<br /><br />
"""),
            DataFunction("macroexpand", "(macroexpand form)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Recursively expands the given form until it is no longer a macro call", """
<br /><code>(macroexpand form)</code><br /><br />
Recursively expands the given form until it is no longer a macro call.
<br /><br />
"""),
            DataFunction("macroexpand-1", "(macroexpand-1 form)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Expands the given form once if it is a macro call", """
<br /><code>(macroexpand-1 form)</code><br /><br />
Expands the given form once if it is a macro call.
<br /><br />
"""),
            DataFunction("map", "(map f & xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns an array consisting of the result of applying `f` to all of the first items in each `xs`,   followed by applying `f` to all the second items in each `xs` until anyone of the `xs` is exhausted", """
<br /><code>(map f & xs)</code><br /><br />
Returns an array consisting of the result of applying <b>f</b> to all of the first items in each <b>xs</b>,<br />
   followed by applying <b>f</b> to all the second items in each <b>xs</b> until anyone of the <b>xs</b> is exhausted.
<br /><br />
"""),
            DataFunction("map-indexed", "(map-indexed f xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Applies `f` to each element in `xs`. `f` is a two-argument function. The first argument  is an index of the element in the sequence and the second element is the element itself", """
<br /><code>(map-indexed f xs)</code><br /><br />
Applies <b>f</b> to each element in <b>xs</b>. <b>f</b> is a two-argument function. The first argument<br />
  is an index of the element in the sequence and the second element is the element itself.
<br /><br />
"""),
            DataFunction("mapcat", "(mapcat f & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Applies `f` on all `xs` and concatenate the result", """
<br /><code>(mapcat f & xs)</code><br /><br />
Applies <b>f</b> on all <b>xs</b> and concatenate the result.
<br /><br />
"""),
            DataFunction("max", "(max & numbers)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the numeric maximum of all numbers", """
<br /><code>(max & numbers)</code><br /><br />
Returns the numeric maximum of all numbers.
<br /><br />
"""),
            DataFunction("mean", "(mean xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the mean of `xs`", """
<br /><code>(mean xs)</code><br /><br />
Returns the mean of <b>xs</b>.
<br /><br />
"""),
            DataFunction("median", "(median xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the median of `xs`", """
<br /><code>(median xs)</code><br /><br />
Returns the median of <b>xs</b>.
<br /><br />
"""),
            DataFunction("memoize", "(memoize f)", PhelCompletionPriority.MACROS, "core", "Returns a memoized version of the function `f`. The memoized function  caches the return value for each set of arguments", """
<br /><code>(memoize f)</code><br /><br />
Returns a memoized version of the function <b>f</b>. The memoized function<br />
  caches the return value for each set of arguments.
<br /><br />
"""),
            DataFunction("merge", "(merge & maps)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Merges multiple maps into one new map. If a key appears in more than one  collection, then later values replace any previous ones", """
<br /><code>(merge & maps)</code><br /><br />
Merges multiple maps into one new map. If a key appears in more than one<br />
  collection, then later values replace any previous ones.
<br /><br />
"""),
            DataFunction("merge-with", "(merge-with f & hash-maps)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Merges multiple maps into one new map. If a key appears in more than one   collection, the result of `(f current-val next-val)` is used", """
<br /><code>(merge-with f & hash-maps)</code><br /><br />
Merges multiple maps into one new map. If a key appears in more than one<br />
   collection, the result of <b>(f current-val next-val)</b> is used.
<br /><br />
"""),
            DataFunction("meta", "(meta obj)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Gets the metadata of the given object or definition", """
<br /><code>(meta obj)</code><br /><br />
Gets the metadata of the given object or definition.
<br /><br />
"""),
            DataFunction("min", "(min & numbers)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the numeric minimum of all numbers", """
<br /><code>(min & numbers)</code><br /><br />
Returns the numeric minimum of all numbers.
<br /><br />
"""),
            DataFunction("name", "(name x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the name string of a string, keyword or symbol", """
<br /><code>(name x)</code><br /><br />
Returns the name string of a string, keyword or symbol.
<br /><br />
"""),
            DataFunction("namespace", "(namespace x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Return the namespace string of a symbol or keyword. Nil if not present", """
<br /><code>(namespace x)</code><br /><br />
Return the namespace string of a symbol or keyword. Nil if not present.
<br /><br />
"""),
            DataFunction("nan?", "(nan? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is not a number", """
<br /><code>(nan? x)</code><br /><br />
Checks if <b>x</b> is not a number.
<br /><br />
"""),
            DataFunction("neg?", "(neg? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is smaller than zero", """
<br /><code>(neg? x)</code><br /><br />
Checks if <b>x</b> is smaller than zero.
<br /><br />
"""),
            DataFunction("next", "(next xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the sequence of elements after the first element. If there are no elements, returns nil", """
<br /><code>(next xs)</code><br /><br />
Returns the sequence of elements after the first element. If there are no elements, returns nil.
<br /><br />
"""),
            DataFunction("nfirst", "(nfirst xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as `(next (first xs))`", """
<br /><code>(nfirst xs)</code><br /><br />
Same as <b>(next (first xs))</b>.
<br /><br />
"""),
            DataFunction("nil?", "(nil? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is nil, false otherwise", """
<br /><code>(nil? x)</code><br /><br />
Returns true if <b>x</b> is nil, false otherwise.
<br /><br />
"""),
            DataFunction("nnext", "(nnext xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as `(next (next xs))`", """
<br /><code>(nnext xs)</code><br /><br />
Same as <b>(next (next xs))</b>.
<br /><br />
"""),
            DataFunction("not", "(not x)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "The `not` function returns `true` if the given value is logical false and `false` otherwise", """
<br /><code>(not x)</code><br /><br />
The <b>not</b> function returns <b>true</b> if the given value is logical false and <b>false</b> otherwise.
<br /><br />
"""),
            DataFunction("not-any?", "(not-any? pred xs)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `(pred x)` is logical false for every `x` in `xs`   or if `xs` is empty. Otherwise returns false", """
<br /><code>(not-any? pred xs)</code><br /><br />
Returns true if <b>(pred x)</b> is logical false for every <b>x</b> in <b>xs</b><br />
   or if <b>xs</b> is empty. Otherwise returns false.
<br /><br />
"""),
            DataFunction("not-empty", "(not-empty coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns `coll` if it contains elements, otherwise nil", """
<br /><code>(not-empty coll)</code><br /><br />
Returns <b>coll</b> if it contains elements, otherwise nil.
<br /><br />
"""),
            DataFunction("not-every?", "(not-every? pred xs)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns false if `(pred x)` is logical true for every `x` in collection `xs`   or if `xs` is empty. Otherwise returns true", """
<br /><code>(not-every? pred xs)</code><br /><br />
Returns false if <b>(pred x)</b> is logical true for every <b>x</b> in collection <b>xs</b><br />
   or if <b>xs</b> is empty. Otherwise returns true.
<br /><br />
"""),
            DataFunction("not=", "(not= a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if all values are unequal. Same as `a != b` in PHP", """
<br /><code>(not= a & more)</code><br /><br />
Checks if all values are unequal. Same as <b>a != b</b> in PHP.
<br /><br />
"""),
            DataFunction("ns", "(ns name imports*)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Defines the namespace for the current file and adds imports to the environment. Imports can either be uses or requires. The keyword :use is used to import PHP classes, the keyword :require is used to import Phel modules and the keyword :require-file is used to load php files", """
<br /><code>(ns name imports*)</code><br /><br />
Defines the namespace for the current file and adds imports to the environment. Imports can either be uses or requires. The keyword <b>:use</b> is used to import PHP classes, the keyword <b>:require</b> is used to import Phel modules and the keyword <b>:require-file</b> is used to load php files.
<br /><br />
"""),
            DataFunction("number?", "(number? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a number, false otherwise", """
<br /><code>(number? x)</code><br /><br />
Returns true if <b>x</b> is a number, false otherwise.
<br /><br />
"""),
            DataFunction("odd?", "(odd? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is odd", """
<br /><code>(odd? x)</code><br /><br />
Checks if <b>x</b> is odd.
<br /><br />
"""),
            DataFunction("one?", "(one? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is one", """
<br /><code>(one? x)</code><br /><br />
Checks if <b>x</b> is one.
<br /><br />
"""),
            DataFunction("or", "(or & args)", PhelCompletionPriority.MACROS, "core", "Evaluates each expression one at a time, from left to right. If a formreturns a logical true value, or returns that value and doesn't evaluate any ofthe other expressions, otherwise, it returns the value of the last expression.Calling or without arguments, returns nil", """
<br /><code>(or & args)</code><br /><br />
Evaluates each expression one at a time, from left to right. If a form<br />
returns a logical true value, or returns that value and doesn't evaluate any of<br />
the other expressions, otherwise, it returns the value of the last expression.<br />
Calling or without arguments, returns nil.
<br /><br />
"""),
            DataFunction("pairs", "(pairs xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Gets the pairs of an associative data structure", """
<br /><code>(pairs xs)</code><br /><br />
Gets the pairs of an associative data structure.
<br /><br />
"""),
            DataFunction("partial", "(partial f & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes a function `f` and fewer than normal arguments of `f` and returns a function  that a variable number of additional arguments. When call `f` will be called  with `args` and the additional arguments", """
<br /><code>(partial f & args)</code><br /><br />
Takes a function <b>f</b> and fewer than normal arguments of <b>f</b> and returns a function<br />
  that a variable number of additional arguments. When call <b>f</b> will be called<br />
  with <b>args</b> and the additional arguments.
<br /><br />
"""),
            DataFunction("partition", "(partition n xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Partition an indexed data structure into vectors of maximum size n. Returns a new vector", """
<br /><code>(partition n xs)</code><br /><br />
Partition an indexed data structure into vectors of maximum size n. Returns a new vector.
<br /><br />
"""),
            DataFunction("partition-by", "(partition-by f xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Applies `f` to each value in `xs`, splitting them each time the return value changes", """
<br /><code>(partition-by f xs)</code><br /><br />
Applies <b>f</b> to each value in <b>xs</b>, splitting them each time the return value changes.
<br /><br />
"""),
            DataFunction("peek", "(peek xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the last element of a sequence", """
<br /><code>(peek xs)</code><br /><br />
Returns the last element of a sequence.
<br /><br />
"""),
            DataFunction("persistent", "(persistent coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Converts a transient collection to a persistent collection", """
<br /><code>(persistent coll)</code><br /><br />
Converts a transient collection to a persistent collection.
<br /><br />
"""),
            DataFunction("phel->php", "(phel->php x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Recursively converts a Phel data structure to a PHP array", """
<br /><code>(phel->php x)</code><br /><br />
Recursively converts a Phel data structure to a PHP array.
<br /><br />
"""),
            DataFunction("php->phel", "(php->phel x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Recursively converts a PHP array to Phel data structures", """
<br /><code>(php->phel x)</code><br /><br />
Recursively converts a PHP array to Phel data structures.
<br /><br />
"""),
            DataFunction("php-array-to-map", "(php-array-to-map arr)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Converts a PHP Array to a map", """
<br /><code>(php-array-to-map arr)</code><br /><br />
Converts a PHP Array to a map.
<br /><br />
"""),
            DataFunction("php-array?", "(php-array? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a PHP Array, false otherwise", """
<br /><code>(php-array? x)</code><br /><br />
Returns true if <b>x</b> is a PHP Array, false otherwise.
<br /><br />
"""),
            DataFunction("php-associative-array", "(php-associative-array & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a PHP associative array. An even number of parameters must be provided", """
<br /><code>(php-associative-array & xs)</code><br /><br />
Creates a PHP associative array. An even number of parameters must be provided.
<br /><br />
"""),
            DataFunction("php-indexed-array", "(php-indexed-array & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a PHP indexed array from the given values", """
<br /><code>(php-indexed-array & xs)</code><br /><br />
Creates a PHP indexed array from the given values.
<br /><br />
"""),
            DataFunction("php-object?", "(php-object? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a PHP object, false otherwise", """
<br /><code>(php-object? x)</code><br /><br />
Returns true if <b>x</b> is a PHP object, false otherwise.
<br /><br />
"""),
            DataFunction("php-resource?", "(php-resource? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a PHP resource, false otherwise", """
<br /><code>(php-resource? x)</code><br /><br />
Returns true if <b>x</b> is a PHP resource, false otherwise.
<br /><br />
"""),
            DataFunction("pop", "(pop xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Removes the last element of the array `xs`. If the array is empty returns nil", """
<br /><code>(pop xs)</code><br /><br />
Removes the last element of the array <b>xs</b>. If the array is empty returns nil.
<br /><br />
"""),
            DataFunction("pos?", "(pos? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is greater than zero", """
<br /><code>(pos? x)</code><br /><br />
Checks if <b>x</b> is greater than zero.
<br /><br />
"""),
            DataFunction("print", "(print & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Prints the given values to the default output stream. Returns nil", """
<br /><code>(print & xs)</code><br /><br />
Prints the given values to the default output stream. Returns nil.
<br /><br />
"""),
            DataFunction("print-str", "(print-str & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as print. But instead of writing it to an output stream, the resulting string is returned", """
<br /><code>(print-str & xs)</code><br /><br />
Same as print. But instead of writing it to an output stream, the resulting string is returned.
<br /><br />
"""),
            DataFunction("printf", "(printf fmt & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Output a formatted string. See PHP's  for more information", """
<br /><code>(printf fmt & xs)</code><br /><br />
Output a formatted string. See PHP's <a href=\"https://www.php.net/manual/en/function.printf.php\">printf</a> for more information.
<br /><br />
"""),
            DataFunction("println", "(println & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as print followed by a newline", """
<br /><code>(println & xs)</code><br /><br />
Same as print followed by a newline.
<br /><br />
"""),
            DataFunction("push", "(push xs x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Inserts `x` at the end of the sequence `xs`", """
<br /><code>(push xs x)</code><br /><br />
Inserts <b>x</b> at the end of the sequence <b>xs</b>.
<br /><br />
"""),
            DataFunction("put", "(put ds key value)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Puts `value` mapped to `key` on the datastructure `ds`. Returns `ds`", """
<br /><code>(put ds key value)</code><br /><br />
Puts <b>value</b> mapped to <b>key</b> on the datastructure <b>ds</b>. Returns <b>ds</b>.
<br /><br />
"""),
            DataFunction("put-in", "(put-in ds [k & ks] v)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Puts a value into a nested data structure", """
<br /><code>(put-in ds [k & ks] v)</code><br /><br />
Puts a value into a nested data structure.
<br /><br />
"""),
            DataFunction("quote", "(NAME_QUOTE)", PhelCompletionPriority.SPECIAL_FORMS, "core", "NAME_QUOTE description", """
<br /><code>(NAME_QUOTE)</code><br />
<br /><br />
"""),
            DataFunction("rand", "(rand )", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a random number between 0 and 1", """
<br /><code>(rand )</code><br /><br />
Returns a random number between 0 and 1.
<br /><br />
"""),
            DataFunction("rand-int", "(rand-int n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a random number between 0 and `n`", """
<br /><code>(rand-int n)</code><br /><br />
Returns a random number between 0 and <b>n</b>.
<br /><br />
"""),
            DataFunction("rand-nth", "(rand-nth xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a random item from xs", """
<br /><code>(rand-nth xs)</code><br /><br />
Returns a random item from xs.
<br /><br />
"""),
            DataFunction("range", "(range a & rest)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Create an array of values `[start, end)`. If the function has one argument then  the range `[0, end)` is returned. With two arguments, returns `[start, end)`.  The third argument is an optional step width (default 1)", """
<br /><code>(range a & rest)</code><br /><br />
Create an array of values <b>[start, end)</b>. If the function has one argument then<br />
  the range <b>[0, end)</b> is returned. With two arguments, returns <b>[start, end)</b>.<br />
  The third argument is an optional step width (default 1).
<br /><br />
"""),
            DataFunction("re-seq", "(re-seq re s)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a sequence of successive matches of pattern in string", """
<br /><code>(re-seq re s)</code><br /><br />
Returns a sequence of successive matches of pattern in string.
<br /><br />
"""),
            DataFunction("read-string", "(read-string s)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Reads the first phel expression from the string s", """
<br /><code>(read-string s)</code><br /><br />
Reads the first phel expression from the string s.
<br /><br />
"""),
            DataFunction("recur", "(recur expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Internally recur is implemented as a PHP while loop and therefore prevents the Maximum function nesting level errors", """
<br /><code>(recur expr*)</code><br /><br />
Internally recur is implemented as a PHP while loop and therefore prevents the Maximum function nesting level errors.
<br /><br />
"""),
            DataFunction("reduce", "(reduce f & xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "(reduce f coll) (reduce f val coll)    f should be a function of 2 arguments. If val is not supplied, returns the result of applying f to the first 2 items in coll, then applying f to that result and the 3rd item, etc.    If coll contains no items, f must accept no arguments as well, and reduce returns the result of calling f with no arguments. If coll has only 1 item, it is returned and f is not called.    If val is supplied, returns the result of applying f to val and the first item in coll, then applying f to that result and the 2nd item, etc. If coll contains no items, returns val and f is not called", """
<br /><code>(reduce f & xs)</code><br /><br />
(reduce f coll) (reduce f val coll)<br />
    f should be a function of 2 arguments. If val is not supplied, returns the result of applying f to the first 2 items in coll, then applying f to that result and the 3rd item, etc.<br />
    If coll contains no items, f must accept no arguments as well, and reduce returns the result of calling f with no arguments. If coll has only 1 item, it is returned and f is not called.<br />
    If val is supplied, returns the result of applying f to val and the first item in coll, then applying f to that result and the 2nd item, etc. If coll contains no items, returns val and f is not called.
<br /><br />
"""),
            DataFunction("remove", "(remove xs offset & [n])", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Removes up to `n` element from array `xs` starting at index `offset`", """
<br /><code>(remove xs offset & [n])</code><br /><br />
Removes up to <b>n</b> element from array <b>xs</b> starting at index <b>offset</b>.
<br /><br />
"""),
            DataFunction("repeat", "(repeat a & rest)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of length n where every element is x.  With one argument returns an infinite sequence of x", """
<br /><code>(repeat a & rest)</code><br /><br />
Returns a vector of length n where every element is x.<br />
  With one argument returns an infinite sequence of x.
<br /><br />
"""),
            DataFunction("repeatedly", "(repeatedly a & rest)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of length n with values produced by repeatedly calling f.  With one argument returns an infinite sequence of calls to f", """
<br /><code>(repeatedly a & rest)</code><br /><br />
Returns a vector of length n with values produced by repeatedly calling f.<br />
  With one argument returns an infinite sequence of calls to f.
<br /><br />
"""),
            DataFunction("rest", "(rest xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns the sequence of elements after the first element. If there are no elements, returns an empty sequence", """
<br /><code>(rest xs)</code><br /><br />
Returns the sequence of elements after the first element. If there are no elements, returns an empty sequence.
<br /><br />
"""),
            DataFunction("reverse", "(reverse xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Reverses the order of the elements in the given sequence", """
<br /><code>(reverse xs)</code><br /><br />
Reverses the order of the elements in the given sequence.
<br /><br />
"""),
            DataFunction("second", "(second xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the second element of an indexed sequence or nil", """
<br /><code>(second xs)</code><br /><br />
Returns the second element of an indexed sequence or nil.
<br /><br />
"""),
            DataFunction("select-keys", "(select-keys m ks)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a new map including key value pairs from `m` selected with of keys `ks`", """
<br /><code>(select-keys m ks)</code><br /><br />
Returns a new map including key value pairs from <b>m</b> selected with of keys <b>ks</b>.
<br /><br />
"""),
            DataFunction("set", "(set & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new Set. If no argument is provided, an empty Set is created", """
<br /><code>(set & xs)</code><br /><br />
Creates a new Set. If no argument is provided, an empty Set is created.
<br /><br />
"""),
            DataFunction("set!", "(set! variable value)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Sets a new value to the given variable", """
<br /><code>(set! variable value)</code><br /><br />
Sets a new value to the given variable.
<br /><br />
"""),
            DataFunction("set-meta!", "(set-meta! obj)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Sets the metadata to a given object", """
<br /><code>(set-meta! obj)</code><br /><br />
Sets the metadata to a given object.
<br /><br />
"""),
            DataFunction("set-var", "(var value)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Variables provide a way to manage mutable state. Each variable contains a single value. To create a variable use the var function", """
<br /><code>(var value)</code><br /><br />
Variables provide a way to manage mutable state. Each variable contains a single value. To create a variable use the var function.
<br /><br />
"""),
            DataFunction("set?", "(set? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a set, false otherwise", """
<br /><code>(set? x)</code><br /><br />
Returns true if <b>x</b> is a set, false otherwise.
<br /><br />
"""),
            DataFunction("shuffle", "(shuffle xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a random permutation of xs", """
<br /><code>(shuffle xs)</code><br /><br />
Returns a random permutation of xs.
<br /><br />
"""),
            DataFunction("slice", "(slice xs & [offset & [length]])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Extract a slice of `xs`", """
<br /><code>(slice xs & [offset & [length]])</code><br /><br />
Extract a slice of <b>xs</b>.
<br /><br />
"""),
            DataFunction("slurp", "(slurp filename & [opts])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Reads entire file into a string. Accepts `opts` map for overriding default  PHP file_get_contents arguments.  See PHP's  for more information", """
<br /><code>(slurp filename & [opts])</code><br /><br />
Reads entire file into a string. Accepts <b>opts</b> map for overriding default<br />
  PHP file_get_contents arguments.<br />
  See PHP's <a href=\"https://www.php.net/manual/en/function.file-get-contents.php\">file_get_contents</a> for more information.
<br /><br />
"""),
            DataFunction("some", "(some pred xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the first logical true value of `(pred x)` for any `x` in `xs`, else nil", """
<br /><code>(some pred xs)</code><br /><br />
Returns the first logical true value of <b>(pred x)</b> for any <b>x</b> in <b>xs</b>, else nil.
<br /><br />
"""),
            DataFunction("some->", "(some-> x & forms)", PhelCompletionPriority.MACROS, "core", "Threads `x` through the forms like `->` but stops when a form returns `nil`", """
<br /><code>(some-> x & forms)</code><br /><br />
Threads <b>x</b> through the forms like <b>-></b> but stops when a form returns <b>nil</b>.
<br /><br />
"""),
            DataFunction("some->>", "(some->> x & forms)", PhelCompletionPriority.MACROS, "core", "Threads `x` through the forms like `->>` but stops when a form returns `nil`", """
<br /><code>(some->> x & forms)</code><br /><br />
Threads <b>x</b> through the forms like <b>->></b> but stops when a form returns <b>nil</b>.
<br /><br />
"""),
            DataFunction("some?", "(some? pred xs)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `(pred x)` is logical true for at least one `x` in `xs`, else false", """
<br /><code>(some? pred xs)</code><br /><br />
Returns true if <b>(pred x)</b> is logical true for at least one <b>x</b> in <b>xs</b>, else false.
<br /><br />
"""),
            DataFunction("sort", "(sort xs & [comp])", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns a sorted vector. If no comparator is supplied compare is used", """
<br /><code>(sort xs & [comp])</code><br /><br />
Returns a sorted vector. If no comparator is supplied compare is used.
<br /><br />
"""),
            DataFunction("sort-by", "(sort-by keyfn xs & [comp])", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns a sorted vector where the sort order is determined by comparing  `(keyfn item)`. If no comparator is supplied compare is used", """
<br /><code>(sort-by keyfn xs & [comp])</code><br /><br />
Returns a sorted vector where the sort order is determined by comparing<br />
  <b>(keyfn item)</b>. If no comparator is supplied compare is used.
<br /><br />
"""),
            DataFunction("spit", "(spit filename data & [opts])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Writes data to file, returning number of bytest that were written or `nil`  on failure. Accepts `opts` map for overriding default PHP file_put_contents  arguments, as example to append to file use {:flags php/FILE_APPEND}.  See PHP's  for more information", """
<br /><code>(spit filename data & [opts])</code><br /><br />
Writes data to file, returning number of bytest that were written or <b>nil</b><br />
  on failure. Accepts <b>opts</b> map for overriding default PHP file_put_contents<br />
  arguments, as example to append to file use <code>{:flags php/FILE_APPEND}</code>.<br />
  See PHP's <a href=\"https://www.php.net/manual/en/function.file-put-contents.php\">file_put_contents</a> for more information.
<br /><br />
"""),
            DataFunction("split-at", "(split-at n xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of `[(take n coll) (drop n coll)]`", """
<br /><code>(split-at n xs)</code><br /><br />
Returns a vector of <b>[(take n coll) (drop n coll)]</b>.
<br /><br />
"""),
            DataFunction("split-with", "(split-with f xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of `[(take-while pred coll) (drop-while pred coll)]`", """
<br /><code>(split-with f xs)</code><br /><br />
Returns a vector of <b>[(take-while pred coll) (drop-while pred coll)]</b>.
<br /><br />
"""),
            DataFunction("str", "(str & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a string by concatenating values together. If no arguments areprovided an empty string is returned. Nil and false are represented as an emptystring. True is represented as 1. Otherwise, it tries to call `__toString`.This is PHP equivalent to `${'$'}args[0] . ${'$'}args[1] . ${'$'}args[2] ...`", """
<br /><code>(str & args)</code><br /><br />
Creates a string by concatenating values together. If no arguments are<br />
provided an empty string is returned. Nil and false are represented as an empty<br />
string. True is represented as 1. Otherwise, it tries to call <b>__toString</b>.<br />
This is PHP equivalent to <b>${'$'}args[0] . ${'$'}args[1] . ${'$'}args[2] ...</b>.
<br /><br />
"""),
            DataFunction("str-contains?", "(str-contains? str s)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if str contains s", """
<br /><code>(str-contains? str s)</code><br /><br />
Returns true if str contains s.
<br /><br />
"""),
            DataFunction("string?", "(string? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a string, false otherwise", """
<br /><code>(string? x)</code><br /><br />
Returns true if <b>x</b> is a string, false otherwise.
<br /><br />
"""),
            DataFunction("struct?", "(struct? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a struct, false otherwise", """
<br /><code>(struct? x)</code><br /><br />
Returns true if <b>x</b> is a struct, false otherwise.
<br /><br />
"""),
            DataFunction("sum", "(sum xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the sum of all elements is `xs`", """
<br /><code>(sum xs)</code><br /><br />
Returns the sum of all elements is <b>xs</b>.
<br /><br />
"""),
            DataFunction("swap!", "(swap! variable f & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Swaps the value of the variable to `(apply f current-value args)`. Returns the values that are swapped in", """
<br /><code>(swap! variable f & args)</code><br /><br />
Swaps the value of the variable to <b>(apply f current-value args)</b>. Returns the values that are swapped in.
<br /><br />
"""),
            DataFunction("symbol", "(symbol name-or-ns & [name])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a new symbol for given string with optional namespace.   Arity-1 returns a symbol without namespace. Arity-2 returns a symbol in given namespace", """
<br /><code>(symbol name-or-ns & [name])</code><br /><br />
Returns a new symbol for given string with optional namespace.<br />
   Arity-1 returns a symbol without namespace. Arity-2 returns a symbol in given namespace.
<br /><br />
"""),
            DataFunction("symbol?", "(symbol? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a symbol, false otherwise", """
<br /><code>(symbol? x)</code><br /><br />
Returns true if <b>x</b> is a symbol, false otherwise.
<br /><br />
"""),
            DataFunction("symmetric-difference", "(symmetric-difference set & sets)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Symmetric difference between multiple sets into a new one", """
<br /><code>(symmetric-difference set & sets)</code><br /><br />
Symmetric difference between multiple sets into a new one.
<br /><br />
"""),
            DataFunction("take", "(take n xs)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Takes the first `n` elements of `xs`", """
<br /><code>(take n xs)</code><br /><br />
Takes the first <b>n</b> elements of <b>xs</b>.
<br /><br />
"""),
            DataFunction("take-last", "(take-last n xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes the last `n` elements of `xs`", """
<br /><code>(take-last n xs)</code><br /><br />
Takes the last <b>n</b> elements of <b>xs</b>.
<br /><br />
"""),
            DataFunction("take-nth", "(take-nth n xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns every nth item in `xs`", """
<br /><code>(take-nth n xs)</code><br /><br />
Returns every nth item in <b>xs</b>.
<br /><br />
"""),
            DataFunction("take-while", "(take-while pred xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes all elements at the front of `xs` where `(pred x)` is true", """
<br /><code>(take-while pred xs)</code><br /><br />
Takes all elements at the front of <b>xs</b> where <b>(pred x)</b> is true.
<br /><br />
"""),
            DataFunction("throw", "(throw exception)", PhelCompletionPriority.CONTROL_FLOW, "core", "Throw an exception", """
<br /><code>(throw exception)</code><br /><br />
Throw an exception.<br />
See <a href=\"/documentation/control-flow/#try-catch-and-finally\">try-catch</a>.
<br /><br />
"""),
            DataFunction("time", "(time expr)", PhelCompletionPriority.MACROS, "core", "Evaluates expr and prints the time it took. Returns the value of expr", """
<br /><code>(time expr)</code><br /><br />
Evaluates expr and prints the time it took. Returns the value of expr.
<br /><br />
"""),
            DataFunction("to-php-array", "(to-php-array xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Create a PHP Array from a sequential data structure", """
<br /><code>(to-php-array xs)</code><br /><br />
Create a PHP Array from a sequential data structure.
<br /><br />
"""),
            DataFunction("transient", "(transient coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Converts a persistent collection to a transient collection", """
<br /><code>(transient coll)</code><br /><br />
Converts a persistent collection to a transient collection.
<br /><br />
"""),
            DataFunction("tree-seq", "(tree-seq branch? children root)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of the nodes in the tree, via a depth-first walk.  branch? is a function with one argument that returns true if the given node  has children.  children must be a function with one argument that returns the children of the node.  root the root node of the tree", """
<br /><code>(tree-seq branch? children root)</code><br /><br />
Returns a vector of the nodes in the tree, via a depth-first walk.<br />
  branch? is a function with one argument that returns true if the given node<br />
  has children.<br />
  children must be a function with one argument that returns the children of the node.<br />
  root the root node of the tree.
<br /><br />
"""),
            DataFunction("true?", "(true? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is true. Same as `x === true` in PHP", """
<br /><code>(true? x)</code><br /><br />
Checks if <b>x</b> is true. Same as <b>x === true</b> in PHP.
<br /><br />
"""),
            DataFunction("truthy?", "(truthy? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is truthy. Same as `x == true` in PHP", """
<br /><code>(truthy? x)</code><br /><br />
Checks if <b>x</b> is truthy. Same as <b>x == true</b> in PHP.
<br /><br />
"""),
            DataFunction("try", "(try expr* catch-clause* finally-clause?)", PhelCompletionPriority.CONTROL_FLOW, "core", "All expressions are evaluated and if no exception is thrown the value of the last expression is returned. If an exception occurs and a matching catch-clause is provided, its expression is evaluated and the value is returned. If no matching catch-clause can be found the exception is propagated out of the function. Before returning normally or abnormally the optionally finally-clause is evaluated", """
<br /><code>(try expr* catch-clause* finally-clause?)</code><br /><br />
All expressions are evaluated and if no exception is thrown the value of the last expression is returned. If an exception occurs and a matching catch-clause is provided, its expression is evaluated and the value is returned. If no matching catch-clause can be found the exception is propagated out of the function. Before returning normally or abnormally the optionally finally-clause is evaluated.
<br /><br />
"""),
            DataFunction("type", "(type x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the type of `x`. The following types can be returned:* `:vector`* `:list`* `:struct`* `:hash-map`* `:set`* `:keyword`* `:symbol`* `:var`* `:int`* `:float`* `:string`* `:nil`* `:boolean`* `:function`* `:php/array`* `:php/resource`* `:php/object`* `:unknown`", """
<br /><code>(type x)</code><br /><br />
Returns the type of <b>x</b>. The following types can be returned:<br />
<br />
* <b>:vector</b><br />
* <b>:list</b><br />
* <b>:struct</b><br />
* <b>:hash-map</b><br />
* <b>:set</b><br />
* <b>:keyword</b><br />
* <b>:symbol</b><br />
* <b>:var</b><br />
* <b>:int</b><br />
* <b>:float</b><br />
* <b>:string</b><br />
* <b>:nil</b><br />
* <b>:boolean</b><br />
* <b>:function</b><br />
* <b>:php/array</b><br />
* <b>:php/resource</b><br />
* <b>:php/object</b><br />
* <b>:unknown</b>
<br /><br />
"""),
            DataFunction("union", "(union & sets)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Union multiple sets into a new one", """
<br /><code>(union & sets)</code><br /><br />
Union multiple sets into a new one.
<br /><br />
"""),
            DataFunction("unquote", "(unquote my-sym)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Values that should be evaluated in a macro are marked with the unquote function (shorthand ,)", """
<br /><code>(unquote my-sym) # Evaluates to my-sym<br />
,my-sym          # Shorthand for (same as above)</code><br /><br />
Values that should be evaluated in a macro are marked with the unquote function (shorthand <b>,</b>).
<br /><br />
"""),
            DataFunction("unquote-splicing", "(unquote-splicing my-sym)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Values that should be evaluated in a macro are marked with the unquote function (shorthand ,@)", """
<br /><code>(unquote-splicing my-sym) # Evaluates to my-sym<br />
,@my-sym                  # Shorthand for (same as above)</code><br /><br />
Values that should be evaluated in a macro are marked with the unquote function (shorthand <b>,@</b>).
<br /><br />
"""),
            DataFunction("unset", "(unset ds key)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns `ds` without `key`", """
<br /><code>(unset ds key)</code><br /><br />
Returns <b>ds</b> without <b>key</b>.
<br /><br />
"""),
            DataFunction("unset-in", "(unset-in ds [k & ks])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Removes a value from a nested data structure", """
<br /><code>(unset-in ds [k & ks])</code><br /><br />
Removes a value from a nested data structure.
<br /><br />
"""),
            DataFunction("update", "(update ds k f & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Updates a value in a datastructure by applying `f` to the current element and replacing it with the result of `f`", """
<br /><code>(update ds k f & args)</code><br /><br />
Updates a value in a datastructure by applying <b>f</b> to the current element and replacing it with the result of <b>f</b>.
<br /><br />
"""),
            DataFunction("update-in", "(update-in ds [k & ks] f & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Updates a value into a nested data structure", """
<br /><code>(update-in ds [k & ks] f & args)</code><br /><br />
Updates a value into a nested data structure.
<br /><br />
"""),
            DataFunction("values", "(values xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Gets the values of an associative data structure", """
<br /><code>(values xs)</code><br /><br />
Gets the values of an associative data structure.
<br /><br />
"""),
            DataFunction("var", "(var value)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Creates a new variable with the given value", """
<br /><code>(var value)</code><br /><br />
Creates a new variable with the given value.
<br /><br />
"""),
            DataFunction("var?", "(var? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if the given value is a variable", """
<br /><code>(var? x)</code><br /><br />
Checks if the given value is a variable.
<br /><br />
"""),
            DataFunction("vector", "(vector & xs) # [& xs]", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new vector. If no argument is provided, an empty vector is created", """
<br /><code>(vector & xs) # [& xs]</code><br /><br />
Creates a new vector. If no argument is provided, an empty vector is created.
<br /><br />
"""),
            DataFunction("vector?", "(vector? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a vector, false otherwise", """
<br /><code>(vector? x)</code><br /><br />
Returns true if <b>x</b> is a vector, false otherwise.
<br /><br />
"""),
            DataFunction("when", "(when test & body)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates `test` and if that is logical true, evaluates `body`", """
<br /><code>(when test & body)</code><br /><br />
Evaluates <b>test</b> and if that is logical true, evaluates <b>body</b>.
<br /><br />
"""),
            DataFunction("when-let", "(when-let bindings & body)", PhelCompletionPriority.CONTROL_FLOW, "core", "When test is true, evaluates body with binding-form bound to the value of test", """
<br /><code>(when-let bindings & body)</code><br /><br />
When test is true, evaluates body with binding-form bound to the value of test
<br /><br />
"""),
            DataFunction("when-not", "(when-not test & body)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates `test` and if that is logical false, evaluates `body`", """
<br /><code>(when-not test & body)</code><br /><br />
Evaluates <b>test</b> and if that is logical false, evaluates <b>body</b>.
<br /><br />
"""),
            DataFunction("with-output-buffer", "(with-output-buffer & body)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Everything that is printed inside the body will be stored in a buffer.   The result of the buffer is returned", """
<br /><code>(with-output-buffer & body)</code><br /><br />
Everything that is printed inside the body will be stored in a buffer.<br />
   The result of the buffer is returned.
<br /><br />
"""),
            DataFunction("zero?", "(zero? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is zero", """
<br /><code>(zero? x)</code><br /><br />
Checks if <b>x</b> is zero.
<br /><br />
"""),
            DataFunction("zipcoll", "(zipcoll a b)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a map from two sequential data structures. Return a new map", """
<br /><code>(zipcoll a b)</code><br /><br />
Creates a map from two sequential data structures. Return a new map.
<br /><br />
"""),
            DataFunction("zipmap", "(zipmap keys vals)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a new map with the keys mapped to the corresponding values. Stops when the shorter of `keys` or `vals` is exhausted", """
<br /><code>(zipmap keys vals)</code><br /><br />
Returns a new map with the keys mapped to the corresponding values. Stops when the shorter of <b>keys</b> or <b>vals</b> is exhausted.
<br /><br />
"""),
        )
    }

    private fun registerDebugFunctions() {
        functions[Namespace.DEBUG] = listOf(
            DataFunction("debug/dbg", "(dbg expr)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Evaluates `expr`, prints the expression and the resulting value.   Returns the value of `expr`", """
<br /><code>(dbg expr)</code><br /><br />
Evaluates <b>expr</b>, prints the expression and the resulting value.<br />
   Returns the value of <b>expr</b>.
<br /><br />
"""),
            DataFunction("debug/dotrace", "(dotrace name f)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Wrap `f` so each call and result are printed with indentation", """
<br /><code>(dotrace name f)</code><br /><br />
Wrap <b>f</b> so each call and result are printed with indentation.
<br /><br />
"""),
            DataFunction("debug/reset-trace-state!", "(reset-trace-state! )", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Resets the internal counters used for tracing", """
<br /><code>(reset-trace-state! )</code><br /><br />
Resets the internal counters used for tracing.
<br /><br />
"""),
            DataFunction("debug/set-trace-id-padding!", "(set-trace-id-padding! estimated-id-padding)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Sets the number of digits used to left-pad trace IDs.  Call this once before tracing begins to ensure aligned output", """
<br /><code>(set-trace-id-padding! estimated-id-padding)</code><br /><br />
Sets the number of digits used to left-pad trace IDs.<br />
  Call this once before tracing begins to ensure aligned output.
<br /><br />
"""),
            DataFunction("debug/spy", "(spy expr)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Evaluates `expr`, prints the resulting value with an optional label, and returns it", """
<br /><code>(spy expr)</code><br /><br />
Evaluates <b>expr</b>, prints the resulting value with an optional label, and returns it.
<br /><br />
"""),
            DataFunction("debug/tap", "(tap value)", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug", "Executes optional side-effects on `value` and returns it unchanged.  Without a handler function the value is printed using `print-str`", """
<br /><code>(tap value)</code><br /><br />
Executes optional side-effects on <b>value</b> and returns it unchanged.<br />
  Without a handler function the value is printed using <b>print-str</b>.
<br /><br />
"""),
        )
    }

    private fun registerHtmlFunctions() {
        functions[Namespace.HTML] = listOf(
            DataFunction("html/doctype", "(doctype type)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "", """
<br /><code>(doctype type)</code><br /><br />

<br /><br />
"""),
            DataFunction("html/escape-html", "(escape-html s)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "Escapes the string that may contain HTML", """
<br /><code>(escape-html s)</code><br /><br />
Escapes the string that may contain HTML.
<br /><br />
"""),
            DataFunction("html/html", "(html & content)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "", """
<br /><code>(html & content)</code><br /><br />

<br /><br />
"""),
            DataFunction("html/raw-string", "(raw-string s)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "Creates a new raw-string struct", """
<br /><code>(raw-string s)</code><br /><br />
Creates a new raw-string struct.
<br /><br />
"""),
            DataFunction("html/raw-string?", "(raw-string? x)", PhelCompletionPriority.HTML_FUNCTIONS, "html", "Checks if `x` is an instance of the raw-string struct", """
<br /><code>(raw-string? x)</code><br /><br />
Checks if <b>x</b> is an instance of the raw-string struct.
<br /><br />
"""),
        )
    }

    private fun registerHttpFunctions() {
        functions[Namespace.HTTP] = listOf(
            DataFunction("http/create-response-from-map", "", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "", """

<br /><br />
"""),
            DataFunction("http/create-response-from-string", "", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "", """

<br /><br />
"""),
            DataFunction("http/emit-response", "(emit-response response)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Emits the response", """
<br /><code>(emit-response response)</code><br /><br />
Emits the response.
<br /><br />
"""),
            DataFunction("http/files-from-globals", "(files-from-globals & [files])", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts the files from `'${'$'}_'FILES` and normalizes them to a map of \"uploaded-file\"", """
<br /><code>(files-from-globals & [files])</code><br /><br />
Extracts the files from <b>'${'$'}_'FILES</b> and normalizes them to a map of \"uploaded-file\".
<br /><br />
"""),
            DataFunction("http/headers-from-server", "(headers-from-server & [server])", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts all headers from the `${'$'}_SERVER` variable", """
<br /><code>(headers-from-server & [server])</code><br /><br />
Extracts all headers from the <b>${'$'}_SERVER</b> variable.
<br /><br />
"""),
            DataFunction("http/request", "(request method uri headers parsed-body query-params cookie-params server-params uploaded-files version attributes)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a new request struct", """
<br /><code>(request method uri headers parsed-body query-params cookie-params server-params uploaded-files version attributes)</code><br /><br />
Creates a new request struct.
<br /><br />
"""),
            DataFunction("http/request-from-globals", "(request-from-globals )", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts a request from `'${'$'}_'SERVER`, `'${'$'}_'GET`, `'${'$'}_'POST`, `'${'$'}_'COOKIE` and `'${'$'}_'FILES`", """
<br /><code>(request-from-globals )</code><br /><br />
Extracts a request from <b>'${'$'}_'SERVER</b>, <b>'${'$'}_'GET</b>, <b>'${'$'}_'POST</b>, <b>'${'$'}_'COOKIE</b> and <b>'${'$'}_'FILES</b>.
<br /><br />
"""),
            DataFunction("http/request-from-globals-args", "(request-from-globals-args server get-parameter post-parameter cookies files)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts a request from args", """
<br /><code>(request-from-globals-args server get-parameter post-parameter cookies files)</code><br /><br />
Extracts a request from args.
<br /><br />
"""),
            DataFunction("http/request-from-map", "(request-from-map {:method method, :version version, :uri uri, :headers headers, :parsed-body parsed-body, :query-params query-params, :cookie-params cookie-params, :server-params server-params, :uploaded-files uploaded-files, :attributes attributes})", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "", """
<br /><code>(request-from-map <code>{:method method, :version version, :uri uri, :headers headers, :parsed-body parsed-body, :query-params query-params, :cookie-params cookie-params, :server-params server-params, :uploaded-files uploaded-files, :attributes attributes}</code>)</code><br /><br />

<br /><br />
"""),
            DataFunction("http/request?", "(request? x)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Checks if `x` is an instance of the request struct", """
<br /><code>(request? x)</code><br /><br />
Checks if <b>x</b> is an instance of the request struct.
<br /><br />
"""),
            DataFunction("http/response", "(response status headers body version reason)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a new response struct", """
<br /><code>(response status headers body version reason)</code><br /><br />
Creates a new response struct.
<br /><br />
"""),
            DataFunction("http/response-from-map", "(response-from-map {:status status, :headers headers, :body body, :version version, :reason reason})", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a response struct from a map. The map can have the following keys:  * `:status` The HTTP Status (default 200)  * `:headers` A map of HTTP Headers (default: empty map)  * `:body` The body of the response (default: empty string)  * `:version` The HTTP Version (default: 1.1)  * `:reason` The HTTP status reason. If not provided a common status reason is taken", """
<br /><code>(response-from-map <code>{:status status, :headers headers, :body body, :version version, :reason reason}</code>)</code><br /><br />
Creates a response struct from a map. The map can have the following keys:<br />
  * <b>:status</b> The HTTP Status (default 200)<br />
  * <b>:headers</b> A map of HTTP Headers (default: empty map)<br />
  * <b>:body</b> The body of the response (default: empty string)<br />
  * <b>:version</b> The HTTP Version (default: 1.1)<br />
  * <b>:reason</b> The HTTP status reason. If not provided a common status reason is taken
<br /><br />
"""),
            DataFunction("http/response-from-string", "(response-from-string s)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Create a response from a string", """
<br /><code>(response-from-string s)</code><br /><br />
Create a response from a string.
<br /><br />
"""),
            DataFunction("http/response?", "(response? x)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Checks if `x` is an instance of the response struct", """
<br /><code>(response? x)</code><br /><br />
Checks if <b>x</b> is an instance of the response struct.
<br /><br />
"""),
            DataFunction("http/uploaded-file", "(uploaded-file tmp-file size error-status client-filename client-media-type)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a new uploaded-file struct", """
<br /><code>(uploaded-file tmp-file size error-status client-filename client-media-type)</code><br /><br />
Creates a new uploaded-file struct.
<br /><br />
"""),
            DataFunction("http/uploaded-file?", "(uploaded-file? x)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Checks if `x` is an instance of the uploaded-file struct", """
<br /><code>(uploaded-file? x)</code><br /><br />
Checks if <b>x</b> is an instance of the uploaded-file struct.
<br /><br />
"""),
            DataFunction("http/uri", "(uri scheme userinfo host port path query fragment)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Creates a new uri struct", """
<br /><code>(uri scheme userinfo host port path query fragment)</code><br /><br />
Creates a new uri struct.
<br /><br />
"""),
            DataFunction("http/uri-from-globals", "(uri-from-globals & [server])", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Extracts the URI from the `${'$'}_SERVER` variable", """
<br /><code>(uri-from-globals & [server])</code><br /><br />
Extracts the URI from the <b>${'$'}_SERVER</b> variable.
<br /><br />
"""),
            DataFunction("http/uri-from-string", "(uri-from-string url)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Create a uri struct from a string", """
<br /><code>(uri-from-string url)</code><br /><br />
Create a uri struct from a string
<br /><br />
"""),
            DataFunction("http/uri?", "(uri? x)", PhelCompletionPriority.HTTP_FUNCTIONS, "http", "Checks if `x` is an instance of the uri struct", """
<br /><code>(uri? x)</code><br /><br />
Checks if <b>x</b> is an instance of the uri struct.
<br /><br />
"""),
        )
    }

    private fun registerJsonFunctions() {
        functions[Namespace.JSON] = listOf(
            DataFunction("json/decode", "(decode json & [{:flags flags, :depth depth}])", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Decodes a JSON string", """
<br /><code>(decode json & [<code>{:flags flags, :depth depth}</code>])</code><br /><br />
Decodes a JSON string.
<br /><br />
"""),
            DataFunction("json/decode-value", "(decode-value x)", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Convert a json data structure to a 'phel compatible' value", """
<br /><code>(decode-value x)</code><br /><br />
Convert a json data structure to a 'phel compatible' value.
<br /><br />
"""),
            DataFunction("json/encode", "(encode value & [{:flags flags, :depth depth}])", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Returns the JSON representation of a value", """
<br /><code>(encode value & [<code>{:flags flags, :depth depth}</code>])</code><br /><br />
Returns the JSON representation of a value.
<br /><br />
"""),
            DataFunction("json/encode-value", "(encode-value x)", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Convert a Phel data type to a 'json compatible' value", """
<br /><code>(encode-value x)</code><br /><br />
Convert a Phel data type to a 'json compatible' value.
<br /><br />
"""),
            DataFunction("json/valid-key?", "(valid-key? v)", PhelCompletionPriority.JSON_FUNCTIONS, "json", "Checks if `v` is a valid JSON key or can be converted to a JSON key", """
<br /><code>(valid-key? v)</code><br /><br />
Checks if <b>v</b> is a valid JSON key or can be converted to a JSON key.
<br /><br />
"""),
        )
    }

    private fun registerPhpInteropFunctions() {
        functions[Namespace.PHP] = listOf(
            DataFunction("php/->", "(php/-> object call*)", PhelCompletionPriority.PHP_INTEROP, "php", "Access to an object property or result of chained calls", """
<br /><code>(php/-> object call*)<br />
(php/:: class call*)</code><br /><br />
Access to an object property or result of chained calls.
<br /><br />
"""),
            DataFunction("php/::", "(php/:: class call*)", PhelCompletionPriority.PHP_INTEROP, "php", "Calls a static method or property from a PHP class. Both methodname and property must be symbols and cannot be an evaluated value", """
<br /><code>(php/:: class (methodname expr*))<br />
(php/:: class call*)</code><br /><br />
<br />
Calls a static method or property from a PHP class. Both methodname and property must be symbols and cannot be an evaluated value.
<br /><br />
"""),
            DataFunction("php/aget", "(php/aget arr index)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[index] ?? null`", """
<br /><code>(php/aget arr index)</code><br /><br />
Equivalent to PHP's <b>arr[index] ?? null</b>.
<br /><br />
"""),
            DataFunction("php/aget-in", "(php/aget-in arr ks)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[k1][k2][k...] ?? null`", """
<br /><code>(php/aget-in arr ks)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...] ?? null</b>.
<br /><br />
"""),
            DataFunction("php/apush", "(php/apush arr value)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's arr[] = value", """
<br /><code>(php/apush arr value)</code><br /><br />
Equivalent to PHP's <b>arr[] = value</b>.
<br /><br />
"""),
            DataFunction("php/apush-in", "(php/apush-in arr ks value)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[k1][k2][k...][] = value`", """
<br /><code>(php/apush-in arr ks value)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...][] = value</b>.
<br /><br />
"""),
            DataFunction("php/aset", "(php/aset arr index value)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[index] = value`", """
<br /><code>(php/aset arr index value)</code><br /><br />
Equivalent to PHP's <b>arr[index] = value</b>.
<br /><br />
"""),
            DataFunction("php/aset-in", "(php/aset-in arr ks value)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `arr[k1][k2][k...] = value`", """
<br /><code>(php/aset-in arr ks value)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...] = value</b>.
<br /><br />
"""),
            DataFunction("php/aunset", "(php/aunset arr index)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `unset(arr[index])`", """
<br /><code>(php/aunset arr index)</code><br /><br />
Equivalent to PHP's <b>unset(arr[index])</b>.
<br /><br />
"""),
            DataFunction("php/aunset-in", "(php/aunset-in arr ks)", PhelCompletionPriority.PHP_INTEROP, "php", "Equivalent to PHP's `unset(arr[k1][k2][k...])`", """
<br /><code>(php/aunset-in arr ks)</code><br /><br />
Equivalent to PHP's <b>unset(arr[k1][k2][k...])</b>.
<br /><br />
"""),
            DataFunction("php/new", "(php/new expr args*)", PhelCompletionPriority.PHP_INTEROP, "php", "Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned", """
<br /><code>(php/new expr args*)</code><br /><br />
Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned.
<br /><br />
"""),
            DataFunction("php/oset", "(php/oset (php/-> object prop) val)", PhelCompletionPriority.PHP_INTEROP, "php", "Use `php/oset` to set a value to a class/object property", """
<br /><code>(php/oset (php/-> object property) value)<br />
(php/oset (php/:: class property) value)</code><br /><br />
Use <b>php/oset</b> to set a value to a class/object property.
<br /><br />
"""),
        )
    }

    private fun registerReplFunctions() {
        functions[Namespace.REPL] = listOf(
            DataFunction("repl/build-facade", "", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "", """

<br /><br />
"""),
            DataFunction("repl/compile-str", "(compile-str s)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "", """
<br /><code>(compile-str s)</code><br /><br />

<br /><br />
"""),
            DataFunction("repl/doc", "(doc sym)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Prints the documentation for the given symbol", """
<br /><code>(doc sym)</code><br /><br />
Prints the documentation for the given symbol.
<br /><br />
"""),
            DataFunction("repl/loaded-namespaces", "(loaded-namespaces )", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Return all namespaces currently loaded in the REPL", """
<br /><code>(loaded-namespaces )</code><br /><br />
Return all namespaces currently loaded in the REPL.
<br /><br />
"""),
            DataFunction("repl/print-colorful", "(print-colorful & xs)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Colored print", """
<br /><code>(print-colorful & xs)</code><br /><br />
Colored print.
<br /><br />
"""),
            DataFunction("repl/println-colorful", "(println-colorful & xs)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Colored println", """
<br /><code>(println-colorful & xs)</code><br /><br />
Colored println.
<br /><br />
"""),
            DataFunction("repl/require", "(require sym & args)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Requires a Phel module into the environment", """
<br /><code>(require sym & args)</code><br /><br />
Requires a Phel module into the environment.
<br /><br />
"""),
            DataFunction("repl/resolve", "(resolve sym)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Resolves the given symbol in the current environment and returns a   resolved Symbol with the absolute namespace or nil if it cannot be resolved", """
<br /><code>(resolve sym)</code><br /><br />
Resolves the given symbol in the current environment and returns a<br />
   resolved Symbol with the absolute namespace or nil if it cannot be resolved.
<br /><br />
"""),
            DataFunction("repl/use", "(use sym & args)", PhelCompletionPriority.REPL_FUNCTIONS, "repl", "Adds a use statement to the environment", """
<br /><code>(use sym & args)</code><br /><br />
Adds a use statement to the environment.
<br /><br />
"""),
        )
    }

    private fun registerStringFunctions() {
        functions[Namespace.STR] = listOf(
            DataFunction("str/blank?", "(blank? s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s is nil, empty, or contains only whitespace", """
<br /><code>(blank? s)</code><br /><br />
True if s is nil, empty, or contains only whitespace.
<br /><br />
"""),
            DataFunction("str/capitalize", "(capitalize s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Converts first character of the string to upper-case, all other  characters to lower-case. Handles multibyte characters", """
<br /><code>(capitalize s)</code><br /><br />
Converts first character of the string to upper-case, all other<br />
  characters to lower-case. Handles multibyte characters.
<br /><br />
"""),
            DataFunction("str/contains?", "(contains? s substr)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s contains substr", """
<br /><code>(contains? s substr)</code><br /><br />
True if s contains substr.
<br /><br />
"""),
            DataFunction("str/ends-with?", "(ends-with? s substr)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s ends with substr", """
<br /><code>(ends-with? s substr)</code><br /><br />
True if s ends with substr.
<br /><br />
"""),
            DataFunction("str/escape", "(escape s cmap)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Return a new string, using cmap to escape each character ch   from s as follows:   If (cmap ch) is nil, append ch to the new string.   If (cmap ch) is non-nil, append (str (cmap ch)) instead", """
<br /><code>(escape s cmap)</code><br /><br />
Return a new string, using cmap to escape each character ch<br />
   from s as follows:<br />
<br />
   If (cmap ch) is nil, append ch to the new string.<br />
   If (cmap ch) is non-nil, append (str (cmap ch)) instead.
<br /><br />
"""),
            DataFunction("str/includes?", "(includes? s substr)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s includes substr", """
<br /><code>(includes? s substr)</code><br /><br />
True if s includes substr.
<br /><br />
"""),
            DataFunction("str/index-of", "(index-of s value & [from-index])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Return index of value (string or char) in s, optionally searching  forward from from-index. Return nil if value not found", """
<br /><code>(index-of s value & [from-index])</code><br /><br />
Return index of value (string or char) in s, optionally searching<br />
  forward from from-index. Return nil if value not found.
<br /><br />
"""),
            DataFunction("str/join", "(join separator & [coll])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string of all elements in coll, as returned by (seq coll),   separated by an optional separator", """
<br /><code>(join separator & [coll])</code><br /><br />
Returns a string of all elements in coll, as returned by (seq coll),<br />
   separated by an optional separator.
<br /><br />
"""),
            DataFunction("str/last-index-of", "(last-index-of s value & [from-index])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Return last index of value (string or char) in s, optionally  searching backward from from-index. Return nil if value not found", """
<br /><code>(last-index-of s value & [from-index])</code><br /><br />
Return last index of value (string or char) in s, optionally<br />
  searching backward from from-index. Return nil if value not found.
<br /><br />
"""),
            DataFunction("str/lower-case", "(lower-case s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Converts string to all lower-case. Handles multibyte characters", """
<br /><code>(lower-case s)</code><br /><br />
Converts string to all lower-case. Handles multibyte characters.
<br /><br />
"""),
            DataFunction("str/pad-both", "(pad-both s len & [pad-str])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string padded on both sides to length `len`.   If `pad-str` is omitted it defaults to a single space", """
<br /><code>(pad-both s len & [pad-str])</code><br /><br />
Returns a string padded on both sides to length <b>len</b>.<br />
   If <b>pad-str</b> is omitted it defaults to a single space.
<br /><br />
"""),
            DataFunction("str/pad-left", "(pad-left s len & [pad-str])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string padded on the left side to length `len`.   If `pad-str` is omitted it defaults to a single space", """
<br /><code>(pad-left s len & [pad-str])</code><br /><br />
Returns a string padded on the left side to length <b>len</b>.<br />
   If <b>pad-str</b> is omitted it defaults to a single space.
<br /><br />
"""),
            DataFunction("str/pad-right", "(pad-right s len & [pad-str])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string padded on the right side to length `len`.   If `pad-str` is omitted it defaults to a single space", """
<br /><code>(pad-right s len & [pad-str])</code><br /><br />
Returns a string padded on the right side to length <b>len</b>.<br />
   If <b>pad-str</b> is omitted it defaults to a single space.
<br /><br />
"""),
            DataFunction("str/re-quote-replacement", "(re-quote-replacement replacement)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Given a replacement string that you wish to be a literal   replacement for a pattern match in replace or replace-first, do the   necessary escaping of special characters in the replacement", """
<br /><code>(re-quote-replacement replacement)</code><br /><br />
Given a replacement string that you wish to be a literal<br />
   replacement for a pattern match in replace or replace-first, do the<br />
   necessary escaping of special characters in the replacement.
<br /><br />
"""),
            DataFunction("str/repeat", "(repeat s n)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns a string containing n copies of s", """
<br /><code>(repeat s n)</code><br /><br />
Returns a string containing n copies of s.
<br /><br />
"""),
            DataFunction("str/replace", "(replace s match replacement)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Replaces all instance of match with replacement in s.   match/replacement can be:   string / string   pattern / (string or function of match).   See also replace-first.   The replacement is literal (i.e. none of its characters are treated   specially) for all cases above except pattern / string.   For pattern / string, $1, $2, etc. in the replacement string are   substituted with the string that matched the corresponding   parenthesized group in the pattern.  If you wish your replacement   string r to be used literally, use (re-quote-replacement r) as the   replacement argument", """
<br /><code>(replace s match replacement)</code><br /><br />
Replaces all instance of match with replacement in s.<br />
<br />
   match/replacement can be:<br />
<br />
   string / string<br />
   pattern / (string or function of match).<br />
<br />
   See also replace-first.<br />
<br />
   The replacement is literal (i.e. none of its characters are treated<br />
   specially) for all cases above except pattern / string.<br />
<br />
   For pattern / string, $1, $2, etc. in the replacement string are<br />
   substituted with the string that matched the corresponding<br />
   parenthesized group in the pattern.  If you wish your replacement<br />
   string r to be used literally, use (re-quote-replacement r) as the<br />
   replacement argument.
<br /><br />
"""),
            DataFunction("str/replace-first", "(replace-first s match replacement)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Replaces the first instance of match with replacement in s.   match/replacement can be:   char / char   string / string   pattern / (string or function of match).   See also replace.   The replacement is literal (i.e. none of its characters are treated   specially) for all cases above except pattern / string.   For pattern / string, $1, $2, etc. in the replacement string are   substituted with the string that matched the corresponding   parenthesized group in the pattern.  If you wish your replacement   string r to be used literally, use (re-quote-replacement r) as the   replacement argument.   Example:   (str/replace-first \"swap first two words\"                                 #\"(\\w+)(\\s+)(\\w+)\" \"$3$2$1\")   -> \"first swap two words\"", """
<br /><code>(replace-first s match replacement)</code><br /><br />
Replaces the first instance of match with replacement in s.<br />
<br />
   match/replacement can be:<br />
<br />
   char / char<br />
   string / string<br />
   pattern / (string or function of match).<br />
<br />
   See also replace.<br />
<br />
   The replacement is literal (i.e. none of its characters are treated<br />
   specially) for all cases above except pattern / string.<br />
<br />
   For pattern / string, $1, $2, etc. in the replacement string are<br />
   substituted with the string that matched the corresponding<br />
   parenthesized group in the pattern.  If you wish your replacement<br />
   string r to be used literally, use (re-quote-replacement r) as the<br />
   replacement argument.<br />
<br />
   Example:<br />
   (str/replace-first \"swap first two words\"<br />
                                 #\"(\\w+)(\\s+)(\\w+)\" \"$3$2$1\")<br />
   -> \"first swap two words\"
<br /><br />
"""),
            DataFunction("str/reverse", "(reverse s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns s with its characters reversed", """
<br /><code>(reverse s)</code><br /><br />
Returns s with its characters reversed.
<br /><br />
"""),
            DataFunction("str/split", "(split s re & [limit])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Splits string on a regular expression.  Optional argument limit is  the maximum number of parts. Not lazy. Returns vector of the parts.  Trailing empty strings are not returned - pass limit of -1 to return all", """
<br /><code>(split s re & [limit])</code><br /><br />
Splits string on a regular expression.  Optional argument limit is<br />
  the maximum number of parts. Not lazy. Returns vector of the parts.<br />
  Trailing empty strings are not returned - pass limit of -1 to return all.
<br /><br />
"""),
            DataFunction("str/split-lines", "(split-lines s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Splits s on \n or \r\n. Trailing empty lines are not returned", """
<br /><code>(split-lines s)</code><br /><br />
Splits s on \n or \r\n. Trailing empty lines are not returned.
<br /><br />
"""),
            DataFunction("str/starts-with?", "(starts-with? s substr)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "True if s starts with substr", """
<br /><code>(starts-with? s substr)</code><br /><br />
True if s starts with substr.
<br /><br />
"""),
            DataFunction("str/subs", "(subs s start & [end])", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Returns the substring of `s` from `start` (inclusive) to `end` (exclusive).   If `end` is omitted, the substring extends to the end of `s`", """
<br /><code>(subs s start & [end])</code><br /><br />
Returns the substring of <b>s</b> from <b>start</b> (inclusive) to <b>end</b> (exclusive).<br />
   If <b>end</b> is omitted, the substring extends to the end of <b>s</b>.
<br /><br />
"""),
            DataFunction("str/trim", "(trim s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Removes whitespace from both ends of string", """
<br /><code>(trim s)</code><br /><br />
Removes whitespace from both ends of string.
<br /><br />
"""),
            DataFunction("str/trim-newline", "(trim-newline s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Removes all trailing newline \n or return \r characters from  string.  Similar to Perl's chomp", """
<br /><code>(trim-newline s)</code><br /><br />
Removes all trailing newline \n or return \r characters from<br />
  string.  Similar to Perl's chomp.
<br /><br />
"""),
            DataFunction("str/triml", "(triml s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Removes whitespace from the left side of string", """
<br /><code>(triml s)</code><br /><br />
Removes whitespace from the left side of string.
<br /><br />
"""),
            DataFunction("str/trimr", "(trimr s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Removes whitespace from the right side of string", """
<br /><code>(trimr s)</code><br /><br />
Removes whitespace from the right side of string.
<br /><br />
"""),
            DataFunction("str/upper-case", "(upper-case s)", PhelCompletionPriority.STRING_FUNCTIONS, "str", "Converts string to all upper-case. Handles multibyte characters", """
<br /><code>(upper-case s)</code><br /><br />
Converts string to all upper-case. Handles multibyte characters.
<br /><br />
"""),
        )
    }

    private fun registerTestFunctions() {
        functions[Namespace.TEST] = listOf(
            DataFunction("test/deftest", "(deftest test-name & body)", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Defines a test function with no arguments", """
<br /><code>(deftest test-name & body)</code><br /><br />
Defines a test function with no arguments.
<br /><br />
"""),
            DataFunction("test/is", "(is form & [message])", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Generic assertion macro", """
<br /><code>(is form & [message])</code><br /><br />
Generic assertion macro.
<br /><br />
"""),
            DataFunction("test/print-summary", "(print-summary )", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Prints the summary of the test suite", """
<br /><code>(print-summary )</code><br /><br />
Prints the summary of the test suite.
<br /><br />
"""),
            DataFunction("test/report", "(report data)", PhelCompletionPriority.TEST_FUNCTIONS, "test", "", """
<br /><code>(report data)</code><br /><br />

<br /><br />
"""),
            DataFunction("test/run-tests", "(run-tests options & namespaces)", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Runs all test functions in the given namespaces", """
<br /><code>(run-tests options & namespaces)</code><br /><br />
Runs all test functions in the given namespaces.
<br /><br />
"""),
            DataFunction("test/successful?", "(successful? )", PhelCompletionPriority.TEST_FUNCTIONS, "test", "Checks if all tests have passed", """
<br /><code>(successful? )</code><br /><br />
Checks if all tests have passed.
<br /><br />
"""),
        )
    }
}
