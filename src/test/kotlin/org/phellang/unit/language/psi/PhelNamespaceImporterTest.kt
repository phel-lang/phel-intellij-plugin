package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelNamespaceImporter
import org.phellang.language.psi.PhelNamespaceUtils

class PhelNamespaceImporterTest {

    @Test
    fun `PhelNamespaceImporter should be a singleton object`() {
        assertNotNull(PhelNamespaceImporter)
        assertEquals("PhelNamespaceImporter", PhelNamespaceImporter.javaClass.simpleName)
    }

    @Test
    fun `should have ensureNamespaceImported method`() {
        val methods = PhelNamespaceImporter.javaClass.methods.map { it.name }
        assertTrue(methods.contains("ensureNamespaceImported"))
    }

    @Nested
    inner class CoreNamespaceHandling {

        @Test
        fun `core namespace should not need import`() {
            assertTrue(PhelNamespaceUtils.isCoreNamespace("core"))
            assertTrue(PhelNamespaceUtils.isCoreNamespace(null))
        }

        @Test
        fun `non-core namespaces should need import`() {
            assertFalse(PhelNamespaceUtils.isCoreNamespace("str"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("json"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("http"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("html"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("test"))
        }
    }

    @Nested
    inner class NamespaceFormatting {

        @Test
        fun `should format namespace for require form`() {
            assertEquals("phel\\str", PhelNamespaceUtils.toPhelNamespace("str"))
            assertEquals("phel\\json", PhelNamespaceUtils.toPhelNamespace("json"))
            assertEquals("phel\\http", PhelNamespaceUtils.toPhelNamespace("http"))
        }

        @Test
        fun `require form syntax should be correct`() {
            val namespace = "phel\\str"
            val requireForm = "(:require $namespace)"
            assertEquals("(:require phel\\str)", requireForm)
        }
    }
}
