package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.registry.PhelArity
import org.phellang.registry.PhelFunctionRegistry
import org.phellang.registry.selectFor

class PhelSpecialFormsTest {

    @Nested
    inner class VariadicHeads {

        /**
         * Regression: `(use \DateTimeImmutable :as Date)` rendered `ClassName:` / `options:`
         * parameter hints.
         *
         * `use` is a real registry entry, so `PhelArityResolver` resolves it and the hint provider
         * happily labelled its arguments positionally — but `use` is a compile-time namespace form,
         * not a call, so the labels were wrong. The arity inspection already skipped `use`; the hint
         * provider kept a separate copy of the skip set that had drifted and did not.
         *
         * This test pins both halves of the bug: the registry really does resolve `use` to a
         * positional signature (so the skip is what prevents the hints), and `use` really is in the
         * one shared set both consumers now read.
         */
        @Test
        fun `use is skipped even though the registry resolves it to a positional signature`() {
            val useFunction = PhelFunctionRegistry.getFunction("use")
            assertNotNull(useFunction, "`use` is expected to be a registry entry; the bug depends on it")

            val arities = PhelArity.parseAll(useFunction!!.signature)
            val arity = arities.selectFor(2)
            assertNotNull(arity, "`(use ClassName & options)` should resolve for a 2-argument call")

            // Without the skip this is exactly what the hint provider would have rendered.
            assertTrue(
                arity!!.params.isNotEmpty(),
                "`use` resolves to named parameters (${arity.params}), which is why it must be skipped",
            )

            assertTrue(
                "use" in PhelSpecialForms.VARIADIC_HEADS,
                "`use` must be skipped, or parameter hints reappear inside (use \\DateTimeImmutable :as Date)",
            )
        }

        /**
         * The six heads the parameter-hint provider's copy of this set was missing before the two
         * copies were merged. Losing any of them silently re-opens the drift.
         */
        @Test
        fun `covers the heads the parameter-hint copy had drifted away from`() {
            val previouslyMissing = listOf(".", "..", "import", "require", "set!", "use")

            previouslyMissing.forEach { head ->
                assertTrue(
                    head in PhelSpecialForms.VARIADIC_HEADS,
                    "'$head' must stay in the shared set; the hint provider's old copy lacked it",
                )
            }
        }

        @Test
        fun `covers special forms, binding forms, threading macros and test macros`() {
            val heads = listOf(
                "if", "when", "do", "quote", "var", "ns", "throw",   // special forms
                "let", "loop", "binding", "for", "foreach", "dofor", // binding forms
                "fn", "defn", "defn-", "def", "defmacro",            // definition forms
                "->", "->>", "as->", "some->", "doto",               // threading macros
                "deftest", "is", "are", "testing",                   // test macros
            )

            heads.forEach { head ->
                assertTrue(head in PhelSpecialForms.VARIADIC_HEADS, "'$head' should be a variadic head")
            }
        }

        /** Ordinary functions must stay out, or every real call loses its hints and arity check. */
        @Test
        fun `does not swallow ordinary functions`() {
            val ordinary = listOf("map", "filter", "reduce", "str/join", "json/encode", "print")

            ordinary.forEach { name ->
                assertFalse(
                    name in PhelSpecialForms.VARIADIC_HEADS,
                    "'$name' is an ordinary function and must still be hinted and arity-checked",
                )
            }
        }
    }

    @Nested
    inner class LetLike {

        @Test
        fun `contains the forms whose second element is a binding vector`() {
            val expected = setOf("let", "if-let", "when-let", "loop", "for", "foreach", "binding", "dofor")

            assertTrue(
                PhelSpecialForms.LET_LIKE.containsAll(expected),
                "LET_LIKE should cover every binding-vector form, found ${PhelSpecialForms.LET_LIKE}",
            )
        }

        @Test
        fun `excludes catch whose second element is an exception class`() {
            assertFalse("catch" in PhelSpecialForms.LET_LIKE, "`catch` binds no [name value] vector")
        }
    }

    @Nested
    inner class FunctionDefining {

        @Test
        fun `contains the fn and defn family`() {
            val expected = setOf("fn", "defn", "defn-", "defmacro", "defmacro-")

            assertTrue(
                PhelSpecialForms.FUNCTION_DEFINING.containsAll(expected),
                "FUNCTION_DEFINING should cover the whole fn/defn family",
            )
        }

        @Test
        fun `every function-defining form is also a variadic head`() {
            PhelSpecialForms.FUNCTION_DEFINING.forEach { head ->
                assertTrue(
                    head in PhelSpecialForms.VARIADIC_HEADS,
                    "'$head' introduces a parameter vector, so it can never be read positionally",
                )
            }
        }
    }
}
