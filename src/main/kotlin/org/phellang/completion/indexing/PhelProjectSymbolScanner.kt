package org.phellang.completion.indexing

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.data.PhelProjectSymbol
import org.phellang.completion.data.SymbolType
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelKeyword
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelLiteral
import org.phellang.language.psi.PhelMap
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.files.PhelFile

object PhelProjectSymbolScanner {

    private val PRIVATE_KEYWORDS = setOf("defn-", "def-", "defmacro-")

    fun scanFile(psiFile: PhelFile): List<PhelProjectSymbol> {
        val namespace = extractNamespace(psiFile) ?: return emptyList()
        val shortNamespace = namespace.substringAfterLast("\\")
        val virtualFile = psiFile.virtualFile ?: return emptyList()

        val symbols = mutableListOf<PhelProjectSymbol>()

        // Find all top-level lists (definitions are at the top level)
        val topLevelLists = PsiTreeUtil.getChildrenOfType(psiFile, PhelList::class.java) ?: return emptyList()

        for (list in topLevelLists) {
            val symbol = extractDefinition(list, namespace, shortNamespace, virtualFile)
            if (symbol != null) {
                symbols.add(symbol)
            }
        }

        return symbols
    }

    fun extractNamespace(file: PhelFile): String? {
        return PhelNamespaceUtils.extractNamespaceFromFile(file)
    }

    private fun extractDefinition(
        list: PhelList,
        namespace: String,
        shortNamespace: String,
        virtualFile: com.intellij.openapi.vfs.VirtualFile
    ): PhelProjectSymbol? {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java) ?: return null

        if (forms.size < 2) return null

        // Get the defining keyword
        val keywordSymbol = if (forms[0] is PhelSymbol) {
            forms[0] as PhelSymbol
        } else {
            PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java)
        } ?: return null

        val keyword = keywordSymbol.text

        // Skip private definition keywords (defn-, def-, defmacro-)
        if (keyword in PRIVATE_KEYWORDS) {
            return null
        }

        val symbolType = SymbolType.fromKeyword(keyword) ?: return null

        // Get the defined name
        val nameSymbol = if (forms[1] is PhelSymbol) {
            forms[1] as PhelSymbol
        } else {
            PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)
        } ?: return null

        val name = nameSymbol.text

        // Check if private - skip private definitions
        if (isPrivateDefinition(forms)) return null

        // Extract signature and docstring
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

    private fun isPrivateDefinition(forms: Array<PhelForm>): Boolean {
        for (form in forms) {
            val text = form.text ?: continue
            if (!text.contains(":private")) continue
            return true
        }

        return false
    }

    private fun extractSignature(keyword: String, name: String, forms: Array<PhelForm>): String {
        return when (keyword) {
            "defn", "defmacro" -> {
                // Check for multi-arity: look for lists that contain a vector as first element
                val aritySignatures = extractMultiAritySignatures(name, forms)
                if (aritySignatures.isNotEmpty()) {
                    aritySignatures.joinToString("\n")
                } else {
                    // Single-arity: find the parameter vector directly
                    val paramVec = findDirectParameterVector(forms)
                    if (paramVec != null) {
                        val params = extractParameterNames(paramVec)
                        if (params.isEmpty()) "($name)" else "($name ${params.joinToString(" ")})"
                    } else {
                        "($name)"
                    }
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

    private fun extractMultiAritySignatures(name: String, forms: Array<PhelForm>): List<String> {
        val signatures = mutableListOf<String>()

        // Start from index 2 (after keyword and name)
        for (i in 2 until forms.size) {
            val form = forms[i]

            // Multi-arity: each arity is a list that starts with a vector
            if (form is PhelList) {
                val listForms = PsiTreeUtil.getChildrenOfType(form, PhelForm::class.java)
                if (!listForms.isNullOrEmpty() && listForms[0] is PhelVec) {
                    val paramVec = listForms[0] as PhelVec
                    val params = extractParameterNames(paramVec)
                    val sig = if (params.isEmpty()) "($name)" else "($name ${params.joinToString(" ")})"
                    signatures.add(sig)
                }
            }
        }

        return signatures
    }

    private fun findDirectParameterVector(forms: Array<PhelForm>): PhelVec? {
        // Start from index 2 (after keyword and name)
        for (i in 2 until forms.size) {
            val form = forms[i]
            if (form is PhelVec) {
                return form
            }
        }
        return null
    }

    private fun extractParameterNames(paramVec: PhelVec): List<String> {
        val params = mutableListOf<String>()
        val forms = PsiTreeUtil.getChildrenOfType(paramVec, PhelForm::class.java) ?: return params

        for (form in forms) {
            val text = form.text
            if (!text.isNullOrBlank()) {
                params.add(text)
            }
        }

        return params
    }

    private fun extractDocstring(forms: Array<PhelForm>): String? {
        if (forms.size < 3) return null

        val potentialDocstring = forms[2]

        // Try string literal first (defn/defmacro style)
        extractStringLiteral(potentialDocstring)?.let { return it }

        // Try map metadata with :doc key (def style)
        extractDocFromMap(potentialDocstring)?.let { return it }

        return null
    }

    private fun extractDocFromMap(form: PhelForm): String? {
        val map = form as? PhelMap ?: return null

        // Get all children and look for :doc keyword followed by a string
        val children = PsiTreeUtil.getChildrenOfType(map, PhelForm::class.java) ?: return null

        for (i in 0 until children.size - 1) {
            val child = children[i]
            if (child is PhelKeyword && child.text == ":doc") {
                // Next element should be the docstring
                val docValue = children[i + 1]
                return extractStringLiteral(docValue)
            }
        }

        return null
    }

    private fun extractStringLiteral(form: PhelForm): String? {
        // Only accept direct string literals, not strings nested inside other forms
        val literal = form as? PhelLiteral ?: return null

        val text = literal.text
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length - 1)
        }

        return null
    }
}
