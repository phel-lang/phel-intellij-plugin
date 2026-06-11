Add a new code inspection to the Phel plugin.

Ask the user for:
1. What should the inspection detect?
2. What severity level? (error, warning, info)
3. Should it provide a quick-fix?

Then:
1. Read existing inspections in `src/main/kotlin/org/phellang/inspection/`
2. Create the new inspection class following existing patterns
3. Register it in `src/main/resources/META-INF/plugin.xml`
4. Add unit and/or integration tests
5. Run `./gradlew test` to verify
