package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelInteropShorthands

class PhelInteropShorthandsTest {

    @Nested
    inner class InteropClassName {

        @Test
        fun `backslash-prefixed names are classified as PHP classes`() {
            assertTrue(PhelInteropShorthands.isInteropClassName("\\DateTime"))
            assertTrue(PhelInteropShorthands.isInteropClassName("\\Foo\\Bar"))
        }

        @Test
        fun `uppercase-starting names are classified as PHP classes`() {
            assertTrue(PhelInteropShorthands.isInteropClassName("DateTime"))
            assertTrue(PhelInteropShorthands.isInteropClassName("Exception"))
        }

        @Test
        fun `lowercase names are not classified as PHP classes`() {
            assertFalse(PhelInteropShorthands.isInteropClassName("phel"))
            assertFalse(PhelInteropShorthands.isInteropClassName("phel.string"))
            assertFalse(PhelInteropShorthands.isInteropClassName("my-app"))
            assertFalse(PhelInteropShorthands.isInteropClassName(""))
        }
    }

    @Nested
    inner class ConstructorShorthand {

        @Test
        fun `ClassName-dot is recognised`() {
            assertTrue(PhelInteropShorthands.isInteropShorthand("DateTimeImmutable."))
            assertTrue(PhelInteropShorthands.isInteropShorthand("Exception."))
        }

        @Test
        fun `lowercase-dot is not recognised unless in used-classes`() {
            assertFalse(PhelInteropShorthands.isInteropShorthand("foo."))
            assertTrue(PhelInteropShorthands.isInteropShorthand("foo.", setOf("foo")))
        }

        @Test
        fun `bare dot and ellipsis are not constructors`() {
            assertFalse(PhelInteropShorthands.isInteropShorthand("."))
            assertFalse(PhelInteropShorthands.isInteropShorthand(".."))
        }
    }

    @Nested
    inner class MethodAndFieldShorthand {

        @Test
        fun `dot-method is recognised`() {
            assertTrue(PhelInteropShorthands.isInteropShorthand(".format"))
            assertTrue(PhelInteropShorthands.isInteropShorthand(".getMessage"))
        }

        @Test
        fun `dot-dash field is recognised`() {
            assertTrue(PhelInteropShorthands.isInteropShorthand(".-s"))
            assertTrue(PhelInteropShorthands.isInteropShorthand(".-property"))
        }

        @Test
        fun `bare dot-dash is not a field access`() {
            assertFalse(PhelInteropShorthands.isInteropShorthand(".-"))
        }
    }

    @Nested
    inner class StaticCallShorthand {

        @Test
        fun `uppercase-qualified static call is recognised`() {
            assertTrue(PhelInteropShorthands.isInteropShorthand("DateTime/createFromFormat"))
            assertTrue(PhelInteropShorthands.isInteropShorthand("DateTimeImmutable/ATOM"))
        }

        @Test
        fun `backslash-qualified static call is recognised`() {
            assertTrue(PhelInteropShorthands.isInteropShorthand("\\Foo\\Bar/baz"))
        }

        @Test
        fun `lowercase qualifier matches when in used-classes`() {
            assertTrue(PhelInteropShorthands.isInteropShorthand("date/today", setOf("date")))
        }

        @Test
        fun `lowercase qualifier outside used-classes is not interop`() {
            assertFalse(PhelInteropShorthands.isInteropShorthand("phel.string/trim"))
            assertFalse(PhelInteropShorthands.isInteropShorthand("str/join"))
        }
    }

    @Nested
    inner class BareReferences {

        @Test
        fun `bare new keyword is interop`() {
            assertTrue(PhelInteropShorthands.isInteropShorthand("new"))
        }

        @Test
        fun `backslash-prefixed class reference is interop`() {
            assertTrue(PhelInteropShorthands.isInteropShorthand("\\DateTime"))
            assertTrue(PhelInteropShorthands.isInteropShorthand("\\DivisionByZeroError"))
        }

        @Test
        fun `regular Phel symbols are not interop`() {
            assertFalse(PhelInteropShorthands.isInteropShorthand("map"))
            assertFalse(PhelInteropShorthands.isInteropShorthand("my-function"))
            assertFalse(PhelInteropShorthands.isInteropShorthand("test?"))
            assertFalse(PhelInteropShorthands.isInteropShorthand("&"))
        }

        @Test
        fun `php-qualified names are not classified here`() {
            // `php/new`, `php/->`, etc. are handled separately in the highlighter.
            // The shorthand detector deliberately ignores them — they're not shorthand forms.
            assertFalse(PhelInteropShorthands.isInteropShorthand("php/new"))
            assertFalse(PhelInteropShorthands.isInteropShorthand("php/->"))
        }
    }
}
