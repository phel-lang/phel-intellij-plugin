package org.phellang.integration.editor

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.editor.paredit.PhelMoveElementLeftRightHandler
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.files.PhelFile

/**
 * The sub-elements Move Element Left/Right is allowed to swap.
 *
 * Asserts what the handler exposes rather than driving the action: the platform owns the swap
 * itself, and what this plugin decides is only which elements are movable.
 */
class PhelMoveElementLeftRightHandlerTest : PhelIntegrationTestCase() {

    private val handler = PhelMoveElementLeftRightHandler()

    private fun parse(text: String): PhelFile = myFixture.configureByText("m.phel", text) as PhelFile

    private fun movableTextsOf(element: PsiElement?): List<String> =
        element?.let { handler.getMovableSubElements(it).map(PsiElement::getText) }.orEmpty()

    private inline fun <reified T : PsiElement> firstOf(text: String): T? =
        PsiTreeUtil.findChildOfType(parse(text), T::class.java)

    fun testExposesTheFormsOfAList() {
        val movable = movableTextsOf(firstOf<PhelList>("(println 1 2)\n"))

        assertEquals(listOf("println", "1", "2"), movable)
    }

    fun testExposesTheElementsOfAVector() {
        val movable = movableTextsOf(firstOf<PhelVec>("[1 2 3]\n"))

        assertEquals(listOf("1", "2", "3"), movable)
    }

    fun testExposesTheEntriesOfAMap() {
        val movable = movableTextsOf(firstOf<PhelMap>("{:a 1 :b 2}\n"))

        assertEquals(listOf(":a", "1", ":b", "2"), movable)
    }

    /** Nothing to reorder, and the platform would otherwise still offer the action. */
    fun testOffersNothingForASingleForm() {
        assertEmpty(movableTextsOf(firstOf<PhelList>("(println)\n")))
    }

    fun testOffersNothingForAnEmptyContainer() {
        assertEmpty(movableTextsOf(firstOf<PhelVec>("[]\n")))
    }

    fun testOffersNothingForANonContainer() {
        val file = parse("(println 1 2)\n")
        val symbol = PsiTreeUtil.findChildrenOfType(file, PsiElement::class.java)
            .first { it.text == "println" && it.firstChild == null }

        assertEmpty(movableTextsOf(symbol))
    }

    fun testExposesNestedFormsAsWholeUnits() {
        val movable = movableTextsOf(firstOf<PhelList>("(f (g 1) 2)\n"))

        assertEquals(listOf("f", "(g 1)", "2"), movable)
    }
}
