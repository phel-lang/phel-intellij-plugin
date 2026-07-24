package org.phellang.unit.registry

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.registry.CompletionInfo
import org.phellang.registry.DeprecationInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelFunction

/**
 * Rendering of the standard-library documentation popup ([DocumentationInfo.toHtml]).
 *
 * Multi-arity stdlib functions carry every arity in a single newline-separated signature
 * string (see KotlinCodeGenerator); the popup must render each arity on its own line, the
 * same way the project-symbol popup does (PhelDocHtml.projectSymbol). Deprecated functions
 * must also surface a notice consistent with the deprecated-function inspection.
 */
class DocumentationInfoRenderingTest {

    private fun functionWithSignature(signature: String, deprecation: DeprecationInfo? = null) = PhelFunction(
        namespace = "trace",
        name = "trace/trace",
        signature = signature,
        completion = CompletionInfo(tailText = "", priority = PhelCompletionPriority.DEBUG_FUNCTIONS),
        documentation = DocumentationInfo(summary = "Prints value and returns it unchanged", deprecation = deprecation),
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

    @Test
    fun `deprecation with a version and replacement renders a notice`() {
        val html = functionWithSignature(
            "(push xs x)",
            DeprecationInfo(version = "0.25.0", replacement = "conj"),
        ).toHtmlDocumentation()

        assertTrue(html.contains("Deprecated"), "Expected a deprecation notice, got: $html")
        assertTrue(html.contains("0.25.0"), "Expected the deprecation version, got: $html")
        assertTrue(html.contains("<code>conj</code>"), "Expected the replacement, got: $html")
    }

    @Test
    fun `deprecation whose version carries a Use message renders the formatted replacement`() {
        val html = functionWithSignature(
            "(str-contains? s x)",
            DeprecationInfo(version = "Use phel\\string\\contains?"),
        ).toHtmlDocumentation()

        assertTrue(html.contains("string/contains?"), "Expected the formatted replacement, got: $html")
        assertFalse(html.contains("phel\\string\\contains?"), "Replacement should be display-formatted, got: $html")
        assertFalse(html.contains("since"), "A Use-message deprecation carries no version, got: $html")
    }

    @Test
    fun `non-deprecated function renders no deprecation notice`() {
        val html = functionWithSignature("(inc x)").toHtmlDocumentation()

        assertFalse(html.contains("Deprecated"), "A healthy function must not show a deprecation notice, got: $html")
    }
}
