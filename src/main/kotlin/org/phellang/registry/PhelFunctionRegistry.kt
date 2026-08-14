package org.phellang.registry

// region GENERATED IMPORTS — updatePhelRegistry; do not edit by hand
import org.phellang.registry.data.registerAiFunctions
import org.phellang.registry.data.registerAsyncFunctions
import org.phellang.registry.data.registerBase64Functions
import org.phellang.registry.data.registerBenchFunctions
import org.phellang.registry.data.registerCliFunctions
import org.phellang.registry.data.registerCoreFunctions
import org.phellang.registry.data.registerEdnFunctions
import org.phellang.registry.data.registerHtmlFunctions
import org.phellang.registry.data.registerHttpClientFunctions
import org.phellang.registry.data.registerHttpFunctions
import org.phellang.registry.data.registerJsonFunctions
import org.phellang.registry.data.registerMatchFunctions
import org.phellang.registry.data.registerMockFunctions
import org.phellang.registry.data.registerPhpInteropFunctions
import org.phellang.registry.data.registerPprintFunctions
import org.phellang.registry.data.registerReaderFunctions
import org.phellang.registry.data.registerReflectFunctions
import org.phellang.registry.data.registerReplFunctions
import org.phellang.registry.data.registerRouterFunctions
import org.phellang.registry.data.registerStringFunctions
import org.phellang.registry.data.registerTraceFunctions
import org.phellang.registry.data.registerTransitFunctions
import org.phellang.registry.data.registerWalkFunctions
import org.phellang.registry.data.registerWatchFunctions
import org.phellang.registry.data.schema.registerSchemaCoercerFunctions
import org.phellang.registry.data.schema.registerSchemaExplainerFunctions
import org.phellang.registry.data.schema.registerSchemaFunctions
import org.phellang.registry.data.schema.registerSchemaGeneratorFunctions
import org.phellang.registry.data.schema.registerSchemaInstrumentFunctions
import org.phellang.registry.data.schema.registerSchemaRegistryFunctions
import org.phellang.registry.data.schema.registerSchemaValidatorFunctions
import org.phellang.registry.data.test.registerTestFunctions
import org.phellang.registry.data.test.registerTestGenFunctions
import org.phellang.registry.data.test.registerTestRoseFunctions
import org.phellang.registry.data.test.registerTestSelectorFunctions
import org.phellang.registry.data.test.registerTestShrinkFunctions
// endregion GENERATED IMPORTS — updatePhelRegistry

// Outside the GENERATED region on purpose: updatePhelRegistry rewrites only what is inside it.
import org.jetbrains.annotations.TestOnly

/**
 * Based on official Phel API documentation: https://phel-lang.org/documentation/api/
 */
object PhelFunctionRegistry {
    private val functions = mutableMapOf<Namespace, List<PhelFunction>>()

    // `functions` is populated once in init and never mutated afterwards, so the flattened
    // view can be computed a single time and reused by every read path.
    private val flattenedFunctions: List<PhelFunction> by lazy { functions.values.flatten() }

    // Deprecated names are stored under both the full and the short name, so lookups work
    // with or without a namespace prefix.
    private val deprecatedFunctionNames: Set<String> by lazy {
        flattenedFunctions
            .filter { it.isDeprecated }
            .flatMap { func ->
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
        functions[Namespace.BENCH] = registerBenchFunctions()
        functions[Namespace.CLI] = registerCliFunctions()
        functions[Namespace.CORE] = registerCoreFunctions()
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
        functions[Namespace.TRACE] = registerTraceFunctions()
        functions[Namespace.TRANSIT] = registerTransitFunctions()
        functions[Namespace.WALK] = registerWalkFunctions()
        functions[Namespace.WATCH] = registerWatchFunctions()
        // endregion GENERATED INIT — updatePhelRegistry

        // Hand-wired native PHP functions (see PhpNativeFunctions.kt, same package so no import),
        // plus the superglobals, which are variables and so appear in no function synopsis for
        // updatePhpRegistry to find (see PhpSuperglobals.kt).
        // Kept out of the GENERATED region so updatePhelRegistry does not overwrite it.
        functions[Namespace.PHP_NATIVE] = phpNativeFunctions() + phpSuperglobals()
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

    // First-wins by exact name: duplicate names across namespaces keep the earliest entry.
    private val functionsByName: Map<String, PhelFunction> by lazy {
        val map = HashMap<String, PhelFunction>()
        for (fn in flattenedFunctions) {
            map.putIfAbsent(fn.name, fn)
        }
        map
    }

    fun getFunction(name: String): PhelFunction? {
        return testFunctionsByName[name] ?: functionsByName[name]
    }

    fun isDeprecated(functionName: String): Boolean {
        if (functionName in testDeprecatedNames || functionName in deprecatedFunctionNames) {
            return true
        }

        // A namespace-prefixed input (e.g. "core/put") may only be stored under its short name.
        val shortName = functionName.substringAfter("/")
        return shortName in testDeprecatedNames || shortName in deprecatedFunctionNames
    }

    // region Test overlay
    //
    // Phel v0.50.0 deleted every deprecated function from the language, so api.json — and with it
    // the generated registry — now carries no deprecation data at all. The deprecation feature
    // (inspection, annotator rule, completion priority) is still shipped and still correct, but
    // its tests had nothing left to exercise. They install synthetic functions here instead of
    // pinning to whichever stdlib names happen to be deprecated this release.
    //
    // Deliberately narrow: the overlay backs [getFunction] and [isDeprecated] only. It is not part
    // of `flattenedFunctions`, so it cannot leak into completion, `getFunctions`, or the
    // priority caches. In production both fields stay empty and each read costs one miss on an
    // empty map.

    private var testFunctionsByName: Map<String, PhelFunction> = emptyMap()
    private var testDeprecatedNames: Set<String> = emptySet()

    /** Overlays [overlay] onto name and deprecation lookups. Always pair with [clearTestFunctions]. */
    @TestOnly
    fun installTestFunctions(overlay: List<PhelFunction>) {
        // Indexed exactly like the production `functionsByName`, so a fixture is reachable under
        // the same name shape the generated data would have used.
        testFunctionsByName = overlay.associateBy { it.name }
        testDeprecatedNames = overlay.filter { it.isDeprecated }
            .flatMapTo(HashSet()) { setOf(it.name, it.name.substringAfter("/")) }
    }

    @TestOnly
    fun clearTestFunctions() {
        testFunctionsByName = emptyMap()
        testDeprecatedNames = emptySet()
    }
    // endregion Test overlay
}
