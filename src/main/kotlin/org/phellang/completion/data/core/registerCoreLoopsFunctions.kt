package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreLoopsFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "dotimes",
        signature = "(dotimes [binding n] & body)",
        completion = CompletionInfo(
            tailText = "Evaluates body n times with binding bound to integers from 0 to n-1",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Evaluates body <code>n</code> times with <code>binding</code> bound to integers from 0 to n-1.<br />
  Returns nil.
""",
            example = "(dotimes [i 5] (println i))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/loops.phel#L17",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "run!",
        signature = "(run! f coll)",
        completion = CompletionInfo(
            tailText = "Calls (f x) for each element in coll for side effects",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Calls <code>(f x)</code> for each element in <code>coll</code> for side effects. Returns nil.
""",
            example = "(run! println [1 2 3])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/loops.phel#L9",
                docs = "",
            ),
        ),
    )
)
