package org.phellang.annotator.validators

import org.phellang.completion.data.Namespace
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.completion.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelReferUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

data class ReferSymbolValidationResult(
    val message: String,
    val symbolName: String,
    val namespace: String,
    val isDuplicate: Boolean = false
)

object PhelReferSymbolValidator {

    fun validateReferSymbol(symbol: PhelSymbol): ReferSymbolValidationResult? {
        val symbolName = symbol.text ?: return null

        // Skip symbols that look like namespaces (contain backslash)
        if (symbolName.contains("\\")) {
            return null
        }

        // Check if this symbol is inside a :refer vector
        val referContext = PhelReferUtils.getReferContext(symbol) ?: return null

        val namespace = referContext.namespace
        val shortNamespace = PhelReferUtils.extractShortNamespace(namespace)

        // Check for duplicate first
        if (PhelReferUtils.isDuplicateInReferVector(symbol)) {
            return ReferSymbolValidationResult(
                message = "'$symbolName' is already referred",
                symbolName = symbolName,
                namespace = namespace,
                isDuplicate = true
            )
        }

        // Check if symbol exists in standard library
        if (existsInStandardLibrary(shortNamespace, symbolName)) {
            return null // Valid
        }

        // Check if symbol exists in project
        val containingFile = symbol.containingFile as? PhelFile
        if (containingFile != null && existsInProject(containingFile, shortNamespace, symbolName)) {
            return null // Valid
        }

        // Symbol doesn't exist
        return ReferSymbolValidationResult(
            message = "Cannot resolve '$symbolName' in namespace '$namespace'",
            symbolName = symbolName,
            namespace = namespace
        )
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

    private fun mapToNamespace(shortNamespace: String): Namespace? {
        return when (shortNamespace.lowercase()) {
            "base64" -> Namespace.BASE64
            "core" -> Namespace.CORE
            "debug" -> Namespace.DEBUG
            "html" -> Namespace.HTML
            "http" -> Namespace.HTTP
            "json" -> Namespace.JSON
            "mock" -> Namespace.MOCK
            "repl" -> Namespace.REPL
            "str" -> Namespace.STRING
            "test" -> Namespace.TEST
            else -> null
        }
    }
}
