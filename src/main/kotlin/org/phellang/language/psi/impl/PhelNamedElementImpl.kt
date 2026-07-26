package org.phellang.language.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiReference
import com.intellij.psi.util.CachedValue
import com.intellij.util.IncorrectOperationException
import org.jetbrains.annotations.NonNls
import org.phellang.language.psi.PhelPsiFactory
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.navigation.PhelItemPresentation
import org.phellang.language.psi.references.PhelReference
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.utils.cachedPerPsi

/**
 * Base implementation for named Phel elements that can be referenced and renamed.
 * This class implements PsiNameIdentifierOwner to enable proper 'Go to Definition' functionality.
 */
abstract class PhelNamedElementImpl(node: ASTNode) : PhelSFormImpl(node), PsiNameIdentifierOwner {
    override fun getName(): String? =
        if (this is PhelSymbol) PhelPsiUtils.getName(this) else null

    @Throws(IncorrectOperationException::class)
    override fun setName(name: @NonNls String): PsiElement {
        if (this !is PhelSymbol) return this
        val newSymbol = PhelPsiFactory.createSymbol(project, name)
        return this.replace(newSymbol)
    }

    override fun getNameIdentifier(): PsiElement? = this

    /**
     * One reference instance per element, until the PSI changes.
     *
     * This built a fresh [PhelReference] on every call, which is a problem twice over:
     * [PhelSymbolAnalyzer.isDefinition] was recomputed each time, and `ResolveCache` keys on the
     * reference, so a new instance meant the resolve cache could never hit. Caching the reference is
     * what makes caching the resolution possible at all.
     */
    override fun getReference(): PsiReference? {
        if (this !is PhelSymbol) return null

        return cachedPerPsi(this, REFERENCE_KEY) {
            PhelReference(this, findUsages = PhelSymbolAnalyzer.isDefinition(this))
        }
    }

    override fun getTextOffset(): Int =
        if (this is PhelSymbol) PhelPsiUtils.getNameTextOffset(this) else super.getTextOffset()

    override fun getPresentation(): ItemPresentation? =
        if (this is PhelSymbol) PhelItemPresentation(this) else super.getPresentation()

    private companion object {
        val REFERENCE_KEY = Key.create<CachedValue<PhelReference>>("phel.symbol.reference")
    }
}
