package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerBenchFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "bench",
        name = "bench/defbench",
        signature = "(defbench bench-name & body)",
        completion = CompletionInfo(
            tailText = "Defines a benchmark function",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Defines a benchmark function.<br /><br />
The body is timed, not asserted. An optional option map may follow the name:<br />
<code>:revs</code> (calls per measured iteration), <code>:iterations</code> (measured iterations),<br />
<code>:warmup</code> (unmeasured iterations first). Anything omitted falls back to the<br />
default. A run-wide option of the same name, such as a <code>phel bench --revs</code><br />
flag, overrides what the benchmark asks for.<br /><br />
Metadata attached to <code>bench-name</code> is forwarded to the defined function, so<br />
<code>^:slow</code> and <code>^{:tags [:io]}</code> work here exactly as they do on <code>deftest</code>.
""",
            example = "(defbench bench-sum {:revs 10000}\n  (reduce + 0 (range 100)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/bench.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "bench",
        name = "bench/run-benchmarks",
        signature = "(run-benchmarks options & namespaces)",
        completion = CompletionInfo(
            tailText = "Runs every defbench in the given namespaces and prints a table",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs every <code>defbench</code> in the given namespaces and prints a table.<br /><br />
Recognized option keys: <code>:revs</code>, <code>:iterations</code>, <code>:warmup</code> (each overrides the<br />
per-benchmark option of the same name), <code>:filter</code> (substring match against<br />
the benchmark name), <code>:store</code> (path to write the results to as a baseline),<br />
<code>:ref</code> (path to a stored baseline to compare against), and <code>:tolerance</code> (a<br />
percentage; when set, a benchmark slower than its baseline by more than this<br />
makes the run unsuccessful).<br /><br />
Returns true when the run is within tolerance, which is always the case when no<br />
tolerance is set.
""",
            example = "(run-benchmarks {:revs 100} 'my-app.bench)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/bench.phel#L285",
                docs = "",
            ),
        ),
    )
)
