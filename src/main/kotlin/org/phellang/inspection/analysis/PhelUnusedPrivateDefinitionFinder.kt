package org.phellang.inspection.analysis

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.indexing.scanner.PhelDefinitionPrivacy
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Finds a private top-level definition that nothing in its own file references.
 *
 * Privacy is what makes this answerable at all: a public definition may be called from any other
 * namespace, so its own file cannot decide. A private one is reachable only from where it is
 * declared, so a file-local scan is the whole story.
 *
 * Privacy itself is delegated to [PhelDefinitionPrivacy], the same judgement the project symbol
 * index uses to decide what to leave out. It handles all three spellings, and the cases a textual
 * search for `:private` gets wrong: `{:private false}` is explicitly public, and a docstring that
 * happens to mention the word does not make a definition private.
 */
internal object PhelUnusedPrivateDefinitionFinder {

    /** The defined name when [list] is a private definition nothing else in the file mentions. */
    fun unusedPrivateDefinition(list: PhelList): PhelSymbol? {
        if (list.parent !is PhelFile) return null

        // activeForms: a `#_`-discarded form must not shift the head and name reads.
        val forms = PhelPsiUtils.activeForms(list)
        if (forms.size < 2) return null

        val keyword = PhelPsiUtils.asSymbol(forms[0])?.text ?: return null
        if (keyword !in PhelSpecialForms.DEFINITION_FORMS) return null

        if (!PhelDefinitionPrivacy.isPrivateKeyword(keyword) && !PhelDefinitionPrivacy.isPrivate(forms)) return null

        val name = PhelPsiUtils.asSymbol(forms[1]) ?: return null
        val nameText = name.text ?: return null

        return if (isReferenced(name, nameText)) null else name
    }

    /**
     * Any symbol with the same text other than the defining name itself.
     *
     * Only the name symbol is excluded, not the whole declaration, so a recursive call counts as a
     * use. A private function that merely calls itself is arguably dead too, but telling that apart
     * from a genuine use needs call-graph reachability; at this inspection's weak-warning level,
     * staying quiet is the safer error.
     */
    private fun isReferenced(nameSymbol: PhelSymbol, name: String): Boolean {
        val file = nameSymbol.containingFile ?: return true

        return PsiTreeUtil.findChildrenOfType(file, PhelSymbol::class.java)
            .any { it.text == name && it !== nameSymbol }
    }
}
