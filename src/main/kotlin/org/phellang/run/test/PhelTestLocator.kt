package org.phellang.run.test

import com.intellij.execution.Location
import com.intellij.execution.PsiLocation
import com.intellij.execution.testframework.sm.runner.SMTestLocator
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Turns a `locationHint` from the test tree back into the `deftest` it came from.
 *
 * Without this a test node navigates nowhere: double-click does nothing, a failure cannot be jumped
 * to, and the run gutter shows no state from the last run. The report itself carries no file or
 * line — only a suite name and a test name — so the location is *resolved* rather than read, using
 * the same two facts the producer already relies on: a suite is a Phel namespace, and
 * [PhelTestDetection] knows a `deftest` when it sees one.
 *
 * `deftest` is deliberately absent from [org.phellang.indexing.PhelProjectSymbolIndex] — it names a
 * test entry point no other namespace calls — so the lookup walks `.phel` files rather than asking
 * the index.
 */
object PhelTestLocator : SMTestLocator {

    /** The scheme in `locationHint='phel://<namespace>/<test>'`. */
    const val PROTOCOL = "phel"

    override fun getLocation(
        protocol: String,
        path: String,
        project: Project,
        scope: GlobalSearchScope,
    ): List<Location<*>> {
        if (protocol != PROTOCOL) return emptyList()

        val target = resolve(project, scope, path) ?: return emptyList()

        return listOf(PsiLocation.fromPsiElement(target))
    }

    /** `<namespace>` alone locates the file; `<namespace>/<test>` locates the `deftest` inside it. */
    private fun resolve(project: Project, scope: GlobalSearchScope, path: String): PsiElement? {
        // Last separator, not first: a Phel namespace holds backslashes, never a slash.
        val namespace = path.substringBeforeLast('/', missingDelimiterValue = path)
        val testName = path.substringAfterLast('/', missingDelimiterValue = "")

        val file = findTestFile(project, scope, namespace) ?: return null
        if (testName.isEmpty()) return file

        return findDeftest(file, testName)
    }

    private fun findTestFile(project: Project, scope: GlobalSearchScope, namespace: String): PhelFile? {
        val wanted = PhelNamespaceUtils.normalizeNamespace(namespace)
        val psiManager = PsiManager.getInstance(project)

        return FilenameIndex.getAllFilesByExt(project, PHEL_EXTENSION, scope)
            .asSequence()
            .filter { it.isValid }
            .mapNotNull { psiManager.findFile(it) as? PhelFile }
            .firstOrNull { declaredNamespaceOf(it) == wanted }
    }

    private fun declaredNamespaceOf(file: PhelFile): String? =
        PhelNamespaceUtils.extractNamespaceFromFile(file)?.let(PhelNamespaceUtils::normalizeNamespace)

    /**
     * The name symbol rather than the whole form, so navigation lands on the test's name the way
     * go-to-definition does.
     */
    private fun findDeftest(file: PhelFile, testName: String): PsiElement? =
        file.children
            .filterIsInstance<PhelList>()
            .firstOrNull { PhelTestDetection.deftestName(it) == testName }
            ?.let { PhelPsiUtils.asSymbol(PhelPsiUtils.activeForms(it).getOrNull(1)) }

    private const val PHEL_EXTENSION = "phel"
}
