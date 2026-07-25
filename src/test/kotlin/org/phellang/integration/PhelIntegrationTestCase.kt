package org.phellang.integration

import com.intellij.openapi.util.Disposer
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.phellang.indexing.PhelProjectSymbolIndex

/**
 * Base class for integration tests that exercise PSI/reference/inspection features.
 *
 * [BasePlatformTestCase] reuses a single light project across every test class in the
 * run, so a project-level service created by one class outlives that class. The
 * [PhelProjectSymbolIndex] service registers a VFS and a PSI-tree-change listener in its
 * constructor; once any test triggers the service (completion, arity resolution, …)
 * those listeners stay attached to the shared project's `PsiManager` and fire during the
 * *next* class's `addFileToProject`, leaving the freshly created file with empty PSI text.
 *
 * Disposing the service in tearDown drops its listeners so each class starts from a clean
 * project; the service is recreated lazily the next time it is needed.
 *
 * The index is also cleared explicitly. `Disposer` will not run `dispose()` a second time on an
 * instance it has already disposed, and `getServiceIfCreated` keeps handing back that same instance,
 * so from the second test method onward disposal alone cleared nothing and one class's definitions
 * turned up in the next class's `getAllSymbols()` (#271).
 */
abstract class PhelIntegrationTestCase : BasePlatformTestCase() {

    override fun tearDown() {
        try {
            project.getServiceIfCreated(PhelProjectSymbolIndex::class.java)?.let { index ->
                index.clear()
                Disposer.dispose(index)
            }
        } finally {
            super.tearDown()
        }
    }
}
