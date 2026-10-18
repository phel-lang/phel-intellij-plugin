package org.phellang.language.psi

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager

object PhelVendorUtils {

    private const val PHEL_VENDOR_PATH = "phel-lang/phel-lang/src/phel"

    private val PHEL_LIBRARY_FOLDER_KEY: Key<CachedValue<VirtualFile?>> = Key.create("phel.vendor.libraryFolder")

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

    /**
     * The `phel-lang/phel-lang/src/phel` folder under the project's `vendor/`, or null.
     *
     * Cached per project. Resolving any standard-library name asks for it, so uncached it cost a
     * `composer.json` index query for every symbol resolved. Only a file or folder being created,
     * deleted, moved or renamed, or a change to the project's roots, can change the answer, so those
     * are the dependencies; edits to file contents leave it alone.
     */
    fun findPhelLibraryFolder(project: Project): VirtualFile? =
        CachedValuesManager.getManager(project).getCachedValue(project, PHEL_LIBRARY_FOLDER_KEY, {
            CachedValueProvider.Result.create(
                computePhelLibraryFolder(project),
                VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS,
                ProjectRootModificationTracker.getInstance(project),
                // Its `composer.json` lookup can run during indexing; leaving dumb mode recomputes it.
                DumbService.getInstance(project).modificationTracker,
            )
        }, false)?.takeIf { it.isValid }

    private fun computePhelLibraryFolder(project: Project): VirtualFile? {
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
}
