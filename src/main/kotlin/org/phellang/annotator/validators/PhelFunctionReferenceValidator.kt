package org.phellang.annotator.validators

import org.phellang.completion.documentation.PhelApiDocumentation
import org.phellang.completion.indexing.PhelProjectSymbolIndex
import org.phellang.core.psi.PhelPsiUtils
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

data class FunctionReferenceValidationResult(
    val message: String,
    val namespace: String,
    val functionName: String
)

object PhelFunctionReferenceValidator {

    fun validateFunctionReference(symbol: PhelSymbol): FunctionReferenceValidationResult? {
        val text = symbol.text ?: return null

        // Only validate namespace-qualified symbols
        if (!text.contains("/")) {
            return null
        }

        val qualifier = PhelPsiUtils.getQualifier(symbol) ?: return null
        val functionName = PhelPsiUtils.getName(symbol) ?: return null

        // Skip php/ interop - can't validate PHP functions
        if (qualifier == "php") {
            return null
        }

        val containingFile = symbol.containingFile as? PhelFile ?: return null

        // Get alias map to resolve the actual namespace
        val aliasMap = PhelNamespaceUtils.extractAliasMap(containingFile)
        val resolvedNamespace = aliasMap[qualifier] ?: qualifier

        // Check if function exists in standard library
        if (existsInStandardLibrary(resolvedNamespace, functionName)) {
            return null // Valid - exists in API
        }

        // Check if function exists in project symbols
        if (existsInProjectSymbols(symbol, resolvedNamespace, functionName)) {
            return null // Valid - exists in project
        }

        // Check if the namespace even exists before reporting function not found
        if (!isNamespaceKnown(symbol, qualifier, resolvedNamespace)) {
            return null // Namespace itself is unknown - let namespace validator handle it
        }

        // Function doesn't exist in a known namespace
        return FunctionReferenceValidationResult(
            message = "Cannot resolve function '$functionName' in namespace '$qualifier'",
            namespace = qualifier,
            functionName = functionName
        )
    }

    private fun existsInStandardLibrary(namespace: String, functionName: String): Boolean {
        // Try canonical name format: namespace/function
        val canonicalName = "$namespace/$functionName"
        if (PhelApiDocumentation.functionDocs.containsKey(canonicalName)) {
            return true
        }

        // Also check if it's a known standard library namespace
        val fullNamespace = PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(namespace)
        if (fullNamespace != null) {
            // Standard library namespace - check with full namespace too
            val fullName = "$fullNamespace/$functionName"
            if (PhelApiDocumentation.functionDocs.containsKey(fullName)) {
                return true
            }
        }

        return false
    }

    private fun existsInProjectSymbols(symbol: PhelSymbol, namespace: String, functionName: String): Boolean {
        val project = symbol.project
        val index = PhelProjectSymbolIndex.getInstance(project)

        // Try to find the symbol in the project index
        val projectSymbol = index.findSymbol(namespace, functionName)
        return projectSymbol != null
    }

    private fun isNamespaceKnown(symbol: PhelSymbol, qualifier: String, resolvedNamespace: String): Boolean {
        // Check standard library
        if (PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(resolvedNamespace) != null) {
            return true
        }

        // Check project namespaces
        val projectNamespace = PhelProjectNamespaceFinder.findByShortName(symbol.project, resolvedNamespace)
        if (projectNamespace != null) {
            return true
        }

        // Also check with the original qualifier (in case it's an alias)
        if (qualifier != resolvedNamespace) {
            val qualifierNamespace = PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)
            if (qualifierNamespace != null) {
                return true
            }
        }

        return false
    }
}
