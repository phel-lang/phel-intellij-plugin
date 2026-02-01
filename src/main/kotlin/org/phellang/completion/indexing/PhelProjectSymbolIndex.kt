package org.phellang.completion.indexing

import com.intellij.openapi.Disposable
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
        symbolsByFile.clear()
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
        val psiManager = PsiManager.getInstance(project)
        val psiFile = psiManager.findFile(file) as? PhelFile ?: return
        refreshFileFromPsi(psiFile)
    }

    /**
     * Refreshes the index from the current PSI state (used for live updates during editing).
     */
    fun refreshFileFromPsi(psiFile: PhelFile) {
        val virtualFile = psiFile.virtualFile ?: return
        val filePath = virtualFile.path

        val oldSymbols = symbolsByFile[filePath] ?: emptyList()

        // Remove old symbols from namespace index
        for (symbol in oldSymbols) {
            val namespaceSymbols = symbolsByNamespace[symbol.shortNamespace]?.toMutableList()
            namespaceSymbols?.removeIf { it.file.path == filePath }
            if (namespaceSymbols != null) {
                symbolsByNamespace[symbol.shortNamespace] = namespaceSymbols
            }
        }

        // Scan file for new symbols from the current PSI state
        val newSymbols = PhelProjectSymbolScanner.scanFile(psiFile)
        symbolsByFile[filePath] = newSymbols

        // Add new symbols to namespace index
        for (symbol in newSymbols) {
            val existingSymbols = symbolsByNamespace[symbol.shortNamespace]?.toMutableList() ?: mutableListOf()
            existingSymbols.add(symbol)
            symbolsByNamespace[symbol.shortNamespace] = existingSymbols
        }
    }

    fun removeFile(file: VirtualFile) {
        val oldSymbols = symbolsByFile.remove(file.path) ?: return

        // Remove symbols from namespace index
        for (symbol in oldSymbols) {
            val namespaceSymbols = symbolsByNamespace[symbol.shortNamespace]?.toMutableList()
            namespaceSymbols?.removeIf { it.file.path == file.path }
            if (namespaceSymbols != null) {
                if (namespaceSymbols.isEmpty()) {
                    symbolsByNamespace.remove(symbol.shortNamespace)
                } else {
                    symbolsByNamespace[symbol.shortNamespace] = namespaceSymbols
                }
            }
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
        val phelFiles = FilenameIndex.getAllFilesByExt(
            project, "phel", GlobalSearchScope.projectScope(project)
        )

        val psiManager = PsiManager.getInstance(project)

        for (virtualFile in phelFiles) {
            val psiFile = psiManager.findFile(virtualFile) as? PhelFile ?: continue

            val symbols = PhelProjectSymbolScanner.scanFile(psiFile)

            if (symbols.isNotEmpty()) {
                symbolsByFile[virtualFile.path] = symbols

                // Group by namespace
                for (symbol in symbols) {
                    val existingSymbols = symbolsByNamespace[symbol.shortNamespace]?.toMutableList() ?: mutableListOf()
                    existingSymbols.add(symbol)
                    symbolsByNamespace[symbol.shortNamespace] = existingSymbols
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
