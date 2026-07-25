package org.phellang.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import org.phellang.core.cli.PhelCliLocator
import java.io.File

/**
 * What every `phel` invocation launched from the IDE has in common: finding the binary, choosing a
 * working directory, and running it in a killable console.
 *
 * Subclasses supply only the command line. Without this the run, REPL and test configurations would
 * each carry their own copy of the binary lookup and process wiring, and the three would drift.
 */
abstract class PhelCliRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : LocatableConfigurationBase<RunProfileState>(project, factory, name) {

    var workingDirectory: String = ""

    protected abstract fun commandLine(binary: File): GeneralCommandLine

    /** Falls back to the project root, which is where `phel` resolves `phel-config.php`. */
    fun effectiveWorkingDirectory(): String = workingDirectory.ifBlank { project.basePath.orEmpty() }

    override fun checkConfiguration() {
        val basePath = project.basePath ?: throw RuntimeConfigurationError("No project base path available")
        if (PhelCliLocator.locate(basePath) == null) {
            throw RuntimeConfigurationError(PhelCliLocator.NOT_FOUND_MESSAGE)
        }
    }

    /**
     * Resolved again at launch rather than carried over from [checkConfiguration]: a `composer
     * install` between validating the dialog and pressing Run is exactly when this changes.
     */
    protected fun requireBinary(): File {
        val basePath = project.basePath ?: throw ExecutionException("No project base path available")
        return PhelCliLocator.locate(basePath) ?: throw ExecutionException(PhelCliLocator.NOT_FOUND_MESSAGE)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState =
        object : CommandLineState(environment) {
            override fun startProcess(): ProcessHandler {
                // Killable so Stop terminates a long-running script or a REPL waiting on input,
                // rather than detaching from it.
                val handler = KillableColoredProcessHandler(commandLine(requireBinary()))
                ProcessTerminatedListener.attach(handler)
                return handler
            }
        }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, WORKING_DIRECTORY_FIELD, workingDirectory)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        workingDirectory = JDOMExternalizerUtil.readField(element, WORKING_DIRECTORY_FIELD).orEmpty()
    }

    protected companion object {
        // Field names are persisted in workspace.xml; changing one silently drops the saved value.
        const val WORKING_DIRECTORY_FIELD = "WORKING_DIRECTORY"
    }
}
