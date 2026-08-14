package org.phellang.indexing

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.*
import com.intellij.psi.PsiManager
import org.phellang.language.infrastructure.PhelFileType
import org.phellang.language.psi.files.PhelFile

class PhelFileChangeListener(private val project: Project) : BulkFileListener {

    override fun after(events: List<VFileEvent>) {
        if (project.isDisposed) return

        val index = PhelProjectSymbolIndex.getInstance(project)
        val update = collectUpdates(events, index)

        when {
            // Refreshing calls PhelProjectSymbolScanner.scanFile, which forces a full PSI parse.
            // after() runs inside the VFS write action on the EDT, so parsing here would freeze the
            // UI for the whole changeset — e.g. a git checkout touching many .phel files. Move it
            // off the write action onto a pooled thread.
            update.toRefresh.isNotEmpty() -> scheduleBackgroundRefresh(index, update.toRefresh)
            update.changed -> triggerRehighlight()
        }
    }

    /** What a batch of VFS events means for the index. Removals are applied as they are found. */
    private fun collectUpdates(events: List<VFileEvent>, index: PhelProjectSymbolIndex): IndexUpdate {
        // A set: one batch can carry several events for the same file (e.g. content + property
        // change), and re-scanning it once is enough.
        val toRefresh = LinkedHashSet<VirtualFile>()
        var changed = false

        for (event in events) {
            val file = event.file ?: continue

            when (actionFor(event)) {
                // Removal is cheap (no parse) and idempotent: dropping a path that was never
                // indexed — e.g. a foreign file filtered out by shouldIndex — is a harmless no-op,
                // so there is no need to scope-check a file that is already gone.
                Action.REMOVE -> if (isPhelFile(file)) {
                    index.removeFile(file)
                    changed = true
                }

                Action.REINDEX -> if (shouldIndex(file)) {
                    toRefresh += file
                    changed = true
                }

                Action.IGNORE -> Unit
            }
        }

        return IndexUpdate(toRefresh, changed)
    }

    /** What [event] asks of the index, before any scope filtering. */
    private fun actionFor(event: VFileEvent): Action = when (event) {
        is VFileDeleteEvent -> Action.REMOVE
        is VFileContentChangeEvent, is VFileCreateEvent, is VFileMoveEvent -> Action.REINDEX
        // A rename changes the path the index is keyed on; other property changes do not.
        is VFilePropertyChangeEvent ->
            if (event.propertyName == VirtualFile.PROP_NAME) Action.REINDEX else Action.IGNORE

        else -> Action.IGNORE
    }

    private enum class Action { REMOVE, REINDEX, IGNORE }

    private class IndexUpdate(val toRefresh: Set<VirtualFile>, val changed: Boolean)

    /**
     * A file this project should index: associated with [PhelFileType] and inside project content.
     *
     * `VirtualFileManager.VFS_CHANGES` is an application-level topic, so a project-bus subscription
     * still receives events for `.phel` files anywhere on disk — another open project, a temp or
     * scratch directory. Without the [ProjectFileIndex] check those symbols would leak into this
     * project's completion and go-to-definition.
     */
    internal fun shouldIndex(file: VirtualFile): Boolean {
        return isPhelFile(file) && ProjectFileIndex.getInstance(project).isInContent(file)
    }

    /** File-type association rather than a raw extension string, so custom mappings are honored. */
    private fun isPhelFile(file: VirtualFile): Boolean {
        return FileTypeRegistry.getInstance().isFileOfType(file, PhelFileType.INSTANCE)
    }

    private fun scheduleBackgroundRefresh(index: PhelProjectSymbolIndex, files: Collection<VirtualFile>) {
        ApplicationManager.getApplication().executeOnPooledThread {
            for (file in files) {
                if (project.isDisposed) return@executeOnPooledThread
                ApplicationManager.getApplication().runReadAction {
                    if (project.isDisposed || !file.isValid) return@runReadAction
                    val psiFile = PsiManager.getInstance(project).findFile(file) as? PhelFile
                        ?: return@runReadAction
                    index.refreshFileFromPsi(psiFile)
                }
            }
            triggerRehighlight()
        }
    }

    private fun triggerRehighlight() {
        if (project.isDisposed) return

        ApplicationManager.getApplication().invokeLater {
            if (project.isDisposed) return@invokeLater

            ApplicationManager.getApplication().runReadAction {
                if (project.isDisposed) return@runReadAction
                val fileEditorManager = FileEditorManager.getInstance(project)
                val psiManager = PsiManager.getInstance(project)
                val daemon = DaemonCodeAnalyzer.getInstance(project)

                fileEditorManager.openFiles
                    .filter { isPhelFile(it) }
                    .mapNotNull { psiManager.findFile(it) }
                    // See PhelPsiChangeListener: restart(PsiFile) is deprecated from 2026.1, but is
                    // the only per-file form the supported platform range offers.
                    .forEach { daemon.restart(it) }
            }
        }
    }
}
