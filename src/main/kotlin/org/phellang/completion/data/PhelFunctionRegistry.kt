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
            DataFunction("base64/decode", "(decode s & [strict?])", """
<br /><code>(decode s & [strict?])</code><br /><br />
Decodes the Base64 encoded string <b>s</b>. If <b>strict?</b> is true invalid characters trigger an error.
<br /><br />
""", PhelCompletionPriority.BASE64_FUNCTIONS, "base64"),
            DataFunction("base64/decode-url", "(decode-url s & [strict?])", """
<br /><code>(decode-url s & [strict?])</code><br /><br />
Decodes a Base64 URL encoded string <b>s</b>. If <b>strict?</b> is true invalid characters trigger an error.
<br /><br />
""", PhelCompletionPriority.BASE64_FUNCTIONS, "base64"),
            DataFunction("base64/encode", "(encode s)", """
<br /><code>(encode s)</code><br /><br />
Returns the Base64 representation of <b>s</b>.
<br /><br />
""", PhelCompletionPriority.BASE64_FUNCTIONS, "base64"),
            DataFunction("base64/encode-url", "(encode-url s)", """
<br /><code>(encode-url s)</code><br /><br />
Returns the URL safe Base64 representation of <b>s</b>. Padding is removed.
<br /><br />
""", PhelCompletionPriority.BASE64_FUNCTIONS, "base64"),
        )
    }

    private fun registerCoreFunctions() {
        functions[Namespace.CORE] = listOf(
            DataFunction("%", "(% dividend divisor)", """
<br /><code>(% dividend divisor)</code><br /><br />
Return the remainder of <b>dividend</b> / <b>divisor</b>.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("*", "(* & xs)", """
<br /><code>(* & xs)</code><br /><br />
Returns the product of all elements in <b>xs</b>. All elements in <b>xs</b> must be<br />
numbers. If <b>xs</b> is empty, return 1.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("**", "(** a x)", """
<br /><code>(** a x)</code><br /><br />
Return <b>a</b> to the power of <b>x</b>.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("*build-mode*", "", """
Set to true when a file is being built/transpiled, false otherwise.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("*compile-mode*", "", """
Deprecated! Use *build-mode* instead. Set to true when a file is compiled, false otherwise.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("*file*", "*file*", """
<br /><code>*file*</code><br /><br />
Returns the path of the current source file.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("*ns*", "*ns*", """
<br /><code>*ns*</code><br /><br />
Returns the namespace in the current scope.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("+", "(+ & xs)", """
<br /><code>(+ & xs)</code><br /><br />
Returns the sum of all elements in <b>xs</b>. All elements <b>xs</b> must be numbers.<br />
  If <b>xs</b> is empty, return 0.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("-", "(- & xs)", """
<br /><code>(- & xs)</code><br /><br />
Returns the difference of all elements in <b>xs</b>. If <b>xs</b> is empty, return 0. If <b>xs</b><br />
  has one element, return the negative value of that element.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("->", "(-> x & forms)", """
<br /><code>(-> x & forms)</code><br /><br />
Threads the expr through the forms. Inserts <b>x</b> as the second item<br />
  in the first form, making a list of it if it is not a list already.<br />
  If there are more forms, insert the first form as the second item in<br />
  the second form, etc.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("->>", "(->> x & forms)", """
<br /><code>(->> x & forms)</code><br /><br />
Threads the expr through the forms. Inserts <b>x</b> as the<br />
  last item in the first form, making a list of it if it is not a<br />
  list already. If there are more forms, insert the first form as the<br />
  last item in the second form, etc.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("/", "(/ & xs)", """
<br /><code>(/ & xs)</code><br /><br />
Returns the nominator divided by all the denominators. If <b>xs</b> is empty,<br />
returns 1. If <b>xs</b> has one value, returns the reciprocal of x.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("<", "(< a & more)", """
<br /><code>(< a & more)</code><br /><br />
Checks if each argument is strictly less than the following argument. Returns a boolean.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("<=", "(<= a & more)", """
<br /><code>(<= a & more)</code><br /><br />
Checks if each argument is less than or equal to the following argument. Returns a boolean.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("<=>", "(<=> a b)", """
<br /><code>(<=> a b)</code><br /><br />
Alias for the spaceship PHP operator in ascending order. Returns an int.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("=", "(= a & more)", """
<br /><code>(= a & more)</code><br /><br />
Checks if all values are equal. Same as <b>a == b</b> in PHP.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction(">", "(> a & more)", """
<br /><code>(> a & more)</code><br /><br />
Checks if each argument is strictly greater than the following argument. Returns a boolean.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction(">=", "(>= a & more)", """
<br /><code>(>= a & more)</code><br /><br />
Checks if each argument is greater than or equal to the following argument. Returns a boolean.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction(">=<", "(>=< a b)", """
<br /><code>(>=< a b)</code><br /><br />
Alias for the spaceship PHP operator in descending order. Returns an int.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("NAN", "", """
Constant for Not a Number (NAN) values.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("all?", "(all? pred xs)", """
<br /><code>(all? pred xs)</code><br /><br />
Returns true if <b>(pred x)</b> is logical true for every <b>x</b> in collection <b>xs</b><br />
   or if <b>xs</b> is empty. Otherwise returns false.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("and", "(and & args)", """
<br /><code>(and & args)</code><br /><br />
Evaluates each expression one at a time, from left to right. If a form<br />
returns logical false, <b>and</b> returns that value and doesn't evaluate any of the<br />
other expressions, otherwise, it returns the value of the last expression.<br />
Calling the <b>and</b> function without arguments returns true.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("apply", "(apply f expr*)", """
<br /><code>(apply f expr*)</code><br /><br />
Calls the function with the given arguments. The last argument must be a list of values, which are passed as separate arguments, rather than a single list. Apply returns the result of the calling function.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("argv", "", """
Vector of arguments passed to the script.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("as->", "(as-> expr name & forms)", """
<br /><code>(as-> expr name & forms)</code><br /><br />
Binds <b>name</b> to <b>expr</b>, evaluates the first form in the lexical context<br />
  of that binding, then binds name to that result, repeating for each<br />
  successive form, returning the result of the last form.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("assert-non-nil", "(assert-non-nil & xs)", """
<br /><code>(assert-non-nil & xs)</code><br /><br />

<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("assoc", "(assoc ds key value)", """
<br /><code>(assoc ds key value)</code><br /><br />
Alias for <b>put</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("assoc-in", "(assoc-in ds ks v)", """
<br /><code>(assoc-in ds ks v)</code><br /><br />
Alias for <b>put-in</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("associative?", "(associative? x)", """
<br /><code>(associative? x)</code><br /><br />
Returns true if <b>x</b> is an associative data structure, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("binding", "(binding bindings & body)", """
<br /><code>(binding bindings & body)</code><br /><br />
Temporary redefines definitions while executing the body.<br />
  The value will be reset after the body was executed.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("bit-and", "(bit-and x y & args)", """
<br /><code>(bit-and x y & args)</code><br /><br />
Bitwise and.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-clear", "(bit-clear x n)", """
<br /><code>(bit-clear x n)</code><br /><br />
Clear bit an index <b>n</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-flip", "(bit-flip x n)", """
<br /><code>(bit-flip x n)</code><br /><br />
Flip bit at index <b>n</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-not", "(bit-not x)", """
<br /><code>(bit-not x)</code><br /><br />
Bitwise complement.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-or", "(bit-or x y & args)", """
<br /><code>(bit-or x y & args)</code><br /><br />
Bitwise or.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-set", "(bit-set x n)", """
<br /><code>(bit-set x n)</code><br /><br />
Set bit an index <b>n</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-shift-left", "(bit-shift-left x n)", """
<br /><code>(bit-shift-left x n)</code><br /><br />
Bitwise shift left.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-shift-right", "(bit-shift-right x n)", """
<br /><code>(bit-shift-right x n)</code><br /><br />
Bitwise shift right.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-test", "(bit-test x n)", """
<br /><code>(bit-test x n)</code><br /><br />
Test bit at index <b>n</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("bit-xor", "(bit-xor x y & args)", """
<br /><code>(bit-xor x y & args)</code><br /><br />
Bitwise xor.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("boolean?", "(boolean? x)", """
<br /><code>(boolean? x)</code><br /><br />
Returns true if <b>x</b> is a boolean, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("butlast", "(butlast xs)", """
<br /><code>(butlast xs)</code><br /><br />
Returns all but the last item in <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("case", "(case e & pairs)", """
<br /><code>(case e & pairs)</code><br /><br />
Takes an expression <b>e</b> and a set of test-content/expression pairs. First<br />
  evaluates <b>e</b> and then finds the first pair where the test-constant matches<br />
  the result of <b>e</b>. The associated expression is then evaluated and returned.<br />
  If no matches can be found a final last expression can be provided that is<br />
  then evaluated and returned. Otherwise, nil is returned.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("coerce-in", "(coerce-in v min max)", """
<br /><code>(coerce-in v min max)</code><br /><br />
Returns <b>v</b> if it is in the range, or <b>min</b> if <b>v</b> is less than <b>min</b>, or <b>max</b> if <b>v</b> is greater than <b>max</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("comment", "(comment &)", """
<br /><code>(comment &)</code><br /><br />
Ignores the body of the comment.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("comp", "(comp & fs)", """
<br /><code>(comp & fs)</code><br /><br />
Takes a list of functions and returns a function that is the composition of those functions.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("compare", "(compare x y)", """
<br /><code>(compare x y)</code><br /><br />
An integer less than, equal to, or greater than zero when <b>x</b> is less than, equal to, or greater than <b>y</b>, respectively.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("compile", "(compile form)", """
<br /><code>(compile form)</code><br /><br />
Returns the compiled PHP code string for the given form.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("complement", "(complement f)", """
<br /><code>(complement f)</code><br /><br />
Returns a function that takes the same arguments as <b>f</b> and returns the opposite truth value.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("concat", "(concat arr & xs)", """
<br /><code>(concat arr & xs)</code><br /><br />
Concatenates multiple sequential data structures.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("cond", "(cond & pairs)", """
<br /><code>(cond & pairs)</code><br /><br />
Takes a set of test/expression pairs. Evaluates each test one at a time.<br />
  If a test returns logically true, the expression is evaluated and returned.<br />
  If no test matches a final last expression can be provided that is then<br />
  evaluated and returned. Otherwise, nil is returned.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("cons", "(cons x xs)", """
<br /><code>(cons x xs)</code><br /><br />
Prepends <b>x</b> to the beginning of <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("constantly", "(constantly x)", """
<br /><code>(constantly x)</code><br /><br />
Returns a function that always returns <b>x</b> and ignores any passed arguments.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("contains-value?", "(contains-value? coll val)", """
<br /><code>(contains-value? coll val)</code><br /><br />
Returns true if the value is present in the given collection, otherwise returns false.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("contains?", "(contains? coll key)", """
<br /><code>(contains? coll key)</code><br /><br />
Returns true if key is present in the given collection, otherwise returns false.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("count", "(count xs)", """
<br /><code>(count xs)</code><br /><br />
Counts the number of elements in a sequence. Can be used on everything that implements the PHP Countable interface.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("dec", "(dec x)", """
<br /><code>(dec x)</code><br /><br />
Decrements <b>x</b> by one.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("declare", "", """
Declare a global symbol before it is defined.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("dedupe", "(dedupe xs)", """
<br /><code>(dedupe xs)</code><br /><br />
Returns a vector with consecutive duplicate values removed in <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("deep-merge", "(deep-merge & args)", """
<br /><code>(deep-merge & args)</code><br /><br />
Recursively merges data structures.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("def", "(def name meta? value)", """
<br /><code>(def name meta? value)</code><br /><br />
This special form binds a value to a global symbol.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("def-", "(def- name value)", """
<br /><code>(def- name value)</code><br /><br />
Define a private value that will not be exported.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("defexception", "(defexception name)", """
<br /><code>(defexception name)</code><br /><br />
Define a new exception.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("defexception*", "(defexception name)", """
<br /><code>(defexception my-ex)</code><br />
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("definterface", "(definterface name & fns)", """
<br /><code>(definterface name & fns)</code><br /><br />
Defines an interface.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("definterface*", "(definterface name & fns)", """
<br /><code>(definterface name & fns)</code><br /><br />
An interface in Phel defines an abstract set of functions. It is directly mapped to a PHP interface. An interface can be defined by using the definterface macro.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("defmacro", "(defmacro name & fdecl)", """
<br /><code>(defmacro name & fdecl)</code><br /><br />
Define a macro.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("defmacro-", "(defmacro- name & fdecl)", """
<br /><code>(defmacro- name & fdecl)</code><br /><br />
Define a private macro that will not be exported.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("defn", "(defn name & fdecl)", """
<br /><code>(defn name & fdecl)</code><br /><br />
Define a new global function.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("defn-", "(defn- name & fdecl)", """
<br /><code>(defn- name & fdecl)</code><br /><br />
Define a private function that will not be exported.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("defstruct", "(defstruct name keys & implementations)", """
<br /><code>(defstruct name keys & implementations)</code><br /><br />
Define a new struct.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("defstruct*", "(defstruct my-struct [a b c])", """
<br /><code>(defstruct my-struct [a b c])</code><br /><br />
A Struct is a special kind of Map. It only supports a predefined number of keys and is associated to a global name. The Struct not only defines itself but also a predicate function.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("deref", "(deref variable)", """
<br /><code>(deref variable)</code><br /><br />
Return the value inside the variable.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("difference", "(difference set & sets)", """
<br /><code>(difference set & sets)</code><br /><br />
Difference between multiple sets into a new one.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("difference-pair", "(difference-pair s1 s2)", """
<br /><code>(difference-pair s1 s2)</code><br /><br />

<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("dissoc", "(dissoc ds key)", """
<br /><code>(dissoc ds key)</code><br /><br />
Alias for <b>unset</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("dissoc-in", "(dissoc-in ds ks)", """
<br /><code>(dissoc-in ds ks)</code><br /><br />
Alias for <b>unset-in</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("distinct", "(distinct xs)", """
<br /><code>(distinct xs)</code><br /><br />
Returns a vector with duplicated values removed in <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("do", "(do expr*)", """
<br /><code>(do expr*)</code><br /><br />
Evaluates the expressions in order and returns the value of the last expression. If no expression is given, nil is returned.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("dofor", "(dofor head & body)", """
<br /><code>(dofor head & body)</code><br /><br />
Repeatedly executes body for side effects with bindings and modifiers as<br />
  provided by for. Returns nil.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("doto", "(doto x & forms)", """
<br /><code>(doto x & forms)</code><br /><br />
Evaluates x then calls all of the methods and functions with the<br />
  value of x supplied at the front of the given arguments. The forms<br />
  are evaluated in order. Returns x.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("drop", "(drop n xs)", """
<br /><code>(drop n xs)</code><br /><br />
Drops the first <b>n</b> elements of <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("drop-last", "(drop-last n xs)", """
<br /><code>(drop-last n xs)</code><br /><br />
Drops the last <b>n</b> elements of <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("drop-while", "(drop-while pred xs)", """
<br /><code>(drop-while pred xs)</code><br /><br />
Drops all elements at the front <b>xs</b> where <b>(pred x)</b> is true.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("empty?", "(empty? x)", """
<br /><code>(empty? x)</code><br /><br />
Returns true if x would be 0, \"\" or empty collection, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("eval", "(eval form)", """
<br /><code>(eval form)</code><br /><br />
Evaluates a form and return the evaluated results.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("even?", "(even? x)", """
<br /><code>(even? x)</code><br /><br />
Checks if <b>x</b> is even.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("every?", "(every? pred xs)", """
<br /><code>(every? pred xs)</code><br /><br />
Alias for <b>all?</b>.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("extreme", "(extreme order args)", """
<br /><code>(extreme order args)</code><br /><br />
Returns the most extreme value in <b>args</b> based on the binary <b>order</b> function.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("false?", "(false? x)", """
<br /><code>(false? x)</code><br /><br />
Checks if <b>x</b> is false. Same as <b>x === false</b> in PHP.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("ffirst", "(ffirst xs)", """
<br /><code>(ffirst xs)</code><br /><br />
Same as <b>(first (first xs))</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("filter", "(filter pred xs)", """
<br /><code>(filter pred xs)</code><br /><br />
Returns all elements of <b>xs</b> where <b>(pred x)</b> is true.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("find", "(find pred xs)", """
<br /><code>(find pred xs)</code><br /><br />
Returns the first item in <b>xs</b> where <b>(pred item)</b> evaluates to true.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("find-index", "(find-index pred xs)", """
<br /><code>(find-index pred xs)</code><br /><br />
Returns the index of the first item in <b>xs</b> where <b>(pred index item)</b> evaluates to true.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("first", "(first xs)", """
<br /><code>(first xs)</code><br /><br />
Returns the first element of an indexed sequence or nil.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("flatten", "(flatten xs)", """
<br /><code>(flatten xs)</code><br /><br />
Takes a nested sequential data structure <b>(tree)</b>, and returns their contents<br />
  as a single, flat vector.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("float?", "(float? x)", """
<br /><code>(float? x)</code><br /><br />
Returns true if <b>x</b> is float point number, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("fn", "(fn [params*] expr*)", """
<br /><code>(fn [params*] expr*)</code><br /><br />
Defines a function. A function consists of a list of parameters and a list of expression. The value of the last expression is returned as the result of the function. All other expression are only evaluated for side effects. If no expression is given, the function returns nil.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("for", "", """

<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("foreach", "(foreach [key value valueExpr] expr*)", """
<br /><code>(foreach [value valueExpr] expr*)<br />
(foreach [key value valueExpr] expr*)</code><br /><br />
The foreach special form can be used to iterate over all kind of PHP datastructures. The return value of foreach is always nil. The loop special form should be preferred of the foreach special form whenever possible.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("format", "(format fmt & xs)", """
<br /><code>(format fmt & xs)</code><br /><br />
Returns a formatted string. See PHP's <a href="https://www.php.net/manual/en/function.sprintf.php">sprintf</a> for more information.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("frequencies", "(frequencies xs)", """
<br /><code>(frequencies xs)</code><br /><br />
Returns a map from distinct items in <b>xs</b> to the number of times they appear.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("full-name", "(full-name x)", """
<br /><code>(full-name x)</code><br /><br />
Return the namespace and name string of a string, keyword or symbol.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("function?", "(function? x)", """
<br /><code>(function? x)</code><br /><br />
Returns true if <b>x</b> is a function, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("gensym", "(gensym )", """
<br /><code>(gensym )</code><br /><br />
Generates a new unique symbol.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("get", "(get ds k & [opt])", """
<br /><code>(get ds k & [opt])</code><br /><br />
Get the value mapped to <b>key</b> from the datastructure <b>ds</b>.<br />
  Returns <b>opt</b> or nil if the value cannot be found.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("get-in", "(get-in ds ks & [opt])", """
<br /><code>(get-in ds ks & [opt])</code><br /><br />
Access a value in a nested data structure. Looks into the data structure via a sequence of keys.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("group-by", "(group-by f xs)", """
<br /><code>(group-by f xs)</code><br /><br />
Returns a map of the elements of xs keyed by the result of<br />
  f on each element.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("hash-map", "(hash-map & xs) # {& xs}", """
<br /><code>(hash-map & xs) # {& xs}</code><br /><br />
Creates a new hash map. If no argument is provided, an empty hash map is created. The number of parameters must be even.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("hash-map?", "(hash-map? x)", """
<br /><code>(hash-map? x)</code><br /><br />
Returns true if <b>x</b> is a hash map, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("id", "(id a & more)", """
<br /><code>(id a & more)</code><br /><br />
Checks if all values are identical. Same as <b>a === b</b> in PHP.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("identity", "(identity x)", """
<br /><code>(identity x)</code><br /><br />
Returns its argument.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("if", "(if test then else?)", """
<br /><code>(if test then else?)</code><br /><br />
A control flow structure. First evaluates test. If test evaluates to true, only the then form is evaluated and the result is returned. If test evaluates to false only the else form is evaluated and the result is returned. If no else form is given, nil will be returned.<br />
<br />
The test evaluates to false if its value is false or equal to nil. Every other value evaluates to true. In sense of PHP this means (test != null && test !== false).
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("if-let", "(if-let bindings then & [else])", """
<br /><code>(if-let bindings then & [else])</code><br /><br />
If test is true, evaluates then with binding-form bound to the value of test,<br />
  if not, yields else
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("if-not", "(if-not test then & [else])", """
<br /><code>(if-not test then & [else])</code><br /><br />
Shorthand for <b>(if (not condition) else then)</b>.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("inc", "(inc x)", """
<br /><code>(inc x)</code><br /><br />
Increments <b>x</b> by one.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("indexed?", "(indexed? x)", """
<br /><code>(indexed? x)</code><br /><br />
Returns true if <b>x</b> is indexed sequence, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("int?", "(int? x)", """
<br /><code>(int? x)</code><br /><br />
Returns true if <b>x</b> is an integer number, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("interleave", "(interleave & xs)", """
<br /><code>(interleave & xs)</code><br /><br />
Returns a vector with the first items of each col, then the second items, etc.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("interpose", "(interpose sep xs)", """
<br /><code>(interpose sep xs)</code><br /><br />
Returns a vector of elements separated by <b>sep</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("intersection", "(intersection set & sets)", """
<br /><code>(intersection set & sets)</code><br /><br />
Intersect multiple sets into a new one.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("into", "(into to & rest)", """
<br /><code>(into to & rest)</code><br /><br />
Returns <b>to</b> with all elements of <b>from</b> added.<br />
<br />
   When <b>from</b> is associative, it is treated as a sequence of key-value pairs.<br />
   Supports persistent and transient collections.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("invert", "(invert map)", """
<br /><code>(invert map)</code><br /><br />
Returns a new map where the keys and values are swapped. If map has<br />
  duplicated values, some keys will be ignored.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("iterate", "(iterate f x)", """
<br /><code>(iterate f x)</code><br /><br />
Returns an infinite sequence of x, (f x), (f (f x)), and so on.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("juxt", "(juxt & fs)", """
<br /><code>(juxt & fs)</code><br /><br />
Takes a list of functions and returns a new function that is the juxtaposition of those functions.<br />
  <b>((juxt a b c) x) => [(a x) (b x) (c x)]</b>.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("keep", "(keep pred xs)", """
<br /><code>(keep pred xs)</code><br /><br />
Returns a list of non-nil results of <b>(pred x)</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("keep-indexed", "(keep-indexed pred xs)", """
<br /><code>(keep-indexed pred xs)</code><br /><br />
Returns a list of non-nil results of <b>(pred i x)</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("keys", "(keys xs)", """
<br /><code>(keys xs)</code><br /><br />
Gets the keys of an associative data structure.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("keyword", "(keyword x)", """
<br /><code>(keyword x)</code><br /><br />
Creates a new Keyword from a given string.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("keyword?", "(keyword? x)", """
<br /><code>(keyword? x)</code><br /><br />
Returns true if <b>x</b> is a keyword, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("kvs", "(kvs xs)", """
<br /><code>(kvs xs)</code><br /><br />
Returns a vector of key-value pairs like <b>[k1 v1 k2 v2 k3 v3 ...]</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("last", "(last xs)", """
<br /><code>(last xs)</code><br /><br />
Returns the last element of <b>xs</b> or nil if <b>xs</b> is empty or nil.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("let", "(let [bindings*] expr*)", """
<br /><code>(let [bindings*] expr*)</code><br /><br />
Creates a new lexical context with assignments defined in bindings. Afterwards the list of expressions is evaluated and the value of the last expression is returned. If no expression is given nil is returned.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("list", "(list & xs) # '(& xs)", """
<br /><code>(list & xs) # '(& xs)</code><br /><br />
Creates a new list. If no argument is provided, an empty list is created.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("list?", "(list? x)", """
<br /><code>(list? x)</code><br /><br />
Returns true if <b>x</b> is a list, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("loop", "(loop [bindings*] expr*)", """
<br /><code>(loop [bindings*] expr*)</code><br /><br />
Creates a new lexical context with variables defined in bindings and defines a recursion point at the top of the loop.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("macroexpand", "(macroexpand form)", """
<br /><code>(macroexpand form)</code><br /><br />
Recursively expands the given form until it is no longer a macro call.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("macroexpand-1", "(macroexpand-1 form)", """
<br /><code>(macroexpand-1 form)</code><br /><br />
Expands the given form once if it is a macro call.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("map", "(map f & xs)", """
<br /><code>(map f & xs)</code><br /><br />
Returns an array consisting of the result of applying <b>f</b> to all of the first items in each <b>xs</b>,<br />
   followed by applying <b>f</b> to all the second items in each <b>xs</b> until anyone of the <b>xs</b> is exhausted.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("map-indexed", "(map-indexed f xs)", """
<br /><code>(map-indexed f xs)</code><br /><br />
Applies <b>f</b> to each element in <b>xs</b>. <b>f</b> is a two-argument function. The first argument<br />
  is an index of the element in the sequence and the second element is the element itself.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("mapcat", "(mapcat f & xs)", """
<br /><code>(mapcat f & xs)</code><br /><br />
Applies <b>f</b> on all <b>xs</b> and concatenate the result.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("max", "(max & numbers)", """
<br /><code>(max & numbers)</code><br /><br />
Returns the numeric maximum of all numbers.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("mean", "(mean xs)", """
<br /><code>(mean xs)</code><br /><br />
Returns the mean of <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("median", "(median xs)", """
<br /><code>(median xs)</code><br /><br />
Returns the median of <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("memoize", "(memoize f)", """
<br /><code>(memoize f)</code><br /><br />
Returns a memoized version of the function <b>f</b>. The memoized function<br />
  caches the return value for each set of arguments.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("merge", "(merge & maps)", """
<br /><code>(merge & maps)</code><br /><br />
Merges multiple maps into one new map. If a key appears in more than one<br />
  collection, then later values replace any previous ones.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("merge-with", "(merge-with f & hash-maps)", """
<br /><code>(merge-with f & hash-maps)</code><br /><br />
Merges multiple maps into one new map. If a key appears in more than one<br />
   collection, the result of <b>(f current-val next-val)</b> is used.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("meta", "(meta obj)", """
<br /><code>(meta obj)</code><br /><br />
Gets the metadata of the given object or definition.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("min", "(min & numbers)", """
<br /><code>(min & numbers)</code><br /><br />
Returns the numeric minimum of all numbers.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("name", "(name x)", """
<br /><code>(name x)</code><br /><br />
Returns the name string of a string, keyword or symbol.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("namespace", "(namespace x)", """
<br /><code>(namespace x)</code><br /><br />
Return the namespace string of a symbol or keyword. Nil if not present.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("nan?", "(nan? x)", """
<br /><code>(nan? x)</code><br /><br />
Checks if <b>x</b> is not a number.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("neg?", "(neg? x)", """
<br /><code>(neg? x)</code><br /><br />
Checks if <b>x</b> is smaller than zero.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("next", "(next xs)", """
<br /><code>(next xs)</code><br /><br />
Returns the sequence of elements after the first element. If there are no elements, returns nil.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("nfirst", "(nfirst xs)", """
<br /><code>(nfirst xs)</code><br /><br />
Same as <b>(next (first xs))</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("nil?", "(nil? x)", """
<br /><code>(nil? x)</code><br /><br />
Returns true if <b>x</b> is nil, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("nnext", "(nnext xs)", """
<br /><code>(nnext xs)</code><br /><br />
Same as <b>(next (next xs))</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("not", "(not x)", """
<br /><code>(not x)</code><br /><br />
The <b>not</b> function returns <b>true</b> if the given value is logical false and <b>false</b> otherwise.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("not-any?", "(not-any? pred xs)", """
<br /><code>(not-any? pred xs)</code><br /><br />
Returns true if <b>(pred x)</b> is logical false for every <b>x</b> in <b>xs</b><br />
   or if <b>xs</b> is empty. Otherwise returns false.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("not-empty", "(not-empty coll)", """
<br /><code>(not-empty coll)</code><br /><br />
Returns <b>coll</b> if it contains elements, otherwise nil.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("not-every?", "(not-every? pred xs)", """
<br /><code>(not-every? pred xs)</code><br /><br />
Returns false if <b>(pred x)</b> is logical true for every <b>x</b> in collection <b>xs</b><br />
   or if <b>xs</b> is empty. Otherwise returns true.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("not=", "(not= a & more)", """
<br /><code>(not= a & more)</code><br /><br />
Checks if all values are unequal. Same as <b>a != b</b> in PHP.
<br /><br />
""", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core"),
            DataFunction("ns", "(ns name imports*)", """
<br /><code>(ns name imports*)</code><br /><br />
Defines the namespace for the current file and adds imports to the environment. Imports can either be uses or requires. The keyword <b>:use</b> is used to import PHP classes, the keyword <b>:require</b> is used to import Phel modules and the keyword <b>:require-file</b> is used to load php files.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("number?", "(number? x)", """
<br /><code>(number? x)</code><br /><br />
Returns true if <b>x</b> is a number, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("odd?", "(odd? x)", """
<br /><code>(odd? x)</code><br /><br />
Checks if <b>x</b> is odd.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("one?", "(one? x)", """
<br /><code>(one? x)</code><br /><br />
Checks if <b>x</b> is one.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("or", "(or & args)", """
<br /><code>(or & args)</code><br /><br />
Evaluates each expression one at a time, from left to right. If a form<br />
returns a logical true value, or returns that value and doesn't evaluate any of<br />
the other expressions, otherwise, it returns the value of the last expression.<br />
Calling or without arguments, returns nil.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("pairs", "(pairs xs)", """
<br /><code>(pairs xs)</code><br /><br />
Gets the pairs of an associative data structure.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("partial", "(partial f & args)", """
<br /><code>(partial f & args)</code><br /><br />
Takes a function <b>f</b> and fewer than normal arguments of <b>f</b> and returns a function<br />
  that a variable number of additional arguments. When call <b>f</b> will be called<br />
  with <b>args</b> and the additional arguments.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("partition", "(partition n xs)", """
<br /><code>(partition n xs)</code><br /><br />
Partition an indexed data structure into vectors of maximum size n. Returns a new vector.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("partition-by", "(partition-by f xs)", """
<br /><code>(partition-by f xs)</code><br /><br />
Applies <b>f</b> to each value in <b>xs</b>, splitting them each time the return value changes.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("peek", "(peek xs)", """
<br /><code>(peek xs)</code><br /><br />
Returns the last element of a sequence.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("persistent", "(persistent coll)", """
<br /><code>(persistent coll)</code><br /><br />
Converts a transient collection to a persistent collection.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("phel->php", "(phel->php x)", """
<br /><code>(phel->php x)</code><br /><br />
Recursively converts a Phel data structure to a PHP array.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("php->phel", "(php->phel x)", """
<br /><code>(php->phel x)</code><br /><br />
Recursively converts a PHP array to Phel data structures.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("php-array-to-map", "(php-array-to-map arr)", """
<br /><code>(php-array-to-map arr)</code><br /><br />
Converts a PHP Array to a map.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("php-array?", "(php-array? x)", """
<br /><code>(php-array? x)</code><br /><br />
Returns true if <b>x</b> is a PHP Array, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("php-associative-array", "(php-associative-array & xs)", """
<br /><code>(php-associative-array & xs)</code><br /><br />
Creates a PHP associative array. An even number of parameters must be provided.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("php-indexed-array", "(php-indexed-array & xs)", """
<br /><code>(php-indexed-array & xs)</code><br /><br />
Creates a PHP indexed array from the given values.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("php-object?", "(php-object? x)", """
<br /><code>(php-object? x)</code><br /><br />
Returns true if <b>x</b> is a PHP object, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("php-resource?", "(php-resource? x)", """
<br /><code>(php-resource? x)</code><br /><br />
Returns true if <b>x</b> is a PHP resource, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("pop", "(pop xs)", """
<br /><code>(pop xs)</code><br /><br />
Removes the last element of the array <b>xs</b>. If the array is empty returns nil.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("pos?", "(pos? x)", """
<br /><code>(pos? x)</code><br /><br />
Checks if <b>x</b> is greater than zero.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("print", "(print & xs)", """
<br /><code>(print & xs)</code><br /><br />
Prints the given values to the default output stream. Returns nil.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("print-str", "(print-str & xs)", """
<br /><code>(print-str & xs)</code><br /><br />
Same as print. But instead of writing it to an output stream, the resulting string is returned.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("printf", "(printf fmt & xs)", """
<br /><code>(printf fmt & xs)</code><br /><br />
Output a formatted string. See PHP's <a href="https://www.php.net/manual/en/function.printf.php">printf</a> for more information.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("println", "(println & xs)", """
<br /><code>(println & xs)</code><br /><br />
Same as print followed by a newline.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("push", "(push xs x)", """
<br /><code>(push xs x)</code><br /><br />
Inserts <b>x</b> at the end of the sequence <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("put", "(put ds key value)", """
<br /><code>(put ds key value)</code><br /><br />
Puts <b>value</b> mapped to <b>key</b> on the datastructure <b>ds</b>. Returns <b>ds</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("put-in", "(put-in ds [k & ks] v)", """
<br /><code>(put-in ds [k & ks] v)</code><br /><br />
Puts a value into a nested data structure.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("quote", "(NAME_QUOTE)", """
<br /><code>(NAME_QUOTE)</code><br />
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("rand", "(rand )", """
<br /><code>(rand )</code><br /><br />
Returns a random number between 0 and 1.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("rand-int", "(rand-int n)", """
<br /><code>(rand-int n)</code><br /><br />
Returns a random number between 0 and <b>n</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("rand-nth", "(rand-nth xs)", """
<br /><code>(rand-nth xs)</code><br /><br />
Returns a random item from xs.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("range", "(range a & rest)", """
<br /><code>(range a & rest)</code><br /><br />
Create an array of values <b>[start, end)</b>. If the function has one argument then<br />
  the range <b>[0, end)</b> is returned. With two arguments, returns <b>[start, end)</b>.<br />
  The third argument is an optional step width (default 1).
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("re-seq", "(re-seq re s)", """
<br /><code>(re-seq re s)</code><br /><br />
Returns a sequence of successive matches of pattern in string.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("read-string", "(read-string s)", """
<br /><code>(read-string s)</code><br /><br />
Reads the first phel expression from the string s.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("recur", "(recur expr*)", """
<br /><code>(recur expr*)<br />
Internally recur is implemented as a PHP while loop and therefore prevents the Maximum function nesting level errors..</code><br />
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("reduce", "(reduce f & xs)", """
<br /><code>(reduce f & xs)</code><br /><br />
(reduce f coll) (reduce f val coll)<br />
    f should be a function of 2 arguments. If val is not supplied, returns the result of applying f to the first 2 items in coll, then applying f to that result and the 3rd item, etc.<br />
    If coll contains no items, f must accept no arguments as well, and reduce returns the result of calling f with no arguments. If coll has only 1 item, it is returned and f is not called.<br />
    If val is supplied, returns the result of applying f to val and the first item in coll, then applying f to that result and the 2nd item, etc. If coll contains no items, returns val and f is not called.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("remove", "(remove xs offset & [n])", """
<br /><code>(remove xs offset & [n])</code><br /><br />
Removes up to <b>n</b> element from array <b>xs</b> starting at index <b>offset</b>.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("repeat", "(repeat a & rest)", """
<br /><code>(repeat a & rest)</code><br /><br />
Returns a vector of length n where every element is x.<br />
  With one argument returns an infinite sequence of x.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("repeatedly", "(repeatedly a & rest)", """
<br /><code>(repeatedly a & rest)</code><br /><br />
Returns a vector of length n with values produced by repeatedly calling f.<br />
  With one argument returns an infinite sequence of calls to f.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("rest", "(rest xs)", """
<br /><code>(rest xs)</code><br /><br />
Returns the sequence of elements after the first element. If there are no elements, returns an empty sequence.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("reverse", "(reverse xs)", """
<br /><code>(reverse xs)</code><br /><br />
Reverses the order of the elements in the given sequence.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("second", "(second xs)", """
<br /><code>(second xs)</code><br /><br />
Returns the second element of an indexed sequence or nil.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("select-keys", "(select-keys m ks)", """
<br /><code>(select-keys m ks)</code><br /><br />
Returns a new map including key value pairs from <b>m</b> selected with of keys <b>ks</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("set", "(set & xs)", """
<br /><code>(set & xs)</code><br /><br />
Creates a new Set. If no argument is provided, an empty Set is created.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("set!", "(set! variable value)", """
<br /><code>(set! variable value)</code><br /><br />
Sets a new value to the given variable.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("set-meta!", "(set-meta! obj)", """
<br /><code>(set-meta! obj)</code><br /><br />
Sets the metadata to a given object.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("set-var", "(var value)", """
<br /><code>(var value)</code><br /><br />
Variables provide a way to manage mutable state. Each variable contains a single value. To create a variable use the var function.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("set?", "(set? x)", """
<br /><code>(set? x)</code><br /><br />
Returns true if <b>x</b> is a set, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("shuffle", "(shuffle xs)", """
<br /><code>(shuffle xs)</code><br /><br />
Returns a random permutation of xs.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("slice", "(slice xs & [offset & [length]])", """
<br /><code>(slice xs & [offset & [length]])</code><br /><br />
Extract a slice of <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("slurp", "(slurp filename & [opts])", """
<br /><code>(slurp filename & [opts])</code><br /><br />
Reads entire file into a string. Accepts <b>opts</b> map for overriding default<br />
  PHP file_get_contents arguments.<br />
  See PHP's <a href="https://www.php.net/manual/en/function.file-get-contents.php">file_get_contents</a> for more information.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("some", "(some pred xs)", """
<br /><code>(some pred xs)</code><br /><br />
Returns the first logical true value of <b>(pred x)</b> for any <b>x</b> in <b>xs</b>, else nil.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("some->", "(some-> x & forms)", """
<br /><code>(some-> x & forms)</code><br /><br />
Threads <b>x</b> through the forms like <b>-></b> but stops when a form returns <b>nil</b>.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("some->>", "(some->> x & forms)", """
<br /><code>(some->> x & forms)</code><br /><br />
Threads <b>x</b> through the forms like <b>->></b> but stops when a form returns <b>nil</b>.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("some?", "(some? pred xs)", """
<br /><code>(some? pred xs)</code><br /><br />
Returns true if <b>(pred x)</b> is logical true for at least one <b>x</b> in <b>xs</b>, else false.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("sort", "(sort xs & [comp])", """
<br /><code>(sort xs & [comp])</code><br /><br />
Returns a sorted vector. If no comparator is supplied compare is used.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("sort-by", "(sort-by keyfn xs & [comp])", """
<br /><code>(sort-by keyfn xs & [comp])</code><br /><br />
Returns a sorted vector where the sort order is determined by comparing<br />
  <b>(keyfn item)</b>. If no comparator is supplied compare is used.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("spit", "(spit filename data & [opts])", """
<br /><code>(spit filename data & [opts])</code><br /><br />
Writes data to file, returning number of bytest that were written or <b>nil</b><br />
  on failure. Accepts <b>opts</b> map for overriding default PHP file_put_contents<br />
  arguments, as example to append to file use <code>{:flags php/FILE_APPEND}</code>.<br />
  See PHP's <a href="https://www.php.net/manual/en/function.file-put-contents.php">file_put_contents</a> for more information.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("split-at", "(split-at n xs)", """
<br /><code>(split-at n xs)</code><br /><br />
Returns a vector of <b>[(take n coll) (drop n coll)]</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("split-with", "(split-with f xs)", """
<br /><code>(split-with f xs)</code><br /><br />
Returns a vector of <b>[(take-while pred coll) (drop-while pred coll)]</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("str", "(str & args)", """
<br /><code>(str & args)</code><br /><br />
Creates a string by concatenating values together. If no arguments are<br />
provided an empty string is returned. Nil and false are represented as an empty<br />
string. True is represented as 1. Otherwise, it tries to call <b>__toString</b>.<br />
This is PHP equivalent to <b>${'$'}args[0] . ${'$'}args[1] . ${'$'}args[2] ...</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("str-contains?", "(str-contains? str s)", """
<br /><code>(str-contains? str s)</code><br /><br />
Returns true if str contains s.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("string?", "(string? x)", """
<br /><code>(string? x)</code><br /><br />
Returns true if <b>x</b> is a string, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("struct?", "(struct? x)", """
<br /><code>(struct? x)</code><br /><br />
Returns true if <b>x</b> is a struct, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("sum", "(sum xs)", """
<br /><code>(sum xs)</code><br /><br />
Returns the sum of all elements is <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("swap!", "(swap! variable f & args)", """
<br /><code>(swap! variable f & args)</code><br /><br />
Swaps the value of the variable to <b>(apply f current-value args)</b>. Returns the values that are swapped in.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("symbol", "(symbol name-or-ns & [name])", """
<br /><code>(symbol name-or-ns & [name])</code><br /><br />
Returns a new symbol for given string with optional namespace.<br />
   Arity-1 returns a symbol without namespace. Arity-2 returns a symbol in given namespace.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("symbol?", "(symbol? x)", """
<br /><code>(symbol? x)</code><br /><br />
Returns true if <b>x</b> is a symbol, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("symmetric-difference", "(symmetric-difference set & sets)", """
<br /><code>(symmetric-difference set & sets)</code><br /><br />
Symmetric difference between multiple sets into a new one.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("take", "(take n xs)", """
<br /><code>(take n xs)</code><br /><br />
Takes the first <b>n</b> elements of <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core"),
            DataFunction("take-last", "(take-last n xs)", """
<br /><code>(take-last n xs)</code><br /><br />
Takes the last <b>n</b> elements of <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("take-nth", "(take-nth n xs)", """
<br /><code>(take-nth n xs)</code><br /><br />
Returns every nth item in <b>xs</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("take-while", "(take-while pred xs)", """
<br /><code>(take-while pred xs)</code><br /><br />
Takes all elements at the front of <b>xs</b> where <b>(pred x)</b> is true.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("throw", "(throw exception)", """
<br /><code>(throw exception)</code><br /><br />
Throw an exception.<br />
See <a href="/documentation/control-flow/#try-catch-and-finally">try-catch</a>.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("time", "(time expr)", """
<br /><code>(time expr)</code><br /><br />
Evaluates expr and prints the time it took. Returns the value of expr.
<br /><br />
""", PhelCompletionPriority.MACROS, "core"),
            DataFunction("to-php-array", "(to-php-array xs)", """
<br /><code>(to-php-array xs)</code><br /><br />
Create a PHP Array from a sequential data structure.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("transient", "(transient coll)", """
<br /><code>(transient coll)</code><br /><br />
Converts a persistent collection to a transient collection.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("tree-seq", "(tree-seq branch? children root)", """
<br /><code>(tree-seq branch? children root)</code><br /><br />
Returns a vector of the nodes in the tree, via a depth-first walk.<br />
  branch? is a function with one argument that returns true if the given node<br />
  has children.<br />
  children must be a function with one argument that returns the children of the node.<br />
  root the root node of the tree.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("true?", "(true? x)", """
<br /><code>(true? x)</code><br /><br />
Checks if <b>x</b> is true. Same as <b>x === true</b> in PHP.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("truthy?", "(truthy? x)", """
<br /><code>(truthy? x)</code><br /><br />
Checks if <b>x</b> is truthy. Same as <b>x == true</b> in PHP.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("try", "(try expr* catch-clause* finally-clause?)", """
<br /><code>(try expr* catch-clause* finally-clause?)</code><br /><br />
All expressions are evaluated and if no exception is thrown the value of the last expression is returned. If an exception occurs and a matching catch-clause is provided, its expression is evaluated and the value is returned. If no matching catch-clause can be found the exception is propagated out of the function. Before returning normally or abnormally the optionally finally-clause is evaluated.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("type", "(type x)", """
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
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("union", "(union & sets)", """
<br /><code>(union & sets)</code><br /><br />
Union multiple sets into a new one.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("unquote", "(unquote my-sym)", """
<br /><code>(unquote my-sym) # Evaluates to my-sym<br />
,my-sym          # Shorthand for (same as above)</code><br /><br />
Values that should be evaluated in a macro are marked with the unquote function (shorthand <b>,</b>).
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("unquote-splicing", "(unquote-splicing my-sym)", """
<br /><code>(unquote-splicing my-sym) # Evaluates to my-sym<br />
,@my-sym                  # Shorthand for (same as above)</code><br /><br />
Values that should be evaluated in a macro are marked with the unquote function (shorthand <b>,@</b>).
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("unset", "(unset ds key)", """
<br /><code>(unset ds key)</code><br /><br />
Returns <b>ds</b> without <b>key</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("unset-in", "(unset-in ds [k & ks])", """
<br /><code>(unset-in ds [k & ks])</code><br /><br />
Removes a value from a nested data structure.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("update", "(update ds k f & args)", """
<br /><code>(update ds k f & args)</code><br /><br />
Updates a value in a datastructure by applying <b>f</b> to the current element and replacing it with the result of <b>f</b>.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("update-in", "(update-in ds [k & ks] f & args)", """
<br /><code>(update-in ds [k & ks] f & args)</code><br /><br />
Updates a value into a nested data structure.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("values", "(values xs)", """
<br /><code>(values xs)</code><br /><br />
Gets the values of an associative data structure.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("var", "(var value)", """
<br /><code>(var value)</code><br /><br />
Creates a new variable with the given value.
<br /><br />
""", PhelCompletionPriority.SPECIAL_FORMS, "core"),
            DataFunction("var?", "(var? x)", """
<br /><code>(var? x)</code><br /><br />
Checks if the given value is a variable.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("vector", "(vector & xs) # [& xs]", """
<br /><code>(vector & xs) # [& xs]</code><br /><br />
Creates a new vector. If no argument is provided, an empty vector is created.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("vector?", "(vector? x)", """
<br /><code>(vector? x)</code><br /><br />
Returns true if <b>x</b> is a vector, false otherwise.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("when", "(when test & body)", """
<br /><code>(when test & body)</code><br /><br />
Evaluates <b>test</b> and if that is logical true, evaluates <b>body</b>.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("when-let", "(when-let bindings & body)", """
<br /><code>(when-let bindings & body)</code><br /><br />
When test is true, evaluates body with binding-form bound to the value of test
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("when-not", "(when-not test & body)", """
<br /><code>(when-not test & body)</code><br /><br />
Evaluates <b>test</b> and if that is logical false, evaluates <b>body</b>.
<br /><br />
""", PhelCompletionPriority.CONTROL_FLOW, "core"),
            DataFunction("with-output-buffer", "(with-output-buffer & body)", """
<br /><code>(with-output-buffer & body)</code><br /><br />
Everything that is printed inside the body will be stored in a buffer.<br />
   The result of the buffer is returned.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("zero?", "(zero? x)", """
<br /><code>(zero? x)</code><br /><br />
Checks if <b>x</b> is zero.
<br /><br />
""", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core"),
            DataFunction("zipcoll", "(zipcoll a b)", """
<br /><code>(zipcoll a b)</code><br /><br />
Creates a map from two sequential data structures. Return a new map.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
            DataFunction("zipmap", "(zipmap keys vals)", """
<br /><code>(zipmap keys vals)</code><br /><br />
Returns a new map with the keys mapped to the corresponding values. Stops when the shorter of <b>keys</b> or <b>vals</b> is exhausted.
<br /><br />
""", PhelCompletionPriority.CORE_FUNCTIONS, "core"),
        )
    }

    private fun registerDebugFunctions() {
        functions[Namespace.DEBUG] = listOf(
            DataFunction("debug/dbg", "(dbg expr)", """
<br /><code>(dbg expr)</code><br /><br />
Evaluates <b>expr</b>, prints the expression and the resulting value.<br />
   Returns the value of <b>expr</b>.
<br /><br />
""", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug"),
            DataFunction("debug/dotrace", "(dotrace name f)", """
<br /><code>(dotrace name f)</code><br /><br />
Wrap <b>f</b> so each call and result are printed with indentation.
<br /><br />
""", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug"),
            DataFunction("debug/reset-trace-state!", "(reset-trace-state! )", """
<br /><code>(reset-trace-state! )</code><br /><br />
Resets the internal counters used for tracing.
<br /><br />
""", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug"),
            DataFunction("debug/set-trace-id-padding!", "(set-trace-id-padding! estimated-id-padding)", """
<br /><code>(set-trace-id-padding! estimated-id-padding)</code><br /><br />
Sets the number of digits used to left-pad trace IDs.<br />
  Call this once before tracing begins to ensure aligned output.
<br /><br />
""", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug"),
            DataFunction("debug/spy", "(spy expr)", """
<br /><code>(spy expr)</code><br /><br />
Evaluates <b>expr</b>, prints the resulting value with an optional label, and returns it.
<br /><br />
""", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug"),
            DataFunction("debug/tap", "(tap value)", """
<br /><code>(tap value)</code><br /><br />
Executes optional side-effects on <b>value</b> and returns it unchanged.<br />
  Without a handler function the value is printed using <b>print-str</b>.
<br /><br />
""", PhelCompletionPriority.DEBUG_FUNCTIONS, "debug"),
        )
    }

    private fun registerHtmlFunctions() {
        functions[Namespace.HTML] = listOf(
            DataFunction("html/doctype", "(doctype type)", """
<br /><code>(doctype type)</code><br /><br />

<br /><br />
""", PhelCompletionPriority.HTML_FUNCTIONS, "html"),
            DataFunction("html/escape-html", "(escape-html s)", """
<br /><code>(escape-html s)</code><br /><br />
Escapes the string that may contain HTML.
<br /><br />
""", PhelCompletionPriority.HTML_FUNCTIONS, "html"),
            DataFunction("html/html", "(html & content)", """
<br /><code>(html & content)</code><br /><br />

<br /><br />
""", PhelCompletionPriority.HTML_FUNCTIONS, "html"),
            DataFunction("html/raw-string", "(raw-string s)", """
<br /><code>(raw-string s)</code><br /><br />
Creates a new raw-string struct.
<br /><br />
""", PhelCompletionPriority.HTML_FUNCTIONS, "html"),
            DataFunction("html/raw-string?", "(raw-string? x)", """
<br /><code>(raw-string? x)</code><br /><br />
Checks if <b>x</b> is an instance of the raw-string struct.
<br /><br />
""", PhelCompletionPriority.HTML_FUNCTIONS, "html"),
        )
    }

    private fun registerHttpFunctions() {
        functions[Namespace.HTTP] = listOf(
            DataFunction("http/create-response-from-map", "", """

<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/create-response-from-string", "", """

<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/emit-response", "(emit-response response)", """
<br /><code>(emit-response response)</code><br /><br />
Emits the response.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/files-from-globals", "(files-from-globals & [files])", """
<br /><code>(files-from-globals & [files])</code><br /><br />
Extracts the files from <b>'${'$'}_'FILES</b> and normalizes them to a map of \"uploaded-file\".
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/headers-from-server", "(headers-from-server & [server])", """
<br /><code>(headers-from-server & [server])</code><br /><br />
Extracts all headers from the <b>${'$'}_SERVER</b> variable.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/request", "(request method uri headers parsed-body query-params cookie-params server-params uploaded-files version attributes)", """
<br /><code>(request method uri headers parsed-body query-params cookie-params server-params uploaded-files version attributes)</code><br /><br />
Creates a new request struct.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/request-from-globals", "(request-from-globals )", """
<br /><code>(request-from-globals )</code><br /><br />
Extracts a request from <b>'${'$'}_'SERVER</b>, <b>'${'$'}_'GET</b>, <b>'${'$'}_'POST</b>, <b>'${'$'}_'COOKIE</b> and <b>'${'$'}_'FILES</b>.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/request-from-globals-args", "(request-from-globals-args server get-parameter post-parameter cookies files)", """
<br /><code>(request-from-globals-args server get-parameter post-parameter cookies files)</code><br /><br />
Extracts a request from args.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/request-from-map", "(request-from-map {:method method, :version version, :uri uri, :headers headers, :parsed-body parsed-body, :query-params query-params, :cookie-params cookie-params, :server-params server-params, :uploaded-files uploaded-files, :attributes attributes})", """
<br /><code>(request-from-map <code>{:method method, :version version, :uri uri, :headers headers, :parsed-body parsed-body, :query-params query-params, :cookie-params cookie-params, :server-params server-params, :uploaded-files uploaded-files, :attributes attributes}</code>)</code><br /><br />

<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/request?", "(request? x)", """
<br /><code>(request? x)</code><br /><br />
Checks if <b>x</b> is an instance of the request struct.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/response", "(response status headers body version reason)", """
<br /><code>(response status headers body version reason)</code><br /><br />
Creates a new response struct.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/response-from-map", "(response-from-map {:status status, :headers headers, :body body, :version version, :reason reason})", """
<br /><code>(response-from-map <code>{:status status, :headers headers, :body body, :version version, :reason reason}</code>)</code><br /><br />
Creates a response struct from a map. The map can have the following keys:<br />
  * <b>:status</b> The HTTP Status (default 200)<br />
  * <b>:headers</b> A map of HTTP Headers (default: empty map)<br />
  * <b>:body</b> The body of the response (default: empty string)<br />
  * <b>:version</b> The HTTP Version (default: 1.1)<br />
  * <b>:reason</b> The HTTP status reason. If not provided a common status reason is taken
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/response-from-string", "(response-from-string s)", """
<br /><code>(response-from-string s)</code><br /><br />
Create a response from a string.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/response?", "(response? x)", """
<br /><code>(response? x)</code><br /><br />
Checks if <b>x</b> is an instance of the response struct.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/uploaded-file", "(uploaded-file tmp-file size error-status client-filename client-media-type)", """
<br /><code>(uploaded-file tmp-file size error-status client-filename client-media-type)</code><br /><br />
Creates a new uploaded-file struct.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/uploaded-file?", "(uploaded-file? x)", """
<br /><code>(uploaded-file? x)</code><br /><br />
Checks if <b>x</b> is an instance of the uploaded-file struct.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/uri", "(uri scheme userinfo host port path query fragment)", """
<br /><code>(uri scheme userinfo host port path query fragment)</code><br /><br />
Creates a new uri struct.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/uri-from-globals", "(uri-from-globals & [server])", """
<br /><code>(uri-from-globals & [server])</code><br /><br />
Extracts the URI from the <b>${'$'}_SERVER</b> variable.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/uri-from-string", "(uri-from-string url)", """
<br /><code>(uri-from-string url)</code><br /><br />
Create a uri struct from a string
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
            DataFunction("http/uri?", "(uri? x)", """
<br /><code>(uri? x)</code><br /><br />
Checks if <b>x</b> is an instance of the uri struct.
<br /><br />
""", PhelCompletionPriority.HTTP_FUNCTIONS, "http"),
        )
    }

    private fun registerJsonFunctions() {
        functions[Namespace.JSON] = listOf(
            DataFunction("json/decode", "(decode json & [{:flags flags, :depth depth}])", """
<br /><code>(decode json & [<code>{:flags flags, :depth depth}</code>])</code><br /><br />
Decodes a JSON string.
<br /><br />
""", PhelCompletionPriority.JSON_FUNCTIONS, "json"),
            DataFunction("json/decode-value", "(decode-value x)", """
<br /><code>(decode-value x)</code><br /><br />
Convert a json data structure to a 'phel compatible' value.
<br /><br />
""", PhelCompletionPriority.JSON_FUNCTIONS, "json"),
            DataFunction("json/encode", "(encode value & [{:flags flags, :depth depth}])", """
<br /><code>(encode value & [<code>{:flags flags, :depth depth}</code>])</code><br /><br />
Returns the JSON representation of a value.
<br /><br />
""", PhelCompletionPriority.JSON_FUNCTIONS, "json"),
            DataFunction("json/encode-value", "(encode-value x)", """
<br /><code>(encode-value x)</code><br /><br />
Convert a Phel data type to a 'json compatible' value.
<br /><br />
""", PhelCompletionPriority.JSON_FUNCTIONS, "json"),
            DataFunction("json/valid-key?", "(valid-key? v)", """
<br /><code>(valid-key? v)</code><br /><br />
Checks if <b>v</b> is a valid JSON key or can be converted to a JSON key.
<br /><br />
""", PhelCompletionPriority.JSON_FUNCTIONS, "json"),
        )
    }

    private fun registerPhpInteropFunctions() {
        functions[Namespace.PHP] = listOf(
            DataFunction("php/->", "(php/-> object call*)", """
<br /><code>(php/-> object call*)<br />
(php/:: class call*)</code><br /><br />
Access to an object property or result of chained calls.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/::", "(php/:: class call*)", """
<br /><code>(php/:: class (methodname expr*))<br />
(php/:: class call*)</code><br /><br />
<br />
Calls a static method or property from a PHP class. Both methodname and property must be symbols and cannot be an evaluated value.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/aget", "(php/aget arr index)", """
<br /><code>(php/aget arr index)</code><br /><br />
Equivalent to PHP's <b>arr[index] ?? null</b>.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/aget-in", "(php/aget-in arr ks)", """
<br /><code>(php/aget-in arr ks)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...] ?? null</b>.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/apush", "(php/apush arr value)", """
<br /><code>(php/apush arr value)</code><br /><br />
Equivalent to PHP's <b>arr[] = value</b>.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/apush-in", "(php/apush-in arr ks value)", """
<br /><code>(php/apush-in arr ks value)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...][] = value</b>.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/aset", "(php/aset arr index value)", """
<br /><code>(php/aset arr index value)</code><br /><br />
Equivalent to PHP's <b>arr[index] = value</b>.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/aset-in", "(php/aset-in arr ks value)", """
<br /><code>(php/aset-in arr ks value)</code><br /><br />
Equivalent to PHP's <b>arr[k1][k2][k...] = value</b>.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/aunset", "(php/aunset arr index)", """
<br /><code>(php/aunset arr index)</code><br /><br />
Equivalent to PHP's <b>unset(arr[index])</b>.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/aunset-in", "(php/aunset-in arr ks)", """
<br /><code>(php/aunset-in arr ks)</code><br /><br />
Equivalent to PHP's <b>unset(arr[k1][k2][k...])</b>.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/new", "(php/new expr args*)", """
<br /><code>(php/new expr args*)</code><br /><br />
Evaluates expr and creates a new PHP class using the arguments. The instance of the class is returned.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
            DataFunction("php/oset", "(php/oset (php/-> object prop) val)", """
<br /><code>(php/oset (php/-> object property) value)<br />
(php/oset (php/:: class property) value)</code><br /><br />
Use <b>php/oset</b> to set a value to a class/object property.
<br /><br />
""", PhelCompletionPriority.PHP_INTEROP, "php"),
        )
    }

    private fun registerReplFunctions() {
        functions[Namespace.REPL] = listOf(
            DataFunction("repl/build-facade", "", """

<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
            DataFunction("repl/compile-str", "(compile-str s)", """
<br /><code>(compile-str s)</code><br /><br />

<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
            DataFunction("repl/doc", "(doc sym)", """
<br /><code>(doc sym)</code><br /><br />
Prints the documentation for the given symbol.
<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
            DataFunction("repl/loaded-namespaces", "(loaded-namespaces )", """
<br /><code>(loaded-namespaces )</code><br /><br />
Return all namespaces currently loaded in the REPL.
<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
            DataFunction("repl/print-colorful", "(print-colorful & xs)", """
<br /><code>(print-colorful & xs)</code><br /><br />
Colored print.
<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
            DataFunction("repl/println-colorful", "(println-colorful & xs)", """
<br /><code>(println-colorful & xs)</code><br /><br />
Colored println.
<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
            DataFunction("repl/require", "(require sym & args)", """
<br /><code>(require sym & args)</code><br /><br />
Requires a Phel module into the environment.
<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
            DataFunction("repl/resolve", "(resolve sym)", """
<br /><code>(resolve sym)</code><br /><br />
Resolves the given symbol in the current environment and returns a<br />
   resolved Symbol with the absolute namespace or nil if it cannot be resolved.
<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
            DataFunction("repl/use", "(use sym & args)", """
<br /><code>(use sym & args)</code><br /><br />
Adds a use statement to the environment.
<br /><br />
""", PhelCompletionPriority.REPL_FUNCTIONS, "repl"),
        )
    }

    private fun registerStringFunctions() {
        functions[Namespace.STR] = listOf(
            DataFunction("str/blank?", "(blank? s)", """
<br /><code>(blank? s)</code><br /><br />
True if s is nil, empty, or contains only whitespace.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/capitalize", "(capitalize s)", """
<br /><code>(capitalize s)</code><br /><br />
Converts first character of the string to upper-case, all other<br />
  characters to lower-case. Handles multibyte characters.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/contains?", "(contains? s substr)", """
<br /><code>(contains? s substr)</code><br /><br />
True if s contains substr.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/ends-with?", "(ends-with? s substr)", """
<br /><code>(ends-with? s substr)</code><br /><br />
True if s ends with substr.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/escape", "(escape s cmap)", """
<br /><code>(escape s cmap)</code><br /><br />
Return a new string, using cmap to escape each character ch<br />
   from s as follows:<br />
<br />
   If (cmap ch) is nil, append ch to the new string.<br />
   If (cmap ch) is non-nil, append (str (cmap ch)) instead.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/includes?", "(includes? s substr)", """
<br /><code>(includes? s substr)</code><br /><br />
True if s includes substr.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/index-of", "(index-of s value & [from-index])", """
<br /><code>(index-of s value & [from-index])</code><br /><br />
Return index of value (string or char) in s, optionally searching<br />
  forward from from-index. Return nil if value not found.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/join", "(join separator & [coll])", """
<br /><code>(join separator & [coll])</code><br /><br />
Returns a string of all elements in coll, as returned by (seq coll),<br />
   separated by an optional separator.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/last-index-of", "(last-index-of s value & [from-index])", """
<br /><code>(last-index-of s value & [from-index])</code><br /><br />
Return last index of value (string or char) in s, optionally<br />
  searching backward from from-index. Return nil if value not found.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/lower-case", "(lower-case s)", """
<br /><code>(lower-case s)</code><br /><br />
Converts string to all lower-case. Handles multibyte characters.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/pad-both", "(pad-both s len & [pad-str])", """
<br /><code>(pad-both s len & [pad-str])</code><br /><br />
Returns a string padded on both sides to length <b>len</b>.<br />
   If <b>pad-str</b> is omitted it defaults to a single space.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/pad-left", "(pad-left s len & [pad-str])", """
<br /><code>(pad-left s len & [pad-str])</code><br /><br />
Returns a string padded on the left side to length <b>len</b>.<br />
   If <b>pad-str</b> is omitted it defaults to a single space.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/pad-right", "(pad-right s len & [pad-str])", """
<br /><code>(pad-right s len & [pad-str])</code><br /><br />
Returns a string padded on the right side to length <b>len</b>.<br />
   If <b>pad-str</b> is omitted it defaults to a single space.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/re-quote-replacement", "(re-quote-replacement replacement)", """
<br /><code>(re-quote-replacement replacement)</code><br /><br />
Given a replacement string that you wish to be a literal<br />
   replacement for a pattern match in replace or replace-first, do the<br />
   necessary escaping of special characters in the replacement.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/repeat", "(repeat s n)", """
<br /><code>(repeat s n)</code><br /><br />
Returns a string containing n copies of s.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/replace", "(replace s match replacement)", """
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
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/replace-first", "(replace-first s match replacement)", """
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
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/reverse", "(reverse s)", """
<br /><code>(reverse s)</code><br /><br />
Returns s with its characters reversed.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/split", "(split s re & [limit])", """
<br /><code>(split s re & [limit])</code><br /><br />
Splits string on a regular expression.  Optional argument limit is<br />
  the maximum number of parts. Not lazy. Returns vector of the parts.<br />
  Trailing empty strings are not returned - pass limit of -1 to return all.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/split-lines", "(split-lines s)", """
<br /><code>(split-lines s)</code><br /><br />
Splits s on \n or \r\n. Trailing empty lines are not returned.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/starts-with?", "(starts-with? s substr)", """
<br /><code>(starts-with? s substr)</code><br /><br />
True if s starts with substr.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/subs", "(subs s start & [end])", """
<br /><code>(subs s start & [end])</code><br /><br />
Returns the substring of <b>s</b> from <b>start</b> (inclusive) to <b>end</b> (exclusive).<br />
   If <b>end</b> is omitted, the substring extends to the end of <b>s</b>.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/trim", "(trim s)", """
<br /><code>(trim s)</code><br /><br />
Removes whitespace from both ends of string.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/trim-newline", "(trim-newline s)", """
<br /><code>(trim-newline s)</code><br /><br />
Removes all trailing newline \n or return \r characters from<br />
  string.  Similar to Perl's chomp.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/triml", "(triml s)", """
<br /><code>(triml s)</code><br /><br />
Removes whitespace from the left side of string.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/trimr", "(trimr s)", """
<br /><code>(trimr s)</code><br /><br />
Removes whitespace from the right side of string.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
            DataFunction("str/upper-case", "(upper-case s)", """
<br /><code>(upper-case s)</code><br /><br />
Converts string to all upper-case. Handles multibyte characters.
<br /><br />
""", PhelCompletionPriority.STRING_FUNCTIONS, "str"),
        )
    }

    private fun registerTestFunctions() {
        functions[Namespace.TEST] = listOf(
            DataFunction("test/deftest", "(deftest test-name & body)", """
<br /><code>(deftest test-name & body)</code><br /><br />
Defines a test function with no arguments.
<br /><br />
""", PhelCompletionPriority.TEST_FUNCTIONS, "test"),
            DataFunction("test/is", "(is form & [message])", """
<br /><code>(is form & [message])</code><br /><br />
Generic assertion macro.
<br /><br />
""", PhelCompletionPriority.TEST_FUNCTIONS, "test"),
            DataFunction("test/print-summary", "(print-summary )", """
<br /><code>(print-summary )</code><br /><br />
Prints the summary of the test suite.
<br /><br />
""", PhelCompletionPriority.TEST_FUNCTIONS, "test"),
            DataFunction("test/report", "(report data)", """
<br /><code>(report data)</code><br /><br />

<br /><br />
""", PhelCompletionPriority.TEST_FUNCTIONS, "test"),
            DataFunction("test/run-tests", "(run-tests options & namespaces)", """
<br /><code>(run-tests options & namespaces)</code><br /><br />
Runs all test functions in the given namespaces.
<br /><br />
""", PhelCompletionPriority.TEST_FUNCTIONS, "test"),
            DataFunction("test/successful?", "(successful? )", """
<br /><code>(successful? )</code><br /><br />
Checks if all tests have passed.
<br /><br />
""", PhelCompletionPriority.TEST_FUNCTIONS, "test"),
        )
    }
}
