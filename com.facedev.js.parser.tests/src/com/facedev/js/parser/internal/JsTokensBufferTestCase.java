package com.facedev.js.parser.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.facedev.js.parser.JsKeywords;
import com.facedev.js.parser.JsParseException;

public class JsTokensBufferTestCase {
	
	public JsTokensBuffer prepare(String input) throws IOException, JsParseException {
		return new JsTokensBuffer(new JsTokenizer(new StringReader(input)));
	}

	@Test
	public void testCreateSavePoint() throws IOException, JsParseException {
		JsTokensBuffer buf = prepare("aaa bbb ccc *");
		SavePoint save1 = buf.createSavePoint();
		assertEquals("aaa", buf.next().toString());
		assertEquals("bbb", buf.next().toString());
		SavePoint save2 = buf.createSavePoint();
		assertEquals("ccc", buf.next().toString());
		assertEquals("*", buf.next().toString());
		assertNull(buf.next());
		
		save2.rollback();
		assertEquals("ccc", buf.next().toString());
		assertEquals("*", buf.next().toString());
		assertNull(buf.next());
		
		save1.rollback();
		assertEquals("aaa", buf.next().toString());
		assertEquals("bbb", buf.next().toString());
		assertEquals("ccc", buf.next().toString());
		assertEquals("*", buf.next().toString());
		assertNull(buf.next());
	}

	@Test
	public void testNext() throws IOException, JsParseException {
		JsTokensBuffer buf = prepare("aaa bbb ccc *");
		assertEquals("aaa", buf.next().toString());
		assertEquals("bbb", buf.next().toString());
		assertEquals("ccc", buf.next().toString());
		assertEquals("*", buf.next().toString());
		assertNull(buf.next());
	}

	@Test
	public void testKeyword() throws IOException, JsParseException {
		JsTokensBuffer buf = prepare("this case thisa");
		assertTrue(buf.isKeyword(JsKeywords.KEYWORD_THIS));
		assertFalse(buf.isKeyword(JsKeywords.KEYWORD_THIS));
		assertFalse(buf.isKeyword(JsKeywords.KEYWORD_THIS));
	}

}
