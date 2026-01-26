package org.phellang.documentation.resolvers

import com.intellij.psi.PsiElement
import org.phellang.completion.documentation.PhelApiDocumentation
import org.phellang.completion.documentation.PhelBasicDocumentation
import org.phellang.core.psi.PhelPsiUtils
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelNamespaceUtils
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
            else -> resolveApiDocumentation(symbol, symbolName)
        }

        return formatAsHtml(content)
    }

    private fun isLocalSymbol(symbol: PhelSymbol): Boolean {
        return PhelSymbolAnalyzer.isParameterReference(symbol) ||
                PhelSymbolAnalyzer.isFunctionParameter(symbol) ||
                PhelSymbolAnalyzer.isLetBinding(symbol)
    }

    private fun generateLocalSymbolDoc(symbol: PhelSymbol, symbolName: String): String {
        val category = PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return "<h3>$symbolName</h3><br />$category<br /><br />"
    }

    private fun resolveApiDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val directDoc = PhelApiDocumentation.functionDocs[symbolName]
        if (directDoc != null) {
            return directDoc
        }

        // If the symbol has a qualifier (e.g., "s/replace"), try to resolve the alias
        val qualifier = PhelPsiUtils.getQualifier(symbol)
        if (qualifier != null) {
            val resolvedName = resolveAliasedSymbolName(symbol, qualifier)
            if (resolvedName != null && resolvedName != symbolName) {
                val aliasedDoc = PhelApiDocumentation.functionDocs[resolvedName]
                if (aliasedDoc != null) {
                    return aliasedDoc
                }
            }
        }

        return generateBasicDocumentation(symbol, symbolName)
    }

    /**
     * Resolves an aliased symbol name to its canonical form.
     * e.g., "s/replace" -> "str/replace" when file has (:require phel\str :as s)
     */
    private fun resolveAliasedSymbolName(symbol: PhelSymbol, qualifier: String): String? {
        val file = symbol.containingFile as? PhelFile ?: return null
        val aliasMap = PhelNamespaceUtils.extractAliasMap(file)

        // Check if the qualifier is an alias
        val resolvedNamespace = aliasMap[qualifier] ?: return null

        // Get the function name part (after the slash)
        val functionName = PhelPsiUtils.getName(symbol) ?: return null

        // Construct the resolved symbol name
        return "$resolvedNamespace/$functionName"
    }

    private fun generateBasicDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val category = PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return "<h3>$symbolName</h3><br />$category<br /><br />"
    }

    private fun formatAsHtml(content: String): String {
        return "<html><body>$content</body></html>"
    }
}
