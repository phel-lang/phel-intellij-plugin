package org.phellang.registry

/**
 * PHP's superglobals, reachable from Phel as `php/$_SERVER` and friends.
 *
 * Hand-curated rather than generated: `updatePhpRegistry` derives [PhpNativeFunctions] from
 * php/doc-en's `<methodsynopsis>` entries, and a superglobal is a *variable*, so it appears in no
 * function synopsis and no generator can find it. The set is fixed by the PHP language itself and
 * has not changed in years, so a static list carries no drift risk.
 *
 * Phel v0.50.0 added completion and hover for these to its own LSP and REPL (#3037); this is the
 * IntelliJ side of the same surface. They are values, not calls, so each carries an empty
 * signature — the same shape the generator emits for any non-callable entry.
 */
internal fun phpSuperglobals(): List<PhelFunction> = listOf(
    superglobal("\$GLOBALS", "References all variables available in global scope"),
    superglobal("\$_SERVER", "Server and execution environment information"),
    superglobal("\$_GET", "HTTP GET variables"),
    superglobal("\$_POST", "HTTP POST variables"),
    superglobal("\$_FILES", "HTTP file upload variables"),
    superglobal("\$_COOKIE", "HTTP cookies"),
    superglobal("\$_SESSION", "Session variables"),
    superglobal("\$_REQUEST", "HTTP request variables"),
    superglobal("\$_ENV", "Environment variables"),
)

private fun superglobal(name: String, summary: String): PhelFunction = PhelFunction(
    namespace = "php",
    name = "php/$name",
    signature = "",
    completion = CompletionInfo(tailText = summary, priority = PhelCompletionPriority.PHP_INTEROP),
    documentation = DocumentationInfo(
        summary = summary,
        links = DocumentationLinks(docs = "https://www.php.net/manual/en/reserved.variables.php"),
    ),
)
