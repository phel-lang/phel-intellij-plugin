package org.phellang.completion.infrastructure

import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile

/**
 * How a stdlib namespace's functions are written in a file where their registry spelling would name
 * something else.
 *
 * The registry spells `phel.string/upper-case` as `string/upper-case`, which relies on the default
 * alias `string`. Once another require claims that name, `string/` is that namespace's, and requiring
 * `phel.string` plainly would take the name back and re-point the file's existing calls: the compiler
 * keeps the last alias of a name.
 */
object PhelStandardLibraryQualifiers {

    /** [qualifier] to write in front of the name, or null to leave the namespace out of completion. */
    data class Override(val qualifier: String?, val fullNamespace: String)

    /**
     * Null when the registry spelling works: no require claims [registryNamespace], or the stdlib's own
     * does, or it is required under an alias, which the alias map already renders. Otherwise its full
     * name when it is required, else an alias of its own (`phel-string`) for the accepted completion to
     * require it under, else nothing when even that alias is taken.
     */
    fun overrideFor(file: PhelFile, registryNamespace: String, standardLibraryAliases: Map<String, String>): Override? {
        val fullNamespace = "phel.$registryNamespace"
        val claimedBy = namedBy(file, registryNamespace)
        if (claimedBy == null || claimedBy == fullNamespace) return null

        val imports = PhelNamespaceUtils.extractImports(file)
            .filter { PhelNamespaceUtils.normalizeNamespace(it.fullNamespace) == fullNamespace }
        if (imports.any { it.alias != null && standardLibraryAliases[it.alias] != null }) return null
        if (imports.isNotEmpty()) return Override(fullNamespace, fullNamespace)

        val alias = fullNamespace.replace('.', '-')
        return Override(alias.takeIf { namedBy(file, it) == null }, fullNamespace)
    }

    private fun namedBy(file: PhelFile, qualifier: String): String? =
        PhelNamespaceUtils.importedNamespaceFor(file, qualifier)?.let(PhelNamespaceUtils::normalizeNamespace)
}
