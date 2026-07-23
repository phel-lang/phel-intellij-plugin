package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerPprintFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "pprint",
        name = "pprint/inspect",
        signature = "(inspect x & [width])",
        completion = CompletionInfo(
            tailText = "Pretty-print a value for humans (colored when stdout is a terminal) and return it unchanged, so i...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Pretty-print a value for humans (colored when stdout is a terminal) and return it unchanged, so it can be dropped into threading pipelines.
""",
            example = "(-&gt; {:a [1 2 3]} inspect (get :a))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/pprint.phel#L119",
                docs = "",
            ),
        ),
    ),
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/pprint.phel#L100",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/pprint.phel#L91",
                docs = "",
            ),
        ),
    )
)
