package org.phellang.annotator.validators

import com.intellij.openapi.project.Project
import org.phellang.annotator.quickfixes.PhelImportNamespaceQuickFix
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelRequireClauseAnalyzer
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/** Checks that the namespace qualifying a `ns/name` symbol is both imported and real. */
object PhelNamespaceValidator {

    /** `php/` is interop and `core/` needs no import, so neither is ever missing. */
    private val EXEMPT_QUALIFIERS = setOf("php", "core")

    fun validateNamespace(symbol: PhelSymbol): List<PhelValidationProblem> {
        val text = symbol.text ?: return emptyList()
        if (!text.contains("/")) return emptyList()

        val qualifier = PhelPsiUtils.getQualifier(symbol) ?: return emptyList()
        if (qualifier in EXEMPT_QUALIFIERS) return emptyList()

        val file = symbol.containingFile as? PhelFile ?: return emptyList()
        if (isPhpClassInterop(text, file)) return emptyList()

        return when (importStatus(file, qualifier)) {
            ImportStatus.VALID -> emptyList()
            ImportStatus.IMPORTED_BUT_NOT_EXISTS -> reportMissingImportTarget(symbol, qualifier)
            ImportStatus.NOT_IMPORTED -> reportNotImported(symbol, qualifier)
        }
    }

    /**
     * `DateTime/createFromFormat` and `\Foo\Bar/CONST` look namespaced, but the qualifier is a PHP
     * class rather than a Phel namespace, so the import lookup would always fail.
     */
    private fun isPhpClassInterop(text: String, file: PhelFile): Boolean =
        PhelInteropShorthands.isInteropShorthand(text, PhelNamespaceUtils.extractUsedClasses(file))

    /**
     * The namespace is imported but nothing resolves it. A fix is only offered when a project file
     * with a matching short namespace exists — otherwise there is nothing to point the import at.
     */
    private fun reportMissingImportTarget(symbol: PhelSymbol, qualifier: String): List<PhelValidationProblem> {
        val suggestion = PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)
            ?: return listOf(PhelValidationProblem("Imported namespace does not exist"))

        return listOf(
            PhelValidationProblem(
                message = "Imported namespace does not exist. Did you mean '$suggestion'?",
                quickFix = PhelImportNamespaceQuickFix(suggestion),
            )
        )
    }

    /** Not imported. If the namespace resolves anyway, offer to add the import; otherwise it is a typo. */
    private fun reportNotImported(symbol: PhelSymbol, qualifier: String): List<PhelValidationProblem> {
        val importable = PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(qualifier)
            ?: PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)
            ?: qualifier.takeIf { isFullNamespace(symbol.project, it) }
            ?: return listOf(PhelValidationProblem("Namespace '$qualifier' does not exist"))

        return listOf(
            PhelValidationProblem(
                message = "Namespace '$qualifier' is not imported",
                quickFix = PhelImportNamespaceQuickFix(importable),
            )
        )
    }

    private enum class ImportStatus { VALID, IMPORTED_BUT_NOT_EXISTS, NOT_IMPORTED }

    private fun importStatus(file: PhelFile, qualifier: String): ImportStatus {
        val imports = PhelRequireClauseAnalyzer.imports(file)

        // An `:as` alias takes priority; otherwise match the import's short namespace, or its full name:
        // a qualifier that is no alias names the namespace itself (`SymbolResolver::resolveWithAlias`).
        val import = imports.firstOrNull { it.alias == qualifier }
            ?: imports.firstOrNull { it.shortNamespace == qualifier }
            ?: imports.firstOrNull { sameNamespace(it.fullNamespace, qualifier) }
            ?: return ImportStatus.NOT_IMPORTED

        return if (namespaceExists(file.project, import.fullNamespace)) {
            ImportStatus.VALID
        } else {
            ImportStatus.IMPORTED_BUT_NOT_EXISTS
        }
    }

    private fun sameNamespace(namespace: String, qualifier: String): Boolean =
        PhelNamespaceUtils.normalizeNamespace(namespace) == PhelNamespaceUtils.normalizeNamespace(qualifier)

    /** `app.util/helper` with no require: the full name of a namespace that exists, so it can be required. */
    private fun isFullNamespace(project: Project, qualifier: String): Boolean =
        PhelNamespaceUtils.looksLikeNamespace(qualifier) && namespaceExists(project, qualifier)

    private fun namespaceExists(project: Project, fullNamespace: String): Boolean =
        PhelProjectNamespaceFinder.isStandardLibrary(fullNamespace) ||
                PhelProjectNamespaceFinder.namespaceExists(project, fullNamespace)
}
