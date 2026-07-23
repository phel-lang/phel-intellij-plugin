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

    # The incomplete index survives in two places, and a retry has to clear both.
    #
    # IdeLayoutIndexService is a shared build service, so the daemon replays it in-process:
    # without the stop, retries failed in ~1s having never re-read the jars.
    ./gradlew --stop >/dev/null 2>&1 || true
    #
    # It also survives on disk. With only the daemon stopped, retries still failed in ~10s on a
    # provably fresh daemon ("1 stopped Daemon could not be reused") and without the originating
    # ClosedFileSystemException, while re-running the whole job on a clean runner has always
    # worked. So the interrupted read leaves the extracted IDE unusable, and it has to be
    # re-extracted. The archive stays in modules-2, so this costs extraction, not a download.
    shopt -s nullglob
    stale=(~/.gradle/caches/*/transforms ~/.gradle/caches/transforms-*)
    if [ ${#stale[@]} -gt 0 ]; then
        echo "Re-extracting the IDE: dropping ${stale[*]}"
        rm -rf "${stale[@]}"
    fi
done

echo "::error::Still hitting the bundled-plugin race after ${MAX_ATTEMPTS} attempts."
exit 1
