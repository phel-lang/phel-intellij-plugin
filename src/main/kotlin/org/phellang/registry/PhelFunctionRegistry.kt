package org.phellang.registry

// region GENERATED IMPORTS — updatePhelRegistry; do not edit by hand
import org.phellang.registry.schema.registerSchemaCoercerFunctions
import org.phellang.registry.schema.registerSchemaExplainerFunctions
import org.phellang.registry.schema.registerSchemaFunctions
import org.phellang.registry.schema.registerSchemaGeneratorFunctions
import org.phellang.registry.schema.registerSchemaInstrumentFunctions
import org.phellang.registry.schema.registerSchemaRegistryFunctions
import org.phellang.registry.schema.registerSchemaValidatorFunctions
import org.phellang.registry.test.registerTestFunctions
import org.phellang.registry.test.registerTestGenFunctions
import org.phellang.registry.test.registerTestRoseFunctions
import org.phellang.registry.test.registerTestSelectorFunctions
import org.phellang.registry.test.registerTestShrinkFunctions
// endregion GENERATED IMPORTS — updatePhelRegistry
import org.phellang.registry.PhelCompletionPriority

/**
 * Based on official Phel API documentation: https://phel-lang.org/documentation/api/
 */
object PhelFunctionRegistry {

    private val functions = mutableMapOf<Namespace, List<PhelFunction>>()

    // `functions` is populated once in init and never mutated afterwards, so the flattened
    // view can be computed a single time and reused by every read path.
    private val flattenedFunctions: List<PhelFunction> by lazy { functions.values.flatten() }

    // Cache of deprecated function names for fast lookup
    private val deprecatedFunctionNames: Set<String> by lazy {
        flattenedFunctions
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
        // region GENERATED INIT — updatePhelRegistry; do not edit by hand
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
        // endregion GENERATED INIT — updatePhelRegistry
    }

    fun getFunctions(namespace: Namespace): List<PhelFunction> {
        return functions[namespace] ?: emptyList()
    }

    fun getFunctions(priority: PhelCompletionPriority): List<PhelFunction> {
        return flattenedFunctions.filter { it.completion.priority == priority }
    }

    // Cache of function names grouped by completion priority for fast membership checks.
    // Used by syntax highlighting, which probes every symbol against several categories.
    private val functionNamesByPriority: Map<PhelCompletionPriority, Set<String>> by lazy {
        flattenedFunctions
            .groupBy { it.completion.priority }
            .mapValues { (_, fns) -> fns.mapTo(HashSet()) { it.name } }
    }

    /** O(1) check for whether a function with [name] exists in the given [priority] category. */
    fun hasFunctionWithName(priority: PhelCompletionPriority, name: String): Boolean {
        return functionNamesByPriority[priority]?.contains(name) == true
    }

    // First-wins lookup by exact name, mirroring the previous `find { it.name == name }`.
    private val functionsByName: Map<String, PhelFunction> by lazy {
        val map = HashMap<String, PhelFunction>()
        for (fn in flattenedFunctions) {
            map.putIfAbsent(fn.name, fn)
        }
        map
    }

    fun getFunction(name: String): PhelFunction? {
        return functionsByName[name]
    }

    fun getAllFunctions(): List<PhelFunction> {
        return flattenedFunctions
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
