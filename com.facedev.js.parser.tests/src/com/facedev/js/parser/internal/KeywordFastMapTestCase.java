package com.facedev.js.parser.internal;

import static junit.framework.Assert.*;

import org.junit.Test;

import com.facedev.js.parser.JsKeywords;

public class KeywordFastMapTestCase {

	@Test
	public void testGet() {
		KeywordsFastMap map = new KeywordsFastMap();
		
		assertSame(JsKeywords.KEYWORD_ABSTRACT, map.get(new String("abstract")));
		assertSame(JsKeywords.KEYWORD_WHILE, map.get(new StringBuilder("while")));
				
		assertNull(map.get("abstrac"));
		assertNull(map.get("abstractt"));
		assertNull(map.get("apstract"));
	}
}
