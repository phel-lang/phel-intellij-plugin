package org.phellang.language.psi.utils

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker

/**
 * Caching a derived value on a PSI element, invalidated by any PSI edit.
 *
 * Every analyzer that memoizes a per-file or per-form scan needs the same four-line incantation:
 * `CachedValuesManager.getCachedValue(holder, key) { CachedValueProvider.Result.create(value,
 * PsiModificationTracker.MODIFICATION_COUNT) }`. Spelling it out at each site buried the one thing
 * that actually varies (what is computed) under the dependency plumbing, and made the choice of
 * dependency look like a per-site decision rather than the single project-wide rule it is.
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
        CachedValueProvider.Result.create(compute(), PsiModificationTracker.MODIFICATION_COUNT)
    }
