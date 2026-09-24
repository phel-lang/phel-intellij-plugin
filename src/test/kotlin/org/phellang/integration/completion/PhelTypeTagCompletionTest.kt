package org.phellang.integration.completion

import org.phellang.integration.PhelIntegrationTestCase

/**
 * Completion after `^` offers what a type tag can name: Phel's short value tags, PHP types, and the
 * classes the namespace imports. Nothing else fits there, least of all the functions `^map` shares a
 * name with, and a tag inside a parameter vector must not be caught by the "naming a parameter" silence.
 */
class PhelTypeTagCompletionTest : PhelIntegrationTestCase() {

    private var fileIndex = 0

    private fun completionsAt(source: String): List<String> {
        myFixture.configureByText("tag_completion${fileIndex++}.phel", source)
        myFixture.completeBasic()

        return myFixture.lookupElementStrings ?: emptyList()
    }

    /** Completes where only one candidate matches, which the fixture inserts; returns the file text. */
    private fun completedText(source: String): String {
        myFixture.configureByText("tag_completion${fileIndex++}.phel", source)
        myFixture.completeBasic()

        return myFixture.file.text
    }

    fun testOffersValueTagsAndPhpTypesOnAParameter() {
        val completions = completionsAt("(ns app\\t)\n(defn f [^<caret> m] m)\n")

        assertContainsElements(completions, "map", "vector", "set", "list", "keyword", "symbol", "atom")
        assertContainsElements(completions, "int", "string", "float", "bool", "array", "null")
    }

    fun testOffersNoFunctionsOrLocals() {
        val completions = completionsAt("(ns app\\t)\n(defn f [y] (let [^<caret> m 1] m))\n")

        assertDoesntContain(completions, "filter", "conj", "defn", "y", "f")
    }

    fun testOffersImportedClasses() {
        val completions = completionsAt("(ns app\\t (:use DateTime))\n(defn f [^<caret> d] d)\n")

        assertContainsElements(completions, "DateTime", "map")
    }

    fun testOffersTagsWithNothingAfterTheTag() {
        val completions = completionsAt("(ns app\\t)\n(defn f [^<caret>])\n")

        assertContainsElements(completions, "map", "int")
    }

    fun testOffersTagsForADefinitionReturnTag() {
        val completions = completionsAt("(ns app\\t)\n(defn f ^<caret> [x] x)\n")

        assertContainsElements(completions, "map", "int")
    }

    fun testCompletesATypedPrefix() {
        assertEquals("(ns app\\t)\n(defn f [^vector v] v)\n", completedText("(ns app\\t)\n(defn f [^vec<caret> v] v)\n"))
    }

    fun testCompletesANullableTag() {
        assertEquals("(ns app\\t)\n(defn f [^?vector v] v)\n", completedText("(ns app\\t)\n(defn f [^?vec<caret> v] v)\n"))
    }

    fun testCompletesTheLastMemberOfAUnion() {
        assertEquals("(ns app\\t)\n(defn f [^map|null m] m)\n", completedText("(ns app\\t)\n(defn f [^map|nu<caret> m] m)\n"))
    }

    fun testCompletionOutsideATagIsUnchanged() {
        val completions = completionsAt("(ns app\\t)\n(defn f [xs] (fil<caret> odd? xs))\n")

        assertContainsElements(completions, "filter")
    }
}
