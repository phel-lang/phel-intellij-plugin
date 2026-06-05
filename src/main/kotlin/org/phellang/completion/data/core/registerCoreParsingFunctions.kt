package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreParsingFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "parse-boolean",
        signature = "(parse-boolean s)",
        completion = CompletionInfo(
            tailText = "Parses a string as a boolean",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Parses a string as a boolean. Returns <code>true</code> for the exact string<br />
  <code>"true"</code>, <code>false</code> for <code>"false"</code>, and <code>nil</code> for any other input.<br />
  The match is case-sensitive and does not tolerate surrounding<br />
  whitespace.
""",
            example = "(parse-boolean \"true\") ; =&gt; true\n(parse-boolean \"True\") ; =&gt; nil",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/parsing.phel#L36",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "parse-double",
        signature = "(parse-double s)",
        completion = CompletionInfo(
            tailText = "Parses a string as a float",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Parses a string as a float. Returns <code>nil</code> for non-numeric input or for<br />
  inputs that are not strings. Accepts <code>Infinity</code>, <code>-Infinity</code>, and <code>NaN</code><br />
  alongside regular decimal and scientific notation.
""",
            example = "(parse-double \"3.14\") ; =&gt; 3.14\n(parse-double \"Infinity\") ; =&gt; INF",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/parsing.phel#L20",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "parse-long",
        signature = "(parse-long s)",
        completion = CompletionInfo(
            tailText = "Parses a string as an integer",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Parses a string as an integer. Returns nil if parsing fails.",
            example = "(parse-long \"123\") ; =&gt; 123",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/parsing.phel#L10",
                docs = "",
            ),
        ),
    )
)
