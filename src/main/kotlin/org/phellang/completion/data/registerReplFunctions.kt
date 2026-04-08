package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerReplFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "repl",
        name = "repl/apropos",
        signature = "(apropos search)",
        completion = CompletionInfo(
            tailText = "Returns a vector of symbols whose name contains the given search string",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a vector of symbols whose name contains the given search string.<br />
  Searches across all loaded namespaces.
""",
            example = "(apropos \"map\") ; =&gt; [phel\\core/flat-map phel\\core/map ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L257",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/build-facade",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L15",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/compile-str",
        signature = "(compile-str s)",
        completion = CompletionInfo(
            tailText = "Compiles a Phel expression string to PHP code",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Compiles a Phel expression string to PHP code.",
            example = "(compile-str \"(+ 1 2)\") ; =&gt; \"(1 + 2)\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L161",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/dir",
        signature = "(dir ns-str)",
        completion = CompletionInfo(
            tailText = "Prints all public definitions in the given namespace string",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Prints all public definitions in the given namespace string.<br /><br />
Returns nil. Prints one name per line, sorted alphabetically.
""",
            example = "(dir \"phel\\core\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L240",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/doc",
        signature = "(doc sym)",
        completion = CompletionInfo(
            tailText = "Prints the documentation for the given symbol",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Prints the documentation for the given symbol.",
            example = "(doc map)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L79",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/eval-capturing",
        signature = "(eval-capturing code-str)",
        completion = CompletionInfo(
            tailText = "Evaluates a string of Phel code while capturing stdout",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates a string of Phel code while capturing stdout. Returns a hash-map<br />
  with :value (the result), :out (captured stdout), :success (boolean), and<br />
  :error (error message or nil).<br /><br />
Useful for nREPL and other tooling that needs to separate printed output from<br />
  return values.
""",
            example = "(eval-capturing \"(do (println \\\"hello\\\") 42)\") ; =&gt; {:value 42 :out \"hello\\n\" :success true :error nil}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L399",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/eval-str",
        signature = "(eval-str s)",
        completion = CompletionInfo(
            tailText = "Evaluates a string of Phel code and returns the result",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates a string of Phel code and returns the result.<br />
  If the string contains multiple expressions, returns the result of the last one.
""",
            example = "(eval-str \"(+ 1 2)\") ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L170",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/find-fn",
        signature = "(find-fn search)",
        completion = CompletionInfo(
            tailText = "Searches for functions whose name or docstring contains the search string",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Searches for functions whose name or docstring contains the search string.<br />
  Returns a vector of maps with :ns, :name, :doc, :private, :min-arity, :max-arity, :is-variadic.
""",
            example = "(find-fn \"map\") ; =&gt; [{:ns \"phel\\core\" :name \"map\" ...} ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L275",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/get-source-code",
        signature = "(get-source-code ns-str name-str)",
        completion = CompletionInfo(
            tailText = "Returns the source code of the definition identified by namespace and name strings, or nil if the...",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the source code of the definition identified by namespace and name<br />
  strings, or nil if the source file cannot be found or metadata is missing.
""",
            example = "(get-source-code \"phel\\core\" \"map\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L307",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/get-symbol-info",
        signature = "(get-symbol-info ns-str name-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of structured metadata for the definition identified by namespace and name str...",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of structured metadata for the definition identified by<br />
  namespace and name strings. Returns nil if no such definition exists.<br /><br />
Keys: :doc, :file, :line, :column, :end-line, :end-column, :private,<br />
        :deprecated, :min-arity, :max-arity, :is-variadic, :ns, :name
""",
            example = "(get-symbol-info \"phel\\core\" \"map\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L198",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/load-file",
        signature = "(load-file path)",
        completion = CompletionInfo(
            tailText = "Loads and evaluates a Phel source file in the current REPL session",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Loads and evaluates a Phel source file in the current REPL session.<br />
  Reads the file content and evaluates it using the compiler, preserving<br />
  the current REPL namespace context. Returns the result of the last expression.
""",
            example = "(load-file \"src/my-module.phel\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L180",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/loaded-namespaces",
        signature = "(loaded-namespaces)",
        completion = CompletionInfo(
            tailText = "Returns all namespaces currently loaded in the REPL",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns all namespaces currently loaded in the REPL.",
            example = "(loaded-namespaces) ; =&gt; [\"phel\\core\" \"phel\\repl\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L22",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/macroexpand",
        signature = "(macroexpand form)",
        completion = CompletionInfo(
            tailText = "Recursively expands the given form until it is no longer a macro call",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Recursively expands the given form until it is no longer a macro call.<br />
  The form is automatically quoted. Returns the fully expanded form.
""",
            example = "(macroexpand (defn foo [x] x))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L475",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/macroexpand-1",
        signature = "(macroexpand-1 form)",
        completion = CompletionInfo(
            tailText = "Expands the given form once",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Expands the given form once. The form is automatically quoted.<br />
  Returns the expanded form, or the original if it is not a macro call.
""",
            example = "(macroexpand-1 (defn foo [x] x))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L467",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/macroexpand-1-form",
        signature = "(macroexpand-1-form form)",
        completion = CompletionInfo(
            tailText = "Expands the given form once if it is a macro call",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Expands the given form once if it is a macro call.<br />
  Takes a quoted form and returns the expanded form, or the original<br />
  form unchanged if it is not a macro call. Uses the CompilerFacade<br />
  to perform the expansion without going through analyze+emit.
""",
            example = "(macroexpand-1-form '(defn foo [x] x))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L446",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/macroexpand-form",
        signature = "(macroexpand-form form)",
        completion = CompletionInfo(
            tailText = "Recursively expands the given form until it is no longer a macro call",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Recursively expands the given form until it is no longer a macro call.<br />
  Takes a quoted form and returns the fully expanded form. Uses the<br />
  CompilerFacade to perform the expansion without going through analyze+emit.
""",
            example = "(macroexpand-form '(defn foo [x] x))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L457",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/ns-aliases",
        signature = "(ns-aliases ns-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of {alias => namespace} for all require aliases in the given namespace string",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of {alias => namespace} for all require aliases in the<br />
  given namespace string.
""",
            example = "(ns-aliases *ns*) ; =&gt; {\"repl\" \"phel\\repl\" ...}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L349",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/ns-list",
        signature = "(ns-list)",
        completion = CompletionInfo(
            tailText = "Returns a sorted vector of all loaded namespace names, decoded to human-readable form (hyphens re...",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a sorted vector of all loaded namespace names, decoded to<br />
  human-readable form (hyphens restored instead of underscores).
""",
            example = "(ns-list) ; =&gt; [\"phel\\core\" \"phel\\repl\" ...]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L28",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/ns-publics",
        signature = "(ns-publics ns-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of {name => value} for all public definitions in the given namespace string",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of {name => value} for all public definitions in the<br />
  given namespace string. Private definitions are excluded.
""",
            example = "(ns-publics \"phel\\core\") ; =&gt; {\"map\" &lt;fn&gt; \"filter\" &lt;fn&gt; ...}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L333",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/ns-refers",
        signature = "(ns-refers ns-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of {name => source-namespace} for all referred symbols in the given namespace ...",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of {name => source-namespace} for all referred symbols<br />
  in the given namespace string.
""",
            example = "(ns-refers *ns*) ; =&gt; {\"doc\" \"phel\\repl\" ...}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L362",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/print-colorful",
        signature = "(print-colorful & xs)",
        completion = CompletionInfo(
            tailText = "Prints arguments with colored output",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Prints arguments with colored output.",
            example = "(print-colorful [1 2 3])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L146",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/println-colorful",
        signature = "(println-colorful & xs)",
        completion = CompletionInfo(
            tailText = "Prints arguments with colored output followed by a newline",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Prints arguments with colored output followed by a newline.",
            example = "(println-colorful [1 2 3])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L153",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/require",
        signature = "(require sym & args)",
        completion = CompletionInfo(
            tailText = "Requires a Phel module into the environment",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Requires a Phel module into the environment.",
            example = "(require phel\\http :as http :refer [request]) ; =&gt; phel\\http",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L109",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/resolve",
        signature = "(resolve sym)",
        completion = CompletionInfo(
            tailText = "Resolves the given symbol in the current environment and returns a resolved Symbol with the absol...",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Resolves the given symbol in the current environment and returns a resolved Symbol with the absolute namespace or nil if it cannot be resolved.
""",
            example = "(resolve 'map) ; =&gt; phel\\core/map",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L59",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/search-doc",
        signature = "(search-doc search)",
        completion = CompletionInfo(
            tailText = "Searches docstrings across all loaded namespaces for the given string",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Searches docstrings across all loaded namespaces for the given string.<br />
  Prints matching function names and their documentation.
""",
            example = "(search-doc \"reduce\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L375",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/source",
        signature = "(source sym)",
        completion = CompletionInfo(
            tailText = "Returns the source code of the given symbol as a string, or nil if unavailable",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the source code of the given symbol as a string, or nil if unavailable.",
            example = "(source map)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L324",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/symbol-info",
        signature = "(symbol-info sym)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of structured metadata for the given symbol, or nil if the symbol cannot be re...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of structured metadata for the given symbol, or nil if<br />
  the symbol cannot be resolved.<br /><br />
Keys: :doc, :file, :line, :column, :end-line, :end-column, :private,<br />
        :deprecated, :min-arity, :max-arity, :is-variadic, :ns, :name
""",
            example = "(symbol-info map) ; =&gt; {:doc \"...\" :ns \"phel\\core\" :name \"map\" ...}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L227",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/test-ns",
        signature = "(test-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Runs all tests in a given namespace from the REPL",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs all tests in a given namespace from the REPL.<br />
  Requires the namespace if not already loaded, finds all deftest definitions,<br />
  runs them, prints results, and returns a hash-map with :pass, :fail, and :error counts.<br />
  Preserves outer test state so it can be safely called during a test run.
""",
            example = "(test-ns \"phel-test\\test\\core\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L426",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/use",
        signature = "(use sym & args)",
        completion = CompletionInfo(
            tailText = "Adds a use statement to the environment",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Adds a use statement to the environment.",
            example = "(use DateTime :as DT) ; =&gt; DateTime",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L123",
                docs = "",
            ),
        ),
    )
)
