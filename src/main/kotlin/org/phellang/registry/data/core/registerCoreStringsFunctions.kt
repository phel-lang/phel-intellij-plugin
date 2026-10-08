package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreStringsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "gensym",
        signature = "(gensym)\n(gensym prefix)",
        completion = CompletionInfo(
            tailText = "Generates a new unique symbol",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generates a new unique symbol. With <code>prefix</code>, the name is <code>prefix</code> followed by a unique number; without it, the prefix is <code>__phel_</code>.
""",
            example = "(gensym \"tmp\") ; =&gt; tmp1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/strings.phel#L85",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "str",
        signature = "(str)\n(str x)\n(str x y)\n(str x y z)\n(str x y z a)\n(str x y z a b)\n(str x y z a b & more)",
        completion = CompletionInfo(
            tailText = "Creates a string by concatenating values together",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a string by concatenating values together. If no arguments are provided an empty string is returned. Nil is represented as an empty string. Booleans are represented as "true" or "false" (matching Clojure semantics). Otherwise, it tries to call <code>__toString</code>.
""",
            example = "(str \"a\" \"b\" \"c\") ; =&gt; \"abc\"\n(str 1 2 3) ; =&gt; \"123\"\n(str 1 nil true) ; =&gt; \"1true\"\n(str) ; =&gt; \"\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/strings.phel#L116",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "subs",
        signature = "(subs s start)\n(subs s start end)",
        completion = CompletionInfo(
            tailText = "Returns the substring of s from start (inclusive) to end (exclusive), or to the end of s when end...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the substring of <code>s</code> from <code>start</code> (inclusive) to <code>end</code> (exclusive), or to the end of <code>s</code> when <code>end</code> is left out. Indexes count multibyte characters. Throws <code>InvalidArgumentException</code> when <code>s</code> is not a string, an index is not an int, or an index is out of range.
""",
            example = "(subs \"hello world\" 6) ; =&gt; \"world\"\n(subs \"hello world\" 0 5) ; =&gt; \"hello\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/strings.phel#L167",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "symbol",
        signature = "(symbol name-or-ns)\n(symbol name-or-ns name)\n(symbol name-or-ns name & ignored)",
        completion = CompletionInfo(
            tailText = "Returns a new symbol for the given name with an optional namespace",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new symbol for the given name with an optional namespace.<br /><br />
With one argument, creates a symbol without namespace. Accepts a string, a keyword, another symbol, or a <code>Var</code> (in which case the result is a fully qualified symbol naming the var). With two arguments, creates a symbol in the given namespace; a <code>nil</code> namespace yields a symbol without a namespace.<br /><br />
Throws <code>InvalidArgumentException</code> for any other input (including functions, numbers, and collections).
""",
            example = "(symbol \"foo\") ; =&gt; foo\n(symbol :abc) ; =&gt; abc\n(symbol nil \"foo\") ; =&gt; foo\n(symbol #'phel.core/+) ; =&gt; phel.core/+",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/strings.phel#L46",
                docs = "",
            ),
        ),
    )
)
