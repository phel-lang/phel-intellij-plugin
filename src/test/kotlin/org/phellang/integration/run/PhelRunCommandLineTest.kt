package org.phellang.integration.run

import com.intellij.util.EnvironmentUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.run.execution.PhelRunCommandLine
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * The `phel run` invocation, asserted without a run configuration.
 *
 * The environment case is the one that regressed for the formatter in #257: `vendor/bin/phel` starts
 * `#!/usr/bin/env php`, and an IDE launched from Dock or Spotlight hands its children a `PATH` with
 * no Homebrew, Herd, asdf or mise `php` on it. Running has exactly the same exposure.
 */
class PhelRunCommandLineTest : PhelIntegrationTestCase() {

    private val binary = File("/project/vendor/bin/phel")
    private val script = "/project/src/main.phel"
    private val workingDir = "/project"

    fun testInvokesRunWithTheScriptPath() {
        val command = PhelRunCommandLine.run(binary, script, workingDir).getCommandLineList(null)

        assertEquals(listOf(binary.absolutePath, "run", script), command)
    }

    fun testRunsInTheGivenWorkingDirectory() {
        val commandLine = PhelRunCommandLine.run(binary, script, workingDir)

        assertEquals(File(workingDir), commandLine.workDirectory)
    }

    fun testCarriesTheShellEnvironment() {
        val environment = PhelRunCommandLine.run(binary, script, workingDir).environment

        val shell = EnvironmentUtil.getEnvironmentMap()
        val missing = shell.filter { (key, value) -> environment[key] != value }

        assertTrue("the shell environment must reach the child process, missing: ${missing.keys}", missing.isEmpty())
    }

    fun testDecodesOutputAsUtf8() {
        assertEquals(StandardCharsets.UTF_8, PhelRunCommandLine.run(binary, script, workingDir).charset)
    }

    fun testInvokesReplWithNoArguments() {
        val command = PhelRunCommandLine.repl(binary, workingDir).getCommandLineList(null)

        assertEquals(listOf(binary.absolutePath, "repl"), command)
    }

    fun testInvokesTestOverTheWholeSuiteWhenNoPathsGiven() {
        val command = PhelRunCommandLine.test(binary, emptyList(), workingDir).getCommandLineList(null)

        assertEquals(listOf(binary.absolutePath, "test"), command)
    }

    fun testInvokesTestWithTheGivenPaths() {
        val command = PhelRunCommandLine.test(binary, listOf("tests/a.phel", "tests/b.phel"), workingDir)
            .getCommandLineList(null)

        assertEquals(listOf(binary.absolutePath, "test", "tests/a.phel", "tests/b.phel"), command)
    }

    fun testEverySubcommandCarriesTheShellEnvironment() {
        val shell = EnvironmentUtil.getEnvironmentMap()

        for (commandLine in listOf(
            PhelRunCommandLine.run(binary, script, workingDir),
            PhelRunCommandLine.repl(binary, workingDir),
            PhelRunCommandLine.test(binary, emptyList(), workingDir),
        )) {
            val missing = shell.filter { (key, value) -> commandLine.environment[key] != value }
            assertTrue("shell environment must reach ${commandLine.commandLineString}: ${missing.keys}", missing.isEmpty())
        }
    }

    fun testTestRequestsBothReportersWhenGivenAReportFile() {
        val report = java.io.File("/tmp/phel-report.xml")

        val command = PhelRunCommandLine.test(binary, emptyList(), workingDir, report).getCommandLineList(null)

        assertEquals(
            listOf(
                binary.absolutePath,
                "test",
                "--reporter=default",
                "--reporter=junit-xml",
                "--output=${report.absolutePath}",
            ),
            command,
        )
    }

    /** Options come before paths: the CLI usage is `test [options] [--] [<paths>...]`. */
    fun testTestPutsReporterOptionsBeforePaths() {
        val report = java.io.File("/tmp/phel-report.xml")

        val command = PhelRunCommandLine.test(binary, listOf("tests/a.phel"), workingDir, report)
            .getCommandLineList(null)

        assertEquals("tests/a.phel", command.last())
        assertTrue(command.indexOf("--reporter=junit-xml") < command.indexOf("tests/a.phel"))
    }

    /** A path with a space in it is still one argument, not two. */
    fun testTestPassesASpacedPathAsASingleArgument() {
        val spaced = "/Users/me/My Projects/app/tests/html.phel"

        val command = PhelRunCommandLine.test(binary, listOf(spaced), workingDir).getCommandLineList(null)

        assertEquals(listOf(binary.absolutePath, "test", spaced), command)
    }

    fun testTestOmitsSelectorsWhenNoneAreGiven() {
        val command = PhelRunCommandLine.test(binary, listOf("tests/a.phel"), workingDir).getCommandLineList(null)

        assertEquals(listOf(binary.absolutePath, "test", "tests/a.phel"), command)
    }

    fun testTestNarrowsToASingleTestWithAFilter() {
        val command = PhelRunCommandLine.test(
            binary,
            listOf("tests/html.phel"),
            workingDir,
            filter = PhelRunCommandLine.exactNameFilter("basic-tags"),
        ).getCommandLineList(null)

        assertEquals(
            listOf(binary.absolutePath, "test", "--filter=/^basic\\-tags$/", "tests/html.phel"),
            command,
        )
    }

    /**
     * No `--ns` companion. `phel` derives the namespace it matches from the file's location, not
     * from its `(ns …)` form, so a pin built from the declared name selects nothing at all:
     * `tests/html.phel` declaring `(ns test.html …)` is matched as `tests.html`.
     */
    fun testTestNeverPinsTheNamespace() {
        val command = PhelRunCommandLine.test(
            binary,
            listOf("tests/html.phel"),
            workingDir,
            filter = PhelRunCommandLine.exactNameFilter("basic-tags"),
        ).getCommandLineList(null)

        assertFalse(command.toString(), command.any { it.startsWith("--ns") })
    }

    fun testTestPutsSelectorsBeforePaths() {
        val command = PhelRunCommandLine.test(
            binary,
            listOf("tests/html.phel"),
            workingDir,
            java.io.File("/tmp/phel-report.xml"),
            filter = PhelRunCommandLine.exactNameFilter("basic-tags"),
        ).getCommandLineList(null)

        assertEquals("tests/html.phel", command.last())
        assertTrue(command.indexOf("--filter=/^basic\\-tags$/") < command.indexOf("tests/html.phel"))
    }

    fun testABlankFilterIsNotPassedAsAnEmptyOption() {
        val command = PhelRunCommandLine.test(binary, listOf("tests/a.phel"), workingDir, filter = "")
            .getCommandLineList(null)

        assertEquals(listOf(binary.absolutePath, "test", "tests/a.phel"), command)
    }

    /**
     * The pattern must survive `compile-filter-regex` in `phel/test/selector.phel` unchanged, which
     * only happens when it is longer than one character and both opens and closes with `/`.
     * Anything else is `preg_quote`d into an unanchored substring match.
     */
    fun testAnExactFilterIsHandedToPhelAsRawPcre() {
        for (name in listOf("basic-tags", "empty?", "parse*", "a/b", "ok")) {
            val pattern = PhelRunCommandLine.exactNameFilter(name)

            assertTrue(pattern, pattern.length > 1)
            assertTrue(pattern, pattern.startsWith("/") && pattern.endsWith("/"))
        }
    }

    /** Anchored, so `basic-tags` cannot also select `basic-tags-extended`. */
    fun testAnExactFilterAnchorsBothEnds() {
        assertEquals("/^basic\\-tags$/", PhelRunCommandLine.exactNameFilter("basic-tags"))
    }

    /** Metacharacters that are idiomatic in Phel names must match literally, not as regex syntax. */
    fun testAnExactFilterEscapesRegexMetacharacters() {
        assertEquals("/^empty\\?$/", PhelRunCommandLine.exactNameFilter("empty?"))
        assertEquals("/^parse\\*$/", PhelRunCommandLine.exactNameFilter("parse*"))
        assertEquals("/^a\\.b$/", PhelRunCommandLine.exactNameFilter("a.b"))
        assertEquals("/^x\\+y$/", PhelRunCommandLine.exactNameFilter("x+y"))
    }

    /** The delimiter itself, which `\Q...\E` would not have protected. */
    fun testAnExactFilterEscapesTheDelimiter() {
        assertEquals("/^a\\/b$/", PhelRunCommandLine.exactNameFilter("a/b"))
    }

    fun testAnExactFilterLeavesLettersDigitsAndUnderscoreAlone() {
        assertEquals("/^abc_123$/", PhelRunCommandLine.exactNameFilter("abc_123"))
    }
}
