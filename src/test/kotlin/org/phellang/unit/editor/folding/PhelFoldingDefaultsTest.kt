package org.phellang.unit.editor.folding

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.*
import org.phellang.editor.folding.PhelFoldingDefaults
import org.phellang.language.psi.*

class PhelFoldingDefaultsTest {

    @ParameterizedTest
    @ValueSource(strings = ["defn", "def", "let", "if", "when", "for"])
    fun `isCollapsedByDefault should return false for non-namespace forms`(formName: String) {
        val list = mock(PhelList::class.java)
        val form = mock(PhelForm::class.java)
        val symbol = mock(PhelSymbol::class.java)

        `when`(symbol.text).thenReturn(formName)
        `when`(form.children).thenReturn(arrayOf(symbol))
        `when`(list.children).thenReturn(arrayOf(form))

        withMockedPsiTreeUtil(list, form, symbol) {
            val result = PhelFoldingDefaults.isCollapsedByDefault()
            assertFalse(result)
        }
    }

    @Test
    fun `isCollapsedByDefault should return false for empty list`() {
        val list = mock(PhelList::class.java)
        `when`(list.children).thenReturn(emptyArray())

        val result = PhelFoldingDefaults.isCollapsedByDefault()

        assertFalse(result)
    }

    @Test
    fun `getDefaultPlaceholderText should return correct placeholder for PhelList`() {
        val list = mock(PhelList::class.java)
        val node = createMockNode(list)

        val result = PhelFoldingDefaults.getDefaultPlaceholderText(node)

        assertEquals("(...)", result)
    }

    @Test
    fun `getDefaultPlaceholderText should return correct placeholder for PhelVec`() {
        val vector = mock(PhelVec::class.java)
        val node = createMockNode(vector)

        val result = PhelFoldingDefaults.getDefaultPlaceholderText(node)

        assertEquals("[...]", result)
    }

    @Test
    fun `getDefaultPlaceholderText should return correct placeholder for PhelMap`() {
        val map = mock(PhelMap::class.java)
        val node = createMockNode(map)

        val result = PhelFoldingDefaults.getDefaultPlaceholderText(node)

        assertEquals("{...}", result)
    }

    @Test
    fun `getDefaultPlaceholderText should return correct placeholder for PhelFormCommentMacro`() {
        val comment = mock(PhelFormCommentMacro::class.java)
        val node = createMockNode(comment)

        val result = PhelFoldingDefaults.getDefaultPlaceholderText(node)

        assertEquals("#_...", result)
    }

    @Test
    fun `getDefaultPlaceholderText should return generic placeholder for unknown elements`() {
        val symbol = mock(PhelSymbol::class.java)
        val node = createMockNode(symbol)

        val result = PhelFoldingDefaults.getDefaultPlaceholderText(node)

        assertEquals("...", result)
    }

    @Test
    fun `defaults should be consistent across multiple calls`() {
        val result1 = PhelFoldingDefaults.isCollapsedByDefault()
        val result2 = PhelFoldingDefaults.isCollapsedByDefault()
        val result3 = PhelFoldingDefaults.isCollapsedByDefault()

        assertEquals(result1, result2)
        assertEquals(result2, result3)
        assertFalse(result1) // Nothing is collapsed by default anymore.
    }

    @Test
    fun `placeholder text should be consistent across multiple calls`() {
        val list = mock(PhelList::class.java)
        val node = createMockNode(list)

        val result1 = PhelFoldingDefaults.getDefaultPlaceholderText(node)
        val result2 = PhelFoldingDefaults.getDefaultPlaceholderText(node)
        val result3 = PhelFoldingDefaults.getDefaultPlaceholderText(node)

        assertEquals(result1, result2)
        assertEquals(result2, result3)
        assertEquals("(...)", result1)
    }

    @Test
    fun `should handle all PSI element types for placeholder text`() {
        val testCases = mapOf(
            mock(PhelList::class.java) to "(...)",
            mock(PhelVec::class.java) to "[...]",
            mock(PhelMap::class.java) to "{...}",
            mock(PhelFormCommentMacro::class.java) to "#_...",
            mock(PhelSymbol::class.java) to "...", // Generic fallback
            mock(PhelForm::class.java) to "..." // Generic fallback
        )

        testCases.forEach { (element, expectedPlaceholder) ->
            val node = createMockNode(element)
            val result = PhelFoldingDefaults.getDefaultPlaceholderText(node)
            assertEquals(expectedPlaceholder, result, "Failed for ${element::class.simpleName}")
        }
    }

    private fun createMockNode(psiElement: com.intellij.psi.PsiElement): ASTNode {
        val node = mock(ASTNode::class.java)
        `when`(node.psi).thenReturn(psiElement)
        return node
    }

    private fun withMockedPsiTreeUtil(list: PhelList, form: PhelForm, symbol: PhelSymbol, action: () -> Unit) {
        mockStatic(PsiTreeUtil::class.java).use { mockedPsiTreeUtil ->
            mockedPsiTreeUtil.`when`<Array<PhelForm>> {
                PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java)
            }.thenReturn(arrayOf(form))

            mockedPsiTreeUtil.`when`<PhelSymbol> {
                PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
            }.thenReturn(symbol)

            action()
        }
    }
}
