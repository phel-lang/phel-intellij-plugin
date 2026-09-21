package org.phellang.inspection.deprecated

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElementVisitor
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelVisitor
import org.phellang.language.psi.analysis.PhelDestructuringAnalyzer
import org.phellang.language.psi.analysis.PhelDestructuringAnalyzer.PairOrder
import org.phellang.language.psi.analysis.PhelFormWalker
import org.phellang.language.psi.analysis.PhelParameterAnalyzer
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Flags map destructuring pairs still written key-first, `{:name n}`, which Phel 0.51.0 deprecated
 * in favour of the Clojure order, `{n :name}` (phel-lang #3115). The compiler reports the same
 * under `--warn-deprecations`; this mirrors it in the IDE, since the key-first order is announced
 * for removal in a future release.
 *
 * Only *patterns* are inspected: the name half of a let-like binding vector and the entries of a
 * function's parameter vector, nested patterns included. An ordinary map literal — a value, an
 * argument, a metadata map — is never a pattern, and neither is the `:or` map of defaults, whose
 * keys name bindings rather than introduce them.
 *
 * Two cases, told apart exactly as `MapBindingDeconstructor::resolveNormalPair` tells them apart:
 *
 * * `{:kw local}`, `{0 local}`, `{"str" local}`: the key cannot be bound to, the value can, so the
 *   pair is unambiguously key-first and the fix swaps the two forms.
 * * `{k v}`: both sides can be bound to, so the compiler reads it key-first and evaluates `k` as the
 *   lookup key. Flipping it would silently change which side is looked up, which is why the
 *   compiler refuses to suggest a spelling and why no quick fix is offered here either; the message
 *   says to write the key as a keyword or a string instead.
 *
 * A pair that binds on neither side (`{:a :b}`) is a compile error, not a deprecation, and is left
 * to the compiler.
 */
class PhelKeyFirstDestructuringInspection : LocalInspectionTool() {

    /**
     * The let-like forms whose head vector really is `[pattern value …]` pairs.
     *
     * `for` and `dofor` take `binding :verb expr` triples interleaved with `:modifier arg` pairs, and
     * `foreach` takes `[value coll]` or `[key value coll]`, so their even entries are not all
     * patterns: in `(for [[k v] :pairs {:a x}] …)` the map is the collection being iterated. The
     * compiler's own `InvalidDestructuringRule` keeps `FOR_FORMS` apart from `LET_LIKE_FORMS` for
     * the same reason. `doseq` stays: `(doseq [x xs] …)` pairs a pattern with a sequence.
     */
    private val PAIRED_BINDING_FORMS = PhelSpecialForms.LET_LIKE - setOf("for", "dofor", "foreach")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhelVisitor() {
            override fun visitList(list: PhelList) {
                for (pattern in patternsDeclaredBy(list)) {
                    for (map in PhelDestructuringAnalyzer.mapPatterns(pattern)) {
                        reportKeyFirstPairs(map, holder)
                    }
                }
            }
        }
    }

    /** The patterns [list] introduces: the even entries of a binding vector, or every parameter. */
    private fun patternsDeclaredBy(list: PhelList): List<PhelForm> {
        val forms = PhelPsiUtils.activeForms(list)
        val head = PhelPsiUtils.asSymbol(forms.firstOrNull())?.text ?: return emptyList()

        return when (head) {
            in PAIRED_BINDING_FORMS -> {
                val vector = forms.getOrNull(1)?.let(PhelFormWalker::vectorOf) ?: return emptyList()
                PhelDestructuringAnalyzer.letPatterns(vector)
            }

            in PhelSpecialForms.FUNCTION_DEFINING -> PhelParameterAnalyzer.parameterVectorsOf(list)
                .flatMap(PhelDestructuringAnalyzer::parameterPatterns)

            else -> emptyList()
        }
    }

    private fun reportKeyFirstPairs(map: PhelMap, holder: ProblemsHolder) {
        for (pair in PhelDestructuringAnalyzer.pairsOf(map)) {
            when (pair.order) {
                PairOrder.KEY_FIRST -> holder.registerProblem(
                    pair.left,
                    "Key-first map destructuring pair {${pair.left.text} ${pair.right.text}} is deprecated since Phel 0.51; " +
                            "write it binding-first, as {${pair.right.text} ${pair.left.text}}",
                    ProblemHighlightType.LIKE_DEPRECATED,
                    SwapToBindingFirstQuickFix(),
                )

                PairOrder.AMBIGUOUS -> holder.registerProblem(
                    pair.left,
                    "Ambiguous map destructuring pair {${pair.left.text} ${pair.right.text}}: both sides are binding forms, " +
                            "so Phel reads it key-first and evaluates '${pair.left.text}' as the lookup key; " +
                            "write the key as a keyword or a string to make the order unambiguous",
                    ProblemHighlightType.WEAK_WARNING,
                )

                PairOrder.BINDING_FIRST, PairOrder.UNBINDABLE -> Unit
            }
        }
    }

    /**
     * Rewrites `{:name n}` as `{n :name}` by swapping the two forms of the pair in the document,
     * keeping whatever whitespace separated them.
     *
     * The problem is registered on the key, so the partner is the next active form of the map. Runs
     * in the platform's write action: a failed document edit rolls back and is reported.
     */
    private class SwapToBindingFirstQuickFix : LocalQuickFix {
        override fun getFamilyName(): String = "Swap to binding-first destructuring"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val key = descriptor.psiElement as? PhelForm ?: return
            if (!key.isValid) return
            val map = key.parent as? PhelMap ?: return

            val entries = PhelPsiUtils.activeForms(map)
            val index = entries.indexOfFirst { it === key }
            val value = entries.getOrNull(index + 1) ?: return

            val file = map.containingFile ?: return
            val docManager = PsiDocumentManager.getInstance(project)
            val document = docManager.getDocument(file) ?: return

            val keyRange = key.textRange
            val valueRange = value.textRange
            val separator = document.getText(TextRange(keyRange.endOffset, valueRange.startOffset))

            document.replaceString(keyRange.startOffset, valueRange.endOffset, value.text + separator + key.text)
            docManager.commitDocument(document)
        }
    }
}
