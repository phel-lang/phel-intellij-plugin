package org.phellang.actions.namespace

import com.intellij.psi.PsiDirectory

class PhelNamespaceGenerator {

    private companion object {
        val SKIP_COMMON_ROOT_DIRECTORIES = listOf("src", "main", "resources", "phel", "public", "root", "tests")
    }

    fun generateNamespace(dir: PsiDirectory, fileName: String): String {
        val project = dir.project
        val projectBasePath = project.basePath ?: return getFileNameWithoutExtension(fileName)

        val pathParts = extractPathParts(dir, projectBasePath)
        val nameWithoutExt = getFileNameWithoutExtension(fileName)
        pathParts.add(nameWithoutExt)

        return buildNamespace(pathParts)
    }

    private fun extractPathParts(dir: PsiDirectory, projectBasePath: String): MutableList<String> {
        val currentPath = dir.virtualFile.path
        val relativePath = if (currentPath.startsWith(projectBasePath)) {
            currentPath.substring(projectBasePath.length).trimStart('/')
        } else {
            dir.name
        }

        val pathParts = relativePath.split('/').filter { it.isNotEmpty() }.toMutableList()
        return filterCommonRootDirectories(pathParts)
    }

    private fun filterCommonRootDirectories(pathParts: MutableList<String>): MutableList<String> {
        while (pathParts.isNotEmpty() && pathParts[0] in SKIP_COMMON_ROOT_DIRECTORIES) {
            pathParts.removeAt(0)
        }
        return pathParts
    }

    private fun buildNamespace(pathParts: List<String>): String {
        return pathParts.joinToString("\\")
    }

    private fun getFileNameWithoutExtension(fileName: String): String {
        return fileName.substringBeforeLast('.')
    }
}
