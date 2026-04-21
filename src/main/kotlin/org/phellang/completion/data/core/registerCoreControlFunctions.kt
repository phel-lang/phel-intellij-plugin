package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreControlFunctions(): List<PhelFunction> = listOf(
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L45",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L31",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "condp",
        signature = "(condp pred expr & clauses)",
        completion = CompletionInfo(
            tailText = "Takes a binary predicate, an expression, and a set of clauses",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a binary predicate, an expression, and a set of clauses.<br />
  Each clause takes the form of either:<br />
    test-expr result-expr<br />
    test-expr :>> result-fn<br />
  For each clause, (pred test-expr expr) is evaluated. If it returns<br />
  logical true, the clause is a match. If a binary clause is a match,<br />
  result-expr is returned. If a ternary clause with :>> is a match,<br />
  the result of (pred test-expr expr) is passed to result-fn and the<br />
  return value is the result. If no clause matches, the default value<br />
  is returned (if provided), otherwise an exception is thrown.
""",
            example = "(condp = 1 1 \"one\" 2 \"two\" \"other\") ; =&gt; \"one\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L76",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L13",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L19",
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
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/control.phel#L25",
                docs = "",
            ),
        ),
    )
)
