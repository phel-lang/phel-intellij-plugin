package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreFuturesFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "future",
        signature = "(future & body)",
        completion = CompletionInfo(
            tailText = "Starts evaluating body asynchronously and returns a Future that can be derefed (blocks the curren...",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Starts evaluating <code>body</code> asynchronously and returns a <code>Future</code><br />
  that can be <code>deref</code>ed (blocks the current fiber until the value is<br />
  available) or checked with <code>realized?</code>.<br /><br />
Supports the 3-arg <code>(deref f timeout-ms timeout-val)</code> form for<br />
  time-bounded blocking, as well as <code>future-cancel</code>, <code>future-cancelled?</code><br />
  and <code>future-done?</code> for lifecycle management.<br /><br />
Must be called from inside a fiber context (e.g. the AMPHP event<br />
  loop or an enclosing <code>async</code> block), because <code>deref</code> resolves via<br />
  AMPHP's fiber-based <code>await</code>.
""",
            example = "(let [f (future (expensive-computation))] @f)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/futures.phel#L9",
                docs = "",
            ),
        ),
    )
)
