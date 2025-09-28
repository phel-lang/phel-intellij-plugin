package org.phellang.unit.editor.enter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.editor.enter.PhelEnterHandlerParenthesisManager
import com.intellij.openapi.editor.Document

@ExtendWith(MockitoExtension::class)
class PhelEnterHandlerParenthesisManagerTest {

    @Mock
    private lateinit var mockDocument: Document

    private lateinit var manager: PhelEnterHandlerParenthesisManager

    @BeforeEach
    fun setUp() {
        manager = PhelEnterHandlerParenthesisManager()
    }

    @Test
    fun `should add closing parenthesis when text ends with opening parenthesis`() {
        val textBeforeCaret = "(defn test ("
        val caretPosition = 11

        `when`(mockDocument.textLength).thenReturn(11)

        val result = manager.shouldAddClosingParenthesis(mockDocument, caretPosition, textBeforeCaret)

        assertTrue(result)
    }

    @Test
    fun `should not add closing parenthesis when text does not end with opening parenthesis`() {
        val textBeforeCaret = "(defn test"
        val caretPosition = 10

        val result = manager.shouldAddClosingParenthesis(mockDocument, caretPosition, textBeforeCaret)

        assertFalse(result)
    }

    @Test
    fun `should not add closing parenthesis when closing parenthesis already exists after caret`() {
        val textBeforeCaret = "(defn test ("
        val caretPosition = 12

        `when`(mockDocument.textLength).thenReturn(13)
        `when`(mockDocument.text).thenReturn("(defn test ()")

        val result = manager.shouldAddClosingParenthesis(mockDocument, caretPosition, textBeforeCaret)

        assertFalse(result)
    }

    @Test
    fun `should handle caret at end of document`() {
        val textBeforeCaret = "(defn test ("
        val caretPosition = 11

        `when`(mockDocument.textLength).thenReturn(11)

        val result = manager.shouldAddClosingParenthesis(mockDocument, caretPosition, textBeforeCaret)

        assertTrue(result)
    }

    @ParameterizedTest
    @CsvSource(
        "0, '\n)'", "2, '\n  )'", "4, '\n    )'", "6, '\n      )'"
    )
    fun `should create correct closing parenthesis text with indentation`(indentationSpaces: Int, expected: String) {
        val result = manager.createClosingParenthesisText(indentationSpaces)

        assertEquals(expected, result)
    }

    @Test
    fun `should create closing parenthesis text with no indentation`() {
        val result = manager.createClosingParenthesisText(0)

        assertEquals("\n)", result)
    }
}
