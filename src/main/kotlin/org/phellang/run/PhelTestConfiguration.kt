package org.phellang.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import org.phellang.run.execution.PhelRunCommandLine
import org.phellang.run.settings.PhelTestConfigurationEditor
import org.phellang.run.test.PhelTestConsoleProperties
import org.phellang.run.test.PhelTestProcessHandler
import java.io.File

/**
 * Runs `phel test`, over the whole suite or over named paths, into a test tree.
 *
 * The tree is built from the `junit-xml` reporter, written to a temporary file and read when the
 * process exits. That reporter rather than `tap` because JUnit XML is a de-facto standard whose
 * parser can be pinned down by tests without a `phel` binary to run; the cost is that the tree
 * appears at the end of the run rather than filling in live, while the console shows the default
 * reporter's output throughout.
 */
class PhelTestConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : PhelCliRunConfiguration(project, factory, name) {

    /** Space-separated paths, empty meaning the whole suite. */
    var testPaths: String = ""

    /**
     * A single test to run, empty meaning every test in scope.
     *
     * Held as the plain name rather than as a `--filter` pattern so that what the run dialog shows
     * is a test name, and so that every run is anchored: `--filter` matches unanchored substrings,
     * so a hand-typed `basic-tags` would silently also run `basic-tags-extended`. The anchoring is
     * applied on the way out, in [commandLine].
     */
    var testName: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = PhelTestConfigurationEditor(project)

    /**
     * A fresh report file per launch. The command line is the single place its location is decided;
     * the process handler reads it back off the command line it is given.
     */
    override fun commandLine(binary: File): GeneralCommandLine =
        PhelRunCommandLine.test(
            binary,
            paths(),
            effectiveWorkingDirectory(),
            FileUtil.createTempFile("phel-test-", ".xml", true),
            filter = testName.ifBlank { null }?.let(PhelRunCommandLine::exactNameFilter),
        )

    override fun createProcessHandler(commandLine: GeneralCommandLine): ProcessHandler =
        PhelTestProcessHandler(commandLine)

    override fun createConsoleView(executor: Executor): ConsoleView {
        val properties = PhelTestConsoleProperties(this, executor)
        return SMTestRunnerConnectionUtil.createConsole(properties.testFrameworkName, properties)
    }

    fun paths(): List<String> = testPaths.split(' ', '\n').map(String::trim).filter(String::isNotEmpty)

    /** File names rather than whole paths: the producer stores absolute ones, and the run widget is narrow. */
    override fun suggestedName(): String {
        if (testName.isNotBlank()) return testName

        val paths = paths()
        if (paths.isEmpty()) return "All tests"

        return "Tests: ${paths.joinToString(" ") { File(it).name }}"
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, TEST_PATHS_FIELD, testPaths)
        JDOMExternalizerUtil.writeField(element, TEST_NAME_FIELD, testName)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        testPaths = JDOMExternalizerUtil.readField(element, TEST_PATHS_FIELD).orEmpty()
        testName = JDOMExternalizerUtil.readField(element, TEST_NAME_FIELD).orEmpty()
    }

    private companion object {
        // Persisted in workspace.xml; append-only, so a configuration saved before TEST_NAME existed
        // still reads back as the whole-suite or whole-file run it was.
        const val TEST_PATHS_FIELD = "TEST_PATHS"
        const val TEST_NAME_FIELD = "TEST_NAME"
    }
}
