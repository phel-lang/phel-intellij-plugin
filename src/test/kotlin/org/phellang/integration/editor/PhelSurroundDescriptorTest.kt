package org.phellang.integration.editor

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import org.phellang.editor.paredit.PhelSurroundDescriptor
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.files.PhelFile

class PhelSurroundDescriptorTest : PhelIntegrationTestCase() {

    private val descriptor = PhelSurroundDescriptor()

    private var fileIndex = 0

    private fun parse(text: String): PhelFile =
        myFixture.configureByText("s${fileIndex++}.phel", text) as PhelFile

    private fun elementsIn(text: String, selection: String): Array<PsiElement> {
        val file = parse(text)
        val start = text.indexOf(selection)
        return descriptor.getElementsToSurround(file, start, start + selection.length)
    }

    fun testOffersListVectorAndMap() {
        val descriptions = descriptor.surrounders.map { it.templateDescription }

        assertEquals(listOf("( ) list", "[ ] vector", "{ } map"), descriptions)
    }

    fun testFindsTheWholeFormsInASelection() {
        val elements = elementsIn("(f 1 2 3)\n", "1 2")

        assertEquals(listOf("1", "2"), elements.map(PsiElement::getText))
    }

    fun testFindsASingleSelectedForm() {
        val elements = elementsIn("(f 1 2 3)\n", "2")

        assertEquals(listOf("2"), elements.map(PsiElement::getText))
    }

    /** A selection cutting through a form has no valid surround: wrapping would unbalance it. */
    fun testIgnoresAPartiallySelectedForm() {
        val elements = elementsIn("(f abc def)\n", "bc de")

        assertEmpty(elements.toList())
    }

    fun testFindsTopLevelFormsAtFileLevel() {
        val elements = elementsIn("(f 1)\n(g 2)\n", "(f 1)\n(g 2)")

        assertEquals(listOf("(f 1)", "(g 2)"), elements.map(PsiElement::getText))
    }

    fun testOffersNothingForAnEmptySelection() {
        val file = parse("(f 1)\n")

        assertEmpty(descriptor.getElementsToSurround(file, 3, 3).toList())
    }

    fun testWrapsTheSelectionInTheChosenBrackets() {
        val file = parse("(f 1 2 3)\n")
        val start = file.text.indexOf("1 2")
        val elements = descriptor.getElementsToSurround(file, start, start + 3)
        val surrounder = descriptor.surrounders.first()

        WriteCommandAction.runWriteCommandAction(project) {
            surrounder.surroundElements(project, myFixture.editor, elements)
        }

        assertEquals("(f (1 2) 3)\n", myFixture.editor.document.text)
    }

    fun testWrapsWithAVector() {
        val file = parse("(f 1 2 3)\n")
        val start = file.text.indexOf("1 2")
        val elements = descriptor.getElementsToSurround(file, start, start + 3)

        WriteCommandAction.runWriteCommandAction(project) {
            descriptor.surrounders[1].surroundElements(project, myFixture.editor, elements)
        }

        assertEquals("(f [1 2] 3)\n", myFixture.editor.document.text)
    }

    fun testLeavesOtherSurroundersAvailable() {
        assertFalse(descriptor.isExclusive)
    }
}
