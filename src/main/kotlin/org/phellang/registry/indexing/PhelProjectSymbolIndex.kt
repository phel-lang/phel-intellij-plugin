package org.phellang.registry.indexing

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.phellang.registry.PhelProjectSymbol
import org.phellang.language.psi.files.PhelFile
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

@Service(Service.Level.PROJECT)
class PhelProjectSymbolIndex(private val project: Project) : Disposable {
    /** Cache: shortNamespace -> List of symbols */
    private val symbolsByNamespace = ConcurrentHashMap<String, List<PhelProjectSymbol>>()

    /** Cache: simple name -> List of symbols. Used for fast cross-namespace lookups by name. */
    private val symbolsByName = ConcurrentHashMap<String, List<PhelProjectSymbol>>()

    /** Cache: file path -> List of symbols */
    private val symbolsByFile = ConcurrentHashMap<String, List<PhelProjectSymbol>>()

    /** Whether a full-project scan has completed. Only ever set true by a scan that finished. */
    @Volatile
    private var indexBuilt = false

    /** Single-flight guard so at most one thread runs the full scan at a time. */
    private val buildInProgress = AtomicBoolean(false)

    init {
        // Register file change listener to keep index in sync (for file saves)
        val connection = project.messageBus.connect(this)
        connection.subscribe(VirtualFileManager.VFS_CHANGES, PhelFileChangeListener(project))

        // Register PSI change listener to keep index in sync (for live edits)
        PsiManager.getInstance(project).addPsiTreeChangeListener(PhelPsiChangeListener(project), this)
    }

    override fun dispose() {
        symbolsByNamespace.clear()
        symbolsByName.clear()
        symbolsByFile.clear()
    }

    fun findByName(name: String): List<PhelProjectSymbol> {
        ensureIndexBuilt()
        return symbolsByName[name] ?: emptyList()
    }

    fun getSymbolsForNamespace(shortNamespace: String): List<PhelProjectSymbol> {
        ensureIndexBuilt()
        return symbolsByNamespace[shortNamespace] ?: emptyList()
    }

    fun getAllSymbols(): List<PhelProjectSymbol> {
        ensureIndexBuilt()
        return symbolsByNamespace.values.flatten()
    }

    fun findSymbol(shortNamespace: String, name: String): PhelProjectSymbol? {
        ensureIndexBuilt()
        return symbolsByNamespace[shortNamespace]?.find { it.name == name }
    }

    fun refreshFile(file: VirtualFile) {
        ApplicationManager.getApplication().runReadAction {
            if (project.isDisposed || !file.isValid) return@runReadAction
            val psiFile = PsiManager.getInstance(project).findFile(file) as? PhelFile ?: return@runReadAction
            refreshFileFromPsi(psiFile)
        }
    }

    /**
     * Refreshes the index from the current PSI state (used for live updates during editing).
     */
    fun refreshFileFromPsi(psiFile: PhelFile) {
        val virtualFile = psiFile.virtualFile ?: return
        val filePath = virtualFile.path

        val oldSymbols = symbolsByFile[filePath] ?: emptyList()

        for (symbol in oldSymbols) {
            removeFromMap(symbolsByNamespace, symbol.shortNamespace, filePath)
            removeFromMap(symbolsByName, symbol.name, filePath)
        }

        val newSymbols = PhelProjectSymbolScanner.scanFile(psiFile)
        symbolsByFile[filePath] = newSymbols

        for (symbol in newSymbols) {
            addToMap(symbolsByNamespace, symbol.shortNamespace, symbol)
            addToMap(symbolsByName, symbol.name, symbol)
        }
    }

    private fun addToMap(
        map: ConcurrentHashMap<String, List<PhelProjectSymbol>>,
        key: String,
        symbol: PhelProjectSymbol,
    ) {
        // Atomic read-modify-write so concurrent VFS/PSI listener updates can't lose entries.
        map.compute(key) { _, existing -> (existing ?: emptyList()) + symbol }
    }

    private fun removeFromMap(
        map: ConcurrentHashMap<String, List<PhelProjectSymbol>>,
        key: String,
        filePath: String,
    ) {
        // Returning null from computeIfPresent removes the key entirely.
        map.computeIfPresent(key) { _, current ->
            current.filterNot { it.file.path == filePath }.ifEmpty { null }
        }
    }

    fun removeFile(file: VirtualFile) {
        val oldSymbols = symbolsByFile.remove(file.path) ?: return
        for (symbol in oldSymbols) {
            removeFromMap(symbolsByNamespace, symbol.shortNamespace, file.path)
            removeFromMap(symbolsByName, symbol.name, file.path)
        }
    }

    private fun ensureIndexBuilt() {
        if (indexBuilt) return

        // Single-flight without holding a monitor across the read action. The old
        // `synchronized(this) { runReadAction { … } }` acquired the read lock while holding the
        // monitor — a lock-ordering deadlock shape against the EDT taking the monitor inside a
        // write action. A concurrent caller that finds a build already running returns whatever is
        // indexed so far rather than blocking on the monitor.
        if (!buildInProgress.compareAndSet(false, true)) return
        try {
            // `indexBuilt` flips only when a scan actually completed. A build cut short by
            // cancellation (ProcessCanceledException) or a disposed project leaves it false, so the
            // next call rebuilds instead of trusting a partial/empty index.
            if (buildIndex()) {
                indexBuilt = true
            }
        } finally {
            buildInProgress.set(false)
        }
    }

    /** @return true only if the scan ran to completion (not disposed, not cancelled). */
    private fun buildIndex(): Boolean {
        var completed = false
        ApplicationManager.getApplication().runReadAction {
            if (project.isDisposed) return@runReadAction
            val phelFiles = FilenameIndex.getAllFilesByExt(
                project, "phel", GlobalSearchScope.projectScope(project)
            )

            val psiManager = PsiManager.getInstance(project)
            val psiFiles = phelFiles.mapNotNull { vf ->
                if (vf.isValid) psiManager.findFile(vf) as? PhelFile else null
            }

            indexFiles(psiFiles)
            completed = true
        }
        return completed
    }

    /**
     * Scans [files] into the index, checking for cancellation before each one so a large project's
     * scan can be abandoned (e.g. the user keeps typing during completion). Re-scanning replaces a
     * file's prior entries via [refreshFileFromPsi], so re-running after a cancelled build is safe
     * and never duplicates. Caller must hold read access.
     */
    internal fun indexFiles(files: List<PhelFile>) {
        for (file in files) {
            ProgressManager.checkCanceled()
            refreshFileFromPsi(file)
        }
    }

    companion object {
        fun getInstance(project: Project): PhelProjectSymbolIndex {
            return project.getService(PhelProjectSymbolIndex::class.java)
        }
    }
}
