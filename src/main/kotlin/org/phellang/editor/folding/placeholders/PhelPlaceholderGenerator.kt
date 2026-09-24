package org.phellang.editor.folding.placeholders

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*
import org.phellang.language.psi.utils.PhelPsiUtils

object PhelPlaceholderGenerator {
    /**
     * Derived rather than listed: this copy had drifted to omit `defonce`, `defenum`, `defprotocol`,
     * `defrecord`, `deftype`, `defmulti` and the starred variants, which folded as bare `(head...`.
     *
     * `ns` is excluded by construction and matched first below, since it renders its own placeholder.
     */
    private val DEFINING_FORMS = PhelSpecialForms.DEFINITION_FORMS

    fun generateListPlaceholder(list: PhelList): String {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java) ?: return "(...)"
        if (forms.isEmpty()) return "(...)"

        val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
        val firstText = firstSymbol?.text ?: return "(...)"

        return when {
            firstText == "ns" && forms.size >= 2 -> {
                generateNamespacePlaceholder(forms[1])
            }

            firstText in DEFINING_FORMS && forms.size >= 2 -> {
                generateDefiningFormPlaceholder(firstText, forms[1])
            }

            // Generic forms - show first symbol
            else -> "($firstText..."
        }
    }

    private fun generateDefiningFormPlaceholder(formType: String, nameForm: PhelForm): String {
        // asSymbol reads past a tag on the name: `(defn ^map build ...)` folds to `(defn build...`.
        val nameSymbol = PhelPsiUtils.asSymbol(nameForm)
        val name = nameSymbol?.text
        return if (name != null) "($formType $name..." else "$formType..."
    }

    private fun generateNamespacePlaceholder(nameForm: PhelForm): String {
        val nameSymbol = PsiTreeUtil.findChildOfType(nameForm, PhelSymbol::class.java)
        val name = nameSymbol?.text
        return if (name != null) "(ns $name..." else "ns..."
    }
}
