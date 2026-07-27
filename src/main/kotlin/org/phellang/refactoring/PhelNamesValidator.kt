package org.phellang.refactoring

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbolChars

class PhelNamesValidator : NamesValidator {

    /**
     * [PhelSpecialForms] rather than a list of this file's own.
     *
     * The hand-written one had `defstruct` but not `defstruct*`, and no `defonce`, `defenum`,
     * `defprotocol`, `defrecord`, `deftype`, `defmulti`, `deftest`, `if-let`, `when-let`, `case`,
     * `cond`, `for`, `foreach` or `binding` — so renaming a symbol to any of those was accepted
     * without a word. It was the fourth copy of a set that exists once on purpose.
     */
    override fun isKeyword(name: String, project: Project?): Boolean =
        name in PhelSpecialForms.VARIADIC_HEADS ||
                name in PhelSpecialForms.NAME_DECLARING ||
                name in LITERALS

    /**
     * Character membership comes from [PhelSymbolChars], which mirrors the lexer's atom classes.
     *
     * The two leading-character rules are this validator's own, because in the lexer they are not
     * character rules at all: `1foo` and `:foo` are kept out of `ATOM` by `NUMBER` and `KEYWORD`
     * matching first, not by `ATOM_START` excluding the character.
     */
    override fun isIdentifier(name: String, project: Project?): Boolean {
        val first = name.firstOrNull() ?: return false
        if (first.isDigit() || first == KEYWORD_SIGIL) return false
        if (!PhelSymbolChars.isSymbolStart(first)) return false

        return name.all(PhelSymbolChars::isSymbolPart)
    }
}

private const val KEYWORD_SIGIL = ':'

/** Not forms, but reserved all the same: renaming something to `nil` shadows the literal. */
private val LITERALS = setOf("true", "false", "nil")
