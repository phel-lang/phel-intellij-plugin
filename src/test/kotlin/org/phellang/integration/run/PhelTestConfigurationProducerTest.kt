package org.phellang.integration.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.execution.configurations.RunConfiguration
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.run.PhelRunConfiguration
import org.phellang.run.PhelRunConfigurationProducer
import org.phellang.run.PhelRunConfigurationType
import org.phellang.run.PhelTestConfiguration
import org.phellang.run.PhelTestConfigurationProducer

/**
 * Which configuration a context yields, asserted end to end rather than reasoned about: the
 * precedence contract between competing producers is platform behavior, so the only trustworthy
 * answer is the one the platform actually gives.
 */
class PhelTestConfigurationProducerTest : PhelIntegrationTestCase() {

    private val testFile = """
        (ns test.html
          (:require phel.test :refer [deftest is]))

        (deftest basic-tags
          (is (= "<div></div>" (html [:div]))))

        (deftest empty-tags
          (is (= "<h1></h1>" (html [:h1]))))
    """.trimIndent()

    private fun configurationAt(text: String, marker: String): RunConfiguration? {
        val file = myFixture.configureByText("html.phel", text)
        val offset = text.indexOf(marker)
        assertTrue("marker '$marker' not found in fixture", offset >= 0)

        val element = file.findElementAt(offset) ?: return null
        return ConfigurationContext(element).configurationsFromContext?.firstOrNull()?.configuration
    }

    private fun testConfigurationAt(text: String, marker: String): PhelTestConfiguration {
        val configuration = configurationAt(text, marker)
        assertInstanceOf(configuration, PhelTestConfiguration::class.java)
        return configuration as PhelTestConfiguration
    }

    fun testATestFileProducesATestConfiguration() {
        val configuration = testConfigurationAt(testFile, "(ns")

        assertTrue(configuration.testPaths.endsWith("html.phel"))
    }

    /** Outside any `deftest`, the whole file runs, so no filter is set. */
    fun testTheNamespaceFormRunsTheWholeFile() {
        val configuration = testConfigurationAt(testFile, "(ns")

        assertEquals("", configuration.testName)
        assertEquals("Tests: html.phel", configuration.name)
    }

    fun testAContextInsideATestRunsOnlyThatTest() {
        val configuration = testConfigurationAt(testFile, "basic-tags")

        assertEquals("basic-tags", configuration.testName)
        assertEquals("basic-tags", configuration.name)
    }

    fun testEachTestGetsItsOwnScope() {
        assertEquals("empty-tags", testConfigurationAt(testFile, "empty-tags").testName)
    }

    fun testTheDeepestPartOfATestStillRunsThatTest() {
        val configuration = testConfigurationAt(testFile, "\"<div></div>\"")

        assertEquals("basic-tags", configuration.testName)
    }

    fun testTheWorkingDirectoryIsTheProjectRoot() {
        val configuration = testConfigurationAt(testFile, "(ns")

        assertEquals(project.basePath.orEmpty(), configuration.workingDirectory)
    }

    /** The whole point: an ordinary file must keep going to `phel run` and its plain console. */
    fun testANonTestFileStillProducesARunConfiguration() {
        val configuration = configurationAt("(ns app.html)\n\n(defn greet [] \"hi\")\n", "(ns")

        assertInstanceOf(configuration, PhelRunConfiguration::class.java)
    }

    fun testAFileThatOnlyLooksLikeATestStillProducesARunConfiguration() {
        val configuration = configurationAt("(ns app.html)\n\n(deftest basic-tags\n  (is true))\n", "basic-tags")

        assertInstanceOf(configuration, PhelRunConfiguration::class.java)
    }

    /**
     * The file, REPL and test configurations share one `ConfigurationType`, so the platform offers
     * every saved configuration to every producer registered for it. With a narrower type parameter
     * the generated bridge method cast a `PhelRunConfiguration` to `PhelTestConfiguration`, and the
     * `ClassCastException` aborted the action update — which emptied the gutter popup for *every*
     * Phel file, test or not, reporting "Nothing here".
     *
     * Nothing reproduces it without a saved sibling configuration for the platform to walk, which is
     * exactly why the rest of this class missed it.
     */
    @Suppress("UNCHECKED_CAST")
    private fun erased(producer: LazyRunConfigurationProducer<*>) =
        producer as RunConfigurationProducer<RunConfiguration>

    private fun contextAt(marker: String): ConfigurationContext {
        val file = myFixture.configureByText("html.phel", testFile)
        return ConfigurationContext(file.findElementAt(testFile.indexOf(marker))!!)
    }

    private fun configurationOfKind(index: Int, name: String) =
        PhelRunConfigurationType().configurationFactories[index]
            .createTemplateConfiguration(project)
            .also { it.name = name }

    fun testToleratesBeingOfferedAConfigurationOfASiblingKind() {
        val context = contextAt("basic-tags")

        for (index in 0..1) {
            val foreign = configurationOfKind(index, "sibling")

            assertFalse(
                "the test producer must decline ${foreign.javaClass.simpleName}, not crash on it",
                erased(PhelTestConfigurationProducer()).isConfigurationFromContext(foreign, context),
            )
        }
    }

    fun testTheFileProducerAlsoToleratesASiblingKind() {
        val context = contextAt("basic-tags")
        val foreign = configurationOfKind(2, "sibling test")

        assertFalse(erased(PhelRunConfigurationProducer()).isConfigurationFromContext(foreign, context))
    }
}
