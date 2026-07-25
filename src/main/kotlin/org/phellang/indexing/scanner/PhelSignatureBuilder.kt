package org.phellang.indexing.scanner

import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.utils.PhelPsiUtils

/** Renders the call signature shown for an indexed definition, e.g. `(my-fn x & rest)`. */
internal object PhelSignatureBuilder {

    fun signatureFor(keyword: String, name: String, forms: List<PhelForm>): String = when (keyword) {
        "defn", "defmacro" -> functionSignature(name, forms)
        "defstruct" -> structSignature(name, forms)
        "definterface" -> "($name ...)"
        else -> nameOnly(name)
    }

    /**
     * A `defn` is single-arity when it has a direct parameter vector and multi-arity only when the
     * body is made of `([params] body)` clauses with no top-level vector.
     *
     * The direct vector is checked first on purpose: otherwise a single-arity body whose first form
     * is a vector-headed list — `(defn idx [i] ([10 20 30] i))`, a vector used as a function — reads
     * as a 3-arity clause.
     */
    private fun functionSignature(name: String, forms: List<PhelForm>): String {
        val paramVec = findDirectParameterVector(forms)
        if (paramVec != null) return format(name, parameterNames(paramVec))

        val arities = multiAritySignatures(name, forms)
        return if (arities.isEmpty()) nameOnly(name) else arities.joinToString("\n")
    }

    private fun structSignature(name: String, forms: List<PhelForm>): String {
        val fieldsVec = findDirectParameterVector(forms) ?: return nameOnly(name)

        return format(name, parameterNames(fieldsVec))
    }

    /** Each arity is a list whose first form is a parameter vector. */
    private fun multiAritySignatures(name: String, forms: List<PhelForm>): List<String> {
        return forms.drop(2)
            .filterIsInstance<PhelList>()
            .mapNotNull { PhelPsiUtils.activeForms(it).firstOrNull() as? PhelVec }
            .map { format(name, parameterNames(it)) }
    }

    private fun findDirectParameterVector(forms: List<PhelForm>): PhelVec? =
        forms.drop(2).filterIsInstance<PhelVec>().firstOrNull()

    private fun parameterNames(paramVec: PhelVec): List<String> =
        PhelPsiUtils.activeForms(paramVec).mapNotNull { it.text?.takeIf(String::isNotBlank) }

    private fun format(name: String, params: List<String>): String =
        if (params.isEmpty()) nameOnly(name) else "($name ${params.joinToString(" ")})"

    private fun nameOnly(name: String): String = "($name)"
}
