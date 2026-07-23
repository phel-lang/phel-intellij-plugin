package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerTraceFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "trace",
        name = "trace/deftrace",
        signature = "(deftrace fn-name-sym & fdecl)",
        completion = CompletionInfo(
            tailText = "Like defn, but every call to the defined function prints its arguments and result via trace-fn",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>defn</code>, but every call to the defined function prints its arguments and result via <code>trace-fn</code>. Recursive calls are traced too. Supports an optional docstring and metadata map like <code>defn</code>.
""",
            example = "(deftrace fact [n] (if (&lt;= n 1) 1 (* n (fact (dec n)))))\n(fact 2)\n; TRACE t1: (fact 2)\n; TRACE t2: | (fact 1)\n; TRACE t2: | =&gt; 1\n; TRACE t1: =&gt; 2",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/trace.phel#L58",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "trace",
        name = "trace/dotrace",
        signature = "(dotrace fn-syms & body)",
        completion = CompletionInfo(
            tailText = "Temporarily traces the given global functions while body runs, then restores them",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Temporarily traces the given global functions while <code>body</code> runs, then restores them. <code>fn-syms</code> is a vector of symbols naming global functions. Process-global like <code>with-redefs</code>, so intended for debugging and tests, not concurrent production code.
""",
            example = "(defn add [a b] (+ a b))\n(dotrace [add] (add 1 2))\n; TRACE t1: (add 1 2)\n; TRACE t1: =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/trace.phel#L70",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "trace",
        name = "trace/reset-trace-state!",
        signature = "(reset-trace-state!)",
        completion = CompletionInfo(
            tailText = "Resets the trace depth and the trace id counter to their initial values",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Resets the trace depth and the trace id counter to their initial values. Useful to make trace output deterministic in tests.
""",
            example = "(reset-trace-state!)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/trace.phel#L21",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "trace",
        name = "trace/trace",
        signature = "(trace value)\n(trace tag value)",
        completion = CompletionInfo(
            tailText = "Prints value to the standard error stream and returns it unchanged",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Prints <code>value</code> to the standard error stream and returns it unchanged. With a <code>tag</code>, prints <code>TRACE tag: value</code>; without, prints <code>TRACE: value</code>. Like <code>dbg</code> but without source-location capture, so it also works when composed point-free.
""",
            example = "(trace :sum (+ 1 2)) ; stderr: TRACE :sum: 3\n; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/trace.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "trace",
        name = "trace/trace-fn",
        signature = "(trace-fn fn-name f)",
        completion = CompletionInfo(
            tailText = "Returns a function that behaves like f but prints every call with its arguments and its result to...",
            priority = PhelCompletionPriority.DEBUG_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a function that behaves like <code>f</code> but prints every call with its arguments and its result to the standard error stream. Nested calls of traced functions are indented by depth and numbered, so recursion and call chains stay readable. <code>fn-name</code> is the label used in the output.
""",
            example = "(def fib-t (trace-fn \"fib\" fib))\n(fib-t 2)\n; TRACE t1: (fib 2)\n; TRACE t2: | (fib 1)\n; TRACE t2: | =&gt; 1\n; TRACE t1: =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/trace.phel#L39",
                docs = "",
            ),
        ),
    )
)
