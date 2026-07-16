package org.phellang.completion.engine

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.completion.PlainPrefixMatcher
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
        rawResult: CompletionResultSet
    ) {
        PhelErrorHandler.safeOperation("completion") {
            val element = parameters.position
            val result = rawResult.withPhelPrefix(parameters)

            val completionContext = PhelCompletionContext(parameters)

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

    /**
     * Re-derives the completion prefix using Phel's identifier alphabet.
     *
     * The platform computes the initial prefix with Java's rules, so it stops at the first character
     * that is not a Java identifier part. In Phel that severs a symbol at `/`, `-`, `?`, `!` and the
     * rest of [PhelCompletionCharFilter]'s alphabet: invoking completion at `(s/|)` yields an *empty*
     * prefix, so nothing filters on `s/` and the whole registry is offered. Typing the same text
     * works only because the char filter widens the prefix while a lookup is already open — which is
     * why this is invisible until you ask for completion at an existing symbol.
     *
     * [PlainPrefixMatcher] rather than the default: `s/upper-case` must match the literal prefix
     * `s/`, and camel-hump matching has nothing to offer kebab-case Phel names anyway.
     */
    private fun CompletionResultSet.withPhelPrefix(parameters: CompletionParameters): CompletionResultSet {
        val position = parameters.position
        val caretInElement = parameters.offset - position.textRange.startOffset
        if (caretInElement <= 0 || caretInElement > position.text.length) return this

        val prefix = position.text.take(caretInElement)
        if (prefix == prefixMatcher.prefix) return this

        return withPrefixMatcher(PlainPrefixMatcher(prefix))
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
        val psiFile = element.containingFile as? PhelFile
        val aliasMap = psiFile?.let { PhelNamespaceUtils.extractAliasMap(it) } ?: emptyMap()

        PhelLocalSymbolCompletions.addLocalSymbols(result, element)

        PhelRegistryCompletionHelper.addStandardLibraryFunctions(result, aliasMap)

        if (psiFile != null) {
            PhelProjectCompletionHelper.addProjectCompletions(result, psiFile, aliasMap)
            PhelUsedClassCompletionHelper.addUsedClassCompletions(result, psiFile)
        }
    }
}
