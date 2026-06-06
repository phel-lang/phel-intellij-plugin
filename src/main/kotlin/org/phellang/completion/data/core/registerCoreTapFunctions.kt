package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreTapFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "add-tap",
        signature = "(add-tap f)",
        completion = CompletionInfo(
            tailText = "Registers f as a tap",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers <code>f</code> as a tap. Every call to <code>tap></code> invokes each registered tap with the tapped value. Returns nil.
""",
            example = "(add-tap println)\n(tap&gt; 42) ; prints 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/tap.phel#L15",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "remove-tap",
        signature = "(remove-tap f)",
        completion = CompletionInfo(
            tailText = "Removes f from the tap set",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes <code>f</code> from the tap set. Returns nil.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/tap.phel#L23",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "tap>",
        signature = "(tap> x)",
        completion = CompletionInfo(
            tailText = "Sends x to every registered tap",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sends <code>x</code> to every registered tap. Exceptions thrown by individual taps are swallowed so one misbehaving tap does not affect the others. Returns true.
""",
            example = "(add-tap println)\n(tap&gt; {:event :login :user \"alice\"})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/core/tap.phel#L30",
                docs = "",
            ),
        ),
    )
)
