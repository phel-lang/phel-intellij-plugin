package org.phellang.language.psi.references

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Resolves a Phel interop symbol to its underlying PHP class — or a specific member — so
 * go-to-definition jumps into the PHP source. This is the mapping half: turning a Phel symbol into
 * the PHP FQN and member name to look up. The reflective lookup itself lives in [PhpIndexBridge],
 * which also isolates the fact that the PHP plugin is an optional, reflectively-loaded dependency.
 */
object PhpClassResolver {
    /**
     * Member kinds surfaced from a PHP class. Private/protected members are excluded from completion
     * and resolution, since Phel can only call what is public from outside the class.
     */
    enum class MemberKind { METHOD, CONSTANT, FIELD }

    data class PhpMemberInfo(
        val name: String,
        val kind: MemberKind,
        val isStatic: Boolean,
        val signature: String,
        val element: PsiElement,
    )

    fun resolveAsPhpClass(symbol: PhelSymbol): List<PsiElement> {
        if (!PhpIndexBridge.isAvailable()) return emptyList()
        val fqn = computeTargetFqn(symbol) ?: return emptyList()
        return PhpIndexBridge.findClassesByFqn(symbol.project, fqn)
    }

    /**
     * Resolves to a specific member (`Foo/method`, `Foo/CONST`, `\Foo\Bar/m`) or to the constructor
     * when the symbol is the `(Class. ...)` head. Empty when the PHP plugin isn't available or the
     * member can't be found — callers should then try [resolveAsPhpClass] for the class-level fallback.
     */
    fun resolveAsPhpMember(symbol: PhelSymbol): List<PsiElement> {
        if (!PhpIndexBridge.isAvailable()) return emptyList()

        val text = symbol.text ?: return emptyList()
        val classFqn = computeTargetFqn(symbol) ?: return emptyList()
        val memberName = extractMemberName(text) ?: return emptyList()

        return PhpIndexBridge.findClassesByFqn(symbol.project, classFqn)
            .flatMap { PhpIndexBridge.findMemberInClass(it, memberName) }
    }

    /**
     * The public, statically reachable members of [classFqn] for completion, inherited members
     * included, deduplicated by kind+name. Empty when the PHP plugin isn't available.
     */
    fun listMembers(project: Project, classFqn: String): List<PhpMemberInfo> {
        if (!PhpIndexBridge.isAvailable()) return emptyList()

        val seen = mutableSetOf<String>()
        return PhpIndexBridge.findClassesByFqn(project, classFqn)
            .flatMap { PhpIndexBridge.collectMembers(it) }
            .filter { seen.add("${it.kind}:${it.name}") }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // FQN derivation (shared)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Computes the PHP FQN we want to look up for [symbol]'s text.
     *
     * * `\Foo\Bar`     -> `\Foo\Bar`
     * * `Foo` (in :use)-> the `:use` clause's full FQN
     * * `Foo.`         -> same as `Foo`
     * * `Foo/method`   -> same as `Foo` (class part of static member access)
     * * any other      -> null (let Phel-only resolution stay authoritative)
     */
    private fun computeTargetFqn(symbol: PhelSymbol): String? {
        val text = symbol.text ?: return null
        val file = symbol.containingFile as? PhelFile ?: return null

        // A class entry inside `(:use ...)` — resolve its full path to the PHP class.
        if (PhelNamespaceUtils.isUseClassSymbol(symbol)) return phpFqnFromUseEntry(text)

        if (text.startsWith("\\") && !text.contains("/")) return text

        if (!PhelInteropShorthands.isInteropShorthand(text, PhelNamespaceUtils.extractUsedClasses(file))) {
            return null
        }

        val rawQualifier = when {
            text.endsWith(".") -> text.dropLast(1)
            text.contains("/") -> text.substringBeforeLast('/')
            else -> text
        }
        if (rawQualifier.isEmpty()) return null

        if (rawQualifier.startsWith("\\")) return rawQualifier

        val useFqn = findUseFqn(file, rawQualifier)
        if (useFqn != null) return useFqn

        return "\\$rawQualifier"
    }

    /**
     * Normalises a `(:use ...)` class entry to its PHP fully-qualified name.
     *
     * Accepts the three spellings Phel allows and folds them to PHP's `\`-separated
     * absolute FQN:
     * * `Phel.Compiler.CompilerFacade`  -> `\Phel\Compiler\CompilerFacade` (modern dot form)
     * * `Phel\Compiler\CompilerFacade`  -> `\Phel\Compiler\CompilerFacade` (legacy form)
     * * `\Phel\Compiler\CompilerFacade` -> `\Phel\Compiler\CompilerFacade`
     * * `Countable`                     -> `\Countable`
     *
     * Returns null for blank input.
     */
    fun phpFqnFromUseEntry(text: String): String? {
        val body = text.trim().trimStart('\\').replace('.', '\\')
        if (body.isEmpty()) return null
        return "\\$body"
    }

    /**
     * For `Foo/method` returns `method`; for `Foo.` returns `__construct` (the
     * canonical PHP constructor name). Returns null when the text doesn't address
     * a specific member.
     */
    private fun extractMemberName(text: String): String? {
        if (text.endsWith(".") && text.length > 1) return PhpIndexBridge.CONSTRUCTOR_NAME
        if (!text.contains("/")) return null
        val tail = text.substringAfterLast('/')
        return tail.ifEmpty { null }
    }

    private fun findUseFqn(file: PhelFile, shortName: String): String? =
        PhelNamespaceUtils.buildUseFqnIndex(file)[shortName]
}
