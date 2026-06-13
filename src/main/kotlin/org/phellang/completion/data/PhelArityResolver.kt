package org.phellang.completion.data

import com.intellij.openapi.project.Project
import org.phellang.completion.indexing.PhelProjectSymbolIndex

/**
 * Resolves the call arities for a function name, used by both the arity-mismatch inspection
 * and the parameter-hint provider. Standard-library functions come from the generated
 * registry; project-defined functions come from the symbol index.
 */
object PhelArityResolver {

    /**
     * Arities for [name], or null when the name is unknown.
     *
     * A namespace-qualified call (`ns/fn`) is resolved against that namespace first: a
     * userland `myns/foo` must bind to the project's `myns/foo`, not to a same-named
     * stdlib function. Only when no matching project symbol exists do we fall back to the
     * registry, and finally to any same-named project symbol regardless of namespace.
     */
    fun resolve(project: Project, name: String): List<PhelArity>? {
        val slash = name.lastIndexOf('/')
        val shortName = if (slash >= 0) name.substring(slash + 1) else name
        val qualifier = if (slash > 0) name.substring(0, slash) else null

        val index = PhelProjectSymbolIndex.getInstance(project)

        if (qualifier != null) {
            index.findSymbol(qualifier, shortName)
                ?.takeIf { it.arities.isNotEmpty() }
                ?.let { return it.arities }
        }

        PhelFunctionRegistry.getFunction(shortName)?.let { fn ->
            val parsed = PhelArity.parseAll(fn.signature)
            if (parsed.isNotEmpty()) return parsed
        }

        return index.findByName(shortName)
            .firstOrNull { it.arities.isNotEmpty() }
            ?.arities
    }
}
