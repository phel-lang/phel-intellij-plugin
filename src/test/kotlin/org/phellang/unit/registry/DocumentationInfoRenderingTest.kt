package org.phellang.unit.registry

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelFunction

/**
 * Rendering of the standard-library documentation popup ([DocumentationInfo.toHtml]).
 *
 * Multi-arity stdlib functions carry every arity in a single newline-separated signature
 * string (see KotlinCodeGenerator); the popup must render each arity on its own line, the
 * same way the project-symbol popup does (PhelDocHtml.projectSymbol).
 */
class DocumentationInfoRenderingTest {

    private fun functionWithSignature(signature: String) = PhelFunction(
        namespace = "trace",
        name = "trace/trace",
        signature = signature,
        completion = CompletionInfo(tailText = "", priority = PhelCompletionPriority.DEBUG_FUNCTIONS),
        documentation = DocumentationInfo(summary = "Prints value and returns it unchanged"),
    )

    @Test
    fun `multi-arity signatures render each arity on its own line`() {
        val html = functionWithSignature("(trace value)\n(trace tag value)").toHtmlDocumentation()

        assertTrue(
            html.contains("<code>(trace value)<br />(trace tag value)</code>"),
            "Expected each arity on its own line, got: $html",
        )
        assertFalse(
            html.contains("\n"),
            "Rendered signature must not leak the raw newline separator, got: $html",
        )
    }

    @Test
    fun `single-arity signature renders unchanged`() {
        val html = functionWithSignature("(inc x)").toHtmlDocumentation()

        assertTrue(
            html.contains("<code>(inc x)</code>"),
            "Single-arity signature should render as-is, got: $html",
        )
    }
}
