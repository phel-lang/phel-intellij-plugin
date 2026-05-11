package org.phellang.language.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.phellang.language.psi.files.PhelFile

object PhelProjectNamespaceFinder {

    // Canonical Phel 0.35+ form is dot-separated. The legacy backslash form is still
    // accepted on input via [normalize] but never emitted from this finder.
    val STANDARD_LIBRARY_SHORT_TO_FULL = mapOf(
        "string" to "phel.string",
        "json" to "phel.json",
        "http" to "phel.http",
        "html" to "phel.html",
        "base64" to "phel.base64",
        "test" to "phel.test",
        "mock" to "phel.mock",
        "repl" to "phel.repl",
        "debug" to "phel.debug",
        "core" to "phel.core",
    )

    val STANDARD_LIBRARY_NAMESPACES: Set<String> = STANDARD_LIBRARY_SHORT_TO_FULL.values.toSet()

    fun namespaceExists(project: Project, fullNamespace: String): Boolean {
        val normalized = PhelNamespaceUtils.normalizeNamespace(fullNamespace)
        if (normalized in STANDARD_LIBRARY_NAMESPACES) {
            return true
        }

        return findAllProjectNamespaces(project).any {
            PhelNamespaceUtils.normalizeNamespace(it) == normalized
        }
    }

    fun findByShortName(project: Project, shortNamespace: String): String? {
        return findAllProjectNamespaces(project).find {
            extractShortNamespace(it) == shortNamespace
        }
    }

    /**
     * Returns the trailing segment of a namespace regardless of whether the segments
     * are split with the canonical `.` (Phel 0.35+) or the legacy `\`.
     */
    fun extractShortNamespace(fullNamespace: String): String {
        val lastDot = fullNamespace.lastIndexOf('.')
        val lastBackslash = fullNamespace.lastIndexOf('\\')
        val cut = maxOf(lastDot, lastBackslash)
        return if (cut < 0) fullNamespace else fullNamespace.substring(cut + 1)
    }

    fun isStandardLibrary(fullNamespace: String): Boolean {
        return PhelNamespaceUtils.normalizeNamespace(fullNamespace) in STANDARD_LIBRARY_NAMESPACES
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
            val fileNamespace = PhelNamespaceUtils.extractNamespaceFromFile(psiFile) ?: continue
            namespaces.add(fileNamespace)
        }

        return namespaces
    }
}
