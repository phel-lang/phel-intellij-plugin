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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L283",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L17",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L187",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/create-ns",
        signature = "(create-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Creates the given namespace if it does not already exist",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates the given namespace if it does not already exist. Returns the<br />
  namespace name. Existing namespaces are left untouched.
""",
            example = "(create-ns \"my-app\\tmp\") ; =&gt; \"my-app\\tmp\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L368",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L266",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L78",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/embed-ns",
        signature = "(embed-ns ns-str & [opts])",
        completion = CompletionInfo(
            tailText = "Builds a searchable index of public functions in a namespace",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Builds a searchable index of public functions in a namespace.",
            example = "(embed-ns \"phel\\core\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L676",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L480",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L196",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/explain",
        signature = "(explain code-str)",
        completion = CompletionInfo(
            tailText = "Explains a Phel expression using AI and REPL introspection",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Explains a Phel expression using AI and REPL introspection.",
            example = "(explain \"(map inc [1 2 3])\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L596",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/explain-sym",
        signature = "(explain-sym sym)",
        completion = CompletionInfo(
            tailText = "Explains a symbol using AI with source code and docs as context",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Explains a symbol using AI with source code and docs as context.",
            example = "(explain-sym map)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L617",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L301",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/find-ns",
        signature = "(find-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Returns the namespace name if it is loaded, or nil if no namespace of that name exists",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the namespace name if it is loaded, or nil if no namespace of<br />
  that name exists.
""",
            example = "(find-ns \"phel\\core\") ; =&gt; \"phel\\core\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L359",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/fix",
        signature = "(fix code-str error-msg)",
        completion = CompletionInfo(
            tailText = "Suggests a fix for Phel code that produced an error",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Suggests a fix for Phel code that produced an error.",
            example = "(fix \"(+ 1 \\\"a\\\")\" \"Cannot add string to integer\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L645",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L333",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L224",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/intern",
        signature = "(intern ns-str name-str & rest)",
        completion = CompletionInfo(
            tailText = "Finds or creates a definition named by name-str in namespace ns-str, setting its root value to va...",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Finds or creates a definition named by name-str in namespace ns-str,<br />
  setting its root value to val when supplied (defaults to nil). The<br />
  namespace is auto-registered by the underlying <code>addDefinition</code> call.<br />
  Returns the fully qualified symbol naming the definition.
""",
            example = "(intern \"user\" \"x\" 42) ; =&gt; user/x",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L386",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L206",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L24",
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
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Recursively expands the given form until it is no longer a macro call.<br />
  The form must be quoted to prevent evaluation, matching Clojure semantics.
""",
            example = "(macroexpand '(when true 1 2))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L556",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/macroexpand-1",
        signature = "(macroexpand-1 form)",
        completion = CompletionInfo(
            tailText = "Expands the given form once if it is a macro call",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Expands the given form once if it is a macro call. The form must be<br />
  quoted to prevent evaluation, matching Clojure semantics.
""",
            example = "(macroexpand-1 '(when true 1 2))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L548",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L527",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L538",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L430",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/ns-interns",
        signature = "(ns-interns ns-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of {name => value} for every definition in the given namespace, including priv...",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of {name => value} for every definition in the<br />
  given namespace, including private ones. Returns an empty map if the<br />
  namespace has no definitions.
""",
            example = "(ns-interns \"phel\\core\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L400",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L30",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L414",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L443",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L172",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L179",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/remove-ns",
        signature = "(remove-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Removes the namespace and all of its definitions from the registry",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes the namespace and all of its definitions from the registry.<br />
  Use with caution. Returns nil.
""",
            example = "(remove-ns \"my-app\\tmp\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L377",
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
            example = "(require 'phel\\http :as http :refer [request]) ; =&gt; phel\\http",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L133",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/review",
        signature = "(review code-str)",
        completion = CompletionInfo(
            tailText = "Gets an AI code review for Phel code",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Gets an AI code review for Phel code.",
            example = "(review \"(defn add [a b] (+ a b))\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L661",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L456",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/search-ns",
        signature = "(search-ns query ns-index & [{:k k, :model model, :provider provider}])",
        completion = CompletionInfo(
            tailText = "Searches a namespace index for functions matching a query",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Searches a namespace index for functions matching a query.",
            example = "(search-ns \"transform collections\" my-ns-index 5)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L701",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L350",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/suggest",
        signature = "(suggest description)",
        completion = CompletionInfo(
            tailText = "Get AI-suggested Phel code for a natural language description",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Get AI-suggested Phel code for a natural language description.",
            example = "(suggest \"filter even numbers from a vector\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L630",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L253",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L507",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L148",
                docs = "",
            ),
        ),
    )
)
