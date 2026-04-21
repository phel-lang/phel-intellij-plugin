package org.phellang.language.psi.navigation

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import javax.swing.Icon
import kotlin.math.min

class PhelItemPresentation(private val myElement: PhelSymbol) : ItemPresentation {
    override fun getPresentableText(): String? {
        val name = PhelPsiUtils.getName(myElement) ?: return null

        if (PhelSymbolAnalyzer.isDefinition(myElement)) {
            val type = getTypeFromDefiningKeyword(PhelPsiUtils.getDefiningKeyword(myElement))
            if (type != null) {
                return "$name [$type]"
            }
        }

        return name
    }

    override fun getLocationString(): String {
        val context = elementContext
        val location = PhelPsiUtils.getLocationString(myElement)
        return if (context != null) "$context $location" else location
    }

    override fun getIcon(unused: Boolean): Icon? = null

    private val elementContext: String?
        get() {
            val containingList = PsiTreeUtil.getParentOfType(myElement, PhelList::class.java) ?: return null
            val listText = containingList.text ?: return null
            return if (listText.length > 60) {
                "(" + listText.substring(1, min(50, listText.length - 1)) + "...)"
            } else {
                listText
            }
        }

    private fun getTypeFromDefiningKeyword(keyword: String?): String? = when (keyword) {
        "def", "def-" -> "variable"
        "defn", "defn-" -> "function"
        "defmacro", "defmacro-" -> "macro"
        "defstruct" -> "struct"
        "definterface" -> "interface"
        "defexception" -> "exception"
        "declare" -> "declaration"
        else -> null
    }
}
