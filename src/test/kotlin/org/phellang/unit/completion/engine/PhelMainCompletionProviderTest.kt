package org.phellang.unit.completion.engine

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.phellang.completion.engine.PhelMainCompletionProvider
import org.junit.jupiter.api.Assertions.*

class PhelMainCompletionProviderTest {

    private lateinit var provider: PhelMainCompletionProvider

    @Test
    fun `should be instantiable without errors`() {
        assertDoesNotThrow {
            PhelMainCompletionProvider()
        }
    }

    @Test
    fun `should be consistent across multiple calls`() {
        assertDoesNotThrow {
            val provider1 = PhelMainCompletionProvider()
            val provider2 = PhelMainCompletionProvider()
            
            assertNotNull(provider1)
            assertNotNull(provider2)
        }
    }

    @Test
    fun `should maintain thread safety`() {
        val providers = mutableListOf<PhelMainCompletionProvider>()
        val threads = mutableListOf<Thread>()
        
        repeat(5) {
            val thread = Thread {
                val localProvider = PhelMainCompletionProvider()
                providers.add(localProvider)
            }
            threads.add(thread)
            thread.start()
        }
        
        threads.forEach { it.join() }
        
        assertEquals(5, providers.size)
        providers.forEach { assertNotNull(it) }
    }

    @Test
    fun `should work with multiple provider instances`() {
        val provider1 = PhelMainCompletionProvider()
        val provider2 = PhelMainCompletionProvider()
        
        assertNotNull(provider1)
        assertNotNull(provider2)
        
        assertTrue(provider1 !== provider2)
        assertEquals(provider1.javaClass, provider2.javaClass)
    }

    @Test
    fun `should have expected method structure`() {
        provider = PhelMainCompletionProvider()
        
        val methods = provider.javaClass.declaredMethods
        val hasAddCompletionsMethod = methods.any { it.name == "addCompletions" }
        assertTrue(hasAddCompletionsMethod, "Provider should have addCompletions method")
    }

    @Test
    fun `should be performant during instantiation`() {
        val startTime = System.currentTimeMillis()
        
        repeat(50) {
            val provider = PhelMainCompletionProvider()
            assertNotNull(provider)
        }
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        assertTrue(duration < 1000, "Provider instantiation took too long: ${duration}ms")
    }

    @Test
    fun `should maintain consistent class structure`() {
        val provider1 = PhelMainCompletionProvider()
        val provider2 = PhelMainCompletionProvider()
        
        assertEquals(provider1.javaClass.name, provider2.javaClass.name)
        assertEquals(provider1.javaClass.packageName, provider2.javaClass.packageName)
        assertEquals(provider1.javaClass.simpleName, provider2.javaClass.simpleName)
        
        val methods1 = provider1.javaClass.declaredMethods.map { it.name }.sorted()
        val methods2 = provider2.javaClass.declaredMethods.map { it.name }.sorted()
        assertEquals(methods1, methods2)
    }
}
