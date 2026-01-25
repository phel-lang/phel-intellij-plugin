package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

/**
 * Based on official Phel API documentation: https://phel-lang.org/documentation/api/
 */
object PhelFunctionRegistry {

    private val functions = mutableMapOf<Namespace, List<PhelFunction>>()
    
    // Cache of deprecated function names for fast lookup
    private val deprecatedFunctionNames: Set<String> by lazy {
        functions.values.flatten()
            .filter { it.isDeprecated }
            .flatMap { func ->
                // Store both full name and short name for flexible lookup
                val shortName = func.name.substringAfter("/")
                if (shortName != func.name) {
                    listOf(func.name, shortName)
                } else {
                    listOf(func.name)
                }
            }
            .toSet()
    }

    init {
        functions[Namespace.BASE64] = registerBase64Functions()
        functions[Namespace.CORE] = registerCoreFunctions()
        functions[Namespace.DEBUG] = registerDebugFunctions()
        functions[Namespace.HTML] = registerHtmlFunctions()
        functions[Namespace.HTTP] = registerHttpFunctions()
        functions[Namespace.JSON] = registerJsonFunctions()
        functions[Namespace.MOCK] = registerMockFunctions()
        functions[Namespace.PHP_INTEROP] = registerPhpInteropFunctions()
        functions[Namespace.REPL] = registerReplFunctions()
        functions[Namespace.STRING] = registerStringFunctions()
        functions[Namespace.TEST] = registerTestFunctions()
    }

    fun getFunctions(namespace: Namespace): List<PhelFunction> {
        return functions[namespace] ?: emptyList()
    }

    fun getFunctions(priority: PhelCompletionPriority): List<PhelFunction> {
        return functions.values.flatten().filter { it.completion.priority == priority }
    }

    fun getFunction(name: String): PhelFunction? {
        return functions.values.flatten().find { it.name == name }
    }

    fun getAllFunctions(): List<PhelFunction> {
        return functions.values.flatten()
    }

    fun isDeprecated(functionName: String): Boolean {
        // Check exact match first
        if (functionName in deprecatedFunctionNames) {
            return true
        }
        
        // If the input has a namespace prefix (e.g., "core/put"), try short name
        val shortName = functionName.substringAfter("/")
        return shortName in deprecatedFunctionNames
    }
}
