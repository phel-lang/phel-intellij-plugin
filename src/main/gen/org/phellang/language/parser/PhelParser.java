// This is a generated file. Not intended for manual editing.
package org.phellang.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.phellang.language.psi.PhelTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PhelParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, EXTENDS_SETS_);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return root(builder_, level_ + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ACCESS, FORM, KEYWORD, LIST,
      LITERAL, MAP, VEC),
  };

  /* ********************************************************** */
  // ('.' | '.-') symbol
  public static boolean access(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access")) return false;
    if (!nextTokenIs(builder_, "<access>", DOT, DOTDASH)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACCESS, "<access>");
    result_ = access_0(builder_, level_ + 1);
    result_ = result_ && symbol(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '.' | '.-'
  private static boolean access_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, DOTDASH);
    return result_;
  }

  /* ********************************************************** */
  // ! '.'
  public static boolean access_left(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "access_left")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_ | _NOT_, ACCESS, "<access left>");
    result_ = !consumeToken(builder_, DOT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // form_prefix form_prefix * form_upper | form_inner | form_comment_macro | multiline_comment | line_comment
  public static boolean form(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, FORM, "<form>");
    result_ = form_0(builder_, level_ + 1);
    if (!result_) result_ = form_inner(builder_, level_ + 1);
    if (!result_) result_ = form_comment_macro(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, MULTILINE_COMMENT);
    if (!result_) result_ = consumeToken(builder_, LINE_COMMENT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // form_prefix form_prefix * form_upper
  private static boolean form_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_0")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = form_prefix(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, form_0_1(builder_, level_ + 1));
    result_ = pinned_ && form_upper(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // form_prefix *
  private static boolean form_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!form_prefix(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "form_0_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // "#_" form
  public static boolean form_comment_macro(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_comment_macro")) return false;
    if (!nextTokenIs(builder_, FORM_COMMENT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, FORM_COMMENT);
    result_ = result_ && form(builder_, level_ + 1);
    exit_section_(builder_, marker_, FORM_COMMENT_MACRO, result_);
    return result_;
  }

  /* ********************************************************** */
  // p_forms | s_forms
  static boolean form_inner(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_inner")) return false;
    boolean result_;
    result_ = p_forms(builder_, level_ + 1);
    if (!result_) result_ = s_forms(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // metadata | reader_macro
  static boolean form_prefix(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_prefix")) return false;
    boolean result_;
    result_ = metadata(builder_, level_ + 1);
    if (!result_) result_ = reader_macro(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // form_inner
  public static boolean form_upper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "form_upper")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_ | _UPPER_, FORM, "<form>");
    result_ = form_inner(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // <<items_entry <<recover>> <<param>>>> *
  static boolean items(PsiBuilder builder_, int level_, Parser recover, Parser param) {
    if (!recursion_guard_(builder_, level_, "items")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    while (true) {
      int pos_ = current_position_(builder_);
      if (!items_entry(builder_, level_ + 1, recover, param)) break;
      if (!empty_element_parsed_guard_(builder_, "items", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, recover);
    return true;
  }

  /* ********************************************************** */
  // (not_eof <<recover>>) <<param>>
  static boolean items_entry(PsiBuilder builder_, int level_, Parser recover, Parser param) {
    if (!recursion_guard_(builder_, level_, "items_entry")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = items_entry_0(builder_, level_ + 1, recover);
    result_ = result_ && param.parse(builder_, level_);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // not_eof <<recover>>
  private static boolean items_entry_0(PsiBuilder builder_, int level_, Parser recover) {
    if (!recursion_guard_(builder_, level_, "items_entry_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = not_eof(builder_, level_ + 1);
    result_ = result_ && recover.parse(builder_, level_);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // keyword_token | (':' | '::') symbol_qualified
  public static boolean keyword(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "keyword")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, KEYWORD, "<keyword>");
    result_ = consumeToken(builder_, KEYWORD_TOKEN);
    if (!result_) result_ = keyword_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (':' | '::') symbol_qualified
  private static boolean keyword_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "keyword_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = keyword_1_0(builder_, level_ + 1);
    result_ = result_ && symbol_qualified(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ':' | '::'
  private static boolean keyword_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "keyword_1_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, COLONCOLON);
    return result_;
  }

  /* ********************************************************** */
  // '(' list_body ')'
  public static boolean list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list")) return false;
    if (!nextTokenIs(builder_, PAREN1)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LIST, null);
    result_ = consumeToken(builder_, PAREN1);
    pinned_ = result_; // pin = '[\(\[\{]'
    result_ = result_ && report_error_(builder_, list_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, PAREN2) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // <<items !')' form>>
  static boolean list_body(PsiBuilder builder_, int level_) {
    return items(builder_, level_ + 1, PhelParser::list_body_0_0, PhelParser::form);
  }

  // !')'
  private static boolean list_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_body_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, PAREN2);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // number | hexnum | binnum | octnum | bool | nil | nan | string | char
  public static boolean literal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "literal")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LITERAL, "<literal>");
    result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, HEXNUM);
    if (!result_) result_ = consumeToken(builder_, BINNUM);
    if (!result_) result_ = consumeToken(builder_, OCTNUM);
    if (!result_) result_ = consumeToken(builder_, BOOL);
    if (!result_) result_ = consumeToken(builder_, NIL);
    if (!result_) result_ = consumeToken(builder_, NAN);
    if (!result_) result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, CHAR);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '{' map_body '}'
  public static boolean map(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map")) return false;
    if (!nextTokenIs(builder_, BRACE1)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MAP, null);
    result_ = consumeToken(builder_, BRACE1);
    pinned_ = result_; // pin = '[\(\[\{]'
    result_ = result_ && report_error_(builder_, map_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, BRACE2) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // <<items !'}' map_entry>>
  static boolean map_body(PsiBuilder builder_, int level_) {
    return items(builder_, level_ + 1, PhelParser::map_body_0_0, PhelParser::map_entry);
  }

  // !'}'
  private static boolean map_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_body_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, BRACE2);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // form form
  static boolean map_entry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_entry")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = form(builder_, level_ + 1);
    result_ = result_ && form(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // "^" (string | symbol | keyword | map)
  public static boolean metadata(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "metadata")) return false;
    if (!nextTokenIs(builder_, "<form>", HAT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, METADATA, "<form>");
    result_ = consumeToken(builder_, HAT);
    result_ = result_ && metadata_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // string | symbol | keyword | map
  private static boolean metadata_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "metadata_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = symbol(builder_, level_ + 1);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = map(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // !<<eof>>
  static boolean not_eof(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_eof")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !eof(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // list | vec | map | set | short_fn
  static boolean p_forms(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "p_forms")) return false;
    boolean result_;
    result_ = list(builder_, level_ + 1);
    if (!result_) result_ = vec(builder_, level_ + 1);
    if (!result_) result_ = map(builder_, level_ + 1);
    if (!result_) result_ = set(builder_, level_ + 1);
    if (!result_) result_ = short_fn(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // "'" | "~" | "~@" | "`" | "," | ",@"
  public static boolean reader_macro(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "reader_macro")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, READER_MACRO, "<reader macro>");
    result_ = consumeToken(builder_, QUOTE);
    if (!result_) result_ = consumeToken(builder_, TILDE);
    if (!result_) result_ = consumeToken(builder_, TILDE_AT);
    if (!result_) result_ = consumeToken(builder_, SYNTAX_QUOTE);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, COMMA_AT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // form *
  static boolean root(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!form(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "root", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // symbol access_left? | keyword | literal | access
  static boolean s_forms(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "s_forms")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = s_forms_0(builder_, level_ + 1);
    if (!result_) result_ = keyword(builder_, level_ + 1);
    if (!result_) result_ = literal(builder_, level_ + 1);
    if (!result_) result_ = access(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // symbol access_left?
  private static boolean s_forms_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "s_forms_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = symbol(builder_, level_ + 1);
    result_ = result_ && s_forms_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // access_left?
  private static boolean s_forms_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "s_forms_0_1")) return false;
    access_left(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '#{' set_body '}'
  public static boolean set(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set")) return false;
    if (!nextTokenIs(builder_, HASH_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_BRACE);
    result_ = result_ && set_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, BRACE2);
    exit_section_(builder_, marker_, SET, result_);
    return result_;
  }

  /* ********************************************************** */
  // <<items !'}' form>>
  static boolean set_body(PsiBuilder builder_, int level_) {
    return items(builder_, level_ + 1, PhelParser::set_body_0_0, PhelParser::form);
  }

  // !'}'
  private static boolean set_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set_body_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, BRACE2);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // "|(" list_body ")"
  public static boolean short_fn(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "short_fn")) return false;
    if (!nextTokenIs(builder_, FN_SHORT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, FN_SHORT);
    result_ = result_ && list_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PAREN2);
    exit_section_(builder_, marker_, SHORT_FN, result_);
    return result_;
  }

  /* ********************************************************** */
  // symbol_qualified
  public static boolean symbol(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "symbol")) return false;
    if (!nextTokenIs(builder_, SYM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, SYMBOL, null);
    result_ = symbol_qualified(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '/' (sym | coloncolon | dot | dotdash | hat | tilde)
  public static boolean symbol_nsq(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "symbol_nsq")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, SYMBOL, "<symbol nsq>");
    result_ = consumeTokenFast(builder_, "/");
    result_ = result_ && symbol_nsq_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // sym | coloncolon | dot | dotdash | hat | tilde
  private static boolean symbol_nsq_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "symbol_nsq_1")) return false;
    boolean result_;
    result_ = consumeTokenFast(builder_, SYM);
    if (!result_) result_ = consumeTokenFast(builder_, COLONCOLON);
    if (!result_) result_ = consumeTokenFast(builder_, DOT);
    if (!result_) result_ = consumeTokenFast(builder_, DOTDASH);
    if (!result_) result_ = consumeTokenFast(builder_, HAT);
    if (!result_) result_ = consumeTokenFast(builder_, TILDE);
    return result_;
  }

  /* ********************************************************** */
  // sym
  public static boolean symbol_plain(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "symbol_plain")) return false;
    if (!nextTokenIs(builder_, SYM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SYM);
    exit_section_(builder_, marker_, SYMBOL, result_);
    return result_;
  }

  /* ********************************************************** */
  // symbol_plain symbol_nsq?
  static boolean symbol_qualified(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "symbol_qualified")) return false;
    if (!nextTokenIs(builder_, SYM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = symbol_plain(builder_, level_ + 1);
    result_ = result_ && symbol_qualified_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // symbol_nsq?
  private static boolean symbol_qualified_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "symbol_qualified_1")) return false;
    symbol_nsq(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' vec_body ']'
  public static boolean vec(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "vec")) return false;
    if (!nextTokenIs(builder_, BRACKET1)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VEC, null);
    result_ = consumeToken(builder_, BRACKET1);
    pinned_ = result_; // pin = '[\(\[\{]'
    result_ = result_ && report_error_(builder_, vec_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, BRACKET2) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // <<items !']' form>>
  static boolean vec_body(PsiBuilder builder_, int level_) {
    return items(builder_, level_ + 1, PhelParser::vec_body_0_0, PhelParser::form);
  }

  // !']'
  private static boolean vec_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "vec_body_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, BRACKET2);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
