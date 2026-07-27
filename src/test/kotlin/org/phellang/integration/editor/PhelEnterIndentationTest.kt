package org.phellang.integration.editor

import com.intellij.application.options.CodeStyle
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.infrastructure.PhelLanguage

/**
 * Where the caret lands after ENTER.
 *
 * The indentation of the new line is one level per still-open bracket, so closing a form must
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

    // ---- Every bracket opens a level, not only a parenthesis ----

    /**
     * A binding vector is a level of its own: `defn`, `let` and the vector make three.
     *
     * The scanner counted only `(` and `)`, so the `[` here was invisible and the new line landed at
     * the `let`'s level instead of inside its bindings.
     */
    fun testOpenVectorIndentsItsContents() {
        assertEquals(6, indentAfterEnter("(defn f []\n  (let [x 1<caret>"))
    }

    fun testOpenMapIndentsItsContents() {
        assertEquals(4, indentAfterEnter("(defn f []\n  {:a 1<caret>"))
    }

    fun testOpenSetIndentsItsContents() {
        assertEquals(4, indentAfterEnter("(defn f []\n  #{:a<caret>"))
    }

    /** With no enclosing list at all, a vector still indents its own contents. */
    fun testTopLevelVectorIndentsItsContents() {
        assertEquals(2, indentAfterEnter("[1 2<caret>"))
    }

    /** Closing the binding vector drops back to the `let` body's level, not out of the `let`. */
    fun testClosingAVectorDedentsByOneLevel() {
        assertEquals(4, indentAfterEnter("(defn f []\n  (let [x 1]<caret>"))
    }

    /** `#(` opens one level, not two: the `#` is part of the opener. */
    fun testAnonymousFunctionIndentsItsBody() {
        assertEquals(4, indentAfterEnter("(defn f []\n  (map #(inc %)<caret>"))
    }

    // ---- Character literals are not brackets ----

    /**
     * `\(` is a character literal, not an opening paren — the lexer's `CHARACTER` rule ends in a
     * catch-all `.`, so any character can follow the backslash.
     */
    fun testCharacterLiteralParenthesesDoNotAffectIndentation() {
        assertEquals(2, indentAfterEnter("(defn f []\n  (print \\()<caret>"))
    }

    fun testCharacterLiteralClosingParenthesisDoesNotDedent() {
        assertEquals(2, indentAfterEnter("(defn f []\n  (print \\))<caret>"))
    }

    // ---- The Code Style indent width ----

    /** The width was hard-coded to two, so the Code Style page could show a number Enter ignored. */
    fun testHonoursTheConfiguredIndentSize() {
        val indentOptions = CodeStyle.getSettings(project).getCommonSettings(PhelLanguage).indentOptions!!
        val original = indentOptions.INDENT_SIZE
        try {
            indentOptions.INDENT_SIZE = 4
            assertEquals(8, indentAfterEnter("(defn f []\n  (when true<caret>"))
        } finally {
            indentOptions.INDENT_SIZE = original
        }
    }
}
