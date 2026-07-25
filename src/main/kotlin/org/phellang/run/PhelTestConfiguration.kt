package org.phellang.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import org.phellang.run.execution.PhelRunCommandLine
import org.phellang.run.settings.PhelTestConfigurationEditor
import java.io.File

/**
 * Runs `phel test`, over the whole suite or over named paths.
 *
 * Output goes to the plain console rather than a test tree. `phel test` takes a `--reporter`, which
 * is the hook an SM tree would need, but mapping a reporter's output onto
 * `SMTRunnerConsoleProperties` means pinning a format this plugin cannot verify from here. Running
 * the suite without leaving the IDE is the part worth having first.
 */
class PhelTestConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : PhelCliRunConfiguration(project, factory, name) {

    /** Space-separated paths, empty meaning the whole suite. */
    var testPaths: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = PhelTestConfigurationEditor(project)

    override fun commandLine(binary: File): GeneralCommandLine =
        PhelRunCommandLine.test(binary, paths(), effectiveWorkingDirectory())

    fun paths(): List<String> = testPaths.split(' ', '\n').map(String::trim).filter(String::isNotEmpty)

    override fun suggestedName(): String = if (testPaths.isBlank()) "All tests" else "Tests: $testPaths"

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, TEST_PATHS_FIELD, testPaths)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        testPaths = JDOMExternalizerUtil.readField(element, TEST_PATHS_FIELD).orEmpty()
    }

    private companion object {
        const val TEST_PATHS_FIELD = "TEST_PATHS"
    }
}
