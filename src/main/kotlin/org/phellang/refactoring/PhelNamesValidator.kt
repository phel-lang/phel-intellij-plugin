package org.phellang.refactoring

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project

class PhelNamesValidator : NamesValidator {

    override fun isKeyword(name: String, project: Project?): Boolean = name in RESERVED

    override fun isIdentifier(name: String, project: Project?): Boolean {
        if (name.isEmpty()) return false
        val first = name[0]
        if (first.isDigit()) return false
        // Leading `:` is a keyword sigil; leading `'`/`` ` ``/`~`/`@`/`#` is a reader macro.
        if (first in INVALID_FIRST_CHARS) return false
        return name.all(::isSymbolChar)
    }

    private fun isSymbolChar(c: Char): Boolean {
        if (c.isLetterOrDigit()) return true
        return c in ALLOWED_PUNCT
    }

}

private val ALLOWED_PUNCT = setOf('-', '_', '?', '!', '*', '+', '/', '<', '>', '=', '.', '&', ':', '\'', '$')

private val INVALID_FIRST_CHARS = setOf(':', '\'', '`', '~', '@', '#')

private val RESERVED = setOf(
    "def",
    "defn",
    "defn-",
    "def-",
    "defmacro",
    "defmacro-",
    "defstruct",
    "definterface",
    "defexception",
    "declare",
    "fn",
    "let",
    "if",
    "when",
    "do",
    "quote",
    "var",
    "throw",
    "try",
    "catch",
    "finally",
    "loop",
    "recur",
    "ns",
    "true",
    "false",
    "nil"
)