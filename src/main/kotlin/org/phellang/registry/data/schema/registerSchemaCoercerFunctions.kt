package org.phellang.registry.data.schema

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerSchemaCoercerFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "schema.coercer",
        name = "schema.coercer/coerce",
        signature = "(coerce schema value)",
        completion = CompletionInfo(
            tailText = "Walks schema and returns value with string-typed inputs coerced into their schema-required types",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Walks <code>schema</code> and returns <code>value</code> with string-typed inputs coerced into their schema-required types. Already-typed values pass through. Unknown or untranslatable values are returned unchanged so validation can report the real failure.
""",
            example = "(coerce :int \"42\") ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/schema/coercer.phel#L221",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.coercer",
        name = "schema.coercer/conform",
        signature = "(conform schema value)",
        completion = CompletionInfo(
            tailText = "Coerces value against schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>value</code> against <code>schema</code>. Returns the coerced value if it then validates, otherwise the invalid marker <code>:phel.schema/invalid</code>.
""",
            example = "(conform :int \"42\") ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/schema/coercer.phel#L231",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.coercer",
        name = "schema.coercer/invalid-marker",
        signature = "",
        completion = CompletionInfo(
            tailText = "Sentinel keyword returned by coercion when a value cannot be coerced to the target schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Sentinel keyword returned by coercion when a value cannot be coerced to the target schema. Re-exported as <code>phel.schema/invalid-marker</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.49.0/src/phel/schema/coercer.phel#L15",
                docs = "",
            ),
        ),
    )
)
