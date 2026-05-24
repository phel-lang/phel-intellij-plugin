package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.phellang.language.infrastructure.PhelIcons
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile

/**
 * Completion for PHP classes brought into scope via `(:use ...)` in the file's
 * `ns` declaration. For each used class `Foo` we offer three lookups that mirror
 * the three interop shorthands users can write:
 *
 *  * `Foo`   — bare reference (catch targets, `(new Foo ...)`)
 *  * `Foo.`  — constructor shorthand, expands to `(php/new Foo ...)`
 *  * `Foo/`  — static call/member prefix; the user types the member next
 */
object PhelUsedClassCompletionHelper {

    @JvmStatic
    fun addUsedClassCompletions(result: CompletionResultSet, file: PhelFile) {
        val classes = PhelNamespaceUtils.extractUsedClasses(file)
        if (classes.isEmpty()) return

        for (className in classes) {
            result.addElement(usedClassLookup(className))
            result.addElement(constructorLookup(className))
            result.addElement(staticPrefixLookup(className))
        }
    }

    private fun usedClassLookup(className: String) = PrioritizedLookupElement.withPriority(
        LookupElementBuilder.create(className)
            .withIcon(PhelIcons.FILE)
            .withTypeText("PHP class (use)", true)
            .withTailText(" — bare reference", true),
        PhelCompletionPriority.PHP_INTEROP.value
    )

    private fun constructorLookup(className: String) = PrioritizedLookupElement.withPriority(
        LookupElementBuilder.create("$className.")
            .withIcon(PhelIcons.FILE)
            .withTypeText("(php/new $className ...)", true)
            .withTailText(" — constructor", true),
        PhelCompletionPriority.PHP_INTEROP.value
    )

    private fun staticPrefixLookup(className: String) = PrioritizedLookupElement.withPriority(
        LookupElementBuilder.create("$className/")
            .withIcon(PhelIcons.FILE)
            .withTypeText("(php/:: $className ...)", true)
            .withTailText(" — static call/member", true),
        PhelCompletionPriority.PHP_INTEROP.value
    )
}
