package org.phellang.unit.inspection.deprecated

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.inspection.deprecated.PhelDeprecatedFunctionQuickFix

class PhelDeprecatedFunctionQuickFixTest {

    @Nested
    inner class FamilyName {

        @Test
        fun `should return consistent family name`() {
            val quickFix = PhelDeprecatedFunctionQuickFix("assoc")

            assertEquals("Replace with suggested alternative", quickFix.familyName)
        }

        @Test
        fun `family name should be same for different replacements`() {
            val quickFix1 = PhelDeprecatedFunctionQuickFix("assoc")
            val quickFix2 = PhelDeprecatedFunctionQuickFix("phel\\str\\contains?")

            assertEquals(quickFix1.familyName, quickFix2.familyName)
        }
    }

    @Nested
    inner class Name {

        @Test
        fun `should format simple replacement`() {
            val quickFix = PhelDeprecatedFunctionQuickFix("assoc")

            assertEquals("Replace with 'assoc'", quickFix.name)
        }

        @Test
        fun `should format namespace replacement`() {
            val quickFix = PhelDeprecatedFunctionQuickFix("phel\\str\\contains?")

            assertEquals("Replace with 'str/contains?'", quickFix.name)
        }

        @Test
        fun `should format json namespace replacement`() {
            val quickFix = PhelDeprecatedFunctionQuickFix("phel\\json\\encode")

            assertEquals("Replace with 'json/encode'", quickFix.name)
        }

        @Test
        fun `should handle http namespace`() {
            val quickFix = PhelDeprecatedFunctionQuickFix("phel\\http\\request")

            assertEquals("Replace with 'http/request'", quickFix.name)
        }
    }
}
