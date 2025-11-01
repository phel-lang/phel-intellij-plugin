package org.phellang.editor.folding.placeholders

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*

object PhelPlaceholderGenerator {

    private val DEFINING_FORMS = setOf(
        "defn", "defn-", "defmacro", "defmacro-", "def", "def-", "defstruct", "definterface", "defexception", "declare"
    )

    fun generateListPlaceholder(list: PhelList): String {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java) ?: return "(...)"
        if (forms.isEmpty()) return "(...)"

        val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
        val firstText = firstSymbol?.text ?: return "(...)"

        return when {
            // Function definitions with names
            firstText in DEFINING_FORMS && forms.size >= 2 -> {
                generateDefiningFormPlaceholder(firstText, forms[1])
            }

            // Namespace declarations
            firstText == "ns" && forms.size >= 2 -> {
                generateNamespacePlaceholder(forms[1])
            }

            // Generic forms - show first symbol
            else -> "($firstText..."
        }
    }

    private fun generateDefiningFormPlaceholder(formType: String, nameForm: PhelForm): String {
        val nameSymbol = PsiTreeUtil.findChildOfType(nameForm, PhelSymbol::class.java)
        val name = nameSymbol?.text
        return if (name != null) "($formType $name..." else "$formType..."
    }

    private fun generateNamespacePlaceholder(nameForm: PhelForm): String {
        val nameSymbol = PsiTreeUtil.findChildOfType(nameForm, PhelSymbol::class.java)
        val name = nameSymbol?.text
        return if (name != null) "(ns $name..." else "ns..."
    }
}
