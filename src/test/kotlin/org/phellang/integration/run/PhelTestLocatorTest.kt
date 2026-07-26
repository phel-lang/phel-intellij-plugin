package org.phellang.integration.run

import com.intellij.psi.search.GlobalSearchScope
import org.phellang.integration.PhelIntegrationTestCase
import org.phellang.language.psi.PhelSymbol
import org.phellang.run.test.PhelTestLocator

/**
 * Turning a `locationHint` back into the `deftest` it names.
 *
 * The report carries no file or line, only a suite name and a test name, so navigation depends
 * entirely on resolving that pair. Without it a test node in the tree goes nowhere.
 */
class PhelTestLocatorTest : PhelIntegrationTestCase() {

    private val testFile = """
        (ns app\core-test
          (:require phel\test :refer [deftest is]))

        (deftest adds
          (is (= 2 (+ 1 1))))

        (deftest subtracts
          (is (= 0 (- 1 1))))
    """.trimIndent()

    private fun locate(path: String) =
        PhelTestLocator.getLocation("phel", path, project, GlobalSearchScope.projectScope(project))

    fun testLocatesADeftestByItsNamespaceAndName() {
        myFixture.addFileToProject("tests/core_test.phel", testFile)

        val locations = locate("app\\core-test/adds")

        assertEquals(1, locations.size)
        val element = locations.single().psiElement
        assertInstanceOf(element, PhelSymbol::class.java)
        assertEquals("adds", element.text)
    }

    /** The right test, not merely the first one in the file. */
    fun testLocatesTheNamedTestRatherThanTheFirst() {
        myFixture.addFileToProject("tests/core_test.phel", testFile)

        assertEquals("subtracts", locate("app\\core-test/subtracts").single().psiElement?.text)
    }

    /** A suite node names only its namespace, and navigates to the file. */
    fun testLocatesTheFileForASuiteWithNoTestName() {
        val file = myFixture.addFileToProject("tests/core_test.phel", testFile)

        val locations = locate("app\\core-test")

        assertEquals(1, locations.size)
        assertEquals(file, locations.single().psiElement)
    }

    fun testReturnsNothingForAnUnknownTest() {
        myFixture.addFileToProject("tests/core_test.phel", testFile)

        assertEmpty(locate("app\\core-test/never-written"))
    }

    fun testReturnsNothingForAnUnknownNamespace() {
        myFixture.addFileToProject("tests/core_test.phel", testFile)

        assertEmpty(locate("app\\nowhere/adds"))
    }

    /** The locator is registered for one protocol and must decline anything else. */
    fun testDeclinesAnotherProtocol() {
        myFixture.addFileToProject("tests/core_test.phel", testFile)

        assertEmpty(
            PhelTestLocator.getLocation("java", "app\\core-test/adds", project, GlobalSearchScope.projectScope(project))
        )
    }
}
