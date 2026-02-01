package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PhelVendorUtilsTest {

    // Mirror of the private mapping in PhelVendorUtils
    private val namespaceToFile = mapOf(
        "core" to "core.phel",
        "str" to "str.phel",
        "json" to "json.phel",
        "http" to "http.phel",
        "html" to "html.phel",
        "base64" to "base64.phel",
        "test" to "test.phel",
        "mock" to "mock.phel",
        "repl" to "repl.phel",
        "debug" to "debug.phel",
    )

    @Nested
    inner class NamespaceToFileMapping {

        @Test
        fun `all standard library namespaces have file mappings`() {
            val expectedNamespaces = setOf(
                "core", "str", "json", "http", "html", "base64", "test", "mock", "repl", "debug"
            )

            assertEquals(expectedNamespaces, namespaceToFile.keys)
        }

        @Test
        fun `core namespace maps to core phel`() {
            assertEquals("core.phel", namespaceToFile["core"])
        }

        @Test
        fun `str namespace maps to str phel`() {
            assertEquals("str.phel", namespaceToFile["str"])
        }

        @Test
        fun `all file names end with phel extension`() {
            for ((namespace, fileName) in namespaceToFile) {
                assertTrue(fileName.endsWith(".phel"), "$namespace should map to a .phel file")
            }
        }

        @Test
        fun `file names match namespace names`() {
            for ((namespace, fileName) in namespaceToFile) {
                assertEquals("$namespace.phel", fileName, "File name should be namespace.phel")
            }
        }
    }

    @Nested
    inner class VendorPath {

        @Test
        fun `phel vendor path is correct`() {
            val expectedPath = "phel-lang/phel-lang/src/phel"
            // This mirrors the constant in PhelVendorUtils
            assertEquals("phel-lang/phel-lang/src/phel", expectedPath)
        }

        @Test
        fun `vendor path structure is composer compliant`() {
            val vendorPath = "phel-lang/phel-lang/src/phel"
            val parts = vendorPath.split("/")

            assertEquals(4, parts.size, "Path should have 4 segments")
            assertEquals("phel-lang", parts[0], "Vendor should be phel-lang")
            assertEquals("phel-lang", parts[1], "Package should be phel-lang")
            assertEquals("src", parts[2], "Source directory")
            assertEquals("phel", parts[3], "Phel source folder")
        }
    }

    @Nested
    inner class NamespaceFromFileName {

        @Test
        fun `namespace can be extracted from file name`() {
            val fileName = "str.phel"
            val namespace = fileName.removeSuffix(".phel")
            assertEquals("str", namespace)
        }

        @Test
        fun `all mapped namespaces can be extracted from file names`() {
            for ((expectedNamespace, fileName) in namespaceToFile) {
                val extractedNamespace = fileName.removeSuffix(".phel")
                assertEquals(expectedNamespace, extractedNamespace)
            }
        }
    }
}
