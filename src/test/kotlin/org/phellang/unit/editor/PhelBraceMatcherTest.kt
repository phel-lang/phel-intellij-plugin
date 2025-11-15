package org.phellang.unit.editor

import com.intellij.psi.PsiFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.editor.PhelBraceMatcher
import org.phellang.language.psi.PhelTypes

@ExtendWith(MockitoExtension::class)
class PhelBraceMatcherTest {

    @Mock
    private lateinit var mockFile: PsiFile

    private lateinit var braceMatcher: PhelBraceMatcher

    @BeforeEach
    fun setUp() {
        braceMatcher = PhelBraceMatcher()
    }

    @Test
    fun `getPairs should return all supported brace pairs`() {
        val pairs = braceMatcher.pairs

        Assertions.assertEquals(5, pairs.size)

        val pairTypes = pairs.map { it.leftBraceType to it.rightBraceType }.toSet()

        Assertions.assertTrue(pairTypes.contains(PhelTypes.PAREN1 to PhelTypes.PAREN2))
        Assertions.assertTrue(pairTypes.contains(PhelTypes.FN_SHORT to PhelTypes.PAREN2))
        Assertions.assertTrue(pairTypes.contains(PhelTypes.BRACKET1 to PhelTypes.BRACKET2))
        Assertions.assertTrue(pairTypes.contains(PhelTypes.BRACE1 to PhelTypes.BRACE2))
        Assertions.assertTrue(pairTypes.contains(PhelTypes.HASH_BRACE to PhelTypes.BRACE2))
    }

    @Test
    fun `isPairedBracesAllowedBeforeType should delegate to context analyzer`() {
        Assertions.assertTrue(braceMatcher.isPairedBracesAllowedBeforeType(PhelTypes.PAREN1, null))

        Assertions.assertFalse(braceMatcher.isPairedBracesAllowedBeforeType(PhelTypes.PAREN1, PhelTypes.PAREN2))
        Assertions.assertFalse(braceMatcher.isPairedBracesAllowedBeforeType(PhelTypes.BRACKET1, PhelTypes.BRACKET2))
        Assertions.assertFalse(braceMatcher.isPairedBracesAllowedBeforeType(PhelTypes.BRACE1, PhelTypes.BRACE2))

        Assertions.assertTrue(braceMatcher.isPairedBracesAllowedBeforeType(PhelTypes.PAREN1, PhelTypes.BRACKET1))
        Assertions.assertTrue(braceMatcher.isPairedBracesAllowedBeforeType(PhelTypes.BRACKET1, PhelTypes.PAREN1))
    }

    @Test
    fun `getCodeConstructStart should delegate to construct locator`() {
        val openingBraceOffset = 15

        val result = braceMatcher.getCodeConstructStart(mockFile, openingBraceOffset)

        Assertions.assertEquals(openingBraceOffset, result)
    }
}
