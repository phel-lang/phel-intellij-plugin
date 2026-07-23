package org.phellang.unit.inspection

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Every `localInspection` registered in plugin.xml must ship a description, or its panel in
 * Settings → Editor → Inspections is blank and the IntelliJ Plugin Verifier flags it. The platform
 * resolves the description from `inspectionDescriptions/<shortName>.html`, so this guards that a new
 * inspection cannot land without one — a plain source scan, no platform fixture, mirroring
 * ArchitectureBoundaryTest.
 */
class InspectionDescriptionsTest {

    private val pluginXml = File("src/main/resources/META-INF/plugin.xml")
    private val descriptionsDir = File("src/main/resources/inspectionDescriptions")

    private val shortNamePattern =
        Regex("""<localInspection\b[^>]*?shortName="([^"]+)"""", RegexOption.DOT_MATCHES_ALL)

    @Test
    fun `every registered localInspection has a non-empty description file`() {
        val shortNames = shortNamePattern.findAll(pluginXml.readText()).map { it.groupValues[1] }.toList()

        assertTrue(shortNames.isNotEmpty(), "expected localInspection registrations in plugin.xml")

        val missing = shortNames.filter { shortName ->
            val file = File(descriptionsDir, "$shortName.html")
            !file.isFile || file.readText().isBlank()
        }

        assertTrue(
            missing.isEmpty(),
            "Inspections missing a description under inspectionDescriptions/: $missing. " +
                "Add <shortName>.html so the Settings panel and the plugin verifier are satisfied.",
        )
    }
}
