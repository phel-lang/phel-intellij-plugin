package org.phellang.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.phellang.run.execution.PhelRunCommandLine
import org.phellang.run.settings.PhelWorkingDirectoryEditor
import java.io.File

/**
 * Runs `phel repl` in the Run console.
 *
 * The console the platform builds for a [com.intellij.execution.configurations.CommandLineState]
 * accepts typed input and forwards it to the process, which is what makes an interactive prompt work
 * here without a dedicated tool window.
 */
class PhelReplConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : PhelCliRunConfiguration(project, factory, name) {

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> =
        PhelWorkingDirectoryEditor(project, "Directory the REPL starts in; defaults to the project root")

    override fun commandLine(binary: File): GeneralCommandLine =
        PhelRunCommandLine.repl(binary, effectiveWorkingDirectory())

    override fun suggestedName(): String = "Phel REPL"
}
