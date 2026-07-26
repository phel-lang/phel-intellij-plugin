package org.phellang.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import com.intellij.util.execution.ParametersListUtil
import org.jdom.Element
import org.phellang.run.execution.PhelRunCommandLine
import org.phellang.run.settings.PhelRunConfigurationEditor
import java.io.File

/** Runs one `.phel` file through `phel run`. */
class PhelRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : PhelCliRunConfiguration(project, factory, name) {

    var scriptPath: String = ""

    /**
     * Arguments passed to the script, encoded as a parameter list.
     *
     * `phel run` forwards these to the script, where they arrive as `*argv*`. Encoded rather than
     * split on spaces so an argument may contain one, the same way [PhelTestConfiguration] holds its
     * paths.
     */
    var programArguments: String = ""

    fun arguments(): List<String> = ParametersListUtil.parse(programArguments)

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = PhelRunConfigurationEditor(project)

    override fun commandLine(binary: File): GeneralCommandLine =
        PhelRunCommandLine.run(binary, scriptPath, effectiveWorkingDirectory(), arguments())

    override fun checkConfiguration() {
        if (scriptPath.isBlank()) {
            throw RuntimeConfigurationError("No Phel file specified")
        }
        if (!File(scriptPath).isFile) {
            throw RuntimeConfigurationError("Phel file does not exist: $scriptPath")
        }
        super.checkConfiguration()
    }

    override fun suggestedName(): String = File(scriptPath).name.ifBlank { "Phel" }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, SCRIPT_PATH_FIELD, scriptPath)
        JDOMExternalizerUtil.writeField(element, PROGRAM_ARGUMENTS_FIELD, programArguments)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        scriptPath = JDOMExternalizerUtil.readField(element, SCRIPT_PATH_FIELD).orEmpty()
        programArguments = JDOMExternalizerUtil.readField(element, PROGRAM_ARGUMENTS_FIELD).orEmpty()
    }

    private companion object {
        // Persisted in workspace.xml; append-only, so a configuration saved before PROGRAM_ARGUMENTS
        // existed still reads back as the plain script run it was.
        const val SCRIPT_PATH_FIELD = "SCRIPT_PATH"
        const val PROGRAM_ARGUMENTS_FIELD = "PROGRAM_ARGUMENTS"
    }
}
