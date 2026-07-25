package org.phellang.inspection.analysis

import com.intellij.psi.PsiElement
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.language.psi.PhelForm
import org.phellang.language.psi.PhelInteropShorthands
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.PhelSpecialForms
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelVec
import org.phellang.language.psi.analysis.PhelFormWalker
import org.phellang.language.psi.PhelSymbol
import org.phellang.language.psi.analysis.PhelSymbolAnalyzer
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.registry.PhelFunctionRegistry
import org.phellang.registry.SymbolType

/**
 * Decides whether a bare symbol names something that exists.
 *
 * Phel's own analyzer resolves a symbol against locals, then the current namespace's globals, then
 * `use` aliases, then `phel.core`, and throws `PHEL001 Cannot resolve symbol` on a miss. This mirrors
 * that order, so what the inspection reports is what the compiler will reject.
 *
 * The bias throughout is to under-report. A missed warning costs the user nothing; a false one on
 * working code costs them trust in every other warning the plugin raises. Every case this cannot
 * decide is therefore skipped rather than guessed at — see [isBeyondStaticAnalysis].
 */
internal object PhelUnresolvedSymbolFinder {

    /**
     * The rest marker, the discard name, and the two implicit parameters the compiler injects into
     * every `defmacro` body — `(apply defn-builder &form &env ...)` appears throughout phel\core.
     */
    private val ALWAYS_IGNORED = setOf("&", "_", "&form", "&env")

    /**
     * Forms that declare a type. Their bodies hold field vectors and protocol method
     * implementations — method names and their `this` receiver are declarations, not references.
     */
    private val TYPE_DECLARING = setOf(
        "defstruct", "defstruct*", "definterface", "definterface*",
        "defrecord", "deftype", "defprotocol",
    )

    /** The name [symbol] fails to resolve, or null when it resolves or is not a reference at all. */
    fun unresolvedName(symbol: PhelSymbol): String? {
        val text = symbol.text?.takeIf { it.isNotBlank() } ?: return null
        if (!isReferencePosition(symbol, text)) return null
        if (isBeyondStaticAnalysis(symbol)) return null
        if (resolves(symbol, text)) return null

        return text
    }

    /**
     * A bare symbol being *read*. Declarations, qualified names and interop are all somebody else's
     * question: a qualified `ns/name` is already checked by the annotator's reference validator.
     */
    private fun isReferencePosition(symbol: PhelSymbol, text: String): Boolean {
        if (text in ALWAYS_IGNORED) return false
        // `%`, `%1`, `%2` are the short-fn anaphors; `$` is accepted defensively.
        if (text.startsWith("%") || text.startsWith("$")) return false
        if (text.contains("/") || text.contains("\\")) return false
        if (text.startsWith(".") || PhelInteropShorthands.isInteropClassName(text)) return false

        // The name a definition or binding introduces is not a reference to anything.
        return !PhelSymbolAnalyzer.isDefinition(symbol)
    }

    /**
     * Positions where a name need not exist, or where the plugin cannot know whether it does.
     *
     * Quoted forms are data — `'foo` never resolves `foo`. A user macro is the harder case: its
     * expansion may bind names that appear nowhere in the source, so anything inside a call to one
     * is unknowable without expanding it, which the plugin cannot do. Phel's own linter makes the
     * same trade, suppressing alias-qualified symbols it cannot evaluate.
     *
     * The rest were found by running this over `phel-lang/src/phel` and grouping what it reported by
     * the shape each symbol sat in. Every one is a position holding something that is not a Phel
     * name at all, or a binding form whose exact shape the plugin does not model.
     */
    private fun isBeyondStaticAnalysis(symbol: PhelSymbol): Boolean =
        isInsideQuotedForm(symbol) ||
                isInsideMacroCall(symbol) ||
                isInsidePhpInterop(symbol) ||
                isInsideNsForm(symbol) ||
                isInsideATypeDeclaration(symbol) ||
                isCatchBinding(symbol) ||
                isBoundByAnEnclosingVector(symbol)

    /**
     * `(php/-> obj (getName))`, `(php/:: Class (create x))`. The head of the inner list is a PHP
     * method, and its arguments may be PHP constants — none of it is resolvable as a Phel name.
     */
    private fun isInsidePhpInterop(symbol: PhelSymbol): Boolean =
        enclosingHeads(symbol).any { it.startsWith(PHP_PREFIX) }

    /** The `ns` form is import syntax: namespace names and `:refer` lists, not expressions. */
    private fun isInsideNsForm(symbol: PhelSymbol): Boolean =
        enclosingHeads(symbol).any { it == "ns" }

    /**
     * Anywhere inside a type declaration: its field vector, and the protocol method implementations
     * that follow it — `(defstruct R [routes] Router (match-by-path [this path] ...))`. The field
     * names, the method names and their `this` receiver are all declarations.
     */
    private fun isInsideATypeDeclaration(symbol: PhelSymbol): Boolean =
        enclosingHeads(symbol).any { it in TYPE_DECLARING }

    /**
     * `(catch \Exception e (println e))` — the class and the binding are declarations, and `e` is in
     * scope for the whole body, so the name has to be recognised there too rather than only in the
     * slot that introduces it.
     */
    private fun isCatchBinding(symbol: PhelSymbol): Boolean {
        val name = symbol.text ?: return false

        return PhelFormWalker.enclosingLists(symbol)
            .filter { PhelFormWalker.headText(it) == "catch" }
            .any { clause ->
                val forms = PhelPsiUtils.activeForms(clause)

                forms.take(3).any { PsiTreeUtil.isAncestor(it, symbol, false) } ||
                        PhelPsiUtils.asSymbol(forms.getOrNull(2))?.text == name
            }
    }

    /**
     * A name that appears anywhere in the binding vector of an enclosing binding form.
     *
     * Deliberately coarse. `foreach` binds three slots rather than pairs, `for` and `dofor` take
     * verbs (`:in`, `:pairs`, `:let`, `:when`), and any of them may destructure — `(for [[k v] :pairs m] ...)`.
     * The plugin models none of those shapes, so matching on the name is what keeps their bindings
     * from being reported throughout the body.
     */
    private fun isBoundByAnEnclosingVector(symbol: PhelSymbol): Boolean {
        val name = symbol.text ?: return false

        return PhelFormWalker.enclosingLists(symbol)
            .filter { PhelFormWalker.headText(it) in PhelSpecialForms.LET_LIKE }
            .mapNotNull { it.children.getOrNull(1) as? PhelVec }
            .any { vector ->
                PsiTreeUtil.findChildrenOfType(vector, PhelSymbol::class.java).any { it.text == name }
            }
    }

    /** The heads of every list enclosing [symbol], innermost first. */
    private fun enclosingHeads(symbol: PhelSymbol): Sequence<String> =
        PhelFormWalker.enclosingLists(symbol).mapNotNull { PhelFormWalker.headText(it) }

    private const val PHP_PREFIX = "php/"

    /**
     * A reader macro is a *prefix* of the form it applies to, not an ancestor of it: the grammar
     * attaches it via `form ::= form_prefix* ...`, so `'foo` is a form carrying a `'` reader macro
     * whose sibling is the symbol. Walking parents alone never sees it — every enclosing form has to
     * be asked for its own prefixes.
     */
    private fun isInsideQuotedForm(symbol: PhelSymbol): Boolean {
        var current: PsiElement? = symbol

        while (current != null && current !is PhelFile) {
            val quoted = (current as? PhelForm)
                ?.readerMacros
                ?.any { it.text.firstOrNull() in QUOTE_CHARS } == true
            if (quoted) return true

            current = current.parent
        }

        return false
    }

    private fun isInsideMacroCall(symbol: PhelSymbol): Boolean {
        var current: PsiElement? = symbol.parent

        while (current != null && current !is PhelFile) {
            val head = (current as? PhelList)?.let { PhelPsiUtils.asSymbol(it.forms.firstOrNull())?.text }
            if (head != null && namesAMacro(symbol, head)) return true

            current = current.parent
        }

        return false
    }

    /**
     * The edited file is checked directly rather than through the index. A macro the user is writing
     * right now is the case most likely to produce a false report, and it is exactly the case the
     * index has not caught up with — it refreshes off VFS events, asynchronously.
     */
    private fun namesAMacro(symbol: PhelSymbol, head: String): Boolean {
        if (definesMacroInFile(symbol.containingFile as? PhelFile, head)) return true

        return PhelProjectSymbolIndex.getInstance(symbol.project)
            .findByName(head)
            .any { it.type == SymbolType.MACRO }
    }

    private fun definesMacroInFile(file: PhelFile?, head: String): Boolean {
        if (file == null) return false

        return file.children.filterIsInstance<PhelList>().any { list ->
            val forms = PhelPsiUtils.activeForms(list)

            forms.size >= 2 &&
                    PhelPsiUtils.asSymbol(forms[0])?.text in MACRO_DEFINING &&
                    PhelPsiUtils.asSymbol(forms[1])?.text == head
        }
    }

    private val MACRO_DEFINING = setOf("defmacro", "defmacro-")

    /** Phel's resolution order: locals, current-namespace globals, `use` aliases, then `phel.core`. */
    private fun resolves(symbol: PhelSymbol, text: String): Boolean {
        // Locals, plus same-file and vendor definitions, all of which the reference already answers.
        if (symbol.reference?.resolve() != null) return true
        if (PhelSymbolAnalyzer.isLocalBindingOrReference(symbol)) return true

        // Language keywords are not always registry entries, and never resolve to a definition.
        if (text in PhelSpecialForms.VARIADIC_HEADS || text in PhelSpecialForms.NAME_DECLARING) return true

        // phel.core and the rest of the bundled standard library.
        if (PhelFunctionRegistry.getFunction(text) != null) return true

        val file = symbol.containingFile as? PhelFile ?: return true
        if (PhelNamespaceUtils.isReferredSymbol(file, text)) return true
        if (text in PhelNamespaceUtils.extractUsedClasses(file)) return true

        return PhelProjectSymbolIndex.getInstance(symbol.project).findByName(text).isNotEmpty()
    }

    private val QUOTE_CHARS = setOf('\'', '`')
}
