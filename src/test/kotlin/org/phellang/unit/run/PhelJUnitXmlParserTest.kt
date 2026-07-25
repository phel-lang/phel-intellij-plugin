package org.phellang.unit.run

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.run.test.PhelJUnitXmlParser
import org.phellang.run.test.PhelTestCase

/**
 * The JUnit XML `phel test --reporter=junit-xml` writes.
 *
 * Parsed rather than streamed, so this is where the test tree's accuracy is decided. Kept a pure
 * unit test: it needs no `phel` binary, which is exactly why this reporter was chosen over `tap`.
 */
class PhelJUnitXmlParserTest {

    private fun parse(xml: String) = PhelJUnitXmlParser.parse(xml)

    @Test
    fun `reads suites and their cases`() {
        val report = parse(
            """
            <testsuites>
              <testsuite name="app\core-test" tests="2">
                <testcase name="adds" time="0.01"/>
                <testcase name="subtracts" time="0.02"/>
              </testsuite>
            </testsuites>
            """.trimIndent()
        )

        assertEquals(1, report.suites.size)
        assertEquals("app\\core-test", report.suites[0].name)
        assertEquals(listOf("adds", "subtracts"), report.suites[0].cases.map { it.name })
    }

    @Test
    fun `a case with no failure element passed`() {
        val report = parse("""<testsuite name="s"><testcase name="ok" time="0.5"/></testsuite>""")

        assertEquals(PhelTestCase.Outcome.Passed, report.suites[0].cases[0].outcome)
    }

    @Test
    fun `reads a failure message and its details`() {
        val report = parse(
            """
            <testsuite name="s">
              <testcase name="broken">
                <failure message="expected 1, got 2">at app\core-test line 12</failure>
              </testcase>
            </testsuite>
            """.trimIndent()
        )

        val outcome = report.suites[0].cases[0].outcome
        assertTrue(outcome is PhelTestCase.Outcome.Failed, outcome.toString())
        outcome as PhelTestCase.Outcome.Failed
        assertEquals("expected 1, got 2", outcome.message)
        assertEquals("at app\\core-test line 12", outcome.details)
    }

    @Test
    fun `an error is distinct from a failure`() {
        val report = parse(
            """
            <testsuite name="s">
              <testcase name="blew-up"><error message="PHEL001">trace</error></testcase>
            </testsuite>
            """.trimIndent()
        )

        assertTrue(report.suites[0].cases[0].outcome is PhelTestCase.Outcome.Errored)
    }

    @Test
    fun `reads a skipped case`() {
        val report = parse(
            """<testsuite name="s"><testcase name="later"><skipped message="wip"/></testcase></testsuite>"""
        )

        val outcome = report.suites[0].cases[0].outcome
        assertTrue(outcome is PhelTestCase.Outcome.Skipped, outcome.toString())
        assertEquals("wip", (outcome as PhelTestCase.Outcome.Skipped).message)
    }

    @Test
    fun `converts the reported time to milliseconds`() {
        val report = parse("""<testsuite name="s"><testcase name="slow" time="1.25"/></testsuite>""")

        assertEquals(1250L, report.suites[0].cases[0].durationMillis)
    }

    @Test
    fun `a case with no time has no duration`() {
        val report = parse("""<testsuite name="s"><testcase name="untimed"/></testsuite>""")

        assertEquals(null, report.suites[0].cases[0].durationMillis)
    }

    @Test
    fun `accepts a bare testsuite without a testsuites wrapper`() {
        val report = parse("""<testsuite name="only"><testcase name="a"/></testsuite>""")

        assertEquals(listOf("only"), report.suites.map { it.name })
    }

    @Test
    fun `reads nested suites`() {
        val report = parse(
            """
            <testsuites>
              <testsuite name="outer">
                <testsuite name="inner"><testcase name="a"/></testsuite>
              </testsuite>
            </testsuites>
            """.trimIndent()
        )

        assertTrue(report.suites.map { it.name }.containsAll(listOf("outer", "inner")))
    }

    /** A run that crashed before writing anything must not surface as a parse error. */
    @Test
    fun `empty input yields an empty report`() {
        assertTrue(parse("").isEmpty)
        assertTrue(parse("   ").isEmpty)
    }

    /** Half-written XML from a killed process is likelier than a malformed reporter. */
    @Test
    fun `malformed xml yields an empty report rather than throwing`() {
        assertTrue(parse("<testsuite name=\"s\"><testcase ").isEmpty)
    }

    @Test
    fun `a suite with no cases is kept so the tree can show it`() {
        val report = parse("""<testsuite name="empty-suite"></testsuite>""")

        assertEquals(listOf("empty-suite"), report.suites.map { it.name })
        assertTrue(report.isEmpty)
    }

    @Test
    fun `a failure without a message attribute still reports its details`() {
        val report = parse(
            """<testsuite name="s"><testcase name="t"><failure>bare detail</failure></testcase></testsuite>"""
        )

        val outcome = report.suites[0].cases[0].outcome as PhelTestCase.Outcome.Failed
        assertEquals("bare detail", outcome.details)
    }
}
