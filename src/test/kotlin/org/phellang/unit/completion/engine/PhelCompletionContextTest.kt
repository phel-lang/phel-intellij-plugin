package org.phellang.unit.completion.engine

import com.intellij.codeInsight.completion.CompletionParameters
import org.junit.jupiter.api.Test
import org.phellang.completion.engine.PhelCompletionContext
import org.junit.jupiter.api.Assertions.*

class PhelCompletionContextTest {

    @Test
    fun `should have proper class structure`() {
        val contextClass = PhelCompletionContext::class.java

        assertEquals("org.phellang.completion.engine", contextClass.packageName)

        assertEquals("PhelCompletionContext", contextClass.simpleName)

        assertTrue(!contextClass.isInterface)
        assertTrue(!java.lang.reflect.Modifier.isAbstract(contextClass.modifiers))
    }

    @Test
    fun `should have expected constructor`() {
        val contextClass = PhelCompletionContext::class.java
        val constructors = contextClass.declaredConstructors

        assertTrue(constructors.isNotEmpty())

        val hasParametersConstructor = constructors.any { constructor ->
            constructor.parameterTypes.any { it == CompletionParameters::class.java }
        }
        assertTrue(hasParametersConstructor, "Should have constructor taking CompletionParameters")
    }

    @Test
    fun `should have expected methods`() {
        val contextClass = PhelCompletionContext::class.java
        val methods = contextClass.declaredMethods.map { it.name }

        assertTrue(methods.contains("shouldSuggestNewForm"), "Should have shouldSuggestNewForm method")
        assertTrue(methods.contains("isInsideParentheses"), "Should have isInsideParentheses method")
        assertTrue(methods.contains("shouldSuppressCompletions"), "Should have shouldSuppressCompletions method")
    }

    @Test
    fun `should have element property`() {
        val contextClass = PhelCompletionContext::class.java
        val fields = contextClass.declaredFields.map { it.name }

        assertTrue(fields.contains("element"), "Should have element field")
    }

    @Test
    fun `should be suitable for completion analysis`() {
        val contextClass = PhelCompletionContext::class.java

        assertTrue(java.lang.reflect.Modifier.isPublic(contextClass.modifiers))

        assertTrue(!java.lang.reflect.Modifier.isAbstract(contextClass.modifiers))

        val publicMethods = contextClass.declaredMethods.filter {
            java.lang.reflect.Modifier.isPublic(it.modifiers)
        }
        assertTrue(publicMethods.isNotEmpty(), "Should have public methods")
    }

    @Test
    fun `should have consistent method signatures`() {
        val contextClass = PhelCompletionContext::class.java
        val methods = contextClass.declaredMethods

        val shouldSuggestNewForm = methods.find { it.name == "shouldSuggestNewForm" }
        val isInsideParentheses = methods.find { it.name == "isInsideParentheses" }
        val shouldSuppressCompletions = methods.find { it.name == "shouldSuppressCompletions" }

        assertNotNull(shouldSuggestNewForm, "shouldSuggestNewForm method should exist")
        assertNotNull(isInsideParentheses, "isInsideParentheses method should exist")
        assertNotNull(shouldSuppressCompletions, "shouldSuppressCompletions method should exist")

        assertEquals(Boolean::class.javaPrimitiveType, shouldSuggestNewForm!!.returnType)
        assertEquals(Boolean::class.javaPrimitiveType, isInsideParentheses!!.returnType)
        assertEquals(Boolean::class.javaPrimitiveType, shouldSuppressCompletions!!.returnType)

        assertEquals(0, shouldSuggestNewForm.parameterCount)
        assertEquals(0, isInsideParentheses.parameterCount)
        assertEquals(0, shouldSuppressCompletions.parameterCount)
    }

    @Test
    fun `should be thread-safe for class loading`() {
        val results = mutableListOf<Class<*>>()
        val threads = mutableListOf<Thread>()

        repeat(5) {
            val thread = Thread {
                val contextClass = PhelCompletionContext::class.java
                results.add(contextClass)
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }

        assertEquals(5, results.size)
        results.forEach {
            assertEquals("PhelCompletionContext", it.simpleName)
        }
    }

    @Test
    fun `should maintain consistent class metadata`() {
        val contextClass = PhelCompletionContext::class.java

        repeat(3) {
            assertEquals("org.phellang.completion.engine", contextClass.packageName)
            assertEquals("PhelCompletionContext", contextClass.simpleName)
            assertTrue(contextClass.declaredMethods.isNotEmpty())
        }
    }

    @Test
    fun `should have proper visibility modifiers`() {
        val contextClass = PhelCompletionContext::class.java

        // Class should be public
        assertTrue(java.lang.reflect.Modifier.isPublic(contextClass.modifiers))

        // Main methods should be public
        val publicMethods = contextClass.declaredMethods.filter {
            java.lang.reflect.Modifier.isPublic(it.modifiers)
        }

        val publicMethodNames = publicMethods.map { it.name }
        assertTrue(publicMethodNames.contains("shouldSuggestNewForm"))
        assertTrue(publicMethodNames.contains("isInsideParentheses"))
        assertTrue(publicMethodNames.contains("shouldSuppressCompletions"))
    }
}
