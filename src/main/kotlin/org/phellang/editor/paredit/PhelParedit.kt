package org.phellang.editor.paredit

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm

object PhelParedit {
    fun slurpForward(file: PsiFile, offset: Int): List<PhelTextEdit>? {
        val container = PhelParediContainers.findEnclosingContainer(file, offset) ?: return null
        val wrapper = container.parent as? PhelForm ?: return null
        val nextForm = PsiTreeUtil.getNextSiblingOfType(wrapper, PhelForm::class.java) ?: return null
        val close = container.lastChild ?: return null

        return listOf(
            PhelTextEdit.insert(nextForm.textRange.endOffset, close.text),
            PhelTextEdit.delete(close.textRange),
        )
    }

    fun barfForward(file: PsiFile, offset: Int): List<PhelTextEdit>? {
        val container = PhelParediContainers.findEnclosingContainer(file, offset) ?: return null
        val close = container.lastChild ?: return null
        val children = formChildrenOf(container)
        if (children.size < 2) return null

        val newCloseOffset = children[children.size - 2].textRange.endOffset
        return listOf(
            PhelTextEdit.delete(close.textRange),
            PhelTextEdit.insert(newCloseOffset, close.text),
        )
    }

    fun slurpBackward(file: PsiFile, offset: Int): List<PhelTextEdit>? {
        val container = PhelParediContainers.findEnclosingContainer(file, offset) ?: return null
        val wrapper = container.parent as? PhelForm ?: return null
        val prevForm = PsiTreeUtil.getPrevSiblingOfType(wrapper, PhelForm::class.java) ?: return null
        val open = container.firstChild ?: return null

        return listOf(
            PhelTextEdit.delete(open.textRange),
            PhelTextEdit.insert(prevForm.textRange.startOffset, open.text),
        )
    }

    fun barfBackward(file: PsiFile, offset: Int): List<PhelTextEdit>? {
        val container = PhelParediContainers.findEnclosingContainer(file, offset) ?: return null
        val open = container.firstChild ?: return null
        val children = formChildrenOf(container)
        if (children.size < 2) return null

        val newOpenOffset = children[1].textRange.startOffset
        return listOf(
            PhelTextEdit.insert(newOpenOffset, open.text),
            PhelTextEdit.delete(open.textRange),
        )
    }

    fun wrap(file: PsiFile, offset: Int, open: Char, close: Char): List<PhelTextEdit>? {
        val form = PhelParediContainers.findFormAtCaret(file, offset) ?: return null
        return listOf(
            PhelTextEdit.insert(form.textRange.endOffset, close.toString()),
            PhelTextEdit.insert(form.textRange.startOffset, open.toString()),
        )
    }

    fun splice(file: PsiFile, offset: Int): List<PhelTextEdit>? {
        val container = PhelParediContainers.findEnclosingContainer(file, offset) ?: return null
        val open = container.firstChild ?: return null
        val close = container.lastChild ?: return null
        if (open === close) return null

        return listOf(
            PhelTextEdit.delete(close.textRange),
            PhelTextEdit.delete(open.textRange),
        )
    }

    fun raise(file: PsiFile, offset: Int): List<PhelTextEdit>? {
        val form = PhelParediContainers.findFormAtCaret(file, offset) ?: return null
        val container = PhelParediContainers.findEnclosingContainer(file, offset) ?: return null
        val containerWrapper = container.parent as? PhelForm ?: return null
        if (form === containerWrapper) return null

        return listOf(PhelTextEdit(containerWrapper.textRange, form.text))
    }

    private fun formChildrenOf(container: PsiElement): List<PhelForm> {
        val arr = PsiTreeUtil.getChildrenOfType(container, PhelForm::class.java) ?: return emptyList()
        return arr.toList()
    }
}
