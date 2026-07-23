package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.PhelVisitor
import org.phellang.language.psi.utils.PhelPsiUtils

class PhelShadowedLetBindingInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                // activeForms, not forms: a `#_`-discarded form must not shift the head/binding-vec
                // reads or the name/value pairing in the binding vector.
                val forms = PhelPsiUtils.activeForms(o)
                if (forms.size < 2) return
                val head = PhelPsiUtils.asSymbol(forms[0]) ?: return
                val headText = head.text ?: return
                if (headText !in BINDING_INTRO_FORMS) return

                val bindingVec = forms[1] as? PhelVec ?: return
                val bindings = PhelPsiUtils.activeForms(bindingVec)

                var i = 0
                while (i < bindings.size) {
                    val target = PhelPsiUtils.asSymbol(bindings[i])
                    if (target != null) {
                        val name = target.text
                        if (!name.isNullOrEmpty() && name != "_" && !name.startsWith("&")) {
                            val outer = findOuterBinding(o, name)
                            if (outer != null) {
                                holder.registerProblem(
                                    target,
                                    "Binding '$name' shadows an outer binding.",
                                    ProblemHighlightType.WEAK_WARNING,
                                )
                            }
                        }
                    }
                    i += 2
                }
            }
        }
    }

    private fun findOuterBinding(innerForm: PhelList, name: String): PsiElement? {
        var current: PsiElement? = innerForm.parent
        while (current != null) {
            if (current is PhelList) {
                val parentForms = PhelPsiUtils.activeForms(current)
                val head = PhelPsiUtils.asSymbol(parentForms.firstOrNull())
                val headText = head?.text
                if (headText != null) {
                    val match = when {
                        headText in BINDING_INTRO_FORMS -> findInBindingVec(parentForms, name)
                        headText in FUNCTION_INTRO_FORMS -> findInParamVec(parentForms, name)
                        else -> null
                    }
                    if (match != null && match !== innerForm) return match
                }
            }
            current = current.parent
        }
        return null
    }

    private fun findInBindingVec(forms: List<PhelForm>, name: String): PsiElement? {
        val vec = forms.getOrNull(1) as? PhelVec ?: return null
        val bindings = PhelPsiUtils.activeForms(vec)
        var i = 0
        while (i < bindings.size) {
            val s = PhelPsiUtils.asSymbol(bindings[i])
            if (s?.text == name) return s
            i += 2
        }
        return null
    }

    private fun findInParamVec(forms: List<PhelForm>, name: String): PsiElement? {
        // (fn [params] ...) — params at index 1.
        // (defn name [params] ...) — params at index 2.
        // (defn name "doc" [params] ...) — scan first vec.
        for (form in forms.drop(1)) {
            if (form is PhelVec) {
                for (p in PhelPsiUtils.activeForms(form)) {
                    val text = PhelPsiUtils.asSymbol(p)?.text
                    if (text == name) return p
                }
                return null
            }
        }
        return null
    }

    companion object {
        private val BINDING_INTRO_FORMS = PhelSpecialForms.LET_LIKE
        private val FUNCTION_INTRO_FORMS = PhelSpecialForms.FUNCTION_DEFINING
    }
}
