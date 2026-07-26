package org.phellang.run.test

/**
 * Renders a parsed report as the service messages the platform's test runner already understands.
 *
 * Chosen over a custom `OutputToGeneralTestEventsConverter`: this SDK has no
 * `SMCustomMessagesParsing` to hang one on, and the default converter parses these messages from the
 * process output anyway. It also keeps the whole translation a pure function, so the tree's contents
 * can be asserted without a `phel` binary, a project, or a console.
 */
object PhelTestServiceMessages {

    fun render(report: PhelTestReport): List<String> = buildList {
        for (suite in report.suites) {
            add(message("testSuiteStarted", "name" to suite.name, "locationHint" to locationOf(suite.name)))
            for (case in suite.cases) addAll(render(suite, case))
            add(message("testSuiteFinished", "name" to suite.name))
        }
    }

    /**
     * The suite name is the Phel namespace, which is what makes a location resolvable at all: the
     * report carries no file or line, so [PhelTestLocator] resolves the pair back to the `deftest`.
     */
    private fun locationOf(suiteName: String, testName: String? = null): String =
        if (testName == null) "${PhelTestLocator.PROTOCOL}://$suiteName" else "${PhelTestLocator.PROTOCOL}://$suiteName/$testName"

    private fun render(suite: PhelTestSuite, case: PhelTestCase): List<String> = buildList {
        add(message("testStarted", "name" to case.name, "locationHint" to locationOf(suite.name, case.name)))

        when (val outcome = case.outcome) {
            is PhelTestCase.Outcome.Passed -> Unit

            is PhelTestCase.Outcome.Skipped ->
                add(message("testIgnored", "name" to case.name, "message" to outcome.message))

            is PhelTestCase.Outcome.Failed ->
                add(message("testFailed", "name" to case.name, "message" to outcome.message, "details" to outcome.details))

            // `error='true'` is what separates a crash from a failed assertion in the tree.
            is PhelTestCase.Outcome.Errored ->
                add(
                    message(
                        "testFailed",
                        "name" to case.name,
                        "message" to outcome.message,
                        "details" to outcome.details,
                        "error" to "true",
                    )
                )
        }

        val duration = case.durationMillis
        if (duration == null) {
            add(message("testFinished", "name" to case.name))
        } else {
            add(message("testFinished", "name" to case.name, "duration" to duration.toString()))
        }
    }

    private fun message(name: String, vararg attributes: Pair<String, String>): String {
        val rendered = attributes.joinToString(" ") { (key, value) -> "$key='${escape(value)}'" }

        return "##teamcity[$name $rendered]"
    }

    /**
     * The service-message escaping rules. Phel names carry backslashes (`app\core-test`) and failure
     * details carry newlines and quotes, so every one of these is reachable.
     */
    private fun escape(value: String): String = buildString {
        for (character in value) {
            when (character) {
                '|' -> append("||")
                '\'' -> append("|'")
                '\n' -> append("|n")
                '\r' -> append("|r")
                '[' -> append("|[")
                ']' -> append("|]")
                else -> append(character)
            }
        }
    }
}
