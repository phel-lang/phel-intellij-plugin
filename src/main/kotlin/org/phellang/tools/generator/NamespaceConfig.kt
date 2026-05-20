package org.phellang.tools.generator

object NamespaceConfig {

    data class NamespaceInfo(
        val functionName: String,
        val fileName: String,
        val subfolder: String? = null,
    )

    private val config = mapOf(
        "ai" to NamespaceInfo("registerAiFunctions", "registerAiFunctions.kt"),
        "async" to NamespaceInfo("registerAsyncFunctions", "registerAsyncFunctions.kt"),
        "base64" to NamespaceInfo("registerBase64Functions", "registerBase64Functions.kt"),
        "cli" to NamespaceInfo("registerCliFunctions", "registerCliFunctions.kt"),
        "core" to NamespaceInfo("registerCoreFunctions", "registerCoreFunctions.kt"),
        "debug" to NamespaceInfo("registerDebugFunctions", "registerDebugFunctions.kt"),
        "html" to NamespaceInfo("registerHtmlFunctions", "registerHtmlFunctions.kt"),
        "http" to NamespaceInfo("registerHttpFunctions", "registerHttpFunctions.kt"),
        "http_client" to NamespaceInfo("registerHttpClientFunctions", "registerHttpClientFunctions.kt"),
        "json" to NamespaceInfo("registerJsonFunctions", "registerJsonFunctions.kt"),
        "match" to NamespaceInfo("registerMatchFunctions", "registerMatchFunctions.kt"),
        "mock" to NamespaceInfo("registerMockFunctions", "registerMockFunctions.kt"),
        "php" to NamespaceInfo("registerPhpInteropFunctions", "registerPhpInteropFunctions.kt"),
        "pprint" to NamespaceInfo("registerPprintFunctions", "registerPprintFunctions.kt"),
        "reader" to NamespaceInfo("registerReaderFunctions", "registerReaderFunctions.kt"),
        "repl" to NamespaceInfo("registerReplFunctions", "registerReplFunctions.kt"),
        "router" to NamespaceInfo("registerRouterFunctions", "registerRouterFunctions.kt"),
        "schema" to NamespaceInfo("registerSchemaFunctions", "registerSchemaFunctions.kt", "schema"),
        "schema.coercer" to NamespaceInfo("registerSchemaCoercerFunctions", "registerSchemaCoercerFunctions.kt", "schema"),
        "schema.explainer" to NamespaceInfo("registerSchemaExplainerFunctions", "registerSchemaExplainerFunctions.kt", "schema"),
        "schema.generator" to NamespaceInfo("registerSchemaGeneratorFunctions", "registerSchemaGeneratorFunctions.kt", "schema"),
        "schema.instrument" to NamespaceInfo("registerSchemaInstrumentFunctions", "registerSchemaInstrumentFunctions.kt", "schema"),
        "schema.registry" to NamespaceInfo("registerSchemaRegistryFunctions", "registerSchemaRegistryFunctions.kt", "schema"),
        "schema.validator" to NamespaceInfo("registerSchemaValidatorFunctions", "registerSchemaValidatorFunctions.kt", "schema"),
        "string" to NamespaceInfo("registerStringFunctions", "registerStringFunctions.kt"),
        "test" to NamespaceInfo("registerTestFunctions", "registerTestFunctions.kt", "test"),
        "test.gen" to NamespaceInfo("registerTestGenFunctions", "registerTestGenFunctions.kt", "test"),
        "test.rose" to NamespaceInfo("registerTestRoseFunctions", "registerTestRoseFunctions.kt", "test"),
        "test.selector" to NamespaceInfo("registerTestSelectorFunctions", "registerTestSelectorFunctions.kt", "test"),
        "test.shrink" to NamespaceInfo("registerTestShrinkFunctions", "registerTestShrinkFunctions.kt", "test"),
        "walk" to NamespaceInfo("registerWalkFunctions", "registerWalkFunctions.kt"),
        "watch" to NamespaceInfo("registerWatchFunctions", "registerWatchFunctions.kt")
    )

    fun getInfo(namespace: String): NamespaceInfo? = config[namespace]

    fun isSupported(namespace: String): Boolean = config.containsKey(namespace)
}
