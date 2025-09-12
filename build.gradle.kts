import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.0.1"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "org.phellang"
version = "0.1.1"

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
    }
}

// Configure IntelliJ Platform Dependencies
dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2.5")
        bundledPlugin("com.intellij.java")
        
        pluginVerifier()
        zipSigner()
        instrumentationTools()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

val generatePhelLexer = tasks.register<GenerateLexerTask>("generatePhelLexer") {
    sourceFile.set(file("src/main/java/org/phellang/language/Phel.flex"))
    targetOutputDir.set(file("src/main/gen/org/phellang/language/"))
    purgeOldFiles.set(true)
}

val generatePhelParser = tasks.register<GenerateParserTask>("generatePhelParser") {
    sourceFile.set(file("src/main/java/org/phellang/language/Phel.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("org/phellang/language/parser/PhelParser.java")
    pathToPsiRoot.set("org/phellang/language/psi")
    purgeOldFiles.set(true)
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        options.release.set(21)
        dependsOn(generatePhelLexer, generatePhelParser)
    }

    intellijPlatform {
        patchPluginXml {
            sinceBuild.set("242")
            untilBuild.set("242.*")
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
