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

    private val PROJECT_CANDIDATES = listOf("bin/phel", "vendor/bin/phel")

    private const val EXECUTABLE_NAME = "phel"

    /** What [locate] looks for, phrased for an error message shown to the user. */
    const val SEARCHED_PATHS = "./bin/phel and ./vendor/bin/phel relative to project root, then PATH"

    /** Kept here so the formatter and the runner report a missing binary identically. */
    const val NOT_FOUND_MESSAGE = "Phel binary not found. Looked for $SEARCHED_PATHS."

    /**
     * Project-local first, then the shell's `PATH`.
     *
     * Order matters: a project pinning a `phel` version through Composer must win over whatever
     * happens to be installed globally, or formatting output would depend on the machine.
     */
    fun locate(basePath: String): File? = locate(basePath, loginShellPathEntries())

    /**
     * Takes the `PATH` entries explicitly so a test can pin them. Resolving them internally would
     * make every assertion depend on whether the machine running it happens to have `phel`
     * installed.
     */
    internal fun locate(basePath: String, pathEntries: List<String>): File? =
        locateInProject(basePath) ?: locateOnPath(pathEntries)

    private fun locateInProject(basePath: String): File? =
        PROJECT_CANDIDATES.asSequence()
            .map { File(basePath, it) }
            .firstOrNull { it.isExecutableFile() }

    private fun locateOnPath(pathEntries: List<String>): File? =
        pathEntries.asSequence()
            .map { File(it, EXECUTABLE_NAME) }
            .firstOrNull { it.isExecutableFile() }

    /**
     * The *login shell's* `PATH`, not the IDE's. An IDE started from Dock or Spotlight on macOS has
     * only `/usr/bin:/bin:/usr/sbin:/sbin`, where a Homebrew or mise `phel` never appears — the same
     * reason the child process needs [PhelCliEnvironment].
     */
    private fun loginShellPathEntries(): List<String> =
        PhelCliEnvironment.loginShellEnvironment()["PATH"]
            ?.split(File.pathSeparatorChar)
            ?.filter { it.isNotBlank() }
            .orEmpty()

    private fun File.isExecutableFile(): Boolean = isFile && canExecute()
}
