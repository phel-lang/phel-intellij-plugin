package org.phellang.run.test

import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource

/**
 * Reads the report written by `phel test --reporter=junit-xml --output=<file>`.
 *
 * JUnit XML rather than the `tap` reporter: it is a de-facto standard, so this parser can be pinned
 * down by tests without a `phel` binary to run. The cost is that a tree built from it appears when
 * the run finishes rather than filling in live.
 *
 * Never throws. A run killed part-way leaves half-written XML, and a formatter for test results is
 * not worth failing a test run over — an unparseable report is reported as no results.
 */
object PhelJUnitXmlParser {

    fun parse(xml: String): PhelTestReport {
        if (xml.isBlank()) return PhelTestReport.EMPTY

        val root = try {
            documentBuilder().parse(InputSource(StringReader(xml))).documentElement
        } catch (e: Exception) {
            return PhelTestReport.EMPTY
        } ?: return PhelTestReport.EMPTY

        val suites = mutableListOf<PhelTestSuite>()
        collectSuites(root, suites)

        return PhelTestReport(suites)
    }

    /** Depth-first so a `<testsuites>` wrapper, a bare `<testsuite>`, and nesting all work. */
    private fun collectSuites(element: Element, into: MutableList<PhelTestSuite>) {
        if (element.tagName == SUITE) {
            into += PhelTestSuite(element.getAttribute("name"), casesOf(element))
        }

        for (child in element.childElements()) {
            collectSuites(child, into)
        }
    }

    /** Direct children only: a nested suite's cases belong to that suite, not to this one. */
    private fun casesOf(suite: Element): List<PhelTestCase> =
        suite.childElements().filter { it.tagName == CASE }.map { case ->
            PhelTestCase(
                name = case.getAttribute("name"),
                durationSeconds = case.getAttribute("time").toDoubleOrNull(),
                outcome = outcomeOf(case),
            )
        }.let(::foldAssertions)

    /**
     * Folds the repeated entries `phel` emits per assertion into one case per test.
     *
     * `--reporter=junit-xml` writes one `<testcase>` per `is` form, each carrying the *enclosing
     * test's* name, so a `deftest` with four assertions arrived as four identically named siblings in
     * the tree. Running a single test from the gutter made that unmistakable.
     *
     * Grouping by name is safe because a namespace cannot define the same `deftest` twice, and
     * `groupBy` keeps first-appearance order so the tree still follows the file.
     */
    private fun foldAssertions(cases: List<PhelTestCase>): List<PhelTestCase> =
        cases.groupBy { it.name }.map { (_, assertions) -> merge(assertions) }

    private fun merge(assertions: List<PhelTestCase>): PhelTestCase {
        // A test reported as a single case is left exactly as it was parsed.
        if (assertions.size == 1) return assertions.single()

        val durations = assertions.mapNotNull { it.durationSeconds }

        return PhelTestCase(
            name = assertions.first().name,
            durationSeconds = if (durations.isEmpty()) null else durations.sum(),
            outcome = mergedOutcome(assertions.map { it.outcome }),
        )
    }

    /**
     * The worst outcome in the group wins, since one failed assertion fails the test. Errors outrank
     * failures because a crash is not a comparison that came out false, and a test counts as skipped
     * only when nothing in it ran.
     */
    private fun mergedOutcome(outcomes: List<PhelTestCase.Outcome>): PhelTestCase.Outcome {
        val errored = outcomes.filterIsInstance<PhelTestCase.Outcome.Errored>()
        if (errored.isNotEmpty()) {
            return PhelTestCase.Outcome.Errored(
                summarize(errored.map { it.message }, outcomes.size, "errored"),
                errored.joinToString(DETAIL_SEPARATOR) { it.details },
            )
        }

        val failed = outcomes.filterIsInstance<PhelTestCase.Outcome.Failed>()
        if (failed.isNotEmpty()) {
            return PhelTestCase.Outcome.Failed(
                summarize(failed.map { it.message }, outcomes.size, "failed"),
                failed.joinToString(DETAIL_SEPARATOR) { it.details },
            )
        }

        val skipped = outcomes.filterIsInstance<PhelTestCase.Outcome.Skipped>()
        if (skipped.size == outcomes.size) return PhelTestCase.Outcome.Skipped(skipped.first().message)

        return PhelTestCase.Outcome.Passed
    }

    /**
     * One bad assertion keeps its own message, which for Phel is the failing form. Several would
     * otherwise be reduced to whichever came first, so the count is reported instead and every form
     * stays in the details.
     */
    private fun summarize(messages: List<String>, total: Int, verb: String): String =
        messages.singleOrNull() ?: "${messages.size} of $total assertions $verb"

    private fun outcomeOf(case: Element): PhelTestCase.Outcome {
        for (child in case.childElements()) {
            val message = child.getAttribute("message")
            val details = child.textContent.orEmpty().trim()

            when (child.tagName) {
                "failure" -> return PhelTestCase.Outcome.Failed(message.ifEmpty { details }, details)
                "error" -> return PhelTestCase.Outcome.Errored(message.ifEmpty { details }, details)
                "skipped" -> return PhelTestCase.Outcome.Skipped(message)
            }
        }

        return PhelTestCase.Outcome.Passed
    }

    private fun Element.childElements(): List<Element> {
        val children = mutableListOf<Element>()
        val nodes = childNodes

        for (i in 0 until nodes.length) {
            val node = nodes.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) children += node as Element
        }

        return children
    }

    /**
     * External entities disabled: the report is a file this plugin asks a subprocess to write, and a
     * parser that resolves entities from it would read whatever the document names.
     */
    private fun documentBuilder() = DocumentBuilderFactory.newInstance().apply {
        setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
        setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "")
        setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "")
        isXIncludeAware = false
        isExpandEntityReferences = false
    }.newDocumentBuilder()

    private const val SUITE = "testsuite"
    private const val CASE = "testcase"

    /** A blank line between the folded assertions, so the tree's detail pane stays readable. */
    private const val DETAIL_SEPARATOR = "\n\n"
}
