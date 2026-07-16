package org.phellang.completion.infrastructure

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.phellang.language.infrastructure.PhelIcons
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.references.PhpClassResolver
import org.phellang.registry.PhelCompletionPriority

/**
 * Completion for PHP classes brought into scope via `(:use ...)` in the file's
 * `ns` declaration. For each used class `Foo` we offer:
 *
 *  * `Foo`   — bare reference (catch targets, `(new Foo ...)`)
 *  * `Foo.`  — constructor shorthand, expands to `(php/new Foo ...)`
 *  * `Foo/`  — static call/member prefix; the user types the member next
 *  * `Foo/name` for each public method/constant/field on `Foo` (when the PHP
 *    plugin is available — silently no-ops otherwise).
 */
object PhelUsedClassCompletionHelper {
    @JvmStatic
    fun addUsedClassCompletions(result: CompletionResultSet, file: PhelFile) {
        val classes = PhelNamespaceUtils.extractUsedClasses(file)
        if (classes.isEmpty()) return

        val fqnByShort = PhelNamespaceUtils.buildUseFqnIndex(file)

        for (className in classes) {
            result.addElement(usedClassLookup(className))
            result.addElement(constructorLookup(className))
            result.addElement(staticPrefixLookup(className))

            val fqn = fqnByShort[className] ?: "\\$className"
            for (member in PhpClassResolver.listMembers(file.project, fqn)) {
                result.addElement(memberLookup(className, member))
            }
        }
    }

    private fun memberLookup(
        className: String,
        member: PhpClassResolver.PhpMemberInfo,
    ) = PrioritizedLookupElement.withPriority(
        LookupElementBuilder.create("$className/${member.name}")
            .withIcon(PhelIcons.FILE)
            .withTypeText(memberTypeText(member), true)
            .withTailText(memberTailText(member), true),
        // Concrete members beat the bare `Class/` prefix so they sort above it.
        PhelCompletionPriority.PHP_INTEROP.value + 0.1,
    )

    private fun memberTypeText(member: PhpClassResolver.PhpMemberInfo): String = when (member.kind) {
        PhpClassResolver.MemberKind.METHOD -> if (member.isStatic) "static method" else "method"
        PhpClassResolver.MemberKind.CONSTANT -> "constant"
        PhpClassResolver.MemberKind.FIELD -> if (member.isStatic) "static field" else "field"
    }

    private fun memberTailText(member: PhpClassResolver.PhpMemberInfo): String = when (member.kind) {
        PhpClassResolver.MemberKind.METHOD -> " — ${member.signature}"
        else -> ""
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
