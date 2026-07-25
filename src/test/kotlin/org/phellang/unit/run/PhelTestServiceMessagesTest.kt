package org.phellang.unit.run

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.run.test.PhelJUnitXmlParser
import org.phellang.run.test.PhelTestCase
import org.phellang.run.test.PhelTestReport
import org.phellang.run.test.PhelTestServiceMessages
import org.phellang.run.test.PhelTestSuite

/**
 * What the test tree is actually built from.
 *
 * Pure by design: the report is rendered to service messages the platform's default converter
 * parses, so the tree's contents can be asserted here without a `phel` binary, a project or a
 * console.
 */
class PhelTestServiceMessagesTest {

    private fun render(report: PhelTestReport) = PhelTestServiceMessages.render(report)

    private fun renderXml(xml: String) = render(PhelJUnitXmlParser.parse(xml))

    private fun passing(name: String, seconds: Double? = null) =
        PhelTestCase(name, seconds, PhelTestCase.Outcome.Passed)

    @Test
    fun `wraps a suite around its cases`() {
        val messages = render(PhelTestReport(listOf(PhelTestSuite("core", listOf(passing("adds"))))))

        assertEquals(
            listOf(
                "##teamcity[testSuiteStarted name='core']",
                "##teamcity[testStarted name='adds']",
                "##teamcity[testFinished name='adds']",
                "##teamcity[testSuiteFinished name='core']",
            ),
            messages,
        )
    }

    @Test
    fun `reports a duration in milliseconds`() {
        val messages = render(PhelTestReport(listOf(PhelTestSuite("s", listOf(passing("slow", 1.5))))))

        assertTrue(messages.any { it == "##teamcity[testFinished name='slow' duration='1500']" }, messages.toString())
    }

    @Test
    fun `a failure carries its message and details`() {
        val messages = renderXml(
            """
            <testsuite name="s">
              <testcase name="broken"><failure message="expected 1">line 12</failure></testcase>
            </testsuite>
            """.trimIndent()
        )

        assertTrue(
            messages.any { it == "##teamcity[testFailed name='broken' message='expected 1' details='line 12']" },
            messages.toString(),
        )
    }

    /** `error='true'` is what separates a crash from a failed assertion in the tree. */
    @Test
    fun `an error is marked as one`() {
        val messages = renderXml(
            """<testsuite name="s"><testcase name="boom"><error message="PHEL001">trace</error></testcase></testsuite>"""
        )

        assertTrue(messages.any { it.startsWith("##teamcity[testFailed") && it.contains("error='true'") }, messages.toString())
    }

    @Test
    fun `a skipped case is ignored rather than failed`() {
        val messages = renderXml(
            """<testsuite name="s"><testcase name="later"><skipped message="wip"/></testcase></testsuite>"""
        )

        assertTrue(messages.any { it == "##teamcity[testIgnored name='later' message='wip']" }, messages.toString())
        assertTrue(messages.none { it.contains("testFailed") }, messages.toString())
    }

    /** Phel namespaces carry backslashes, so this path is hit by every real suite name. */
    @Test
    fun `escapes a namespace name`() {
        val messages = render(PhelTestReport(listOf(PhelTestSuite("app\\core-test", emptyList()))))

        assertEquals("##teamcity[testSuiteStarted name='app\\core-test']", messages.first())
    }

    @Test
    fun `escapes quotes newlines and brackets in failure details`() {
        val report = PhelTestReport(
            listOf(
                PhelTestSuite(
                    "s",
                    listOf(
                        PhelTestCase(
                            "t",
                            null,
                            PhelTestCase.Outcome.Failed("it's [bad]", "line one\nline two|end\r"),
                        )
                    ),
                )
            )
        )

        val failure = render(report).single { it.contains("testFailed") }

        assertTrue(failure.contains("message='it|'s |[bad|]'"), failure)
        assertTrue(failure.contains("details='line one|nline two||end|r'"), failure)
    }

    @Test
    fun `an empty report renders nothing`() {
        assertTrue(render(PhelTestReport.EMPTY).isEmpty())
    }

    @Test
    fun `a suite with no cases still opens and closes`() {
        val messages = render(PhelTestReport(listOf(PhelTestSuite("empty", emptyList()))))

        assertEquals(
            listOf("##teamcity[testSuiteStarted name='empty']", "##teamcity[testSuiteFinished name='empty']"),
            messages,
        )
    }

    @Test
    fun `renders every suite in a multi-suite report`() {
        val messages = renderXml(
            """
            <testsuites>
              <testsuite name="a"><testcase name="x"/></testsuite>
              <testsuite name="b"><testcase name="y"/></testsuite>
            </testsuites>
            """.trimIndent()
        )

        assertEquals(2, messages.count { it.startsWith("##teamcity[testSuiteStarted") })
        assertEquals(2, messages.count { it.startsWith("##teamcity[testStarted") })
    }

    /** Every started test must be finished, or the tree shows it running forever. */
    @Test
    fun `every started test is finished`() {
        val messages = renderXml(
            """
            <testsuite name="s">
              <testcase name="ok"/>
              <testcase name="bad"><failure message="m">d</failure></testcase>
              <testcase name="skip"><skipped message="w"/></testcase>
            </testsuite>
            """.trimIndent()
        )

        assertEquals(
            messages.count { it.startsWith("##teamcity[testStarted") },
            messages.count { it.startsWith("##teamcity[testFinished") },
        )
    }
}
