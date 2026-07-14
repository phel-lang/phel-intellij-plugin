package org.phellang.language.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Reads the `(:use ...)` clauses of a file's `(ns ...)` form. Phel's `:use` brings PHP *classes*
 * (not Phel namespaces) into scope, so they can be named without the leading `\` — e.g.
 * `(:use \DateTime \Exception)` enables `(DateTime. …)` and `DateTime/createFromFormat`.
 *
 * Pure computation — [PhelNamespaceUtils] owns the per-file caching and delegates here.
 */
internal object PhelUseClauseAnalyzer {

    /**
     * The short class names declared via `(:use ...)`. Both `\Foo\Bar` and `Foo\Bar` yield `Bar` —
     * the name a user actually types in `(Bar. …)`, `(.method bar)` or `Bar/staticFn`.
     */
    fun computeUsedClasses(file: PhelFile): Set<String> {
        return classEntries(file)
            .mapNotNull { extractShortClassName(it.text) }
            .toSet()
    }

    /**
     * `\Phel\Compiler\CompilerFacade` -> `CompilerFacade`. Null when [text] has no class name
     * (empty, or nothing but separators). Backslashes are PHP's FQCN separator — this is not legacy
     * Phel-namespace handling.
     */
    fun extractShortClassName(text: String): String? {
        val trimmed = text.trimStart('\\')
        if (trimmed.isEmpty()) return null
        return trimmed.substringAfterLast('\\').ifEmpty { null }
    }

    /**
     * The file's `(:use ...)` entries indexed as short class name -> fully-qualified name, the FQN
     * normalized to a leading `\` (`Foo\Bar` -> `\Foo\Bar`).
     *
     * First entry wins on a duplicate short name — that case is already broken PHP, and first-wins
     * keeps this agreeing with go-to-definition.
     */
    fun buildUseFqnIndex(file: PhelFile): Map<String, String> {
        val index = mutableMapOf<String, String>()
        for (entry in classEntries(file)) {
            val raw = entry.text
            val shortName = extractShortClassName(raw) ?: continue
            val fqn = if (raw.startsWith("\\")) raw else "\\$raw"
            index.putIfAbsent(shortName, fqn)
        }
        return index
    }

    /**
     * True when [symbol] is one of the class entries in a `(:use ...)` clause — not the `:use`
     * keyword itself. Wires go-to-definition from the imported class name to its PHP declaration.
     */
    fun isUseClassSymbol(symbol: PhelSymbol): Boolean {
        val clause = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return false
        val forms = clause.forms
        if (forms.isEmpty()) return false

        val firstKeyword = forms[0] as? PhelKeyword
            ?: PsiTreeUtil.findChildOfType(forms[0], PhelKeyword::class.java)
        if (firstKeyword?.text != ":use") return false

        return forms.drop(1).any { it === symbol || PsiTreeUtil.isAncestor(it, symbol, false) }
    }

    /** The class-entry symbols across all `(:use ...)` clauses in [file]. */
    private fun classEntries(file: PhelFile): List<PhelSymbol> {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return emptyList()

        return PhelNamespaceUtils.findUseForms(nsDeclaration)
            .flatMap { it.forms.drop(1) }
            .mapNotNull(PhelPsiUtils::asSymbol)
    }
}
