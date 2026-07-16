package org.phellang.registry.data

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerReaderFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "reader",
        name = "reader/register-tag",
        signature = "(register-tag tag-name f)",
        completion = CompletionInfo(
            tailText = "Registers a handler for reader tag tag-name (a string without the leading #)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers a handler for reader tag <code>tag-name</code> (a string without the leading <code>#</code>). The handler <code>f</code> is a 1-arg function receiving the already-read form value and returning the value that should replace the tagged literal.<br /><br />
Re-registering an existing tag overwrites the previous handler.
""",
            example = "(register-tag \"date\" my-ns/parse-date)\n  ; later: #date \"2026-04-20\" =&gt; (my-ns/parse-date \"2026-04-20\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/reader.phel#L19",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reader",
        name = "reader/registered-tags",
        signature = "(registered-tags)",
        completion = CompletionInfo(
            tailText = "Returns a sorted vector of the currently registered reader tag names",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a sorted vector of the currently registered reader tag names.",
            example = "(registered-tags) ; =&gt; [\"inst\" \"regex\" \"uuid\"]",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/reader.phel#L45",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reader",
        name = "reader/tag-registered?",
        signature = "(tag-registered? tag-name)",
        completion = CompletionInfo(
            tailText = "Returns true if a handler is registered for reader tag tag-name",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if a handler is registered for reader tag <code>tag-name</code>.
""",
            example = "(tag-registered? \"inst\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/reader.phel#L30",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "reader",
        name = "reader/unregister-tag",
        signature = "(unregister-tag tag-name)",
        completion = CompletionInfo(
            tailText = "Removes any handler registered for reader tag tag-name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes any handler registered for reader tag <code>tag-name</code>. Built-in tags can be unregistered but are re-installed on the next reader bootstrap.
""",
            example = "(unregister-tag \"date\")",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/reader.phel#L37",
                docs = "",
            ),
        ),
    )
)
