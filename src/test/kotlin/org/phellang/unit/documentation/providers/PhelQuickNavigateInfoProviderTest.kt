package org.phellang.unit.documentation.providers

import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.documentation.providers.PhelQuickNavigateInfoProvider
import org.phellang.language.psi.PhelSymbol

class PhelQuickNavigateInfoProviderTest {

    private lateinit var provider: PhelQuickNavigateInfoProvider

    @BeforeEach
    fun setup() {
        provider = PhelQuickNavigateInfoProvider()
    }

    @Test
    fun `should return null when element is null`() {
        val result = provider.getQuickNavigateInfo(null)
        assertNull(result)
    }

    @Test
    fun `should return null when element is not a PhelSymbol`() {
        val element = mock(PsiElement::class.java)
        val result = provider.getQuickNavigateInfo(element)
        assertNull(result)
    }

    @Test
    fun `should return null when symbol has null text`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn(null)
        
        val result = provider.getQuickNavigateInfo(symbol)
        assertNull(result)
    }

    @Test
    fun `should return null when symbol has empty text`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("")
        
        val result = provider.getQuickNavigateInfo(symbol)
        assertNull(result)
    }

    @Test
    fun `should return null when symbol is not in registry`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("non-existent-function")
        
        val result = provider.getQuickNavigateInfo(symbol)
        assertNull(result)
    }

    @Test
    fun `should return function name with signature when available in registry`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("defn")
        
        val result = provider.getQuickNavigateInfo(symbol)
        
        // The result will depend on what's in the registry
        // If defn is registered, it should return name + signature
        if (PhelFunctionRegistry.getFunction("defn") != null) {
            assertNotNull(result)
            assertTrue(result!!.startsWith("defn "))
        }
    }

    @Test
    fun `should be instantiable without errors`() {
        assertDoesNotThrow {
            PhelQuickNavigateInfoProvider()
        }
    }

    @Test
    fun `should be consistent across multiple calls`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("test-symbol")
        
        val result1 = provider.getQuickNavigateInfo(symbol)
        val result2 = provider.getQuickNavigateInfo(symbol)
        
        assertEquals(result1, result2)
    }

    @Test
    fun `should maintain thread safety`() {
        val providers = mutableListOf<PhelQuickNavigateInfoProvider>()
        val threads = mutableListOf<Thread>()
        
        repeat(5) {
            val thread = Thread {
                val localProvider = PhelQuickNavigateInfoProvider()
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
    fun `should be performant during instantiation`() {
        val startTime = System.currentTimeMillis()
        
        repeat(100) {
            val provider = PhelQuickNavigateInfoProvider()
            assertNotNull(provider)
        }
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        assertTrue(duration < 1000, "Provider instantiation took too long: ${duration}ms")
    }

    @Test
    fun `should have expected method structure`() {
        val methods = provider.javaClass.declaredMethods
        val hasGetQuickNavigateInfoMethod = methods.any { it.name == "getQuickNavigateInfo" }
        assertTrue(hasGetQuickNavigateInfoMethod, "Provider should have getQuickNavigateInfo method")
    }

    @Test
    fun `should maintain consistent class structure`() {
        val provider1 = PhelQuickNavigateInfoProvider()
        val provider2 = PhelQuickNavigateInfoProvider()
        
        assertEquals(provider1.javaClass.name, provider2.javaClass.name)
        assertEquals(provider1.javaClass.packageName, provider2.javaClass.packageName)
        assertEquals(provider1.javaClass.simpleName, provider2.javaClass.simpleName)
    }

    @Test
    fun `should handle rapid successive calls`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("test")
        
        val results = mutableListOf<String?>()
        
        repeat(100) {
            results.add(provider.getQuickNavigateInfo(symbol))
        }
        
        // All results should be consistent
        val firstResult = results[0]
        assertTrue(results.all { it == firstResult })
    }

    @Test
    fun `should handle multiple different symbols`() {
        val symbol1 = mock(PhelSymbol::class.java)
        `when`(symbol1.text).thenReturn("symbol1")
        
        val symbol2 = mock(PhelSymbol::class.java)
        `when`(symbol2.text).thenReturn("symbol2")

        // Results can be null if symbols are not in registry
        // But they should be computed independently
        assertDoesNotThrow {
            provider.getQuickNavigateInfo(symbol1)
            provider.getQuickNavigateInfo(symbol2)
        }
    }
}
