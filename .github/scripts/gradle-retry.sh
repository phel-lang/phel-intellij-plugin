#!/usr/bin/env bash
#
# Runs Gradle, retrying only the known IntelliJ Platform Gradle Plugin race.
#
# IdeLayoutIndexService parses plugin.xml out of the transformed IDE jars to build its
# bundled-plugin index. The zip filesystem backing those jars is sometimes closed while it is
# still reading, which surfaces as:
#
#   Unable to read descriptor [plugin.xml] from [.../ideaIC-<version>/plugins/java/lib/java-impl.jar]
#   java.nio.file.ClosedFileSystemException
#   Following 1 plugins could not be created: plugins/java
#
# The index then comes back incomplete and the build fails with the misleading
#
#   Could not find bundled plugin with ID: 'com.intellij.java'
#
# Upstream: https://github.com/JetBrains/intellij-platform-gradle-plugin/issues/2192
# (open; JetBrains confirmed it on their own pipeline and their guidance is to re-run).
# It is a race, so it hits whichever configuration resolves first, which is why it has shown up
# as ':intellijPlatformRuntimeClasspath' in one job and ':intellijPlatformTestRuntimeClasspath'
# in another. Delete this script and call ./gradlew directly once the upstream fix ships.
#
# Only this signature is retried. Any other failure, a real test or compile error, fails
# immediately so genuine breakage is never masked.

set -uo pipefail

MAX_ATTEMPTS="${GRADLE_RETRY_ATTEMPTS:-3}"
RACE_SIGNATURE='ClosedFileSystemException|Could not find bundled plugin with ID'

log="$(mktemp)"
trap 'rm -f "$log"' EXIT

for attempt in $(seq 1 "$MAX_ATTEMPTS"); do
    if [ "$attempt" -gt 1 ]; then
        echo "::group::Gradle attempt ${attempt}/${MAX_ATTEMPTS}"
    fi

    ./gradlew "$@" 2>&1 | tee "$log"
    status="${PIPESTATUS[0]}"

    if [ "$attempt" -gt 1 ]; then
        echo "::endgroup::"
    fi

    if [ "$status" -eq 0 ]; then
        exit 0
    fi

    if ! grep -qE "$RACE_SIGNATURE" "$log"; then
        echo "::error::Gradle failed for a reason unrelated to intellij-platform-gradle-plugin#2192. Not retrying."
        exit "$status"
    fi

    echo "::warning::Attempt ${attempt}/${MAX_ATTEMPTS} hit the bundled-plugin race (intellij-platform-gradle-plugin#2192)."
done

echo "::error::Still hitting the bundled-plugin race after ${MAX_ATTEMPTS} attempts."
exit 1
