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

        fun detectNsContext(element: PsiElement): NsContext? {
            val containingList = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return null
            val firstFormText = getFirstFormText(containingList, element)

            // Case 1: Directly inside (ns ...) — not useful (ns name is second position)
            // Case 2: Inside a sub-list of ns — this is where import forms go
            // Case 3: Inside a sub-list of a sub-list of ns — :as/:refer options

            // Check if the containing list is a direct child of the ns form
            val parentList = PsiTreeUtil.getParentOfType(containingList, PhelList::class.java)

            if (parentList != null && getFirstFormText(parentList, element) == "ns") {
                // We're in a sub-list of ns, e.g., (ns name (|)) or (ns name (:require |))
                // At first position of this sub-list → suggest :require, :require-file, :use
                if (firstFormText == null || isElementAtFirstFormPosition(containingList, element)) {
                    return NsContext.NS_BODY_KEYWORD
                }

                // After the form keyword (:require/:use), check position for :as/:refer
                if (firstFormText == ":require" || firstFormText == ":use") {
                    return detectOptionContext(containingList, element, firstFormText)
                }

                // Inside :require-file — no keyword suggestions (file path position)
                return null
            }

            // Check if we're two levels deep: sub-list → sub-list of ns
            // This handles (:require ns :refer [|]) where element is inside a vector
            if (parentList != null) {
                val grandparentList = PsiTreeUtil.getParentOfType(parentList, PhelList::class.java)
                if (grandparentList != null && getFirstFormText(grandparentList, element) == "ns") {
                    val parentFirstForm = getFirstFormText(parentList, element)
                    if (parentFirstForm == ":require" || parentFirstForm == ":use") {
                        return detectOptionContext(parentList, element, parentFirstForm)
                    }
                }
            }

            return null
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
