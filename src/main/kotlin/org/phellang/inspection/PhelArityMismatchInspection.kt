package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.phellang.registry.PhelArityResolver
import org.phellang.registry.accepts
import org.phellang.registry.describe
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.PhelVisitor

class PhelArityMismatchInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                val forms = PhelPsiUtils.activeForms(o)
                if (forms.isEmpty()) return
                val head = PhelPsiUtils.asSymbol(forms[0]) ?: return
                val name = head.text ?: return

                if (name in SKIP_HEADS) return
                if (name.startsWith("php/") || name.startsWith(".") || name.startsWith("php-")) return
                if (name.contains("IntelliJIdeaRulezzz")) return

                if (isInQuoteContext(o)) return
                if (isThreadedArgList(o)) return
                if (resolvesToLocalBinding(head, name)) return
                if (containsShortFnMarker(forms)) return

                val arities = PhelArityResolver.resolve(head.project, name) ?: return
                if (arities.isEmpty()) return

                val argCount = forms.size - 1
                if (!arities.accepts(argCount)) {
                    holder.registerProblem(
                        head,
                        "Wrong number of args ($argCount) passed to '$name'. Expected: ${arities.describe()}.",
                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    )
                }
            }
        }
    }

    /**
     * True when any argument is a bare `|` symbol — the marker for Phel's deprecated
     * `|(...)` short-fn syntax. The lexer treats `|` as a plain symbol (it does not
     * recognize the removed short-fn opener), so `|(> $ 10)` parses as two forms — a `|`
     * symbol and a list — inflating the textual argument count. We can't trust the count
     * in that case, so skip the check rather than report a false positive.
     */
    private fun containsShortFnMarker(forms: List<PhelForm>): Boolean {
        for (i in 1 until forms.size) {
            if (PhelPsiUtils.asSymbol(forms[i])?.text == "|") return true
        }
        return false
    }

    /**
     * True when [list] is a data form (inside a `quote`, `var`, syntax-quote `` ` ``,
     * or quote `'` reader macro) and therefore not a call site.
     */
    private fun isInQuoteContext(list: PhelList): Boolean {
        var current: PsiElement? = list
        while (current != null) {
            if (current is PhelForm) {
                for (macro in current.readerMacros) {
                    val t = macro.text
                    if (t == "'" || t == "`") return true
                }
            }
            if (current is PhelList && current !== list) {
                val head = PhelPsiUtils.asSymbol(current.forms.firstOrNull())?.text
                if (head == "quote" || head == "var") return true
            }
            current = current.parent
        }
        return false
    }

    /**
     * True when [list] is positioned as an argument inside a threading macro
     * (`->`, `->>`, `as->`, `some->`, `some->>`, `cond->`, `cond->>`, `doto`).
     * In that case the actual argument count seen at runtime is shifted, so we
     * cannot trust the textual arg count and must skip the check.
     */
    private fun isThreadedArgList(list: PhelList): Boolean {
        val parentList = findEnclosingList(list) ?: return false
        val parentForms = parentList.forms
        val head = PhelPsiUtils.asSymbol(parentForms.firstOrNull())?.text ?: return false
        if (head !in THREADING_HEADS) return false
        val firstFormRange = parentForms.firstOrNull()?.textRange ?: return false
        // True when [list] is an argument (i.e., not the head form) of the threading macro.
        return !firstFormRange.contains(list.textRange.startOffset)
    }

    private fun findEnclosingList(list: PhelList): PhelList? {
        var current: PsiElement? = list.parent
        while (current != null) {
            if (current is PhelList) return current
            current = current.parent
        }
        return null
    }

    /**
     * True when [name] resolves to a local binding (let/loop/for/binding, or `fn`/`defn`
     * parameter) visible at [head]. Such a name shadows any same-named core fn, so
     * the registry arity must not be applied.
     */
    private fun resolvesToLocalBinding(head: PhelSymbol, name: String): Boolean {
        var current: PsiElement? = head.parent
        while (current != null) {
            if (current is PhelList) {
                val parentForms = current.forms
                val parentHead = PhelPsiUtils.asSymbol(parentForms.firstOrNull())?.text
                if (parentHead != null) {
                    if (parentHead in BINDING_INTRO_FORMS && bindingVecContains(parentForms, name)) return true
                    if (parentHead in FUNCTION_INTRO_FORMS && paramVecContains(parentForms, name)) return true
                }
            }
            current = current.parent
        }
        return false
    }

    private fun bindingVecContains(forms: List<PhelForm>, name: String): Boolean {
        val vec = forms.getOrNull(1) as? org.phellang.language.psi.PhelVec ?: return false
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
            if (form is org.phellang.language.psi.PhelVec) {
                for (p in form.forms) {
                    if (PhelPsiUtils.asSymbol(p)?.text == name) return true
                }
                return false
            }
        }
        return false
    }

}

private val THREADING_HEADS = setOf(
    "->", "->>", "as->", "some->", "some->>", "cond->", "cond->>", "doto",
)

private val BINDING_INTRO_FORMS = PhelSpecialForms.LET_LIKE

private val FUNCTION_INTRO_FORMS = PhelSpecialForms.FUNCTION_DEFINING

// Special forms / macros that legitimately accept variable arg shapes.
// Skipping avoids false positives.
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
    "with-output-buffer",
    "import",
    "require",
    "use",
    "set!",
    "..",
    "."
)