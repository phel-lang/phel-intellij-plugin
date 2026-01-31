package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.Key
import org.phellang.completion.handlers.NamespacedInsertHandler
import org.phellang.core.utils.PhelErrorHandler
import javax.swing.Icon

object PhelCompletionUtils {

    val FULL_NAMESPACE_KEY: Key<String> = Key.create("PHEL_FULL_NAMESPACE")

    @JvmStatic
    fun addLocalSymbolCompletion(result: CompletionResultSet, name: String, type: String, icon: Icon?) {
        PhelErrorHandler.safeOperation {
            val priority = when (type) {
                "Parameter", "Function Parameter" -> PhelCompletionPriority.CURRENT_SCOPE_LOCALS
                "Let Binding", "Local Variable", "Loop Binding" -> PhelCompletionPriority.CURRENT_SCOPE_LOCALS
                "Function", "Function (recursive)", "Global Variable" -> PhelCompletionPriority.RECENT_DEFINITIONS
                else -> PhelCompletionPriority.PROJECT_SYMBOLS
            }

            val element = createLookupElement(name, icon, null, type, bold = true)
            result.addElement(PrioritizedLookupElement.withPriority(element, priority.value))
        }
    }

    @JvmStatic
    fun addRankedCompletion(
        result: CompletionResultSet,
        name: String,
        signature: String,
        description: String,
        priority: PhelCompletionPriority
    ) {
        PhelErrorHandler.safeOperation {
            val element = createLookupElement(name, AllIcons.Nodes.Method, signature, description)
            result.addElement(PrioritizedLookupElement.withPriority(element, priority.value))
        }
    }

    @JvmStatic
    fun addRankedCompletionWithNamespace(
        result: CompletionResultSet,
        name: String,
        signature: String,
        description: String,
        priority: PhelCompletionPriority,
        fullNamespace: String
    ) {
        PhelErrorHandler.safeOperation {
            val element = createLookupElement(name, AllIcons.Nodes.Method, signature, description, fullNamespace = fullNamespace)
            result.addElement(PrioritizedLookupElement.withPriority(element, priority.value))
        }
    }

    @JvmStatic
    private fun createLookupElement(
        name: String,
        icon: Icon?,
        signature: String?,
        description: String?,
        bold: Boolean = false,
        fullNamespace: String? = null
    ): LookupElement {
        return PhelErrorHandler.safeOperation {
            var builder = LookupElementBuilder.create(name).withIcon(icon)

            if (signature != null) {
                builder = builder.withTypeText(signature)
            }

            if (description != null) {
                builder = builder.withTailText(" $description", true)
            }

            // Add boldness if requested (for local symbols)
            if (bold) {
                builder = builder.withBoldness(true)
            }

            // Use namespaced insert handler if the name contains '/'
            if (name.contains('/')) {
                builder = builder.withInsertHandler(NamespacedInsertHandler())
            }

            val element = builder as LookupElement

            // Store full namespace for auto-import if provided
            if (fullNamespace != null) {
                element.putUserData(FULL_NAMESPACE_KEY, fullNamespace)
            }

            element
        } ?: LookupElementBuilder.create(name)
    }
}
