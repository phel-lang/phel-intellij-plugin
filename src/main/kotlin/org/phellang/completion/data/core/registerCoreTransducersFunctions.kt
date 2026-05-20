package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreTransducersFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "completing",
        signature = "(completing f & args)",
        completion = CompletionInfo(
            tailText = "Takes a reducing function f of 2 args and returns a fn suitable for transduce by adding a 1-arity...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a reducing function <code>f</code> of 2 args and returns a fn suitable for transduce<br />
  by adding a 1-arity (completion) that calls <code>cf</code> (default: identity).
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L74",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reduce",
        signature = "(reduce f & args)",
        completion = CompletionInfo(
            tailText = "Reduces collection to a single value by repeatedly applying function to accumulator and elements",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reduces collection to a single value by repeatedly applying function to accumulator and elements.<br />
  Respects early termination via <code>(reduced val)</code>.
""",
            example = "(reduce + [1 2 3 4]) ; =&gt; 10",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L50",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reduced",
        signature = "(reduced x)",
        completion = CompletionInfo(
            tailText = "Wraps x in a Reduced, signaling early termination from reduce/transduce",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Wraps <code>x</code> in a Reduced, signaling early termination from reduce/transduce.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L18",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reduced?",
        signature = "(reduced? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Reduced value",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a Reduced value.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "transduce",
        signature = "(transduce xform f & args)",
        completion = CompletionInfo(
            tailText = "Reduce with a transformation of f (xf)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reduce with a transformation of <code>f</code> (xf). If init is not supplied,<br />
  <code>(f)</code> will be called to produce it. <code>f</code> should be a reducing function<br />
  that returns the initial value when called with no arguments.
""",
            example = "(transduce (map inc) + [1 2 3]) ; =&gt; 9",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L86",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "unreduced",
        signature = "(unreduced x)",
        completion = CompletionInfo(
            tailText = "If x is Reduced, returns the unwrapped value; otherwise returns x",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
If <code>x</code> is Reduced, returns the unwrapped value; otherwise returns <code>x</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "volatile!",
        signature = "(volatile! val)",
        completion = CompletionInfo(
            tailText = "Creates a volatile mutable reference with initial value val",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Creates a volatile mutable reference with initial value <code>val</code>.<br />
  Use for transducer state that needs fast mutation without watches.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L103",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "volatile?",
        signature = "(volatile? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Volatile",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a Volatile.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L123",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vreset!",
        signature = "(vreset! vol val)",
        completion = CompletionInfo(
            tailText = "Sets the value of volatile vol to val",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sets the value of volatile <code>vol</code> to <code>val</code>. Returns <code>val</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L110",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vswap!",
        signature = "(vswap! vol f & args)",
        completion = CompletionInfo(
            tailText = "Applies f to the current value of volatile vol plus args, and sets the new value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Applies <code>f</code> to the current value of volatile <code>vol</code> plus <code>args</code>,<br />
  and sets the new value. Returns the new value.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/transducers.phel#L116",
                docs = "",
            ),
        ),
    )
)
