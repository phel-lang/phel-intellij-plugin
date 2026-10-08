package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreTransducersFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "completing",
        signature = "(completing f)\n(completing f cf)",
        completion = CompletionInfo(
            tailText = "Takes a reducing function f of 2 args and returns a fn suitable for transduce by adding a 1-arity...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Takes a reducing function <code>f</code> of 2 args and returns a fn suitable for transduce by adding a 1-arity (completion) that calls <code>cf</code> (default: identity).
""",
            example = "(transduce (filter even?) (completing conj) [] [1 2 3 4]) ; =&gt; [2 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L107",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "ensure-reduced",
        signature = "(ensure-reduced x)",
        completion = CompletionInfo(
            tailText = "Returns x if it is already reduced, otherwise wraps it in reduced",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>x</code> if it is already reduced, otherwise wraps it in <code>reduced</code>. Never wraps twice.
""",
            example = "(reduced? (ensure-reduced 1)) ; =&gt; true\n(unreduced (ensure-reduced (reduced 1))) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L41",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "halt-when",
        signature = "(halt-when pred)\n(halt-when pred retf)",
        completion = CompletionInfo(
            tailText = "Returns a transducer that ends the transduction at the first input for which pred is truthy",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a transducer that ends the transduction at the first input for which <code>pred</code> is truthy. The whole transduction then returns that input, or <code>(retf (rf result) input)</code> when <code>retf</code> is given. Inputs that do not match pass through unchanged.
""",
            example = "(transduce (halt-when neg?) conj [] [1 2 -1 3]) ; =&gt; -1\n(transduce (halt-when neg? (fn [r x] [r x])) conj [] [1 -1 2]) ; =&gt; [[1] -1]\n(into [] (halt-when neg?) [1 2]) ; =&gt; [1 2]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L142",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reduce",
        signature = "(reduce f coll)\n(reduce f init coll)",
        completion = CompletionInfo(
            tailText = "Reduces collection to a single value by repeatedly applying function to accumulator and elements",
            priority = PhelCompletionPriority.COLLECTION_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reduces collection to a single value by repeatedly applying function to accumulator and elements. Respects early termination via <code>(reduced val)</code>.
""",
            example = "(reduce + [1 2 3 4]) ; =&gt; 10",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L55",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "reduce-kv",
        signature = "(reduce-kv f init coll)",
        completion = CompletionInfo(
            tailText = "Reduces an associative collection by applying f to the accumulator, key and value of each entry, ...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reduces an associative collection by applying <code>f</code> to the accumulator, key and value of each entry, starting with <code>init</code>. Vectors use the index as key. Respects early termination via <code>(reduced val)</code>.
""",
            example = "(reduce-kv (fn [m k v] (assoc m v k)) {} {:a 1 :b 2}) ; =&gt; {1 :a, 2 :b}",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L91",
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
            example = "(reduce (fn [acc x] (if (= x 3) (reduced acc) (+ acc x))) 0 [1 2 3 4]) ; =&gt; 3",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L18",
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
            example = "(reduced? (reduced 1)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L25",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "transduce",
        signature = "(transduce xform f coll)\n(transduce xform f init coll)",
        completion = CompletionInfo(
            tailText = "Reduce with a transformation of f (xf)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Reduce with a transformation of <code>f</code> (xf). If init is not supplied,<br />
  <code>(f)</code> will be called to produce it. <code>f</code> should be a reducing function<br />
  that returns the initial value when called with no arguments. The<br />
  result is passed once to <code>f</code>'s 1-arity (completion); wrap a 2-arity<br />
  reducer in <code>completing</code>.
""",
            example = "(transduce (map inc) + [1 2 3]) ; =&gt; 9",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L118",
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
            example = "(unreduced (reduced 1)) ; =&gt; 1\n(unreduced 1) ; =&gt; 1",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L32",
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
Creates a volatile mutable reference with initial value <code>val</code>. Use for transducer state that needs fast mutation without watches.
""",
            example = "(let [v (volatile! 0)] (vreset! v 5) @v) ; =&gt; 5",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L160",
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
            example = "(volatile? (volatile! 0)) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L181",
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
            example = "(let [v (volatile! 0)] (vreset! v 9)) ; =&gt; 9",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L167",
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
Applies <code>f</code> to the current value of volatile <code>vol</code> plus <code>args</code>, and sets the new value. Returns the new value.
""",
            example = "(let [v (volatile! 10)] (vswap! v + 5)) ; =&gt; 15",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.54.0/src/phel/core/transducers.phel#L174",
                docs = "",
            ),
        ),
    )
)
