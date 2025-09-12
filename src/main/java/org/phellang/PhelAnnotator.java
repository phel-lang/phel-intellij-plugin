package org.phellang;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.phellang.language.psi.PhelSymbol;
import org.phellang.language.psi.PhelKeyword;
import org.phellang.language.psi.PhelPsiUtil;
import org.phellang.language.psi.PhelTypes;
import org.phellang.language.psi.PhelForm;
import org.phellang.language.psi.impl.PhelVecImpl;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;

import java.util.Set;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Advanced annotator for context-sensitive Phel syntax highlighting
 * Handles PHP interop, built-in functions, and namespace-aware highlighting
 */
public class PhelAnnotator implements Annotator {

    // PHP interop highlighting
    public static final TextAttributesKey PHP_INTEROP =
            createTextAttributesKey("PHEL_PHP_INTEROP", DefaultLanguageHighlighterColors.STATIC_METHOD);
    
    public static final TextAttributesKey PHP_VARIABLE =
            createTextAttributesKey("PHEL_PHP_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE);

    // Built-in function categories
    public static final TextAttributesKey CORE_FUNCTION =
            createTextAttributesKey("PHEL_CORE_FUNCTION", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
    
    public static final TextAttributesKey MACRO =
            createTextAttributesKey("PHEL_MACRO", DefaultLanguageHighlighterColors.KEYWORD);
    
    public static final TextAttributesKey SPECIAL_FORM =
            createTextAttributesKey("PHEL_SPECIAL_FORM", DefaultLanguageHighlighterColors.KEYWORD);

    // Namespace highlighting  
    public static final TextAttributesKey NAMESPACE_PREFIX =
            createTextAttributesKey("PHEL_NAMESPACE", DefaultLanguageHighlighterColors.CLASS_NAME);
        
    // Form comment highlighting - for forms commented out by #_
    public static final TextAttributesKey COMMENTED_OUT_FORM =
            createTextAttributesKey("PHEL_COMMENTED_OUT_FORM", DefaultLanguageHighlighterColors.LINE_COMMENT);
        
    // Function parameter highlighting (more distinct color)
    public static final TextAttributesKey FUNCTION_PARAMETER =
            createTextAttributesKey("PHEL_FUNCTION_PARAMETER", DefaultLanguageHighlighterColors.INSTANCE_FIELD);

    // Core Phel special forms
    private static final Set<String> SPECIAL_FORMS = Set.of(
            "def", "fn", "if", "let", "do", "quote", "var", "recur", "throw", "try", "catch", "finally",
            "loop", "case", "cond", "when", "when-not", "when-let", "if-let", "if-not", "ns",
            "defn", "defn-", "defmacro", "defmacro-", "defstruct", "definterface"
    );

    // Core Phel macros
    private static final Set<String> MACROS = Set.of(
            "and", "or", "->", "->>", "as->", "cond->", "cond->>", "some->", "some->>",
            "doto", "for", "dofor", "foreach", "comment", "declare"
    );

    // Common core functions (subset for performance)
    private static final Set<String> CORE_FUNCTIONS = Set.of(
            // Collection operations
            "map", "filter", "reduce", "apply", "count", "first", "rest", "cons", "conj",
            "get", "get-in", "assoc", "dissoc", "update", "merge", "keys", "values",
            "peek", "pop", "push", "remove", "second", "ffirst", "next", "nnext", "nfirst",
            
            // Sequence operations
            "take", "drop", "take-while", "drop-while", "take-nth", "take-last",
            "distinct", "reverse", "sort", "sort-by", "shuffle", "flatten",
            "range", "repeat", "interleave", "interpose", "partition", "split-at", "split-with",
            
            // String operations
            "str", "print", "println", "print-str", "format", "printf",
            "str-contains?", "slurp", "spit",
            
            // Function operations
            "identity", "comp", "partial", "constantly", "complement", "juxt", "memoize",
            
            // Math operations
            "inc", "dec", "sum", "mean", "extreme",
            "+", "-", "*", "/", "mod", "rem", "quot",
            "=", "not=", "<", "<=", ">", ">=", "min", "max", "abs",
            
            // Predicates
            "pos?", "neg?", "zero?", "one?", "even?", "odd?", "nil?", "some?", "all?",
            "true?", "false?", "truthy?", "boolean?", "number?", "string?", "keyword?", "symbol?",
            "list?", "vector?", "map?", "set?", "seq?", "coll?", "empty?", "not-empty",
            "float?", "int?", "function?", "struct?", "hash-map?", "php-array?",
            "php-resource?", "php-object?", "indexed?", "associative?", "var?",
            
            // Random functions
            "rand", "rand-int", "rand-nth",
            
            // Meta and symbol operations
            "meta", "name", "namespace", "full-name", "symbol", "keyword", "gensym",
            
            // Evaluation and compilation
            "eval", "compile", "macroexpand", "macroexpand-1", "read-string",
            
            // Bit operations
            "bit-and", "bit-or", "bit-xor", "bit-not", "bit-shift-left", "bit-shift-right",
            "bit-set", "bit-clear", "bit-flip", "bit-test",
            
            // Other core functions
            "not", "compare", "type", "contains?", "find", "find-index",
            "frequencies", "group-by", "union", "intersection", "difference",
            "symmetric-difference", "select-keys", "invert", "merge-with", "deep-merge",
            
            // String module functions (phel\str)
            "split", "join", "replace", "replace-first", "trim", "triml", "trimr",
            "capitalize", "lower-case", "upper-case", "starts-with?", "ends-with?",
            "blank?", "includes?", "index-of", "last-index-of", "split-lines",
            "pad-left", "pad-right", "trim-newline", "escape", "re-quote-replacement",
            
            // JSON module functions (phel\json)
            "encode", "decode",
            
            // Base64 module functions (phel\base64)
            "encode-url", "decode-url",
            
            // HTML module functions (phel\html)
            "escape-html", "doctype",
            
            // Test module functions (phel\test)
            "report", "print-summary", "run-tests", "successful?",
            
            // REPL module functions (phel\repl)
            "loaded-namespaces", "resolve", "print-colorful", "println-colorful", "compile-str",
            
            // Trace module functions (phel\trace)
            "dotrace", "reset-trace-state!", "set-trace-id-padding!",
            
            // HTTP module functions (phel\http) - most commonly used
            "request-from-globals", "response-from-map", "response-from-string", "emit-response"
    );

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Check if this element is commented out by #_ form comment
        if (isCommentedOutByFormComment(element)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element.getTextRange())
                    .textAttributes(COMMENTED_OUT_FORM)
                    .create();
            return; // Don't apply other highlighting to commented-out forms
        }
        
        if (element instanceof PhelKeyword) {
            annotatePhelKeyword((PhelKeyword) element, holder);
        } else if (element instanceof PhelSymbol) {
            PhelSymbol symbol = (PhelSymbol) element;
            String text = symbol.getText();
            
            if (text != null) {
                annotateSymbol(symbol, text, holder);
            }
        }
    }

    private void annotateSymbol(@NotNull PhelSymbol symbol, @NotNull String text, @NotNull AnnotationHolder holder) {
        // Function parameters (check first, before other classifications)
        if (PhelPsiUtil.isDefinition(symbol)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(symbol.getTextRange())
                    .textAttributes(FUNCTION_PARAMETER)
                    .create();
            return;
        }
        
        // PHP interop patterns
        if (isPhpInterop(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(symbol.getTextRange())
                    .textAttributes(PHP_INTEROP)
                    .create();
            return;
        }
        
        // PHP variables (superglobals)
        if (isPhpVariable(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(symbol.getTextRange())
                    .textAttributes(PHP_VARIABLE)
                    .create();
            return;
        }
        
        // Namespace prefix highlighting
        if (hasNamespacePrefix(text)) {
            highlightNamespacePrefix(symbol, text, holder);
            return;
        }
        
        // Special forms (highest priority)
        if (SPECIAL_FORMS.contains(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(symbol.getTextRange())
                    .textAttributes(SPECIAL_FORM)
                    .create();
            return;
        }
        
        // Macros
        if (MACROS.contains(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(symbol.getTextRange())
                    .textAttributes(MACRO)
                    .create();
            return;
        }
        
        // Core functions
        if (CORE_FUNCTIONS.contains(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(symbol.getTextRange())
                    .textAttributes(CORE_FUNCTION)
                    .create();
            return;
        }
    }

    private boolean isPhpInterop(@NotNull String text) {
        return text.startsWith("php/") || 
               text.equals("php/->") || 
               text.equals("php/::") ||
               text.equals("php/new") ||
               text.startsWith("php/a") ||  // php/aget, php/aset, php/apush, php/aunset
               text.startsWith("php/o");    // php/oset
    }

    private boolean isPhpVariable(@NotNull String text) {
        return text.startsWith("php/$") ||
               text.equals("php/$_SERVER") ||
               text.equals("php/$_GET") ||
               text.equals("php/$_POST") ||
               text.equals("php/$_COOKIE") ||
               text.equals("php/$_FILES") ||
               text.equals("php/$_SESSION") ||
               text.equals("php/$GLOBALS");
    }

    private boolean hasNamespacePrefix(@NotNull String text) {
        // Check for namespace separators: / or \
        return (text.contains("/") && !text.startsWith("php/")) || 
               text.contains("\\");
    }

    private void highlightNamespacePrefix(@NotNull PhelSymbol symbol, @NotNull String text, @NotNull AnnotationHolder holder) {
        int separatorIndex = -1;
        
        // Find the last separator (/ or \)
        int slashIndex = text.lastIndexOf('/');
        int backslashIndex = text.lastIndexOf('\\');
        separatorIndex = Math.max(slashIndex, backslashIndex);
        
        if (separatorIndex > 0) {
            // Highlight only the namespace part (before the separator)
            int startOffset = symbol.getTextRange().getStartOffset();
            int endOffset = startOffset + separatorIndex;
            
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(TextRange.create(startOffset, endOffset))
                    .textAttributes(NAMESPACE_PREFIX)
                    .create();
        }
    }
    
    /**
     * Annotate keyword elements to ensure proper highlighting
     */
    private void annotatePhelKeyword(@NotNull PhelKeyword keyword, @NotNull AnnotationHolder holder) {
        // Highlight the entire keyword element
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(keyword.getTextRange())
                .textAttributes(PhelSyntaxHighlighter.KEYWORD)
                .create();
    }
    
    /**
     * Check if an element is commented out by #_ form comment(s).
     * Handles stacking of multiple #_ tokens (e.g., #_#_ comments out two forms).
     */
    private boolean isCommentedOutByFormComment(@NotNull PsiElement element) {
        // First try text-based detection for vectors (workaround for grammar bug)
        if (isInCommentedVectorByText(element)) {
            return true;
        }
        
        // Find the form that contains this element
        PhelForm containingForm = findContainingForm(element);
        if (containingForm == null) {
            return false;
        }
        
        // Check if this form is commented out by counting preceding #_ tokens
        // First check the containing form itself, then check parent forms
        return isFormCommentedOut(containingForm) || isParentFormCommentedOut(containingForm);
    }
    
    /**
     * Find the immediate form that contains the given element.
     */
    private PhelForm findContainingForm(@NotNull PsiElement element) {
        PsiElement current = element;
        while (current != null) {
            if (current instanceof PhelForm) {
                return (PhelForm) current;
            }
            current = current.getParent();
        }
        return null;
    }
    
    /**
     * Check if a form is commented out by counting preceding #_ tokens.
     * Handles stacking: #_ comments out 1 form, #_#_ comments out 2 forms, etc.
     */
    private boolean isFormCommentedOut(@NotNull PhelForm form) {
        PsiElement parent = form.getParent();
        if (parent == null) {
            return false;
        }
        
        // Get all children of the parent to analyze the sequence
        PsiElement[] children = parent.getChildren();
        int formIndex = -1;
        
        // Find the index of our form
        for (int i = 0; i < children.length; i++) {
            if (children[i] == form) {
                formIndex = i;
                break;
            }
        }
        
        if (formIndex == -1) {
            return false;
        }
        
        // Find the sequence of forms that this form belongs to
        // and check if #_ tokens precede that sequence
        
        // Step 1: Find the start of the form sequence that contains our form
        int sequenceStart = formIndex;
        for (int i = formIndex - 1; i >= 0; i--) {
            PsiElement child = children[i];
            if (child instanceof PsiWhiteSpace) {
                continue; // Skip whitespace
            } else if (child instanceof PhelForm) {
                sequenceStart = i; // Found another form, extend sequence backwards
            } else {
                break; // Hit non-form, non-whitespace - end of sequence
            }
        }
        
        // Step 2: Count consecutive #_ tokens immediately before the sequence
        int consecutiveFormComments = 0;
        for (int i = sequenceStart - 1; i >= 0; i--) {
            PsiElement child = children[i];
            
            if (child instanceof PsiWhiteSpace) {
                continue; // Skip whitespace
            } else if (child.getNode() != null && child.getNode().getElementType() == PhelTypes.FORM_COMMENT) {
                consecutiveFormComments++;
            } else if (isFormCommentMacro(child)) {
                consecutiveFormComments++;
            } else {
                break;
            }
        }
        
        if (consecutiveFormComments == 0) {
            return false;
        }
        
        // Step 3: Determine position of our form within the sequence
        int formPosition = 0;
        for (int i = sequenceStart; i <= formIndex; i++) {
            PsiElement child = children[i];
            if (child instanceof PhelForm) {
                formPosition++;
                if (child == form) {
                    break;
                }
            }
        }
        
        return formPosition <= consecutiveFormComments;
    }
    
    /**
     * Check if any parent form is commented out by #_. This handles cases like [#_:one :two]
     * where :one and :two are inside a vector that is commented out.
     */
    private boolean isParentFormCommentedOut(@NotNull PhelForm form) {
        PsiElement parent = form.getParent();
        while (parent != null) {
            if (parent instanceof PhelForm) {
                PhelForm parentForm = (PhelForm) parent;
                if (isFormCommentedOut(parentForm)) {
                    return true;
                }
                parent = parent.getParent();
            } else {
                break;
            }
        }
        return false;
    }
    
    /**
     * Check if a PSI element is a form_comment_macro (i.e., #_ form).
     */
    private boolean isFormCommentMacro(@NotNull PsiElement element) {
        // Check if this is a form_comment_macro by looking at its structure
        if (element.getNode() == null) {
            return false;
        }
        
        // Look for elements that start with #_ token
        PsiElement[] children = element.getChildren();
        if (children.length > 0) {
            PsiElement firstChild = children[0];
            if (firstChild.getNode() != null && firstChild.getNode().getElementType() == PhelTypes.FORM_COMMENT) {
                return true;
            }
        }
        
        // Also check if the element itself is the form_comment token
        return element.getNode().getElementType() == PhelTypes.FORM_COMMENT;
    }
    
    /**
     * Workaround for grammar bug: detect form comments by analyzing text content.
     * This handles cases like [#_:one :two] where the PSI tree is missing #_ tokens.
     */
    private boolean isInCommentedVectorByText(@NotNull PsiElement element) {
        // Check if we're inside a vector
        PsiElement parent = element.getParent();
        if (!(parent instanceof PhelVecImpl)) {
            return false;
        }
        
        PhelVecImpl vector = (PhelVecImpl) parent;
        String vectorText = vector.getText();
        
        // Find element's position within the vector text
        int elementOffsetInVector = element.getTextOffset() - vector.getTextOffset();
        
        // Look for #_ patterns before this element's position
        String beforeElement = vectorText.substring(0, elementOffsetInVector);
        
        // Simple regex to find #_ patterns
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("#_");
        java.util.regex.Matcher matcher = pattern.matcher(beforeElement);
        
        int formCommentCount = 0;
        while (matcher.find()) {
            formCommentCount++;
        }
        
        if (formCommentCount > 0) {
            // Count how many actual forms (keywords/symbols) appear before this element
            String textBeforeForCounting = beforeElement.replaceAll("#_", "").trim();
            
            // Use regex to find all keywords and symbols in the text
            java.util.regex.Pattern formPattern = java.util.regex.Pattern.compile("(:[\\w-]+|[a-zA-Z][\\w-]*)");
            java.util.regex.Matcher formMatcher = formPattern.matcher(textBeforeForCounting);
            
            int formsBeforeCount = 0;
            while (formMatcher.find()) {
                formsBeforeCount++;
            }
            
            // If this element is at position N (0-based) and there are M #_ tokens,
            // then it's commented out if N < M
            return formsBeforeCount < formCommentCount;
        }
        
        return false;
    }
}
