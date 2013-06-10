package com.facedev.js.parser.internal;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;


public class JsAstParserTestCase {
	
	private static boolean isCritical = false;
	
	private static JsParseLogger getLogger(){
		isCritical = false;
		return new JsParseLogger() {

			public void log(Message message, Token... tokens) throws JsParseException {
				if (message.isCritical()) {
					isCritical = true;
				}
			}
			
		};
	}

	@Test
	public void testSimple() throws IOException, JsParseException {
		assertTrue(parseString("var x = 0;", getLogger()));
		assertFalse(isCritical);
		assertFalse(parseString("var x = ;", getLogger()));
		assertTrue(isCritical);
	}
	
	@Test
	public void testFn() throws IOException, JsParseException {
		assertTrue(parseString("jQuery = function( selector, context ) {}", getLogger()));
		assertFalse(isCritical);
	}
	
	@Test
	public void testJQueryVar() throws IOException, JsParseException {
		assertTrue(parse("com/facedev/js/parser/internal/resources/jquery_var.js", getLogger()));
		assertFalse(isCritical);
	}
	
	@Test
	public void testJQuery() throws IOException, JsParseException {
		assertTrue(parse("com/facedev/js/parser/internal/resources/jquery-2.0.2.js", getLogger()));
		assertFalse(isCritical);
	}
	
	@Test
	public void testFnJqueryEq () throws IOException, JsParseException {
		assertTrue(parseString("{ eq : function( i ) { \n" +
		"var len = this.length,\n" +
			"j = +i + ( i < 0 ? len : 0 );\n" +
			"return this.pushStack( j >= 0 && j < len ? [ this[j] ] : [] );\n" +
		"}}", getLogger()));
		assertFalse(isCritical);
	}
	
	@Test
	public void testTernary () throws IOException, JsParseException {
		assertTrue(parseString("var eq = +i + ( i < 0 ? len : 0 );", getLogger()));
		assertFalse(isCritical);
	}
	
	@Test
	public void testReturn() throws IOException, JsParseException {
		assertTrue(parseString("return this.pushStack( j >= 0 && j < len ? [ this[j] ] : [] );\n", getLogger()));
		assertFalse(isCritical);
	}

	private boolean parseString(String content, JsParseLogger logger) throws IOException, JsParseException {
		return parse(new StringReader(content), logger);
	}
	
	private boolean parse(String file, JsParseLogger logger) throws IOException, JsParseException {
		InputStream data = null;
		try {
			data = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
			if (data == null) {
				throw new IOException("Unable to load file: " + file);
			}
			return parse(new InputStreamReader(data), logger);
		} finally {
			if (data != null) data.close();
		}
	}
	
	private boolean parse(Reader data, JsParseLogger logger) throws IOException, JsParseException {
		JsTokenizer tokenizer = new JsTokenizer(data);
		JsAstParser parser = new JsAstParser(tokenizer, logger);
		return parser.parse() != null;
	}
}
