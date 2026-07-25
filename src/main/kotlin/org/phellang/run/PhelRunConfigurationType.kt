package org.phellang.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import org.phellang.language.infrastructure.PhelIcons
import javax.swing.Icon

class PhelRunConfigurationType : ConfigurationType {

    /**
     * Factory ids are persisted alongside the type id. `Phel` is the original and keeps its name so
     * configurations saved before the REPL and test factories existed still load.
     */
    private val fileFactory = factory("Phel", "Phel file") { project, factory ->
        PhelRunConfiguration(project, factory, "Phel")
    }

    private val replFactory = factory("PhelRepl", "Phel REPL") { project, factory ->
        PhelReplConfiguration(project, factory, "Phel REPL")
    }

    private val testFactory = factory("PhelTest", "Phel tests") { project, factory ->
        PhelTestConfiguration(project, factory, "Phel tests")
    }

    override fun getDisplayName(): String = "Phel"

    override fun getConfigurationTypeDescription(): String = "Run Phel through the project's phel CLI"

    override fun getIcon(): Icon = PhelIcons.FILE

    override fun getId(): String = ID

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
        arrayOf(fileFactory, replFactory, testFactory)

    private fun factory(
        id: String,
        displayName: String,
        create: (Project, ConfigurationFactory) -> RunConfiguration,
    ): ConfigurationFactory = object : ConfigurationFactory(this) {

        override fun getId(): String = id

        override fun getName(): String = displayName

        override fun createTemplateConfiguration(project: Project): RunConfiguration = create(project, this)
    }

    companion object {
        /** Persisted in workspace.xml against every saved configuration; renaming it orphans them. */
        const val ID = "PhelRunConfiguration"

        fun getInstance(): PhelRunConfigurationType =
            ConfigurationTypeUtil.findConfigurationType(PhelRunConfigurationType::class.java)

        /** The factory the context producer creates a file-running configuration from. */
        fun fileFactory(): ConfigurationFactory = getInstance().fileFactory
    }
}
