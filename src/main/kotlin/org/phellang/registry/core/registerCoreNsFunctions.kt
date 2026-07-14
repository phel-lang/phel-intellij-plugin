package org.phellang.registry.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreNsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "canonical-ns",
        signature = "(canonical-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Returns the canonical (dot-separated) form of a namespace string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the canonical (dot-separated) form of a namespace string. Pass user-supplied namespace strings through this before any registry lookup or write.
""",
            example = "(canonical-ns \"phel\\core\") ; =&gt; \"phel.core\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L17",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "create-ns",
        signature = "(create-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Creates the given namespace if it does not already exist",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates the given namespace if it does not already exist. Returns the namespace name in display form. Existing namespaces are left untouched.
""",
            example = "(create-ns \"my-app.tmp\") ; =&gt; \"my-app.tmp\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L60",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "display-ns",
        signature = "(display-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Returns the display (dot-separated) form of a namespace string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the display (dot-separated) form of a namespace string. Equivalent to <code>canonical-ns</code>; kept as a separate name so call sites read intent (display vs. canonicalize).
""",
            example = "(display-ns \"phel\\core\") ; =&gt; \"phel.core\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "find-ns",
        signature = "(find-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Returns the namespace name in display form if it is loaded, or nil if no namespace of that name e...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the namespace name in display form if it is loaded, or nil if no namespace of that name exists. Accepts dot or backslash separators on input.
""",
            example = "(find-ns \"phel.core\") ; =&gt; \"phel.core\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L51",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "find-var",
        signature = "(find-var sym)",
        completion = CompletionInfo(
            tailText = "Returns the Var named by the fully-qualified symbol, or nil if no matching definition exists",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the <code>Var</code> named by the fully-qualified symbol, or nil if no matching definition exists. Tries the symbol name verbatim first (matching how <code>def</code> stores entries), then falls back to the encoded form (matching how <code>intern</code> stores entries).
""",
            example = "(find-var 'phel.core/map) ; =&gt; #'phel.core/map",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L147",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "intern",
        signature = "(intern ns-str name-str & rest)",
        completion = CompletionInfo(
            tailText = "Finds or creates a definition named by name-str in namespace ns-str",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Finds or creates a definition named by name-str in namespace ns-str. When a value is supplied, sets the root value to val (overwriting any existing value). When no value is supplied, returns the existing definition unchanged, or creates a new one with nil if none exists. Auto-registers the namespace when creating a new definition; the arity-2 form is a no-op when the definition already exists. Returns the fully qualified symbol naming the definition (with the namespace in display form).
""",
            example = "(intern \"user\" \"x\" 42) ; =&gt; user/x",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L97",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "load-file",
        signature = "(load-file path)",
        completion = CompletionInfo(
            tailText = "Loads and evaluates a Phel source file",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Loads and evaluates a Phel source file. Reads the file content and evaluates it using the compiler. Returns the result of the last expression, or nil if the file cannot be read.
""",
            example = "(load-file \"src/my-module.phel\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L201",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "loaded-namespaces",
        signature = "(loaded-namespaces)",
        completion = CompletionInfo(
            tailText = "Returns all namespaces currently loaded, in dot-separated display form",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns all namespaces currently loaded, in dot-separated display form.",
            example = "(loaded-namespaces) ; =&gt; [\"phel.core\" \"phel.repl\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L31",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ns-aliases",
        signature = "(ns-aliases ns-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of {alias => namespace} for all require aliases in the given namespace string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of {alias => namespace} for all require aliases in the<br />
  given namespace string. Target namespaces are returned in display form.
""",
            example = "(ns-aliases *ns*) ; =&gt; {\"repl\" \"phel.repl\" ...}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L175",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ns-interns",
        signature = "(ns-interns ns-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of {name => value} for every definition in the given namespace, including priv...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of {name => value} for every definition in the<br />
  given namespace, including private ones. Returns an empty map if the<br />
  namespace has no definitions.
""",
            example = "(ns-interns \"phel\\core\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L88",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ns-publics",
        signature = "(ns-publics ns-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of {name => value} for all public definitions in the given namespace string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of {name => value} for all public definitions in the<br />
  given namespace string. Private definitions are excluded.
""",
            example = "(ns-publics \"phel\\core\") ; =&gt; {\"map\" &lt;fn&gt; \"filter\" &lt;fn&gt; ...}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L164",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ns-refers",
        signature = "(ns-refers ns-str)",
        completion = CompletionInfo(
            tailText = "Returns a hash-map of {name => source-namespace} for all referred symbols in the given namespace ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a hash-map of {name => source-namespace} for all referred symbols<br />
  in the given namespace string. Source namespaces are returned in display form.
""",
            example = "(ns-refers *ns*) ; =&gt; {\"doc\" \"phel.repl\" ...}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L188",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "remove-ns",
        signature = "(remove-ns ns-str)",
        completion = CompletionInfo(
            tailText = "Removes the namespace and all of its definitions from the registry",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes the namespace and all of its definitions from the registry. Use with caution. Returns nil.
""",
            example = "(remove-ns \"my-app\\tmp\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L69",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "var-get",
        signature = "(var-get x)\n(var-get x not-found)",
        completion = CompletionInfo(
            tailText = "Resolves the value bound to a Var or to a fully-qualified symbol",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Resolves the value bound to a <code>Var</code> or to a fully-qualified symbol.<br /><br />
Accepts a <code>Var</code> instance (typically from <code>(var sym)</code> or <code>#'sym</code>) and<br />
  returns its current root value. Accepts a fully-qualified symbol and<br />
  reads the matching definition from the registry; tries the symbol name<br />
  verbatim first, then the encoded form, so it pairs with both <code>def</code> and<br />
  <code>intern</code>.<br /><br />
With one argument, returns nil when the symbol is not defined. With two<br />
  arguments, returns <code>not-found</code> instead, which lets callers disambiguate a<br />
  stored nil from a missing definition. Returns <code>not-found</code> for non-symbol<br />
  or unqualified inputs.
""",
            example = "(var-get #'phel.core/map) ; =&gt; &lt;function:map&gt;\n(var-get (intern \"user\" \"x\" 42)) ; =&gt; 42\n(var-get 'user/missing ::none) ; =&gt; :user/none",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/core/ns.phel#L112",
                docs = "",
            ),
        ),
    )
)
