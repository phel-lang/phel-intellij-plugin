package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

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
            example = "(ex-cause (ex-info \"boom\" {})) ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/exceptions.phel#L35",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-data",
        signature = "(ex-data ex)",
        completion = CompletionInfo(
            tailText = "Returns the data map from an ex-info exception, or nil if not an ExceptionInfo",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns the data map from an ex-info exception, or nil if not an ExceptionInfo.",
            example = "(ex-data (ex-info \"boom\" {:a 1})) ; =&gt; {:a 1}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/exceptions.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ex-info",
        signature = "(ex-info msg data)\n(ex-info msg data cause)",
        completion = CompletionInfo(
            tailText = "Creates an exception with a message and a data map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates an exception with a message and a data map. Optionally takes a cause.",
            example = "(throw (ex-info \"Invalid input\" {:field :email}))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/exceptions.phel#L11",
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
            example = "(ex-message (ex-info \"boom\" {})) ; =&gt; \"boom\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/exceptions.phel#L28",
                docs = "",
            ),
        ),
    )
)
