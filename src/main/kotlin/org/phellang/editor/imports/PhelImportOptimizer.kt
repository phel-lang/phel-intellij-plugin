package org.phellang.editor.imports

import com.intellij.lang.ImportOptimizer
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelUnusedImportFinder
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelImportRemoval

/**
 * Removes every unused `(:require …)` from a file on Optimize Imports.
 *
 * The two halves already existed and are unchanged: [PhelUnusedImportFinder] decides what is unused
 * — the same judgement `PhelUnusedImportInspection` reports one at a time — and [PhelImportRemoval]
 * deletes one entry, the same code both quick fixes call. This only does them together, so the
 * action and the inspection can never disagree about what counts as unused.
 *
 * It removes and does not reorder. Sorting the surviving clauses is a separate decision with its own
 * failure mode (a diff on every file the first time anyone runs it), and nothing yet asks for it.
 */
class PhelImportOptimizer : ImportOptimizer {

    override fun supports(file: PsiFile): Boolean = file is PhelFile

    /**
     * Collected here, deleted in the returned [Runnable].
     *
     * The platform calls this outside a write action and runs the result inside one, so nothing may
     * be mutated yet. Smart pointers rather than elements because the PSI can be reparsed in
     * between, which would leave plain references invalid by the time the deletion runs.
     */
    override fun processFile(file: PsiFile): Runnable {
        val unused = unusedImportsIn(file).map {
            SmartPointerManager.getInstance(file.project).createSmartPsiElementPointer(it)
        }

        return Runnable { unused.forEach { pointer -> pointer.element?.let(PhelImportRemoval::removeEnclosingImport) } }
    }

    /**
     * Only symbols inside a `(:require …)` form, for the reason the inspection gives: a
     * namespace-shaped symbol can appear in ordinary code, and the declaration's *own* name is
     * namespace-shaped and never used as a qualifier in its own file.
     */
    private fun unusedImportsIn(file: PsiFile): List<PhelSymbol> {
        val phelFile = file as? PhelFile ?: return emptyList()
        val declaration = PhelNamespaceUtils.findNamespaceDeclaration(phelFile) ?: return emptyList()

        return PhelNamespaceUtils.findRequireForms(declaration)
            .flatMap { PsiTreeUtil.findChildrenOfType(it, PhelSymbol::class.java) }
            .filter(PhelUnusedImportFinder::isUnusedImport)
    }
}
