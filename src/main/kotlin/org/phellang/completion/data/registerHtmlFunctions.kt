package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerHtmlFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "html",
        name = "html/doctype",
        signature = "(doctype type)",
        completion = CompletionInfo(
            tailText = "Returns an HTML doctype declaration",
            priority = PhelCompletionPriority.HTML_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns an HTML doctype declaration.",
            example = "(doctype :html5) ; =&gt; \"&lt;!DOCTYPE html&gt;\\n\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L170",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "html",
        name = "html/escape-html",
        signature = "(escape-html s)",
        completion = CompletionInfo(
            tailText = "Escapes HTML special characters to prevent XSS",
            priority = PhelCompletionPriority.HTML_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Escapes HTML special characters to prevent XSS.",
            example = "(escape-html \"&lt;div&gt;\") ; =&gt; \"&amp;lt;div&amp;gt;\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L6",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "html",
        name = "html/html",
        signature = "(html & content)",
        completion = CompletionInfo(
            tailText = "Compiles Phel vectors to HTML strings",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = "Compiles Phel vectors to HTML strings.",
            example = "(html [:div \"Hello\"]) ; =&gt; \"&lt;div&gt;Hello&lt;/div&gt;\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L155",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "html",
        name = "html/raw-string",
        signature = "(raw-string s)",
        completion = CompletionInfo(
            tailText = "Creates a new raw-string struct",
            priority = PhelCompletionPriority.HTML_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Creates a new raw-string struct.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L4",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "html",
        name = "html/raw-string?",
        signature = "(raw-string? x)",
        completion = CompletionInfo(
            tailText = "Checks if x is an instance of the raw-string struct",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Checks if <code>x</code> is an instance of the raw-string struct.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L4",
                docs = "",
            ),
        ),
    )
)
