package com.facedev.js.parser.internal;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import com.facedev.js.parser.JsParseException;

public class TokenizerTestCase {

	@Test
	public void testBase() throws JsParseException {
		Tokenizer tokenizer = new Tokenizer(new StringReader("a b c d"));

		assertEquals("a", tokenizer.next().toString());
		assertEquals("b", tokenizer.next().toString());
		assertEquals("c", tokenizer.next().toString());
		assertEquals("d", tokenizer.next().toString());
		assertNotNull(tokenizer.current());
		assertNull(tokenizer.next());
		assertNull(tokenizer.current());
		assertNull(tokenizer.next());
		assertNull(tokenizer.next());
		assertNull(tokenizer.current());
		
		
		assertEquals("d", tokenizer.previous().toString());
		assertEquals("c", tokenizer.previous().toString());
		assertEquals("b", tokenizer.previous().toString());
		assertEquals("a", tokenizer.previous().toString());
		assertNotNull(tokenizer.current());
		try {
			tokenizer.previous();
			fail();
		} catch(JsParseException ex) {
		}
		
		assertEquals("a", tokenizer.current().toString());
		assertEquals("b", tokenizer.next().toString());
		assertEquals("c", tokenizer.next().toString());
		
		tokenizer.commit();
		
		assertNotNull(tokenizer.current());
		
		try {
			tokenizer.previous();
			fail();
		} catch(JsParseException ex) {
		}
		assertNotNull(tokenizer.current());
		
		assertEquals("c", tokenizer.current().toString());
		assertEquals("d", tokenizer.next().toString());
		
		assertNotNull(tokenizer.current());
		assertNull(tokenizer.next());
		assertNull(tokenizer.next());
		assertNull(tokenizer.next());
		assertNull(tokenizer.current());
		
		assertEquals("d", tokenizer.previous().toString());
		assertEquals("c", tokenizer.previous().toString());
		
		assertNotNull(tokenizer.current());
		try {
			tokenizer.previous();
			fail();
		} catch(JsParseException ex) {
		}
		assertNotNull(tokenizer.current());
	}

	@Test
	public void testNames() throws JsParseException {
		Tokenizer tokenizer = new Tokenizer(new StringReader(
				" aaa bbb ccc ddd   "));

		assertEquals("aaa", tokenizer.next().toString());
		assertEquals("bbb", tokenizer.next().toString());
		assertEquals("ccc", tokenizer.next().toString());
		assertEquals("ddd", tokenizer.next().toString());
		assertNull(tokenizer.next());
	}
	@Test
	public void testOperators() throws JsParseException {
		Tokenizer tokenizer = new Tokenizer(new StringReader(
		" ++ -- +expr -expr ~!*a/%+- << >> >>> < > <= >= , ;[]{}()." +
		"instanceof == != ===   !== &^|&&||?: = += -= *= b/= %= &= ^= |= <<= >>= >>>=  "));

		assertEquals("++", tokenizer.next().toString());
		assertEquals("--", tokenizer.next().toString());
		assertEquals("+", tokenizer.next().toString());
		assertEquals("expr", tokenizer.next().toString());
		assertEquals("-", tokenizer.next().toString());
		assertEquals("expr", tokenizer.next().toString());
		assertEquals("~", tokenizer.next().toString());
		assertEquals("!", tokenizer.next().toString());
		assertEquals("*", tokenizer.next().toString());
		assertEquals("a", tokenizer.next().toString());
		assertEquals("/", tokenizer.next().toString());
		assertEquals("%", tokenizer.next().toString());
		assertEquals("+", tokenizer.next().toString());
		assertEquals("-", tokenizer.next().toString());
		assertEquals("<<", tokenizer.next().toString());
		assertEquals(">>", tokenizer.next().toString());
		assertEquals(">>>", tokenizer.next().toString());
		assertEquals("<", tokenizer.next().toString());
		assertEquals(">", tokenizer.next().toString());
		assertEquals("<=", tokenizer.next().toString());
		assertEquals(">=", tokenizer.next().toString());
		assertEquals(",", tokenizer.next().toString());
		assertEquals(";", tokenizer.next().toString());
		assertEquals("[", tokenizer.next().toString());
		assertEquals("]", tokenizer.next().toString());
		assertEquals("{", tokenizer.next().toString());
		assertEquals("}", tokenizer.next().toString());
		assertEquals("(", tokenizer.next().toString());
		assertEquals(")", tokenizer.next().toString());
		assertEquals(".", tokenizer.next().toString());
		assertEquals("instanceof", tokenizer.next().toString());
		assertEquals("==", tokenizer.next().toString());
		assertEquals("!=", tokenizer.next().toString());
		assertEquals("===", tokenizer.next().toString());
		assertEquals("!==", tokenizer.next().toString());
		assertEquals("&", tokenizer.next().toString());
		assertEquals("^", tokenizer.next().toString());
		assertEquals("|", tokenizer.next().toString());
		assertEquals("&&", tokenizer.next().toString());
		assertEquals("||", tokenizer.next().toString());
		assertEquals("?", tokenizer.next().toString());
		assertEquals(":", tokenizer.next().toString());
		assertEquals("=", tokenizer.next().toString());
		assertEquals("+=", tokenizer.next().toString());
		assertEquals("-=", tokenizer.next().toString());
		assertEquals("*=", tokenizer.next().toString());
		assertEquals("b", tokenizer.next().toString());
		assertEquals("/=", tokenizer.next().toString());
		assertEquals("%=", tokenizer.next().toString());
		assertEquals("&=", tokenizer.next().toString());
		assertEquals("^=", tokenizer.next().toString());
		assertEquals("|=", tokenizer.next().toString());
		assertEquals("<<=", tokenizer.next().toString());
		assertEquals(">>=", tokenizer.next().toString());
		assertEquals(">>>=", tokenizer.next().toString());

		assertNull(tokenizer.next());
	}

	@Test
	public void testLiterals() throws JsParseException {
		Tokenizer tokenizer = new Tokenizer(new StringReader(
				" 2aaa 141234 'a' \"asdfjlkasjdf \\\\\\\"\" * /this is pattern/gi "));

		assertEquals("2aaa", tokenizer.next().toString());
		assertEquals("141234", tokenizer.next().toString());
		assertEquals("'a'", tokenizer.next().toString());
		assertEquals("\"asdfjlkasjdf \\\\\\\"\"", tokenizer.next().toString());
		assertEquals("*", tokenizer.next().toString());
		assertEquals("/this is pattern/gi", tokenizer.next().toString());
		
		assertNull(tokenizer.next());
	}

	@Test
	public void testComments() throws JsParseException {
		Tokenizer tokenizer = new Tokenizer(new StringReader(
				" // this is comment\n" +
				"aaa /* this is other \n multi line \n comment */   bbb"));

		assertEquals("// this is comment", tokenizer.next().toString());
		assertEquals("aaa", tokenizer.next().toString());
		assertEquals(2, tokenizer.current().getLine());
		assertEquals(1, tokenizer.current().getOffset());
		assertEquals("/* this is other \n multi line \n comment */", tokenizer
				.next().toString());

		assertEquals("bbb", tokenizer.next().toString());
		assertEquals(4, tokenizer.current().getLine());
		assertEquals(15, tokenizer.current().getOffset());

		assertNull(tokenizer.next());
	}

	@Test
	public void testLineAndOffset() throws JsParseException {
		Tokenizer tokenizer = new Tokenizer(new StringReader(
				" // this is comment\n" +
				"aaa /* this is other \n multi line \n comment */   bbb\n" +
				"ccc 23\n   \n   \n   \r\n \r aa"));

		assertEquals("// this is comment", tokenizer.next().toString());
		assertEquals(1, tokenizer.current().getLine());
		assertEquals(2, tokenizer.current().getOffset());

		assertEquals("aaa", tokenizer.next().toString());
		assertEquals(2, tokenizer.current().getLine());
		assertEquals(1, tokenizer.current().getOffset());

		assertEquals("/* this is other \n multi line \n comment */",
				tokenizer.next().toString());
		assertEquals(2, tokenizer.current().getLine());
		assertEquals(5, tokenizer.current().getOffset());

		assertEquals("bbb", tokenizer.next().toString());
		assertEquals(4, tokenizer.current().getLine());
		assertEquals(15, tokenizer.current().getOffset());

		assertEquals("ccc", tokenizer.next().toString());
		assertEquals(5, tokenizer.current().getLine());
		assertEquals(1, tokenizer.current().getOffset());

		assertEquals("23", tokenizer.next().toString());
		assertEquals(5, tokenizer.current().getLine());
		assertEquals(5, tokenizer.current().getOffset());

		assertEquals("aa", tokenizer.next().toString());
		assertEquals(10, tokenizer.current().getLine());
		assertEquals(2, tokenizer.current().getOffset());
		
		assertNull(tokenizer.next());
	}
	
	@Test
	public void testUnicodeEscapeSequences() throws JsParseException {
		String seq = "\\u00" + Integer.toHexString((int)'a');
		Tokenizer tokenizer = new Tokenizer(new StringReader("  d" + seq + "t" + seq + "    "));
		
		assertEquals("data", tokenizer.next().toString());
		
		assertNull(tokenizer.next());
	}
}
