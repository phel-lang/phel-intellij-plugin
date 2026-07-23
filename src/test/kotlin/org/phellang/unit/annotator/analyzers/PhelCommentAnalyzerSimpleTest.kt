package org.phellang.unit.annotator.analyzers

import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.annotator.analyzers.PhelCommentAnalyzer
import org.phellang.language.psi.PhelHashFn

/**
 * Unit coverage of `isInsideAnonFunction`'s ancestor walk, in isolation from PSI. Mockito returns a
 * null `containingFile`, so these deliberately bypass the `#(`-in-file fast path — that branch needs
 * a real file and is covered by PhelAnonFunctionDetectionTest.
 */
@ExtendWith(MockitoExtension::class)
class PhelCommentAnalyzerSimpleTest {

    @Mock
    private lateinit var element: PsiElement

    @Mock
    private lateinit var hashFn: PhelHashFn

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 5, 20])
    fun `returns true when a PhelHashFn is an ancestor at any depth`(intermediateDepth: Int) {
        // element -> [intermediateDepth plain parents] -> hashFn
        var deepest = element
        repeat(intermediateDepth) {
            val link = mock(PsiElement::class.java)
            `when`(deepest.parent).thenReturn(link)
            deepest = link
        }
        `when`(deepest.parent).thenReturn(hashFn)

        assertTrue(PhelCommentAnalyzer.isInsideAnonFunction(element))
    }

    @Test
    fun `returns false when the element has no parent`() {
        `when`(element.parent).thenReturn(null)

        assertFalse(PhelCommentAnalyzer.isInsideAnonFunction(element))
    }

    @Test
    fun `returns false when no ancestor is a PhelHashFn`() {
        val p1 = mock(PsiElement::class.java)
        val p2 = mock(PsiElement::class.java)
        `when`(element.parent).thenReturn(p1)
        `when`(p1.parent).thenReturn(p2)
        `when`(p2.parent).thenReturn(null)

        assertFalse(PhelCommentAnalyzer.isInsideAnonFunction(element))
    }
}
