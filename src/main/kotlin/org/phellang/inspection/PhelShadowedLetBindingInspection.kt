package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.PhelVisitor

class PhelShadowedLetBindingInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                val forms = o.forms
                if (forms.size < 2) return
                val head = forms[0] as? PhelSymbol ?: return
                val headText = head.text ?: return
                if (headText !in BINDING_INTRO_FORMS) return

                val bindingVec = forms[1] as? PhelVec ?: return
                val bindings = bindingVec.forms

                var i = 0
                while (i < bindings.size) {
                    val target = bindings[i] as? PhelSymbol
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
                val parentForms = current.forms
                val head = parentForms.firstOrNull() as? PhelSymbol
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
        val bindings = vec.forms
        var i = 0
        while (i < bindings.size) {
            val s = bindings[i] as? PhelSymbol
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
                for (p in form.forms) {
                    val text = (p as? PhelSymbol)?.text ?: PsiTreeUtil.findChildOfType(p, PhelSymbol::class.java)?.text
                    if (text == name) return p
                }
                return null
            }
        }
        return null
    }

    companion object {
        private val BINDING_INTRO_FORMS = setOf(
            "let", "if-let", "when-let", "loop", "for", "foreach", "binding", "dofor",
        )
        private val FUNCTION_INTRO_FORMS = setOf("fn", "defn", "defn-", "defmacro", "defmacro-")
    }
}
