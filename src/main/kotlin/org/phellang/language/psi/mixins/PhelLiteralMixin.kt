package org.phellang.language.psi.mixins

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.impl.PhelSFormImpl

abstract class PhelLiteralMixin(node: ASTNode) : PhelSFormImpl(node), PhelLiteral {

    /**
     * Delegates to the registered [com.intellij.psi.PsiReferenceContributor]s. The
     * GrammarKit-generated base (`ASTWrapperPsiElement`) does not consult the
     * reference registry on its own, so string literals would otherwise expose no
     * references and `findReferenceAt` (used by go-to-declaration) returns nothing.
     *
     * @see org.phellang.language.psi.references.PhelFilePathReferenceContributor
     */
    override fun getReferences(): Array<PsiReference> =
        ReferenceProvidersRegistry.getReferencesFromProviders(this)
}
