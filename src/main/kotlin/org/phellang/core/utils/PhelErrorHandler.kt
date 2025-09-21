package org.phellang.core.utils

import com.intellij.openapi.progress.ProcessCanceledException
import kotlin.jvm.java

object PhelErrorHandler {

    inline fun <T> safeOperation(operation: () -> T?): T? {
        return try {
            operation()
        } catch (e: ProcessCanceledException) {
            // ProcessCanceledException should always be re-thrown
            // This is IntelliJ's way of cancelling background operations
            throw e
        } catch (e: Exception) {
            // Only log other exceptions
            println("Exception in file: " + this::class.java + ", caused by " + e.javaClass.name + ". Cause: " + e.cause.toString())
            null
        }
    }
}
