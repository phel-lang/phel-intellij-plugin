package org.phellang.core.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Bindings introduced by `let`-like forms: `(let [x 1] …)`, `loop`, `for`, `binding`, `when-let`.
 *
 * A binding vector alternates names and values — `[name1 value1 name2 value2]` — so only the even
 * positions declare anything.
 */
internal object PhelLetBindingAnalyzer {

    private val LET_LIKE_FORMS = PhelSpecialForms.LET_LIKE

    /** True when [symbol] IS one of the names a let-like form binds. */
    fun isLetBinding(symbol: PhelSymbol): Boolean {
        val bindingVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return false
        val containingList = PsiTreeUtil.getParentOfType(bindingVec, PhelList::class.java) ?: return false

        val forms = PsiTreeUtil.getChildrenOfType(containingList, org.phellang.language.psi.PhelForm::class.java)
        if (forms == null || forms.size < 2) return false

        val head = PhelPsiUtils.asSymbol(forms[0]) ?: return false
        if (head.text !in LET_LIKE_FORMS) return false

        // The vector must be the form's *binding* vector (its second element), reached either
        // directly or through a PhelForm wrapper — not some vector in the body.
        if (!PhelFormWalker.isSameOrWrapperOf(forms[1], bindingVec)) return false

        val bindings = PsiTreeUtil.getChildrenOfType(bindingVec, org.phellang.language.psi.PhelForm::class.java)
            ?: return false

        return (bindings.indices step 2).any { PhelPsiUtils.asSymbol(bindings[it]) === symbol }
    }

    /** True when [symbolText] names a binding of some enclosing let-like form — i.e. a *reference*. */
    fun isReferenceToLetBinding(symbol: PhelSymbol, symbolText: String): Boolean {
        return PhelFormWalker.enclosingLists(symbol)
            .filter { PhelFormWalker.headText(it) in LET_LIKE_FORMS }
            .mapNotNull { it.children.getOrNull(1) as? PhelVec }
            .any { bindingVec -> bindsName(bindingVec, symbolText) }
    }

    private fun bindsName(bindingVec: PhelVec, symbolText: String): Boolean {
        val children = bindingVec.children
        return (children.indices step 2).any { i ->
            val child = children[i]
            PhelFormWalker.isSymbolLike(child) && child.text == symbolText
        }
    }
}
