package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerReplFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "repl",
        name = "repl/build-facade",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L11",
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
            summary = """Compiles a Phel expression string to PHP code.""",
            example = "(compile-str \"(+ 1 2)\") ; => \"(1 + 2)\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L135",
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
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Prints the documentation for the given symbol.""",
            example = "(doc map)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L53",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/loaded-namespaces",
        signature = "(loaded-namespaces )",
        completion = CompletionInfo(
            tailText = "Returns all namespaces currently loaded in the REPL",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Returns all namespaces currently loaded in the REPL.""",
            example = "(loaded-namespaces) ; => [\"phel\\core\" \"phel\repl\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L18",
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
            summary = """Prints arguments with colored output.""",
            example = "(print-colorful [1 2 3])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L120",
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
            summary = """Prints arguments with colored output followed by a newline.""",
            example = "(println-colorful [1 2 3])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L127",
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
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Requires a Phel module into the environment.""",
            example = "(require phel\\http :as http :refer [request]) ; => phel\\http",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L83",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "repl",
        name = "repl/resolve",
        signature = "(resolve sym)",
        completion = CompletionInfo(
            tailText = "Resolves the given symbol in the current environment and returns a resolved Symbol with the absolute namespace or nil if it cannot be resolved",
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Resolves the given symbol in the current environment and returns a resolved Symbol with the absolute namespace or nil if it cannot be resolved.""",
            example = "(resolve 'map) ; => phel\\core/map",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L33",
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
            priority = PhelCompletionPriority.REPL_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Adds a use statement to the environment.""",
            example = "(use DateTime :as DT) ; => DateTime",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/repl.phel#L97",
                docs = "",
            ),
        ),
    ),
)
