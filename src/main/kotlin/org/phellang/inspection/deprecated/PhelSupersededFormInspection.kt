package org.phellang.inspection.deprecated

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVisitor
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer

/**
 * Flags the source forms Phel v0.50.0 superseded, mirroring the compiler's own
 * `--warn-deprecations` output in the IDE.
 *
 * One rule drives the interop entries: `php/` means host access, and is never a second spelling
 * for something Phel already says the Clojure way. The three interop forms below each have a
 * Clojure-style spelling that is now *the* spelling; they remain as the compilation target the
 * shorthand expands to, which is why they still appear in the registry and in completion.
 *
 * The rest of the `php/` family is deliberately absent: `php/aget`, `php/aset`, `php/apush`,
 * `php/aunset`, `php/oset`, `php/ref` and `php/callable` each reach a PHP capability Phel has no
 * other word for, so they are current, not superseded.
 *
 * No quick fix. Every replacement here reorders or re-nests the call's arguments —
 * `(php/-> obj (m a))` becomes `(.m obj a)` — so there is no text substitution that is correct in
 * general, and a fix that mangles a working call is worse than none. The message names the
 * replacement instead.
 *
 * Sources: the language-surface spec's "Deprecated inside 1.x" table, and phel-lang #2877 / #2888.
 */
class PhelSupersededFormInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitSymbol(symbol: PhelSymbol) {
                val text = symbol.text ?: return
                val superseded = SUPERSEDED[text] ?: return

                // A local binding or a definition that happens to share the name is not the form.
                if (PhelSymbolAnalyzer.isLocalBindingOrReference(symbol)) return
                if (PhelSymbolAnalyzer.isDefinition(symbol)) return

                holder.registerProblem(
                    symbol,
                    "'$text' is deprecated since Phel 0.50; write $superseded instead",
                    ProblemHighlightType.LIKE_DEPRECATED,
                )
            }
        }
    }

    private companion object {
        /** Superseded form -> the spelling to write instead, as it appears in the warning. */
        val SUPERSEDED = mapOf(
            "php/new" to "(Foo. arg) or (new Foo arg)",
            "php/->" to "(.method obj arg) or (.-field obj)",
            "php/::" to "(Foo/method arg) or Foo/CONST",
            "set-var" to "(alter-var-root #'v f), or (set! v x) for the current binding frame",
        )
    }
}
