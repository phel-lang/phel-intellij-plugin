package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

/**
 * Based on official Phel API documentation: https://phel-lang.org/documentation/api/
 */
object PhelFunctionRegistry {

    private val functions = mutableMapOf<Namespace, List<PhelFunction>>()

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
}
