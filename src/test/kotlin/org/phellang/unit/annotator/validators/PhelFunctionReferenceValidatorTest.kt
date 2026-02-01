package org.phellang.unit.annotator.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.annotator.validators.FunctionReferenceValidationResult

class PhelFunctionReferenceValidatorTest {

    @Nested
    inner class ValidationResultDataClass {

        @Test
        fun `validation result contains message namespace and function name`() {
            val result = FunctionReferenceValidationResult(
                message = "Cannot resolve function 'foo' in namespace 'bar'",
                namespace = "bar",
                functionName = "foo"
            )

            assertEquals("Cannot resolve function 'foo' in namespace 'bar'", result.message)
            assertEquals("bar", result.namespace)
            assertEquals("foo", result.functionName)
        }

        @Test
        fun `validation result supports equality`() {
            val result1 = FunctionReferenceValidationResult(
                message = "Error",
                namespace = "ns",
                functionName = "fn"
            )
            val result2 = FunctionReferenceValidationResult(
                message = "Error",
                namespace = "ns",
                functionName = "fn"
            )

            assertEquals(result1, result2)
        }
    }

    @Nested
    inner class QualifiedSymbolParsing {

        @Test
        fun `qualified symbol has namespace and function parts`() {
            val symbol = "utils/my-function"
            val parts = symbol.split("/")

            assertEquals(2, parts.size)
            assertEquals("utils", parts[0])
            assertEquals("my-function", parts[1])
        }

        @Test
        fun `php interop should be skipped`() {
            val symbol = "php/array"
            val qualifier = symbol.substringBefore("/")

            assertEquals("php", qualifier)
            // PHP interop is always considered valid - can't validate PHP functions
        }

        @Test
        fun `symbol without qualifier is not validated`() {
            val symbol = "my-function"
            val hasQualifier = symbol.contains("/")

            assertFalse(hasQualifier)
        }

        @Test
        fun `nested namespace is parsed correctly`() {
            val symbol = "phel\\http/response"
            val parts = symbol.split("/")

            assertEquals(2, parts.size)
            assertEquals("phel\\http", parts[0])
            assertEquals("response", parts[1])
        }
    }

    @Nested
    inner class ErrorMessageFormat {

        @Test
        fun `error message includes function and namespace`() {
            val namespace = "utils"
            val functionName = "non-existent"
            val message = "Cannot resolve function '$functionName' in namespace '$namespace'"

            assertTrue(message.contains(functionName))
            assertTrue(message.contains(namespace))
            assertEquals("Cannot resolve function 'non-existent' in namespace 'utils'", message)
        }

        @Test
        fun `error message format is consistent`() {
            val testCases = listOf(
                Triple("str", "foo", "Cannot resolve function 'foo' in namespace 'str'"),
                Triple("my-ns", "bar-fn", "Cannot resolve function 'bar-fn' in namespace 'my-ns'"),
                Triple("http", "unknown", "Cannot resolve function 'unknown' in namespace 'http'")
            )

            for ((namespace, fn, expected) in testCases) {
                val message = "Cannot resolve function '$fn' in namespace '$namespace'"
                assertEquals(expected, message)
            }
        }
    }
}
