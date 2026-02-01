import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

group = "org.phellang"
version = "0.3.0"

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.11.0"
    id("org.jetbrains.grammarkit") version "2023.3.0.1"
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
        }
    }
    test {
        kotlin {
            srcDirs("src/test/kotlin")
        }
    }
}

// Configure IntelliJ Platform Dependencies
dependencies {
    intellijPlatform {
        intellijIdea("2025.1") {
            type.set(org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaCommunity)
        }
        bundledPlugin("com.intellij.java")

        pluginVerifier()
        zipSigner()
    }

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:6.0.2")
    testImplementation("org.mockito:mockito-core:5.21.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.21.0")
    implementation("com.google.code.gson:gson:2.13.2")
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
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = projectDir

    // Ensure the project is compiled first
    dependsOn("classes")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        options.release.set(21)
        dependsOn(generatePhelLexer, generatePhelParser)
    }

    // Configure Kotlin compilation for main sources
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
            languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1
            apiVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1
        }
        dependsOn(generatePhelLexer, generatePhelParser)
    }

    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
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
            "-Xshare:off"  // Disable CDS to avoid classloader conflicts
        )
    }

    intellijPlatform {
        buildSearchableOptions {
            enabled = false
        }

        pluginVerification {
            ides {
                create("IC", "2024.2.5")
                create("IC", "2024.3.1")
                create("IC", "2025.1")
            }
        }

        patchPluginXml {
            sinceBuild.set("242")
            untilBuild.set("261.*")
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
