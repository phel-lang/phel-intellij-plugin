package org.phellang.registry

enum class Namespace {
    // region GENERATED — updatePhelRegistry; do not edit by hand
    AI,
    ASYNC,
    BASE64,
    CLI,
    CORE,
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
    TRACE,
    TRANSIT,
    WALK,
    WATCH,
    // endregion GENERATED — updatePhelRegistry

    // Hand-wired: native PHP library functions surfaced through the php/ prefix. Kept outside the
    // GENERATED region so updatePhelRegistry preserves it (RegistryWiringGenerator only rewrites
    // between the markers). See registry/PhpNativeFunctions.kt.
    PHP_NATIVE,
    ;

    companion object {
        private val ALIASES: Map<String, Namespace> = mapOf(
            "ai" to AI,
            "base64" to BASE64,
            "core" to CORE,
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
            "trace" to TRACE,
            "transit" to TRANSIT,
        )

        /** Maps a short/alias namespace token (case-insensitive) to a [Namespace], or null if unknown. */
        fun fromShortName(shortNamespace: String): Namespace? =
            ALIASES[shortNamespace.lowercase()]
    }
}
