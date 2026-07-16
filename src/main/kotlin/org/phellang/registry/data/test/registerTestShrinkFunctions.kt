package org.phellang.registry.data.test

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

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
Rose tree whose root is <code>args</code> and children shrink each positional argument in place using its value-based shrinker. Arguments are never dropped (they are function parameters, not collection elements).
""",
            example = "(args-&gt;rose [10])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/test/shrink.phel#L103",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/test/shrink.phel#L84",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/test/shrink.phel#L111",
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
Builds a rose tree for <code>v</code> using the built-in shrink strategy that matches its runtime type. Integers shrink toward zero; strings, vectors, lists, hash-maps and sets shrink by element removal plus recursive element shrinks; everything else is a leaf.
""",
            example = "(value-&gt;rose 10)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/test/shrink.phel#L48",
                docs = "",
            ),
        ),
    )
)
