package org.phellang.registry.data.schema

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.DocumentationLinks
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.PhelFunction

import org.phellang.registry.PhelCompletionPriority

internal fun registerSchemaValidatorFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/map-entry-optional?",
        signature = "(map-entry-optional? entry)",
        completion = CompletionInfo(
            tailText = "Returns true when a [:map",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> when a <code>[:map ...]</code> entry is declared with<br />
  <code>{:optional true}</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L177",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/map-entry-options",
        signature = "(map-entry-options entry)",
        completion = CompletionInfo(
            tailText = "Options map for a [:map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Options map for a <code>[:map ...]</code> entry <code>[key opts? schema]</code>. Returns an empty map when the entry has no options position.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L163",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/map-entry-schema",
        signature = "(map-entry-schema entry)",
        completion = CompletionInfo(
            tailText = "Inner schema of a [:map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Inner schema of a <code>[:map ...]</code> entry, skipping the options map when present.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L170",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/resolve-or-default",
        signature = "(resolve-or-default head f default)",
        completion = CompletionInfo(
            tailText = "Like resolve-or-throw, but returns default when head is not registered instead of throwing",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Like <code>resolve-or-throw</code>, but returns <code>default</code> when <code>head</code> is not registered instead of throwing. Used by <code>coerce</code> where unknown heads are intentionally passed through unchanged for downstream validation to flag.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L38",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/resolve-or-throw",
        signature = "(resolve-or-throw label head f)",
        completion = CompletionInfo(
            tailText = "Resolves an unknown head by looking it up in the registry",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Resolves an unknown head by looking it up in the registry. When<br />
  <code>head</code> names a registered schema, invokes <code>f</code> with the registered<br />
  schema; otherwise throws an <code>InvalidArgumentException</code> mentioning<br />
  <code>label</code> (e.g. <code>"unknown schema kind"</code> or <code>"no generator for schema kind"</code>).<br /><br />
Every head-dispatching caller uses this so the error shape is<br />
  consistent across validator/explainer/coercer/generator.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L24",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/schema-args",
        signature = "(schema-args schema)",
        completion = CompletionInfo(
            tailText = "Returns the positional arguments of schema (children past the head and optional options map)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the positional arguments of <code>schema</code> (children past the head and optional options map).
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L65",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/schema-head",
        signature = "(schema-head schema)",
        completion = CompletionInfo(
            tailText = "Returns the head (kind keyword) of schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the head (kind keyword) of <code>schema</code>. Keyword schemas are their own head; vector schemas use their first element.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L49",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/schema-options",
        signature = "(schema-options schema)",
        completion = CompletionInfo(
            tailText = "Returns the options map of schema or {} if none",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the options map of <code>schema</code> or <code>{}</code> if none.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L74",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.validator",
        name = "schema.validator/valid?",
        signature = "(valid? schema value)",
        completion = CompletionInfo(
            tailText = "Returns true if value conforms to schema, otherwise false",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if <code>value</code> conforms to <code>schema</code>, otherwise <code>false</code>.
""",
            example = "(valid? :int 1) ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.47.0/src/phel/schema/validator.phel#L280",
                docs = "",
            ),
        ),
    )
)
