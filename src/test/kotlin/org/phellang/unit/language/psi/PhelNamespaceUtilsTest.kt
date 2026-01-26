package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelNamespaceUtils

class PhelNamespaceUtilsTest {

    @Nested
    inner class NamespaceExtraction {

        @Test
        fun `should extract namespace from qualified function name`() {
            assertEquals("str", PhelNamespaceUtils.extractNamespace("str/join"))
            assertEquals("http", PhelNamespaceUtils.extractNamespace("http/request"))
            assertEquals("json", PhelNamespaceUtils.extractNamespace("json/encode"))
        }

        @Test
        fun `should return null for unqualified function name`() {
            assertNull(PhelNamespaceUtils.extractNamespace("map"))
            assertNull(PhelNamespaceUtils.extractNamespace("filter"))
            assertNull(PhelNamespaceUtils.extractNamespace("reduce"))
        }

        @Test
        fun `should handle function names with special characters`() {
            assertEquals("str", PhelNamespaceUtils.extractNamespace("str/contains?"))
            assertEquals("test", PhelNamespaceUtils.extractNamespace("test/is"))
        }
    }

    @Nested
    inner class PhelNamespaceConversion {

        @Test
        fun `should convert short namespace to Phel format`() {
            assertEquals("phel\\str", PhelNamespaceUtils.toPhelNamespace("str"))
            assertEquals("phel\\http", PhelNamespaceUtils.toPhelNamespace("http"))
            assertEquals("phel\\json", PhelNamespaceUtils.toPhelNamespace("json"))
        }
    }

    @Nested
    inner class CoreNamespaceDetection {

        @Test
        fun `should identify core namespace`() {
            assertTrue(PhelNamespaceUtils.isCoreNamespace("core"))
            assertTrue(PhelNamespaceUtils.isCoreNamespace(null))
        }

        @Test
        fun `should not identify non-core namespaces as core`() {
            assertFalse(PhelNamespaceUtils.isCoreNamespace("str"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("http"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("json"))
        }
    }

    @Nested
    inner class ShortNamespaceExtraction {

        @Test
        fun `substringAfterLast extracts short namespace from phel namespace`() {
            assertEquals("str", "phel\\str".substringAfterLast("\\"))
            assertEquals("http", "phel\\http".substringAfterLast("\\"))
            assertEquals("json", "phel\\json".substringAfterLast("\\"))
        }

        @Test
        fun `substringAfterLast handles namespace without prefix`() {
            assertEquals("str", "str".substringAfterLast("\\"))
            assertEquals("core", "core".substringAfterLast("\\"))
        }

        @Test
        fun `substringAfterLast handles nested namespaces`() {
            assertEquals("nested", "phel\\some\\deeply\\nested".substringAfterLast("\\"))
        }
    }
}
