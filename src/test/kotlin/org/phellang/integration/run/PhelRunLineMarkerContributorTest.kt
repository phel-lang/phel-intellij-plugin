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
}
