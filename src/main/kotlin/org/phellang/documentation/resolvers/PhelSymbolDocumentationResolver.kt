package org.phellang.documentation.resolvers

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelReferUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelTypeTags
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.files.PhelFile

class PhelSymbolDocumentationResolver {
    fun resolveDocumentation(element: PsiElement?, originalElement: PsiElement?): String? {
        val symbol = PhelPsiUtils.findTopmostSymbol(originalElement)
            ?: PhelPsiUtils.findTopmostSymbol(element)
            ?: return null

        val symbolName = symbol.text
        if (symbolName.isNullOrEmpty()) {
            return null
        }

        val content = when {
            PhelTypeTags.isTypeTag(symbol) -> typeTagDocumentation(symbolName)
            isLocalSymbol(symbol) -> generateLocalSymbolDoc(symbol, symbolName)
            isInsideReferVector(symbol) -> resolveReferSymbolDocumentation(symbol, symbolName)
            else -> resolveApiDocumentation(symbol, symbolName)
        }

        return PhelDocHtml.page(content)
    }

    /**
     * `^map`, `^?int`, `^map|null`: what each member of the tag names. Never the documentation of the
     * function a tag happens to spell, which is what the API lookup below would find for `map`.
     */
    private fun typeTagDocumentation(tag: String): String =
        PhelDocHtml.typeTag(tag, PhelTypeTags.members(tag).map { it.name to describeTagMember(it) })

    private fun describeTagMember(member: PhelTypeTags.Member): String {
        val kind = PhelTypeTags.backingClass(member.name)
            ?.let { "Phel value type, \\$it" }
            ?: if (member.name in PhelTypeTags.PHP_TYPES) "PHP type" else "PHP class"

        return if (member.nullable) "nullable $kind" else kind
    }

    private fun isInsideReferVector(symbol: PhelSymbol): Boolean {
        val symbolName = symbol.text ?: return false

        // Symbols in :refer vectors don't have backslashes (they're simple names)
        if (symbolName.contains("\\")) {
            return false
        }

        return PhelReferUtils.isInsideReferVector(symbol)
    }

    private fun resolveReferSymbolDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val namespace = PhelReferUtils.getReferNamespace(symbol) ?: return generateBasicDocumentation(symbol, symbolName)

        // The registry is keyed by short name, so only a stdlib namespace may use it.
        if (PhelProjectNamespaceFinder.isStandardLibrary(namespace)) {
            val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(namespace)
            PhelApiDocumentation.getDocumentation("$shortNamespace/$symbolName")?.let { return it }
        }

        val projectDoc = resolveProjectSymbolDocumentation(symbol, namespace, symbolName)
        if (projectDoc != null) return projectDoc

        return generateBasicDocumentation(symbol, symbolName)
    }

    private fun isLocalSymbol(symbol: PhelSymbol): Boolean {
        // A call to a function defined in this file is not a local: it has documentation of its own,
        // and describing it as a binding hides the very docstring the user hovered for.
        if (PhelSymbolAnalyzer.isLocalFunctionReference(symbol)) return false

        return PhelSymbolAnalyzer.isLocalBindingOrReference(symbol)
    }

    private fun generateLocalSymbolDoc(symbol: PhelSymbol, symbolName: String): String =
        PhelDocHtml.localSymbol(symbolName, PhelBasicDocumentation.generateBasicDocForElement(symbol))

    private fun resolveApiDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val qualifier = PhelPsiUtils.getQualifier(symbol)
        val functionName = PhelPsiUtils.getName(symbol)

        val doc = if (qualifier != null && functionName != null) {
            qualifiedDocumentation(symbol, symbolName, qualifier, functionName)
        } else {
            unqualifiedDocumentation(symbol, symbolName)
        }

        return doc ?: generateBasicDocumentation(symbol, symbolName)
    }

    /**
     * A qualified symbol such as `str/replace` or `s/replace`.
     *
     * The qualifier may be an `:as` alias, so it is resolved through the file's alias map before the
     * canonical lookup; the symbol's literal text is tried next in case it is already canonical. Both
     * are registry lookups, keyed by short name, so a qualifier that names a required project namespace
     * skips them: `app.string :as s` documents its own `s/join`, not `phel.string`'s.
     */
    private fun qualifiedDocumentation(
        symbol: PhelSymbol,
        symbolName: String,
        qualifier: String,
        functionName: String,
    ): String? {
        val file = symbol.containingFile as? PhelFile ?: return null
        val imported = PhelNamespaceUtils.importedNamespaceFor(file, qualifier)

        if (imported == null || PhelProjectNamespaceFinder.isStandardLibrary(imported)) {
            val resolvedNamespace = PhelProjectNamespaceFinder.registryNamespaceOf(imported ?: qualifier)
                ?: PhelNamespaceUtils.extractAliasMap(file)[qualifier]
                ?: qualifier
            PhelApiDocumentation.getDocumentation("$resolvedNamespace/$functionName")?.let { return it }
            PhelApiDocumentation.getDocumentation(symbolName)?.let { return it }
        }

        val projectSymbol = PhelProjectSymbolIndex.getInstance(symbol.project).findQualified(file, qualifier, functionName)
        return projectSymbol?.let(PhelDocHtml::projectSymbol)
    }

    /**
     * A bare symbol such as `map`: the standard library first, then the namespace a `:refer` clause
     * brought the name in from.
     */
    private fun unqualifiedDocumentation(symbol: PhelSymbol, symbolName: String): String? {
        PhelApiDocumentation.getDocumentation(symbolName)?.let { return it }

        val file = symbol.containingFile as? PhelFile ?: return null

        // An unqualified name resolves against the current namespace before anything else — that is
        // how a recursive call, or a call to a sibling definition, finds its own documentation.
        ownNamespaceDocumentation(symbol, file, symbolName)?.let { return it }

        val sourceNamespace = PhelNamespaceUtils.findReferSource(file, symbolName) ?: return null

        // The registry is keyed by short name, so only a stdlib namespace may use it: a project's
        // `app.string` refers its own `join`, not `phel.string`'s.
        val standardLibraryDocumentation = if (PhelProjectNamespaceFinder.isStandardLibrary(sourceNamespace)) {
            val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(sourceNamespace)
            PhelApiDocumentation.getDocumentation("$shortNamespace/$symbolName")
        } else {
            null
        }

        return standardLibraryDocumentation ?: resolveProjectSymbolDocumentation(symbol, sourceNamespace, symbolName)
    }

    private fun ownNamespaceDocumentation(symbol: PhelSymbol, file: PhelFile, symbolName: String): String? {
        val namespace = PhelNamespaceUtils.extractNamespaceFromFile(file) ?: return null

        return resolveProjectSymbolDocumentation(symbol, namespace, symbolName)
    }

    /** The project's definition of [functionName] in [namespace], a full name. */
    private fun resolveProjectSymbolDocumentation(
        symbol: PhelSymbol, namespace: String, functionName: String
    ): String? {
        val project = symbol.project
        val index = PhelProjectSymbolIndex.getInstance(project)
        val projectSymbol = index.findInNamespace(namespace, functionName) ?: return null

        return PhelDocHtml.projectSymbol(projectSymbol)
    }

    private fun generateBasicDocumentation(symbol: PhelSymbol, symbolName: String): String {
        val docstring = extractDefinitionDocstring(symbol)?.takeIf { it.isNotBlank() }
        val description = docstring ?: PhelBasicDocumentation.generateBasicDocForElement(symbol)
        return PhelDocHtml.basic(symbolName, description)
    }

    /**
     * Returns the first top-level string literal that appears after the defining keyword and
     * the defined name — skipping attribute maps and parameter vectors. Phel allows the
     * docstring to be placed either right after the name `(defn name "doc" [x] body)` or
     * later in the body `(defn name {:meta} [x] "doc" body)`.
     */
    private fun extractDefinitionDocstring(symbol: PhelSymbol): String? {
        val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java) ?: return null
        val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java) ?: return null
        if (forms.size < 3) return null

        // Only treat the symbol as a definition site when it sits at the name position.
        val nameSymbol = PhelPsiUtils.asSymbol(forms[1])
        if (nameSymbol !== symbol) return null

        for (i in 2 until forms.size) {
            directStringLiteralText(forms[i])?.let { return it }
        }
        return null
    }

    private fun directStringLiteralText(form: PhelForm): String? {
        val literal = (form as? PhelLiteral)
            ?: form.children.firstOrNull { it is PhelLiteral } as? PhelLiteral
            ?: return null
        val raw = literal.text ?: return null
        if (raw.length < 2 || !raw.startsWith("\"") || !raw.endsWith("\"")) return null
        return raw.substring(1, raw.length - 1)
    }

}
