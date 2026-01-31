package org.phellang.language.psi

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

object PhelVendorUtils {

    private const val PHEL_VENDOR_PATH = "phel-lang/phel-lang/src/phel"

    private val NAMESPACE_TO_FILE = mapOf(
        "core" to "core.phel",
        "str" to "str.phel",
        "json" to "json.phel",
        "http" to "http.phel",
        "html" to "html.phel",
        "base64" to "base64.phel",
        "test" to "test.phel",
        "mock" to "mock.phel",
        "repl" to "repl.phel",
        "debug" to "debug.phel",
    )

    fun findVendorFolder(project: Project): VirtualFile? {
        // First try: Look for composer.json
        val composerFiles =
            FilenameIndex.getVirtualFilesByName("composer.json", GlobalSearchScope.projectScope(project))

        for (composerFile in composerFiles) {
            val projectRoot = composerFile.parent ?: continue
            val vendorFolder = projectRoot.findChild("vendor")
            if (vendorFolder != null && vendorFolder.isDirectory) {
                return vendorFolder
            }
        }

        // Second try: Check project base path
        val basePath = project.basePath ?: return null
        val projectRoot = LocalFileSystem.getInstance().findFileByPath(basePath)
        return projectRoot?.findChild("vendor")
    }

    fun findPhelLibraryFolder(project: Project): VirtualFile? {
        val vendorFolder = findVendorFolder(project) ?: return null
        val phelFolder = vendorFolder.findFileByRelativePath(PHEL_VENDOR_PATH)
        return if (phelFolder != null && phelFolder.isDirectory) phelFolder else null
    }

    fun findStandardLibraryFile(project: Project, namespace: String): VirtualFile? {
        val phelFolder = findPhelLibraryFolder(project) ?: return null
        val fileName = NAMESPACE_TO_FILE[namespace] ?: return null
        return phelFolder.findChild(fileName)
    }
}
