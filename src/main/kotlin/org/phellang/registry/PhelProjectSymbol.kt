package org.phellang.registry

import com.intellij.openapi.vfs.VirtualFile

data class PhelProjectSymbol(
    val namespace: String,
    val shortNamespace: String,
    val name: String,
    val qualifiedName: String,
    val signature: String,
    val type: SymbolType,
    val file: VirtualFile,
    val docstring: String? = null,
    val arities: List<PhelArity> = PhelArity.parseAll(signature),
)

/**
 * What an indexed project symbol is.
 *
 * The keyword lists are spelled out here rather than derived from `PhelSpecialForms`, because
 * `registry` is the bottom leaf and may import nothing else under `org.phellang`. A test pins the
 * two together so the duplication cannot drift: every definition form must appear here or be listed
 * there as a deliberate exclusion.
 *
 * `declare` and `deftest` are the exclusions. A `declare` is a forward reference whose real
 * definition follows — indexing both would offer one name twice — and a `deftest` names a test entry
 * point that other namespaces do not call by name.
 */
enum class SymbolType(
    /** The canonical public spelling, shown wherever a single keyword is rendered. */
    val primaryKeyword: String,
    val keywords: Set<String>,
) {
    FUNCTION("defn", setOf("defn", "defn-")),
    VALUE("def", setOf("def", "def-", "defonce")),
    MACRO("defmacro", setOf("defmacro", "defmacro-")),
    STRUCT("defstruct", setOf("defstruct", "defstruct*")),
    INTERFACE("definterface", setOf("definterface", "definterface*")),
    EXCEPTION("defexception", setOf("defexception", "defexception*")),
    ENUM("defenum", setOf("defenum")),
    PROTOCOL("defprotocol", setOf("defprotocol")),
    RECORD("defrecord", setOf("defrecord")),
    TYPE("deftype", setOf("deftype")),
    MULTIMETHOD("defmulti", setOf("defmulti"));

    companion object {
        private val keywordMap: Map<String, SymbolType> =
            entries.flatMap { type -> type.keywords.map { it to type } }.toMap()

        fun fromKeyword(keyword: String): SymbolType? = keywordMap[keyword]

        /** All keywords that define an indexable symbol. */
        val definingKeywords: Set<String> = keywordMap.keys
    }
}
