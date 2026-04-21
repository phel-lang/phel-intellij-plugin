package org.phellang.core.utils

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException

object PhelErrorHandler {

    @PublishedApi
    internal val LOG = Logger.getInstance(PhelErrorHandler::class.java)

    inline fun <T> safeOperation(operation: () -> T?): T? {
        return try {
            operation()
        } catch (e: ProcessCanceledException) {
            throw e
        } catch (e: Exception) {
            LOG.warn("Exception in ${e.javaClass.name}: cause=${e.cause?.toString() ?: "none"}", e)
            null
        }
    }
}
