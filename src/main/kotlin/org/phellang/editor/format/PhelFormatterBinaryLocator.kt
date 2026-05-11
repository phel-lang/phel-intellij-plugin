package org.phellang.editor.format

import java.io.File

object PhelFormatterBinaryLocator {

    private val CANDIDATES = listOf("bin/phel", "vendor/bin/phel")

    fun locate(basePath: String): File? {
        for (candidate in CANDIDATES) {
            val file = File(basePath, candidate)
            if (file.isFile && file.canExecute()) return file
        }
        return null
    }
}
