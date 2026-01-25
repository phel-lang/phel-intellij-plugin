package org.phellang.unit.tools.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.completion.infrastructure.PhelCompletionPriority
import org.phellang.tools.model.ApiFunction
import org.phellang.tools.model.ApiFunctionMeta
import org.phellang.tools.model.PriorityRules

class PriorityRulesTest {

    private fun createApiFunction(
        namespace: String = "core", name: String = "test", deprecated: String? = null, macro: Boolean? = null
    ): ApiFunction {
        return ApiFunction(
            namespace = namespace,
            name = name,
            description = "Test description",
            doc = "Test doc",
            signatures = listOf("(test)"),
            githubUrl = "https://github.com/test",
            docUrl = "https://docs.test",
            meta = ApiFunctionMeta(
                deprecated = deprecated, macro = macro
            )
        )
    }

    @Nested
    inner class DeprecatedFunctions {

        @Test
        fun `should return DEPRECATED_FUNCTIONS for deprecated function`() {
            val function = createApiFunction(deprecated = "1.0")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.DEPRECATED_FUNCTIONS, priority)
        }

        @Test
        fun `deprecated should take precedence over macro`() {
            val function = createApiFunction(deprecated = "1.0", macro = true)
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.DEPRECATED_FUNCTIONS, priority)
        }

        @Test
        fun `deprecated should take precedence over predicate`() {
            val function = createApiFunction(name = "empty?", deprecated = "1.0")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.DEPRECATED_FUNCTIONS, priority)
        }
    }

    @Nested
    inner class Macros {

        @Test
        fun `should return MACROS for macro function`() {
            val function = createApiFunction(macro = true)
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.MACROS, priority)
        }

        @Test
        fun `macro should take precedence over predicate`() {
            val function = createApiFunction(name = "macro?", macro = true)
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.MACROS, priority)
        }
    }

    @Nested
    inner class PredicateFunctions {

        @Test
        fun `should return PREDICATE_FUNCTIONS for function ending with question mark`() {
            val function = createApiFunction(name = "nil?")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.PREDICATE_FUNCTIONS, priority)
        }

        @Test
        fun `should return PREDICATE_FUNCTIONS for empty question mark`() {
            val function = createApiFunction(name = "empty?")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.PREDICATE_FUNCTIONS, priority)
        }

        @Test
        fun `should return PREDICATE_FUNCTIONS for namespaced predicate`() {
            val function = createApiFunction(namespace = "test", name = "test/passed?")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.PREDICATE_FUNCTIONS, priority)
        }
    }

    @Nested
    inner class ArithmeticOperators {

        @Test
        fun `should return ARITHMETIC_FUNCTIONS for plus operator`() {
            val function = createApiFunction(name = "+")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.ARITHMETIC_FUNCTIONS, priority)
        }

        @Test
        fun `should return ARITHMETIC_FUNCTIONS for minus operator`() {
            val function = createApiFunction(name = "-")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.ARITHMETIC_FUNCTIONS, priority)
        }

        @Test
        fun `should return ARITHMETIC_FUNCTIONS for comparison operators`() {
            listOf("<", "<=", ">", ">=", "=", "<=>").forEach { op ->
                val function = createApiFunction(name = op)
                val priority = PriorityRules.determinePriority(function)
                assertEquals(PhelCompletionPriority.ARITHMETIC_FUNCTIONS, priority, "Failed for operator: $op")
            }
        }

        @Test
        fun `should return ARITHMETIC_FUNCTIONS for bit operations`() {
            listOf("bit-and", "bit-or", "bit-xor").forEach { op ->
                val function = createApiFunction(name = op)
                val priority = PriorityRules.determinePriority(function)
                assertEquals(PhelCompletionPriority.ARITHMETIC_FUNCTIONS, priority, "Failed for operator: $op")
            }
        }

        @Test
        fun `should return ARITHMETIC_FUNCTIONS for inc and dec`() {
            assertEquals(
                PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
                PriorityRules.determinePriority(createApiFunction(name = "inc"))
            )
            assertEquals(
                PhelCompletionPriority.ARITHMETIC_FUNCTIONS,
                PriorityRules.determinePriority(createApiFunction(name = "dec"))
            )
        }
    }

    @Nested
    inner class SpecialForms {

        @Test
        fun `should return SPECIAL_FORMS for def`() {
            val function = createApiFunction(name = "def")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.SPECIAL_FORMS, priority)
        }

        @Test
        fun `should return SPECIAL_FORMS for defn`() {
            val function = createApiFunction(name = "defn")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.SPECIAL_FORMS, priority)
        }

        @Test
        fun `should return SPECIAL_FORMS for let`() {
            val function = createApiFunction(name = "let")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.SPECIAL_FORMS, priority)
        }

        @Test
        fun `should return SPECIAL_FORMS for fn`() {
            val function = createApiFunction(name = "fn")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.SPECIAL_FORMS, priority)
        }

        @Test
        fun `should return SPECIAL_FORMS for php interop forms`() {
            listOf("php/new", "php/->", "php/::").forEach { form ->
                val function = createApiFunction(namespace = "php", name = form)
                val priority = PriorityRules.determinePriority(function)
                assertEquals(PhelCompletionPriority.SPECIAL_FORMS, priority, "Failed for: $form")
            }
        }
    }

    @Nested
    inner class ControlFlow {

        @Test
        fun `should return CONTROL_FLOW for if`() {
            val function = createApiFunction(name = "if")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.CONTROL_FLOW, priority)
        }

        @Test
        fun `should return CONTROL_FLOW for when`() {
            val function = createApiFunction(name = "when")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.CONTROL_FLOW, priority)
        }

        @Test
        fun `should return CONTROL_FLOW for cond`() {
            val function = createApiFunction(name = "cond")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.CONTROL_FLOW, priority)
        }

        @Test
        fun `should return CONTROL_FLOW for and and or`() {
            assertEquals(
                PhelCompletionPriority.CONTROL_FLOW, PriorityRules.determinePriority(createApiFunction(name = "and"))
            )
            assertEquals(
                PhelCompletionPriority.CONTROL_FLOW, PriorityRules.determinePriority(createApiFunction(name = "or"))
            )
        }

        @Test
        fun `should return CONTROL_FLOW for loop constructs`() {
            listOf("foreach", "for", "dofor", "doseq").forEach { construct ->
                val function = createApiFunction(name = construct)
                val priority = PriorityRules.determinePriority(function)
                assertEquals(PhelCompletionPriority.CONTROL_FLOW, priority, "Failed for: $construct")
            }
        }
    }

    @Nested
    inner class CollectionFunctions {

        @Test
        fun `should return COLLECTION_FUNCTIONS for map in core namespace`() {
            val function = createApiFunction(namespace = "core", name = "map")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.COLLECTION_FUNCTIONS, priority)
        }

        @Test
        fun `should return COLLECTION_FUNCTIONS for filter in core namespace`() {
            val function = createApiFunction(namespace = "core", name = "filter")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.COLLECTION_FUNCTIONS, priority)
        }

        @Test
        fun `should return COLLECTION_FUNCTIONS for reduce in core namespace`() {
            val function = createApiFunction(namespace = "core", name = "reduce")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.COLLECTION_FUNCTIONS, priority)
        }

        @Test
        fun `should not return COLLECTION_FUNCTIONS for map in non-core namespace`() {
            val function = createApiFunction(namespace = "custom", name = "map")
            val priority = PriorityRules.determinePriority(function)
            // Falls back to namespace priority (CORE_FUNCTIONS as default)
            assertEquals(PhelCompletionPriority.CORE_FUNCTIONS, priority)
        }
    }

    @Nested
    inner class NamespacePriority {

        @Test
        fun `should return STRING_FUNCTIONS for str namespace`() {
            val function = createApiFunction(namespace = "str", name = "str/join")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.STRING_FUNCTIONS, priority)
        }

        @Test
        fun `should return JSON_FUNCTIONS for json namespace`() {
            val function = createApiFunction(namespace = "json", name = "json/encode")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.JSON_FUNCTIONS, priority)
        }

        @Test
        fun `should return HTML_FUNCTIONS for html namespace`() {
            val function = createApiFunction(namespace = "html", name = "html/html")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.HTML_FUNCTIONS, priority)
        }

        @Test
        fun `should return TEST_FUNCTIONS for test namespace`() {
            val function = createApiFunction(namespace = "test", name = "test/is")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.TEST_FUNCTIONS, priority)
        }

        @Test
        fun `should return CORE_FUNCTIONS as default for unknown namespace`() {
            val function = createApiFunction(namespace = "unknown", name = "unknown/func")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.CORE_FUNCTIONS, priority)
        }
    }

    @Nested
    inner class ShortNameExtraction {

        @Test
        fun `should extract short name from namespaced function`() {
            // Test via priority detection
            val function = createApiFunction(namespace = "core", name = "core/map")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.COLLECTION_FUNCTIONS, priority)
        }

        @Test
        fun `should handle function without namespace prefix`() {
            val function = createApiFunction(namespace = "core", name = "map")
            val priority = PriorityRules.determinePriority(function)
            assertEquals(PhelCompletionPriority.COLLECTION_FUNCTIONS, priority)
        }
    }
}
