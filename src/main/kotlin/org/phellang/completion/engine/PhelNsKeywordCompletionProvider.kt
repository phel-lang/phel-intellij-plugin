package org.phellang.completion.engine

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.phellang.registry.PhelCompletionPriority
import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.psi.*
import org.phellang.language.psi.utils.PhelPsiUtils

class PhelNsKeywordCompletionProvider : CompletionProvider<CompletionParameters?>() {
    override fun addCompletions(
        parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
    ) {
        PhelErrorHandler.safeOperation("ns keyword completion") {
            addNsKeywordCompletions(parameters.position, result)
        }
    }

    companion object {
        fun addNsKeywordCompletions(element: PsiElement, result: CompletionResultSet) {
            val nsContext = detectNsContext(element) ?: return

            val keywords = when (nsContext) {
                NsContext.NS_BODY_KEYWORD -> NS_TOP_LEVEL_KEYWORDS
                NsContext.REQUIRE_OPTION -> REQUIRE_OPTIONS
                NsContext.USE_OPTION -> USE_OPTIONS
            }

            for (keyword in keywords) {
                val lookupElement = LookupElementBuilder.create(keyword.text)
                    .withTypeText(keyword.signature)
                    .withTailText(" ${keyword.description}", true)
                    .withIcon(AllIcons.Nodes.Tag)
                    .withBoldness(true)

                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        lookupElement, PhelCompletionPriority.NS_KEYWORDS.value
                    )
                )
            }
        }

        /**
         * Which set of `ns` keywords, if any, belongs at the caret.
         *
         * Two shapes reach here: the caret inside an import form that sits directly under `ns`
         * (`(ns n (:require <caret>))`), and the caret one level deeper inside that form's own
         * option (`(ns n (:require m :refer [<caret>]))`).
         */
        fun detectNsContext(element: PsiElement): NsContext? {
            val containingList = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return null
            val parentList = PsiTreeUtil.getParentOfType(containingList, PhelList::class.java) ?: return null

            return if (getFirstFormText(parentList, element) == NS_FORM) {
                detectInImportForm(containingList, element)
            } else {
                detectInNestedOption(parentList, element)
            }
        }

        /** The caret is inside an import form sitting directly under `ns`. */
        private fun detectInImportForm(importList: PhelList, element: PsiElement): NsContext? {
            val firstFormText = getFirstFormText(importList, element)

            // The head slot of the form, so the import keywords themselves belong here.
            if (firstFormText == null || isElementAtFirstFormPosition(importList, element)) {
                return NsContext.NS_BODY_KEYWORD
            }

            // Past the head of a :require / :use, so the per-import options belong here.
            if (firstFormText in IMPORT_FORMS_WITH_OPTIONS) {
                return detectOptionContext(importList, element, firstFormText)
            }

            // Inside :require-file, where the position holds a file path and has no keywords.
            return null
        }

        /** The caret is nested below an import form, e.g. inside its `:refer` vector. */
        private fun detectInNestedOption(importList: PhelList, element: PsiElement): NsContext? {
            val grandparent = PsiTreeUtil.getParentOfType(importList, PhelList::class.java) ?: return null
            if (getFirstFormText(grandparent, element) != NS_FORM) return null

            val firstFormText = getFirstFormText(importList, element) ?: return null
            if (firstFormText !in IMPORT_FORMS_WITH_OPTIONS) return null

            return detectOptionContext(importList, element, firstFormText)
        }

        private fun detectOptionContext(
            importList: PhelList, element: PsiElement, formType: String
        ): NsContext? {
            val precedingKeyword = findPrecedingKeyword(importList, element)

            // Right after :as → alias name position, no keyword suggestions
            if (precedingKeyword == ":as") return null

            // Right after :refer → symbol position, no keyword suggestions here
            // (refer completions are handled by PhelReferCompletionHelper)
            if (precedingKeyword == ":refer") return null

            // After namespace/class or after alias/symbols → suggest available options
            val alreadyUsed = collectExistingKeywords(importList, element)

            return when (formType) {
                ":require" -> {
                    val available = REQUIRE_OPTIONS.filter { it.text !in alreadyUsed }
                    if (available.isNotEmpty()) NsContext.REQUIRE_OPTION else null
                }

                ":use" -> {
                    val available = USE_OPTIONS.filter { it.text !in alreadyUsed }
                    if (available.isNotEmpty()) NsContext.USE_OPTION else null
                }

                else -> null
            }
        }

        private fun findPrecedingKeyword(list: PhelList, element: PsiElement): String? {
            var previousKeyword: String? = null
            for (form in list.forms) {
                if (PsiTreeUtil.isAncestor(form, element, false)) {
                    return previousKeyword
                }

                previousKeyword = PhelPsiUtils.asKeyword(form)?.text
            }

            // Element is after all forms — return the last keyword seen
            return previousKeyword
        }

        private fun collectExistingKeywords(list: PhelList, element: PsiElement): Set<String> {
            val keywords = mutableSetOf<String>()

            for (form in list.forms) {
                // Skip the element being completed
                if (PsiTreeUtil.isAncestor(form, element, false)) continue

                val keyword = PhelPsiUtils.asKeyword(form)
                if (keyword != null) {
                    val text = keyword.text
                    // Only track option keywords, not the form keyword itself
                    if (text == ":as" || text == ":refer") {
                        keywords.add(text)
                    }
                }
            }

            return keywords
        }

        private fun isElementAtFirstFormPosition(list: PhelList, element: PsiElement): Boolean {
            val firstForm = list.forms.firstOrNull() ?: return true
            return PsiTreeUtil.isAncestor(firstForm, element, false)
        }

        private fun getFirstFormText(list: PhelList, completionElement: PsiElement): String? {
            val firstForm = list.forms.firstOrNull() ?: return null

            // If the first form contains the completion element, it's being typed — no established first form
            if (PsiTreeUtil.isAncestor(firstForm, completionElement, false)) {
                return null
            }

            val symbol = PhelPsiUtils.asSymbol(firstForm)
            if (symbol != null) return symbol.text

            return PhelPsiUtils.asKeyword(firstForm)?.text
        }

        private const val NS_FORM = "ns"

        /** `:require-file` takes a path rather than options, so it is deliberately absent. */
        private val IMPORT_FORMS_WITH_OPTIONS = setOf(":require", ":use")

        private val NS_TOP_LEVEL_KEYWORDS = listOf(
            NsKeyword(":require", "(:require namespace)", "Import Phel module"),
            NsKeyword(":require-file", "(:require-file \"path\")", "Import PHP file"),
            NsKeyword(":use", "(:use ClassName)", "Import PHP class"),
        )

        private val REQUIRE_OPTIONS = listOf(
            NsKeyword(":as", ":as alias", "Create namespace alias"),
            NsKeyword(":refer", ":refer [symbols]", "Import specific symbols"),
        )

        private val USE_OPTIONS = listOf(
            NsKeyword(":as", ":as Alias", "Create class alias"),
            NsKeyword(":refer", ":refer [symbols]", "Import specific symbols"),
        )
    }

    enum class NsContext {
        NS_BODY_KEYWORD,
        REQUIRE_OPTION,
        USE_OPTION,
    }

    private data class NsKeyword(
        val text: String,
        val signature: String,
        val description: String,
    )
}
