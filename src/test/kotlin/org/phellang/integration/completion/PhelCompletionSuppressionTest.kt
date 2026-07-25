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

    fun testSuppressedWhileNamingAnyDefinitionForm() {
        for (form in listOf(
            "def", "def-", "defn", "defn-", "defmacro", "defmacro-",
            "defstruct", "definterface", "defexception", "defonce", "defenum",
            "declare", "defprotocol", "defrecord", "deftype", "defmulti", "ns",
        )) {
            assertSuppressed("($form <caret>)")
        }
    }

    fun testSuppressedWhileNamingADefinitionNestedInAFunction() =
        assertSuppressed("(defn outer [] (def <caret>))")

    /**
     * `(defmethod multi-name dispatch-val ...)` names an *existing* multimethod, so this is the one
     * `def`-prefixed head where completing the second slot is exactly what the user wants.
     */
    fun testOffersCompletionsOnADefmethodTarget() =
        assertOffersCompletions("(defmethod area <caret>)")

    /**
     * The second element of these is an ordinary expression, not a name.
     *
     * All of them were suppressed while the predicates gated on the SPECIAL_FORMS completion
     * priority, a ranking bucket that happens to contain them — so completion was dead in a `do`
     * block, in the exception slot of a `throw`, and in the arguments of a `recur`.
     */
    fun testOffersCompletionsInTheBodyOfANonDeclaringSpecialForm() {
        for (form in listOf("do", "try", "throw", "recur", "quote", "catch", "finally")) {
            assertOffersCompletions("(defn f [] ($form <caret>))")
        }
    }

    fun testSuppressedInsideAnFnParameterVector() = assertSuppressed("(fn [<caret>] 1)")

    /**
     * The slot before the vector has been typed. Every one of these requires a `[` next, per its
     * registry signature, so any candidate offered there would be wrong.
     */
    fun testSuppressedWhereOnlyAVectorCanFollow() {
        for (form in listOf("fn", "let", "loop", "if-let", "when-let", "for", "foreach", "binding", "dofor")) {
            assertSuppressed("(defn f [] ($form <caret>))")
        }
    }

    /** The `defn` family is exempt: slot 1 is its name, and slot 2 also accepts a docstring or metadata. */
    fun testOffersCompletionsWhereADocstringOrMetadataMayFollowInstead() =
        assertOffersCompletions("(defn f <caret>)")

    /** Once the vector exists, the slot is a collection and the body positions stay live. */
    fun testOffersCompletionsInABodyAfterTheVector() =
        assertOffersCompletions("(defn f [] (let [a 1] <caret>))")

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
