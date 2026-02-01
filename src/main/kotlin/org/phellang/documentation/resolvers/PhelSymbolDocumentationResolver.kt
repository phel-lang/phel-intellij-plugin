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

        // For qualified symbols (e.g., "str/replace", "s/replace"), validate the qualifier
        if (qualifier != null) {
            val file = symbol.containingFile as? PhelFile
            if (file != null) {
                val aliasMap = PhelNamespaceUtils.extractAliasMap(file)

                // Check if qualifier is an alias (e.g., "s" -> "str")
                val resolvedNamespace = aliasMap[qualifier]
                if (resolvedNamespace != null) {
                    // Qualifier is a valid alias, resolve to canonical name
                    val functionName =
                        PhelPsiUtils.getName(symbol) ?: return generateBasicDocumentation(symbol, symbolName)
                    val canonicalName = "$resolvedNamespace/$functionName"
                    val aliasedDoc = PhelApiDocumentation.functionDocs[canonicalName]
                    if (aliasedDoc != null) {
                        return aliasedDoc
                    }
                } else {
                    // Qualifier is NOT an alias - check if it's a directly imported namespace
                    if (!isNamespaceDirectlyImported(file, qualifier)) {
                        // Namespace is not valid in this context (e.g., using "str" when imported as "s")
                        return generateBasicDocumentation(symbol, symbolName)
                    }
                    // Namespace is directly imported, proceed with direct lookup
                    val directDoc = PhelApiDocumentation.functionDocs[symbolName]
                    if (directDoc != null) {
                        return directDoc
                    }
                }
            }
        } else {
            // Unqualified symbol (e.g., "map") - direct lookup
            val directDoc = PhelApiDocumentation.functionDocs[symbolName]
            if (directDoc != null) {
                return directDoc
            }
        }

        return generateBasicDocumentation(symbol, symbolName)
    }

    private fun isNamespaceDirectlyImported(file: PhelFile, shortNamespace: String): Boolean {
        // Core namespace is always available
        if (PhelNamespaceUtils.isCoreNamespace(shortNamespace)) {
            return true
        }

        // PHP interop namespace is always available (built-in language feature)
        if (shortNamespace == "php") {
            return true
        }

        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false
        val phelNamespace = PhelNamespaceUtils.toPhelNamespace(shortNamespace)

        // Check if namespace is required AND not aliased
        val aliasMap = PhelNamespaceUtils.extractAliasMap(file)
        val hasAlias = aliasMap.values.contains(shortNamespace)

        // If the namespace has an alias, it's not directly imported
        if (hasAlias) {
            return false
        }

        // Check if it's in the require list
        return PhelNamespaceUtils.isNamespaceRequired(nsDeclaration, phelNamespace)
    }

    private fun generateBasicDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val category = PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return "<h3>$symbolName</h3><br />$category<br /><br />"
    }

    private fun formatAsHtml(content: String): String {
        return "<html><body>$content</body></html>"
    }
}
