package org.phellang.editor.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.files.PhelFile
import javax.swing.Icon

class PhelStructureViewElement(private val element: NavigatablePsiElement) :
    StructureViewTreeElement, SortableTreeElement {

    override fun getValue(): Any = element

    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)

    override fun canNavigate(): Boolean = element.canNavigate()

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getAlphaSortKey(): String = presentationText() ?: ""

    override fun getPresentation(): ItemPresentation = object : ItemPresentation {
        override fun getPresentableText(): String? = presentationText()
        override fun getIcon(unused: Boolean): Icon? = presentationIcon()
    }

    override fun getChildren(): Array<TreeElement> {
        val container = element as? PhelFile ?: return TreeElement.EMPTY_ARRAY
        val topLevelForms = PsiTreeUtil.getChildrenOfType(container, PhelForm::class.java)
            ?: return TreeElement.EMPTY_ARRAY
        return topLevelForms
            .mapNotNull { PsiTreeUtil.findChildOfType(it, PhelList::class.java) }
            .filter { PhelStructuralFormRecognizer.classify(it) != null }
            .map { PhelStructureViewElement(it as NavigatablePsiElement) }
            .toTypedArray()
    }

    private fun presentationText(): String? = when (element) {
        is PhelFile -> element.name
        is PhelList -> textFor(element)
        else -> null
    }

    private fun presentationIcon(): Icon? = when (element) {
        is PhelFile -> element.fileType.icon
        is PhelList -> PhelStructuralFormRecognizer.classify(element)?.icon
        else -> null
    }

    private fun textFor(list: PhelList): String? {
        val form = PhelStructuralFormRecognizer.classify(list) ?: return null
        val name = PhelStructuralFormRecognizer.definedNameOf(list)
        return if (name != null) name else form.keyword
    }
}
