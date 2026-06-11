Add a new completion feature to the plugin.

Ask the user for:
1. What kind of completion? (template, namespace function, PHP interop, local symbol)
2. The trigger text and what it should expand to
3. Any documentation to show on hover

Then:
1. Read the existing completion infrastructure in `src/main/kotlin/org/phellang/completion/`
2. Add the new completion following existing patterns
3. Register it in `plugin.xml` if needed
4. Add a test for the new completion
5. Run `./gradlew test` to verify
