package org.phellang.unit.annotator.analyzers

import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.annotator.analyzers.PhelCommentAnalyzer
import org.phellang.language.psi.PhelHashFn

@ExtendWith(MockitoExtension::class)
class PhelCommentAnalyzerSimpleTest {

    @Mock
    private lateinit var mockElement: PsiElement

    @Mock
    private lateinit var mockParent: PsiElement

    @Mock
    private lateinit var mockGrandParent: PsiElement

    @Mock
    private lateinit var mockHashFn: PhelHashFn

    @Test
    fun `isInsideAnonFunction should return true when element is directly inside PhelHashFn`() {
        `when`(mockElement.parent).thenReturn(mockHashFn)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertTrue(result)
    }

    @Test
    fun `isInsideAnonFunction should return true when element is nested inside PhelHashFn`() {
        // Setup: element -> parent -> hashFn
        `when`(mockElement.parent).thenReturn(mockParent)
        `when`(mockParent.parent).thenReturn(mockHashFn)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertTrue(result)
    }

    @Test
    fun `isInsideAnonFunction should return true when element is deeply nested inside PhelHashFn`() {
        // Setup: element -> parent -> grandparent -> hashFn
        `when`(mockElement.parent).thenReturn(mockParent)
        `when`(mockParent.parent).thenReturn(mockGrandParent)
        `when`(mockGrandParent.parent).thenReturn(mockHashFn)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertTrue(result)
    }

    @Test
    fun `isInsideAnonFunction should return false when element has no parent`() {
        `when`(mockElement.parent).thenReturn(null)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertFalse(result)
    }

    @Test
    fun `isInsideAnonFunction should return false when element is not inside PhelHashFn`() {
        // Setup: element -> parent -> grandparent -> null (no short function in hierarchy)
        `when`(mockElement.parent).thenReturn(mockParent)
        `when`(mockParent.parent).thenReturn(mockGrandParent)
        `when`(mockGrandParent.parent).thenReturn(null)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertFalse(result)
    }

    @Test
    fun `isInsideAnonFunction should return false when parent chain contains only regular elements`() {
        val mockRegularElement1 = mock(PsiElement::class.java)
        val mockRegularElement2 = mock(PsiElement::class.java)

        // Setup: element -> regular1 -> regular2 -> null
        `when`(mockElement.parent).thenReturn(mockRegularElement1)
        `when`(mockRegularElement1.parent).thenReturn(mockRegularElement2)
        `when`(mockRegularElement2.parent).thenReturn(null)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertFalse(result)
    }

    @Test
    fun `isInsideAnonFunction should handle complex parent hierarchies`() {
        val mockIntermediateElement1 = mock(PsiElement::class.java)
        val mockIntermediateElement2 = mock(PsiElement::class.java)
        val mockIntermediateElement3 = mock(PsiElement::class.java)

        // Setup: element -> intermediate1 -> intermediate2 -> intermediate3 -> hashFn
        `when`(mockElement.parent).thenReturn(mockIntermediateElement1)
        `when`(mockIntermediateElement1.parent).thenReturn(mockIntermediateElement2)
        `when`(mockIntermediateElement2.parent).thenReturn(mockIntermediateElement3)
        `when`(mockIntermediateElement3.parent).thenReturn(mockHashFn)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertTrue(result)
    }

    @Test
    fun `isInsideAnonFunction should stop at first PhelHashFn found`() {
        // Setup: element -> hashFn (should stop at first one)
        `when`(mockElement.parent).thenReturn(mockHashFn)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertTrue(result)
    }

    @Test
    fun `isInsideAnonFunction should handle mixed element types in hierarchy`() {
        val mockMixedElement1 = mock(PsiElement::class.java)
        val mockMixedElement2 = mock(PsiElement::class.java)

        // Setup: element -> mixed1 -> mixed2 -> hashFn -> mixed3
        `when`(mockElement.parent).thenReturn(mockMixedElement1)
        `when`(mockMixedElement1.parent).thenReturn(mockMixedElement2)
        `when`(mockMixedElement2.parent).thenReturn(mockHashFn)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertTrue(result)
    }

    @Test
    fun `isInsideAnonFunction should handle null parent chain gracefully`() {
        // Test with element that has null parent
        `when`(mockElement.parent).thenReturn(null)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertFalse(result)
    }

    @Test
    fun `isInsideAnonFunction should handle very deep nesting`() {
        // Create a deep hierarchy: element -> p1 -> p2 -> p3 -> p4 -> p5 -> hashFn
        val parents = (1..5).map { mock(PsiElement::class.java) }

        `when`(mockElement.parent).thenReturn(parents[0])
        for (i in 0 until parents.size - 1) {
            `when`(parents[i].parent).thenReturn(parents[i + 1])
        }
        `when`(parents.last().parent).thenReturn(mockHashFn)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        assertTrue(result)
    }

    @Test
    fun `isInsideAnonFunction should be performant with deep hierarchies`() {
        // Test performance with very deep nesting (100 levels)
        val parents = (1..100).map { mock(PsiElement::class.java) }

        `when`(mockElement.parent).thenReturn(parents[0])
        for (i in 0 until parents.size - 1) {
            `when`(parents[i].parent).thenReturn(parents[i + 1])
        }
        `when`(parents.last().parent).thenReturn(mockHashFn)

        val startTime = System.nanoTime()
        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)
        val endTime = System.nanoTime()

        assertTrue(result)
        // Should complete in reasonable time (less than 10ms)
        assertTrue((endTime - startTime) < 10_000_000, "Should complete quickly even with deep nesting")
    }

    @Test
    fun `isInsideAnonFunction should handle multiple short functions in hierarchy`() {
        // Setup: element -> hashFn (should stop at first one)
        `when`(mockElement.parent).thenReturn(mockHashFn)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        // Should return true for the first short function found
        assertTrue(result)
    }

    @Test
    fun `isInsideAnonFunction should be consistent across multiple calls`() {
        `when`(mockElement.parent).thenReturn(mockHashFn)

        // Call multiple times to ensure consistency
        repeat(5) {
            assertTrue(PhelCommentAnalyzer.isInsideAnonFunction(mockElement))
        }
    }

    @Test
    fun `isInsideAnonFunction should handle long chains without short functions`() {
        // Test a long chain without any short functions
        val parents = (1..20).map { mock(PsiElement::class.java) }

        `when`(mockElement.parent).thenReturn(parents[0])
        for (i in 0 until parents.size - 1) {
            `when`(parents[i].parent).thenReturn(parents[i + 1])
        }
        `when`(parents.last().parent).thenReturn(null)

        val result = PhelCommentAnalyzer.isInsideAnonFunction(mockElement)

        // Should return false since no PhelHashFn is found
        assertFalse(result)
    }
}
