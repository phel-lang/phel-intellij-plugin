// This is a generated file. Not intended for manual editing.
package org.phellang.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PhelForm extends PsiElement {

  @Nullable
  PhelFormCommentMacro getFormCommentMacro();

  @Nullable
  PhelHashFn getHashFn();

  @Nullable
  PhelReaderConditional getReaderConditional();

  @Nullable
  PhelReaderConditionalSplice getReaderConditionalSplice();

  @Nullable
  PhelSet getSet();

  @Nullable
  PhelShortFn getShortFn();

  @Nullable
  PhelSymbol getSymbol();

  @NotNull
  List<PhelMetadata> getMetas();

  @NotNull
  List<PhelReaderMacro> getReaderMacros();

}
