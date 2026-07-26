package org.phellang.integration.parameterinfo

import com.intellij.testFramework.utils.parameterInfo.MockCreateParameterInfoContext
import com.intellij.testFramework.utils.parameterInfo.MockUpdateParameterInfoContext
import org.phellang.indexing.PhelProjectSymbolIndex
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelList
import org.phellang.language.psi.files.PhelFile
import org.phellang.parameterinfo.PhelParameterInfoHandler
import org.phellang.registry.PhelArity

/**
 * The Ctrl+P popup over a Phel call.
 *
 * What it shows is every arity of the callee, which is the thing the inlay hints cannot do — they
 * label one argument each and only ever reflect the arity that already matched.
 */
class PhelParameterInfoHandlerTest : PhelIntegrationTestCase() {

    private val handler = PhelParameterInfoHandler()
    private var fileIndex = 0

    /**
     * Primed before asking: `PhelArityResolver` consults the project symbol index for a project
     * function, and a file created by `configureByText` is not in it until something indexes it.
     * Without this the result depends on which test ran first.
     */
    private fun aritiesAt(source: String): List<PhelArity>? {
        val file = myFixture.configureByText("pi${fileIndex++}.phel", source) as PhelFile
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)
        val context = MockCreateParameterInfoContext(myFixture.editor, myFixture.file)

        handler.findElementForParameterInfo(context) ?: return null

        return context.itemsToShow?.map { it as PhelArity }
    }

    private fun currentParameterAt(source: String): Int {
        val file = myFixture.configureByText("pi${fileIndex++}.phel", source) as PhelFile
        PhelProjectSymbolIndex.getInstance(project).refreshFileFromPsi(file)
        val create = MockCreateParameterInfoContext(myFixture.editor, myFixture.file)
        val call = handler.findElementForParameterInfo(create) as PhelList

        val update = MockUpdateParameterInfoContext(myFixture.editor, myFixture.file)
        handler.updateParameterInfo(call, update)

        return update.currentParameter
    }

    /** A stdlib function, resolved from the registry rather than the project index. */
    fun testOffersTheAritiesOfAStdlibFunction() {
        val arities = aritiesAt("(ns app\\m)\n(defn f [] (map <caret>))\n")

        assertNotNull("map should resolve to at least one arity", arities)
        assertTrue(arities.toString(), arities!!.isNotEmpty())
    }

    fun testOffersTheAritiesOfAProjectFunction() {
        val arities = aritiesAt("(ns app\\m)\n(defn greet [name greeting] 1)\n(defn f [] (greet <caret>))\n")

        assertNotNull(arities)
        assertEquals(listOf("name", "greeting"), arities!!.single().params)
    }

    /** The caret moves through the arguments, and the popup follows it. */
    fun testTracksWhichArgumentTheCaretIsIn() {
        val source = "(ns app\\m)\n(defn greet [name greeting] 1)\n(defn f [] (greet \"a\"<caret> \"b\"))\n"

        assertEquals(0, currentParameterAt(source))
    }

    fun testTracksTheSecondArgument() {
        val source = "(ns app\\m)\n(defn greet [name greeting] 1)\n(defn f [] (greet \"a\" \"b\"<caret>))\n"

        assertEquals(1, currentParameterAt(source))
    }

    /**
     * The same heads the inlay hints skip, for the same reason: their arguments are not positional,
     * so a parameter list would be a lie rather than a help.
     */
    fun testDeclinesASpecialForm() {
        assertNull(aritiesAt("(ns app\\m)\n(defn f [] (let [<caret>] 1))\n"))
    }

    fun testDeclinesInterop() {
        assertNull(aritiesAt("(ns app\\m)\n(defn f [] (php/strlen <caret>))\n"))
    }

    /** A local binding shadowing a known name is a different function entirely. */
    fun testDeclinesALocalBindingThatShadowsAKnownName() {
        assertNull(aritiesAt("(ns app\\m)\n(defn f [] (let [map (fn [x] x)] (map <caret>)))\n"))
    }

    fun testDeclinesAnUnknownName() {
        assertNull(aritiesAt("(ns app\\m)\n(defn f [] (no-such-function-anywhere <caret>))\n"))
    }
}
