package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.*
import org.phellang.language.psi.utils.PhelPsiUtils

class PhelUnusedLetBindingInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                val forms = o.forms
                if (forms.size < 2) return
                val head = PhelPsiUtils.asSymbol(forms[0]) ?: return
                if (head.text !in LET_LIKE_FORMS) return

                val bindingVec = forms[1] as? PhelVec ?: return
                val bindings = bindingVec.forms

                val body = forms.drop(2)
                if (body.isEmpty()) return

                // Names referenced anywhere in the body, collected in a single pass.
                val bodySymbols = collectSymbolTexts(body)

                // Walk bindings from the end so that, by the time we reach a binding name at
                // an even index, `laterValueSymbols` already holds every symbol used in the
                // value slots that follow it (Phel binds sequentially, so a value can only
                // reference names introduced earlier).
                val laterValueSymbols = HashSet<String>()
                val unused = ArrayList<PhelSymbol>()
                for (i in bindings.indices.reversed()) {
                    if (i % 2 == 1) {
                        collectSymbolTextsInto(bindings[i], laterValueSymbols)
                        continue
                    }
                    val target = PhelPsiUtils.asSymbol(bindings[i]) ?: continue
                    val name = target.text
                    if (name == "_" || name.startsWith("_") || name.startsWith("&")) continue
                    if (name !in bodySymbols && name !in laterValueSymbols) {
                        unused.add(target)
                    }
                }

                // Report top-to-bottom for a natural reading order.
                for (target in unused.asReversed()) {
                    holder.registerProblem(
                        target,
                        "Binding '${target.text}' is never used.",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                        PhelRemoveLetBindingQuickFix(),
                    )
                }
            }
        }
    }

    private fun collectSymbolTexts(forms: List<PhelForm>): Set<String> {
        val result = HashSet<String>()
        for (form in forms) collectSymbolTextsInto(form, result)
        return result
    }

    /** Adds the text of [element] (when it is a symbol) plus every descendant symbol. */
    private fun collectSymbolTextsInto(element: PhelForm, into: MutableSet<String>) {
        if (element is PhelSymbol) into.add(element.text)
        for (s in PsiTreeUtil.findChildrenOfType(element, PhelSymbol::class.java)) {
            into.add(s.text)
        }
    }
}

private val LET_LIKE_FORMS = PhelSpecialForms.LET_LIKE