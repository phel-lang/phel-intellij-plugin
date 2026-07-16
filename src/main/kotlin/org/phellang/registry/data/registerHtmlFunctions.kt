package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerHtmlFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "html",
        name = "html/doctype",
        signature = "(doctype type)",
        completion = CompletionInfo(
            tailText = "Returns an HTML doctype declaration for the given type",
            priority = PhelCompletionPriority.HTML_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns an HTML doctype declaration for the given type. Supports <code>:html4</code>,<br />
  <code>:xhtml-strict</code>, <code>:xhtml-transitional</code>, and <code>:html5</code>. Defaults to <code>:html5</code>.
""",
            example = "(doctype :html5) ; =&gt; \"&lt;!DOCTYPE html&gt;\\n\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/html.phel#L175",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/html.phel#L6",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "html",
        name = "html/html",
        signature = "(html & content)",
        completion = CompletionInfo(
            tailText = "Compiles nested Phel vectors to HTML strings at compile-time",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Compiles nested Phel vectors to HTML strings at compile-time. A vector<br />
  <code>[tag attrs? children*]</code> becomes an element; <code>tag</code> is a keyword or string,<br />
  the optional <code>attrs</code> is a map, and children are strings, keywords, raw<br />
  strings, or nested vectors. Special list forms <code>for</code>, <code>if</code>, <code>when</code>, and<br />
  <code>when-not</code> are supported for dynamic content.
""",
            example = "(html [:div \"Hello\"]) ; =&gt; \"&lt;div&gt;Hello&lt;/div&gt;\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/html.phel#L155",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/html.phel#L4",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/html.phel#L4",
                docs = "",
            ),
        ),
    )
)
