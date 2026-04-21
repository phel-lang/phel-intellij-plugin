package org.phellang.language.psi.utils

/**
 * Semantic categories used to classify Phel symbols for highlighting and reference resolution.
 *
 * Language-layer enum kept independent of completion ranking so callers that need symbol
 * classification don't reach into the completion package for it.
 */
enum class SymbolCategory {
    SPECIAL_FORMS,
    CONTROL_FLOW,
    MACROS,
    CORE_FUNCTIONS,
    COLLECTION_FUNCTIONS,
}
