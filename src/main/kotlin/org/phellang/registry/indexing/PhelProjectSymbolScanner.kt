package org.phellang.registry.indexing

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.registry.PhelProjectSymbol
import org.phellang.registry.SymbolType
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelMetadata
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelProjectNamespaceFinder
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

object PhelProjectSymbolScanner {
    private val PRIVATE_KEYWORDS = setOf("defn-", "def-", "defmacro-")

    private const val PRIVATE_KEY = ":private"

    private const val DOC_KEY = ":doc"

    fun scanFile(psiFile: PhelFile): List<PhelProjectSymbol> {
        val namespace = PhelNamespaceUtils.extractNamespaceFromFile(psiFile) ?: return emptyList()
        val shortNamespace = PhelProjectNamespaceFinder.extractShortNamespace(namespace)
        val virtualFile = psiFile.virtualFile ?: return emptyList()

        val symbols = mutableListOf<PhelProjectSymbol>()

        val topLevelLists = PsiTreeUtil.getChildrenOfType(psiFile, PhelList::class.java) ?: return emptyList()

        for (list in topLevelLists) {
            val symbol = extractDefinition(list, namespace, shortNamespace, virtualFile)
            if (symbol != null) {
                symbols.add(symbol)
            }
        }

        return symbols
    }

    private fun extractDefinition(
        list: PhelList,
        namespace: String,
        shortNamespace: String,
        virtualFile: com.intellij.openapi.vfs.VirtualFile
    ): PhelProjectSymbol? {
        // activeForms, not list.forms: a `#_`-discarded form must not shift the positional reads
        // below. `(defn #_old new [x] …)` has to index `new`, not `old`.
        val forms = PhelPsiUtils.activeForms(list)
        if (forms.size < 2) return null

        val keywordSymbol = PhelPsiUtils.asSymbol(forms[0])
            ?: return null

        val keyword = keywordSymbol.text

        if (keyword in PRIVATE_KEYWORDS) return null

        val symbolType = SymbolType.fromKeyword(keyword) ?: return null

        val nameSymbol = PhelPsiUtils.asSymbol(forms[1])
            ?: return null

        val name = nameSymbol.text

        if (isPrivateDefinition(forms)) return null

        val signature = extractSignature(keyword, name, forms)
        val docstring = extractDocstring(forms)

        return PhelProjectSymbol(
            namespace = namespace,
            shortNamespace = shortNamespace,
            name = name,
            qualifiedName = "$shortNamespace/$name",
            signature = signature,
            type = symbolType,
            file = virtualFile,
            docstring = docstring
        )
    }

    /**
     * Phel marks a definition private with the `defn-` family (handled by [PRIVATE_KEYWORDS]), a
     * `^` flag on the name symbol, or a keyword/map in the metadata slot between the name and the
     * value — `(def x :private 1)` and `(def x {:private true} 1)`.
     *
     * The body is never consulted: a definition that merely mentions `:private` in its docstring
     * or references a `:private-mode` keyword stays public.
     */
    private fun isPrivateDefinition(forms: List<PhelForm>): Boolean {
        return hasPrivateNameFlag(forms[1]) || hasPrivateMetadataSlot(forms)
    }

    /** `^:private` / `^{:private true}` attached to the name symbol. */
    private fun hasPrivateNameFlag(nameForm: PhelForm): Boolean {
        val metadata = PsiTreeUtil.getChildrenOfType(nameForm, PhelMetadata::class.java) ?: return false

        return metadata.any { meta ->
            PsiTreeUtil.getChildOfType(meta, PhelKeyword::class.java)?.text == PRIVATE_KEY ||
                PsiTreeUtil.getChildOfType(meta, PhelMap::class.java)?.let { declaresPrivate(it) } == true
        }
    }

    /**
     * The metadata slot sits between the name and the value, so the last form is always the value
     * (or body) and never metadata — that is what keeps `(def defaults {:visibility :private-ish})`
     * public while `(def x {:private true} 1)` is private.
     */
    private fun hasPrivateMetadataSlot(forms: List<PhelForm>): Boolean {
        for (i in 2 until forms.size - 1) {
            when (val form = forms[i]) {
                is PhelKeyword -> if (form.text == PRIVATE_KEY) return true
                is PhelMap -> if (declaresPrivate(form)) return true
                // A docstring may precede the flag; anything else ends the metadata slot.
                is PhelLiteral -> if (extractStringLiteral(form) == null) return false
                else -> return false
            }
        }
        return false
    }

    /** True when [map] binds `:private` to `true`; `{:private false}` is explicitly public. */
    private fun declaresPrivate(map: PhelMap): Boolean {
        return mapValueFor(map, PRIVATE_KEY)?.text == "true"
    }

    /** The form bound to [key] in [map], or null when the key is absent. */
    private fun mapValueFor(map: PhelMap, key: String): PhelForm? {
        val children = PhelPsiUtils.activeForms(map)
        for (i in 0 until children.size - 1) {
            val child = children[i]
            if (child is PhelKeyword && child.text == key) return children[i + 1]
        }
        return null
    }

    private fun extractSignature(keyword: String, name: String, forms: List<PhelForm>): String {
        return when (keyword) {
            "defn", "defmacro" -> {
                // A defn is single-arity when it has a direct parameter vector
                // (`(defn f [x] body)`) and multi-arity only when the body is made of
                // `([params] body)` clauses with no top-level vector. Check the direct
                // vector first: otherwise a single-arity body whose first form is a
                // vector-headed list — e.g. `(defn idx [i] ([10 20 30] i))`, a vector
                // used as a function — is misread as a 3-arity clause.
                val paramVec = findDirectParameterVector(forms)
                if (paramVec != null) {
                    val params = extractParameterNames(paramVec)
                    if (params.isEmpty()) "($name)" else "($name ${params.joinToString(" ")})"
                } else {
                    val aritySignatures = extractMultiAritySignatures(name, forms)
                    if (aritySignatures.isNotEmpty()) aritySignatures.joinToString("\n") else "($name)"
                }
            }

            "defstruct" -> {
                val fieldsVec = findDirectParameterVector(forms)
                if (fieldsVec != null) {
                    val fields = extractParameterNames(fieldsVec)
                    if (fields.isEmpty()) "($name)" else "($name ${fields.joinToString(" ")})"
                } else {
                    "($name)"
                }
            }

            "definterface" -> "($name ...)"
            "defexception" -> "($name)"
            else -> "($name)"
        }
    }

    private fun extractMultiAritySignatures(name: String, forms: List<PhelForm>): List<String> {
        val signatures = mutableListOf<String>()

        for (i in 2 until forms.size) {
            val form = forms[i]

            // Multi-arity: each arity is a list whose first form is a vector.
            if (form is PhelList) {
                val paramVec = PhelPsiUtils.activeForms(form).firstOrNull() as? PhelVec ?: continue
                val params = extractParameterNames(paramVec)
                val sig = if (params.isEmpty()) "($name)" else "($name ${params.joinToString(" ")})"
                signatures.add(sig)
            }
        }

        return signatures
    }

    private fun findDirectParameterVector(forms: List<PhelForm>): PhelVec? {
        for (i in 2 until forms.size) {
            val form = forms[i]
            if (form is PhelVec) return form
        }
        return null
    }

    private fun extractParameterNames(paramVec: PhelVec): List<String> {
        val params = mutableListOf<String>()
        for (form in PhelPsiUtils.activeForms(paramVec)) {
            val text = form.text
            if (!text.isNullOrBlank()) params.add(text)
        }
        return params
    }

    private fun extractDocstring(forms: List<PhelForm>): String? {
        if (forms.size < 3) return null

        // First try the canonical position right after the name; supports the
        // `(def name {:doc "..."} value)` style as well.
        val potentialDocstring = forms[2]
        extractStringLiteral(potentialDocstring)?.let { return it }
        extractDocFromMap(potentialDocstring)?.let { return it }

        // Otherwise scan the rest of the body for the first top-level string literal.
        // Covers `(defn foo [x] "doc" body)` and `(defn foo {:meta} [x] "doc" body)`.
        for (i in 3 until forms.size) {
            extractStringLiteral(forms[i])?.let { return it }
        }
        return null
    }

    private fun extractDocFromMap(form: PhelForm): String? {
        val map = form as? PhelMap ?: return null
        val doc = mapValueFor(map, DOC_KEY) ?: return null
        return extractStringLiteral(doc)
    }

    private fun extractStringLiteral(form: PhelForm): String? {
        val literal = form as? PhelLiteral ?: return null
        val text = literal.text
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length - 1)
        }
        return null
    }
}
