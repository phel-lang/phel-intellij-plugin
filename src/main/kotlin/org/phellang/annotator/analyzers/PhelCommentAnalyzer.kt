package org.phellang.annotator.analyzers

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.phellang.language.psi.*
import java.util.regex.Pattern.compile

object PhelCommentAnalyzer {
    private val CONTAINER_TOKEN_PATTERN = compile("(#_|:[\\w-]+|[a-zA-Z][\\w-]*)")

    private const val FORM_COMMENT_MARKER = "#_"

    // Only `#(` opens an anonymous function (PhelHashFn); the deprecated `|(` is lexed as a
    // plain symbol and never produces one.
    private const val ANON_FN_MARKER = "#("

    private val HAS_FORM_COMMENT_KEY: Key<CachedValue<Boolean>> = Key.create("phel.hasFormComment")

    private val HAS_ANON_FN_KEY: Key<CachedValue<Boolean>> = Key.create("phel.hasAnonFn")

    fun isCommentedOutByFormComment(element: PsiElement): Boolean {
        // Fast path: a file with no `#_` marker anywhere can't comment out any form, so skip
        // the containing-form walk and text tokenization that otherwise run for every element.
        val file = element.containingFile ?: return false
        if (!fileHasFormComment(file)) return false

        val containingForm = findContainingForm(element) ?: return false

        return isInCommentedFormByText(containingForm)
    }

    private fun fileHasFormComment(file: PsiFile): Boolean {
        return CachedValuesManager.getCachedValue(file, HAS_FORM_COMMENT_KEY) {
            CachedValueProvider.Result.create(
                file.text.contains(FORM_COMMENT_MARKER),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    fun isInsideAnonFunction(element: PsiElement): Boolean {
        // Fast path: a file with no `#(` can't contain an anonymous function, so skip the
        // ancestor walk that otherwise runs for every annotated symbol/keyword. When no
        // containing file is available we fall through to the walk.
        val file = element.containingFile
        if (file != null && !fileHasAnonFunction(file)) return false

        var current = element.parent
        while (current != null) {
            if (current is PhelHashFn) {
                return true
            }
            current = current.parent
        }
        return false
    }

    private fun fileHasAnonFunction(file: PsiFile): Boolean {
        return CachedValuesManager.getCachedValue(file, HAS_ANON_FN_KEY) {
            CachedValueProvider.Result.create(
                file.text.contains(ANON_FN_MARKER),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    private fun isInCommentedFormByText(element: PsiElement): Boolean {
        val parent = element.parent ?: return false

        val containerText = parent.text
        if (containerText.isNullOrEmpty()) return false

        // Fast path for the overwhelmingly common case: a container without any `#_`
        // marker can't comment out anything, so skip the regex tokenization and offset
        // math that runs otherwise for every annotated element.
        if (!containerText.contains(FORM_COMMENT_MARKER)) return false

        return isFormCommentedInContainer(containerText, element, parent)
    }

    private fun isFormCommentedInContainer(
        containerText: String, targetElement: PsiElement, container: PsiElement
    ): Boolean {
        val tokens = parseContainerTokens(containerText)

        val targetOffset = calculateRelativeOffset(targetElement, container)
        if (targetOffset < 0) return false

        val targetTokenIndex = findTargetTokenIndex(tokens, targetOffset)
        if (targetTokenIndex == -1) return false

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
                    pendingComments++
                }

                TokenType.FORM -> {
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
