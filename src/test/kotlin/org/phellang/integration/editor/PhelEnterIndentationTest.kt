package org.phellang.integration.editor

import org.phellang.integration.PhelIntegrationTestCase

/**
 * Where the caret lands after ENTER.
 *
 * The indentation of the new line is one level per still-open parenthesis, so closing a form must
 * *reduce* it. The handler used to compute only the extra indentation to add to what the platform
 * had already copied from the previous line, clamped at zero — it could indent further but never
 * less, so `(print "hello"))` left the next line two spaces in, and `(print 1)))` four, when both
 * forms were over.
 */
class PhelEnterIndentationTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    /** The number of leading spaces on the line the caret ends up on. */
    private fun indentAfterEnter(source: String): Int {
        myFixture.configureByText("ei${fileIndex++}.phel", source)
        myFixture.type('\n')

        val document = myFixture.editor.document
        val line = document.getLineNumber(myFixture.editor.caretModel.offset)
        val text = document.text.substring(document.getLineStartOffset(line), document.getLineEndOffset(line))

        return text.takeWhile { it == ' ' }.length
    }

    fun testClosedTopLevelFormReturnsToColumnZero() {
        assertEquals(0, indentAfterEnter("(defn some-fn []\n  (print \"hello\"))<caret>"))
    }

    fun testClosingSeveralFormsAtOnceReturnsToColumnZero() {
        assertEquals(0, indentAfterEnter("(defn f []\n  (when true\n    (print 1)))<caret>"))
    }

    /** Closing one of two open forms dedents by one level, not all the way. */
    fun testClosingOneOfTwoFormsDedentsByOneLevel() {
        assertEquals(2, indentAfterEnter("(defn f []\n  (when true\n    (print 1))<caret>"))
    }

    fun testOpenFormKeepsItsBodyIndented() {
        assertEquals(2, indentAfterEnter("(defn some-fn []\n  (print \"hello\")<caret>"))
    }

    fun testHeadLineIndentsItsBody() {
        assertEquals(2, indentAfterEnter("(defn some-fn []<caret>"))
    }

    fun testNestedOpenFormsIndentCumulatively() {
        assertEquals(4, indentAfterEnter("(defn f []\n  (when true<caret>"))
    }

    fun testTopLevelStaysAtColumnZero() {
        assertEquals(0, indentAfterEnter("(def a 1)<caret>"))
    }

    /** A closing paren inside a string is not a closing paren. */
    fun testParenthesesInStringsDoNotAffectIndentation() {
        assertEquals(2, indentAfterEnter("(defn f []\n  (print \"))\")<caret>"))
    }

    /** Nor is one in a comment. */
    fun testParenthesesInCommentsDoNotAffectIndentation() {
        assertEquals(2, indentAfterEnter("(defn f []\n  (print 1) ; ))\n  (print 2)<caret>"))
    }
}
