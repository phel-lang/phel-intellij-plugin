// This is a generated file. Not intended for manual editing.
package org.phellang.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;

public class PhelVisitor extends PsiElementVisitor {

  public void visitAccess(@NotNull PhelAccess o) {
    visitSForm(o);
  }

  public void visitForm(@NotNull PhelForm o) {
    visitPsiElement(o);
  }

  public void visitFormCommentMacro(@NotNull PhelFormCommentMacro o) {
    visitPsiElement(o);
  }

  public void visitKeyword(@NotNull PhelKeyword o) {
    visitSForm(o);
  }

  public void visitLVForm(@NotNull PhelLVForm o) {
    visitPForm(o);
  }

  public void visitList(@NotNull PhelList o) {
    visitLVForm(o);
  }

  public void visitLiteral(@NotNull PhelLiteral o) {
    visitSForm(o);
  }

  public void visitMap(@NotNull PhelMap o) {
    visitPForm(o);
  }

  public void visitMetadata(@NotNull PhelMetadata o) {
    visitPsiElement(o);
  }

  public void visitPForm(@NotNull PhelPForm o) {
    visitForm(o);
  }

  public void visitReaderMacro(@NotNull PhelReaderMacro o) {
    visitPsiElement(o);
  }

  public void visitSForm(@NotNull PhelSForm o) {
    visitForm(o);
  }

  public void visitSymbol(@NotNull PhelSymbol o) {
    visitPsiNameIdentifierOwner(o);
  }

  public void visitVec(@NotNull PhelVec o) {
    visitLVForm(o);
  }

  public void visitPsiNameIdentifierOwner(@NotNull PsiNameIdentifierOwner o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
