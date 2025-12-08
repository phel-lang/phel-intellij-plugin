package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerHtmlFunctions(): List<DataFunction> = listOf(
    DataFunction(
        namespace = "html",
        name = "html/doctype",
        doc = """Returns an HTML doctype declaration.""",
        signature = "(doctype type)",
        description = """Returns an HTML doctype declaration""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L170",
        docUrl = "",
        meta = FunctionMeta(
            example = "(doctype :html5) ; => \"<!DOCTYPE html>\n\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.HTML_FUNCTIONS,
    ),
    DataFunction(
        namespace = "html",
        name = "html/escape-html",
        doc = """Escapes HTML special characters to prevent XSS.""",
        signature = "(escape-html s)",
        description = """Escapes HTML special characters to prevent XSS""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L6",
        docUrl = "",
        meta = FunctionMeta(
            example = "(escape-html \"<div>\") ; => \"&lt;div&gt;\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.HTML_FUNCTIONS,
    ),
    DataFunction(
        namespace = "html",
        name = "html/html",
        doc = """Compiles Phel vectors to HTML strings.""",
        signature = "(html & content)",
        description = """Compiles Phel vectors to HTML strings""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L155",
        docUrl = "",
        meta = FunctionMeta(
            example = "(html [:div \"Hello\"]) ; => \"<div>Hello</div>\"",
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.HTML_FUNCTIONS,
    ),
    DataFunction(
        namespace = "html",
        name = "html/raw-string",
        doc = """Creates a new raw-string struct.""",
        signature = "(raw-string s)",
        description = """Creates a new raw-string struct""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L4",
        docUrl = "",
        meta = FunctionMeta(
            example = null,
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.HTML_FUNCTIONS,
    ),
    DataFunction(
        namespace = "html",
        name = "html/raw-string?",
        doc = """Checks if <b>x</b> is an instance of the raw-string struct.""",
        signature = "(raw-string? x)",
        description = """Checks if x is an instance of the raw-string struct""",
        githubUrl = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/html.phel#L4",
        docUrl = "",
        meta = FunctionMeta(
            example = null,
            deprecatedVersion = null,
            supersededBy = null,
        ),
        priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
    ),
)
