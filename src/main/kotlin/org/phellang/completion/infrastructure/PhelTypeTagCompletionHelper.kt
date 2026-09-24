package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PlainPrefixMatcher
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelTypeTags
import org.phellang.language.psi.files.PhelFile

/**
 * Completion after `^`: the names a type tag can take, and nothing else. A tag names a type, so the
 * functions, locals and project symbols offered everywhere else are all wrong here.
 *
 * Only the member being typed is completed: in `^?vec` the `?` stays, and in `^map|nu` only `nu` is
 * replaced, so nullable and union tags complete one member at a time.
 */
object PhelTypeTagCompletionHelper {

    fun addTypeTagCompletions(result: CompletionResultSet, file: PhelFile?) {
        val memberResult = result.withPrefixMatcher(PlainPrefixMatcher(memberPrefix(result.prefixMatcher.prefix)))

        for ((alias, backingClass) in PhelTypeTags.VALUE_TYPE_ALIASES) {
            memberResult.addElement(
                LookupElementBuilder.create(alias).withTypeText(backingClass.substringAfterLast('\\'), true)
            )
        }

        for (type in PhelTypeTags.PHP_TYPES) {
            memberResult.addElement(LookupElementBuilder.create(type).withTypeText("PHP type", true))
        }

        val usedClasses = file?.let(PhelNamespaceUtils::extractUsedClasses).orEmpty()
        for (className in usedClasses) {
            memberResult.addElement(LookupElementBuilder.create(className).withTypeText("class", true))
        }
    }

    /** The member under the caret: after the last `|` or `&`, without its `?`. */
    private fun memberPrefix(typed: String): String =
        typed.substringAfterLast('|').substringAfterLast('&').removePrefix("?")
}
