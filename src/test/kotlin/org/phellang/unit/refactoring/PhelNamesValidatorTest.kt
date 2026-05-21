package org.phellang.unit.refactoring

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.refactoring.PhelNamesValidator

class PhelNamesValidatorTest {

    private val validator = PhelNamesValidator()

    @Test
    fun `accepts ordinary identifiers`() {
        assertTrue(validator.isIdentifier("foo", null))
        assertTrue(validator.isIdentifier("foo-bar", null))
        assertTrue(validator.isIdentifier("foo_bar", null))
    }

    @Test
    fun `accepts predicate names`() {
        assertTrue(validator.isIdentifier("nil?", null))
        assertTrue(validator.isIdentifier("set!", null))
    }

    @Test
    fun `accepts arithmetic operators`() {
        assertTrue(validator.isIdentifier("+", null))
        assertTrue(validator.isIdentifier("-", null))
        assertTrue(validator.isIdentifier("*", null))
        assertTrue(validator.isIdentifier("=", null))
        assertTrue(validator.isIdentifier("<=", null))
    }

    @Test
    fun `rejects empty name`() {
        assertFalse(validator.isIdentifier("", null))
    }

    @Test
    fun `rejects name starting with digit`() {
        assertFalse(validator.isIdentifier("1foo", null))
    }

    @Test
    fun `rejects leading reader-macro and keyword sigils`() {
        assertFalse(validator.isIdentifier(":foo", null))
        assertFalse(validator.isIdentifier("'foo", null))
        assertFalse(validator.isIdentifier("`foo", null))
        assertFalse(validator.isIdentifier("~foo", null))
        assertFalse(validator.isIdentifier("@foo", null))
        assertFalse(validator.isIdentifier("#foo", null))
    }

    @Test
    fun `rejects whitespace and parens`() {
        assertFalse(validator.isIdentifier("foo bar", null))
        assertFalse(validator.isIdentifier("(foo)", null))
    }

    @Test
    fun `marks reserved words as keywords`() {
        assertTrue(validator.isKeyword("def", null))
        assertTrue(validator.isKeyword("defn", null))
        assertTrue(validator.isKeyword("ns", null))
        assertFalse(validator.isKeyword("foo", null))
    }
}
