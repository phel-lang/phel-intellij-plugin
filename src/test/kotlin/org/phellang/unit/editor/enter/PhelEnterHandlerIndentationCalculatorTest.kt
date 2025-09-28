package org.phellang.unit.editor.enter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.phellang.editor.enter.PhelEnterHandlerIndentationCalculator

class PhelEnterHandlerIndentationCalculatorTest {

    private lateinit var calculator: PhelEnterHandlerIndentationCalculator

    @BeforeEach
    fun setUp() {
        calculator = PhelEnterHandlerIndentationCalculator()
    }

    @ParameterizedTest
    @CsvSource(
        "'', 0", "'  ', 2", "'    ', 4", "'      ', 6", "'\t', 1", "'\t  ', 3"
    )
    fun `should correctly count current indentation spaces`(indentation: String, expectedSpaces: Int) {
        val currentLineText = "$indentation(some code)"

        val result = calculator.getCurrentIndentationSpaces(currentLineText)

        assertEquals(expectedSpaces, result)
    }

    @Test
    fun `should handle empty line text`() {
        val result = calculator.getCurrentIndentationSpaces("")

        assertEquals(0, result)
    }

    @Test
    fun `should handle line with only whitespace`() {
        val result = calculator.getCurrentIndentationSpaces("    ")

        assertEquals(4, result)
    }

    @Test
    fun `should stop counting at first non-whitespace character`() {
        val result = calculator.getCurrentIndentationSpaces("  (defn")

        assertEquals(2, result)
    }
}
