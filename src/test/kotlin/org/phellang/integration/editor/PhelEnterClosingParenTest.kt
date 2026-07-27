package org.phellang.integration.editor

import org.phellang.integration.PhelIntegrationTestCase

/**
 * Enter adds a closing paren only when the line's *code* ends on an opening one.
 *
 * The predicate used to read the line's raw text, so a `(` that was only part of a comment — or of a
 * string — pulled a closing paren into the code below it, closing nothing.
 */
class PhelEnterClosingParenTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun afterEnter(source: String): String {
        myFixture.configureByText("ecp${fileIndex++}.phel", source)
        myFixture.type('\n')

        return myFixture.editor.document.text
    }

    /** The behaviour being preserved: a genuinely open paren still gets its closer. */
    fun testStillClosesAGenuinelyOpenParen() {
        val text = afterEnter("(defn f []\n  (<caret>")

        assertTrue("an open paren should still be closed: $text", text.contains(")"))
    }

    fun testDoesNotCloseAParenThatOnlyEndsAComment() {
        val text = afterEnter("(println 1) ; ((((<caret>")

        assertFalse("the parens are prose, nothing is open: $text", text.contains(")\n") || text.trimEnd().endsWith(")"))
    }

    fun testDoesNotCloseAParenInsideAString() {
        val text = afterEnter("(def s \"an open paren: (<caret>")

        assertEquals("the paren is inside a string literal: $text", 0, text.count { it == ')' })
    }

    /** A comment after real code does not change what the code ends on. */
    fun testIgnoresATrailingCommentWhenTheCodeIsBalanced() {
        val text = afterEnter("(println 1) ; note<caret>")

        assertEquals("nothing is open: $text", 1, text.count { it == ')' })
    }

    /** And still closes when the code ends on `(` despite a trailing comment. */
    fun testClosesWhenCodeEndsOnAParenBeforeAComment() {
        val text = afterEnter("(defn f []\n  (println ( ; note<caret>")

        assertTrue("the second paren is open: $text", text.count { it == ')' } >= 1)
    }
}
