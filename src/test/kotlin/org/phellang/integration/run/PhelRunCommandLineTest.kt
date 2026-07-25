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
}
