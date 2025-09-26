package org.phellang.completion.engine

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.infrastructure.PhelCompletionPriority
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.core.utils.PhelErrorHandler
import org.phellang.language.psi.*

class PhelCompletionContext(
    parameters: CompletionParameters
) {
    val element: PsiElement = parameters.position

    fun shouldSuggestNewForm(): Boolean {
        return PhelErrorHandler.safeOperation {
            isAtFileLevel()
        } ?: false
    }

    fun isInsideParentheses(): Boolean {
        return PhelErrorHandler.safeOperation {
            val parent = element.parent

            isAfterOpeningParen() || (parent is PhelList && parent.parent !is PhelFile)
        } ?: false
    }

    fun shouldSuppressCompletions(): Boolean {
        return PhelErrorHandler.safeOperation {
            isInFunctionNamePosition() || isInParameterDefinitionPosition() || isInBindingDefinitionPosition() || isInDefinitionNamePosition()
        } ?: false
    }

    private fun isAtFileLevel(): Boolean {
        var current = element
        var depth = 0

        while (depth < 10) { // Prevent infinite loops
            if (current is PhelFile) {
                return true
            }

            if (current is PhelList) {
                return false
            }

            current = current.parent
            depth++
        }

        return false
    }

    private fun isAfterOpeningParen(): Boolean {
        return PhelErrorHandler.safeOperation {
            val text = element.containingFile.text
            val offset = element.textOffset

            if (offset > 0) {
                val charBefore = text[offset - 1]
                charBefore == '(' || charBefore == '[' || charBefore == '{'
            } else {
                false
            }
        } ?: false
    }

    private fun isInFunctionNamePosition(): Boolean {
        val containingList = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return false
        val forms = containingList.forms

        if (forms.isEmpty()) return false

        val parentList = PsiTreeUtil.getParentOfType(containingList, PhelList::class.java)
        if (parentList != null) {
            val parentForms = parentList.forms
            if (parentForms.isNotEmpty()) {
                val parentFirstForm = parentForms[0]
                if (parentFirstForm is PhelSymbol || parentFirstForm is PhelAccess) {
                    val parentType = parentFirstForm.text
                    if (PhelSymbolAnalyzer.isSymbolType(parentType, PhelCompletionPriority.SPECIAL_FORMS)) {
                        return false
                    }
                }
            }
        }

        val firstForm = forms[0]
        if (firstForm is PhelSymbol || firstForm is PhelAccess) {
            val formType = firstForm.text
            if (PhelSymbolAnalyzer.isSymbolType(formType, PhelCompletionPriority.SPECIAL_FORMS)) {
                if (forms.size >= 2) {
                    val nameForm = forms[1]
                    return isElementPartOfForm(element, nameForm)
                }
            }
        }

        return false
    }

    private fun isInParameterDefinitionPosition(): Boolean {
        val containingVec = PsiTreeUtil.getParentOfType(element, PhelVec::class.java) ?: return false
        val containingList = PsiTreeUtil.getParentOfType(containingVec, PhelList::class.java) ?: return false

        val children = containingList.children
        if (children.isEmpty()) return false

        val functionType = when (val firstChild = children[0]) {
            is PhelSymbol -> firstChild.text
            is PhelAccess -> firstChild.text
            else -> return false
        }

        return when (functionType) {
            "fn" -> {
                children.size >= 2 && children[1] === containingVec
            }

            "defn", "defn-", "defmacro", "defmacro-" -> {
                for (i in 2 until children.size) {
                    if (children[i] === containingVec) {
                        return true
                    }
                    if (children[i] is PhelVec) {
                        return false
                    }
                }
                false
            }

            else -> false
        }
    }

    private fun isInBindingDefinitionPosition(): Boolean {
        val containingVec = PsiTreeUtil.getParentOfType(element, PhelVec::class.java) ?: return false
        val containingList = PsiTreeUtil.getParentOfType(containingVec, PhelList::class.java) ?: return false

        val children = containingList.children
        if (children.size < 2) return false

        val bindingType = when (val firstChild = children[0]) {
            is PhelSymbol -> firstChild.text
            is PhelAccess -> firstChild.text
            else -> return false
        }

        val isBindingForm = PhelSymbolAnalyzer.isSymbolType(bindingType, PhelCompletionPriority.CONTROL_FLOW)
        if (!isBindingForm) return false

        if (children[1] !== containingVec) return false

        val bindingChildren = containingVec.children
        for (i in bindingChildren.indices step 2) {
            val bindingChild = bindingChildren[i]
            if (isElementPartOfForm(element, bindingChild)) {
                return true
            }
        }

        return false
    }

    private fun isInDefinitionNamePosition(): Boolean {
        val containingList = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return false
        val children = containingList.children

        if (children.size < 2) return false

        val defType = when (val firstChild = children[0]) {
            is PhelSymbol -> firstChild.text
            is PhelAccess -> firstChild.text
            else -> return false
        }

        val isDefinitionForm = PhelSymbolAnalyzer.isSymbolType(defType, PhelCompletionPriority.SPECIAL_FORMS)
        if (!isDefinitionForm) return false

        val nameElement = children[1]
        return isElementPartOfForm(element, nameElement)
    }

    private fun isElementPartOfForm(element: PsiElement, form: PsiElement): Boolean {
        var current: PsiElement? = element
        while (current != null) {
            if (current === form) return true
            if (current is PhelList || current is PhelVec || current is PhelMap) break
            current = current.parent
        }
        return false
    }
}
