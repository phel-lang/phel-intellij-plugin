package org.phellang.registry.data.schema

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerSchemaExplainerFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "schema.explainer",
        name = "schema.explainer/explain",
        signature = "(explain schema value)",
        completion = CompletionInfo(
            tailText = "Returns nil if value conforms to schema, otherwise a map describing the violations",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>nil</code> if <code>value</code> conforms to <code>schema</code>, otherwise a map describing the violations.
""",
            example = "(explain :int :oops)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/schema/explainer.phel#L234",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.explainer",
        name = "schema.explainer/human-readable-explain",
        signature = "(human-readable-explain result)",
        completion = CompletionInfo(
            tailText = "Renders an explain result as a multi-line human string suitable for REPL or CI output",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Renders an explain result as a multi-line human string suitable for REPL or CI output. Returns <code>nil</code> for a <code>nil</code> result.
""",
            example = "(human-readable-explain (explain :int :oops))",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.48.0/src/phel/schema/explainer.phel#L254",
                docs = "",
            ),
        ),
    )
)
