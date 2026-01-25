package org.phellang.tools.generator

object NamespaceConfig {

    data class NamespaceInfo(val functionName: String, val fileName: String)

    private val config = mapOf(
        "base64" to NamespaceInfo("registerBase64Functions", "registerBase64Functions.kt"),
        "core" to NamespaceInfo("registerCoreFunctions", "registerCoreFunctions.kt"),
        "debug" to NamespaceInfo("registerDebugFunctions", "registerDebugFunctions.kt"),
        "html" to NamespaceInfo("registerHtmlFunctions", "registerHtmlFunctions.kt"),
        "http" to NamespaceInfo("registerHttpFunctions", "registerHttpFunctions.kt"),
        "json" to NamespaceInfo("registerJsonFunctions", "registerJsonFunctions.kt"),
        "mock" to NamespaceInfo("registerMockFunctions", "registerMockFunctions.kt"),
        "php" to NamespaceInfo("registerPhpInteropFunctions", "registerPhpInteropFunctions.kt"),
        "repl" to NamespaceInfo("registerReplFunctions", "registerReplFunctions.kt"),
        "str" to NamespaceInfo("registerStringFunctions", "registerStringFunctions.kt"),
        "test" to NamespaceInfo("registerTestFunctions", "registerTestFunctions.kt")
    )

    fun getInfo(namespace: String): NamespaceInfo? = config[namespace]

    fun isSupported(namespace: String): Boolean = config.containsKey(namespace)
}
