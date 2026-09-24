package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.phellang.language.psi.PhelTypeTags
import org.phellang.language.psi.PhelTypeTags.Member

class PhelTypeTagsTest {

    /** Phel 0.53.0's `TagResolver::TYPE_ALIASES`, the one table the compiler reads the short tags from. */
    @Test
    fun `the seven value aliases name the classes Phel backs them with`() {
        assertEquals(
            mapOf(
                "map" to "Phel\\Lang\\Collections\\Map\\PersistentMapInterface",
                "vector" to "Phel\\Lang\\Collections\\Vector\\PersistentVectorInterface",
                "set" to "Phel\\Lang\\Collections\\HashSet\\PersistentHashSetInterface",
                "list" to "Phel\\Lang\\Collections\\LinkedList\\PersistentListInterface",
                "keyword" to "Phel\\Lang\\Keyword",
                "symbol" to "Phel\\Lang\\Symbol",
                "atom" to "Phel\\Lang\\Atom",
            ),
            PhelTypeTags.VALUE_TYPE_ALIASES,
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["int", "float", "string", "bool", "array", "callable", "mixed", "null"])
    fun `common PHP types are offered`(type: String) {
        assertTrue(type in PhelTypeTags.PHP_TYPES)
    }

    @Test
    fun `a value alias and a PHP type never share a name`() {
        assertTrue(PhelTypeTags.PHP_TYPES.none { it in PhelTypeTags.VALUE_TYPE_ALIASES })
    }

    @Test
    fun `a plain tag is one member`() {
        assertEquals(listOf(Member("map", nullable = false)), PhelTypeTags.members("map"))
    }

    @Test
    fun `a leading question mark makes the member nullable`() {
        assertEquals(listOf(Member("map", nullable = true)), PhelTypeTags.members("?map"))
    }

    @Test
    fun `a union lists each member in order`() {
        assertEquals(
            listOf(Member("map", nullable = false), Member("null", nullable = false)),
            PhelTypeTags.members("map|null"),
        )
    }

    @Test
    fun `an intersection lists each member in order`() {
        assertEquals(
            listOf(Member("Foo", nullable = false), Member("Bar", nullable = false)),
            PhelTypeTags.members("Foo&Bar"),
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "?", "|", "?|&"])
    fun `a tag with no name in it has no members`(tag: String) {
        assertEquals(emptyList<Member>(), PhelTypeTags.members(tag))
    }

    @Test
    fun `only the value aliases have a backing class`() {
        assertEquals("Phel\\Lang\\Atom", PhelTypeTags.backingClass("atom"))
        assertNull(PhelTypeTags.backingClass("int"))
        assertNull(PhelTypeTags.backingClass("DateTime"))
    }
}
