package org.phellang.language.psi.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.util.IncorrectOperationException
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.utils.PhelPsiUtils
import java.util.Collections
import java.util.IdentityHashMap

/**
 * Reference for a Phel symbol, resolving to *every* matching target rather than just one — a name
 * can be a function and a macro, a forward declaration and its definition, or simply redefined.
 *
 * This class owns only the [PsiReferenceBase] contract and the order in which the resolvers are
 * consulted; each way of finding a target lives in its own resolver:
 *
 * * [PhelRequireNamespaceResolver] — the namespace head of a `(:require ...)` spec
 * * [PhpClassResolver]             — PHP interop (`(:use ...)` entries, `Class/method`)
 * * [PhelQualifiedSymbolResolver]  — `ns/name`, including `:as` aliases
 * * [PhelLocalScopeResolver]       — `let` bindings and function parameters
 * * [PhelDefinitionSearcher]       — top-level definitions in this file, then the project
 * * [PhelUsageFinder]              — the reverse direction, when the symbol *is* the definition
 */
class PhelReference @JvmOverloads constructor(
    element: PhelSymbol,
    /** True: find usages of this definition. False: resolve this usage to its definitions. */
    private val findUsages: Boolean = PhelSymbolAnalyzer.isDefinition(element)
) : PsiReferenceBase<PhelSymbol>(element, calculateRangeInElement(element)), PsiPolyVariantReference {

    private val symbolName: String? = PhelPsiUtils.getName(element)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val name = symbolName?.takeIf { it.isNotEmpty() } ?: return ResolveResult.EMPTY_ARRAY

        val targets = if (findUsages) {
            PhelUsageFinder.findUsages(myElement, name)
        } else {
            findDefinitions(name)
        }

        return targets.map(::PsiElementResolveResult).toTypedArray()
    }

    /**
     * The first resolver to produce a target wins, so the order encodes precedence: a require head
     * and a `(:use ...)` class must never fall through to Phel-side name matching, and a qualified
     * symbol must not be matched against local scope.
     */
    private fun findDefinitions(name: String): List<PsiElement> {
        PhelRequireNamespaceResolver.resolve(myElement)
            .takeIf { it.isNotEmpty() }
            ?.let { return it }

        // A PHP class in `(:use ...)` resolves only to its PHP declaration — otherwise a bare class
        // name such as `InvalidArgumentException` false-matches its own usages in this file.
        if (PhelNamespaceUtils.isUseClassSymbol(myElement)) {
            return resolvePhpTargets()
        }

        PhelPsiUtils.getQualifier(myElement)?.let { qualifier ->
            // PHP interop first: a PHP class qualifier must never be matched against a Phel namespace.
            resolvePhpTargets().takeIf { it.isNotEmpty() }?.let { return it }
            return PhelQualifiedSymbolResolver.resolve(myElement, qualifier, name)
        }

        return findUnqualifiedDefinitions(name)
    }

    /** Every scope an unqualified name can be declared in, de-duplicated by PSI identity. */
    private fun findUnqualifiedDefinitions(name: String): List<PsiElement> {
        val seen = Collections.newSetFromMap(IdentityHashMap<PsiElement, Boolean>())
        val results = mutableListOf<PsiElement>()

        fun addAll(elements: List<PsiElement>) = elements.forEach { if (seen.add(it)) results.add(it) }

        addAll(listOfNotNull(PhelLocalScopeResolver.resolve(myElement, name)))
        addAll(PhelDefinitionSearcher.findInCurrentFile(myElement, name))
        myElement.containingFile?.let { addAll(PhelLocalScopeResolver.findAllParametersIn(it, name)) }
        addAll(PhelDefinitionSearcher.findInProject(myElement, name))

        // The standard library is only consulted when nothing else matched — `map`, `filter` and the
        // rest of phel\core would otherwise shadow a project's own definitions.
        if (results.isEmpty()) {
            addAll(PhelDefinitionFinder.collectVendorDefinitions(myElement.project, "core", name))
        }

        // PHP interop is the last fall-through: a no-op when the PHP plugin isn't installed, and kept
        // off the hot path for ordinary Phel symbols.
        if (results.isEmpty()) {
            addAll(resolvePhpTargets())
        }

        return results
    }

    /** Prefer the specific member (`Class.` -> `__construct`) so go-to-def lands on the precise node. */
    private fun resolvePhpTargets(): List<PsiElement> {
        val members = PhpClassResolver.resolveAsPhpMember(myElement)
        if (members.isNotEmpty()) return members

        return PhpClassResolver.resolveAsPhpClass(myElement)
    }

    override fun resolve(): PsiElement? {
        val results = multiResolve(false)

        // Several usages of one definition: returning null makes the platform show its own chooser
        // rather than silently jumping to the first.
        if (findUsages && results.size > 1) return null

        return results.firstOrNull()?.element
    }

    override fun isReferenceTo(element: PsiElement): Boolean =
        multiResolve(false).any { element == it.element }

    override fun getVariants(): Array<Any?> {
        val name = symbolName ?: return emptyArray()

        val variants = mutableListOf<PsiElement>()
        variants += listOfNotNull(PhelLocalScopeResolver.resolve(myElement, name))
        variants += PhelDefinitionSearcher.findInCurrentFile(myElement, name)

        if (variants.size < MAX_PROJECT_VARIANTS) {
            variants += PhelDefinitionSearcher.findInProject(myElement, name)
        }

        return variants.toTypedArray()
    }

    @Throws(IncorrectOperationException::class)
    override fun handleElementRename(newElementName: String): PsiElement {
        val currentText = myElement.text ?: return myElement

        // A qualified symbol renames only its name part: `str/join` -> `str/<new>`.
        val slashIndex = currentText.lastIndexOf('/')
        val newText = if (slashIndex >= 0) {
            currentText.take(slashIndex + 1) + newElementName
        } else {
            newElementName
        }

        return myElement.setName(newText)
    }

    @Throws(IncorrectOperationException::class)
    override fun bindToElement(element: PsiElement): PsiElement = myElement

    companion object {
        /** Project-wide variants are only collected while the list is still short enough to be useful. */
        private const val MAX_PROJECT_VARIANTS = 20

        /** A qualified symbol references only the part after the `/`; an unqualified one, all of it. */
        private fun calculateRangeInElement(element: PhelSymbol): TextRange {
            val text = element.text ?: return TextRange.EMPTY_RANGE

            val slashIndex = text.lastIndexOf('/')
            if (slashIndex >= 0 && slashIndex < text.length - 1) {
                return TextRange.from(slashIndex + 1, text.length - slashIndex - 1)
            }

            return TextRange.from(0, text.length)
        }
    }
}
