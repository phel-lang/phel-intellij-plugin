package org.phellang.completion.indexing

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.*
import com.intellij.psi.PsiManager
import com.intellij.util.FileContentUtilCore

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

            val fileEditorManager = FileEditorManager.getInstance(project)
            val psiManager = PsiManager.getInstance(project)
            
            val openPhelFiles = fileEditorManager.openFiles
                .filter { it.extension == "phel" }
                .mapNotNull { psiManager.findFile(it)?.virtualFile }
            
            if (openPhelFiles.isNotEmpty()) {
                FileContentUtilCore.reparseFiles(openPhelFiles)
            }
        }
    }
}
