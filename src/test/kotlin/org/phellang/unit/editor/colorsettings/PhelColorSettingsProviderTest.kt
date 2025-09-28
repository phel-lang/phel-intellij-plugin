package org.phellang.unit.editor.colorsettings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.phellang.PhelSyntaxHighlighter
import org.phellang.annotator.infrastructure.PhelAnnotationConstants
import org.phellang.editor.colorsettings.PhelColorSettingsProvider

class PhelColorSettingsProviderTest {

    private lateinit var provider: PhelColorSettingsProvider

    @BeforeEach
    fun setUp() {
        provider = PhelColorSettingsProvider()
    }

    @Test
    fun `should return comprehensive attribute descriptors`() {
        val descriptors = provider.getAttributeDescriptors()
        val displayNames = descriptors.map { it.displayName }.toSet()

        assertTrue(descriptors.isNotEmpty())

        // Verify key categories are present
        assertTrue(displayNames.contains("Comments"))
        assertTrue(displayNames.contains("Strings"))
        assertTrue(displayNames.contains("Parentheses"))
        assertTrue(displayNames.contains("Function names (in call position)"))
        assertTrue(displayNames.contains("PHP interop"))

        // Verify all descriptors have valid keys and unique names
        assertEquals(descriptors.size, displayNames.size, "All display names should be unique")
        descriptors.forEach { descriptor ->
            assertNotNull(descriptor.key, "Key should not be null for: ${descriptor.displayName}")
        }
    }

    @Test
    fun `should have correct key mappings`() {
        val descriptors = provider.getAttributeDescriptors()
        val descriptorMap = descriptors.associateBy { it.displayName }

        assertEquals(PhelSyntaxHighlighter.COMMENT, descriptorMap["Comments"]?.key)
        assertEquals(PhelSyntaxHighlighter.STRING, descriptorMap["Strings"]?.key)
        assertEquals(PhelAnnotationConstants.FUNCTION_NAME, descriptorMap["Function names (in call position)"]?.key)
        assertEquals(PhelAnnotationConstants.PHP_INTEROP, descriptorMap["PHP interop"]?.key)
    }
}
