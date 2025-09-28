package org.phellang.unit.editor.colorsettings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.phellang.PhelIcons
import org.phellang.PhelSyntaxHighlighter
import org.phellang.editor.colorsettings.PhelColorSettingsConfiguration

class PhelColorSettingsConfigurationTest {

    private lateinit var configuration: PhelColorSettingsConfiguration

    @BeforeEach
    fun setUp() {
        configuration = PhelColorSettingsConfiguration()
    }

    @Test
    fun `should provide correct configuration values`() {
        assertEquals("Phel Lang", configuration.getDisplayName())
        assertSame(PhelIcons.FILE, configuration.getIcon())
        assertTrue(configuration.getHighlighter() is PhelSyntaxHighlighter)
        assertNull(configuration.getAdditionalHighlightingTagToDescriptorMap())
        assertEquals(0, configuration.getColorDescriptors().size)
    }

    @Test
    fun `should return new highlighter instances`() {
        val highlighter1 = configuration.getHighlighter()
        val highlighter2 = configuration.getHighlighter()
        assertNotSame(highlighter1, highlighter2)
    }
}
