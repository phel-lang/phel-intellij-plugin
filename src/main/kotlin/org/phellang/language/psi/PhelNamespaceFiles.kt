package org.phellang.language.psi

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.jetbrains.annotations.TestOnly
import org.phellang.language.infrastructure.PhelLanguage
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.utils.cachedPerPsi
import java.util.Optional

/**
 * Which files make up each namespace, and which files require it.
 *
 * Cross-file resolution follows the Phel compiler, which only ever looks into a known namespace: the
 * current one, a `:refer`'d one, or the one a qualifier names (`SymbolResolver::resolveWithoutAlias`).
 * So it needs "the files of namespace N", never a sweep over every project file. One project-wide
 * snapshot answers that, built from each file's cached namespace and `(:require ...)` clauses.
 *
 * The snapshot is rebuilt only when a Phel file's PSI changes or a file is created, deleted, moved or
 * renamed. Editing any other kind of file leaves it alone. It holds [VirtualFile]s rather than PSI,
 * and every lookup maps back to live PSI.
 *
 * It does not use `PhelProjectSymbolIndex`: that index holds public top-level definitions only, is
 * keyed by short namespace, and refreshes asynchronously; and `indexing` sits above `language`.
 */
internal object PhelNamespaceFiles {

    private val DECLARED_NAMESPACE_KEY: Key<CachedValue<Optional<String>>> = Key.create("phel.declaredNamespace")
    private val SNAPSHOT_KEY: Key<CachedValue<Snapshot>> = Key.create("phel.namespaceFiles")

    private const val NS = "ns"
    private const val IN_NS = "in-ns"

    /**
     * The namespace [file] belongs to, normalized to dots: the name in its first top-level `(ns X)` or
     * `(in-ns X)`, else null.
     *
     * `in-ns` is how one namespace spans several files (`phel.core` opens each bucket file under `core/` with
     * `(in-ns phel.core)`), so such a file joins the namespace it names.
     */
    fun declaredNamespace(file: PhelFile): String? =
        cachedPerPsi(file, DECLARED_NAMESPACE_KEY) { Optional.ofNullable(computeDeclaredNamespace(file)) }.orElse(null)

    /**
     * The files of [namespace]: every project file that declares it, then, for a standard-library
     * namespace, its files under `vendor/`. Those are found by path, so a project that excludes
     * `vendor/` still has them; a file both routes reach is listed once.
     */
    fun filesOf(project: Project, namespace: String): List<PhelFile> {
        val normalized = PhelNamespaceUtils.normalizeNamespace(namespace)
        val inProject = toPhelFiles(project, snapshot(project).filesByNamespace[normalized].orEmpty())

        // A stdlib folder holds sub-namespaces too (`schema/coercer.phel` is `phel.schema.coercer`), so a
        // vendor file counts only when it declares this namespace; `core/` buckets do, with `in-ns`.
        val inVendor = if (PhelProjectNamespaceFinder.isStandardLibrary(normalized)) {
            toPhelFiles(project, PhelVendorUtils.findStandardLibraryFiles(project, normalized))
                .filter { declaredNamespace(it) == normalized }
        } else {
            emptyList()
        }

        return (inProject + inVendor).distinct()
    }

    /** The files whose namespace ends in [shortNamespace], for a qualifier the file never required. */
    fun filesWithShortNamespace(project: Project, shortNamespace: String): List<PhelFile> {
        val files = snapshot(project).filesByNamespace
            .filterKeys { PhelProjectNamespaceFinder.extractShortNamespace(it) == shortNamespace }
            .values
            .flatten()

        return toPhelFiles(project, files)
    }

    /** The files whose `(:require ...)` imports [namespace], with or without `:as` or `:refer`. */
    fun importersOf(project: Project, namespace: String): List<PhelFile> {
        val normalized = PhelNamespaceUtils.normalizeNamespace(namespace)
        return toPhelFiles(project, snapshot(project).importersByNamespace[normalized].orEmpty())
    }

    /** The current snapshot, so a test can tell whether an edit rebuilt it. */
    @TestOnly
    fun snapshotForTest(project: Project): Any = snapshot(project)

    private class Snapshot(
        val filesByNamespace: Map<String, List<VirtualFile>>,
        val importersByNamespace: Map<String, List<VirtualFile>>,
    )

    private fun snapshot(project: Project): Snapshot =
        CachedValuesManager.getManager(project).getCachedValue(project, SNAPSHOT_KEY, {
            CachedValueProvider.Result.create(
                buildSnapshot(project),
                PsiModificationTracker.getInstance(project).forLanguage(PhelLanguage),
                VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS,
                ProjectRootModificationTracker.getInstance(project),
                // The file name index answers from the VFS during indexing instead of throwing, so a
                // snapshot built then may be partial; entering or leaving dumb mode rebuilds it.
                DumbService.getInstance(project).modificationTracker,
            )
        }, false)

    private fun buildSnapshot(project: Project): Snapshot {
        val psiManager = PsiManager.getInstance(project)
        val filesByNamespace = LinkedHashMap<String, MutableList<VirtualFile>>()
        val importersByNamespace = LinkedHashMap<String, MutableList<VirtualFile>>()

        for (virtualFile in FilenameIndex.getAllFilesByExt(project, "phel", GlobalSearchScope.projectScope(project))) {
            ProgressManager.checkCanceled()
            val file = psiManager.findFile(virtualFile) as? PhelFile ?: continue

            declaredNamespace(file)?.let { filesByNamespace.getOrPut(it) { mutableListOf() } += virtualFile }

            PhelNamespaceUtils.extractImports(file)
                .map { PhelNamespaceUtils.normalizeNamespace(it.fullNamespace) }
                .distinct()
                .forEach { importersByNamespace.getOrPut(it) { mutableListOf() } += virtualFile }
        }

        return Snapshot(filesByNamespace, importersByNamespace)
    }

    private fun toPhelFiles(project: Project, files: List<VirtualFile>): List<PhelFile> {
        val psiManager = PsiManager.getInstance(project)
        return files.mapNotNull { if (it.isValid) psiManager.findFile(it) as? PhelFile else null }
    }

    /**
     * The name in the first top-level `(ns X)` or `(in-ns X)`. Top-level and not `#_`-discarded, as the
     * compiler reads it: a `(ns ~n)` in a macro template or a discarded `#_(ns old)` is not this file's.
     */
    private fun computeDeclaredNamespace(file: PhelFile): String? {
        val declared = PhelPsiUtils.activeForms(file)
            .filterIsInstance<PhelList>()
            .firstNotNullOfOrNull(::namespaceDeclaredBy)
            ?: return null

        return PhelNamespaceUtils.normalizeNamespace(declared)
    }

    /**
     * `X` for `(ns X ...)`, `(in-ns X)` or `(in-ns "X")`, else null. A name that is a list, vector,
     * map or keyword is no name: `(ns (:require a.b))`, typed before the name, declares nothing.
     */
    private fun namespaceDeclaredBy(list: PhelList): String? {
        val forms = PhelPsiUtils.activeForms(list)
        val head = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text
        if (head != NS && head != IN_NS) return null

        val nameForm = forms.getOrNull(1) ?: return null
        if (head == IN_NS) stringLiteralOf(nameForm)?.let { return it }
        if (nameForm is PhelList || nameForm is PhelVec || nameForm is PhelMap || nameForm is PhelKeyword) return null

        return PhelPsiUtils.asSymbol(nameForm)?.text
    }

    /** The contents of a double-quoted string literal, otherwise null. */
    private fun stringLiteralOf(form: PhelForm): String? {
        val text = (form as? PhelLiteral)?.text ?: return null
        if (text.length < 2 || !text.startsWith("\"") || !text.endsWith("\"")) return null

        return text.substring(1, text.length - 1)
    }
}
