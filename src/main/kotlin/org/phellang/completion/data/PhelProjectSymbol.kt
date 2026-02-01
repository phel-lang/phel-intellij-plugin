package org.phellang.completion.data

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
)

enum class SymbolType(val keyword: String) {
    FUNCTION("defn"), VALUE("def"), MACRO("defmacro"), STRUCT("defstruct"), INTERFACE("definterface");

    companion object {
        private val keywordMap = entries.associateBy { it.keyword }

        fun fromKeyword(keyword: String): SymbolType? = keywordMap[keyword]

        /** All keywords that define symbols */
        val definingKeywords: Set<String> = entries.map { it.keyword }.toSet()
    }
}
