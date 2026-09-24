package org.phellang.language.psi

/**
 * Type tags: the symbol after `^` on a parameter, a binding or a definition, as in `(defn f [^map m] ...)`.
 *
 * A tag names a type, never a var. Phel 0.53.0 made that matter: its short tags for Phel's own values
 * (`^map`, `^vector`, `^set`, `^list`, `^keyword`, `^symbol`, `^atom`) are also the names of core
 * functions, so every feature that reads the symbol as a var reference (highlighting, hover, navigation,
 * rename, the unused checks) misreads them. This is the one place that decides what is a tag; every such
 * feature asks it rather than re-deriving the PSI shape.
 */
object PhelTypeTags {

    /**
     * The short tags for Phel values and the class each one stands for, as upstream's
     * `Phel\Shared\TagResolver::TYPE_ALIASES` (v0.53.0) defines them.
     */
    val VALUE_TYPE_ALIASES: Map<String, String> = mapOf(
        "map" to "Phel\\Lang\\Collections\\Map\\PersistentMapInterface",
        "vector" to "Phel\\Lang\\Collections\\Vector\\PersistentVectorInterface",
        "set" to "Phel\\Lang\\Collections\\HashSet\\PersistentHashSetInterface",
        "list" to "Phel\\Lang\\Collections\\LinkedList\\PersistentListInterface",
        "keyword" to "Phel\\Lang\\Keyword",
        "symbol" to "Phel\\Lang\\Symbol",
        "atom" to "Phel\\Lang\\Atom",
    )

    /** The PHP types a tag passes through to the generated signature unchanged. */
    val PHP_TYPES: List<String> = listOf(
        "int", "float", "string", "bool", "array", "callable", "iterable", "object", "mixed", "null", "void", "never",
    )

    /** One member of a tag: `?map` is a nullable `map`, and `map|null` has two members. */
    data class Member(val name: String, val nullable: Boolean)

    /**
     * True when [symbol] is the tag of a `^tag` prefix. Only a bare symbol counts: in `^{:tag map}` the
     * metadata is a map, and a symbol inside it is an ordinary form.
     */
    fun isTypeTag(symbol: PhelSymbol): Boolean = (symbol.parent as? PhelMetadata)?.symbol === symbol

    /** The members of a union (`|`) or intersection (`&`) tag, each with its `?` prefix read off. */
    fun members(tag: String): List<Member> = tag.split('|', '&')
        .map(String::trim)
        .mapNotNull { part ->
            val nullable = part.startsWith('?')
            part.removePrefix("?").takeIf { it.isNotEmpty() }?.let { Member(it, nullable) }
        }

    /** The class a short value tag stands for, or null when [name] is not one of [VALUE_TYPE_ALIASES]. */
    fun backingClass(name: String): String? = VALUE_TYPE_ALIASES[name]
}
