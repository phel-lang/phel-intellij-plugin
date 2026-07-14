package org.phellang.integration.completion

import com.intellij.codeInsight.completion.CompletionType
import org.phellang.integration.PhelIntegrationTestCase

/**
 * Completion invoked *at* an existing symbol, rather than typed character by character.
 *
 * The two paths derive the prefix differently. While a lookup is open, [org.phellang.completion
 * .PhelCompletionCharFilter] widens the prefix through Phel's identifier alphabet (`/`, `-`, `?`,
 * `!`, …), so typing `s` then `/` filters correctly. But the *initial* prefix on an explicit
 * invocation is computed by the platform using Java's identifier rules, which sever the symbol at
 * `/` — the prefix came out empty, so nothing filtered on the `s/` already written and the entire
 * registry was offered.
 */
class PhelCompletionPrefixTest : PhelIntegrationTestCase() {

    fun testInvokingCompletionAfterAnAliasOffersOnlyThatNamespace() {
        val suggestions = completeAt(
            """
            (ns app\m
              (:require phel\string :as s))

            (defn f [t] (s/<caret>))
            """.trimIndent()
        )

        assertTrue(
            "expected the alias to filter to phel\\string functions, got: ${suggestions.take(15)}",
            suggestions.any { it.startsWith("s/") },
        )
        assertTrue(
            "everything offered must respect the `s/` already written, got: " +
                    "${suggestions.filterNot { it.startsWith("s/") }.take(10)}",
            suggestions.all { it.startsWith("s/") },
        )
    }

    fun testInvokingCompletionAfterAFullNamespaceOffersOnlyThatNamespace() {
        val suggestions = completeAt(
            """
            (ns app\m
              (:require phel\string))

            (defn f [t] (string/<caret>))
            """.trimIndent()
        )

        assertTrue(
            "expected string/ functions, got: ${suggestions.take(15)}",
            suggestions.any { it.startsWith("string/") },
        )
        assertTrue(
            "nothing unrelated may leak past the `string/` prefix, got: " +
                    "${suggestions.filterNot { it.startsWith("string/") }.take(10)}",
            suggestions.all { it.startsWith("string/") },
        )
    }

    fun testKebabCaseIsNotSeveredAtTheHyphen() {
        // `-` is not a Java identifier char either, so the platform's prefix started *after* it.
        val suggestions = completeAt(
            """
            (ns app\m)

            (defn my-helper [] 1)
            (defn my-other [] 2)
            (defn g [] (my-<caret>))
            """.trimIndent()
        )

        assertTrue(
            "kebab-case locals must survive the prefix `my-`, got: ${suggestions.take(10)}",
            suggestions.containsAll(listOf("my-helper", "my-other")),
        )
        assertTrue(
            "nothing without the `my-` prefix may be offered, got: " +
                    "${suggestions.filterNot { it.startsWith("my-") }.take(10)}",
            suggestions.all { it.startsWith("my-") },
        )
    }

    private fun completeAt(text: String): List<String> {
        myFixture.configureByText("a.phel", text)
        myFixture.complete(CompletionType.BASIC)
        // Null when a single candidate matched and the platform inserted it outright; these fixtures
        // are written so that at least two candidates always match, so a null here is a real failure.
        return myFixture.lookupElementStrings ?: emptyList()
    }
}
