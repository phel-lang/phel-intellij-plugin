package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import org.phellang.registry.Namespace
import org.phellang.registry.PhelFunction
import org.phellang.registry.PhelFunctionRegistry

object PhelRegistryCompletionHelper {
    /**
     * Offers every namespace the registry knows. Iterating `Namespace.entries` (generated from
     * `api.json`) rather than a hand-maintained list keeps a namespace added to `NamespaceConfig`
     * from being loaded but never suggested.
     */
    @JvmStatic
    fun addStandardLibraryFunctions(result: CompletionResultSet, aliasMap: Map<String, String> = emptyMap()) {
        Namespace.entries.forEach { namespace ->
            addNamespaceFunctions(result, namespace, aliasMap)
        }
    }

    private fun addNamespaceFunctions(
        result: CompletionResultSet, namespace: Namespace, aliasMap: Map<String, String>
    ) {
        val functions = PhelFunctionRegistry.getFunctions(namespace)
        functions.forEach { function ->
            val displayName = transformNameWithAlias(function, aliasMap)
            PhelCompletionUtils.addRankedCompletion(
                result, displayName, function.signature, function.completion.tailText, function.completion.priority
            )
        }
    }

    /** Renders `str/join` as `s/join` when the file aliased `str` to `s` via `(:require ... :as s)`. */
    internal fun transformNameWithAlias(function: PhelFunction, aliasMap: Map<String, String>): String {
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
