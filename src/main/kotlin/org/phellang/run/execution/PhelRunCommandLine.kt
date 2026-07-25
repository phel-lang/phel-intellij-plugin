package org.phellang.run.execution

import com.intellij.execution.configurations.GeneralCommandLine
import org.phellang.core.cli.PhelCliEnvironment
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * Builds the `phel` invocations the run configurations need.
 *
 * Kept apart from the configurations so a command can be asserted on without constructing a project:
 * the subcommand, the argument order and the inherited environment are the parts that actually
 * break.
 *
 * Subcommand names are verified against
 * https://phel-lang.org/documentation/tooling/cli-commands/ — `run [options] [--] <path> [<argv>...]`,
 * `repl`, and `test [options] [--] [<paths>...]`.
 */
object PhelRunCommandLine {

    fun run(binary: File, scriptPath: String, workingDirectory: String): GeneralCommandLine =
        command(binary, "run", listOf(scriptPath), workingDirectory)

    fun repl(binary: File, workingDirectory: String): GeneralCommandLine =
        command(binary, "repl", emptyList(), workingDirectory)

    /** With no paths, `phel test` runs the whole suite as configured by `phel-config.php`. */
    fun test(binary: File, paths: List<String>, workingDirectory: String): GeneralCommandLine =
        command(binary, "test", paths, workingDirectory)

    private fun command(
        binary: File,
        subcommand: String,
        arguments: List<String>,
        workingDirectory: String,
    ): GeneralCommandLine =
        GeneralCommandLine(binary.absolutePath)
            .withParameters(listOf(subcommand) + arguments)
            .withWorkDirectory(workingDirectory)
            .withCharset(StandardCharsets.UTF_8)
            // The same login-shell environment the formatter needs: `vendor/bin/phel` is a PHP
            // script, and an IDE started from Dock or Spotlight has no `php` on its PATH.
            .withEnvironment(PhelCliEnvironment.loginShellEnvironment())
}
