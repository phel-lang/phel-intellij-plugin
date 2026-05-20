package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerWatchFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "watch",
        name = "watch/clear-on-reload",
        signature = "(clear-on-reload namespace)",
        completion = CompletionInfo(
            tailText = "Removes every hook registered for namespace",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes every hook registered for <code>namespace</code>.
""",
            example = "(clear-on-reload \"my-app\\core\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/watch.phel#L18",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "watch",
        name = "watch/register-on-reload",
        signature = "(register-on-reload namespace name hook-fn)",
        completion = CompletionInfo(
            tailText = "Registers a zero-arg function to run every time namespace is reloaded by the watcher",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers a zero-arg function to run every time <code>namespace</code> is<br />
  reloaded by the watcher. Replaces any previously registered hook<br />
  with the same <code>name</code>.
""",
            example = "(register-on-reload \"my-app\\core\" :refresh (fn [] (println \"reloaded\")))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/watch.phel#L7",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "watch",
        name = "watch/run-on-reload-hooks",
        signature = "(run-on-reload-hooks namespace)",
        completion = CompletionInfo(
            tailText = "Executes every hook registered for namespace",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Executes every hook registered for <code>namespace</code>. Returns the<br />
  number of hooks that fired.
""",
            example = "(run-on-reload-hooks \"my-app\\core\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/watch.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "watch",
        name = "watch/watch!",
        signature = "(watch! paths & [opts])",
        completion = CompletionInfo(
            tailText = "Starts the file watcher on the given vector of paths",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Starts the file watcher on the given vector of paths. Blocks until<br />
  the watcher is stopped (e.g. Ctrl+C). Reloads changed namespaces in<br />
  dependency order and runs any hooks registered with <code>register-on-reload</code>.
""",
            example = "(watch! [\"src/\" \"tests/\"])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/watch.phel#L36",
                docs = "",
            ),
        ),
    )
)
