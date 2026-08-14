package org.phellang.fixtures

import org.phellang.registry.CompletionInfo
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelFunction

/**
 * In-memory deprecated functions for the deprecation feature's tests.
 *
 * Phel v0.50.0 removed every deprecated function from the language, so `api.json` — and therefore
 * the generated registry — no longer contains a single entry with deprecation metadata. The
 * feature itself (inspection, annotator rule, quick-fix, completion priority) is unchanged and
 * still ships; only its test data disappeared.
 *
 * These fixtures reproduce the entries as they were generated for Phel v0.49.0, so the tests keep
 * exercising the real shapes the code has to handle:
 *
 *  - a plain `version` + `replacement` pair ([PUT] and friends), and
 *  - the `version = "Use phel\ns\fn"` convention, where the version field carries the replacement
 *    instead ([STR_CONTAINS]), which [DeprecationInfo.rawReplacement] has to unpack.
 *
 * Core functions are named without a `core/` prefix, matching how the generator emits them.
 *
 * Install with `PhelFunctionRegistry.installTestFunctions(...)` and always clear afterwards.
 */
object PhelDeprecatedFunctionFixtures {

    val PUT = deprecated(
        name = "put",
        signature = "(put ds key value)",
        summary = "Puts a value into a data structure.",
        deprecation = DeprecationInfo(version = "0.25.0", replacement = "assoc"),
    )

    val PUT_IN = deprecated(
        name = "put-in",
        signature = "(put-in ds ks v)",
        summary = "Puts a value into a nested data structure.",
        deprecation = DeprecationInfo(version = "0.25.0", replacement = "assoc-in"),
    )

    val PUSH = deprecated(
        name = "push",
        signature = "(push xs x)",
        summary = "Inserts x at the end of the sequence xs.",
        deprecation = DeprecationInfo(version = "0.25.0", replacement = "conj"),
    )

    val UNSET = deprecated(
        name = "unset",
        signature = "(unset ds key)",
        summary = "Removes an entry from a data structure.",
        deprecation = DeprecationInfo(version = "0.25.0", replacement = "dissoc"),
    )

    val UNSET_IN = deprecated(
        name = "unset-in",
        signature = "(unset-in ds ks)",
        summary = "Removes an entry from a nested data structure.",
        deprecation = DeprecationInfo(version = "0.25.0", replacement = "dissoc-in"),
    )

    val FUNCTION_P = deprecated(
        name = "function?",
        signature = "(function? x)",
        summary = "Checks if x is a function.",
        deprecation = DeprecationInfo(version = "0.32.0", replacement = "fn?"),
    )

    /** The "Use xyz" convention: the replacement rides in the version field, not in `replacement`. */
    val STR_CONTAINS = deprecated(
        name = "str-contains?",
        signature = "(str-contains? s subs)",
        summary = "Checks if the string contains the substring.",
        deprecation = DeprecationInfo(version = "Use phel\\string\\contains?"),
    )

    /** Every fixture, for tests that just need the registry to report deprecations again. */
    val ALL = listOf(PUT, PUT_IN, PUSH, UNSET, UNSET_IN, FUNCTION_P, STR_CONTAINS)

    private fun deprecated(
        name: String,
        signature: String,
        summary: String,
        deprecation: DeprecationInfo,
    ) = PhelFunction(
        namespace = "core",
        name = name,
        signature = signature,
        completion = CompletionInfo(
            tailText = summary,
            // The generator routes any deprecated function here before any other rule.
            priority = PhelCompletionPriority.DEPRECATED_FUNCTIONS,
        ),
        documentation = DocumentationInfo(summary = summary, deprecation = deprecation),
    )
}
