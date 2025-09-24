// This is a generated file. Not intended for manual editing.
package org.phellang.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.phellang.language.psi.PhelTypes.*;
import org.phellang.language.psi.PhelFormMixin;
import org.phellang.language.psi.*;

public class PhelFormImpl extends PhelFormMixin implements PhelForm {

  public PhelFormImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PhelVisitor visitor) {
    visitor.visitForm(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PhelVisitor) accept((PhelVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PhelFormCommentMacro getFormCommentMacro() {
    return findChildByClass(PhelFormCommentMacro.class);
  }

  @Override
  @Nullable
  public PhelSet getSet() {
    return findChildByClass(PhelSet.class);
  }

  @Override
  @Nullable
  public PhelShortFn getShortFn() {
    return findChildByClass(PhelShortFn.class);
  }

  @Override
  @Nullable
  public PhelSymbol getSymbol() {
    return findChildByClass(PhelSymbol.class);
  }

  @Override
  @NotNull
  public List<PhelMetadata> getMetas() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PhelMetadata.class);
  }

  @Override
  @NotNull
  public List<PhelReaderMacro> getReaderMacros() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PhelReaderMacro.class);
  }

}
