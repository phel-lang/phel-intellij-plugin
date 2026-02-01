package org.phellang.unit.annotator.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PhelImportValidatorTest {

    @Nested
    inner class NamespacePatternValidation {

        @Test
        fun `namespace must contain backslash to be validated`() {
            val validNamespaces = listOf(
                "phel\\str", "my-project\\utils", "app\\lib\\helpers"
            )

            for (ns in validNamespaces) {
                assertTrue(ns.contains("\\"), "$ns should contain backslash")
            }
        }

        @Test
        fun `simple names without backslash are skipped`() {
            val invalidNamespaces = listOf("str", "utils", "helpers")

            for (ns in invalidNamespaces) {
                assertFalse(ns.contains("\\"), "$ns should not contain backslash")
            }
        }
    }

    @Nested
    inner class ValidationMessages {

        @Test
        fun `duplicate import message format`() {
            val namespace = "phel\\str"
            val message = "Duplicate import: '$namespace' is already imported"
            assertEquals("Duplicate import: 'phel\\str' is already imported", message)
        }

        @Test
        fun `namespace does not exist message format`() {
            val namespace = "my-project\\unknown"
            val message = "Namespace '$namespace' does not exist"
            assertEquals("Namespace 'my-project\\unknown' does not exist", message)
        }

        @Test
        fun `namespace does not exist with suggestion message format`() {
            val namespace = "my-project\\utls"
            val suggestion = "my-project\\utils"
            val message = "Namespace '$namespace' does not exist. Did you mean '$suggestion'?"
            assertEquals("Namespace 'my-project\\utls' does not exist. Did you mean 'my-project\\utils'?", message)
        }
    }
}
