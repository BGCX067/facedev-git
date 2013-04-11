package com.facedev.js.parser.internal;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.facedev.js.parser.JsKeywords;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.Token;

public class TokenizerTestCase {

	@Test
	public void testBase() throws JsParseException, IOException {
		JsTokenizer tokenizer = new JsTokenizer(new StringReader("a b c d"));

		assertEquals("a", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("b", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("c", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("d", tokenizer.next().toString());
		assertNull(tokenizer.next());
	}

	@Test
	public void testNames() throws JsParseException, IOException {
		JsTokenizer tokenizer = new JsTokenizer(new StringReader(
				" aaa bbb ccc ddd   "));

		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("aaa", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("bbb", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("ccc", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("ddd", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertNull(tokenizer.next());
	}
	@Test
	public void testOperators() throws JsParseException, IOException {
		JsTokenizer tokenizer = new JsTokenizer(new StringReader(
		" ++ -- +expr -expr ~!*a/%+- << >> >>> < > <= >= , ;[]{}()." +
		"instanceof == != ===   !== &^|&&||?: = += -= *= b/= %= &= ^= |= <<= >>= >>>=  "));

		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("++", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("--", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("+", tokenizer.next().toString());
		assertEquals("expr", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("-", tokenizer.next().toString());
		assertEquals("expr", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("~", tokenizer.next().toString());
		assertEquals("!", tokenizer.next().toString());
		assertEquals("*", tokenizer.next().toString());
		assertEquals("a", tokenizer.next().toString());
		assertEquals("/", tokenizer.next().toString());
		assertEquals("%", tokenizer.next().toString());
		assertEquals("+", tokenizer.next().toString());
		assertEquals("-", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("<<", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(">>", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(">>>", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("<", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(">", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("<=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(">=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(",", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(";", tokenizer.next().toString());
		assertEquals("[", tokenizer.next().toString());
		assertEquals("]", tokenizer.next().toString());
		assertEquals("{", tokenizer.next().toString());
		assertEquals("}", tokenizer.next().toString());
		assertEquals("(", tokenizer.next().toString());
		assertEquals(")", tokenizer.next().toString());
		assertEquals(".", tokenizer.next().toString());
		assertEquals("instanceof", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("==", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("!=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("===", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("!==", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("&", tokenizer.next().toString());
		assertEquals("^", tokenizer.next().toString());
		assertEquals("|", tokenizer.next().toString());
		assertEquals("&&", tokenizer.next().toString());
		assertEquals("||", tokenizer.next().toString());
		assertEquals("?", tokenizer.next().toString());
		assertEquals(":", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("+=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("-=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("*=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("b", tokenizer.next().toString());
		assertEquals("/=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("%=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("&=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("^=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("|=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("<<=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(">>=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(">>>=", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());

		assertNull(tokenizer.next());
	}

	@Test
	public void testLiterals() throws JsParseException, IOException {
		JsTokenizer tokenizer = new JsTokenizer(new StringReader(
				" .23 2aaa 141234 'a' \"asdfjlkasjdf \\\\\\\"\" * /this is pattern/gi " +
				"0x023.method   "));

		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals(".23", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("2", tokenizer.next().toString());
		assertEquals("aaa", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("141234", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("'a'", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("\"asdfjlkasjdf \\\\\\\"\"", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("*", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("/this is pattern/gi", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("0x023", tokenizer.next().toString());
		assertEquals(".", tokenizer.next().toString());
		assertEquals("method", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		
		assertNull(tokenizer.next());
	}

	@Test
	public void testComments() throws JsParseException, IOException {
		JsTokenizer tokenizer = new JsTokenizer(new StringReader(
				" // this is comment\n" +
				"aaa /* this is other \n multi line \n comment */   bbb"));

		assertTrue(tokenizer.next().isWhiteSpace());
		Token tok = tokenizer.next();
		assertTrue(tok.isComment());
		assertEquals("// this is comment", tok.toString());
		assertTrue(tokenizer.next().isLineTerminator());
		
		tok = tokenizer.next();
		assertEquals("aaa", tok.toString());
		assertEquals(2, tok.getLine());
		assertEquals(1, tok.getOffset());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		
		assertEquals("/* this is other \n multi line \n comment */", tokenizer
				.next().toString());
		
		assertTrue(tokenizer.next().isWhiteSpace());

		tok = tokenizer.next();
		assertEquals("bbb", tok.toString());
		assertEquals(4, tok.getLine());
		assertEquals(15, tok.getOffset());

		assertNull(tokenizer.next());
	}

	@Test
	public void testLineAndOffset() throws JsParseException, IOException {
		JsTokenizer tokenizer = new JsTokenizer(new StringReader(
				" // this is comment\n" +
				"aaa /* this is other \n multi line \n comment */   bbb\n" +
				"ccc 23\n   \n   \n   \r\n \r aa"));

		assertTrue(tokenizer.next().isWhiteSpace());
		Token tok = tokenizer.next();
		assertEquals("// this is comment", tok.toString());
		assertEquals(1, tok.getLine());
		assertEquals(2, tok.getOffset());
		
		tok = tokenizer.next();
		assertTrue(tok.isLineTerminator());
		assertEquals(1, tok.getLine());
		assertEquals(20, tok.getOffset());

		tok = tokenizer.next();
		assertEquals("aaa", tok.toString());
		assertEquals(2, tok.getLine());
		assertEquals(1, tok.getOffset());

		assertTrue(tokenizer.next().isWhiteSpace());
		
		tok = tokenizer.next();
		assertEquals("/* this is other \n multi line \n comment */",
				tok.toString());
		assertEquals(2, tok.getLine());
		assertEquals(5, tok.getOffset());
		
		assertTrue(tokenizer.next().isWhiteSpace());

		tok = tokenizer.next();
		assertEquals("bbb", tok.toString());
		assertEquals(4, tok.getLine());
		assertEquals(15, tok.getOffset());

		tok = tokenizer.next();
		assertTrue(tok.isLineTerminator());
		assertEquals(4, tok.getLine());
		assertEquals(18, tok.getOffset());

		tok = tokenizer.next();
		assertEquals("ccc", tok.toString());
		assertEquals(5, tok.getLine());
		assertEquals(1, tok.getOffset());
		
		assertTrue(tokenizer.next().isWhiteSpace());

		tok = tokenizer.next();
		assertEquals("23", tok.toString());
		assertEquals(5, tok.getLine());
		assertEquals(5, tok.getOffset());
		
		assertTrue(tokenizer.next().isLineTerminator());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isLineTerminator());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isLineTerminator());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isLineTerminator());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isLineTerminator());
		assertTrue(tokenizer.next().isWhiteSpace());

		tok = tokenizer.next();
		assertEquals("aa", tok.toString());
		assertEquals(10, tok.getLine());
		assertEquals(2, tok.getOffset());
		
		assertNull(tokenizer.next());
	}
	
	@Test
	public void testUnicodeEscapeSequences() throws JsParseException, IOException {
		String seq = "\\u00" + Integer.toHexString((int)'a');
		JsTokenizer tokenizer = new JsTokenizer(new StringReader("  d" + seq + "t" + seq + "    "));
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertEquals("data", tokenizer.next().toString());
		assertTrue(tokenizer.next().isWhiteSpace());
		
		assertNull(tokenizer.next());
	}
	
	@Test
	public void testClassification() throws JsParseException, IOException {
		JsTokenizer tokenizer = new JsTokenizer(new StringReader(
				"   /**/ // aa \n 25 .25e3 aaa case ; \n \r /regex/ig" +
				" / 'case' \"if\"     "));
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isComment());
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isComment());
		
		assertTrue(tokenizer.next().isLineTerminator());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isNumberLiteral());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isNumberLiteral());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isIdentifier());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		
		Token tok = tokenizer.next();
		assertFalse(tok.isIdentifier());
		assertTrue(tok.isReserved());
		assertTrue(tok.isKeyword());
		assertTrue(tok.isKeyword(JsKeywords.KEYWORD_CASE));
		
		assertTrue(tokenizer.next().isWhiteSpace());
		
		assertTrue(tokenizer.next().isPunctuator());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isLineTerminator());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isLineTerminator());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isRegexpLiteral());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertFalse(tokenizer.next().isRegexpLiteral());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isStringLiteral());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		assertTrue(tokenizer.next().isStringLiteral());
		
		assertTrue(tokenizer.next().isWhiteSpace());
		
		assertNull(tokenizer.next());
	}
}
