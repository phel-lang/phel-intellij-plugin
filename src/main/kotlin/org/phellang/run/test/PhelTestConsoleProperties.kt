package org.phellang.run.test

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties
import com.intellij.execution.testframework.sm.runner.SMTestLocator

/**
 * The tree is fed by service messages the process handler emits, which the platform's default
 * converter already parses, so the only thing supplied here is the locator that turns their
 * `locationHint` back into a `deftest`.
 */
class PhelTestConsoleProperties(
    configuration: RunConfiguration,
    executor: Executor,
) : SMTRunnerConsoleProperties(configuration, FRAMEWORK_NAME, executor) {

    init {
        // Names, not generated ids: the location is resolved from the suite and test name, which are
        // exactly what the report carries.
        isIdBasedTestTree = false
    }

    override fun getTestLocator(): SMTestLocator = PhelTestLocator

    companion object {
        const val FRAMEWORK_NAME = "Phel"
    }
}
