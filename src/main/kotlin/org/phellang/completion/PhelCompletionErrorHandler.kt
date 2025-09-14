package org.phellang.completion

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement

/**
 * Centralized error handling for Phel completion system
 * Provides graceful fallbacks and meaningful error messages for edge cases
 */
object PhelCompletionErrorHandler {
    private val LOG = Logger.getInstance(PhelCompletionErrorHandler::class.java)

    /**
     * Execute completion operation with comprehensive error handling
     * @param operation The completion operation to execute
     * @param context Description of what operation is being performed
     * @param fallbackAction Action to take if the operation fails
     * @return true if operation succeeded, false if fallback was used
     */
    @JvmStatic
    fun withErrorHandling(
        operation: CompletionOperation,
        context: String,
        fallbackAction: Runnable?
    ): Boolean {
        try {
            operation.execute()
            return true
        } catch (e: NullPointerException) {
            LOG.warn("Null pointer exception in completion: $context", e)
            addErrorCompletion(
                operation.result, "⚠️ PSI tree issue",
                "Completion failed due to null PSI element in $context"
            )
            fallbackAction?.run()
            return false
        } catch (e: IndexOutOfBoundsException) {
            LOG.warn("Index out of bounds in completion: $context", e)
            addErrorCompletion(
                operation.result, "⚠️ Malformed syntax",
                "Completion failed due to unexpected syntax structure"
            )
            fallbackAction?.run()
            return false
        } catch (e: ClassCastException) {
            LOG.warn("Type casting error in completion: $context", e)
            addErrorCompletion(
                operation.result, "⚠️ Type mismatch",
                "Completion failed due to unexpected PSI element type"
            )
            fallbackAction?.run()
            return false
        } catch (e: Exception) {
            LOG.error("Unexpected error in completion: $context", e)
            addErrorCompletion(
                operation.result, "❌ Completion error",
                "Unexpected error: " + e.message
            )
            fallbackAction?.run()
            return false
        }
    }

    /**
     * Safely get PSI element text with null checking
     */
    @JvmStatic
    fun safeGetText(element: PsiElement?): String {
        try {
            if (element == null) {
                LOG.debug("Attempted to get text from null PSI element")
                return ""
            }
            val text = element.text
            return text ?: ""
        } catch (e: Exception) {
            LOG.warn("Error getting text from PSI element", e)
            return ""
        }
    }

    /**
     * Safely get PSI element children with null checking
     */
    @JvmStatic
    fun safeGetChildren(element: PsiElement?): Array<PsiElement> {
        try {
            if (element == null) {
                return arrayOf()
            }
            val children: Array<PsiElement> = element.children
            return children
        } catch (_: Exception) {
            return arrayOf()
        }
    }

    /**
     * Safely access array element with bounds checking
     */
    fun <T> safeArrayAccess(array: Array<T>, index: Int): T? {
        if (array.isEmpty()) {
            LOG.debug("Attempted to access element from empty array at index $index")
            return null
        }
        if (index < 0 || index >= array.size) {
            LOG.debug("Array index out of bounds: " + index + " (length: " + array.size + ")")
            return null
        }
        try {
            return array[index]
        } catch (e: Exception) {
            LOG.warn("Unexpected error accessing array element at index $index", e)
            return null
        }
    }

    /**
     * Add error completion item to inform user of completion issues
     */
    private fun addErrorCompletion(
        result: CompletionResultSet?,
        name: String,
        description: String
    ) {
        if (result == null) return

        try {
            val errorElement = LookupElementBuilder.create(name)
                .withTailText(" - $description", true)
                .withIcon(PhelIconProvider.STRUCTURAL)
                .withInsertHandler { context: InsertionContext?, _: LookupElement? ->
                    // Remove the error completion after insertion
                    context!!.document.deleteString(context.startOffset, context.tailOffset)
                }

            result.addElement(errorElement)
        } catch (_: Exception) {
        }
    }

    /**
     * Check if completion context is in a recoverable state
     */
    @JvmStatic
    fun isCompletionContextValid(element: PsiElement?): Boolean {
        if (element == null) {
            LOG.debug("Completion context invalid: null element")
            return false
        }

        // Check if we're in a malformed or incomplete PSI tree
        try {
            element.containingFile
            element.parent
            return true
        } catch (e: Exception) {
            LOG.debug("Completion context invalid: PSI tree issues", e)
            return false
        }
    }

    /**
     * Create a completion operation with result set access
     */
    @JvmStatic
    fun withResultSet(operation: ThrowingRunnable): CompletionOperation {
        return CompletionOperation { operation.run() }
    }

    /**
     * Functional interface for completion operations that can fail
     */
    fun interface CompletionOperation {
        @Throws(Exception::class)
        fun execute()

        val result: CompletionResultSet?
            /**
             * Get the completion result set for error reporting
             * Default implementation returns null - override if error completions needed
             */
            get() = null
    }

    fun interface ThrowingRunnable {
        @Throws(Exception::class)
        fun run()
    }
}
