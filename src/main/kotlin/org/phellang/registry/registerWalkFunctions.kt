package org.phellang.registry

import org.phellang.registry.PhelCompletionPriority

internal fun registerWalkFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "walk",
        name = "walk/keywordize-keys",
        signature = "(keywordize-keys m)",
        completion = CompletionInfo(
            tailText = "Convert string map keys to keywords, recursively",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Convert string map keys to keywords, recursively.",
            example = "(keywordize-keys {\"name\" \"phel\"}) ; =&gt; {:name \"phel\"}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/walk.phel#L59",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "walk",
        name = "walk/postwalk",
        signature = "(postwalk f form)",
        completion = CompletionInfo(
            tailText = "Bottom-up tree walk — applies f after recursing into children",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Bottom-up tree walk — applies f after recursing into children.",
            example = "(postwalk inc [1 [2 3]]) ; =&gt; [2 [3 4]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/walk.phel#L25",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "walk",
        name = "walk/postwalk-replace",
        signature = "(postwalk-replace smap form)",
        completion = CompletionInfo(
            tailText = "Replace values bottom-up using a substitution map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Replace values bottom-up using a substitution map.",
            example = "(postwalk-replace {:a :b} [:a :c]) ; =&gt; [:b :c]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/walk.phel#L41",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "walk",
        name = "walk/prewalk",
        signature = "(prewalk f form)",
        completion = CompletionInfo(
            tailText = "Top-down tree walk — applies f before recursing into children",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Top-down tree walk — applies f before recursing into children.",
            example = "(prewalk identity [1 [2 3]]) ; =&gt; [1 [2 3]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/walk.phel#L33",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "walk",
        name = "walk/prewalk-replace",
        signature = "(prewalk-replace smap form)",
        completion = CompletionInfo(
            tailText = "Replace values top-down using a substitution map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Replace values top-down using a substitution map.",
            example = "(prewalk-replace {:a :b} [:a :c]) ; =&gt; [:b :c]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/walk.phel#L50",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "walk",
        name = "walk/stringify-keys",
        signature = "(stringify-keys m)",
        completion = CompletionInfo(
            tailText = "Convert keyword map keys to strings, recursively",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Convert keyword map keys to strings, recursively.",
            example = "(stringify-keys {:name \"phel\"}) ; =&gt; {\"name\" \"phel\"}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/walk.phel#L74",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "walk",
        name = "walk/walk",
        signature = "(walk inner outer form)",
        completion = CompletionInfo(
            tailText = "Generic tree walker for nested data structures",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generic tree walker for nested data structures.",
            example = "(walk inc identity [1 2 3]) ; =&gt; [2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/walk.phel#L3",
                docs = "",
            ),
        ),
    )
)
