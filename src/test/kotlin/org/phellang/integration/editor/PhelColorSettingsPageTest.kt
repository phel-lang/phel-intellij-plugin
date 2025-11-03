package org.phellang.integration.editor

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.phellang.language.infrastructure.PhelIcons
import org.phellang.syntax.PhelSyntaxHighlighter
import org.phellang.editor.PhelColorSettingsPage

class PhelColorSettingsPageTest {

    private lateinit var colorSettingsPage: PhelColorSettingsPage

    @BeforeEach
    fun setUp() {
        colorSettingsPage = PhelColorSettingsPage()
    }

    @Test
    fun `color settings page should implement ColorSettingsPage interface correctly`() {
        Assertions.assertNotNull(colorSettingsPage.displayName)
        Assertions.assertNotNull(colorSettingsPage.icon)
        Assertions.assertNotNull(colorSettingsPage.highlighter)
        Assertions.assertNotNull(colorSettingsPage.demoText)
        Assertions.assertNotNull(colorSettingsPage.attributeDescriptors)
        Assertions.assertNotNull(colorSettingsPage.colorDescriptors)
        Assertions.assertNull(colorSettingsPage.additionalHighlightingTagToDescriptorMap)
    }

    @Test
    fun `getDisplayName should return expected value`() {
        val displayName = colorSettingsPage.displayName

        Assertions.assertEquals("Phel Lang", displayName)
    }

    @Test
    fun `getIcon should return Phel file icon`() {
        val icon = colorSettingsPage.icon

        Assertions.assertSame(PhelIcons.FILE, icon)
    }

    @Test
    fun `getHighlighter should return PhelSyntaxHighlighter`() {
        val highlighter = colorSettingsPage.highlighter

        Assertions.assertTrue(highlighter is PhelSyntaxHighlighter)
    }

    @Test
    fun `getDemoText should return comprehensive demo text`() {
        val demoText = colorSettingsPage.demoText

        Assertions.assertNotNull(demoText)
        Assertions.assertTrue(demoText.isNotEmpty())

        // Should contain key Phel language elements
        Assertions.assertTrue(demoText.contains("(ns "))
        Assertions.assertTrue(demoText.contains("(defn "))
        Assertions.assertTrue(demoText.contains(":require"))
        Assertions.assertTrue(demoText.contains("true"))
        Assertions.assertTrue(demoText.contains("false"))
        Assertions.assertTrue(demoText.contains("nil"))
    }

    @Test
    fun `getAttributeDescriptors should return comprehensive descriptors`() {
        val descriptors = colorSettingsPage.attributeDescriptors

        Assertions.assertNotNull(descriptors)
        Assertions.assertTrue(descriptors.isNotEmpty())

        val displayNames = descriptors.map { it.displayName }.toSet()

        // Should include basic syntax elements
        Assertions.assertTrue(displayNames.contains("Comments"))
        Assertions.assertTrue(displayNames.contains("Strings"))
        Assertions.assertTrue(displayNames.contains("Numbers"))
        Assertions.assertTrue(displayNames.contains("Parentheses"))

        // Should include semantic elements
        Assertions.assertTrue(displayNames.contains("Function names (in call position)"))
        Assertions.assertTrue(displayNames.contains("PHP interop"))
    }

    @Test
    fun `getColorDescriptors should return empty array`() {
        val colorDescriptors = colorSettingsPage.colorDescriptors

        Assertions.assertNotNull(colorDescriptors)
        Assertions.assertEquals(0, colorDescriptors.size)
    }

    @Test
    fun `getAdditionalHighlightingTagToDescriptorMap should return null`() {
        val tagMap = colorSettingsPage.additionalHighlightingTagToDescriptorMap

        Assertions.assertNull(tagMap)
    }

    @Test
    fun `integration between components should work seamlessly`() {
        val displayName = colorSettingsPage.displayName
        val icon = colorSettingsPage.icon
        val highlighter = colorSettingsPage.highlighter
        val demoText = colorSettingsPage.demoText
        val attributeDescriptors = colorSettingsPage.attributeDescriptors

        // All should be valid and non-null (except where null is expected)
        Assertions.assertNotNull(displayName)
        Assertions.assertFalse(displayName.isEmpty())

        Assertions.assertNotNull(icon)
        Assertions.assertNotNull(highlighter)

        Assertions.assertNotNull(demoText)
        Assertions.assertTrue(demoText.length > 100) // Should be substantial

        Assertions.assertNotNull(attributeDescriptors)
        Assertions.assertTrue(attributeDescriptors.size > 10) // Should have many descriptors

        // All attribute descriptors should have valid keys
        attributeDescriptors.forEach { descriptor ->
            Assertions.assertNotNull(descriptor.key, "Descriptor '${descriptor.displayName}' should have a key")
            Assertions.assertNotNull(descriptor.displayName, "Descriptor should have a display name")
            Assertions.assertFalse(descriptor.displayName.isEmpty(), "Display name should not be empty")
        }
    }

    @Test
    fun `demo text should showcase all attribute descriptor categories`() {
        val demoText = colorSettingsPage.demoText

        // Basic syntax should be covered
        Assertions.assertTrue(demoText.contains("\""), "Should contain strings")
        Assertions.assertTrue(demoText.contains("#"), "Should contain comments")
        Assertions.assertTrue(demoText.contains("("), "Should contain parentheses")
        Assertions.assertTrue(demoText.contains("["), "Should contain brackets")
        Assertions.assertTrue(demoText.contains("{"), "Should contain braces")

        // Numbers in various formats
        Assertions.assertTrue(
            demoText.contains("42") || demoText.contains("0x") || demoText.contains("3.14"), "Should contain numbers"
        )

        // Phel-specific elements
        Assertions.assertTrue(
            demoText.contains("defn") || demoText.contains("def"), "Should contain function definitions"
        )
        Assertions.assertTrue(demoText.contains("php/"), "Should contain PHP interop examples")
    }

    @Test
    fun `color settings page should be ready for IDE integration`() {
        // This test verifies that the color settings page provides everything needed
        // for proper integration with IntelliJ's color settings system

        val page = colorSettingsPage

        // Display name for the settings tree
        val displayName = page.displayName
        Assertions.assertNotNull(displayName)
        Assertions.assertFalse(displayName.trim().isEmpty())

        // Icon for the settings tree
        val icon = page.icon
        Assertions.assertNotNull(icon)

        // Syntax highlighter for basic highlighting
        val highlighter = page.highlighter
        Assertions.assertNotNull(highlighter)

        // Demo text for preview
        val demoText = page.demoText
        Assertions.assertNotNull(demoText)
        Assertions.assertTrue(demoText.length > 50, "Demo text should be substantial")

        // Attribute descriptors for customizable colors
        val attributes = page.attributeDescriptors
        Assertions.assertNotNull(attributes)
        Assertions.assertTrue(attributes.isNotEmpty(), "Should provide customizable attributes")

        // Color descriptors (can be empty)
        val colors = page.colorDescriptors
        Assertions.assertNotNull(colors)

        // Additional highlighting (can be null)
        // No assertion needed for getAdditionalHighlightingTagToDescriptorMap()
    }
}
