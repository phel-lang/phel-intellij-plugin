package org.phellang.completion.indexing

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.completion.data.PhelProjectSymbol
import org.phellang.completion.data.SymbolType
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelList
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
        if (isPrivateDefinition(forms)) {
            return null
        }

        // Extract signature
        val signature = extractSignature(keyword, name, forms)

        return PhelProjectSymbol(
            namespace = namespace,
            shortNamespace = shortNamespace,
            name = name,
            qualifiedName = "$shortNamespace/$name",
            signature = signature,
            type = symbolType,
            file = virtualFile
        )
    }

    private fun isPrivateDefinition(forms: Array<PhelForm>): Boolean {
        for (form in forms) {
            val text = form.text
            if (text != null) {
                if (text.contains(":private")) {
                    return true
                }
            }
        }

        return false
    }

    private fun extractSignature(keyword: String, name: String, forms: Array<PhelForm>): String {
        return when (keyword) {
            "defn", "defmacro" -> {
                // Find parameter vector
                val paramVec = findParameterVector(forms)
                if (paramVec != null) {
                    val params = extractParameterNames(paramVec)
                    "($name ${params.joinToString(" ")})"
                } else {
                    "($name)"
                }
            }

            "defstruct" -> {
                // defstruct has fields in a vector
                val fieldsVec = findParameterVector(forms)
                if (fieldsVec != null) {
                    val fields = extractParameterNames(fieldsVec)
                    "($name ${fields.joinToString(" ")})"
                } else {
                    "($name)"
                }
            }

            "definterface" -> {
                "($name ...)"
            }

            else -> {
                // def - just the name
                "($name)"
            }
        }
    }

    private fun findParameterVector(forms: Array<PhelForm>): PhelVec? {
        // Start from index 2 (after keyword and name)
        for (i in 2 until forms.size) {
            val form = forms[i]
            if (form is PhelVec) {
                return form
            }
            // Also check if the form contains a vector
            val nestedVec = PsiTreeUtil.findChildOfType(form, PhelVec::class.java)
            if (nestedVec != null) {
                return nestedVec
            }
        }
        return null
    }

    private fun extractParameterNames(paramVec: PhelVec): List<String> {
        val params = mutableListOf<String>()
        val forms = PsiTreeUtil.getChildrenOfType(paramVec, PhelForm::class.java) ?: return params

        for (form in forms) {
            val symbol = if (form is PhelSymbol) {
                form
            } else {
                PsiTreeUtil.findChildOfType(form, PhelSymbol::class.java)
            }
            if (symbol != null) {
                params.add(symbol.text)
            }
        }

        return params
    }
}
