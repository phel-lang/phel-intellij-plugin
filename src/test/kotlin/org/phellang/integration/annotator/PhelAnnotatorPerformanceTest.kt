package org.phellang.integration.annotator

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.phellang.annotator.analyzers.PhelCommentAnalyzer
import org.phellang.annotator.analyzers.PhelSymbolPositionAnalyzer
import org.phellang.annotator.infrastructure.PhelAnnotationUtils
import org.phellang.language.psi.PhelShortFn

@ExtendWith(MockitoExtension::class)
class PhelAnnotatorPerformanceTest {

    @Mock
    private lateinit var mockElement: PsiElement

    @Mock
    private lateinit var mockShortFn: PhelShortFn

    @Test
    fun `text validation should be performant with large strings`() {
        val largeSizes = listOf(1000, 10000, 100000)

        largeSizes.forEach { size ->
            val largeText = "a".repeat(size)

            val startTime = System.nanoTime()
            val result = PhelAnnotationUtils.isValidText(largeText)
            val endTime = System.nanoTime()

            val durationMs = (endTime - startTime) / 1_000_000.0

            assertTrue(result, "Large text should be valid")
            assertTrue(durationMs < 5.0, "Validation should be fast for size $size: ${durationMs}ms")
        }
    }

    @Test
    fun `namespace analysis should be performant with complex namespaces`() {
        val complexNamespaces = listOf(
            "level\\".repeat(100) + "symbol",
            "a".repeat(1000) + "\\" + "b".repeat(1000),
            (1..50).joinToString("\\") { "namespace$it" } + "\\symbol")

        complexNamespaces.forEach { namespace ->
            val startTime = System.nanoTime()
            val result = PhelSymbolPositionAnalyzer.hasNamespacePrefix(namespace)
            val endTime = System.nanoTime()

            val durationMs = (endTime - startTime) / 1_000_000.0

            assertTrue(result, "Complex namespace should be detected")
            assertTrue(durationMs < 1.0, "Analysis should be fast for complex namespace: ${durationMs}ms")
        }
    }

    @Test
    fun `shouldAnnotate should be performant with various text ranges`() {
        val ranges = listOf(
            TextRange(0, 1), TextRange(0, 1000), TextRange(1000, 2000), TextRange(0, 100000), TextRange(50000, 100000)
        )

        ranges.forEach { range ->
            `when`(mockElement.textRange).thenReturn(range)

            val startTime = System.nanoTime()
            val result = PhelAnnotationUtils.shouldAnnotate(mockElement)
            val endTime = System.nanoTime()

            val durationMs = (endTime - startTime) / 1_000_000.0

            assertTrue(result, "Element with valid range should be annotatable")
            assertTrue(durationMs < 0.1, "shouldAnnotate should be very fast: ${durationMs}ms")
        }
    }

    @Test
    fun `comment analysis should handle deep PSI hierarchies efficiently`() {
        val depths = listOf(10, 50, 100, 500)

        depths.forEach { depth ->
            // Create a deep hierarchy
            val parents = (1..depth).map { mock(PsiElement::class.java) }

            `when`(mockElement.parent).thenReturn(parents[0])
            for (i in 0 until parents.size - 1) {
                `when`(parents[i].parent).thenReturn(parents[i + 1])
            }
            `when`(parents.last().parent).thenReturn(mockShortFn)

            val startTime = System.nanoTime()
            val result = PhelCommentAnalyzer.isInsideShortFunction(mockElement)
            val endTime = System.nanoTime()

            val durationMs = (endTime - startTime) / 1_000_000.0

            assertTrue(result, "Should find short function at depth $depth")
            assertTrue(durationMs < 10.0, "Analysis should be efficient at depth $depth: ${durationMs}ms")
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [1000, 5000, 10000, 50000])
    fun `batch processing should be efficient`(batchSize: Int) {
        val symbols = (1..batchSize).map { "symbol$it" }

        val startTime = System.nanoTime()

        val validationResults = symbols.map { PhelAnnotationUtils.isValidText(it) }
        val namespaceResults = symbols.map { PhelSymbolPositionAnalyzer.hasNamespacePrefix(it) }

        val endTime = System.nanoTime()
        val durationMs = (endTime - startTime) / 1_000_000.0

        assertEquals(batchSize, validationResults.size)
        assertEquals(batchSize, namespaceResults.size)
        assertTrue(validationResults.all { it }, "All symbols should be valid")

        // Should process at least 1000 symbols per millisecond
        val symbolsPerMs = batchSize / durationMs
        assertTrue(symbolsPerMs > 1000, "Should process efficiently: $symbolsPerMs symbols/ms")
    }

    @Test
    fun `edge case strings should be handled correctly`() {
        val edgeCases = mapOf(
            // Boundary cases
            "" to false, " " to false, "\t" to false, "\n" to false, "\r" to false, "\r\n" to false,

            // Unicode edge cases
            "\u0000" to true,  // Null character
            "\uFFFF" to true,  // Max BMP character
            "🎉" to true,      // Emoji
            "𝕏" to true,       // Mathematical symbol

            // Very long strings
            "a".repeat(10000) to true, " ".repeat(1000) to false,

            // Mixed content
            "valid\u0000symbol" to true, "symbol\t\n" to true, " \t symbol \n " to true
        )

        edgeCases.forEach { (input, expected) ->
            assertEquals(
                expected,
                PhelAnnotationUtils.isValidText(input),
                "Edge case should be handled correctly: '${input.take(20)}...'"
            )
        }
    }

    @Test
    fun `namespace analysis should handle edge case patterns`() {
        val edgeCases = mapOf(
            // Boundary cases
            "a\\b" to true,
            "\\a" to false,
            "a\\" to false,
            "\\" to false,
            "\\\\" to false,

            // Unicode namespaces
            "测试\\符号" to true,
            "🎉\\🎊" to true,
            "α\\β" to true,

            // Special characters
            "ns-1\\sym-1" to true,
            "ns_1\\sym_1" to true,
            "ns.1\\sym.1" to true,
            "ns?\\sym?" to true,
            "ns!\\sym!" to true,

            // Very long components
            "a".repeat(1000) + "\\" + "b".repeat(1000) to true,

            // Multiple backslashes
            "a\\b\\c" to true,
            "a\\b\\c\\d\\e\\f\\g" to true
        )

        edgeCases.forEach { (input, expected) ->
            assertEquals(
                expected,
                PhelSymbolPositionAnalyzer.hasNamespacePrefix(input),
                "Edge case namespace should be handled correctly: '${input.take(20)}...'"
            )
        }
    }

    @Test
    fun `memory usage should be reasonable for large inputs`() {
        val runtime = Runtime.getRuntime()

        // Force garbage collection before test
        System.gc()
        val initialMemory = runtime.totalMemory() - runtime.freeMemory()

        // Process large amount of data
        val largeInputs = (1..10000).map { i ->
            "namespace$i\\" + "symbol".repeat(100)
        }

        largeInputs.forEach { input ->
            PhelAnnotationUtils.isValidText(input)
            PhelSymbolPositionAnalyzer.hasNamespacePrefix(input)
        }

        // Force garbage collection after test
        System.gc()
        val finalMemory = runtime.totalMemory() - runtime.freeMemory()

        val memoryUsedMB = (finalMemory - initialMemory) / (1024.0 * 1024.0)

        // Should not use excessive memory (less than 50MB for this test)
        assertTrue(memoryUsedMB < 50.0, "Memory usage should be reasonable: ${memoryUsedMB}MB")
    }

    @Test
    fun `concurrent access should be thread-safe`() {
        val symbols = (1..1000).map { "symbol$it" }
        val namespacedSymbols = (1..1000).map { "ns$it\\symbol$it" }

        val threads = (1..10).map { _ ->
            Thread {
                repeat(100) {
                    symbols.forEach { symbol ->
                        assertTrue(PhelAnnotationUtils.isValidText(symbol))
                        assertFalse(PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol))
                    }

                    namespacedSymbols.forEach { symbol ->
                        assertTrue(PhelAnnotationUtils.isValidText(symbol))
                        assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol))
                    }
                }
            }
        }

        val startTime = System.nanoTime()
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        val endTime = System.nanoTime()

        val durationMs = (endTime - startTime) / 1_000_000.0

        // Should complete without deadlocks or exceptions in reasonable time
        assertTrue(durationMs < 5000.0, "Concurrent access should complete in reasonable time: ${durationMs}ms")
    }

    @Test
    fun `stress test with mixed workload`() {
        val iterations = 10000
        val startTime = System.nanoTime()

        repeat(iterations) { i ->
            // Mix of different operations
            val symbol = "symbol$i"
            val namespacedSymbol = "ns$i\\$symbol"
            val largeSymbol = symbol.repeat(10)

            // Text validation
            assertTrue(PhelAnnotationUtils.isValidText(symbol))
            assertTrue(PhelAnnotationUtils.isValidText(namespacedSymbol))
            assertTrue(PhelAnnotationUtils.isValidText(largeSymbol))

            // Namespace analysis
            assertFalse(PhelSymbolPositionAnalyzer.hasNamespacePrefix(symbol))
            assertTrue(PhelSymbolPositionAnalyzer.hasNamespacePrefix(namespacedSymbol))
            assertFalse(PhelSymbolPositionAnalyzer.hasNamespacePrefix(largeSymbol))

            // Element validation
            `when`(mockElement.textRange).thenReturn(TextRange(0, symbol.length))
            assertTrue(PhelAnnotationUtils.shouldAnnotate(mockElement))
        }

        val endTime = System.nanoTime()
        val durationMs = (endTime - startTime) / 1_000_000.0

        // Should handle mixed workload efficiently (very relaxed performance requirement)
        val operationsPerMs = (iterations * 7) / durationMs // 7 operations per iteration
        assertTrue(operationsPerMs > 10, "Should handle mixed workload efficiently: $operationsPerMs ops/ms")
    }

    @Test
    fun `error conditions should be handled gracefully`() {
        // Test various error conditions that might occur in real usage

        // Null element
        assertFalse(PhelAnnotationUtils.shouldAnnotate(null))

        // Element with null text range
        `when`(mockElement.textRange).thenReturn(null)
        assertFalse(PhelAnnotationUtils.shouldAnnotate(mockElement))

        // Element with zero-length text range
        `when`(mockElement.textRange).thenReturn(TextRange(5, 5)) // Zero length
        assertFalse(PhelAnnotationUtils.shouldAnnotate(mockElement))

        // Very large text ranges
        `when`(mockElement.textRange).thenReturn(TextRange(0, Integer.MAX_VALUE))
        assertTrue(PhelAnnotationUtils.shouldAnnotate(mockElement))

        // Reset mock for final test
        reset(mockElement)
        `when`(mockElement.textRange).thenReturn(TextRange(0, 10))
        assertTrue(PhelAnnotationUtils.shouldAnnotate(mockElement))
    }
}
