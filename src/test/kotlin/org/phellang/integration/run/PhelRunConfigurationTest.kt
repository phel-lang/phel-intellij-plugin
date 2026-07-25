package org.phellang.integration.run

import com.intellij.execution.configurations.RuntimeConfigurationError
import org.jdom.Element
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.run.PhelRunConfiguration
import org.phellang.run.PhelRunConfigurationType
import java.io.File

class PhelRunConfigurationTest : PhelIntegrationTestCase() {

    private fun newConfiguration(): PhelRunConfiguration {
        val factory = PhelRunConfigurationType().configurationFactories.first()
        return PhelRunConfiguration(project, factory, "Phel")
    }

    fun testRoundTripsThroughXml() {
        val saved = newConfiguration().apply {
            scriptPath = "/project/src/main.phel"
            workingDirectory = "/project"
        }
        val element = Element("configuration")
        saved.writeExternal(element)

        val loaded = newConfiguration()
        loaded.readExternal(element)

        assertEquals("/project/src/main.phel", loaded.scriptPath)
        assertEquals("/project", loaded.workingDirectory)
    }

    fun testReadsBackEmptyFieldsFromAConfigurationNeverConfigured() {
        val element = Element("configuration")
        newConfiguration().writeExternal(element)

        val loaded = newConfiguration()
        loaded.readExternal(element)

        assertEquals("", loaded.scriptPath)
        assertEquals("", loaded.workingDirectory)
    }

    fun testRejectsAnUnsetScript() {
        assertEquals("No Phel file specified", validationErrorOf(newConfiguration()))
    }

    fun testRejectsAScriptThatDoesNotExist() {
        val configuration = newConfiguration().apply { scriptPath = "/nowhere/missing.phel" }

        val message = validationErrorOf(configuration)

        assertTrue(message, message.startsWith("Phel file does not exist"))
    }

    /** A project without `bin/phel` or `vendor/bin/phel` cannot run anything; say so before launching. */
    fun testRejectsAProjectWithNoPhelBinary() {
        val script = File.createTempFile("phel-run-", ".phel")
        try {
            val configuration = newConfiguration().apply { scriptPath = script.absolutePath }

            val message = validationErrorOf(configuration)

            assertTrue(message, message.startsWith("Phel binary not found"))
        } finally {
            script.delete()
        }
    }

    private fun validationErrorOf(configuration: PhelRunConfiguration): String {
        try {
            configuration.checkConfiguration()
        } catch (e: RuntimeConfigurationError) {
            return e.message.orEmpty()
        }
        fail("expected checkConfiguration to reject this configuration")
        error("unreachable")
    }

    fun testNamesItselfAfterTheScriptFile() {
        val configuration = newConfiguration().apply { scriptPath = "/project/src/main.phel" }

        assertEquals("main.phel", configuration.suggestedName())
    }

    fun testFallsBackToTheProjectRootAsWorkingDirectory() {
        val configuration = newConfiguration().apply { scriptPath = "/project/src/main.phel" }

        assertEquals(project.basePath.orEmpty(), configuration.effectiveWorkingDirectory())
    }

    fun testKeepsAnExplicitWorkingDirectory() {
        val configuration = newConfiguration().apply { workingDirectory = "/elsewhere" }

        assertEquals("/elsewhere", configuration.effectiveWorkingDirectory())
    }

    fun testTypeIdIsStableForPersistedConfigurations() {
        assertEquals("PhelRunConfiguration", PhelRunConfigurationType().id)
    }
}
