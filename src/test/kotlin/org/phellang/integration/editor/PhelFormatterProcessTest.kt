package org.phellang.integration.editor

import com.intellij.util.EnvironmentUtil
import org.phellang.editor.format.PhelFormatterProcess
import org.phellang.integration.PhelIntegrationTestCase
import java.io.File

/**
 * The formatter must run `phel` with the *shell's* environment, not the IDE's.
 *
 * `vendor/bin/phel` starts `#!/usr/bin/env php`, and an IDE launched from Dock or Spotlight on macOS
 * hands its children `PATH=/usr/bin:/bin:/usr/sbin:/sbin` — where a Homebrew, Herd, asdf or mise
 * `php` does not appear. Formatting failed with `env: php: No such file or directory` even though
 * the binary was found and executable.
 */
class PhelFormatterProcessTest : PhelIntegrationTestCase() {

    private val binary = File("/somewhere/vendor/bin/phel")
    private val target = File("/tmp/target.phel")
    private val workingDir = File("/somewhere")
    private val output = File("/tmp/out.log")

    fun testCarriesTheShellEnvironment() {
        val environment = PhelFormatterProcess.command(binary, target, workingDir, output).environment()

        val shell = EnvironmentUtil.getEnvironmentMap()
        val missing = shell.filter { (key, value) -> environment[key] != value }

        assertTrue("the shell environment must reach the child process, missing: ${missing.keys}", missing.isEmpty())
    }

    fun testInvokesFmtOnTheTargetFile() {
        val command = PhelFormatterProcess.command(binary, target, workingDir, output).command()

        assertEquals(listOf(binary.absolutePath, "fmt", target.absolutePath), command)
    }

    fun testRunsInTheProjectDirectory() {
        assertEquals(workingDir, PhelFormatterProcess.command(binary, target, workingDir, output).directory())
    }

    /** To a file, not a pipe: nothing drains the stream before the wait, so a pipe could deadlock. */
    fun testRedirectsOutputToAFileWithStderrMerged() {
        val builder = PhelFormatterProcess.command(binary, target, workingDir, output)

        assertEquals(output, builder.redirectOutput().file())
        assertTrue(builder.redirectErrorStream())
    }
}
