package org.phellang.core.utils

import kotlin.jvm.java

object PhelErrorHandler {

    inline fun <T> safeOperation(operation: () -> T?): T? {
        return try {
            operation()
        } catch (e: Exception) {
            println("Exception in file: " + this::class.java + ", caused by " + e.javaClass.name + ". Cause: " + e.cause.toString())
            null
        }
    }
}
