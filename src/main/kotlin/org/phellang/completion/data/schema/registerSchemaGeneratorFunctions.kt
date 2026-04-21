package org.phellang.completion.data.schema

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerSchemaGeneratorFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "schema\\generator",
        name = "schema\\generator/generate",
        signature = "(generate schema)",
        completion = CompletionInfo(
            tailText = "Generates one random value conforming to schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Generates one random value conforming to <code>schema</code>. Accepts the same<br />
  <code>:size</code> and <code>:seed</code> options as <code>phel\test\gen/generate</code>.
""",
            example = "(generate :int)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/schema/generator.phel#L186",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema\\generator",
        name = "schema\\generator/schema->gen",
        signature = "(schema->gen schema)",
        completion = CompletionInfo(
            tailText = "Returns a phel\\test\\gen generator for schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a <code>phel\test\gen</code> generator for <code>schema</code>. Honours a<br />
  <code>{:gen <gen>}</code> override in the schema options when present.
""",
            example = "(schema-&gt;gen [:int])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/schema/generator.phel#L169",
                docs = "",
            ),
        ),
    )
)
