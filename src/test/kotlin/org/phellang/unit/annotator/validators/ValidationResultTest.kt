package org.phellang.unit.annotator.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.annotator.validators.ImportValidationResult
import org.phellang.annotator.validators.NamespaceValidationResult

class ValidationResultTest {

    @Nested
    inner class ImportValidationResultTests {

        @Test
        fun `creates result with all fields`() {
            val result = ImportValidationResult(
                message = "Namespace does not exist",
                suggestedNamespace = "phel-project\\utils",
                isDuplicate = false
            )

            assertEquals("Namespace does not exist", result.message)
            assertEquals("phel-project\\utils", result.suggestedNamespace)
            assertFalse(result.isDuplicate)
        }

        @Test
        fun `isDuplicate defaults to false`() {
            val result = ImportValidationResult(
                message = "Test message",
                suggestedNamespace = null
            )

            assertFalse(result.isDuplicate)
        }

        @Test
        fun `creates duplicate import result`() {
            val result = ImportValidationResult(
                message = "Duplicate import: 'phel\\str' is already imported",
                suggestedNamespace = null,
                isDuplicate = true
            )

            assertTrue(result.isDuplicate)
            assertNull(result.suggestedNamespace)
        }

        @Test
        fun `creates result with suggestion`() {
            val result = ImportValidationResult(
                message = "Did you mean 'phel-project\\utils'?",
                suggestedNamespace = "phel-project\\utils"
            )

            assertNotNull(result.suggestedNamespace)
            assertEquals("phel-project\\utils", result.suggestedNamespace)
        }

        @Test
        fun `creates result without suggestion`() {
            val result = ImportValidationResult(
                message = "Namespace does not exist",
                suggestedNamespace = null
            )

            assertNull(result.suggestedNamespace)
        }

        @Test
        fun `equality works correctly`() {
            val result1 = ImportValidationResult("msg", "ns", false)
            val result2 = ImportValidationResult("msg", "ns", false)
            val result3 = ImportValidationResult("msg", "ns", true)

            assertEquals(result1, result2)
            assertNotEquals(result1, result3)
        }
    }

    @Nested
    inner class NamespaceValidationResultTests {

        @Test
        fun `creates result with all fields`() {
            val result = NamespaceValidationResult(
                message = "Namespace 'str' is not imported",
                fullNamespace = "phel\\str",
                shortNamespace = "str"
            )

            assertEquals("Namespace 'str' is not imported", result.message)
            assertEquals("phel\\str", result.fullNamespace)
            assertEquals("str", result.shortNamespace)
        }

        @Test
        fun `creates result with null fullNamespace`() {
            val result = NamespaceValidationResult(
                message = "Namespace 'unknown' does not exist",
                fullNamespace = null,
                shortNamespace = "unknown"
            )

            assertNull(result.fullNamespace)
            assertEquals("unknown", result.shortNamespace)
        }

        @Test
        fun `creates result for standard library`() {
            val result = NamespaceValidationResult(
                message = "Namespace 'str' is not imported",
                fullNamespace = "phel\\str",
                shortNamespace = "str"
            )

            assertEquals("phel\\str", result.fullNamespace)
        }

        @Test
        fun `creates result for project namespace`() {
            val result = NamespaceValidationResult(
                message = "Namespace 'utils' is not imported",
                fullNamespace = "phel-project\\utils",
                shortNamespace = "utils"
            )

            assertEquals("phel-project\\utils", result.fullNamespace)
            assertEquals("utils", result.shortNamespace)
        }

        @Test
        fun `equality works correctly`() {
            val result1 = NamespaceValidationResult("msg", "full", "short")
            val result2 = NamespaceValidationResult("msg", "full", "short")
            val result3 = NamespaceValidationResult("msg", null, "short")

            assertEquals(result1, result2)
            assertNotEquals(result1, result3)
        }

        @Test
        fun `copy works correctly`() {
            val result = NamespaceValidationResult("original", "phel\\str", "str")
            val copied = result.copy(message = "modified")

            assertEquals("modified", copied.message)
            assertEquals("phel\\str", copied.fullNamespace)
            assertEquals("str", copied.shortNamespace)
        }
    }

    @Nested
    inner class ComparisonTests {

        @Test
        fun `both result types have message field`() {
            val importResult = ImportValidationResult("import message", null)
            val namespaceResult = NamespaceValidationResult("namespace message", null, "short")

            assertNotNull(importResult.message)
            assertNotNull(namespaceResult.message)
        }

        @Test
        fun `suggested namespace in ImportResult serves same purpose as fullNamespace in NamespaceResult`() {
            // Both fields store the full namespace that can be used for quick fixes
            val importResult = ImportValidationResult(
                message = "msg",
                suggestedNamespace = "phel\\str"
            )
            val namespaceResult = NamespaceValidationResult(
                message = "msg",
                fullNamespace = "phel\\str",
                shortNamespace = "str"
            )

            // Both can be used to import the correct namespace
            assertEquals(importResult.suggestedNamespace, namespaceResult.fullNamespace)
        }
    }
}
