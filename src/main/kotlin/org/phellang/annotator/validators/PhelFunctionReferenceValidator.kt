package org.phellang.annotator.validators

import org.phellang.registry.PhelFunctionRegistry
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

object PhelFunctionReferenceValidator {
    fun validateFunctionReference(symbol: PhelSymbol): List<PhelValidationProblem> {
        val text = symbol.text ?: return emptyList()
        if (!text.contains("/")) return emptyList()

        // php/ interop resolves to PHP, whose functions this validator cannot check.
        val qualifier = PhelPsiUtils.getQualifier(symbol)?.takeIf { it != "php" } ?: return emptyList()
        val functionName = PhelPsiUtils.getName(symbol) ?: return emptyList()

        val file = symbol.containingFile as? PhelFile ?: return emptyList()
        if (isPhpClassInterop(text, file)) return emptyList()

        val resolvedNamespace = PhelNamespaceUtils.extractAliasMap(file)[qualifier] ?: qualifier
        if (resolves(symbol, resolvedNamespace, functionName)) return emptyList()

        // An unknown namespace is the namespace validator's problem, not this one's.
        if (!isNamespaceKnown(symbol, qualifier, resolvedNamespace)) return emptyList()

        // The namespace is real, so the function name itself is wrong — and only the user can say
        // what they meant, which is why no fix is offered.
        return listOf(PhelValidationProblem("Cannot resolve function '$functionName' in namespace '$qualifier'"))
    }

    /**
     * `DateTime/createFromFormat` and `\Foo\Bar/CONST` look namespaced, but the qualifier is a PHP
     * class rather than a Phel namespace, so the regular function lookup cannot validate them.
     */
    private fun isPhpClassInterop(text: String, file: PhelFile): Boolean =
        PhelInteropShorthands.isInteropShorthand(text, PhelNamespaceUtils.extractUsedClasses(file))

    private fun resolves(symbol: PhelSymbol, namespace: String, functionName: String): Boolean =
        existsInStandardLibrary(namespace, functionName) ||
                existsInProjectSymbols(symbol, namespace, functionName)

    private fun existsInStandardLibrary(namespace: String, functionName: String): Boolean {
        val canonicalName = "$namespace/$functionName"
        if (PhelFunctionRegistry.getFunction(canonicalName) != null) {
            return true
        }

        val fullNamespace = PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(namespace)
        if (fullNamespace != null) {
            val fullName = "$fullNamespace/$functionName"
            if (PhelFunctionRegistry.getFunction(fullName) != null) {
                return true
            }
        }

        return false
    }

    private fun existsInProjectSymbols(symbol: PhelSymbol, namespace: String, functionName: String): Boolean {
        val project = symbol.project
        val index = PhelProjectSymbolIndex.getInstance(project)

        val projectSymbol = index.findSymbol(namespace, functionName)
        return projectSymbol != null
    }

    private fun isNamespaceKnown(symbol: PhelSymbol, qualifier: String, resolvedNamespace: String): Boolean {
        if (PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(resolvedNamespace) != null) {
            return true
        }

        val projectNamespace = PhelProjectNamespaceFinder.findByShortName(symbol.project, resolvedNamespace)
        if (projectNamespace != null) {
            return true
        }

        if (qualifier != resolvedNamespace) {
            val qualifierNamespace = PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)
            if (qualifierNamespace != null) {
                return true
            }
        }

        return false
    }
}
