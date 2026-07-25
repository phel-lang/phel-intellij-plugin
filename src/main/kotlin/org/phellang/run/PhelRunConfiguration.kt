package org.phellang.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import org.phellang.core.cli.PhelCliLocator
import org.phellang.run.execution.PhelRunCommandLine
import org.phellang.run.settings.PhelRunConfigurationEditor
import java.io.File

class PhelRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : LocatableConfigurationBase<RunProfileState>(project, factory, name) {

    var scriptPath: String = ""
    var workingDirectory: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = PhelRunConfigurationEditor(project)

    override fun checkConfiguration() {
        if (scriptPath.isBlank()) {
            throw RuntimeConfigurationError("No Phel file specified")
        }
        if (!File(scriptPath).isFile) {
            throw RuntimeConfigurationError("Phel file does not exist: $scriptPath")
        }
        val basePath = project.basePath ?: throw RuntimeConfigurationError("No project base path available")
        if (PhelCliLocator.locate(basePath) == null) {
            throw RuntimeConfigurationError(PhelCliLocator.NOT_FOUND_MESSAGE)
        }
    }

    /**
     * Resolved again at launch rather than carried over from [checkConfiguration]: a `composer
     * install` between validating the dialog and pressing Run is exactly when this changes.
     */
    private fun requireBinary(): File {
        val basePath = project.basePath ?: throw ExecutionException("No project base path available")
        return PhelCliLocator.locate(basePath) ?: throw ExecutionException(PhelCliLocator.NOT_FOUND_MESSAGE)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState =
        PhelRunState(environment)

    /** Falls back to the project root, which is where `phel run` expects to resolve `phel-config.php`. */
    fun effectiveWorkingDirectory(): String = workingDirectory.ifBlank { project.basePath.orEmpty() }

    override fun suggestedName(): String = File(scriptPath).name.ifBlank { "Phel" }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, SCRIPT_PATH_FIELD, scriptPath)
        JDOMExternalizerUtil.writeField(element, WORKING_DIRECTORY_FIELD, workingDirectory)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        scriptPath = JDOMExternalizerUtil.readField(element, SCRIPT_PATH_FIELD).orEmpty()
        workingDirectory = JDOMExternalizerUtil.readField(element, WORKING_DIRECTORY_FIELD).orEmpty()
    }

    private inner class PhelRunState(environment: ExecutionEnvironment) : CommandLineState(environment) {

        override fun startProcess(): ProcessHandler {
            val commandLine = PhelRunCommandLine.build(requireBinary(), scriptPath, effectiveWorkingDirectory())

            // Killable so Stop actually terminates a long-running script rather than detaching from it.
            val handler = KillableColoredProcessHandler(commandLine)
            ProcessTerminatedListener.attach(handler)
            return handler
        }
    }

    private companion object {
        // Field names are persisted in workspace.xml; changing one silently drops the saved value.
        const val SCRIPT_PATH_FIELD = "SCRIPT_PATH"
        const val WORKING_DIRECTORY_FIELD = "WORKING_DIRECTORY"
    }
}
