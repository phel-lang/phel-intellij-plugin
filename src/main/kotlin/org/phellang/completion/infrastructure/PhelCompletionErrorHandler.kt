package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.diagnostic.Logger
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
        } catch (e: NullPointerException) {
            LOG.warn("Null pointer exception in completion: $context", e)
            addErrorCompletion(
                operation.result, "⚠️ PSI tree issue", "Completion failed due to null PSI element in $context"
            )
            fallbackAction?.run()
            return false
        } catch (e: IndexOutOfBoundsException) {
            LOG.warn("Index out of bounds in completion: $context", e)
            addErrorCompletion(
                operation.result, "⚠️ Malformed syntax", "Completion failed due to unexpected syntax structure"
            )
            fallbackAction?.run()
            return false
        } catch (e: ClassCastException) {
            LOG.warn("Type casting error in completion: $context", e)
            addErrorCompletion(
                operation.result, "⚠️ Type mismatch", "Completion failed due to unexpected PSI element type"
            )
            fallbackAction?.run()
            return false
        } catch (e: Exception) {
            LOG.error("Unexpected error in completion: $context", e)
            addErrorCompletion(
                operation.result, "❌ Completion error", "Unexpected error: " + e.message
            )
            fallbackAction?.run()
            return false
        }
    }

    private fun addErrorCompletion(
        result: CompletionResultSet?, name: String, description: String
    ) {
        if (result == null) return

        try {
            val errorElement = LookupElementBuilder.create(name).withTailText(" - $description", true)
                .withIcon(AllIcons.General.Information)
                .withInsertHandler { context: InsertionContext?, _: LookupElement? ->
                    context!!.document.deleteString(context.startOffset, context.tailOffset)
                }

            result.addElement(errorElement)
        } catch (_: Exception) {
        }
    }

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

    @JvmStatic
    fun withResultSet(operation: ThrowingRunnable): CompletionOperation {
        return CompletionOperation { operation.run() }
    }

    fun interface CompletionOperation {
        @Throws(Exception::class)
        fun execute()

        val result: CompletionResultSet? get() = null
    }

    fun interface ThrowingRunnable {
        @Throws(Exception::class)
        fun run()
    }
}
