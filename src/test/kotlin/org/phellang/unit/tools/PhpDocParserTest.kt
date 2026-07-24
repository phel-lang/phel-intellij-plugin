package org.phellang.unit.tools

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.phellang.tools.generator.PhpDocParser

class PhpDocParserTest {

    @Test
    fun `parses name, tag-stripped summary and a signature with an optional param`() {
        val xml = """
            <refentry>
             <refnamediv>
              <refname>substr</refname>
              <refpurpose>Return part of a <function>string</function></refpurpose>
             </refnamediv>
             <refsect1 role="description">
              &reftitle.description;
              <methodsynopsis>
               <type>string</type><methodname>substr</methodname>
               <methodparam><type>string</type><parameter>string</parameter></methodparam>
               <methodparam><type>int</type><parameter>offset</parameter></methodparam>
               <methodparam choice="opt"><type class="union"><type>int</type><type>null</type></type><parameter>length</parameter><initializer>&null;</initializer></methodparam>
              </methodsynopsis>
             </refsect1>
            </refentry>
        """.trimIndent()

        val doc = PhpDocParser.parse(xml, "strings")!!

        assertEquals("substr", doc.name)
        assertEquals("Return part of a string", doc.summary)
        assertEquals("(php/substr string offset [length])", doc.signature)
        assertEquals("strings", doc.extension)
    }

    @Test
    fun `marks a repeated param as variadic`() {
        val xml = """
            <refnamediv><refname>array_merge</refname><refpurpose>Merge one or more arrays</refpurpose></refnamediv>
            <methodsynopsis>
             <type>array</type><methodname>array_merge</methodname>
             <methodparam rep="repeat"><type>array</type><parameter>arrays</parameter></methodparam>
            </methodsynopsis>
        """.trimIndent()

        assertEquals("(php/array_merge & arrays)", PhpDocParser.parse(xml, "array")!!.signature)
    }

    @Test
    fun `resolves DocBook type entities in the summary to their bare name`() {
        val xml = """
            <refnamediv><refname>array_all</refname><refpurpose>Checks if all &array; elements satisfy a callback</refpurpose></refnamediv>
            <methodsynopsis><methodname>array_all</methodname><methodparam><parameter>array</parameter></methodparam></methodsynopsis>
        """.trimIndent()

        assertEquals("Checks if all array elements satisfy a callback", PhpDocParser.parse(xml, "array")!!.summary)
    }

    @Test
    fun `handles a function with no parameters`() {
        val xml = """
            <refnamediv><refname>time</refname><refpurpose>Return current Unix timestamp</refpurpose></refnamediv>
            <methodsynopsis><type>int</type><methodname>time</methodname></methodsynopsis>
        """.trimIndent()

        assertEquals("(php/time)", PhpDocParser.parse(xml, "datetime")!!.signature)
    }

    @Test
    fun `skips class methods (refname contains a scope resolution)`() {
        val xml = """
            <refnamediv><refname>DateTime::format</refname><refpurpose>Formats the date</refpurpose></refnamediv>
            <methodsynopsis><methodname>DateTime::format</methodname></methodsynopsis>
        """.trimIndent()

        assertNull(PhpDocParser.parse(xml, "datetime"))
    }

    @Test
    fun `returns null when there is no method synopsis to build a signature from`() {
        val xml = "<refnamediv><refname>whatever</refname><refpurpose>Some prose</refpurpose></refnamediv>"

        assertNull(PhpDocParser.parse(xml, "misc"))
    }
}
