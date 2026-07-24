package org.phellang.completion.engine.locals

import com.intellij.psi.PsiElement
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.registry.PhelCompletionPriority

/**
 * What a top-level definition is called in completion, and how highly it ranks.
 *
 * Display text and priority were two parallel `when` blocks keyed on the same keyword, which let
 * them drift; hanging both off one constant keeps a keyword's rendering and ranking together.
 */
private enum class LocalDefinitionKind(
    val description: String,
    val priority: PhelCompletionPriority,
) {
    VARIABLE("Local Variable", PhelCompletionPriority.PROJECT_SYMBOLS),
    FUNCTION("Local Function", PhelCompletionPriority.RECENT_DEFINITIONS),
    MACRO("Local Macro", PhelCompletionPriority.RECENT_DEFINITIONS),
    EXCEPTION("Local Exception", PhelCompletionPriority.PROJECT_SYMBOLS),
    INTERFACE("Local Interface", PhelCompletionPriority.PROJECT_SYMBOLS),
    STRUCT("Local Struct", PhelCompletionPriority.PROJECT_SYMBOLS),
}

private val BY_KEYWORD = mapOf(
    "def" to LocalDefinitionKind.VARIABLE,
    "defn" to LocalDefinitionKind.FUNCTION,
    "defn-" to LocalDefinitionKind.FUNCTION,
    "defmacro" to LocalDefinitionKind.MACRO,
    "defmacro-" to LocalDefinitionKind.MACRO,
    "defexception" to LocalDefinitionKind.EXCEPTION,
    "defexception*" to LocalDefinitionKind.EXCEPTION,
    "definterface" to LocalDefinitionKind.INTERFACE,
    "definterface*" to LocalDefinitionKind.INTERFACE,
    "defstruct" to LocalDefinitionKind.STRUCT,
    "defstruct*" to LocalDefinitionKind.STRUCT,
)

/**
 * Offers the edited file's own top-level definitions, which are legal to type unqualified.
 *
 * Definitions in *other* files deliberately do not come from here: `PhelProjectCompletionHelper`
 * serves those from the symbol index, namespace-qualified and with auto-import, which is the only
 * spelling that compiles.
 */
internal object PhelFileDefinitionCollector {

    fun collect(position: PsiElement, sink: PhelLocalSymbolSink) {
        val file = position.containingFile as? PhelFile ?: return

        for (list in file.children.filterIsInstance<PhelList>()) {
            offer(list, sink)
        }
    }

    private fun offer(list: PhelList, sink: PhelLocalSymbolSink) {
        // activeForms, not children: `#_` leaves the discarded form in the tree, so reading raw
        // children would offer the discarded name of `(defn #_old new [x] x)` instead of `new`.
        val forms = PhelPsiUtils.activeForms(list)
        if (forms.size < 2) return

        val keyword = PhelPsiUtils.asSymbol(forms[0])?.text ?: return
        val kind = BY_KEYWORD[keyword] ?: return
        val name = PhelPsiUtils.asSymbol(forms[1])?.text ?: return

        sink.addFileDefinition(name, kind.description, kind.priority)
    }
}
