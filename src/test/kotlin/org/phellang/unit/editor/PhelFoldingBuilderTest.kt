package org.phellang.unit.editor

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.phellang.editor.PhelFoldingBuilder
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelFormCommentMacro
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec

class PhelFoldingBuilderTest {

    private lateinit var foldingBuilder: PhelFoldingBuilder
    private lateinit var mockDocument: Document

    @BeforeEach
    fun setUp() {
        foldingBuilder = PhelFoldingBuilder()
        mockDocument = Mockito.mock(Document::class.java)
    }

    @Test
    fun `buildFoldRegions should return empty array for non-foldable elements`() {
        val mockSymbol = Mockito.mock(PhelSymbol::class.java)
        val mockNode = createMockNode(mockSymbol, TextRange(0, 5))
        Mockito.`when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        val result = foldingBuilder.buildFoldRegions(mockNode, mockDocument)

        Assertions.assertEquals(0, result.size)
    }

    @Test
    fun `buildFoldRegions should return folding descriptor for valid list`() {
        val mockList = Mockito.mock(PhelList::class.java)
        val mockNode = createMockNode(mockList, TextRange(0, 50))
        Mockito.`when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(0)).thenReturn(0)
        Mockito.`when`(mockDocument.getLineNumber(50)).thenReturn(2)

        val result = foldingBuilder.buildFoldRegions(mockNode, mockDocument)

        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(TextRange(0, 50), result[0].range)
    }

    @Test
    fun `buildFoldRegions should filter out conflicting descriptors`() {
        val mockList = Mockito.mock(PhelList::class.java)
        val mockVec = Mockito.mock(PhelVec::class.java)

        val parentNode = createMockNode(mockList, TextRange(0, 100))
        val childNode = createMockNode(mockVec, TextRange(10, 50))

        Mockito.`when`(parentNode.getChildren(null)).thenReturn(arrayOf(childNode))
        Mockito.`when`(childNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(ArgumentMatchers.anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 25 // Simple line calculation
        }

        val result = foldingBuilder.buildFoldRegions(parentNode, mockDocument)

        // Should prioritize outer list over inner vector
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(TextRange(0, 100), result[0].range)
    }

    @Test
    fun `buildFoldRegions should handle multiple non-conflicting elements`() {
        val mockList1 = Mockito.mock(PhelList::class.java)
        val mockList2 = Mockito.mock(PhelList::class.java)

        val parentNode = createMockNode(mockList1, TextRange(0, 50))
        val siblingNode = createMockNode(mockList2, TextRange(100, 150))

        Mockito.`when`(parentNode.getChildren(null)).thenReturn(arrayOf(siblingNode))
        Mockito.`when`(siblingNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(ArgumentMatchers.anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 25 // Simple line calculation
        }

        val result = foldingBuilder.buildFoldRegions(parentNode, mockDocument)

        // Both should be kept as they don't conflict
        Assertions.assertEquals(2, result.size)
        val ranges = result.map { it.range }
        Assertions.assertTrue(ranges.contains(TextRange(0, 50)))
        Assertions.assertTrue(ranges.contains(TextRange(100, 150)))
    }

    @Test
    fun `getPlaceholderText should return correct placeholder for PhelList`() {
        val mockList = Mockito.mock(PhelList::class.java)
        val mockNode = createMockNode(mockList, TextRange(0, 20))

        val result = foldingBuilder.getPlaceholderText(mockNode)

        Assertions.assertEquals("(...)", result)
    }

    @Test
    fun `getPlaceholderText should return correct placeholder for PhelVec`() {
        val mockVec = Mockito.mock(PhelVec::class.java)
        val mockNode = createMockNode(mockVec, TextRange(0, 20))

        val result = foldingBuilder.getPlaceholderText(mockNode)

        Assertions.assertEquals("[...]", result)
    }

    @Test
    fun `getPlaceholderText should return correct placeholder for PhelMap`() {
        val mockMap = Mockito.mock(PhelMap::class.java)
        val mockNode = createMockNode(mockMap, TextRange(0, 20))

        val result = foldingBuilder.getPlaceholderText(mockNode)

        Assertions.assertEquals("{...}", result)
    }

    @Test
    fun `getPlaceholderText should return correct placeholder for PhelFormCommentMacro`() {
        val mockComment = Mockito.mock(PhelFormCommentMacro::class.java)
        val mockNode = createMockNode(mockComment, TextRange(0, 20))

        val result = foldingBuilder.getPlaceholderText(mockNode)

        Assertions.assertEquals("#_...", result)
    }

    @Test
    fun `getPlaceholderText should return generic placeholder for unknown elements`() {
        val mockSymbol = Mockito.mock(PhelSymbol::class.java)
        val mockNode = createMockNode(mockSymbol, TextRange(0, 20))

        val result = foldingBuilder.getPlaceholderText(mockNode)

        Assertions.assertEquals("...", result)
    }

    @Test
    fun `isCollapsedByDefault should return true for namespace declarations`() {
        val list = createMockListWithFirstSymbol("ns")
        val node = createMockNode(list, TextRange(0, 50))

        val result = foldingBuilder.isCollapsedByDefault(node)

        Assertions.assertTrue(result)
    }

    @Test
    fun `isCollapsedByDefault should return false for non-namespace forms`() {
        val list = createMockListWithFirstSymbol("defn")
        val node = createMockNode(list, TextRange(0, 50))

        val result = foldingBuilder.isCollapsedByDefault(node)

        Assertions.assertFalse(result)
    }

    @Test
    fun `isCollapsedByDefault should return false for non-list elements`() {
        val mockVec = Mockito.mock(PhelVec::class.java)
        val mockNode = createMockNode(mockVec, TextRange(0, 20))

        val result = foldingBuilder.isCollapsedByDefault(mockNode)

        Assertions.assertFalse(result)
    }

    @Test
    fun `buildFoldRegions should handle comment forms correctly`() {
        val mockComment = Mockito.mock(PhelFormCommentMacro::class.java)
        val mockNode = createMockNode(mockComment, TextRange(0, 30))
        Mockito.`when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(0)).thenReturn(0)
        Mockito.`when`(mockDocument.getLineNumber(30)).thenReturn(1)

        val result = foldingBuilder.buildFoldRegions(mockNode, mockDocument)

        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals("#_...", result[0].placeholderText)
    }

    @Test
    fun `buildFoldRegions should not fold single-line comment forms`() {
        val mockComment = Mockito.mock(PhelFormCommentMacro::class.java)
        val mockNode = createMockNode(mockComment, TextRange(0, 20))
        Mockito.`when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for single-line validation
        Mockito.`when`(mockDocument.getLineNumber(0)).thenReturn(0)
        Mockito.`when`(mockDocument.getLineNumber(20)).thenReturn(0) // Same line

        val result = foldingBuilder.buildFoldRegions(mockNode, mockDocument)

        Assertions.assertEquals(0, result.size)
    }

    @Test
    fun `buildFoldRegions should handle complex nested structures`() {
        val mockList = Mockito.mock(PhelList::class.java)
        val mockVec1 = Mockito.mock(PhelVec::class.java)
        val mockVec2 = Mockito.mock(PhelVec::class.java)
        val mockMap = Mockito.mock(PhelMap::class.java)

        val parentNode = createMockNode(mockList, TextRange(0, 200))
        val vecNode1 = createMockNode(mockVec1, TextRange(10, 50))
        val vecNode2 = createMockNode(mockVec2, TextRange(60, 100))
        val mapNode = createMockNode(mockMap, TextRange(110, 150))

        Mockito.`when`(parentNode.getChildren(null)).thenReturn(arrayOf(vecNode1, vecNode2, mapNode))
        Mockito.`when`(vecNode1.getChildren(null)).thenReturn(emptyArray())
        Mockito.`when`(vecNode2.getChildren(null)).thenReturn(emptyArray())
        Mockito.`when`(mapNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(ArgumentMatchers.anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 30 // Simple line calculation
        }

        val result = foldingBuilder.buildFoldRegions(parentNode, mockDocument)

        // Should prioritize the outer list over inner vectors/maps
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(TextRange(0, 200), result[0].range)
    }

    @Test
    fun `folding builder should be reusable across multiple calls`() {
        val mockList = Mockito.mock(PhelList::class.java)
        val mockNode = createMockNode(mockList, TextRange(0, 25))
        Mockito.`when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(0)).thenReturn(0)
        Mockito.`when`(mockDocument.getLineNumber(25)).thenReturn(1)

        val result1 = foldingBuilder.buildFoldRegions(mockNode, mockDocument)
        val result2 = foldingBuilder.buildFoldRegions(mockNode, mockDocument)

        Assertions.assertEquals(result1.size, result2.size)
        Assertions.assertEquals(1, result1.size)
        Assertions.assertEquals(1, result2.size)
        Assertions.assertEquals(result1[0].range, result2[0].range)
    }

    @Test
    fun `buildFoldRegions should handle empty document gracefully`() {
        val mockSymbol = Mockito.mock(PhelSymbol::class.java)
        val mockNode = createMockNode(mockSymbol, TextRange(0, 0))
        Mockito.`when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        val result = foldingBuilder.buildFoldRegions(mockNode, mockDocument)

        Assertions.assertEquals(0, result.size)
    }

    private fun createMockNode(psiElement: PsiElement, textRange: TextRange): ASTNode {
        val node = Mockito.mock(ASTNode::class.java)
        Mockito.`when`(node.psi).thenReturn(psiElement)

        // Mock the textRange property and node property for PSI elements
        when (psiElement) {
            is PhelList -> {
                Mockito.`when`(psiElement.textRange).thenReturn(textRange)
                Mockito.`when`(psiElement.node).thenReturn(node)
            }
            is PhelVec -> {
                Mockito.`when`(psiElement.textRange).thenReturn(textRange)
                Mockito.`when`(psiElement.node).thenReturn(node)
            }
            is PhelMap -> {
                Mockito.`when`(psiElement.textRange).thenReturn(textRange)
                Mockito.`when`(psiElement.node).thenReturn(node)
            }
            is PhelFormCommentMacro -> {
                Mockito.`when`(psiElement.textRange).thenReturn(textRange)
                Mockito.`when`(psiElement.node).thenReturn(node)
            }
            is PhelSymbol -> {
                Mockito.`when`(psiElement.textRange).thenReturn(textRange)
                Mockito.`when`(psiElement.node).thenReturn(node)
            }
        }

        return node
    }

    private fun createMockListWithFirstSymbol(symbolText: String): PhelList {
        val list = Mockito.mock(PhelList::class.java)
        val form = Mockito.mock(PhelForm::class.java)
        val symbol = Mockito.mock(PhelSymbol::class.java)

        Mockito.`when`(symbol.text).thenReturn(symbolText)

        // Mock the PSI tree structure properly
        Mockito.mockStatic(PsiTreeUtil::class.java).use { mockedPsiTreeUtil ->
            mockedPsiTreeUtil.`when`<Array<PhelForm>> {
                PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java)
            }.thenReturn(arrayOf(form))

            mockedPsiTreeUtil.`when`<PhelSymbol> {
                PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
            }.thenReturn(symbol)
        }

        return list
    }
}