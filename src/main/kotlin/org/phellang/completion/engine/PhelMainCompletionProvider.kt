package org.phellang.completion.engine

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.phellang.completion.handlers.*
import org.phellang.completion.infrastructure.PhelCompletionErrorHandler
import org.phellang.completion.infrastructure.PhelProjectCompletionHelper
import org.phellang.completion.infrastructure.PhelReferCompletionHelper
import org.phellang.completion.infrastructure.PhelRegistryCompletionHelper
import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.infrastructure.PhelIcons
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile

class PhelMainCompletionProvider : CompletionProvider<CompletionParameters?>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
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
                // Inside :refer vector - suggest functions from the required namespace
                completionContext.isInsideReferVector() -> {
                    val namespace = completionContext.getReferNamespace()
                    if (namespace != null) {
                        val psiFile = completionContext.element.containingFile as? PhelFile
                        val alreadyReferred = completionContext.getAlreadyReferredSymbols()
                        PhelReferCompletionHelper.addReferCompletions(result, namespace, psiFile, alreadyReferred)
                    }
                }

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
            // Extract alias map once for efficient lookup during completion
            val psiFile = element.containingFile as? PhelFile
            val aliasMap = psiFile?.let { PhelNamespaceUtils.extractAliasMap(it) } ?: emptyMap()

            // Local symbols (parameters, let bindings, etc.)
            PhelLocalSymbolCompletions.addLocalSymbols(result, element)

            // Standard library functions
            PhelRegistryCompletionHelper.addCoreFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addStringFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addJsonFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addHtmlFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addHttpFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addDebugFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addBase64Functions(result, aliasMap)
            PhelRegistryCompletionHelper.addTestFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addPhpInteropFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addReplFunctions(result, aliasMap)
            PhelRegistryCompletionHelper.addMockFunctions(result, aliasMap)

            // Project symbols (functions from other project files)
            if (psiFile != null) {
                PhelProjectCompletionHelper.addProjectCompletions(result, psiFile, aliasMap)
            }
        }
    }
}
