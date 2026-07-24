package org.phellang.indexing.scanner

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelMetadata

/**
 * Decides whether a top-level definition is private, and so must stay out of the project symbol
 * index.
 *
 * Phel offers three spellings: the `defn-` family, a `^` flag on the name symbol, and a
 * keyword or map in the metadata slot between the name and the value.
 */
internal object PhelDefinitionPrivacy {

    private val PRIVATE_KEYWORDS = setOf("defn-", "def-", "defmacro-")

    private const val PRIVATE_KEY = ":private"

    /** The `defn-` / `def-` / `defmacro-` family, private by virtue of the keyword alone. */
    fun isPrivateKeyword(keyword: String): Boolean = keyword in PRIVATE_KEYWORDS

    /**
     * The body is never consulted: a definition that merely mentions `:private` in its docstring or
     * references a `:private-mode` keyword stays public.
     */
    fun isPrivate(forms: List<PhelForm>): Boolean =
        hasPrivateNameFlag(forms[1]) || hasPrivateMetadataSlot(forms)

    /** `^:private` / `^{:private true}` attached to the name symbol. */
    private fun hasPrivateNameFlag(nameForm: PhelForm): Boolean {
        val metadata = PsiTreeUtil.getChildrenOfType(nameForm, PhelMetadata::class.java) ?: return false

        return metadata.any { meta ->
            PsiTreeUtil.getChildOfType(meta, PhelKeyword::class.java)?.text == PRIVATE_KEY ||
                    PsiTreeUtil.getChildOfType(meta, PhelMap::class.java)?.let { declaresPrivate(it) } == true
        }
    }

    /**
     * The metadata slot sits between the name and the value, so the last form is always the value
     * (or body) and never metadata — that is what keeps `(def defaults {:visibility :private-ish})`
     * public while `(def x {:private true} 1)` is private.
     */
    private fun hasPrivateMetadataSlot(forms: List<PhelForm>): Boolean {
        for (i in 2 until forms.size - 1) {
            when (val form = forms[i]) {
                is PhelKeyword -> if (form.text == PRIVATE_KEY) return true
                is PhelMap -> if (declaresPrivate(form)) return true
                // A docstring may precede the flag; anything else ends the metadata slot.
                else -> if (PhelFormReader.stringLiteralOf(form) == null) return false
            }
        }

        return false
    }

    /** True when [map] binds `:private` to `true`; `{:private false}` is explicitly public. */
    private fun declaresPrivate(map: PhelMap): Boolean =
        PhelFormReader.mapValueFor(map, PRIVATE_KEY)?.text == "true"
}
