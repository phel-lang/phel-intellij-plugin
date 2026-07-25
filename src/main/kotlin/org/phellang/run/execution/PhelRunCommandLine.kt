package org.phellang.run.execution

import com.intellij.execution.configurations.GeneralCommandLine
import org.phellang.core.cli.PhelCliEnvironment
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * Builds the `phel run <path>` invocation.
 *
 * Kept apart from the run configuration so the command can be asserted on without constructing a
 * project: the argument order and the inherited environment are the parts that actually break.
 */
object PhelRunCommandLine {

    /** Verified against https://phel-lang.org/documentation/tooling/cli-commands/: `run [options] [--] <path> [<argv>...]`. */
    private const val RUN_COMMAND = "run"

    fun build(binary: File, scriptPath: String, workingDirectory: String): GeneralCommandLine =
        GeneralCommandLine(binary.absolutePath, RUN_COMMAND, scriptPath)
            .withWorkDirectory(workingDirectory)
            .withCharset(StandardCharsets.UTF_8)
            // The same login-shell environment the formatter needs: `vendor/bin/phel` is a PHP
            // script, and an IDE started from Dock or Spotlight has no `php` on its PATH.
            .withEnvironment(PhelCliEnvironment.loginShellEnvironment())
}
