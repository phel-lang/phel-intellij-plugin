package org.phellang.integration

import com.intellij.openapi.util.Disposer
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.phellang.registry.indexing.PhelProjectSymbolIndex

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
 */
abstract class PhelIntegrationTestCase : BasePlatformTestCase() {

    override fun tearDown() {
        try {
            project.getServiceIfCreated(PhelProjectSymbolIndex::class.java)
                ?.let { Disposer.dispose(it) }
        } finally {
            super.tearDown()
        }
    }
}
