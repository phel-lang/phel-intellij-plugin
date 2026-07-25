package org.phellang.run.test

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import java.io.File

/**
 * Runs `phel test` and, once it exits, replays its report as service messages so the test tree is
 * populated.
 *
 * `phel test` writes JUnit XML to a file rather than streaming events, so there is nothing to
 * publish until the process is done. The messages are emitted *before* termination is announced:
 * the runner stops accepting them afterwards, and the tree would stay empty.
 */
class PhelTestProcessHandler(
    commandLine: GeneralCommandLine,
    private val reportFile: File,
) : KillableColoredProcessHandler(commandLine) {

    override fun notifyProcessTerminated(exitCode: Int) {
        for (line in PhelTestServiceMessages.render(readReport())) {
            notifyTextAvailable(line + "\n", ProcessOutputTypes.STDOUT)
        }

        super.notifyProcessTerminated(exitCode)
    }

    /**
     * A missing or unreadable report becomes no results rather than an error: the run may have been
     * stopped, or `phel` may have failed before writing anything, and either way the console already
     * shows what happened.
     */
    private fun readReport(): PhelTestReport {
        if (!reportFile.isFile) return PhelTestReport.EMPTY

        return try {
            PhelJUnitXmlParser.parse(reportFile.readText())
        } catch (e: Exception) {
            PhelTestReport.EMPTY
        } finally {
            reportFile.delete()
        }
    }
}
