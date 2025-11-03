package org.phellang.unit.language

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.language.infrastructure.PhelFileType
import org.phellang.language.infrastructure.PhelIcons
import org.phellang.language.infrastructure.PhelLanguage

class PhelFileTypeTest {

    @Test
    fun `INSTANCE should be singleton`() {
        val instance1 = PhelFileType.INSTANCE
        val instance2 = PhelFileType.INSTANCE

        assertSame(instance1, instance2)
        assertNotNull(instance1)
    }

    @Test
    fun `should use PhelLanguage`() {
        val fileType = PhelFileType.INSTANCE

        assertEquals(PhelLanguage, fileType.language)
    }

    @Test
    fun `getName should return correct name`() {
        val fileType = PhelFileType.INSTANCE

        assertEquals("Phel file", fileType.name)
    }

    @Test
    fun `getDescription should return correct description`() {
        val fileType = PhelFileType.INSTANCE

        assertEquals("Phel language file", fileType.description)
    }

    @Test
    fun `getDefaultExtension should return phel`() {
        val fileType = PhelFileType.INSTANCE

        assertEquals("phel", fileType.defaultExtension)
    }

    @Test
    fun `getIcon should return PhelIcons FILE`() {
        val fileType = PhelFileType.INSTANCE

        assertEquals(PhelIcons.FILE, fileType.icon)
        assertNotNull(fileType.icon)
    }

    @Test
    fun `should be consistent across multiple calls`() {
        val fileType = PhelFileType.INSTANCE

        // Test consistency of all methods
        assertEquals("Phel file", fileType.name)
        assertEquals("Phel file", fileType.name) // Second call

        assertEquals("Phel language file", fileType.description)
        assertEquals("Phel language file", fileType.description) // Second call

        assertEquals("phel", fileType.defaultExtension)
        assertEquals("phel", fileType.defaultExtension) // Second call

        assertEquals(PhelIcons.FILE, fileType.icon)
        assertEquals(PhelIcons.FILE, fileType.icon) // Second call
    }

    @Test
    fun `toString should return meaningful representation`() {
        val fileType = PhelFileType.INSTANCE

        val stringRepresentation = fileType.toString()

        assertNotNull(stringRepresentation)
        assertTrue(stringRepresentation.isNotEmpty())
        // Should contain class name
        assertTrue(stringRepresentation.contains("PhelFileType"))
    }

    @Test
    fun `should have proper hashCode and equals behavior`() {
        val fileType1 = PhelFileType.INSTANCE
        val fileType2 = PhelFileType.INSTANCE

        // Same instance should be equal
        assertEquals(fileType1, fileType2)
        assertEquals(fileType1.hashCode(), fileType2.hashCode())
    }

    @Test
    fun `should be suitable for IntelliJ file type registry`() {
        val fileType = PhelFileType.INSTANCE

        // Verify all required properties are non-null and valid
        assertNotNull(fileType.name)
        assertNotNull(fileType.description)
        assertNotNull(fileType.defaultExtension)
        assertNotNull(fileType.icon)
        assertNotNull(fileType.language)

        // Verify properties are not empty
        assertTrue(fileType.name.isNotEmpty())
        assertTrue(fileType.description.isNotEmpty())
        assertTrue(fileType.defaultExtension.isNotEmpty())

        // Verify extension doesn't start with dot
        assertFalse(fileType.defaultExtension.startsWith("."))
    }

    @Test
    fun `should have correct language association`() {
        val fileType = PhelFileType.INSTANCE

        assertEquals(PhelLanguage, fileType.language)
        assertEquals("Phel", fileType.language.id)
        assertEquals("Phel", fileType.language.displayName)
    }

    @Test
    fun `should be thread-safe singleton`() {
        // Test concurrent access to singleton
        val instances = mutableListOf<PhelFileType>()

        val threads = (1..10).map {
            Thread {
                synchronized(instances) {
                    instances.add(PhelFileType.INSTANCE)
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        // All instances should be the same reference
        assertEquals(10, instances.size)
        assertTrue(instances.all { it === PhelFileType.INSTANCE })
    }

    @Test
    fun `should maintain immutable properties`() {
        val fileType = PhelFileType.INSTANCE

        // Properties should be consistent across multiple accesses
        val name1 = fileType.name
        val name2 = fileType.name
        assertEquals(name1, name2)

        val description1 = fileType.description
        val description2 = fileType.description
        assertEquals(description1, description2)

        val extension1 = fileType.defaultExtension
        val extension2 = fileType.defaultExtension
        assertEquals(extension1, extension2)

        val icon1 = fileType.icon
        val icon2 = fileType.icon
        assertSame(icon1, icon2)
    }
}
