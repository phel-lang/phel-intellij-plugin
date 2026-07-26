package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.phellang.language.psi.PhelSymbolChars

class PhelSymbolCharsTest {

    @ParameterizedTest
    @ValueSource(chars = ['a', 'z', 'A', 'Z', '0', '9'])
    fun `alphanumerics are symbol characters`(c: Char) {
        assertTrue(PhelSymbolChars.isSymbolStart(c), "'$c' should be able to open a symbol")
        assertTrue(PhelSymbolChars.isSymbolPart(c), "'$c' should be able to continue a symbol")
    }

    @ParameterizedTest
    @ValueSource(chars = ['-', '_', '?', '!', '*', '+', '<', '>', '=', '&', '/', ':', '%', '$', '.', '\\'])
    fun `the punctuation Phel symbols actually use is accepted`(c: Char) {
        assertTrue(PhelSymbolChars.isSymbolStart(c), "'$c' should be able to open a symbol")
        assertTrue(PhelSymbolChars.isSymbolPart(c), "'$c' should be able to continue a symbol")
    }

    @ParameterizedTest
    @ValueSource(chars = ['(', ')', '[', ']', '{', '}', ' ', '\n', '\r', '\t', ',', '`', '@', ';', '~', '^', '"'])
    fun `brackets, whitespace and sigils are never symbol characters`(c: Char) {
        assertFalse(PhelSymbolChars.isSymbolStart(c), "'$c' should not open a symbol")
        assertFalse(PhelSymbolChars.isSymbolPart(c), "'$c' should not continue a symbol")
    }

    /** `,` is whitespace in Phel, not punctuation. */
    @Test
    fun `comma is whitespace`() {
        assertFalse(PhelSymbolChars.isSymbolPart(','))
    }

    /**
     * `'` and `#` are the one asymmetry in the lexer's two classes: `ATOM_START` excludes them,
     * `ATOM_CONT` does not.
     */
    @Test
    fun `quote and hash may continue a symbol but not open one`() {
        listOf('\'', '#').forEach { c ->
            assertFalse(PhelSymbolChars.isSymbolStart(c), "'$c' is a reader sigil at the start of a symbol")
            assertTrue(PhelSymbolChars.isSymbolPart(c), "'$c' is ordinary inside a symbol")
        }
    }

    @Test
    fun `every character of the names Phel really uses is accepted`() {
        val names = listOf(
            "str/split", "json/encode", "php/->", "php/::", "mock/with-mock",
            "nil?", "set!", "<=", "*out*", "%", "%1",
            ".method", ".-field", "DateTime.", "\\DateTimeImmutable", "\\Foo\\Bar",
        )

        names.forEach { name ->
            assertTrue(
                PhelSymbolChars.isSymbolStart(name.first()) && name.all(PhelSymbolChars::isSymbolPart),
                "every character of '$name' should be a symbol character",
            )
        }
    }
}
