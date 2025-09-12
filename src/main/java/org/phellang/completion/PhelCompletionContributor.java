package org.phellang.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.phellang.PhelIcons;
import org.phellang.language.psi.PhelList;
import org.phellang.language.psi.PhelSymbol;
import org.phellang.language.psi.PhelTypes;
import org.phellang.language.psi.PhelVec;
import org.phellang.language.psi.impl.PhelAccessImpl;
import org.phellang.language.psi.PhelAccess;

import javax.swing.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
     * Custom insert handler for balanced parentheses - inserts "()" and positions cursor inside
     */
    public static class BalancedParenthesesInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            // Replace the completion with "()" 
            String completionText = "()";
            document.replaceString(caretOffset, context.getTailOffset(), completionText);
            
            // Position cursor between the parentheses
            editor.getCaretModel().moveToOffset(caretOffset + 1);
        }
    }
    
    private static final BalancedParenthesesInsertHandler BALANCED_PARENS_INSERT_HANDLER = new BalancedParenthesesInsertHandler();
    
    /**
     * Custom insert handler for balanced brackets - inserts "[]" and positions cursor inside
     */
    public static class BalancedBracketsInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            // Replace the completion with "[]" 
            String completionText = "[]";
            document.replaceString(caretOffset, context.getTailOffset(), completionText);
            
            // Position cursor between the brackets
            editor.getCaretModel().moveToOffset(caretOffset + 1);
        }
    }
    
    private static final BalancedBracketsInsertHandler BALANCED_BRACKETS_INSERT_HANDLER = new BalancedBracketsInsertHandler();
    
    /**
     * Template insert handler for defn - inserts "(defn name [params] body)" with "name" selected as placeholder
     */
    public static class DefnTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(defn name [] ())";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Select "name" placeholder for immediate editing
            int nameStart = caretOffset + 6; // "(defn ".length()
            int nameEnd = nameStart + 4; // "name".length()
            editor.getSelectionModel().setSelection(nameStart, nameEnd);
            editor.getCaretModel().moveToOffset(nameEnd);
        }
    }
    
    private static final DefnTemplateInsertHandler DEFN_TEMPLATE_INSERT_HANDLER = new DefnTemplateInsertHandler();
    
    /**
     * Template insert handler for def - inserts "(def name value)" with "name" selected as placeholder
     */
    public static class DefTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(def name )";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Select "name" placeholder for immediate editing
            int nameStart = caretOffset + 5; // "(def ".length()
            int nameEnd = nameStart + 4; // "name".length()
            editor.getSelectionModel().setSelection(nameStart, nameEnd);
            editor.getCaretModel().moveToOffset(nameEnd);
        }
    }
    
    private static final DefTemplateInsertHandler DEF_TEMPLATE_INSERT_HANDLER = new DefTemplateInsertHandler();
    
    /**
     * Template insert handler for let - inserts "(let [bindings] body)" with "bindings" selected as placeholder
     */
    public static class LetTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(let [bindings] )";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Select "bindings" placeholder for immediate editing
            int bindingsStart = caretOffset + 6; // "(let [".length()
            int bindingsEnd = bindingsStart + 8; // "bindings".length()
            editor.getSelectionModel().setSelection(bindingsStart, bindingsEnd);
            editor.getCaretModel().moveToOffset(bindingsEnd);
        }
    }
    
    private static final LetTemplateInsertHandler LET_TEMPLATE_INSERT_HANDLER = new LetTemplateInsertHandler();
    
    /**
     * Template insert handler for if - inserts "(if condition then else)" with "condition" selected as placeholder
     */
    public static class IfTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(if condition) ";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Select "condition" placeholder for immediate editing
            int conditionStart = caretOffset + 4; // "(if ".length()
            int conditionEnd = conditionStart + 9; // "condition".length()
            editor.getSelectionModel().setSelection(conditionStart, conditionEnd);
            editor.getCaretModel().moveToOffset(conditionEnd);
        }
    }
    
    private static final IfTemplateInsertHandler IF_TEMPLATE_INSERT_HANDLER = new IfTemplateInsertHandler();
    
    /**
     * Template insert handler for fn - inserts "(fn [params] body)" with "params" selected as placeholder
     */
    public static class FnTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(fn [params] )";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Select "params" placeholder for immediate editing
            int paramsStart = caretOffset + 5; // "(fn [".length()
            int paramsEnd = paramsStart + 6; // "params".length()
            editor.getSelectionModel().setSelection(paramsStart, paramsEnd);
            editor.getCaretModel().moveToOffset(paramsEnd);
        }
    }
    
    private static final FnTemplateInsertHandler FN_TEMPLATE_INSERT_HANDLER = new FnTemplateInsertHandler();
    
    /**
     * Template insert handler for when - inserts "(when condition body)" with "condition" selected as placeholder
     */
    public static class WhenTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(when condition )";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Select "condition" placeholder for immediate editing
            int conditionStart = caretOffset + 6; // "(when ".length()
            int conditionEnd = conditionStart + 9; // "condition".length()
            editor.getSelectionModel().setSelection(conditionStart, conditionEnd);
            editor.getCaretModel().moveToOffset(conditionEnd);
        }
    }
    
    private static final WhenTemplateInsertHandler WHEN_TEMPLATE_INSERT_HANDLER = new WhenTemplateInsertHandler();
    
    /**
     * Template insert handler for :require-file - inserts "(:require-file )" with cursor positioned inside
     */
    public static class RequireFileTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(:require-file )";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Position cursor inside the parentheses, after the space
            int cursorPos = caretOffset + 15; // "(:require-file ".length()
            editor.getCaretModel().moveToOffset(cursorPos);
        }
    }
    
    private static final RequireFileTemplateInsertHandler REQUIRE_FILE_TEMPLATE_INSERT_HANDLER = new RequireFileTemplateInsertHandler();
    
    /**
     * Template insert handler for :require - inserts "(:require )" with cursor positioned inside
     */
    public static class RequireTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(:require )";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Position cursor inside the parentheses, after the space
            int cursorPos = caretOffset + 10; // "(:require ".length()
            editor.getCaretModel().moveToOffset(cursorPos);
        }
    }
    
    private static final RequireTemplateInsertHandler REQUIRE_TEMPLATE_INSERT_HANDLER = new RequireTemplateInsertHandler();
    
    /**
     * Template insert handler for :use - inserts "(:use )" with cursor positioned inside
     */
    public static class UseTemplateInsertHandler implements InsertHandler<LookupElement> {
        @Override
        public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
            Editor editor = context.getEditor();
            Document document = editor.getDocument();
            int caretOffset = context.getStartOffset();
            
            String template = "(:use )";
            document.replaceString(caretOffset, context.getTailOffset(), template);
            
            // Position cursor inside the parentheses, after the space
            int cursorPos = caretOffset + 6; // "(:use ".length()
            editor.getCaretModel().moveToOffset(cursorPos);
        }
    }
    
    private static final UseTemplateInsertHandler USE_TEMPLATE_INSERT_HANDLER = new UseTemplateInsertHandler();

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

            // Comprehensive error handling for completion operation
            PhelCompletionErrorHandler.withErrorHandling(
                PhelCompletionErrorHandler.withResultSet(result, () -> {
                    performCompletionWithValidation(parameters, result);
                }),
                "main completion process",
                () -> {
                    // Fallback: provide basic completions if detailed completion fails
                    addBasicFallbackCompletions(result);
                }
            );
        }
        
        private void performCompletionWithValidation(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) throws Exception {
            PsiElement element = parameters.getPosition();
            
            // Validate completion context before proceeding
            if (!PhelCompletionErrorHandler.isCompletionContextValid(element)) {
                throw new IllegalStateException("Invalid completion context: PSI element or tree is malformed");
            }
            
            String prefix = getCompletionPrefix(element);
            

            // PRIORITY 0: New form completion - suggest ONLY "(" when starting a new expression
            // This is the ONLY valid syntax at top level, so return early
            if (shouldSuggestNewForm(element, prefix)) {
                addNewFormCompletion(result);
                // Also add template completions at top level for convenience
                addTemplateCompletions(result);
                return; // Don't show any other completions at top level
            }

            // Check if we're at the parameter vector position in definition forms
            if (isInParameterVectorPosition(element)) {
                addParameterVectorCompletion(result);
                return; // Only suggest [ for parameter vector
            }
            
            // Check if we're inside a parameter vector
            boolean insideParamVector = isInsideParameterVector(element);
            if (insideParamVector) {
                addParameterCompletions(result, element);
                return; // Only suggest parameter names and ] (if unclosed)
            }

            // Check if we're in a namespace (ns) context - special handling needed
            if (isInNamespaceContext(element)) {
            addNamespaceCompletions(result, element);
                return; // Early return for namespace contexts
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
                
                // Add structural completions
                if (isInDefinitionBodyContext(element)) {
                    addDefinitionBodyCompletion(result);
                } else {
                    addExpressionCompletion(result);
                }
                
                // Add template completions for common constructs (only in expression contexts)
                addTemplateCompletions(result);
                
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
                .create("()")
                .withPresentableText("()")
                .withTypeText("Start new expression")
                .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE)
                .bold();

            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                element, PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS.value));
        }
        
        /**
         * Add completion for expressions in function bodies, let bodies, etc. - balanced ()
         */
        private void addExpressionCompletion(CompletionResultSet result) {
            LookupElementBuilder balancedParen = LookupElementBuilder
                .create("()")
                .withPresentableText("()")
                .withTypeText("Start new expression")
                .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                balancedParen, PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS.value));
        }
        
        /**
         * Add completion for definition bodies (defn, defmacro) - suggest balanced ()
         */
        private void addDefinitionBodyCompletion(CompletionResultSet result) {
            // Focus on balanced parentheses for new expressions - users can manually type ) to close
            LookupElementBuilder balancedParen = LookupElementBuilder
                .create("()")
                .withPresentableText("()")
                .withTypeText("Start new expression")
                .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                balancedParen, PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS.value));
        }
        
        /**
         * Add template completions for common Phel constructs
         */
        private void addTemplateCompletions(CompletionResultSet result) {
            // defn template - (defn name [params] body)
            LookupElementBuilder defnTemplate = LookupElementBuilder
                .create("defn-template")
                .withPresentableText("defn")
                .withTypeText("Function definition template")
                .withTailText(" (defn name [params] body)", true)
                .withInsertHandler(DEFN_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                defnTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value));
            
            // def template - (def name value)
            LookupElementBuilder defTemplate = LookupElementBuilder
                .create("def-template")
                .withPresentableText("def")
                .withTypeText("Variable definition template")
                .withTailText(" (def name value)", true)
                .withInsertHandler(DEF_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                defTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value));
            
            // let template - (let [bindings] body)
            LookupElementBuilder letTemplate = LookupElementBuilder
                .create("let-template")
                .withPresentableText("let")
                .withTypeText("Let binding template")
                .withTailText(" (let [bindings] body)", true)
                .withInsertHandler(LET_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                letTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value));
            
            // if template - (if condition then else)
            LookupElementBuilder ifTemplate = LookupElementBuilder
                .create("if-template")
                .withPresentableText("if")
                .withTypeText("Conditional template")
                .withTailText(" (if condition then else)", true)
                .withInsertHandler(IF_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                ifTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value));
            
            // fn template - (fn [params] body)
            LookupElementBuilder fnTemplate = LookupElementBuilder
                .create("fn-template")
                .withPresentableText("fn")
                .withTypeText("Anonymous function template")
                .withTailText(" (fn [params] body)", true)
                .withInsertHandler(FN_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                fnTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value));
            
            // when template - (when condition body)
            LookupElementBuilder whenTemplate = LookupElementBuilder
                .create("when-template")
                .withPresentableText("when")
                .withTypeText("Conditional template")
                .withTailText(" (when condition body)", true)
                .withInsertHandler(WHEN_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                whenTemplate, PhelCompletionRanking.Priority.API_FUNCTIONS.value));
        }
        
        /**
         * Check if we're in a definition body context (defn, defmacro, fn)
         * where both ( and ) should be suggested for function bodies
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
                                text.equals("defmacro") || text.equals("defmacro-") ||
                                text.equals("deftest")) {
                                // Check if we're in the body position (after parameters)
                                int cursorPosition = getCursorPositionInList(list, element);
                                // defn/defmacro: name [params] body (position 3+)
                                // deftest: test-name body (position 2+) 
                                int bodyStartPosition = text.equals("deftest") ? 2 : 3;
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
                .withPresentableText("[]")
                .withTypeText("Function parameters")
                .withTailText(" Parameter vector", true)
                .withInsertHandler(BALANCED_BRACKETS_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);

            result.addElement(element);
        }
        
        /**
         * Check if we're inside a parameter/binding vector [...]
         * e.g., (defn factorial [n |), (let [a |), (for [x |), (loop [i |) should suggest parameter names and ]
         */
        private boolean isInsideParameterVector(PsiElement element) {
            // Find the MOST IMMEDIATE/CLOSEST PhelVec ancestor
            // This is crucial for nested structures like (let [a (let [b (let [c ] |
            PhelVec closestVector = null;
            PsiElement current = element;
            
            // Find all PhelVec ancestors and choose the best one
            java.util.List<PhelVec> vectors = new java.util.ArrayList<>();
            while (current != null) {
                if (current instanceof PhelVec) {
                    vectors.add((PhelVec) current);
                }
                current = current.getParent();
            }
            
            // Choose the best vector from our candidates
            for (PhelVec vector : vectors) {
                String vectorText = vector.getText();
                
                // Skip malformed giant vectors
                if (vectorText.length() > 1000) {
                    continue;
                }
                
                // Priority 1: Small completed vectors that are probably inner vectors like [c]
                if (vectorText.endsWith("]") && vectorText.length() < 20) {
                    // But check if cursor is actually after this vector (outside it)
                    int cursorOffset = element.getTextOffset();
                    int vectorStart = vector.getTextOffset();
                    int cursorOffsetInVector = cursorOffset - vectorStart;
                    
                    if (cursorOffsetInVector >= vectorText.length() - 1) {
                        continue; // Keep looking for a parent vector we're actually inside
                    } else {
                        closestVector = vector;
                        break;
                    }
                }
                
                // Priority 2: Unclosed vectors that we might be inside (intermediate vectors like [b ...])
                else if (!vectorText.endsWith("]") && vectorText.length() < 500) {
                    closestVector = vector;
                    break; // This is probably the vector we're inside that needs closing
                }
                
                // Priority 3: Any reasonable-sized vector as fallback
                else if (vectorText.length() < 100) {
                    if (closestVector == null) {
                        closestVector = vector;
                    }
                }
            }
            
            if (closestVector == null) {
                return false; // No vector ancestor
            }
            
            String vectorText = closestVector.getText();
            
            // ROBUSTNESS: Handle malformed/incomplete syntax where vectors are huge due to missing closing brackets
            if (vectorText.length() > 1000) {
                return false; // Probably malformed syntax with missing closing brackets
            }
            
            // Calculate cursor position relative to the vector start
            int cursorOffset = element.getTextOffset();
            int vectorStart = closestVector.getTextOffset();
            int cursorOffsetInVector = cursorOffset - vectorStart;
            int vectorLength = vectorText.length();
            
            
            // If vector ends with ] and cursor is at or after the closing ], we're NOT inside
            if (vectorText.endsWith("]") && cursorOffsetInVector >= vectorLength - 1) {
                return false; // We're after the closing ], not inside
            }
            
            // Check if this vector is in a parameter/binding position
            PsiElement parent = closestVector.getParent();
            if (parent instanceof PhelList) {
                PhelList list = (PhelList) parent;
                PsiElement[] children = list.getChildren();
                
                // Look for definition/binding keywords
                for (int i = 0; i < children.length; i++) {
                    PsiElement child = children[i];
                    
                    if (child instanceof PhelSymbol || child instanceof PhelAccessImpl) {
                        String text = child.getText();
                        if (text.equals("defn") || text.equals("defn-") || 
                            text.equals("defmacro") || text.equals("defmacro-") ||
                            text.equals("fn") || text.equals("let") || 
                            text.equals("for") || text.equals("loop")) {
                            return true; // This vector is in a binding context and cursor is inside
                        }
                    }
                }
            }
            
            return false;
        }
        
        /**
         * Check if we're inside a parameter vector that is already closed: [param|]
         */
        private boolean isInsideClosedParameterVector(PsiElement element) {
            // Find the immediate PhelVec ancestor
            PsiElement current = element;
            while (current != null) {
                if (current instanceof PhelVec) {
                    PhelVec vector = (PhelVec) current;
                    String vectorText = vector.getText().trim();
                    
                    // Check if this vector ends with ] (is closed)
                    if (vectorText.endsWith("]")) {
                        // Verify that the cursor is actually INSIDE this closed vector
                        int cursorOffset = element.getTextOffset();
                        int vectorStart = vector.getTextOffset();
                        int vectorEnd = vectorStart + vectorText.length();
                        
                        // If cursor is before the closing bracket, we're inside a closed vector
                        if (cursorOffset >= vectorStart && cursorOffset < vectorEnd - 1) {
                            return true;
                        }
                    }
                    
                    // Check parent vector if current one doesn't match
                    current = current.getParent();
                } else {
                    current = current.getParent();
                }
            }
            
            return false;
        }
        
        /**
         * Add parameter completions - suggests parameter names and closing ] (if vector is unclosed)
         */
        private void addParameterCompletions(CompletionResultSet result, PsiElement element) {
            
            // Check if we're inside a closed parameter vector [param|]
            boolean isVectorClosed = isInsideClosedParameterVector(element);
            
            // Only suggest closing bracket if the vector is NOT already closed
            if (!isVectorClosed) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    "]",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,  // High priority for structural elements like ]
                    null,  // No signature
                    "Close parameter vector",
                    org.phellang.PhelIcons.FILE
                );
            }
            
            // Suggest common parameter names
            String[] commonParams = {
                "x", "y", "z",           // Generic single values
                "n", "m", "i", "j",      // Numeric/index values  
                "coll", "xs", "ys",      // Collections
                "f", "pred",             // Functions/predicates
                "key", "val", "kv",      // Key-value pairs
                "a", "b", "c",           // Generic arguments
                "item", "elem",          // Collection elements
                "acc", "result",         // Accumulators
                "param", "param3", "param4", "parameter", "params"  // Parameter-like names
            };
            
            for (String param : commonParams) {
                // Use smart ranking for parameter names - lower priority than closing bracket
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    param,
                    PhelCompletionRanking.Priority.PROJECT_SYMBOLS,  // Lower priority for parameter suggestions (25.0)
                    null,  // No signature
                    "Parameter name",
                    PhelIconProvider.PARAMETER
                );
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
                            boolean isExpression = isExpressionContext(functionName, cursorPosition, functionIndex);
                            if (isExpression) {
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
         * Basic fallback completions when detailed completion fails
         */
        private void addBasicFallbackCompletions(@NotNull CompletionResultSet result) {
            try {
                // Provide basic Phel syntax completions as fallback
                addBasicValues(result);
                
                // Add core language constructs with HIGHEST priority
                LookupElementBuilder balancedParen = LookupElementBuilder.create("(")
                    .withPresentableText("() ")
                    .withTailText(" Start expression", true)
                    .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER)
                    .withIcon(org.phellang.PhelIcons.FILE);
                result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                    balancedParen, PhelCompletionRanking.Priority.CURRENT_SCOPE_LOCALS.value));
                    
                // Add most common functions without context-aware ranking
                String[] commonFunctions = {"defn", "fn", "let", "if", "when", "map", "filter", "reduce", "get", "count"};
                for (String func : commonFunctions) {
                    result.addElement(LookupElementBuilder.create(func)
                        .withIcon(PhelIconProvider.SPECIAL_FORM));
                }
            } catch (Exception e) {
                // Even fallback failed - just provide minimal completion
                result.addElement(LookupElementBuilder.create("(")
                    .withTailText(" - Expression", true)
                    .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER));
            }
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
            
            // 4. Always allow nested expressions in arguments - any function can accept (nested-call) as arguments
            // Add balanced parentheses for nested expressions
            LookupElementBuilder parenElement = LookupElementBuilder
                .create("()")
                .withPresentableText("() ")
                .withTypeText("Nested expression")
                .withInsertHandler(BALANCED_PARENS_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            
            // High priority for nested expressions (but not highest since values are also important in arguments)
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                parenElement, PhelCompletionRanking.Priority.COMMON_BUILTINS.value));
        }

        /**
         * Check if we're in an expression context rather than argument context
         * for special forms like defn, let, if, etc.
         */
        private boolean isExpressionContext(String functionName, int cursorPosition, int functionIndex) {
            // For ns: (ns namespace-name (:require...) (:use...)) - special handling needed
            if (functionName.equals("ns")) {
                return false; // Handle ns specially, not as expression context
            }
            
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
            
            // For deftest: (deftest test-name body1 body2 ...)
            // Positions: 0=deftest, 1=test-name, 2+=test body expressions
            if (functionName.equals("deftest")) {
                return cursorPosition >= functionIndex + 2; // Position 2+ = test body
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
            
            // For with-output-buffer: (with-output-buffer expr1 expr2 ...)
            // Positions: 0=with-output-buffer, 1+=body expressions
            if (functionName.equals("with-output-buffer")) {
                return cursorPosition >= functionIndex + 1; // Position 1+ = body expressions
            }
            
            return false; // Default: treat as argument context
        }
        
        /**
         * Check if we're in a namespace (ns) context that needs special completion
         */
        private boolean isInNamespaceContext(PsiElement element) {
            // Find the parent list and check if it's an ns form
            PsiElement current = element;
            while (current != null) {
                if (current instanceof PhelList) {
                    PhelList list = (PhelList) current;
                    PsiElement[] children = list.getChildren();
                    
                    if (children.length >= 1) {
                        PsiElement firstChild = children[0];
                        if (firstChild instanceof PhelSymbol || firstChild instanceof PhelAccessImpl) {
                            String text = firstChild.getText();
                            if (text.equals("ns")) {
                                return true; // We're inside an ns form
                            }
                        }
                    }
                }
                current = current.getParent();
            }
            return false;
        }
        
        /**
         * Add namespace-specific completions for ns forms
         */
        private void addNamespaceCompletions(CompletionResultSet result, PsiElement element) {
            try {
                // Find the ns list to understand the context
                PhelList nsList = null;
                PsiElement current = element;
                while (current != null) {
                    if (current instanceof PhelList) {
                        PhelList list = (PhelList) current;
                        PsiElement[] children = list.getChildren();
                        if (children.length >= 1) {
                            PsiElement firstChild = children[0];
                            if ((firstChild instanceof PhelSymbol || firstChild instanceof PhelAccessImpl) 
                                && firstChild.getText().equals("ns")) {
                                nsList = list;
                                break;
                            }
                        }
                    }
                    current = current.getParent();
                }
                
                if (nsList == null) return;
                
                // Determine cursor position in the ns form
                int cursorPosition = getCursorPositionInList(nsList, element);
                PsiElement[] nsChildren = nsList.getChildren();
                
                // Position 0 = ns, Position 1 = namespace-name, Position 2+ = namespace options
                if (cursorPosition <= 1) {
                    // Still completing namespace name - no special completions needed
                    return;
                }
                
                // Check what kind of namespace completion context we're in
                String contextText = getCurrentTextContext(element, 50);
                
                // Determine completion type based on context  
                boolean inRequire = isInRequireContext(element);
                boolean inRequireFile = isInRequireFileContext(element);
                boolean inUse = isInUseContext(element);
                
                if (inRequire) {
                    addRequireCompletions(result, element);
                } else if (inRequireFile) {
                    addRequireFileCompletions(result, element);
                } else if (inUse) {
                    addUseCompletions(result, element);
                } else {
                    // Top-level ns completions
                    addTopLevelNamespaceCompletions(result);
                }
                
            } catch (Exception e) {
                // Fallback to basic completions
                addTopLevelNamespaceCompletions(result);
            }
        }
        
        /**
         * Add top-level namespace completions (:require, :require-file, :use)
         */
        private void addTopLevelNamespaceCompletions(CompletionResultSet result) {
            // Add (:require with custom insert handler that positions cursor inside
            LookupElementBuilder requireElement = LookupElementBuilder
                .create("(:require )")
                .withPresentableText("(:require )")
                .withTypeText("Import namespace with optional alias")
                .withTailText(" namespace\\name :as alias", true)
                .withInsertHandler(REQUIRE_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                requireElement, PhelCompletionRanking.Priority.SPECIAL_FORMS.value));
            
            // Add (:require-file with custom insert handler that positions cursor inside
            LookupElementBuilder requireFileElement = LookupElementBuilder
                .create("(:require-file )")
                .withPresentableText("(:require-file )")
                .withTypeText("Include PHP file (e.g., vendor/autoload.php)")
                .withTailText(" \"path/to/file.php\"", true)
                .withInsertHandler(REQUIRE_FILE_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                requireFileElement, PhelCompletionRanking.Priority.SPECIAL_FORMS.value));
            
            // Add (:use with custom insert handler that positions cursor inside
            LookupElementBuilder useElement = LookupElementBuilder
                .create("(:use )")
                .withPresentableText("(:use )")
                .withTypeText("Import symbols from namespace or PHP class")
                .withTailText(" namespace\\name) or Some\\Php\\Class)", true)
                .withInsertHandler(USE_TEMPLATE_INSERT_HANDLER)
                .withIcon(org.phellang.PhelIcons.FILE);
            result.addElement(com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority(
                useElement, PhelCompletionRanking.Priority.MACROS.value));
        }
        
        /**
         * Check if we're in a :require context (but not :require-file)
         */
        private boolean isInRequireContext(PsiElement element) {
            String context = getCurrentTextContext(element, 200);
            
            
            boolean hasRequire = context.contains(":require") || context.contains("(:require");
            boolean hasRequireFile = context.contains(":require-file") || context.contains("(:require-file");
            
            // Check if we're inside an incomplete require statement vs after a completed one
            // Look for complete (:require ...) patterns in the context
            boolean afterCompleteRequire = false;
            
            // Simpler approach: if context contains a line ending with ) and current line is just IntellijIdeaRulezzz
            String[] lines = context.split("\\n");
            
            if (lines.length >= 2) {
                String currentLine = lines[lines.length - 1].trim();
                
                // If current line is just the IntelliJ placeholder, check previous lines for complete forms
                if (currentLine.equals("IntellijIdeaRulezzz") || currentLine.isEmpty()) {
                    // Look for any complete require forms in previous lines
                    for (int i = 0; i < lines.length - 1; i++) {
                        String line = lines[i].trim();
                        // Simple check: line contains :require and ends with )
                        if (line.contains(":require") && line.endsWith(")")) {
                            afterCompleteRequire = true;
                            break;
                        }
                    }
                }
            }
            
            
            return hasRequire && !hasRequireFile && !afterCompleteRequire;
        }
        
        /**
         * Check if we're in a :require-file context
         */
        private boolean isInRequireFileContext(PsiElement element) {
            String context = getCurrentTextContext(element, 200);
            
            
            boolean hasRequireFile = context.contains(":require-file") || context.contains("(:require-file");
            
            // Use same simple line-by-line approach as isInRequireContext
            String[] lines = context.split("\\n");
            
            boolean afterCompleteRequireFile = false;
            if (lines.length >= 2) {
                String currentLine = lines[lines.length - 1].trim();
                
                // If current line is just the IntelliJ placeholder, check previous lines for complete forms
                if (currentLine.equals("IntellijIdeaRulezzz") || currentLine.isEmpty()) {
                    // Look for any complete require-file forms in previous lines
                    for (int i = 0; i < lines.length - 1; i++) {
                        String line = lines[i].trim();
                        // Simple check: line contains :require-file and ends with )
                        if (line.contains(":require-file") && line.endsWith(")")) {
                            afterCompleteRequireFile = true;
                            break;
                        }
                    }
                }
            }
            
            
            return hasRequireFile && !afterCompleteRequireFile;
        }
        
        /**
         * Check if we're in a :use context
         */
        private boolean isInUseContext(PsiElement element) {
            String context = getCurrentTextContext(element, 200);
            
            boolean hasUse = context.contains(":use") || context.contains("(:use");
            
            // Use same simple line-by-line approach
            String[] lines = context.split("\\n");
            boolean afterCompleteUse = false;
            if (lines.length >= 2) {
                String currentLine = lines[lines.length - 1].trim();
                
                // If current line is just the IntelliJ placeholder, check previous lines for complete forms
                if (currentLine.equals("IntellijIdeaRulezzz") || currentLine.isEmpty()) {
                    // Look for any complete use forms in previous lines
                    for (int i = 0; i < lines.length - 1; i++) {
                        String line = lines[i].trim();
                        // Simple check: line contains :use and ends with )
                        if (line.contains(":use") && line.endsWith(")")) {
                            afterCompleteUse = true;
                            break;
                        }
                    }
                }
            }
            
            return hasUse && !afterCompleteUse;
        }
        
        /**
         * Add completions for :require context
         */
        private void addRequireCompletions(CompletionResultSet result, PsiElement element) {
            String context = getCurrentTextContext(element, 200);
            
            
            // Extract the current namespace prefix being typed
            String namespacePrefix = extractNamespacePrefix(context);
            
            // Improved logic: detect different completion scenarios
            boolean hasCompleteNamespaceWithSpace = context.matches(".*:require\\s+[a-zA-Z][a-zA-Z0-9\\\\.-]+\\s+.*");
            boolean justAfterRequire = context.matches(".*:require\\s*IntellijIdeaRulezzz.*") ||
                                     context.matches(".*\\(:require\\s*IntellijIdeaRulezzz.*");
            boolean hasPartialNamespace = !namespacePrefix.isEmpty();
            boolean isCompleteKnownNamespace = isKnownCompleteNamespace(namespacePrefix);
            
            
            // Case 1: After a complete known namespace (like "phel\base64") - suggest options
            if (isCompleteKnownNamespace) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":as",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":as alias-name",
                    "Create an alias for the namespace",
                    org.phellang.PhelIcons.FILE
                );
                
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":refer []",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":refer [symbol1 symbol2]",
                    "Import specific symbols directly",
                    org.phellang.PhelIcons.FILE
                );
            }
            // Case 2: Just after :require OR typing partial namespace (like "phel\")
            else if (justAfterRequire || hasPartialNamespace) {
                // Suggest common Phel namespaces with prefix filtering
                addCommonPhelNamespacesWithPrefix(result, namespacePrefix);
                
                // Suggest actual namespaces found in project .phel files with prefix filtering
                addProjectPhelNamespacesWithPrefix(result, element, namespacePrefix);
            }
            // Case 3: After a complete namespace name followed by space - suggest options
            else if (hasCompleteNamespaceWithSpace) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":as",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":as alias-name",
                    "Create an alias for the namespace",
                    org.phellang.PhelIcons.FILE
                );
                
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":refer []",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":refer [symbol1 symbol2]",
                    "Import specific symbols directly",
                    org.phellang.PhelIcons.FILE
                );
            }
            // Case 3: After :refer, suggest [ to start symbol list
            else if (context.contains(":refer") && !context.contains("[")) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    "[",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    "[symbol-list]",
                    "Start symbol list for :refer",
                    org.phellang.PhelIcons.FILE
                );
            }
            // Case 4: Unexpected fallback - should not happen with good logic
            else {
            }
        }
        
        /**
         * Add completions for :require-file context
         */
        private void addRequireFileCompletions(CompletionResultSet result, PsiElement element) {
            String context = getCurrentTextContext(element, 200);
            
            
            // Check if there's already a filename string in the context
            boolean hasFilename = context.matches(".*:require-file\\s+\"[^\"]+\".*");
            boolean justAfterRequireFile = context.matches(".*:require-file\\s*IntellijIdeaRulezzz.*") ||
                                         context.matches(".*\\(:require-file\\s*IntellijIdeaRulezzz.*");
            
            
            // Case 1: Just after :require-file (no filename yet)
            if (justAfterRequireFile) {
                
                // Suggest common PHP file paths
                String[] commonPaths = {
                    "\"vendor/autoload.php\"",
                    "\"bootstrap.php\"",
                    "\"config.php\"",
                    "\"functions.php\""
                };
                
                for (String path : commonPaths) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        path,
                        PhelCompletionRanking.Priority.API_FUNCTIONS,
                        null,
                        "Common PHP file to include",
                        PhelIconProvider.PHP_INTEROP
                    );
                }
            }
            // Case 2: After a filename - suggest options
            else if (hasFilename) {
                
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":as",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":as alias-name",
                    "Create alias for the included file",
                    org.phellang.PhelIcons.FILE
                );
                
                // Removed ) completion - users can manually close the form
            }
            // Case 3: Fallback
            else {
                
                // Suggest common PHP file paths
                String[] commonPaths = {
                    "\"vendor/autoload.php\"",
                    "\"bootstrap.php\"",
                    "\"config.php\"",
                    "\"functions.php\""
                };
                
                for (String path : commonPaths) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        path,
                        PhelCompletionRanking.Priority.API_FUNCTIONS,
                        null,
                        "Common PHP file to include",
                        PhelIconProvider.PHP_INTEROP
                    );
                }
            }
        }
        
        /**
         * Add completions for :use context
         */
        private void addUseCompletions(CompletionResultSet result, PsiElement element) {
            String context = getCurrentTextContext(element, 200);
            
            
            // Check if there's already a PHP class in the context
            boolean hasClass = context.matches(".*:use\\s+\\\\?[a-zA-Z][a-zA-Z0-9\\\\.-]+.*");
            boolean justAfterUse = context.matches(".*:use\\s*IntellijIdeaRulezzz.*") ||
                                 context.matches(".*\\(:use\\s*IntellijIdeaRulezzz.*");
            
            
            // Case 1: Just after :use (no class yet)
            if (justAfterUse) {
                
                // :use is for PHP classes only, not Phel namespaces
                addPhpClassCompletions(result);
                
                // Add example suggestions
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    "\\DateTime",
                    PhelCompletionRanking.Priority.API_FUNCTIONS,
                    null,
                    "PHP DateTime class (example)",
                    PhelIconProvider.PHP_INTEROP
                );
            }
            // Case 2: After a PHP class name - suggest options
            else if (hasClass) {
                
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    ":as",
                    PhelCompletionRanking.Priority.SPECIAL_FORMS,
                    ":as alias-name",
                    "Create alias for the imported PHP class",
                    org.phellang.PhelIcons.FILE
                );
                
                // Removed ) completion - users can manually close the form
            }
            // Case 3: Fallback
            else {
                
                // :use is for PHP classes only, not Phel namespaces
                addPhpClassCompletions(result);
            }
        }
        
        /**
         * Add completions for PHP class imports
         */
        private void addPhpClassCompletions(CompletionResultSet result) {
            // Suggest common PHP class patterns
            String[] commonPhpClasses = {
                "Exception",
                "DateTime",
                "PDO",
                "stdClass"
            };
            
            for (String className : commonPhpClasses) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    className,
                    PhelCompletionRanking.Priority.PHP_INTEROP,
                    null,
                    "PHP class",
                    PhelIconProvider.PHP_INTEROP
                );
            }
        }
        
        /**
         * Add common Phel namespace suggestions
         */
        private void addCommonPhelNamespaces(CompletionResultSet result) {
            String[] commonNamespaces = {
                "phel\\test", "phel\\json", "phel\\http", "phel\\str", 
                "phel\\html", "phel\\core", "phel\\base64", "phel\\local",
                "phel\\repl", "phel\\trace"
            };
            
            
            for (String namespace : commonNamespaces) {
                PhelCompletionRanking.addRankedCompletion(
                    result,
                    namespace,
                    PhelCompletionRanking.Priority.API_FUNCTIONS,
                    null,
                    "Common Phel namespace",
                    PhelIconProvider.NAMESPACE
                );
            }
        }
        
        /**
         * Get text context around the current element for parsing
         */
        private String getCurrentTextContext(PsiElement element, int maxLength) {
            try {
                StringBuilder context = new StringBuilder();
                
                // First, get the parent list to understand the broader context
                PsiElement parentList = element;
                while (parentList != null && !(parentList instanceof PhelList)) {
                    parentList = parentList.getParent();
                }
                
                if (parentList != null) {
                    String parentText = parentList.getText();
                    if (parentText != null && parentText.length() <= maxLength) {
                        context.append(parentText);
                    } else if (parentText != null) {
                        // If parent is too long, get a reasonable portion
                        int cursorOffset = element.getTextOffset() - parentList.getTextOffset();
                        int startOffset = Math.max(0, cursorOffset - maxLength / 2);
                        int endOffset = Math.min(parentText.length(), cursorOffset + maxLength / 2);
                        context.append(parentText.substring(startOffset, endOffset));
                    }
                }
                
                return context.toString();
                
            } catch (Exception e) {
                return "";
            }
        }
        
        /**
         * Scan project .phel files and extract namespace declarations for completion
         */
        private void addProjectPhelNamespaces(CompletionResultSet result, PsiElement element) {
            try {
                
                // Get the project from the PSI element
                Project project = element.getProject();
                
                // Use GlobalSearchScope to find all .phel files in the project
                GlobalSearchScope scope = GlobalSearchScope.allScope(project);
                
                // Find all .phel files using FilenameIndex
                Collection<VirtualFile> phelFiles = FilenameIndex.getAllFilesByExt(project, "phel", scope);
                
                
                Set<String> foundNamespaces = new HashSet<>();
                
                for (VirtualFile virtualFile : phelFiles) {
                    try {
                        // Get PSI file from virtual file
                        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                        
                        if (psiFile != null) {
                            String namespace = extractNamespaceFromPhelFile(psiFile);
                            if (namespace != null && !namespace.isEmpty()) {
                                foundNamespaces.add(namespace);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                
                // Add found namespaces to completion results
                for (String namespace : foundNamespaces) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        namespace,
                        PhelCompletionRanking.Priority.PROJECT_SYMBOLS, // Higher priority than common ones
                        null,
                        "Project namespace from " + namespace.replace("\\", "/") + ".phel",
                        PhelIconProvider.NAMESPACE
                    );
                }
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Extract namespace declaration from a .phel file
         */
        private String extractNamespaceFromPhelFile(PsiFile psiFile) {
            try {
                // Look for (ns namespace-name) declarations
                PsiElement[] children = psiFile.getChildren();
                
                for (PsiElement child : children) {
                    if (child instanceof PhelList) {
                        PhelList list = (PhelList) child;
                        PsiElement[] listChildren = list.getChildren();
                        
                        // Check if first element is 'ns'
                        if (listChildren.length >= 2) {
                            PsiElement firstElement = listChildren[0];
                            if (firstElement instanceof PhelAccess) {
                                String firstText = firstElement.getText().trim();
                                if ("ns".equals(firstText)) {
                                    // Second element should be the namespace name
                                    PsiElement secondElement = listChildren[1];
                                    if (secondElement instanceof PhelAccess) {
                                        String namespace = secondElement.getText().trim();
                                        return namespace;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
            
            return null;
        }
        
        /**
         * Extract namespace prefix from the current context (e.g., "phel\" from "(:require phel\IntellijIdeaRulezzz")
         */
        private String extractNamespacePrefix(String context) {
            try {
                // Look for pattern: (:require PREFIX_HERE IntellijIdeaRulezzz
                int requirePos = context.lastIndexOf(":require");
                if (requirePos == -1) return "";
                
                String afterRequire = context.substring(requirePos + 8); // Skip ":require"
                
                // Find the namespace part before IntellijIdeaRulezzz
                int ideaPos = afterRequire.indexOf("IntellijIdeaRulezzz");
                if (ideaPos == -1) return "";
                
                String beforeIdea = afterRequire.substring(0, ideaPos).trim();
                
                // Remove any leading whitespace and extract the last word (partial namespace)
                String[] parts = beforeIdea.split("\\s+");
                if (parts.length > 0) {
                    String lastPart = parts[parts.length - 1];
                    // Only return if it looks like a namespace prefix (contains letters/backslash)
                    if (lastPart.matches("[a-zA-Z][a-zA-Z0-9\\\\.-]*")) {
                        return lastPart;
                    }
                }
                
                return "";
            } catch (Exception e) {
                return "";
            }
        }
        
        /**
         * Add common Phel namespaces with prefix filtering
         */
        private void addCommonPhelNamespacesWithPrefix(CompletionResultSet result, String prefix) {
            String[] commonNamespaces = {
                "phel\\test", "phel\\json", "phel\\http", "phel\\str", "phel\\html", 
                "phel\\core", "phel\\base64", "phel\\local", "phel\\repl", "phel\\trace"
            };
            
            for (String namespace : commonNamespaces) {
                // Filter by prefix (case insensitive)
                if (prefix.isEmpty() || namespace.toLowerCase().startsWith(prefix.toLowerCase())) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        namespace,
                        PhelCompletionRanking.Priority.API_FUNCTIONS,
                        null,
                        "Common Phel namespace",
                        PhelIconProvider.NAMESPACE
                    );
                }
            }
        }
        
        /**
         * Add project Phel namespaces with prefix filtering
         */
        private void addProjectPhelNamespacesWithPrefix(CompletionResultSet result, PsiElement element, String prefix) {
            try {
                
                // Get the project from the PSI element
                Project project = element.getProject();
                
                // Use GlobalSearchScope to find all .phel files in the project
                GlobalSearchScope scope = GlobalSearchScope.allScope(project);
                
                // Find all .phel files using FilenameIndex
                Collection<VirtualFile> phelFiles = FilenameIndex.getAllFilesByExt(project, "phel", scope);
                
                Set<String> foundNamespaces = new HashSet<>();
                
                for (VirtualFile virtualFile : phelFiles) {
                    try {
                        // Get PSI file from virtual file
                        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
                        
                        if (psiFile != null) {
                            String namespace = extractNamespaceFromPhelFile(psiFile);
                            if (namespace != null && !namespace.isEmpty()) {
                                // Filter by prefix (case insensitive)
                                if (prefix.isEmpty() || namespace.toLowerCase().startsWith(prefix.toLowerCase())) {
                                    foundNamespaces.add(namespace);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                
                // Add found namespaces to completion results
                for (String namespace : foundNamespaces) {
                    PhelCompletionRanking.addRankedCompletion(
                        result,
                        namespace,
                        PhelCompletionRanking.Priority.PROJECT_SYMBOLS, // Higher priority than common ones
                        null,
                        "Project namespace from " + namespace.replace("\\", "/") + ".phel",
                        PhelIconProvider.NAMESPACE
                    );
                }
                
                
            } catch (Exception e) {
            }
        }
        
        /**
         * Check if the given text looks like a complete namespace (not partial)
         * Simple heuristic: if it doesn't end with backslash and has proper namespace format
         */
        private boolean isKnownCompleteNamespace(String namespace) {
            if (namespace == null || namespace.isEmpty()) {
                return false;
            }
            
            // If it ends with backslash, it's still partial (e.g., "phel\")
            if (namespace.endsWith("\\")) {
                return false;
            }
            
            // Check if it looks like a complete namespace format: word\word (at least one backslash)
            if (namespace.matches("[a-zA-Z][a-zA-Z0-9]*\\\\[a-zA-Z][a-zA-Z0-9.-]*")) {
                return true;
            }
            
            return false;
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
