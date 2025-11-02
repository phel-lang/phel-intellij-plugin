package org.phellang.integration.editor

import com.intellij.openapi.editor.Document
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.phellang.editor.typing.context.PhelStringContextAnalyzer
import org.phellang.editor.typing.pairing.PhelCharacterPairing

class PhelTypingIntegrationTest {

    @Test
    fun `character pairing and string context should work together`() {
        val text = "\"hello world\""
        val offset = 5 // Inside string

        // String context should detect we're inside a string
        val isInsideString = PhelStringContextAnalyzer.isInsideString(text, offset)
        Assertions.assertTrue(isInsideString)

        // Character pairing should not auto-close inside strings
        val mockDocument = Mockito.mock(Document::class.java)
        Mockito.`when`(mockDocument.charsSequence).thenReturn(text)

        val shouldAutoClose = PhelCharacterPairing.shouldAutoClose(mockDocument, offset)
        Assertions.assertFalse(shouldAutoClose)

        // Both should be consistent
        Assertions.assertTrue(isInsideString && !shouldAutoClose)
    }

    @Test
    fun `string context analysis should be consistent across different scenarios`() {
        val testCases = listOf(
            "simple text" to listOf(5 to false),
            "\"simple string\"" to listOf(0 to false, 1 to true, 7 to true, 15 to false),
            "\"first\" \"second\"" to listOf(3 to true, 7 to false, 11 to true),
            "\"escaped \\\"quote\\\"\"" to listOf(5 to true, 10 to true, 17 to true),
            "code \"string\" more" to listOf(4 to false, 6 to true, 13 to false, 15 to false)
        )

        testCases.forEach { (text, expectations) ->
            expectations.forEach { (offset, expectedInside) ->
                val actualInside = PhelStringContextAnalyzer.isInsideString(text, offset)
                Assertions.assertEquals(
                    expectedInside, actualInside, "String context analysis failed for '$text' at offset $offset"
                )
            }
        }
    }

    @Test
    fun `character pairing should respect string context in all scenarios`() {
        data class PairingScenario(val text: String, val offset: Int, val expectedAutoClose: Boolean)

        val testCases = listOf(
            PairingScenario("normal text", 6, true),        // Should auto-close outside string (at space)
            PairingScenario("\"inside string\"", 5, false), // Should not auto-close inside string
            PairingScenario("\"first\" between", 7, true),  // Should auto-close between strings (at space)
            PairingScenario("before\"after", 5, false)      // Should not auto-close when next char is quote
        )

        testCases.forEach { scenario ->
            val mockDocument = Mockito.mock(Document::class.java)
            Mockito.`when`(mockDocument.charsSequence).thenReturn(scenario.text)

            val shouldAutoClose = PhelCharacterPairing.shouldAutoClose(mockDocument, scenario.offset)
            Assertions.assertEquals(
                scenario.expectedAutoClose,
                shouldAutoClose,
                "Auto-close behavior incorrect for '${scenario.text}' at offset ${scenario.offset}"
            )
        }
    }

    @Test
    fun `component integration should maintain performance characteristics`() {
        val largeText = "\"" + "a".repeat(10000) + "\""

        // Should handle large strings efficiently
        val startTime = System.currentTimeMillis()

        repeat(1000) {
            PhelStringContextAnalyzer.isInsideString(largeText, 5000)
        }

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Should complete within reasonable time (less than 1 second for 1000 iterations)
        Assertions.assertTrue(duration < 1000, "String context analysis should be performant")
    }
}
