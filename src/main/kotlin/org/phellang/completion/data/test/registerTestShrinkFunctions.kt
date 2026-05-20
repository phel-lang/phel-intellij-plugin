package org.phellang.completion.data.test

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerTestShrinkFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "test.shrink",
        name = "test.shrink/args->rose",
        signature = "(args->rose args)",
        completion = CompletionInfo(
            tailText = "Rose tree whose root is args and children shrink each positional argument in place using its valu...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Rose tree whose root is <code>args</code> and children shrink each positional<br />
  argument in place using its value-based shrinker. Arguments are<br />
  never dropped (they are function parameters, not collection<br />
  elements).
""",
            example = "(args-&gt;rose [10])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test/shrink.phel#L108",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.shrink",
        name = "test.shrink/shrink",
        signature = "(shrink pred tree)",
        completion = CompletionInfo(
            tailText = "Walks rose tree tree depth-first, greedily descending into any child whose root still fails pred",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Walks rose tree <code>tree</code> depth-first, greedily descending into any<br />
  child whose root still fails <code>pred</code>. Returns<br />
  <code>{:smallest v :shrink-steps n :tree final-tree}</code>.
""",
            example = "(shrink pred (value-&gt;rose failing-value))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test/shrink.phel#L89",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.shrink",
        name = "test.shrink/shrink-args",
        signature = "(shrink-args property args)",
        completion = CompletionInfo(
            tailText = "Shrinks a failing args vector using property",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Shrinks a failing args vector using <code>property</code>. Returns<br />
  <code>{:smallest args :shrink-steps n}</code> where <code>args</code> is the smallest args<br />
  vector that still makes <code>property</code> fail.
""",
            example = "(shrink-args property [10])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test/shrink.phel#L119",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "test.shrink",
        name = "test.shrink/value->rose",
        signature = "(value->rose v)",
        completion = CompletionInfo(
            tailText = "Builds a rose tree for v using the built-in shrink strategy that matches its runtime type",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Builds a rose tree for <code>v</code> using the built-in shrink strategy that<br />
  matches its runtime type. Integers shrink toward zero; strings,<br />
  vectors, lists, hash-maps and sets shrink by element removal plus<br />
  recursive element shrinks; everything else is a leaf.
""",
            example = "(value-&gt;rose 10)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/test/shrink.phel#L49",
                docs = "",
            ),
        ),
    )
)
