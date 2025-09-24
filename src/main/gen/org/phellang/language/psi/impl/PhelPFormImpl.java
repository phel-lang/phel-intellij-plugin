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

public class PhelPFormImpl extends PhelFormImpl implements PhelPForm {

  public PhelPFormImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PhelVisitor visitor) {
    visitor.visitPForm(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PhelVisitor) accept((PhelVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PhelForm> getForms() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PhelForm.class);
  }

}
