package org.phellang.run.test

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.PhelNamespaceUtils
import org.phellang.language.psi.files.PhelFile
import org.phellang.language.psi.utils.PhelPsiUtils

/**
 * Tells a Phel test file from an ordinary one, and locates the `deftest` forms in it.
 *
 * Detection is semantic — the file's `ns` requires `phel\test` — rather than positional. Deriving it
 * from `withTestDirs([...])` in `phel-config.php` was rejected: that file is PHP evaluated by PHP,
 * its `testDirs` already defaults to `['tests']` so the call's presence carries no information, and
 * `phel-config-local.php` overrides it. It would also err both ways here, marking a fixture inside
 * `tests/` that defines nothing and missing a test file kept outside it.
 */
object PhelTestDetection {

    fun isTestFile(file: PhelFile): Boolean {
        val declaration = PhelNamespaceUtils.findNamespaceDeclaration(file) ?: return false
        return PhelNamespaceUtils.isNamespaceRequired(declaration, TEST_NAMESPACE)
    }

    /**
     * The test [list] defines, or null when it is not a `deftest`.
     *
     * [PhelPsiUtils.activeForms] rather than `list.forms`, because `#_` leaves the discarded form in
     * the tree: reading raw children would take the discarded name of `(deftest #_old new ...)`.
     * [PhelPsiUtils.asSymbol] rather than a cast, because a simple symbol parses as `PhelAccess`.
     */
    fun deftestName(list: PhelList): String? {
        val forms = PhelPsiUtils.activeForms(list)
        if (forms.size < 2) return null

        val head = PhelPsiUtils.asSymbol(forms[0])?.text ?: return null
        if (!isDeftest(head)) return null

        return PhelPsiUtils.asSymbol(forms[1])?.text
    }

    /**
     * The name of the test [element] heads, or null when it heads nothing runnable.
     *
     * Ordered cheapest-first: this is called for every leaf in the file while highlighting.
     */
    fun deftestHeadedBy(element: PsiElement): String? {
        val text = element.text
        if (text != DEFTEST && !text.endsWith(ALIASED_DEFTEST)) return null

        val file = element.containingFile as? PhelFile ?: return null
        if (!isTestFile(file)) return null

        val list = PsiTreeUtil.getParentOfType(element, PhelList::class.java) ?: return null
        if (list.parent !== file) return null

        val name = deftestName(list) ?: return null
        // The head text sits on a leaf beneath the symbol; `strict = false` also accepts the symbol
        // itself, so this covers both shapes without a separate identity check.
        val head = PhelPsiUtils.asSymbol(PhelPsiUtils.activeForms(list).firstOrNull()) ?: return null
        if (!PsiTreeUtil.isAncestor(head, element, false)) return null

        return name
    }

    /** The test [element] sits inside, however deep, or null when it is not within one. */
    fun enclosingDeftestName(element: PsiElement): String? {
        var list = PsiTreeUtil.getParentOfType(element, PhelList::class.java)
        while (list != null) {
            if (list.parent is PhelFile) return deftestName(list)
            list = PsiTreeUtil.getParentOfType(list, PhelList::class.java)
        }

        return null
    }

    /** `deftest` when referred, `t/deftest` when the require was aliased with `:as`. */
    private fun isDeftest(head: String): Boolean = head == DEFTEST || head.endsWith(ALIASED_DEFTEST)

    private const val DEFTEST = "deftest"

    private const val ALIASED_DEFTEST = "/$DEFTEST"

    private const val TEST_NAMESPACE = "phel\\test"
}
