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
import org.phellang.indexing.PhelArityResolver
import org.phellang.registry.selectFor
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelLocalBindingScope
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
            if (!takesPositionalParameters(headSymbol, headName)) return

            val args = forms.drop(1)
            val arities = PhelArityResolver.resolve(headSymbol.project, headName) ?: return
            val arity = arities.selectFor(args.size) ?: return

            emitHints(args, arity, sink)
        }

        /** A head whose arguments are not plain positional parameters has no names to show. */
        private fun takesPositionalParameters(headSymbol: PhelSymbol, headName: String): Boolean {
            if (headName in PhelSpecialForms.VARIADIC_HEADS) return false
            // Interop resolves to PHP, whose parameter names the registry does not carry.
            if (PhelInteropShorthands.isInteropCall(headName)) return false

            // A local binding shadowing a known name is a different function entirely.
            return !PhelLocalBindingScope.resolvesToLocalBinding(headSymbol, headName)
        }

        private fun emitHints(args: List<PhelForm>, arity: PhelArity, sink: InlayTreeSink) {
            for ((i, arg) in args.withIndex()) {
                val paramName = paramNameAt(arity, i) ?: continue
                if (paramName == "_") continue
                // An argument already spelled like the parameter makes the hint pure noise.
                if (arg.text?.trim() == paramName) continue

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
    }
}
