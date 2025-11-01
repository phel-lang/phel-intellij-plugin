package org.phellang.unit.editor.folding.placeholders

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.editor.folding.placeholders.PhelPlaceholderGenerator
import org.phellang.language.psi.*

class PhelPlaceholderGeneratorTest {

    @Test
    fun `generateListPlaceholder should return generic placeholder for empty list`() {
        val mockList = mock(PhelList::class.java)
        `when`(mockList.children).thenReturn(emptyArray())

        val result = PhelPlaceholderGenerator.generateListPlaceholder(mockList)

        assertEquals("(...)", result)
    }
}
