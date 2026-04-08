Run full verification of the project: generate, compile, test, and build.

Steps:
1. Run `./gradlew generatePhelLexer generatePhelParser` to ensure generated code is up to date
2. Run `./gradlew compileKotlin compileJava` to verify compilation
3. Run `./gradlew test` and report results
4. Run `./gradlew buildPlugin` to verify the plugin packages correctly
5. Report any failures with details
