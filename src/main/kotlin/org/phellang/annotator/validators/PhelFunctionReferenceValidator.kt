package org.phellang.annotator.validators

import org.phellang.registry.PhelFunctionRegistry
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

object PhelFunctionReferenceValidator {
    fun validateFunctionReference(symbol: PhelSymbol): List<PhelValidationProblem> {
        val text = symbol.text ?: return emptyList()
        if (!text.contains("/")) return emptyList()

        // php/ interop resolves to PHP, whose functions this validator cannot check.
        val qualifier = PhelPsiUtils.getQualifier(symbol)?.takeIf { it != "php" } ?: return emptyList()
        val functionName = PhelPsiUtils.getName(symbol) ?: return emptyList()

        val file = symbol.containingFile as? PhelFile ?: return emptyList()
        if (isPhpClassInterop(text, file)) return emptyList()

        val resolvedNamespace = PhelNamespaceUtils.extractAliasMap(file)[qualifier] ?: qualifier
        if (resolves(file, qualifier, resolvedNamespace, functionName)) return emptyList()

        // An unknown namespace is the namespace validator's problem, not this one's.
        if (!isNamespaceKnown(file, symbol, qualifier, resolvedNamespace)) return emptyList()

        // The namespace is real, so the function name itself is wrong — and only the user can say
        // what they meant, which is why no fix is offered.
        return listOf(PhelValidationProblem("Cannot resolve function '$functionName' in namespace '$qualifier'"))
    }

    /**
     * `DateTime/createFromFormat` and `\Foo\Bar/CONST` look namespaced, but the qualifier is a PHP
     * class rather than a Phel namespace, so the regular function lookup cannot validate them.
     */
    private fun isPhpClassInterop(text: String, file: PhelFile): Boolean =
        PhelInteropShorthands.isInteropShorthand(text, PhelNamespaceUtils.extractUsedClasses(file))

    /**
     * [resolvedNamespace] is the alias map's answer, a *short* namespace, which is how the registry is
     * keyed. So the registry is asked only when the qualifier does not name a required project
     * namespace: `app.string :as s` has no `s/split` just because `phel.string` does.
     */
    private fun resolves(file: PhelFile, qualifier: String, resolvedNamespace: String, functionName: String): Boolean {
        val imported = PhelNamespaceUtils.importedNamespaceFor(file, qualifier)
        val mayBeStandardLibrary = imported == null || PhelProjectNamespaceFinder.isStandardLibrary(imported)
        // A stdlib namespace in full, required or as written, is filed under its registry name:
        // `phel.string/split` is `string/split`, and `phel.schema.coercer :as c` is `schema.coercer`.
        val registryNamespace = PhelProjectNamespaceFinder.registryNamespaceOf(imported ?: qualifier) ?: resolvedNamespace

        return (mayBeStandardLibrary && existsInStandardLibrary(registryNamespace, functionName)) ||
                existsInProjectSymbols(file, qualifier, functionName)
    }

    private fun existsInStandardLibrary(namespace: String, functionName: String): Boolean {
        val canonicalName = "$namespace/$functionName"
        if (PhelFunctionRegistry.getFunction(canonicalName) != null) {
            return true
        }

        val fullNamespace = PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(namespace)
        if (fullNamespace != null) {
            val fullName = "$fullNamespace/$functionName"
            if (PhelFunctionRegistry.getFunction(fullName) != null) {
                return true
            }
        }

        return false
    }

    private fun existsInProjectSymbols(file: PhelFile, qualifier: String, functionName: String): Boolean =
        PhelProjectSymbolIndex.getInstance(file.project).findQualified(file, qualifier, functionName) != null

    private fun isNamespaceKnown(file: PhelFile, symbol: PhelSymbol, qualifier: String, resolvedNamespace: String): Boolean {
        val named = PhelNamespaceUtils.importedNamespaceFor(file, qualifier) ?: qualifier
        if (PhelProjectNamespaceFinder.isStandardLibrary(named)) return true

        if (PhelProjectNamespaceFinder.getStandardLibraryFullNamespace(resolvedNamespace) != null) {
            return true
        }

        val projectNamespace = PhelProjectNamespaceFinder.findByShortName(symbol.project, resolvedNamespace)
        if (projectNamespace != null) {
            return true
        }

        if (qualifier != resolvedNamespace) {
            val qualifierNamespace = PhelProjectNamespaceFinder.findByShortName(symbol.project, qualifier)
            if (qualifierNamespace != null) {
                return true
            }
        }

        return false
    }
}
