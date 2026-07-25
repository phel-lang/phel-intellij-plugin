package org.phellang.unit.completion.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.registry.SymbolType

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
        fun `fromKeyword returns correct type for defexception`() {
            assertEquals(SymbolType.EXCEPTION, SymbolType.fromKeyword("defexception"))
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
            assertTrue(keywords.contains("defexception"))
            assertTrue(keywords.contains("defonce"))
            assertTrue(keywords.contains("defrecord"))
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
            assertEquals("defn", SymbolType.FUNCTION.primaryKeyword)
            assertEquals("def", SymbolType.VALUE.primaryKeyword)
            assertEquals("defmacro", SymbolType.MACRO.primaryKeyword)
            assertEquals("defstruct", SymbolType.STRUCT.primaryKeyword)
            assertEquals("definterface", SymbolType.INTERFACE.primaryKeyword)
            assertEquals("defexception", SymbolType.EXCEPTION.primaryKeyword)

            // Private and starred spellings resolve to the same type as their public form.
            assertEquals(SymbolType.FUNCTION, SymbolType.fromKeyword("defn-"))
            assertEquals(SymbolType.VALUE, SymbolType.fromKeyword("defonce"))
            assertEquals(SymbolType.STRUCT, SymbolType.fromKeyword("defstruct*"))
        }

        @Test
        fun `all entries are accessible`() {
            // Membership is pinned against PhelSpecialForms.DEFINITION_FORMS by
            // PhelDefinitionFormCoverageTest; this only guards the enum being reachable at all.
            assertTrue(SymbolType.entries.isNotEmpty())
        }
    }
}
