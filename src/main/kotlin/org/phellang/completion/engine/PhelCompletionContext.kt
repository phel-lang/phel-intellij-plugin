package org.phellang.completion.engine

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.psi.PsiElement
import org.phellang.completion.engine.context.PhelBindingPositions
import org.phellang.completion.engine.context.PhelNamePositions
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelReferUtils
import org.phellang.language.psi.files.PhelFile

/**
 * What the caret's surroundings say about which completions make sense.
 *
 * The predicates that decide whether the caret is *declaring* a name rather than referring to one
 * live in `context/`; they are pure position tests over PSI and are the bulk of the logic.
 */
class PhelCompletionContext(parameters: CompletionParameters) {

    val element: PsiElement = parameters.position

    fun shouldSuggestNewForm(): Boolean = isAtFileLevel()

    /**
     * A position that introduces a name, or that can only be followed by `[`, has nothing to
     * complete against.
     */
    fun shouldSuppressCompletions(): Boolean =
        PhelNamePositions.isFunctionName(element) ||
                PhelBindingPositions.isParameterVector(element) ||
                PhelBindingPositions.isBindingName(element) ||
                PhelBindingPositions.isAwaitingVector(element) ||
                PhelNamePositions.isDefinitionName(element)

    fun isInsideReferVector(): Boolean = PhelReferUtils.isInsideReferVector(element)

    fun getReferNamespace(): String? = PhelReferUtils.getReferNamespace(element)

    fun getAlreadyReferredSymbols(): Set<String> = PhelReferUtils.getAlreadyReferredSymbols(element)

    /** True when no enclosing list is reached before the file itself. */
    private fun isAtFileLevel(): Boolean {
        var current: PsiElement = element
        var depth = 0

        while (depth < MAX_DEPTH) {
            if (current is PhelFile) return true
            if (current is PhelList) return false

            current = current.parent
            depth++
        }

        return false
    }

    private companion object {
        /** Guards against a malformed tree walking forever; deeper than any real nesting. */
        const val MAX_DEPTH = 10
    }
}
