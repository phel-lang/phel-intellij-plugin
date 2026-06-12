package org.phellang.completion.data

import com.intellij.openapi.project.Project
import org.phellang.completion.indexing.PhelProjectSymbolIndex

/**
 * Resolves the call arities for a function name, used by both the arity-mismatch inspection
 * and the parameter-hint provider. Standard-library functions come from the generated
 * registry; project-defined functions come from the symbol index.
 */
object PhelArityResolver {

    /** Arities for [name] (namespace qualifier ignored), or null when the name is unknown. */
    fun resolve(project: Project, name: String): List<PhelArity>? {
        val shortName = name.substringAfterLast('/')

        PhelFunctionRegistry.getFunction(shortName)?.let { fn ->
            val parsed = PhelArity.parseAll(fn.signature)
            if (parsed.isNotEmpty()) return parsed
        }

        return PhelProjectSymbolIndex.getInstance(project)
            .findByName(shortName)
            .firstOrNull { it.arities.isNotEmpty() }
            ?.arities
    }
}
