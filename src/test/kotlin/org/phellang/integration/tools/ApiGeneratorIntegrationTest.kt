package org.phellang.integration.tools

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.phellang.tools.generator.KotlinCodeGenerator
import org.phellang.tools.generator.NamespaceConfig
import org.phellang.tools.model.ApiFunction
import org.phellang.tools.model.ApiFunctionMeta
import java.io.File
import java.nio.file.Files

class ApiGeneratorIntegrationTest {

    private lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        tempDir = Files.createTempDirectory("api-generator-test").toFile()
    }

    @AfterEach
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    private fun createApiFunction(
        namespace: String,
        name: String,
        description: String = "Test description",
        signatures: List<String> = listOf("(test)"),
        example: String? = null,
        deprecated: String? = null,
        supersededBy: String? = null,
        macro: Boolean? = null
    ): ApiFunction {
        return ApiFunction(
            namespace = namespace,
            name = name,
            description = description,
            doc = "Test doc",
            signatures = signatures,
            githubUrl = "https://github.com/test",
            docUrl = "https://docs.test",
            meta = ApiFunctionMeta(
                example = example, deprecated = deprecated, supersededBy = supersededBy, macro = macro
            )
        )
    }

    @Nested
    inner class FileGeneration {

        @Test
        fun `should generate file for single namespace`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction("json", "json/encode", description = "Encodes to JSON")
            )

            generator.generate(functions)

            val generatedFile = File(tempDir, "registerJsonFunctions.kt")
            assertTrue(generatedFile.exists(), "Generated file should exist")
        }

        @Test
        fun `should generate separate files for each namespace`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction("json", "json/encode"),
                createApiFunction("str", "str/join"),
                createApiFunction("html", "html/html")
            )

            generator.generate(functions)

            assertTrue(File(tempDir, "registerJsonFunctions.kt").exists())
            assertTrue(File(tempDir, "registerStringFunctions.kt").exists())
            assertTrue(File(tempDir, "registerHtmlFunctions.kt").exists())
        }

        @Test
        fun `should not generate file for unknown namespace`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction("unknown", "unknown/func")
            )

            generator.generate(functions)

            assertEquals(0, tempDir.listFiles()?.size ?: 0)
        }
    }

    @Nested
    inner class FileContent {

        @Test
        fun `should generate valid Kotlin file structure`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction("json", "json/encode", signatures = listOf("(encode value)"))
            )

            generator.generate(functions)

            val content = File(tempDir, "registerJsonFunctions.kt").readText()
            assertTrue(content.contains("package org.phellang.completion.data"))
            assertTrue(content.contains("import org.phellang.completion.infrastructure.PhelCompletionPriority"))
            assertTrue(content.contains("internal fun registerJsonFunctions(): List<PhelFunction>"))
        }

        @Test
        fun `should include function metadata`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction(
                    namespace = "json",
                    name = "json/encode",
                    description = "Encodes value to JSON string",
                    signatures = listOf("(encode value)")
                )
            )

            generator.generate(functions)

            val content = File(tempDir, "registerJsonFunctions.kt").readText()
            assertTrue(content.contains("namespace = \"json\""))
            assertTrue(content.contains("name = \"json/encode\""))
            assertTrue(content.contains("signature = \"(encode value)\""))
        }

        @Test
        fun `should include priority from PriorityRules`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction("json", "json/encode")
            )

            generator.generate(functions)

            val content = File(tempDir, "registerJsonFunctions.kt").readText()
            assertTrue(content.contains("priority = PhelCompletionPriority.JSON_FUNCTIONS"))
        }

        @Test
        fun `should escape HTML in examples`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction(
                    namespace = "html", name = "html/escape-html", example = "(escape-html \"<div>\")"
                )
            )

            generator.generate(functions)

            val content = File(tempDir, "registerHtmlFunctions.kt").readText()
            assertTrue(content.contains("&lt;div&gt;"), "HTML should be escaped in example")
        }

        @Test
        fun `should include deprecation info when present`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction(
                    namespace = "core", name = "old-func", deprecated = "1.0", supersededBy = "new-func"
                )
            )

            generator.generate(functions)

            val content = File(tempDir, "registerCoreFunctions.kt").readText()
            assertTrue(content.contains("deprecation = DeprecationInfo"))
            assertTrue(content.contains("version = \"1.0\""))
            assertTrue(content.contains("replacement = \"new-func\""))
        }

        @Test
        fun `should convert markdown to HTML in summary`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction(
                    namespace = "json",
                    name = "json/encode",
                    description = "Use `encode` to convert. See [PHP docs](https://php.net)"
                )
            )

            generator.generate(functions)

            val content = File(tempDir, "registerJsonFunctions.kt").readText()
            assertTrue(content.contains("<code>encode</code>"))
            assertTrue(content.contains("<a href=\"https://php.net\">PHP docs</a>"))
        }
    }

    @Nested
    inner class MultipleFunctionsInNamespace {

        @Test
        fun `should generate all functions in a single file`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction("json", "json/encode"),
                createApiFunction("json", "json/decode"),
                createApiFunction("json", "json/valid?")
            )

            generator.generate(functions)

            val content = File(tempDir, "registerJsonFunctions.kt").readText()
            assertTrue(content.contains("\"json/encode\""))
            assertTrue(content.contains("\"json/decode\""))
            assertTrue(content.contains("\"json/valid?\""))
        }

        @Test
        fun `should separate functions with commas`() {
            val generator = KotlinCodeGenerator(tempDir)
            val functions = listOf(
                createApiFunction("json", "json/encode"), createApiFunction("json", "json/decode")
            )

            generator.generate(functions)

            val content = File(tempDir, "registerJsonFunctions.kt").readText()
            // Count PhelFunction occurrences
            val phelFunctionCount = content.split("PhelFunction(").size - 1
            assertEquals(2, phelFunctionCount)
        }
    }

    @Nested
    inner class NamespaceConfigTest {

        @Test
        fun `should support all standard namespaces`() {
            val expectedNamespaces = listOf(
                "base64", "core", "debug", "html", "http", "json", "mock", "php", "repl", "str", "test"
            )

            expectedNamespaces.forEach { namespace ->
                assertTrue(NamespaceConfig.isSupported(namespace), "Namespace $namespace should be supported")
            }
        }

        @Test
        fun `should return correct file names`() {
            assertEquals("registerJsonFunctions.kt", NamespaceConfig.getInfo("json")?.fileName)
            assertEquals("registerCoreFunctions.kt", NamespaceConfig.getInfo("core")?.fileName)
            assertEquals("registerStringFunctions.kt", NamespaceConfig.getInfo("str")?.fileName)
        }

        @Test
        fun `should return correct function names`() {
            assertEquals("registerJsonFunctions", NamespaceConfig.getInfo("json")?.functionName)
            assertEquals("registerPhpInteropFunctions", NamespaceConfig.getInfo("php")?.functionName)
        }
    }
}
