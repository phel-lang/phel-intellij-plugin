package org.phellang.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.phellang.language.psi.files.PhelFile

/** Makes Run available from a `.phel` file's context menu, editor gutter and Run Anything. */
class PhelRunConfigurationProducer : LazyRunConfigurationProducer<PhelRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory =
        PhelRunConfigurationType.getInstance().configurationFactories.first()

    override fun setupConfigurationFromContext(
        configuration: PhelRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val virtualFile = phelFilePath(context) ?: return false

        configuration.scriptPath = virtualFile
        configuration.workingDirectory = context.project.basePath.orEmpty()
        configuration.setName(configuration.suggestedName())
        return true
    }

    override fun isConfigurationFromContext(
        configuration: PhelRunConfiguration,
        context: ConfigurationContext,
    ): Boolean = phelFilePath(context) == configuration.scriptPath

    private fun phelFilePath(context: ConfigurationContext): String? {
        val file = context.psiLocation?.containingFile as? PhelFile ?: return null
        return file.virtualFile?.path
    }
}
