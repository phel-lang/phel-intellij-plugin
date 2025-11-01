package org.phellang.integration.editor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.phellang.editor.PhelFoldingBuilder
import org.phellang.editor.folding.collectors.PhelFoldingCollector
import org.phellang.editor.folding.placeholders.PhelPlaceholderGenerator
import org.phellang.editor.folding.resolvers.PhelFoldingConflictResolver
import org.phellang.editor.folding.validators.PhelFoldingValidator
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelFormCommentMacro
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec

class PhelFoldingIntegrationTest {

    private lateinit var foldingBuilder: PhelFoldingBuilder
    private lateinit var collector: PhelFoldingCollector
    private lateinit var mockDocument: Document

    @BeforeEach
    fun setUp() {
        foldingBuilder = PhelFoldingBuilder()
        collector = PhelFoldingCollector()
        mockDocument = Mockito.mock(Document::class.java)
    }

    @Test
    fun `collector and conflict resolver should work together`() {
        // Create mock PSI elements
        val outerList = Mockito.mock(PhelList::class.java)
        val innerVec = Mockito.mock(PhelVec::class.java)

        val outerNode = createMockNode(outerList, TextRange(0, 100))
        val innerNode = createMockNode(innerVec, TextRange(10, 50))

        Mockito.`when`(outerNode.getChildren(null)).thenReturn(arrayOf(innerNode))
        Mockito.`when`(innerNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(ArgumentMatchers.anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 25 // Simple line calculation
        }

        // Collect descriptors
        val descriptors = collector.collectFoldingDescriptors(outerNode, mockDocument)
        Assertions.assertEquals(2, descriptors.size)

        // Resolve conflicts
        val resolved = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)
        Assertions.assertEquals(1, resolved.size)
        Assertions.assertEquals(TextRange(0, 100), resolved[0].range) // Should keep outer list
    }

    @Test
    fun `full folding builder integration should prioritize binding constructs`() {
        // Create a binding construct (let) with inner vector
        val letList = createMockListWithFirstSymbol("let")
        val bindingVec = Mockito.mock(PhelVec::class.java)

        val letNode = createMockNode(letList, TextRange(0, 200))
        val vecNode = createMockNode(bindingVec, TextRange(10, 100))

        Mockito.`when`(letNode.getChildren(null)).thenReturn(arrayOf(vecNode))
        Mockito.`when`(vecNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(ArgumentMatchers.anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 50 // Simple line calculation
        }

        val result = foldingBuilder.buildFoldRegions(letNode, mockDocument)

        // Should prioritize the let binding over inner vector
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(TextRange(0, 200), result[0].range)
        Assertions.assertTrue(result[0].placeholderText?.startsWith("(let") == true)
    }

    @Test
    fun `folding builder should handle namespace declarations correctly`() {
        val nsList = createMockListWithFirstSymbol("ns")
        val nsNode = createMockNode(nsList, TextRange(0, 80))
        Mockito.`when`(nsNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(0)).thenReturn(0)
        Mockito.`when`(mockDocument.getLineNumber(80)).thenReturn(3)

        val result = foldingBuilder.buildFoldRegions(nsNode, mockDocument)

        Assertions.assertEquals(1, result.size)
        Assertions.assertTrue(foldingBuilder.isCollapsedByDefault(result[0].element))
        Assertions.assertTrue(result[0].placeholderText?.startsWith("(ns") == true)
    }

    @Test
    fun `folding builder should handle multiple non-conflicting elements`() {
        val list1 = Mockito.mock(PhelList::class.java)
        val list2 = Mockito.mock(PhelList::class.java)
        val vec1 = Mockito.mock(PhelVec::class.java)

        val node1 = createMockNode(list1, TextRange(0, 50))
        val node2 = createMockNode(list2, TextRange(100, 150))
        val node3 = createMockNode(vec1, TextRange(200, 250))

        Mockito.`when`(node1.getChildren(null)).thenReturn(arrayOf(node2, node3))
        Mockito.`when`(node2.getChildren(null)).thenReturn(emptyArray())
        Mockito.`when`(node3.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(ArgumentMatchers.anyInt())).thenAnswer { invocation ->
            val offset = invocation.getArgument<Int>(0)
            offset / 25 // Simple line calculation
        }

        val result = foldingBuilder.buildFoldRegions(node1, mockDocument)

        // All three should be kept as they don't conflict
        Assertions.assertEquals(3, result.size)
        val ranges = result.map { it.range }
        Assertions.assertTrue(ranges.contains(TextRange(0, 50)))
        Assertions.assertTrue(ranges.contains(TextRange(100, 150)))
        Assertions.assertTrue(ranges.contains(TextRange(200, 250)))
    }

    @Test
    fun `component integration should handle comment forms correctly`() {
        val commentForm = Mockito.mock(PhelFormCommentMacro::class.java)
        val commentNode = createMockNode(commentForm, TextRange(0, 30))
        Mockito.`when`(commentNode.getChildren(null)).thenReturn(emptyArray())

        // Mock document for multi-line validation
        Mockito.`when`(mockDocument.getLineNumber(0)).thenReturn(0)
        Mockito.`when`(mockDocument.getLineNumber(30)).thenReturn(1)

        val descriptors = collector.collectFoldingDescriptors(commentNode, mockDocument)
        Assertions.assertEquals(1, descriptors.size)
        Assertions.assertEquals("#_...", descriptors[0].placeholderText)

        // Should not be collapsed by default
        Assertions.assertFalse(foldingBuilder.isCollapsedByDefault(descriptors[0].element))
    }

    @Test
    fun `validator should reject ranges that are too short or single-line`() {
        // Test short range
        val shortRange = TextRange(0, 10)
        Mockito.`when`(mockDocument.getLineNumber(0)).thenReturn(0)
        Mockito.`when`(mockDocument.getLineNumber(10)).thenReturn(1)

        Assertions.assertFalse(PhelFoldingValidator.isValidFoldingRange(shortRange, mockDocument))

        // Test single-line range
        val singleLineRange = TextRange(0, 50)
        Mockito.`when`(mockDocument.getLineNumber(0)).thenReturn(0)
        Mockito.`when`(mockDocument.getLineNumber(50)).thenReturn(0) // Same line

        Assertions.assertFalse(PhelFoldingValidator.isValidFoldingRange(singleLineRange, mockDocument))
    }

    @Test
    fun `placeholder generator should provide contextual text for defining forms`() {
        val defnList = createMockListWithFirstSymbol("defn")
        val defList = createMockListWithFirstSymbol("def")
        val nsListWithName = createMockNsSymbol()

        Assertions.assertTrue(PhelPlaceholderGenerator.generateListPlaceholder(defnList).startsWith("(defn"))
        Assertions.assertTrue(PhelPlaceholderGenerator.generateListPlaceholder(defList).startsWith("(def"))
        Assertions.assertTrue(PhelPlaceholderGenerator.generateListPlaceholder(nsListWithName).contains("my-namespace"))
    }

    @Test
    fun `conflict resolver should handle complex nested scenarios`() {
        // Create a complex scenario: outer list with multiple inner elements
        val outerList = Mockito.mock(PhelList::class.java)
        val innerVec1 = Mockito.mock(PhelVec::class.java)
        val innerVec2 = Mockito.mock(PhelVec::class.java)
        val innerMap = Mockito.mock(PhelMap::class.java)

        val outerDescriptor =
            FoldingDescriptor(createMockNode(outerList, TextRange(0, 200)), TextRange(0, 200), null, "(...)")
        val vec1Descriptor =
            FoldingDescriptor(createMockNode(innerVec1, TextRange(10, 50)), TextRange(10, 50), null, "[...]")
        val vec2Descriptor =
            FoldingDescriptor(createMockNode(innerVec2, TextRange(60, 100)), TextRange(60, 100), null, "[...]")
        val mapDescriptor =
            FoldingDescriptor(createMockNode(innerMap, TextRange(110, 150)), TextRange(110, 150), null, "{...}")

        val descriptors = listOf(outerDescriptor, vec1Descriptor, vec2Descriptor, mapDescriptor)
        val resolved = PhelFoldingConflictResolver.removeConflictingDescriptors(descriptors)

        // Should keep only the outer list
        Assertions.assertEquals(1, resolved.size)
        Assertions.assertEquals(TextRange(0, 200), resolved[0].range)
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
        Mockito.`when`(form.children).thenReturn(arrayOf(symbol))
        Mockito.`when`(list.children).thenReturn(arrayOf(form))

        return list
    }

    private fun createMockNsSymbol(): PhelList {
        val list = Mockito.mock(PhelList::class.java)
        val form1 = Mockito.mock(PhelForm::class.java)
        val form2 = Mockito.mock(PhelForm::class.java)
        val symbol1 = Mockito.mock(PhelSymbol::class.java)
        val symbol2 = Mockito.mock(PhelSymbol::class.java)

        Mockito.`when`(symbol1.text).thenReturn("ns")
        Mockito.`when`(symbol2.text).thenReturn("my-namespace")
        Mockito.`when`(form1.children).thenReturn(arrayOf(symbol1))
        Mockito.`when`(form2.children).thenReturn(arrayOf(symbol2))
        Mockito.`when`(list.children).thenReturn(arrayOf(form1, form2))

        return list
    }
}
