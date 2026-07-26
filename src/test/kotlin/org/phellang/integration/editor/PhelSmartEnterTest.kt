package org.phellang.integration.editor

import org.phellang.integration.PhelIntegrationTestCase

/**
 * Complete Current Statement closes what the line leaves open.
 *
 * Driven through the real action id, so the registration is exercised rather than the processor
 * called directly.
 */
class PhelSmartEnterTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun completed(source: String): String {
        myFixture.configureByText("se${fileIndex++}.phel", source)
        myFixture.performEditorAction("EditorCompleteStatement")

        return myFixture.editor.document.text
    }

    fun testClosesAnOpenList() {
        assertEquals("(println 1)", completed("(println 1<caret>"))
    }

    /** Innermost first: the vector closes before the list that holds it. */
    fun testClosesNestedBracketsInTheRightOrder() {
        assertEquals("(defn f [x])", completed("(defn f [x<caret>"))
    }

    fun testClosesAMapAndItsEnclosingList() {
        assertEquals("(def m {:a 1})", completed("(def m {:a 1<caret>"))
    }

    fun testClosesASet() {
        assertEquals("(def s #{1 2})", completed("(def s #{1 2<caret>"))
    }

    fun testClosesAnAnonymousFunction() {
        assertEquals("(map #(inc %))", completed("(map #(inc %<caret>"))
    }

    /**
     * Nothing to close, so the processor declines and the platform's default runs — which inserts a
     * line break. That fall-through is the point: declining must leave the action still useful.
     */
    fun testLeavesAnAlreadyBalancedLineAlone() {
        assertEquals("(println 1)\n", completed("(println 1)<caret>"))
    }

    /** A bracket inside a string is text, not structure. */
    fun testIgnoresBracketsInsideAString() {
        assertEquals("(println \"(unclosed\")", completed("(println \"(unclosed\"<caret>"))
    }

    /** Brackets in a comment are prose: one list is open here, not five. */
    fun testIgnoresBracketsInAComment() {
        assertEquals("(println (inc 1)) ; ((((", completed("(println (inc 1) ; ((((<caret>"))
    }

    /** And the closer goes before the comment, or it would be commented out along with the note. */
    fun testInsertsBeforeATrailingComment() {
        assertEquals("(println (inc 1)) ; note", completed("(println (inc 1) ; note<caret>"))
    }

    /** `\(` is a character literal. */
    fun testIgnoresACharacterLiteralBracket() {
        assertEquals("(println \\()", completed("(println \\(<caret>"))
    }

    /** The caret sits after everything it just inserted, ready to keep typing. */
    fun testLeavesTheCaretAfterTheInsertedBrackets() {
        myFixture.configureByText("se-caret.phel", "(defn f [x<caret>")
        myFixture.performEditorAction("EditorCompleteStatement")

        assertEquals(myFixture.editor.document.textLength, myFixture.editor.caretModel.offset)
    }

    /** Completing the whole line, not the caret position: text after the caret still counts. */
    fun testClosesWhatFollowsTheCaretToo() {
        assertEquals("(println (inc 1))", completed("(println <caret>(inc 1"))
    }
}
