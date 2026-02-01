package org.phellang.completion.indexing

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import org.phellang.language.psi.files.PhelFile

class PhelPsiChangeListener(private val project: Project) : PsiTreeChangeAdapter() {

    private var pendingRefresh: PhelFile? = null
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
        if (psiFile.virtualFile == null) return

        // Debounce: schedule refresh after a short delay to batch rapid changes
        pendingRefresh = psiFile

        if (!refreshScheduled) {
            refreshScheduled = true
            ApplicationManager.getApplication().invokeLater {
                if (project.isDisposed) return@invokeLater

                refreshScheduled = false
                val fileToRefresh = pendingRefresh ?: return@invokeLater
                pendingRefresh = null

                if (fileToRefresh.virtualFile == null) return@invokeLater

                // Refresh the index with the current PSI state
                val index = PhelProjectSymbolIndex.getInstance(project)
                index.refreshFileFromPsi(fileToRefresh)

                // Trigger re-highlighting of open editors
                triggerRehighlight()
            }
        }
    }

    private fun triggerRehighlight() {
        if (project.isDisposed) return

        val psiManager = PsiManager.getInstance(project)
        val fileEditorManager = FileEditorManager.getInstance(project)
        val daemonCodeAnalyzer = DaemonCodeAnalyzer.getInstance(project)

        // Re-analyze all open Phel files
        for (file in fileEditorManager.openFiles) {
            if (file.extension != "phel") continue
            val psiFile = psiManager.findFile(file) ?: continue
            daemonCodeAnalyzer.restart(psiFile)
        }
    }
}
