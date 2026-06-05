package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreAsyncFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "->closure",
        signature = "(->closure f)",
        completion = CompletionInfo(
            tailText = "Converts a Phel function to a PHP Closure",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Converts a Phel function to a PHP Closure.<br />
  Many PHP libraries (AMPHP, ReactPHP) type-hint <code>\Closure</code> and reject<br />
  Phel's <code>AbstractFn</code> even though it is callable. This bridges the gap.
""",
            example = "(-&gt;closure (fn [x] (* x 2)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L19",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "async",
        signature = "(async & body)",
        completion = CompletionInfo(
            tailText = "Runs body asynchronously in a new fiber",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs body asynchronously in a new fiber. Returns an Amp\Future.<br /><br />
Captures the caller's dynamic bindings and reinstalls them inside<br />
  the fiber, so <code>binding</code>s established outside <code>async</code> are visible to<br />
  <code>body</code> (binding conveyance, as in Clojure's <code>future</code>).
""",
            example = "(async (do-something))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L27",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "await",
        signature = "(await future)",
        completion = CompletionInfo(
            tailText = "Blocks the current fiber until the Future resolves and returns its value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Blocks the current fiber until the Future resolves and returns its value.<br />
  Accepts either a raw <code>Amp\Future</code> or a <code>Future</code> wrapper.
""",
            example = "(await (async (+ 1 2)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L49",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "await-all",
        signature = "(await-all futures)",
        completion = CompletionInfo(
            tailText = "Awaits all Futures in the given collection",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Awaits all Futures in the given collection. Returns a vector of results.<br />
  Accepts a mix of raw <code>Amp\Future</code> and <code>Future</code> wrappers.
""",
            example = "(await-all [(async 1) (async 2)])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L57",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "await-any",
        signature = "(await-any futures)",
        completion = CompletionInfo(
            tailText = "Awaits the first Future to resolve",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Awaits the first Future to resolve. Returns its value.<br />
  Accepts a mix of raw <code>Amp\Future</code> and <code>Future</code> wrappers.
""",
            example = "(await-any [(async 1) (async 2)])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L67",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "deliver",
        signature = "(deliver p value)",
        completion = CompletionInfo(
            tailText = "Delivers value to p if it is still unrealized",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Delivers <code>value</code> to <code>p</code> if it is still unrealized. Returns <code>p</code> on<br />
  first delivery, <code>nil</code> if <code>p</code> was already delivered.
""",
            example = "(deliver (promise) 7)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L151",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "future-call",
        signature = "(future-call f)",
        completion = CompletionInfo(
            tailText = "Invokes f (a zero-arg function) in a new fiber",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Invokes <code>f</code> (a zero-arg function) in a new fiber. Returns a<br />
  <code>PhelFiberFuture</code> you can <code>deref</code> or inspect with <code>future-done?</code><br />
  and <code>future-cancel</code>.<br /><br />
Captures the caller's dynamic bindings and reinstalls them inside<br />
  the fiber before invoking <code>f</code>, so bindings are conveyed the same<br />
  way as with <code>future</code>/<code>async</code>.
""",
            example = "(future-call (fn [] 42))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L159",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "future-cancel",
        signature = "(future-cancel f)",
        completion = CompletionInfo(
            tailText = "Signals the future's internal cancellation token, causing any pending or subsequent deref call to...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Signals the future's internal cancellation token, causing any pending<br />
  or subsequent <code>deref</code> call to raise <code>CancelledException</code> (or return the<br />
  timeout value for the 3-arg form). Due to AMPHP's cooperative fibers,<br />
  the body keeps running until its next cancellation checkpoint; from<br />
  the caller's perspective the future behaves as cancelled after this<br />
  returns.
""",
            example = "(future-cancel (future (expensive-call)))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L96",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "future-cancelled?",
        signature = "(future-cancelled? f)",
        completion = CompletionInfo(
            tailText = "Returns true if future-cancel was called on f, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>future-cancel</code> was called on <code>f</code>, <code>false</code> otherwise.
""",
            example = "(future-cancelled? f)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L108",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "future-done?",
        signature = "(future-done? f)",
        completion = CompletionInfo(
            tailText = "Returns true if the future is in a final state (completed, failed, or cancelled)",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if the future is in a final state (completed, failed,<br />
  or cancelled).<br /><br />
Dispatches on type: for a <code>\Phel\Fiber\Domain\Future</code> (fiber-backed)<br />
  it calls <code>isDone</code>; for any other value it falls back to <code>realized?</code>.<br />
  This lets the same predicate serve both the AMPHP <code>Future</code> wrapper<br />
  and the cooperative fiber future.
""",
            example = "(future-done? f)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L115",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "future-fiber",
        signature = "(future-fiber & body)",
        completion = CompletionInfo(
            tailText = "Runs body in a new fiber via the cooperative scheduler",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runs <code>body</code> in a new fiber via the cooperative scheduler. Returns a<br />
  fiber-future that supports <code>deref</code>, <code>realized?</code>, <code>future-done?</code>, and<br />
  <code>future-cancel</code>. Works at the top level (no enclosing <code>async</code> block<br />
  required), in contrast to <code>future</code> which requires an AMPHP fiber<br />
  context.
""",
            example = "@(future-fiber (+ 1 2))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L175",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "future?",
        signature = "(future? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a fiber-future (from future-call/future-fiber) or a Future (from the AMPHP-b...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a fiber-future (from <code>future-call</code>/<code>future-fiber</code>)<br />
  or a <code>Future</code> (from the AMPHP-backed <code>future</code> macro).
""",
            example = "(future? (future-call (fn [] 1))) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L186",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "pmap",
        signature = "(pmap f & colls)",
        completion = CompletionInfo(
            tailText = "Like map, but applies f to elements concurrently via fibers",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>map</code>, but applies <code>f</code> to elements concurrently via fibers.<br /><br />
Returns a vector of results in the original order. With multiple<br />
  collections, applies <code>f</code> to corresponding elements from each collection,<br />
  stopping when the shortest collection is exhausted.<br /><br />
PHP fibers are cooperative on a single thread, so <code>pmap</code> overlaps<br />
  IO-bound work (HTTP, DB, file IO) but does not parallelize CPU-bound<br />
  computations across cores — unlike <code>clojure.core/pmap</code>, which uses a<br />
  thread pool. ClojureScript and Basilisp follow the same single-threaded<br />
  model.
""",
            example = "(pmap inc [1 2 3]) ; =&gt; [2 3 4]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L76",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "promise",
        signature = "(promise)",
        completion = CompletionInfo(
            tailText = "Returns a new unrealized promise",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a new unrealized promise. Deliver a value with <code>(deliver p v)</code><br />
  and read it back with <code>@p</code> (or <code>(deref p)</code>).<br /><br />
Once delivered the value is frozen; subsequent <code>deliver</code> calls are<br />
  no-ops. <code>deref</code> on an unrealized promise blocks: from inside a fiber<br />
  it suspends cooperatively, from the top level it drains the scheduler<br />
  ready queue and sleeps briefly between checks.
""",
            example = "(let [p (promise)] (deliver p 42) @p) ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/async.phel#L138",
                docs = "",
            ),
        ),
    )
)
