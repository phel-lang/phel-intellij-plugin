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
    fun `FILE icon should have valid dimensions`() {
        val fileIcon = PhelIcons.FILE

        // Icons should have positive dimensions (basic requirement)
        assertTrue(fileIcon.iconWidth > 0, "Icon width must be positive: ${fileIcon.iconWidth}")
        assertTrue(fileIcon.iconHeight > 0, "Icon height must be positive: ${fileIcon.iconHeight}")

        // Sanity check - icons shouldn't be unreasonably large (prevents obvious errors)
        assertTrue(fileIcon.iconWidth <= 512, "Icon width seems too large: ${fileIcon.iconWidth}")
        assertTrue(fileIcon.iconHeight <= 512, "Icon height seems too large: ${fileIcon.iconHeight}")
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
        val fileIcons = mutableListOf<Icon>()

        val threads = (1..10).map {
            Thread {
                synchronized(fileIcons) {
                    fileIcons.add(PhelIcons.FILE)
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        // All instances should be the same (cached)
        assertEquals(10, fileIcons.size)
        assertTrue(fileIcons.all { it === PhelIcons.FILE })
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
    fun `FILE icon should be suitable for IntelliJ UI`() {
        val fileIcon = PhelIcons.FILE

        // Basic validation - icon should be usable
        assertNotNull(fileIcon)
        assertDoesNotThrow {
            fileIcon.iconWidth
            fileIcon.iconHeight
        }

        // Aspect ratio should be reasonable (not extremely stretched)
        val width = fileIcon.iconWidth
        val height = fileIcon.iconHeight
        if (width > 0 && height > 0) {
            val aspectRatio = width.toDouble() / height.toDouble()
            assertTrue(aspectRatio in 0.1..10.0, "Icon aspect ratio should be reasonable: $aspectRatio")
        }
    }
}
