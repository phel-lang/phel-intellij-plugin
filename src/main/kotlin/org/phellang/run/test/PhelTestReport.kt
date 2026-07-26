package org.phellang.run.test

/** A parsed `phel test --reporter=junit-xml` report. */
data class PhelTestReport(val suites: List<PhelTestSuite>) {

    companion object {
        val EMPTY = PhelTestReport(emptyList())
    }
}

data class PhelTestSuite(val name: String, val cases: List<PhelTestCase>)

data class PhelTestCase(
    val name: String,
    /** Seconds, as reported. Null when the report omits it. */
    val durationSeconds: Double?,
    val outcome: Outcome,
) {
    /** Milliseconds, which is the unit the test tree wants. */
    val durationMillis: Long? get() = durationSeconds?.let { (it * 1000).toLong() }

    sealed interface Outcome {
        data object Passed : Outcome

        /** [details] is the element's text: a stack trace or comparison, shown in the tree. */
        data class Failed(val message: String, val details: String) : Outcome

        data class Errored(val message: String, val details: String) : Outcome

        data class Skipped(val message: String) : Outcome
    }
}
