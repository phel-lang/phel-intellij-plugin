package org.phellang.registry

import org.phellang.registry.PhelCompletionPriority

internal fun registerPprintFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "pprint",
        name = "pprint/pprint",
        signature = "(pprint form & [width])",
        completion = CompletionInfo(
            tailText = "Pretty-print a data structure to stdout with line breaks and indentation",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Pretty-print a data structure to stdout with line breaks and indentation.",
            example = "(pprint {:a [1 2 3] :b {:c 4 :d 5}})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/pprint.phel#L91",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "pprint",
        name = "pprint/pprint-str",
        signature = "(pprint-str form & [width])",
        completion = CompletionInfo(
            tailText = "Pretty-print a data structure to a string with line breaks and indentation",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Pretty-print a data structure to a string with line breaks and indentation.",
            example = "(pprint-str {:a [1 2 3] :b {:c 4 :d 5}})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/pprint.phel#L82",
                docs = "",
            ),
        ),
    )
)
