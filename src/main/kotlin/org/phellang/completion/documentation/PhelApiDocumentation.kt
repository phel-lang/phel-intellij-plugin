package org.phellang.completion.documentation

import org.phellang.completion.data.PhelFunctionRegistry
import java.util.concurrent.ConcurrentHashMap

object PhelApiDocumentation {

    // Rendered HTML documentation, keyed by fully-qualified function name. Built lazily on
    // first request instead of eagerly rendering every function up front — highlighting only
    // needs to know whether a name exists (see [hasDocumentation]), not its rendered HTML.
    private val rendered = ConcurrentHashMap<String, String>()

    /** True when the standard-library registry has a function with this exact name. */
    fun hasDocumentation(name: String): Boolean = PhelFunctionRegistry.getFunction(name) != null

    /** Rendered HTML documentation for [name], or null when no such standard-library function exists. */
    fun getDocumentation(name: String): String? {
        val function = PhelFunctionRegistry.getFunction(name) ?: return null
        return rendered.computeIfAbsent(name) {
            "<h3>${function.name}</h3>${function.toHtmlDocumentation()}"
        }
    }
}
