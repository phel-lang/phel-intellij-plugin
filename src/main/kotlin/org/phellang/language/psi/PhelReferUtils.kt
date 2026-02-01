package org.phellang.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

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
        // Find the containing vector
        val containingVec = PsiTreeUtil.getParentOfType(element, PhelVec::class.java) ?: return null

        // Find the containing require form
        val requireForm = PsiTreeUtil.getParentOfType(containingVec, PhelList::class.java) ?: return null

        // Get forms in the require statement
        val forms = PsiTreeUtil.getChildrenOfType(requireForm, PhelForm::class.java) ?: return null
        if (forms.isEmpty()) return null

        // First form should be :require keyword
        val firstForm = forms[0]
        val firstKeyword = firstForm as? PhelKeyword
            ?: PsiTreeUtil.findChildOfType(firstForm, PhelKeyword::class.java)
        if (firstKeyword?.text != ":require") return null

        // Find the namespace and check if our vector follows :refer
        var namespace: String? = null

        for (i in 1 until forms.size) {
            val form = forms[i]

            // Check for keyword
            val keyword = form as? PhelKeyword
                ?: PsiTreeUtil.findChildOfType(form, PhelKeyword::class.java)

            if (keyword?.text == ":refer" && i + 1 < forms.size) {
                // Check if the next form is our containing vector
                if (forms[i + 1] === containingVec || PsiTreeUtil.isAncestor(forms[i + 1], containingVec, false)) {
                    return if (namespace != null) {
                        ReferContext(namespace, containingVec)
                    } else {
                        null
                    }
                }
            }

            // If not a keyword, might be the namespace
            if (keyword == null) {
                val namespaceSymbol = form as? PhelSymbol
                    ?: PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
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

        // Get all symbols in the vector
        val symbols = PsiTreeUtil.findChildrenOfType(containingVec, PhelSymbol::class.java)

        return symbols.mapNotNull { symbol ->
            // Skip the excluded element
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

        // Get all symbols in the vector
        val allSymbols = PsiTreeUtil.findChildrenOfType(containingVec, PhelSymbol::class.java)

        // Check if there's a symbol with the same name before this one
        for (otherSymbol in allSymbols) {
            // Stop when we reach our symbol
            if (otherSymbol === symbol) {
                return false
            }

            // If we find the same name before our symbol, it's a duplicate
            if (otherSymbol.text == symbolName) {
                return true
            }
        }

        return false
    }

    @JvmStatic
    fun extractShortNamespace(fullNamespace: String): String {
        return fullNamespace.substringAfterLast("\\")
    }
}
