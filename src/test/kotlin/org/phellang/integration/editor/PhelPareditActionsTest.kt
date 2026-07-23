package org.phellang.integration.editor

import org.phellang.integration.PhelIntegrationTestCase

/**
 * End-to-end coverage of the nine registered paredit actions, driven through the real action system
 * (`performEditorAction`) so registration, the handler and the operation are all exercised. Each
 * case pins the observed before -> after so a regression that silently rewrites the user's source
 * fails loudly. Behaviour was captured from the shipped actions, not assumed.
 */
class PhelPareditActionsTest : PhelIntegrationTestCase() {

    private fun run(actionId: String, before: String, after: String) {
        myFixture.configureByText("a.phel", before)
        myFixture.performEditorAction(actionId)
        myFixture.checkResult(after)
    }

    /** The action finds nothing to do and leaves the source untouched. */
    private fun noOp(actionId: String, source: String) {
        run(actionId, source, source.replace("<caret>", ""))
    }

    // --- Slurp forward ---

    fun testSlurpForwardPullsFollowingFormIntoTheOuterList() {
        run("Phel.Paredit.SlurpForward", "(a (b<caret>)) c", "(a (b) c)")
    }

    /**
     * KNOWN DEFECT (characterization test): slurping across mismatched delimiters moves the inner
     * close delimiter and produces unbalanced output. Pinned so the corruption is visible and a
     * later fix updates this deliberately. Worth its own issue.
     */
    fun testSlurpForwardAcrossMismatchedDelimitersIsUnbalanced() {
        run("Phel.Paredit.SlurpForward", "(a [b<caret>]) c", "(a [b) c]")
    }

    fun testSlurpForwardIsANoOpWithoutAnEnclosingWrapper() {
        noOp("Phel.Paredit.SlurpForward", "(a<caret>) b")
    }

    fun testSlurpForwardIsANoOpWithNothingToSlurp() {
        noOp("Phel.Paredit.SlurpForward", "(a<caret>)")
    }

    // --- Barf forward ---

    fun testBarfForwardEjectsTheLastFormToTheRight() {
        run("Phel.Paredit.BarfForward", "(a<caret> b)", "(a) b")
    }

    fun testBarfForwardOnANestedForm() {
        run("Phel.Paredit.BarfForward", "(a (b c<caret>))", "(a (b) c)")
    }

    fun testBarfForwardIsANoOpOnASingleElementForm() {
        noOp("Phel.Paredit.BarfForward", "(a<caret>)")
    }

    fun testBarfForwardIsANoOpOnAnEmptyForm() {
        noOp("Phel.Paredit.BarfForward", "(<caret>)")
    }

    // --- Slurp backward ---

    fun testSlurpBackwardPullsThePrecedingFormIntoTheOuterList() {
        run("Phel.Paredit.SlurpBackward", "a ((b<caret>))", "(a (b))")
    }

    fun testSlurpBackwardIsANoOpWithNothingBefore() {
        noOp("Phel.Paredit.SlurpBackward", "(a (b<caret>))")
    }

    // --- Barf backward ---

    fun testBarfBackwardEjectsTheFirstFormToTheLeft() {
        run("Phel.Paredit.BarfBackward", "(a<caret> b)", "a (b)")
    }

    fun testBarfBackwardOnANestedForm() {
        run("Phel.Paredit.BarfBackward", "(a (b<caret> c))", "(a b (c))")
    }

    // --- Splice ---

    fun testSpliceRemovesTheEnclosingListDelimiters() {
        run("Phel.Paredit.Splice", "(a (b<caret> c) d)", "(a b c d)")
    }

    fun testSpliceRemovesVectorDelimiters() {
        run("Phel.Paredit.Splice", "(a [b<caret> c] d)", "(a b c d)")
    }

    fun testSpliceRemovesMapDelimiters() {
        run("Phel.Paredit.Splice", "(a {b<caret> c} d)", "(a b c d)")
    }

    fun testSpliceAtTopLevel() {
        run("Phel.Paredit.Splice", "(a<caret> b)", "a b")
    }

    fun testSpliceOnAnEmptyFormRemovesIt() {
        run("Phel.Paredit.Splice", "(<caret>)", "")
    }

    fun testSpliceIsANoOpOutsideAnyForm() {
        noOp("Phel.Paredit.Splice", "a<caret>")
    }

    // --- Raise ---

    fun testRaiseReplacesTheContainerWithTheFormAtCaret() {
        run("Phel.Paredit.Raise", "(a (b<caret>) c)", "(b)")
    }

    fun testRaiseFromInsideAVector() {
        run("Phel.Paredit.Raise", "[a (b<caret>) c]", "(b)")
    }

    fun testRaiseIsANoOpOnATopLevelForm() {
        noOp("Phel.Paredit.Raise", "(a<caret>)")
    }

    // --- Wrap ---

    fun testWrapParenWrapsTheFormAtCaret() {
        run("Phel.Paredit.WrapParen", "foo<caret>", "(foo)")
    }

    fun testWrapParenWrapsAWholeList() {
        run("Phel.Paredit.WrapParen", "(a b<caret>)", "((a b))")
    }

    fun testWrapBracketWrapsInSquareBrackets() {
        run("Phel.Paredit.WrapBracket", "foo<caret>", "[foo]")
    }

    fun testWrapBraceWrapsInBraces() {
        run("Phel.Paredit.WrapBrace", "foo<caret>", "{foo}")
    }
}
