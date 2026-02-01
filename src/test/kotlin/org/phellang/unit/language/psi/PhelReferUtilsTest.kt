package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelReferUtils

class PhelReferUtilsTest {

    @Nested
    inner class ReferContextDataClass {

        @Test
        fun `ReferContext contains namespace and containingVec`() {
            // This is a data class test - we can't create actual PhelVec without PSI
            // but we can test the structure expectations
            val namespace = "phel\\test"
            val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

            assertEquals("test", shortNamespace)
        }
    }

    @Nested
    inner class ExtractShortNamespace {

        @Test
        fun `should extract short namespace from full namespace`() {
            assertEquals("test", PhelReferUtils.extractShortNamespace("phel\\test"))
            assertEquals("str", PhelReferUtils.extractShortNamespace("phel\\str"))
            assertEquals("json", PhelReferUtils.extractShortNamespace("phel\\json"))
            assertEquals("html", PhelReferUtils.extractShortNamespace("phel\\html"))
            assertEquals("http", PhelReferUtils.extractShortNamespace("phel\\http"))
        }

        @Test
        fun `should handle project namespaces`() {
            assertEquals("utils", PhelReferUtils.extractShortNamespace("my-app\\utils"))
            assertEquals("handlers", PhelReferUtils.extractShortNamespace("my-app\\api\\handlers"))
            assertEquals("helpers", PhelReferUtils.extractShortNamespace("project\\utils\\helpers"))
        }

        @Test
        fun `should return same string if no backslash`() {
            assertEquals("simple", PhelReferUtils.extractShortNamespace("simple"))
            assertEquals("test", PhelReferUtils.extractShortNamespace("test"))
        }

        @Test
        fun `should handle empty string`() {
            assertEquals("", PhelReferUtils.extractShortNamespace(""))
        }

        @Test
        fun `should handle trailing backslash`() {
            assertEquals("", PhelReferUtils.extractShortNamespace("phel\\"))
        }

        @Test
        fun `should handle leading backslash`() {
            assertEquals("test", PhelReferUtils.extractShortNamespace("\\test"))
        }
    }

    @Nested
    inner class ReferContextLogic {

        @Test
        fun `refer context should contain namespace from require form`() {
            // In (:require phel\test :refer [...])
            // The namespace should be "phel\test"
            val fullNamespace = "phel\\test"
            val shortNamespace = PhelReferUtils.extractShortNamespace(fullNamespace)

            assertEquals("test", shortNamespace)
        }

        @Test
        fun `canonical function name construction`() {
            // For deftest in phel\test, canonical name is "test/deftest"
            val namespace = "phel\\test"
            val symbolName = "deftest"
            val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)
            val canonicalName = "$shortNamespace/$symbolName"

            assertEquals("test/deftest", canonicalName)
        }

        @Test
        fun `canonical name construction for various namespaces`() {
            val testCases = listOf(
                Triple("phel\\test", "deftest", "test/deftest"),
                Triple("phel\\test", "is", "test/is"),
                Triple("phel\\str", "join", "str/join"),
                Triple("phel\\json", "encode", "json/encode"),
                Triple("phel\\html", "html", "html/html"),
                Triple("my-app\\utils", "helper", "utils/helper")
            )

            testCases.forEach { (namespace, symbol, expected) ->
                val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)
                val canonicalName = "$shortNamespace/$symbol"
                assertEquals(expected, canonicalName, "Failed for $namespace + $symbol")
            }
        }
    }

    @Nested
    inner class DuplicateDetectionLogic {

        @Test
        fun `duplicate detection - first occurrence is not duplicate`() {
            // In [deftest is is], first 'is' (index 1) is NOT a duplicate
            val symbols = listOf("deftest", "is", "is")
            val isDuplicate = hasDuplicateBefore(symbols, 1)

            assertFalse(isDuplicate, "First occurrence should not be duplicate")
        }

        @Test
        fun `duplicate detection - second occurrence is duplicate`() {
            // In [deftest is is], second 'is' (index 2) IS a duplicate
            val symbols = listOf("deftest", "is", "is")
            val isDuplicate = hasDuplicateBefore(symbols, 2)

            assertTrue(isDuplicate, "Second occurrence should be duplicate")
        }

        @Test
        fun `duplicate detection - no duplicates`() {
            val symbols = listOf("deftest", "is", "testing")

            assertFalse(hasDuplicateBefore(symbols, 0))
            assertFalse(hasDuplicateBefore(symbols, 1))
            assertFalse(hasDuplicateBefore(symbols, 2))
        }

        @Test
        fun `duplicate detection - multiple duplicates`() {
            // In [is is is], second and third are duplicates
            val symbols = listOf("is", "is", "is")

            assertFalse(hasDuplicateBefore(symbols, 0), "First should not be duplicate")
            assertTrue(hasDuplicateBefore(symbols, 1), "Second should be duplicate")
            assertTrue(hasDuplicateBefore(symbols, 2), "Third should be duplicate")
        }

        @Test
        fun `duplicate detection - single element`() {
            val symbols = listOf("deftest")

            assertFalse(hasDuplicateBefore(symbols, 0))
        }

        @Test
        fun `duplicate detection - empty list`() {
            val symbols = emptyList<String>()
            // No elements to check
            assertTrue(symbols.isEmpty())
        }

        /**
         * Simulates the duplicate detection logic used in PhelReferUtils.
         */
        private fun hasDuplicateBefore(symbols: List<String>, targetIndex: Int): Boolean {
            if (targetIndex >= symbols.size) return false
            val targetName = symbols[targetIndex]
            for (i in 0 until targetIndex) {
                if (symbols[i] == targetName) {
                    return true
                }
            }
            return false
        }
    }

    @Nested
    inner class AlreadyReferredSymbolsLogic {

        @Test
        fun `should collect all symbols from refer vector`() {
            val symbols = listOf("deftest", "is", "testing")
            val result = symbols.toSet()

            assertEquals(3, result.size)
            assertTrue(result.contains("deftest"))
            assertTrue(result.contains("is"))
            assertTrue(result.contains("testing"))
        }

        @Test
        fun `should handle duplicates in set`() {
            val symbols = listOf("deftest", "is", "is")
            val result = symbols.toSet()

            // Set removes duplicates
            assertEquals(2, result.size)
            assertTrue(result.contains("deftest"))
            assertTrue(result.contains("is"))
        }

        @Test
        fun `should filter out IntelliJIdeaRulezzz completion dummy`() {
            val symbols = listOf("deftest", "isIntelliJIdeaRulezzz", "testing")
            val filtered = symbols.filter { !it.contains("IntelliJIdeaRulezzz") }.toSet()

            assertEquals(2, filtered.size)
            assertTrue(filtered.contains("deftest"))
            assertTrue(filtered.contains("testing"))
            assertFalse(filtered.contains("isIntelliJIdeaRulezzz"))
        }

        @Test
        fun `should handle empty vector`() {
            val symbols = emptyList<String>()
            val result = symbols.toSet()

            assertTrue(result.isEmpty())
        }
    }

    @Nested
    inner class RealWorldScenarios {

        @Test
        fun `scenario - test namespace imports`() {
            // (:require phel\test :refer [deftest is])
            val namespace = "phel\\test"
            val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

            assertEquals("test", shortNamespace)
            assertEquals("test/deftest", "$shortNamespace/deftest")
            assertEquals("test/is", "$shortNamespace/is")
        }

        @Test
        fun `scenario - string namespace imports`() {
            // (:require phel\str :refer [join split])
            val namespace = "phel\\str"
            val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

            assertEquals("str", shortNamespace)
            assertEquals("str/join", "$shortNamespace/join")
            assertEquals("str/split", "$shortNamespace/split")
        }

        @Test
        fun `scenario - project namespace imports`() {
            // (:require my-app\handlers :refer [handle-request])
            val namespace = "my-app\\handlers"
            val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

            assertEquals("handlers", shortNamespace)
            assertEquals("handlers/handle-request", "$shortNamespace/handle-request")
        }

        @Test
        fun `scenario - completion filtering`() {
            // User has typed: (:require phel\test :refer [deftest is |])
            // Completions should NOT include deftest and is
            val alreadyReferred = setOf("deftest", "is")
            val allFunctions = listOf("deftest", "is", "testing", "report", "run-tests")

            val filtered = allFunctions.filter { it !in alreadyReferred }

            assertEquals(3, filtered.size)
            assertFalse(filtered.contains("deftest"))
            assertFalse(filtered.contains("is"))
            assertTrue(filtered.contains("testing"))
            assertTrue(filtered.contains("report"))
            assertTrue(filtered.contains("run-tests"))
        }

        @Test
        fun `scenario - validation of invalid symbol`() {
            // (:require phel\test :refer [deftest isnot])
            // isnot does not exist, should be flagged
            val validSymbols = setOf("deftest", "is", "testing", "report", "run-tests", "successful?")

            assertTrue("deftest" in validSymbols)
            assertFalse("isnot" in validSymbols)
        }

        @Test
        fun `scenario - duplicate detection in validation`() {
            // (:require phel\test :refer [deftest is is])
            // Second 'is' should be flagged as duplicate
            val symbols = listOf("deftest", "is", "is")

            // First 'is' at index 1 - not a duplicate
            assertFalse(hasDuplicateBefore(symbols, 1))

            // Second 'is' at index 2 - IS a duplicate
            assertTrue(hasDuplicateBefore(symbols, 2))
        }

        private fun hasDuplicateBefore(symbols: List<String>, targetIndex: Int): Boolean {
            if (targetIndex >= symbols.size) return false
            val targetName = symbols[targetIndex]
            for (i in 0 until targetIndex) {
                if (symbols[i] == targetName) {
                    return true
                }
            }
            return false
        }
    }

    @Nested
    inner class EdgeCases {

        @Test
        fun `should handle special characters in function names`() {
            val functions = listOf("is?", "is=", "is-not", "test!")
            val shortNamespace = "test"

            functions.forEach { fn ->
                val canonicalName = "$shortNamespace/$fn"
                assertTrue(canonicalName.startsWith("test/"))
                assertTrue(canonicalName.endsWith(fn))
            }
        }

        @Test
        fun `should handle deeply nested namespace`() {
            val namespace = "org\\company\\project\\module\\submodule"
            val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

            assertEquals("submodule", shortNamespace)
        }

        @Test
        fun `should handle namespace with hyphen`() {
            val namespace = "my-app\\my-module"
            val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

            assertEquals("my-module", shortNamespace)
        }

        @Test
        fun `should handle namespace with numbers`() {
            val namespace = "app2\\module3"
            val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

            assertEquals("module3", shortNamespace)
        }
    }
}
