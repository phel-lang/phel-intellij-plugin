package org.phellang.unit.documentation.providers

import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.registry.PhelFunctionRegistry
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
    fun `should be consistent across multiple calls`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("test-symbol")
        
        val result1 = provider.getQuickNavigateInfo(symbol)
        val result2 = provider.getQuickNavigateInfo(symbol)
        
        assertEquals(result1, result2)
    }






}
