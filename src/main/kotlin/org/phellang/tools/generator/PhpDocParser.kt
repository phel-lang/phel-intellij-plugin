package org.phellang.tools.generator

import org.phellang.tools.model.PhpFunctionDoc

/**
 * Extracts a [PhpFunctionDoc] from a php/doc-en function XML.
 *
 * The DocBook files reference external DTD entities (`&reftitle.description;`, `&true;`, ...) that a
 * validating XML parser chokes on, and those live in the description/examples sections we do not need.
 * So this pulls the three self-contained pieces near the top of the file by pattern:
 * `<refname>` (the function name), `<refpurpose>` (the summary), and the first `<methodsynopsis>`
 * (the parameter list). No DTD, no entity resolution.
 */
object PhpDocParser {

    private val REFNAME = Regex("<refname>(.*?)</refname>", RegexOption.DOT_MATCHES_ALL)
    private val REFPURPOSE = Regex("<refpurpose>(.*?)</refpurpose>", RegexOption.DOT_MATCHES_ALL)
    private val METHODSYNOPSIS = Regex("<methodsynopsis\\b[^>]*>(.*?)</methodsynopsis>", RegexOption.DOT_MATCHES_ALL)
    private val METHODPARAM = Regex("<methodparam\\b([^>]*)>(.*?)</methodparam>", RegexOption.DOT_MATCHES_ALL)
    private val PARAMETER = Regex("<parameter[^>]*>(.*?)</parameter>", RegexOption.DOT_MATCHES_ALL)
    private val TAG = Regex("<[^>]+>")
    private val WHITESPACE = Regex("\\s+")

    // Bare global function names only: skips class methods (Foo::bar), operators and other odd refnames.
    private val FUNCTION_NAME = Regex("[a-z_][a-z0-9_]*", RegexOption.IGNORE_CASE)

    fun parse(xml: String, extension: String): PhpFunctionDoc? {
        val name = REFNAME.find(xml)?.groupValues?.get(1)?.let(::stripTags)?.trim() ?: return null
        if (!FUNCTION_NAME.matches(name)) return null

        val summary = REFPURPOSE.find(xml)?.groupValues?.get(1)?.let(::plainText) ?: return null
        if (summary.isBlank()) return null

        val synopsis = METHODSYNOPSIS.find(xml)?.groupValues?.get(1) ?: return null

        return PhpFunctionDoc(
            name = name,
            summary = summary,
            signature = buildSignature(name, synopsis),
            extension = extension,
        )
    }

    private fun buildSignature(name: String, synopsis: String): String {
        val params = METHODPARAM.findAll(synopsis).mapNotNull { match ->
            val attributes = match.groupValues[1]
            val body = match.groupValues[2]
            val parameter = PARAMETER.find(body)?.groupValues?.get(1)?.let(::stripTags)?.trim()
            if (parameter.isNullOrEmpty()) return@mapNotNull null
            when {
                attributes.contains("rep=\"repeat\"") -> "& $parameter"
                attributes.contains("choice=\"opt\"") -> "[$parameter]"
                else -> parameter
            }
        }.toList()

        return if (params.isEmpty()) "(php/$name)" else "(php/$name ${params.joinToString(" ")})"
    }

    private fun stripTags(text: String): String = TAG.replace(text, "")

    // DocBook type/keyword entities (&array;, &true;, &null;, ...) expand to their own name; the doc-en
    // DTD is not loaded, so resolve them to that name. Runs after the standard entities so a decoded
    // `&amp;` is a bare ampersand by the time this sees the text.
    private val CUSTOM_ENTITY = Regex("&([A-Za-z][\\w.:-]*);")

    /** Tag-stripped, whitespace-collapsed, with the XML and DocBook entities that appear in prose decoded. */
    private fun plainText(raw: String): String =
        WHITESPACE.replace(stripTags(raw), " ").trim()
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&#039;", "'")
            .replace(CUSTOM_ENTITY) { it.groupValues[1] }
}
