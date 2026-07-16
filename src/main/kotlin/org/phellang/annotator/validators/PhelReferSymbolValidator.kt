package org.phellang.annotator.validators

import org.phellang.registry.Namespace
import org.phellang.registry.PhelFunctionRegistry
import org.phellang.registry.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelReferUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

object PhelReferSymbolValidator {
    fun validateReferSymbol(symbol: PhelSymbol): List<PhelValidationProblem> {
        val symbolName = symbol.text ?: return emptyList()

        // Skip symbols that look like namespaces (contain backslash)
        if (symbolName.contains("\\")) {
            return emptyList()
        }

        val referContext = PhelReferUtils.getReferContext(symbol) ?: return emptyList()

        val namespace = referContext.namespace
        val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

        if (PhelReferUtils.isDuplicateInReferVector(symbol)) {
            return listOf(PhelValidationProblem("'$symbolName' is already referred"))
        }

        if (existsInStandardLibrary(shortNamespace, symbolName)) {
            return emptyList() // Valid
        }

        val containingFile = symbol.containingFile as? PhelFile
        if (containingFile != null && existsInProject(containingFile, shortNamespace, symbolName)) {
            return emptyList() // Valid
        }

        return listOf(PhelValidationProblem("Cannot resolve '$symbolName' in namespace '$namespace'"))
    }

    fun isInsideReferVector(symbol: PhelSymbol): Boolean {
        return PhelReferUtils.isInsideReferVector(symbol)
    }

    private fun existsInStandardLibrary(shortNamespace: String, symbolName: String): Boolean {
        val namespace = mapToNamespace(shortNamespace) ?: return false
        val functions = PhelFunctionRegistry.getFunctions(namespace)

        return functions.any { function ->
            val functionName = function.name.substringAfter("/")
            functionName == symbolName
        }
    }

    private fun existsInProject(file: PhelFile, shortNamespace: String, symbolName: String): Boolean {
        val index = PhelProjectSymbolIndex.getInstance(file.project)
        val symbols = index.getSymbolsForNamespace(shortNamespace)

        return symbols.any { it.name == symbolName }
    }

    private fun mapToNamespace(shortNamespace: String): Namespace? =
        Namespace.fromShortName(shortNamespace)
}
