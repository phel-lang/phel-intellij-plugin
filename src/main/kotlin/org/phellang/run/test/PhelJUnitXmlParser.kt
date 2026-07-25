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
        }

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
}
