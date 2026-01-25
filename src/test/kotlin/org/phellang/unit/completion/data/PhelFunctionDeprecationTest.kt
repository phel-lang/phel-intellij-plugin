package org.phellang.unit.completion.data

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.data.PhelFunctionRegistry

class PhelFunctionDeprecationTest {

    @Nested
    inner class IsDeprecatedProperty {

        @Test
        fun `should return true for deprecated functions`() {
            // These are actual deprecated functions from the generated registry
            val deprecatedFunctions = listOf(
                "put",     // core/put - deprecated in favor of assoc
                "push",    // core/push - deprecated in favor of conj
                "put-in",  // core/put-in - deprecated in favor of assoc-in
                "unset",   // core/unset - deprecated in favor of dissoc
                "unset-in" // core/unset-in - deprecated in favor of dissoc-in
            )

            deprecatedFunctions.forEach { name ->
                assertTrue(
                    PhelFunctionRegistry.isDeprecated(name), "Function '$name' should be marked as deprecated"
                )
            }
        }

        @Test
        fun `should return false for non-deprecated functions`() {
            val nonDeprecatedFunctions = listOf("map", "filter", "reduce", "str/join", "json/encode", "html/html")

            nonDeprecatedFunctions.forEach { name ->
                assertFalse(
                    PhelFunctionRegistry.isDeprecated(name), "Function '$name' should not be marked as deprecated"
                )
            }
        }

        @Test
        fun `should handle qualified names`() {
            assertTrue(PhelFunctionRegistry.isDeprecated("core/put"))
            assertFalse(PhelFunctionRegistry.isDeprecated("core/map"))
        }

        @Test
        fun `should handle short names`() {
            assertTrue(PhelFunctionRegistry.isDeprecated("put"))
            assertFalse(PhelFunctionRegistry.isDeprecated("map"))
        }

        @Test
        fun `should return false for unknown functions`() {
            assertFalse(PhelFunctionRegistry.isDeprecated("unknown-function"))
            assertFalse(PhelFunctionRegistry.isDeprecated("non/existent"))
        }
    }

    @Nested
    inner class DeprecationMetadata {

        @Test
        fun `deprecated functions should have deprecation info`() {
            val putFunction = PhelFunctionRegistry.getFunction("put")
            assertTrue(putFunction != null, "Function 'put' should exist in registry")
            assertTrue(putFunction!!.isDeprecated, "Function 'put' should be deprecated")
            assertTrue(putFunction.documentation.deprecation != null, "Function 'put' should have deprecation info")
        }

        @Test
        fun `non-deprecated functions should not have deprecation info`() {
            val mapFunction = PhelFunctionRegistry.getFunction("map")
            assertTrue(mapFunction != null, "Function 'map' should exist in registry")
            assertFalse(mapFunction!!.isDeprecated, "Function 'map' should not be deprecated")
            assertTrue(mapFunction.documentation.deprecation == null, "Function 'map' should not have deprecation info")
        }

        @Test
        fun `deprecation info should include version`() {
            val putFunction = PhelFunctionRegistry.getFunction("put")
            val deprecationInfo = putFunction?.documentation?.deprecation
            assertTrue(deprecationInfo != null, "Deprecation info should exist")
            assertTrue(deprecationInfo!!.version.isNotBlank(), "Version should not be blank")
        }

        @Test
        fun `deprecation info should include replacement when available`() {
            val putFunction = PhelFunctionRegistry.getFunction("put")
            val deprecationInfo = putFunction?.documentation?.deprecation
            assertTrue(deprecationInfo?.replacement == "assoc", "Replacement should be 'assoc'")
        }

        @Test
        fun `deprecated functions should have DEPRECATED_FUNCTIONS priority`() {
            val putFunction = PhelFunctionRegistry.getFunction("put")
            assertTrue(putFunction?.completion?.priority?.name == "DEPRECATED_FUNCTIONS")
        }
    }

    @Nested
    inner class RegistryLookup {

        @Test
        fun `should find function by exact name match`() {
            val function = PhelFunctionRegistry.getFunction("put")
            assertTrue(function != null)
            assertTrue(function?.name == "put" || function?.name == "core/put")
        }

        @Test
        fun `should return null for non-existent function`() {
            val function = PhelFunctionRegistry.getFunction("non-existent-function")
            assertTrue(function == null)
        }
    }
}
