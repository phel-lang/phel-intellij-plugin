package org.phellang.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
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

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = PhelRunConfigurationEditor(project)

    override fun commandLine(binary: File): GeneralCommandLine =
        PhelRunCommandLine.run(binary, scriptPath, effectiveWorkingDirectory())

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
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        scriptPath = JDOMExternalizerUtil.readField(element, SCRIPT_PATH_FIELD).orEmpty()
    }

    private companion object {
        const val SCRIPT_PATH_FIELD = "SCRIPT_PATH"
    }
}
