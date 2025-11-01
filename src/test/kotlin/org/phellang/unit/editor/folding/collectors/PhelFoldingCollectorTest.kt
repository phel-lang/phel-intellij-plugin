package org.phellang.unit.editor.folding.collectors

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.editor.folding.collectors.PhelFoldingCollector
import org.phellang.language.psi.*

class PhelFoldingCollectorTest {

    private lateinit var collector: PhelFoldingCollector
    private lateinit var mockDocument: Document

    @BeforeEach
    fun setUp() {
        collector = PhelFoldingCollector()
        mockDocument = mock(Document::class.java)
    }

    @Test
    fun `collectFoldingDescriptors should return empty list for non-foldable node`() {
        val mockSymbol = mock(PhelSymbol::class.java)
        val mockNode = createMockNode(mockSymbol, TextRange(0, 5))
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        val result = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `collectFoldingDescriptors should collect folding descriptor for valid list`() {
        val mockList = mock(PhelList::class.java)
        val mockNode = createMockNode(mockList, TextRange(0, 50))
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document to return different lines for multi-line validation
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(50)).thenReturn(2)

        val result = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertEquals(1, result.size)
        assertEquals(TextRange(0, 50), result[0].range)
    }

    @Test
    fun `collectFoldingDescriptors should collect folding descriptor for valid vector`() {
        val mockVec = mock(PhelVec::class.java)
        val mockNode = createMockNode(mockVec, TextRange(0, 30))
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document to return different lines for multi-line validation
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(30)).thenReturn(1)

        val result = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertEquals(1, result.size)
        assertEquals(TextRange(0, 30), result[0].range)
        assertEquals("[...]", result[0].placeholderText)
    }

    @Test
    fun `collectFoldingDescriptors should collect folding descriptor for valid map`() {
        val mockMap = mock(PhelMap::class.java)
        val mockNode = createMockNode(mockMap, TextRange(0, 40))
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document to return different lines for multi-line validation
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(40)).thenReturn(2)

        val result = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertEquals(1, result.size)
        assertEquals(TextRange(0, 40), result[0].range)
        assertEquals("{...}", result[0].placeholderText)
    }

    @Test
    fun `collectFoldingDescriptors should collect folding descriptor for valid comment form`() {
        val mockComment = mock(PhelFormCommentMacro::class.java)
        val mockNode = createMockNode(mockComment, TextRange(0, 25))
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document to return different lines for multi-line validation
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(25)).thenReturn(1)

        val result = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertEquals(1, result.size)
        assertEquals(TextRange(0, 25), result[0].range)
        assertEquals("#_...", result[0].placeholderText)
    }

    @Test
    fun `collectFoldingDescriptors should not collect descriptor for single-line comment form`() {
        val mockComment = mock(PhelFormCommentMacro::class.java)
        val mockNode = createMockNode(mockComment, TextRange(0, 20))
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document to return same line for single-line validation
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(20)).thenReturn(0)

        val result = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `collectFoldingDescriptors should not collect descriptor for short ranges`() {
        val mockList = mock(PhelList::class.java)
        val mockNode = createMockNode(mockList, TextRange(0, 10)) // Below minimum length
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document to return different lines
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(10)).thenReturn(1)

        val result = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `collectFoldingDescriptors should recursively process children`() {
        val mockList = mock(PhelList::class.java)
        val mockVec = mock(PhelVec::class.java)

        val parentNode = createMockNode(mockList, TextRange(0, 100))
        val childNode = createMockNode(mockVec, TextRange(10, 50))

        `when`(parentNode.getChildren(null)).thenReturn(arrayOf(childNode))
        `when`(childNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        `when`(mockDocument.getLineNumber(anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 25 // Simple line calculation
        }

        val result = collector.collectFoldingDescriptors(parentNode, mockDocument)

        assertEquals(2, result.size) // Both parent and child should be collected

        val ranges = result.map { it.range }
        assertTrue(ranges.contains(TextRange(0, 100)))
        assertTrue(ranges.contains(TextRange(10, 50)))
    }

    @Test
    fun `collectFoldingDescriptors should handle mixed PSI element types`() {
        val mockList = mock(PhelList::class.java)
        val mockVec = mock(PhelVec::class.java)
        val mockMap = mock(PhelMap::class.java)
        val mockComment = mock(PhelFormCommentMacro::class.java)

        val parentNode = createMockNode(mockList, TextRange(0, 200))
        val vecNode = createMockNode(mockVec, TextRange(10, 50))
        val mapNode = createMockNode(mockMap, TextRange(60, 100))
        val commentNode = createMockNode(mockComment, TextRange(110, 150))

        `when`(parentNode.getChildren(null)).thenReturn(arrayOf(vecNode, mapNode, commentNode))
        `when`(vecNode.getChildren(null)).thenReturn(emptyArray())
        `when`(mapNode.getChildren(null)).thenReturn(emptyArray())
        `when`(commentNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        `when`(mockDocument.getLineNumber(anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 30 // Simple line calculation
        }

        val result = collector.collectFoldingDescriptors(parentNode, mockDocument)

        assertEquals(4, result.size) // All elements should be collected

        val placeholders = result.map { it.placeholderText }
        assertTrue(placeholders.contains("(...)"))
        assertTrue(placeholders.contains("[...]"))
        assertTrue(placeholders.contains("{...}"))
        assertTrue(placeholders.contains("#_..."))
    }

    @Test
    fun `collectFoldingDescriptors should handle empty children arrays`() {
        val mockList = mock(PhelList::class.java)
        val mockNode = createMockNode(mockList, TextRange(0, 30))
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(30)).thenReturn(1)

        val result = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertEquals(1, result.size)
        assertEquals(TextRange(0, 30), result[0].range)
    }

    @Test
    fun `collector should be reusable across multiple calls`() {
        val mockList = mock(PhelList::class.java)
        val mockNode = createMockNode(mockList, TextRange(0, 25))
        `when`(mockNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        `when`(mockDocument.getLineNumber(0)).thenReturn(0)
        `when`(mockDocument.getLineNumber(25)).thenReturn(1)

        val result1 = collector.collectFoldingDescriptors(mockNode, mockDocument)
        val result2 = collector.collectFoldingDescriptors(mockNode, mockDocument)

        assertEquals(result1.size, result2.size)
        assertEquals(1, result1.size)
        assertEquals(1, result2.size)
    }

    @Test
    fun `collectFoldingDescriptors should handle deeply nested structures`() {
        val mockList1 = mock(PhelList::class.java)
        val mockList2 = mock(PhelList::class.java)
        val mockVec = mock(PhelVec::class.java)

        val level1Node = createMockNode(mockList1, TextRange(0, 100))
        val level2Node = createMockNode(mockList2, TextRange(10, 80))
        val level3Node = createMockNode(mockVec, TextRange(20, 60))

        `when`(level1Node.getChildren(null)).thenReturn(arrayOf(level2Node))
        `when`(level2Node.getChildren(null)).thenReturn(arrayOf(level3Node))
        `when`(level3Node.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        `when`(mockDocument.getLineNumber(anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 20 // Simple line calculation
        }

        val result = collector.collectFoldingDescriptors(level1Node, mockDocument)

        assertEquals(3, result.size) // All three levels should be collected

        val ranges = result.map { it.range }
        assertTrue(ranges.contains(TextRange(0, 100)))
        assertTrue(ranges.contains(TextRange(10, 80)))
        assertTrue(ranges.contains(TextRange(20, 60)))
    }

    private fun createMockNode(psiElement: com.intellij.psi.PsiElement, textRange: TextRange): ASTNode {
        val node = mock(ASTNode::class.java)
        `when`(node.psi).thenReturn(psiElement)

        // Mock the textRange property and node property for PSI elements
        when (psiElement) {
            is PhelList -> {
                `when`(psiElement.textRange).thenReturn(textRange)
                `when`(psiElement.node).thenReturn(node)
            }

            is PhelVec -> {
                `when`(psiElement.textRange).thenReturn(textRange)
                `when`(psiElement.node).thenReturn(node)
            }

            is PhelMap -> {
                `when`(psiElement.textRange).thenReturn(textRange)
                `when`(psiElement.node).thenReturn(node)
            }

            is PhelFormCommentMacro -> {
                `when`(psiElement.textRange).thenReturn(textRange)
                `when`(psiElement.node).thenReturn(node)
            }

            is PhelSymbol -> {
                `when`(psiElement.textRange).thenReturn(textRange)
                `when`(psiElement.node).thenReturn(node)
            }
        }

        return node
    }
}
