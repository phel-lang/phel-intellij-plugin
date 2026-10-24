package org.phellang.indexing

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.indexing.scanner.PhelSignatureBuilder
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceFiles
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelFormWalker
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.references.PhelDefinitionSearcher
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.registry.PhelArity
import org.phellang.registry.PhelFunction
import org.phellang.registry.PhelFunctionRegistry
import org.phellang.registry.PhelProjectSymbol
import org.phellang.registry.SymbolType

/**
 * Resolves the call arities for a function name, used by both the arity-mismatch inspection
 * and the parameter-hint provider. Standard-library functions come from the generated
 * registry; project-defined functions come from their definitions.
 *
 * The arities are always those of the definition the compiler would call, never a same-named one
 * elsewhere: checking a call against another namespace's function reports the wrong thing.
 */
object PhelArityResolver {

    private const val CORE = "core"
    private const val COMMENT = "comment"

    /** Definitions whose parameters are known. A `def` holds any value, and its index signature `(x)` is no arity. */
    private val CALLABLE_TYPES = setOf(SymbolType.FUNCTION, SymbolType.MACRO, SymbolType.STRUCT)

    /**
     * Arities for [name], the call head [head] spells, or null when the name is unknown or its
     * definition declares no parameters. Locals are ruled out by the callers.
     */
    fun resolve(head: PhelSymbol, name: String): List<PhelArity>? {
        val slash = name.lastIndexOf('/')
        val shortName = if (slash >= 0) name.substring(slash + 1) else name
        val qualifier = if (slash > 0) name.substring(0, slash) else null

        val file = head.containingFile as? PhelFile ?: return if (qualifier == null) registryArities(shortName) else null

        return if (qualifier != null) {
            qualifiedArities(PhelProjectSymbolIndex.getInstance(head.project), file, qualifier, shortName)
        } else {
            unqualifiedArities(head, file, shortName)
        }
    }

    /**
     * `qualifier/name` is checked against the namespace the qualifier names and nowhere else: a userland
     * `u/count` binds to the `count` of the namespace `u` stands for, never to the stdlib's `count` or to
     * another namespace's. Unless the qualifier names a required project namespace, the stdlib's by its
     * registry name first (`string/join`; core functions are registered bare); else the project's
     * definition there.
     */
    private fun qualifiedArities(
        index: PhelProjectSymbolIndex,
        file: PhelFile,
        qualifier: String,
        name: String,
    ): List<PhelArity>? {
        val imported = PhelNamespaceUtils.importedNamespaceFor(file, qualifier)

        // The registry first for the stdlib: a vendored copy in the index, `phel.core`'s `in-ns`
        // buckets say, must not change what completion and hover show for the same function.
        if (imported == null || PhelProjectNamespaceFinder.isStandardLibrary(imported)) {
            stdlibFunction(imported ?: qualifier, name)?.let { return it.arities.ifEmpty { null } }
        }

        return index.findQualified(file, qualifier, name)?.let(::aritiesOf)
    }

    /**
     * A bare name, in the compiler's order (`SymbolResolver::resolveWithoutAlias`): the namespace it is
     * `:refer`'d from, else the current namespace, this file first and private definitions included;
     * then `phel.core`. A definition found there decides, even one that declares no parameters: it
     * shadows core's. Only a definition the compiler sees counts, see [isLive].
     */
    private fun unqualifiedArities(head: PhelSymbol, file: PhelFile, name: String): List<PhelArity>? {
        // The cached set first: finding the referring spec walks every require, and most heads are not referred.
        val referredFrom = name.takeIf { PhelNamespaceUtils.isReferredSymbol(file, it) }
            ?.let { PhelNamespaceUtils.findReferSource(file, it) }

        if (referredFrom != null && PhelProjectNamespaceFinder.isStandardLibrary(referredFrom)) {
            // Core only when the referred namespace has no such name, as in the compiler.
            val function = stdlibFunction(referredFrom, name) ?: PhelFunctionRegistry.getFunction(name)
            return function?.arities?.ifEmpty { null }
        }

        val definitions = if (referredFrom != null) {
            PhelDefinitionSearcher.findInNamespace(head, referredFrom, name, publicOnly = true).filter(::isLive)
        } else {
            // This file's own definition is enough; the namespace's other files are read only without one.
            PhelDefinitionSearcher.findInCurrentFile(head, name).filter(::isLive)
                .ifEmpty { namespaceDefinitions(head, file, name).filter(::isLive) }
        }

        if (definitions.isEmpty()) return registryArities(name)

        return definitions.firstNotNullOfOrNull(::aritiesOfDefinition)
    }

    /** The definitions of [name] in the other files of [file]'s namespace. */
    private fun namespaceDefinitions(head: PhelSymbol, file: PhelFile, name: String): List<PsiElement> {
        val namespace = PhelNamespaceFiles.declaredNamespace(file) ?: return emptyList()
        return PhelDefinitionSearcher.findInNamespace(head, namespace, name, publicOnly = false)
    }

    /**
     * A stdlib function by its registry name: `string/join`, `test.gen/int`, `http_client/get`, or bare
     * for core. [namespace] is a full stdlib namespace, or a qualifier no require accounts for, taken as
     * the registry name it spells.
     */
    private fun stdlibFunction(namespace: String, name: String): PhelFunction? {
        val registryNamespace = PhelProjectNamespaceFinder.registryNamespaceOf(namespace) ?: namespace
        return PhelFunctionRegistry.getFunction(if (registryNamespace == CORE) name else "$registryNamespace/$name")
    }

    /**
     * Whether the compiler ever sees the definition [nameSymbol] names. The definition finder also reads
     * ones it never does, which navigation wants: a `#_`-discarded form, the body of a `(comment ...)`,
     * a quoted form or `` ` `` template, and a template's `~name`. A call is checked against neither: an
     * old `(comment (defn helper [a b c] ...))` draft above the real `helper` made every call wrong.
     */
    private fun isLive(nameSymbol: PsiElement): Boolean {
        val list = PsiTreeUtil.getParentOfType(nameSymbol, PhelList::class.java) ?: return false
        val nameForm = PhelPsiUtils.activeForms(list).getOrNull(1) ?: return false

        if (PhelPsiUtils.asSymbol(nameForm) !== nameSymbol || nameForm.readerMacros.isNotEmpty()) return false
        if (PhelPsiUtils.isDiscardedByFormComment(list) || PhelFormWalker.isInQuoteContext(list)) return false

        return PhelFormWalker.enclosingLists(list).none { PhelFormWalker.headText(it) == COMMENT }
    }

    private fun registryArities(registryName: String): List<PhelArity>? =
        PhelFunctionRegistry.getFunction(registryName)?.arities?.ifEmpty { null }

    private fun aritiesOf(symbol: PhelProjectSymbol): List<PhelArity>? =
        symbol.arities.takeIf { symbol.type in CALLABLE_TYPES }?.ifEmpty { null }

    /**
     * The arities of the definition [nameSymbol] names, read the way the index reads them, or null for
     * one that declares no parameters (`def`, `declare`, `defmulti`, ...).
     */
    private fun aritiesOfDefinition(nameSymbol: PsiElement): List<PhelArity>? {
        val list = PsiTreeUtil.getParentOfType(nameSymbol, PhelList::class.java) ?: return null
        val forms = PhelPsiUtils.activeForms(list)
        val keyword = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text ?: return null
        val type = SymbolType.fromKeyword(keyword)?.takeIf { it in CALLABLE_TYPES } ?: return null
        if (!PhelSignatureBuilder.declaresParameters(type.primaryKeyword, forms)) return null
        val name = nameSymbol.text ?: return null

        return PhelArity.parseAll(PhelSignatureBuilder.signatureFor(type.primaryKeyword, name, forms)).ifEmpty { null }
    }
}
