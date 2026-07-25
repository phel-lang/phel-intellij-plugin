package org.phellang.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import org.phellang.language.infrastructure.PhelIcons
import javax.swing.Icon

class PhelRunConfigurationType : ConfigurationType {

    private val fileFactory = PhelRunConfigurationFactory(this)
    private val replFactory = PhelReplConfigurationFactory(this)
    private val testFactory = PhelTestConfigurationFactory(this)

    override fun getDisplayName(): String = "Phel"

    override fun getConfigurationTypeDescription(): String = "Run Phel through the project's phel CLI"

    override fun getIcon(): Icon = PhelIcons.FILE

    override fun getId(): String = ID

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
        arrayOf(fileFactory, replFactory, testFactory)

    companion object {
        /** Persisted in workspace.xml against every saved configuration; renaming it orphans them. */
        const val ID = "PhelRunConfiguration"

        fun getInstance(): PhelRunConfigurationType =
            ConfigurationTypeUtil.findConfigurationType(PhelRunConfigurationType::class.java)

        /** The factory the file-running configuration uses, which the context producer creates. */
        fun fileFactory(): ConfigurationFactory = getInstance().fileFactory
    }
}

/**
 * Factory ids are persisted alongside the type id. `Phel` is the original and keeps its name so
 * configurations saved before the REPL and test factories existed still load.
 */
class PhelRunConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "Phel"

    override fun getName(): String = "Phel file"

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        PhelRunConfiguration(project, this, "Phel")
}

class PhelReplConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "PhelRepl"

    override fun getName(): String = "Phel REPL"

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        PhelReplConfiguration(project, this, "Phel REPL")
}

class PhelTestConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "PhelTest"

    override fun getName(): String = "Phel tests"

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        PhelTestConfiguration(project, this, "Phel tests")
}
