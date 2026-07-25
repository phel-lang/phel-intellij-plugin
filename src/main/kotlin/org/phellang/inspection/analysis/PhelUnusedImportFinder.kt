package org.phellang.inspection.analysis

import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelImportAnalysis
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.cachedPerPsi

/**
 * Decides whether a required namespace is never used in the file that requires it.
 *
 * Moved here from `annotator/validators/PhelImportValidator`, which still owns the two problems
 * that are genuinely annotator concerns (a duplicate import, and one that does not resolve). This
 * one became an inspection so it can be switched off or re-levelled from Settings; the annotator no
 * longer reports it, or every unused import would be flagged twice.
 */
internal object PhelUnusedImportFinder {

    private val USED_QUALIFIERS_KEY: Key<CachedValue<Set<String>>> =
        Key.create("phel.usedNamespaceQualifiers")

    fun isUnusedImport(namespaceSymbol: PhelSymbol): Boolean {
        val fullNamespace = namespaceSymbol.text ?: return false
        if (!PhelNamespaceUtils.looksLikeNamespace(fullNamespace)) return false

        val containingFile = namespaceSymbol.containingFile as? PhelFile ?: return false

        // `:refer` pulls names in unqualified, so no `ns/name` qualifier will ever appear for it.
        // Judging such an import by qualifier use would report every one of them as unused.
        if (hasReferClause(namespaceSymbol)) return false

        // A duplicate is trivially also unused, and the annotator already reports it as a duplicate.
        // Saying both on the same symbol is the noise this precedence rule exists to avoid.
        if (PhelImportAnalysis.isDuplicateImport(containingFile, namespaceSymbol, fullNamespace)) return false

        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)
        val aliasMap = PhelNamespaceUtils.extractAliasMap(containingFile)
        val alias = aliasMap.entries.find { it.value == shortNamespace }?.key

        // Under `:as`, uses are written with the alias, so that is the qualifier to look for.
        val qualifierToFind = alias ?: shortNamespace

        return qualifierToFind !in usedNamespaceQualifiers(containingFile)
    }

    /**
     * Qualifiers (`str` in `str/join`) used by namespace-qualified symbols outside the `(ns …)`
     * declaration. Every import in the file asks the same question, so the scan is cached per file
     * rather than re-run per import.
     */
    private fun usedNamespaceQualifiers(file: PhelFile): Set<String> =
        cachedPerPsi(file, USED_QUALIFIERS_KEY) { computeUsedNamespaceQualifiers(file) }

    private fun computeUsedNamespaceQualifiers(file: PhelFile): Set<String> {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file)
        val qualifiers = HashSet<String>()

        for (symbol in PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)) {
            // A qualifier inside the declaration is the import itself, not a use of it.
            if (nsDeclaration != null && PsiTreeUtil.isAncestor(nsDeclaration, symbol, false)) continue

            val text = symbol.text ?: continue
            if (text.contains("/")) {
                qualifiers.add(text.substringBefore("/"))
            }
        }

        return qualifiers
    }

    private fun hasReferClause(namespaceSymbol: PhelSymbol): Boolean {
        val requireForm = PsiTreeUtil.getParentOfType(namespaceSymbol, PhelList::class.java) ?: return false

        return PsiTreeUtil.findChildrenOfType(requireForm, PhelKeyword::class.java).any { it.text == ":refer" }
    }
}
