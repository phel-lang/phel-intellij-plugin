package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreFunctions(): List<DataFunction> = listOf(
    DataFunction("%", "(% dividend divisor)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Return the remainder of `dividend` / `divisor`", """
<br /><code>(% dividend divisor)</code><br /><br />
Return the remainder of <b>dividend</b> / <b>divisor</b>.<br />
<br />
"""),
    DataFunction("*", "(* & xs)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the product of all elements in `xs`. All elements in `xs` must benumbers. If `xs` is empty, return 1", """
<br /><code>(* & xs)</code><br /><br />
Returns the product of all elements in <b>xs</b>. All elements in <b>xs</b> must be<br />
numbers. If <b>xs</b> is empty, return 1.<br />
<br />
"""),
    DataFunction("**", "(** a x)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Return `a` to the power of `x`", """
<br /><code>(** a x)</code><br /><br />
Return <b>a</b> to the power of <b>x</b>.<br />
<br />
"""),
    DataFunction("*build-mode*", "", PhelCompletionPriority.MACROS, "core", "Set to true when a file is being built/compiled, false otherwise", """
Set to true when a file is being built/compiled, false otherwise.<br />
<br />
"""),
    DataFunction("*file*", "*file*", PhelCompletionPriority.MACROS, "core", "Returns the path of the current source file", """
<br /><code>*file*</code><br /><br />
Returns the path of the current source file.<br />
<br />
"""),
    DataFunction("*ns*", "*ns*", PhelCompletionPriority.MACROS, "core", "Returns the namespace in the current scope", """
<br /><code>*ns*</code><br /><br />
Returns the namespace in the current scope.<br />
<br />
"""),
    DataFunction("+", "(+ & xs)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the sum of all elements in `xs`. All elements `xs` must be numbers.  If `xs` is empty, return 0", """
<br /><code>(+ & xs)</code><br /><br />
Returns the sum of all elements in <b>xs</b>. All elements <b>xs</b> must be numbers.<br />
  If <b>xs</b> is empty, return 0.<br />
<br />
"""),
    DataFunction("-", "(- & xs)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the difference of all elements in `xs`. If `xs` is empty, return 0. If `xs`  has one element, return the negative value of that element", """
<br /><code>(- & xs)</code><br /><br />
Returns the difference of all elements in <b>xs</b>. If <b>xs</b> is empty, return 0. If <b>xs</b><br />
  has one element, return the negative value of that element.<br />
<br />
"""),
    DataFunction("->", "(-> x & forms)", PhelCompletionPriority.MACROS, "core", "Threads the expr through the forms. Inserts `x` as the second item  in the first form, making a list of it if it is not a list already.  If there are more forms, insert the first form as the second item in  the second form, etc", """
<br /><code>(-> x & forms)</code><br /><br />
Threads the expr through the forms. Inserts <b>x</b> as the second item<br />
  in the first form, making a list of it if it is not a list already.<br />
  If there are more forms, insert the first form as the second item in<br />
  the second form, etc.<br />
<br />
"""),
    DataFunction("->>", "(->> x & forms)", PhelCompletionPriority.MACROS, "core", "Threads the expr through the forms. Inserts `x` as the  last item in the first form, making a list of it if it is not a  list already. If there are more forms, insert the first form as the  last item in the second form, etc", """
<br /><code>(->> x & forms)</code><br /><br />
Threads the expr through the forms. Inserts <b>x</b> as the<br />
  last item in the first form, making a list of it if it is not a<br />
  list already. If there are more forms, insert the first form as the<br />
  last item in the second form, etc.<br />
<br />
"""),
    DataFunction("/", "(/ & xs)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the nominator divided by all the denominators. If `xs` is empty,returns 1. If `xs` has one value, returns the reciprocal of x", """
<br /><code>(/ & xs)</code><br /><br />
Returns the nominator divided by all the denominators. If <b>xs</b> is empty,<br />
returns 1. If <b>xs</b> has one value, returns the reciprocal of x.<br />
<br />
"""),
    DataFunction("<", "(< a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if each argument is strictly less than the following argument", """
<br /><code>(< a & more)</code><br /><br />
Checks if each argument is strictly less than the following argument.<br />
<br />
  <pre><code>(< 1 2 3 4)<br /># => true</code></pre>
<br />
"""),
    DataFunction("<=", "(<= a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if each argument is less than or equal to the following argument. Returns a boolean", """
<br /><code>(<= a & more)</code><br /><br />
Checks if each argument is less than or equal to the following argument. Returns a boolean.<br />
<br />
"""),
    DataFunction("<=>", "(<=> a b)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Alias for the spaceship PHP operator in ascending order. Returns an int", """
<br /><code>(<=> a b)</code><br /><br />
Alias for the spaceship PHP operator in ascending order. Returns an int.<br />
<br />
"""),
    DataFunction("=", "(= a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if all values are equal (value equality, not identity)", """
<br /><code>(= a & more)</code><br /><br />
Checks if all values are equal (value equality, not identity).<br />
<br />
  <pre><code>(= [1 2 3] [1 2 3])<br /># => true</code></pre>
<br />
"""),
    DataFunction(">", "(> a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if each argument is strictly greater than the following argument", """
<br /><code>(> a & more)</code><br /><br />
Checks if each argument is strictly greater than the following argument.<br />
<br />
  <pre><code>(> 4 3 2 1)<br /># => true</code></pre>
<br />
"""),
    DataFunction(">=", "(>= a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if each argument is greater than or equal to the following argument. Returns a boolean", """
<br /><code>(>= a & more)</code><br /><br />
Checks if each argument is greater than or equal to the following argument. Returns a boolean.<br />
<br />
"""),
    DataFunction(">=<", "(>=< a b)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Alias for the spaceship PHP operator in descending order. Returns an int", """
<br /><code>(>=< a b)</code><br /><br />
Alias for the spaceship PHP operator in descending order. Returns an int.<br />
<br />
"""),
    DataFunction("NAN", "", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Constant for Not a Number (NAN) values", """
Constant for Not a Number (NAN) values.<br />
<br />
"""),
    DataFunction("all?", "(all? pred coll)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if predicate is true for every element in collection, false otherwise", """
<br /><code>(all? pred coll)</code><br /><br />
Returns true if predicate is true for every element in collection, false otherwise.<br />
<br />
  <pre><code>(all? even? [2 4 6 8])<br /># => true</code></pre>
<br />
"""),
    DataFunction("and", "(and & args)", PhelCompletionPriority.MACROS, "core", "Evaluates expressions left to right, returning the first falsy value or the last value", """
<br /><code>(and & args)</code><br /><br />
Evaluates expressions left to right, returning the first falsy value or the last value.<br />
<br />
  <pre><code>(and true 1 \"hello\")<br /># => \"hello\"</code></pre>
<br />
"""),
    DataFunction("apply", "(apply f expr*)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Calls the function with the given arguments. The last argument must be a list of values, which are passed as separate arguments, rather than a single list. Apply returns the result of the calling function", """
<br /><code>(apply f expr*)</code><br /><br />
Calls the function with the given arguments. The last argument must be a list of values, which are passed as separate arguments, rather than a single list. Apply returns the result of the calling function.<br />
<br />
"""),
    DataFunction("argv", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Vector of arguments passed to the script", """
Vector of arguments passed to the script.<br />
<br />
"""),
    DataFunction("as->", "(as-> expr name & forms)", PhelCompletionPriority.MACROS, "core", "Binds `name` to `expr`, evaluates the first form in the lexical context  of that binding, then binds name to that result, repeating for each  successive form, returning the result of the last form", """
<br /><code>(as-> expr name & forms)</code><br /><br />
Binds <b>name</b> to <b>expr</b>, evaluates the first form in the lexical context<br />
  of that binding, then binds name to that result, repeating for each<br />
  successive form, returning the result of the last form.<br />
<br />
"""),
    DataFunction("assert-non-nil", "(assert-non-nil & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "", """
<br /><code>(assert-non-nil & xs)</code><br /><br />
<br />
<br />
"""),
    DataFunction("assoc", "(assoc ds key value)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Associates a value with a key in a collection", """
<br /><code>(assoc ds key value)</code><br /><br />
Associates a value with a key in a collection.<br />
<br />
  <pre><code>(assoc <code>{:a 1}</code> :b 2)<br /># => <code>{:a 1 :b 2}</code></code></pre>
<br />
"""),
    DataFunction("assoc-in", "(assoc-in ds [k & ks] v)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Associates a value in a nested data structure", """
<br /><code>(assoc-in ds [k & ks] v)</code><br /><br />
Associates a value in a nested data structure.<br />
<br />
"""),
    DataFunction("associative?", "(associative? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is an associative data structure, false otherwise", """
<br /><code>(associative? x)</code><br /><br />
Returns true if <b>x</b> is an associative data structure, false otherwise.<br />
<br />
"""),
    DataFunction("binding", "(binding bindings & body)", PhelCompletionPriority.MACROS, "core", "Temporary redefines definitions while executing the body.  The value will be reset after the body was executed", """
<br /><code>(binding bindings & body)</code><br /><br />
Temporary redefines definitions while executing the body.<br />
  The value will be reset after the body was executed.<br />
<br />
"""),
    DataFunction("bit-and", "(bit-and x y & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise and", """
<br /><code>(bit-and x y & args)</code><br /><br />
Bitwise and.<br />
<br />
"""),
    DataFunction("bit-clear", "(bit-clear x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Clear bit an index `n`", """
<br /><code>(bit-clear x n)</code><br /><br />
Clear bit an index <b>n</b>.<br />
<br />
"""),
    DataFunction("bit-flip", "(bit-flip x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Flip bit at index `n`", """
<br /><code>(bit-flip x n)</code><br /><br />
Flip bit at index <b>n</b>.<br />
<br />
"""),
    DataFunction("bit-not", "(bit-not x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise complement", """
<br /><code>(bit-not x)</code><br /><br />
Bitwise complement.<br />
<br />
"""),
    DataFunction("bit-or", "(bit-or x y & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise or", """
<br /><code>(bit-or x y & args)</code><br /><br />
Bitwise or.<br />
<br />
"""),
    DataFunction("bit-set", "(bit-set x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Set bit an index `n`", """
<br /><code>(bit-set x n)</code><br /><br />
Set bit an index <b>n</b>.<br />
<br />
"""),
    DataFunction("bit-shift-left", "(bit-shift-left x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise shift left", """
<br /><code>(bit-shift-left x n)</code><br /><br />
Bitwise shift left.<br />
<br />
"""),
    DataFunction("bit-shift-right", "(bit-shift-right x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise shift right", """
<br /><code>(bit-shift-right x n)</code><br /><br />
Bitwise shift right.<br />
<br />
"""),
    DataFunction("bit-test", "(bit-test x n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Test bit at index `n`", """
<br /><code>(bit-test x n)</code><br /><br />
Test bit at index <b>n</b>.<br />
<br />
"""),
    DataFunction("bit-xor", "(bit-xor x y & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Bitwise xor", """
<br /><code>(bit-xor x y & args)</code><br /><br />
Bitwise xor.<br />
<br />
"""),
    DataFunction("boolean?", "(boolean? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a boolean, false otherwise", """
<br /><code>(boolean? x)</code><br /><br />
Returns true if <b>x</b> is a boolean, false otherwise.<br />
<br />
"""),
    DataFunction("butlast", "(butlast coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns all but the last item in `coll`", """
<br /><code>(butlast coll)</code><br /><br />
Returns all but the last item in <b>coll</b>.<br />
<br />
"""),
    DataFunction("case", "(case e & pairs)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates expression and matches it against constant test values, returning the associated result", """
<br /><code>(case e & pairs)</code><br /><br />
Evaluates expression and matches it against constant test values, returning the associated result.<br />
<br />
  <pre><code>(case x<br />  1 \"one\"<br />  2 \"two\"<br />  \"other\")<br /># => \"one\" (when x is 1)</code></pre>
<br />
"""),
    DataFunction("catch", "(catch exception-type exception-name expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Handle exceptions thrown in a `try` block by matching on the provided exception type. The caught exception is bound to exception-name while evaluating the expressions", """
<br /><code>(catch exception-type exception-name expr*)</code><br /><br />
Handle exceptions thrown in a <b>try</b> block by matching on the provided exception type. The caught exception is bound to <b>exception-name</b> while evaluating the expressions.<br />
<br />
"""),
    DataFunction("coerce-in", "(coerce-in v min max)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns `v` if it is in the range, or `min` if `v` is less than `min`, or `max` if `v` is greater than `max`", """
<br /><code>(coerce-in v min max)</code><br /><br />
Returns <b>v</b> if it is in the range, or <b>min</b> if <b>v</b> is less than <b>min</b>, or <b>max</b> if <b>v</b> is greater than <b>max</b>.<br />
<br />
"""),
    DataFunction("comment", "(comment &)", PhelCompletionPriority.MACROS, "core", "Ignores the body of the comment", """
<br /><code>(comment &)</code><br /><br />
Ignores the body of the comment.<br />
<br />
"""),
    DataFunction("comp", "(comp & fs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes a list of functions and returns a function that is the composition of those functions", """
<br /><code>(comp & fs)</code><br /><br />
Takes a list of functions and returns a function that is the composition of those functions.<br />
<br />
"""),
    DataFunction("compare", "(compare x y)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Wrapper for PHP's spaceship operator (`php/<=>`).  Returns an integer less than, equal to, or greater than zero  when `x` is less than, equal to, or greater than `y`, respectively", """
<br /><code>(compare x y)</code><br /><br />
Wrapper for PHP's spaceship operator (<b>php/<=></b>).<br />
  Returns an integer less than, equal to, or greater than zero<br />
  when <b>x</b> is less than, equal to, or greater than <b>y</b>, respectively.<br />
<br />
"""),
    DataFunction("compile", "(compile form)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the compiled PHP code string for the given form", """
<br /><code>(compile form)</code><br /><br />
Returns the compiled PHP code string for the given form.<br />
<br />
"""),
    DataFunction("complement", "(complement f)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a function that takes the same arguments as `f` and returns the opposite truth value", """
<br /><code>(complement f)</code><br /><br />
Returns a function that takes the same arguments as <b>f</b> and returns the opposite truth value.<br />
<br />
"""),
    DataFunction("concat", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Concatenates multiple collections into a lazy sequence", """
Concatenates multiple collections into a lazy sequence.<br />
<br />
  <pre><code>(concat [1 2] [3 4])<br /># => (1 2 3 4)</code></pre>
<br />
"""),
    DataFunction("cond", "(cond & pairs)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates test/expression pairs, returning the first matching expression", """
<br /><code>(cond & pairs)</code><br /><br />
Evaluates test/expression pairs, returning the first matching expression.<br />
<br />
  <pre><code>(cond (< x 0) \"negative\" (> x 0) \"positive\" \"zero\")<br /># => \"negative\", \"positive\", or \"zero\" depending on x</code></pre>
<br />
"""),
    DataFunction("conj", "(conj )", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns a new collection with values added. Appends to vectors/sets, prepends to lists.    Examples:      (conj [1 2] 3)      # => [1 2 3]      (conj {:a 1} [:b 2])      # => {:a 1 :b 2}", """
<br /><code>(conj )</code><br /><br />
Returns a new collection with values added. Appends to vectors/sets, prepends to lists.<br />
<br />
    Examples:<br />
      (conj [1 2] 3)<br />
      # => [1 2 3]<br />
<br />
      (conj <code>{:a 1}</code> [:b 2])<br />
      # => <code>{:a 1 :b 2}</code><br />
<br />
"""),
    DataFunction("cons", "(cons x coll)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Prepends an element to the beginning of a collection", """
<br /><code>(cons x coll)</code><br /><br />
Prepends an element to the beginning of a collection.<br />
<br />
  <pre><code>(cons 0 [1 2 3])<br /># => [0 1 2 3]</code></pre>
<br />
"""),
    DataFunction("constantly", "(constantly x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a function that always returns `x` and ignores any passed arguments", """
<br /><code>(constantly x)</code><br /><br />
Returns a function that always returns <b>x</b> and ignores any passed arguments.<br />
<br />
"""),
    DataFunction("contains-value?", "(contains-value? coll val)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if the value is present in the given collection, otherwise returns false", """
<br /><code>(contains-value? coll val)</code><br /><br />
Returns true if the value is present in the given collection, otherwise returns false.<br />
<br />
"""),
    DataFunction("contains?", "(contains? coll key)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if key is present in collection (checks keys/indices, not values)", """
<br /><code>(contains? coll key)</code><br /><br />
Returns true if key is present in collection (checks keys/indices, not values).<br />
<br />
  <pre><code>(contains? [10 20 30] 1)<br /># => true</code></pre>
<br />
"""),
    DataFunction("count", "(count coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Counts the number of elements in a sequence. Can be used on everything that implements the PHP Countable interface.  Works with lists, vectors, hash-maps, sets, and PHP arrays.  Returns 0 for nil. Throws an exception for strings (use `php/strlen` or `php/mb_strlen` instead).  Examples:    (count [1 2 3])    # => 3    (count '(:a :b))    # => 2    (count {:name \"Alice\" :age 30})    # => 2    (count #{1 2 3 4})    # => 4    (count [])    # => 0    (count nil)    # => 0  See also: empty?, seq", """
<br /><code>(count coll)</code><br /><br />
Counts the number of elements in a sequence. Can be used on everything that implements the PHP Countable interface.<br />
<br />
  Works with lists, vectors, hash-maps, sets, and PHP arrays.<br />
  Returns 0 for nil. Throws an exception for strings (use <b>php/strlen</b> or <b>php/mb_strlen</b> instead).<br />
<br />
  Examples:<br />
    (count [1 2 3])<br />
    # => 3<br />
<br />
    (count '(:a :b))<br />
    # => 2<br />
<br />
    (count <code>{:name \"Alice\" :age 30}</code>)<br />
    # => 2<br />
<br />
    (count #{1 2 3 4})<br />
    # => 4<br />
<br />
    (count [])<br />
    # => 0<br />
<br />
    (count nil)<br />
    # => 0<br />
<br />
  See also: empty?, seq<br />
<br />
"""),
    DataFunction("csv-seq", "(csv-seq filename)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence of rows from a CSV file", """
<br /><code>(csv-seq filename)</code><br /><br />
Returns a lazy sequence of rows from a CSV file.<br />
<br />
  <pre><code>(take 10 (csv-seq \"data.csv\"))<br /># => [[\"col1\" \"col2\"] [\"val1\" \"val2\"] ...]</code></pre>
<br />
"""),
    DataFunction("cycle", "(cycle coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns an infinite lazy sequence that cycles through the elements of collection", """
<br /><code>(cycle coll)</code><br /><br />
Returns an infinite lazy sequence that cycles through the elements of collection.<br />
<br />
"""),
    DataFunction("dec", "(dec x)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Decrements `x` by one", """
<br /><code>(dec x)</code><br /><br />
Decrements <b>x</b> by one.<br />
<br />
"""),
    DataFunction("declare", "", PhelCompletionPriority.MACROS, "core", "Declare a global symbol before it is defined", """
Declare a global symbol before it is defined.<br />
<br />
"""),
    DataFunction("dedupe", "(dedupe coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence with consecutive duplicate values removed in `coll`", """
<br /><code>(dedupe coll)</code><br /><br />
Returns a lazy sequence with consecutive duplicate values removed in <b>coll</b>.<br />
<br />
"""),
    DataFunction("deep-merge", "(deep-merge & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Recursively merges data structures", """
<br /><code>(deep-merge & args)</code><br /><br />
Recursively merges data structures.<br />
<br />
"""),
    DataFunction("def", "(def name meta? value)", PhelCompletionPriority.SPECIAL_FORMS, "core", "This special form binds a value to a global symbol", """
<br /><code>(def name meta? value)</code><br /><br />
This special form binds a value to a global symbol.<br />
<br />
"""),
    DataFunction("def-", "", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a private value that will not be exported", """
Define a private value that will not be exported.<br />
<br />
"""),
    DataFunction("defexception", "(defexception name)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a new exception", """
<br /><code>(defexception name)</code><br /><br />
Define a new exception.<br />
<br />
"""),
    DataFunction("defexception*", "(defexception name)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Defines a new exception", """
<br /><code>(defexception my-ex)</code><br /><br />
<br />
"""),
    DataFunction("definterface", "(definterface name & fns)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Defines an interface", """
<br /><code>(definterface name & fns)</code><br /><br />
Defines an interface.<br />
<br />
"""),
    DataFunction("definterface*", "(definterface name & fns)", PhelCompletionPriority.SPECIAL_FORMS, "core", "An interface in Phel defines an abstract set of functions. It is directly mapped to a PHP interface. An interface can be defined by using the definterface macro", """
<br /><code>(definterface name & fns)</code><br /><br />
An interface in Phel defines an abstract set of functions. It is directly mapped to a PHP interface. An interface can be defined by using the definterface macro.<br />
<br />
"""),
    DataFunction("defmacro", "", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a macro", """
Define a macro.<br />
<br />
"""),
    DataFunction("defmacro-", "(defmacro- name & fdecl)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a private macro that will not be exported", """
<br /><code>(defmacro- name & fdecl)</code><br /><br />
Define a private macro that will not be exported.<br />
<br />
"""),
    DataFunction("defn", "", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a new global function", """
Define a new global function.<br />
<br />
"""),
    DataFunction("defn-", "(defn- name & fdecl)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a private function that will not be exported", """
<br /><code>(defn- name & fdecl)</code><br /><br />
Define a private function that will not be exported.<br />
<br />
"""),
    DataFunction("defstruct", "(defstruct name keys & implementations)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Define a new struct", """
<br /><code>(defstruct name keys & implementations)</code><br /><br />
Define a new struct.<br />
<br />
"""),
    DataFunction("defstruct*", "(defstruct my-struct [a b c])", PhelCompletionPriority.SPECIAL_FORMS, "core", "A Struct is a special kind of Map. It only supports a predefined number of keys and is associated to a global name. The Struct not only defines itself but also a predicate function", """
<br /><code>(defstruct my-struct [a b c])</code><br /><br />
A Struct is a special kind of Map. It only supports a predefined number of keys and is associated to a global name. The Struct not only defines itself but also a predicate function.<br />
<br />
"""),
    DataFunction("deref", "(deref variable)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Return the value inside the variable", """
<br /><code>(deref variable)</code><br /><br />
Return the value inside the variable.<br />
<br />
"""),
    DataFunction("difference", "(difference set & sets)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Difference between multiple sets into a new one", """
<br /><code>(difference set & sets)</code><br /><br />
Difference between multiple sets into a new one.<br />
<br />
"""),
    DataFunction("difference-pair", "(difference-pair s1 s2)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "", """
<br /><code>(difference-pair s1 s2)</code><br /><br />
<br />
<br />
"""),
    DataFunction("dissoc", "(dissoc ds key)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Dissociates `key` from the datastructure `ds`. Returns `ds` without `key`", """
<br /><code>(dissoc ds key)</code><br /><br />
Dissociates <b>key</b> from the datastructure <b>ds</b>. Returns <b>ds</b> without <b>key</b>.<br />
<br />
"""),
    DataFunction("dissoc-in", "(dissoc-in ds [k & ks])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Dissociates a value from a nested data structure", """
<br /><code>(dissoc-in ds [k & ks])</code><br /><br />
Dissociates a value from a nested data structure.<br />
<br />
"""),
    DataFunction("distinct", "(distinct coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence with duplicated values removed in `coll`", """
<br /><code>(distinct coll)</code><br /><br />
Returns a lazy sequence with duplicated values removed in <b>coll</b>.<br />
<br />
"""),
    DataFunction("do", "(do expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates the expressions in order and returns the value of the last expression. If no expression is given, nil is returned", """
<br /><code>(do expr*)</code><br /><br />
Evaluates the expressions in order and returns the value of the last expression. If no expression is given, nil is returned.<br />
<br />
"""),
    DataFunction("doall", "(doall coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Forces realization of a lazy sequence and returns it as a vector", """
<br /><code>(doall coll)</code><br /><br />
Forces realization of a lazy sequence and returns it as a vector.<br />
<br />
  <pre><code>(doall (map println [1 2 3]))<br /># => [nil nil nil]</code></pre>
<br />
"""),
    DataFunction("dofor", "(dofor head & body)", PhelCompletionPriority.CONTROL_FLOW, "core", "Repeatedly executes body for side effects with bindings and modifiers as  provided by for. Returns nil", """
<br /><code>(dofor head & body)</code><br /><br />
Repeatedly executes body for side effects with bindings and modifiers as<br />
  provided by for. Returns nil.<br />
<br />
"""),
    DataFunction("dorun", "(dorun coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Forces realization of a lazy sequence for side effects, returns nil", """
<br /><code>(dorun coll)</code><br /><br />
Forces realization of a lazy sequence for side effects, returns nil.<br />
<br />
  <pre><code>(dorun (map println [1 2 3]))<br /># => nil</code></pre>
<br />
"""),
    DataFunction("doseq", "(doseq seq-exprs & body)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Alias for `dofor`", """
<br /><code>(doseq seq-exprs & body)</code><br /><br />
Alias for <b>dofor</b>.<br />
<br />
"""),
    DataFunction("doto", "(doto x & forms)", PhelCompletionPriority.MACROS, "core", "Evaluates x then calls all of the methods and functions with the  value of x supplied at the front of the given arguments. The forms  are evaluated in order. Returns x", """
<br /><code>(doto x & forms)</code><br /><br />
Evaluates x then calls all of the methods and functions with the<br />
  value of x supplied at the front of the given arguments. The forms<br />
  are evaluated in order. Returns x.<br />
<br />
"""),
    DataFunction("drop", "(drop n coll)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Drops the first `n` elements of `coll`. Returns a lazy sequence", """
<br /><code>(drop n coll)</code><br /><br />
Drops the first <b>n</b> elements of <b>coll</b>. Returns a lazy sequence.<br />
<br />
"""),
    DataFunction("drop-last", "(drop-last n coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Drops the last `n` elements of `coll`", """
<br /><code>(drop-last n coll)</code><br /><br />
Drops the last <b>n</b> elements of <b>coll</b>.<br />
<br />
"""),
    DataFunction("drop-while", "(drop-while pred coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Drops all elements at the front of `coll` where `(pred x)` is true. Returns a lazy sequence", """
<br /><code>(drop-while pred coll)</code><br /><br />
Drops all elements at the front of <b>coll</b> where <b>(pred x)</b> is true. Returns a lazy sequence.<br />
<br />
"""),
    DataFunction("empty?", "(empty? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if x would be 0, \"\" or empty collection, false otherwise", """
<br /><code>(empty? x)</code><br /><br />
Returns true if x would be 0, \"\" or empty collection, false otherwise.<br />
<br />
"""),
    DataFunction("eval", "(eval form)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Evaluates a form and return the evaluated results", """
<br /><code>(eval form)</code><br /><br />
Evaluates a form and return the evaluated results.<br />
<br />
"""),
    DataFunction("even?", "(even? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is even", """
<br /><code>(even? x)</code><br /><br />
Checks if <b>x</b> is even.<br />
<br />
"""),
    DataFunction("every?", "(every? pred coll)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Alias for `all?`", """
<br /><code>(every? pred coll)</code><br /><br />
Alias for <b>all?</b>.<br />
<br />
"""),
    DataFunction("extreme", "(extreme order args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the most extreme value in `args` based on the binary `order` function", """
<br /><code>(extreme order args)</code><br /><br />
Returns the most extreme value in <b>args</b> based on the binary <b>order</b> function.<br />
<br />
"""),
    DataFunction("false?", "(false? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if value is exactly false (not just falsy)", """
<br /><code>(false? x)</code><br /><br />
Checks if value is exactly false (not just falsy).<br />
<br />
  <pre><code>(false? nil)<br /># => false</code></pre>
<br />
"""),
    DataFunction("ffirst", "(ffirst coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as `(first (first coll))`", """
<br /><code>(ffirst coll)</code><br /><br />
Same as <b>(first (first coll))</b>.<br />
<br />
"""),
    DataFunction("file-seq", "(file-seq path)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence of all files and directories in a directory tree", """
<br /><code>(file-seq path)</code><br /><br />
Returns a lazy sequence of all files and directories in a directory tree.<br />
<br />
  <pre><code>(filter |(php/str_ends_with $ \".phel\") (file-seq \"src/\"))<br /># => [\"src/file1.phel\" \"src/file2.phel\" ...]</code></pre>
<br />
"""),
    DataFunction("filter", "(filter pred coll)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns a lazy sequence of elements where predicate returns true", """
<br /><code>(filter pred coll)</code><br /><br />
Returns a lazy sequence of elements where predicate returns true.<br />
<br />
  <pre><code>(filter even? [1 2 3 4 5 6])<br /># => (2 4 6)</code></pre>
<br />
"""),
    DataFunction("finally", "(finally expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluate expressions after the try body and all matching catches have completed. The finally block runs regardless of whether an exception was thrown", """
<br /><code>(finally expr*)</code><br /><br />
Evaluate expressions after the try body and all matching catches have completed. The finally block runs regardless of whether an exception was thrown.<br />
<br />
"""),
    DataFunction("find", "(find pred coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the first item in `coll` where `(pred item)` evaluates to true", """
<br /><code>(find pred coll)</code><br /><br />
Returns the first item in <b>coll</b> where <b>(pred item)</b> evaluates to true.<br />
<br />
"""),
    DataFunction("find-index", "(find-index pred coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the index of the first item in `coll` where `(pred index item)` evaluates to true", """
<br /><code>(find-index pred coll)</code><br /><br />
Returns the index of the first item in <b>coll</b> where <b>(pred index item)</b> evaluates to true.<br />
<br />
"""),
    DataFunction("first", "", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns the first element of a sequence, or nil if empty", """
Returns the first element of a sequence, or nil if empty.<br />
<br />
  <pre><code>(first [1 2 3])<br /># => 1</code></pre>
<br />
"""),
    DataFunction("flatten", "(flatten coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Flattens nested sequential structure into a lazy sequence of all leaf values", """
<br /><code>(flatten coll)</code><br /><br />
Flattens nested sequential structure into a lazy sequence of all leaf values.<br />
<br />
  <pre><code>(flatten [[1 2] [3 [4 5]] 6])<br /># => (1 2 3 4 5 6)</code></pre>
<br />
"""),
    DataFunction("float?", "(float? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is float point number, false otherwise", """
<br /><code>(float? x)</code><br /><br />
Returns true if <b>x</b> is float point number, false otherwise.<br />
<br />
"""),
    DataFunction("fn", "(fn [params*] expr*)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Defines a function. A function consists of a list of parameters and a list of expression. The value of the last expression is returned as the result of the function. All other expression are only evaluated for side effects. If no expression is given, the function returns nil", """
<br /><code>(fn [params*] expr*)</code><br /><br />
Defines a function. A function consists of a list of parameters and a list of expression. The value of the last expression is returned as the result of the function. All other expression are only evaluated for side effects. If no expression is given, the function returns nil.<br />
<br />
"""),
    DataFunction("for", "", PhelCompletionPriority.CONTROL_FLOW, "core", "", """
<br />
<br />
"""),
    DataFunction("foreach", "(foreach [key value valueExpr] expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "The foreach special form can be used to iterate over all kind of PHP datastructures. The return value of foreach is always nil. The loop special form should be preferred of the foreach special form whenever possible", """
<br /><code>(foreach [value valueExpr] expr*)<br />
(foreach [key value valueExpr] expr*)</code><br /><br />
The foreach special form can be used to iterate over all kind of PHP datastructures. The return value of foreach is always nil. The loop special form should be preferred of the foreach special form whenever possible.<br />
<br />
"""),
    DataFunction("format", "(format fmt & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a formatted string. See PHP's  for more information", """
<br /><code>(format fmt & xs)</code><br /><br />
Returns a formatted string. See PHP's <a href=\\"https://www.php.net/manual/en/function.sprintf.php\\">sprintf</a> for more information.<br />
<br />
"""),
    DataFunction("frequencies", "(frequencies coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a map from distinct items in `coll` to the number of times they appear", """
<br /><code>(frequencies coll)</code><br /><br />
Returns a map from distinct items in <b>coll</b> to the number of times they appear.<br />
<br />
"""),
    DataFunction("full-name", "(full-name x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Return the namespace and name string of a string, keyword or symbol", """
<br /><code>(full-name x)</code><br /><br />
Return the namespace and name string of a string, keyword or symbol.<br />
<br />
"""),
    DataFunction("function?", "(function? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a function, false otherwise", """
<br /><code>(function? x)</code><br /><br />
Returns true if <b>x</b> is a function, false otherwise.<br />
<br />
"""),
    DataFunction("gensym", "(gensym )", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Generates a new unique symbol", """
<br /><code>(gensym )</code><br /><br />
Generates a new unique symbol.<br />
<br />
"""),
    DataFunction("get", "(get ds k & [opt])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Gets the value at key in a collection. Returns default if not found", """
<br /><code>(get ds k & [opt])</code><br /><br />
Gets the value at key in a collection. Returns default if not found.<br />
<br />
  <pre><code>(get <code>{:a 1}</code> :a)<br /># => 1</code></pre>
<br />
"""),
    DataFunction("get-in", "(get-in ds ks & [opt])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Access a value in a nested data structure. Looks into the data structure via a sequence of keys", """
<br /><code>(get-in ds ks & [opt])</code><br /><br />
Access a value in a nested data structure. Looks into the data structure via a sequence of keys.<br />
<br />
"""),
    DataFunction("group-by", "(group-by f coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a map of the elements of coll keyed by the result of  f on each element", """
<br /><code>(group-by f coll)</code><br /><br />
Returns a map of the elements of coll keyed by the result of<br />
  f on each element.<br />
<br />
"""),
    DataFunction("hash-map", "(hash-map & xs) # {& xs}", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new hash map. If no argument is provided, an empty hash map is created. The number of parameters must be even", """
<br /><code>(hash-map & xs) # {& xs}</code><br /><br />
Creates a new hash map. If no argument is provided, an empty hash map is created. The number of parameters must be even.<br />
<br />
"""),
    DataFunction("hash-map?", "(hash-map? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a hash map, false otherwise", """
<br /><code>(hash-map? x)</code><br /><br />
Returns true if <b>x</b> is a hash map, false otherwise.<br />
<br />
"""),
    DataFunction("id", "(id a & more)", PhelCompletionPriority.MACROS, "core", "Checks if all values are identical. Same as `a === b` in PHP", """
<br /><code>(id a & more)</code><br /><br />
Checks if all values are identical. Same as <b>a === b</b> in PHP.<br />
<br />
"""),
    DataFunction("identity", "(identity x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns its argument", """
<br /><code>(identity x)</code><br /><br />
Returns its argument.<br />
<br />
"""),
    DataFunction("if", "(if test then else?)", PhelCompletionPriority.CONTROL_FLOW, "core", "A control flow structure. First evaluates test. If test evaluates to true, only the then form is evaluated and the result is returned. If test evaluates to false only the else form is evaluated and the result is returned. If no else form is given, nil will be returned", """
<br /><code>(if test then else?)</code><br /><br />
A control flow structure. First evaluates test. If test evaluates to true, only the then form is evaluated and the result is returned. If test evaluates to false only the else form is evaluated and the result is returned. If no else form is given, nil will be returned.<br />
<br />
The test evaluates to false if its value is false or equal to nil. Every other value evaluates to true. In sense of PHP this means (test != null && test !== false).<br />
<br />
"""),
    DataFunction("if-let", "(if-let bindings then & [else])", PhelCompletionPriority.CONTROL_FLOW, "core", "If test is true, evaluates then with binding-form bound to the value of test,  if not, yields else", """
<br /><code>(if-let bindings then & [else])</code><br /><br />
If test is true, evaluates then with binding-form bound to the value of test,<br />
  if not, yields else<br />
<br />
"""),
    DataFunction("if-not", "(if-not test then & [else])", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates then if test is false, else otherwise", """
<br /><code>(if-not test then & [else])</code><br /><br />
Evaluates then if test is false, else otherwise.<br />
<br />
  <pre><code>(if-not (< 5 3) \"not less\" \"less\")<br /># => \"not less\"</code></pre>
<br />
"""),
    DataFunction("inc", "(inc x)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Increments `x` by one", """
<br /><code>(inc x)</code><br /><br />
Increments <b>x</b> by one.<br />
<br />
"""),
    DataFunction("indexed?", "(indexed? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is indexed sequence, false otherwise", """
<br /><code>(indexed? x)</code><br /><br />
Returns true if <b>x</b> is indexed sequence, false otherwise.<br />
<br />
"""),
    DataFunction("int?", "(int? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is an integer number, false otherwise", """
<br /><code>(int? x)</code><br /><br />
Returns true if <b>x</b> is an integer number, false otherwise.<br />
<br />
"""),
    DataFunction("interleave", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Interleaves multiple collections. Returns a lazy sequence.  Returns elements by taking one from each collection in turn.  Pads with nil when collections have different lengths.  Works with infinite sequences", """
Interleaves multiple collections. Returns a lazy sequence.<br />
<br />
  Returns elements by taking one from each collection in turn.<br />
  Pads with nil when collections have different lengths.<br />
  Works with infinite sequences.<br />
<br />
  Example: (interleave [1 2 3] [:a :b :c]) ; => (1 :a 2 :b 3 :c)<br />
<br />
"""),
    DataFunction("interpose", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns elements separated by a separator. Returns a lazy sequence.  Inserts `sep` between each element of the collection.  Works with infinite sequences", """
Returns elements separated by a separator. Returns a lazy sequence.<br />
<br />
  Inserts <b>sep</b> between each element of the collection.<br />
  Works with infinite sequences.<br />
<br />
  Example: (interpose 0 [1 2 3]) ; => (1 0 2 0 3)<br />
<br />
"""),
    DataFunction("intersection", "(intersection set & sets)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Intersect multiple sets into a new one", """
<br /><code>(intersection set & sets)</code><br /><br />
Intersect multiple sets into a new one.<br />
<br />
"""),
    DataFunction("into", "(into to & rest)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns `to` with all elements of `from` added.   When `from` is associative, it is treated as a sequence of key-value pairs.   Supports persistent and transient collections", """
<br /><code>(into to & rest)</code><br /><br />
Returns <b>to</b> with all elements of <b>from</b> added.<br />
<br />
   When <b>from</b> is associative, it is treated as a sequence of key-value pairs.<br />
   Supports persistent and transient collections.<br />
<br />
"""),
    DataFunction("invert", "(invert map)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a new map where the keys and values are swapped. If map has  duplicated values, some keys will be ignored", """
<br /><code>(invert map)</code><br /><br />
Returns a new map where the keys and values are swapped. If map has<br />
  duplicated values, some keys will be ignored.<br />
<br />
"""),
    DataFunction("iterate", "(iterate f x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns an infinite lazy sequence of x, (f x), (f (f x)), and so on", """
<br /><code>(iterate f x)</code><br /><br />
Returns an infinite lazy sequence of x, (f x), (f (f x)), and so on.<br />
<br />
"""),
    DataFunction("juxt", "(juxt & fs)", PhelCompletionPriority.MACROS, "core", "Takes a list of functions and returns a new function that is the juxtaposition of those functions.  `((juxt a b c) x) => [(a x) (b x) (c x)]`", """
<br /><code>(juxt & fs)</code><br /><br />
Takes a list of functions and returns a new function that is the juxtaposition of those functions.<br />
  <b>((juxt a b c) x) => [(a x) (b x) (c x)]</b>.<br />
<br />
"""),
    DataFunction("keep", "(keep pred coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence of non-nil results of applying function to elements", """
<br /><code>(keep pred coll)</code><br /><br />
Returns a lazy sequence of non-nil results of applying function to elements.<br />
<br />
  <pre><code>(keep #(when (even? %) (* % %)) [1 2 3 4 5])<br /># => (4 16)</code></pre>
<br />
"""),
    DataFunction("keep-indexed", "(keep-indexed pred coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence of non-nil results of `(pred i x)`", """
<br /><code>(keep-indexed pred coll)</code><br /><br />
Returns a lazy sequence of non-nil results of <b>(pred i x)</b>.<br />
<br />
"""),
    DataFunction("keys", "(keys coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a sequence of all keys in a map", """
<br /><code>(keys coll)</code><br /><br />
Returns a sequence of all keys in a map.<br />
<br />
  <pre><code>(keys <code>{:a 1 :b 2}</code>)<br /># => (:a :b)</code></pre>
<br />
"""),
    DataFunction("keyword", "(keyword x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new Keyword from a given string", """
<br /><code>(keyword x)</code><br /><br />
Creates a new Keyword from a given string.<br />
<br />
"""),
    DataFunction("keyword?", "(keyword? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a keyword, false otherwise", """
<br /><code>(keyword? x)</code><br /><br />
Returns true if <b>x</b> is a keyword, false otherwise.<br />
<br />
"""),
    DataFunction("kvs", "(kvs coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of key-value pairs like `[k1 v1 k2 v2 k3 v3 ...]`", """
<br /><code>(kvs coll)</code><br /><br />
Returns a vector of key-value pairs like <b>[k1 v1 k2 v2 k3 v3 ...]</b>.<br />
<br />
"""),
    DataFunction("last", "(last coll)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns the last element of `coll` or nil if `coll` is empty or nil", """
<br /><code>(last coll)</code><br /><br />
Returns the last element of <b>coll</b> or nil if <b>coll</b> is empty or nil.<br />
<br />
"""),
    DataFunction("lazy-cat", "(lazy-cat & colls)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Concatenates collections into a lazy sequence (expands to concat)", """
<br /><code>(lazy-cat & colls)</code><br /><br />
Concatenates collections into a lazy sequence (expands to concat).<br />
<br />
  <pre><code>(lazy-cat [1 2] [3 4])<br /># => (1 2 3 4)</code></pre>
<br />
"""),
    DataFunction("lazy-seq", "(lazy-seq & body)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a lazy sequence that evaluates the body only when accessed", """
<br /><code>(lazy-seq & body)</code><br /><br />
Creates a lazy sequence that evaluates the body only when accessed.<br />
<br />
  <pre><code>(defn my-range [n]<br />  (when (> n 0)<br />    (lazy-seq (cons n (my-range (dec n))))))</code></pre>
<br />
"""),
    DataFunction("let", "(let [bindings*] expr*)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Creates a new lexical context with assignments defined in bindings. Afterwards the list of expressions is evaluated and the value of the last expression is returned. If no expression is given nil is returned", """
<br /><code>(let [bindings*] expr*)</code><br /><br />
Creates a new lexical context with assignments defined in bindings. Afterwards the list of expressions is evaluated and the value of the last expression is returned. If no expression is given nil is returned.<br />
<br />
"""),
    DataFunction("line-seq", "(line-seq filename)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence of lines from a file", """
<br /><code>(line-seq filename)</code><br /><br />
Returns a lazy sequence of lines from a file.<br />
<br />
  <pre><code>(take 10 (line-seq \"large-file.txt\"))<br /># => [\"line1\" \"line2\" ...]</code></pre>
<br />
"""),
    DataFunction("list", "(list & xs) # '(& xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new list. If no argument is provided, an empty list is created", """
<br /><code>(list & xs) # '(& xs)</code><br /><br />
Creates a new list. If no argument is provided, an empty list is created.<br />
<br />
"""),
    DataFunction("list?", "(list? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a list, false otherwise", """
<br /><code>(list? x)</code><br /><br />
Returns true if <b>x</b> is a list, false otherwise.<br />
<br />
"""),
    DataFunction("loop", "(loop [bindings*] expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Creates a new lexical context with variables defined in bindings and defines a recursion point at the top of the loop", """
<br /><code>(loop [bindings*] expr*)</code><br /><br />
Creates a new lexical context with variables defined in bindings and defines a recursion point at the top of the loop.<br />
<br />
"""),
    DataFunction("macroexpand", "(macroexpand form)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Recursively expands the given form until it is no longer a macro call", """
<br /><code>(macroexpand form)</code><br /><br />
Recursively expands the given form until it is no longer a macro call.<br />
<br />
"""),
    DataFunction("macroexpand-1", "(macroexpand-1 form)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Expands the given form once if it is a macro call", """
<br /><code>(macroexpand-1 form)</code><br /><br />
Expands the given form once if it is a macro call.<br />
<br />
"""),
    DataFunction("map", "(map f & colls)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns a lazy sequence of the result of applying `f` to all of the first items in each coll,   followed by applying `f` to all the second items in each coll until anyone of the colls is exhausted.  When given a single collection, applies the function to each element.  With multiple collections, applies the function to corresponding elements from each collection,  stopping when the shortest collection is exhausted.  Examples:    (map inc [1 2 3])    # => (2 3 4)    (map #(* % 2) [1 2 3 4])    # => (2 4 6 8)    (map + [1 2 3] [10 20 30])    # => (11 22 33)    (map str [\"a\" \"b\" \"c\"] [1 2 3])    # => (\"a1\" \"b2\" \"c3\")    (map list [1 2] [3 4] [5 6])    # => ((1 3 5) (2 4 6))  See also: filter, reduce, map-indexed, mapcat", """
<br /><code>(map f & colls)</code><br /><br />
Returns a lazy sequence of the result of applying <b>f</b> to all of the first items in each coll,<br />
   followed by applying <b>f</b> to all the second items in each coll until anyone of the colls is exhausted.<br />
<br />
  When given a single collection, applies the function to each element.<br />
  With multiple collections, applies the function to corresponding elements from each collection,<br />
  stopping when the shortest collection is exhausted.<br />
<br />
  Examples:<br />
    (map inc [1 2 3])<br />
    # => (2 3 4)<br />
<br />
    (map #(* % 2) [1 2 3 4])<br />
    # => (2 4 6 8)<br />
<br />
    (map + [1 2 3] [10 20 30])<br />
    # => (11 22 33)<br />
<br />
    (map str [\"a\" \"b\" \"c\"] [1 2 3])<br />
    # => (\"a1\" \"b2\" \"c3\")<br />
<br />
    (map list [1 2] [3 4] [5 6])<br />
    # => ((1 3 5) (2 4 6))<br />
<br />
  See also: filter, reduce, map-indexed, mapcat<br />
<br />
"""),
    DataFunction("map-indexed", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Maps a function over a collection with index. Returns a lazy sequence.  Applies `f` to each element in `xs`. `f` is a two-argument function where  the first argument is the index (0-based) and the second is the element itself.  Works with infinite sequences", """
Maps a function over a collection with index. Returns a lazy sequence.<br />
<br />
  Applies <b>f</b> to each element in <b>xs</b>. <b>f</b> is a two-argument function where<br />
  the first argument is the index (0-based) and the second is the element itself.<br />
  Works with infinite sequences.<br />
<br />
  Example: (map-indexed vector [:a :b :c]) ; => ([0 :a] [1 :b] [2 :c])<br />
<br />
"""),
    DataFunction("mapcat", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Maps a function over a collection and concatenates the results. Returns a lazy sequence.  Applies `f` to each element of the collection, where `f` should return a collection.  All resulting collections are concatenated into a single lazy sequence.  Works with infinite sequences", """
Maps a function over a collection and concatenates the results. Returns a lazy sequence.<br />
<br />
  Applies <b>f</b> to each element of the collection, where <b>f</b> should return a collection.<br />
  All resulting collections are concatenated into a single lazy sequence.<br />
  Works with infinite sequences.<br />
<br />
  Example: (mapcat reverse [[1 2] [3 4]]) ; => (2 1 4 3)<br />
<br />
"""),
    DataFunction("max", "(max & numbers)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the numeric maximum of all numbers", """
<br /><code>(max & numbers)</code><br /><br />
Returns the numeric maximum of all numbers.<br />
<br />
"""),
    DataFunction("mean", "(mean xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the mean of `xs`", """
<br /><code>(mean xs)</code><br /><br />
Returns the mean of <b>xs</b>.<br />
<br />
"""),
    DataFunction("median", "(median xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the median of `xs`", """
<br /><code>(median xs)</code><br /><br />
Returns the median of <b>xs</b>.<br />
<br />
"""),
    DataFunction("memoize", "(memoize f)", PhelCompletionPriority.MACROS, "core", "Returns a memoized version of the function `f`. The memoized function  caches the return value for each set of arguments", """
<br /><code>(memoize f)</code><br /><br />
Returns a memoized version of the function <b>f</b>. The memoized function<br />
  caches the return value for each set of arguments.<br />
<br />
"""),
    DataFunction("merge", "(merge & maps)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Merges multiple maps into one new map. If a key appears in more than one  collection, then later values replace any previous ones", """
<br /><code>(merge & maps)</code><br /><br />
Merges multiple maps into one new map. If a key appears in more than one<br />
  collection, then later values replace any previous ones.<br />
<br />
"""),
    DataFunction("merge-with", "(merge-with f & hash-maps)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Merges multiple maps into one new map. If a key appears in more than one   collection, the result of `(f current-val next-val)` is used", """
<br /><code>(merge-with f & hash-maps)</code><br /><br />
Merges multiple maps into one new map. If a key appears in more than one<br />
   collection, the result of <b>(f current-val next-val)</b> is used.<br />
<br />
"""),
    DataFunction("meta", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Gets the metadata of the given object or definition", """
Gets the metadata of the given object or definition.<br />
<br />
"""),
    DataFunction("min", "(min & numbers)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns the numeric minimum of all numbers", """
<br /><code>(min & numbers)</code><br /><br />
Returns the numeric minimum of all numbers.<br />
<br />
"""),
    DataFunction("name", "(name x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the name string of a string, keyword or symbol", """
<br /><code>(name x)</code><br /><br />
Returns the name string of a string, keyword or symbol.<br />
<br />
"""),
    DataFunction("namespace", "(namespace x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Return the namespace string of a symbol or keyword. Nil if not present", """
<br /><code>(namespace x)</code><br /><br />
Return the namespace string of a symbol or keyword. Nil if not present.<br />
<br />
"""),
    DataFunction("nan?", "(nan? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is not a number", """
<br /><code>(nan? x)</code><br /><br />
Checks if <b>x</b> is not a number.<br />
<br />
"""),
    DataFunction("neg?", "(neg? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is smaller than zero", """
<br /><code>(neg? x)</code><br /><br />
Checks if <b>x</b> is smaller than zero.<br />
<br />
"""),
    DataFunction("next", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the sequence after the first element, or nil if empty", """
Returns the sequence after the first element, or nil if empty.<br />
<br />
  <pre><code>(next [1 2 3])<br /># => [2 3]</code></pre>
<br />
"""),
    DataFunction("nfirst", "(nfirst coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as `(next (first coll))`", """
<br /><code>(nfirst coll)</code><br /><br />
Same as <b>(next (first coll))</b>.<br />
<br />
"""),
    DataFunction("nil?", "(nil? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if value is nil, false otherwise", """
<br /><code>(nil? x)</code><br /><br />
Returns true if value is nil, false otherwise.<br />
<br />
  <pre><code>(nil? (get <code>{:a 1}</code> :b))<br /># => true</code></pre>
<br />
"""),
    DataFunction("nnext", "(nnext coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as `(next (next coll))`", """
<br /><code>(nnext coll)</code><br /><br />
Same as <b>(next (next coll))</b>.<br />
<br />
"""),
    DataFunction("not", "(not x)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Returns true if value is falsy (nil or false), false otherwise", """
<br /><code>(not x)</code><br /><br />
Returns true if value is falsy (nil or false), false otherwise.<br />
<br />
  <pre><code>(not nil)<br /># => true</code></pre>
<br />
"""),
    DataFunction("not-any?", "(not-any? pred coll)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `(pred x)` is logical false for every `x` in `coll`   or if `coll` is empty. Otherwise returns false", """
<br /><code>(not-any? pred coll)</code><br /><br />
Returns true if <b>(pred x)</b> is logical false for every <b>x</b> in <b>coll</b><br />
   or if <b>coll</b> is empty. Otherwise returns false.<br />
<br />
"""),
    DataFunction("not-empty", "(not-empty coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns `coll` if it contains elements, otherwise nil", """
<br /><code>(not-empty coll)</code><br /><br />
Returns <b>coll</b> if it contains elements, otherwise nil.<br />
<br />
"""),
    DataFunction("not-every?", "(not-every? pred coll)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns false if `(pred x)` is logical true for every `x` in collection `coll`   or if `coll` is empty. Otherwise returns true", """
<br /><code>(not-every? pred coll)</code><br /><br />
Returns false if <b>(pred x)</b> is logical true for every <b>x</b> in collection <b>coll</b><br />
   or if <b>coll</b> is empty. Otherwise returns true.<br />
<br />
"""),
    DataFunction("not=", "(not= a & more)", PhelCompletionPriority.ARITHMETIC_FUNCTIONS, "core", "Checks if all values are unequal. Same as `a != b` in PHP", """
<br /><code>(not= a & more)</code><br /><br />
Checks if all values are unequal. Same as <b>a != b</b> in PHP.<br />
<br />
"""),
    DataFunction("ns", "(ns name imports*)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Defines the namespace for the current file and adds imports to the environment. Imports can either be uses or requires. The keyword :use is used to import PHP classes, the keyword :require is used to import Phel modules and the keyword :require-file is used to load php files", """
<br /><code>(ns name imports*)</code><br /><br />
Defines the namespace for the current file and adds imports to the environment. Imports can either be uses or requires. The keyword <b>:use</b> is used to import PHP classes, the keyword <b>:require</b> is used to import Phel modules and the keyword <b>:require-file</b> is used to load php files.<br />
<br />
"""),
    DataFunction("number?", "(number? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a number, false otherwise", """
<br /><code>(number? x)</code><br /><br />
Returns true if <b>x</b> is a number, false otherwise.<br />
<br />
"""),
    DataFunction("odd?", "(odd? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is odd", """
<br /><code>(odd? x)</code><br /><br />
Checks if <b>x</b> is odd.<br />
<br />
"""),
    DataFunction("one?", "(one? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is one", """
<br /><code>(one? x)</code><br /><br />
Checks if <b>x</b> is one.<br />
<br />
"""),
    DataFunction("or", "(or & args)", PhelCompletionPriority.MACROS, "core", "Evaluates expressions left to right, returning the first truthy value or the last value", """
<br /><code>(or & args)</code><br /><br />
Evaluates expressions left to right, returning the first truthy value or the last value.<br />
<br />
  <pre><code>(or false nil 42 100)<br /># => 42</code></pre>
<br />
"""),
    DataFunction("pairs", "(pairs coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Gets the pairs of an associative data structure", """
<br /><code>(pairs coll)</code><br /><br />
Gets the pairs of an associative data structure.<br />
<br />
"""),
    DataFunction("partial", "(partial f & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes a function `f` and fewer than normal arguments of `f` and returns a function  that a variable number of additional arguments. When call `f` will be called  with `args` and the additional arguments", """
<br /><code>(partial f & args)</code><br /><br />
Takes a function <b>f</b> and fewer than normal arguments of <b>f</b> and returns a function<br />
  that a variable number of additional arguments. When call <b>f</b> will be called<br />
  with <b>args</b> and the additional arguments.<br />
<br />
"""),
    DataFunction("partition", "(partition n coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Partitions collection into chunks of size n, dropping incomplete final partition", """
<br /><code>(partition n coll)</code><br /><br />
Partitions collection into chunks of size n, dropping incomplete final partition.<br />
<br />
  <pre><code>(partition 3 [1 2 3 4 5 6 7])<br /># => ([1 2 3] [4 5 6])</code></pre>
<br />
"""),
    DataFunction("partition-all", "(partition-all n coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Partitions collection into chunks of size n, including incomplete final partition", """
<br /><code>(partition-all n coll)</code><br /><br />
Partitions collection into chunks of size n, including incomplete final partition.<br />
<br />
  <pre><code>(partition-all 3 [1 2 3 4 5 6 7])<br /># => ([1 2 3] [4 5 6] [7])</code></pre>
<br />
"""),
    DataFunction("partition-by", "(partition-by f coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence of partitions. Applies `f` to each value in `coll`, splitting them each time the return value changes", """
<br /><code>(partition-by f coll)</code><br /><br />
Returns a lazy sequence of partitions. Applies <b>f</b> to each value in <b>coll</b>, splitting them each time the return value changes.<br />
<br />
"""),
    DataFunction("peek", "(peek coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the last element of a sequence", """
<br /><code>(peek coll)</code><br /><br />
Returns the last element of a sequence.<br />
<br />
"""),
    DataFunction("persistent", "(persistent coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Converts a transient collection to a persistent collection", """
<br /><code>(persistent coll)</code><br /><br />
Converts a transient collection to a persistent collection.<br />
<br />
"""),
    DataFunction("phel->php", "(phel->php x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Recursively converts a Phel data structure to a PHP array", """
<br /><code>(phel->php x)</code><br /><br />
Recursively converts a Phel data structure to a PHP array.<br />
<br />
"""),
    DataFunction("php->phel", "(php->phel x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Recursively converts a PHP array to Phel data structures", """
<br /><code>(php->phel x)</code><br /><br />
Recursively converts a PHP array to Phel data structures.<br />
<br />
"""),
    DataFunction("php-array-to-map", "(php-array-to-map arr)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Converts a PHP Array to a map", """
<br /><code>(php-array-to-map arr)</code><br /><br />
Converts a PHP Array to a map.<br />
<br />
"""),
    DataFunction("php-array?", "(php-array? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a PHP Array, false otherwise", """
<br /><code>(php-array? x)</code><br /><br />
Returns true if <b>x</b> is a PHP Array, false otherwise.<br />
<br />
"""),
    DataFunction("php-associative-array", "(php-associative-array & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a PHP associative array. An even number of parameters must be provided", """
<br /><code>(php-associative-array & xs)</code><br /><br />
Creates a PHP associative array. An even number of parameters must be provided.<br />
<br />
"""),
    DataFunction("php-indexed-array", "(php-indexed-array & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a PHP indexed array from the given values", """
<br /><code>(php-indexed-array & xs)</code><br /><br />
Creates a PHP indexed array from the given values.<br />
<br />
"""),
    DataFunction("php-object?", "(php-object? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a PHP object, false otherwise", """
<br /><code>(php-object? x)</code><br /><br />
Returns true if <b>x</b> is a PHP object, false otherwise.<br />
<br />
"""),
    DataFunction("php-resource?", "(php-resource? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a PHP resource, false otherwise", """
<br /><code>(php-resource? x)</code><br /><br />
Returns true if <b>x</b> is a PHP resource, false otherwise.<br />
<br />
"""),
    DataFunction("pop", "(pop coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Removes the last element of the array `coll`. If the array is empty returns nil", """
<br /><code>(pop coll)</code><br /><br />
Removes the last element of the array <b>coll</b>. If the array is empty returns nil.<br />
<br />
"""),
    DataFunction("pos?", "(pos? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is greater than zero", """
<br /><code>(pos? x)</code><br /><br />
Checks if <b>x</b> is greater than zero.<br />
<br />
"""),
    DataFunction("print", "(print & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Prints the given values to the default output stream. Returns nil", """
<br /><code>(print & xs)</code><br /><br />
Prints the given values to the default output stream. Returns nil.<br />
<br />
"""),
    DataFunction("print-str", "(print-str & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as print. But instead of writing it to an output stream, the resulting string is returned", """
<br /><code>(print-str & xs)</code><br /><br />
Same as print. But instead of writing it to an output stream, the resulting string is returned.<br />
<br />
"""),
    DataFunction("printf", "(printf fmt & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Output a formatted string. See PHP's  for more information", """
<br /><code>(printf fmt & xs)</code><br /><br />
Output a formatted string. See PHP's <a href=\\"https://www.php.net/manual/en/function.printf.php\\">printf</a> for more information.<br />
<br />
"""),
    DataFunction("println", "(println & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Same as print followed by a newline", """
<br /><code>(println & xs)</code><br /><br />
Same as print followed by a newline.<br />
<br />
"""),
    DataFunction("push", "(push coll x)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Inserts `x` at the end of the sequence `coll`.  Deprecated: Use `conj` instead for Clojure compatibility", """
<br /><code>(push coll x)</code><br /><br />
Inserts <b>x</b> at the end of the sequence <b>coll</b>.<br />
<br />
  Deprecated: Use <b>conj</b> instead for Clojure compatibility.<br />
<br />
"""),
    DataFunction("put", "(put ds key value)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Puts `value` mapped to `key` on the datastructure `ds`. Returns `ds`.  Deprecated: Use `assoc` instead", """
<br /><code>(put ds key value)</code><br /><br />
Puts <b>value</b> mapped to <b>key</b> on the datastructure <b>ds</b>. Returns <b>ds</b>.<br />
<br />
  Deprecated: Use <b>assoc</b> instead.<br />
<br />
"""),
    DataFunction("put-in", "(put-in ds ks v)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Puts a value into a nested data structure.  Deprecated: Use `assoc-in` instead", """
<br /><code>(put-in ds ks v)</code><br /><br />
Puts a value into a nested data structure.<br />
<br />
  Deprecated: Use <b>assoc-in</b> instead.<br />
<br />
"""),
    DataFunction("quote", "(NAME_QUOTE)", PhelCompletionPriority.SPECIAL_FORMS, "core", "NAME_QUOTE description", """
<br /><code>(NAME_QUOTE)</code><br /><br />
<br />
"""),
    DataFunction("rand", "(rand )", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a random number between 0 and 1", """
<br /><code>(rand )</code><br /><br />
Returns a random number between 0 and 1.<br />
<br />
"""),
    DataFunction("rand-int", "(rand-int n)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a random number between 0 and `n`", """
<br /><code>(rand-int n)</code><br /><br />
Returns a random number between 0 and <b>n</b>.<br />
<br />
"""),
    DataFunction("rand-nth", "(rand-nth xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a random item from xs", """
<br /><code>(rand-nth xs)</code><br /><br />
Returns a random item from xs.<br />
<br />
"""),
    DataFunction("range", "(range a & rest)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a lazy sequence of numbers from start to end (exclusive)", """
<br /><code>(range a & rest)</code><br /><br />
Creates a lazy sequence of numbers from start to end (exclusive).<br />
<br />
  <pre><code>(range 5)<br /># => (0 1 2 3 4)</code></pre>
<br />
"""),
    DataFunction("re-seq", "(re-seq re s)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a sequence of successive matches of pattern in string", """
<br /><code>(re-seq re s)</code><br /><br />
Returns a sequence of successive matches of pattern in string.<br />
<br />
"""),
    DataFunction("read-file-lazy", "(read-file-lazy filename)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a lazy sequence of byte chunks from a file", """
<br /><code>(read-file-lazy filename)</code><br /><br />
Returns a lazy sequence of byte chunks from a file.<br />
<br />
  <pre><code>(take 5 (read-file-lazy \"large-file.bin\" 1024))<br /># => [\"chunk1\" \"chunk2\" ...]</code></pre>
<br />
"""),
    DataFunction("read-string", "(read-string s)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Reads the first phel expression from the string s", """
<br /><code>(read-string s)</code><br /><br />
Reads the first phel expression from the string s.<br />
<br />
"""),
    DataFunction("realized?", "(realized? coll)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if a lazy sequence has been realized, false otherwise", """
<br /><code>(realized? coll)</code><br /><br />
Returns true if a lazy sequence has been realized, false otherwise.<br />
<br />
  <pre><code>(realized? (take 5 (iterate inc 1)))<br /># => false</code></pre>
<br />
"""),
    DataFunction("recur", "(recur expr*)", PhelCompletionPriority.CONTROL_FLOW, "core", "Internally recur is implemented as a PHP while loop and therefore prevents the Maximum function nesting level errors", """
<br /><code>(recur expr*)</code><br /><br />
Internally recur is implemented as a PHP while loop and therefore prevents the Maximum function nesting level errors.<br />
<br />
"""),
    DataFunction("reduce", "(reduce f & args)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Reduces collection to a single value by repeatedly applying function to accumulator and elements", """
<br /><code>(reduce f & args)</code><br /><br />
Reduces collection to a single value by repeatedly applying function to accumulator and elements.<br />
<br />
  <pre><code>(reduce + [1 2 3 4])<br /># => 10</code></pre>
<br />
"""),
    DataFunction("remove", "(remove coll offset & [n])", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Removes up to `n` element from array `coll` starting at index `offset`", """
<br /><code>(remove coll offset & [n])</code><br /><br />
Removes up to <b>n</b> element from array <b>coll</b> starting at index <b>offset</b>.<br />
<br />
"""),
    DataFunction("repeat", "(repeat a & rest)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of length n where every element is x.  With one argument returns an infinite lazy sequence of x", """
<br /><code>(repeat a & rest)</code><br /><br />
Returns a vector of length n where every element is x.<br />
  With one argument returns an infinite lazy sequence of x.<br />
<br />
"""),
    DataFunction("repeatedly", "(repeatedly a & rest)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of length n with values produced by repeatedly calling f.  With one argument returns an infinite lazy sequence of calls to f", """
<br /><code>(repeatedly a & rest)</code><br /><br />
Returns a vector of length n with values produced by repeatedly calling f.<br />
  With one argument returns an infinite lazy sequence of calls to f.<br />
<br />
"""),
    DataFunction("rest", "(rest coll)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns the sequence after the first element, or empty sequence if none", """
<br /><code>(rest coll)</code><br /><br />
Returns the sequence after the first element, or empty sequence if none.<br />
<br />
  <pre><code>(rest [1 2 3])<br /># => [2 3]</code></pre>
<br />
"""),
    DataFunction("reverse", "(reverse coll)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Reverses the order of the elements in the given sequence", """
<br /><code>(reverse coll)</code><br /><br />
Reverses the order of the elements in the given sequence.<br />
<br />
"""),
    DataFunction("second", "(second coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the second element of a sequence, or nil if not present", """
<br /><code>(second coll)</code><br /><br />
Returns the second element of a sequence, or nil if not present.<br />
<br />
  <pre><code>(second [1 2 3])<br /># => 2</code></pre>
<br />
"""),
    DataFunction("select-keys", "(select-keys m ks)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a new map including key value pairs from `m` selected with of keys `ks`", """
<br /><code>(select-keys m ks)</code><br /><br />
Returns a new map including key value pairs from <b>m</b> selected with of keys <b>ks</b>.<br />
<br />
"""),
    DataFunction("set", "(set & xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new Set from the given arguments. Shortcut: #{}", """
<br /><code>(set & xs)</code><br /><br />
Creates a new Set from the given arguments. Shortcut: #{}<br />
<br />
  <pre><code>(set 1 2 3)<br /># => #{1 2 3}</code></pre>
<br />
"""),
    DataFunction("set!", "(set! variable value)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Sets a new value to the given variable", """
<br /><code>(set! variable value)</code><br /><br />
Sets a new value to the given variable.<br />
<br />
"""),
    DataFunction("set-meta!", "", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Sets the metadata to a given object", """
Sets the metadata to a given object.<br />
<br />
"""),
    DataFunction("set-var", "(var value)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Variables provide a way to manage mutable state. Each variable contains a single value. To create a variable use the var function", """
<br /><code>(var value)</code><br /><br />
Variables provide a way to manage mutable state. Each variable contains a single value. To create a variable use the var function.<br />
<br />
"""),
    DataFunction("set?", "(set? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a set, false otherwise", """
<br /><code>(set? x)</code><br /><br />
Returns true if <b>x</b> is a set, false otherwise.<br />
<br />
"""),
    DataFunction("shuffle", "(shuffle coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a random permutation of coll", """
<br /><code>(shuffle coll)</code><br /><br />
Returns a random permutation of coll.<br />
<br />
"""),
    DataFunction("slice", "(slice coll & [offset & [length]])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Extract a slice of `coll`", """
<br /><code>(slice coll & [offset & [length]])</code><br /><br />
Extract a slice of <b>coll</b>.<br />
<br />
"""),
    DataFunction("slurp", "(slurp path & [opts])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Reads entire file or URL into a string", """
<br /><code>(slurp path & [opts])</code><br /><br />
Reads entire file or URL into a string.<br />
<br />
  <pre><code>(slurp \"file.txt\")<br /># => \"file contents\"</code></pre>
<br />
"""),
    DataFunction("some", "(some pred coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the first truthy value of applying predicate to elements, or nil if none found", """
<br /><code>(some pred coll)</code><br /><br />
Returns the first truthy value of applying predicate to elements, or nil if none found.<br />
<br />
  <pre><code>(some #(when (> % 10) %) [5 15 8])<br /># => 15</code></pre>
<br />
"""),
    DataFunction("some->", "(some-> x & forms)", PhelCompletionPriority.MACROS, "core", "Threads `x` through the forms like `->` but stops when a form returns `nil`", """
<br /><code>(some-> x & forms)</code><br /><br />
Threads <b>x</b> through the forms like <b>-></b> but stops when a form returns <b>nil</b>.<br />
<br />
"""),
    DataFunction("some->>", "(some->> x & forms)", PhelCompletionPriority.MACROS, "core", "Threads `x` through the forms like `->>` but stops when a form returns `nil`", """
<br /><code>(some->> x & forms)</code><br /><br />
Threads <b>x</b> through the forms like <b>->></b> but stops when a form returns <b>nil</b>.<br />
<br />
"""),
    DataFunction("some?", "(some? pred coll)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if predicate is true for at least one element in collection, false otherwise", """
<br /><code>(some? pred coll)</code><br /><br />
Returns true if predicate is true for at least one element in collection, false otherwise.<br />
<br />
  <pre><code>(some? even? [1 3 5 6 7])<br /># => true</code></pre>
<br />
"""),
    DataFunction("sort", "(sort coll & [comp])", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns a sorted vector. If no comparator is supplied compare is used", """
<br /><code>(sort coll & [comp])</code><br /><br />
Returns a sorted vector. If no comparator is supplied compare is used.<br />
<br />
"""),
    DataFunction("sort-by", "(sort-by keyfn coll & [comp])", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Returns a sorted vector where the sort order is determined by comparing  `(keyfn item)`. If no comparator is supplied compare is used", """
<br /><code>(sort-by keyfn coll & [comp])</code><br /><br />
Returns a sorted vector where the sort order is determined by comparing<br />
  <b>(keyfn item)</b>. If no comparator is supplied compare is used.<br />
<br />
"""),
    DataFunction("spit", "(spit filename data & [opts])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Writes data to file, returning number of bytest that were written or `nil`  on failure. Accepts `opts` map for overriding default PHP file_put_contents  arguments, as example to append to file use {:flags php/FILE_APPEND}.  See PHP's  for more information", """
<br /><code>(spit filename data & [opts])</code><br /><br />
Writes data to file, returning number of bytest that were written or <b>nil</b><br />
  on failure. Accepts <b>opts</b> map for overriding default PHP file_put_contents<br />
  arguments, as example to append to file use <code>{:flags php/FILE_APPEND}</code>.<br />
  See PHP's <a href=\\"https://www.php.net/manual/en/function.file-put-contents.php\\">file_put_contents</a> for more information.<br />
<br />
"""),
    DataFunction("split-at", "(split-at n coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of `[(take n coll) (drop n coll)]`", """
<br /><code>(split-at n coll)</code><br /><br />
Returns a vector of <b>[(take n coll) (drop n coll)]</b>.<br />
<br />
"""),
    DataFunction("split-with", "(split-with f coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of `[(take-while pred coll) (drop-while pred coll)]`", """
<br /><code>(split-with f coll)</code><br /><br />
Returns a vector of <b>[(take-while pred coll) (drop-while pred coll)]</b>.<br />
<br />
"""),
    DataFunction("str", "(str & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a string by concatenating values together. If no arguments areprovided an empty string is returned. Nil and false are represented as an emptystring. True is represented as 1. Otherwise, it tries to call `__toString`.This is PHP equivalent to `${'$'}args[0] . ${'$'}args[1] . ${'$'}args[2] ...`", """
<br /><code>(str & args)</code><br /><br />
Creates a string by concatenating values together. If no arguments are<br />
provided an empty string is returned. Nil and false are represented as an empty<br />
string. True is represented as 1. Otherwise, it tries to call <b>__toString</b>.<br />
This is PHP equivalent to <b>${'$'}args[0] . ${'$'}args[1] . ${'$'}args[2] ...</b>.<br />
<br />
"""),
    DataFunction("str-contains?", "(str-contains? str s)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if str contains s", """
<br /><code>(str-contains? str s)</code><br /><br />
Returns true if str contains s.<br />
<br />
"""),
    DataFunction("string?", "(string? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a string, false otherwise", """
<br /><code>(string? x)</code><br /><br />
Returns true if <b>x</b> is a string, false otherwise.<br />
<br />
"""),
    DataFunction("struct?", "(struct? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a struct, false otherwise", """
<br /><code>(struct? x)</code><br /><br />
Returns true if <b>x</b> is a struct, false otherwise.<br />
<br />
"""),
    DataFunction("sum", "(sum xs)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns the sum of all elements is `xs`", """
<br /><code>(sum xs)</code><br /><br />
Returns the sum of all elements is <b>xs</b>.<br />
<br />
"""),
    DataFunction("swap!", "(swap! variable f & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Swaps the value of the variable to `(apply f current-value args)`. Returns the values that are swapped in", """
<br /><code>(swap! variable f & args)</code><br /><br />
Swaps the value of the variable to <b>(apply f current-value args)</b>. Returns the values that are swapped in.<br />
<br />
"""),
    DataFunction("symbol", "(symbol name-or-ns & [name])", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a new symbol for given string with optional namespace.   Arity-1 returns a symbol without namespace. Arity-2 returns a symbol in given namespace", """
<br /><code>(symbol name-or-ns & [name])</code><br /><br />
Returns a new symbol for given string with optional namespace.<br />
   Arity-1 returns a symbol without namespace. Arity-2 returns a symbol in given namespace.<br />
<br />
"""),
    DataFunction("symbol?", "(symbol? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a symbol, false otherwise", """
<br /><code>(symbol? x)</code><br /><br />
Returns true if <b>x</b> is a symbol, false otherwise.<br />
<br />
"""),
    DataFunction("symmetric-difference", "(symmetric-difference set & sets)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Symmetric difference between multiple sets into a new one", """
<br /><code>(symmetric-difference set & sets)</code><br /><br />
Symmetric difference between multiple sets into a new one.<br />
<br />
"""),
    DataFunction("take", "(take n coll)", PhelCompletionPriority.COLLECTION_FUNCTIONS, "core", "Takes the first `n` elements of `coll`.  Note: Metadata preservation works with inline calls but may be lost when binding  to variables. Use inline calls or force realization with doall if metadata needed.  See local/investigate-metadata-binding-issue.md for details", """
<br /><code>(take n coll)</code><br /><br />
Takes the first <b>n</b> elements of <b>coll</b>.<br />
<br />
  Note: Metadata preservation works with inline calls but may be lost when binding<br />
  to variables. Use inline calls or force realization with doall if metadata needed.<br />
  See local/investigate-metadata-binding-issue.md for details.<br />
<br />
"""),
    DataFunction("take-last", "(take-last n coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes the last `n` elements of `coll`", """
<br /><code>(take-last n coll)</code><br /><br />
Takes the last <b>n</b> elements of <b>coll</b>.<br />
<br />
"""),
    DataFunction("take-nth", "(take-nth n coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns every nth item in `coll`. Returns a lazy sequence", """
<br /><code>(take-nth n coll)</code><br /><br />
Returns every nth item in <b>coll</b>. Returns a lazy sequence.<br />
<br />
"""),
    DataFunction("take-while", "(take-while pred coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Takes all elements at the front of `coll` where `(pred x)` is true. Returns a lazy sequence", """
<br /><code>(take-while pred coll)</code><br /><br />
Takes all elements at the front of <b>coll</b> where <b>(pred x)</b> is true. Returns a lazy sequence.<br />
<br />
"""),
    DataFunction("throw", "(throw exception)", PhelCompletionPriority.CONTROL_FLOW, "core", "Throw an exception", """
<br /><code>(throw exception)</code><br /><br />
Throw an exception.<br />
See <a href=\\"/documentation/control-flow/#try-catch-and-finally\\">try-catch</a>.<br />
<br />
"""),
    DataFunction("time", "(time expr)", PhelCompletionPriority.MACROS, "core", "Evaluates expr and prints the time it took. Returns the value of expr", """
<br /><code>(time expr)</code><br /><br />
Evaluates expr and prints the time it took. Returns the value of expr.<br />
<br />
"""),
    DataFunction("to-php-array", "(to-php-array coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Create a PHP Array from a sequential data structure", """
<br /><code>(to-php-array coll)</code><br /><br />
Create a PHP Array from a sequential data structure.<br />
<br />
"""),
    DataFunction("transient", "(transient coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Converts a persistent collection to a transient collection", """
<br /><code>(transient coll)</code><br /><br />
Converts a persistent collection to a transient collection.<br />
<br />
"""),
    DataFunction("tree-seq", "(tree-seq branch? children root)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a vector of the nodes in the tree, via a depth-first walk.  branch? is a function with one argument that returns true if the given node  has children.  children must be a function with one argument that returns the children of the node.  root the root node of the tree", """
<br /><code>(tree-seq branch? children root)</code><br /><br />
Returns a vector of the nodes in the tree, via a depth-first walk.<br />
  branch? is a function with one argument that returns true if the given node<br />
  has children.<br />
  children must be a function with one argument that returns the children of the node.<br />
  root the root node of the tree.<br />
<br />
"""),
    DataFunction("true?", "(true? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if value is exactly true (not just truthy)", """
<br /><code>(true? x)</code><br /><br />
Checks if value is exactly true (not just truthy).<br />
<br />
  <pre><code>(true? 1)<br /># => false</code></pre>
<br />
"""),
    DataFunction("truthy?", "(truthy? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is truthy. Same as `x == true` in PHP", """
<br /><code>(truthy? x)</code><br /><br />
Checks if <b>x</b> is truthy. Same as <b>x == true</b> in PHP.<br />
<br />
"""),
    DataFunction("try", "(try expr* catch-clause* finally-clause?)", PhelCompletionPriority.CONTROL_FLOW, "core", "All expressions are evaluated and if no exception is thrown the value of the last expression is returned. If an exception occurs and a matching catch-clause is provided, its expression is evaluated and the value is returned. If no matching catch-clause can be found the exception is propagated out of the function. Before returning normally or abnormally the optionally finally-clause is evaluated", """
<br /><code>(try expr* catch-clause* finally-clause?)</code><br /><br />
All expressions are evaluated and if no exception is thrown the value of the last expression is returned. If an exception occurs and a matching catch-clause is provided, its expression is evaluated and the value is returned. If no matching catch-clause can be found the exception is propagated out of the function. Before returning normally or abnormally the optionally finally-clause is evaluated.<br />
<br />
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
* <b>:unknown</b><br />
<br />
"""),
    DataFunction("union", "(union & sets)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Union multiple sets into a new one", """
<br /><code>(union & sets)</code><br /><br />
Union multiple sets into a new one.<br />
<br />
"""),
    DataFunction("unquote", "(unquote my-sym)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Values that should be evaluated in a macro are marked with the unquote function (shorthand ,)", """
<br /><code>(unquote my-sym) # Evaluates to my-sym<br />
,my-sym          # Shorthand for (same as above)</code><br /><br />
Values that should be evaluated in a macro are marked with the unquote function (shorthand <b>,</b>).<br />
<br />
"""),
    DataFunction("unquote-splicing", "(unquote-splicing my-sym)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Values that should be evaluated in a macro are marked with the unquote function (shorthand ,@)", """
<br /><code>(unquote-splicing my-sym) # Evaluates to my-sym<br />
,@my-sym                  # Shorthand for (same as above)</code><br /><br />
Values that should be evaluated in a macro are marked with the unquote function (shorthand <b>,@</b>).<br />
<br />
"""),
    DataFunction("unset", "(unset ds key)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns `ds` without `key`.  Deprecated: Use `dissoc` instead", """
<br /><code>(unset ds key)</code><br /><br />
Returns <b>ds</b> without <b>key</b>.<br />
<br />
  Deprecated: Use <b>dissoc</b> instead.<br />
<br />
"""),
    DataFunction("unset-in", "(unset-in ds ks)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Removes a value from a nested data structure.  Deprecated: Use `dissoc-in` instead", """
<br /><code>(unset-in ds ks)</code><br /><br />
Removes a value from a nested data structure.<br />
<br />
  Deprecated: Use <b>dissoc-in</b> instead.<br />
<br />
"""),
    DataFunction("update", "(update ds k f & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Updates a value in a datastructure by applying `f` to the current element and replacing it with the result of `f`", """
<br /><code>(update ds k f & args)</code><br /><br />
Updates a value in a datastructure by applying <b>f</b> to the current element and replacing it with the result of <b>f</b>.<br />
<br />
"""),
    DataFunction("update-in", "(update-in ds [k & ks] f & args)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Updates a value into a nested data structure", """
<br /><code>(update-in ds [k & ks] f & args)</code><br /><br />
Updates a value into a nested data structure.<br />
<br />
"""),
    DataFunction("values", "(values coll)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a sequence of all values in a map", """
<br /><code>(values coll)</code><br /><br />
Returns a sequence of all values in a map.<br />
<br />
  <pre><code>(values <code>{:a 1 :b 2}</code>)<br /># => (1 2)</code></pre>
<br />
"""),
    DataFunction("var", "(var value)", PhelCompletionPriority.SPECIAL_FORMS, "core", "Creates a new variable with the given value", """
<br /><code>(var value)</code><br /><br />
Creates a new variable with the given value.<br />
<br />
"""),
    DataFunction("var?", "(var? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if the given value is a variable", """
<br /><code>(var? x)</code><br /><br />
Checks if the given value is a variable.<br />
<br />
"""),
    DataFunction("vector", "(vector & xs) # [& xs]", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a new vector. If no argument is provided, an empty vector is created", """
<br /><code>(vector & xs) # [& xs]</code><br /><br />
Creates a new vector. If no argument is provided, an empty vector is created.<br />
<br />
"""),
    DataFunction("vector?", "(vector? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Returns true if `x` is a vector, false otherwise", """
<br /><code>(vector? x)</code><br /><br />
Returns true if <b>x</b> is a vector, false otherwise.<br />
<br />
"""),
    DataFunction("when", "(when test & body)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates body if test is true, otherwise returns nil", """
<br /><code>(when test & body)</code><br /><br />
Evaluates body if test is true, otherwise returns nil.<br />
<br />
  <pre><code>(when (> 10 5) \"greater\")<br /># => \"greater\"</code></pre>
<br />
"""),
    DataFunction("when-let", "(when-let bindings & body)", PhelCompletionPriority.CONTROL_FLOW, "core", "When test is true, evaluates body with binding-form bound to the value of test", """
<br /><code>(when-let bindings & body)</code><br /><br />
When test is true, evaluates body with binding-form bound to the value of test<br />
<br />
"""),
    DataFunction("when-not", "(when-not test & body)", PhelCompletionPriority.CONTROL_FLOW, "core", "Evaluates body if test is false, otherwise returns nil", """
<br /><code>(when-not test & body)</code><br /><br />
Evaluates body if test is false, otherwise returns nil.<br />
<br />
  <pre><code>(when-not (empty? [1 2 3]) \"has items\")<br /># => \"has items\"</code></pre>
<br />
"""),
    DataFunction("with-output-buffer", "(with-output-buffer & body)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Everything that is printed inside the body will be stored in a buffer.   The result of the buffer is returned", """
<br /><code>(with-output-buffer & body)</code><br /><br />
Everything that is printed inside the body will be stored in a buffer.<br />
   The result of the buffer is returned.<br />
<br />
"""),
    DataFunction("zero?", "(zero? x)", PhelCompletionPriority.PREDICATE_FUNCTIONS, "core", "Checks if `x` is zero", """
<br /><code>(zero? x)</code><br /><br />
Checks if <b>x</b> is zero.<br />
<br />
"""),
    DataFunction("zipcoll", "(zipcoll a b)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Creates a map from two sequential data structures. Return a new map", """
<br /><code>(zipcoll a b)</code><br /><br />
Creates a map from two sequential data structures. Return a new map.<br />
<br />
"""),
    DataFunction("zipmap", "(zipmap keys vals)", PhelCompletionPriority.CORE_FUNCTIONS, "core", "Returns a new map with the keys mapped to the corresponding values. Stops when the shorter of `keys` or `vals` is exhausted", """
<br /><code>(zipmap keys vals)</code><br /><br />
Returns a new map with the keys mapped to the corresponding values. Stops when the shorter of <b>keys</b> or <b>vals</b> is exhausted.<br />
<br />
"""),
)
