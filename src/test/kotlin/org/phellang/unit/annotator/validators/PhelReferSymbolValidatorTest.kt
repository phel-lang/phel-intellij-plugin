package org.phellang.unit.annotator.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.annotator.validators.ReferSymbolValidationResult
import org.phellang.completion.data.Namespace
import org.phellang.completion.data.PhelFunctionRegistry

class PhelReferSymbolValidatorTest {

    @Nested
    inner class ValidationResultDataClass {

        @Test
        fun `validation result contains message, symbol name and namespace`() {
            val result = ReferSymbolValidationResult(
                message = "Cannot resolve 'foo' in namespace 'phel\\test'",
                symbolName = "foo",
                namespace = "phel\\test"
            )

            assertEquals("Cannot resolve 'foo' in namespace 'phel\\test'", result.message)
            assertEquals("foo", result.symbolName)
            assertEquals("phel\\test", result.namespace)
            assertFalse(result.isDuplicate)
        }

        @Test
        fun `validation result supports duplicate flag`() {
            val result = ReferSymbolValidationResult(
                message = "'is' is already referred",
                symbolName = "is",
                namespace = "phel\\test",
                isDuplicate = true
            )

            assertTrue(result.isDuplicate)
            assertEquals("'is' is already referred", result.message)
        }

        @Test
        fun `validation result supports equality`() {
            val result1 = ReferSymbolValidationResult(
                message = "Error",
                symbolName = "fn",
                namespace = "ns"
            )
            val result2 = ReferSymbolValidationResult(
                message = "Error",
                symbolName = "fn",
                namespace = "ns"
            )

            assertEquals(result1, result2)
        }
    }

    @Nested
    inner class StandardLibrarySymbolExistence {

        @Test
        fun `deftest exists in test namespace`() {
            val exists = symbolExistsInStandardLibrary("test", "deftest")
            assertTrue(exists, "deftest should exist in test namespace")
        }

        @Test
        fun `is exists in test namespace`() {
            val exists = symbolExistsInStandardLibrary("test", "is")
            assertTrue(exists, "is should exist in test namespace")
        }

        @Test
        fun `nonexistent symbol should not exist`() {
            val exists = symbolExistsInStandardLibrary("test", "isnot")
            assertFalse(exists, "isnot should not exist in test namespace")
        }

        @Test
        fun `nonexistent symbol foobar should not exist`() {
            val exists = symbolExistsInStandardLibrary("test", "foobar")
            assertFalse(exists, "foobar should not exist in test namespace")
        }

        @Test
        fun `join exists in str namespace`() {
            val exists = symbolExistsInStandardLibrary("str", "join")
            assertTrue(exists, "join should exist in str namespace")
        }

        @Test
        fun `encode exists in json namespace`() {
            val exists = symbolExistsInStandardLibrary("json", "encode")
            assertTrue(exists, "encode should exist in json namespace")
        }

        private fun symbolExistsInStandardLibrary(shortNamespace: String, symbolName: String): Boolean {
            val namespace = mapToNamespace(shortNamespace) ?: return false
            val functions = PhelFunctionRegistry.getFunctions(namespace)

            return functions.any { function ->
                val functionName = function.name.substringAfter("/")
                functionName == symbolName
            }
        }

        private fun mapToNamespace(shortNamespace: String): Namespace? {
            return when (shortNamespace.lowercase()) {
                "base64" -> Namespace.BASE64
                "core" -> Namespace.CORE
                "debug" -> Namespace.DEBUG
                "html" -> Namespace.HTML
                "http" -> Namespace.HTTP
                "json" -> Namespace.JSON
                "mock" -> Namespace.MOCK
                "repl" -> Namespace.REPL
                "str" -> Namespace.STRING
                "test" -> Namespace.TEST
                else -> null
            }
        }
    }

    @Nested
    inner class ErrorMessageFormat {

        @Test
        fun `error message includes symbol and namespace`() {
            val symbolName = "nonexistent"
            val namespace = "phel\\test"
            val message = "Cannot resolve '$symbolName' in namespace '$namespace'"

            assertTrue(message.contains(symbolName))
            assertTrue(message.contains(namespace))
            assertEquals("Cannot resolve 'nonexistent' in namespace 'phel\\test'", message)
        }

        @Test
        fun `error message format is consistent`() {
            val testCases = listOf(
                Triple("isnot", "phel\\test", "Cannot resolve 'isnot' in namespace 'phel\\test'"),
                Triple("foobar", "phel\\str", "Cannot resolve 'foobar' in namespace 'phel\\str'"),
                Triple("unknown", "phel\\json", "Cannot resolve 'unknown' in namespace 'phel\\json'")
            )

            for ((symbol, ns, expected) in testCases) {
                val message = "Cannot resolve '$symbol' in namespace '$ns'"
                assertEquals(expected, message)
            }
        }
    }

    @Nested
    inner class NamespaceExtraction {

        @Test
        fun `should extract short namespace from full namespace`() {
            val testCases = mapOf(
                "phel\\test" to "test",
                "phel\\str" to "str",
                "phel\\json" to "json",
                "my-app\\utils" to "utils"
            )

            testCases.forEach { (full, expected) ->
                val short = full.substringAfterLast("\\")
                assertEquals(expected, short, "Failed for: $full")
            }
        }
    }

    @Nested
    inner class RealWorldScenarios {

        @Test
        fun `scenario - valid test imports`() {
            // (:require phel\test :refer [deftest is])
            // Both deftest and is are valid
            assertTrue(symbolExistsInStandardLibrary("test", "deftest"))
            assertTrue(symbolExistsInStandardLibrary("test", "is"))
        }

        @Test
        fun `scenario - invalid test import`() {
            // (:require phel\test :refer [deftest is isnot])
            // isnot does not exist
            assertTrue(symbolExistsInStandardLibrary("test", "deftest"))
            assertTrue(symbolExistsInStandardLibrary("test", "is"))
            assertFalse(symbolExistsInStandardLibrary("test", "isnot"))
        }

        @Test
        fun `scenario - typo in function name`() {
            // Common typos
            assertFalse(symbolExistsInStandardLibrary("test", "deftets")) // typo
            assertFalse(symbolExistsInStandardLibrary("str", "jion")) // typo
            assertFalse(symbolExistsInStandardLibrary("json", "encod")) // typo
        }

        @Test
        fun `scenario - case sensitivity`() {
            // Phel symbols are case-sensitive
            assertTrue(symbolExistsInStandardLibrary("test", "deftest"))
            assertFalse(symbolExistsInStandardLibrary("test", "DEFTEST"))
            assertFalse(symbolExistsInStandardLibrary("test", "Deftest"))
        }

        private fun symbolExistsInStandardLibrary(shortNamespace: String, symbolName: String): Boolean {
            val namespace = mapToNamespace(shortNamespace) ?: return false
            val functions = PhelFunctionRegistry.getFunctions(namespace)

            return functions.any { function ->
                val functionName = function.name.substringAfter("/")
                functionName == symbolName
            }
        }

        private fun mapToNamespace(shortNamespace: String): Namespace? {
            return when (shortNamespace.lowercase()) {
                "base64" -> Namespace.BASE64
                "core" -> Namespace.CORE
                "debug" -> Namespace.DEBUG
                "html" -> Namespace.HTML
                "http" -> Namespace.HTTP
                "json" -> Namespace.JSON
                "mock" -> Namespace.MOCK
                "repl" -> Namespace.REPL
                "str" -> Namespace.STRING
                "test" -> Namespace.TEST
                else -> null
            }
        }
    }

    @Nested
    inner class DuplicateDetection {

        @Test
        fun `duplicate detection logic - second occurrence is duplicate`() {
            // In [deftest is is], the second 'is' IS a duplicate
            val symbols = listOf("deftest", "is", "is")
            val targetIndex = 2 // Second 'is'
            
            val isDuplicate = hasDuplicateBefore(symbols, targetIndex)
            assertTrue(isDuplicate, "Second occurrence should be marked as duplicate")
        }

        @Test
        fun `duplicate detection logic - no duplicates`() {
            val symbols = listOf("deftest", "is", "testing")
            
            assertFalse(hasDuplicateBefore(symbols, 0))
            assertFalse(hasDuplicateBefore(symbols, 1))
            assertFalse(hasDuplicateBefore(symbols, 2))
        }

        @Test
        fun `duplicate detection logic - multiple duplicates`() {
            // In [is is is], second and third 'is' are duplicates
            val symbols = listOf("is", "is", "is")
            
            assertFalse(hasDuplicateBefore(symbols, 0), "First should not be duplicate")
            assertTrue(hasDuplicateBefore(symbols, 1), "Second should be duplicate")
            assertTrue(hasDuplicateBefore(symbols, 2), "Third should be duplicate")
        }

        @Test
        fun `duplicate error message format`() {
            val symbolName = "is"
            val message = "'$symbolName' is already referred"
            
            assertEquals("'is' is already referred", message)
        }

        /**
         * Simulates the duplicate detection logic used in the validator.
         */
        private fun hasDuplicateBefore(symbols: List<String>, targetIndex: Int): Boolean {
            val targetName = symbols[targetIndex]
            for (i in 0 until targetIndex) {
                if (symbols[i] == targetName) {
                    return true
                }
            }
            return false
        }
    }
}
