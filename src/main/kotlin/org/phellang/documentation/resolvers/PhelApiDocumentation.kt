package org.phellang.documentation.resolvers

import org.phellang.registry.PhelFunctionRegistry
import java.util.concurrent.ConcurrentHashMap

object PhelApiDocumentation {

    // Rendered HTML documentation, keyed by fully-qualified function name. Built lazily on first
    // request rather than eagerly rendering all 900+ registry functions: hover asks for one at a time.
    private val rendered = ConcurrentHashMap<String, String>()

    /** Rendered HTML documentation for [name], or null when no such standard-library function exists. */
    fun getDocumentation(name: String): String? {
        val function = PhelFunctionRegistry.getFunction(name) ?: return null
        return rendered.computeIfAbsent(name) {
            "<h3>${function.name}</h3>${function.toHtmlDocumentation()}"
        }
    }
}
