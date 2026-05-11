package org.phellang.unit.editor.structure

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.phellang.editor.structure.PhelStructuralForm

class PhelStructuralFormTest {

    @Test
    fun `keyword lookup recognises every defining form`() {
        PhelStructuralForm.entries.forEach { form ->
            assertEquals(form, PhelStructuralForm.fromKeyword(form.keyword))
        }
    }

    @Test
    fun `unknown or null keywords return null`() {
        assertNull(PhelStructuralForm.fromKeyword(null))
        assertNull(PhelStructuralForm.fromKeyword(""))
        assertNull(PhelStructuralForm.fromKeyword("let"))
        assertNull(PhelStructuralForm.fromKeyword("if"))
    }

    @Test
    fun `keywords are unique across the enum`() {
        val keywords = PhelStructuralForm.entries.map { it.keyword }
        assertEquals(keywords.size, keywords.toSet().size)
    }
}
