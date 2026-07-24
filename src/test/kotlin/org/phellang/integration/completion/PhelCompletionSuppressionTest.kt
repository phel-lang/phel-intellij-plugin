package org.phellang.integration.completion

import org.phellang.integration.PhelIntegrationTestCase

/**
 * Completion is suppressed wherever the caret is introducing a name.
 *
 * Offering the standard library while the user types the name of a new `defn`, a parameter, or a
 * `let` binding is not merely noisy: accepting a suggestion there silently redefines something.
 * The mirror cases matter just as much, since over-suppressing would leave real positions dead.
 */
class PhelCompletionSuppressionTest : PhelIntegrationTestCase() {

    private fun completionsAt(source: String): List<String> {
        myFixture.configureByText("a.phel", "(ns my\\app)\n$source")
        myFixture.completeBasic()

        return myFixture.lookupElementStrings ?: emptyList()
    }

    private fun assertSuppressed(source: String) {
        val before = "(ns my\\app)\n$source".replace("<caret>", "")

        val completions = completionsAt(source)

        assertTrue("expected no completions, got $completions", completions.isEmpty())
        assertEquals("nothing should have been inserted", before, myFixture.file.text)
    }

    private fun assertOffersCompletions(source: String) {
        val completions = completionsAt(source)

        assertTrue("expected completions at this position", completions.isNotEmpty())
    }

    fun testSuppressedWhileNamingADef() = assertSuppressed("(def <caret>)")

    /**
     * Known gap, asserted so a change is noticed rather than assumed.
     *
     * Both name predicates gate on the SPECIAL_FORMS registry priority, which holds `def`,
     * `defstruct`, `definterface` and `defexception` but not `defn` / `defmacro`. Naming a `defn`
     * therefore still offers the whole standard library. Closing it means keying the predicates on
     * an explicit definition-keyword set instead of the completion-priority bucket.
     */
    fun testNamingADefnIsNotYetSuppressed() =
        assertOffersCompletions("(defn <caret>)")

    fun testSuppressedInsideAnFnParameterVector() = assertSuppressed("(fn [<caret>] 1)")

    fun testSuppressedInsideADefnParameterVector() = assertSuppressed("(defn f [<caret>] 1)")

    fun testSuppressedOnTheNameHalfOfALetBinding() = assertSuppressed("(defn f [] (let [<caret> 1] 1))")

    /** The value half of a binding is an ordinary expression position. */
    fun testOffersCompletionsOnTheValueHalfOfALetBinding() =
        assertOffersCompletions("(defn f [] (let [x <caret>] x))")

    fun testOffersCompletionsOnTheValueHalfOfALoopBinding() =
        assertOffersCompletions("(defn f [] (loop [acc <caret>] acc))")

    fun testOffersCompletionsOnALaterValueInTheSameVector() =
        assertOffersCompletions("(defn f [] (let [a 1 b <caret>] b))")

    fun testSuppressedOnTheNameHalfOfALoopBinding() =
        assertSuppressed("(defn f [] (loop [<caret> 0] 1))")

    fun testSuppressedOnALaterNameInTheSameVector() =
        assertSuppressed("(defn f [] (let [a 1 <caret> 2] a))")

    fun testSuppressedOnTheNameHalfOfAnIfLetBinding() =
        assertSuppressed("(defn f [] (if-let [<caret> 1] 1 2))")

    /**
     * `#_` discards the form after it without removing it from the tree, so counting raw children
     * flips the name/value parity and inverts both answers below.
     *
     * With the discard stripped, `keep` is the name and the caret after it is its value; in the
     * second case the caret itself is the name.
     */
    fun testDiscardedEntryDoesNotFlipTheNameValueParity() {
        assertOffersCompletions("(defn f [] (let [#_gone keep <caret>] 1))")
        assertSuppressed("(defn f [] (let [#_gone <caret> 1] 1))")
    }

    fun testOffersCompletionsInACallPosition() = assertOffersCompletions("(defn f [] (<caret>))")

    fun testOffersCompletionsInsideABody() = assertOffersCompletions("(defn f [x] <caret>)")

    /**
     * A vector in the body is a literal, not a parameter list, so names inside it are references.
     * Only the first vector after the name declares parameters.
     */
    fun testOffersCompletionsInsideALiteralVectorInTheBody() =
        assertOffersCompletions("(defn f [x] [<caret>])")
}
