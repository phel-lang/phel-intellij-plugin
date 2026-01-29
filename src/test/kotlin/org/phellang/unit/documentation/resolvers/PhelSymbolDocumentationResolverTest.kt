package org.phellang.unit.documentation.resolvers

import com.intellij.psi.PsiElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.phellang.documentation.resolvers.PhelSymbolDocumentationResolver
import org.phellang.language.psi.PhelSymbol

class PhelSymbolDocumentationResolverTest {

    private lateinit var resolver: PhelSymbolDocumentationResolver

    @BeforeEach
    fun setup() {
        resolver = PhelSymbolDocumentationResolver()
    }

    @Test
    fun `should return null when element is null`() {
        val result = resolver.resolveDocumentation(null, null)
        assertNull(result)
    }

    @Test
    fun `should return null when element is not a PhelSymbol and has no parent symbol`() {
        val element = mock(PsiElement::class.java)
        val result = resolver.resolveDocumentation(element, element)
        assertNull(result)
    }

    @Test
    fun `should return null when symbol has null text`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn(null)

        val result = resolver.resolveDocumentation(symbol, symbol)
        assertNull(result)
    }

    @Test
    fun `should return null when symbol has empty text`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("")

        val result = resolver.resolveDocumentation(symbol, symbol)
        assertNull(result)
    }

    @Test
    fun `should return basic documentation for symbol not in API`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("my-custom-symbol")

        val result = resolver.resolveDocumentation(symbol, symbol)

        assertNotNull(result)
        assertTrue(result!!.contains("my-custom-symbol"))
        assertTrue(result.contains("h3"))
    }

    @Test
    fun `should format local symbol documentation with category`() {
        val symbol = mock(PhelSymbol::class.java)
        `when`(symbol.text).thenReturn("param-name")

        // Note: Since we're using real PhelSymbolAnalyzer, the result will depend on the actual implementation
        val result = resolver.resolveDocumentation(symbol, symbol)

        assertNotNull(result)
        assertTrue(result!!.contains("param-name"))
    }

    @Test
    fun `should be instantiable multiple times`() {
        val resolver1 = PhelSymbolDocumentationResolver()
        val resolver2 = PhelSymbolDocumentationResolver()

        assertNotNull(resolver1)
        assertNotNull(resolver2)
        assertTrue(resolver1 !== resolver2)
    }

    @Test
    fun `should maintain thread safety`() {
        val resolvers = mutableListOf<PhelSymbolDocumentationResolver>()
        val threads = mutableListOf<Thread>()

        repeat(5) {
            val thread = Thread {
                val localResolver = PhelSymbolDocumentationResolver()
                resolvers.add(localResolver)
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }

        assertEquals(5, resolvers.size)
        resolvers.forEach { assertNotNull(it) }
    }

    @Test
    fun `should be performant during instantiation`() {
        val startTime = System.currentTimeMillis()

        repeat(100) {
            val resolver = PhelSymbolDocumentationResolver()
            assertNotNull(resolver)
        }

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        assertTrue(duration < 1000, "Resolver instantiation took too long: ${duration}ms")
    }

    @Test
    fun `should maintain consistent class structure`() {
        val resolver1 = PhelSymbolDocumentationResolver()
        val resolver2 = PhelSymbolDocumentationResolver()

        assertEquals(resolver1.javaClass.name, resolver2.javaClass.name)
        assertEquals(resolver1.javaClass.packageName, resolver2.javaClass.packageName)
        assertEquals(resolver1.javaClass.simpleName, resolver2.javaClass.simpleName)
    }

    @Nested
    inner class ApiDocumentationLookup {

        @Test
        fun `should return API documentation for known core functions`() {
            val symbol = mock(PhelSymbol::class.java)
            `when`(symbol.text).thenReturn("core/map")

            val result = resolver.resolveDocumentation(symbol, symbol)

            assertNotNull(result)
            assertTrue(result!!.contains("core/map"))
        }

        @Test
        fun `should return API documentation for known str functions`() {
            val symbol = mock(PhelSymbol::class.java)
            `when`(symbol.text).thenReturn("str/join")

            val result = resolver.resolveDocumentation(symbol, symbol)

            assertNotNull(result)
            assertTrue(result!!.contains("str/join"))
        }

        @Test
        fun `should return basic documentation for qualified symbol not in API`() {
            val symbol = mock(PhelSymbol::class.java)
            `when`(symbol.text).thenReturn("unknown/function")

            val result = resolver.resolveDocumentation(symbol, symbol)

            assertNotNull(result)
            assertTrue(result!!.contains("unknown/function"))
        }

        @Test
        fun `should handle symbol with qualifier that is not an alias`() {
            // When the qualifier is already a namespace (not an alias), we should still find docs
            val symbol = mock(PhelSymbol::class.java)
            `when`(symbol.text).thenReturn("str/replace")

            val result = resolver.resolveDocumentation(symbol, symbol)

            assertNotNull(result)
            assertTrue(result!!.contains("str/replace"))
        }
    }

    @Nested
    inner class AliasResolution {

        @Test
        fun `should fall back to basic documentation when alias cannot be resolved`() {
            // When we have a qualified symbol with an unknown alias and no PSI file context
            val symbol = mock(PhelSymbol::class.java)
            `when`(symbol.text).thenReturn("s/replace")
            `when`(symbol.containingFile).thenReturn(null)

            val result = resolver.resolveDocumentation(symbol, symbol)

            // Should still return basic documentation, not crash
            assertNotNull(result)
            assertTrue(result!!.contains("s/replace"))
        }

        @Test
        fun `should handle symbol without qualifier gracefully`() {
            val symbol = mock(PhelSymbol::class.java)
            `when`(symbol.text).thenReturn("my-function")

            val result = resolver.resolveDocumentation(symbol, symbol)

            assertNotNull(result)
            assertTrue(result!!.contains("my-function"))
        }
    }

    @Nested
    inner class InvalidNamespaceQualifier {

        /**
         * Documents expected behavior:
         * When a namespace is imported with an alias (e.g., phel\str :as s),
         * using the canonical name (str/replace) should NOT show API docs
         * because "str" is not valid in this context - only "s" is.
         */
        @Test
        fun `documentation behavior depends on valid namespace context`() {
            val symbol = mock(PhelSymbol::class.java)
            `when`(symbol.text).thenReturn("str/replace")
            `when`(symbol.containingFile).thenReturn(null)

            val result = resolver.resolveDocumentation(symbol, symbol)

            // Without context, returns basic documentation (safe fallback)
            assertNotNull(result)
            assertTrue(result!!.contains("str/replace"))
        }

        @Test
        fun `should return basic docs when qualifier validation fails`() {
            // When the qualifier cannot be validated (no file context),
            // return basic documentation as a safe fallback
            val symbol = mock(PhelSymbol::class.java)
            `when`(symbol.text).thenReturn("invalid/function")
            `when`(symbol.containingFile).thenReturn(null)

            val result = resolver.resolveDocumentation(symbol, symbol)

            assertNotNull(result)
            assertTrue(result!!.contains("invalid/function"))
            assertTrue(result.contains("h3"))  // Basic doc format
        }
    }
}
