package org.phellang.tools.model

/**
 * One native PHP function, extracted from its php/doc-en DocBook entry.
 *
 * @property name the bare PHP function name (e.g. `str_decrement`)
 * @property summary the plain-text `<refpurpose>`
 * @property signature a Phel-style call form (e.g. `(php/substr string offset [length])`)
 * @property extension the doc-en extension directory the function belongs to (e.g. `strings`)
 */
data class PhpFunctionDoc(
    val name: String,
    val summary: String,
    val signature: String,
    val extension: String,
)
