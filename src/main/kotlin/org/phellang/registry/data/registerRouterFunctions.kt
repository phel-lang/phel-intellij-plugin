package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerRouterFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "router",
        name = "router/CompiledSymfonyRouter",
        signature = "(CompiledSymfonyRouter normalized-routes matcher generator indexed-routes)",
        completion = CompletionInfo(
            tailText = "Creates a new CompiledSymfonyRouter struct",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new CompiledSymfonyRouter struct.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L238",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/CompiledSymfonyRouter?",
        signature = "(CompiledSymfonyRouter? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is an instance of the CompiledSymfonyRouter struct",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is an instance of the CompiledSymfonyRouter struct.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L238",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/SymfonyRouter",
        signature = "(SymfonyRouter normalized-routes route-collection matcher generator)",
        completion = CompletionInfo(
            tailText = "Creates a new SymfonyRouter struct",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new SymfonyRouter struct.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L197",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/SymfonyRouter?",
        signature = "(SymfonyRouter? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is an instance of the SymfonyRouter struct",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is an instance of the SymfonyRouter struct.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L197",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/build-compiled-router",
        signature = "(build-compiled-router raw-routes options compiled-matcher-routes compiled-generator-routes)",
        completion = CompletionInfo(
            tailText = "Runtime constructor for compiled-router expansions",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Runtime constructor for <code>compiled-router</code> expansions. Builds the normalized<br />
   and indexed route tables (which may carry <code>:handler</code> functions) from<br />
   <code>raw-routes</code> at runtime, pairing them with the Symfony matcher/generator<br />
   tables precompiled at macro-expansion time. Internal — call <code>compiled-router</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L248",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/compiled-router",
        signature = "(compiled-router raw-routes & [options])",
        completion = CompletionInfo(
            tailText = "Like router but performs Symfony's route compilation at macro-expansion time",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>router</code> but performs Symfony's route compilation at macro-expansion time. The resulting router uses Symfony's <code>CompiledUrlMatcher</code>/<code>Generator</code> which are ~3x faster than their dynamic counterparts for large route tables.<br /><br />
Because compilation runs during macro expansion, <code>raw-routes</code> must be a literal vector at the call site — it cannot be built from runtime values. Use <code>router</code> if your routes are dynamic.
""",
            example = "(compiled-router [[\"/ping\" {:get {:handler pong}}]])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L266",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/flatten-routes",
        signature = "(flatten-routes raw-routes path-prefix common-data)",
        completion = CompletionInfo(
            tailText = "Flattens nested routes to a vector of [path data] tuples",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Flattens nested routes to a vector of <code>[path data]</code> tuples.<br /><br />
Parent paths are concatenated with their children's paths and parent data<br />
  maps are deep-merged into their children's data. <code>path-prefix</code> and<br />
  <code>common-data</code> are merged into every route and let you mount a subtree under<br />
  a prefix or share data across all routes.
""",
            example = "(flatten-routes [\"/api\" {:middleware [:auth]}\n                 [\"/ping\" {:handler :ping}]] \"\" {})\n; =&gt; [[\"/api/ping\" {:middleware [:auth] :handler :ping}]]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L51",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/generate",
        signature = "(generate this route-name parameter)",
        completion = CompletionInfo(
            tailText = "Generate a url for a route",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Generate a url for a route",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L137",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/handler",
        signature = "(handler router & [options])",
        completion = CompletionInfo(
            tailText = "Builds a request -> response function from a router",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Builds a <code>request -> response</code> function from a router.<br /><br />
Matching flow per request:<br /><br />
1. Resolve the request's path via <code>match-by-path</code>.<br />
  2. Look up the handler for the request method in the precompiled dispatch<br />
     table, falling back to the route's method-agnostic <code>:handler</code>.<br />
  3. Invoke the handler with the request enriched with <code>[:attributes :match]</code><br />
     and <code>[:attributes :route-data]</code>. A <code>nil</code> response triggers the<br />
     <code>:not-acceptable</code> handler.<br /><br />
Options:<br /><br />
| key                   | description |<br />
  | ----------------------|-------------|<br />
  | <code>:middleware</code>         | global middleware applied to every matched route<br />
  | <code>:default-handler</code>    | fallback used for any 404/405/406 case not covered by a specific override<br />
  | <code>:not-found</code>          | handler invoked when no route matches (HTTP 404)<br />
  | <code>:method-not-allowed</code> | handler invoked when the path matches but no handler exists for the request method (HTTP 405)<br />
  | <code>:not-acceptable</code>     | handler invoked when a matched handler returns <code>nil</code> (HTTP 406)<br /><br />
Dispatch is precompiled at handler construction time, so per-request work<br />
  is reduced to two hash-map lookups.
""",
            example = "(handler (router [[\"/ping\" {:get {:handler pong}}]])\n         {:middleware [logging-mw]\n          :not-found  (fn [_] {:status 404 :body \"nope\"})})",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L283",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/match-by-name",
        signature = "(match-by-name this route-name)",
        completion = CompletionInfo(
            tailText = "Matches a route given a route name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Matches a route given a route name. Returns nil if route can't be found.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L137",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/match-by-path",
        signature = "(match-by-path this path)",
        completion = CompletionInfo(
            tailText = "Matches a route given a path",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Matches a route given a path. Returns nil if path doesn't match.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L137",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/router",
        signature = "(router raw-routes & [options])",
        completion = CompletionInfo(
            tailText = "Builds a dynamic Router from a nested route tree",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Builds a dynamic <code>Router</code> from a nested route tree.<br /><br />
Routes are described as <code>[path data children*]</code> where <code>data</code> is an optional<br />
  hash-map. Children inherit their parent's path prefix and have parent data<br />
  deep-merged into their own. <code>data</code> may contain:<br /><br />
- <code>:handler</code> — a 1-arg <code>request -> response</code> function (matches any method)<br />
  - <code>:middleware</code> — a vector of <code>(fn [handler request])</code> wrappers<br />
  - <code>:name</code> — a keyword used for URL generation via <code>match-by-name</code> / <code>generate</code><br />
  - method keys (<code>:get</code>, <code>:post</code>, …) — nested maps with their own <code>:handler</code><br />
    and <code>:middleware</code> that apply only for that HTTP method<br /><br />
<code>options</code> accepts <code>:path</code> (prefix prepended to every route) and <code>:data</code><br />
  (merged into every route's data).
""",
            example = "(router [[\"/ping\" {:get {:handler pong}}]])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L213",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "router",
        name = "router/routes",
        signature = "(routes this)",
        completion = CompletionInfo(
            tailText = "Returns all registered routes as a vector of [path data] tuples",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns all registered routes as a vector of [path data] tuples.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/router.phel#L137",
                docs = "",
            ),
        ),
    )
)
