package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

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
Starts evaluating <code>body</code> asynchronously and returns a <code>Future</code> that can be <code>deref</code>ed (blocks the current fiber until the value is available) or checked with <code>realized?</code>.<br /><br />
Supports the 3-arg <code>(deref f timeout-ms timeout-val)</code> form for time-bounded blocking, as well as <code>future-cancel</code>, <code>future-cancelled?</code> and <code>future-done?</code> for lifecycle management.<br /><br />
Must be called from inside a fiber context (e.g. the AMPHP event loop or an enclosing <code>async</code> block), because <code>deref</code> resolves via AMPHP's fiber-based <code>await</code>.
""",
            example = "(let [f (future (expensive-computation))] @f)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/core/futures.phel#L9",
                docs = "",
            ),
        ),
    )
)
