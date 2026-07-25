package org.phellang.run.test

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties

/**
 * Plain properties: the tree is fed by service messages the process handler emits, which the
 * platform's default converter already parses, so nothing custom is needed here.
 */
class PhelTestConsoleProperties(
    configuration: RunConfiguration,
    executor: Executor,
) : SMTRunnerConsoleProperties(configuration, FRAMEWORK_NAME, executor) {

    init {
        // The report carries no source locations, so a test node cannot navigate anywhere yet.
        isIdBasedTestTree = false
    }

    companion object {
        const val FRAMEWORK_NAME = "Phel"
    }
}
