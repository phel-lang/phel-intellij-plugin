package org.phellang.completion

import com.intellij.icons.AllIcons
import com.intellij.ui.LayeredIcon
import javax.swing.Icon
import javax.swing.SwingConstants

/**
 * Centralized icon provider for Phel completion system
 * Provides visually distinct icons for different symbol types to enhance user experience
 */
object PhelIconProvider {
    // === SCOPING ICONS (Highest Priority) ===
    /**
     * Function parameters - Blue parameter icon with emphasis
     * Used for: (defn func [param] param←)
     */
    @JvmField
    val PARAMETER: Icon = AllIcons.Nodes.Parameter // Blue parameter icon

    /**
     * Local bindings - Green variable icon
     * Used for: (let [binding val] binding←)
     */
    @JvmField
    val LOCAL_BINDING: Icon = AllIcons.Nodes.Variable // Green variable icon

    /**
     * Current function (for recursive calls) - Method icon with recursion indicator
     * Used for: (defn factorial [n] (if ... (factorial ...)))
     */
    @JvmField
    val RECURSIVE_FUNCTION: Icon = createLayeredIcon(
        AllIcons.Nodes.Method, AllIcons.General.ArrowDown
    )

    // === LANGUAGE CONSTRUCT ICONS ===
    /**
     * Special forms - Purple diamond icon for core language constructs
     * Used for: defn, let, if, when, do, loop, etc.
     */
    @JvmField
    val SPECIAL_FORM: Icon = AllIcons.Nodes.Enum // Purple diamond

    /**
     * Control flow - Yellow conditional icon
     * Used for: if, when, cond, case, try, catch
     */
    @JvmField
    val CONTROL_FLOW: Icon = AllIcons.General.Filter // Yellow filter icon

    /**
     * Macros - Pink abstract method icon
     * Used for: defmacro, threading macros (->, ->>)
     */
    @JvmField
    val MACRO: Icon = AllIcons.Nodes.AbstractMethod // Pink abstract method

    // === FUNCTION TYPE ICONS ===
    /**
     * Predicate functions - Function icon with inspection overlay
     * Used for: nil?, empty?, even?, string?, etc.
     */
    @JvmField
    val PREDICATE: Icon = createLayeredIcon(
        AllIcons.Nodes.Function, AllIcons.General.InspectionsEye // Eye icon suggests "checking/testing"
    )

    /**
     * Collection functions - Stack icon for data manipulation
     * Used for: map, filter, reduce, conj, assoc, etc.
     */
    @JvmField
    val COLLECTION_FUNCTION: Icon = AllIcons.Nodes.DataTables // Stack/table icon

    /**
     * String functions - Text icon
     * Used for: str, subs, split, join, format, etc.
     */
    @JvmField
    val STRING_FUNCTION: Icon = AllIcons.FileTypes.Text // Text file icon

    /**
     * Math functions - Formula icon
     * Used for: +, -, *, /, mod, inc, dec, abs, etc.
     */
    @JvmField
    val MATH_FUNCTION: Icon = AllIcons.Debugger.EvaluateExpression // Calculator-like icon

    /**
     * I/O functions - Console icon
     * Used for: print, println, read, slurp, spit, etc.
     */
    @JvmField
    val IO_FUNCTION: Icon = AllIcons.Debugger.Console // Console icon

    // === NAMESPACE & PROJECT ICONS ===
    /**
     * Namespaces - Package icon for namespace imports
     * Used for: phel\test, phel\json, etc.
     */
    @JvmField
    val NAMESPACE: Icon = AllIcons.Nodes.Package // Package icon for namespaces

    /**
     * Namespaced functions - Package icon with function overlay
     * Used for: str/split, http/get, json/encode, etc.
     */
    @JvmField
    val NAMESPACED_FUNCTION: Icon = createLayeredIcon(
        AllIcons.Nodes.Package, AllIcons.Nodes.Function
    )

    /**
     * Local project definitions - File icon with function overlay
     * Used for: functions defined in other files in the same project
     */
    val PROJECT_FUNCTION: Icon = createLayeredIcon(
        AllIcons.FileTypes.Any_type, AllIcons.Nodes.Function
    )

    // === INTEROP ICONS ===
    /**
     * PHP interop - Execution icon with web overlay
     * Used for: php/array, php/isset, php/json_encode, etc.
     */
    @JvmField
    val PHP_INTEROP: Icon = createLayeredIcon(
        AllIcons.Actions.Execute,  // Gear icon suggests "external execution"
        AllIcons.General.Web // Web icon suggests "external system"
    )

    // === VALUE & LITERAL ICONS ===
    /**
     * Constants and literals - Constant icon
     * Used for: nil, true, false, numbers, strings
     */
    val LITERAL: Icon = AllIcons.Nodes.Constant // Diamond constant icon

    /**
     * Keywords - Key icon
     * Used for: :keyword, ::namespaced-keyword
     */
    @JvmField
    val KEYWORD: Icon = AllIcons.Nodes.PropertyReadWrite // Key icon

    // === COMPLETION HELPER ICONS ===
    /**
     * Structural completions - Building block icon
     * Used for: (, ), [, ], {, }
     */
    @JvmField
    val STRUCTURAL: Icon = AllIcons.General.Add // Building blocks

    /**
     * Parameter hints - Light bulb for suggestions
     * Used for: function signature hints and parameter suggestions
     */
    val HINT: Icon = AllIcons.Actions.IntentionBulb // Light bulb

    /**
     * Context suggestions - Compass icon for context-aware suggestions
     * Used for: contextually relevant suggestions (predicates in filter, etc.)
     */
    @JvmField
    val CONTEXT_RELEVANT: Icon = AllIcons.Actions.Find // Magnifying glass

    // === UTILITY METHODS ===
    /**
     * Create a layered icon with a small overlay
     */
    private fun createLayeredIcon(baseIcon: Icon, overlayIcon: Icon): Icon {
        val layered = LayeredIcon(2)


        // Set base icon
        layered.setIcon(baseIcon, 0)


        // Add overlay in bottom-right corner (scaled down)
        layered.setIcon(overlayIcon, 1, SwingConstants.SOUTH_EAST)

        return layered
    }

    /**
     * Get appropriate icon based on symbol type and characteristics
     */
    fun getIconForSymbol(symbolName: String, symbolType: String, isLocal: Boolean, isContextRelevant: Boolean): Icon {
        // Local scope symbols always get highest priority icons
        if (isLocal) {
            if ("Parameter" == symbolType || "Function Parameter" == symbolType) {
                return PARAMETER
            }
            if ("Let Binding" == symbolType || "Local Variable" == symbolType) {
                return LOCAL_BINDING
            }
            if ("Function (recursive)" == symbolType) {
                return RECURSIVE_FUNCTION
            }
        }

        // Context-relevant symbols get special highlighting
        if (isContextRelevant) {
            return CONTEXT_RELEVANT
        }

        // Function type-based icons
        if (symbolName.endsWith("?")) {
            return PREDICATE
        }
        if (symbolName.startsWith("php/")) {
            return PHP_INTEROP
        }
        if (symbolName.contains("/")) {
            return NAMESPACED_FUNCTION
        }
        if (symbolName.startsWith(":")) {
            return KEYWORD
        }

        // Language construct icons
        if (isSpecialForm(symbolName)) {
            return SPECIAL_FORM
        }
        if (isControlFlow(symbolName)) {
            return CONTROL_FLOW
        }
        if (isMacro(symbolName)) {
            return MACRO
        }

        // Function category icons
        if (isCollectionFunction(symbolName)) {
            return COLLECTION_FUNCTION
        }
        if (isStringFunction(symbolName)) {
            return STRING_FUNCTION
        }
        if (isMathFunction(symbolName)) {
            return MATH_FUNCTION
        }
        if (isIoFunction(symbolName)) {
            return IO_FUNCTION
        }

        // Default to generic function icon
        return AllIcons.Nodes.Function
    }

    // === CLASSIFICATION HELPER METHODS ===
    private fun isSpecialForm(name: String): Boolean {
        return mutableListOf<String?>(
            "def", "defn", "defn-", "defmacro", "defmacro-", "defstruct", "let", "fn", "quote", "var", "ns"
        ).contains(name)
    }

    private fun isControlFlow(name: String): Boolean {
        return mutableListOf<String?>(
            "if", "when", "when-not", "cond", "case", "try", "catch", "finally"
        ).contains(name)
    }

    private fun isMacro(name: String): Boolean {
        return mutableListOf<String?>(
            "->", "->>", "as->", "doto", "when-let", "if-let"
        ).contains(name)
    }

    private fun isCollectionFunction(name: String): Boolean {
        return mutableListOf<String?>(
            "map",
            "filter",
            "reduce",
            "conj",
            "cons",
            "assoc",
            "dissoc",
            "get",
            "put",
            "count",
            "first",
            "rest",
            "take",
            "drop"
        ).contains(name)
    }

    private fun isStringFunction(name: String): Boolean {
        return name.startsWith("str/") || mutableListOf<String?>(
            "str", "subs", "format", "split", "join", "trim"
        ).contains(name)
    }

    private fun isMathFunction(name: String): Boolean {
        return mutableListOf<String?>(
            "+", "-", "*", "/", "mod", "inc", "dec", "abs", "max", "min"
        ).contains(name)
    }

    private fun isIoFunction(name: String): Boolean {
        return mutableListOf<String?>(
            "print", "println", "read", "slurp", "spit"
        ).contains(name)
    }
}
