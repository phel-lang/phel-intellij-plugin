package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreMetaFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "meta",
        signature = "",
        completion = CompletionInfo(
            tailText = "Gets the metadata attached to a value",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Gets the metadata attached to a value.<br />
  For a quoted symbol (<code>(meta 'foo)</code>) the definition metadata registered via <code>def</code> is returned.<br />
  For any other expression the value is looked up at runtime and its <code>MetaInterface</code> metadata returned.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/meta.phel#L23",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "set-meta!",
        signature = "",
        completion = CompletionInfo(
            tailText = "Sets the metadata to a given object",
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Sets the metadata to a given object.",
            example = null,
            deprecation = DeprecationInfo(version = "0.32.0", replacement = "with-meta"),
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/meta.phel#L70",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vary-meta",
        signature = "",
        completion = CompletionInfo(
            tailText = "Returns an object with (apply f (meta obj) args) as its new metadata",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns an object with (apply f (meta obj) args) as its new metadata.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/meta.phel#L77",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "with-meta",
        signature = "",
        completion = CompletionInfo(
            tailText = "Returns obj with the given metadata meta attached",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>obj</code> with the given metadata <code>meta</code> attached.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.39.0/src/phel/core/meta.phel#L64",
                docs = "",
            ),
        ),
    )
)
