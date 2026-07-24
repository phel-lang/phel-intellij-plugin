package org.phellang.integration.completion

import org.phellang.integration.PhelIntegrationTestCase

/**
 * The completions that come from the edited file itself: parameters, binding-vector entries, and
 * its own top-level definitions.
 *
 * These are asserted through the real completion pipeline rather than against the collectors, so
 * scope order and de-duplication (which only exist once the three collectors share a sink) are
 * covered as the user experiences them.
 */
class PhelLocalSymbolCompletionTest : PhelIntegrationTestCase() {

    /** Completion offers the whole standard library too, so tests assert containment, not equality. */
    private fun completionsFor(source: String): List<String> {
        myFixture.configureByText("a.phel", "(ns my\\app)\n$source")
        myFixture.completeBasic()

        return myFixture.lookupElementStrings ?: emptyList()
    }

    fun testFunctionParametersAreOffered() {
        val completions = completionsFor("(defn f [alpha beta] al<caret>)")

        assertTrue("expected alpha in $completions", completions.contains("alpha"))
    }

    fun testParametersOfAnEnclosingFunctionDoNotLeakIntoAnInnerOne() {
        val completions = completionsFor("(defn outer [outer-arg] (fn [inner-arg] <caret>))")

        assertTrue("expected inner-arg in $completions", completions.contains("inner-arg"))
        assertFalse("outer-arg must not leak in $completions", completions.contains("outer-arg"))
    }

    fun testLetBindingsAreOffered() {
        val completions = completionsFor("(defn f [] (let [total 1] to<caret>))")

        assertTrue("expected total in $completions", completions.contains("total"))
    }

    /** Both `let` scopes are live at the caret, so both must be offered. */
    fun testNestedLetBindingsAreAllOffered() {
        val completions = completionsFor("(defn f [] (let [outer 1] (let [inner 2] <caret>)))")

        assertTrue("expected outer in $completions", completions.contains("outer"))
        assertTrue("expected inner in $completions", completions.contains("inner"))
    }

    fun testLoopAndForBindingsAreOffered() {
        val loop = completionsFor("(defn f [] (loop [acc 0] ac<caret>))")
        assertTrue("expected acc in $loop", loop.contains("acc"))

        val forForm = completionsFor("(defn f [] (for [item :in [1 2]] it<caret>))")
        assertTrue("expected item in $forForm", forForm.contains("item"))
    }

    fun testTopLevelFunctionsOfTheSameFileAreOffered() {
        val completions = completionsFor("(defn helper [x] x)\n(defn g [] <caret>)")

        assertTrue("expected helper in $completions", completions.contains("helper"))
    }

    /**
     * A `def` ranks at PROJECT_SYMBOLS, below the RECENT_DEFINITIONS a `defn` gets, so it can fall
     * off the end of an unfiltered lookup list. Typed against a prefix it is the only candidate, and
     * a sole candidate is inserted rather than offered — so the completed text is what to assert.
     */
    fun testTopLevelDefinitionsOfTheSameFileAreOffered() {
        myFixture.configureByText("a.phel", "(ns my\\app)\n(def limit 10)\n(defn g [] lim<caret>)")
        myFixture.completeBasic()

        assertTrue("expected `limit` completed in ${myFixture.file.text}", myFixture.file.text.contains("(defn g [] limit)"))
    }

    fun testEachNameIsOfferedOnlyOnce() {
        val completions = completionsFor("(defn dup [x] x)\n(defn g [dup] du<caret>)")

        assertEquals("dup offered more than once in $completions", 1, completions.count { it == "dup" })
    }

    /**
     * `#_` discards the form that follows it, so the discarded name is never bound. Reading the raw
     * PSI children instead of the active forms offers `old`, a name that does not exist at runtime.
     */
    fun testDiscardedDefinitionNameIsNotOffered() {
        val completions = completionsFor("(defn #_old new-name [x] x)\n(defn g [] <caret>)")

        assertTrue("expected new-name in $completions", completions.contains("new-name"))
        assertFalse("discarded name must not be offered in $completions", completions.contains("old"))
    }

    fun testDiscardedParameterIsNotOffered() {
        val completions = completionsFor("(defn f [kept #_dropped] <caret>)")

        assertTrue("expected kept in $completions", completions.contains("kept"))
        assertFalse("discarded parameter must not be offered in $completions", completions.contains("dropped"))
    }
}
