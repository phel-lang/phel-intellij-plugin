// This is a generated file. Not intended for manual editing.
package org.phellang.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.phellang.language.psi.PhelTypes.*;
import org.phellang.language.psi.PhelMetadataMixin;
import org.phellang.language.psi.*;

public class PhelMetadataImpl extends PhelMetadataMixin implements PhelMetadata {

  public PhelMetadataImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PhelVisitor visitor) {
    visitor.visitMetadata(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PhelVisitor) accept((PhelVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PhelKeyword getKeyword() {
    return findChildByClass(PhelKeyword.class);
  }

  @Override
  @Nullable
  public PhelMap getMap() {
    return findChildByClass(PhelMap.class);
  }

  @Override
  @Nullable
  public PhelSymbol getSymbol() {
    return findChildByClass(PhelSymbol.class);
  }

}
