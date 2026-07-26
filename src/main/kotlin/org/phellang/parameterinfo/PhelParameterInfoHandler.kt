package org.phellang.parameterinfo

import com.intellij.lang.parameterInfo.CreateParameterInfoContext
import com.intellij.lang.parameterInfo.ParameterInfoHandler
import com.intellij.lang.parameterInfo.ParameterInfoUIContext
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.indexing.PhelArityResolver
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.analysis.PhelLocalBindingScope
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.registry.PhelArity

/**
 * The Ctrl+P popup: every arity of the called function, with the current parameter in bold.
 *
 * Distinct from the inlay hints, which label one argument each and can only ever show the arity that
 * already matched. This is what answers "what else could I pass?" — for a multi-arity function the
 * popup lists all of them and highlights the one the current argument count selects.
 *
 * Both features skip the same heads for the same reason, so both ask [PhelSpecialForms.VARIADIC_HEADS]
 * and [PhelInteropShorthands.isInteropCall] rather than keeping their own idea of what is positional.
 */
class PhelParameterInfoHandler : ParameterInfoHandler<PhelList, PhelArity> {

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): PhelList? {
        val call = callAt(context) ?: return null
        val arities = aritiesOf(call) ?: return null

        context.itemsToShow = arities.toTypedArray()

        return call
    }

    override fun showParameterInfo(element: PhelList, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset, this)
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): PhelList? = callAt(context)

    override fun updateParameterInfo(parameterOwner: PhelList, context: UpdateParameterInfoContext) {
        context.setCurrentParameter(argumentIndexAt(parameterOwner, context.offset))
    }

    /**
     * One line per arity. An arity that cannot accept the current argument count is greyed out, so a
     * two-arity function shows at a glance which one is in play.
     */
    override fun updateUI(p: PhelArity?, context: ParameterInfoUIContext) {
        if (p == null) return

        val names = p.params.mapIndexed { index, name -> if (p.variadic && index == p.fixedCount) "& $name" else name }
        if (names.isEmpty()) {
            context.setupUIComponentPresentation("<no parameters>", -1, -1, true, false, false, context.defaultParameterColor)
            return
        }

        val current = context.currentParameterIndex
        val highlighted = highlightedIndex(p, current)
        val text = names.joinToString(", ")
        val start = if (highlighted < 0) -1 else names.take(highlighted).sumOf { it.length + 2 }
        val end = if (highlighted < 0) -1 else start + names[highlighted].length

        context.setupUIComponentPresentation(
            text,
            start,
            end,
            /* isDisabled = */ !accepts(p, current),
            false,
            false,
            context.defaultParameterColor,
        )
    }

    /** Past the fixed parameters of a variadic arity every further argument belongs to the rest one. */
    private fun highlightedIndex(arity: PhelArity, argumentIndex: Int): Int = when {
        argumentIndex < 0 -> -1
        argumentIndex < arity.params.size -> argumentIndex
        arity.variadic -> arity.params.size - 1
        else -> -1
    }

    private fun accepts(arity: PhelArity, argumentIndex: Int): Boolean {
        if (argumentIndex < 0) return true

        return if (arity.variadic) true else argumentIndex < arity.params.size
    }

    private fun callAt(context: com.intellij.lang.parameterInfo.ParameterInfoContext): PhelList? {
        val at = context.file.findElementAt(context.offset) ?: return null

        return generateSequence(PsiTreeUtil.getParentOfType(at, PhelList::class.java)) {
            PsiTreeUtil.getParentOfType(it, PhelList::class.java)
        }.firstOrNull { aritiesOf(it) != null }
    }

    /** Null whenever the head is not a plain positional call, which is the same rule the hints use. */
    private fun aritiesOf(call: PhelList): List<PhelArity>? {
        val forms = PhelPsiUtils.activeForms(call)
        val head = PhelPsiUtils.asSymbol(forms.firstOrNull()) ?: return null
        val name = head.text ?: return null

        if (name in PhelSpecialForms.VARIADIC_HEADS) return null
        if (PhelInteropShorthands.isInteropCall(name)) return null
        if (PhelLocalBindingScope.resolvesToLocalBinding(head, name)) return null

        return PhelArityResolver.resolve(head.project, name)?.takeIf { it.isNotEmpty() }
    }

    /** Which argument the caret sits in: 0 for the first, counting the head as -1. */
    private fun argumentIndexAt(call: PhelList, offset: Int): Int {
        val forms = PhelPsiUtils.activeForms(call)

        return forms.drop(1).indexOfLast { it.textRange.startOffset <= offset }
    }
}
