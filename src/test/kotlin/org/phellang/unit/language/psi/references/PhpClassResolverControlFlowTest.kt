package org.phellang.unit.language.psi.references

import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.IndexNotReadyException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.phellang.language.psi.references.PhpIndexBridge
import java.io.IOException
import java.lang.reflect.InvocationTargetException

/**
 * The PHP plugin is loaded reflectively and is not a compile-time dependency, so the resolver's
 * behaviour against a real PhpIndex cannot be tested here. What *is* testable — and what actually
 * caused the bug — is the decision about which exceptions may be swallowed.
 *
 * `Method.invoke` wraps whatever the callee threw in an [InvocationTargetException]. A
 * [ProcessCanceledException] from a PHP stub-index query therefore does not arrive as a PCE, so a
 * PCE-aware catch upstream cannot see it and a `catch (Throwable)` here silently ate it — breaking
 * read-action cancellation on the go-to-definition path.
 */
class PhpClassResolverControlFlowTest {

    @Test
    fun `rethrows a ProcessCanceledException hidden inside an InvocationTargetException`() {
        val pce = ProcessCanceledException()

        val thrown = assertThrows(ProcessCanceledException::class.java) {
            PhpIndexBridge.rethrowIfPlatformControlFlow(InvocationTargetException(pce))
        }

        // The original PCE must escape, not a wrapper: the platform matches on identity/type.
        assertSame(pce, thrown)
    }

    @Test
    fun `rethrows an IndexNotReadyException hidden inside an InvocationTargetException`() {
        // Swallowing this reported "no PHP class found" during dumb mode instead of letting the
        // platform re-run the resolve once indexing finished.
        //
        // Mocked rather than built via IndexNotReadyException.create(), which needs an initialised
        // Application and therefore only works when some earlier test happened to boot the
        // platform — an order-dependent test we do not want.
        val indexNotReady = mock(IndexNotReadyException::class.java)

        assertThrows(IndexNotReadyException::class.java) {
            PhpIndexBridge.rethrowIfPlatformControlFlow(InvocationTargetException(indexNotReady))
        }
    }

    @Test
    fun `rethrows a bare ProcessCanceledException that was not wrapped`() {
        assertThrows(ProcessCanceledException::class.java) {
            PhpIndexBridge.rethrowIfPlatformControlFlow(ProcessCanceledException())
        }
    }

    @Test
    fun `rethrows an Error rather than absorbing it`() {
        // catch (Throwable) previously swallowed StackOverflowError / OutOfMemoryError too.
        assertThrows(StackOverflowError::class.java) {
            PhpIndexBridge.rethrowIfPlatformControlFlow(InvocationTargetException(StackOverflowError()))
        }
    }

    @Test
    fun `lets a genuine reflection failure through to the caller's fallback`() {
        // "The PHP plugin's API is not the shape we expect" is the case the broad catch exists for:
        // it must NOT be rethrown, so the resolver can degrade to "no PHP target found".
        assertDoesNotThrow {
            PhpIndexBridge.rethrowIfPlatformControlFlow(NoSuchMethodException("getAnyByFQN"))
        }
        assertDoesNotThrow {
            PhpIndexBridge.rethrowIfPlatformControlFlow(InvocationTargetException(IOException("boom")))
        }
    }
}
