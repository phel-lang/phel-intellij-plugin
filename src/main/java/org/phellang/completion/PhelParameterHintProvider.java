package org.phellang.completion;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.phellang.language.psi.PhelList;
import org.phellang.language.psi.PhelSymbol;

import javax.swing.*;

/**
 * Provides intelligent parameter hints and function signature completions
 */
public class PhelParameterHintProvider {

    private static final Icon HINT_ICON = AllIcons.Actions.IntentionBulb;

    /**
     * Add parameter hint completions based on current context
     */
    public static void addParameterHints(@NotNull CompletionResultSet result, @NotNull PsiElement position) {
        // Check if we're inside a function call
        PhelList containingList = PsiTreeUtil.getParentOfType(position, PhelList.class);
        if (containingList != null) {
            PsiElement firstElement = containingList.getFirstChild();
            if (firstElement instanceof PhelSymbol) {
                String functionName = firstElement.getText();
                addFunctionSignatureHint(result, functionName);
            }
        }
    }

    /**
     * Add function signature hint for known functions
     */
    private static void addFunctionSignatureHint(@NotNull CompletionResultSet result, String functionName) {
        String signature = getFunctionSignature(functionName);
        if (signature != null) {
            result.addElement(
                LookupElementBuilder.create("(" + functionName + " )")
                    .withIcon(HINT_ICON)
                    .withTypeText(signature)
                    .withTailText(" - function signature", true)
                    .withBoldness(true)
                    .withPresentableText(functionName + " " + signature)
            );
        }
    }

    /**
     * Get function signature for known core functions (static access for external use)
     */
    public static String getFunctionSignatureStatic(String functionName) {
        return getFunctionSignature(functionName);
    }

    /**
     * Get function signature for known core functions
     */
    private static String getFunctionSignature(String functionName) {
        switch (functionName) {
            // Control flow
            case "if": return "test then else?";
            case "when": return "test & body";
            case "let": return "[bindings*] expr*";
            case "loop": return "[bindings*] expr*";
            case "for": return "seq-exprs body";
            case "defn": return "name [params*] body*";
            case "fn": return "[params*] body*";

            // Collection functions
            case "map": return "f coll";
            case "filter": return "pred coll";
            case "reduce": return "f coll";
            case "get": return "coll key default?";
            case "put": return "coll key val";
            case "count": return "coll";
            case "first": return "coll";
            case "rest": return "coll";
            case "cons": return "item coll";
            case "conj": return "coll & items";

            // String functions  
            case "str": return "& args";
            case "print": return "& xs";
            case "println": return "& xs";
            case "format": return "fmt & args";

            // Arithmetic
            case "+": return "& args";
            case "-": return "& args";
            case "*": return "& args";
            case "/": return "& args";
            case "=": return "& args";
            case "<": return "& args";
            case ">": return "& args";
            case "<=": return "& args";
            case ">=": return "& args";

            // Predicates
            case "nil?": return "x";
            case "empty?": return "coll";
            case "even?": return "n";
            case "odd?": return "n";
            case "pos?": return "n";
            case "neg?": return "n";
            case "zero?": return "n";

            // Higher-order functions
            case "apply": return "f args";
            case "comp": return "& fns";
            case "partial": return "f & args";
            case "constantly": return "x";
            case "identity": return "x";

            // Threading macros
            case "->": return "x & forms";
            case "->>": return "x & forms";
            case "as->": return "expr name & forms";

            // Logical
            case "and": return "& forms";
            case "or": return "& forms";
            case "not": return "x";

            default: return null;
        }
    }

    /**
     * Add parameter position hints when inside a function call
     */
    public static void addPositionHints(@NotNull CompletionResultSet result, @NotNull PsiElement position) {
        PhelList containingList = PsiTreeUtil.getParentOfType(position, PhelList.class);
        if (containingList == null) return;

        PsiElement firstElement = containingList.getFirstChild();
        if (!(firstElement instanceof PhelSymbol)) return;

        String functionName = firstElement.getText();
        
        // Calculate which parameter position we're at
        int parameterIndex = getParameterIndex(containingList, position);
        if (parameterIndex >= 0) {
            String positionHint = getParameterPositionHint(functionName, parameterIndex);
            if (positionHint != null) {
                result.addElement(
                    LookupElementBuilder.create("")
                        .withIcon(HINT_ICON)
                        .withTypeText("Parameter " + (parameterIndex + 1) + ": " + positionHint)
                        .withTailText(" - current parameter", true)
                        .withPresentableText("→ " + positionHint)
                );
            }
        }
    }

    /**
     * Calculate the parameter index within a function call
     */
    private static int getParameterIndex(PhelList functionCall, PsiElement position) {
        PsiElement[] children = functionCall.getChildren();
        int index = -1;
        
        for (int i = 0; i < children.length; i++) {
            if (PsiTreeUtil.isAncestor(children[i], position, false)) {
                return i - 1; // Subtract 1 because first child is function name
            }
        }
        
        return index;
    }

    /**
     * Get parameter hint for specific position in known functions
     */
    private static String getParameterPositionHint(String functionName, int parameterIndex) {
        switch (functionName) {
            case "get":
                switch (parameterIndex) {
                    case 0: return "collection";
                    case 1: return "key";
                    case 2: return "default-value";
                    default: return null;
                }
            case "put":
                switch (parameterIndex) {
                    case 0: return "collection";
                    case 1: return "key";
                    case 2: return "value";
                    default: return null;
                }
            case "map":
                switch (parameterIndex) {
                    case 0: return "function";
                    case 1: return "collection";
                    default: return null;
                }
            case "filter":
                switch (parameterIndex) {
                    case 0: return "predicate";
                    case 1: return "collection";
                    default: return null;
                }
            case "reduce":
                switch (parameterIndex) {
                    case 0: return "function";
                    case 1: return "collection";
                    case 2: return "initial-value";
                    default: return null;
                }
            case "let":
                switch (parameterIndex) {
                    case 0: return "bindings-vector";
                    default: return "body-expression-" + parameterIndex;
                }
            case "if":
                switch (parameterIndex) {
                    case 0: return "test-condition";
                    case 1: return "then-expression";
                    case 2: return "else-expression";
                    default: return null;
                }
            case "defn":
                switch (parameterIndex) {
                    case 0: return "function-name";
                    case 1: return "doc-string or parameters";
                    case 2: return "parameters or body";
                    default: return "body-expression-" + (parameterIndex - 2);
                }
            default: return null;
        }
    }
}
