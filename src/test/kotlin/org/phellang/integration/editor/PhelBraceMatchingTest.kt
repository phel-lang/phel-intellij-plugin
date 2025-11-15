package org.phellang.integration.editor

import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.editor.PhelBraceMatcher
import org.phellang.editor.matching.PhelBraceContextAnalyzer
import org.phellang.editor.matching.PhelBracePairProvider
import org.phellang.language.psi.PhelTypes
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class)
class PhelBraceMatchingTest {

    @Mock
    private lateinit var mockFile: PsiFile

    private lateinit var braceMatcher: PhelBraceMatcher
    private lateinit var bracePairProvider: PhelBracePairProvider
    private lateinit var contextAnalyzer: PhelBraceContextAnalyzer

    @BeforeEach
    fun setUp() {
        braceMatcher = PhelBraceMatcher()
        bracePairProvider = PhelBracePairProvider()
        contextAnalyzer = PhelBraceContextAnalyzer(bracePairProvider)
    }

    @Test
    fun `complete brace matching workflow should work end-to-end`() {
        val pairs = braceMatcher.pairs
        Assertions.assertEquals(5, pairs.size)

        Assertions.assertTrue(braceMatcher.isPairedBracesAllowedBeforeType(PhelTypes.PAREN1, null))
        Assertions.assertFalse(braceMatcher.isPairedBracesAllowedBeforeType(PhelTypes.PAREN1, PhelTypes.PAREN2))

        val constructStart = braceMatcher.getCodeConstructStart(mockFile, 0)
        Assertions.assertEquals(0, constructStart)
    }

    @ParameterizedTest
    @MethodSource("braceMatchingScenarios")
    fun `brace matching should handle various Phel code scenarios`(
        description: String,
        openingBraceType: IElementType,
        contextType: IElementType?,
        braceOffset: Int,
        expectedConstructStart: Int,
        shouldAllowPairing: Boolean
    ) {
        // Test construct start location
        val actualConstructStart = braceMatcher.getCodeConstructStart(mockFile, braceOffset)
        Assertions.assertEquals(expectedConstructStart, actualConstructStart, "Construct start for: $description")

        // Test brace pairing logic with actual scenario parameters
        val actualAllowsPairing = braceMatcher.isPairedBracesAllowedBeforeType(openingBraceType, contextType)
        Assertions.assertEquals(shouldAllowPairing, actualAllowsPairing, "Pairing allowed for: $description")
    }

    @Test
    fun `brace matching should handle nested structures correctly`() {
        val positions = listOf(0, 5, 9, 17, 21) // positions of various braces

        positions.forEach { position ->
            val constructStart = braceMatcher.getCodeConstructStart(mockFile, position)
            Assertions.assertEquals(
                position,
                constructStart,
                "Construct start should be at brace position for nested structure"
            )
        }
    }

    @Test
    fun `context analysis should work correctly across all brace types`() {
        val allOpeningBraces = listOf(PhelTypes.PAREN1, PhelTypes.BRACKET1, PhelTypes.BRACE1)
        val allClosingBraces = listOf(PhelTypes.PAREN2, PhelTypes.BRACKET2, PhelTypes.BRACE2)

        allOpeningBraces.forEach { opening ->
            allClosingBraces.forEach { closing ->
                val result = braceMatcher.isPairedBracesAllowedBeforeType(opening, closing)
                Assertions.assertFalse(result, "Should not allow pairing of $opening before $closing")
            }
        }

        allOpeningBraces.forEach { opening ->
            allOpeningBraces.forEach { context ->
                val result = braceMatcher.isPairedBracesAllowedBeforeType(opening, context)
                Assertions.assertTrue(result, "Should allow pairing of $opening before $context")
            }
        }
    }

    companion object {
        @JvmStatic
        fun braceMatchingScenarios(): Stream<Arguments> = Stream.of(
            // Test pairing allowed scenarios (null context)
            Arguments.of("Parentheses with no context", PhelTypes.PAREN1, null, 0, 0, true),
            Arguments.of("Brackets with no context", PhelTypes.BRACKET1, null, 5, 5, true),
            Arguments.of("Braces with no context", PhelTypes.BRACE1, null, 10, 10, true),
            
            // Test pairing not allowed scenarios (closing brace context)
            Arguments.of("Parentheses before closing paren", PhelTypes.PAREN1, PhelTypes.PAREN2, 0, 0, false),
            Arguments.of("Brackets before closing bracket", PhelTypes.BRACKET1, PhelTypes.BRACKET2, 5, 5, false),
            Arguments.of("Braces before closing brace", PhelTypes.BRACE1, PhelTypes.BRACE2, 10, 10, false),
            
            // Test pairing allowed scenarios (opening brace context)
            Arguments.of("Parentheses before opening paren", PhelTypes.PAREN1, PhelTypes.PAREN1, 15, 15, true),
            Arguments.of("Mixed braces - paren before bracket", PhelTypes.PAREN1, PhelTypes.BRACKET1, 20, 20, true)
        )
    }
}
