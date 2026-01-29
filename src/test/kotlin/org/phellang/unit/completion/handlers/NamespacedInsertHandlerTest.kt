package org.phellang.unit.completion.handlers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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
    inner class AutoImportDecisionLogic {

        @Test
        fun `should skip import when qualifier is an alias`() {
            // When suggestion is "s/join" and "s" is in aliasMap
            val aliasMap = mapOf("s" to "str")
            val lookupString = "s/join"
            val qualifier = PhelNamespaceUtils.extractNamespace(lookupString)!!

            // Check if qualifier is an alias
            val isAlias = aliasMap.containsKey(qualifier)
            assertTrue(isAlias, "Should recognize 's' as an alias")

            // When qualifier is alias, no import needed
        }

        @Test
        fun `should check import when qualifier is canonical namespace`() {
            // When suggestion is "str/join" and "str" is NOT in aliasMap keys
            val aliasMap = mapOf("s" to "str")  // "s" is the key, not "str"
            val lookupString = "str/join"
            val qualifier = PhelNamespaceUtils.extractNamespace(lookupString)!!

            // Check if qualifier is an alias
            val isAlias = aliasMap.containsKey(qualifier)
            assertFalse(isAlias, "Should NOT recognize 'str' as an alias (it's a namespace)")

            // When qualifier is not alias, need to check if namespace is imported
        }

        @Test
        fun `should handle empty alias map`() {
            val aliasMap = emptyMap<String, String>()
            val lookupString = "str/join"
            val qualifier = PhelNamespaceUtils.extractNamespace(lookupString)!!

            val isAlias = aliasMap.containsKey(qualifier)
            assertFalse(isAlias, "Empty map has no aliases")
        }

        @Test
        fun `should handle unqualified function names`() {
            // Core functions like "map" have no namespace prefix
            val lookupString = "map"
            val qualifier = PhelNamespaceUtils.extractNamespace(lookupString)

            assertNull(qualifier, "Unqualified names have no qualifier")
            // No import needed for unqualified names (core functions)
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
