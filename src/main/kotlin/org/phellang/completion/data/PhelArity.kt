package org.phellang.completion.data

import org.phellang.completion.data.PhelArity.Companion.parseAll

data class PhelArity(
    val params: List<String>,
    val variadic: Boolean,
) {
    val fixedCount: Int get() = if (variadic) params.size - 1 else params.size

    companion object {
        /**
         * Parses a signature string like "(name a b)" or "(name a b & rest)".
         * For multi-arity (newline-separated), call [parseAll].
         */
        fun parseSignature(signature: String): PhelArity? {
            val trimmed = signature.trim()
            if (!trimmed.startsWith("(") || !trimmed.endsWith(")")) return null
            val inner = trimmed.substring(1, trimmed.length - 1).trim()
            if (inner.isEmpty()) return null

            val tokens = tokenize(inner)
            if (tokens.isEmpty()) return null

            val paramTokens = tokens.drop(1) // first token is the function name

            val ampIndex = paramTokens.indexOf("&")
            val lastToken = paramTokens.lastOrNull()
            return when {
                ampIndex >= 0 -> {
                    val before = paramTokens.subList(0, ampIndex)
                    val rest = paramTokens.subList(ampIndex + 1, paramTokens.size)
                    val restName = rest.firstOrNull() ?: "rest"
                    PhelArity(params = before + restName, variadic = true)
                }

                // Star-suffix convention (e.g. "(apply f expr*)") -- the registry's other
                // way of spelling "zero or more trailing args" alongside `& rest`. Without
                // this, variadic stdlib fns like `apply` parse as fixed-arity and a call
                // with extra args is wrongly flagged.
                lastToken != null && lastToken.length > 1 && lastToken.endsWith("*") -> {
                    val before = paramTokens.dropLast(1)
                    val restName = lastToken.dropLast(1)
                    PhelArity(params = before + restName, variadic = true)
                }

                else -> PhelArity(params = paramTokens, variadic = false)
            }
        }

        fun parseAll(signature: String): List<PhelArity> =
            signature.lineSequence()
                .mapNotNull { parseSignature(it) }
                .toList()

        /**
         * Splits the parameter region into top-level tokens. Vectors and maps used for
         * destructuring count as a single token; their inner text is preserved literally.
         */
        private fun tokenize(text: String): List<String> {
            val tokens = mutableListOf<String>()
            val current = StringBuilder()
            var depth = 0
            for (c in text) {
                when {
                    c == '[' || c == '{' || c == '(' -> {
                        depth++
                        current.append(c)
                    }

                    c == ']' || c == '}' || c == ')' -> {
                        depth--
                        current.append(c)
                    }

                    c.isWhitespace() && depth == 0 -> {
                        if (current.isNotEmpty()) {
                            tokens.add(current.toString())
                            current.clear()
                        }
                    }

                    else -> current.append(c)
                }
            }
            if (current.isNotEmpty()) tokens.add(current.toString())
            return tokens
        }
    }
}

fun List<PhelArity>.accepts(argCount: Int): Boolean {
    if (isEmpty()) return true // unknown arity -- don't flag
    for (arity in this) {
        if (arity.variadic) {
            if (argCount >= arity.fixedCount) return true
        } else {
            if (argCount == arity.params.size) return true
        }
    }
    return false
}

fun List<PhelArity>.describe(): String {
    if (isEmpty()) return "?"
    return joinToString(" or ") { arity ->
        if (arity.variadic) "${arity.fixedCount}+" else "${arity.params.size}"
    }
}

/** Returns the arity that best matches the given argument count, or null when none does. */
fun List<PhelArity>.selectFor(argCount: Int): PhelArity? {
    // Prefer exact-fixed match first.
    val exact = firstOrNull { !it.variadic && it.params.size == argCount }
    if (exact != null) return exact
    return firstOrNull { it.variadic && argCount >= it.fixedCount }
}
