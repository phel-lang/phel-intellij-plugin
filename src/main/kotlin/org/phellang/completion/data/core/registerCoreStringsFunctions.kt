package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreStringsFunctions(): List<PhelFunction> = listOf(
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/strings.phel#L80",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/strings.phel#L110",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "symbol",
        signature = "(symbol name-or-ns & [name])",
        completion = CompletionInfo(
            tailText = "Returns a new symbol for the given name with an optional namespace",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new symbol for the given name with an optional namespace.<br /><br />
With one argument, creates a symbol without namespace. Accepts a<br />
  string, a keyword, another symbol, or a <code>Var</code> (in which case the<br />
  result is a fully qualified symbol naming the var). With two<br />
  arguments, creates a symbol in the given namespace; a <code>nil</code><br />
  namespace yields a symbol without a namespace.<br /><br />
Throws <code>InvalidArgumentException</code> for any other input (including<br />
  functions, numbers, and collections).
""",
            example = "(symbol \"foo\") ; =&gt; foo\n(symbol :abc) ; =&gt; abc\n(symbol nil \"foo\") ; =&gt; foo\n(symbol #'phel.core/+) ; =&gt; phel.core/+",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/strings.phel#L47",
                docs = "",
            ),
        ),
    )
)
