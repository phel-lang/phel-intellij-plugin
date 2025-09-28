package org.phellang.unit.editor.matching

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.phellang.editor.matching.PhelBracePairProvider
import org.phellang.language.psi.PhelTypes
import java.util.stream.Stream

class PhelBracePairProviderTest {

    private lateinit var provider: PhelBracePairProvider

    @BeforeEach
    fun setUp() {
        provider = PhelBracePairProvider()
    }

    @Test
    fun `getBracePairs should return all supported pairs`() {
        val pairs = provider.getBracePairs()
        
        assertEquals(3, pairs.size)
        
        // Check parentheses
        val parenPair = pairs.find { it.leftBraceType == PhelTypes.PAREN1 }
        assertNotNull(parenPair)
        assertEquals(PhelTypes.PAREN2, parenPair!!.rightBraceType)
        assertFalse(parenPair.isStructural)
        
        // Check brackets
        val bracketPair = pairs.find { it.leftBraceType == PhelTypes.BRACKET1 }
        assertNotNull(bracketPair)
        assertEquals(PhelTypes.BRACKET2, bracketPair!!.rightBraceType)
        assertFalse(bracketPair.isStructural)
        
        // Check braces
        val bracePair = pairs.find { it.leftBraceType == PhelTypes.BRACE1 }
        assertNotNull(bracePair)
        assertEquals(PhelTypes.BRACE2, bracePair!!.rightBraceType)
        assertFalse(bracePair.isStructural)
    }

    @ParameterizedTest
    @MethodSource("closingBraceTestCases")
    fun `isClosingBrace should correctly identify closing braces`(elementType: com.intellij.psi.tree.IElementType, expected: Boolean) {
        assertEquals(expected, provider.isClosingBrace(elementType))
    }

    @Test
    fun `opening brace sets should be disjoint`() {
        val openingBraces = setOf(PhelTypes.PAREN1, PhelTypes.BRACKET1, PhelTypes.BRACE1)

        openingBraces.forEach { opening ->
            assertFalse(provider.isClosingBrace(opening), "Opening brace $opening should not be identified as closing")
        }
    }

    companion object {
        @JvmStatic
        fun closingBraceTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(PhelTypes.PAREN2, true),
            Arguments.of(PhelTypes.BRACKET2, true),
            Arguments.of(PhelTypes.BRACE2, true),
            Arguments.of(PhelTypes.PAREN1, false),
            Arguments.of(PhelTypes.BRACKET1, false),
            Arguments.of(PhelTypes.BRACE1, false)
        )
    }
}
