package org.phellang.integration.psi

import com.intellij.psi.util.PsiTreeUtil
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.utils.PhelPsiUtils
import org.phellang.language.psi.files.PhelFile

/**
 * Regression coverage for [PhelPsiUtils.asSymbol]. Simple symbols at a list head or a
 * binding position parse as a PhelAccess wrapper rather than a bare PhelSymbol; several
 * features (inspections, inlay hints) relied on a plain `as? PhelSymbol` cast and so
 * silently did nothing. asSymbol must unwrap those forms to their PhelSymbol.
 */
class PhelAsSymbolTest : PhelIntegrationTestCase() {

    fun testUnwrapsListHead() {
        val list = firstList("(ns app\\m)\n(let [x 1] x)\n", head = "let")
        val head = PhelPsiUtils.asSymbol(list.forms[0])
        assertNotNull("let head should unwrap to a symbol", head)
        assertEquals("let", head!!.text)
    }

    fun testUnwrapsBindingName() {
        val list = firstList("(ns app\\m)\n(let [foo 1] foo)\n", head = "let")
        val vec = list.forms[1] as com.intellij.psi.PsiElement
        val firstBinding = PsiTreeUtil.findChildrenOfType(vec, org.phellang.language.psi.PhelForm::class.java).first()
        val symbol = PhelPsiUtils.asSymbol(firstBinding)
        assertNotNull("binding name should unwrap to a symbol", symbol)
        assertEquals("foo", symbol!!.text)
    }

    fun testNullReturnsNull() {
        assertNull(PhelPsiUtils.asSymbol(null))
    }

    private fun firstList(text: String, head: String): PhelList {
        // Unique path per class: the shared BasePlatformTestCase project does not reliably
        // clean files between classes, so sharing a path (e.g. src/main.phel) lets stale
        // content from another test leak in and resolve incorrectly.
        val vf = myFixture.addFileToProject("src/as_symbol_test.phel", text).virtualFile
        val phelFile = com.intellij.psi.PsiManager.getInstance(project).findFile(vf) as PhelFile
        return PsiTreeUtil.findChildrenOfType(phelFile, PhelList::class.java)
            .first { PhelPsiUtils.asSymbol(it.forms.firstOrNull())?.text == head }
    }
}
