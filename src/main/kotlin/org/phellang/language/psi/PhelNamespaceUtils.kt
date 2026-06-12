package org.phellang.language.psi

import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.files.PhelFile
import java.util.Optional

object PhelNamespaceUtils {

    private val USED_CLASSES_KEY: Key<CachedValue<Set<String>>> = Key.create("phel.namespace.usedClasses")
    private val REFERRED_SYMBOLS_KEY: Key<CachedValue<Set<String>>> = Key.create("phel.namespace.referredSymbols")
    private val ALIAS_MAP_KEY: Key<CachedValue<Map<String, String>>> = Key.create("phel.namespace.aliasMap")
    private val NS_DECLARATION_KEY: Key<CachedValue<Optional<PhelList>>> = Key.create("phel.namespace.declaration")
    private val REQUIRE_FORMS_KEY: Key<CachedValue<List<PhelList>>> = Key.create("phel.namespace.requireForms")
    private val USE_FORMS_KEY: Key<CachedValue<List<PhelList>>> = Key.create("phel.namespace.useForms")

    fun findNamespaceDeclaration(file: PhelFile): PhelList? {
        // Resolved once per file: highlighting and reference resolution look up the (ns …)
        // form repeatedly (per qualified symbol), and each lookup otherwise scans the tree.
        return CachedValuesManager.getCachedValue(file, NS_DECLARATION_KEY) {
            CachedValueProvider.Result.create(
                Optional.ofNullable(computeNamespaceDeclaration(file)),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }.orElse(null)
    }

    private fun computeNamespaceDeclaration(file: PhelFile): PhelList? {
        // (ns my-ns (:require ...) (:use ...))
        val lists = PsiTreeUtil.findChildrenOfType(file, PhelList::class.java)
        return lists.firstOrNull { list ->
            val forms = list.forms
            if (forms.isEmpty()) return@firstOrNull false

            val firstForm = forms[0]
            val firstSymbol = if (firstForm is PhelSymbol) firstForm
            else PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)
            firstSymbol?.text == "ns"
        }
    }

    fun isNamespaceRequired(nsDeclaration: PhelList, namespace: String): Boolean {
        val target = normalizeNamespace(namespace)
        val requireForms = findRequireForms(nsDeclaration)
        return requireForms.any { requireForm ->
            // Check if the require form contains the namespace (either dot or backslash form)
            val symbols = PsiTreeUtil.findChildrenOfType(requireForm, PhelSymbol::class.java)
            symbols.any { sym -> sym.text?.let { normalizeNamespace(it) } == target }
        }
    }

    fun findRequireForms(nsDeclaration: PhelList): List<PhelList> {
        // Cached on the (cached, per-file) ns element: the validators look these up once per
        // qualified symbol, each otherwise re-walking the ns form's child lists.
        return CachedValuesManager.getCachedValue(nsDeclaration, REQUIRE_FORMS_KEY) {
            CachedValueProvider.Result.create(
                findNsClauseForms(nsDeclaration, ":require"),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    /**
     * Finds `(:use ...)` clauses inside an `(ns ...)` declaration. Phel's `:use`
     * brings PHP classes (not Phel namespaces) into scope so they can be referenced
     * without the leading `\`, e.g. `(:use \DateTime \Exception)` enables
     * `DateTime/createFromFormat`, `(DateTime. ...)`, etc.
     */
    fun findUseForms(nsDeclaration: PhelList): List<PhelList> {
        return CachedValuesManager.getCachedValue(nsDeclaration, USE_FORMS_KEY) {
            CachedValueProvider.Result.create(
                findNsClauseForms(nsDeclaration, ":use"),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    private fun findNsClauseForms(nsDeclaration: PhelList, clauseKeyword: String): List<PhelList> {
        return PsiTreeUtil.findChildrenOfType(nsDeclaration, PhelList::class.java).filter { list ->
            val forms = list.forms
            if (forms.isEmpty()) return@filter false

            val firstForm = forms[0]
            val firstKeyword = firstForm as? PhelKeyword
                ?: PsiTreeUtil.findChildOfType(firstForm, PhelKeyword::class.java)
            firstKeyword?.text == clauseKeyword
        }
    }

    /**
     * Extracts PHP class short names declared via `(:use ...)` in the file's `ns` form.
     *
     * Both `\Foo\Bar` and `Foo\Bar` produce the short name `Bar`. The short name is
     * what users actually type in interop shorthands such as `(Bar. arg)`,
     * `(.method bar)`, or `Bar/static-fn`.
     */
    fun extractUsedClasses(file: PhelFile): Set<String> {
        // Highlighting probes this once per symbol; cache the per-file result and let
        // the PSI modification tracker invalidate it whenever the file is edited.
        return CachedValuesManager.getCachedValue(file, USED_CLASSES_KEY) {
            CachedValueProvider.Result.create(
                computeUsedClasses(file),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    private fun computeUsedClasses(file: PhelFile): Set<String> {
        val nsDeclaration = findNamespaceDeclaration(file) ?: return emptySet()
        val useForms = findUseForms(nsDeclaration)
        if (useForms.isEmpty()) return emptySet()

        val classes = mutableSetOf<String>()
        for (useForm in useForms) {
            val forms = useForm.forms
            for (i in 1 until forms.size) {
                val form = forms[i]
                val symbol = form as? PhelSymbol ?: PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
                val text = symbol?.text ?: continue
                val shortName = extractShortClassName(text) ?: continue
                classes.add(shortName)
            }
        }
        return classes
    }

    /**
     * True when [symbol] is one of the PHP class entries inside a `(:use ...)` clause
     * (i.e. not the `:use` keyword itself). Used to wire go-to-definition from the
     * imported class name straight to its PHP class declaration.
     *
     * Handles every spelling Phel accepts for the entry — bare (`Countable`),
     * dot-separated (`Phel.Compiler.CompilerFacade`, the modern form) and the legacy
     * backslash form (`Phel\Compiler\CompilerFacade`) — since each lexes as a single
     * symbol.
     */
    fun isUseClassSymbol(symbol: PhelSymbol): Boolean {
        val clause = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return false
        val forms = clause.forms
        if (forms.isEmpty()) return false

        val firstKeyword = forms[0] as? PhelKeyword
            ?: PsiTreeUtil.findChildOfType(forms[0], PhelKeyword::class.java)
        if (firstKeyword?.text != ":use") return false

        // Reject the keyword form itself; accept any class-entry form (forms[1..n]).
        return forms.drop(1).any { it === symbol || PsiTreeUtil.isAncestor(it, symbol, false) }
    }

    private fun extractShortClassName(text: String): String? {
        val trimmed = text.trimStart('\\')
        if (trimmed.isEmpty()) return null
        val short = trimmed.substringAfterLast('\\')
        return short.ifEmpty { null }
    }

    fun extractAliasMap(file: PhelFile): Map<String, String> {
        return CachedValuesManager.getCachedValue(file, ALIAS_MAP_KEY) {
            CachedValueProvider.Result.create(
                computeAliasMap(file),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    private fun computeAliasMap(file: PhelFile): Map<String, String> {
        val aliasMap = mutableMapOf<String, String>()

        val nsDeclaration = findNamespaceDeclaration(file) ?: return aliasMap
        val requireForms = findRequireForms(nsDeclaration)

        for (requireForm in requireForms) {
            val forms = requireForm.forms

            var i = 1
            while (i < forms.size) {
                val form = forms[i]

                // Get the namespace symbol
                val namespaceSymbol = if (form is PhelSymbol) form
                else PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)

                if (namespaceSymbol != null) {
                    val namespace = namespaceSymbol.text

                    // Check if next form is :as keyword
                    if (i + 1 < forms.size) {
                        val nextForm = forms[i + 1]
                        val asKeyword = nextForm as? PhelKeyword
                            ?: PsiTreeUtil.findChildOfType(nextForm, PhelKeyword::class.java)

                        if (asKeyword?.text == ":as" && i + 2 < forms.size) {
                            // Get the alias symbol
                            val aliasForm = forms[i + 2]
                            val aliasSymbol = if (aliasForm is PhelSymbol) aliasForm
                            else PsiTreeUtil.findChildOfType(aliasForm, PhelSymbol::class.java)

                            if (aliasSymbol != null) {
                                val alias = aliasSymbol.text
                                val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(namespace)
                                aliasMap[alias] = shortNamespace
                                i += 3  // Skip namespace, :as, and alias
                                continue
                            }
                        }
                    }
                }
                i++
            }
        }

        return aliasMap
    }

    /**
     * Extracts the namespace name from a function name.
     * e.g., "str/contains?" -> "str"
     *       "contains?" -> null (no namespace)
     */
    fun extractNamespace(functionName: String): String? {
        return if (functionName.contains("/")) {
            functionName.substringBefore("/")
        } else {
            null
        }
    }

    /**
     * Canonicalises a namespace to dot-separated form (Phel 0.35+).
     * Accepts the legacy backslash form so callers can compare namespaces written
     * either way without caring which the user typed.
     *
     * e.g., "phel\\string" -> "phel.string"
     *       "phel.string"  -> "phel.string"
     */
    fun normalizeNamespace(namespace: String): String {
        return namespace.replace('\\', '.')
    }

    fun extractNamespaceFromDeclaration(nsDeclaration: PhelList): String? {
        val forms = nsDeclaration.forms
        if (forms.size < 2) return null

        val namespaceSymbol = forms[1] as? PhelSymbol
            ?: PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)
        return namespaceSymbol?.text
    }

    fun extractShortNamespaceFromDeclaration(nsDeclaration: PhelList): String? {
        return extractNamespaceFromDeclaration(nsDeclaration)
            ?.let(PhelProjectNamespaceFinder::extractShortNamespace)
    }

    fun extractNamespaceFromFile(file: PhelFile): String? {
        val nsDeclaration = findNamespaceDeclaration(file) ?: return null
        return extractNamespaceFromDeclaration(nsDeclaration)
    }

    /**
     * Converts a short namespace to its canonical Phel form (Phel 0.35+ dot-separated).
     * e.g., "string" -> "phel.string"
     *       "http"   -> "phel.http"
     */
    fun toPhelNamespace(shortNamespace: String): String {
        return "phel.$shortNamespace"
    }

    fun isCoreNamespace(namespace: String?): Boolean {
        return namespace == null || namespace == "core"
    }

    fun isNamespaceImportedOrAliased(file: PhelFile, shortNamespace: String): Boolean {
        if (isCoreNamespace(shortNamespace)) {
            return true
        }

        val nsDeclaration = findNamespaceDeclaration(file) ?: return false
        val phelNamespace = toPhelNamespace(shortNamespace)

        // Check direct import
        if (isNamespaceRequired(nsDeclaration, phelNamespace)) {
            return true
        }

        // Check if imported via alias
        val aliasMap = extractAliasMap(file)
        return aliasMap.values.contains(shortNamespace)
    }

    fun extractReferredSymbols(file: PhelFile): Set<String> {
        return CachedValuesManager.getCachedValue(file, REFERRED_SYMBOLS_KEY) {
            CachedValueProvider.Result.create(
                computeReferredSymbols(file),
                PsiModificationTracker.MODIFICATION_COUNT,
            )
        }
    }

    private fun computeReferredSymbols(file: PhelFile): Set<String> {
        val referredSymbols = mutableSetOf<String>()

        val nsDeclaration = findNamespaceDeclaration(file) ?: return referredSymbols
        val requireForms = findRequireForms(nsDeclaration)

        for (requireForm in requireForms) {
            val forms = requireForm.forms

            var i = 1
            while (i < forms.size) {
                val form = forms[i]

                // Check if this is a :refer keyword
                val keyword = form as? PhelKeyword
                    ?: PsiTreeUtil.findChildOfType(form, PhelKeyword::class.java)

                if (keyword?.text == ":refer" && i + 1 < forms.size) {
                    // Next form should be a vector with symbols
                    val vectorForm = forms[i + 1]

                    // Extract symbols from the vector (vec contains PhelSymbol children)
                    val symbols = PsiTreeUtil.findChildrenOfType(vectorForm, PhelSymbol::class.java)
                    for (symbol in symbols) {
                        val symbolText = symbol.text
                        // Skip namespace-qualified symbols (either separator) and PHP FQNs.
                        if (symbolText != null && !symbolText.contains("\\") && !symbolText.contains("/")) {
                            referredSymbols.add(symbolText)
                        }
                    }

                    i += 2 // Skip :refer and the vector
                    continue
                }

                i++
            }
        }

        return referredSymbols
    }

    fun isReferredSymbol(file: PhelFile, symbolName: String): Boolean {
        return extractReferredSymbols(file).contains(symbolName)
    }

    /**
     * If [symbolName] is referred from a `(:require [ns :refer [...]])` clause in [file],
     * returns the source namespace (the head of that require clause). Returns null when
     * the symbol isn't refer'd from any require, so callers can fall back to other lookups.
     */
    fun findReferSource(file: PhelFile, symbolName: String): String? {
        val nsDeclaration = findNamespaceDeclaration(file) ?: return null
        val requireForms = findRequireForms(nsDeclaration)

        for (requireForm in requireForms) {
            val forms = requireForm.forms
            if (forms.isEmpty()) continue

            val sourceNamespace = (forms[0] as? PhelSymbol)?.text
                ?: PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)?.text
                ?: continue

            var i = 1
            while (i < forms.size) {
                val keyword = (forms[i] as? PhelKeyword)
                    ?: PsiTreeUtil.findChildOfType(forms[i], PhelKeyword::class.java)
                if (keyword?.text == ":refer" && i + 1 < forms.size) {
                    val symbols = PsiTreeUtil.findChildrenOfType(forms[i + 1], PhelSymbol::class.java)
                    if (symbols.any { it.text == symbolName }) {
                        return sourceNamespace
                    }
                    i += 2
                    continue
                }
                i++
            }
        }
        return null
    }
}
