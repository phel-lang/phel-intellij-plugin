package org.phellang.annotator.analyzers

import com.intellij.psi.PsiElement
import org.phellang.language.psi.*

object PhelCommentAnalyzer {

    fun isCommentedOutByFormComment(element: PsiElement): Boolean {
        val containingForm = findContainingForm(element) ?: return false

        return isInCommentedFormByText(containingForm)
    }

    private fun isInCommentedFormByText(element: PsiElement): Boolean {
        val parent = element.parent ?: return false

        val containerText = parent.text
        if (containerText.isNullOrEmpty()) return false

        return isFormCommentedInContainer(containerText, element, parent)
    }

    private fun isFormCommentedInContainer(
        containerText: String, targetElement: PsiElement, container: PsiElement
    ): Boolean {
        // Find all forms and #_ tokens in order
        val tokens = parseContainerTokens(containerText)

        // Find the position of our target element
        val targetOffset = targetElement.textOffset - container.textOffset

        // Find which token our target element corresponds to
        var targetTokenIndex = -1

        for (i in tokens.indices) {
            val token = tokens[i]
            if (token.type == TokenType.FORM) {
                // Check if this form overlaps with our target element
                if (token.start <= targetOffset && targetOffset < token.end) {
                    targetTokenIndex = i
                    break
                }
            }
        }

        if (targetTokenIndex == -1) return false

        // Process tokens sequentially to determine which forms are commented
        val commentedTokens = mutableSetOf<Int>()
        var pendingComments = 0

        for (i in tokens.indices) {
            val token = tokens[i]
            if (token.type == TokenType.COMMENT) {
                // Each #_ adds one pending comment
                pendingComments++
            } else if (token.type == TokenType.FORM) {
                // If we have pending comments, this form gets commented
                if (pendingComments > 0) {
                    commentedTokens.add(i)
                    pendingComments--
                }
            }
        }

        return commentedTokens.contains(targetTokenIndex)
    }

    fun isInsideShortFunction(element: PsiElement): Boolean {
        // Check if this element is inside a short function |(...)
        var current = element.parent
        while (current != null) {
            if (current is PhelShortFn) {
                return true
            }
            current = current.parent
        }
        return false
    }

    fun isInsideSet(element: PsiElement): Boolean {
        // Check if this element is inside a set
        var current = element.parent
        while (current != null) {
            if (current is PhelSet) {
                return true
            }
            current = current.parent
        }
        return false
    }

    private fun findContainingForm(element: PsiElement): PhelForm? {
        // Walk up the PSI tree to find the containing form
        var current = element
        while (current.parent != null) {
            current = current.parent
            if (current is PhelForm) {
                return current
            }
        }
        return null
    }

    private data class Token(val type: TokenType, val start: Int, val end: Int, val text: String)
    private enum class TokenType { COMMENT, FORM }

    private fun parseContainerTokens(containerText: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val pattern = java.util.regex.Pattern.compile("(#_|:[\\w-]+|[a-zA-Z][\\w-]*)")
        val matcher = pattern.matcher(containerText)

        while (matcher.find()) {
            val text = matcher.group()
            val type = if (text == "#_") TokenType.COMMENT else TokenType.FORM
            tokens.add(Token(type, matcher.start(), matcher.end(), text))
        }

        return tokens
    }
}
