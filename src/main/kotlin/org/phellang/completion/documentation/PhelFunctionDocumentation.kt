package org.phellang.completion.documentation

import org.phellang.completion.data.PhelFunctionRegistry

object PhelFunctionDocumentation {

    val functionDocs = mutableMapOf<String, String>()

    init {
        initializeDocumentation()
    }

    private fun initializeDocumentation() {
        val elements = PhelFunctionRegistry.getAllFunctions()

        for (element in elements) {
            functionDocs[element.name] = "<h3>${element.name}</h3>" +
                    "<p><b>Signature:</b> <code>${element.signature}</code></p>" +
                    "<p>${element.description}</p>"
        }
    }
}
