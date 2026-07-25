package org.phellang.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import org.phellang.language.infrastructure.PhelIcons
import javax.swing.Icon

class PhelRunConfigurationType : ConfigurationType {

    private val factory = PhelRunConfigurationFactory(this)

    override fun getDisplayName(): String = "Phel"

    override fun getConfigurationTypeDescription(): String = "Run a Phel file with the project's phel CLI"

    override fun getIcon(): Icon = PhelIcons.FILE

    override fun getId(): String = ID

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(factory)

    companion object {
        /** Persisted in workspace.xml against every saved configuration; renaming it orphans them. */
        const val ID = "PhelRunConfiguration"

        fun getInstance(): PhelRunConfigurationType =
            ConfigurationTypeUtil.findConfigurationType(PhelRunConfigurationType::class.java)
    }
}

class PhelRunConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "Phel"

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        PhelRunConfiguration(project, this, "Phel")
}
