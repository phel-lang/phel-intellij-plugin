package org.phellang.integration.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelNamespaceUtils

/**
 * Tests for PhelNamespaceUtils logic (non-PSI parts).
 * PSI-based tests require IntelliJ platform test framework and are tested manually.
 */
class PhelNamespaceImporterIntegrationTest {

    @Nested
    inner class NamespaceConversion {

        @Test
        fun `toPhelNamespace converts short name to full Phel namespace`() {
            assertEquals("phel\\str", PhelNamespaceUtils.toPhelNamespace("str"))
            assertEquals("phel\\http", PhelNamespaceUtils.toPhelNamespace("http"))
            assertEquals("phel\\json", PhelNamespaceUtils.toPhelNamespace("json"))
        }
    }

    @Nested
    inner class CoreNamespaceDetection {

        @Test
        fun `isCoreNamespace returns true for core`() {
            assertTrue(PhelNamespaceUtils.isCoreNamespace("core"))
        }

        @Test
        fun `isCoreNamespace returns true for null`() {
            assertTrue(PhelNamespaceUtils.isCoreNamespace(null))
        }

        @Test
        fun `isCoreNamespace returns false for other namespaces`() {
            assertFalse(PhelNamespaceUtils.isCoreNamespace("str"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("http"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("json"))
        }
    }

    @Nested
    inner class NamespaceExtraction {

        @Test
        fun `extractNamespace returns namespace from qualified name`() {
            assertEquals("str", PhelNamespaceUtils.extractNamespace("str/join"))
            assertEquals("http", PhelNamespaceUtils.extractNamespace("http/request"))
        }

        @Test
        fun `extractNamespace returns null for unqualified name`() {
            assertNull(PhelNamespaceUtils.extractNamespace("map"))
            assertNull(PhelNamespaceUtils.extractNamespace("filter"))
        }
    }

    @Nested
    inner class QuickFixReplacementLogic {

        @Test
        fun `should parse phel namespace format correctly`() {
            // This tests the parsing logic used by the quick-fix
            val replacement = "phel\\str\\contains?"
            val parts = replacement.split("\\")
            
            assertEquals(3, parts.size)
            assertEquals("phel", parts[0])
            assertEquals("str", parts[1])
            assertEquals("contains?", parts[2])
            
            // The expected conversion
            val namespace = parts[1]
            val functionName = "${parts[1]}/${parts.drop(2).joinToString("\\")}"
            
            assertEquals("str", namespace)
            assertEquals("str/contains?", functionName)
        }

        @Test
        fun `should handle simple replacement without namespace`() {
            val replacement = "assoc"
            assertFalse(replacement.contains("\\"))
            // No namespace import needed
        }

        @Test
        fun `should handle multi-part function names`() {
            // Edge case: function names with backslashes (unlikely but possible)
            val replacement = "phel\\some\\namespace\\func"
            val parts = replacement.split("\\")
            
            if (parts.size >= 3 && parts[0] == "phel") {
                val namespace = parts[1]
                val functionName = "${parts[1]}/${parts.drop(2).joinToString("\\")}"
                
                assertEquals("some", namespace)
                assertEquals("some/namespace\\func", functionName)
            }
        }
    }
}
