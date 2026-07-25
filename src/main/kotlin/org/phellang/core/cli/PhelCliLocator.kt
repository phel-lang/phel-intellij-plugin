package org.phellang.core.cli

import java.io.File

/**
 * Finds the project's `phel` executable.
 *
 * Lives in `core` rather than beside either caller so that everything shelling out to the CLI (the
 * formatter, run configurations) resolves the same binary. Two callers with their own copy of this
 * list would eventually disagree about what a project means by "phel".
 */
object PhelCliLocator {

    private val CANDIDATES = listOf("bin/phel", "vendor/bin/phel")

    /** What [locate] looks for, phrased for an error message shown to the user. */
    const val SEARCHED_PATHS = "./bin/phel and ./vendor/bin/phel relative to project root"

    /** Kept here so the formatter and the runner report a missing binary identically. */
    const val NOT_FOUND_MESSAGE = "Phel binary not found. Looked for $SEARCHED_PATHS."

    fun locate(basePath: String): File? {
        for (candidate in CANDIDATES) {
            val file = File(basePath, candidate)
            if (file.isFile && file.canExecute()) return file
        }
        return null
    }
}
