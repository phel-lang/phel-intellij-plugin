package org.phellang.documentation.resolvers

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.documentation.PhelApiDocumentation
import org.phellang.completion.documentation.PhelBasicDocumentation
import org.phellang.registry.indexing.PhelProjectSymbolIndex
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.utils.PhelPsiUtils
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

        return PhelDocHtml.page(content)
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
        val apiDoc = PhelApiDocumentation.getDocumentation(canonicalName)
        if (apiDoc != null) return apiDoc

        // Try project symbols
        val projectDoc = resolveProjectSymbolDocumentation(symbol, shortNamespace, symbolName)
        if (projectDoc != null) return projectDoc

        return generateBasicDocumentation(symbol, symbolName)
    }

    private fun isLocalSymbol(symbol: PhelSymbol): Boolean {
        return PhelSymbolAnalyzer.isLocalBindingOrReference(symbol)
    }

    private fun generateLocalSymbolDoc(symbol: PhelSymbol, symbolName: String): String =
        PhelDocHtml.localSymbol(symbolName, PhelBasicDocumentation.generateBasicDocForElement(symbol))

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
            val apiDoc = PhelApiDocumentation.getDocumentation(canonicalName)
            if (apiDoc != null) return apiDoc

            // Also try with original qualifier for direct lookups
            val directDoc = PhelApiDocumentation.getDocumentation(symbolName)
            if (directDoc != null) return directDoc

            // Try project symbols - use the resolved namespace (alias -> actual) or qualifier directly
            val projectDoc = resolveProjectSymbolDocumentation(symbol, resolvedNamespace, functionName)
            if (projectDoc != null) return projectDoc
        } else {
            // Unqualified symbol (e.g., "map") - direct API lookup first
            val directDoc = PhelApiDocumentation.getDocumentation(symbolName)
            if (directDoc != null) return directDoc

            // Then try resolving via a `:refer` clause: if the file imports this name
            // from another namespace, look it up by (sourceNamespace, name).
            val file = symbol.containingFile as? PhelFile
            if (file != null) {
                val sourceNamespace = PhelNamespaceUtils.findReferSource(file, symbolName)
                if (sourceNamespace != null) {
                    val shortNamespace = PhelReferUtils.extractShortNamespace(sourceNamespace)
                    val canonicalName = "$shortNamespace/$symbolName"
                    PhelApiDocumentation.getDocumentation(canonicalName)?.let { return it }
                    val projectDoc = resolveProjectSymbolDocumentation(symbol, shortNamespace, symbolName)
                    if (projectDoc != null) return projectDoc
                }
            }
        }

        return generateBasicDocumentation(symbol, symbolName)
    }

    private fun resolveProjectSymbolDocumentation(
        symbol: PhelSymbol, shortNamespace: String, functionName: String
    ): String? {
        val project = symbol.project
        val index = PhelProjectSymbolIndex.getInstance(project)
        val projectSymbol = index.findSymbol(shortNamespace, functionName) ?: return null

        return PhelDocHtml.projectSymbol(projectSymbol)
    }

    private fun generateBasicDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val docstring = extractDefinitionDocstring(symbol)?.takeIf { it.isNotBlank() }
        val description = docstring ?: PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return PhelDocHtml.basic(symbolName, description)
    }

    /**
     * Returns the first top-level string literal that appears after the defining keyword and
     * the defined name — skipping attribute maps and parameter vectors. Phel allows the
     * docstring to be placed either right after the name `(defn name "doc" [x] body)` or
     * later in the body `(defn name {:meta} [x] "doc" body)`.
     */
    private fun extractDefinitionDocstring(symbol: PhelSymbol): String? {
        val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return null
        val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java) ?: return null
        if (forms.size < 3) return null

        // Only treat the symbol as a definition site when it sits at the name position.
        val nameSymbol = PhelPsiUtils.asSymbol(forms[1])
        if (nameSymbol !== symbol) return null

        for (i in 2 until forms.size) {
            directStringLiteralText(forms[i])?.let { return it }
        }
        return null
    }

    private fun directStringLiteralText(form: PhelForm): String? {
        val literal = (form as? PhelLiteral)
            ?: form.children.firstOrNull { it is PhelLiteral } as? PhelLiteral
            ?: return null
        val raw = literal.text ?: return null
        if (raw.length < 2 || !raw.startsWith("\"") || !raw.endsWith("\"")) return null
        return raw.substring(1, raw.length - 1)
    }

}
