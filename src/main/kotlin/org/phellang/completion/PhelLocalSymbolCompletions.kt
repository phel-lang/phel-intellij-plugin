package org.phellang.completion

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import org.phellang.PhelFileType
import org.phellang.language.psi.PhelFile
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.impl.PhelAccessImpl
import javax.swing.Icon

/**
 * Local symbol completions for Phel - provides completions for parameters,
 * let bindings, and other local variables in the current scope
 */
object PhelLocalSymbolCompletions {
    private val PARAMETER_ICON = AllIcons.Nodes.Parameter
    private val LOCAL_VARIABLE_ICON = AllIcons.Nodes.Variable
    private val DEFINITION_ICON = AllIcons.Nodes.Method

    /**
     * Add local symbol completions based on current position
     * Enhanced with performance optimizations
     */
    @JvmStatic
    fun addLocalSymbols(result: CompletionResultSet, position: PsiElement) {
        // Comprehensive error handling for local symbol completion
        PhelCompletionErrorHandler.withErrorHandling(PhelCompletionErrorHandler.withResultSet {
            addLocalSymbolsWithValidation(result, position)
        }, "local symbol completion") {
            // Fallback: add basic local symbols if detailed analysis fails
            addBasicLocalFallback(result, position)
        }
    }

    @Throws(Exception::class)
    private fun addLocalSymbolsWithValidation(result: CompletionResultSet, position: PsiElement) {
        // Performance check - skip expensive operations if needed
        if (PhelCompletionPerformanceOptimizer.shouldSkipExpensiveOperations(position)) {
            return
        }

        // Validate element before processing
        check(PhelCompletionErrorHandler.isCompletionContextValid(position)) { "Invalid PSI element context for local symbol completion" }

        val addedSymbols: MutableSet<String?> = HashSet()

        // Use optimized binding collection
        val bindings: MutableMap<String?, String?> = HashMap()
        PhelCompletionPerformanceOptimizer.collectBindingsEfficiently(position, bindings)

        // Add optimized bindings to completion results
        for (entry in bindings.entries) {
            val symbolName: String = entry.key!!
            val bindingType: String = entry.value!!
            val icon = getIconForBindingType(bindingType)
            addSymbolCompletion(result, symbolName, bindingType, icon, addedSymbols)
        }

        // Add local definitions with performance limits
        addLocalDefinitionSymbolsOptimized(result, position, addedSymbols)

        // ALSO try a completely simple version without optimizations
        addLocalDefinitionSymbolsSimple(result, position, addedSymbols)

        // Add global definitions from other files in project (with limits for performance)
        addProjectGlobalDefinitions(result, position, addedSymbols)
    }

    /**
     * Add a symbol completion if not already added
     */
    private fun addSymbolCompletion(
        result: CompletionResultSet,
        symbolName: String,
        typeText: String,
        icon: Icon?,
        addedSymbols: MutableSet<String?>
    ) {
        if (!addedSymbols.contains(symbolName) && !symbolName.trim { it <= ' ' }.isEmpty()) {
            addedSymbols.add(symbolName)

            // Use smart ranking based on symbol type
            val priority = getSymbolPriority(typeText)

            // Create ranked element with local symbol styling
            val element: LookupElement = PhelCompletionRanking.createRankedElement(symbolName, typeText, null, icon)
                .withBoldness(true) // Local symbols are bold for visibility

            // Apply priority using IntelliJ's PrioritizedLookupElement
            result.addElement(PrioritizedLookupElement.withPriority(element, priority.value))
        }
    }

    /**
     * Basic fallback for local symbol completion when detailed analysis fails
     */
    private fun addBasicLocalFallback(result: CompletionResultSet, position: PsiElement) {
        try {
            // Try to add at least some basic local symbols using simple PSI traversal
            val currentFunctionName = getCurrentFunctionName(position)
            if (currentFunctionName != null && !currentFunctionName.isEmpty()) {
                result.addElement(
                    LookupElementBuilder.create(currentFunctionName).withIcon(PhelIconProvider.RECURSIVE_FUNCTION)
                        .withTailText(" (current function)", true)
                )
            }

            // Add common parameter names as fallback
            val commonParams = arrayOf<String?>("x", "y", "n", "item", "coll", "f", "pred")
            for (param in commonParams) {
                result.addElement(
                    LookupElementBuilder.create(param!!).withIcon(PhelIconProvider.PARAMETER)
                        .withTailText(" (common parameter)", true)
                )
            }
        } catch (_: Exception) {
            // Even fallback failed - provide minimal completions
        }
    }

    /**
     * Get priority based on symbol type
     */
    private fun getSymbolPriority(typeText: String): PhelCompletionRanking.Priority {
        return when (typeText) {
            "Parameter", "Function Parameter" -> PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS
            "Let Binding", "Local Variable", "Loop Binding" -> PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS
            "Catch Binding", "If-Let Binding" -> PhelCompletionRanking.Priority.NESTED_SCOPE_LOCALS
            "Function", "Function (recursive)" -> PhelCompletionRanking.Priority.CURRENT_FUNCTION_RECURSIVE
            "Local Definition" -> PhelCompletionRanking.Priority.RECENT_DEFINITIONS
            "Simple Global Variable" -> PhelCompletionRanking.Priority.PROJECT_SYMBOLS
            else -> PhelCompletionRanking.Priority.PROJECT_SYMBOLS
        }
    }

    /**
     * Add global definitions from other Phel files in the project
     */
    private fun addProjectGlobalDefinitions(
        result: CompletionResultSet, position: PsiElement, addedSymbols: MutableSet<String?>
    ) {
        val project = position.project

        // Skip in large projects for performance
        if (PhelCompletionPerformanceOptimizer.shouldSkipExpensiveOperations(position)) {
            return
        }

        try {
            // Iterate through all project files and filter by type
            val phelFiles: MutableCollection<VirtualFile> = ArrayList()
            ProjectRootManager.getInstance(project).fileIndex.iterateContent { virtualFile: VirtualFile? ->
                    if (phelFiles.size < 20 &&  // Limit for performance
                        virtualFile!!.fileType == PhelFileType.INSTANCE
                    ) {
                        phelFiles.add(virtualFile)
                    }
                    true
                }

            val psiManager = PsiManager.getInstance(project)
            val currentFile = position.containingFile

            var fileCount = 0
            val maxFilesToScan = 20 // Performance limit

            for (virtualFile in phelFiles) {
                if (fileCount >= maxFilesToScan) break

                // Skip current file - already processed
                if (virtualFile == currentFile.virtualFile) continue

                val psiFile = psiManager.findFile(virtualFile)
                if (psiFile is PhelFile) {
                    extractGlobalDefinitionsFromFile(psiFile, result, addedSymbols)
                    fileCount++
                }
            }
        } catch (_: Exception) {
            // Silently ignore errors to avoid disrupting completion
        }
    }

    /**
     * Extract global definitions from a specific Phel file
     */
    private fun extractGlobalDefinitionsFromFile(
        file: PhelFile, result: CompletionResultSet, addedSymbols: MutableSet<String?>
    ) {
        var definitionCount = 0
        val maxDefinitionsPerFile = 10

        for (child in file.children) {
            if (definitionCount >= maxDefinitionsPerFile) break

            if (child is PhelList) {
                val children: Array<PsiElement> = child.children
                if (children.size >= 2) {
                    val firstElement = children[0]

                    if (firstElement is PhelSymbol) {
                        val defType = firstElement.text

                        // Only include publicly accessible definitions
                        if (defType == "defn" || defType == "def" || defType == "defmacro" || defType == "defstruct") {
                            val nameElement = children[1]
                            if (nameElement is PhelSymbol) {
                                val symbolName = nameElement.text
                                val displayType =
                                    if (defType == "def") "Global Variable" else if (defType == "defn") "Public Function" else if (defType == "defmacro") "Public Macro" else "Public Definition"


                                // Add file context
                                val fileName = file.name.replace(".phel", "")
                                val typeWithContext = "$displayType ($fileName)"

                                addSymbolCompletion(result, symbolName, typeWithContext, DEFINITION_ICON, addedSymbols)
                                definitionCount++
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Simple version for scanning all local definitions in current file
     */
    private fun addLocalDefinitionSymbolsSimple(
        result: CompletionResultSet, position: PsiElement, addedSymbols: MutableSet<String?>
    ) {
        val file = position.containingFile as PhelFile? ?: return

        // Just iterate through all children without any limits
        for (child in file.children) {
            if (child is PhelList) {
                // Get children array and look for def + variable pattern  
                val children: Array<PsiElement> = child.children

                // Look for (def symbol-name ...) pattern 
                for (i in 0..<children.size - 1) {
                    val defChild = children[i]
                    if ((defChild is PhelSymbol || defChild is PhelAccessImpl) && "def" == defChild.text) {
                        // Check next element for the variable name

                        if (i + 1 < children.size) {
                            val nameChild = children[i + 1]

                            if (nameChild is PhelSymbol || nameChild is PhelAccessImpl) {
                                val symbolName = nameChild.text
                                addSymbolCompletion(
                                    result, symbolName, "Simple Global Variable", DEFINITION_ICON, addedSymbols
                                )
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get icon for binding type
     */
    private fun getIconForBindingType(bindingType: String): Icon {
        return if (bindingType.contains("Parameter")) {
            PARAMETER_ICON
        } else if (bindingType.contains("Definition")) {
            DEFINITION_ICON
        } else {
            LOCAL_VARIABLE_ICON
        }
    }

    /**
     * Get the name of the function currently being defined at the given position
     * This is used to mark recursive function calls with a special indicator
     */
    private fun getCurrentFunctionName(position: PsiElement): String? {
        var current: PsiElement? = position


        // Walk up the PSI tree to find the containing function definition
        while (current != null) {
            if (current is PhelList) {
                val list = current
                val children: Array<PsiElement> = list.children

                if (children.size >= 2) {
                    val firstElement = children[0]

                    // Check if this is a function definition
                    if (firstElement is PhelSymbol || firstElement is PhelAccessImpl) {
                        val defType = firstElement.text
                        if (defType == "defn" || defType == "defn-" || defType == "defmacro" || defType == "defmacro-") {
                            // Get the function name (second element)
                            val nameElement = children[1]
                            if (nameElement is PhelSymbol || nameElement is PhelAccessImpl) {
                                return nameElement.text
                            }
                        }
                    }
                }
            }
            current = current.parent
        }

        return null // Not inside a function definition
    }

    /**
     * Optimized local definition symbols - limits file scanning
     */
    private fun addLocalDefinitionSymbolsOptimized(
        result: CompletionResultSet, position: PsiElement, addedSymbols: MutableSet<String?>
    ) {
        val file = position.containingFile as PhelFile?
        if (file != null) {
            // Limit the number of top-level definitions we process
            var definitionCount = 0
            val maxDefinitions = 50

            for (child in file.children) {
                if (definitionCount >= maxDefinitions) break

                if (child is PhelList) {
                    val children: Array<PsiElement> = child.children

                    // Check if we have at least 3 elements (opening paren, def-type, symbol-name)
                    if (children.size >= 3) {
                        // Look for definition type starting from first child
                        var nameElement: PsiElement?

                        // Find the definition type element (def, defn, etc.)
                        for (i in 0..<children.size - 1) {
                            val childElement = children[i]
                            if (childElement is PhelSymbol || childElement is PhelAccessImpl) {
                                val text = childElement.text

                                if (text == "defn" || text == "defn-" || text == "def" || text == "defmacro" || text == "defmacro-" || text == "defstruct") {

                                    // Name should be the next element
                                    if (i + 1 < children.size) {
                                        nameElement = children[i + 1]

                                        if (nameElement is PhelSymbol || nameElement is PhelAccessImpl) {
                                            val symbolName = nameElement.text
                                            var displayType =
                                                if (text == "def") "Global Variable" else if (text == "defn" || text == "defn-") "Function" else if (text == "defmacro" || text == "defmacro-") "Macro" else "Definition"
                                            // Check if this is the current function being defined (for recursive calls)
                                            val currentFunctionName = getCurrentFunctionName(position)
                                            if (currentFunctionName != null && currentFunctionName == symbolName) {
                                                displayType += " (recursive)"
                                            }

                                            addSymbolCompletion(
                                                result, symbolName, displayType, DEFINITION_ICON, addedSymbols
                                            )
                                            definitionCount++
                                            break // Found what we need
                                        }
                                    }
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
