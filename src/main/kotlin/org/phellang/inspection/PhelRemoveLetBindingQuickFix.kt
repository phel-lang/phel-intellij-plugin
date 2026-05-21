package org.phellang.inspection

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelVec

class PhelRemoveLetBindingQuickFix : LocalQuickFix {

    override fun getFamilyName(): String = "Remove unused let binding"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement
        try {
            val targetForm = findBindingForm(element) ?: return
            val vec = targetForm.parent as? PhelVec ?: return

            val forms = vec.forms
            val idx = forms.indexOf(targetForm)
            if (idx < 0) return
            val valueForm = forms.getOrNull(idx + 1) ?: return

            val deleteFrom: PsiElement = leadingWhitespaceOrSelf(targetForm)
            val deleteTo: PsiElement = trailingWhitespaceOrSelf(valueForm)
            vec.node.removeRange(deleteFrom.node, deleteTo.node.treeNext)
        } catch (e: ProcessCanceledException) {
            throw e
        } catch (e: Exception) {
            LOG.warn("Failed to remove let binding", e)
        }
    }

    private fun findBindingForm(element: PsiElement): PhelForm? {
        // The reported element is the binding symbol; walk up to the PhelForm that is a
        // direct child of the binding vector.
        var current: PsiElement? = element
        while (current != null) {
            val parent = current.parent
            if (parent is PhelVec && current is PhelForm) return current
            current = parent
        }
        // Fallback: try ancestor PhelForm whose parent is a PhelVec.
        val form = PsiTreeUtil.getParentOfType(element, PhelForm::class.java) ?: return null
        return if (form.parent is PhelVec) form else null
    }

    private fun leadingWhitespaceOrSelf(form: PhelForm): PsiElement {
        var node = form.prevSibling
        var anchor: PsiElement = form
        while (node is PsiWhiteSpace) {
            anchor = node
            node = node.prevSibling
        }
        return anchor
    }

    private fun trailingWhitespaceOrSelf(form: PhelForm): PsiElement {
        var node = form.nextSibling
        var anchor: PsiElement = form
        while (node is PsiWhiteSpace) {
            anchor = node
            node = node.nextSibling
        }
        return anchor
    }

    companion object {
        private val LOG = Logger.getInstance(PhelRemoveLetBindingQuickFix::class.java)
    }
}
