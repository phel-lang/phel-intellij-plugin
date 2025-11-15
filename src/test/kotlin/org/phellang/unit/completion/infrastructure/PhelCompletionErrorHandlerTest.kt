package org.phellang.unit.completion.infrastructure

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.phellang.completion.infrastructure.PhelCompletionErrorHandler
import org.junit.jupiter.api.Assertions.*

class PhelCompletionErrorHandlerTest {

    @Test
    fun `isCompletionContextValid should return false for null element`() {
        val result = PhelCompletionErrorHandler.isCompletionContextValid(null)
        assertFalse(result)
    }

    @Test
    fun `error handler should have expected methods`() {
        val handlerClass = PhelCompletionErrorHandler::class.java
        val methods = handlerClass.declaredMethods.map { it.name }

        // Should have the main error handling methods
        assertTrue(methods.contains("withErrorHandling"), "Should have withErrorHandling method")
        assertTrue(methods.contains("isCompletionContextValid"), "Should have isCompletionContextValid method")
        assertTrue(methods.contains("withResultSet"), "Should have withResultSet method")
    }

    @Test
    fun `error handler should have proper class structure`() {
        val handlerClass = PhelCompletionErrorHandler::class.java

        assertEquals("org.phellang.completion.infrastructure", handlerClass.packageName)

        assertEquals("PhelCompletionErrorHandler", handlerClass.simpleName)

        assertTrue(java.lang.reflect.Modifier.isFinal(handlerClass.modifiers))
    }

    @Test
    fun `error handler should have nested interfaces`() {
        val handlerClass = PhelCompletionErrorHandler::class.java
        val nestedClasses = handlerClass.declaredClasses.map { it.simpleName }

        assertTrue(nestedClasses.contains("CompletionOperation"), "Should have CompletionOperation interface")
        assertTrue(nestedClasses.contains("ThrowingRunnable"), "Should have ThrowingRunnable interface")
    }

    @Test
    fun `CompletionOperation interface should have correct structure`() {
        val operationClass = PhelCompletionErrorHandler.CompletionOperation::class.java

        assertTrue(operationClass.isInterface)

        val methods = operationClass.declaredMethods.map { it.name }
        assertTrue(methods.contains("execute"), "Should have execute method")

        assertTrue(methods.contains("getResult"), "Should have getResult method")
    }

    @Test
    fun `ThrowingRunnable interface should have correct structure`() {
        val runnableClass = PhelCompletionErrorHandler.ThrowingRunnable::class.java

        assertTrue(runnableClass.isInterface)

        val methods = runnableClass.declaredMethods.map { it.name }
        assertTrue(methods.contains("run"), "Should have run method")
    }

    @Test
    fun `CompletionOperation should be executable`() {
        var executed = false
        val mockRunnable = PhelCompletionErrorHandler.ThrowingRunnable {
            executed = true
        }

        val operation = PhelCompletionErrorHandler.withResultSet(mockRunnable)

        assertDoesNotThrow {
            operation.execute()
        }

        assertTrue(executed, "Operation should have executed the runnable")
    }

    @Test
    fun `error handler should be thread-safe`() {
        val results = java.util.concurrent.ConcurrentLinkedQueue<Boolean>()
        val threads = mutableListOf<Thread>()

        repeat(5) {
            val thread = Thread {
                try {
                    val result = PhelCompletionErrorHandler.isCompletionContextValid(null)
                    results.add(!result) // Should be false, so !result should be true
                } catch (_: Exception) {
                    results.add(false)
                }
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }

        assertEquals(5, results.size)
        assertTrue(results.all { it }, "All threads should have succeeded")
    }

    @Test
    fun `error handler should maintain consistent behavior`() {
        repeat(3) {
            val result = PhelCompletionErrorHandler.isCompletionContextValid(null)
            assertFalse(result, "Should consistently return false for null")
        }
    }

    @Test
    fun `error handler should handle interface instantiation`() {
        assertDoesNotThrow {
            val runnable = PhelCompletionErrorHandler.ThrowingRunnable {
                // Empty implementation
            }
            assertNotNull(runnable)

            val operation = PhelCompletionErrorHandler.CompletionOperation {
                // Empty implementation
            }
            assertNotNull(operation)
        }
    }

    @Test
    fun `error handler should support functional interface usage`() {
        // Test that the interfaces work as functional interfaces
        var runnableExecuted = false
        var operationExecuted = false

        val runnable = PhelCompletionErrorHandler.ThrowingRunnable {
            runnableExecuted = true
        }

        val operation = PhelCompletionErrorHandler.CompletionOperation {
            operationExecuted = true
        }

        assertDoesNotThrow {
            runnable.run()
            operation.execute()
        }

        assertTrue(runnableExecuted)
        assertTrue(operationExecuted)
    }
}
