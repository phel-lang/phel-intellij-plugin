package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreCollectionsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "hash-set",
        signature = "(hash-set & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new Set from the given arguments",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new Set from the given arguments. Shortcut: #{}",
            example = "(hash-set 1 2 3) ; =&gt; #{1 2 3}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/collections.phel#L15",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted-map",
        signature = "(sorted-map & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new sorted map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new sorted map. Keys are in natural sorted order.<br />
  The number of parameters must be even.
""",
            example = "(sorted-map :c 3 :a 1 :b 2) ; keys iterate as :a :b :c",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/collections.phel#L21",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted-map-by",
        signature = "(sorted-map-by comp & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new sorted map using the given comparator for key ordering",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a new sorted map using the given comparator for key ordering.<br />
  The comparator takes two arguments and returns a negative integer,<br />
  zero, or a positive integer.
""",
            example = "(sorted-map-by (fn [a b] (compare b a)) :a 1 :b 2) ; keys iterate as :b :a",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/collections.phel#L29",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted-set",
        signature = "(sorted-set & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new sorted set",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new sorted set. Elements are in natural sorted order.",
            example = "(sorted-set 3 1 2) ; iterates as 1 2 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/collections.phel#L38",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "sorted-set-by",
        signature = "(sorted-set-by comp & xs)",
        completion = CompletionInfo(
            tailText = "Creates a new sorted set using the given comparator for element ordering",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new sorted set using the given comparator for element ordering.",
            example = "(sorted-set-by (fn [a b] (compare b a)) 3 1 2) ; iterates as 3 2 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/collections.phel#L45",
                docs = "",
            ),
        ),
    )
)
