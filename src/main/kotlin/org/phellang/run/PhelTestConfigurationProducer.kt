package org.phellang.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import org.phellang.language.psi.files.PhelFile
import org.phellang.run.test.PhelTestDetection

/**
 * Sends a test file to `phel test`, and so into the test tree, rather than to `phel run`.
 *
 * Scope follows the context: inside a `deftest` it is that one test, anywhere else in the file it is
 * the whole file. `PhelRunConfigurationProducer` declines test files outright, which is what keeps
 * the two from competing.
 *
 * Parameterised on [PhelCliRunConfiguration], not on [PhelTestConfiguration], because the platform
 * offers every configuration of a *type* to every producer registered for it, and the file, REPL and
 * test configurations all share `PhelRunConfigurationType` — they differ only by factory. Narrowing
 * the type parameter instead made the generated bridge cast a `PhelRunConfiguration` to
 * `PhelTestConfiguration`, and the resulting `ClassCastException` killed the whole gutter popup, so
 * clicking Run reported "Nothing here" on *every* Phel file. The `as?` below is the real guard.
 */
class PhelTestConfigurationProducer : LazyRunConfigurationProducer<PhelCliRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory = PhelRunConfigurationType.testFactory()

    override fun setupConfigurationFromContext(
        configuration: PhelCliRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val testConfiguration = configuration as? PhelTestConfiguration ?: return false
        val location = locate(context) ?: return false

        testConfiguration.testPaths = location.path
        testConfiguration.testName = location.testName
        testConfiguration.workingDirectory = context.project.basePath.orEmpty()
        testConfiguration.setName(testConfiguration.suggestedName())
        return true
    }

    /**
     * The test name as well as the path: comparing the path alone would make a single-test run
     * indistinguishable from a whole-file run of the same file, and the second invocation would
     * silently reuse the first configuration and run the wrong set of tests.
     */
    override fun isConfigurationFromContext(
        configuration: PhelCliRunConfiguration,
        context: ConfigurationContext,
    ): Boolean {
        val testConfiguration = configuration as? PhelTestConfiguration ?: return false
        val location = locate(context) ?: return false

        return testConfiguration.testPaths == location.path && testConfiguration.testName == location.testName
    }

    private fun locate(context: ConfigurationContext): TestLocation? {
        val element = context.psiLocation ?: return null
        val file = element.containingFile as? PhelFile ?: return null
        if (!PhelTestDetection.isTestFile(file)) return null

        val path = file.virtualFile?.path ?: return null

        return TestLocation(path, PhelTestDetection.enclosingDeftestName(element).orEmpty())
    }

    private data class TestLocation(val path: String, val testName: String)
}
