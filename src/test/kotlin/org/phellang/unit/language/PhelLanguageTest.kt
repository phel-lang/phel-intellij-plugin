package org.phellang.unit.language

import com.intellij.lang.Language
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.language.infrastructure.PhelLanguage

class PhelLanguageTest {

    @Test
    fun `should be singleton object`() {
        val language1 = PhelLanguage
        val language2 = PhelLanguage

        assertSame(language1, language2)
        assertNotNull(language1)
    }

    @Test
    fun `should have correct ID`() {
        assertEquals("Phel", PhelLanguage.id)
    }

    @Test
    fun `should have correct display name`() {
        assertEquals("Phel", PhelLanguage.displayName)
    }

    @Test
    fun `ID and display name should be consistent`() {
        assertEquals(PhelLanguage.id, PhelLanguage.displayName)
    }

    @Test
    fun `should have correct case sensitivity`() {
        // Phel language case sensitivity behavior
        assertFalse(PhelLanguage.isCaseSensitive)
    }

    @Test
    fun `should have meaningful toString representation`() {
        val stringRepresentation = PhelLanguage.toString()
        
        assertNotNull(stringRepresentation)
        assertTrue(stringRepresentation.isNotEmpty())
        assertTrue(stringRepresentation.contains("Phel"))
    }

    @Test
    fun `should be suitable for IntelliJ language registry`() {
        // Verify all required properties are valid
        assertNotNull(PhelLanguage.id)
        assertNotNull(PhelLanguage.displayName)
        assertTrue(PhelLanguage.id.isNotEmpty())
        assertTrue(PhelLanguage.displayName.isNotEmpty())
        
        // ID should not contain spaces or special characters
        assertFalse(PhelLanguage.id.contains(" "))
        assertTrue(PhelLanguage.id.matches(Regex("[a-zA-Z][a-zA-Z0-9]*")))
    }

    @Test
    fun `should maintain consistent properties across multiple accesses`() {
        val id1 = PhelLanguage.id
        val id2 = PhelLanguage.id
        assertEquals(id1, id2)
        
        val displayName1 = PhelLanguage.displayName
        val displayName2 = PhelLanguage.displayName
        assertEquals(displayName1, displayName2)
        
        val caseSensitive1 = PhelLanguage.isCaseSensitive
        val caseSensitive2 = PhelLanguage.isCaseSensitive
        assertEquals(caseSensitive1, caseSensitive2)
    }

    @Test
    fun `should have proper hashCode and equals behavior`() {
        val language1 = PhelLanguage
        val language2 = PhelLanguage

        // Same instance should be equal
        assertEquals(language1, language2)
        assertEquals(language1.hashCode(), language2.hashCode())
    }

    @Test
    fun `should be thread-safe singleton`() {
        // Test concurrent access to singleton
        val languages = mutableListOf<Language>()
        
        val threads = (1..10).map {
            Thread {
                synchronized(languages) {
                    languages.add(PhelLanguage)
                }
            }
        }
        
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        
        // All instances should be the same reference
        assertEquals(10, languages.size)
        assertTrue(languages.all { it === PhelLanguage })
    }

    @Test
    fun `should be findable by ID`() {
        val foundLanguage = Language.findLanguageByID("Phel")
        
        assertNotNull(foundLanguage)
        assertEquals(PhelLanguage, foundLanguage)
    }

    @Test
    fun `should be registered in Language registry`() {
        val registeredLanguages = Language.getRegisteredLanguages()
        
        assertTrue(registeredLanguages.contains(PhelLanguage))
    }
}
