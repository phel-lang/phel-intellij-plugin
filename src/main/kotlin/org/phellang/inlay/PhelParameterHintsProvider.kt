package org.phellang.inlay

import com.intellij.codeInsight.hints.declarative.HintFormat
import com.intellij.codeInsight.hints.declarative.InlayHintsCollector
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.phellang.registry.PhelArity
import org.phellang.registry.PhelArityResolver
import org.phellang.registry.selectFor
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

class PhelParameterHintsProvider : InlayHintsProvider {

    override fun createCollector(file: PsiFile, editor: Editor): InlayHintsCollector = Collector()

    private class Collector : SharedBypassCollector {

        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            if (element !is PhelList) return

            val forms = PhelPsiUtils.activeForms(element)
            if (forms.size < 2) return

            val headSymbol = PhelPsiUtils.asSymbol(forms[0]) ?: return
            val headName = headSymbol.text ?: return

            if (headName in SKIP_HEADS) return
            if (headName.startsWith("php/") || headName.startsWith(".") || headName.startsWith("php-")) {
                return
            }
            if (resolvesToLocalBinding(headSymbol, headName)) return

            val args = forms.drop(1)
            val arities = PhelArityResolver.resolve(headSymbol.project, headName) ?: return
            val arity = arities.selectFor(args.size) ?: return

            for ((i, arg) in args.withIndex()) {
                val paramName = paramNameAt(arity, i) ?: continue
                if (paramName == "_") continue
                val argText = arg.text?.trim()
                if (argText != null && argText == paramName) continue
                sink.addPresentation(
                    InlineInlayPosition(arg.textRange.startOffset, false),
                    hintFormat = HintFormat.default,
                ) {
                    text("$paramName:")
                }
            }
        }

        private fun paramNameAt(arity: PhelArity, argIndex: Int): String? {
            return if (arity.variadic && argIndex >= arity.fixedCount) {
                // Once we're past the fixed arity, all remaining args fold into the rest param.
                // Drop the `& ` prefix to keep the inlay tight.
                arity.params.lastOrNull()
            } else {
                arity.params.getOrNull(argIndex)
            }
        }

        /** True when [name] resolves to an enclosing let/loop binding or fn parameter. */
        private fun resolvesToLocalBinding(head: PhelSymbol, name: String): Boolean {
            var current: PsiElement? = head.parent
            while (current != null) {
                if (current is PhelList) {
                    val forms = current.forms
                    val parentHead = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text
                    if (parentHead != null) {
                        if (parentHead in BINDING_INTRO_FORMS && bindingVecContains(forms, name)) return true
                        if (parentHead in FUNCTION_INTRO_FORMS && paramVecContains(forms, name)) return true
                    }
                }
                current = current.parent
            }
            return false
        }

        private fun bindingVecContains(forms: List<PhelForm>, name: String): Boolean {
            val vec = forms.getOrNull(1) as? PhelVec ?: return false
            val bindings = vec.forms
            var i = 0
            while (i < bindings.size) {
                if (PhelPsiUtils.asSymbol(bindings[i])?.text == name) return true
                i += 2
            }
            return false
        }

        private fun paramVecContains(forms: List<PhelForm>, name: String): Boolean {
            for (form in forms.drop(1)) {
                if (form is PhelVec) {
                    for (p in form.forms) {
                        if (PhelPsiUtils.asSymbol(p)?.text == name) return true
                    }
                    return false
                }
            }
            return false
        }
    }
}

private val BINDING_INTRO_FORMS = PhelSpecialForms.LET_LIKE

private val FUNCTION_INTRO_FORMS = PhelSpecialForms.FUNCTION_DEFINING

// Special forms and macros where param-name hints would be noise.
private val SKIP_HEADS = setOf(
    "if",
    "if-not",
    "when",
    "when-not",
    "if-let",
    "when-let",
    "if-some",
    "when-some",
    "do",
    "let",
    "loop",
    "recur",
    "fn",
    "defn",
    "defn-",
    "def",
    "def-",
    "defmacro",
    "defmacro-",
    "defstruct",
    "definterface",
    "defexception",
    "declare",
    "ns",
    "quote",
    "var",
    "try",
    "catch",
    "finally",
    "throw",
    "case",
    "cond",
    "condp",
    "and",
    "or",
    "->",
    "->>",
    "as->",
    "some->",
    "some->>",
    "doto",
    "binding",
    "for",
    "foreach",
    "dofor",
    "comment",
    "deftest",
    "is",
    "are",
    "testing",
    "with-output-buffer"
)
