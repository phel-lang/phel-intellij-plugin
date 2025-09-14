package org.phellang.completion

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol

/**
 * Provides intelligent parameter hints and function signature completions
 */
object PhelParameterHintProvider {
    private val HINT_ICON = AllIcons.Actions.IntentionBulb

    /**
     * Add parameter hint completions based on current context
     */
    @JvmStatic
    fun addParameterHints(result: CompletionResultSet, position: PsiElement) {
        // Check if we're inside a function call
        val containingList = PsiTreeUtil.getParentOfType(position, PhelList::class.java)
        if (containingList != null) {
            val firstElement = containingList.firstChild
            if (firstElement is PhelSymbol) {
                val functionName = firstElement.text
                addFunctionSignatureHint(result, functionName)
            }
        }
    }

    /**
     * Add function signature hint for known functions
     */
    private fun addFunctionSignatureHint(result: CompletionResultSet, functionName: String) {
        val signature = getFunctionSignature(functionName)
        if (signature != null) {
            result.addElement(
                LookupElementBuilder.create("($functionName )").withIcon(HINT_ICON).withTypeText(signature)
                    .withTailText(" - function signature", true).withBoldness(true)
                    .withPresentableText("$functionName $signature")
            )
        }
    }

    /**
     * Get function signature for known core functions
     */
    private fun getFunctionSignature(functionName: String): String? {
        when (functionName) {
            "if" -> return "test then else?"
            "when" -> return "test & body"
            "let" -> return "[bindings*] expr*"
            "loop" -> return "[bindings*] expr*"
            "for" -> return "seq-exprs body"
            "defn" -> return "name [params*] body*"
            "fn" -> return "[params*] body*"

            "map" -> return "f coll"
            "filter" -> return "pred coll"
            "reduce" -> return "f coll"
            "get" -> return "coll key default?"
            "put" -> return "coll key val"
            "count" -> return "coll"
            "first" -> return "coll"
            "rest" -> return "coll"
            "cons" -> return "item coll"
            "conj" -> return "coll & items"

            "str" -> return "& args"
            "print" -> return "& xs"
            "println" -> return "& xs"
            "format" -> return "fmt & args"

            "+" -> return "& args"
            "-" -> return "& args"
            "*" -> return "& args"
            "/" -> return "& args"
            "=" -> return "& args"
            "<" -> return "& args"
            ">" -> return "& args"
            "<=" -> return "& args"
            ">=" -> return "& args"

            "nil?" -> return "x"
            "empty?" -> return "coll"
            "even?" -> return "n"
            "odd?" -> return "n"
            "pos?" -> return "n"
            "neg?" -> return "n"
            "zero?" -> return "n"

            "apply" -> return "f args"
            "comp" -> return "& fns"
            "partial" -> return "f & args"
            "constantly" -> return "x"
            "identity" -> return "x"

            "->" -> return "x & forms"
            "->>" -> return "x & forms"
            "as->" -> return "expr name & forms"

            "and" -> return "& forms"
            "or" -> return "& forms"
            "not" -> return "x"

            else -> return null
        }
    }

    /**
     * Add parameter position hints when inside a function call
     */
    @JvmStatic
    fun addPositionHints(result: CompletionResultSet, position: PsiElement) {
        val containingList = PsiTreeUtil.getParentOfType(position, PhelList::class.java) ?: return

        val firstElement = containingList.firstChild
        if (firstElement !is PhelSymbol) return

        val functionName = firstElement.text

        // Calculate which parameter position we're at
        val parameterIndex = getParameterIndex(containingList, position)
        if (parameterIndex >= 0) {
            val positionHint = getParameterPositionHint(functionName, parameterIndex)
            if (positionHint != null) {
                result.addElement(
                    LookupElementBuilder.create("").withIcon(HINT_ICON)
                        .withTypeText("Parameter " + (parameterIndex + 1) + ": " + positionHint)
                        .withTailText(" - current parameter", true).withPresentableText("→ $positionHint")
                )
            }
        }
    }

    /**
     * Calculate the parameter index within a function call
     */
    private fun getParameterIndex(functionCall: PhelList, position: PsiElement): Int {
        val children: Array<PsiElement> = functionCall.children
        val index = -1

        for (i in children.indices) {
            if (PsiTreeUtil.isAncestor(children[i], position, false)) {
                return i - 1 // Subtract 1 because first child is function name
            }
        }

        return index
    }

    /**
     * Get parameter hint for specific position in known functions
     */
    private fun getParameterPositionHint(functionName: String, parameterIndex: Int): String? {
        when (functionName) {
            "get" -> return when (parameterIndex) {
                0 -> "collection"
                1 -> "key"
                2 -> "default-value"
                else -> null
            }

            "put" -> return when (parameterIndex) {
                0 -> "collection"
                1 -> "key"
                2 -> "value"
                else -> null
            }

            "map" -> return when (parameterIndex) {
                0 -> "function"
                1 -> "collection"
                else -> null
            }

            "filter" -> return when (parameterIndex) {
                0 -> "predicate"
                1 -> "collection"
                else -> null
            }

            "reduce" -> return when (parameterIndex) {
                0 -> "function"
                1 -> "collection"
                2 -> "initial-value"
                else -> null
            }

            "let" -> return when (parameterIndex) {
                0 -> "bindings-vector"
                else -> "body-expression-$parameterIndex"
            }

            "if" -> return when (parameterIndex) {
                0 -> "test-condition"
                1 -> "then-expression"
                2 -> "else-expression"
                else -> null
            }

            "defn" -> return when (parameterIndex) {
                0 -> "function-name"
                1 -> "doc-string or parameters"
                2 -> "parameters or body"
                else -> "body-expression-" + (parameterIndex - 2)
            }

            else -> return null
        }
    }
}
