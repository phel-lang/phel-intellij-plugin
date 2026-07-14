package org.phellang.documentation.resolvers

import org.phellang.registry.PhelProjectSymbol

/**
 * Renders the hover-popup HTML. Kept apart from [PhelSymbolDocumentationResolver], which decides
 * *which* documentation applies; this only knows how to present it.
 */
internal object PhelDocHtml {

    /** Wraps rendered [content] as a complete popup document. */
    fun page(content: String): String = "<html><body>$content</body></html>"

    /** A local binding or parameter: just its name and the analyzer's one-line category. */
    fun localSymbol(name: String, category: String): String =
        "<h3>$name</h3><br />$category<br /><br />"

    /** A symbol with no registry/project entry: its name and whatever description we could find. */
    fun basic(name: String, description: String): String =
        "<h3>$name</h3><br />$description<br /><br />"

    /** A project-defined symbol: qualified name, signature, docstring and where it lives. */
    fun projectSymbol(symbol: PhelProjectSymbol): String = buildString {
        append("<h3>${symbol.qualifiedName}</h3><br />")

        // Multi-arity signatures are newline-separated; render each on its own line.
        append("<code>${symbol.signature.replace("\n", "<br />")}</code><br /><br />")

        if (!symbol.docstring.isNullOrBlank()) {
            append(symbol.docstring)
            append("<br />")
        }

        append("<br /><i>${symbol.type.keyword} in ${symbol.namespace}</i><br /><br />")
    }
}
