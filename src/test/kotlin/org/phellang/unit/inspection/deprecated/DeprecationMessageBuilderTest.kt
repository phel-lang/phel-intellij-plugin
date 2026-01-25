package org.phellang.unit.inspection.deprecated

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.data.DeprecationInfo
import org.phellang.inspection.deprecated.DeprecationMessageBuilder

class DeprecationMessageBuilderTest {

    @Nested
    inner class Build {

        @Test
        fun `should build message for null deprecation`() {
            val message = DeprecationMessageBuilder.build("old-func", null)

            assertEquals("'old-func' is deprecated", message)
        }

        @Test
        fun `should build message with Use prefix in version`() {
            val deprecation = DeprecationInfo(version = "Use phel\\str\\contains?")

            val message = DeprecationMessageBuilder.build("str-contains?", deprecation)

            assertEquals("'str-contains?' is deprecated. Use 'str/contains?' instead", message)
        }

        @Test
        fun `should build message with version and replacement`() {
            val deprecation = DeprecationInfo(version = "0.25.0", replacement = "assoc")

            val message = DeprecationMessageBuilder.build("put", deprecation)

            assertEquals("'put' is deprecated since 0.25.0. Use 'assoc' instead", message)
        }

        @Test
        fun `should build message with version only`() {
            val deprecation = DeprecationInfo(version = "1.0.0")

            val message = DeprecationMessageBuilder.build("old-func", deprecation)

            assertEquals("'old-func' is deprecated since 1.0.0", message)
        }

        @Test
        fun `should format namespace replacement in message`() {
            val deprecation = DeprecationInfo(version = "0.25.0", replacement = "phel\\json\\encode")

            val message = DeprecationMessageBuilder.build("json-encode", deprecation)

            assertEquals("'json-encode' is deprecated since 0.25.0. Use 'json/encode' instead", message)
        }

        @Test
        fun `should handle Use prefix with extra whitespace`() {
            val deprecation = DeprecationInfo(version = "Use   assoc  ")

            val message = DeprecationMessageBuilder.build("put", deprecation)

            assertEquals("'put' is deprecated. Use 'assoc' instead", message)
        }
    }

    @Nested
    inner class ExtractReplacement {

        @Test
        fun `should return null for null deprecation`() {
            val replacement = DeprecationMessageBuilder.extractReplacement(null)

            assertNull(replacement)
        }

        @Test
        fun `should extract from Use prefix`() {
            val deprecation = DeprecationInfo(version = "Use phel\\str\\contains?")

            val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)

            assertEquals("phel\\str\\contains?", replacement)
        }

        @Test
        fun `should extract from replacement field`() {
            val deprecation = DeprecationInfo(version = "0.25.0", replacement = "assoc")

            val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)

            assertEquals("assoc", replacement)
        }

        @Test
        fun `should return null when no replacement available`() {
            val deprecation = DeprecationInfo(version = "1.0.0")

            val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)

            assertNull(replacement)
        }

        @Test
        fun `should prefer Use prefix over replacement field`() {
            // If version starts with "Use", that takes precedence
            val deprecation = DeprecationInfo(version = "Use new-func", replacement = "other-func")

            val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)

            assertEquals("new-func", replacement)
        }

        @Test
        fun `should trim whitespace from Use extraction`() {
            val deprecation = DeprecationInfo(version = "Use   phel\\str\\join  ")

            val replacement = DeprecationMessageBuilder.extractReplacement(deprecation)

            assertEquals("phel\\str\\join", replacement)
        }
    }
}
