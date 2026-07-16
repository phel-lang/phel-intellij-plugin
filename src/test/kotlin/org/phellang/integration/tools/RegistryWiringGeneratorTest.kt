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
            package org.phellang.registry

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
            package org.phellang.registry

            // region GENERATED IMPORTS — updatePhelRegistry; do not edit by hand
            // endregion GENERATED IMPORTS — updatePhelRegistry
            import org.phellang.registry.PhelCompletionPriority

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
    fun `wires registry init and imports every generated namespace`() {
        writeNamespaceStub()
        writeRegistryStub()

        RegistryWiringGenerator(tempDir).generate()

        val registryText = File(tempDir, "PhelFunctionRegistry.kt").readText()
        assertTrue(registryText.contains("functions[Namespace.CORE] = registerCoreFunctions()"))

        // The generated payload lives in `registry/data`, below the model package, so the registry
        // needs an import for *every* namespace — including flat (non-subfolder) ones.
        assertTrue(
            registryText.contains("import org.phellang.registry.data.registerCoreFunctions"),
            "a flat namespace needs an import from the data package"
        )
        assertTrue(
            registryText.contains("import org.phellang.registry.data.schema.registerSchemaFunctions"),
            "a subfolder namespace needs an import nested under the data package"
        )
        assertTrue(
            registryText.contains("import org.phellang.registry.PhelCompletionPriority"),
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
