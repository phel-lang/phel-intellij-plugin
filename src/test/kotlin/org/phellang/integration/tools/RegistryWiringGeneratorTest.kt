package org.phellang.integration.tools

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.phellang.tools.generator.NamespaceConfig
import org.phellang.tools.generator.RegistryWiringGenerator
import java.io.File
import java.nio.file.Files

class RegistryWiringGeneratorTest {

    private lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        tempDir = Files.createTempDirectory("wiring-test").toFile()
    }

    @AfterEach
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    private fun writeNamespaceStub() {
        File(tempDir, "Namespace.kt").writeText(
            """
            package org.phellang.completion.data

            enum class Namespace {
                // region GENERATED — updatePhelRegistry; do not edit by hand
                PLACEHOLDER,
                // endregion GENERATED — updatePhelRegistry
                ;

                companion object {
                    private val ALIASES = emptyMap<String, Namespace>()
                }
            }
            """.trimIndent()
        )
    }

    private fun writeRegistryStub() {
        File(tempDir, "PhelFunctionRegistry.kt").writeText(
            """
            package org.phellang.completion.data

            // region GENERATED IMPORTS — updatePhelRegistry; do not edit by hand
            // endregion GENERATED IMPORTS — updatePhelRegistry
            import org.phellang.completion.infrastructure.PhelCompletionPriority

            object PhelFunctionRegistry {
                init {
                    // region GENERATED INIT — updatePhelRegistry; do not edit by hand
                    // endregion GENERATED INIT — updatePhelRegistry
                }
            }
            """.trimIndent()
        )
    }

    @Test
    fun `wires every configured namespace into the enum`() {
        writeNamespaceStub()
        writeRegistryStub()

        RegistryWiringGenerator(tempDir).generate()

        val enumText = File(tempDir, "Namespace.kt").readText()
        // php config key maps to the PHP_INTEROP enum constant via the function name.
        assertTrue(enumText.contains("    PHP_INTEROP,"), "expected derived PHP_INTEROP constant")
        assertTrue(enumText.contains("    HTTP_CLIENT,"), "expected derived HTTP_CLIENT constant")
        assertTrue(!enumText.contains("PLACEHOLDER"), "placeholder should be replaced")
    }

    @Test
    fun `wires registry init and subfolder imports`() {
        writeNamespaceStub()
        writeRegistryStub()

        RegistryWiringGenerator(tempDir).generate()

        val registryText = File(tempDir, "PhelFunctionRegistry.kt").readText()
        assertTrue(registryText.contains("functions[Namespace.CORE] = registerCoreFunctions()"))
        assertTrue(
            registryText.contains("import org.phellang.completion.data.schema.registerSchemaFunctions"),
            "subfolder namespaces need an import"
        )
        assertTrue(
            registryText.contains("import org.phellang.completion.infrastructure.PhelCompletionPriority"),
            "non-generated imports must be preserved"
        )
    }

    @Test
    fun `is idempotent`() {
        writeNamespaceStub()
        writeRegistryStub()

        RegistryWiringGenerator(tempDir).generate()
        val firstEnum = File(tempDir, "Namespace.kt").readText()
        val firstRegistry = File(tempDir, "PhelFunctionRegistry.kt").readText()

        RegistryWiringGenerator(tempDir).generate()

        assertEquals(firstEnum, File(tempDir, "Namespace.kt").readText())
        assertEquals(firstRegistry, File(tempDir, "PhelFunctionRegistry.kt").readText())
    }

    @Test
    fun `wires exactly one entry per configured namespace`() {
        writeNamespaceStub()
        writeRegistryStub()

        RegistryWiringGenerator(tempDir).generate()

        val initLines = File(tempDir, "PhelFunctionRegistry.kt").readText()
            .lines()
            .filter { it.contains("functions[Namespace.") }
        assertEquals(NamespaceConfig.all().size, initLines.size)
    }
}
