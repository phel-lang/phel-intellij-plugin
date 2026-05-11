package org.phellang.editor.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.psi.PsiFile
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.files.PhelFile

class PhelStructureViewModel(file: PhelFile) :
    StructureViewModelBase(file, PhelStructureViewElement(file)),
    StructureViewModel.ElementInfoProvider {

    init {
        withSorters(Sorter.ALPHA_SORTER)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean =
        element.value is PsiFile

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean =
        element.value is PhelList
}
