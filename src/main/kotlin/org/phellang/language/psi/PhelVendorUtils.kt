package org.phellang.language.psi

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

object PhelVendorUtils {

    private const val PHEL_VENDOR_PATH = "phel-lang/phel-lang/src/phel"

    fun findVendorFolder(project: Project): VirtualFile? {
        val composerFiles =
            FilenameIndex.getVirtualFilesByName("composer.json", GlobalSearchScope.projectScope(project))

        for (composerFile in composerFiles) {
            val projectRoot = composerFile.parent ?: continue
            val vendorFolder = projectRoot.findChild("vendor")
            if (vendorFolder != null && vendorFolder.isDirectory) {
                return vendorFolder
            }
        }

        val basePath = project.basePath ?: return null
        val projectRoot = LocalFileSystem.getInstance().findFileByPath(basePath)
        return projectRoot?.findChild("vendor")
    }

    fun findPhelLibraryFolder(project: Project): VirtualFile? {
        val vendorFolder = findVendorFolder(project) ?: return null
        val phelFolder = vendorFolder.findFileByRelativePath(PHEL_VENDOR_PATH)
        return if (phelFolder != null && phelFolder.isDirectory) phelFolder else null
    }

    /**
     * Returns every vendor `.phel` file that backs the given namespace.
     *
     * Phel 0.35+ splits `phel.core` into many bucket files under `core/`
     * (e.g. `core/arrays.phel`, `core/booleans.phel`). Sub-namespaces like
     * `schema.coercer` map to `schema/coercer.phel`.
     */
    fun findStandardLibraryFiles(project: Project, namespace: String): List<VirtualFile> {
        val phelFolder = findPhelLibraryFolder(project) ?: return emptyList()
        val normalized = namespace.replace('\\', '.').removePrefix("phel.")

        val results = mutableListOf<VirtualFile>()

        if (normalized.contains('.')) {
            // Sub-namespace: schema.coercer -> schema/coercer.phel
            val relPath = normalized.replace('.', '/') + ".phel"
            phelFolder.findFileByRelativePath(relPath)?.let { results.add(it) }
            return results
        }

        // Root file (e.g. core.phel, string.phel)
        phelFolder.findChild("$normalized.phel")?.let { results.add(it) }

        // Bucket files under <namespace>/ (core/* in Phel 0.35+, future-proof for other splits)
        phelFolder.findChild(normalized)?.takeIf { it.isDirectory }?.children
            ?.filter { !it.isDirectory && it.name.endsWith(".phel") }
            ?.forEach { results.add(it) }

        return results
    }

    /** Legacy single-file lookup. Prefer [findStandardLibraryFiles]. */
    fun findStandardLibraryFile(project: Project, namespace: String): VirtualFile? =
        findStandardLibraryFiles(project, namespace).firstOrNull()
}
