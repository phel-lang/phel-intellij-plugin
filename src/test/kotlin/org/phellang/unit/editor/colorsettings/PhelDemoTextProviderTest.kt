package org.phellang.unit.editor.colorsettings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.phellang.editor.colorsettings.PhelDemoTextProvider

class PhelDemoTextProviderTest {

    private lateinit var provider: PhelDemoTextProvider

    @BeforeEach
    fun setUp() {
        provider = PhelDemoTextProvider()
    }

    @Test
    fun `should return comprehensive demo text`() {
        val demoText = provider.getDemoText()

        assertTrue(demoText.isNotEmpty())
        assertTrue(demoText.length > 1000, "Should be substantial")

        // Key Phel language elements
        assertTrue(demoText.contains("(ns "))
        assertTrue(demoText.contains("(defn "))
        assertTrue(demoText.contains(":require"))
        assertTrue(demoText.contains("true"))
        assertTrue(demoText.contains("nil"))
        assertTrue(demoText.contains("php/"))
        assertTrue(demoText.contains("->"))
        assertTrue(demoText.contains("#|"))
        assertTrue(demoText.contains("|#"))
    }

    @Test
    fun `should be consistent across calls`() {
        assertEquals(provider.getDemoText(), provider.getDemoText())
    }
}
