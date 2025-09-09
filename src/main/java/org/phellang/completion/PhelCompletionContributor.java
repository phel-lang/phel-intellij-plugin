package org.phellang.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.phellang.PhelIcons;
import org.phellang.language.psi.PhelList;
import org.phellang.language.psi.PhelSymbol;
import org.phellang.language.psi.PhelTypes;
import org.phellang.language.psi.PhelVec;
import org.phellang.language.psi.impl.PhelAccessImpl;

import javax.swing.*;

/**
 * Main completion contributor for Phel language
 * Provides intelligent completions for API functions, PHP interop, and language constructs
 */
public class PhelCompletionContributor extends CompletionContributor {

    public PhelCompletionContributor() {
        // Complete symbols (functions, variables, etc.)
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PhelTypes.SYM).withLanguage(org.phellang.PhelLanguage.INSTANCE), new PhelSymbolCompletionProvider());

        // Complete after colon (keywords)
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PhelTypes.SYM).afterLeaf(PlatformPatterns.psiElement(PhelTypes.COLON)).withLanguage(org.phellang.PhelLanguage.INSTANCE), new PhelKeywordCompletionProvider());

        // Complete after namespace colon
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PhelTypes.SYM).afterLeaf(PlatformPatterns.psiElement(PhelTypes.COLONCOLON)).withLanguage(org.phellang.PhelLanguage.INSTANCE), new PhelNamespaceKeywordCompletionProvider());
    }

    /**
     * Custom insert handler for namespaced completions to prevent text duplication
     * Handles completions containing '/' by properly replacing the typed prefix
     */
    public static class NamespacedInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            // Find the start of the current symbol
            int symbolStart = caretOffset;
            CharSequence text = document.getCharsSequence();
            
            // Move back to find the beginning of the symbol
            while (symbolStart > 0) {
                char c = text.charAt(symbolStart - 1);
                // Stop at whitespace, opening parenthesis, or other delimiters
                if (Character.isWhitespace(c) || c == '(' || c == '[' || c == '{') {
                    break;
                }
                symbolStart--;
            }
            
            // Replace the entire symbol with the completion
            int endOffset = context.getTailOffset();
            String completionText = item.getLookupString();
            
            document.replaceString(symbolStart, endOffset, completionText);
            editor.getCaretModel().moveToOffset(symbolStart + completionText.length());
        }
    }

    private static final NamespacedInsertHandler NAMESPACED_INSERT_HANDLER = new NamespacedInsertHandler();

    /**
     * Creates a lookup element with proper insert handling for namespaced completions
     */
    public static LookupElementBuilder createNamespacedLookupElement(String name, Icon icon, String typeText, String tailText) {
        LookupElementBuilder builder = LookupElementBuilder.create(name);
        if (icon != null) {
            builder = builder.withIcon(icon);
        }
        if (typeText != null) {
            builder = builder.withTypeText(typeText);
        }
        if (tailText != null) {
            builder = builder.withTailText(tailText, true);
        }
        
        // Apply custom insert handler for completions containing '/'
        if (name.contains("/")) {
            builder = builder.withInsertHandler(NAMESPACED_INSERT_HANDLER);
        }
        
        return builder;
    }

    /**
     * Core symbol completion provider - handles functions, macros, and built-ins
     * Enhanced with local symbol completion and smart priority ordering
     */
    private static class PhelSymbolCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

            PsiElement element = parameters.getPosition();
            String prefix = getCompletionPrefix(element);
            

            // PRIORITY 0: New form completion - suggest ONLY "(" when starting a new expression
            // This is the ONLY valid syntax at top level, so return early
            if (shouldSuggestNewForm(element, prefix)) {
                addNewFormCompletion(result);
                return; // Don't show any other completions - only "(" is valid
            }

            // Check if we're at the parameter vector position in definition forms
            if (isInParameterVectorPosition(element)) {
                addParameterVectorCompletion(result);
                return; // Only suggest [ for parameter vector
            }
            
            // Check if we're inside a parameter vector
            if (isInsideParameterVector(element)) {
                addParameterCompletions(result);
                return; // Only suggest parameter names and ]
            }

            // Check if we're inside function arguments where only values are valid
            FunctionCallContext functionContext = getFunctionCallContext(element);
            if (functionContext != null && functionContext.isInArgumentPosition()) {
                // Inside function arguments - only show values, not function names
                addArgumentCompletions(result, element, functionContext);
                return; // Don't show function names - they're invalid syntax here
            }

            // Check if we're in a definition name position (defn name, def name, etc.)
            if (isInDefinitionNamePosition(element)) {
                // In definition name position - don't suggest existing names to avoid conflicts
                addDefinitionNameHints(result);
                return; // Don't show existing function names - they would cause conflicts
            }
            
            // Determine if we're in a context where raw function names are valid
            boolean isInFunctionNameContext = isInFunctionNameContext(element);
            
            // PRIORITY 0.5: Add "(" for expression contexts (function bodies, let bodies, etc.)
            // but NOT when we're right after "(" where function names should be suggested
            if (!isInFunctionNameContext) {
                // CRITICAL: Add local symbols FIRST in expression contexts (parameters, let bindings, etc.)
                PhelLocalSymbolCompletions.addLocalSymbols(result, element);
                
                // Then add basic values that are valid in expression contexts
                addBasicValues(result);
                
                // Only add API functions if we're in an argument context (like filter predicates)
                // NOT in definition bodies where bare function names are invalid syntax
                boolean inArgContext = isInArgumentContext(element);
                if (inArgContext) {
                    // Add context-aware API functions in argument positions
                    // This fixes filter context where predicates should be suggested
                    PhelApiCompletions.addCoreFunctions(result, prefix, element);
                    PhelApiCompletions.addPredicateFunctions(result, prefix, element);
                    PhelApiCompletions.addCollectionFunctions(result, prefix, element);
                    PhelApiCompletions.addArithmeticFunctions(result, prefix, element);
                } else {
                }
                
                // Finally add structural completions
                if (isInDefinitionBodyContext(element)) {
                    addDefinitionBodyCompletion(result);
                } else {
                    addExpressionCompletion(result);
                }
                return; // Early return for expression contexts
            }
            
            
            // PRIORITY 1: Parameter hints and function signatures - when inside function calls
            PhelParameterHintProvider.addParameterHints(result, element);
            PhelParameterHintProvider.addPositionHints(result, element);

            // PRIORITY 2: Local symbols (parameters, let bindings, local definitions) - added first for highest priority
            PhelLocalSymbolCompletions.addLocalSymbols(result, element);

            // PRIORITY 3: Basic values that are valid in expression contexts
            addBasicValues(result);

            // PRIORITY 4: Only add API functions if we're in a context where function names are valid
            if (isInFunctionNameContext) {
                // Special forms and core macros - important language constructs
                PhelLanguageCompletions.addSpecialForms(result, prefix);
                PhelLanguageCompletions.addCoreMacros(result, prefix);

                // Core API functions with context-aware ranking
                PhelApiCompletions.addCoreFunctions(result, prefix, element);
                PhelApiCompletions.addPredicateFunctions(result, prefix, element);
                PhelApiCompletions.addCollectionFunctions(result, prefix, element);
                PhelApiCompletions.addArithmeticFunctions(result, prefix, element);

                // Namespace functions
                PhelNamespaceCompletions.addNamespaceFunctions(result, prefix, element);

                // PHP interop - lower priority
                PhelPhpInteropCompletions.addPhpInterop(result, prefix);
            }
        }

        private String getCompletionPrefix(PsiElement element) {
            String text = element.getText();
            // Remove IntelliJ's dummy identifier completely (with or without space)
            text = text.replace("IntellijIdeaRulezzz ", "")
                      .replace("IntellijIdeaRulezzz", "")
                      .trim();
            return text;
        }

        /**
         * Check if we should suggest starting a new form with "(" 
         * In Phel, "(" is the ONLY valid way to start a top-level expression.
         * Simple rule: if we're at top level (not inside a PhelList) and no prefix, suggest "("
         */
        private boolean shouldSuggestNewForm(PsiElement element, String prefix) {
            
            // If user has already typed something, don't suggest "("
            if (prefix.length() > 0) {
                return false;
            }

            // Check if we're at the top level (not inside any PhelList)
            PsiElement current = element;
            while (current != null) {
                
                if (current instanceof PhelList) {
                    return false; // We're inside an existing form
                }
                current = current.getParent();
            }

            return true; // We're at top level, suggest "("
        }

        /**
         * Add completion for starting a new form - the ONLY valid syntax at top level
         */
        private void addNewFormCompletion(CompletionResultSet result) {
            
            LookupElementBuilder element = LookupElementBuilder
                .create("(")
                .withPresentableText("(  ← Start new expression")
                .withTypeText("Only valid syntax at this level")
                .withTailText(" (required)", true)
                .withInsertHandler((context, item) -> {
                    // After inserting "(", position cursor inside
                    int caretOffset = context.getEditor().getCaretModel().getOffset();
                    context.getEditor().getCaretModel().moveToOffset(caretOffset);
                })
                .withIcon(org.phellang.PhelIcons.FILE)
                .bold();

            result.addElement(element);
        }
        
        /**
         * Add completion for expressions in function bodies, let bodies, etc.
         */
        private void addExpressionCompletion(CompletionResultSet result) {
            // Add opening parenthesis to start new expressions
            LookupElementBuilder openParen = LookupElementBuilder
                .create("(")
                .withPresentableText("(")
                .withTypeText("Start expression")
                .withTailText(" (new expression)", true)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(openParen);
            
            // Add closing parenthesis to close current expression
            LookupElementBuilder closeParen = LookupElementBuilder
                .create(")")
                .withPresentableText(")")
                .withTypeText("Close expression")
                .withTailText(" (end expression)", true)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(closeParen);
        }
        
        /**
         * Add completion for definition bodies (defn, defmacro) - only suggest (
         */
        private void addDefinitionBodyCompletion(CompletionResultSet result) {
            // In function/macro bodies, only suggest opening parenthesis
            LookupElementBuilder openParen = LookupElementBuilder
                .create("(")
                .withPresentableText("(")
                .withTypeText("Start expression")
                .withTailText(" (function body)", true)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(openParen);
        }
        
        /**
         * Check if we're in a definition body context (defn, defmacro, fn)
         * where only ( should be suggested, not )
         */
        private boolean isInDefinitionBodyContext(PsiElement element) {
            PsiElement current = element;
            while (current != null) {
                if (current instanceof PhelList) {
                    PhelList list = (PhelList) current;
                    PsiElement[] children = list.getChildren();
                    
                    if (children.length >= 1) {
                        PsiElement firstChild = children[0];
                        if (firstChild instanceof PhelSymbol || firstChild instanceof PhelAccessImpl) {
                            String text = firstChild.getText();
                            if (text.equals("defn") || text.equals("defn-") || 
                                text.equals("defmacro") || text.equals("defmacro-")) {
                                // Check if we're in the body position (after parameters)
                                int cursorPosition = getCursorPositionInList(list, element);
                                int bodyStartPosition = 3; // defn/defmacro: name [params] body
                                return cursorPosition >= bodyStartPosition;
                            }
                        }
                    }
                }
                current = current.getParent();
            }
            return false;
        }
        
        /**
         * Check if we're at the parameter vector position in definition forms like defn, defmacro
         * e.g., (defn factorial | should suggest [
         */
        private boolean isInParameterVectorPosition(PsiElement element) {
            PsiElement current = element;
            while (current != null) {
                if (current instanceof PhelList) {
                    PhelList list = (PhelList) current;
                    PsiElement[] children = list.getChildren();
                    
                    if (children.length >= 2) {
                        // Find the definition type (defn, defmacro, etc.)
                        String defType = null;
                        int defTypeIndex = -1;
                        
                        for (int i = 0; i < children.length; i++) {
                            PsiElement child = children[i];
                            if (child instanceof PhelSymbol || child instanceof PhelAccessImpl) {
                                String text = child.getText();
                                if (text.equals("defn") || text.equals("defn-") || 
                                    text.equals("defmacro") || text.equals("defmacro-") ||
                                    text.equals("fn")) {
                                    defType = text;
                                    defTypeIndex = i;
                                    break;
                                }
                            }
                        }
                        
                        if (defType != null) {
                            // Check cursor position relative to the definition
                            int cursorPosition = getCursorPositionInList(list, element);
                            
                            // CRITICAL: Check if we're inside an existing PhelVec at the parameter position
                            int paramVectorPosition = (defType.equals("fn")) ? defTypeIndex + 1 : defTypeIndex + 2;
                            
                            if (cursorPosition == paramVectorPosition) {
                                // Check if there's already a PhelVec at this position
                                if (paramVectorPosition < children.length && children[paramVectorPosition] instanceof PhelVec) {
                                    return false; // We're inside the vector, not at the position to create it
                                } else {
                                    return true; // We're at the position to create the parameter vector
                                }
                            }
                        }
                    }
                }
                current = current.getParent();
            }
            return false;
        }
        
        /**
         * Add parameter vector completion - suggests [ to start parameter list
         */
        private void addParameterVectorCompletion(CompletionResultSet result) {
            LookupElementBuilder element = LookupElementBuilder
                .create("[")
                .withPresentableText("[")
                .withTypeText("Parameter vector")
                .withTailText(" (function parameters)", true)
                .withIcon(org.phellang.PhelIcons.FILE);
            
            result.addElement(element);
        }
        
        /**
         * Check if we're inside a parameter vector [...]
         * e.g., (defn factorial [n | should suggest parameter names and ]
         */
        private boolean isInsideParameterVector(PsiElement element) {
            PsiElement current = element;
            while (current != null) {
                if (current instanceof PhelVec) {
                    // Check if this vector is in a parameter position
                    PsiElement parent = current.getParent();
                    
                    if (parent instanceof PhelList) {
                        PhelList list = (PhelList) parent;
                        PsiElement[] children = list.getChildren();
                        
                        // Look for definition keywords
                        for (int i = 0; i < children.length; i++) {
                            PsiElement child = children[i];
                            
                            if (child instanceof PhelSymbol || child instanceof PhelAccessImpl) {
                                String text = child.getText();
                                if (text.equals("defn") || text.equals("defn-") || 
                                    text.equals("defmacro") || text.equals("defmacro-") ||
                                    text.equals("fn")) {
                                    return true; // This vector is in a function definition
                                }
                            }
                        }
                    }
                }
                current = current.getParent();
            }
            return false;
        }
        
        /**
         * Add parameter completions - suggests parameter names and closing ]
         */
        private void addParameterCompletions(CompletionResultSet result) {
            // Suggest closing bracket to end parameter vector
            LookupElementBuilder closeBracket = LookupElementBuilder
                .create("]")
                .withPresentableText("]")
                .withTypeText("Close parameters")
                .withTailText(" (end parameter list)", true)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(closeBracket);
            
            // Suggest common parameter names
            String[] commonParams = {
                "x", "y", "z",           // Generic single values
                "n", "m", "i", "j",      // Numeric/index values  
                "coll", "xs", "ys",      // Collections
                "f", "pred",             // Functions/predicates
                "key", "val", "kv",      // Key-value pairs
                "a", "b", "c",           // Generic arguments
                "item", "elem",          // Collection elements
                "acc", "result"          // Accumulators
            };
            
            for (String param : commonParams) {
                LookupElementBuilder paramElement = LookupElementBuilder
                    .create(param)
                    .withPresentableText(param)
                    .withTypeText("Parameter")
                    .withIcon(AllIcons.Nodes.Parameter);
                result.addElement(paramElement);
            }
        }
        
        /**
         * Check if we're in a context where raw function names are valid
         * (like at the start of an expression after "(")
         */
        private boolean isInFunctionNameContext(PsiElement element) {
            // Walk up to find the containing list
            PsiElement current = element;
            while (current != null && !(current instanceof PhelList)) {
                current = current.getParent();
            }
            
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement[] children = list.getChildren();
                
                // Find cursor position in list
                int cursorPosition = getCursorPositionInList(list, element);
                
                // Check if we're at position 0 (right after opening paren)
                // That's where function names are valid
                boolean atFunctionPosition = (cursorPosition == 0);
                
                return atFunctionPosition;
            }
            
            return false;
        }
        
        /**
         * Add basic values that are always valid in expression contexts
         */
        private void addBasicValues(CompletionResultSet result) {
            // Basic literals with smart ranking
            PhelCompletionRanking.addRankedCompletion(result, "nil", 
                PhelCompletionRanking.Priority.COMMON_BUILTINS, "nil value", null, AllIcons.Nodes.Constant);
            
            PhelCompletionRanking.addRankedCompletion(result, "true", 
                PhelCompletionRanking.Priority.COMMON_BUILTINS, "boolean", null, AllIcons.Nodes.Constant);
            
            PhelCompletionRanking.addRankedCompletion(result, "false", 
                PhelCompletionRanking.Priority.COMMON_BUILTINS, "boolean", null, AllIcons.Nodes.Constant);
        }
        
        /**
         * Check if we're in an argument context where API functions are valid
         * (like filter predicates, map functions) vs definition bodies where they're not
         */
        private boolean isInArgumentContext(PsiElement element) {
            // Walk up to find the containing list
            PsiElement current = element;
            while (current != null && !(current instanceof PhelList)) {
                current = current.getParent();
            }
            
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement[] children = list.getChildren();
                
                for (int i = 0; i < children.length; i++) {
                }
                
                // Look for function name - skip opening parenthesis
                for (PsiElement child : children) {
                    if (child instanceof PhelAccessImpl) {
                        String functionName = child.getText();
                        
                        // If we're inside a regular function call (not a definition form),
                        // then we're in an argument context where API functions are valid
                        boolean isDefForm = isDefinitionForm(functionName);
                        return !isDefForm;
                    }
                }
            }
            
            return false;
        }
        
        /**
         * Check if a function name is a definition form (defn, let, etc.)
         */
        private boolean isDefinitionForm(String functionName) {
            return functionName.equals("defn") || 
                   functionName.equals("defn-") ||
                   functionName.equals("defmacro") ||
                   functionName.equals("defmacro-") ||
                   functionName.equals("let") ||
                   functionName.equals("loop") ||
                   functionName.equals("binding") ||
                   functionName.equals("fn") ||
                   functionName.equals("if-let") ||
                   functionName.equals("when-let");
        }
        
        /**
         * Check if we're in a definition name position (defn name, def name, etc.)
         */
        private boolean isInDefinitionNamePosition(PsiElement element) {
            
            // Walk up to find the containing list
            PsiElement current = element;
            while (current != null && !(current instanceof PhelList)) {
                current = current.getParent();
            }
            
            if (current instanceof PhelList) {
                PhelList list = (PhelList) current;
                PsiElement[] children = list.getChildren();
                
                if (children.length >= 1) {
                    // Check if first element is a definition keyword
                    PsiElement firstElement = children[0];
                    if (firstElement instanceof PhelSymbol || firstElement instanceof PhelAccessImpl) {
                        String defKeyword = firstElement.getText();
                        
                        // Check if it's a definition form
                        if (defKeyword.equals("defn") || defKeyword.equals("defn-") ||
                            defKeyword.equals("def") || defKeyword.equals("defmacro") ||
                            defKeyword.equals("defmacro-") || defKeyword.equals("defstruct")) {
                            
                            // Find cursor position
                            int cursorPosition = getCursorPositionInList(list, element);
                            
                            // Position 1 is the name position for all these forms
                            // (defn NAME [params] body...)
                            // (def NAME value)
                            boolean isNamePosition = (cursorPosition == 1);
                            
                            return isNamePosition;
                        }
                    }
                }
            }
            
            return false;
        }
        
        /**
         * Add helpful hints for definition name position without suggesting existing names
         */
        private void addDefinitionNameHints(CompletionResultSet result) {
            
            // Add a helpful hint instead of existing function names
            result.addElement(
                LookupElementBuilder.create("")
                    .withPresentableText("Type a unique name...")
                    .withTypeText("Function names must be unique")
                    .withIcon(AllIcons.General.Information)
                    .withInsertHandler((context, item) -> {
                        // Don't actually insert anything, just show the hint
                    })
            );
            
        }

        /**
         * Detect if we're inside a function call and at what position
         */
        private FunctionCallContext getFunctionCallContext(PsiElement element) {
            
            // Walk up the PSI tree to find the containing function call
            PsiElement current = element;
            while (current != null) {
                if (current instanceof PhelList) {
                    PhelList list = (PhelList) current;
                    PsiElement[] children = list.getChildren();
                    
                    if (children.length >= 1) {
                        // Find the function name (skip opening paren)
                        String functionName = null;
                        int functionIndex = -1;
                        
                        for (int i = 0; i < children.length; i++) {
                            PsiElement child = children[i];
                            if (child instanceof PhelSymbol || child instanceof PhelAccessImpl) {
                                functionName = child.getText();
                                functionIndex = i;
                                break;
                            }
                        }
                        
                        if (functionName != null) {
                            // Check if we're completing the function name itself vs arguments
                            int cursorPosition = getCursorPositionInList(list, element);
                            
                            
                            // If we're completing the function name itself, don't treat as argument position
                            if (cursorPosition == functionIndex && isElementBeingCompleted(children[functionIndex], element)) {
                                return null; // Show function completions, not argument completions
                            }
                            
                            // Special handling for forms where certain positions are expression contexts, not arguments
                            if (isExpressionContext(functionName, cursorPosition, functionIndex)) {
                                return null; // Allow normal expression completions including "("
                            }
                            
                            // If cursor is after the function name, we're in arguments
                            if (cursorPosition > functionIndex) {
                                return new FunctionCallContext(functionName, cursorPosition - functionIndex - 1);
                            }
                        }
                    }
                }
                current = current.getParent();
            }
            
            return null;
        }

        /**
         * Get the cursor position within a PhelList
         */
        private int getCursorPositionInList(PhelList list, PsiElement cursorElement) {
            PsiElement[] children = list.getChildren();
            for (int i = 0; i < children.length; i++) {
                if (isAncestorOrEqual(children[i], cursorElement)) {
                    return i;
                }
            }
            return children.length; // At the end
        }

        /**
         * Check if ancestor contains or is equal to descendant
         */
        private boolean isAncestorOrEqual(PsiElement ancestor, PsiElement descendant) {
            PsiElement current = descendant;
            while (current != null) {
                if (current == ancestor) {
                    return true;
                }
                current = current.getParent();
            }
            return false;
        }

        /**
         * Check if the completion element is within the function name element
         * This helps distinguish between completing function names vs arguments
         */
        private boolean isElementBeingCompleted(PsiElement functionElement, PsiElement completionElement) {
            // Check if the completion element is the same as or contained within the function element
            return isAncestorOrEqual(functionElement, completionElement) || functionElement == completionElement;
        }

        /**
         * Add completions appropriate for function arguments (values, not function names)
         */
        private void addArgumentCompletions(CompletionResultSet result, PsiElement element, FunctionCallContext context) {
            
            
            // ONLY show things that can be VALUES in arguments:
            
            // 1. Local variables and parameters (highest priority)
            PhelLocalSymbolCompletions.addLocalSymbols(result, element);
            
            // 2. Basic values
            addBasicValues(result);
            
            // 3. Context-aware API functions (this is what was missing!)
            // Functions like predicates are valid as argument values
            PhelApiCompletions.addCoreFunctions(result, "", element);
            PhelApiCompletions.addPredicateFunctions(result, "", element);
            PhelApiCompletions.addCollectionFunctions(result, "", element);
            PhelApiCompletions.addArithmeticFunctions(result, "", element);
            
            // 4. Context-aware nested expressions - only for functions that accept complex values
            if (acceptsNestedExpressions(context.getFunctionName())) {
                // Add opening parenthesis for nested expressions
            LookupElementBuilder parenElement = LookupElementBuilder
                .create("(")
                .withPresentableText("(")
                .withTypeText("Nested expression")
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(parenElement);
            }
            
            // 5. Always allow closing the current expression with )
            LookupElementBuilder closeParenElement = LookupElementBuilder
                .create(")")
                .withPresentableText(")")
                .withTypeText("Close expression")
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(closeParenElement);
            
            // 4. For numeric-only functions, suggest they need numbers
            if (isNumericOnlyFunction(context.getFunctionName())) {
                // Add helpful hint that this function only accepts numbers
            }
            
            // 5. For variadic functions like print, suggest that more arguments are possible
            if (isVariadicFunction(context.getFunctionName())) {
            }
            
            // DON'T show:
            // - Function names (defn, let, map, etc.) - these are invalid as arguments
            // - Special forms - invalid as arguments  
            // - Core API functions - invalid as arguments unless as nested calls
            
        }

        /**
         * Check if a function is variadic (takes & args)
         */
        private boolean isVariadicFunction(String functionName) {
            String signature = PhelParameterHintProvider.getFunctionSignatureStatic(functionName);
            return signature != null && signature.contains("&");
        }
        
        /**
         * Check if a function only accepts numeric arguments
         */
        private boolean isNumericOnlyFunction(String functionName) {
            String[] numericFunctions = {"max", "min", "+", "-", "*", "/", "%", "**", 
                                        "inc", "dec", "<", "<=", ">", ">=", "=", "not=",
                                        "pos?", "neg?", "zero?", "one?", "even?", "odd?"};
            for (String func : numericFunctions) {
                if (func.equals(functionName)) {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * Check if a function accepts nested expressions as arguments
         */
        private boolean acceptsNestedExpressions(String functionName) {
            // Functions that typically DON'T accept nested expressions as values
            // These are special forms where arguments have special syntax/semantics
            String[] noNestedExpressions = {"def", "defn", "let", "if", "when", "cond", "ns"};
            for (String func : noNestedExpressions) {
                if (func.equals(functionName)) {
                    return false;
                }
            }
            
            // ALL other functions can accept nested expressions that evaluate to values
            // This includes numeric functions like +, -, *, / which can accept (max 1 2), etc.
            return true;
        }
        
        /**
         * Check if we're in an expression context rather than argument context
         * for special forms like defn, let, if, etc.
         */
        private boolean isExpressionContext(String functionName, int cursorPosition, int functionIndex) {
            // For def: (def name value) - at position 3+, the def form is complete, allow new expressions
            if (functionName.equals("def")) {
                return cursorPosition >= functionIndex + 3; // Position 3+ = after complete def, allow new expressions
            }
            
            // For defstruct: (defstruct name [fields*]) - at position 3+, the defstruct form is complete
            if (functionName.equals("defstruct")) {
                return cursorPosition >= functionIndex + 3; // Position 3+ = after fields vector, allow new expressions
            }
            
            // For defn: (defn name [params] body1 body2 ...)
            // Positions: 0=defn, 1=name, 2=[params], 3+=body expressions
            if (functionName.equals("defn") || functionName.equals("defn-")) {
                boolean isExpression = cursorPosition >= functionIndex + 3;
                return isExpression; // Position 3+ = function body
            }
            
            // For defmacro: (defmacro name [params] body1 body2 ...)
            if (functionName.equals("defmacro") || functionName.equals("defmacro-")) {
                return cursorPosition >= functionIndex + 3; // Position 3+ = macro body
            }
            
            // For let: (let [bindings] body1 body2 ...)
            // Positions: 0=let, 1=[bindings], 2+=body expressions  
            if (functionName.equals("let")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = let body
            }
            
            // For if: (if test then else)
            // Positions: 0=if, 1=test, 2=then, 3=else - all are expressions
            if (functionName.equals("if")) {
                return cursorPosition >= functionIndex + 1; // All positions after 'if' are expressions
            }
            
            // For when: (when test body1 body2 ...)
            if (functionName.equals("when") || functionName.equals("when-not")) {
                return cursorPosition >= functionIndex + 1; // All positions after 'when' are expressions  
            }
            
            // For when-let: (when-let [binding test] body1 body2 ...)
            if (functionName.equals("when-let")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = body expressions
            }
            
            // For if-let: (if-let [binding test] then else?)
            if (functionName.equals("if-let")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = then/else expressions
            }
            
            // For do: (do expr1 expr2 ...)
            if (functionName.equals("do")) {
                return cursorPosition >= functionIndex + 1; // All positions after 'do' are expressions
            }
            
            // For case: (case expr & clauses) - clauses are expressions
            if (functionName.equals("case") || functionName.equals("cond")) {
                return cursorPosition >= functionIndex + 1; // All positions after case/cond are expressions
            }
            
            // For binding: (binding [bindings*] expr*) - similar to let
            if (functionName.equals("binding")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = body expressions
            }
            
            // For try: (try expr* catch* finally?) - expressions in body
            if (functionName.equals("try")) {
                return cursorPosition >= functionIndex + 1; // All positions after 'try' are expressions
            }
            
            // For fn: (fn [params] body1 body2 ...)
            if (functionName.equals("fn")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = function body
            }
            
            // For loop: (loop [bindings] body1 body2 ...)
            if (functionName.equals("loop")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = loop body
            }
            
            // For iteration constructs: (for [seq-exprs] body), (foreach [binding coll] body*)
            if (functionName.equals("for") || functionName.equals("dofor") || functionName.equals("foreach")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = body expressions
            }
            
            // For definterface: (definterface name & methods) - methods are expressions
            if (functionName.equals("definterface")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = method expressions
            }
            
            return false; // Default: treat as argument context
        }
    }

    /**
     * Context information for function calls
     */
    private static class FunctionCallContext {
        private final String functionName;
        private final int argumentPosition;
        
        public FunctionCallContext(String functionName, int argumentPosition) {
            this.functionName = functionName;
            this.argumentPosition = argumentPosition;
        }
        
        public String getFunctionName() {
            return functionName;
        }
        
        public int getArgumentPosition() {
            return argumentPosition;
        }
        
        public boolean isInArgumentPosition() {
            return argumentPosition >= 0; // 0 = first argument, 1 = second, etc.
        }
    }

    /**
     * Keyword completion provider - handles :keyword completions
     */
    private static class PhelKeywordCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

            // Common keywords used in Phel
            String[] commonKeywords = {"require", "use", "refer", "as", "import", "export", "private", "doc", "test", "deprecated", "added", "range", "in", "keys", "pairs", "while", "let", "when", "reduce", "debug", "prod", "dev", "cache", "timeout", "method", "path", "headers", "body", "status", "type", "name", "value"};

            Icon keywordIcon = AllIcons.Nodes.PropertyReadWrite;
            for (String keyword : commonKeywords) {
                PhelCompletionRanking.addRankedCompletion(result, keyword, 
                    PhelCompletionRanking.Priority.API_FUNCTIONS, "keyword", null, keywordIcon);
            }
        }
    }

    /**
     * Namespace keyword completion provider - handles ::keyword completions
     */
    private static class PhelNamespaceKeywordCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {

            // Current namespace keywords - these would ideally come from analyzing the current file
            String[] namespaceKeywords = {"private", "export", "test", "doc", "deprecated", "inline", "macro", "dynamic", "const"};

            Icon namespaceIcon = AllIcons.Nodes.Constant;
            for (String keyword : namespaceKeywords) {
                result.addElement(LookupElementBuilder.create(keyword).withIcon(namespaceIcon).withTypeText("namespace keyword"));
            }
        }
    }
}
