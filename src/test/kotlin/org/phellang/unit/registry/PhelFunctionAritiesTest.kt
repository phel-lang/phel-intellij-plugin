package org.phellang.unit.registry

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.PhelArity
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelFunction

class PhelFunctionAritiesTest {

    private fun function(signature: String) = PhelFunction(
        namespace = "core",
        name = "get",
        signature = signature,
        completion = CompletionInfo(tailText = "", priority = PhelCompletionPriority.CORE_FUNCTIONS),
        documentation = DocumentationInfo(summary = ""),
    )

    @Test
    fun `arities are the parsed signature, one per line`() {
        val signature = "(get ds k)\n(get ds k opt)"

        val arities = function(signature).arities

        assertEquals(PhelArity.parseAll(signature), arities)
        assertEquals(2, arities.size)
    }

    @Test
    fun `arities are parsed once and kept`() {
        val get = function("(get ds k)")

        assertSame(get.arities, get.arities)
    }

    @Test
    fun `a signature with no call shape has no arities`() {
        assertTrue(function("").arities.isEmpty())
    }
}
