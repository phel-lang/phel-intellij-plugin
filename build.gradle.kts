import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginProjectConfigurationTask

group = "org.phellang"
version = "0.5.3"

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("org.jetbrains.intellij.platform") version "2.18.1"
    id("org.jetbrains.grammarkit") version "2023.3.0.3"
}

tasks.withType<Wrapper> {
    gradleVersion = "9.3.0"
}

System.setProperty("org.gradle.internal.deprecation.disable", "true")

gradle.settingsEvaluated {
    System.setProperty("org.gradle.internal.deprecation.disable", "true")
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
        kotlin {
            srcDirs("src/main/kotlin", "src/main/gen")
            // The build-time registry generator lives under src/main/kotlin for the tools tests to
            // reach, but it is compiled by the isolated `generator` source set below and must not be
            // part of the plugin's main output — otherwise it ships in the distribution jar and drags
            // gson in with it. Tests get it via the generator output (see `dependencies`).
            exclude("org/phellang/tools/**")
        }
    }
    test {
        kotlin {
            srcDirs("src/test/kotlin")
        }
    }
    // Isolated source set for the registry generator. It must compile WITHOUT the files it
    // generates, otherwise `updatePhelRegistry` cannot bootstrap a missing/stale registry/data.
    create("generator") {
        kotlin {
            srcDirs("src/main/kotlin")
            include(
                "org/phellang/tools/**",
                // The only registry type the generator references.
                "org/phellang/registry/PhelCompletionPriority.kt",
            )
        }
    }
}

// Configure IntelliJ Platform Dependencies
dependencies {
    intellijPlatform {
        intellijIdea("2025.2.6") {
            type.set(org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaCommunity)
        }
        bundledPlugin("com.intellij.java")

        pluginVerifier()
        zipSigner()
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.1.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.1.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:6.1.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    // gson is used only by the build-time generator under org/phellang/tools/**, never by runtime
    // plugin code (ArchitectureBoundaryTest enforces this), so it is not a plugin `implementation`
    // dependency and never ships in the distribution. The generator source set and the tools tests
    // each get their own copy; the tests also read the generator's compiled output for the tools
    // classes, which the main source set deliberately excludes.
    "generatorImplementation"("com.google.code.gson:gson:2.14.0")
    // The main source set receives the Kotlin stdlib transitively from the IntelliJ Platform, but
    // the isolated generator source set does not depend on the platform. With the auto-added stdlib
    // now disabled (kotlin.stdlib.default.dependency=false, see gradle.properties) it must declare
    // its own — it is a plain JVM program, not plugin code that ships.
    "generatorImplementation"(kotlin("stdlib"))
    testImplementation("com.google.code.gson:gson:2.14.0")
    testImplementation(sourceSets["generator"].output)
}

configurations.all {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    jvmToolchain(21)
}

val generatePhelLexer = tasks.register<GenerateLexerTask>("generatePhelLexer") {
    sourceFile.set(file("src/main/kotlin/org/phellang/language/Phel.flex"))
    targetOutputDir.set(file("src/main/gen/org/phellang/language/"))
    purgeOldFiles.set(true)
}

val generatePhelParser = tasks.register<GenerateParserTask>("generatePhelParser") {
    sourceFile.set(file("src/main/kotlin/org/phellang/language/parser/Phel.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("org/phellang/language/parser/PhelParser.java")
    pathToPsiRoot.set("org/phellang/language/psi")
    purgeOldFiles.set(true)

    // Suppress warnings from IntelliJ's internal libraries
    jvmArgs = listOf(
        "--add-opens=java.base/sun.misc=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.desktop/java.awt=ALL-UNNAMED",
        "-Dfile.encoding=UTF-8",
        "-Djava.awt.headless=true"
    )
}

// Task to update PhelFunctionRegistry from the official Phel API
val updatePhelRegistry = tasks.register<JavaExec>("updatePhelRegistry") {
    group = "tools"
    description = "Fetches Phel API from phel-lang.org and regenerates PhelFunctionRegistry files"
    mainClass.set("org.phellang.tools.ApiGeneratorKt")
    // Deliberately NOT sourceSets["main"]: the main source set does not compile until the files
    // this task generates exist, so depending on it would make a missing registry/data unrecoverable.
    classpath = sourceSets["generator"].runtimeClasspath
    workingDir = projectDir

    dependsOn("generatorClasses")
}

// The generator source set is intentionally free of lexer/parser (and PSI) dependencies, so its
// compile tasks must not be wired to grammar generation.
val generatorCompileTasks = sourceSets["generator"].let {
    setOf(it.compileJavaTaskName, it.getCompileTaskName("kotlin"))
}

// Mute only the two informational items that reflect this plugin's deliberate, documented
// compatibility policy, so every *other* configuration issue still surfaces — the CI gate
// (.github/workflows/build.yml) fails the build when any unmuted issue is reported. Keep this list
// as narrow as possible; do not mute real problems.
//
//  - since-build (243) is intentionally lower than the platform we build against (252): that is how
//    the plugin declares support for older IDEs, and the IntelliJ Plugin Verifier exercises the
//    whole range for real.
//  - until-build (262.*) is a deliberate upper bound (compat 2024.3 — 2026.2.x). The verifier
//    suggests dropping it for open-ended forward compatibility on 243+; whether to do so is a
//    separate forward-compat policy decision, not part of this change.
tasks.named<VerifyPluginProjectConfigurationTask>("verifyPluginProjectConfiguration") {
    mutedMessages.addAll(
        "since-build is lower than target platform version",
        "until-build property should be removed",
    )
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        options.release.set(21)
        if (name !in generatorCompileTasks) {
            dependsOn(generatePhelLexer, generatePhelParser)
        }
    }

    // Configure Kotlin compilation for main sources
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
            // languageVersion must be >= the Kotlin the target platform bundles (252 -> 2.1);
            // apiVersion must be <= the Kotlin API the minimum supported IDE (since-build 243 =
            // IDEA 2024.3) guarantees, which is 2.0. The Kotlin 2.4 compiler no longer accepts
            // apiVersion 1.9, so 2.0 is also the floor — which is why since-build cannot stay at 242
            // (2024.2 only ships Kotlin API 1.9). languageVersion 2.1 is deprecation-flagged by the
            // 2.4 compiler; it stays until the minimum platform bundles >= 2.2 (since-build >= 253).
            languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1
            apiVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
        }
        if (name !in generatorCompileTasks) {
            dependsOn(generatePhelLexer, generatePhelParser)
        }
    }

    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter", "junit-vintage")
        }
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = false
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }

        // Increase memory for tests
        minHeapSize = "512m"
        maxHeapSize = "2048m"

        systemProperty("idea.log.debug.categories", "")
        systemProperty("idea.suppress.layout.warnings", "true")
        systemProperty("idea.is.unit.test", "true")
        systemProperty("idea.force.use.core.classloader", "true")

        // Disable CDS archive for tests to avoid classloader conflicts
        jvmArgs(
            "-Didea.log.debug.categories=",
            "-Didea.suppress.layout.warnings=true",
            "-Didea.is.unit.test=true",
            "-Didea.force.use.core.classloader=true",
            "-Xshare:off" // Disable CDS to avoid classloader conflicts
        )
    }

    intellijPlatform {
        buildSearchableOptions {
            enabled = false
        }

        pluginVerification {
            ides {
                create("IC", "2024.3")
                create("IC", "2025.1")
                create("IC", "2025.2")
                create("IC", "2025.2.6")
            }
        }

        patchPluginXml {
            sinceBuild.set("243")
            untilBuild.set("262.*")
        }

        signPlugin {
            certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
            privateKey.set(System.getenv("PRIVATE_KEY"))
            password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
        }

        publishPlugin {
            token.set(System.getenv("PUBLISH_TOKEN"))
        }
    }
}
