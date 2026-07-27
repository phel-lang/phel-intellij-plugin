package org.phellang.unit.run

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.run.test.PhelJUnitXmlParser
import org.phellang.run.test.PhelTestCase
import org.phellang.run.test.PhelTestSuite

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
        assertEquals(emptyList<PhelTestSuite>(), parse("").suites)
        assertEquals(emptyList<PhelTestSuite>(), parse("   ").suites)
    }

    /** Half-written XML from a killed process is likelier than a malformed reporter. */
    @Test
    fun `malformed xml yields an empty report rather than throwing`() {
        assertEquals(emptyList<PhelTestSuite>(), parse("<testsuite name=\"s\"><testcase ").suites)
    }

    @Test
    fun `a suite with no cases is kept so the tree can show it`() {
        val report = parse("""<testsuite name="empty-suite"></testsuite>""")

        assertEquals(listOf("empty-suite"), report.suites.map { it.name })
        assertEquals(emptyList<PhelTestCase>(), report.suites.single().cases)
    }

    @Test
    fun `a failure without a message attribute still reports its details`() {
        val report = parse(
            """<testsuite name="s"><testcase name="t"><failure>bare detail</failure></testcase></testsuite>"""
        )

        val outcome = report.suites[0].cases[0].outcome as PhelTestCase.Outcome.Failed
        assertEquals("bare detail", outcome.details)
    }

    // ---- one node per test, not per assertion ----
    //
    // `phel` writes a <testcase> per `is` form, each named after the enclosing test, so these are
    // the shapes a real report has. Verbatim from `phel test --reporter=junit-xml` on a `deftest`
    // with four passing assertions and one with three failing ones.

    @Test
    fun `folds the repeated cases of one test into a single node`() {
        val report = parse(
            """
            <testsuites tests="4">
              <testsuite name="tests.html" tests="4">
                <testcase name="void-tags" classname="" file="/p/tests/html.phel" line="22"></testcase>
                <testcase name="void-tags" classname="" file="/p/tests/html.phel" line="22"></testcase>
                <testcase name="void-tags" classname="" file="/p/tests/html.phel" line="22"></testcase>
                <testcase name="void-tags" classname="" file="/p/tests/html.phel" line="22"></testcase>
              </testsuite>
            </testsuites>
            """.trimIndent()
        )

        assertEquals(listOf("void-tags"), report.suites[0].cases.map { it.name })
        assertEquals(PhelTestCase.Outcome.Passed, report.suites[0].cases[0].outcome)
    }

    @Test
    fun `a folded test keeps every failing assertion in its details`() {
        val report = parse(
            """
            <testsuite name="tests.html" tests="3">
              <testcase name="void-tags-ignore-content"><failure type="AssertionFailed">(= "&lt;br /&gt;" a)</failure></testcase>
              <testcase name="void-tags-ignore-content"><failure type="AssertionFailed">(= "&lt;img /&gt;" b)</failure></testcase>
              <testcase name="void-tags-ignore-content"><failure type="AssertionFailed">(= "&lt;input /&gt;" c)</failure></testcase>
            </testsuite>
            """.trimIndent()
        )

        val outcome = report.suites[0].cases.single().outcome as PhelTestCase.Outcome.Failed
        assertEquals("3 of 3 assertions failed", outcome.message)
        assertEquals("""(= "<br />" a)""" + "\n\n" + """(= "<img />" b)""" + "\n\n" + """(= "<input />" c)""", outcome.details)
    }

    @Test
    fun `one bad assertion among many fails the whole test and keeps its own message`() {
        val report = parse(
            """
            <testsuite name="s">
              <testcase name="mixed"/>
              <testcase name="mixed"><failure message="boom">detail</failure></testcase>
              <testcase name="mixed"/>
            </testsuite>
            """.trimIndent()
        )

        val outcome = report.suites[0].cases.single().outcome as PhelTestCase.Outcome.Failed
        assertEquals("boom", outcome.message)
        assertEquals("detail", outcome.details)
    }

    /** A crash is not a comparison that came out false, so it outranks a failed assertion. */
    @Test
    fun `an error outranks a failure in the same test`() {
        val report = parse(
            """
            <testsuite name="s">
              <testcase name="mixed"><failure message="assert">f</failure></testcase>
              <testcase name="mixed"><error message="PHEL001">trace</error></testcase>
            </testsuite>
            """.trimIndent()
        )

        val outcome = report.suites[0].cases.single().outcome as PhelTestCase.Outcome.Errored
        assertEquals("PHEL001", outcome.message)
    }

    @Test
    fun `a test counts as skipped only when nothing in it ran`() {
        val allSkipped = parse(
            """
            <testsuite name="s">
              <testcase name="wip"><skipped message="later"/></testcase>
              <testcase name="wip"><skipped message="later"/></testcase>
            </testsuite>
            """.trimIndent()
        )
        assertEquals(
            PhelTestCase.Outcome.Skipped("later"),
            allSkipped.suites[0].cases.single().outcome,
        )

        val partlySkipped = parse(
            """
            <testsuite name="s">
              <testcase name="wip"><skipped message="later"/></testcase>
              <testcase name="wip"/>
            </testsuite>
            """.trimIndent()
        )
        assertEquals(PhelTestCase.Outcome.Passed, partlySkipped.suites[0].cases.single().outcome)
    }

    @Test
    fun `a folded test totals the time of its assertions`() {
        val report = parse(
            """
            <testsuite name="s">
              <testcase name="t" time="0.01"/>
              <testcase name="t" time="0.02"/>
            </testsuite>
            """.trimIndent()
        )

        assertEquals(30L, report.suites[0].cases.single().durationMillis)
    }

    @Test
    fun `distinct tests in one suite stay distinct`() {
        val report = parse(
            """
            <testsuite name="s">
              <testcase name="second"/>
              <testcase name="second"/>
              <testcase name="first"/>
            </testsuite>
            """.trimIndent()
        )

        // Order follows the report, so the tree still follows the file.
        assertEquals(listOf("second", "first"), report.suites[0].cases.map { it.name })
    }

    /** Same name, different suites: two namespaces may each define `basic-tags`. */
    @Test
    fun `tests with the same name in different suites are not folded together`() {
        val report = parse(
            """
            <testsuites>
              <testsuite name="a"><testcase name="shared"/></testsuite>
              <testsuite name="b"><testcase name="shared"/></testsuite>
            </testsuites>
            """.trimIndent()
        )

        assertEquals(listOf("shared"), report.suites[0].cases.map { it.name })
        assertEquals(listOf("shared"), report.suites[1].cases.map { it.name })
    }
}
