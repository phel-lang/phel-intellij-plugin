package org.phellang.language.psi

/**
 * Which characters a Phel symbol is made of.
 *
 * Mirrors the lexer's own `ATOM_START` / `ATOM_CONT` classes in `language/Phel.flex`, which are the
 * only real answer to this question. Both are written there as *complements* — everything except the
 * brackets, whitespace and the reader sigils — so any hand-written allowlist is narrower than the
 * language, and the two that existed were narrower in different directions: the completion char
 * filter allowed `%` but not `.` or `\`, the rename validator allowed `.` and `'` but not `%`, and
 * neither allowed `\`. Typing `.` while completing `.method`, or `\` while completing `\DateTime`,
 * therefore closed the lookup.
 *
 * `,` is absent because Phel lexes it as whitespace (`WHITE_SPACE=[\s,]+`).
 *
 * `"` is excluded from both, where the lexer's complement class technically admits it mid-atom: the
 * `STRING` rule matches first at every token boundary, so an atom containing a quote cannot arise
 * from well-formed source, and treating one as part of a name would have the completion popup swallow
 * the opening quote of a string.
 */
object PhelSymbolChars {

    /** Brackets, whitespace and the sigils that terminate an atom wherever they appear. */
    private val NEVER = setOf(
        '(', ')', '[', ']', '{', '}',
        ',', '`', '@', ';', '~', '^', '"',
        ' ', '\n', '\r', '\t',
    )

    /** `'` and `#` may sit inside a symbol but never open one: there they are reader-macro sigils. */
    private val NEVER_FIRST = NEVER + setOf('\'', '#')

    /** True when [c] may be the first character of a symbol. */
    fun isSymbolStart(c: Char): Boolean = c !in NEVER_FIRST

    /** True when [c] may appear in a symbol after its first character. */
    fun isSymbolPart(c: Char): Boolean = c !in NEVER
}
