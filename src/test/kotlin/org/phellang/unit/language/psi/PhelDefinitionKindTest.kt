package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelDefinitionKind

class PhelDefinitionKindTest {

    /**
     * Pins the exact keyword-to-noun mapping that `PhelItemPresentation` and
     * `PhelFindUsagesProvider` each used to spell out by hand. If this table and those two
     * call sites ever disagree again, that is the drift this enum exists to prevent.
     */
    @Test
    fun `every defining keyword maps to the noun navigation shows`() {
        val expected = mapOf(
            "def" to "variable",
            "def-" to "variable",
            "defn" to "function",
            "defn-" to "function",
            "defmacro" to "macro",
            "defmacro-" to "macro",
            "defstruct" to "struct",
            "definterface" to "interface",
            "defexception" to "exception",
            "declare" to "declaration",
        )

        expected.forEach { (keyword, noun) ->
            assertEquals(noun, PhelDefinitionKind.fromKeyword(keyword)?.noun, "keyword `$keyword`")
        }
    }

    @Test
    fun `the enum declares no keyword beyond that mapping`() {
        val declared = PhelDefinitionKind.entries.flatMap { it.keywords }.toSet()

        assertEquals(
            setOf(
                "def", "def-", "defn", "defn-", "defmacro", "defmacro-",
                "defstruct", "definterface", "defexception", "declare",
            ),
            declared,
        )
    }

    @Test
    fun `keyword lookup round-trips every constant`() {
        PhelDefinitionKind.entries.forEach { kind ->
            kind.keywords.forEach { keyword ->
                assertEquals(kind, PhelDefinitionKind.fromKeyword(keyword), "keyword `$keyword`")
            }
        }
    }

    @Test
    fun `keywords are not shared between kinds`() {
        val all = PhelDefinitionKind.entries.flatMap { it.keywords }

        assertEquals(all.size, all.toSet().size)
    }

    /** Binding forms and calls are not definitions, so navigation falls back to its own default. */
    @Test
    fun `unknown, empty and null keywords resolve to nothing`() {
        assertNull(PhelDefinitionKind.fromKeyword(null))
        assertNull(PhelDefinitionKind.fromKeyword(""))
        assertNull(PhelDefinitionKind.fromKeyword("let"))
        assertNull(PhelDefinitionKind.fromKeyword("deftest"))
        assertNull(PhelDefinitionKind.fromKeyword("ns"))
    }
}
