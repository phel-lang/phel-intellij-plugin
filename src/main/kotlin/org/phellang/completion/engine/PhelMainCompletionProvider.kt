package org.phellang.completion.engine

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.phellang.completion.handlers.*
import org.phellang.completion.infrastructure.PhelProjectCompletionHelper
import org.phellang.completion.infrastructure.PhelReferCompletionHelper
import org.phellang.completion.infrastructure.PhelRegistryCompletionHelper
import org.phellang.completion.infrastructure.PhelUsedClassCompletionHelper
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
        PhelErrorHandler.safeOperation("completion") {
            val element = parameters.position

            val completionContext = PhelCompletionContext(parameters)

            // Suppress completions in inappropriate contexts
            if (completionContext.shouldSuppressCompletions()) {
                return@safeOperation
            }

            // Check if we're inside an ns form — add ns keyword suggestions
            // and suppress general completions at ns keyword positions
            val nsContext = PhelNsKeywordCompletionProvider.detectNsContext(element)
            if (nsContext != null) {
                PhelNsKeywordCompletionProvider.addNsKeywordCompletions(element, result)

                // At keyword positions (NS_BODY_KEYWORD, REQUIRE_OPTION, USE_OPTION),
                // don't show general function completions — they're not relevant
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
                .withInsertHandler(PhelTemplateInsertHandler.PARENTHESIS)
        )

        result.addElement(
            LookupElementBuilder.create("defn").withTypeText("(defn name [args] body)").withIcon(PhelIcons.FILE)
                .withInsertHandler(PhelTemplateInsertHandler.DEFN)
        )

        result.addElement(
            LookupElementBuilder.create("def").withTypeText("(def name value)").withIcon(PhelIcons.FILE)
                .withInsertHandler(PhelTemplateInsertHandler.DEF)
        )

        result.addElement(
            LookupElementBuilder.create("let").withTypeText("(let [bindings] body)").withIcon(PhelIcons.FILE)
                .withInsertHandler(PhelTemplateInsertHandler.LET)
        )

        result.addElement(
            LookupElementBuilder.create("if").withTypeText("(if condition then else)").withIcon(PhelIcons.FILE)
                .withInsertHandler(PhelTemplateInsertHandler.IF)
        )

        result.addElement(
            LookupElementBuilder.create("fn").withTypeText("(fn [args] body)").withIcon(PhelIcons.FILE)
                .withInsertHandler(PhelTemplateInsertHandler.FN)
        )
    }

    private fun addGeneralCompletions(element: PsiElement, result: CompletionResultSet) {
        // Extract alias map once for efficient lookup during completion
        val psiFile = element.containingFile as? PhelFile
        val aliasMap = psiFile?.let { PhelNamespaceUtils.extractAliasMap(it) } ?: emptyMap()

        // Local symbols (parameters, let bindings, etc.)
        PhelLocalSymbolCompletions.addLocalSymbols(result, element)

        PhelRegistryCompletionHelper.addStandardLibraryFunctions(result, aliasMap)

        // Project symbols (functions from other project files)
        if (psiFile != null) {
            PhelProjectCompletionHelper.addProjectCompletions(result, psiFile, aliasMap)
            PhelUsedClassCompletionHelper.addUsedClassCompletions(result, psiFile)
        }
    }
}
