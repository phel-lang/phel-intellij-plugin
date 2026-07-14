package org.phellang.core.psi

import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.phellang.language.psi.PhelAccess
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/** The names of functions a file defines, used to paint a call to one of them as a local reference. */
internal object PhelLocalFunctionIndex {

    private val LOCAL_FUNCTION_NAMES_KEY: Key<CachedValue<Set<String>>> = Key.create("phel.localFunctionNames")

    private val FUNCTION_DEFINING_FORMS = PhelSpecialForms.FUNCTION_DEFINING

    /** True when [symbolText] calls a function this file defines — and [symbol] is not that definition. */
    fun isReferenceToLocalFunction(symbol: PhelSymbol, symbolText: String, definitionForms: Set<String>): Boolean {
        if (isFunctionName(symbol, definitionForms)) return false

        val file = symbol.containingFile as? PhelFile ?: return false
        return symbolText in namesDefinedIn(file)
    }

    /**
     * Top-level `fn`/`defn`-family names in [file].
     *
     * Cached: highlighting probes this once per regular symbol, and re-walking every top-level form
     * each time would make the scan quadratic in the size of the file.
     */
    private fun namesDefinedIn(file: PhelFile): Set<String> =
        CachedValuesManager.getCachedValue(file, LOCAL_FUNCTION_NAMES_KEY) {
            CachedValueProvider.Result.create(
                computeNames(file),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }

    private fun computeNames(file: PhelFile): Set<String> = file.children
        .filterIsInstance<PhelList>()
        .filter { PhelFormWalker.headText(it) in FUNCTION_DEFINING_FORMS }
        .mapNotNull { it.children.getOrNull(1) }
        .filter(PhelFormWalker::isSymbolLike)
        .mapNotNull { it.text }
        .toSet()

    /** True when [symbol] is the *name* in `(defn <symbol> …)` rather than a call to it. */
    private fun isFunctionName(symbol: PhelSymbol, definitionForms: Set<String>): Boolean {
        val parent = symbol.parent

        // The name may be wrapped in a PhelAccess, in which case the list is one level further up.
        val list = if (parent is PhelAccess) parent.parent as? PhelList else parent as? PhelList
        list ?: return false

        val children = list.children
        if (children.size < 2) return false
        if (PhelFormWalker.headText(list) !in definitionForms) return false

        val second = children[1]
        return second === symbol || (second is PhelAccess && second === parent)
    }
}
