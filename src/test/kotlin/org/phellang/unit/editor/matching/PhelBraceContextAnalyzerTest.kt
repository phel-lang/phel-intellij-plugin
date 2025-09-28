package org.phellang.unit.editor.matching

import com.intellij.psi.tree.IElementType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.*
import org.phellang.editor.matching.PhelBraceContextAnalyzer
import org.phellang.editor.matching.PhelBracePairProvider
import org.phellang.language.psi.PhelTypes
import java.util.stream.Stream

class PhelBraceContextAnalyzerTest {

    private lateinit var mockBracePairProvider: PhelBracePairProvider
    private lateinit var analyzer: PhelBraceContextAnalyzer

    @BeforeEach
    fun setUp() {
        mockBracePairProvider = mock(PhelBracePairProvider::class.java)
        analyzer = PhelBraceContextAnalyzer(mockBracePairProvider)

        `when`(mockBracePairProvider.isClosingBrace(PhelTypes.PAREN2)).thenReturn(true)
        `when`(mockBracePairProvider.isClosingBrace(PhelTypes.BRACKET2)).thenReturn(true)
        `when`(mockBracePairProvider.isClosingBrace(PhelTypes.BRACE2)).thenReturn(true)
        `when`(mockBracePairProvider.isClosingBrace(PhelTypes.PAREN1)).thenReturn(false)
        `when`(mockBracePairProvider.isClosingBrace(PhelTypes.BRACKET1)).thenReturn(false)
        `when`(mockBracePairProvider.isClosingBrace(PhelTypes.BRACE1)).thenReturn(false)
    }

    @Test
    fun `isPairedBracesAllowedBeforeType should allow pairing when context is null`() {
        val result = analyzer.isPairedBracesAllowedBeforeType(null)
        assertTrue(result)
    }

    @ParameterizedTest
    @MethodSource("closingBraceTestCases")
    fun `isPairedBracesAllowedBeforeType should not allow pairing before closing braces`(contextType: IElementType) {
        val result = analyzer.isPairedBracesAllowedBeforeType(contextType)
        assertFalse(result, "Should not allow pairing before closing brace $contextType")
    }

    @ParameterizedTest
    @MethodSource("openingBraceTestCases")
    fun `isPairedBracesAllowedBeforeType should allow pairing before opening braces`(contextType: IElementType) {
        val result = analyzer.isPairedBracesAllowedBeforeType(contextType)
        assertTrue(result, "Should allow pairing before opening brace $contextType")
    }

    @Test
    fun `analyzer should use the provided brace pair provider`() {
        analyzer.isPairedBracesAllowedBeforeType(PhelTypes.PAREN2)
        verify(mockBracePairProvider).isClosingBrace(PhelTypes.PAREN2)
    }

    companion object {
        @JvmStatic
        fun closingBraceTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(PhelTypes.PAREN2),
            Arguments.of(PhelTypes.BRACKET2),
            Arguments.of(PhelTypes.BRACE2),
            Arguments.of(PhelTypes.BRACKET2),
            Arguments.of(PhelTypes.BRACE2)
        )

        @JvmStatic
        fun openingBraceTestCases(): Stream<Arguments> = Stream.of(
            Arguments.of(PhelTypes.PAREN1, PhelTypes.PAREN1),
            Arguments.of(PhelTypes.BRACKET1, PhelTypes.BRACKET1),
            Arguments.of(PhelTypes.BRACE1, PhelTypes.BRACE1),
            Arguments.of(PhelTypes.PAREN1, PhelTypes.BRACKET1),
            Arguments.of(PhelTypes.BRACKET1, PhelTypes.BRACE1)
        )
    }
}
