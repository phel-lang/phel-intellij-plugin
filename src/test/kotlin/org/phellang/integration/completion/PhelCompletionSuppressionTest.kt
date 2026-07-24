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

    /**
     * Known gap, asserted so a change is noticed rather than assumed.
     *
     * The value half of a binding is an ordinary expression position and ought to complete, but
     * `let`'s binding vector *is* its second form, so the name-position predicate claims the whole
     * vector — values included. It cannot simply be excluded: `isBindingName` gates on the
     * CONTROL_FLOW priority, which does not contain `let`, so today the name half is suppressed
     * only as a side effect of that same over-broad claim. Fixing this means giving the binding
     * predicate an explicit set of binding forms, then narrowing the name predicate to symbols.
     */
    fun testValueHalfOfALetBindingIsSuppressedTooBroadly() =
        assertSuppressed("(defn f [] (let [x <caret>] x))")

    fun testOffersCompletionsInACallPosition() = assertOffersCompletions("(defn f [] (<caret>))")

    fun testOffersCompletionsInsideABody() = assertOffersCompletions("(defn f [x] <caret>)")

    /**
     * A vector in the body is a literal, not a parameter list, so names inside it are references.
     * Only the first vector after the name declares parameters.
     */
    fun testOffersCompletionsInsideALiteralVectorInTheBody() =
        assertOffersCompletions("(defn f [x] [<caret>])")
}
