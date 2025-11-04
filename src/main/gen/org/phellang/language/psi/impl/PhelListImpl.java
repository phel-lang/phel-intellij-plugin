// This is a generated file. Not intended for manual editing.
package org.phellang.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.phellang.language.psi.PhelTypes.*;
import org.phellang.language.psi.mixins.PhelListMixin;
import org.phellang.language.psi.*;

public class PhelListImpl extends PhelListMixin implements PhelList {

  public PhelListImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PhelVisitor visitor) {
    visitor.visitList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PhelVisitor) accept((PhelVisitor)visitor);
    else super.accept(visitor);
  }

}
