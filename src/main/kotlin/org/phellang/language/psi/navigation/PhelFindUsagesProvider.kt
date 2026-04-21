package org.phellang.language.psi.navigation

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.lexer.PhelLexerAdapter
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypes
import org.phellang.language.psi.elements.PhelTokenSets
import kotlin.math.min

class PhelFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            PhelLexerAdapter(),
            PhelTokenSets.SYM,
            PhelTokenSets.LINE_COMMENT,
            TokenSet.create(PhelTypes.STRING, PhelTypes.NUMBER, PhelTypes.BOOL, PhelTypes.NIL)
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean = psiElement is PsiNamedElement

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        if (element is PhelSymbol) {
            if (PhelSymbolAnalyzer.isDefinition(element)) {
                return when (PhelPsiUtils.getDefiningKeyword(element)) {
                    "def", "def-" -> "variable"
                    "defn", "defn-" -> "function"
                    "defmacro", "defmacro-" -> "macro"
                    "defstruct" -> "struct"
                    "definterface" -> "interface"
                    "defexception" -> "exception"
                    "declare" -> "declaration"
                    else -> "symbol"
                }
            }
            return "symbol"
        }
        return "element"
    }

    override fun getDescriptiveName(element: PsiElement): String {
        if (element is PhelSymbol) {
            val name = PhelPsiUtils.getName(element) ?: return "<unnamed>"
            val type = getType(element)
            return if (!"symbol".equals(type, ignoreCase = true)) "$name [$type]" else name
        }
        return element.toString()
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        if (element is PhelSymbol) {
            val name = PhelPsiUtils.getName(element) ?: return element.toString()
            val context = getElementContext(element)
            val location = PhelPsiUtils.getLocationString(element)

            val result = StringBuilder()
            result.append(if (useFullName) getFullyQualifiedName(element, name) else name)
            if (!context.isNullOrEmpty()) result.append(" ").append(context)
            if (location.isNotEmpty()) result.append(" ").append(location)
            return result.toString()
        }
        return element.toString()
    }

    private fun getElementContext(symbol: PhelSymbol): String? {
        val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return null
        val listText = containingList.text ?: return null
        return if (listText.length > 50) {
            "in (" + listText.substring(1, min(47, listText.length - 1)) + "...)"
        } else {
            "in $listText"
        }
    }

    private fun getFullyQualifiedName(element: PsiElement, name: String): String {
        if (element is PhelSymbol) {
            val qualifier = PhelPsiUtils.getQualifier(element)
            if (qualifier != null) return "$qualifier/$name"
        }
        return name
    }
}
