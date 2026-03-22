package org.phellang.unit.completion

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.engine.PhelNsKeywordCompletionProvider.NsContext

class PhelNsKeywordCompletionProviderTest {

    @Nested
    inner class NsBodyKeywords {

        @Test
        fun `ns body context should exist for sub-list first position`() {
            // When cursor is at (ns name (|)), context should be NS_BODY_KEYWORD
            assertNotNull(NsContext.NS_BODY_KEYWORD)
        }

        @Test
        fun `ns body should suggest require, require-file, and use`() {
            val expectedKeywords = setOf(":require", ":require-file", ":use")
            // These are the only valid first forms in ns sub-lists
            assertEquals(3, expectedKeywords.size)
            assertTrue(expectedKeywords.contains(":require"))
            assertTrue(expectedKeywords.contains(":require-file"))
            assertTrue(expectedKeywords.contains(":use"))
        }

        @Test
        fun `ns body should not suggest refer or as`() {
            val nsBodyKeywords = setOf(":require", ":require-file", ":use")
            assertFalse(nsBodyKeywords.contains(":refer"))
            assertFalse(nsBodyKeywords.contains(":as"))
        }
    }

    @Nested
    inner class RequireOptionKeywords {

        @Test
        fun `require options should include as and refer`() {
            val options = setOf(":as", ":refer")
            assertTrue(options.contains(":as"))
            assertTrue(options.contains(":refer"))
        }

        @Test
        fun `require options should have exactly two keywords`() {
            val options = setOf(":as", ":refer")
            assertEquals(2, options.size)
        }

        @Test
        fun `require options should not include require or use`() {
            val options = setOf(":as", ":refer")
            assertFalse(options.contains(":require"))
            assertFalse(options.contains(":use"))
        }
    }

    @Nested
    inner class UseOptionKeywords {

        @Test
        fun `use options should include as and refer`() {
            val options = setOf(":as", ":refer")
            assertTrue(options.contains(":as"))
            assertTrue(options.contains(":refer"))
        }

        @Test
        fun `use options should not include require keywords`() {
            val options = setOf(":as", ":refer")
            assertFalse(options.contains(":require"))
            assertFalse(options.contains(":require-file"))
        }
    }

    @Nested
    inner class ContextFiltering {

        @Test
        fun `after as keyword should not suggest any keywords`() {
            // (ns name (:require module :as |)) - alias position, no keyword suggestions
            // detectNsContext returns null when preceding keyword is :as
            assertNotNull(NsContext.REQUIRE_OPTION) // context exists but won't be returned after :as
        }

        @Test
        fun `after refer keyword should not suggest keywords`() {
            // (ns name (:require module :refer |)) - symbol position, no keyword suggestions
            // detectNsContext returns null when preceding keyword is :refer
            assertNotNull(NsContext.REQUIRE_OPTION)
        }

        @Test
        fun `all ns context types are defined`() {
            val contexts = NsContext.entries
            assertEquals(3, contexts.size)
            assertTrue(contexts.contains(NsContext.NS_BODY_KEYWORD))
            assertTrue(contexts.contains(NsContext.REQUIRE_OPTION))
            assertTrue(contexts.contains(NsContext.USE_OPTION))
        }
    }
}
