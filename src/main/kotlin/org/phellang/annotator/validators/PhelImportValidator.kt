package org.phellang.annotator.validators

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.annotator.quickfixes.PhelFixImportQuickFix
import org.phellang.annotator.quickfixes.PhelRemoveImportQuickFix
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Validates (:require ...) statements to ensure imported namespaces exist.
 */
object PhelImportValidator {
    private val USED_QUALIFIERS_KEY: Key<CachedValue<Set<String>>> =
        Key.create("phel.usedNamespaceQualifiers")

    fun validateImport(namespaceSymbol: PhelSymbol): List<PhelValidationProblem> {
        val fullNamespace = namespaceSymbol.text ?: return emptyList()

        // Must look like a namespace: dot-separated (Phel 0.35+ canonical) or legacy backslash.
        if (!looksLikeNamespace(fullNamespace)) {
            return emptyList()
        }

        val project = namespaceSymbol.project
        val containingFile = namespaceSymbol.containingFile as? PhelFile

        // A duplicate is reported on its own: it is trivially also "unused", and saying so twice
        // on the same symbol is noise.
        if (containingFile != null && isDuplicateImport(containingFile, namespaceSymbol, fullNamespace)) {
            return listOf(
                PhelValidationProblem(
                    message = "Duplicate import: '$fullNamespace' is already imported",
                    quickFix = PhelRemoveImportQuickFix(namespaceSymbol, "Remove duplicate import"),
                )
            )
        }

        return buildList {
            missingNamespaceProblem(project, namespaceSymbol, fullNamespace)?.let(::add)
            unusedImportProblem(namespaceSymbol)?.let(::add)
        }
    }

    /** Null when the namespace resolves — to the standard library or to a project file. */
    private fun missingNamespaceProblem(
        project: Project, namespaceSymbol: PhelSymbol, fullNamespace: String
    ): PhelValidationProblem? {
        if (PhelProjectNamespaceFinder.isStandardLibrary(fullNamespace)) return null
        if (PhelProjectNamespaceFinder.namespaceExists(project, fullNamespace)) return null

        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)
        val suggestion = PhelProjectNamespaceFinder.findByShortName(project, shortNamespace)

        return if (suggestion != null && suggestion != fullNamespace) {
            PhelValidationProblem(
                message = "Namespace '$fullNamespace' does not exist. Did you mean '$suggestion'?",
                quickFix = PhelFixImportQuickFix(namespaceSymbol, suggestion),
            )
        } else {
            PhelValidationProblem("Namespace '$fullNamespace' does not exist")
        }
    }

    private fun unusedImportProblem(namespaceSymbol: PhelSymbol): PhelValidationProblem? {
        if (!isUnusedImport(namespaceSymbol)) return null

        return PhelValidationProblem(
            message = "Unused import",
            quickFix = PhelRemoveImportQuickFix(namespaceSymbol, "Remove unused import"),
            severity = PhelValidationProblem.Severity.WEAK_WARNING,
        )
    }

    private fun looksLikeNamespace(text: String): Boolean {
        return text.contains('\\') || text.contains('.')
    }

    private fun isDuplicateImport(file: PhelFile, currentSymbol: PhelSymbol, namespace: String): Boolean {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false
        val requireForms = PhelNamespaceUtils.findRequireForms(nsDeclaration)
        val normalizedTarget = PhelNamespaceUtils.normalizeNamespace(namespace)

        var foundFirst = false

        for (requireForm in requireForms) {
            val symbols = PsiTreeUtil.findChildrenOfType(requireForm, PhelSymbol::class.java)

            for (symbol in symbols) {
                val symbolText = symbol.text ?: continue
                if (!looksLikeNamespace(symbolText)) continue

                if (PhelNamespaceUtils.normalizeNamespace(symbolText) == normalizedTarget) {
                    if (symbol === currentSymbol) {
                        if (foundFirst) {
                            return true
                        }
                    } else {
                        if (!foundFirst) {
                            foundFirst = true
                        } else if (symbol.textOffset < currentSymbol.textOffset) {
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    fun isUnusedImport(namespaceSymbol: PhelSymbol): Boolean {
        val fullNamespace = namespaceSymbol.text ?: return false
        if (!looksLikeNamespace(fullNamespace)) return false

        val containingFile = namespaceSymbol.containingFile as? PhelFile ?: return false

        // Check if this import uses :refer - if so, it's not "unused" in the traditional sense
        if (hasReferClause(namespaceSymbol)) {
            return false
        }

        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)

        val aliasMap = PhelNamespaceUtils.extractAliasMap(containingFile)
        val alias = aliasMap.entries.find { it.value == shortNamespace }?.key

        // The qualifier to look for is either the alias or the short namespace
        val qualifierToFind = alias ?: shortNamespace

        // Unused when no namespace-qualified symbol in the file body uses this qualifier.
        return qualifierToFind !in usedNamespaceQualifiers(containingFile)
    }

    /**
     * Qualifiers (`str` in `str/join`) used by namespace-qualified symbols outside the
     * `(ns ...)` declaration. Highlighting checks each import against this set, so the
     * per-file scan is cached and shared across all imports rather than re-run per import.
     */
    private fun usedNamespaceQualifiers(file: PhelFile): Set<String> {
        return CachedValuesManager.getCachedValue(file, USED_QUALIFIERS_KEY) {
            CachedValueProvider.Result.create(
                computeUsedNamespaceQualifiers(file),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    private fun computeUsedNamespaceQualifiers(file: PhelFile): Set<String> {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file)
        val qualifiers = HashSet<String>()
        for (symbol in PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)) {
            // Skip symbols inside the namespace declaration itself.
            if (nsDeclaration != null && PsiTreeUtil.isAncestor(nsDeclaration, symbol, false)) {
                continue
            }
            val text = symbol.text ?: continue
            if (text.contains("/")) {
                qualifiers.add(text.substringBefore("/"))
            }
        }
        return qualifiers
    }

    private fun hasReferClause(namespaceSymbol: PhelSymbol): Boolean {
        val requireForm = PsiTreeUtil.getParentOfType(namespaceSymbol, PhelList::class.java) ?: return false

        val keywords = PsiTreeUtil.findChildrenOfType(requireForm, PhelKeyword::class.java)
        return keywords.any { it.text == ":refer" }
    }
}
