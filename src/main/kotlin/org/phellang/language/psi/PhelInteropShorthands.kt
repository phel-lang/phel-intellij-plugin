package org.phellang.language.psi

import org.phellang.language.psi.files.PhelFile

/**
 * Recogniser for Phel's PHP interop shorthand surface forms. These are the terse
 * alternatives to `(php/new ...)`, `(php/-> ...)`, `(php/:: ...)` documented at
 * https://phel-lang.org/documentation/php-interop/.
 *
 * | Shorthand                 | Expands to                         |
 * |---------------------------|------------------------------------|
 * | `(ClassName. args)`       | `(php/new ClassName args)`         |
 * | `(new ClassName args)`    | `(php/new ClassName args)`         |
 * | `(.method obj args)`      | `(php/-> obj (method args))`       |
 * | `(.-field obj)`           | `(php/-> obj field)`               |
 * | `(ClassName/method args)` | `(php/:: ClassName (method args))` |
 * | `ClassName/MEMBER`        | `(php/:: ClassName MEMBER)`        |
 *
 * The detector here works purely on a symbol's source text. We treat any qualifier
 * that (a) is `\`-prefixed, (b) starts with an ASCII uppercase letter, or (c) was
 * brought into scope via the file's `(:use ...)` clause as a PHP class — Phel
 * namespaces are conventionally lowercase + dot/backslash separated, so this
 * heuristic doesn't collide with real namespaces in practice.
 */
object PhelInteropShorthands {

    /** `new` is the long-form constructor keyword (sister to `Class.`). */
    private const val NEW_KEYWORD = "new"

    fun isInteropClassName(text: String): Boolean {
        if (text.isEmpty()) return false
        if (text.startsWith("\\")) return true
        return text[0] in 'A'..'Z'
    }

    /**
     * Returns true when [text] is one of the lexical shorthands listed above.
     * [usedClasses] should be the file's `(:use ...)` short-name set (see
     * [PhelNamespaceUtils.extractUsedClasses]); pass an empty set to rely on the
     * uppercase-first / backslash-prefix heuristic alone.
     */
    fun isInteropShorthand(text: String, usedClasses: Set<String> = emptySet()): Boolean {
        if (text.isEmpty()) return false

        // (.-field obj)
        if (text.length > 2 && text.startsWith(".-")) return true

        // (.method obj args) — `.` and `..` are not shorthands
        if (text.length > 1 && text[0] == '.' && text[1] != '.' && text[1] != '-') return true

        // (ClassName. args) — trailing dot constructor
        if (text.length > 1 && text.endsWith(".")) {
            val prefix = text.dropLast(1)
            if (isInteropClassName(prefix) || prefix in usedClasses) return true
        }

        // (new ClassName args)
        if (text == NEW_KEYWORD) return true

        // ClassName/method or ClassName/CONST
        if (text.contains("/") && !text.startsWith("/") && !text.endsWith("/")) {
            val qualifier = text.substringBeforeLast('/')
            if (isInteropClassName(qualifier)) return true
            val shortQualifier = qualifier.trimStart('\\').substringAfterLast('\\')
            if (shortQualifier in usedClasses) return true
        }

        // \Foo or \Foo\Bar with no `/` — bare PHP class reference (e.g., catch target)
        if (text.startsWith("\\") && !text.contains("/")) return true

        return false
    }

    /** Convenience overload that pulls the `(:use ...)` set from the symbol's file. */
    fun isInteropShorthand(symbol: PhelSymbol): Boolean {
        val text = symbol.text ?: return false
        val file = symbol.containingFile as? PhelFile
        val usedClasses = if (file != null) PhelNamespaceUtils.extractUsedClasses(file) else emptySet()
        return isInteropShorthand(text, usedClasses)
    }
}
