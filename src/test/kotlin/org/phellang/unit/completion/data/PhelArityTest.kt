package org.phellang.unit.completion.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.completion.data.PhelArity
import org.phellang.completion.data.accepts
import org.phellang.completion.data.describe
import org.phellang.completion.data.selectFor

class PhelArityTest {

    @Test
    fun `parses fixed-arity signature`() {
        val arity = PhelArity.parseSignature("(foo a b)")
        assertNotNull(arity)
        assertEquals(listOf("a", "b"), arity!!.params)
        assertFalse(arity.variadic)
        assertEquals(2, arity.fixedCount)
    }

    @Test
    fun `parses zero-arg signature`() {
        val arity = PhelArity.parseSignature("(foo)")
        assertNotNull(arity)
        assertEquals(emptyList<String>(), arity!!.params)
        assertFalse(arity.variadic)
        assertEquals(0, arity.fixedCount)
    }

    @Test
    fun `parses variadic signature`() {
        val arity = PhelArity.parseSignature("(foo a & rest)")
        assertNotNull(arity)
        assertEquals(listOf("a", "rest"), arity!!.params)
        assertTrue(arity.variadic)
        assertEquals(1, arity.fixedCount)
    }

    @Test
    fun `parses multi-arity newline-separated`() {
        val arities = PhelArity.parseAll("(foo a)\n(foo a b)\n(foo a b & xs)")
        assertEquals(3, arities.size)
        assertFalse(arities[0].variadic)
        assertEquals(1, arities[0].params.size)
        assertTrue(arities[2].variadic)
        assertEquals(2, arities[2].fixedCount)
    }

    @Test
    fun `accepts checks fixed and variadic arities`() {
        val arities = listOf(
            PhelArity(listOf("a", "b"), variadic = false),
            PhelArity(listOf("a", "rest"), variadic = true),
        )
        assertTrue(arities.accepts(1)) // variadic min
        assertTrue(arities.accepts(2))
        assertTrue(arities.accepts(5)) // variadic
        assertFalse(arities.accepts(0))
    }

    @Test
    fun `selectFor returns exact fixed match before variadic`() {
        val arities = listOf(
            PhelArity(listOf("a", "b"), variadic = false),
            PhelArity(listOf("rest"), variadic = true),
        )
        val match = arities.selectFor(2)
        assertNotNull(match)
        assertFalse(match!!.variadic)
        assertEquals(2, match.params.size)
    }

    @Test
    fun `describe summarises arities`() {
        val arities = listOf(
            PhelArity(listOf("a"), variadic = false),
            PhelArity(listOf("a", "rest"), variadic = true),
        )
        assertEquals("1 or 1+", arities.describe())
    }

    @Test
    fun `accepts is permissive on empty arities`() {
        assertTrue(emptyList<PhelArity>().accepts(0))
        assertTrue(emptyList<PhelArity>().accepts(99))
    }
}
