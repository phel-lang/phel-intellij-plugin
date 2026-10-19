package org.phellang.indexing

import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.registry.PhelArity
import org.phellang.registry.PhelFunctionRegistry

/**
 * Resolves the call arities for a function name, used by both the arity-mismatch inspection
 * and the parameter-hint provider. Standard-library functions come from the generated
 * registry; project-defined functions come from the symbol index.
 */
object PhelArityResolver {

    private const val CORE = "core"

    /**
     * Arities for [name], the call head [head] spells, or null when the name is unknown.
     *
     * A namespace-qualified call (`ns/fn`) takes them from the namespace its qualifier names, see
     * [qualifiedArities]. A bare name takes the registry's, then any same-named project symbol's.
     */
    fun resolve(head: PhelSymbol, name: String): List<PhelArity>? {
        val slash = name.lastIndexOf('/')
        val shortName = if (slash >= 0) name.substring(slash + 1) else name
        val qualifier = if (slash > 0) name.substring(0, slash) else null

        val index = PhelProjectSymbolIndex.getInstance(head.project)

        if (qualifier != null) {
            val file = head.containingFile as? PhelFile ?: return null
            return qualifiedArities(index, file, qualifier, shortName)
        }

        PhelFunctionRegistry.getFunction(shortName)
            ?.arities
            ?.takeIf { it.isNotEmpty() }
            ?.let { return it }

        return index.findByName(shortName)
            .firstOrNull { it.arities.isNotEmpty() }
            ?.arities
    }

    /**
     * `qualifier/name` is checked against the namespace the qualifier names and nowhere else: a userland
     * `u/count` binds to the `count` of the namespace `u` stands for, never to the stdlib's `count` or to
     * another namespace's. The project's definition there first; else, unless the qualifier names a
     * required project namespace, the stdlib's by its registry name (`string/join`; core functions are
     * registered bare).
     */
    private fun qualifiedArities(
        index: PhelProjectSymbolIndex,
        file: PhelFile,
        qualifier: String,
        name: String,
    ): List<PhelArity>? {
        index.findQualified(file, qualifier, name)?.let { return it.arities.ifEmpty { null } }

        val imported = PhelNamespaceUtils.importedNamespaceFor(file, qualifier)
        if (imported != null && !PhelProjectNamespaceFinder.isStandardLibrary(imported)) return null

        val namespace = imported?.let(PhelProjectNamespaceFinder::extractShortNamespace) ?: qualifier
        val registryName = if (namespace == CORE) name else "$namespace/$name"

        return PhelFunctionRegistry.getFunction(registryName)?.arities?.ifEmpty { null }
    }
}
