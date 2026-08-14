package org.phellang.unit.annotator.highlighters.rules

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.phellang.annotator.highlighters.rules.ConstructorClassArgumentRule
import org.phellang.annotator.highlighters.rules.DeprecatedSymbolRule
import org.phellang.annotator.highlighters.rules.KnownFormRule
import org.phellang.annotator.highlighters.rules.NamespacePrefixRule
import org.phellang.annotator.highlighters.rules.PhelHighlightDecision
import org.phellang.annotator.highlighters.rules.PhelSymbolContext
import org.phellang.annotator.highlighters.rules.PhpQualifiedRule
import org.phellang.annotator.highlighters.rules.QualifiedSymbolRule
import org.phellang.annotator.highlighters.rules.RegularSymbolRule
import org.phellang.annotator.highlighters.rules.VariadicMarkerRule
import org.phellang.core.highlighting.PhelAnnotationConstants.DEPRECATED_SYMBOL
import org.phellang.core.highlighting.PhelAnnotationConstants.FUNCTION_CALL
import org.phellang.core.highlighting.PhelAnnotationConstants.NAMESPACE_SYMBOL
import org.phellang.core.highlighting.PhelAnnotationConstants.PHP_INTEROP
import org.phellang.core.highlighting.PhelAnnotationConstants.REGULAR_SYMBOL
import org.phellang.fixtures.PhelDeprecatedFunctionFixtures
import org.phellang.language.psi.PhelSymbol
import org.phellang.registry.PhelFunctionRegistry

/**
 * Covers the rules whose decision is driven by the symbol's text alone, which is every rule that
 * can decline without consulting PSI. The PSI-dependent branches (parameter vectors, call position,
 * constructor arguments) are exercised end-to-end by the annotator integration tests.
 */
class PhelHighlightRuleTest {

    private fun contextFor(text: String): PhelSymbolContext {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn(text)
        return PhelSymbolContext(symbol, text)
    }

    private fun assertPaints(decision: PhelHighlightDecision?, expected: Any) {
        val paint = assertInstanceOf(PhelHighlightDecision.Paint::class.java, decision)
        assertEquals(expected, paint.attributes)
    }

    @Nested
    inner class PhpQualified {

        @ParameterizedTest
        @ValueSource(strings = ["php/aget", "php/->", "php/::", "php/new", "php/+"])
        fun `paints explicitly qualified php interop`(text: String) {
            assertPaints(PhpQualifiedRule.decide(contextFor(text)), PHP_INTEROP)
        }

        @ParameterizedTest
        @ValueSource(strings = ["php", "phpx/foo", "str/split", "map", "", "/php"])
        fun `declines anything not prefixed with php slash`(text: String) {
            assertNull(PhpQualifiedRule.decide(contextFor(text)))
        }
    }

    @Nested
    inner class KnownForms {

        @ParameterizedTest
        @ValueSource(strings = ["def", "defn", "fn", "if", "let", "do", "quote", "try", "throw"])
        fun `paints built-in forms as calls`(text: String) {
            assertPaints(KnownFormRule.decide(contextFor(text)), FUNCTION_CALL)
        }

        @ParameterizedTest
        @ValueSource(strings = ["my-own-function", "some-local", "totally-unknown-name", ""])
        fun `declines names the language layer does not recognise`(text: String) {
            assertNull(KnownFormRule.decide(contextFor(text)))
        }
    }

    @Nested
    inner class Deprecation {

        // The rule asks the registry, which has held no deprecated functions since Phel v0.50.0
        // dropped them all, so the names below are supplied as fixtures.
        @BeforeEach
        fun installFixtures() {
            PhelFunctionRegistry.installTestFunctions(PhelDeprecatedFunctionFixtures.ALL)
        }

        @AfterEach
        fun clearFixtures() {
            PhelFunctionRegistry.clearTestFunctions()
        }

        /** `function?` and `push` were deprecated core functions up to Phel v0.49.0. */
        @ParameterizedTest
        @ValueSource(strings = ["function?", "push"])
        fun `paints deprecated core functions`(text: String) {
            assertPaints(DeprecatedSymbolRule.decide(contextFor(text)), DEPRECATED_SYMBOL)
        }

        @Test
        fun `paints a deprecated function reached through its namespace`() {
            assertPaints(DeprecatedSymbolRule.decide(contextFor("core/push")), DEPRECATED_SYMBOL)
        }

        @ParameterizedTest
        @ValueSource(strings = ["map", "conj", "assoc", "not-a-function-at-all"])
        fun `declines functions that are current`(text: String) {
            assertNull(DeprecatedSymbolRule.decide(contextFor(text)))
        }
    }

    @Nested
    inner class QualifiedSymbols {

        @ParameterizedTest
        @ValueSource(strings = ["/", "/foo", "foo/", "no-slash-here", ""])
        fun `declines a bare or malformed slash rather than treating it as a qualifier`(text: String) {
            assertNull(QualifiedSymbolRule.decide(contextFor(text)))
        }
    }

    @Nested
    inner class NamespacePrefixes {

        @ParameterizedTest
        @ValueSource(strings = ["my-ns\\func", "core\\map", "a\\b\\c"])
        fun `paints backslash-separated namespace prefixes`(text: String) {
            assertPaints(NamespacePrefixRule.decide(contextFor(text)), NAMESPACE_SYMBOL)
        }

        @ParameterizedTest
        @ValueSource(strings = ["\\leading", "trailing\\", "plain", ""])
        fun `declines malformed prefixes`(text: String) {
            assertNull(NamespacePrefixRule.decide(contextFor(text)))
        }
    }

    @Nested
    inner class TextOnlyDeclines {

        @ParameterizedTest
        @ValueSource(strings = ["&&", "and", "x", ""])
        fun `variadic marker declines any text that is not an ampersand`(text: String) {
            assertNull(VariadicMarkerRule.decide(contextFor(text)))
        }

        @ParameterizedTest
        @ValueSource(strings = ["lowercase-name", "my-macro", "", "123"])
        fun `constructor argument rule declines text that cannot be a php class`(text: String) {
            assertNull(ConstructorClassArgumentRule.decide(contextFor(text)))
        }
    }

    @Nested
    inner class Fallback {

        @ParameterizedTest
        @ValueSource(strings = ["anything", "x", "some-symbol", "?!"])
        fun `the terminal rule always decides, so the chain is total`(text: String) {
            assertPaints(RegularSymbolRule.decide(contextFor(text)), REGULAR_SYMBOL)
        }
    }
}
