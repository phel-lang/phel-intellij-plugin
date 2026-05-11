package org.phellang.unit.editor.paredit

import com.intellij.openapi.util.TextRange
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.phellang.editor.paredit.PhelTextEdit

class PhelTextEditTest {

    @Test
    fun `insert factory builds a zero-width edit at the offset`() {
        val edit = PhelTextEdit.insert(7, ")")

        assertEquals(TextRange(7, 7), edit.range)
        assertEquals(")", edit.replacement)
    }

    @Test
    fun `delete factory wipes the given range`() {
        val edit = PhelTextEdit.delete(TextRange(4, 5))

        assertEquals(TextRange(4, 5), edit.range)
        assertEquals("", edit.replacement)
    }
}
