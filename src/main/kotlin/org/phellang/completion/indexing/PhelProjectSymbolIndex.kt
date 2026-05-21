package org.phellang.completion.indexing

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.phellang.completion.data.PhelProjectSymbol
import org.phellang.language.psi.files.PhelFile
import java.util.concurrent.ConcurrentHashMap

@Service(Service.Level.PROJECT)
class PhelProjectSymbolIndex(private val project: Project) : Disposable {

    /** Cache: shortNamespace -> List of symbols */
    private val symbolsByNamespace = ConcurrentHashMap<String, List<PhelProjectSymbol>>()

    /** Cache: simple name -> List of symbols. Used for fast cross-namespace lookups by name. */
    private val symbolsByName = ConcurrentHashMap<String, List<PhelProjectSymbol>>()

    /** Cache: file path -> List of symbols */
    private val symbolsByFile = ConcurrentHashMap<String, List<PhelProjectSymbol>>()

    /** Whether the index has been built */
    @Volatile
    private var indexBuilt = false

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
        ReadAction.run<RuntimeException> {
            if (project.isDisposed || !file.isValid) return@run
            val psiFile = PsiManager.getInstance(project).findFile(file) as? PhelFile ?: return@run
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

        // Remove old symbols from namespace + name indices
        for (symbol in oldSymbols) {
            removeFromMap(symbolsByNamespace, symbol.shortNamespace, filePath)
            removeFromMap(symbolsByName, symbol.name, filePath)
        }

        // Scan file for new symbols from the current PSI state
        val newSymbols = PhelProjectSymbolScanner.scanFile(psiFile)
        symbolsByFile[filePath] = newSymbols

        // Add new symbols to namespace + name indices
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
        val existing = map[key]?.toMutableList() ?: mutableListOf()
        existing.add(symbol)
        map[key] = existing
    }

    private fun removeFromMap(
        map: ConcurrentHashMap<String, List<PhelProjectSymbol>>,
        key: String,
        filePath: String,
    ) {
        val current = map[key]?.toMutableList() ?: return
        current.removeIf { it.file.path == filePath }
        if (current.isEmpty()) map.remove(key) else map[key] = current
    }

    fun removeFile(file: VirtualFile) {
        val oldSymbols = symbolsByFile.remove(file.path) ?: return
        for (symbol in oldSymbols) {
            removeFromMap(symbolsByNamespace, symbol.shortNamespace, file.path)
            removeFromMap(symbolsByName, symbol.name, file.path)
        }
    }

    private fun ensureIndexBuilt() {
        if (!indexBuilt) {
            synchronized(this) {
                if (!indexBuilt) {
                    buildIndex()
                    indexBuilt = true
                }
            }
        }
    }

    private fun buildIndex() {
        ReadAction.run<RuntimeException> {
            if (project.isDisposed) return@run
            val phelFiles = FilenameIndex.getAllFilesByExt(
                project, "phel", GlobalSearchScope.projectScope(project)
            )

            val psiManager = PsiManager.getInstance(project)

            for (virtualFile in phelFiles) {
                if (!virtualFile.isValid) continue
                val psiFile = psiManager.findFile(virtualFile) as? PhelFile ?: continue

                val symbols = PhelProjectSymbolScanner.scanFile(psiFile)

                if (symbols.isNotEmpty()) {
                    symbolsByFile[virtualFile.path] = symbols
                    for (symbol in symbols) {
                        addToMap(symbolsByNamespace, symbol.shortNamespace, symbol)
                        addToMap(symbolsByName, symbol.name, symbol)
                    }
                }
            }
        }
    }

    companion object {
        fun getInstance(project: Project): PhelProjectSymbolIndex {
            return project.getService(PhelProjectSymbolIndex::class.java)
        }
    }
}
