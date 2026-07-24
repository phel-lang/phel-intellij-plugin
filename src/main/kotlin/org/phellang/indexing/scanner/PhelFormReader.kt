package org.phellang.indexing.scanner

import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * The two primitive reads the definition scanners share: unwrapping a string literal and looking a
 * key up in a literal map. Both privacy detection and docstring extraction need them, and neither
 * owns the other.
 */
internal object PhelFormReader {

    /** The contents of [form] when it is a double-quoted string literal, otherwise null. */
    fun stringLiteralOf(form: PhelForm): String? {
        val text = (form as? PhelLiteral)?.text ?: return null
        if (!text.startsWith("\"") || !text.endsWith("\"") || text.length < 2) return null

        return text.substring(1, text.length - 1)
    }

    /** The form bound to [key] in [map], or null when the key is absent. */
    fun mapValueFor(map: PhelMap, key: String): PhelForm? {
        val forms = PhelPsiUtils.activeForms(map)
        for (i in 0 until forms.size - 1) {
            if ((forms[i] as? PhelKeyword)?.text == key) return forms[i + 1]
        }

        return null
    }
}
