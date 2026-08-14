package org.phellang.indexing

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import org.phellang.language.psi.files.PhelFile
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

class PhelPsiChangeListener(private val project: Project) : PsiTreeChangeAdapter() {

    /**
     * Every file edited since the last pass, not just the most recent one.
     *
     * This was a single slot. Editing file A and then file B before the scheduled pass ran overwrote
     * A's entry, and the already-set `refreshScheduled` flag meant no second pass was queued — so A's
     * symbols stayed stale until something else happened to touch it. A split editor, a multi-file
     * quick fix or a rename across files hit this every time.
     *
     * VirtualFile rather than PsiFile: it stays valid across the reparse, and the PSI is resolved
     * freshly when the pass runs.
     */
    private val pendingRefresh: MutableSet<VirtualFile> = ConcurrentHashMap.newKeySet()

    /** Coalesces one typing burst into a single pass. */
    private val refreshScheduled = AtomicBoolean(false)

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

        pendingRefresh += virtualFile

        if (refreshScheduled.compareAndSet(false, true)) {
            ApplicationManager.getApplication().invokeLater { refreshPending() }
        }
    }

    /**
     * The flag is cleared before the set is drained, so an edit arriving mid-pass schedules the next
     * pass rather than being swallowed by this one. The cost is an occasional redundant refresh,
     * which [PhelProjectSymbolIndex.refreshFileFromPsi] is idempotent under.
     */
    private fun refreshPending() {
        refreshScheduled.set(false)

        val files = pendingRefresh.toSet()
        pendingRefresh -= files

        if (project.isDisposed) return

        ApplicationManager.getApplication().runReadAction {
            if (project.isDisposed) return@runReadAction

            val psiManager = PsiManager.getInstance(project)
            val index = PhelProjectSymbolIndex.getInstance(project)
            val daemon = DaemonCodeAnalyzer.getInstance(project)

            for (file in files) {
                if (!file.isValid) continue
                val freshPsi = psiManager.findFile(file) as? PhelFile ?: continue

                index.refreshFileFromPsi(freshPsi)
                // restart(PsiFile) is deprecated from 2026.1, which the Marketplace verifier
                // reports. It stays until sinceBuild moves past 243: the platforms this plugin
                // supports expose only restart() and restart(PsiFile), so there is nothing else to
                // call, and the no-arg form would rehighlight every open file instead of this one.
                daemon.restart(freshPsi)
            }
        }
    }
}
