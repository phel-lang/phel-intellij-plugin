package org.phellang.unit.completion.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.phellang.registry.CompletionInfo
import org.phellang.registry.DocumentationInfo
import org.phellang.registry.PhelFunction
import org.phellang.registry.PhelCompletionPriority
import org.phellang.completion.infrastructure.PhelRegistryCompletionHelper

class PhelRegistryCompletionHelperTest {

    @Test
    fun `aliased namespace is rendered with the alias`() {
        val transformed = PhelRegistryCompletionHelper.transformNameWithAlias(
            createTestFunction("str", "str/join"), mapOf("s" to "str")
        )

        assertEquals("s/join", transformed)
    }

    @Test
    fun `namespace without an alias keeps its full name`() {
        val transformed = PhelRegistryCompletionHelper.transformNameWithAlias(
            createTestFunction("json", "json/encode"), mapOf("s" to "str")
        )

        assertEquals("json/encode", transformed)
    }

    @Test
    fun `empty alias map leaves every name untouched`() {
        val transformed = PhelRegistryCompletionHelper.transformNameWithAlias(
            createTestFunction("str", "str/join"), emptyMap()
        )

        assertEquals("str/join", transformed)
    }

    @Test
    fun `each namespace resolves to its own alias`() {
        val aliasMap = mapOf("s" to "str", "j" to "json", "h" to "http")

        assertEquals(
            "s/join",
            PhelRegistryCompletionHelper.transformNameWithAlias(createTestFunction("str", "str/join"), aliasMap)
        )
        assertEquals(
            "j/encode",
            PhelRegistryCompletionHelper.transformNameWithAlias(createTestFunction("json", "json/encode"), aliasMap)
        )
        assertEquals(
            "h/request",
            PhelRegistryCompletionHelper.transformNameWithAlias(createTestFunction("http", "http/request"), aliasMap)
        )
    }

    @Test
    fun `an unqualified name is returned as-is when its namespace is aliased`() {
        val transformed = PhelRegistryCompletionHelper.transformNameWithAlias(
            createTestFunction("core", "map"), mapOf("c" to "core")
        )

        assertEquals("c/map", transformed)
    }

    private fun createTestFunction(namespace: String, name: String): PhelFunction {
        return PhelFunction(
            namespace = namespace, name = name, signature = "($name ...)", completion = CompletionInfo(
                tailText = "Test function", priority = PhelCompletionPriority.CORE_FUNCTIONS
            ), documentation = DocumentationInfo(
                summary = "Test function documentation"
            )
        )
    }
}
