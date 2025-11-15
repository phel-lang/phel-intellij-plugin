package org.phellang.completion.engine

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.phellang.language.infrastructure.PhelIcons
import org.phellang.completion.handlers.*
import org.phellang.completion.infrastructure.PhelCompletionErrorHandler
import org.phellang.completion.infrastructure.PhelRegistryCompletionHelper
import org.phellang.core.utils.PhelErrorHandler

class PhelMainCompletionProvider : CompletionProvider<CompletionParameters?>() {

    override fun addCompletions(
        parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
    ) {
        PhelErrorHandler.safeOperation {
            val element = parameters.position

            check(PhelCompletionErrorHandler.isCompletionContextValid(element)) {
                "Invalid completion context"
            }

            val completionContext = PhelCompletionContext(parameters)

            // Suppress completions in inappropriate contexts
            if (completionContext.shouldSuppressCompletions()) {
                return@safeOperation
            }

            when {
                // At top level (nothing written) - suggest structural completions
                completionContext.shouldSuggestNewForm() -> {
                    addTemplateCompletions(result)
                }

                // Inside parentheses - suggest function completions
                completionContext.isInsideParentheses() -> {
                    addGeneralCompletions(completionContext.element, result)
                }

                // Default case - general completions
                else -> {
                    addGeneralCompletions(completionContext.element, result)
                }
            }
        }
    }

    private fun addTemplateCompletions(result: CompletionResultSet) {
        result.addElement(
            LookupElementBuilder.create("()").withTypeText("(...)").withIcon(PhelIcons.FILE)
                .withInsertHandler(ParenthesisTemplateInsertHandler())
        )

        result.addElement(
            LookupElementBuilder.create("defn").withTypeText("(defn name [args] body)").withIcon(PhelIcons.FILE)
                .withInsertHandler(DefnTemplateInsertHandler())
        )

        result.addElement(
            LookupElementBuilder.create("def").withTypeText("(def name value)").withIcon(PhelIcons.FILE)
                .withInsertHandler(DefTemplateInsertHandler())
        )

        result.addElement(
            LookupElementBuilder.create("let").withTypeText("(let [bindings] body)").withIcon(PhelIcons.FILE)
                .withInsertHandler(LetTemplateInsertHandler())
        )

        result.addElement(
            LookupElementBuilder.create("if").withTypeText("(if condition then else)").withIcon(PhelIcons.FILE)
                .withInsertHandler(IfTemplateInsertHandler())
        )

        result.addElement(
            LookupElementBuilder.create("fn").withTypeText("(fn [args] body)").withIcon(PhelIcons.FILE)
                .withInsertHandler(FnTemplateInsertHandler())
        )
    }

    private fun addGeneralCompletions(element: PsiElement, result: CompletionResultSet) {
        PhelErrorHandler.safeOperation {
            PhelLocalSymbolCompletions.addLocalSymbols(result, element)
            PhelRegistryCompletionHelper.addCoreFunctions(result)
            PhelRegistryCompletionHelper.addStringFunctions(result)
            PhelRegistryCompletionHelper.addJsonFunctions(result)
            PhelRegistryCompletionHelper.addHtmlFunctions(result)
            PhelRegistryCompletionHelper.addHttpFunctions(result)
            PhelRegistryCompletionHelper.addDebugFunctions(result)
            PhelRegistryCompletionHelper.addBase64Functions(result)
            PhelRegistryCompletionHelper.addTestFunctions(result)
            PhelRegistryCompletionHelper.addPhpInteropFunctions(result)
            PhelRegistryCompletionHelper.addReplFunctions(result)
            PhelRegistryCompletionHelper.addMockFunctions(result)
        }
    }
}
