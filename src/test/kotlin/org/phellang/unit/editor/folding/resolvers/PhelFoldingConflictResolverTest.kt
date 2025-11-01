package org.phellang.unit.editor.folding.resolvers

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.editor.folding.resolvers.PhelFoldingConflictResolver
import org.phellang.language.psi.*

class PhelFoldingConflictResolverTest {

    @Test
    fun `removeConflictingDescriptors should keep non-conflicting descriptors`() {
        val node1 = createMockNode(mock(PhelList::class.java))
        val node2 = createMockNode(mock(PhelVec::class.java))

        val descriptor1 = FoldingDescriptor(node1, TextRange(0, 10), null, "(...)")
        val descriptor2 = FoldingDescriptor(node2, TextRange(20, 30), null, "[...]")

        val descriptors = listOf(descriptor1, descriptor2)

        val result = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)

        assertEquals(2, result.size)
        assertTrue(result.contains(descriptor1))
        assertTrue(result.contains(descriptor2))
    }

    @Test
    fun `removeConflictingDescriptors should prioritize outer list over inner vector`() {
        val outerList = mock(PhelList::class.java)
        val innerVec = mock(PhelVec::class.java)

        val outerNode = createMockNode(outerList)
        val innerNode = createMockNode(innerVec)

        val outerDescriptor = FoldingDescriptor(outerNode, TextRange(0, 50), null, "(...)")
        val innerDescriptor = FoldingDescriptor(innerNode, TextRange(10, 30), null, "[...]")

        val descriptors = listOf(outerDescriptor, innerDescriptor)

        val result = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)

        assertEquals(1, result.size)
        assertTrue(result.contains(outerDescriptor))
        assertFalse(result.contains(innerDescriptor))
    }

    @Test
    fun `removeConflictingDescriptors should prioritize outer list over inner map`() {
        val outerList = mock(PhelList::class.java)
        val innerMap = mock(PhelMap::class.java)

        val outerNode = createMockNode(outerList)
        val innerNode = createMockNode(innerMap)

        val outerDescriptor = FoldingDescriptor(outerNode, TextRange(0, 50), null, "(...)")
        val innerDescriptor = FoldingDescriptor(innerNode, TextRange(10, 30), null, "{...}")

        val descriptors = listOf(outerDescriptor, innerDescriptor)

        val result = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)

        assertEquals(1, result.size)
        assertTrue(result.contains(outerDescriptor))
        assertFalse(result.contains(innerDescriptor))
    }

    @Test
    fun `removeConflictingDescriptors should handle multiple conflicts correctly`() {
        val outerList = mock(PhelList::class.java)
        val innerVec1 = mock(PhelVec::class.java)
        val innerVec2 = mock(PhelVec::class.java)
        val separateList = mock(PhelList::class.java)

        val outerNode = createMockNode(outerList)
        val innerNode1 = createMockNode(innerVec1)
        val innerNode2 = createMockNode(innerVec2)
        val separateNode = createMockNode(separateList)

        val outerDescriptor = FoldingDescriptor(outerNode, TextRange(0, 100), null, "(...)")
        val innerDescriptor1 = FoldingDescriptor(innerNode1, TextRange(10, 30), null, "[...]")
        val innerDescriptor2 = FoldingDescriptor(innerNode2, TextRange(40, 60), null, "[...]")
        val separateDescriptor = FoldingDescriptor(separateNode, TextRange(200, 300), null, "(...)")

        val descriptors = listOf(outerDescriptor, innerDescriptor1, innerDescriptor2, separateDescriptor)

        val result = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)

        assertEquals(2, result.size)
        assertTrue(result.contains(outerDescriptor))
        assertTrue(result.contains(separateDescriptor))
        assertFalse(result.contains(innerDescriptor1))
        assertFalse(result.contains(innerDescriptor2))
    }

    @Test
    fun `conflict resolver should be consistent across multiple calls`() {
        val outerList = mock(PhelList::class.java)
        val innerVec = mock(PhelVec::class.java)

        val outerNode = createMockNode(outerList)
        val innerNode = createMockNode(innerVec)

        val outerDescriptor = FoldingDescriptor(outerNode, TextRange(0, 50), null, "(...)")
        val innerDescriptor = FoldingDescriptor(innerNode, TextRange(10, 30), null, "[...]")

        val descriptors = listOf(outerDescriptor, innerDescriptor)

        val result1 = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)
        val result2 = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)
        val result3 = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)

        assertEquals(result1.size, result2.size)
        assertEquals(result2.size, result3.size)
        assertEquals(1, result1.size) // Should consistently keep only the outer descriptor
    }

    private fun createMockNode(psiElement: com.intellij.psi.PsiElement): ASTNode {
        val node = mock(ASTNode::class.java)
        `when`(node.psi).thenReturn(psiElement)
        return node
    }
}
