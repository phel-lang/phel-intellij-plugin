package org.phellang.unit.language.psi

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.phellang.language.psi.PhelDefinitionKind
import org.phellang.language.psi.PhelSpecialForms
import org.phellang.registry.SymbolType

/**
 * Pins every consumer of "what declares a name" to one source of truth.
 *
 * Five copies of this fact had drifted apart, each with different membership: navigation resolved
 * forms the folding placeholder ignored, which the structure view had no noun for, which the symbol
 * index never stored. The copies that *can* derive from [PhelSpecialForms.DEFINITION_FORMS] now do.
 * The two that cannot are pinned here instead:
 *
 * * [SymbolType] lives in `registry`, the bottom leaf, which may import nothing else under
 *   `org.phellang` — deriving it would break the boundary the architecture test enforces.
 * * [PhelDefinitionKind] carries a display noun per kind, so a new form needs a human decision
 *   rather than a set operation.
 *
 * Both are allowed to be narrower than the canonical set, but only deliberately: an exclusion has to
 * be named below, which is the point at which someone has to justify it.
 */
class PhelDefinitionFormCoverageTest {

    /** Forward references and test entry points are declarations, but not indexable symbols. */
    private val notIndexed = setOf("declare", "deftest")

    @Test
    fun `every definition form has a symbol type or a stated reason not to`() {
        val missing = PhelSpecialForms.DEFINITION_FORMS - SymbolType.definingKeywords - notIndexed

        assertTrue(
            missing.isEmpty(),
            "These forms declare a name but are not indexed: $missing. Add them to SymbolType, or " +
                    "to this test's `notIndexed` set with a reason.",
        )
    }

    @Test
    fun `no symbol type claims a keyword the canonical set does not know`() {
        val unknown = SymbolType.definingKeywords - PhelSpecialForms.DEFINITION_FORMS

        assertTrue(unknown.isEmpty(), "SymbolType indexes forms absent from DEFINITION_FORMS: $unknown")
    }

    @Test
    fun `every definition form has a noun for the structure view and find usages`() {
        val missing = PhelSpecialForms.DEFINITION_FORMS - PhelDefinitionKind.coveredKeywords

        assertTrue(
            missing.isEmpty(),
            "These forms would render as the anonymous \"symbol\": $missing. Give each a " +
                    "PhelDefinitionKind noun.",
        )
    }

    @Test
    fun `no definition kind claims a keyword the canonical set does not know`() {
        val unknown = PhelDefinitionKind.coveredKeywords - PhelSpecialForms.DEFINITION_FORMS

        assertTrue(unknown.isEmpty(), "PhelDefinitionKind names forms absent from DEFINITION_FORMS: $unknown")
    }

    @Test
    fun `the canonical definition set is the name-declaring set without ns`() {
        assertEquals(PhelSpecialForms.NAME_DECLARING - "ns", PhelSpecialForms.DEFINITION_FORMS)

        assertTrue("ns" in PhelSpecialForms.NAME_DECLARING, "`ns` still declares a name")
        assertTrue("ns" !in PhelSpecialForms.DEFINITION_FORMS, "`ns` names a namespace, not a symbol")
    }

    @Test
    fun `a primary keyword is one the type actually claims`() {
        for (type in SymbolType.entries) {
            assertTrue(
                type.primaryKeyword in type.keywords,
                "${type.name}'s primary keyword `${type.primaryKeyword}` is not among its keywords",
            )
        }
    }
}
