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
            tailText = "Parses s as a canonical UUID string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Parses <code>s</code> as a canonical UUID string. Returns the lower-cased UUID<br />
  string if valid, or nil otherwise. Since PHP has no UUID type, UUIDs<br />
  are returned as strings.
""",
            example = "(parse-uuid \"550E8400-E29B-41D4-A716-446655440000\")\n  ; =&gt; \"550e8400-e29b-41d4-a716-446655440000\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L43",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "random-uuid",
        signature = "(random-uuid)",
        completion = CompletionInfo(
            tailText = "Returns a random UUID v4 string",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "Returns a random UUID v4 string.",
            example = "(random-uuid) ; =&gt; \"550e8400-e29b-41d4-a716-446655440000\"",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L25",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid-nil-value",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L54",
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
            example = "(uuid-nil? \"00000000-0000-0000-0000-000000000000\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L69",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid-regex",
        signature = "",
        completion = CompletionInfo(
            tailText = "",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = "",
            example = null,
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L12",
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
  canonical UUID.
""",
            example = "(uuid-variant \"550e8400-e29b-41d4-a716-446655440000\") ; =&gt; :rfc-4122",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L86",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid-version",
        signature = "(uuid-version x)",
        completion = CompletionInfo(
            tailText = "Returns the version digit (1-5) encoded in UUID x, or nil if x is not a canonical UUID",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns the version digit (1-5) encoded in UUID <code>x</code>, or nil if <code>x</code> is<br />
  not a canonical UUID.
""",
            example = "(uuid-version \"550e8400-e29b-41d4-a716-446655440000\") ; =&gt; 4",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L77",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid=",
        signature = "(uuid= a b)",
        completion = CompletionInfo(
            tailText = "Returns true if a and b are canonical UUID strings that compare equal case-insensitively",
            priority = PhelCompletionPriority.CORE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>a</code> and <code>b</code> are canonical UUID strings that compare<br />
  equal case-insensitively. Returns false if either argument is not a<br />
  valid UUID.
""",
            example = "(uuid= \"550E8400-E29B-41D4-A716-446655440000\"\n         \"550e8400-e29b-41d4-a716-446655440000\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L57",
                docs = "",
            ),
        ),
    ),
    PhelFunction(
        namespace = "core",
        name = "uuid?",
        signature = "(uuid? x)",
        completion = CompletionInfo(
            tailText = "Returns true if x is a canonical UUID string (36 characters, 8-4-4-4-12 hexadecimal groups), fals...",
            priority = PhelCompletionPriority.PREDICATE_FUNCTIONS,
        ),
        documentation = DocumentationInfo(
            summary = """
Returns true if <code>x</code> is a canonical UUID string (36 characters,<br />
  <code>8-4-4-4-12</code> hexadecimal groups), false otherwise. PHP has no UUID<br />
  type, so UUIDs are represented as strings.
""",
            example = "(uuid? \"550e8400-e29b-41d4-a716-446655440000\") ; =&gt; true",
            links = DocumentationLinks(
                github = "https://github.com/phel-lang/phel-lang/blob/main/src/phel/core/uuid.phel#L15",
                docs = "",
            ),
        ),
    )
)
