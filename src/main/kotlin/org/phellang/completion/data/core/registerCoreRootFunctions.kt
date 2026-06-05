package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreRootFunctions(): List<PhelFunction> = listOf(
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core.phel#L21",
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
            example = "(println *ns*) ; =&gt; \"my-app.core\"",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core.phel#L199",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core.phel#L204",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core.phel#L59",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core.phel#L187",
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
        name = "defonce",
        signature = "(defonce name meta? value)",
        completion = CompletionInfo(
            tailText = "Like def, but only binds the value when name is not already defined",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>def</code>, but only binds the value when <code>name</code> is not already defined.
""",
            example = "(defonce app-state (atom {}))",
            links = DocumentationLinks(
                github = "",
                docs = "/documentation/global-and-local-bindings/#definition-def",
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
        name = "first",
        signature = "",
        completion = CompletionInfo(
            tailText = "Returns the first element of a sequence, or nil if empty",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the first element of a sequence, or nil if empty.<br /><br />
Maps are treated as a sequence of entries: <code>(first {:a 1})</code> returns a<br />
  typed <code>MapEntry</code> equal by value to <code>[:a 1]</code>. Strings are treated as<br />
  sequences of multibyte characters.
""",
            example = "(first [1 2 3]) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core.phel#L146",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core.phel#L94",
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
        name = "queue",
        signature = "",
        completion = CompletionInfo(
            tailText = "Creates a persistent FIFO queue",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a persistent FIFO queue. With no arguments returns an empty<br />
  queue; with arguments returns a queue with the values pushed in order so<br />
  that the first argument is at the front.
""",
            example = "(queue 1 2 3) ; =&gt; first 1, then 2, then 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core.phel#L45",
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
    )
)
