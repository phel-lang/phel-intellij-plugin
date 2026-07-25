package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import org.phellang.completion.engine.context.PhelCallPosition
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.registry.Namespace
import org.phellang.registry.PhelCompletionPriority
import org.phellang.registry.PhelFunction
import org.phellang.registry.PhelFunctionRegistry

object PhelRegistryCompletionHelper {
    /**
     * Offers every namespace the registry knows. Iterating `Namespace.entries` (generated from
     * `api.json`) rather than a hand-maintained list keeps a namespace added to `NamespaceConfig`
     * from being loaded but never suggested.
     */
    @JvmStatic
    @JvmOverloads
    fun addStandardLibraryFunctions(
        result: CompletionResultSet,
        aliasMap: Map<String, String> = emptyMap(),
        position: PhelCallPosition = PhelCallPosition.OTHER,
    ) {
        Namespace.entries.forEach { namespace ->
            addNamespaceFunctions(result, namespace, aliasMap, position)
        }
    }

    private fun addNamespaceFunctions(
        result: CompletionResultSet,
        namespace: Namespace,
        aliasMap: Map<String, String>,
        position: PhelCallPosition,
    ) {
        val functions = PhelFunctionRegistry.getFunctions(namespace)
        functions.forEach { function ->
            // A macro or special form cannot be an argument, so offering one there is always wrong.
            if (position == PhelCallPosition.PLAIN_CALL_ARGUMENT && function.isCallOnly) return@forEach

            val displayName = transformNameWithAlias(function, aliasMap)
            PhelCompletionUtils.addRankedCompletion(
                result, displayName, function.signature, function.completion.tailText, priorityFor(function, position)
            )
        }
    }

    /**
     * Definition forms sink in head position.
     *
     * They rank above every function by default, which is right at top level — where the templates
     * offer them anyway — but inside a body you are calling something, and `def`, `defstruct` and
     * `definterface` were burying `map`, `str` and `println` at the top of the list. Nesting a
     * definition inside a body is legal, so they are demoted rather than removed.
     */
    private fun priorityFor(function: PhelFunction, position: PhelCallPosition): PhelCompletionPriority {
        if (position != PhelCallPosition.HEAD) return function.completion.priority
        if (function.name.substringAfter("/") !in PhelSpecialForms.NAME_DECLARING) return function.completion.priority

        return PhelCompletionPriority.PROJECT_SYMBOLS
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
