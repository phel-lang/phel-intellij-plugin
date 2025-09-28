package org.phellang.unit.annotator.analyzers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.phellang.annotator.analyzers.TokenType

class TokenTypeTest {

    @Test
    fun `TokenType should have expected values`() {
        val expectedTypes = setOf(TokenType.FORM, TokenType.COMMENT)
        val actualTypes = TokenType.entries.toSet()

        assertEquals(expectedTypes, actualTypes)
        assertEquals(2, TokenType.entries.size)
    }

    @Test
    fun `TokenType FORM should have correct properties`() {
        val formType = TokenType.FORM

        assertNotNull(formType)
        assertEquals("FORM", formType.name)
        assertEquals(1, formType.ordinal) // Second enum value
    }

    @Test
    fun `TokenType COMMENT should have correct properties`() {
        val commentType = TokenType.COMMENT

        assertNotNull(commentType)
        assertEquals("COMMENT", commentType.name)
    }

    @ParameterizedTest
    @EnumSource(TokenType::class)
    fun `TokenType values should be consistent`(tokenType: TokenType) {
        // Each enum value should be non-null and have a name
        assertNotNull(tokenType)
        assertNotNull(tokenType.name)
        assertTrue(tokenType.name.isNotEmpty())

        // Should be able to convert to string
        assertNotNull(tokenType.toString())
    }

    @Test
    fun `TokenType should support valueOf operations`() {
        assertEquals(TokenType.FORM, TokenType.valueOf("FORM"))
        assertEquals(TokenType.COMMENT, TokenType.valueOf("COMMENT"))
    }

    @Test
    fun `TokenType valueOf should throw for invalid names`() {
        assertThrows(IllegalArgumentException::class.java) {
            TokenType.valueOf("INVALID")
        }

        assertThrows(IllegalArgumentException::class.java) {
            TokenType.valueOf("form") // Case sensitive
        }

        assertThrows(IllegalArgumentException::class.java) {
            TokenType.valueOf("")
        }
    }

    @Test
    fun `TokenType should support equality operations`() {
        assertEquals(TokenType.FORM, TokenType.FORM)
        assertEquals(TokenType.COMMENT, TokenType.COMMENT)
        assertNotEquals(TokenType.FORM, TokenType.COMMENT)

        // Should work with valueOf
        assertEquals(TokenType.FORM, TokenType.valueOf("FORM"))
        assertEquals(TokenType.COMMENT, TokenType.valueOf("COMMENT"))
    }

    @Test
    fun `TokenType should have consistent hashCode`() {
        assertEquals(TokenType.FORM.hashCode(), TokenType.FORM.hashCode())
        assertEquals(TokenType.COMMENT.hashCode(), TokenType.COMMENT.hashCode())

        // Different types should have different hash codes (usually)
        assertNotEquals(TokenType.FORM.hashCode(), TokenType.COMMENT.hashCode())
    }

    @Test
    fun `TokenType should be serializable`() {
        // Enums are serializable by default in Kotlin/Java
        val formType = TokenType.FORM
        val commentType = TokenType.COMMENT

        // Should be able to convert to string and back
        assertEquals(formType, TokenType.valueOf(formType.name))
        assertEquals(commentType, TokenType.valueOf(commentType.name))
    }

    @Test
    fun `TokenType should have meaningful semantic names`() {
        // Verify that the enum names make sense for their purpose
        assertTrue(TokenType.FORM.name.contains("FORM"))
        assertTrue(TokenType.COMMENT.name.contains("COMMENT"))

        // Names should be uppercase (enum convention)
        assertEquals(TokenType.FORM.name, TokenType.FORM.name.uppercase())
        assertEquals(TokenType.COMMENT.name, TokenType.COMMENT.name.uppercase())
    }

    @Test
    fun `TokenType should support iteration`() {
        val allTypes = mutableListOf<TokenType>()

        for (type in TokenType.entries) {
            allTypes.add(type)
        }

        assertEquals(2, allTypes.size)
        assertTrue(allTypes.contains(TokenType.FORM))
        assertTrue(allTypes.contains(TokenType.COMMENT))
    }

    @Test
    fun `TokenType should be suitable for Phel comment analysis`() {
        // Verify that the enum values make sense for Phel language analysis

        // FORM should represent Phel forms/expressions
        val formType = TokenType.FORM
        assertNotNull(formType)

        // COMMENT should represent #_ form comments
        val commentType = TokenType.COMMENT
        assertNotNull(commentType)

        // Should have exactly these two types for the comment analysis use case
        assertEquals(setOf(TokenType.FORM, TokenType.COMMENT), TokenType.entries.toSet())
    }
}
