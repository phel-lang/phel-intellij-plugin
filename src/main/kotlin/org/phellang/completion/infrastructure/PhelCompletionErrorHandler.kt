package org.phellang.completion.infrastructure

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.psi.PsiElement

object PhelCompletionErrorHandler {
    private val LOG = Logger.getInstance(PhelCompletionErrorHandler::class.java)

    @JvmStatic
    fun withErrorHandling(
        operation: CompletionOperation, context: String, fallbackAction: Runnable?
    ): Boolean {
        try {
            operation.execute()
            return true
        } catch (e: ProcessCanceledException) {
            throw e
        } catch (e: IndexNotReadyException) {
            throw e
        } catch (e: NullPointerException) {
            LOG.warn("Null pointer exception in completion: $context", e)
            fallbackAction?.run()
            return false
        } catch (e: IndexOutOfBoundsException) {
            LOG.warn("Index out of bounds in completion: $context", e)
            fallbackAction?.run()
            return false
        } catch (e: ClassCastException) {
            LOG.warn("Type casting error in completion: $context", e)
            fallbackAction?.run()
            return false
        } catch (e: Exception) {
            LOG.error("Unexpected error in completion: $context", e)
            fallbackAction?.run()
            return false
        }
    }

    @JvmStatic
    fun isCompletionContextValid(element: PsiElement?): Boolean {
        if (element == null) {
            LOG.debug("Completion context invalid: null element")
            return false
        }

        try {
            element.containingFile
            element.parent
            return true
        } catch (e: ProcessCanceledException) {
            throw e
        } catch (e: Exception) {
            LOG.debug("Completion context invalid: PSI tree issues", e)
            return false
        }
    }

    @JvmStatic
    fun withResultSet(operation: ThrowingRunnable): CompletionOperation =
        CompletionOperation { operation.run() }

    fun interface CompletionOperation {
        @Throws(Exception::class)
        fun execute()
    }

    fun interface ThrowingRunnable {
        @Throws(Exception::class)
        fun run()
    }
}
