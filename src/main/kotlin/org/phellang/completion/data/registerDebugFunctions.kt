package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerDebugFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "debug",
        name = "debug/dbg",
        signature = "(dbg expr)",
        completion = CompletionInfo(
            tailText = "Evaluates an expression and prints it with its result",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Evaluates an expression and prints it with its result.""",
            example = "(dbg (+ 1 2))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/debug.phel#L46",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "debug",
        name = "debug/dotrace",
        signature = "(dotrace name f)",
        completion = CompletionInfo(
            tailText = "Wraps a function to print each call and result with indentation",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Wraps a function to print each call and result with indentation.""",
            example = "(def add (dotrace \"add\" +))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/debug.phel#L18",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "debug",
        name = "debug/reset-trace-state!",
        signature = "(reset-trace-state! )",
        completion = CompletionInfo(
            tailText = "Resets trace counters to initial values",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Resets trace counters to initial values.""",
            example = "(reset-trace-state!)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/debug.phel#L37",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "debug",
        name = "debug/set-trace-id-padding!",
        signature = "(set-trace-id-padding! estimated-id-padding)",
        completion = CompletionInfo(
            tailText = "Sets the number of digits for trace ID padding",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Sets the number of digits for trace ID padding.""",
            example = "(set-trace-id-padding! 3)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/debug.phel#L9",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "debug",
        name = "debug/spy",
        signature = "(spy expr)",
        completion = CompletionInfo(
            tailText = "Evaluates an expression and prints it with an optional label",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Evaluates an expression and prints it with an optional label.""",
            example = "(spy (+ 1 2))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/debug.phel#L55",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "debug",
        name = "debug/tap",
        signature = "(tap value)",
        completion = CompletionInfo(
            tailText = "Prints a value and returns it unchanged. Useful in pipelines",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """Prints a value and returns it unchanged. Useful in pipelines.""",
            example = "(-> 5 (tap) (* 2))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/debug.phel#L71",
                docs = "",
            ),
        ),
    ),
)
