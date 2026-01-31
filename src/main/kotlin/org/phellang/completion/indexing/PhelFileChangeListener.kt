package org.phellang.completion.indexing

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.*
import com.intellij.psi.PsiManager

class PhelFileChangeListener(private val project: Project) : BulkFileListener {

    override fun after(events: List<VFileEvent>) {
        if (project.isDisposed) return

        val index = PhelProjectSymbolIndex.getInstance(project)
        var needsReparse = false

        for (event in events) {
            val file = event.file ?: continue

            if (!isPhelFile(file)) continue

            when (event) {
                is VFileContentChangeEvent -> {
                    // File content changed - refresh its symbols
                    index.refreshFile(file)
                    needsReparse = true
                }

                is VFileCreateEvent -> {
                    // New file created - add to index
                    index.refreshFile(file)
                    needsReparse = true
                }

                is VFileDeleteEvent -> {
                    // File deleted - remove from index
                    index.removeFile(file)
                    needsReparse = true
                }

                is VFileMoveEvent -> {
                    // File moved - refresh index
                    index.refreshFile(file)
                    needsReparse = true
                }

                is VFilePropertyChangeEvent -> {
                    // File renamed
                    if (event.propertyName == VirtualFile.PROP_NAME) {
                        index.refreshFile(file)
                        needsReparse = true
                    }
                }
            }
        }

        // Trigger re-highlighting of open editors if needed
        if (needsReparse) {
            triggerRehighlight()
        }
    }

    private fun isPhelFile(file: VirtualFile): Boolean {
        return file.extension == "phel"
    }

    private fun triggerRehighlight() {
        if (project.isDisposed) return

        ApplicationManager.getApplication().invokeLater {
            if (project.isDisposed) return@invokeLater

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
}
