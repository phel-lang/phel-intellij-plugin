package org.phellang.completion.engine

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import org.phellang.registry.PhelCompletionPriority
import org.phellang.completion.infrastructure.PhelCompletionUtils
import org.phellang.core.psi.PhelSymbolAnalyzer
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.impl.PhelAccessImpl
import javax.swing.Icon

private val FUNCTION_INTRO_FORMS = PhelSpecialForms.FUNCTION_DEFINING

object PhelLocalSymbolCompletions {
    val PARAMETER_ICON = AllIcons.Nodes.Parameter
    val VARIABLE_ICON = AllIcons.Nodes.Variable

    /**
     * Symbols bound in the file being edited: parameters, let/loop bindings, and its own
     * top-level definitions. All of these are legal to type unqualified, and all are found by
     * walking PSI the user already has parsed.
     *
     * Symbols from *other* files are deliberately not handled here — [PhelProjectCompletionHelper]
     * serves those from the symbol index, namespace-qualified and with auto-import, which is the
     * only spelling that actually compiles.
     */
    @JvmStatic
    fun addLocalSymbols(result: CompletionResultSet, position: PsiElement) {
        val addedSymbols: MutableSet<String> = HashSet()

        // First priority: Add function parameters from current scope
        addCurrentFunctionParameters(result, position, addedSymbols)

        // Second priority: Add let bindings from current scope
        addCurrentLetBindings(result, position, addedSymbols)

        // Third priority: Add this file's own top-level definitions
        addLocalDefinitionSymbolsSimple(result, position, addedSymbols)
    }

    private fun addSymbolCompletion(
        result: CompletionResultSet,
        symbolName: String,
        typeText: String,
        icon: Icon?,
        addedSymbols: MutableSet<String>
    ) {
        if (!addedSymbols.contains(symbolName) && symbolName.isNotBlank()) {
            addedSymbols.add(symbolName)

            PhelCompletionUtils.addLocalSymbolCompletion(result, symbolName, typeText, icon)
        }
    }

    private fun addCurrentFunctionParameters(
        result: CompletionResultSet, position: PsiElement, addedSymbols: MutableSet<String>
    ) {
        var current = position.parent
        var depth = 0

        while (current != null && depth < 10) {
            if (current is PhelList) {
                val children = current.children
                if (children.isNotEmpty()) {
                    val firstChild = children[0]
                    if (firstChild is PhelSymbol || firstChild is PhelAccessImpl) {
                        val functionType = firstChild.text

                        if (functionType in FUNCTION_INTRO_FORMS) {
                            val paramVec = PhelSymbolAnalyzer.findParameterVector(current) ?: break
                            val paramChildren = paramVec.children
                            for (paramChild in paramChildren) {
                                if (paramChild !is PhelSymbol && paramChild !is PhelAccessImpl) continue

                                val paramName = paramChild.text
                                if (paramName.isNullOrEmpty()) continue

                                addSymbolCompletion(
                                    result,
                                    paramName,
                                    "Function Parameter",
                                    PARAMETER_ICON,
                                    addedSymbols
                                )
                            }
                            break // Found the function, stop looking
                        }
                    }
                }
            }
            current = current.parent
            depth++
        }
    }

    private fun addCurrentLetBindings(
        result: CompletionResultSet, position: PsiElement, addedSymbols: MutableSet<String>
    ) {
        var current = position.parent
        var depth = 0

        while (current != null && depth < 10) {
            if (current is PhelList) {
                val children = current.children
                if (children.isNotEmpty()) {
                    val firstChild = children[0]
                    if (firstChild is PhelSymbol || firstChild is PhelAccessImpl) {
                        val bindingType = firstChild.text

                        if (bindingType == "let" || bindingType == "for" || bindingType == "loop" || bindingType == "binding") {
                            if (children.size > 1) {
                                val bindingElement = children[1]
                                if (bindingElement is PhelVec) {
                                    // Extract bindings from the vector (every other element is a binding name)
                                    val bindingChildren = bindingElement.children
                                    for (i in bindingChildren.indices step 2) {
                                        val bindingChild = bindingChildren[i]
                                        if (bindingChild !is PhelSymbol && bindingChild !is PhelAccessImpl) continue

                                        val bindingName = bindingChild.text
                                        if (bindingName.isNullOrEmpty()) continue

                                        val typeText = when (bindingType) {
                                            "let" -> "Let Binding"
                                            "loop" -> "Loop Binding"
                                            else -> "Local Variable"
                                        }
                                        addSymbolCompletion(
                                            result, bindingName, typeText, VARIABLE_ICON, addedSymbols
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            current = current.parent
            depth++
        }
    }

    private fun addLocalDefinitionSymbolsSimple(
        result: CompletionResultSet, position: PsiElement, addedSymbols: MutableSet<String>
    ) {
        val file = position.containingFile as? PhelFile ?: return

        for (child in file.children) {
            if (child !is PhelList) continue
            val children: Array<PsiElement> = child.children

            if (children.size < 2) continue

            val firstElement = children[0]

            if (firstElement !is PhelSymbol && firstElement !is PhelAccessImpl) continue

            val defType = firstElement.text
            val localDefinitionTypes = arrayOf(
                "def",
                "defn",
                "defn-",
                "defmacro",
                "defmacro-",
                "defexception",
                "defexception*",
                "definterface",
                "definterface*",
                "defstruct",
                "defstruct*"
            )

            if (!localDefinitionTypes.contains(defType)) continue

            val nameElement = children[1]
            if (nameElement !is PhelSymbol && nameElement !is PhelAccessImpl) continue

            val symbolName = nameElement.text
            val priority = when (defType) {
                "defn", "defn-", "defmacro", "defmacro-" -> PhelCompletionPriority.RECENT_DEFINITIONS
                else -> PhelCompletionPriority.PROJECT_SYMBOLS
            }

            val displayType = when (defType) {
                "def" -> "Local Variable"
                "defn", "defn-" -> "Local Function"
                "defmacro", "defmacro-" -> "Local Macro"
                "defexception", "defexception*" -> "Local Exception"
                "definterface", "definterface*" -> "Local Interface"
                "defstruct", "defstruct*" -> "Local Struct"
                else -> "Local Definition"
            }

            if (addedSymbols.contains(symbolName) || symbolName.trim().isEmpty()) continue

            addedSymbols.add(symbolName)
            PhelCompletionUtils.addRankedCompletion(
                result, symbolName, "", displayType, priority
            )
        }
    }

}