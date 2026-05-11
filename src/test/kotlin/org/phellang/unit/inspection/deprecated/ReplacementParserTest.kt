package org.phellang.unit.inspection.deprecated

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.inspection.deprecated.ReplacementParser

class ReplacementParserTest {

    @Nested
    inner class Parse {

        @Test
        fun `should parse phel namespace format`() {
            val result = ReplacementParser.parse("phel\\string\\contains?")

            assertEquals("string", result.namespace)
            assertEquals("string/contains?", result.functionName)
        }

        @Test
        fun `should parse multi-level namespace`() {
            val result = ReplacementParser.parse("phel\\json\\encode")

            assertEquals("json", result.namespace)
            assertEquals("json/encode", result.functionName)
        }

        @Test
        fun `should handle simple replacement without namespace`() {
            val result = ReplacementParser.parse("assoc")

            assertNull(result.namespace)
            assertEquals("assoc", result.functionName)
        }

        @Test
        fun `should handle core function replacement`() {
            val result = ReplacementParser.parse("map")

            assertNull(result.namespace)
            assertEquals("map", result.functionName)
        }

        @Test
        fun `should handle replacement with special characters`() {
            val result = ReplacementParser.parse("phel\\string\\starts-with?")

            assertEquals("string", result.namespace)
            assertEquals("string/starts-with?", result.functionName)
        }

        @Test
        fun `should handle non-phel namespace prefix`() {
            // If it starts with something other than "phel", treat as simple
            val result = ReplacementParser.parse("custom\\something")

            assertNull(result.namespace)
            assertEquals("custom\\something", result.functionName)
        }

        @Test
        fun `should parse dot-separated phel namespace`() {
            // Phel 0.35+ canonical form uses dots throughout.
            val result = ReplacementParser.parse("phel.string.contains?")

            assertEquals("string", result.namespace)
            assertEquals("string/contains?", result.functionName)
        }

        @Test
        fun `should parse dot-namespace with slash function`() {
            // Mixed form: dot-separated namespace + slash before function name.
            val result = ReplacementParser.parse("phel.string/contains?")

            assertEquals("string", result.namespace)
            assertEquals("string/contains?", result.functionName)
        }

        @Test
        fun `should parse short ns slash fn form`() {
            // Already in display shape — short-namespace + function.
            val result = ReplacementParser.parse("string/contains?")

            assertEquals("string", result.namespace)
            assertEquals("string/contains?", result.functionName)
        }

        @Test
        fun `should handle phel prefix with only two parts`() {
            // "phel\something" without function name
            val result = ReplacementParser.parse("phel\\string")

            assertNull(result.namespace)
            assertEquals("phel\\string", result.functionName)
        }
    }

    @Nested
    inner class FormatForDisplay {

        @Test
        fun `should format phel namespace to slash notation`() {
            val result = ReplacementParser.formatForDisplay("phel\\string\\contains?")

            assertEquals("string/contains?", result)
        }

        @Test
        fun `should keep simple replacement as-is`() {
            val result = ReplacementParser.formatForDisplay("assoc")

            assertEquals("assoc", result)
        }

        @Test
        fun `should format json namespace correctly`() {
            val result = ReplacementParser.formatForDisplay("phel\\json\\encode")

            assertEquals("json/encode", result)
        }

        @Test
        fun `should handle http namespace`() {
            val result = ReplacementParser.formatForDisplay("phel\\http\\request")

            assertEquals("http/request", result)
        }

        @Test
        fun `should handle special characters in function name`() {
            val result = ReplacementParser.formatForDisplay("phel\\string\\blank?")

            assertEquals("string/blank?", result)
        }

        @Test
        fun `should not format non-phel prefix`() {
            val result = ReplacementParser.formatForDisplay("other\\namespace\\func")

            assertEquals("other\\namespace\\func", result)
        }
    }

    @Nested
    inner class ParsedReplacementDataClass {

        @Test
        fun `should support equality`() {
            val r1 = ReplacementParser.ParsedReplacement("string", "string/join")
            val r2 = ReplacementParser.ParsedReplacement("string", "string/join")

            assertEquals(r1, r2)
        }

        @Test
        fun `should support destructuring`() {
            val (namespace, functionName) = ReplacementParser.parse("phel\\string\\join")

            assertEquals("string", namespace)
            assertEquals("string/join", functionName)
        }
    }
}
