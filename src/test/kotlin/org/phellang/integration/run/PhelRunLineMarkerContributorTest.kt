package org.phellang.integration.run

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiElement
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.run.PhelRunLineMarkerContributor

class PhelRunLineMarkerContributorTest : PhelIntegrationTestCase() {

    private val contributor = PhelRunLineMarkerContributor()

    private fun markedElements(text: String): List<PsiElement> {
        val file = myFixture.configureByText("core.phel", text)
        return PsiTreeUtil.collectElements(file) { it.firstChild == null }
            .filter { contributor.getInfo(it) != null }
    }

    fun testMarksTheNamespaceDeclarationOnce() {
        val marked = markedElements("(ns simple)\n(defn greet [] \"hi\")\n")

        assertEquals("expected exactly one run marker, got ${marked.map { it.text }}", 1, marked.size)
        assertEquals("ns", marked.single().text)
    }

    fun testDoesNotMarkAFileWithoutANamespace() {
        assertEmpty(markedElements("(defn greet [] \"hi\")\n"))
    }

    /** `ns` as an argument is not a namespace declaration, and running it would mean nothing. */
    fun testDoesNotMarkAnNsSymbolOutsideTheDeclaration() {
        val marked = markedElements("(defn describe [ns] ns)\n")

        assertEmpty(marked)
    }

    fun testMarksOnlyTheDeclarationWhenNsAppearsElsewhere() {
        val marked = markedElements("(ns simple)\n(defn describe [ns] ns)\n")

        assertEquals(1, marked.size)
        val declaration = marked.single()
        assertEquals("ns", declaration.text)
        // The one inside the declaration starts before the defn that shadows the name.
        assertTrue(declaration.textOffset < 11)
    }

    fun testOffersTheRunAction() {
        val file = myFixture.configureByText("core.phel", "(ns simple)\n")
        val info: RunLineMarkerContributor.Info = PsiTreeUtil.collectElements(file) { it.firstChild == null }
            .firstNotNullOf { contributor.getInfo(it) }

        assertTrue("expected at least one executor action on the gutter marker", info.actions.isNotEmpty())
    }

    // ---- tests ----

    private val testFile = """
        (ns test.html
          (:require phel.test :refer [deftest is]))

        (deftest basic-tags
          (is (= "<div></div>" (html [:div]))))

        (deftest empty-tags
          (is (= "<h1></h1>" (html [:h1]))))
    """.trimIndent()

    fun testMarksTheNamespaceAndEveryTest() {
        val marked = markedElements(testFile)

        assertEquals(listOf("ns", "deftest", "deftest"), marked.map { it.text })
    }

    fun testDoesNotMarkAssertionsOrOtherInnerForms() {
        val marked = markedElements(testFile)

        assertFalse("assertions are not independently runnable", marked.any { it.text == "is" })
    }

    /** Without the `phel\test` require there is nothing for `phel test` to find, so no icon. */
    fun testDoesNotMarkTestsInAFileThatDoesNotRequirePhelTest() {
        val marked = markedElements("(ns app.html)\n\n(deftest basic-tags\n  (is true))\n")

        assertEquals(listOf("ns"), marked.map { it.text })
    }

    fun testNamesTheTestInItsTooltip() {
        val file = myFixture.configureByText("html.phel", testFile)
        val info = PsiTreeUtil.collectElements(file) { it.firstChild == null }
            .filter { it.text == "deftest" }
            .mapNotNull { contributor.getInfo(it) }

        assertEquals(listOf("Run basic-tags", "Run empty-tags"), info.map { it.tooltipProvider?.apply(null) })
    }

    fun testNamesTheFileInTheNamespaceTooltip() {
        val file = myFixture.configureByText("html.phel", testFile)
        val info = PsiTreeUtil.collectElements(file) { it.firstChild == null }
            .single { it.text == "ns" }
            .let { contributor.getInfo(it) }

        assertEquals("Run html.phel", info?.tooltipProvider?.apply(null))
    }
}
