package org.phellang.language.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiReference
import com.intellij.util.IncorrectOperationException
import org.jetbrains.annotations.NonNls
import org.phellang.core.psi.PhelPsiUtils
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.navigation.PhelItemPresentation
import org.phellang.language.psi.references.PhelReference

/**
 * Base implementation for named Phel elements that can be referenced and renamed.
 * This class implements PsiNameIdentifierOwner to enable proper 'Go to Definition' functionality.
 */
abstract class PhelNamedElementImpl(node: ASTNode) : PhelSFormImpl(node), PsiNameIdentifierOwner {
    override fun getName(): String? =
        if (this is PhelSymbol) PhelPsiUtils.getName(this) else null

    @Throws(IncorrectOperationException::class)
    override fun setName(name: @NonNls String): PsiElement {
        // Rename not yet supported -- returns element unchanged.
        // Implementing this requires creating a replacement PSI node via PhelPsiFactory.
        return this
    }

    override fun getNameIdentifier(): PsiElement? = this

    override fun getReference(): PsiReference? {
        if (this is PhelSymbol) {
            val isDefinition = PhelSymbolAnalyzer.isDefinition(this)
            return PhelReference(this, findUsages = isDefinition)
        }
        return null
    }

    override fun getTextOffset(): Int =
        if (this is PhelSymbol) PhelPsiUtils.getNameTextOffset(this) else super.getTextOffset()

    override fun getPresentation(): ItemPresentation? =
        if (this is PhelSymbol) PhelItemPresentation(this) else super.getPresentation()
}
