package org.phellang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext
import org.phellang.PhelIcons
import org.phellang.PhelLanguage
import org.phellang.language.psi.*
import org.phellang.language.psi.impl.PhelAccessImpl
import java.util.*
import javax.swing.Icon
import kotlin.math.max
import kotlin.math.min

/**
 * Main completion contributor for Phel language
 * Provides intelligent completions for API functions, PHP interop, and language constructs
 */
class PhelCompletionContributor : CompletionContributor() {
    /**
     * Custom insert handler for namespaced completions to prevent text duplication
     * Handles completions containing '/' by properly replacing the typed prefix
     */
    class NamespacedInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            // Find the start of the current symbol
            var symbolStart = caretOffset
            val text = document.charsSequence

            // Move back to find the beginning of the symbol
            while (symbolStart > 0) {
                val c = text[symbolStart - 1]
                // Stop at whitespace, opening parenthesis, or other delimiters
                if (Character.isWhitespace(c) || c == '(' || c == '[' || c == '{') {
                    break
                }
                symbolStart--
            }

            // Replace the entire symbol with the completion
            val endOffset = context.tailOffset
            val completionText = item.lookupString

            document.replaceString(symbolStart, endOffset, completionText)
            editor.caretModel.moveToOffset(symbolStart + completionText.length)
        }
    }

    /**
     * Custom insert handler for balanced parentheses - inserts "()" and positions cursor inside
     */
    class BalancedParenthesesInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            // Replace the completion with "()" 
            val completionText = "()"
            document.replaceString(caretOffset, context.tailOffset, completionText)

            // Position cursor between the parentheses
            editor.caretModel.moveToOffset(caretOffset + 1)
        }
    }

    /**
     * Custom insert handler for balanced brackets - inserts "[]" and positions cursor inside
     */
    class BalancedBracketsInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            // Replace the completion with "[]" 
            val completionText = "[]"
            document.replaceString(caretOffset, context.tailOffset, completionText)

            // Position cursor between the brackets
            editor.caretModel.moveToOffset(caretOffset + 1)
        }
    }

    /**
     * Template insert handler for defn - inserts "(defn name [params] body)" with "name" selected as placeholder
     */
    class DefnTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(defn name [] ())"
            document.replaceString(caretOffset, context.tailOffset, template)

            // Select "name" placeholder for immediate editing
            val nameStart = caretOffset + 6 // "(defn ".length()
            val nameEnd = nameStart + 4 // "name".length()
            editor.selectionModel.setSelection(nameStart, nameEnd)
            editor.caretModel.moveToOffset(nameEnd)
        }
    }

    /**
     * Template insert handler for def - inserts "(def name value)" with "name" selected as placeholder
     */
    class DefTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(def name )"
            document.replaceString(caretOffset, context.tailOffset, template)

            // Select "name" placeholder for immediate editing
            val nameStart = caretOffset + 5 // "(def ".length()
            val nameEnd = nameStart + 4 // "name".length()
            editor.selectionModel.setSelection(nameStart, nameEnd)
            editor.caretModel.moveToOffset(nameEnd)
        }
    }

    /**
     * Template insert handler for let - inserts "(let [bindings] body)" with "bindings" selected as placeholder
     */
    class LetTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(let [bindings] )"
            document.replaceString(caretOffset, context.tailOffset, template)

            // Select "bindings" placeholder for immediate editing
            val bindingsStart = caretOffset + 6 // "(let [".length()
            val bindingsEnd = bindingsStart + 8 // "bindings".length()
            editor.selectionModel.setSelection(bindingsStart, bindingsEnd)
            editor.caretModel.moveToOffset(bindingsEnd)
        }
    }

    /**
     * Template insert handler for if - inserts "(if condition then else)" with "condition" selected as placeholder
     */
    class IfTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(if condition) "
            document.replaceString(caretOffset, context.tailOffset, template)

            // Select "condition" placeholder for immediate editing
            val conditionStart = caretOffset + 4 // "(if ".length()
            val conditionEnd = conditionStart + 9 // "condition".length()
            editor.selectionModel.setSelection(conditionStart, conditionEnd)
            editor.caretModel.moveToOffset(conditionEnd)
        }
    }

    /**
     * Template insert handler for fn - inserts "(fn [params] body)" with "params" selected as placeholder
     */
    class FnTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(fn [params] )"
            document.replaceString(caretOffset, context.tailOffset, template)

            // Select "params" placeholder for immediate editing
            val paramsStart = caretOffset + 5 // "(fn [".length()
            val paramsEnd = paramsStart + 6 // "params".length()
            editor.selectionModel.setSelection(paramsStart, paramsEnd)
            editor.caretModel.moveToOffset(paramsEnd)
        }
    }

    /**
     * Template insert handler for when - inserts "(when condition body)" with "condition" selected as placeholder
     */
    class WhenTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(when condition )"
            document.replaceString(caretOffset, context.tailOffset, template)

            // Select "condition" placeholder for immediate editing
            val conditionStart = caretOffset + 6 // "(when ".length()
            val conditionEnd = conditionStart + 9 // "condition".length()
            editor.selectionModel.setSelection(conditionStart, conditionEnd)
            editor.caretModel.moveToOffset(conditionEnd)
        }
    }

    /**
     * Template insert handler for :require-file - inserts "(:require-file )" with cursor positioned inside
     */
    class RequireFileTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(:require-file )"
            document.replaceString(caretOffset, context.tailOffset, template)

            // Position cursor inside the parentheses, after the space
            val cursorPos = caretOffset + 15 // "(:require-file ".length()
            editor.caretModel.moveToOffset(cursorPos)
        }
    }

    /**
     * Template insert handler for :require - inserts "(:require )" with cursor positioned inside
     */
    class RequireTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(:require )"
            document.replaceString(caretOffset, context.tailOffset, template)

            // Position cursor inside the parentheses, after the space
            val cursorPos = caretOffset + 10 // "(:require ".length()
            editor.caretModel.moveToOffset(cursorPos)
        }
    }

    /**
     * Template insert handler for :use - inserts "(:use )" with cursor positioned inside
     */
    class UseTemplateInsertHandler : InsertHandler<LookupElement?> {
        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            val editor = context.editor
            val document = editor.document
            val caretOffset = context.startOffset

            val template = "(:use )"
            document.replaceString(caretOffset, context.tailOffset, template)

            // Position cursor inside the parentheses, after the space
            val cursorPos = caretOffset + 6 // "(:use ".length()
            editor.caretModel.moveToOffset(cursorPos)
        }
    }

    init {
        // Complete symbols (functions, variables, etc.)
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PhelTypes.SYM).withLanguage(PhelLanguage.INSTANCE),
            PhelSymbolCompletionProvider()
        )

        // Complete after colon (keywords)
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PhelTypes.SYM).afterLeaf(PlatformPatterns.psiElement(PhelTypes.COLON))
                .withLanguage(
                    PhelLanguage.INSTANCE
                ),
            PhelKeywordCompletionProvider()
        )

        // Complete after namespace colon
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PhelTypes.SYM).afterLeaf(PlatformPatterns.psiElement(PhelTypes.COLONCOLON))
                .withLanguage(
                    PhelLanguage.INSTANCE
                ),
            PhelNamespaceKeywordCompletionProvider()
        )
    }

    /**
     * Core symbol completion provider - handles functions, macros, and built-ins
     * Enhanced with local symbol completion and smart priority ordering
     */
    private class PhelSymbolCompletionProvider : CompletionProvider<CompletionParameters?>() {
        override fun addCompletions(
            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
        ) {
            // Comprehensive error handling for completion operation
            PhelCompletionErrorHandler.withErrorHandling(
                PhelCompletionErrorHandler.withResultSet {
                    performCompletionWithValidation(parameters, result)
                }, "main completion process"
            ) {
                // Fallback: provide basic completions if detailed completion fails
                addBasicFallbackCompletions(result)
            }
        }

        @Throws(Exception::class)
        fun performCompletionWithValidation(parameters: CompletionParameters, result: CompletionResultSet) {
            val element = parameters.position

            // Validate completion context before proceeding
            check(PhelCompletionErrorHandler.isCompletionContextValid(element)) { "Invalid completion context: PSI element or tree is malformed" }

            val prefix = getCompletionPrefix(element)

            if (shouldSuggestNewForm(element, prefix)) {
                addNewFormCompletion(result)
                // Also add template completions at top level for convenience
                addTemplateCompletions(result)
                return  // Don't show any other completions at top level
            }

            // Check if we're at the parameter vector position in definition forms
            if (isInParameterVectorPosition(element)) {
                addParameterVectorCompletion(result)
                return  // Only suggest [ for parameter vector
            }

            // Check if we're inside a parameter vector
            val insideParamVector = isInsideParameterVector(element)
            if (insideParamVector) {
                addParameterCompletions(result, element)
                return  // Only suggest parameter names and ] (if unclosed)
            }

            // Check if we're in a namespace (ns) context - special handling needed
            if (isInNamespaceContext(element)) {
                addNamespaceCompletions(result, element)
                return  // Early return for namespace contexts
            }

            // Check if we're inside function arguments where only values are valid
            val functionContext = getFunctionCallContext(element)
            if (functionContext != null && functionContext.isInArgumentPosition) {
                // Inside function arguments - only show values, not function names
                addArgumentCompletions(result, element)
                return  // Don't show function names - they're invalid syntax here
            }

            // Check if we're in a definition name position (defn name, def name, etc.)
            if (isInDefinitionNamePosition(element)) {
                // In definition name position - don't suggest existing names to avoid conflicts
                addDefinitionNameHints(result)
                return  // Don't show existing function names - they would cause conflicts
            }

            // Determine if we're in a context where raw function names are valid
            val isInFunctionNameContext = isInFunctionNameContext(element)

            if (!isInFunctionNameContext) {
                // CRITICAL: Add local symbols FIRST in expression contexts (parameters, let bindings, etc.)
                PhelLocalSymbolCompletions.addLocalSymbols(result, element)

                // Then add basic values that are valid in expression contexts
                addBasicValues(result)

                // Only add API functions if we're in an argument context (like filter predicates)
                // NOT in definition bodies where bare function names are invalid syntax
                val inArgContext = isInArgumentContext(element)
                if (inArgContext) {
                    // Add context-aware API functions in argument positions
                    // This fixes filter context where predicates should be suggested
                    PhelApiCompletions.addCoreFunctions(result, element)
                    PhelApiCompletions.addPredicateFunctions(result, element)
                    PhelApiCompletions.addCollectionFunctions(result)
                    PhelApiCompletions.addArithmeticFunctions(result)
                }

                // Add structural completions
                if (isInDefinitionBodyContext(element)) {
                    addDefinitionBodyCompletion(result)
                } else {
                    addExpressionCompletion(result)
                }

                // Add template completions for common constructs (only in expression contexts)
                addTemplateCompletions(result)

                return  // Early return for expression contexts
            }

            // Parameter hints and function signatures - when inside function calls
            PhelParameterHintProvider.addParameterHints(result, element)
            PhelParameterHintProvider.addPositionHints(result, element)

            // Local symbols (parameters, let bindings, local definitions) - added first for highest priority
            PhelLocalSymbolCompletions.addLocalSymbols(result, element)

            // Basic values that are valid in expression contexts
            addBasicValues(result)

            // PRIORITY 4: Only add API functions if we're in a context where function names are valid
            // Special forms and core macros - important language constructs
            PhelLanguageCompletions.addSpecialForms(result)
            PhelLanguageCompletions.addCoreMacros(result)

            // Core API functions with context-aware ranking
            PhelApiCompletions.addCoreFunctions(result, element)
            PhelApiCompletions.addPredicateFunctions(result, element)
            PhelApiCompletions.addCollectionFunctions(result)
            PhelApiCompletions.addArithmeticFunctions(result)

            // Namespace functions
            PhelNamespaceCompletions.addNamespaceFunctions(result)

            // PHP interop - lower priority
            PhelPhpInteropCompletions.addPhpInterop(result)
        }

        fun getCompletionPrefix(element: PsiElement): String {
            var text = element.text
            // Remove IntelliJ's dummy identifier completely (with or without space)
            text = text.replace("IntellijIdeaRulezzz ", "").replace("IntellijIdeaRulezzz", "").trim { it <= ' ' }
            return text
        }

        /**
         * Check if we should suggest starting a new form with "("
         * Simple rule: if we're at top level (not inside a PhelList) and no prefix, suggest "("
         */
        fun shouldSuggestNewForm(element: PsiElement?, prefix: String): Boolean {
            // If user has already typed something, don't suggest "()" and other constructions
            if (prefix.isNotEmpty()) {
                return false
            }

            // Check if we're at the top level (not inside any PhelList)
            var current = element
            while (current != null) {
                if (current is PhelList) {
                    return false // We're inside an existing form
                }
                current = current.parent
            }

            return true // We're at top level, suggest "("
        }

        /**
         * Add completion for starting a new form - the ONLY valid syntax at top level
         */
        fun addNewFormCompletion(result: CompletionResultSet) {
            val element =
                LookupElementBuilder.create("()").withPresentableText("()").withTypeText("Start new expression")
                    .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER).withIcon(PhelIcons.FILE).bold()

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    element, PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS.value
                )
            )
        }

        /**
         * Add completion for expressions in function bodies, let bodies, etc. - balanced ()
         */
        fun addExpressionCompletion(result: CompletionResultSet) {
            val balancedParen =
                LookupElementBuilder.create("()").withPresentableText("()").withTypeText("Start new expression")
                    .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER).withIcon(PhelIcons.FILE)

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    balancedParen, PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS.value
                )
            )
        }

        /**
         * Add completion for definition bodies (defn, defmacro) - suggest balanced ()
         */
        fun addDefinitionBodyCompletion(result: CompletionResultSet) {
            // Focus on balanced parentheses for new expressions - users can manually type ) to close
            val balancedParen =
                LookupElementBuilder.create("()").withPresentableText("()").withTypeText("Start new expression")
                    .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER).withIcon(PhelIcons.FILE)

            result.addElement(
                PrioritizedLookupElement.withPriority(
                    balancedParen, PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS.value
                )
            )
        }

        /**
         * Add template completions for common Phel constructs
         */
        fun addTemplateCompletions(result: CompletionResultSet) {
            // defn template - (defn name [params] body)
            val defnTemplate = LookupElementBuilder.create("defn-template").withPresentableText("defn")
                .withTypeText("Function definition template").withTailText(" (defn name [params] body)", true)
                .withInsertHandler(DEFN_TEMPLATE_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    defnTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value
                )
            )


            // def template - (def name value)
            val defTemplate = LookupElementBuilder.create("def-template").withPresentableText("def")
                .withTypeText("Variable definition template").withTailText(" (def name value)", true)
                .withInsertHandler(DEF_TEMPLATE_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    defTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value
                )
            )


            // let template - (let [bindings] body)
            val letTemplate = LookupElementBuilder.create("let-template").withPresentableText("let")
                .withTypeText("Let binding template").withTailText(" (let [bindings] body)", true)
                .withInsertHandler(LET_TEMPLATE_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    letTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value
                )
            )


            // if template - (if condition then else)
            val ifTemplate = LookupElementBuilder.create("if-template").withPresentableText("if")
                .withTypeText("Conditional template").withTailText(" (if condition then else)", true)
                .withInsertHandler(IF_TEMPLATE_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    ifTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value
                )
            )


            // fn template - (fn [params] body)
            val fnTemplate = LookupElementBuilder.create("fn-template").withPresentableText("fn")
                .withTypeText("Anonymous function template").withTailText(" (fn [params] body)", true)
                .withInsertHandler(FN_TEMPLATE_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    fnTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value
                )
            )


            // when template - (when condition body)
            val whenTemplate = LookupElementBuilder.create("when-template").withPresentableText("when")
                .withTypeText("Conditional template").withTailText(" (when condition body)", true)
                .withInsertHandler(WHEN_TEMPLATE_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    whenTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value
                )
            )
        }

        /**
         * Check if we're in a definition body context (defn, defmacro, fn)
         * where both ( and ) should be suggested for function bodies
         */
        fun isInDefinitionBodyContext(element: PsiElement?): Boolean {
            var current = element
            while (current != null) {
                if (current is PhelList) {
                    val list = current
                    val children: Array<PsiElement> = list.children

                    if (children.isNotEmpty()) {
                        val firstChild = children[0]
                        if (firstChild is PhelSymbol || firstChild is PhelAccessImpl) {
                            val text = firstChild.text
                            if (text == "defn" || text == "defn-" || text == "defmacro" || text == "defmacro-" || text == "deftest") {
                                // Check if we're in the body position (after parameters)
                                val cursorPosition = getCursorPositionInList(list, element)
                                // defn/defmacro: name [params] body (position 3+)
                                // deftest: test-name body (position 2+) 
                                val bodyStartPosition = if (text == "deftest") 2 else 3
                                return cursorPosition >= bodyStartPosition
                            }
                        }
                    }
                }
                current = current.parent
            }
            return false
        }

        /**
         * Check if we're at the parameter vector position in definition forms like defn, defmacro
         * e.g., (defn factorial | should suggest [
         */
        fun isInParameterVectorPosition(element: PsiElement?): Boolean {
            var current = element
            while (current != null) {
                if (current is PhelList) {
                    val list = current
                    val children: Array<PsiElement> = list.children

                    if (children.size >= 2) {
                        // Find the definition type (defn, defmacro, etc.)
                        var defType: String? = null
                        var defTypeIndex = -1

                        for (i in children.indices) {
                            val child = children[i]
                            if (child is PhelSymbol || child is PhelAccessImpl) {
                                val text = child.text
                                if (text == "defn" || text == "defn-" || text == "defmacro" || text == "defmacro-" || text == "fn") {
                                    defType = text
                                    defTypeIndex = i
                                    break
                                }
                            }
                        }

                        if (defType != null) {
                            // Check cursor position relative to the definition
                            val cursorPosition = getCursorPositionInList(list, element)


                            // CRITICAL: Check if we're inside an existing PhelVec at the parameter position
                            val paramVectorPosition = if (defType == "fn") defTypeIndex + 1 else defTypeIndex + 2

                            if (cursorPosition == paramVectorPosition) {
                                // Check if there's already a PhelVec at this position
                                return if (paramVectorPosition < children.size && children[paramVectorPosition] is PhelVec) {
                                    false // We're inside the vector, not at the position to create it
                                } else {
                                    true // We're at the position to create the parameter vector
                                }
                            }
                        }
                    }
                }
                current = current.parent
            }
            return false
        }

        /**
         * Add parameter vector completion - suggests [ to start parameter list
         */
        fun addParameterVectorCompletion(result: CompletionResultSet) {
            val element = LookupElementBuilder.create("[").withPresentableText("[]").withTypeText("Function parameters")
                .withTailText(" Parameter vector", true).withInsertHandler(BALANCED_BRACKETS_INSERT_HANDLER)
                .withIcon(PhelIcons.FILE)

            result.addElement(element)
        }

        /**
         * Check if we're inside a parameter/binding vector [...]
         * e.g., (defn factorial [n |), (let [a |), (for [x |), (loop [i |) should suggest parameter names and ]
         */
        fun isInsideParameterVector(element: PsiElement?): Boolean {
            // Find the MOST IMMEDIATE/CLOSEST PhelVec ancestor
            // This is crucial for nested structures like (let [a (let [b (let [c ] |
            var closestVector: PhelVec? = null
            var current = element


            // Find all PhelVec ancestors and choose the best one
            val vectors: MutableList<PhelVec> = ArrayList<PhelVec>()
            while (current != null) {
                if (current is PhelVec) {
                    vectors.add(current)
                }
                current = current.parent
            }


            // Choose the best vector from our candidates
            for (vector in vectors) {
                val vectorText = vector.text


                // Skip malformed giant vectors
                if (vectorText.length > 1000) {
                    continue
                }


                // Priority 1: Small completed vectors that are probably inner vectors like [c]
                if (vectorText.endsWith("]") && vectorText.length < 20) {
                    // But check if cursor is actually after this vector (outside it)
                    val cursorOffset = element!!.textOffset
                    val vectorStart = vector.textOffset
                    val cursorOffsetInVector = cursorOffset - vectorStart

                    if (cursorOffsetInVector >= vectorText.length - 1) {
                        continue  // Keep looking for a parent vector we're actually inside
                    } else {
                        closestVector = vector
                        break
                    }
                } else if (!vectorText.endsWith("]") && vectorText.length < 500) {
                    closestVector = vector
                    break // This is probably the vector we're inside that needs closing
                } else if (vectorText.length < 100) {
                    if (closestVector == null) {
                        closestVector = vector
                    }
                }
            }

            if (closestVector == null) {
                return false // No vector ancestor
            }

            val vectorText = closestVector.text

            // ROBUSTNESS: Handle malformed/incomplete syntax where vectors are huge due to missing closing brackets
            if (vectorText.length > 1000) {
                return false // Probably malformed syntax with missing closing brackets
            }

            // Calculate cursor position relative to the vector start
            val cursorOffset = element!!.textOffset
            val vectorStart = closestVector.textOffset
            val cursorOffsetInVector = cursorOffset - vectorStart
            val vectorLength = vectorText.length

            // If vector ends with ] and cursor is at or after the closing ], we're NOT inside
            if (vectorText.endsWith("]") && cursorOffsetInVector >= vectorLength - 1) {
                return false // We're after the closing ], not inside
            }

            // Check if this vector is in a parameter/binding position
            val parent = closestVector.parent
            if (parent is PhelList) {
                val children: Array<PsiElement> = parent.children

                // Look for definition/binding keywords
                for (i in children.indices) {
                    val child = children[i]

                    if (child is PhelSymbol || child is PhelAccessImpl) {
                        val text = child.text
                        if (text == "defn" || text == "defn-" || text == "defmacro" || text == "defmacro-" || text == "fn" || text == "let" || text == "for" || text == "loop") {
                            return true // This vector is in a binding context and cursor is inside
                        }
                    }
                }
            }

            return false
        }

        /**
         * Check if we're inside a parameter vector that is already closed: [param|]
         */
        fun isInsideClosedParameterVector(element: PsiElement?): Boolean {
            // Find the immediate PhelVec ancestor
            var current = element
            while (current != null) {
                if (current is PhelVec) {
                    val vector = current
                    val vectorText = vector.text.trim { it <= ' ' }

                    // Check if this vector ends with ] (is closed)
                    if (vectorText.endsWith("]")) {
                        // Verify that the cursor is actually INSIDE this closed vector
                        val cursorOffset = element!!.textOffset
                        val vectorStart = vector.textOffset
                        val vectorEnd = vectorStart + vectorText.length


                        // If cursor is before the closing bracket, we're inside a closed vector
                        if (cursorOffset >= vectorStart && cursorOffset < vectorEnd - 1) {
                            return true
                        }
                    }

                    // Check parent vector if current one doesn't match
                    current = current.parent
                } else {
                    current = current.parent
                }
            }

            return false
        }

        /**
         * Add parameter completions - suggests parameter names and closing ] (if vector is unclosed)
         */
        fun addParameterCompletions(result: CompletionResultSet, element: PsiElement?) {
            // Check if we're inside a closed parameter vector [param|]

            val isVectorClosed = isInsideClosedParameterVector(element)


            // Only suggest closing bracket if the vector is NOT already closed
            if (!isVectorClosed) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    "]",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,  // High priority for structural elements like ]
                    null,  // No signature
                    "Close parameter vector",
                    PhelIcons.FILE
                )
            }


            // Suggest common parameter names
            val commonParams = arrayOf<String?>(
                "x", "y", "z",  // Generic single values
                "n", "m", "i", "j",  // Numeric/index values  
                "coll", "xs", "ys",  // Collections
                "f", "pred",  // Functions/predicates
                "key", "val", "kv",  // Key-value pairs
                "a", "b", "c",  // Generic arguments
                "item", "elem",  // Collection elements
                "acc", "result",  // Accumulators
                "param", "param3", "param4", "parameter", "params" // Parameter-like names
            )

            for (param in commonParams) {
                // Use smart ranking for parameter names - lower priority than closing bracket
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    param!!,
                    PhelCompletionRanking.Priority.PROJECT_SYMBOLS,  // Lower priority for parameter suggestions (25.0)
                    null,  // No signature
                    "Parameter name",
                    PhelIconProvider.PARAMETER
                )
            }
        }

        /**
         * Check if we're in a context where raw function names are valid
         * (like at the start of an expression after "(")
         */
        fun isInFunctionNameContext(element: PsiElement?): Boolean {
            // Walk up to find the containing list
            var current = element
            while (current != null && current !is PhelList) {
                current = current.parent
            }

            if (current is PhelList) {
                val list = current

                // Find cursor position in list
                val cursorPosition = getCursorPositionInList(list, element)

                // Check if we're at position 0 (right after opening paren)
                // That's where function names are valid
                val atFunctionPosition = (cursorPosition == 0)

                return atFunctionPosition
            }

            return false
        }

        /**
         * Add basic values that are always valid in expression contexts
         */
        fun addBasicValues(result: CompletionResultSet) {
            // Basic literals with smart ranking
            PhelCompletionRanking.addRankedCompletion(
                result,
                "nil",
                PhelCompletionRanking.Priority.COMMON_BUILTINS,
                "nil value",
                null,
                AllIcons.Nodes.Constant
            )

            PhelCompletionRanking.addRankedCompletion(
                result, "true", PhelCompletionRanking.Priority.COMMON_BUILTINS, "boolean", null, AllIcons.Nodes.Constant
            )

            PhelCompletionRanking.addRankedCompletion(
                result,
                "false",
                PhelCompletionRanking.Priority.COMMON_BUILTINS,
                "boolean",
                null,
                AllIcons.Nodes.Constant
            )
        }

        /**
         * Check if we're in an argument context where API functions are valid
         * (like filter predicates, map functions) vs definition bodies where they're not
         */
        fun isInArgumentContext(element: PsiElement?): Boolean {
            // Walk up to find the containing list
            var current = element
            while (current != null && current !is PhelList) {
                current = current.parent
            }

            if (current is PhelList) {
                val list = current
                val children: Array<PsiElement> = list.children

                // Look for function name - skip opening parenthesis
                for (child in children) {
                    if (child is PhelAccessImpl) {
                        val functionName = child.text

                        // If we're inside a regular function call (not a definition form),
                        // then we're in an argument context where API functions are valid
                        val isDefForm = isDefinitionForm(functionName)
                        return !isDefForm
                    }
                }
            }

            return false
        }

        /**
         * Check if a function name is a definition form (defn, let, etc.)
         */
        fun isDefinitionForm(functionName: String): Boolean {
            return functionName == "defn" || functionName == "defn-" || functionName == "defmacro" || functionName == "defmacro-" || functionName == "let" || functionName == "loop" || functionName == "binding" || functionName == "fn" || functionName == "if-let" || functionName == "when-let"
        }

        /**
         * Check if we're in a definition name position (defn name, def name, etc.)
         */
        fun isInDefinitionNamePosition(element: PsiElement?): Boolean {
            // Walk up to find the containing list

            var current = element
            while (current != null && current !is PhelList) {
                current = current.parent
            }

            if (current is PhelList) {
                val list = current
                val children: Array<PsiElement> = list.children

                if (children.isNotEmpty()) {
                    // Check if first element is a definition keyword
                    val firstElement = children[0]
                    if (firstElement is PhelSymbol || firstElement is PhelAccessImpl) {
                        val defKeyword = firstElement.text

                        // Check if it's a definition form
                        if (defKeyword == "defn" || defKeyword == "defn-" || defKeyword == "def" || defKeyword == "defmacro" || defKeyword == "defmacro-" || defKeyword == "defstruct") {
                            // Find cursor position
                            val cursorPosition = getCursorPositionInList(list, element)

                            // Position 1 is the name position for all these forms
                            // (defn NAME [params] body...)
                            // (def NAME value)
                            val isNamePosition = (cursorPosition == 1)

                            return isNamePosition
                        }
                    }
                }
            }

            return false
        }

        /**
         * Add helpful hints for definition name position without suggesting existing names
         */
        fun addDefinitionNameHints(result: CompletionResultSet) {
            // Add a helpful hint instead of existing function names
            result.addElement(
                LookupElementBuilder.create("").withPresentableText("Type a unique name...")
                    .withTypeText("Function names must be unique").withIcon(AllIcons.General.Information)
                    .withInsertHandler { _: InsertionContext?, _: LookupElement? -> })
        }

        /**
         * Detect if we're inside a function call and at what position
         */
        fun getFunctionCallContext(element: PsiElement?): FunctionCallContext? {
            // Walk up the PSI tree to find the containing function call
            var current = element
            while (current != null) {
                if (current is PhelList) {
                    val list = current
                    val children: Array<PsiElement> = list.children

                    if (children.isNotEmpty()) {
                        // Find the function name (skip opening paren)
                        var functionName: String? = null
                        var functionIndex = -1

                        for (i in children.indices) {
                            val child = children[i]
                            if (child is PhelSymbol || child is PhelAccessImpl) {
                                functionName = child.text
                                functionIndex = i
                                break
                            }
                        }

                        if (functionName != null) {
                            // Check if we're completing the function name itself vs arguments
                            val cursorPosition = getCursorPositionInList(list, element)

                            // If we're completing the function name itself, don't treat as argument position
                            if (cursorPosition == functionIndex && isElementBeingCompleted(
                                    children[functionIndex], element
                                )
                            ) {
                                return null // Show function completions, not argument completions
                            }

                            // Special handling for forms where certain positions are expression contexts, not arguments
                            val isExpression = isExpressionContext(functionName, cursorPosition, functionIndex)
                            if (isExpression) {
                                return null // Allow normal expression completions including "("
                            }

                            // If cursor is after the function name, we're in arguments
                            if (cursorPosition > functionIndex) {
                                return FunctionCallContext(cursorPosition - functionIndex - 1)
                            }
                        }
                    }
                }
                current = current.parent
            }

            return null
        }

        /**
         * Get the cursor position within a PhelList
         */
        fun getCursorPositionInList(list: PhelList, cursorElement: PsiElement?): Int {
            val children: Array<PsiElement> = list.children
            for (i in children.indices) {
                if (isAncestorOrEqual(children[i], cursorElement)) {
                    return i
                }
            }
            return children.size // At the end
        }

        /**
         * Check if ancestor contains or is equal to descendant
         */
        fun isAncestorOrEqual(ancestor: PsiElement?, descendant: PsiElement?): Boolean {
            var current = descendant
            while (current != null) {
                if (current === ancestor) {
                    return true
                }
                current = current.parent
            }
            return false
        }

        /**
         * Check if the completion element is within the function name element
         * This helps distinguish between completing function names vs arguments
         */
        fun isElementBeingCompleted(functionElement: PsiElement?, completionElement: PsiElement?): Boolean {
            // Check if the completion element is the same as or contained within the function element
            return isAncestorOrEqual(functionElement, completionElement) || functionElement === completionElement
        }

        /**
         * Basic fallback completions when detailed completion fails
         */
        fun addBasicFallbackCompletions(result: CompletionResultSet) {
            try {
                // Provide basic Phel syntax completions as fallback
                addBasicValues(result)

                // Add core language constructs with HIGHEST priority
                val balancedParen =
                    LookupElementBuilder.create("(").withPresentableText("() ").withTailText(" Start expression", true)
                        .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER).withIcon(PhelIcons.FILE)
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        balancedParen, PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS.value
                    )
                )

                // Add most common functions without context-aware ranking
                val commonFunctions =
                    arrayOf<String?>("defn", "fn", "let", "if", "when", "map", "filter", "reduce", "get", "count")
                for (func in commonFunctions) {
                    result.addElement(
                        LookupElementBuilder.create(func!!).withIcon(PhelIconProvider.SPECIAL_FORM)
                    )
                }
            } catch (_: Exception) {
                // Even fallback failed - just provide minimal completion
                result.addElement(
                    LookupElementBuilder.create("(").withTailText(" - Expression", true)
                        .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER)
                )
            }
        }

        /**
         * Add completions appropriate for function arguments (values, not function names)
         */
        fun addArgumentCompletions(result: CompletionResultSet, element: PsiElement) {
            // Local variables and parameters (highest priority)
            PhelLocalSymbolCompletions.addLocalSymbols(result, element)
            // Basic values
            addBasicValues(result)
            // Context-aware API functions (unctions like predicates are valid as argument values)
            PhelApiCompletions.addCoreFunctions(result, element)
            PhelApiCompletions.addPredicateFunctions(result, element)
            PhelApiCompletions.addCollectionFunctions(result)
            PhelApiCompletions.addArithmeticFunctions(result)
            // Always allow nested expressions in arguments - any function can accept (nested-call) as arguments
            val parenElement =
                LookupElementBuilder.create("()").withPresentableText("() ").withTypeText("Nested expression")
                    .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            // Nested expressions
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    parenElement, PhelCompletionRanking.Priority.COMMON_BUILTINS.value
                )
            )
        }

        /**
         * Check if we're in an expression context rather than argument context
         * for special forms like defn, let, if, etc.
         */
        fun isExpressionContext(functionName: String, cursorPosition: Int, functionIndex: Int): Boolean {
            // For ns: (ns namespace-name (:require...) (:use...)) - special handling needed
            if (functionName == "ns") {
                return false // Handle ns specially, not as expression context
            }

            // For def: (def name value) - at position 3+, the def form is complete, allow new expressions
            if (functionName == "def") {
                return cursorPosition >= functionIndex + 3 // Position 3+ = after complete def, allow new expressions
            }

            // For defstruct: (defstruct name [fields*]) - at position 3+, the defstruct form is complete
            if (functionName == "defstruct") {
                return cursorPosition >= functionIndex + 3 // Position 3+ = after fields vector, allow new expressions
            }

            // For defn: (defn name [params] body1 body2 ...)
            // Positions: 0=defn, 1=name, 2=[params], 3+=body expressions
            if (functionName == "defn" || functionName == "defn-") {
                val isExpression = cursorPosition >= functionIndex + 3
                return isExpression // Position 3+ = function body
            }

            // For defmacro: (defmacro name [params] body1 body2 ...)
            if (functionName == "defmacro" || functionName == "defmacro-") {
                return cursorPosition >= functionIndex + 3 // Position 3+ = macro body
            }

            // For deftest: (deftest test-name body1 body2 ...)
            // Positions: 0=deftest, 1=test-name, 2+=test body expressions
            if (functionName == "deftest") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = test body
            }

            // For let: (let [bindings] body1 body2 ...)
            // Positions: 0=let, 1=[bindings], 2+=body expressions  
            if (functionName == "let") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = let body
            }

            // For if: (if test then else)
            // Positions: 0=if, 1=test, 2=then, 3=else - all are expressions
            if (functionName == "if") {
                return cursorPosition >= functionIndex + 1 // All positions after 'if' are expressions
            }

            // For when: (when test body1 body2 ...)
            if (functionName == "when" || functionName == "when-not") {
                return cursorPosition >= functionIndex + 1 // All positions after 'when' are expressions  
            }

            // For when-let: (when-let [binding test] body1 body2 ...)
            if (functionName == "when-let") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = body expressions
            }

            // For if-let: (if-let [binding test] then else?)
            if (functionName == "if-let") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = then/else expressions
            }

            // For do: (do expr1 expr2 ...)
            if (functionName == "do") {
                return cursorPosition >= functionIndex + 1 // All positions after 'do' are expressions
            }

            // For case: (case expr & clauses) - clauses are expressions
            if (functionName == "case" || functionName == "cond") {
                return cursorPosition >= functionIndex + 1 // All positions after case/cond are expressions
            }

            // For binding: (binding [bindings*] expr*) - similar to let
            if (functionName == "binding") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = body expressions
            }

            // For try: (try expr* catch* finally?) - expressions in body
            if (functionName == "try") {
                return cursorPosition >= functionIndex + 1 // All positions after 'try' are expressions
            }

            // For fn: (fn [params] body1 body2 ...)
            if (functionName == "fn") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = function body
            }

            // For loop: (loop [bindings] body1 body2 ...)
            if (functionName == "loop") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = loop body
            }

            // For iteration constructs: (for [seq-exprs] body), (foreach [binding coll] body*)
            if (functionName == "for" || functionName == "dofor" || functionName == "foreach") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = body expressions
            }

            // For definterface: (definterface name & methods) - methods are expressions
            if (functionName == "definterface") {
                return cursorPosition >= functionIndex + 2 // Position 2+ = method expressions
            }

            // For with-output-buffer: (with-output-buffer expr1 expr2 ...)
            // Positions: 0=with-output-buffer, 1+=body expressions
            if (functionName == "with-output-buffer") {
                return cursorPosition >= functionIndex + 1 // Position 1+ = body expressions
            }

            return false // Default: treat as argument context
        }

        /**
         * Check if we're in a namespace (ns) context that needs special completion
         */
        fun isInNamespaceContext(element: PsiElement?): Boolean {
            // Find the parent list and check if it's an ns form
            var current = element
            while (current != null) {
                if (current is PhelList) {
                    val list = current
                    val children: Array<PsiElement> = list.children

                    if (children.isNotEmpty()) {
                        val firstChild = children[0]
                        if (firstChild is PhelSymbol || firstChild is PhelAccessImpl) {
                            val text = firstChild.text
                            if (text == "ns") {
                                return true // We're inside an ns form
                            }
                        }
                    }
                }
                current = current.parent
            }
            return false
        }

        /**
         * Add namespace-specific completions for ns forms
         */
        fun addNamespaceCompletions(result: CompletionResultSet, element: PsiElement?) {
            try {
                // Find the ns list to understand the context
                var nsList: PhelList? = null
                var current = element
                while (current != null) {
                    if (current is PhelList) {
                        val list = current
                        val children: Array<PsiElement> = list.children
                        if (children.isNotEmpty()) {
                            val firstChild = children[0]
                            if ((firstChild is PhelSymbol || firstChild is PhelAccessImpl) && firstChild.text == "ns") {
                                nsList = list
                                break
                            }
                        }
                    }
                    current = current.parent
                }

                if (nsList == null) return

                // Determine cursor position in the ns form
                val cursorPosition = getCursorPositionInList(nsList, element)

                // Position 0 = ns, Position 1 = namespace-name, Position 2+ = namespace options
                if (cursorPosition <= 1) {
                    // Still completing namespace name - no special completions needed
                    return
                }

                // Determine completion type based on context  
                val inRequire = isInRequireContext(element)
                val inRequireFile = isInRequireFileContext(element)
                val inUse = isInUseContext(element)

                if (inRequire) {
                    addRequireCompletions(result, element!!)
                } else if (inRequireFile) {
                    addRequireFileCompletions(result, element)
                } else if (inUse) {
                    addUseCompletions(result, element)
                } else {
                    // Top-level ns completions
                    addTopLevelNamespaceCompletions(result)
                }
            } catch (_: Exception) {
                // Fallback to basic completions
                addTopLevelNamespaceCompletions(result)
            }
        }

        /**
         * Add top-level namespace completions (:require, :require-file, :use)
         */
        fun addTopLevelNamespaceCompletions(result: CompletionResultSet) {
            // Add (:require with custom insert handler that positions cursor inside
            val requireElement = LookupElementBuilder.create("(:require )").withPresentableText("(:require )")
                .withTypeText("Import namespace with optional alias").withTailText(" namespace\\name :as alias", true)
                .withInsertHandler(REQUIRE_TEMPLATE_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    requireElement, PhelCompletionRanking.Priority.SPECIAL_FORMS.value
                )
            )

            // Add (:require-file with custom insert handler that positions cursor inside
            val requireFileElement =
                LookupElementBuilder.create("(:require-file )").withPresentableText("(:require-file )")
                    .withTypeText("Include PHP file (e.g., vendor/autoload.php)")
                    .withTailText(" \"path/to/file.php\"", true).withInsertHandler(REQUIRE_FILE_TEMPLATE_INSERT_HANDLER)
                    .withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    requireFileElement, PhelCompletionRanking.Priority.SPECIAL_FORMS.value
                )
            )

            // Add (:use with custom insert handler that positions cursor inside
            val useElement = LookupElementBuilder.create("(:use )").withPresentableText("(:use )")
                .withTypeText("Import symbols from namespace or PHP class")
                .withTailText(" namespace\\name) or Some\\Php\\Class)", true)
                .withInsertHandler(USE_TEMPLATE_INSERT_HANDLER).withIcon(PhelIcons.FILE)
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    useElement, PhelCompletionRanking.Priority.MACROS.value
                )
            )
        }

        /**
         * Check if we're in a :require context (but not :require-file)
         */
        fun isInRequireContext(element: PsiElement?): Boolean {
            val context = getCurrentTextContext(element, 200)

            val hasRequire = context.contains(":require") || context.contains("(:require")
            val hasRequireFile = context.contains(":require-file") || context.contains("(:require-file")

            // Check if we're inside an incomplete require statement vs after a completed one
            // Look for complete (:require ...) patterns in the context
            var afterCompleteRequire = false

            // Simpler approach: if context contains a line ending with ) and current line is just IntellijIdeaRulezzz
            val lines = context.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (lines.size >= 2) {
                val currentLine = lines[lines.size - 1].trim { it <= ' ' }

                // If current line is just the IntelliJ placeholder, check previous lines for complete forms
                if (currentLine == "IntellijIdeaRulezzz" || currentLine.isEmpty()) {
                    // Look for any complete require forms in previous lines
                    for (i in 0..<lines.size - 1) {
                        val line = lines[i].trim { it <= ' ' }
                        // Simple check: line contains :require and ends with )
                        if (line.contains(":require") && line.endsWith(")")) {
                            afterCompleteRequire = true
                            break
                        }
                    }
                }
            }

            return hasRequire && !hasRequireFile && !afterCompleteRequire
        }

        /**
         * Check if we're in a :require-file context
         */
        fun isInRequireFileContext(element: PsiElement?): Boolean {
            val context = getCurrentTextContext(element, 200)
            val hasRequireFile = context.contains(":require-file") || context.contains("(:require-file")

            // Use same simple line-by-line approach as isInRequireContext
            val lines = context.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            var afterCompleteRequireFile = false
            if (lines.size >= 2) {
                val currentLine = lines[lines.size - 1].trim { it <= ' ' }

                // If current line is just the IntelliJ placeholder, check previous lines for complete forms
                if (currentLine == "IntellijIdeaRulezzz" || currentLine.isEmpty()) {
                    // Look for any complete require-file forms in previous lines
                    for (i in 0..<lines.size - 1) {
                        val line = lines[i].trim { it <= ' ' }
                        // Simple check: line contains :require-file and ends with )
                        if (line.contains(":require-file") && line.endsWith(")")) {
                            afterCompleteRequireFile = true
                            break
                        }
                    }
                }
            }

            return hasRequireFile && !afterCompleteRequireFile
        }

        /**
         * Check if we're in a :use context
         */
        fun isInUseContext(element: PsiElement?): Boolean {
            val context = getCurrentTextContext(element, 200)

            val hasUse = context.contains(":use") || context.contains("(:use")

            // Use same simple line-by-line approach
            val lines = context.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var afterCompleteUse = false
            if (lines.size >= 2) {
                val currentLine = lines[lines.size - 1].trim { it <= ' ' }

                // If current line is just the IntelliJ placeholder, check previous lines for complete forms
                if (currentLine == "IntellijIdeaRulezzz" || currentLine.isEmpty()) {
                    // Look for any complete use forms in previous lines
                    for (i in 0..<lines.size - 1) {
                        val line = lines[i].trim { it <= ' ' }
                        // Simple check: line contains :use and ends with )
                        if (line.contains(":use") && line.endsWith(")")) {
                            afterCompleteUse = true
                            break
                        }
                    }
                }
            }

            return hasUse && !afterCompleteUse
        }

        /**
         * Add completions for :require context
         */
        fun addRequireCompletions(result: CompletionResultSet, element: PsiElement) {
            val context = getCurrentTextContext(element, 200)

            // Extract the current namespace prefix being typed
            val namespacePrefix = extractNamespacePrefix(context)

            // Improved logic: detect different completion scenarios
            val hasCompleteNamespaceWithSpace =
                context.matches(".*:require\\s+[a-zA-Z][a-zA-Z0-9\\\\.-]+\\s+.*".toRegex())
            val justAfterRequire =
                context.matches(".*:require\\s*IntellijIdeaRulezzz.*".toRegex()) || context.matches(".*\\(:require\\s*IntellijIdeaRulezzz.*".toRegex())
            val hasPartialNamespace = !namespacePrefix.isEmpty()
            val isCompleteKnownNamespace = isKnownCompleteNamespace(namespacePrefix)

            // After a complete known namespace (like "phel\base64") - suggest options
            if (isCompleteKnownNamespace) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":as",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":as alias-name",
                    "Create an alias for the namespace",
                    PhelIcons.FILE
                )

                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":refer []",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":refer [symbol1 symbol2]",
                    "Import specific symbols directly",
                    PhelIcons.FILE
                )
            } else if (justAfterRequire || hasPartialNamespace) {
                // Suggest common Phel namespaces with prefix filtering
                addCommonPhelNamespacesWithPrefix(result, namespacePrefix)

                // Suggest actual namespaces found in project .phel files with prefix filtering
                addProjectPhelNamespacesWithPrefix(result, element, namespacePrefix)
            } else if (hasCompleteNamespaceWithSpace) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":as",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":as alias-name",
                    "Create an alias for the namespace",
                    PhelIcons.FILE
                )

                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":refer []",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":refer [symbol1 symbol2]",
                    "Import specific symbols directly",
                    PhelIcons.FILE
                )
            } else if (context.contains(":refer") && !context.contains("[")) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    "[",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    "[symbol-list]",
                    "Start symbol list for :refer",
                    PhelIcons.FILE
                )
            }
        }

        /**
         * Add completions for :require-file context
         */
        fun addRequireFileCompletions(result: CompletionResultSet, element: PsiElement?) {
            val context = getCurrentTextContext(element, 200)

            // Check if there's already a filename string in the context
            val hasFilename = context.matches(".*:require-file\\s+\"[^\"]+\".*".toRegex())
            val justAfterRequireFile =
                context.matches(".*:require-file\\s*IntellijIdeaRulezzz.*".toRegex()) || context.matches(".*\\(:require-file\\s*IntellijIdeaRulezzz.*".toRegex())

            // Just after :require-file (no filename yet)
            if (justAfterRequireFile) {
                // Suggest common PHP file paths
                val commonPaths = arrayOf<String?>(
                    "\"vendor/autoload.php\"", "\"bootstrap.php\"", "\"config.php\"", "\"functions.php\""
                )

                for (path in commonPaths) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        path!!,
                        PhelCompletionRanking.Priority.API_FUNCTIONS,
                        null,
                        "Common PHP file to include",
                        PhelIconProvider.PHP_INTEROP
                    )
                }
            } else if (hasFilename) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":as",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":as alias-name",
                    "Create alias for the included file",
                    PhelIcons.FILE
                )

                // Removed ) completion - users can manually close the form
            } else {
                // Suggest common PHP file paths
                val commonPaths = arrayOf<String?>(
                    "\"vendor/autoload.php\"", "\"bootstrap.php\"", "\"config.php\"", "\"functions.php\""
                )

                for (path in commonPaths) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        path!!,
                        PhelCompletionRanking.Priority.API_FUNCTIONS,
                        null,
                        "Common PHP file to include",
                        PhelIconProvider.PHP_INTEROP
                    )
                }
            }
        }

        /**
         * Add completions for :use context
         */
        fun addUseCompletions(result: CompletionResultSet, element: PsiElement?) {
            val context = getCurrentTextContext(element, 200)


            // Check if there's already a PHP class in the context
            val hasClass = context.matches(".*:use\\s+\\\\?[a-zA-Z][a-zA-Z0-9\\\\.-]+.*".toRegex())
            val justAfterUse =
                context.matches(".*:use\\s*IntellijIdeaRulezzz.*".toRegex()) || context.matches(".*\\(:use\\s*IntellijIdeaRulezzz.*".toRegex())


            // Case 1: Just after :use (no class yet)
            if (justAfterUse) {
                // :use is for PHP classes only, not Phel namespaces

                addPhpClassCompletions(result)


                // Add example suggestions
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    "\\DateTime",
                    PhelCompletionRanking.Priority.API_FUNCTIONS,
                    null,
                    "PHP DateTime class (example)",
                    PhelIconProvider.PHP_INTEROP
                )
            } else if (hasClass) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":as",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":as alias-name",
                    "Create alias for the imported PHP class",
                    PhelIcons.FILE
                )


                // Removed ) completion - users can manually close the form
            } else {
                // :use is for PHP classes only, not Phel namespaces

                addPhpClassCompletions(result)
            }
        }

        /**
         * Add completions for PHP class imports
         */
        fun addPhpClassCompletions(result: CompletionResultSet) {
            // Suggest common PHP class patterns
            val commonPhpClasses = arrayOf<String?>(
                "Exception", "DateTime", "PDO", "stdClass"
            )

            for (className in commonPhpClasses) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    className!!,
                    PhelCompletionRanking.Priority.PHP_INTEROP,
                    null,
                    "PHP class",
                    PhelIconProvider.PHP_INTEROP
                )
            }
        }

        /**
         * Get text context around the current element for parsing
         */
        fun getCurrentTextContext(element: PsiElement?, maxLength: Int): String {
            try {
                val context = StringBuilder()

                // First, get the parent list to understand the broader context
                var parentList = element
                while (parentList != null && parentList !is PhelList) {
                    parentList = parentList.parent
                }

                if (parentList != null) {
                    val parentText = parentList.text
                    if (parentText != null && parentText.length <= maxLength) {
                        context.append(parentText)
                    } else if (parentText != null) {
                        // If parent is too long, get a reasonable portion
                        val cursorOffset = element!!.textOffset - parentList.textOffset
                        val startOffset = max(0, cursorOffset - maxLength / 2)
                        val endOffset = min(parentText.length, cursorOffset + maxLength / 2)
                        context.append(parentText.substring(startOffset, endOffset))
                    }
                }

                return context.toString()
            } catch (_: Exception) {
                return ""
            }
        }

        /**
         * Extract namespace declaration from a .phel file
         */
        fun extractNamespaceFromPhelFile(psiFile: PsiFile): String? {
            try {
                // Look for (ns namespace-name) declarations
                val children: Array<PsiElement> = psiFile.children

                for (child in children) {
                    if (child is PhelList) {
                        val listChildren: Array<PsiElement> = child.children
                        // Check if first element is 'ns'
                        if (listChildren.size >= 2) {
                            val firstElement = listChildren[0]
                            if (firstElement is PhelAccess) {
                                val firstText = firstElement.text.trim { it <= ' ' }
                                if ("ns" == firstText) {
                                    // Second element should be the namespace name
                                    val secondElement = listChildren[1]
                                    if (secondElement is PhelAccess) {
                                        val namespace = secondElement.text.trim { it <= ' ' }
                                        return namespace
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (_: Exception) {
            }

            return null
        }

        /**
         * Extract namespace prefix from the current context (e.g., "phel\" from "(:require phel\IntellijIdeaRulezzz")
         */
        fun extractNamespacePrefix(context: String): String {
            try {
                // Look for pattern: (:require PREFIX_HERE IntellijIdeaRulezzz
                val requirePos = context.lastIndexOf(":require")
                if (requirePos == -1) return ""

                val afterRequire = context.substring(requirePos + 8) // Skip ":require"

                // Find the namespace part before IntellijIdeaRulezzz
                val ideaPos = afterRequire.indexOf("IntellijIdeaRulezzz")
                if (ideaPos == -1) return ""

                val beforeIdea = afterRequire.take(ideaPos).trim { it <= ' ' }

                // Remove any leading whitespace and extract the last word (partial namespace)
                val parts = beforeIdea.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (parts.isNotEmpty()) {
                    val lastPart = parts[parts.size - 1]
                    // Only return if it looks like a namespace prefix (contains letters/backslash)
                    if (lastPart.matches("[a-zA-Z][a-zA-Z0-9\\\\.-]*".toRegex())) {
                        return lastPart
                    }
                }

                return ""
            } catch (_: Exception) {
                return ""
            }
        }

        /**
         * Add common Phel namespaces with prefix filtering
         */
        fun addCommonPhelNamespacesWithPrefix(result: CompletionResultSet, prefix: String) {
            val commonNamespaces = arrayOf<String?>(
                "phel\\test",
                "phel\\json",
                "phel\\http",
                "phel\\str",
                "phel\\html",
                "phel\\core",
                "phel\\base64",
                "phel\\local",
                "phel\\repl",
                "phel\\trace"
            )

            for (namespace in commonNamespaces) {
                // Filter by prefix (case-insensitive)
                if (prefix.isEmpty() || namespace!!.lowercase(Locale.getDefault())
                        .startsWith(prefix.lowercase(Locale.getDefault()))
                ) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        namespace!!,
                        PhelCompletionRanking.Priority.API_FUNCTIONS,
                        null,
                        "Common Phel namespace",
                        PhelIconProvider.NAMESPACE
                    )
                }
            }
        }

        /**
         * Add project Phel namespaces with prefix filtering
         */
        fun addProjectPhelNamespacesWithPrefix(result: CompletionResultSet, element: PsiElement, prefix: String) {
            try {
                // Get the project from the PSI element
                val project = element.project

                // Use GlobalSearchScope to find all .phel files in the project
                val scope = GlobalSearchScope.allScope(project)


                // Find all .phel files using FilenameIndex
                val phelFiles = FilenameIndex.getAllFilesByExt(project, "phel", scope)

                val foundNamespaces: MutableSet<String> = HashSet<String>()

                for (virtualFile in phelFiles) {
                    try {
                        // Get PSI file from virtual file
                        val psiFile = PsiManager.getInstance(project).findFile(virtualFile)

                        if (psiFile != null) {
                            val namespace = extractNamespaceFromPhelFile(psiFile)
                            if (namespace != null && !namespace.isEmpty()) {
                                // Filter by prefix (case-insensitive)
                                if (prefix.isEmpty() || namespace.lowercase(Locale.getDefault()).startsWith(
                                        prefix.lowercase(Locale.getDefault())
                                    )
                                ) {
                                    foundNamespaces.add(namespace)
                                }
                            }
                        }
                    } catch (_: Exception) {
                    }
                }

                // Add found namespaces to completion results
                for (namespace in foundNamespaces) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        namespace,
                        PhelCompletionRanking.Priority.PROJECT_SYMBOLS,  // Higher priority than common ones
                        null,
                        "Project namespace from " + namespace.replace("\\", "/") + ".phel",
                        PhelIconProvider.NAMESPACE
                    )
                }
            } catch (_: Exception) {
            }
        }

        /**
         * Check if the given text looks like a complete namespace (not partial)
         * Simple heuristic: if it doesn't end with backslash and has proper namespace format
         */
        fun isKnownCompleteNamespace(namespace: String?): Boolean {
            if (namespace == null || namespace.isEmpty()) {
                return false
            }

            // If it ends with backslash, it's still partial (e.g., "phel\")
            if (namespace.endsWith("\\")) {
                return false
            }

            // Check if it looks like a complete namespace format: word\word (at least one backslash)
            if (namespace.matches("[a-zA-Z][a-zA-Z0-9]*\\\\[a-zA-Z][a-zA-Z0-9.-]*".toRegex())) {
                return true
            }

            return false
        }
    }

    private class FunctionCallContext(val argumentPosition: Int) {
        val isInArgumentPosition: Boolean
            get() = argumentPosition >= 0 // 0 = first argument, 1 = second, etc.
    }

    /**
     * Keyword completion provider - handles :keyword completions
     */
    private class PhelKeywordCompletionProvider : CompletionProvider<CompletionParameters?>() {
        override fun addCompletions(
            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
        ) {
            // Common keywords used in Phel
            val commonKeywords = arrayOf<String?>(
                "require",
                "use",
                "refer",
                "as",
                "import",
                "export",
                "private",
                "doc",
                "test",
                "deprecated",
                "added",
                "range",
                "in",
                "keys",
                "pairs",
                "while",
                "let",
                "when",
                "reduce",
                "debug",
                "prod",
                "dev",
                "cache",
                "timeout",
                "method",
                "path",
                "headers",
                "body",
                "status",
                "type",
                "name",
                "value"
            )

            val keywordIcon = AllIcons.Nodes.PropertyReadWrite
            for (keyword in commonKeywords) {
                PhelCompletionRanking.addRankedCompletion(
                    result, keyword!!, PhelCompletionRanking.Priority.API_FUNCTIONS, "keyword", null, keywordIcon
                )
            }
        }
    }

    /**
     * Namespace keyword completion provider - handles ::keyword completions
     */
    private class PhelNamespaceKeywordCompletionProvider : CompletionProvider<CompletionParameters?>() {
        override fun addCompletions(
            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
        ) {
            // Current namespace keywords - these would ideally come from analyzing the current file
            val namespaceKeywords = arrayOf<String?>(
                "private", "export", "test", "doc", "deprecated", "inline", "macro", "dynamic", "const"
            )

            val namespaceIcon = AllIcons.Nodes.Constant
            for (keyword in namespaceKeywords) {
                result.addElement(
                    LookupElementBuilder.create(keyword!!).withIcon(namespaceIcon).withTypeText("namespace keyword")
                )
            }
        }
    }
}

private val NAMESPACED_INSERT_HANDLER = PhelCompletionContributor.NamespacedInsertHandler()

private val BALANCED_PARENS_INSERT_HANDLER = PhelCompletionContributor.BalancedParenthesesInsertHandler()

private val BALANCED_BRACKETS_INSERT_HANDLER = PhelCompletionContributor.BalancedBracketsInsertHandler()

private val DEFN_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.DefnTemplateInsertHandler()

private val DEF_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.DefTemplateInsertHandler()

private val LET_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.LetTemplateInsertHandler()

private val IF_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.IfTemplateInsertHandler()

private val FN_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.FnTemplateInsertHandler()

private val WHEN_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.WhenTemplateInsertHandler()

private val REQUIRE_FILE_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.RequireFileTemplateInsertHandler()

private val REQUIRE_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.RequireTemplateInsertHandler()

private val USE_TEMPLATE_INSERT_HANDLER = PhelCompletionContributor.UseTemplateInsertHandler()

/**
 * Creates a lookup element with proper insert handling for namespaced completions
 */
fun createNamespacedLookupElement(
    name: String,
    icon: Icon?,
    typeText: String?,
    tailText: String?
): LookupElementBuilder {
    var builder = LookupElementBuilder.create(name)
    if (icon != null) {
        builder = builder.withIcon(icon)
    }
    if (typeText != null) {
        builder = builder.withTypeText(typeText)
    }
    if (tailText != null) {
        builder = builder.withTailText(tailText, true)
    }

    // Apply custom insert handler for completions containing '/'
    if (name.contains("/")) {
        builder = builder.withInsertHandler(NAMESPACED_INSERT_HANDLER)
    }

    return builder
}