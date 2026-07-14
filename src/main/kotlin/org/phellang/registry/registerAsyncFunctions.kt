package org.phellang.registry

import org.phellang.registry.PhelCompletionPriority

internal fun registerAsyncFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "async",
        name = "async/delay",
        signature = "(delay seconds)",
        completion = CompletionInfo(
            tailText = "Suspends execution for seconds",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Suspends execution for <code>seconds</code>. Accepts any number (int, float, or<br />
  <code>Ratio</code>); sub-second precision such as <code>0.05</code> is supported.<br /><br />
At the top level this behaves like <code>php/sleep</code> / <code>php/usleep</code>: it<br />
  blocks the script for the given duration. Inside an <code>async</code>/<code>future</code><br />
  body (or any AMPHP fiber context) it suspends the <em>fiber</em>, not the<br />
  whole process, so other concurrent fibers keep running and the<br />
  delay is cancellable via <code>future-cancel</code>.<br /><br />
<code>phel.async/delay</code> is <strong>not</strong> the Clojure <code>clojure.core/delay</code>. The<br />
  Clojure form returns a lazy thunk that caches the result of evaluating<br />
  <code>body</code> on first <code>deref</code>; Phel's version is a sleep primitive. They are<br />
  unrelated. Reach for <code>phel.async/delay</code> when you want to throttle, time<br />
  out, or compose with <code>future</code> / <code>pmap</code>; reach for <code>php/sleep</code> when you<br />
  just want a blocking pause and don't care about fiber semantics.
""",
            example = "(delay 0.5) ; suspends current fiber for 500ms\n  (async (delay 1.0) :done) ; =&gt; future that resolves after 1s\n  (delay (/ 1 1000)) ; ratio collapses to a float",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/async.phel#L18",
                docs = "",
            ),
        ),
    )
)
