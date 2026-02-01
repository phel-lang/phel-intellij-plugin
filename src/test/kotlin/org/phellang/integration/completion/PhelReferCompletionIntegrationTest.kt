package org.phellang.integration.completion

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.data.Namespace
import org.phellang.completion.data.PhelFunctionRegistry

class PhelReferCompletionIntegrationTest {

    @Nested
    inner class TestNamespaceFunctions {

        @Test
        fun `test namespace should have deftest function`() {
            val functions = PhelFunctionRegistry.getFunctions(Namespace.TEST)
            val functionNames = functions.map { it.name.substringAfter("/") }

            assertTrue(functionNames.contains("deftest"), "Should contain deftest")
        }

        @Test
        fun `test namespace should have is function`() {
            val functions = PhelFunctionRegistry.getFunctions(Namespace.TEST)
            val functionNames = functions.map { it.name.substringAfter("/") }

            assertTrue(functionNames.contains("is"), "Should contain is")
        }

        @Test
        fun `test namespace functions should be non-empty`() {
            val functions = PhelFunctionRegistry.getFunctions(Namespace.TEST)

            assertTrue(functions.isNotEmpty(), "Test namespace should have functions")
        }
    }

    @Nested
    inner class CompletionBehavior {

        @Test
        fun `completions should only include function names without namespace prefix`() {
            val functions = PhelFunctionRegistry.getFunctions(Namespace.TEST)

            functions.forEach { function ->
                val functionName = function.name.substringAfter("/")

                assertFalse(functionName.contains("/"), "Function name should not contain slash: $functionName")
                assertFalse(functionName.contains("\\"), "Function name should not contain backslash: $functionName")
            }
        }

        @Test
        fun `completions should have signature information`() {
            val functions = PhelFunctionRegistry.getFunctions(Namespace.TEST)

            functions.forEach { function ->
                assertNotNull(function.signature, "Function should have signature: ${function.name}")
                assertTrue(function.signature.isNotBlank(), "Signature should not be blank: ${function.name}")
            }
        }

        @Test
        fun `completions should have documentation summary`() {
            val functions = PhelFunctionRegistry.getFunctions(Namespace.TEST)

            functions.forEach { function ->
                assertNotNull(function.documentation.summary, "Function should have summary: ${function.name}")
                assertTrue(function.documentation.summary.isNotBlank(), "Summary should not be blank: ${function.name}")
            }
        }
    }

    @Nested
    inner class RealWorldScenarios {

        @Test
        fun `scenario - completing deftest from phel test`() {
            // User types: (ns my-app\test (:require phel\test :refer [|]))
            // Expected: deftest, is, testing, etc.

            val namespace = Namespace.TEST
            val functions = PhelFunctionRegistry.getFunctions(namespace)
            val functionNames = functions.map { it.name.substringAfter("/") }

            assertTrue(functionNames.contains("deftest"))
            assertTrue(functionNames.contains("is"))
        }

        @Test
        fun `scenario - completing str functions`() {
            // User types: (ns my-app\core (:require phel\str :refer [|]))
            // Expected: join, split, contains?, etc.

            val namespace = Namespace.STRING
            val functions = PhelFunctionRegistry.getFunctions(namespace)

            assertTrue(functions.isNotEmpty(), "String namespace should have functions")
        }
    }
}
