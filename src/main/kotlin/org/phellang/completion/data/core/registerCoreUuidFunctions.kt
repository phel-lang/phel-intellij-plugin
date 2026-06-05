package org.phellang.completion.data.core

import org.phellang.completion.data.CompletionInfo
import org.phellang.completion.data.DocumentationInfo
import org.phellang.completion.data.DocumentationLinks
import org.phellang.completion.data.DeprecationInfo
import org.phellang.completion.data.PhelFunction

import org.phellang.completion.infrastructure.PhelCompletionPriority

internal fun registerCoreUuidFunctions(): List<PhelFunction> = listOf(
    PhelFunction(
        namespace = "core",
        name = "parse-uuid",
        signature = "(parse-uuid s)",
        completion = CompletionInfo(
            tailText = "Parses s as a canonical UUID string and returns a Phel\\Lang\\UUID value, or nil if s is not a vali...",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Parses <code>s</code> as a canonical UUID string and returns a <code>Phel\Lang\UUID</code><br />
  value, or nil if <code>s</code> is not a valid canonical UUID. Already-typed<br />
  <code>UUID</code> values pass through unchanged.
""",
            example = "(parse-uuid \"550E8400-E29B-41D4-A716-446655440000\")\n  ; =&gt; #uuid \"550e8400-e29b-41d4-a716-446655440000\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/uuid.phel#L29",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "random-uuid",
        signature = "(random-uuid)",
        completion = CompletionInfo(
            tailText = "Returns a random version 4 UUID as a Phel\\Lang\\UUID value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a random version 4 UUID as a <code>Phel\Lang\UUID</code> value.
""",
            example = "(random-uuid) ; =&gt; #uuid \"550e8400-e29b-41d4-a716-446655440000\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/uuid.phel#L22",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid-nil?",
        signature = "(uuid-nil? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is the nil UUID (all zeros), false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is the nil UUID (all zeros), false otherwise.
""",
            example = "(uuid-nil? #uuid \"00000000-0000-0000-0000-000000000000\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/uuid.phel#L54",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid-variant",
        signature = "(uuid-variant x)",
        completion = CompletionInfo(
            tailText = "Returns a keyword describing the variant field of UUID x: :ncs, :rfc-4122, :microsoft, or :reserved",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns a keyword describing the variant field of UUID <code>x</code>: <code>:ncs</code>,<br />
  <code>:rfc-4122</code>, <code>:microsoft</code>, or <code>:reserved</code>. Returns nil if <code>x</code> is not a<br />
  <code>UUID</code> value.
""",
            example = "(uuid-variant #uuid \"550e8400-e29b-41d4-a716-446655440000\") ; =&gt; :rfc-4122",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/uuid.phel#L71",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid-version",
        signature = "(uuid-version x)",
        completion = CompletionInfo(
            tailText = "Returns the version digit (1-5) encoded in UUID x, or nil if x is not a UUID value",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the version digit (1-5) encoded in UUID <code>x</code>, or nil if <code>x</code> is<br />
  not a <code>UUID</code> value.
""",
            example = "(uuid-version #uuid \"550e8400-e29b-41d4-a716-446655440000\") ; =&gt; 4",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/uuid.phel#L62",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid=",
        signature = "(uuid= a b)",
        completion = CompletionInfo(
            tailText = "Returns true if a and b are Phel\\Lang\\UUID values with the same canonical form",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>a</code> and <code>b</code> are <code>Phel\Lang\UUID</code> values with the<br />
  same canonical form. Strings (or anything else) are rejected even if<br />
  their textual content would match — coerce with <code>parse-uuid</code> first.
""",
            example = "(uuid= #uuid \"550e8400-e29b-41d4-a716-446655440000\"\n         #uuid \"550E8400-E29B-41D4-A716-446655440000\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/uuid.phel#L44",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid?",
        signature = "(uuid? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a Phel\\Lang\\UUID value, false otherwise",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a <code>Phel\Lang\UUID</code> value, false otherwise.<br />
  Canonical UUID strings are rejected — wrap them with <code>parse-uuid</code> (or<br />
  the <code>#uuid "..."</code> literal) to obtain a typed value.
""",
            example = "(uuid? #uuid \"550e8400-e29b-41d4-a716-446655440000\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/v0.41.0/src/phel/core/uuid.phel#L13",
                docs = "",
            ),
        ),
    )
)
