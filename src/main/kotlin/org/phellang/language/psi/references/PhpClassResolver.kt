package org.phellang.language.psi.references

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile
import java.lang.reflect.InvocationTargetException

/**
 * Reflection-based bridge to the JetBrains PHP plugin (PhpStorm / Ultimate with
 * the PHP plugin installed). Resolves a Phel interop symbol to its underlying
 * PHP class — or, where applicable, a specific class member — so go-to-definition
 * jumps straight into the PHP source.
 *
 * The PHP plugin is **not** a compile-time dependency — we never want to break
 * Phel support in IDEA Community / WebStorm / Rider just because PHP isn't on
 * the classpath. Instead we load `com.jetbrains.php.PhpIndex` reflectively on
 * first use; if absent (or anything else goes wrong) we return an empty list,
 * which is the same as "no PHP target found" and lets the existing Phel-only
 * resolution remain authoritative.
 */
object PhpClassResolver {

    private val LOG = Logger.getInstance(PhpClassResolver::class.java)

    /**
     * Member kinds we surface from a PHP class. We deliberately exclude private/
     * protected members from completion and resolution since Phel can only call
     * what's public from outside the class.
     */
    enum class MemberKind { METHOD, CONSTANT, FIELD }

    data class PhpMemberInfo(
        val name: String,
        val kind: MemberKind,
        val isStatic: Boolean,
        val signature: String,
        val element: PsiElement,
    )

    @Volatile
    private var phpIndexClass: Class<*>? = null

    @Volatile
    private var checkedForPhpPlugin: Boolean = false

    // ──────────────────────────────────────────────────────────────────────────
    // Class-level resolution (existing API; unchanged surface)
    // ──────────────────────────────────────────────────────────────────────────

    fun resolveAsPhpClass(symbol: PhelSymbol): List<PsiElement> {
        if (!checkedForPhpPlugin) loadPhpIndexClass()
        if (phpIndexClass == null) return emptyList()
        val fqn = computeTargetFqn(symbol) ?: return emptyList()
        return findClassesByFqn(symbol.project, fqn).mapNotNull { it as? PsiElement }
    }

    /**
     * Resolves to a specific member (`Foo/method`, `Foo/CONST`, `\Foo\Bar/m`) or to
     * the constructor when the symbol is the `(Class. ...)` head. Falls back to an
     * empty list when the PHP plugin isn't available or the member can't be found —
     * callers should then call [resolveAsPhpClass] for the class-level fallback.
     */
    fun resolveAsPhpMember(symbol: PhelSymbol): List<PsiElement> {
        if (!checkedForPhpPlugin) loadPhpIndexClass()
        if (phpIndexClass == null) return emptyList()

        val text = symbol.text ?: return emptyList()
        val classFqn = computeTargetFqn(symbol) ?: return emptyList()
        val memberName = extractMemberName(text) ?: return emptyList()
        val classes = findClassesByFqn(symbol.project, classFqn)
        if (classes.isEmpty()) return emptyList()

        return classes.flatMap { phpClass ->
            findMemberInClass(phpClass, memberName)
        }
    }

    /**
     * Enumerates the public, *statically reachable* members of [classFqn] for use
     * in completion. Inherited members are included. Returns an empty list when
     * the PHP plugin isn't available.
     */
    fun listMembers(project: Project, classFqn: String): List<PhpMemberInfo> {
        if (!checkedForPhpPlugin) loadPhpIndexClass()
        if (phpIndexClass == null) return emptyList()

        val classes = findClassesByFqn(project, classFqn)
        if (classes.isEmpty()) return emptyList()

        val seen = mutableSetOf<String>()
        val result = mutableListOf<PhpMemberInfo>()
        for (phpClass in classes) {
            for (info in collectMembers(phpClass)) {
                val key = "${info.kind}:${info.name}"
                if (seen.add(key)) result.add(info)
            }
        }
        return result
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
        if (text.endsWith(".") && text.length > 1) return CONSTRUCTOR_NAME
        if (!text.contains("/")) return null
        val tail = text.substringAfterLast('/')
        return tail.ifEmpty { null }
    }

    private fun findUseFqn(file: PhelFile, shortName: String): String? =
        PhelNamespaceUtils.buildUseFqnIndex(file)[shortName]

    // ──────────────────────────────────────────────────────────────────────────
    // PhpIndex reflection
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Reflection wraps whatever the callee threw in an [InvocationTargetException], so a
     * [ProcessCanceledException] coming out of a PHP stub-index query does *not* arrive here
     * as a PCE and is invisible to every PCE-aware catch upstream. Swallowing it would break
     * read-action cancellation (this runs under `PhelReference.multiResolve`). Unwrap and
     * rethrow anything the platform owns; everything else is a genuine "the PHP plugin's API
     * is not the shape we expect" and is handled by the caller.
     */
    internal fun rethrowIfPlatformControlFlow(t: Throwable) {
        val cause = (t as? InvocationTargetException)?.targetException ?: t
        if (cause is ProcessCanceledException || cause is IndexNotReadyException || cause is Error) {
            throw cause
        }
    }

    private fun findClassesByFqn(project: Project, fqn: String): List<Any> {
        val phpIndex = phpIndexClass ?: return emptyList()
        return try {
            val instance = phpIndex.getMethod("getInstance", Project::class.java).invoke(null, project)
            // `getAnyByFQN` resolves classes, interfaces, traits and enums alike — using
            // `getClassesByFQN` would miss interfaces such as `PersistentMapInterface`.
            val getByFqn = phpIndex.getMethod("getAnyByFQN", String::class.java)
            @Suppress("UNCHECKED_CAST")
            val phpTypes = getByFqn.invoke(instance, fqn) as? Collection<Any> ?: return emptyList()
            phpTypes.toList()
        } catch (t: Throwable) {
            rethrowIfPlatformControlFlow(t)
            LOG.warn("PHP type lookup failed for '$fqn'", t)
            emptyList()
        }
    }

    private fun findMemberInClass(phpClass: Any, memberName: String): List<PsiElement> {
        val cls = phpClass::class.java
        val results = mutableListOf<PsiElement>()

        // Methods (including the constructor when memberName == "__construct").
        tryInvokeCollection(cls, phpClass, "getMethods")?.let { methods ->
            for (method in methods) {
                if (readStringProperty(method, "getName") == memberName) {
                    (method as? PsiElement)?.let { results.add(it) }
                }
            }
        }

        // Fields (which in the PHP plugin model also covers constants).
        tryInvokeCollection(cls, phpClass, "getFields")?.let { fields ->
            for (field in fields) {
                if (readStringProperty(field, "getName") == memberName) {
                    (field as? PsiElement)?.let { results.add(it) }
                }
            }
        }

        return results
    }

    private fun collectMembers(phpClass: Any): List<PhpMemberInfo> {
        val cls = phpClass::class.java
        val out = mutableListOf<PhpMemberInfo>()

        tryInvokeCollection(cls, phpClass, "getMethods")?.forEach { method ->
            val name = readStringProperty(method, "getName") ?: return@forEach
            if (name == CONSTRUCTOR_NAME) return@forEach // surfaced via `Class.` shorthand, not `Class/`
            if (!isAccessible(method)) return@forEach
            val element = method as? PsiElement ?: return@forEach
            val isStatic = readBoolProperty(method, "isStatic") ?: false
            val signature = methodSignature(method, name)
            out.add(PhpMemberInfo(name, MemberKind.METHOD, isStatic, signature, element))
        }

        tryInvokeCollection(cls, phpClass, "getFields")?.forEach { field ->
            val name = readStringProperty(field, "getName") ?: return@forEach
            if (!isAccessible(field)) return@forEach
            val element = field as? PsiElement ?: return@forEach
            val isConstant = readBoolProperty(field, "isConstant") ?: false
            val isStatic = readBoolProperty(field, "isStatic") ?: isConstant
            val kind = if (isConstant) MemberKind.CONSTANT else MemberKind.FIELD
            out.add(PhpMemberInfo(name, kind, isStatic, name, element))
        }

        return out
    }

    private fun methodSignature(method: Any?, name: String): String {
        if (method == null) return "$name(...)"
        return try {
            val getParameters = method.javaClass.getMethod("getParameters")
            val params = getParameters.invoke(method) as? Array<*> ?: return "$name(...)"
            val rendered = params.joinToString(", ") { p ->
                readStringProperty(p, "getName")?.let { "$$it" } ?: "?"
            }
            "$name($rendered)"
        } catch (t: Throwable) {
            rethrowIfPlatformControlFlow(t)
            LOG.warn("Could not read parameters of PHP method '$name'", t)
            "$name(...)"
        }
    }

    private fun isAccessible(member: Any?): Boolean {
        if (member == null) return false
        // Access object lives on Method/Field via `getModifier().getAccess()`.
        // Anything that isn't explicitly private/protected is considered callable
        // from Phel.
        return try {
            val getModifier = member.javaClass.getMethod("getModifier")
            val modifier = getModifier.invoke(member) ?: return true
            val getAccess = modifier.javaClass.getMethod("getAccess")
            val access = getAccess.invoke(modifier) ?: return true
            val name = (access.javaClass.getMethod("name").invoke(access) as? String)?.uppercase()
            name == null || name == "PUBLIC"
        } catch (t: Throwable) {
            rethrowIfPlatformControlFlow(t)
            // Fail open: if the PHP plugin's modifier API ever changes shape, offering a
            // member we can't classify beats hiding every member of the class.
            LOG.warn("Could not read PHP member accessibility; treating it as public", t)
            true
        }
    }

    private fun tryInvokeCollection(cls: Class<*>, target: Any, name: String): Collection<*>? {
        return try {
            val m = cls.methods.firstOrNull { it.name == name && it.parameterCount == 0 } ?: return null
            m.invoke(target) as? Collection<*>
        } catch (t: Throwable) {
            rethrowIfPlatformControlFlow(t)
            LOG.warn("Reflective call to PHP '$name()' failed", t)
            null
        }
    }

    private fun readStringProperty(target: Any?, methodName: String): String? {
        if (target == null) return null
        return try {
            target.javaClass.getMethod(methodName).invoke(target) as? String
        } catch (t: Throwable) {
            rethrowIfPlatformControlFlow(t)
            LOG.warn("Reflective read of PHP '$methodName()' failed", t)
            null
        }
    }

    private fun readBoolProperty(target: Any?, methodName: String): Boolean? {
        if (target == null) return null
        return try {
            target.javaClass.getMethod(methodName).invoke(target) as? Boolean
        } catch (t: Throwable) {
            rethrowIfPlatformControlFlow(t)
            LOG.warn("Reflective read of PHP '$methodName()' failed", t)
            null
        }
    }

    private fun loadPhpIndexClass() {
        synchronized(this) {
            if (checkedForPhpPlugin) return
            checkedForPhpPlugin = true
            phpIndexClass = try {
                Class.forName("com.jetbrains.php.PhpIndex", false, javaClass.classLoader)
            } catch (_: ClassNotFoundException) {
                null
            } catch (t: Throwable) {
                LOG.debug("Unexpected error loading PhpIndex", t)
                null
            }
        }
    }

    private const val CONSTRUCTOR_NAME = "__construct"
}
