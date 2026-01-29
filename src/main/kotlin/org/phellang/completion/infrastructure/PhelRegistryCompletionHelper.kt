package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import org.phellang.completion.data.Namespace
import org.phellang.completion.data.PhelFunction
import org.phellang.completion.data.PhelFunctionRegistry

object PhelRegistryCompletionHelper {

    @JvmStatic
    fun addCoreFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.CORE, aliasMap)
    }

    @JvmStatic
    fun addStringFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.STRING, aliasMap)
    }

    @JvmStatic
    fun addJsonFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.JSON, aliasMap)
    }

    @JvmStatic
    fun addHttpFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.HTTP, aliasMap)
    }

    @JvmStatic
    fun addHtmlFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.HTML, aliasMap)
    }

    @JvmStatic
    fun addDebugFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.DEBUG, aliasMap)
    }

    @JvmStatic
    fun addTestFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.TEST, aliasMap)
    }

    @JvmStatic
    fun addReplFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.REPL, aliasMap)
    }

    @JvmStatic
    fun addBase64Functions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.BASE64, aliasMap)
    }

    @JvmStatic
    fun addPhpInteropFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.PHP_INTEROP, aliasMap)
    }

    @JvmStatic
    fun addMockFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        addNamespaceFunctions(result, Namespace.MOCK, aliasMap)
    }

    private fun addNamespaceFunctions(
        result: CompletionResultSet,
        namespace: Namespace,
        aliasMap: Map<String, String>
    ) {
        val functions = PhelFunctionRegistry.getFunctions(namespace)
        functions.forEach { function ->
            val displayName = transformNameWithAlias(function, aliasMap)
            PhelCompletionUtils.addRankedCompletion(
                result,
                displayName,
                function.signature,
                function.completion.tailText,
                function.completion.priority
            )
        }
    }

    private fun transformNameWithAlias(function: PhelFunction, aliasMap: Map<String, String>): String {
        // aliasMap is: alias -> namespace (e.g., "s" -> "str")
        // We need reverse lookup: namespace -> alias
        val alias = aliasMap.entries.find { it.value == function.namespace }?.key
        return if (alias != null) {
            val functionName = function.name.substringAfter("/")
            "$alias/$functionName"
        } else {
            function.name
        }
    }
}
