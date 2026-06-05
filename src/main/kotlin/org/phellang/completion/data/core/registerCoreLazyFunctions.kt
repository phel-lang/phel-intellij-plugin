package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreLazyFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "delay",
        signature = "(delay & body)",
        completion = CompletionInfo(
            tailText = "Takes a body of expressions and yields a Delay object that will invoke the body only the first ti...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a body of expressions and yields a Delay object that will invoke the<br />
  body only the first time it is forced (via force or deref/@), caching the result.
""",
            example = "(def d (delay (println \"computing\") 42))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/lazy.phel#L12",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "delay?",
        signature = "(delay? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Delay",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns true if x is a Delay.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/lazy.phel#L29",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "force",
        signature = "(force x)",
        completion = CompletionInfo(
            tailText = "If x is a Delay, forces it and returns its cached value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "If x is a Delay, forces it and returns its cached value. Otherwise returns x.",
            example = "(force (delay 42)) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/lazy.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "iteration",
        signature = "(iteration step opts)",
        completion = CompletionInfo(
            tailText = "Creates a lazy sequence from successive calls to step",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a lazy sequence from successive calls to <code>step</code>.<br />
  <code>step</code> is called with a key (starting with <code>:initk</code>) and returns a result.<br />
  <code>:kf</code> extracts the next key, <code>:vf</code> extracts the value from the result.<br />
  Terminates when the result is nil.<br /><br />
Options map keys:<br />
    :kf     — key function (default: identity)<br />
    :vf     — value function (default: identity)<br />
    :initk  — initial key (default: nil)
""",
            example = "(iteration fetch-page {:kf :next-token :vf :items :initk nil})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/lazy.phel#L37",
                docs = "",
            ),
        ),
    )
)
