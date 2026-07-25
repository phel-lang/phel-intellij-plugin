package org.phellang.completion.engine

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PlainPrefixMatcher
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.phellang.completion.engine.context.PhelCallPosition
import org.phellang.completion.handlers.PhelTemplateInsertHandler
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
        rawResult: CompletionResultSet,
    ) {
        PhelErrorHandler.safeOperation("completion") {
            complete(parameters, rawResult.withPhelPrefix(parameters))
        }
    }

    private fun complete(parameters: CompletionParameters, result: CompletionResultSet) {
        val element = parameters.position
        val completionContext = PhelCompletionContext(parameters)

        if (completionContext.shouldSuppressCompletions()) return

        // Inside an ns form the keyword suggestions are the only relevant ones, so general function
        // completions are suppressed rather than merged in.
        if (PhelNsKeywordCompletionProvider.detectNsContext(element) != null) {
            PhelNsKeywordCompletionProvider.addNsKeywordCompletions(element, result)
            return
        }

        when {
            completionContext.isInsideReferVector() -> addReferCompletions(completionContext, result)
            completionContext.shouldSuggestNewForm() -> addTemplateCompletions(result)
            else -> addGeneralCompletions(element, result)
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

    /** Inside a `:refer` vector, only the required namespace's own symbols make sense. */
    private fun addReferCompletions(completionContext: PhelCompletionContext, result: CompletionResultSet) {
        val namespace = completionContext.getReferNamespace() ?: return
        val psiFile = completionContext.element.containingFile as? PhelFile

        PhelReferCompletionHelper.addReferCompletions(
            result, namespace, psiFile, completionContext.getAlreadyReferredSymbols()
        )
    }

    private fun addTemplateCompletions(result: CompletionResultSet) {
        for (template in FORM_TEMPLATES) {
            result.addElement(
                LookupElementBuilder.create(template.lookupString)
                    .withTypeText(template.preview)
                    .withIcon(PhelIcons.FILE)
                    .withInsertHandler(template.insertHandler)
            )
        }
    }

    private fun addGeneralCompletions(element: PsiElement, result: CompletionResultSet) {
        val psiFile = element.containingFile as? PhelFile
        val aliasMap = psiFile?.let { PhelNamespaceUtils.extractAliasMap(it) } ?: emptyMap()

        // In Lisp the head of a form is what gets called and the rest are values, so the two
        // positions accept different things — see PhelCallPosition.
        val position = PhelCallPosition.of(element)

        PhelLocalSymbolCompletions.addLocalSymbols(result, element)
        PhelRegistryCompletionHelper.addStandardLibraryFunctions(result, aliasMap, position)

        if (psiFile != null) {
            PhelProjectCompletionHelper.addProjectCompletions(result, psiFile, aliasMap)
            PhelUsedClassCompletionHelper.addUsedClassCompletions(result, psiFile)
        }
    }

    /** A structural suggestion offered at top level, where there is no form to complete against. */
    private class FormTemplate(
        val lookupString: String,
        val preview: String,
        val insertHandler: PhelTemplateInsertHandler,
    )

    private companion object {
        val FORM_TEMPLATES = listOf(
            FormTemplate("()", "(...)", PhelTemplateInsertHandler.PARENTHESIS),
            FormTemplate("defn", "(defn name [args] body)", PhelTemplateInsertHandler.DEFN),
            FormTemplate("def", "(def name value)", PhelTemplateInsertHandler.DEF),
            FormTemplate("let", "(let [bindings] body)", PhelTemplateInsertHandler.LET),
            FormTemplate("if", "(if condition then else)", PhelTemplateInsertHandler.IF),
            FormTemplate("fn", "(fn [args] body)", PhelTemplateInsertHandler.FN),
        )
    }
}
