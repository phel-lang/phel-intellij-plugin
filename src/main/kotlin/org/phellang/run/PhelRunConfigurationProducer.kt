package org.phellang.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.phellang.language.psi.files.PhelFile
import org.phellang.run.test.PhelTestDetection

/**
 * Makes Run available from a `.phel` file's context menu, editor gutter and Run Anything.
 *
 * Parameterised on [PhelCliRunConfiguration] for the same reason as [PhelTestConfigurationProducer]:
 * the file, REPL and test configurations share one `ConfigurationType`, so the platform offers this
 * producer configurations that are not [PhelRunConfiguration]s, and a narrower type parameter turns
 * that into a `ClassCastException` in the generated bridge method.
 */
class PhelRunConfigurationProducer : LazyRunConfigurationProducer<PhelCliRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory =
        PhelRunConfigurationType.fileFactory()

    override fun setupConfigurationFromContext(
        configuration: PhelCliRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val runConfiguration = configuration as? PhelRunConfiguration ?: return false
        val virtualFile = phelFilePath(context) ?: return false

        runConfiguration.scriptPath = virtualFile
        runConfiguration.workingDirectory = context.project.basePath.orEmpty()
        runConfiguration.setName(runConfiguration.suggestedName())
        return true
    }

    override fun isConfigurationFromContext(
        configuration: PhelCliRunConfiguration,
        context: ConfigurationContext,
    ): Boolean {
        val runConfiguration = configuration as? PhelRunConfiguration ?: return false

        return phelFilePath(context) == runConfiguration.scriptPath
    }

    /**
     * Test files are declined so [PhelTestConfigurationProducer] takes them. Declining outright,
     * rather than leaning on producer precedence, keeps which one wins independent of the platform's
     * ordering rules.
     */
    private fun phelFilePath(context: ConfigurationContext): String? {
        val file = context.psiLocation?.containingFile as? PhelFile ?: return null
        if (PhelTestDetection.isTestFile(file)) return null

        return file.virtualFile?.path
    }
}
