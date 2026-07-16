package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerMatchFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "match",
        name = "match/match",
        signature = "(match targets & body)",
        completion = CompletionInfo(
            tailText = "Pattern matching over a vector of targets",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Pattern matching over a vector of targets.",
            example = "(match [x y] [0 _] :zero-x [_ 0] :zero-y [a b] [:both a b])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/match.phel#L280",
                docs = "",
            ),
        ),
    )
)
