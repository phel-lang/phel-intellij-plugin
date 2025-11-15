package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import org.phellang.completion.data.Namespace
import org.phellang.completion.data.PhelFunctionRegistry

object PhelRegistryCompletionHelper {

    @JvmStatic
    fun addCoreFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.CORE)
    }

    @JvmStatic
    fun addStringFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.STR)
    }

    @JvmStatic
    fun addJsonFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.JSON)
    }

    @JvmStatic
    fun addHttpFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.HTTP)
    }

    @JvmStatic
    fun addHtmlFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.HTML)
    }

    @JvmStatic
    fun addDebugFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.DEBUG)
    }

    @JvmStatic
    fun addTestFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.TEST)
    }

    @JvmStatic
    fun addReplFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.REPL)
    }

    @JvmStatic
    fun addBase64Functions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.BASE64)
    }

    @JvmStatic
    fun addPhpInteropFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.PHP)
    }

    @JvmStatic
    fun addMockFunctions(result: CompletionResultSet) {
        addNamespaceFunctions(result, Namespace.MOCK)
    }

    private fun addNamespaceFunctions(result: CompletionResultSet, namespace: Namespace) {
        val functions = PhelFunctionRegistry.getFunctions(namespace)
        functions.forEach { function ->
            PhelCompletionUtils.addRankedCompletion(
                result,
                function.name,
                function.signature,
                function.description,
                function.priority
            )
        }
    }
}
