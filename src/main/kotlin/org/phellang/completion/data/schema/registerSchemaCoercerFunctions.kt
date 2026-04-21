package org.phellang.completion.data.schema

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerSchemaCoercerFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "schema\\coercer",
        name = "schema\\coercer/coerce",
        signature = "(coerce schema value)",
        completion = CompletionInfo(
            tailText = "Walks schema and returns value with string-typed inputs coerced into their schema-required types",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Walks <code>schema</code> and returns <code>value</code> with string-typed inputs coerced<br />
  into their schema-required types. Already-typed values pass through.<br />
  Unknown or untranslatable values are returned unchanged so validation<br />
  can report the real failure.
""",
            example = "(coerce :int \"42\") ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/schema/coercer.phel#L200",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema\\coercer",
        name = "schema\\coercer/conform",
        signature = "(conform schema value)",
        completion = CompletionInfo(
            tailText = "Coerces value against schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Coerces <code>value</code> against <code>schema</code>. Returns the coerced value if it<br />
  then validates, otherwise the invalid marker <code>:phel.schema/invalid</code>.
""",
            example = "(conform :int \"42\") ; =&gt; 42",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/schema/coercer.phel#L213",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema\\coercer",
        name = "schema\\coercer/invalid-marker",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/schema/coercer.phel#L16",
                docs = "",
            ),
        ),
    )
)
