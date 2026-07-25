package org.phellang.integration.completion

import org.phellang.integration.PhelIntegrationTestCase

/**
 * Completion respects what a position can actually hold.
 *
 * In Lisp the head of a form is what gets called and the rest are values. Functions are values, so
 * `(map inc xs)` must keep offering `inc` — but macros and special forms are compile-time
 * constructs, so `(println let)` is not expressible and offering `let` there is always wrong.
 * Everything used to be offered in both positions, byte-identically.
 */
class PhelCallPositionCompletionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun completionsAt(source: String): List<String> {
        myFixture.configureByText("cp${fileIndex++}.phel", "(ns my\\app)\n$source")
        myFixture.completeBasic()

        return myFixture.lookupElementStrings ?: emptyList()
    }

    fun testMacrosAndSpecialFormsAreNotOfferedAsArguments() {
        val completions = completionsAt("(defn f [] (println w<caret>))")

        for (callOnly in listOf("when", "when-let", "when-not", "when-some", "while", "with-open")) {
            assertFalse("`$callOnly` cannot be an argument, got $completions", completions.contains(callOnly))
        }
    }

    fun testSpecialFormsAreNotOfferedAsArguments() {
        val completions = completionsAt("(defn f [] (println <caret>))")

        for (specialForm in listOf("let", "do", "fn", "try", "quote", "recur")) {
            assertFalse("`$specialForm` cannot be an argument, got ${completions.take(20)}", completions.contains(specialForm))
        }
    }

    /** The whole point of higher-order functions — functions are values. */
    fun testFunctionsAreStillOfferedAsArguments() {
        val completions = completionsAt("(defn f [xs] (map i<caret> xs))")

        assertTrue("expected inc in $completions", completions.contains("inc"))
    }

    /** `not` sits in the CONTROL_FLOW ranking bucket but is an ordinary function. */
    fun testNotIsOfferedAsAnArgumentDespiteItsRankingBucket() {
        val completions = completionsAt("(defn f [xs] (map no<caret> xs))")

        assertTrue("`not` is a function and valid here, got $completions", completions.contains("not"))
    }

    fun testMacrosAreStillOfferedInHeadPosition() {
        val completions = completionsAt("(defn f [] (w<caret>))")

        assertTrue("expected when in $completions", completions.contains("when"))
        assertTrue("expected when-let in $completions", completions.contains("when-let"))
    }

    /**
     * A threading macro's textual arguments become heads once it expands, so macros stay valid
     * there — `(-> x (when-let [y v] y))`.
     */
    fun testMacrosAreOfferedInsideThreadingMacros() {
        val completions = completionsAt("(defn f [x] (-> x (w<caret>)))")

        assertTrue("expected when-let inside -> in $completions", completions.contains("when-let"))
    }

    fun testMacrosAreOfferedInsideDoto() {
        val completions = completionsAt("(defn f [x] (doto x (w<caret>)))")

        assertTrue("expected when inside doto in $completions", completions.contains("when"))
    }

    /** Definition forms sink in head position: inside a body you are calling something. */
    fun testDefinitionFormsDoNotDominateHeadPosition() {
        val top = completionsAt("(defn f [] (<caret>))").take(12)

        for (definitionForm in listOf("def", "defstruct", "definterface", "defexception")) {
            assertFalse("`$definitionForm` should not crowd the top of a call position: $top", top.contains(definitionForm))
        }
    }

    /**
     * Demoted, not removed — nesting a definition inside a body is legal. An unfiltered lookup is
     * capped long before rank 979, so a prefix is how a low-ranked entry is actually reached.
     */
    fun testDefinitionFormsRemainReachableByPrefix() {
        val completions = completionsAt("(defn f [] (defs<caret>))")

        assertTrue("expected defstruct in $completions", completions.contains("defstruct"))
    }
}
