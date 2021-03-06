/* The following code was generated by JFlex 1.4.3 on 11.06.13 14:20 */

package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsKeywords;


@SuppressWarnings("unused")

/**
 * This class is a scanner generated by JFlex.
 * Provides fast DFA-based scanner for javascript source.
 */

final class JsTokenizer {

  /** This character denotes the end of file */
  private static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  private static final int DIV_ONLY = 2;
  private static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\4\1\3\2\4\1\2\22\0\1\4\1\47\1\64\1\0"+
    "\1\7\1\52\1\53\1\65\1\41\1\55\1\6\1\50\1\41\1\51"+
    "\1\43\1\5\1\57\11\60\1\41\1\41\1\44\1\46\1\45\1\41"+
    "\1\0\4\63\1\61\1\63\21\7\1\62\2\7\1\42\1\11\1\56"+
    "\1\52\1\7\1\0\1\15\1\12\1\25\1\17\1\14\1\26\1\36"+
    "\1\34\1\21\1\7\1\16\1\31\1\37\1\22\1\20\1\30\1\7"+
    "\1\13\1\23\1\24\1\35\1\33\1\32\1\40\1\27\1\7\1\41"+
    "\1\54\1\41\1\41\41\0\1\4\11\0\1\7\12\0\1\7\4\0"+
    "\1\7\5\0\27\7\1\0\37\7\1\0\u013f\7\31\0\162\7\4\0"+
    "\14\7\16\0\5\7\11\0\1\7\21\0\160\10\12\0\1\7\13\0"+
    "\1\7\1\0\3\7\1\0\1\7\1\0\24\7\1\0\54\7\1\0"+
    "\46\7\1\0\5\7\4\0\202\7\1\0\5\10\2\0\105\7\1\0"+
    "\46\7\2\0\2\7\6\0\20\7\41\0\46\7\2\0\1\7\7\0"+
    "\47\7\11\0\55\10\1\0\1\10\1\0\2\10\1\0\2\10\1\0"+
    "\1\10\10\0\33\7\5\0\3\7\35\0\13\10\6\0\32\7\5\0"+
    "\13\7\37\10\4\0\2\7\1\10\143\7\1\0\1\7\7\10\2\0"+
    "\6\10\2\7\2\10\1\0\4\10\2\7\12\10\3\7\2\0\1\7"+
    "\20\0\1\7\1\10\36\7\33\10\2\0\3\7\60\0\46\7\13\10"+
    "\1\7\16\0\12\10\41\0\11\10\42\0\4\10\1\0\11\10\1\0"+
    "\3\10\1\0\5\10\53\0\3\10\210\0\33\10\1\0\4\10\66\7"+
    "\3\10\1\7\22\10\1\7\7\10\12\7\2\10\2\0\12\10\21\0"+
    "\3\10\1\0\10\7\2\0\2\7\2\0\26\7\1\0\7\7\1\0"+
    "\1\7\3\0\4\7\2\0\1\10\1\7\7\10\2\0\2\10\2\0"+
    "\3\10\11\0\1\10\4\0\2\7\1\0\3\7\2\10\2\0\12\10"+
    "\2\7\17\0\3\10\1\0\6\7\4\0\2\7\2\0\26\7\1\0"+
    "\7\7\1\0\2\7\1\0\2\7\1\0\2\7\2\0\1\10\1\0"+
    "\5\10\4\0\2\10\2\0\3\10\3\0\1\10\7\0\4\7\1\0"+
    "\1\7\7\0\14\10\3\7\1\10\13\0\3\10\1\0\11\7\1\0"+
    "\3\7\1\0\26\7\1\0\7\7\1\0\2\7\1\0\5\7\2\0"+
    "\1\10\1\7\10\10\1\0\3\10\1\0\3\10\2\0\1\7\17\0"+
    "\2\7\2\10\2\0\12\10\21\0\3\10\1\0\10\7\2\0\2\7"+
    "\2\0\26\7\1\0\7\7\1\0\2\7\1\0\5\7\2\0\1\10"+
    "\1\7\7\10\2\0\2\10\2\0\3\10\10\0\2\10\4\0\2\7"+
    "\1\0\3\7\2\10\2\0\12\10\1\0\1\7\20\0\1\10\1\7"+
    "\1\0\6\7\3\0\3\7\1\0\4\7\3\0\2\7\1\0\1\7"+
    "\1\0\2\7\3\0\2\7\3\0\3\7\3\0\10\7\1\0\3\7"+
    "\4\0\5\10\3\0\3\10\1\0\4\10\11\0\1\10\16\0\12\10"+
    "\21\0\3\10\1\0\10\7\1\0\3\7\1\0\27\7\1\0\12\7"+
    "\1\0\5\7\4\0\7\10\1\0\3\10\1\0\4\10\7\0\2\10"+
    "\11\0\2\7\2\10\2\0\12\10\22\0\2\10\1\0\10\7\1\0"+
    "\3\7\1\0\27\7\1\0\12\7\1\0\5\7\2\0\1\10\1\7"+
    "\7\10\1\0\3\10\1\0\4\10\7\0\2\10\7\0\1\7\1\0"+
    "\2\7\2\10\2\0\12\10\22\0\2\10\1\0\10\7\1\0\3\7"+
    "\1\0\27\7\1\0\20\7\4\0\7\10\1\0\3\10\1\0\4\10"+
    "\11\0\1\10\10\0\2\7\2\10\2\0\12\10\22\0\2\10\1\0"+
    "\22\7\3\0\30\7\1\0\11\7\1\0\1\7\2\0\7\7\3\0"+
    "\1\10\4\0\6\10\1\0\1\10\1\0\10\10\22\0\2\10\15\0"+
    "\60\7\1\10\2\7\7\10\5\0\7\7\10\10\1\0\12\10\47\0"+
    "\2\7\1\0\1\7\2\0\2\7\1\0\1\7\2\0\1\7\6\0"+
    "\4\7\1\0\7\7\1\0\3\7\1\0\1\7\1\0\1\7\2\0"+
    "\2\7\1\0\4\7\1\10\2\7\6\10\1\0\2\10\1\7\2\0"+
    "\5\7\1\0\1\7\1\0\6\10\2\0\12\10\2\0\2\7\42\0"+
    "\1\7\27\0\2\10\6\0\12\10\13\0\1\10\1\0\1\10\1\0"+
    "\1\10\4\0\2\10\10\7\1\0\42\7\6\0\24\10\1\0\2\10"+
    "\4\7\1\0\13\10\1\0\44\10\11\0\1\10\71\0\42\7\1\0"+
    "\5\7\1\0\2\7\24\10\1\0\12\10\6\0\6\7\4\10\4\0"+
    "\3\10\1\0\3\10\2\0\7\10\3\0\4\10\15\0\14\10\1\0"+
    "\17\10\2\0\46\7\12\0\51\7\7\0\132\7\5\0\104\7\5\0"+
    "\122\7\6\0\7\7\1\0\77\7\1\0\1\7\1\0\4\7\2\0"+
    "\7\7\1\0\1\7\1\0\4\7\2\0\47\7\1\0\1\7\1\0"+
    "\4\7\2\0\37\7\1\0\1\7\1\0\4\7\2\0\7\7\1\0"+
    "\1\7\1\0\4\7\2\0\7\7\1\0\7\7\1\0\27\7\1\0"+
    "\37\7\1\0\1\7\1\0\4\7\2\0\7\7\1\0\47\7\1\0"+
    "\23\7\2\0\3\10\100\0\125\7\14\0\u026c\7\2\0\10\7\11\0"+
    "\1\4\32\7\5\0\113\7\25\0\15\7\1\0\4\7\3\10\13\0"+
    "\22\7\3\10\13\0\22\7\2\10\14\0\15\7\1\0\3\7\1\0"+
    "\2\10\14\0\64\7\40\10\3\0\1\7\4\0\1\7\1\10\2\0"+
    "\12\10\41\0\3\10\1\4\1\0\12\10\6\0\130\7\10\0\51\7"+
    "\1\10\126\0\35\7\3\0\14\10\4\0\14\10\12\0\12\10\36\7"+
    "\2\0\5\7\73\0\21\10\7\0\2\10\6\0\12\10\75\0\5\10"+
    "\71\0\12\10\1\0\35\10\2\0\13\10\6\0\12\10\146\0\5\10"+
    "\57\0\21\10\13\0\12\10\21\0\11\10\14\0\3\10\36\0\15\10"+
    "\2\0\12\10\54\0\16\10\60\0\24\10\10\0\12\10\6\0\12\10"+
    "\166\0\3\10\1\0\25\10\4\0\1\10\4\0\3\10\13\0\154\7"+
    "\124\0\47\10\25\0\4\10\234\7\4\0\132\7\6\0\26\7\2\0"+
    "\6\7\2\0\46\7\2\0\6\7\2\0\10\7\1\0\1\7\1\0"+
    "\1\7\1\0\1\7\1\0\37\7\2\0\65\7\1\0\7\7\1\0"+
    "\1\7\3\0\3\7\1\0\7\7\3\0\4\7\2\0\6\7\4\0"+
    "\15\7\5\0\3\7\1\0\7\7\3\0\13\4\1\0\2\10\32\0"+
    "\2\1\5\0\1\4\17\0\2\10\23\0\1\10\12\0\1\4\21\0"+
    "\1\7\15\0\1\7\120\0\15\10\4\0\1\10\3\0\14\10\21\0"+
    "\1\7\4\0\1\7\2\0\12\7\1\0\1\7\3\0\5\7\6\0"+
    "\1\7\1\0\1\7\1\0\1\7\1\0\4\7\1\0\3\7\1\0"+
    "\7\7\3\0\3\7\5\0\5\7\u0ba5\0\3\10\215\0\1\10\140\0"+
    "\40\10\u0200\0\1\4\4\0\2\7\43\0\6\10\1\0\5\7\5\0"+
    "\2\7\4\0\126\7\2\0\2\10\2\0\3\7\1\0\132\7\1\0"+
    "\4\7\5\0\50\7\4\0\136\7\21\0\30\7\70\0\20\7\u0200\0"+
    "\u19b6\7\112\0\u51a6\7\132\0\u048d\7\u0193\0\12\10\105\0\1\10\4\0"+
    "\12\10\41\0\1\10\120\0\1\10\1\10\u0110\0\1\10\3\0\1\10"+
    "\4\0\1\10\27\0\5\10\130\0\2\10\62\0\21\10\13\0\12\10"+
    "\6\0\22\10\16\0\12\10\34\0\10\10\31\0\15\10\54\0\4\10"+
    "\57\0\16\10\17\0\12\10\117\0\16\10\14\0\1\10\10\0\2\10"+
    "\2\0\12\10\41\0\1\10\64\0\1\10\1\0\3\10\2\0\2\10"+
    "\5\0\2\10\1\0\1\10\51\0\5\10\5\0\2\10\354\0\10\10"+
    "\1\0\2\10\2\0\12\10\6\0\u2ba4\7\u215c\0\u012e\7\2\0\73\7"+
    "\225\0\7\7\14\0\5\7\5\0\1\7\1\10\12\7\1\0\15\7"+
    "\1\0\5\7\1\0\1\7\1\0\2\7\1\0\2\7\1\0\154\7"+
    "\41\0\u016b\7\22\0\100\7\2\0\66\7\50\0\14\7\4\0\20\10"+
    "\20\0\7\10\14\0\2\10\30\0\3\10\40\0\5\7\1\0\207\7"+
    "\2\0\1\4\20\0\12\10\7\0\32\7\4\0\1\10\1\0\32\7"+
    "\13\0\131\7\3\0\6\7\2\0\6\7\2\0\6\7\2\0\3\7"+
    "\43\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\2\2\1\3\1\4\1\5\1\6\1\1"+
    "\17\6\11\5\1\7\2\10\2\1\1\4\1\0\1\11"+
    "\3\0\1\4\2\0\6\6\2\12\30\6\1\10\1\5"+
    "\4\0\1\13\2\0\1\4\1\14\4\0\37\6\1\15"+
    "\3\6\1\0\2\10\6\0\1\11\1\14\2\0\2\6"+
    "\1\16\10\6\1\17\2\6\1\20\12\6\5\0\17\6"+
    "\1\21\17\6";

  private static int [] zzUnpackAction() {
    int [] result = new int[204];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\66\0\154\0\154\0\242\0\330\0\u010e\0\u0144"+
    "\0\u017a\0\u01b0\0\u01e6\0\u021c\0\u0252\0\u0288\0\u02be\0\u02f4"+
    "\0\u032a\0\u0360\0\u0396\0\u03cc\0\u0402\0\u0438\0\u046e\0\u04a4"+
    "\0\u04da\0\154\0\u0510\0\u0546\0\u057c\0\u05b2\0\u05e8\0\u061e"+
    "\0\u0654\0\u068a\0\154\0\u06c0\0\u06f6\0\u072c\0\u0762\0\u0798"+
    "\0\u07ce\0\u0804\0\u083a\0\u0870\0\u08a6\0\u07ce\0\u01b0\0\u08dc"+
    "\0\u0912\0\u0948\0\u097e\0\u09b4\0\u09ea\0\u0a20\0\u017a\0\u0a56"+
    "\0\u0a8c\0\u0ac2\0\u0af8\0\u0b2e\0\u0b64\0\u0b9a\0\u0bd0\0\u0c06"+
    "\0\u0c3c\0\u0c72\0\u0ca8\0\u0cde\0\u0d14\0\u0d4a\0\u0d80\0\u0db6"+
    "\0\u0dec\0\u0e22\0\u0e58\0\u0e8e\0\u0ec4\0\u0efa\0\u0f30\0\u0f66"+
    "\0\u0f9c\0\u0fd2\0\u1008\0\u103e\0\u072c\0\u1074\0\154\0\u0762"+
    "\0\u10aa\0\154\0\u10e0\0\u1116\0\u114c\0\u1182\0\u11b8\0\u11ee"+
    "\0\u1224\0\u125a\0\u1290\0\u12c6\0\u12fc\0\u1332\0\u1368\0\u139e"+
    "\0\u13d4\0\u140a\0\u1440\0\u1476\0\u14ac\0\u14e2\0\u1518\0\u154e"+
    "\0\u1584\0\u15ba\0\u15f0\0\u1626\0\u165c\0\u1692\0\u16c8\0\u16fe"+
    "\0\u1734\0\u176a\0\u17a0\0\u17d6\0\u180c\0\u1842\0\u017a\0\u1878"+
    "\0\u18ae\0\u18e4\0\u191a\0\u191a\0\u103e\0\u1950\0\u1986\0\u19bc"+
    "\0\u19f2\0\u1a28\0\u1a5e\0\154\0\u1a94\0\u1aca\0\u1b00\0\u1b36"+
    "\0\u1b6c\0\u017a\0\u1ba2\0\u1bd8\0\u1c0e\0\u1c44\0\u1c7a\0\u1cb0"+
    "\0\u1ce6\0\u1d1c\0\u017a\0\u1d52\0\u1d88\0\u017a\0\u1dbe\0\u1df4"+
    "\0\u1e2a\0\u1e60\0\u1e96\0\u1ecc\0\u1f02\0\u1f38\0\u1f6e\0\u1fa4"+
    "\0\u1fda\0\u2010\0\u2046\0\u207c\0\u20b2\0\u20e8\0\u211e\0\u2154"+
    "\0\u218a\0\u21c0\0\u21f6\0\u222c\0\u2262\0\u2298\0\u22ce\0\u2304"+
    "\0\u233a\0\u2370\0\u23a6\0\u23dc\0\u2412\0\u2448\0\u247e\0\u24b4"+
    "\0\u24ea\0\u2520\0\u2556\0\u258c\0\u25c2\0\u25f8\0\u262e\0\u2664"+
    "\0\u269a\0\u26d0\0\u2706\0\u273c";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[204];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\4\1\6\1\7\1\10\1\11"+
    "\1\3\1\12\1\13\1\14\1\15\2\11\1\16\1\11"+
    "\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\1\30\1\31\5\11\2\32\1\33\1\34\1\35"+
    "\2\36\1\37\1\40\1\10\1\41\1\42\2\43\1\44"+
    "\1\45\3\11\1\46\1\47\1\3\1\4\1\5\1\4"+
    "\1\6\1\50\1\10\1\11\1\3\1\12\1\13\1\14"+
    "\1\15\2\11\1\16\1\11\1\17\1\20\1\21\1\22"+
    "\1\23\1\24\1\25\1\26\1\27\1\30\1\31\5\11"+
    "\2\32\1\33\1\34\1\35\2\36\1\37\1\40\1\10"+
    "\1\41\1\42\2\43\1\44\1\45\3\11\1\46\1\47"+
    "\71\0\1\4\66\0\1\6\61\0\1\51\3\0\1\51"+
    "\1\52\1\53\2\51\1\54\30\51\1\55\3\51\1\56"+
    "\17\51\46\0\1\32\26\0\2\11\1\57\27\11\16\0"+
    "\5\11\37\0\1\60\37\0\2\11\1\57\1\11\1\61"+
    "\25\11\16\0\5\11\11\0\2\11\1\57\2\11\1\62"+
    "\24\11\16\0\5\11\11\0\2\11\1\57\10\11\1\63"+
    "\6\11\1\64\6\11\1\65\16\0\5\11\11\0\2\11"+
    "\1\57\2\11\1\66\3\11\1\67\20\11\16\0\5\11"+
    "\11\0\2\11\1\57\10\11\1\70\3\11\1\67\10\11"+
    "\1\71\1\11\16\0\5\11\11\0\2\11\1\57\2\11"+
    "\1\72\20\11\1\73\3\11\16\0\5\11\11\0\2\11"+
    "\1\57\12\11\1\74\5\11\1\75\2\11\1\76\3\11"+
    "\16\0\5\11\11\0\2\11\1\57\1\11\1\77\13\11"+
    "\1\100\4\11\1\101\4\11\16\0\5\11\11\0\2\11"+
    "\1\57\3\11\1\102\2\11\1\103\10\11\1\104\7\11"+
    "\16\0\5\11\11\0\2\11\1\57\3\11\1\105\2\11"+
    "\1\106\1\107\13\11\1\110\3\11\16\0\5\11\11\0"+
    "\2\11\1\57\7\11\1\111\17\11\16\0\5\11\11\0"+
    "\2\11\1\57\1\11\1\112\1\11\1\113\17\11\1\114"+
    "\3\11\16\0\5\11\11\0\2\11\1\57\2\11\1\115"+
    "\24\11\16\0\5\11\11\0\2\11\1\57\7\11\1\116"+
    "\12\11\1\117\4\11\16\0\5\11\11\0\2\11\1\57"+
    "\3\11\1\106\2\11\1\120\20\11\16\0\5\11\61\0"+
    "\2\121\51\0\1\10\1\0\1\32\64\0\1\122\1\32"+
    "\65\0\1\10\65\0\1\32\1\0\1\32\63\0\1\32"+
    "\2\0\1\32\62\0\1\32\4\0\1\32\60\0\1\32"+
    "\5\0\1\32\25\0\1\123\23\0\1\124\2\0\1\121"+
    "\15\0\1\123\1\124\17\0\1\123\26\0\1\121\13\0"+
    "\2\45\1\123\4\0\1\125\3\0\5\125\1\126\52\125"+
    "\1\127\1\125\1\130\3\0\5\130\1\131\53\130\1\127"+
    "\5\0\1\52\1\53\37\0\1\132\17\0\1\51\3\0"+
    "\1\51\1\133\3\51\1\54\30\51\1\55\23\51\1\52"+
    "\3\0\62\52\6\53\1\134\57\53\1\51\3\0\62\51"+
    "\1\55\3\0\5\55\1\135\30\55\1\0\13\55\1\136"+
    "\7\55\12\0\1\137\1\0\2\137\1\0\1\137\5\0"+
    "\2\137\30\0\3\137\1\0\1\137\11\0\2\11\1\57"+
    "\2\11\1\140\24\11\16\0\5\11\11\0\2\11\1\57"+
    "\12\11\1\141\14\11\16\0\5\11\11\0\2\11\1\57"+
    "\23\11\1\142\3\11\16\0\5\11\11\0\2\11\1\57"+
    "\11\11\1\143\15\11\16\0\5\11\11\0\2\11\1\57"+
    "\12\11\1\144\3\11\1\145\10\11\16\0\5\11\11\0"+
    "\2\11\1\57\1\146\13\11\1\147\2\11\1\150\7\11"+
    "\16\0\5\11\11\0\2\11\1\57\11\11\1\151\1\152"+
    "\14\11\16\0\5\11\11\0\2\11\1\57\16\11\1\153"+
    "\10\11\16\0\5\11\11\0\2\11\1\57\20\11\1\67"+
    "\6\11\16\0\5\11\11\0\2\11\1\57\17\11\1\154"+
    "\7\11\16\0\5\11\11\0\2\11\1\57\3\11\1\155"+
    "\23\11\16\0\5\11\11\0\2\11\1\57\7\11\1\156"+
    "\17\11\16\0\5\11\11\0\2\11\1\57\16\11\1\157"+
    "\10\11\16\0\5\11\11\0\2\11\1\57\15\11\1\67"+
    "\5\11\1\160\3\11\16\0\5\11\11\0\2\11\1\57"+
    "\16\11\1\161\10\11\16\0\5\11\11\0\2\11\1\57"+
    "\1\11\1\162\5\11\1\163\17\11\16\0\5\11\11\0"+
    "\2\11\1\57\11\11\1\143\1\164\14\11\16\0\5\11"+
    "\11\0\2\11\1\57\10\11\1\165\16\11\16\0\5\11"+
    "\11\0\2\11\1\57\3\11\1\166\23\11\16\0\5\11"+
    "\11\0\2\11\1\57\17\11\1\167\7\11\16\0\5\11"+
    "\11\0\2\11\1\57\1\11\1\67\25\11\16\0\5\11"+
    "\11\0\2\11\1\57\10\11\1\170\16\11\16\0\5\11"+
    "\11\0\2\11\1\57\10\11\1\171\16\11\16\0\5\11"+
    "\11\0\2\11\1\57\2\11\1\172\24\11\16\0\5\11"+
    "\11\0\2\11\1\57\6\11\1\173\1\174\17\11\16\0"+
    "\5\11\11\0\2\11\1\57\13\11\1\175\13\11\16\0"+
    "\5\11\11\0\2\11\1\57\1\176\26\11\16\0\5\11"+
    "\11\0\2\11\1\57\12\11\1\177\14\11\16\0\5\11"+
    "\11\0\2\11\1\57\12\11\1\200\14\11\16\0\5\11"+
    "\11\0\2\11\1\57\7\11\1\201\17\11\16\0\5\11"+
    "\11\0\2\11\1\57\7\11\1\202\17\11\16\0\5\11"+
    "\16\0\1\123\42\0\2\121\1\123\51\0\1\10\1\32"+
    "\67\0\2\203\5\0\2\204\17\0\1\205\1\0\2\205"+
    "\1\0\1\205\5\0\2\205\30\0\3\205\1\0\1\205"+
    "\2\0\2\125\1\206\32\125\1\207\2\125\1\210\16\125"+
    "\2\0\5\125\2\130\1\211\32\130\1\212\2\130\1\213"+
    "\17\130\1\0\5\130\7\0\2\133\1\0\27\133\16\0"+
    "\5\133\2\0\5\53\1\214\60\53\1\55\3\0\62\55"+
    "\1\136\3\0\1\136\1\215\3\136\1\216\30\136\1\55"+
    "\23\136\12\0\1\217\1\0\2\217\1\0\1\217\5\0"+
    "\2\217\30\0\3\217\1\0\1\217\11\0\2\11\1\57"+
    "\3\11\1\220\23\11\16\0\5\11\11\0\2\11\1\57"+
    "\23\11\1\221\3\11\16\0\5\11\11\0\2\11\1\57"+
    "\25\11\1\222\1\11\16\0\5\11\11\0\2\11\1\57"+
    "\2\11\1\67\24\11\16\0\5\11\11\0\2\11\1\57"+
    "\2\11\1\223\24\11\16\0\5\11\11\0\2\11\1\57"+
    "\6\11\1\224\20\11\16\0\5\11\11\0\2\11\1\57"+
    "\23\11\1\225\3\11\16\0\5\11\11\0\2\11\1\57"+
    "\3\11\1\226\23\11\16\0\5\11\11\0\2\11\1\57"+
    "\2\11\1\227\24\11\16\0\5\11\11\0\2\11\1\57"+
    "\12\11\1\230\14\11\16\0\5\11\11\0\2\11\1\57"+
    "\2\11\1\231\24\11\16\0\5\11\11\0\2\11\1\57"+
    "\6\11\1\224\10\11\1\232\7\11\16\0\5\11\11\0"+
    "\2\11\1\57\17\11\1\233\7\11\16\0\5\11\11\0"+
    "\2\11\1\57\12\11\1\234\14\11\16\0\5\11\11\0"+
    "\2\11\1\57\12\11\1\164\14\11\16\0\5\11\11\0"+
    "\2\11\1\57\2\11\1\235\24\11\16\0\5\11\11\0"+
    "\2\11\1\57\2\11\1\236\24\11\16\0\5\11\11\0"+
    "\2\11\1\57\2\11\1\237\24\11\16\0\5\11\11\0"+
    "\2\11\1\57\6\11\1\72\20\11\16\0\5\11\11\0"+
    "\2\11\1\57\11\11\1\67\15\11\16\0\5\11\11\0"+
    "\2\11\1\57\13\11\1\200\13\11\16\0\5\11\11\0"+
    "\2\11\1\57\11\11\1\240\1\241\14\11\16\0\5\11"+
    "\11\0\2\11\1\57\11\11\1\242\15\11\16\0\5\11"+
    "\11\0\2\11\1\57\11\11\1\160\15\11\16\0\5\11"+
    "\11\0\2\11\1\57\3\11\1\243\23\11\16\0\5\11"+
    "\11\0\2\11\1\57\13\11\1\244\13\11\16\0\5\11"+
    "\11\0\2\11\1\57\17\11\1\245\7\11\16\0\5\11"+
    "\11\0\2\11\1\57\12\11\1\246\14\11\16\0\5\11"+
    "\11\0\2\11\1\57\21\11\1\247\5\11\16\0\5\11"+
    "\11\0\2\11\1\57\4\11\1\250\22\11\16\0\5\11"+
    "\11\0\2\11\1\57\17\11\1\234\7\11\16\0\5\11"+
    "\11\0\2\11\1\57\22\11\1\67\4\11\16\0\5\11"+
    "\11\0\2\11\1\57\17\11\1\143\7\11\16\0\5\11"+
    "\11\0\2\11\1\57\5\11\1\67\21\11\16\0\5\11"+
    "\61\0\2\204\5\0\1\125\2\0\6\125\1\126\52\125"+
    "\1\127\1\125\12\0\1\251\1\0\2\251\1\0\1\251"+
    "\5\0\2\251\30\0\3\251\1\0\1\251\14\0\1\252"+
    "\1\0\2\252\1\0\1\252\5\0\2\252\30\0\3\252"+
    "\1\0\1\252\2\0\1\130\2\0\6\130\1\131\53\130"+
    "\1\127\12\0\1\253\1\0\2\253\1\0\1\253\5\0"+
    "\2\253\30\0\3\253\1\0\1\253\14\0\1\254\1\0"+
    "\2\254\1\0\1\254\5\0\2\254\30\0\3\254\1\0"+
    "\1\254\2\0\1\55\3\0\3\55\2\215\1\135\27\215"+
    "\1\55\1\0\13\55\1\136\5\215\2\55\1\136\3\0"+
    "\62\136\12\0\1\255\1\0\2\255\1\0\1\255\5\0"+
    "\2\255\30\0\3\255\1\0\1\255\11\0\2\11\1\57"+
    "\4\11\1\67\22\11\16\0\5\11\11\0\2\11\1\57"+
    "\1\11\1\256\25\11\16\0\5\11\11\0\2\11\1\57"+
    "\10\11\1\257\16\11\16\0\5\11\11\0\2\11\1\57"+
    "\1\11\1\240\25\11\16\0\5\11\11\0\2\11\1\57"+
    "\24\11\1\260\2\11\16\0\5\11\11\0\2\11\1\57"+
    "\23\11\1\261\3\11\16\0\5\11\11\0\2\11\1\57"+
    "\12\11\1\143\14\11\16\0\5\11\11\0\2\11\1\57"+
    "\3\11\1\262\23\11\16\0\5\11\11\0\2\11\1\57"+
    "\1\11\1\263\25\11\16\0\5\11\11\0\2\11\1\57"+
    "\2\11\1\264\24\11\16\0\5\11\11\0\2\11\1\57"+
    "\7\11\1\265\17\11\16\0\5\11\11\0\2\11\1\57"+
    "\1\11\1\222\25\11\16\0\5\11\11\0\2\11\1\57"+
    "\6\11\1\266\20\11\16\0\5\11\11\0\2\11\1\57"+
    "\12\11\1\222\14\11\16\0\5\11\11\0\2\11\1\57"+
    "\7\11\1\267\17\11\16\0\5\11\11\0\2\11\1\57"+
    "\11\11\1\222\15\11\16\0\5\11\11\0\2\11\1\57"+
    "\17\11\1\270\7\11\16\0\5\11\11\0\2\11\1\57"+
    "\12\11\1\271\14\11\16\0\5\11\11\0\2\11\1\57"+
    "\5\11\1\177\21\11\16\0\5\11\11\0\2\11\1\57"+
    "\2\11\1\272\24\11\16\0\5\11\11\0\2\11\1\57"+
    "\3\11\1\273\23\11\16\0\5\11\11\0\2\11\1\57"+
    "\3\11\1\274\23\11\16\0\5\11\14\0\1\210\1\0"+
    "\2\210\1\0\1\210\5\0\2\210\30\0\3\210\1\0"+
    "\1\210\14\0\1\125\1\0\2\125\1\0\1\125\5\0"+
    "\2\125\30\0\3\125\1\0\1\125\14\0\1\213\1\0"+
    "\2\213\1\0\1\213\5\0\2\213\30\0\3\213\1\0"+
    "\1\213\14\0\1\130\1\0\2\130\1\0\1\130\5\0"+
    "\2\130\30\0\3\130\1\0\1\130\14\0\1\275\1\0"+
    "\2\275\1\0\1\275\5\0\2\275\30\0\3\275\1\0"+
    "\1\275\11\0\2\11\1\57\10\11\1\67\16\11\16\0"+
    "\5\11\11\0\2\11\1\57\5\11\1\242\21\11\16\0"+
    "\5\11\11\0\2\11\1\57\24\11\1\276\2\11\16\0"+
    "\5\11\11\0\2\11\1\57\17\11\1\277\7\11\16\0"+
    "\5\11\11\0\2\11\1\57\10\11\1\300\16\11\16\0"+
    "\5\11\11\0\2\11\1\57\14\11\1\301\12\11\16\0"+
    "\5\11\11\0\2\11\1\57\25\11\1\302\1\11\16\0"+
    "\5\11\11\0\2\11\1\57\13\11\1\177\13\11\16\0"+
    "\5\11\11\0\2\11\1\57\14\11\1\67\12\11\16\0"+
    "\5\11\11\0\2\11\1\57\10\11\1\303\16\11\16\0"+
    "\5\11\11\0\2\11\1\57\17\11\1\304\7\11\16\0"+
    "\5\11\11\0\2\11\1\57\7\11\1\305\17\11\16\0"+
    "\5\11\11\0\2\11\1\57\13\11\1\306\13\11\16\0"+
    "\5\11\11\0\2\11\1\57\12\11\1\307\14\11\16\0"+
    "\5\11\11\0\2\11\1\57\24\11\1\307\2\11\16\0"+
    "\5\11\11\0\2\275\1\57\27\275\16\0\5\275\11\0"+
    "\2\11\1\57\2\11\1\106\24\11\16\0\5\11\11\0"+
    "\2\11\1\57\12\11\1\67\14\11\16\0\5\11\11\0"+
    "\2\11\1\57\13\11\1\161\13\11\16\0\5\11\11\0"+
    "\2\11\1\57\3\11\1\310\23\11\16\0\5\11\11\0"+
    "\2\11\1\57\2\11\1\311\24\11\16\0\5\11\11\0"+
    "\2\11\1\57\23\11\1\143\3\11\16\0\5\11\11\0"+
    "\2\11\1\57\15\11\1\67\11\11\16\0\5\11\11\0"+
    "\2\11\1\57\6\11\1\256\20\11\16\0\5\11\11\0"+
    "\2\11\1\57\12\11\1\312\14\11\16\0\5\11\11\0"+
    "\2\11\1\57\2\11\1\177\24\11\16\0\5\11\11\0"+
    "\2\11\1\57\13\11\1\307\13\11\16\0\5\11\11\0"+
    "\2\11\1\57\10\11\1\313\16\11\16\0\5\11\11\0"+
    "\2\11\1\57\2\11\1\245\24\11\16\0\5\11\11\0"+
    "\2\11\1\57\12\11\1\314\14\11\16\0\5\11\11\0"+
    "\2\11\1\57\11\11\1\177\15\11\16\0\5\11\2\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[10098];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\2\11\25\1\1\11\10\1\1\11\5\1\1\0"+
    "\1\1\3\0\1\1\2\0\42\1\4\0\1\11\2\0"+
    "\1\11\1\1\4\0\43\1\1\0\2\1\6\0\1\11"+
    "\1\1\2\0\31\1\5\0\37\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[204];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
  JsFlexToken next() throws java.io.IOException, JsParseException {
    return nextToken();
  }
  
  /**
   * Replaces unicode escape sequences (\\uxxxx) in the text provided to real characters.
   */
  private String unescape(String text) {
    int length = text.length();
    int escEnd = length - 5;
    StringBuilder result = new StringBuilder(escEnd+1);
    for (int i = 0; i < length; i++) {
      char c = text.charAt(i);
      if (c != '\\' || (i >= escEnd)) {
        result.append(c);
        continue;
      }
      if (text.charAt(++i) != 'u') {
        result.append(text.charAt(i));
        continue;
      }
      i++;
      int start = i;
      i += 3;
      result.appendCodePoint(Integer.parseInt(text.substring(start, i+1), 16));
    }
    return result.toString();
  }


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  JsTokenizer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  JsTokenizer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2022) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  private final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  private final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  private final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  private final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  private final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  private final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  private final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) throws JsParseException {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new JsParseException(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  private void yypushback(int number)  throws JsParseException {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private JsFlexToken nextToken() throws java.io.IOException, JsParseException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 10: 
          { yybegin(YYINITIAL);
                                  return new JsFlexToken(yytext().intern(), 
                                          JsFlexToken.RESERVED | JsFlexToken.RESERVED_KEYWORD, yyline, yycolumn);
          }
        case 18: break;
        case 1: 
          { /*Error fallback: matches any input*/
  yybegin(YYINITIAL);
  return new JsFlexToken(yytext(), JsFlexToken.ERROR, yyline, yycolumn);
          }
        case 19: break;
        case 7: 
          { yybegin(DIV_ONLY);
                                  return new JsFlexToken(yytext().intern(), JsFlexToken.PUNKTUATOR | 
                                          JsFlexToken.ANY_PUNKTUATOR, yyline, yycolumn);
          }
        case 20: break;
        case 13: 
          { yybegin(YYINITIAL);
                                  return new JsFlexToken(yytext().intern(), 
                                          JsFlexToken.RESERVED | JsFlexToken.FUTURE_RESERVED_STRICT, yyline, yycolumn);
          }
        case 21: break;
        case 5: 
          { yybegin(YYINITIAL);
                                  return new JsFlexToken(yytext().intern(), JsFlexToken.PUNKTUATOR | 
                                          JsFlexToken.ANY_PUNKTUATOR, yyline, yycolumn);
          }
        case 22: break;
        case 17: 
          { yybegin(DIV_ONLY);
                                  return new JsFlexToken(unescape(yytext()), JsFlexToken.IDENTIFIER, yyline, yycolumn);
          }
        case 23: break;
        case 16: 
          { yybegin(DIV_ONLY);
                                  return new JsFlexToken(yytext().intern(), 
                                          JsFlexToken.RESERVED | JsFlexToken.RESERVED_KEYWORD |
                                          JsFlexToken.LITERAL | JsFlexToken.BOOLEAN_LITERAL, yyline, yycolumn);
          }
        case 24: break;
        case 15: 
          { yybegin(DIV_ONLY);
                                  return new JsFlexToken(yytext().intern(), JsFlexToken.RESERVED | 
                                          JsFlexToken.RESERVED_KEYWORD | JsFlexToken.LITERAL | 
                                          JsFlexToken.NULL_LITERAL, yyline, yycolumn);
          }
        case 25: break;
        case 14: 
          { yybegin(YYINITIAL);
                                  return new JsFlexToken(yytext().intern(), 
                                          JsFlexToken.RESERVED | JsFlexToken.FUTURE_RESERVED, yyline, yycolumn);
          }
        case 26: break;
        case 11: 
          { yybegin(DIV_ONLY);
                                  return new JsFlexToken(yytext(), JsFlexToken.LITERAL | 
                                          JsFlexToken.STRING_LITERAL, yyline, yycolumn);
          }
        case 27: break;
        case 9: 
          { return new JsFlexToken(yytext(), JsFlexToken.COMMENT | JsFlexToken.IGNORED, yyline, yycolumn);
          }
        case 28: break;
        case 6: 
          { yybegin(DIV_ONLY);
                                  return new JsFlexToken(yytext(), JsFlexToken.IDENTIFIER, yyline, yycolumn);
          }
        case 29: break;
        case 3: 
          { return new JsFlexToken(JsFlexToken.TOKEN_WHITE_SPACE, JsFlexToken.WHITE_SPACE |  
                                          JsFlexToken.IGNORED, yyline, yycolumn);
          }
        case 30: break;
        case 12: 
          { yybegin(DIV_ONLY);
                                  return new JsFlexToken(yytext(), JsFlexToken.LITERAL | 
                                          JsFlexToken.REGEXP_LITERAL, yyline, yycolumn);
          }
        case 31: break;
        case 8: 
          { yybegin(DIV_ONLY);
                                  return new JsFlexToken(yytext(), JsFlexToken.LITERAL | 
                                          JsFlexToken.NUMERIC_LITERAL, yyline, yycolumn);
          }
        case 32: break;
        case 2: 
          { return new JsFlexToken(JsFlexToken.TOKEN_LINE_TERMINATOR, JsFlexToken.LINE_TERMINATOR | 
                                          JsFlexToken.IGNORED, yyline, yycolumn);
          }
        case 33: break;
        case 4: 
          { yybegin(YYINITIAL);
                                  return new JsFlexToken(yytext().intern(), JsFlexToken.DIV_PUNKTUATOR | 
                                          JsFlexToken.ANY_PUNKTUATOR, yyline, yycolumn);
          }
        case 34: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            return null;
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
