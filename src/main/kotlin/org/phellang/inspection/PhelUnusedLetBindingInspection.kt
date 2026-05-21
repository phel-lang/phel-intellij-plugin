package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*

class PhelUnusedLetBindingInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                val forms = o.forms
                if (forms.size < 2) return
                val head = forms[0] as? PhelSymbol ?: return
                if (head.text !in LET_LIKE_FORMS) return

                val bindingVec = forms[1] as? PhelVec ?: return
                val bindings = bindingVec.forms

                val body = forms.drop(2)
                if (body.isEmpty()) return

                var i = 0
                while (i < bindings.size) {
                    val target = bindings[i]
                    val name = (target as? PhelSymbol)?.text
                    if (name != null && name != "_" && !name.startsWith("_") && !name.startsWith("&")) {
                        if (!isUsedInBody(name, body) && !isUsedInLaterBindings(name, bindings, i)) {
                            holder.registerProblem(
                                target,
                                "Binding '$name' is never used.",
                                ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                                PhelRemoveLetBindingQuickFix(),
                            )
                        }
                    }
                    i += 2
                }
            }
        }
    }

    private fun isUsedInBody(name: String, body: List<PhelForm>): Boolean {
        for (form in body) {
            val symbols = PsiTreeUtil.findChildrenOfType(form, PhelSymbol::class.java)
            for (s in symbols) {
                if (s.text == name) return true
            }
            if ((form as? PhelSymbol)?.text == name) return true
        }
        return false
    }

    private fun isUsedInLaterBindings(name: String, bindings: List<PhelForm>, fromIndex: Int): Boolean {
        // Values at odd indices can reference earlier names.
        var i = fromIndex + 1
        while (i < bindings.size) {
            val value = bindings[i]
            val symbols = PsiTreeUtil.findChildrenOfType(value, PhelSymbol::class.java)
            for (s in symbols) {
                if (s.text == name) return true
            }
            i += 2
        }
        return false
    }

}

private val LET_LIKE_FORMS = setOf(
    "let", "if-let", "when-let", "loop", "for", "foreach", "binding", "dofor",
)