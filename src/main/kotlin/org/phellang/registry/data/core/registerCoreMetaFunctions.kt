package org.phellang.registry.data.core

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerCoreMetaFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "meta",
        signature = "(meta obj)",
        completion = CompletionInfo(
            tailText = "Gets the metadata attached to a value",
            priority = PhelCompletionPriority.MACROS,
        ),
        documentation = DocumentationInfo(
            summary = """
Gets the metadata attached to a value. For a quoted symbol (<code>(meta 'foo)</code>) the definition metadata registered via <code>def</code> is returned. For any other expression the value is looked up at runtime and its <code>MetaInterface</code> metadata returned.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/meta.phel#L23",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "vary-meta",
        signature = "(vary-meta obj f & args)",
        completion = CompletionInfo(
            tailText = "Returns an object with (apply f (meta obj) args) as its new metadata",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns an object with (apply f (meta obj) args) as its new metadata.",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/meta.phel#L70",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "with-meta",
        signature = "(with-meta obj meta)",
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
                github = "https://github.com/phel-lang/phel-lang/blob/v0.50.0/src/phel/core/meta.phel#L64",
                docs = "",
            ),
        ),
    )
)
