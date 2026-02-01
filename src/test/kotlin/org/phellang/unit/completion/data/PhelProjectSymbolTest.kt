package org.phellang.unit.completion.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.data.SymbolType

class PhelProjectSymbolTest {

    @Nested
    inner class SymbolTypeTests {

        @Test
        fun `fromKeyword returns correct type for defn`() {
            assertEquals(SymbolType.FUNCTION, SymbolType.fromKeyword("defn"))
        }

        @Test
        fun `fromKeyword returns correct type for def`() {
            assertEquals(SymbolType.VALUE, SymbolType.fromKeyword("def"))
        }

        @Test
        fun `fromKeyword returns correct type for defmacro`() {
            assertEquals(SymbolType.MACRO, SymbolType.fromKeyword("defmacro"))
        }

        @Test
        fun `fromKeyword returns correct type for defstruct`() {
            assertEquals(SymbolType.STRUCT, SymbolType.fromKeyword("defstruct"))
        }

        @Test
        fun `fromKeyword returns correct type for definterface`() {
            assertEquals(SymbolType.INTERFACE, SymbolType.fromKeyword("definterface"))
        }

        @Test
        fun `fromKeyword returns null for unknown keyword`() {
            assertNull(SymbolType.fromKeyword("unknown"))
            assertNull(SymbolType.fromKeyword("let"))
            assertNull(SymbolType.fromKeyword("fn"))
        }

        @Test
        fun `definingKeywords contains all definition keywords`() {
            val keywords = SymbolType.definingKeywords

            assertTrue(keywords.contains("defn"))
            assertTrue(keywords.contains("def"))
            assertTrue(keywords.contains("defmacro"))
            assertTrue(keywords.contains("defstruct"))
            assertTrue(keywords.contains("definterface"))
            assertEquals(5, keywords.size)
        }

        @Test
        fun `definingKeywords does not contain non-definition keywords`() {
            val keywords = SymbolType.definingKeywords

            assertFalse(keywords.contains("let"))
            assertFalse(keywords.contains("fn"))
            assertFalse(keywords.contains("if"))
            assertFalse(keywords.contains("when"))
        }
    }

    @Nested
    inner class SymbolTypeEnumValues {

        @Test
        fun `all symbol types have correct keywords`() {
            assertEquals("defn", SymbolType.FUNCTION.keyword)
            assertEquals("def", SymbolType.VALUE.keyword)
            assertEquals("defmacro", SymbolType.MACRO.keyword)
            assertEquals("defstruct", SymbolType.STRUCT.keyword)
            assertEquals("definterface", SymbolType.INTERFACE.keyword)
        }

        @Test
        fun `all entries are accessible`() {
            val entries = SymbolType.entries
            assertEquals(5, entries.size)
        }
    }
}
