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

    /** `%` and `%1` are the anonymous-function parameters; the old character list rejected them. */
    @Test
    fun `accepts the anonymous-function parameters`() {
        assertTrue(validator.isIdentifier("%", null))
        assertTrue(validator.isIdentifier("%1", null))
    }

    @Test
    fun `accepts interop shorthands and PHP class references`() {
        assertTrue(validator.isIdentifier(".method", null))
        assertTrue(validator.isIdentifier(".-field", null))
        assertTrue(validator.isIdentifier("\\DateTimeImmutable", null))
    }

    @Test
    fun `marks reserved words as keywords`() {
        assertTrue(validator.isKeyword("def", null))
        assertTrue(validator.isKeyword("defn", null))
        assertTrue(validator.isKeyword("ns", null))
        assertFalse(validator.isKeyword("foo", null))
    }

    /**
     * The forms the hand-written reserved list had drifted away from. It carried `defstruct` but not
     * `defstruct*`, and none of the binding or dispatch forms at all, so renaming a symbol to any of
     * these was accepted in silence.
     */
    @Test
    fun `marks the forms the old hand-written list was missing`() {
        val previouslyMissing = listOf(
            "defstruct*", "definterface*", "defexception*",
            "defonce", "defenum", "defprotocol", "defrecord", "deftype", "defmulti", "deftest",
            "if-let", "when-let", "case", "cond", "for", "foreach", "binding",
        )

        previouslyMissing.forEach { name ->
            assertTrue(validator.isKeyword(name, null), "'$name' is a Phel form and must read as a keyword")
        }
    }

    @Test
    fun `still marks the literals as keywords`() {
        listOf("true", "false", "nil").forEach { name ->
            assertTrue(validator.isKeyword(name, null), "'$name' is a literal, not a free name")
        }
    }
}
