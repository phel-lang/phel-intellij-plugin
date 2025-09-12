package org.phellang.completion;

import com.intellij.icons.AllIcons;
import com.intellij.ui.LayeredIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Centralized icon provider for Phel completion system
 * Provides visually distinct icons for different symbol types to enhance user experience
 */
public class PhelIconProvider {

    // === SCOPING ICONS (Highest Priority) ===
    
    /**
     * Function parameters - Blue parameter icon with emphasis
     * Used for: (defn func [param] param←)
     */
    public static final Icon PARAMETER = AllIcons.Nodes.Parameter; // Blue parameter icon
    
    /**
     * Local bindings - Green variable icon  
     * Used for: (let [binding val] binding←)
     */
    public static final Icon LOCAL_BINDING = AllIcons.Nodes.Variable; // Green variable icon
    
    /**
     * Current function (for recursive calls) - Method icon with recursion indicator
     * Used for: (defn factorial [n] (if ... (factorial ...)))
     */
    public static final Icon RECURSIVE_FUNCTION = createLayeredIcon(
        AllIcons.Nodes.Method, 
        AllIcons.General.ArrowDown
    );

    // === LANGUAGE CONSTRUCT ICONS ===
    
    /**
     * Special forms - Purple diamond icon for core language constructs
     * Used for: defn, let, if, when, do, loop, etc.
     */
    public static final Icon SPECIAL_FORM = AllIcons.Nodes.Enum; // Purple diamond
    
    /**
     * Control flow - Yellow conditional icon
     * Used for: if, when, cond, case, try, catch
     */
    public static final Icon CONTROL_FLOW = AllIcons.General.Filter; // Yellow filter icon
    
    /**
     * Macros - Pink abstract method icon  
     * Used for: defmacro, threading macros (->, ->>)
     */
    public static final Icon MACRO = AllIcons.Nodes.AbstractMethod; // Pink abstract method

    // === FUNCTION TYPE ICONS ===
    
    /**
     * Predicate functions - Function icon with inspection overlay
     * Used for: nil?, empty?, even?, string?, etc.
     */
    public static final Icon PREDICATE = createLayeredIcon(
        AllIcons.Nodes.Function,
        AllIcons.General.InspectionsEye // Eye icon suggests "checking/testing"
    );
    
    /**
     * Collection functions - Stack icon for data manipulation
     * Used for: map, filter, reduce, conj, assoc, etc.
     */
    public static final Icon COLLECTION_FUNCTION = AllIcons.Nodes.DataTables; // Stack/table icon
    
    /**
     * String functions - Text icon
     * Used for: str, subs, split, join, format, etc.
     */
    public static final Icon STRING_FUNCTION = AllIcons.FileTypes.Text; // Text file icon
    
    /**
     * Math functions - Formula icon
     * Used for: +, -, *, /, mod, inc, dec, abs, etc.
     */
    public static final Icon MATH_FUNCTION = AllIcons.Debugger.EvaluateExpression; // Calculator-like icon
    
    /**
     * I/O functions - Console icon
     * Used for: print, println, read, slurp, spit, etc.
     */
    public static final Icon IO_FUNCTION = AllIcons.Debugger.Console; // Console icon

    // === NAMESPACE & PROJECT ICONS ===
    
    /**
     * Namespaces - Package icon for namespace imports
     * Used for: phel\test, phel\json, etc.
     */
    public static final Icon NAMESPACE = AllIcons.Nodes.Package; // Package icon for namespaces
    
    /**
     * Namespaced functions - Package icon with function overlay
     * Used for: str/split, http/get, json/encode, etc.
     */
    public static final Icon NAMESPACED_FUNCTION = createLayeredIcon(
        AllIcons.Nodes.Package,
        AllIcons.Nodes.Function
    );
    
    /**
     * Local project definitions - File icon with function overlay
     * Used for: functions defined in other files in the same project
     */
    public static final Icon PROJECT_FUNCTION = createLayeredIcon(
        AllIcons.FileTypes.Any_type,
        AllIcons.Nodes.Function
    );

    // === INTEROP ICONS ===
    
    /**
     * PHP interop - Execution icon with web overlay  
     * Used for: php/array, php/isset, php/json_encode, etc.
     */
    public static final Icon PHP_INTEROP = createLayeredIcon(
        AllIcons.Actions.Execute, // Gear icon suggests "external execution"
        AllIcons.General.Web     // Web icon suggests "external system"
    );

    // === VALUE & LITERAL ICONS ===
    
    /**
     * Constants and literals - Constant icon
     * Used for: nil, true, false, numbers, strings
     */
    public static final Icon LITERAL = AllIcons.Nodes.Constant; // Diamond constant icon
    
    /**
     * Keywords - Key icon  
     * Used for: :keyword, ::namespaced-keyword
     */
    public static final Icon KEYWORD = AllIcons.Nodes.PropertyReadWrite; // Key icon

    // === COMPLETION HELPER ICONS ===
    
    /**
     * Structural completions - Building block icon
     * Used for: (, ), [, ], {, }
     */
    public static final Icon STRUCTURAL = AllIcons.General.Add; // Building blocks
    
    /**
     * Parameter hints - Light bulb for suggestions
     * Used for: function signature hints and parameter suggestions
     */
    public static final Icon HINT = AllIcons.Actions.IntentionBulb; // Light bulb
    
    /**
     * Context suggestions - Compass icon for context-aware suggestions
     * Used for: contextually relevant suggestions (predicates in filter, etc.)
     */
    public static final Icon CONTEXT_RELEVANT = AllIcons.Actions.Find; // Magnifying glass

    // === UTILITY METHODS ===
    
    /**
     * Create a layered icon with a small overlay
     */
    private static Icon createLayeredIcon(@NotNull Icon baseIcon, @NotNull Icon overlayIcon) {
        LayeredIcon layered = new LayeredIcon(2);
        
        // Set base icon
        layered.setIcon(baseIcon, 0);
        
        // Add overlay in bottom-right corner (scaled down)
        layered.setIcon(overlayIcon, 1, SwingConstants.SOUTH_EAST);
        
        return layered;
    }
    
    /**
     * Get appropriate icon based on symbol type and characteristics
     */
    @NotNull
    public static Icon getIconForSymbol(@NotNull String symbolName, @NotNull String symbolType, boolean isLocal, boolean isContextRelevant) {
        // Local scope symbols always get highest priority icons
        if (isLocal) {
            if ("Parameter".equals(symbolType) || "Function Parameter".equals(symbolType)) {
                return PARAMETER;
            }
            if ("Let Binding".equals(symbolType) || "Local Variable".equals(symbolType)) {
                return LOCAL_BINDING;
            }
            if ("Function (recursive)".equals(symbolType)) {
                return RECURSIVE_FUNCTION;
            }
        }
        
        // Context-relevant symbols get special highlighting
        if (isContextRelevant) {
            return CONTEXT_RELEVANT;
        }
        
        // Function type-based icons
        if (symbolName.endsWith("?")) {
            return PREDICATE;
        }
        if (symbolName.startsWith("php/")) {
            return PHP_INTEROP;
        }
        if (symbolName.contains("/")) {
            return NAMESPACED_FUNCTION;
        }
        if (symbolName.startsWith(":")) {
            return KEYWORD;
        }
        
        // Language construct icons
        if (isSpecialForm(symbolName)) {
            return SPECIAL_FORM;
        }
        if (isControlFlow(symbolName)) {
            return CONTROL_FLOW;
        }
        if (isMacro(symbolName)) {
            return MACRO;
        }
        
        // Function category icons
        if (isCollectionFunction(symbolName)) {
            return COLLECTION_FUNCTION;
        }
        if (isStringFunction(symbolName)) {
            return STRING_FUNCTION;
        }
        if (isMathFunction(symbolName)) {
            return MATH_FUNCTION;
        }
        if (isIoFunction(symbolName)) {
            return IO_FUNCTION;
        }
        
        // Default to generic function icon
        return AllIcons.Nodes.Function;
    }
    
    // === CLASSIFICATION HELPER METHODS ===
    
    private static boolean isSpecialForm(@NotNull String name) {
        return java.util.Arrays.asList(
            "def", "defn", "defn-", "defmacro", "defmacro-", "defstruct", 
            "let", "fn", "quote", "var", "ns"
        ).contains(name);
    }
    
    private static boolean isControlFlow(@NotNull String name) {
        return java.util.Arrays.asList(
            "if", "when", "when-not", "cond", "case", "try", "catch", "finally"
        ).contains(name);
    }
    
    private static boolean isMacro(@NotNull String name) {
        return java.util.Arrays.asList(
            "->", "->>", "as->", "doto", "when-let", "if-let"
        ).contains(name);
    }
    
    private static boolean isCollectionFunction(@NotNull String name) {
        return java.util.Arrays.asList(
            "map", "filter", "reduce", "conj", "cons", "assoc", "dissoc", 
            "get", "put", "count", "first", "rest", "take", "drop"
        ).contains(name);
    }
    
    private static boolean isStringFunction(@NotNull String name) {
        return name.startsWith("str/") || java.util.Arrays.asList(
            "str", "subs", "format", "split", "join", "trim"
        ).contains(name);
    }
    
    private static boolean isMathFunction(@NotNull String name) {
        return java.util.Arrays.asList(
            "+", "-", "*", "/", "mod", "inc", "dec", "abs", "max", "min"
        ).contains(name);
    }
    
    private static boolean isIoFunction(@NotNull String name) {
        return java.util.Arrays.asList(
            "print", "println", "read", "slurp", "spit"
        ).contains(name);
    }
}
