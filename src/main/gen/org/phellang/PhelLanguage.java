package org.phellang;

import com.intellij.lang.Language;

public class PhelLanguage extends Language {
    public static final PhelLanguage INSTANCE = new PhelLanguage();

    private PhelLanguage() {
        super("Phel");
    }
}
