package org.phellang.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.utils.PhelPsiUtils

object PhelReferUtils {
    data class ReferContext(
        val namespace: String,
        val containingVec: PhelVec
    )

    @JvmStatic
    fun isInsideReferVector(element: PsiElement): Boolean {
        return getReferContext(element) != null
    }

    @JvmStatic
    fun getReferNamespace(element: PsiElement): String? {
        return getReferContext(element)?.namespace
    }

    @JvmStatic
    fun getReferContext(element: PsiElement): ReferContext? {
        val containingVec = PsiTreeUtil.getParentOfType(element, PhelVec::class.java) ?: return null

        val requireForm = PsiTreeUtil.getParentOfType(containingVec, PhelList::class.java) ?: return null

        val forms = requireForm.forms
        if (forms.isEmpty()) return null

        if (PhelPsiUtils.asKeyword(forms[0])?.text != ":require") return null

        // The namespace symbol precedes :refer; remember it so the context can carry it.
        var namespace: String? = null

        for (i in 1 until forms.size) {
            val form = forms[i]

            val keyword = PhelPsiUtils.asKeyword(form)

            if (keyword?.text == ":refer" && i + 1 < forms.size) {
                if (forms[i + 1] === containingVec || PsiTreeUtil.isAncestor(forms[i + 1], containingVec, false)) {
                    return if (namespace != null) {
                        ReferContext(namespace, containingVec)
                    } else {
                        null
                    }
                }
            }

            if (keyword == null) {
                val namespaceSymbol = PhelPsiUtils.asSymbol(form)
                if (namespaceSymbol != null && namespace == null) {
                    namespace = namespaceSymbol.text
                }
            }
        }

        return null
    }

    @JvmStatic
    fun getAlreadyReferredSymbols(element: PsiElement, excludeElement: PsiElement? = null): Set<String> {
        val containingVec = PsiTreeUtil.getParentOfType(element, PhelVec::class.java) ?: return emptySet()

        val symbols = PsiTreeUtil.findChildrenOfType(containingVec, PhelSymbol::class.java)

        return symbols.mapNotNull { symbol ->
            if (excludeElement != null && symbol === excludeElement) {
                return@mapNotNull null
            }

            val text = symbol.text
            // Skip the dummy completion identifier (IntelliJIdeaRulezzz)
            if (text != null && !text.contains("IntelliJIdeaRulezzz")) {
                text
            } else {
                null
            }
        }.toSet()
    }

    @JvmStatic
    fun isDuplicateInReferVector(symbol: PhelSymbol): Boolean {
        val symbolName = symbol.text ?: return false
        val containingVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return false

        val allSymbols = PsiTreeUtil.findChildrenOfType(containingVec, PhelSymbol::class.java)

        for (otherSymbol in allSymbols) {
            if (otherSymbol === symbol) {
                return false
            }

            if (otherSymbol.text == symbolName) {
                return true
            }
        }

        return false
    }

    @JvmStatic
    fun extractShortNamespace(fullNamespace: String): String =
        PhelProjectNamespaceFinder.extractShortNamespace(fullNamespace)
}
