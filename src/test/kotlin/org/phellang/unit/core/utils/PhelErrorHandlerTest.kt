package org.phellang.unit.core.utils

import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.IndexNotReadyException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.core.utils.PhelErrorHandler

class PhelErrorHandlerTest {

    @Test
    fun `returns the operation's result when it succeeds`() {
        val result = PhelErrorHandler.safeOperation("test") { "success" }

        assertEquals("success", result)
    }

    @Test
    fun `returns null when the operation throws`() {
        val result = PhelErrorHandler.safeOperation<String>("test") {
            throw RuntimeException("Test exception")
        }

        assertNull(result)
    }

    @Test
    fun `rethrows ProcessCanceledException so the platform can cancel the read action`() {
        assertThrows(ProcessCanceledException::class.java) {
            PhelErrorHandler.safeOperation("test") {
                throw ProcessCanceledException()
            }
        }
    }

    @Test
    fun `rethrows IndexNotReadyException instead of degrading to an empty result`() {
        // IndexNotReadyException must reach the platform, which reruns us once indexing finishes.
        // Swallowing it would silently return "nothing found" during dumb mode.
        assertThrows(IndexNotReadyException::class.java) {
            PhelErrorHandler.safeOperation("test") {
                throw IndexNotReadyException.create()
            }
        }
    }
}
