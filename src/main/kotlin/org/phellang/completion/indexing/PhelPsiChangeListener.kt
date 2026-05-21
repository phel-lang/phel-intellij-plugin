package org.phellang.completion.indexing

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import org.phellang.language.psi.files.PhelFile

class PhelPsiChangeListener(private val project: Project) : PsiTreeChangeAdapter() {

    @Volatile
    private var pendingRefresh: VirtualFile? = null

    @Volatile
    private var refreshScheduled = false

    override fun childAdded(event: PsiTreeChangeEvent) {
        handleChange(event)
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        handleChange(event)
    }

    override fun childReplaced(event: PsiTreeChangeEvent) {
        handleChange(event)
    }

    override fun childrenChanged(event: PsiTreeChangeEvent) {
        handleChange(event)
    }

    private fun handleChange(event: PsiTreeChangeEvent) {
        if (project.isDisposed) return

        val psiFile = event.file as? PhelFile ?: return
        val virtualFile = psiFile.virtualFile ?: return

        // Debounce: store VirtualFile (stable across reparse) and resolve PSI freshly later
        pendingRefresh = virtualFile

        if (!refreshScheduled) {
            refreshScheduled = true
            ApplicationManager.getApplication().invokeLater {
                refreshScheduled = false
                val fileToRefresh = pendingRefresh ?: return@invokeLater
                pendingRefresh = null

                if (project.isDisposed || !fileToRefresh.isValid) return@invokeLater

                runReadAction {
                    if (project.isDisposed) return@runReadAction
                    val freshPsi = PsiManager.getInstance(project).findFile(fileToRefresh) as? PhelFile
                        ?: return@runReadAction
                    PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(freshPsi)
                    DaemonCodeAnalyzer.getInstance(project).restart(freshPsi)
                }
            }
        }
    }
}
