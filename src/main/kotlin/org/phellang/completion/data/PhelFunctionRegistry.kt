package org.phellang.completion.data

import org.phellang.completion.infrastructure.PhelCompletionPriority

/**
 * Based on official Phel API documentation: https://phel-lang.org/documentation/api/
 */
object PhelFunctionRegistry {

    private val functions = mutableMapOf<Namespace, List<DataFunction>>()

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

    fun getFunctions(namespace: Namespace): List<DataFunction> {
        return functions[namespace] ?: emptyList()
    }

    fun getFunctions(priority: PhelCompletionPriority): List<DataFunction> {
        return functions.values.flatten().filter { it.priority == priority }
    }

    fun getFunction(name: String): DataFunction? {
        return functions.values.flatten().find { it.name == name }
    }

    fun getAllFunctions(): List<DataFunction> {
        return functions.values.flatten()
    }
}
