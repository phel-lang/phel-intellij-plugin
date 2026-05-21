package org.phellang.inlay

import com.intellij.codeInsight.hints.HintInfo
import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.psi.PsiElement
import org.phellang.completion.data.PhelArity
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.completion.data.selectFor
import org.phellang.completion.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec

class PhelParameterHintsProvider : InlayParameterHintsProvider {

    override fun getParameterHints(element: PsiElement): List<InlayInfo> {
        if (element !is PhelList) return emptyList()

        val forms = element.forms
        if (forms.size < 2) return emptyList()

        val headSymbol = forms[0] as? PhelSymbol ?: return emptyList()
        val headName = headSymbol.text ?: return emptyList()

        if (headName in SKIP_HEADS) return emptyList()
        if (headName.startsWith("php/") || headName.startsWith(".") || headName.startsWith("php-")) {
            return emptyList()
        }
        if (resolvesToLocalBinding(headSymbol, headName)) return emptyList()

        val args = forms.drop(1)
        val arities = resolveArities(headSymbol, headName) ?: return emptyList()
        val arity = arities.selectFor(args.size) ?: return emptyList()

        val hints = mutableListOf<InlayInfo>()
        for ((i, arg) in args.withIndex()) {
            val paramName = paramNameAt(arity, i) ?: continue
            if (paramName == "_") continue
            val argText = arg.text?.trim()
            if (argText != null && argText == paramName) continue
            hints.add(InlayInfo(paramName, arg.textRange.startOffset))
        }
        return hints
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

    private fun resolveArities(headSymbol: PhelSymbol, headName: String): List<PhelArity>? {
        val shortName = headName.substringAfterLast('/')

        PhelFunctionRegistry.getFunction(shortName)?.let { fn ->
            val parsed = PhelArity.parseAll(fn.signature)
            if (parsed.isNotEmpty()) return parsed
        }

        val index = PhelProjectSymbolIndex.getInstance(headSymbol.project)
        return index.findByName(shortName).firstOrNull { it.arities.isNotEmpty() }?.arities
    }

    /** True when [name] resolves to an enclosing let/loop binding or fn parameter. */
    private fun resolvesToLocalBinding(head: PhelSymbol, name: String): Boolean {
        var current: PsiElement? = head.parent
        while (current != null) {
            if (current is PhelList) {
                val forms = current.forms
                val parentHead = (forms.firstOrNull() as? PhelSymbol)?.text
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
            if ((bindings[i] as? PhelSymbol)?.text == name) return true
            i += 2
        }
        return false
    }

    private fun paramVecContains(forms: List<PhelForm>, name: String): Boolean {
        for (form in forms.drop(1)) {
            if (form is PhelVec) {
                for (p in form.forms) {
                    if ((p as? PhelSymbol)?.text == name) return true
                }
                return false
            }
        }
        return false
    }

    override fun getHintInfo(element: PsiElement): HintInfo? {
        if (element !is PhelList) return null
        val head = element.forms.firstOrNull() as? PhelSymbol ?: return null
        val name = head.text ?: return null
        return HintInfo.MethodInfo(name, emptyList())
    }

    override fun getDefaultBlackList(): Set<String> = emptySet()

    override fun isBlackListSupported(): Boolean = true
}

private val BINDING_INTRO_FORMS = setOf(
    "let", "if-let", "when-let", "loop", "for", "foreach", "binding", "dofor",
)

private val FUNCTION_INTRO_FORMS = setOf("fn", "defn", "defn-", "defmacro", "defmacro-")

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