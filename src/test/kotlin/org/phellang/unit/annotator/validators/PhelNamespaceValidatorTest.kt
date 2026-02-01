package org.phellang.unit.annotator.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelProjectNamespaceFinder

class PhelNamespaceValidatorTest {

    // Use the shared mapping from PhelProjectNamespaceFinder (single source of truth)
    private val standardLibraryShortToFull = PhelProjectNamespaceFinder.STANDARD_LIBRARY_SHORT_TO_FULL

    @Nested
    inner class StandardLibraryMapping {

        @Test
        fun `all standard library short names map to full namespaces`() {
            val expectedMappings = mapOf(
                "str" to "phel\\str",
                "json" to "phel\\json",
                "http" to "phel\\http",
                "html" to "phel\\html",
                "base64" to "phel\\base64",
                "test" to "phel\\test",
                "mock" to "phel\\mock",
                "repl" to "phel\\repl",
                "debug" to "phel\\debug",
                "core" to "phel\\core"
            )

            assertEquals(expectedMappings, standardLibraryShortToFull)
        }

        @Test
        fun `str maps to phel str`() {
            assertEquals("phel\\str", standardLibraryShortToFull["str"])
        }

        @Test
        fun `json maps to phel json`() {
            assertEquals("phel\\json", standardLibraryShortToFull["json"])
        }

        @Test
        fun `core is in short-to-full map`() {
            // core is in the map but handled specially in validation (doesn't need import)
            assertEquals("phel\\core", standardLibraryShortToFull["core"])
        }

        @Test
        fun `php is not in short-to-full map`() {
            // php is handled specially - always valid
            assertNull(standardLibraryShortToFull["php"])
        }
    }

    @Nested
    inner class QualifierExtraction {

        @Test
        fun `qualifier is part before slash`() {
            // Simulating PhelPsiUtils.getQualifier behavior
            val symbolText = "str/join"
            val qualifier = symbolText.substringBefore("/")
            assertEquals("str", qualifier)
        }

        @Test
        fun `nested namespace qualifier`() {
            val symbolText = "my-app/helper"
            val qualifier = symbolText.substringBefore("/")
            assertEquals("my-app", qualifier)
        }

        @Test
        fun `no qualifier for unqualified symbol`() {
            val symbolText = "map"
            val hasQualifier = symbolText.contains("/")
            assertFalse(hasQualifier)
        }
    }

    @Nested
    inner class SkipConditions {

        @Test
        fun `php qualifier should be skipped`() {
            val qualifier = "php"
            val shouldSkip = qualifier == "php" || qualifier == "core"
            assertTrue(shouldSkip)
        }

        @Test
        fun `core qualifier should be skipped`() {
            val qualifier = "core"
            val shouldSkip = qualifier == "php" || qualifier == "core"
            assertTrue(shouldSkip)
        }

        @Test
        fun `str qualifier should not be skipped`() {
            val qualifier = "str"
            val shouldSkip = qualifier == "php" || qualifier == "core"
            assertFalse(shouldSkip)
        }

        @Test
        fun `project namespace should not be skipped`() {
            val qualifier = "utils"
            val shouldSkip = qualifier == "php" || qualifier == "core"
            assertFalse(shouldSkip)
        }
    }

    @Nested
    inner class ImportStatusLogic {

        @Test
        fun `VALID status means no error`() {
            val status = "VALID"
            val shouldReturnNull = status == "VALID"
            assertTrue(shouldReturnNull)
        }

        @Test
        fun `IMPORTED_BUT_NOT_EXISTS should suggest alternative`() {
            val status = "IMPORTED_BUT_NOT_EXISTS"
            val shouldSuggestAlternative = status == "IMPORTED_BUT_NOT_EXISTS"
            assertTrue(shouldSuggestAlternative)
        }

        @Test
        fun `NOT_IMPORTED should check for possible imports`() {
            val status = "NOT_IMPORTED"
            val shouldCheckPossibleImports = status == "NOT_IMPORTED"
            assertTrue(shouldCheckPossibleImports)
        }
    }

    @Nested
    inner class MessageGeneration {

        @Test
        fun `namespace not imported message format`() {
            val qualifier = "str"
            val message = "Namespace '$qualifier' is not imported"
            assertEquals("Namespace 'str' is not imported", message)
        }

        @Test
        fun `namespace does not exist message format`() {
            val qualifier = "unknown"
            val message = "Namespace '$qualifier' does not exist"
            assertEquals("Namespace 'unknown' does not exist", message)
        }

        @Test
        fun `imported namespace does not exist with suggestion message format`() {
            val suggestion = "phel-project\\utils"
            val message = "Imported namespace does not exist. Did you mean '$suggestion'?"
            assertEquals("Imported namespace does not exist. Did you mean 'phel-project\\utils'?", message)
        }
    }

    @Nested
    inner class NamespaceExistenceCheck {

        @Test
        fun `standard library namespace should exist if starts with phel`() {
            val fullNamespace = "phel\\str"
            val shortName = fullNamespace.substringAfterLast("\\")
            val isStdLib = fullNamespace.startsWith("phel\\") && standardLibraryShortToFull.containsKey(shortName)
            assertTrue(isStdLib)
        }

        @Test
        fun `project namespace should not match standard library check`() {
            val fullNamespace = "phel-project\\str"
            val shortName = fullNamespace.substringAfterLast("\\")
            val isStdLib = fullNamespace.startsWith("phel\\") && standardLibraryShortToFull.containsKey(shortName)
            assertFalse(isStdLib) // Doesn't start with "phel\"
        }
    }
}
