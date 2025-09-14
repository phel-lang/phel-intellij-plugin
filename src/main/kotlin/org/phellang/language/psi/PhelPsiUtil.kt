package org.phellang.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil

/**
 * Utility methods for working with Phel PSI elements.
 * This class provides helper functions for analyzing and manipulating Phel code structures.
 */
object PhelPsiUtil {
    /**
     * Get the name of a Phel symbol.
     * For qualified symbols like "ns/name", returns just "name".
     * For simple symbols like "name", returns "name".
     */
    @JvmStatic
    fun getName(symbol: PhelSymbol): String? {
        val text = symbol.text ?: return null

        // Handle qualified symbols (namespace/name)
        val slashIndex = text.lastIndexOf('/')
        if (slashIndex > 0 && slashIndex < text.length - 1) {
            return text.substring(slashIndex + 1)
        }

        return text
    }

    /**
     * Get the namespace qualifier of a symbol.
     * For "ns/name" returns "ns", for "name" returns null.
     */
    @JvmStatic
    fun getQualifier(symbol: PhelSymbol): String? {
        val text = symbol.text ?: return null

        val slashIndex = text.lastIndexOf('/')
        if (slashIndex > 0) {
            return text.take(slashIndex)
        }

        return null
    }

    /**
     * Get the text offset of the symbol name part (excluding qualifier).
     * Used for proper positioning in rename refactoring.
     */
    @JvmStatic
    fun getNameTextOffset(symbol: PhelSymbol): Int {
        val text = symbol.text ?: return symbol.textRange.startOffset

        val slashIndex = text.lastIndexOf('/')
        if (slashIndex > 0 && slashIndex < text.length - 1) {
            return symbol.textRange.startOffset + slashIndex + 1
        }

        return symbol.textRange.startOffset
    }

    /**
     * Check if this symbol is in a definition position (being defined rather than referenced).
     * This helps distinguish between definitions and usages for proper highlighting.
     */
    @JvmStatic
    fun isDefinition(symbol: PhelSymbol): Boolean {
        // Check if this symbol is a function parameter
        if (isFunctionParameter(symbol)) {
            return true
        }

        // Check if this symbol is a let binding
        if (isLetBinding(symbol)) {
            return true
        }

        // Check if this symbol is the first element in a list starting with def, defn, etc.
        val containingList = PsiTreeUtil.getParentOfType(symbol, PhelList::class.java)
        if (containingList != null) {
            val firstForm = PsiTreeUtil.findChildOfType(containingList, PhelForm::class.java)
            if (firstForm != null) {
                val firstSymbol = PsiTreeUtil.findChildOfType(firstForm, PhelSymbol::class.java)
                if (firstSymbol != null) {
                    val firstSymbolText = firstSymbol.text
                    if (isDefiningForm(firstSymbolText)) {
                        // Check if this symbol is the second element (the name being defined)
                        val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java)
                        if (forms != null && forms.size > 1) {
                            val definedName = PsiTreeUtil.findChildOfType(forms[1], PhelSymbol::class.java)
                            return symbol === definedName
                        }
                    }
                }
            }
        }

        return false
    }

    /**
     * Check if a symbol represents a defining form (def, defn, defmacro, etc.).
     */
    @JvmStatic
    fun isDefiningForm(symbolText: String?): Boolean {
        if (symbolText == null) {
            return false
        }

        return symbolText == "def" || symbolText == "defn" || symbolText == "defmacro" || symbolText == "defstruct" || symbolText == "definterface" || symbolText == "defexception" || symbolText == "declare" || symbolText == "def-" || symbolText == "defn-" || symbolText == "defmacro-"
    }

    /**
     * Check if this symbol is a function parameter.
     * Parameters appear in parameter vectors: (defn name [param1 param2] ...) or (fn [param1 param2] ...)
     */
    private fun isFunctionParameter(symbol: PhelSymbol): Boolean {
        // Check if symbol is inside a parameter vector
        val paramVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return false


        // Check if the parameter vector is part of a function definition
        val containingList = PsiTreeUtil.getParentOfType(paramVec, PhelList::class.java) ?: return false

        val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java)
        if (forms == null || forms.size < 2) return false

        // Check first symbol to see if it's a function-defining form
        val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java) ?: return false

        val keyword = firstSymbol.text

        if (keyword == "defn" || keyword == "defn-" || keyword == "defmacro" || keyword == "defmacro-") {
            // For (defn name [params] ...) and (defn- name [params] ...), parameter vector is at forms[2]
            // forms[2] IS the parameter vector directly (not wrapped)
            return forms.size >= 3 && forms[2] === paramVec
        } else if (keyword == "fn") {
            // For (fn [params] ...), parameter vector is at forms[1]  
            // forms[1] IS the parameter vector directly (not wrapped)
            return forms[1] === paramVec
        }

        return false
    }

    /**
     * Check if this symbol is a let binding.
     * Let bindings appear in binding vectors: (let [symbol value symbol2 value2] ...)
     */
    private fun isLetBinding(symbol: PhelSymbol): Boolean {
        // Check if symbol is inside a binding vector
        val bindingVec = PsiTreeUtil.getParentOfType(symbol, PhelVec::class.java) ?: return false

        // Check if the binding vector is part of a let form
        val containingList = PsiTreeUtil.getParentOfType(bindingVec, PhelList::class.java) ?: return false

        val forms = PsiTreeUtil.getChildrenOfType(containingList, PhelForm::class.java)
        if (forms == null || forms.size < 2) return false

        // Check if first symbol is a binding form
        val firstSymbol = PsiTreeUtil.findChildOfType(forms[0], PhelSymbol::class.java) ?: return false
        val formType = firstSymbol.text
        if (("let" != formType) && ("for" != formType) && ("binding" != formType) && ("loop" != formType) && ("foreach" != formType) && ("dofor" != formType)) return false

        // Check if binding vector is at forms[1] - forms[1] IS the bindingVec directly
        if (forms[1] !== bindingVec) return false

        // Check if symbol is in an even position (binding symbols are at even indices)
        val bindings = PsiTreeUtil.getChildrenOfType(bindingVec, PhelForm::class.java) ?: return false

        var i = 0
        while (i < bindings.size) {
            val bindingSymbol = PsiTreeUtil.findChildOfType(bindings[i], PhelSymbol::class.java)
            if (bindingSymbol === symbol) {
                return true // Found symbol at even index (binding position)
            }
            i += 2
        }

        return false
    }

    /**
     * Set the name of a symbol (for rename refactoring)
     */
    @JvmStatic
    @Suppress("UNUSED_PARAMETER")
    fun setName(symbol: PhelSymbol, newName: String): PsiElement {
        // For now, return the element unchanged
        // Full implementation would create a new element with the new name
        return symbol
    }

    /**
     * Get the name identifier element for a symbol
     */
    @JvmStatic
    fun getNameIdentifier(symbol: PhelSymbol): PsiElement? {
        // The symbol itself is the name identifier
        return symbol
    }

    /**
     * Get the text offset of a symbol
     */
    @JvmStatic
    fun getTextOffset(symbol: PhelSymbol): Int {
        return symbol.textOffset
    }

    /**
     * Get the text offset of a list
     */
    @JvmStatic
    fun getTextOffset(list: PhelList): Int {
        return list.textOffset
    }

    /**
     * Get the reference for a symbol (for go-to-definition)
     */
    @JvmStatic
    fun getReference(symbol: PhelSymbol): PsiReference? {
        // Return the symbol's existing reference if it implements PsiNamedElement
        return symbol.reference
    }

    /**
     * Get the first form in a list
     */
    @JvmStatic
    fun getFirst(list: PhelList): PhelForm? {
        val forms = PsiTreeUtil.getChildrenOfType(list, PhelForm::class.java)
        return if (forms != null && forms.isNotEmpty()) forms[0] else null
    }

    /**
     * Get the literal type of a literal element
     */
    @JvmStatic
    fun getLiteralType(literal: PhelLiteral): String? {
        val text = literal.text ?: return null
        
        return when {
            text.matches(Regex("[+-]?\\d+(\\.\\d*)?([eE][+-]?\\d+)?")) -> "number"
            text.matches(Regex("[+-]?0x[\\da-fA-F_]+")) -> "hexnum"
            text.matches(Regex("[+-]?0b[01_]+")) -> "binnum"
            text.matches(Regex("[+-]?0o[0-7_]+")) -> "octnum"
            text.startsWith("\"") && text.endsWith("\"") -> "string"
            text == "true" || text == "false" -> "boolean"
            text == "nil" -> "nil"
            text == "NAN" -> "nan"
            text.startsWith("\\") -> "char"
            else -> "unknown"
        }
    }

    /**
     * Get the literal text content (without quotes for strings, etc.)
     */
    @JvmStatic
    fun getLiteralText(literal: PhelLiteral): String? {
        val text = literal.text ?: return null
        val type = getLiteralType(literal)
        
        return when (type) {
            "string" -> if (text.length >= 2) text.substring(1, text.length - 1) else text
            "char" -> if (text.length > 1) text.substring(1) else text
            else -> text
        }
    }

    /**
     * Get string representation for forms
     */
    @JvmStatic
    fun toString(form: PhelForm): String {
        return "PhelForm(${form.text})"
    }

    /**
     * Get string representation for metadata
     */
    @JvmStatic
    fun toString(metadata: PhelMetadata): String {
        return "PhelMetadata(${metadata.text})"
    }

    /**
     * Get string representation for reader macros
     */
    @JvmStatic
    fun toString(readerMacro: PhelReaderMacro): String {
        return "PhelReaderMacro(${readerMacro.text})"
    }
}

