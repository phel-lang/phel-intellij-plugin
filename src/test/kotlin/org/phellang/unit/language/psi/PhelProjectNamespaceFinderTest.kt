package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelProjectNamespaceFinder

class PhelProjectNamespaceFinderTest {

    @Nested
    inner class ExtractShortNamespace {

        @Test
        fun `extracts short name from full namespace`() {
            assertEquals("utils", PhelProjectNamespaceFinder.extractShortNamespace("phel-project\\utils"))
            assertEquals("math", PhelProjectNamespaceFinder.extractShortNamespace("phel-project\\math"))
            assertEquals("core", PhelProjectNamespaceFinder.extractShortNamespace("phel\\core"))
        }

        @Test
        fun `handles nested namespaces`() {
            assertEquals("helpers", PhelProjectNamespaceFinder.extractShortNamespace("my-app\\lib\\helpers"))
            assertEquals("deep", PhelProjectNamespaceFinder.extractShortNamespace("a\\b\\c\\deep"))
        }

        @Test
        fun `returns same string if no separator`() {
            assertEquals("utils", PhelProjectNamespaceFinder.extractShortNamespace("utils"))
            assertEquals("core", PhelProjectNamespaceFinder.extractShortNamespace("core"))
        }

        @Test
        fun `handles empty string`() {
            assertEquals("", PhelProjectNamespaceFinder.extractShortNamespace(""))
        }
    }

    @Nested
    inner class IsStandardLibrary {

        @Test
        fun `recognizes standard library namespaces`() {
            assertTrue(PhelProjectNamespaceFinder.isStandardLibrary("phel\\str"))
            assertTrue(PhelProjectNamespaceFinder.isStandardLibrary("phel\\json"))
            assertTrue(PhelProjectNamespaceFinder.isStandardLibrary("phel\\core"))
            assertTrue(PhelProjectNamespaceFinder.isStandardLibrary("phel\\http"))
            assertTrue(PhelProjectNamespaceFinder.isStandardLibrary("phel\\html"))
        }

        @Test
        fun `rejects non-standard namespaces`() {
            assertFalse(PhelProjectNamespaceFinder.isStandardLibrary("phel-project\\utils"))
            assertFalse(PhelProjectNamespaceFinder.isStandardLibrary("my-app\\core"))
            assertFalse(PhelProjectNamespaceFinder.isStandardLibrary("str"))
        }
    }

    @Nested
    inner class StandardLibraryNamespaces {

        @Test
        fun `all expected standard library namespaces are defined`() {
            val expected = setOf(
                "phel\\str",
                "phel\\json",
                "phel\\http",
                "phel\\html",
                "phel\\base64",
                "phel\\test",
                "phel\\mock",
                "phel\\repl",
                "phel\\debug",
                "phel\\core"
            )
            assertEquals(expected, PhelProjectNamespaceFinder.STANDARD_LIBRARY_NAMESPACES)
        }

        @Test
        fun `short to full mapping contains all namespaces`() {
            val shortToFull = PhelProjectNamespaceFinder.STANDARD_LIBRARY_SHORT_TO_FULL
            
            assertEquals("phel\\str", shortToFull["str"])
            assertEquals("phel\\json", shortToFull["json"])
            assertEquals("phel\\core", shortToFull["core"])
        }

        @Test
        fun `getStandardLibraryFullNamespace returns correct namespace`() {
            assertEquals("phel\\str", PhelProjectNamespaceFinder.getStandardLibraryFullNamespace("str"))
            assertEquals("phel\\json", PhelProjectNamespaceFinder.getStandardLibraryFullNamespace("json"))
            assertEquals("phel\\core", PhelProjectNamespaceFinder.getStandardLibraryFullNamespace("core"))
        }

        @Test
        fun `getStandardLibraryFullNamespace is case insensitive`() {
            assertEquals("phel\\str", PhelProjectNamespaceFinder.getStandardLibraryFullNamespace("STR"))
            assertEquals("phel\\json", PhelProjectNamespaceFinder.getStandardLibraryFullNamespace("JSON"))
        }

        @Test
        fun `getStandardLibraryFullNamespace returns null for unknown`() {
            assertNull(PhelProjectNamespaceFinder.getStandardLibraryFullNamespace("unknown"))
            assertNull(PhelProjectNamespaceFinder.getStandardLibraryFullNamespace("php"))
        }
    }
}
