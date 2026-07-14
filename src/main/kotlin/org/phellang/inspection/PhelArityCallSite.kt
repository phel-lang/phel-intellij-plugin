package org.phellang.inspection

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Decides whether a list is a real function call whose argument count can be checked, or one of the
 * many shapes where the textual count is untrustworthy or the head isn't a registry function.
 *
 * Every rule here exists to prevent a *false* arity warning; the arity check itself is in
 * [PhelArityMismatchInspection]. Kept separate because "is this even a call site" is a distinct
 * question from "does this call have the right number of arguments".
 */
internal object PhelArityCallSite {

    /** True when the arity check must not run for [list] with head [name]. */
    fun shouldSkip(list: PhelList, head: PhelSymbol, name: String, forms: List<PhelForm>): Boolean {
        if (name in SKIP_HEADS) return true
        if (name.startsWith("php/") || name.startsWith(".") || name.startsWith("php-")) return true
        // The dummy identifier the platform injects at the caret mid-completion.
        if (name.contains("IntelliJIdeaRulezzz")) return true

        if (isInQuoteContext(list)) return true
        if (isThreadedArgList(list)) return true
        if (resolvesToLocalBinding(head, name)) return true
        if (containsShortFnMarker(forms)) return true

        return false
    }

    /**
     * True when any argument is a bare `|` symbol — the marker for Phel's deprecated `|(...)`
     * short-fn syntax. The lexer treats `|` as a plain symbol, so `|(> $ 10)` parses as two forms
     * (a `|` symbol and a list), inflating the textual argument count. Skip rather than misreport.
     */
    private fun containsShortFnMarker(forms: List<PhelForm>): Boolean =
        forms.drop(1).any { PhelPsiUtils.asSymbol(it)?.text == "|" }

    /**
     * True when [list] is a data form — inside `quote`, `var`, or a `'` / `` ` `` reader macro — and
     * therefore not a call site.
     */
    private fun isInQuoteContext(list: PhelList): Boolean {
        var current: PsiElement? = list
        while (current != null) {
            if (current is PhelForm && current.readerMacros.any { it.text == "'" || it.text == "`" }) {
                return true
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
     * True when [list] is an *argument* of a threading macro (`->`, `->>`, `as->`, `some->`, …). The
     * macro shifts the runtime argument count, so the textual count can't be trusted.
     */
    private fun isThreadedArgList(list: PhelList): Boolean {
        val parentList = enclosingList(list) ?: return false
        val parentForms = parentList.forms
        val head = PhelPsiUtils.asSymbol(parentForms.firstOrNull())?.text ?: return false
        if (head !in THREADING_HEADS) return false

        val headRange = parentForms.firstOrNull()?.textRange ?: return false
        // [list] is an argument (not the head form) of the threading macro.
        return !headRange.contains(list.textRange.startOffset)
    }

    /**
     * True when [name] resolves to a local binding — a `let`/`loop`/`for`/`binding` name or a
     * function parameter — visible at [head]. Such a name shadows any same-named core function, so
     * the registry's arity must not be applied.
     */
    private fun resolvesToLocalBinding(head: PhelSymbol, name: String): Boolean {
        var current: PsiElement? = head.parent
        while (current != null) {
            if (current is PhelList) {
                val forms = current.forms
                val parentHead = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text
                if (parentHead in BINDING_INTRO_FORMS && bindingVecContains(forms, name)) return true
                if (parentHead in FUNCTION_INTRO_FORMS && paramVecContains(forms, name)) return true
            }
            current = current.parent
        }
        return false
    }

    private fun enclosingList(list: PhelList): PhelList? =
        generateSequence(list.parent) { it.parent }.filterIsInstance<PhelList>().firstOrNull()

    /** Names sit at the even positions of a `[name value …]` binding vector. */
    private fun bindingVecContains(forms: List<PhelForm>, name: String): Boolean {
        val bindings = (forms.getOrNull(1) as? PhelVec)?.forms ?: return false
        return (bindings.indices step 2).any { PhelPsiUtils.asSymbol(bindings[it])?.text == name }
    }

    private fun paramVecContains(forms: List<PhelForm>, name: String): Boolean {
        val paramVec = forms.drop(1).firstNotNullOfOrNull { it as? PhelVec } ?: return false
        return paramVec.forms.any { PhelPsiUtils.asSymbol(it)?.text == name }
    }

    private val THREADING_HEADS = setOf(
        "->", "->>", "as->", "some->", "some->>", "cond->", "cond->>", "doto",
    )

    private val BINDING_INTRO_FORMS = PhelSpecialForms.LET_LIKE
    private val FUNCTION_INTRO_FORMS = PhelSpecialForms.FUNCTION_DEFINING

    /** Special forms and macros that legitimately accept variable arg shapes. */
    private val SKIP_HEADS = setOf(
        "if", "if-not", "when", "when-not", "if-let", "when-let", "if-some", "when-some",
        "do", "let", "loop", "recur", "fn", "defn", "defn-", "def", "def-", "defmacro", "defmacro-",
        "defstruct", "definterface", "defexception", "declare", "ns", "quote", "var",
        "try", "catch", "finally", "throw", "case", "cond", "condp", "and", "or",
        "->", "->>", "as->", "some->", "some->>", "doto", "binding", "for", "foreach", "dofor",
        "comment", "deftest", "is", "are", "testing", "with-output-buffer",
        "import", "require", "use", "set!", "..", ".",
    )
}
