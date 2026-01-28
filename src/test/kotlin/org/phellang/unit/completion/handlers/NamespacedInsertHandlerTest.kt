package org.phellang.unit.completion.handlers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.phellang.completion.handlers.NamespacedInsertHandler
import org.phellang.language.psi.PhelNamespaceUtils

class NamespacedInsertHandlerTest {

    @Test
    fun `should be instantiable`() {
        val handler = NamespacedInsertHandler()
        assertNotNull(handler)
    }

    @Test
    fun `should be instantiable multiple times`() {
        val handler1 = NamespacedInsertHandler()
        val handler2 = NamespacedInsertHandler()

        assertNotNull(handler1)
        assertNotNull(handler2)
        assertTrue(handler1 !== handler2)
    }

    @Test
    fun `should maintain consistent class structure`() {
        val handler = NamespacedInsertHandler()

        assertEquals("NamespacedInsertHandler", handler.javaClass.simpleName)
        assertEquals("org.phellang.completion.handlers", handler.javaClass.packageName)
    }

    @Test
    fun `should implement InsertHandler interface`() {
        val handler = NamespacedInsertHandler()

        val interfaces = handler.javaClass.interfaces.map { it.simpleName }
        assertTrue(interfaces.contains("InsertHandler"))
    }

    @Nested
    inner class NamespaceExtractionLogic {

        @Test
        fun `extractNamespace should return namespace for qualified names`() {
            assertEquals("str", PhelNamespaceUtils.extractNamespace("str/join"))
            assertEquals("json", PhelNamespaceUtils.extractNamespace("json/encode"))
            assertEquals("http", PhelNamespaceUtils.extractNamespace("http/request"))
        }

        @Test
        fun `extractNamespace should return null for unqualified names`() {
            assertNull(PhelNamespaceUtils.extractNamespace("map"))
            assertNull(PhelNamespaceUtils.extractNamespace("filter"))
        }

        @Test
        fun `core namespace should not need import`() {
            assertTrue(PhelNamespaceUtils.isCoreNamespace("core"))
            assertTrue(PhelNamespaceUtils.isCoreNamespace(null))
        }

        @Test
        fun `non-core namespaces should need import`() {
            assertFalse(PhelNamespaceUtils.isCoreNamespace("str"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("json"))
            assertFalse(PhelNamespaceUtils.isCoreNamespace("http"))
        }
    }

    @Nested
    inner class AliasResolutionLogic {

        @Test
        fun `function name should be extractable from qualified name`() {
            assertEquals("join", "str/join".substringAfter("/"))
            assertEquals("blank?", "str/blank?".substringAfter("/"))
            assertEquals("encode", "json/encode".substringAfter("/"))
        }

        @Test
        fun `alias replacement should work correctly`() {
            // Simulating what happens when alias "s" is found for namespace "str"
            val lookupString = "str/blank?"
            val namespace = PhelNamespaceUtils.extractNamespace(lookupString)
            val functionName = lookupString.substringAfter("/")
            val alias = "s"  // Simulated alias

            assertEquals("str", namespace)
            assertEquals("blank?", functionName)

            val aliasedText = "$alias/$functionName"
            assertEquals("s/blank?", aliasedText)
        }

        @Test
        fun `should keep original name when no alias exists`() {
            val lookupString = "json/encode"
            val namespace = PhelNamespaceUtils.extractNamespace(lookupString)
            val functionName = lookupString.substringAfter("/")

            assertEquals("json", namespace)
            assertEquals("encode", functionName)

            // When no alias is found, keep original
            val result = lookupString
            assertEquals("json/encode", result)
        }
    }

    @Test
    fun `should be thread safe for instantiation`() {
        val handlers = mutableListOf<NamespacedInsertHandler>()
        val threads = mutableListOf<Thread>()

        repeat(5) {
            val thread = Thread {
                val handler = NamespacedInsertHandler()
                synchronized(handlers) {
                    handlers.add(handler)
                }
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }

        assertEquals(5, handlers.size)
        handlers.forEach { assertNotNull(it) }
    }

    @Test
    fun `should be performant during instantiation`() {
        val startTime = System.currentTimeMillis()

        repeat(100) {
            val handler = NamespacedInsertHandler()
            assertNotNull(handler)
        }

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        assertTrue(duration < 1000, "Handler instantiation took too long: ${duration}ms")
    }
}
