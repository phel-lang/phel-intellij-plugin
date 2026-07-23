package org.phellang.integration.annotator

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.analyzers.PhelCommentAnalyzer
import org.phellang.core.highlighting.PhelAnnotationConstants.COMMENTED_OUT_FORM
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol

/**
 * `#_` discards the next form. These cases pin the language rule across every container and
 * every form shape — compound forms, operator symbols and strings included, which the former
 * regex-over-raw-text implementation got wrong (it greyed out live code for `#_+` and `"#_"`).
 */
class PhelFormCommentHighlightingTest : PhelIntegrationTestCase() {

    fun testPlainSymbolIsDiscarded() = assertDiscarded("(list #_alpha beta)", "alpha")

    fun testStackedDiscardsDropTwoForms() =
        assertDiscarded("(list #_#_alpha beta gamma)", "alpha", "beta")

    fun testCompoundFormDiscardsEverythingInside() =
        assertDiscarded("(list #_(alpha beta) gamma)", "alpha", "beta")

    fun testVectorFormIsDiscarded() = assertDiscarded("(list #_[alpha] beta)", "alpha")

    fun testMapFormIsDiscarded() = assertDiscarded("(list #_{:a alpha} beta)", "alpha")

    fun testSetFormIsDiscarded() = assertDiscarded("(list #_#{alpha} beta)", "alpha")

    /** The whole anonymous function is discarded, operator symbol included. */
    fun testAnonFnFormIsDiscarded() = assertDiscarded("(list #_#(+ alpha) beta)", "+", "alpha")

    /** `+` is not word-like; the regex tokenizer skipped it and greyed `beta` instead. */
    fun testOperatorSymbolIsDiscarded() = assertDiscarded("(list #_+ beta)", "+")

    /** `#_` inside a string is data, not a discard marker. */
    fun testFormCommentInsideStringDiscardsNothing() = assertDiscarded("(list \"#_\" beta)")

    fun testQuotedFormIsDiscarded() = assertDiscarded("(list #_'alpha beta)", "alpha")

    /** Metadata binds to the form it annotates, so `#_` drops both together. */
    fun testMetadataAnnotatedFormIsDiscarded() =
        assertDiscarded("(list #_^{:m 1} alpha beta)", "alpha")

    fun testTopLevelDiscardOutsideAnyList() = assertDiscarded("#_alpha beta", "alpha")

    fun testDiscardInsideVectorContainer() = assertDiscarded("[#_alpha beta]", "alpha")

    fun testDiscardInsideMapContainer() = assertDiscarded("{#_alpha beta :k v}", "alpha")

    fun testNestedDiscardInsideDiscardedForm() =
        assertDiscarded("(list #_(a #_b c) d)", "a", "b", "c")

    fun testFileWithoutAnyDiscardGreysNothing() = assertDiscarded("(list alpha beta)")

    /** Asserts exactly [expected] symbols are greyed, both via the analyzer and end to end. */
    private fun assertDiscarded(source: String, vararg expected: String) {
        val file = myFixture.configureByText("a.phel", source)

        val actual = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
            .filter { PhelCommentAnalyzer.isCommentedOutByFormComment(it) }
            .map { it.text }
        assertEquals("analyzer verdict for `$source`", expected.toList(), actual)

        val greyedRanges = myFixture.doHighlighting()
            .filter { it.forcedTextAttributesKey == COMMENTED_OUT_FORM }
            .map { it.startOffset to it.endOffset }
        val greyedSymbols = PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
            .filter { symbol -> greyedRanges.any { it.first <= symbol.textRange.startOffset && symbol.textRange.endOffset <= it.second } }
            .map { it.text }
        assertEquals("annotator highlighting for `$source`", expected.toList(), greyedSymbols)
    }
}
