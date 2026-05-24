package org.phellang.language.psi.references

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.files.PhelFile

/**
 * Reflection-based bridge to the JetBrains PHP plugin (PhpStorm / Ultimate with
 * the PHP plugin installed). Resolves a Phel interop symbol to its underlying
 * PHP class so go-to-definition jumps straight into the class source.
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
     * Memoised reflective handle to `PhpIndex`. `NotLoaded` means we tried and
     * couldn't find the class (PHP plugin missing); `null` means we haven't
     * tried yet.
     */
    @Volatile
    private var phpIndexClass: Class<*>? = null

    @Volatile
    private var checkedForPhpPlugin: Boolean = false

    fun resolveAsPhpClass(symbol: PhelSymbol): List<PsiElement> {
        if (!checkedForPhpPlugin) loadPhpIndexClass()
        val phpIndex = phpIndexClass ?: return emptyList()

        val text = symbol.text ?: return emptyList()
        val fqn = resolveTargetFqn(symbol, text) ?: return emptyList()

        return try {
            val instanceMethod = phpIndex.getMethod("getInstance", Project::class.java)
            val instance = instanceMethod.invoke(null, symbol.project)

            val getByFqn = phpIndex.getMethod("getClassesByFQN", String::class.java)
            @Suppress("UNCHECKED_CAST")
            val classes = getByFqn.invoke(instance, fqn) as? Collection<Any> ?: return emptyList()
            classes.mapNotNull { it as? PsiElement }
        } catch (t: Throwable) {
            LOG.debug("PHP class resolution failed for '$fqn'", t)
            emptyList()
        }
    }

    /**
     * Computes the PHP FQN we want to look up for [text] in [symbol]'s file.
     *
     * * `\Foo\Bar`     -> `\Foo\Bar`
     * * `Foo` (in :use)-> the `:use` clause's full FQN (e.g. `\Foo\Bar`)
     * * `Foo.`         -> same as `Foo`
     * * `Foo/method`   -> same as `Foo`
     * * any other      -> null (let Phel-only resolution stay authoritative)
     */
    private fun resolveTargetFqn(symbol: PhelSymbol, text: String): String? {
        val file = symbol.containingFile as? PhelFile ?: return null

        if (text.startsWith("\\") && !text.contains("/")) {
            return text
        }

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

        // Bare short name like `DateTime`. Try to find the matching `:use` FQN
        // (e.g. `(:use \DateTime)` -> `\DateTime`, `(:use \Foo\Bar)` -> `\Foo\Bar`).
        val useFqn = findUseFqn(file, rawQualifier)
        if (useFqn != null) return useFqn

        // Fallback: ask PhpIndex for the bare name at the global namespace.
        return "\\$rawQualifier"
    }

    private fun findUseFqn(file: PhelFile, shortName: String): String? {
        val nsDeclaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return null
        val useForms = PhelNamespaceUtils.findUseForms(nsDeclaration)
        for (useForm in useForms) {
            val forms = useForm.forms
            for (i in 1 until forms.size) {
                val form = forms[i]
                val sym = form as? PhelSymbol
                    ?: com.intellij.psi.util.PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
                val raw = sym?.text ?: continue
                val candidateShort = raw.trimStart('\\').substringAfterLast('\\')
                if (candidateShort == shortName) {
                    return if (raw.startsWith("\\")) raw else "\\$raw"
                }
            }
        }
        return null
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
