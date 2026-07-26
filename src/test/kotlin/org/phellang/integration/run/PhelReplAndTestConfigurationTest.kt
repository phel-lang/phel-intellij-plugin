package org.phellang.integration.run

import com.intellij.execution.configurations.ConfigurationFactory
import org.jdom.Element
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.run.PhelReplConfiguration
import org.phellang.run.PhelRunConfigurationType
import org.phellang.run.PhelTestConfiguration

class PhelReplAndTestConfigurationTest : PhelIntegrationTestCase() {

    private fun factories(): Array<ConfigurationFactory> = PhelRunConfigurationType().configurationFactories

    private fun replConfiguration() = PhelReplConfiguration(project, factories()[1], "Phel REPL")

    private fun testConfiguration() = PhelTestConfiguration(project, factories()[2], "Phel tests")

    fun testTypeOffersFileReplAndTestFactories() {
        assertEquals(listOf("Phel file", "Phel REPL", "Phel tests"), factories().map { it.name })
    }

    /**
     * The original factory keeps its id: it is persisted alongside the type id, so renaming it
     * would orphan every configuration saved before the REPL and test factories existed.
     */
    fun testOriginalFactoryIdIsUnchanged() {
        assertEquals("Phel", factories()[0].id)
    }

    fun testReplAndTestFactoryIdsAreDistinct() {
        assertEquals(listOf("Phel", "PhelRepl", "PhelTest"), factories().map { it.id })
    }

    // ---- REPL ----

    fun testReplNamesItself() {
        assertEquals("Phel REPL", replConfiguration().suggestedName())
    }

    fun testReplDefaultsToTheProjectRoot() {
        assertEquals(project.basePath.orEmpty(), replConfiguration().effectiveWorkingDirectory())
    }

    fun testReplKeepsAnExplicitWorkingDirectory() {
        val configuration = replConfiguration().apply { workingDirectory = "/elsewhere" }

        assertEquals("/elsewhere", configuration.effectiveWorkingDirectory())
    }

    fun testReplRoundTripsThroughXml() {
        val saved = replConfiguration().apply { workingDirectory = "/project" }
        val element = Element("configuration")
        saved.writeExternal(element)

        val loaded = replConfiguration()
        loaded.readExternal(element)

        assertEquals("/project", loaded.workingDirectory)
    }

    // ---- tests ----

    fun testTestConfigurationRunsEverythingByDefault() {
        val configuration = testConfiguration()

        assertEmpty(configuration.paths())
        assertEquals("All tests", configuration.suggestedName())
    }

    fun testTestConfigurationSplitsSeveralPaths() {
        val configuration = testConfiguration().apply { setPaths(listOf("tests/a.phel", "tests/b.phel")) }

        assertEquals(listOf("tests/a.phel", "tests/b.phel"), configuration.paths())
    }

    fun testTestConfigurationIgnoresStrayWhitespace() {
        val configuration = testConfiguration().apply { testPaths = "  \n tests/a.phel \n " }

        assertEquals(listOf("tests/a.phel"), configuration.paths())
    }

    /**
     * The bug: a path is one path however many spaces are in it.
     *
     * The producer stores an absolute path, so on a project under `/Users/me/My Projects` the
     * space-separated split handed `phel test` two paths that do not exist.
     */
    fun testTestConfigurationKeepsAPathContainingSpacesIntact() {
        val spaced = "/Users/me/My Projects/app/tests/html.phel"
        val configuration = testConfiguration().apply { setPaths(listOf(spaced)) }

        assertEquals(listOf(spaced), configuration.paths())
        assertEquals("Tests: html.phel", configuration.suggestedName())
    }

    fun testTestConfigurationNamesItselfAfterSeveralSpacedPaths() {
        val configuration = testConfiguration().apply {
            setPaths(listOf("/My Projects/app/tests/a.phel", "/My Projects/app/tests/b.phel"))
        }

        assertEquals("Tests: a.phel b.phel", configuration.suggestedName())
    }

    /** File names, not whole paths: the producer stores absolute ones and the run widget is narrow. */
    fun testTestConfigurationNamesItselfAfterItsPaths() {
        val configuration = testConfiguration().apply { testPaths = "tests/a.phel" }

        assertEquals("Tests: a.phel", configuration.suggestedName())
    }

    fun testTestConfigurationNamesItselfAfterASingleTest() {
        val configuration = testConfiguration().apply {
            testPaths = "/project/tests/html.phel"
            testName = "basic-tags"
        }

        assertEquals("basic-tags", configuration.suggestedName())
    }

    fun testTestConfigurationRoundTripsThroughXml() {
        val saved = testConfiguration().apply {
            testPaths = "tests/a.phel"
            workingDirectory = "/project"
        }
        val element = Element("configuration")
        saved.writeExternal(element)

        val loaded = testConfiguration()
        loaded.readExternal(element)

        assertEquals("tests/a.phel", loaded.testPaths)
        assertEquals("/project", loaded.workingDirectory)
    }

    fun testTestConfigurationRoundTripsASingleTestThroughXml() {
        val saved = testConfiguration().apply {
            testPaths = "/project/tests/html.phel"
            testName = "basic-tags"
        }
        val element = Element("configuration")
        saved.writeExternal(element)

        val loaded = testConfiguration()
        loaded.readExternal(element)

        assertEquals("/project/tests/html.phel", loaded.testPaths)
        assertEquals("basic-tags", loaded.testName)
    }

    /**
     * The new keys are append-only: a configuration saved before they existed must still load as
     * the whole-file run it was, rather than silently acquiring a filter.
     */
    fun testTestConfigurationLoadsAnElementSavedBeforeTheTestFieldsExisted() {
        val element = Element("configuration")
        com.intellij.openapi.util.JDOMExternalizerUtil.writeField(element, "TEST_PATHS", "tests/a.phel")

        val loaded = testConfiguration()
        loaded.readExternal(element)

        assertEquals("tests/a.phel", loaded.testPaths)
        assertEquals("", loaded.testName)
    }

    /**
     * A value saved before the quoting existed still means what it meant.
     *
     * `parse` reads an unquoted run of paths exactly as the old space split did, which is what let
     * the persistence key stay the same across the change.
     */
    fun testTestConfigurationStillReadsUnquotedLegacyPaths() {
        val element = Element("configuration")
        com.intellij.openapi.util.JDOMExternalizerUtil.writeField(
            element, "TEST_PATHS", "tests/a.phel tests/b.phel"
        )

        val loaded = testConfiguration()
        loaded.readExternal(element)

        assertEquals(listOf("tests/a.phel", "tests/b.phel"), loaded.paths())
    }

    fun testTestConfigurationRoundTripsASpacedPathThroughXml() {
        val spaced = "/Users/me/My Projects/app/tests/html.phel"
        val element = Element("configuration")
        testConfiguration().apply { setPaths(listOf(spaced)) }.writeExternal(element)

        val loaded = testConfiguration().apply { readExternal(element) }

        assertEquals(listOf(spaced), loaded.paths())
    }

    /** Neither can run without a binary, and both must say so before launching. */
    fun testBothRejectAProjectWithNoPhelBinary() {
        for (configuration in listOf(replConfiguration(), testConfiguration())) {
            val message = try {
                configuration.checkConfiguration()
                null
            } catch (e: com.intellij.execution.configurations.RuntimeConfigurationError) {
                e.message
            }

            assertNotNull("expected ${configuration.javaClass.simpleName} to reject a binary-less project", message)
            assertTrue(message!!, message.startsWith("Phel binary not found"))
        }
    }
}
