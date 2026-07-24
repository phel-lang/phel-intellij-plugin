package org.phellang.language.psi

/**
 * What a top-level `def…` head declares, and the noun the IDE uses for it.
 *
 * Navigation surfaces this in two places that must agree: the structure/navbar label
 * (`PhelItemPresentation`) and Find Usages' element type (`PhelFindUsagesProvider`). Both spelled
 * the same seven-branch mapping out by hand, so the two could drift into disagreeing about what a
 * `defexception` is called. The mapping lives here once instead.
 *
 * Public and private variants (`def` / `def-`) are the same kind; the distinction is visibility,
 * not what is being declared, and neither caller renders it.
 *
 * Deliberately not shared with the other keyword-to-label mappings in the codebase: hover
 * documentation says "Function Definition" and completion says "Local Function" for the very same
 * `defn`. Those are separate display vocabularies aimed at different popups, not copies of this
 * one. A feature that wants its own wording should map from a [PhelDefinitionKind], not add a
 * noun here.
 */
enum class PhelDefinitionKind(val noun: String, val keywords: Set<String>) {
    VARIABLE("variable", setOf("def", "def-")),
    FUNCTION("function", setOf("defn", "defn-")),
    MACRO("macro", setOf("defmacro", "defmacro-")),
    STRUCT("struct", setOf("defstruct")),
    INTERFACE("interface", setOf("definterface")),
    EXCEPTION("exception", setOf("defexception")),
    DECLARATION("declaration", setOf("declare"));

    companion object {
        private val byKeyword: Map<String, PhelDefinitionKind> =
            entries.flatMap { kind -> kind.keywords.map { it to kind } }.toMap()

        /** The kind [keyword] declares, or null when it declares nothing (or is absent). */
        fun fromKeyword(keyword: String?): PhelDefinitionKind? = keyword?.let { byKeyword[it] }
    }
}
