// This is a generated file. Not intended for manual editing.
package org.phellang.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.phellang.language.psi.PhelTypes.*;
import org.phellang.language.psi.*;

public class PhelAccessImpl extends PhelSFormImpl implements PhelAccess {

  public PhelAccessImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PhelVisitor visitor) {
    visitor.visitAccess(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PhelVisitor) accept((PhelVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PhelSymbol getSymbol() {
    return findNotNullChildByClass(PhelSymbol.class);
  }

}
