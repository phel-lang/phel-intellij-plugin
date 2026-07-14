package org.phellang.registry

enum class Namespace {
    // region GENERATED — updatePhelRegistry; do not edit by hand
    AI,
    ASYNC,
    BASE64,
    CLI,
    CORE,
    DEBUG,
    EDN,
    HTML,
    HTTP,
    HTTP_CLIENT,
    JSON,
    MATCH,
    MOCK,
    PHP_INTEROP,
    PPRINT,
    READER,
    REFLECT,
    REPL,
    ROUTER,
    SCHEMA,
    SCHEMA_COERCER,
    SCHEMA_EXPLAINER,
    SCHEMA_GENERATOR,
    SCHEMA_INSTRUMENT,
    SCHEMA_REGISTRY,
    SCHEMA_VALIDATOR,
    STRING,
    TEST,
    TEST_GEN,
    TEST_ROSE,
    TEST_SELECTOR,
    TEST_SHRINK,
    TRANSIT,
    WALK,
    WATCH,
    // endregion GENERATED — updatePhelRegistry
    ;

    companion object {
        private val ALIASES: Map<String, Namespace> = mapOf(
            "ai" to AI,
            "base64" to BASE64,
            "core" to CORE,
            "debug" to DEBUG,
            "edn" to EDN,
            "html" to HTML,
            "http" to HTTP,
            "http-client" to HTTP_CLIENT,
            "http_client" to HTTP_CLIENT,
            "json" to JSON,
            "mock" to MOCK,
            "reflect" to REFLECT,
            "repl" to REPL,
            "string" to STRING,
            "test" to TEST,
            "transit" to TRANSIT,
        )

        /** Maps a short/alias namespace token (case-insensitive) to a [Namespace], or null if unknown. */
        fun fromShortName(shortNamespace: String): Namespace? =
            ALIASES[shortNamespace.lowercase()]
    }
}
