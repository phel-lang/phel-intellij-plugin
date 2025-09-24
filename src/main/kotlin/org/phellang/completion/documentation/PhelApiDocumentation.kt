package org.phellang.completion.documentation

import org.phellang.completion.data.PhelFunctionRegistry

object PhelApiDocumentation {

    val functionDocs = mutableMapOf<String, String>()

    init {
        initializeDocumentation()
    }

    private fun initializeDocumentation() {
        val elements = PhelFunctionRegistry.getAllFunctions()

        for (element in elements) {
            functionDocs[element.name] = "<h3>${element.name}</h3>${element.descriptionHtml}"
        }
    }
}
