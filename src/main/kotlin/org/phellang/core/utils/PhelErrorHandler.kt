package org.phellang.core.utils

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.IndexNotReadyException

object PhelErrorHandler {

    @PublishedApi
    internal val LOG = Logger.getInstance(PhelErrorHandler::class.java)

    /**
     * Last-resort guard for an IntelliJ extension-point boundary (a completion provider, an
     * insert handler): the platform calls us, so an escaping exception becomes a user-visible
     * error balloon on every keystroke. Cancellation and dumb-mode signals belong to the
     * platform and are rethrown untouched.
     *
     * Use this *only* at such a boundary. Wrapping ordinary helpers in it converts a bug into
     * a silent `null` and hides the stack trace that would have identified it.
     *
     * [context] names the operation, and is what makes the logged warning actionable.
     */
    inline fun <T> safeOperation(context: String, operation: () -> T?): T? {
        return try {
            operation()
        } catch (e: ProcessCanceledException) {
            throw e
        } catch (e: IndexNotReadyException) {
            throw e
        } catch (e: Exception) {
            LOG.warn("Phel: $context failed", e)
            null
        }
    }
}
