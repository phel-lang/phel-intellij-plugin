#!/usr/bin/env bash
#
# Runs Gradle, retrying only known-transient CI failures. Two classes are retried; everything
# else (a real test or compile error) fails immediately so genuine breakage is never masked.
#
# 1. The IntelliJ Platform Gradle Plugin bundled-plugin race.
#    IdeLayoutIndexService parses plugin.xml out of the transformed IDE jars to build its
#    bundled-plugin index. The zip filesystem backing those jars is sometimes closed while it is
#    still reading, which surfaces as:
#
#      Unable to read descriptor [plugin.xml] from [.../ideaIC-<version>/plugins/java/lib/java-impl.jar]
#      java.nio.file.ClosedFileSystemException
#      Following 1 plugins could not be created: plugins/java
#
#    The index then comes back incomplete and the build fails with the misleading
#
#      Could not find bundled plugin with ID: 'com.intellij.java'
#
#    Upstream: https://github.com/JetBrains/intellij-platform-gradle-plugin/issues/2192
#    (open; JetBrains confirmed it on their own pipeline and their guidance is to re-run). It is a
#    race, so it hits whichever configuration resolves first. Clearing the transforms cache between
#    attempts is what makes the retry stick (see below). Delete this handling once #2192 ships a fix.
#
# 2. Transient network failures fetching the platform or Plugin Verifier IDEs, e.g.
#
#      Could not resolve idea:ideaIC:2024.3
#      Read timed out / Connection reset / Premature end of Content-Length
#
#    These just need another attempt; no cache surgery. A genuinely wrong IDE coordinate still fails
#    (it simply exhausts the attempts), so real misconfiguration is not masked, only slowed.

set -uo pipefail

MAX_ATTEMPTS="${GRADLE_RETRY_ATTEMPTS:-5}"

# The bundled-plugin race — needs the half-extracted IDE dropped, not just a re-run.
RACE_SIGNATURE='ClosedFileSystemException|Could not find bundled plugin with ID'
# Transient dependency resolution / download failures — a plain re-run clears them.
NETWORK_SIGNATURE='Could not resolve idea:ideaIC|Read timed out|Connection (reset|timed out)|Premature end of Content-Length|Could not (GET|HEAD|transfer)|Gateway Time-?out|(502|503|504) '

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

    hit_race=false; grep -qE "$RACE_SIGNATURE" "$log" && hit_race=true
    hit_network=false; grep -qE "$NETWORK_SIGNATURE" "$log" && hit_network=true

    if [ "$hit_race" = false ] && [ "$hit_network" = false ]; then
        echo "::error::Gradle failed for a reason unrelated to a known transient CI flake. Not retrying."
        exit "$status"
    fi

    if [ "$attempt" -eq "$MAX_ATTEMPTS" ]; then
        break
    fi

    if [ "$hit_race" = true ]; then
        echo "::warning::Attempt ${attempt}/${MAX_ATTEMPTS} hit the bundled-plugin race (intellij-platform-gradle-plugin#2192)."
    else
        echo "::warning::Attempt ${attempt}/${MAX_ATTEMPTS} hit a transient network/resolution failure."
    fi

    # The shared IdeLayoutIndexService is replayed in-process, so stop the daemon: without this,
    # retries returned in ~1s having never re-read the jars.
    ./gradlew --stop >/dev/null 2>&1 || true

    # The race also survives on disk as a half-extracted IDE. With only the daemon stopped, retries
    # still failed on a provably fresh daemon without the originating ClosedFileSystemException,
    # while re-running the whole job on a clean runner always worked. So the interrupted read leaves
    # the extracted IDE unusable and it has to be re-extracted. The archive stays in modules-2, so
    # this costs extraction, not a download. A pure network failure does not need this.
    if [ "$hit_race" = true ]; then
        shopt -s nullglob
        stale=(~/.gradle/caches/*/transforms ~/.gradle/caches/transforms-*)
        if [ ${#stale[@]} -gt 0 ]; then
            echo "Re-extracting the IDE: dropping ${stale[*]}"
            rm -rf "${stale[@]}"
        fi
    fi

    # Back off before retrying; network wobbles in particular benefit from a short pause.
    sleep $((attempt * 5))
done

echo "::error::Still failing with a transient signature after ${MAX_ATTEMPTS} attempts."
exit 1
