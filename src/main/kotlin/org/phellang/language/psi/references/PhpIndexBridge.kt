package org.phellang.language.psi.references

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.phellang.language.psi.references.PhpClassResolver.MemberKind
import org.phellang.language.psi.references.PhpClassResolver.PhpMemberInfo
import java.lang.reflect.InvocationTargetException

/**
 * The reflective bridge to the JetBrains PHP plugin. Everything that touches `com.jetbrains.php.*`
 * lives here, so the resolver above can be read as plain Phel→PHP mapping.
 *
 * The PHP plugin is not a compile-time dependency — Phel support must not break in IDEA Community
 * just because PHP isn't on the classpath — so `PhpIndex` and every method on the objects it returns
 * are reached reflectively. When the plugin is absent, or its API isn't the shape we expect, every
 * call degrades to "nothing found" and lets the Phel-only resolution stay authoritative.
 */
internal object PhpIndexBridge {

    private val LOG = Logger.getInstance(PhpIndexBridge::class.java)

    const val CONSTRUCTOR_NAME = "__construct"

    @Volatile
    private var phpIndexClass: Class<*>? = null

    @Volatile
    private var checkedForPhpPlugin: Boolean = false

    /** False when the PHP plugin isn't installed — callers should then return no PHP targets. */
    fun isAvailable(): Boolean {
        if (!checkedForPhpPlugin) loadPhpIndexClass()
        return phpIndexClass != null
    }

    /** The PHP declarations for [fqn] — classes, interfaces, traits and enums alike. */
    fun findClassesByFqn(project: Project, fqn: String): List<Any> {
        val phpIndex = phpIndexClass ?: return emptyList()
        return try {
            val instance = phpIndex.getMethod("getInstance", Project::class.java).invoke(null, project)
            // `getAnyByFQN` covers interfaces such as `PersistentMapInterface`, which
            // `getClassesByFQN` would miss.
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

    /** The declaration(s) of [memberName] on [phpClass] — a method or a field/constant. */
    fun findMemberInClass(phpClass: Any, memberName: String): List<PsiElement> {
        val results = mutableListOf<PsiElement>()

        // Methods (including the constructor when memberName == "__construct").
        membersOf(phpClass, "getMethods")
            .filter { readStringProperty(it, "getName") == memberName }
            .forEach { (it as? PsiElement)?.let(results::add) }

        // Fields (which in the PHP plugin model also cover constants).
        membersOf(phpClass, "getFields")
            .filter { readStringProperty(it, "getName") == memberName }
            .forEach { (it as? PsiElement)?.let(results::add) }

        return results
    }

    /** The public members of [phpClass] surfaced for completion — private/protected are excluded. */
    fun collectMembers(phpClass: Any): List<PhpMemberInfo> {
        val out = mutableListOf<PhpMemberInfo>()

        membersOf(phpClass, "getMethods").forEach { method ->
            val name = readStringProperty(method, "getName") ?: return@forEach
            if (name == CONSTRUCTOR_NAME) return@forEach // surfaced via `Class.`, not `Class/`
            if (!isAccessible(method)) return@forEach
            val element = method as? PsiElement ?: return@forEach
            val isStatic = readBoolProperty(method, "isStatic") ?: false
            out += PhpMemberInfo(name, MemberKind.METHOD, isStatic, methodSignature(method, name), element)
        }

        membersOf(phpClass, "getFields").forEach { field ->
            val name = readStringProperty(field, "getName") ?: return@forEach
            if (!isAccessible(field)) return@forEach
            val element = field as? PsiElement ?: return@forEach
            val isConstant = readBoolProperty(field, "isConstant") ?: false
            val isStatic = readBoolProperty(field, "isStatic") ?: isConstant
            val kind = if (isConstant) MemberKind.CONSTANT else MemberKind.FIELD
            out += PhpMemberInfo(name, kind, isStatic, name, element)
        }

        return out
    }

    /**
     * Reflection wraps whatever the callee threw in an [InvocationTargetException], so a
     * [ProcessCanceledException] out of a PHP stub-index query does *not* arrive as a PCE and is
     * invisible to every PCE-aware catch upstream. Swallowing it would break read-action
     * cancellation (this runs under `PhelReference.multiResolve`). Rethrow anything the platform
     * owns; everything else is a genuine "the PHP plugin's API is not the shape we expect".
     */
    fun rethrowIfPlatformControlFlow(t: Throwable) {
        val cause = (t as? InvocationTargetException)?.targetException ?: t
        if (cause is ProcessCanceledException || cause is IndexNotReadyException || cause is Error) {
            throw cause
        }
    }

    private fun membersOf(phpClass: Any, accessor: String): Collection<*> =
        tryInvokeCollection(phpClass::class.java, phpClass, accessor) ?: emptyList<Any>()

    private fun methodSignature(method: Any?, name: String): String {
        if (method == null) return "$name(...)"
        return try {
            val params = method.javaClass.getMethod("getParameters").invoke(method) as? Array<*>
                ?: return "$name(...)"
            val rendered = params.joinToString(", ") { p -> readStringProperty(p, "getName")?.let { "$$it" } ?: "?" }
            "$name($rendered)"
        } catch (t: Throwable) {
            rethrowIfPlatformControlFlow(t)
            LOG.warn("Could not read parameters of PHP method '$name'", t)
            "$name(...)"
        }
    }

    /** Anything not explicitly private/protected is callable from Phel. */
    private fun isAccessible(member: Any?): Boolean {
        if (member == null) return false
        return try {
            val modifier = member.javaClass.getMethod("getModifier").invoke(member) ?: return true
            val access = modifier.javaClass.getMethod("getAccess").invoke(modifier) ?: return true
            val name = (access.javaClass.getMethod("name").invoke(access) as? String)?.uppercase()
            name == null || name == "PUBLIC"
        } catch (t: Throwable) {
            rethrowIfPlatformControlFlow(t)
            // Fail open: if the modifier API ever changes shape, offering a member we can't classify
            // beats hiding every member of the class.
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

    private fun readStringProperty(target: Any?, methodName: String): String? =
        readProperty(target, methodName) as? String

    private fun readBoolProperty(target: Any?, methodName: String): Boolean? =
        readProperty(target, methodName) as? Boolean

    private fun readProperty(target: Any?, methodName: String): Any? {
        if (target == null) return null
        return try {
            target.javaClass.getMethod(methodName).invoke(target)
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
}
