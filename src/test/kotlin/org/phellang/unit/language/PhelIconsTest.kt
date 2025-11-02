package org.phellang.unit.language

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.language.infrastructure.PhelIcons
import javax.swing.Icon

class PhelIconsTest {

    @Test
    fun `should be singleton object`() {
        val icons1 = PhelIcons
        val icons2 = PhelIcons

        assertSame(icons1, icons2)
        assertNotNull(icons1)
    }

    @Test
    fun `FILE icon should have reasonable dimensions`() {
        val fileIcon = PhelIcons.FILE

        // Icons should have positive dimensions
        assertTrue(fileIcon.iconWidth > 0)
        assertTrue(fileIcon.iconHeight > 0)

        // Common icon sizes are 16x16, 32x32, etc.
        // Most file icons are 16x16
        assertTrue(fileIcon.iconWidth in 8..64)
        assertTrue(fileIcon.iconHeight in 8..64)
    }

    @Test
    fun `FILE icon should be consistent across multiple accesses`() {
        val icon1 = PhelIcons.FILE
        val icon2 = PhelIcons.FILE

        // Should return the same instance (cached)
        assertSame(icon1, icon2)
    }

    @Test
    fun `FILE icon should have consistent properties`() {
        val fileIcon = PhelIcons.FILE

        val width1 = fileIcon.iconWidth
        val width2 = fileIcon.iconWidth
        assertEquals(width1, width2)

        val height1 = fileIcon.iconHeight
        val height2 = fileIcon.iconHeight
        assertEquals(height1, height2)
    }

    @Test
    fun `should be thread-safe singleton`() {
        // Test concurrent access to singleton
        val icons = mutableListOf<Any>()

        val threads = (1..10).map {
            Thread {
                synchronized(icons) {
                    icons.add(PhelIcons)
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        // All instances should be the same reference
        assertEquals(10, icons.size)
        assertTrue(icons.all { it === PhelIcons })
    }

    @Test
    fun `FILE icon should be thread-safe`() {
        // Test concurrent access to FILE icon
        val fileIcons = mutableSetOf<Icon>()

        val threads = (1..10).map {
            Thread {
                fileIcons.add(PhelIcons.FILE)
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        // All instances should be the same (cached)
        assertEquals(1, fileIcons.size)
        assertTrue(fileIcons.contains(PhelIcons.FILE))
    }

    @Test
    fun `should have proper toString representation`() {
        val stringRepresentation = PhelIcons.toString()

        assertNotNull(stringRepresentation)
        assertTrue(stringRepresentation.isNotEmpty())
        assertTrue(stringRepresentation.contains("PhelIcons"))
    }

    @Test
    fun `should be suitable for IntelliJ icon usage`() {
        val fileIcon = PhelIcons.FILE

        // Verify icon can be used in IntelliJ contexts
        assertNotNull(fileIcon)

        // Icon should be paintable (basic requirement)
        assertDoesNotThrow {
            // This tests that the icon can provide its dimensions without throwing
            fileIcon.iconWidth
            fileIcon.iconHeight
        }
    }

    @Test
    fun `FILE icon should handle painting gracefully`() {
        val fileIcon = PhelIcons.FILE

        // Test that icon can be queried for painting without exceptions
        assertDoesNotThrow {
            // These are basic operations that should work for any valid icon
            val width = fileIcon.iconWidth
            val height = fileIcon.iconHeight

            assertTrue(width >= 0)
            assertTrue(height >= 0)
        }
    }

    @Test
    fun `should maintain immutable icon references`() {
        val icon1 = PhelIcons.FILE
        val icon2 = PhelIcons.FILE

        // References should be identical (cached)
        assertSame(icon1, icon2)

        // Properties should be consistent
        assertEquals(icon1.iconWidth, icon2.iconWidth)
        assertEquals(icon1.iconHeight, icon2.iconHeight)
    }

    @Test
    fun `should load icon from correct resource path`() {
        // This test verifies that the icon loading doesn't fail
        // The actual path "/icons/phel.png" should exist in resources
        assertDoesNotThrow {
            val icon = PhelIcons.FILE
            assertNotNull(icon)
        }
    }

    @Test
    fun `should have reasonable icon size for file type`() {
        val fileIcon = PhelIcons.FILE

        // File type icons are typically 16x16 in IntelliJ
        // But we'll allow some flexibility for different icon sets
        val width = fileIcon.iconWidth
        val height = fileIcon.iconHeight

        // Should be square or close to square for file icons
        val aspectRatio = width.toDouble() / height.toDouble()
        assertTrue(aspectRatio in 0.5..2.0, "Icon aspect ratio should be reasonable: $aspectRatio")

        // Should be a reasonable size for UI display
        assertTrue(width in 8..48, "Icon width should be reasonable for UI: $width")
        assertTrue(height in 8..48, "Icon height should be reasonable for UI: $height")
    }
}
