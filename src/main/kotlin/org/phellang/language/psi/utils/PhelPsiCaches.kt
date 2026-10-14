package org.phellang.language.psi.utils

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager

/**
 * Caching a derived value on a PSI element, invalidated when that element's file changes.
 *
 * Every analyzer that memoizes a per-file or per-form scan needs the same incantation; spelling it
 * out at each site buried the one thing that actually varies (what is computed) under the
 * dependency plumbing, and made the choice of dependency look like a per-site decision rather than
 * the single rule it is.
 *
 * The rule: a value depends on its holder's own file and nothing else. The platform stamps a PSI
 * dependency with its containing file's modification stamp, which moves only when that file's PSI
 * changes. It used to be the global `PsiModificationTracker.MODIFICATION_COUNT`, so a keystroke in
 * any file threw away every other file's cached `ns` form, alias map and definitions, and the next
 * project-wide lookup rebuilt them all. That was never needed: every value cached through here is
 * computed from the holder's own file (plus static registry data and pure string helpers).
 * **Keep it that way.** A value that reads another file, the project index or the VFS must not use
 * this; give it its own dependencies, as [org.phellang.language.psi.PhelVendorUtils] does.
 *
 * The cached data is softly referenced by the platform, so a value holding PSI does not pin the
 * file's tree in memory.
 *
 * Lives in `language/psi/utils` rather than `core`: `core` already depends on `language`, so putting
 * it the other way round would add an edge back.
 *
 * Only the element-scoped overload is offered. `PhelProjectNamespaceFinder` caches on the *project*
 * via `CachedValuesManager.getManager(project)` with a different `trackValue` argument; folding that
 * in would mean a flag parameter, so it deliberately keeps its own call.
 */
internal fun <T> cachedPerPsi(holder: PsiElement, key: Key<CachedValue<T>>, compute: () -> T): T =
    CachedValuesManager.getCachedValue(holder, key) {
        CachedValueProvider.Result.create(compute(), holder)
    }
