package org.phellang.completion.data

import org.phellang.completion.data.schema.registerSchemaCoercerFunctions
import org.phellang.completion.data.schema.registerSchemaExplainerFunctions
import org.phellang.completion.data.schema.registerSchemaFunctions
import org.phellang.completion.data.schema.registerSchemaGeneratorFunctions
import org.phellang.completion.data.schema.registerSchemaInstrumentFunctions
import org.phellang.completion.data.schema.registerSchemaRegistryFunctions
import org.phellang.completion.data.schema.registerSchemaValidatorFunctions
import org.phellang.completion.data.test.registerTestFunctions
import org.phellang.completion.data.test.registerTestGenFunctions
import org.phellang.completion.data.test.registerTestRoseFunctions
import org.phellang.completion.data.test.registerTestSelectorFunctions
import org.phellang.completion.data.test.registerTestShrinkFunctions
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
        functions[Namespace.AI] = registerAiFunctions()
        functions[Namespace.ASYNC] = registerAsyncFunctions()
        functions[Namespace.BASE64] = registerBase64Functions()
        functions[Namespace.CLI] = registerCliFunctions()
        functions[Namespace.CORE] = registerCoreFunctions()
        functions[Namespace.DEBUG] = registerDebugFunctions()
        functions[Namespace.EDN] = registerEdnFunctions()
        functions[Namespace.HTML] = registerHtmlFunctions()
        functions[Namespace.HTTP] = registerHttpFunctions()
        functions[Namespace.HTTP_CLIENT] = registerHttpClientFunctions()
        functions[Namespace.JSON] = registerJsonFunctions()
        functions[Namespace.MATCH] = registerMatchFunctions()
        functions[Namespace.MOCK] = registerMockFunctions()
        functions[Namespace.PHP_INTEROP] = registerPhpInteropFunctions()
        functions[Namespace.PPRINT] = registerPprintFunctions()
        functions[Namespace.READER] = registerReaderFunctions()
        functions[Namespace.REFLECT] = registerReflectFunctions()
        functions[Namespace.REPL] = registerReplFunctions()
        functions[Namespace.ROUTER] = registerRouterFunctions()
        functions[Namespace.SCHEMA] = registerSchemaFunctions()
        functions[Namespace.SCHEMA_COERCER] = registerSchemaCoercerFunctions()
        functions[Namespace.SCHEMA_EXPLAINER] = registerSchemaExplainerFunctions()
        functions[Namespace.SCHEMA_GENERATOR] = registerSchemaGeneratorFunctions()
        functions[Namespace.SCHEMA_INSTRUMENT] = registerSchemaInstrumentFunctions()
        functions[Namespace.SCHEMA_REGISTRY] = registerSchemaRegistryFunctions()
        functions[Namespace.SCHEMA_VALIDATOR] = registerSchemaValidatorFunctions()
        functions[Namespace.STRING] = registerStringFunctions()
        functions[Namespace.TEST] = registerTestFunctions()
        functions[Namespace.TEST_GEN] = registerTestGenFunctions()
        functions[Namespace.TEST_ROSE] = registerTestRoseFunctions()
        functions[Namespace.TEST_SELECTOR] = registerTestSelectorFunctions()
        functions[Namespace.TEST_SHRINK] = registerTestShrinkFunctions()
        functions[Namespace.TRANSIT] = registerTransitFunctions()
        functions[Namespace.WALK] = registerWalkFunctions()
        functions[Namespace.WATCH] = registerWatchFunctions()
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
