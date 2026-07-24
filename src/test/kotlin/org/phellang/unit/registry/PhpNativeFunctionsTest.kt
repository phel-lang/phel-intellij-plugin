package org.phellang.unit.registry

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.registry.Namespace
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelFunctionRegistry

/**
 * The curated native-PHP seed ([org.phellang.registry.phpNativeFunctions]) as wired into the
 * registry under [Namespace.PHP_NATIVE]. Reads through the public registry so it also proves the
 * hand-wiring (kept outside the GENERATED regions) is in effect.
 */
class PhpNativeFunctionsTest {

    private val functions = PhelFunctionRegistry.getFunctions(Namespace.PHP_NATIVE)

    @Test
    fun `every entry is php-prefixed, PHP_INTEROP priority, with a php_net function link`() {
        assertTrue(functions.isNotEmpty(), "expected native PHP functions to be registered")
        functions.forEach { fn ->
            assertTrue(fn.name.startsWith("php/"), "expected a php/ name, got: ${fn.name}")
            assertEquals(PhelCompletionPriority.PHP_INTEROP, fn.completion.priority, fn.name)
            val docs = fn.documentation.links.docs
            assertTrue(
                docs.startsWith("https://www.php.net/manual/en/function.") && docs.endsWith(".php"),
                "expected a php.net function link for ${fn.name}, got: $docs",
            )
        }
    }

    @Test
    fun `strlen resolves with a signature and its php_net slug`() {
        val strlen = functions.single { it.name == "php/strlen" }
        assertTrue(strlen.signature.startsWith("(php/strlen"), "unexpected signature: ${strlen.signature}")
        assertEquals("https://www.php.net/manual/en/function.strlen.php", strlen.documentation.links.docs)
    }

    @Test
    fun `the generated core set is comprehensive, covering functions absent from the original seed`() {
        assertTrue(functions.size >= 500, "expected the generated core set, got ${functions.size}")
        // str_decrement (PHP 8.3) was not in the hand-authored seed; the doc-en generator covers it.
        assertNotNull(PhelFunctionRegistry.getFunction("php/str_decrement"))
        // mbstring and spl extensions are part of the core set.
        assertNotNull(PhelFunctionRegistry.getFunction("php/mb_strlen"))
        assertNotNull(PhelFunctionRegistry.getFunction("php/iterator_to_array"))
    }

    @Test
    fun `underscores in the name become dashes in the php_net slug`() {
        val arrayMap = functions.single { it.name == "php/array_map" }
        assertEquals("https://www.php.net/manual/en/function.array-map.php", arrayMap.documentation.links.docs)
        assertFalse(arrayMap.documentation.links.docs.contains("array_map"), "the slug must use a dash, not an underscore")
    }

    @Test
    fun `native functions resolve by their full php name`() {
        assertNotNull(PhelFunctionRegistry.getFunction("php/strlen"))
        assertNotNull(PhelFunctionRegistry.getFunction("php/array_map"))
    }
}
