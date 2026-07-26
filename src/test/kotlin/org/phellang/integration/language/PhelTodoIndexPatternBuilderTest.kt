package org.phellang.integration.language

import com.intellij.psi.search.PsiTodoSearchHelper
import org.phellang.integration.PhelIntegrationTestCase

/**
 * `TODO` and `FIXME` in a Phel comment reach the platform's TODO index.
 *
 * Driven through [PsiTodoSearchHelper], the same entry point the TODO tool window and the commit
 * dialog's check use, so the registration is exercised rather than the builder called directly.
 */
class PhelTodoIndexPatternBuilderTest : PhelIntegrationTestCase() {

    private fun todosIn(source: String): List<String> {
        val file = myFixture.configureByText("todo.phel", source)

        return PsiTodoSearchHelper.getInstance(project)
            .findTodoItems(file)
            .map { file.text.substring(it.textRange.startOffset, it.textRange.endOffset) }
    }

    fun testFindsATodoInALineComment() {
        val todos = todosIn("(ns app\\m)\n; TODO tidy this up\n(defn f [] 1)\n")

        assertEquals(listOf("TODO tidy this up"), todos)
    }

    fun testFindsAFixme() {
        val todos = todosIn("(ns app\\m)\n; FIXME broken on empty input\n(defn f [] 1)\n")

        assertEquals(listOf("FIXME broken on empty input"), todos)
    }

    fun testFindsATrailingTodoAfterCode() {
        val todos = todosIn("(ns app\\m)\n(defn f [] 1) ; TODO memoize\n")

        assertEquals(listOf("TODO memoize"), todos)
    }

    /** Compared as a set: `findTodoItems` does not promise document order, and does not return it. */
    fun testFindsEveryTodoInAFile() {
        val todos = todosIn("(ns app\\m)\n; TODO first\n(defn f [] 1)\n; TODO second\n")

        assertEquals(setOf("TODO first", "TODO second"), todos.toSet())
    }

    /** Ordinary prose is not a note. */
    fun testIgnoresACommentWithNoPattern() {
        assertEmpty(todosIn("(ns app\\m)\n; just explaining the next form\n(defn f [] 1)\n"))
    }

    /** Code is not a comment, however much it looks like one. */
    fun testIgnoresTodoInAString() {
        assertEmpty(todosIn("(ns app\\m)\n(defn f [] \"TODO not a comment\")\n"))
    }

    /**
     * `#_` discards the form after it, so a note inside one is commented-out code rather than a
     * message to the reader — and the token itself is only the `#_` marker, carrying no prose.
     */
    fun testIgnoresATodoInsideADiscardedForm() {
        assertEmpty(todosIn("(ns app\\m)\n(list #_(def TODO 1) 2)\n"))
    }
}
