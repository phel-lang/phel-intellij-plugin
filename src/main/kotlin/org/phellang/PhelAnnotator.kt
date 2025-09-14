package org.phellang

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import org.phellang.language.psi.*
import org.phellang.language.psi.impl.PhelVecImpl
import java.util.regex.Pattern
import kotlin.math.max

/**
 * Advanced annotator for context-sensitive Phel syntax highlighting
 * Handles PHP interop, built-in functions, and namespace-aware highlighting
 */
class PhelAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Check if this element is commented out by #_ form comment
        if (isCommentedOutByFormComment(element)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element.textRange)
                .textAttributes(COMMENTED_OUT_FORM).create()
            return  // Don't apply other highlighting to commented-out forms
        }

        if (element is PhelKeyword) {
            annotatePhelKeyword(element, holder)
        } else if (element is PhelSymbol) {
            val text = element.text

            if (text != null) {
                annotateSymbol(element, text, holder)
            }
        }
    }

    private fun annotateSymbol(symbol: PhelSymbol, text: String, holder: AnnotationHolder) {
        // Function parameters (check first, before other classifications)
        if (PhelPsiUtil.isDefinition(symbol)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(symbol.textRange)
                .textAttributes(FUNCTION_PARAMETER).create()
            return
        }

        // PHP interop patterns
        if (isPhpInterop(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(symbol.textRange)
                .textAttributes(PHP_INTEROP).create()
            return
        }

        // PHP variables (superglobals)
        if (isPhpVariable(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(symbol.textRange)
                .textAttributes(PHP_VARIABLE).create()
            return
        }

        // Namespace prefix highlighting
        if (hasNamespacePrefix(text)) {
            highlightNamespacePrefix(symbol, text, holder)
            return
        }

        // Special forms (highest priority)
        if (SPECIAL_FORMS.contains(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(symbol.textRange)
                .textAttributes(SPECIAL_FORM).create()
            return
        }

        // Macros
        if (MACROS.contains(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(symbol.textRange).textAttributes(MACRO)
                .create()
            return
        }

        // Core functions
        if (CORE_FUNCTIONS.contains(text)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(symbol.textRange)
                .textAttributes(CORE_FUNCTION).create()
            return
        }
    }

    private fun isPhpInterop(text: String): Boolean {
        return text.startsWith("php/") || text == "php/->" || text == "php/::" || text == "php/new" || text.startsWith("php/a") ||  // php/aget, php/aset, php/apush, php/aunset
                text.startsWith("php/o") // php/oset
    }

    private fun isPhpVariable(text: String): Boolean {
        return text.startsWith("php/$") || text == "php/\$_SERVER" || text == "php/\$_GET" || text == "php/\$_POST" || text == "php/\$_COOKIE" || text == "php/\$_FILES" || text == "php/\$_SESSION" || text == "php/\$GLOBALS"
    }

    private fun hasNamespacePrefix(text: String): Boolean {
        // Check for namespace separators: / or \
        return (text.contains("/") && !text.startsWith("php/")) || text.contains("\\")
    }

    private fun highlightNamespacePrefix(symbol: PhelSymbol, text: String, holder: AnnotationHolder) {
        var separatorIndex: Int

        // Find the last separator (/ or \)
        val slashIndex = text.lastIndexOf('/')
        val backslashIndex = text.lastIndexOf('\\')
        separatorIndex = max(slashIndex, backslashIndex)

        if (separatorIndex > 0) {
            // Highlight only the namespace part (before the separator)
            val startOffset = symbol.textRange.startOffset
            val endOffset = startOffset + separatorIndex

            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(TextRange.create(startOffset, endOffset))
                .textAttributes(NAMESPACE_PREFIX).create()
        }
    }

    /**
     * Annotate keyword elements to ensure proper highlighting
     */
    private fun annotatePhelKeyword(keyword: PhelKeyword, holder: AnnotationHolder) {
        // Highlight the entire keyword element
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(keyword.textRange)
            .textAttributes(PhelSyntaxHighlighter.KEYWORD).create()
    }

    /**
     * Check if an element is commented out by #_ form comment(s).
     * Handles stacking of multiple #_ tokens (e.g., #_#_ comments out two forms).
     */
    private fun isCommentedOutByFormComment(element: PsiElement): Boolean {
        // First try text-based detection for vectors (workaround for grammar bug)
        if (isInCommentedVectorByText(element)) {
            return true
        }

        // Find the form that contains this element
        val containingForm: PhelForm = findContainingForm(element) ?: return false

        // Check if this form is commented out by counting preceding #_ tokens
        // First check the containing form itself, then check parent forms
        return isFormCommentedOut(containingForm) || isParentFormCommentedOut(containingForm)
    }

    /**
     * Find the immediate form that contains the given element.
     */
    private fun findContainingForm(element: PsiElement): PhelForm? {
        var current: PsiElement? = element
        while (current != null) {
            if (current is PhelForm) {
                return current
            }
            current = current.parent
        }
        return null
    }

    /**
     * Check if a form is commented out by counting preceding #_ tokens.
     * Handles stacking: #_ comments out 1 form, #_#_ comments out 2 forms, etc.
     */
    private fun isFormCommentedOut(form: PhelForm): Boolean {
        val parent = form.parent ?: return false

        // Get all children of the parent to analyze the sequence
        val children = parent.children
        var formIndex = -1

        // Find the index of our form
        for (i in children.indices) {
            if (children[i] === form) {
                formIndex = i
                break
            }
        }

        if (formIndex == -1) {
            return false
        }

        // Find the sequence of forms that this form belongs to
        // and check if #_ tokens precede that sequence

        // Find the start of the form sequence that contains our form
        var sequenceStart = formIndex
        for (i in formIndex - 1 downTo 0) {
            val child: PsiElement = children[i]
            if (child is PsiWhiteSpace) {
                continue  // Skip whitespace
            } else if (child is PhelForm) {
                sequenceStart = i // Found another form, extend sequence backwards
            } else {
                break // Hit non-form, non-whitespace - end of sequence
            }
        }

        // Count consecutive #_ tokens immediately before the sequence
        var consecutiveFormComments = 0
        for (i in sequenceStart - 1 downTo 0) {
            val child = children[i]

            if (child is PsiWhiteSpace) {
                continue  // Skip whitespace
            } else if (child.node != null && child.node.elementType === PhelTypes.FORM_COMMENT) {
                consecutiveFormComments++
            } else if (isFormCommentMacro(child)) {
                consecutiveFormComments++
            } else {
                break
            }
        }

        if (consecutiveFormComments == 0) {
            return false
        }

        // Determine position of our form within the sequence
        var formPosition = 0
        for (i in sequenceStart..formIndex) {
            val child: PsiElement = children[i]
            if (child is PhelForm) {
                formPosition++
                if (child === form) {
                    break
                }
            }
        }

        return formPosition <= consecutiveFormComments
    }

    /**
     * Check if any parent form is commented out by #_. This handles cases like [#_:one :two]
     * where :one and :two are inside a vector that is commented out.
     */
    private fun isParentFormCommentedOut(form: PhelForm): Boolean {
        var parent = form.parent
        while (parent != null) {
            if (parent is PhelForm) {
                val parentForm = parent
                if (isFormCommentedOut(parentForm)) {
                    return true
                }
                parent = parent.parent
            } else {
                break
            }
        }
        return false
    }

    /**
     * Check if a PSI element is a form_comment_macro (i.e., #_ form).
     */
    private fun isFormCommentMacro(element: PsiElement): Boolean {
        // Check if this is a form_comment_macro by looking at its structure
        if (element.node == null) {
            return false
        }

        // Look for elements that start with #_ token
        val children = element.children
        if (children.size > 0) {
            val firstChild = children[0]
            if (firstChild.node != null && firstChild.node.elementType === PhelTypes.FORM_COMMENT) {
                return true
            }
        }

        // Also check if the element itself is the form_comment token
        return element.node.elementType === PhelTypes.FORM_COMMENT
    }

    /**
     * Workaround for grammar bug: detect form comments by analyzing text content.
     * This handles cases like [#_:one :two] where the PSI tree is missing #_ tokens.
     */
    private fun isInCommentedVectorByText(element: PsiElement): Boolean {
        // Check if we're inside a vector
        val parent = element.parent
        if (parent !is PhelVecImpl) {
            return false
        }

        val vectorText = parent.text

        // Find element's position within the vector text
        val elementOffsetInVector = element.textOffset - parent.textOffset

        // Look for #_ patterns before this element's position
        val beforeElement = vectorText.take(elementOffsetInVector)

        // Simple regex to find #_ patterns
        val pattern = Pattern.compile("#_")
        val matcher = pattern.matcher(beforeElement)

        var formCommentCount = 0
        while (matcher.find()) {
            formCommentCount++
        }

        if (formCommentCount > 0) {
            // Count how many actual forms (keywords/symbols) appear before this element
            val textBeforeForCounting = beforeElement.replace("#_".toRegex(), "").trim { it <= ' ' }


            // Use regex to find all keywords and symbols in the text
            val formPattern = Pattern.compile("(:[\\w-]+|[a-zA-Z][\\w-]*)")
            val formMatcher = formPattern.matcher(textBeforeForCounting)

            var formsBeforeCount = 0
            while (formMatcher.find()) {
                formsBeforeCount++
            }


            // If this element is at position N (0-based) and there are M #_ tokens,
            // then it's commented out if N < M
            return formsBeforeCount < formCommentCount
        }

        return false
    }
}

// PHP interop highlighting
@JvmField
val PHP_INTEROP: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
    "PHEL_PHP_INTEROP", DefaultLanguageHighlighterColors.STATIC_METHOD
)

@JvmField
val PHP_VARIABLE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
    "PHEL_PHP_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE
)

// Built-in function categories
@JvmField
val CORE_FUNCTION: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
    "PHEL_CORE_FUNCTION", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
)

@JvmField
val MACRO: TextAttributesKey =
    TextAttributesKey.createTextAttributesKey("PHEL_MACRO", DefaultLanguageHighlighterColors.KEYWORD)

@JvmField
val SPECIAL_FORM: TextAttributesKey =
    TextAttributesKey.createTextAttributesKey("PHEL_SPECIAL_FORM", DefaultLanguageHighlighterColors.KEYWORD)

// Namespace highlighting
@JvmField
val NAMESPACE_PREFIX: TextAttributesKey =
    TextAttributesKey.createTextAttributesKey("PHEL_NAMESPACE", DefaultLanguageHighlighterColors.CLASS_NAME)

// Form comment highlighting - for forms commented out by #_
@JvmField
val COMMENTED_OUT_FORM: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
    "PHEL_COMMENTED_OUT_FORM", DefaultLanguageHighlighterColors.LINE_COMMENT
)

// Function parameter highlighting (more distinct color)
@JvmField
val FUNCTION_PARAMETER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
    "PHEL_FUNCTION_PARAMETER", DefaultLanguageHighlighterColors.INSTANCE_FIELD
)

// Core Phel special forms
private val SPECIAL_FORMS = mutableSetOf<String?>(
    "def",
    "fn",
    "if",
    "let",
    "do",
    "quote",
    "var",
    "recur",
    "throw",
    "try",
    "catch",
    "finally",
    "loop",
    "case",
    "cond",
    "when",
    "when-not",
    "when-let",
    "if-let",
    "if-not",
    "ns",
    "defn",
    "defn-",
    "defmacro",
    "defmacro-",
    "defstruct",
    "definterface"
)

// Core Phel macros
private val MACROS = mutableSetOf<String?>(
    "and",
    "or",
    "->",
    "->>",
    "as->",
    "cond->",
    "cond->>",
    "some->",
    "some->>",
    "doto",
    "for",
    "dofor",
    "foreach",
    "comment",
    "declare"
)

// Common core functions (subset for performance)
private val CORE_FUNCTIONS = mutableSetOf<String?>(
    // Collection operations
    "map",
    "filter",
    "reduce",
    "apply",
    "count",
    "first",
    "rest",
    "cons",
    "conj",
    "get",
    "get-in",
    "assoc",
    "dissoc",
    "update",
    "merge",
    "keys",
    "values",
    "peek",
    "pop",
    "push",
    "remove",
    "second",
    "ffirst",
    "next",
    "nnext",
    "nfirst",

    // Sequence operations
    "take",
    "drop",
    "take-while",
    "drop-while",
    "take-nth",
    "take-last",
    "distinct",
    "reverse",
    "sort",
    "sort-by",
    "shuffle",
    "flatten",
    "range",
    "repeat",
    "interleave",
    "interpose",
    "partition",
    "split-at",
    "split-with",

    // String operations
    "str",
    "print",
    "println",
    "print-str",
    "format",
    "printf",
    "str-contains?",
    "slurp",
    "spit",

    // Function operations
    "identity",
    "comp",
    "partial",
    "constantly",
    "complement",
    "juxt",
    "memoize",

    // Math operations
    "inc",
    "dec",
    "sum",
    "mean",
    "extreme",
    "+",
    "-",
    "*",
    "/",
    "mod",
    "rem",
    "quot",
    "=",
    "not=",
    "<",
    "<=",
    ">",
    ">=",
    "min",
    "max",
    "abs",

    // Predicates
    "pos?",
    "neg?",
    "zero?",
    "one?",
    "even?",
    "odd?",
    "nil?",
    "some?",
    "all?",
    "true?",
    "false?",
    "truthy?",
    "boolean?",
    "number?",
    "string?",
    "keyword?",
    "symbol?",
    "list?",
    "vector?",
    "map?",
    "set?",
    "seq?",
    "coll?",
    "empty?",
    "not-empty",
    "float?",
    "int?",
    "function?",
    "struct?",
    "hash-map?",
    "php-array?",
    "php-resource?",
    "php-object?",
    "indexed?",
    "associative?",
    "var?",

    // Random functions
    "rand",
    "rand-int",
    "rand-nth",

    // Meta and symbol operations
    "meta",
    "name",
    "namespace",
    "full-name",
    "symbol",
    "keyword",
    "gensym",

    // Evaluation and compilation
    "eval",
    "compile",
    "macroexpand",
    "macroexpand-1",
    "read-string",

    // Bit operations
    "bit-and",
    "bit-or",
    "bit-xor",
    "bit-not",
    "bit-shift-left",
    "bit-shift-right",
    "bit-set",
    "bit-clear",
    "bit-flip",
    "bit-test",

    // Other core functions
    "not",
    "compare",
    "type",
    "contains?",
    "find",
    "find-index",
    "frequencies",
    "group-by",
    "union",
    "intersection",
    "difference",
    "symmetric-difference",
    "select-keys",
    "invert",
    "merge-with",
    "deep-merge",

    // String module functions (phel\str)
    "split",
    "join",
    "replace",
    "replace-first",
    "trim",
    "triml",
    "trimr",
    "capitalize",
    "lower-case",
    "upper-case",
    "starts-with?",
    "ends-with?",
    "blank?",
    "includes?",
    "index-of",
    "last-index-of",
    "split-lines",
    "pad-left",
    "pad-right",
    "trim-newline",
    "escape",
    "re-quote-replacement",

    // JSON module functions (phel\json)
    "encode",
    "decode",

    // Base64 module functions (phel\base64)
    "encode-url",
    "decode-url",

    // HTML module functions (phel\html)
    "escape-html",
    "doctype",

    // Test module functions (phel\test)
    "report",
    "print-summary",
    "run-tests",
    "successful?",

    // REPL module functions (phel\repl)
    "loaded-namespaces",
    "resolve",
    "print-colorful",
    "println-colorful",
    "compile-str",

    // Trace module functions (phel\trace)
    "dotrace",
    "reset-trace-state!",
    "set-trace-id-padding!",

    // HTTP module functions (phel\http) - most commonly used
    "request-from-globals",
    "response-from-map",
    "response-from-string",
    "emit-response"
)