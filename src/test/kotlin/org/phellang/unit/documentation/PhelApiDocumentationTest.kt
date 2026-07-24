package org.phellang.unit.documentation

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.documentation.resolvers.PhelApiDocumentation
import org.phellang.registry.Namespace
import org.phellang.registry.PhelFunctionRegistry

/**
 * [PhelApiDocumentation] assembles the standard-library hover HTML. Native PHP functions get a
 * php.net link appended; Phel functions do not (their dormant links stay unrendered).
 */
class PhelApiDocumentationTest {

    @Test
    fun `native PHP function documentation links out to php_net`() {
        val html = PhelApiDocumentation.getDocumentation("php/strlen")

        assertNotNull(html, "expected documentation for a registered native PHP function")
        assertTrue(html!!.contains("php.net documentation"), "expected a php.net link, got: $html")
        assertTrue(
            html.contains("https://www.php.net/manual/en/function.strlen.php"),
            "expected the strlen php.net URL, got: $html",
        )
    }

    @Test
    fun `phel stdlib function documentation does not link out to php_net`() {
        val phelFunction = PhelFunctionRegistry.getFunctions(Namespace.CORE).first()
        val html = PhelApiDocumentation.getDocumentation(phelFunction.name)

        assertNotNull(html, "expected documentation for a core function")
        assertFalse(html!!.contains("php.net"), "a non-php function must not link php.net, got: $html")
    }

    @Test
    fun `unknown function has no documentation`() {
        assertNull(PhelApiDocumentation.getDocumentation("php/not-a-real-function-xyz"))
    }
}
