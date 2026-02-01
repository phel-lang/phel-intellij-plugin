package org.phellang.documentation.resolvers

import com.intellij.psi.PsiElement
import org.phellang.completion.data.PhelProjectSymbol
import org.phellang.completion.documentation.PhelApiDocumentation
import org.phellang.completion.documentation.PhelBasicDocumentation
import org.phellang.completion.indexing.PhelProjectSymbolIndex
import org.phellang.core.psi.PhelPsiUtils
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelReferUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

class PhelSymbolDocumentationResolver {

    fun resolveDocumentation(element: PsiElement?, originalElement: PsiElement?): String? {
        val symbol = PhelPsiUtils.findTopmostSymbol(originalElement)
            ?: PhelPsiUtils.findTopmostSymbol(element)
            ?: return null

        val symbolName = symbol.text
        if (symbolName.isNullOrEmpty()) {
            return null
        }

        val content = when {
            isLocalSymbol(symbol) -> generateLocalSymbolDoc(symbol, symbolName)
            isInsideReferVector(symbol) -> resolveReferSymbolDocumentation(symbol, symbolName)
            else -> resolveApiDocumentation(symbol, symbolName)
        }

        return formatAsHtml(content)
    }

    private fun isInsideReferVector(symbol: PhelSymbol): Boolean {
        val symbolName = symbol.text ?: return false

        // Symbols in :refer vectors don't have backslashes (they're simple names)
        if (symbolName.contains("\\")) {
            return false
        }

        return PhelReferUtils.isInsideReferVector(symbol)
    }

    private fun resolveReferSymbolDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val namespace = PhelReferUtils.getReferNamespace(symbol) ?: return generateBasicDocumentation(symbol, symbolName)
        val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

        // Try API documentation (standard library)
        val canonicalName = "$shortNamespace/$symbolName"
        val apiDoc = PhelApiDocumentation.functionDocs[canonicalName]
        if (apiDoc != null) return apiDoc

        // Try project symbols
        val projectDoc = resolveProjectSymbolDocumentation(symbol, shortNamespace, symbolName)
        if (projectDoc != null) return projectDoc

        return generateBasicDocumentation(symbol, symbolName)
    }

    private fun isLocalSymbol(symbol: PhelSymbol): Boolean {
        return PhelSymbolAnalyzer.isParameterReference(symbol)
                || PhelSymbolAnalyzer.isFunctionParameter(symbol)
                || PhelSymbolAnalyzer.isLetBinding(symbol)
    }

    private fun generateLocalSymbolDoc(symbol: PhelSymbol, symbolName: String): String {
        val category = PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return "<h3>$symbolName</h3><br />$category<br /><br />"
    }

    private fun resolveApiDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val qualifier = PhelPsiUtils.getQualifier(symbol)
        val functionName = PhelPsiUtils.getName(symbol)

        // For qualified symbols (e.g., "str/replace", "s/replace", "math/is-positive")
        if (qualifier != null && functionName != null) {
            val file = symbol.containingFile as? PhelFile ?: return generateBasicDocumentation(symbol, symbolName)
            val aliasMap = PhelNamespaceUtils.extractAliasMap(file)

            // Check if qualifier is an alias (e.g., "s" -> "str")
            val resolvedNamespace = aliasMap[qualifier] ?: qualifier

            // Try API documentation first (standard library)
            val canonicalName = "$resolvedNamespace/$functionName"
            val apiDoc = PhelApiDocumentation.functionDocs[canonicalName]
            if (apiDoc != null) return apiDoc

            // Also try with original qualifier for direct lookups
            val directDoc = PhelApiDocumentation.functionDocs[symbolName]
            if (directDoc != null) return directDoc

            // Try project symbols - use the resolved namespace (alias -> actual) or qualifier directly
            val projectDoc = resolveProjectSymbolDocumentation(symbol, resolvedNamespace, functionName)
            if (projectDoc != null) return projectDoc
        } else {
            // Unqualified symbol (e.g., "map") - direct lookup
            val directDoc = PhelApiDocumentation.functionDocs[symbolName]
            if (directDoc != null) return directDoc
        }

        return generateBasicDocumentation(symbol, symbolName)
    }

    private fun resolveProjectSymbolDocumentation(
        symbol: PhelSymbol, shortNamespace: String, functionName: String
    ): String? {
        val project = symbol.project
        val index = PhelProjectSymbolIndex.getInstance(project)
        val projectSymbol = index.findSymbol(shortNamespace, functionName) ?: return null

        return generateProjectSymbolDocumentation(projectSymbol)
    }

    private fun generateProjectSymbolDocumentation(symbol: PhelProjectSymbol): String {
        return buildString {
            // Title (qualified name)
            append("<h3>${symbol.qualifiedName}</h3>")
            append("<br />")

            // Signature (convert newlines to <br> for multi-arity functions)
            val signatureHtml = symbol.signature.replace("\n", "<br />")
            append("<code>$signatureHtml</code><br /><br />")

            // Description (docstring) if available
            if (!symbol.docstring.isNullOrBlank()) {
                append(symbol.docstring)
                append("<br />")
            }

            // File location as subtle info
            append("<br />")
            append("<i>${symbol.type.keyword} in ${symbol.namespace}</i>")
            append("<br /><br />")
        }
    }

    private fun generateBasicDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val category = PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return "<h3>$symbolName</h3><br />$category<br /><br />"
    }

    private fun formatAsHtml(content: String): String {
        return "<html><body>$content</body></html>"
    }
}
