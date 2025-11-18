package org.phellang.unit.core.utils

import com.intellij.openapi.progress.ProcessCanceledException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.core.utils.PhelErrorHandler

class PhelErrorHandlerTest {

    @Test
    fun `safeOperation should return result when operation succeeds`() {
        val result = PhelErrorHandler.safeOperation {
            "success"
        }

        assertEquals("success", result)
    }

    @Test
    fun `safeOperation should return null when operation throws exception`() {
        val result = PhelErrorHandler.safeOperation {
            throw RuntimeException("Test exception")
        }

        assertNull(result)
    }

    @Test
    fun `safeOperation should re-throw ProcessCanceledException`() {
        assertThrows(ProcessCanceledException::class.java) {
            PhelErrorHandler.safeOperation {
                throw ProcessCanceledException()
            }
        }
    }

    @Test
    fun `safeOperation should handle null return values`() {
        val result = PhelErrorHandler.safeOperation {
            null
        }

        assertNull(result)
    }

    @Test
    fun `safeOperation should handle complex types`() {
        val result = PhelErrorHandler.safeOperation {
            listOf(1, 2, 3)
        }

        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun `safeOperation should handle nested exceptions`() {
        val result = PhelErrorHandler.safeOperation {
            try {
                throw IllegalStateException("Inner exception")
            } catch (e: Exception) {
                throw RuntimeException("Outer exception", e)
            }
        }

        assertNull(result)
    }

    @Test
    fun `safeOperation should work with different return types`() {
        val intResult = PhelErrorHandler.safeOperation { 42 }
        val stringResult = PhelErrorHandler.safeOperation { "test" }
        val boolResult = PhelErrorHandler.safeOperation { true }
        val listResult = PhelErrorHandler.safeOperation { emptyList<String>() }

        assertEquals(42, intResult)
        assertEquals("test", stringResult)
        assertEquals(true, boolResult)
        assertEquals(emptyList<String>(), listResult)
    }

    @Test
    fun `safeOperation should be thread-safe`() {
        val results = mutableListOf<String?>()
        val threads = mutableListOf<Thread>()

        repeat(10) { index ->
            val thread = Thread {
                val result = PhelErrorHandler.safeOperation {
                    if (index % 2 == 0) "success-$index" else throw RuntimeException("error-$index")
                }
                synchronized(results) {
                    results.add(result)
                }
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }

        assertEquals(10, results.size)
        val successCount = results.filterNotNull().size
        assertEquals(5, successCount)
    }

    @Test
    fun `safeOperation should handle array index out of bounds`() {
        val result = PhelErrorHandler.safeOperation {
            val array = arrayOf(1, 2, 3)
            array[10]
        }

        assertNull(result)
    }

    @Test
    fun `safeOperation should be reusable`() {
        repeat(100) {
            val result = PhelErrorHandler.safeOperation {
                if (it % 2 == 0) it else throw RuntimeException()
            }

            if (it % 2 == 0) {
                assertEquals(it, result)
            } else {
                assertNull(result)
            }
        }
    }

    @Test
    fun `safeOperation should handle errors in lambda capture`() {
        val capturedValue = "test"
        val result = PhelErrorHandler.safeOperation {
            capturedValue.uppercase()
        }

        assertEquals("TEST", result)
    }

    @Test
    fun `safeOperation should maintain operation order`() {
        val operations = mutableListOf<Int>()

        repeat(5) { index ->
            PhelErrorHandler.safeOperation {
                operations.add(index)
                index
            }
        }

        assertEquals(listOf(0, 1, 2, 3, 4), operations)
    }
}
