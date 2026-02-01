package org.phellang.language.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.phellang.completion.indexing.PhelProjectSymbolScanner
import org.phellang.language.psi.files.PhelFile

object PhelProjectNamespaceFinder {

    val STANDARD_LIBRARY_SHORT_TO_FULL = mapOf(
        "str" to "phel\\str",
        "json" to "phel\\json",
        "http" to "phel\\http",
        "html" to "phel\\html",
        "base64" to "phel\\base64",
        "test" to "phel\\test",
        "mock" to "phel\\mock",
        "repl" to "phel\\repl",
        "debug" to "phel\\debug",
        "core" to "phel\\core",
    )

    val STANDARD_LIBRARY_NAMESPACES: Set<String> = STANDARD_LIBRARY_SHORT_TO_FULL.values.toSet()

    fun namespaceExists(project: Project, fullNamespace: String): Boolean {
        // Check standard library first
        if (fullNamespace in STANDARD_LIBRARY_NAMESPACES) {
            return true
        }
        
        // Search project files
        return findAllProjectNamespaces(project).any { it == fullNamespace }
    }

    fun findByShortName(project: Project, shortNamespace: String): String? {
        return findAllProjectNamespaces(project).find { 
            extractShortNamespace(it) == shortNamespace 
        }
    }

    fun extractShortNamespace(fullNamespace: String): String {
        return fullNamespace.substringAfterLast("\\")
    }

    fun isStandardLibrary(fullNamespace: String): Boolean {
        return fullNamespace in STANDARD_LIBRARY_NAMESPACES
    }

    fun getStandardLibraryFullNamespace(shortName: String): String? {
        return STANDARD_LIBRARY_SHORT_TO_FULL[shortName.lowercase()]
    }

    fun findAllProjectNamespaces(project: Project): List<String> {
        val namespaces = mutableListOf<String>()
        
        val phelFiles = FilenameIndex.getAllFilesByExt(
            project, "phel", GlobalSearchScope.projectScope(project)
        )
        val psiManager = PsiManager.getInstance(project)

        for (virtualFile in phelFiles) {
            val psiFile = psiManager.findFile(virtualFile) as? PhelFile ?: continue
            val fileNamespace = PhelProjectSymbolScanner.extractNamespace(psiFile) ?: continue
            namespaces.add(fileNamespace)
        }

        return namespaces
    }
}
