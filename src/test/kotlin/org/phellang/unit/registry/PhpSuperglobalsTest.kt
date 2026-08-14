package org.phellang.unit.registry

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.registry.Namespace
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelFunctionRegistry

/**
 * PHP's superglobals reach the registry, so completion and hover offer `php/$_SERVER` the way
 * Phel's own tooling does since v0.50.0 (#3037).
 */
class PhpSuperglobalsTest {

    private val superglobals = PhelFunctionRegistry.getFunctions(Namespace.PHP_NATIVE)
        .filter { it.name.startsWith("php/$") }

    @Test
    fun `every PHP superglobal is registered`() {
        val expected = setOf(
            "php/\$GLOBALS", "php/\$_SERVER", "php/\$_GET", "php/\$_POST", "php/\$_FILES",
            "php/\$_COOKIE", "php/\$_SESSION", "php/\$_REQUEST", "php/\$_ENV",
        )

        assertEquals(expected, superglobals.map { it.name }.toSet())
    }

    @Test
    fun `a superglobal is reachable by name`() {
        val server = PhelFunctionRegistry.getFunction("php/\$_SERVER")

        assertNotNull(server, "php/\$_SERVER should resolve")
        assertTrue(server!!.documentation.summary.isNotBlank(), "should carry a summary for hover")
    }

    @Test
    fun `superglobals are values, not calls`() {
        superglobals.forEach {
            assertEquals("", it.signature, "${it.name} is a variable and takes no signature")
            assertTrue(!it.isCallOnly, "${it.name} must be usable in value position")
        }
    }

    @Test
    fun `superglobals complete at PHP interop priority`() {
        superglobals.forEach {
            assertEquals(PhelCompletionPriority.PHP_INTEROP, it.completion.priority, it.name)
        }
    }

    @Test
    fun `superglobals do not displace the generated native functions`() {
        val natives = PhelFunctionRegistry.getFunctions(Namespace.PHP_NATIVE)

        assertTrue(natives.size > superglobals.size, "generated natives should still be present")
        assertNotNull(PhelFunctionRegistry.getFunction("php/array_map"), "php/array_map should still resolve")
    }
}
