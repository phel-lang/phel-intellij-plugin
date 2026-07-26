package org.phellang.language.infrastructure

import com.intellij.openapi.util.IconLoader.getIcon
import javax.swing.Icon

object PhelIcons {
    /**
     * SVG, so it stays sharp on a HiDPI display: the 16x16 PNG it replaces was upscaled and blurred.
     *
     * No `_dark` counterpart. The platform looks one up automatically and falls back to this when
     * there is none, which is the right outcome for a coloured brand mark — a `_dark` variant is for
     * icons drawn in theme-dependent greys, and inverting the Phel purple would make it a different
     * logo. The white stroke keeps the shape legible against a dark background.
     */
    @JvmField
    val FILE: Icon = getIcon("/icons/phel.svg", PhelIcons::class.java)
}
