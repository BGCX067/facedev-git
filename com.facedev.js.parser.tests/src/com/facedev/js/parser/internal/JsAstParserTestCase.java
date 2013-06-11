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
		assertTrue(parseString("var i = { eq : function( i ) { \n" +
		"var len = this.length,\n" +
			"j = +i + ( i < 0 ? len : 0 );\n" +
			"return this.pushStack( j >= 0 && j < len ? [ this[j] ] : [] );\n" +
		"}, next: 12}", getLogger()));
		assertFalse(isCritical);
	}
	
	@Test
	public void testTryCatch() throws IOException, JsParseException {
		assertTrue(parseString("try {\n" +
			"	core_hasOwn.call();\n" +
			"} catch ( e ) {\n" +
			"	return false;\n" +
			"}\n", getLogger()));
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
	
	@Test
	public void testOr() throws IOException, JsParseException {
		assertTrue(parseString("var slice = 2,\n" +
			"// Use a stripped-down indexOf if we can't use a native one\n" +
			"indexOf = arr.indexOf || function( elem ) {\n" +
			"var i = 0,\n" +
			"len = this.length;\n" +
			"for ( ; i < len; i++ ) {\n" +
			"if ( this[i] === elem ) {\n" +
			"return i;\n" +
			"}\n" +
			"}\n" +
			"return -1;\n" +
			"},\n" +
			"booleans = 4;", getLogger()));
		assertFalse(isCritical);
	}
	
	@Test
	public void testSizzle() throws IOException, JsParseException {
		assertTrue(parse("com/facedev/js/parser/internal/resources/sizzle.js", getLogger()));
		assertFalse(isCritical);
	}
	
	@Test
	public void testSizzleGetText() throws IOException, JsParseException {
		assertTrue(parse("com/facedev/js/parser/internal/resources/sizzle_get_text.js", getLogger()));
		assertFalse(isCritical);
	}

	@Test
	public void testSelfCallingFnTryCatch () throws IOException, JsParseException {
		assertTrue(parseString("(function( window, undefined ) { \n" + 
				"try {\n" + 
				"var i = 0;\n" +
				"} catch (e) { \n" +
				"}\n" +
				"})( window );", getLogger()));
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
