package org.phellang.completion.infrastructure

/**
 * Higher values appear first in completion lists.
 */
enum class PhelCompletionPriority(val value: Double) {
    // Local scope - always most relevant
    CURRENT_SCOPE_LOCALS(100.0),
    RECENT_DEFINITIONS(90.0),
    
    // Context-specific completions (very relevant in their context)
    REFER_COMPLETIONS(95.0),

    // Language fundamentals (daily use)
    SPECIAL_FORMS(85.0),
    CONTROL_FLOW(80.0),
    MACROS(75.0),

    // Core operations (very common)
    CORE_FUNCTIONS(60.0),
    COLLECTION_FUNCTIONS(55.0),
    PREDICATE_FUNCTIONS(50.0),
    STRING_FUNCTIONS(45.0),
    ARITHMETIC_FUNCTIONS(40.0),

    // Domain-specific (contextual)
    JSON_FUNCTIONS(30.0),
    HTML_FUNCTIONS(29.0),
    HTTP_FUNCTIONS(28.0),

    // Development and testing
    TEST_FUNCTIONS(25.0),
    MOCK_FUNCTIONS(24.0),
    DEBUG_FUNCTIONS(20.0),
    REPL_FUNCTIONS(19.0),

    // Specialized
    PHP_INTEROP(15.0),
    BASE64_FUNCTIONS(12.0),
    PROJECT_SYMBOLS(10.0),

    // Should appear last
    DEPRECATED_FUNCTIONS(1.0);
}
