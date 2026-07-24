package org.phellang.indexing.scanner

import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelMap

/** Finds the documentation string of a top-level definition, in either place Phel allows it. */
internal object PhelDocstringReader {

    private const val DOC_KEY = ":doc"

    fun docstringOf(forms: List<PhelForm>): String? {
        if (forms.size < 3) return null

        return canonicalDocstring(forms[2]) ?: firstBodyStringLiteral(forms)
    }

    /**
     * The canonical position right after the name, either as a bare string or as the `:doc` entry of
     * a metadata map: `(def name "..." value)` and `(def name {:doc "..."} value)`.
     */
    private fun canonicalDocstring(form: PhelForm): String? =
        PhelFormReader.stringLiteralOf(form) ?: docFromMap(form)

    /**
     * Otherwise the first top-level string in the body, which covers `(defn foo [x] "doc" body)` and
     * `(defn foo {:meta} [x] "doc" body)`.
     */
    private fun firstBodyStringLiteral(forms: List<PhelForm>): String? =
        forms.drop(3).firstNotNullOfOrNull { PhelFormReader.stringLiteralOf(it) }

    private fun docFromMap(form: PhelForm): String? {
        val map = form as? PhelMap ?: return null
        val doc = PhelFormReader.mapValueFor(map, DOC_KEY) ?: return null

        return PhelFormReader.stringLiteralOf(doc)
    }
}
