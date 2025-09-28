package org.phellang.annotator.analyzers

import com.intellij.psi.PsiElement
import org.phellang.language.psi.*
import java.util.regex.Pattern.compile

object PhelCommentAnalyzer {

    private val CONTAINER_TOKEN_PATTERN = compile("(#_|:[\\w-]+|[a-zA-Z][\\w-]*)")

    private const val FORM_COMMENT_MARKER = "#_"

    fun isCommentedOutByFormComment(element: PsiElement): Boolean {
        val containingForm = findContainingForm(element) ?: return false

        return isInCommentedFormByText(containingForm)
    }

    fun isInsideShortFunction(element: PsiElement): Boolean {
        var current = element.parent
        while (current != null) {
            if (current is PhelShortFn) {
                return true
            }
            current = current.parent
        }
        return false
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

        // Calculate the relative position of our target element within the container
        val targetOffset = calculateRelativeOffset(targetElement, container)
        if (targetOffset < 0) return false

        // Find which token our target element corresponds to
        val targetTokenIndex = findTargetTokenIndex(tokens, targetOffset)
        if (targetTokenIndex == -1) return false

        // Process tokens sequentially to determine which forms are commented
        return isTokenCommented(tokens, targetTokenIndex)
    }

    private fun calculateRelativeOffset(targetElement: PsiElement, container: PsiElement): Int {
        val targetOffset = targetElement.textOffset
        val containerOffset = container.textOffset

        return if (targetOffset >= containerOffset) {
            targetOffset - containerOffset
        } else {
            -1 // Invalid offset
        }
    }

    private fun findTargetTokenIndex(tokens: List<Token>, targetOffset: Int): Int {
        for (i in tokens.indices) {
            val token = tokens[i]
            if (token.type == TokenType.FORM) {
                // Check if this form overlaps with our target element
                if (token.start <= targetOffset && targetOffset < token.end) {
                    return i
                }
            }
        }
        return -1
    }

    private fun isTokenCommented(tokens: List<Token>, targetTokenIndex: Int): Boolean {
        val commentedTokens = mutableSetOf<Int>()
        var pendingComments = 0

        for (i in tokens.indices) {
            val token = tokens[i]
            when (token.type) {
                TokenType.COMMENT -> {
                    // Each #_ adds one pending comment
                    pendingComments++
                }

                TokenType.FORM -> {
                    // If we have pending comments, this form gets commented
                    if (pendingComments > 0) {
                        commentedTokens.add(i)
                        pendingComments--
                    }
                }
            }
        }

        return commentedTokens.contains(targetTokenIndex)
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

    private fun parseContainerTokens(containerText: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val matcher = CONTAINER_TOKEN_PATTERN.matcher(containerText)

        while (matcher.find()) {
            val text = matcher.group()
            val type = if (text == FORM_COMMENT_MARKER) TokenType.COMMENT else TokenType.FORM
            tokens.add(Token(type, matcher.start(), matcher.end(), text))
        }

        return tokens
    }
}
