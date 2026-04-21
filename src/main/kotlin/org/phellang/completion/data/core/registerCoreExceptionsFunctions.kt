package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreExceptionsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "ex-cause",
        signature = "(ex-cause ex)",
        completion = CompletionInfo(
            tailText = "Returns the cause of an exception, or nil",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the cause of an exception, or nil.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/exceptions.phel#L34",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-data",
        signature = "(ex-data ex)",
        completion = CompletionInfo(
            tailText = "Returns the data map from an ex-info exception, or nil if not an ExInfoException",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the data map from an ex-info exception, or nil if not an ExInfoException.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/exceptions.phel#L21",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-info",
        signature = "(ex-info msg data)",
        completion = CompletionInfo(
            tailText = "Creates an exception with a message and a data map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates an exception with a message and a data map. Optionally takes a cause.",
            example = "(throw (ex-info \"Invalid input\" {:field :email}))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/exceptions.phel#L12",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-message",
        signature = "(ex-message ex)",
        completion = CompletionInfo(
            tailText = "Returns the message of an exception",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the message of an exception.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/exceptions.phel#L28",
                docs = "",
            ),
        ),
    )
)
