Fetch the latest Phel API from https://phel-lang.org/api.json and regenerate the function registry.

Steps:
1. Run `./gradlew updatePhelRegistry`
2. Check git diff on `src/main/kotlin/org/phellang/registry/register*Functions.kt` to see what changed
3. Run `./gradlew test` to verify nothing broke
4. Summarize what functions were added, removed, or updated
