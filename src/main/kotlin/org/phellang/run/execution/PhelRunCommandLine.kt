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

    /**
     * `phel run [options] [--] <path> [<argv>...]`, so [arguments] follow the script.
     *
     * The `--` is emitted only when there are arguments, and it is what makes them safe: an argument
     * of `-v` would otherwise be read as an option to `phel` itself rather than passed through to the
     * script. With no arguments there is nothing to guard, and the command stays as short as the one
     * a user already reads in the console.
     */
    fun run(
        binary: File,
        scriptPath: String,
        workingDirectory: String,
        arguments: List<String> = emptyList(),
    ): GeneralCommandLine {
        val positional = if (arguments.isEmpty()) listOf(scriptPath) else listOf(END_OF_OPTIONS, scriptPath) + arguments

        return command(binary, "run", positional, workingDirectory)
    }

    fun repl(binary: File, workingDirectory: String): GeneralCommandLine =
        command(binary, "repl", emptyList(), workingDirectory)

    /**
     * With no paths, `phel test` runs the whole suite as configured by `phel-config.php`.
     *
     * Both reporters are requested when [reportFile] is given: `default` keeps the console output
     * the user watches during the run, and `junit-xml` writes the machine-readable report the test
     * tree is built from once the process exits. `--reporter` is documented as repeatable.
     *
     * [filter] narrows the run to a single test; see [exactNameFilter] for why it has to arrive
     * already anchored.
     *
     * There is deliberately no `--ns` companion. Pinning the namespace would close the small hole
     * where a same-named `deftest` in a dependency also matches, but the namespace `phel` matches
     * against is derived from the file's location, not from its `(ns …)` form, and the two differ
     * freely: `tests/html.phel` declaring `(ns test.html …)` is matched as `tests.html`. A wrong pin
     * silently selects nothing, which is a worse failure than the over-match it was guarding.
     */
    fun test(
        binary: File,
        paths: List<String>,
        workingDirectory: String,
        reportFile: File? = null,
        filter: String? = null,
    ): GeneralCommandLine {
        val reporters = if (reportFile == null) {
            emptyList()
        } else {
            listOf("--reporter=default", "--reporter=junit-xml", "$OUTPUT_OPTION${reportFile.absolutePath}")
        }

        val selectors = if (filter.isNullOrBlank()) emptyList() else listOf("$FILTER_OPTION$filter")

        return command(binary, "test", reporters + selectors + paths, workingDirectory)
    }

    /**
     * An anchored PCRE selecting [testName] and nothing else.
     *
     * `--filter` treats a bare fragment as an *unanchored substring*: `compile-filter-regex` in
     * `phel/test/selector.phel` runs it through `preg_quote` and wraps it in `/.../`, so the bare
     * name `basic-tags` would also select `basic-tags-extended`. Only a pattern that already opens
     * and closes with `/` is passed through as raw PCRE, so anchoring means delimiting it here.
     *
     * The escaping is done character by character rather than with [Regex.escape], which emits
     * `\Q...\E`: PCRE resolves the `/` delimiter before it honours `\Q`, so a `/` inside the name
     * would still cut the pattern short. Characters above ASCII are left alone — they carry no
     * special meaning in PCRE, and it is backslash-escaping them that is unsafe.
     */
    fun exactNameFilter(testName: String): String =
        testName.map { char ->
            when {
                char in 'a'..'z' || char in 'A'..'Z' || char in '0'..'9' || char == '_' -> char.toString()
                char.code > 127 -> char.toString()
                else -> "\\$char"
            }
        }.joinToString("", prefix = "/^", postfix = "$/")

    /** Where `junit-xml` writes. Read back off the command line rather than passed around beside it. */
    const val OUTPUT_OPTION = "--output="

    private const val FILTER_OPTION = "--filter="

    /** Everything after this is positional, whatever it starts with. */
    private const val END_OF_OPTIONS = "--"

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
