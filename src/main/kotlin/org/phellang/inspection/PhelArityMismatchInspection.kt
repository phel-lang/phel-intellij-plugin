package org.phellang.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import org.phellang.completion.data.PhelArity
import org.phellang.completion.data.PhelFunctionRegistry
import org.phellang.completion.data.accepts
import org.phellang.completion.data.describe
import org.phellang.completion.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVisitor

class PhelArityMismatchInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(o: PhelList) {
                val forms = o.forms
                if (forms.isEmpty()) return
                val head = forms[0] as? PhelSymbol ?: return
                val name = head.text ?: return

                if (name in SKIP_HEADS) return
                if (name.startsWith("php/") || name.startsWith(".") || name.startsWith("php-")) return
                if (name.contains("IntelliJIdeaRulezzz")) return

                if (isInQuoteContext(o)) return
                if (isThreadedArgList(o)) return

                val arities = resolveArities(head, name) ?: return
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

    private fun resolveArities(head: PhelSymbol, name: String): List<PhelArity>? {
        val shortName = name.substringAfterLast('/')

        PhelFunctionRegistry.getFunction(shortName)?.let { fn ->
            val parsed = PhelArity.parseAll(fn.signature)
            if (parsed.isNotEmpty()) return parsed
        }

        val index = PhelProjectSymbolIndex.getInstance(head.project)
        val candidates = index.findByName(shortName)
        val withArities = candidates.firstOrNull { it.arities.isNotEmpty() }
        return withArities?.arities
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
                val head = (current.forms.firstOrNull() as? PhelSymbol)?.text
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
        val head = (parentForms.firstOrNull() as? PhelSymbol)?.text ?: return false
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

}

private val THREADING_HEADS = setOf(
    "->", "->>", "as->", "some->", "some->>", "cond->", "cond->>", "doto",
)

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