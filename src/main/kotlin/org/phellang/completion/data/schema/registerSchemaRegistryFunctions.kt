package org.phellang.completion.data.schema

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerSchemaRegistryFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "schema.registry",
        name = "schema.registry/clear!",
        signature = "(clear!)",
        completion = CompletionInfo(
            tailText = "Removes every user-registered schema",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes every user-registered schema. Returns <code>nil</code>.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/registry.phel#L48",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.registry",
        name = "schema.registry/deref-ref",
        signature = "(deref-ref name)",
        completion = CompletionInfo(
            tailText = "Returns the schema registered under name, or nil if no schema is registered with that name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the schema registered under <code>name</code>, or <code>nil</code> if no schema is registered with that name.
""",
            example = "(deref-ref :email)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/registry.phel#L29",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.registry",
        name = "schema.registry/register!",
        signature = "(register! name schema)",
        completion = CompletionInfo(
            tailText = "Registers schema under name (usually a keyword)",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Registers <code>schema</code> under <code>name</code> (usually a keyword). Overwrites any previous entry. Returns the registered schema.
""",
            example = "(register! :email [:and :string [:re #\"@\"]])",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/registry.phel#L13",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.registry",
        name = "schema.registry/registered?",
        signature = "(registered? name)",
        completion = CompletionInfo(
            tailText = "Returns true if a schema is registered under name",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns <code>true</code> if a schema is registered under <code>name</code>.
""",
            example = "(registered? :email) ; =&gt; true/false",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/registry.phel#L36",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.registry",
        name = "schema.registry/registry-snapshot",
        signature = "(registry-snapshot)",
        completion = CompletionInfo(
            tailText = "Returns the current registry as a plain map",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the current registry as a plain map. Intended for inspection and testing.
""",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/registry.phel#L43",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "schema.registry",
        name = "schema.registry/unregister!",
        signature = "(unregister! name)",
        completion = CompletionInfo(
            tailText = "Removes the schema bound to name",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Removes the schema bound to <code>name</code>. Returns <code>nil</code>.
""",
            example = "(unregister! :email)",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.42.0/src/phel/schema/registry.phel#L21",
                docs = "",
            ),
        ),
    )
)
