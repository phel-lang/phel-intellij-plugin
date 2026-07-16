package org.phellang.annotator.validators

import org.phellang.completion.documentation.PhelApiDocumentation
import org.phellang.registry.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

object PhelFunctionReferenceValidator {
    fun validateFunctionReference(symbol: PhelSymbol): List<PhelValidationProblem> {
        val text = symbol.text ?: return emptyList()

        if (!text.contains("/")) {
            return emptyList()
        }

        val qualifier = PhelPsiUtils.getQualifier(symbol) ?: return emptyList()
        val functionName = PhelPsiUtils.getName(symbol) ?: return emptyList()

        // Skip php/ interop - can't validate PHP functions
        if (qualifier == "php") {
            return emptyList()
        }

        val containingFile = symbol.containingFile as? PhelFile ?: return emptyList()

        // Skip PHP-class interop shorthands (e.g. `DateTime/createFromFormat`,
        // `\Foo\Bar/CONST`). The qualifier is a PHP class, not a Phel namespace,
        // so the regular function lookup can't validate it.
        val usedClasses = PhelNamespaceUtils.extractUsedClasses(containingFile)
        if (PhelInteropShorthands.isInteropShorthand(text, usedClasses)) {
            return emptyList()
        }

        val aliasMap = PhelNamespaceUtils.extractAliasMap(containingFile)
        val resolvedNamespace = aliasMap[qualifier] ?: qualifier

        if (existsInStandardLibrary(resolvedNamespace, functionName)) {
            return emptyList() // Valid - exists in API
        }

        if (existsInProjectSymbols(symbol, resolvedNamespace, functionName)) {
            return emptyList() // Valid - exists in project
        }

        // Check if the namespace even exists before reporting function not found
        if (!isNamespaceKnown(symbol, qualifier, resolvedNamespace)) {
            return emptyList() // Namespace itself is unknown - let namespace validator handle it
        }

        // Function doesn't exist in a known namespace. No fix to offer: we know the namespace is
        // real, so the function name itself is wrong and only the user can say what they meant.
        return listOf(PhelValidationProblem("Cannot resolve function '$functionName' in namespace '$qualifier'"))
    }

    private fun existsInStandardLibrary(namespace: String, functionName: String): Boolean {
        val canonicalName = "$namespace/$functionName"
        if (PhelApiDocumentation.hasDocumentation(canonicalName)) {
            return true
        }

        val fullNamespace = PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(namespace)
        if (fullNamespace != null) {
            val fullName = "$fullNamespace/$functionName"
            if (PhelApiDocumentation.hasDocumentation(fullName)) {
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
